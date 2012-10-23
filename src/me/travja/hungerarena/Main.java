package me.Travja.HungerArena;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin{
	static final Logger log = Logger.getLogger("Minecraft");
	public ArrayList<String> Playing = new ArrayList<String>();
	public ArrayList<String> Ready = new ArrayList<String>();
	public ArrayList<String> Dead = new ArrayList<String>();
	public ArrayList<String> Quit = new ArrayList<String>();
	public ArrayList<String> Out = new ArrayList<String>();
	public ArrayList<String> Watching = new ArrayList<String>();
	public ArrayList<String> NeedConfirm = new ArrayList<String>();
	public ArrayList<Location> location = new ArrayList<Location>();
	public ArrayList<Player> Tele = new ArrayList<Player>();
	public HashSet<String> Frozen = new HashSet<String>();
	public List<String> worlds;
	public Listener DeathListener = new DeathListener(this);
	public Listener SpectatorListener = new SpectatorListener(this);
	public Listener FreezeListener = new FreezeListener(this);
	public Listener JoinAndQuitListener = new JoinAndQuitListener(this);
	public Listener ChatListener = new ChatListener(this);
	public Listener Chests = new Chests(this);
	public Listener PvP = new PvP(this);
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
	public boolean open = true;
	public FileConfiguration config;
	public FileConfiguration spawns = null;
	public File spawnsFile = null;
	public FileConfiguration data = null;
	public File dataFile = null;
	public File managementFile = null;
	public FileConfiguration management = null;
	public ArrayList<ItemStack> Reward = new ArrayList<ItemStack>();
	public ArrayList<ItemStack> Cost = new ArrayList<ItemStack>();
	public ArrayList<ItemStack> Fee = new ArrayList<ItemStack>();
	public boolean vault = false;
	public boolean eco = false;
	public Economy econ = null;
	int i = 0;
	public void onEnable(){
		config = this.getConfig();
		config.options().copyDefaults(true);
		this.saveDefaultConfig();
		spawns = this.getSpawns();
		spawns.options().copyDefaults(true);
		this.saveSpawns();
		data = this.getData();
		data.options().copyDefaults(true);
		this.saveData();
		management = this.getManagement();
		management.options().copyDefaults(true);
		this.saveManagement();
		log.info("[HungerArena] enabled v" + getDescription().getVersion());
		getServer().getPluginManager().registerEvents(DeathListener, this);
		getServer().getPluginManager().registerEvents(SpectatorListener, this);
		getServer().getPluginManager().registerEvents(FreezeListener, this);
		getServer().getPluginManager().registerEvents(JoinAndQuitListener, this);
		getServer().getPluginManager().registerEvents(ChatListener, this);
		getServer().getPluginManager().registerEvents(Chests, this);
		getServer().getPluginManager().registerEvents(PvP, this);
		getServer().getPluginManager().registerEvents(CommandBlock, this);
		getServer().getPluginManager().registerEvents(Signs, this);
		getServer().getPluginManager().registerEvents(BlockStorage, this);
		getServer().getPluginManager().registerEvents(WinGames, this);
		getServer().getPluginManager().registerEvents(Damage, this);
		getCommand("Ha").setExecutor(HaCommands);
		getCommand("Sponsor").setExecutor(SponsorCommands);
		getCommand("Startpoint").setExecutor(SpawnsCommand);
		for(String spawnlocations:spawns.getStringList("Spawns")){
			String[] coords = spawnlocations.split(",");
			if(coords[4].equalsIgnoreCase("true")){
				double x = Double.parseDouble(coords[0]);
				double y = Double.parseDouble(coords[1]);
				double z = Double.parseDouble(coords[2]);
				String world = coords[3];
				World w = getServer().getWorld(world);
				Location loc = new Location(w, x, y, z);
				location.add(loc);
			}
		}
		System.out.println("[HungerArena] Loaded " + location.size() + " tribute spawns!");
		if (setupEconomy()) {
			log.info("[HungerArena] Found Vault! Hooking in for economy!");
		}
		if (config.getDouble("config.version") != 1.3) {
			config.set("config.version", 1.3);
			config.set("rewardEco.enabled", false);
			config.set("rewardEco.reward", 100);
		}
		if (config.getBoolean("rewardEco.enabled", true) || config.getBoolean("sponsorEco.enabled", true) || config.getBoolean("EntryFee.eco", true)) {
			if (vault == true) {
				log.info("Economy hook deployed.");
				eco = true;
			}else{
				log.info("You want economy support... yet you either don't have Vault or don't have an economy plugin. Sorry, can't give you it.");
			}
		}
		if (!eco) {
			if (vault == true) {
				log.info(ChatColor.GREEN + "We see that you have Vault on your server. To set economy support to true, enable it in the config.");
			}
		}
		try{
			for(String rewards: config.getStringList("Reward")){
				String[] rinfo = rewards.split(",");
				Reward.add(new ItemStack(Integer.parseInt(rinfo[0]), Integer.parseInt(rinfo[1])));
			}
			for(String scost: config.getStringList("Sponsor_Cost")){
				String[] sinfo = scost.split(",");
				Cost.add(new ItemStack(Integer.parseInt(sinfo[0]), Integer.parseInt(sinfo[1])));
			}
			if(config.getBoolean("EntryFee.enabled")){
				for(String fee: config.getStringList("EntryFee.fee")){
					String[] finfo = fee.split(",");
					Fee.add(new ItemStack(Integer.parseInt(finfo[0]), Integer.parseInt(finfo[1])));
				}
			}
		}catch(Exception e){
			log.warning("Could not add a reward/sponsor/entry cost! One of the rewards/costs is not a number!");
		}
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
	public void reloadSpawns() {
		if (spawnsFile == null) {
			spawnsFile = new File(getDataFolder(), "spawns.yml");
		}
		spawns = YamlConfiguration.loadConfiguration(spawnsFile);

		// Look for defaults in the jar
		InputStream defConfigStream = this.getResource("spawns.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			spawns.setDefaults(defConfig);
		}
	}
	public FileConfiguration getSpawns() {
		if (spawns == null) {
			this.reloadSpawns();
		}
		return spawns;
	}
	public void saveSpawns() {
		if (spawns == null || spawnsFile == null) {
			return;
		}
		try {
			getSpawns().save(spawnsFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + spawnsFile, ex);
		}
	}
	public void reloadData() {
		if (dataFile == null) {
			dataFile = new File(getDataFolder(), "Data.yml");
		}
		data = YamlConfiguration.loadConfiguration(dataFile);

		// Look for defaults in the jar
		InputStream defConfigStream = this.getResource("Data.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			data.setDefaults(defConfig);
		}
	}
	public FileConfiguration getData() {
		if (data == null) {
			this.reloadData();
		}
		return data;
	}
	public void saveData() {
		if (data == null || dataFile == null) {
			return;
		}
		try {
			getData().save(dataFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + dataFile, ex);
		}
	}
	public void reloadManagement() {
		if (managementFile == null) {
			managementFile = new File(getDataFolder(), "commandAndBlockManagement.yml");
		}
		management = YamlConfiguration.loadConfiguration(managementFile);

		// Look for defaults in the jar
		InputStream defConfigStream = this.getResource("commandAndBlockManagement.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			management.setDefaults(defConfig);
		}
	}
	public FileConfiguration getManagement() {
		if (management == null) {
			this.reloadManagement();
		}
		return management;
	}
	public void saveManagement() {
		if (management == null || managementFile == null) {
			return;
		}
		try {
			getManagement().save(managementFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + managementFile, ex);
		}
	}
	public void winner(){
		String[] Spawncoords = spawns.getString("Spawn_coords").split(",");
		World spawnw = getServer().getWorld(Spawncoords[3]);
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
		if(Playing.size()== 1 && canjoin== false){
			//Announce winner
			for(i = 0; i < Playing.size(); i++){
				String winnername = Playing.get(i++);
				Player winner = getServer().getPlayerExact(winnername);
				String winnername2 = winner.getName();
				getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
				winner.getInventory().clear();
				winner.getInventory().setBoots(null);
				winner.getInventory().setChestplate(null);
				winner.getInventory().setHelmet(null);
				winner.getInventory().setLeggings(null);
				for(PotionEffect pe: winner.getActivePotionEffects()){
					PotionEffectType potion = pe.getType();
					winner.removePotionEffect(potion);
				}
				Tele.add(winner);
				winner.teleport(Spawn);
				if(!config.getBoolean("rewardEco.enabled")){
					for(ItemStack Rewards: Reward){
						winner.getInventory().addItem(Rewards);
					}
				}else{
					econ.depositPlayer(winner.getName(), config.getDouble("eco.reward"));
				}
				Playing.clear();
			}
			//Show spectators
			for(String s1: Watching){
				Player spectator = getServer().getPlayerExact(s1);
				spectator.setAllowFlight(false);
				spectator.teleport(Spawn);
				for(Player online:getServer().getOnlinePlayers()){
					online.showPlayer(spectator);
				}
			}
			if(config.getString("Auto_Restart").equalsIgnoreCase("True")){
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
					public void run(){
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart");

					}
				}, 220L);
			}
		}
	}
}
