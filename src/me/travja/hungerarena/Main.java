package me.Travja.HungerArena;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	static final Logger log = Logger.getLogger("Minecraft");
	public ArrayList<String> Playing = new ArrayList<String>();
	public ArrayList<String> Ready = new ArrayList<String>();
	public ArrayList<String> Dead = new ArrayList<String>();
	public ArrayList<String> Quit = new ArrayList<String>();
	public ArrayList<String> Out = new ArrayList<String>();
	public ArrayList<String> Watching = new ArrayList<String>();
	public ArrayList<String> NeedConfirm = new ArrayList<String>();
	public HashSet<String> Frozen = new HashSet<String>();
	public List<String> worlds;
	public Listener DeathListener = new DeathListener(this);
	public Listener SpectatorListener = new SpectatorListener(this);
	public Listener FreezeListener = new FreezeListener(this);
	public Listener JoinAndQuitListener = new JoinAndQuitListener(this);
	public Listener ChatListener = new ChatListener(this);
	public Listener Chests = new Chests(this);
	public Listener PvP = new PvP(this);
	public Listener Blocks = new Blocks(this);
	public Listener CommandBlock = new CommandBlock(this);
	public Listener Damage = new DmgListener(this);
	public Listener Teleport = new TeleportListener(this);
	public Listener Signs = new Signs(this);
	public Listener BlockStorage = new BlockStorage(this);
	public Listener WinGames = new WinGamesListener(this);
	public CommandExecutor HaCommands = new HaCommands(this);
	public CommandExecutor SponsorCommands = new SponsorCommands(this);
	public CommandExecutor SpawnsCommand = new SpawnsCommand(this);
	public boolean canjoin;
	public boolean exists;
	public boolean restricted;
	public FileConfiguration config;
	public ItemStack Reward;
	public ItemStack Cost;
	public boolean vault = false;
	public Economy econ = null;
	public void onEnable(){
		config = this.getConfig();
		config.options().copyDefaults(true);
		this.saveDefaultConfig();
		log.info("[HungerArena] enabled v" + getDescription().getVersion());
		getServer().getPluginManager().registerEvents(DeathListener, this);
		getServer().getPluginManager().registerEvents(SpectatorListener, this);
		getServer().getPluginManager().registerEvents(FreezeListener, this);
		getServer().getPluginManager().registerEvents(JoinAndQuitListener, this);
		getServer().getPluginManager().registerEvents(ChatListener, this);
		getServer().getPluginManager().registerEvents(Chests, this);
		getServer().getPluginManager().registerEvents(PvP, this);
		getServer().getPluginManager().registerEvents(Blocks, this);
		getServer().getPluginManager().registerEvents(CommandBlock, this);
		getServer().getPluginManager().registerEvents(Signs, this);
		getServer().getPluginManager().registerEvents(BlockStorage, this);
		getServer().getPluginManager().registerEvents(WinGames, this);
		getServer().getPluginManager().registerEvents(Damage, this);
		getCommand("Ha").setExecutor(HaCommands);
		getCommand("Sponsor").setExecutor(SponsorCommands);
		getCommand("Startpoint").setExecutor(SpawnsCommand);
		if (setupEconomy()) {
			log.info(ChatColor.AQUA + "[HungerArena] Found Vault! Hooking in for economy!");
		}
		if (config.getDouble("config.version") != 1.3) {
			config.set("config.version", 1.3);
			config.set("eco.enabled", false);
			config.set("eco.reward", 100);
		}
		if (config.getBoolean("eco.enabled", true)) {
			if (vault == true) {
				log.info(ChatColor.AQUA + "Economy hook deployed.");
			} else {
				log.info(ChatColor.RED + "You want economy support... yet you don't have Vault. Sorry, can't give you it.");
			}
		}
		if (config.getBoolean("eco.enabled", false)) {
			if (vault == true) {
				log.info(ChatColor.GREEN + "We see that you have Vault on your server. To set economy support to true, enable it in the config.");
			}
		}
		Reward = new ItemStack(config.getInt("Reward.ID"), config.getInt("Reward.Amount"));
		Cost = new ItemStack(config.getInt("Sponsor_Cost.ID"), config.getInt("Sponsor_Cost.Amount"));
		worlds = config.getStringList("worlds");
		if(worlds.isEmpty()){
			restricted = false;
		}else if(!worlds.isEmpty()){
			restricted = true;
		}
	}

	public void onDisable(){
		log.info("[HungerArena] disabled v" + getDescription().getVersion());
	}

	public boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		vault = true;
		return econ != null;
	}
}
