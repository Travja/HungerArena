package me.Travja.HungerArena;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	Logger log;
	public ArrayList<String> Playing = new ArrayList<String>();
	public ArrayList<String> Ready = new ArrayList<String>();
	public ArrayList<String> Dead = new ArrayList<String>();
	public ArrayList<String> Quit = new ArrayList<String>();
	public ArrayList<String> Out = new ArrayList<String>();
	public ArrayList<String> Watching = new ArrayList<String>();
	public ArrayList<String> NeedConfirm = new ArrayList<String>();
	public HashSet<String> Frozen = new HashSet<String>();
	public Listener DeathListener = new DeathListener(this);
	public Listener SpectatorListener = new SpectatorListener(this);
	public Listener FreezeListener = new FreezeListener(this);
	public Listener JoinAndQuitListener = new JoinAndQuitListener(this);
	public Listener ChatListener = new ChatListener(this);
	public Listener Chests = new Chests(this);
	public Listener PvP = new PvP(this);
	public Listener Blocks = new Blocks(this);
	public Listener CommandBlock = new CommandBlock(this);
	public Listener Signs = new Signs(this);
	public Listener BlockStorage = new BlockStorage(this);
	public CommandExecutor HaCommands = new HaCommands(this);
	public CommandExecutor SponsorCommands = new SponsorCommands(this);
	public CommandExecutor SpawnsCommand = new SpawnsCommand(this);
	public boolean canjoin;
	public boolean exists;
	public FileConfiguration config;
	public ItemStack Reward;
	public ItemStack Cost;
	public void onEnable(){
		log = this.getLogger();
		log.info("HungerArena has been Enabled");
		config = getConfig();
		config.options().copyDefaults(true);
		this.saveDefaultConfig();
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
		getCommand("Ha").setExecutor(HaCommands);
		getCommand("Sponsor").setExecutor(SponsorCommands);
		getCommand("Startpoint").setExecutor(SpawnsCommand);
		Reward = new ItemStack(config.getInt("Reward.ID"), config.getInt("Reward.Amount"));
		Cost = new ItemStack(config.getInt("Sponsor_Cost.ID"), config.getInt("Sponsor_Cost.Amount"));
	}
	public void onDisable(){
		log = this.getLogger();
		log.info("HungerArena has been Disabled");
	}
}
