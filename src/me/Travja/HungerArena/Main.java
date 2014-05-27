package me.Travja.HungerArena;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.Travja.HungerArena.Listeners.BlockStorage;
import me.Travja.HungerArena.Listeners.Boundaries;
import me.Travja.HungerArena.Listeners.ChatListener;
import me.Travja.HungerArena.Listeners.DeathListener;
import me.Travja.HungerArena.Listeners.DmgListener;
import me.Travja.HungerArena.Listeners.FreezeListener;
import me.Travja.HungerArena.Listeners.JoinAndQuitListener;
import me.Travja.HungerArena.Listeners.PvP;
import me.Travja.HungerArena.Listeners.Signs;
import me.Travja.HungerArena.Listeners.SpectatorListener;
import me.Travja.HungerArena.Listeners.TeleportListener;
import me.Travja.HungerArena.Listeners.spawnsListener;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Main extends JavaPlugin{
	static Logger log;
	public HashMap<Integer, List<String>> Playing = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Ready = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Dead = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Quit = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Out = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Watching = new HashMap<Integer, List<String>>();
	public HashMap<String, Integer> Kills = new HashMap<String, Integer>();
	public HashMap<Integer, List<String>> NeedConfirm = new HashMap<Integer, List<String>>();
	public HashMap<Integer, HashMap<Integer, Location>> location = new HashMap<Integer, HashMap<Integer, Location>>();
	public HashMap<Integer, List<String>> inArena = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Frozen = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> arena = new HashMap<Integer, List<String>>();
	public HashMap<Integer, Boolean> canjoin = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Integer> maxPlayers = new HashMap<Integer, Integer>();
	public HashMap<Integer, Boolean> open = new HashMap<Integer, Boolean>();
	public HashMap<String, String> setting = new HashMap<String, String>();
	public HashMap<Integer, Integer> gp = new HashMap<Integer, Integer>();
	public ArrayList<Player> Tele = new ArrayList<Player>();
	public ArrayList<String> needInv = new ArrayList<String>();
	public List<String> worlds = new ArrayList<String>();

	public HashMap<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();

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
	public Listener WorldChange = new WorldChange(this);
	public Listener Boundaries = new Boundaries(this);
	public Listener spawnsListener = new spawnsListener(this);
	public CommandExecutor HaCommands = new HaCommands(this);
	public CommandExecutor SponsorCommands = new SponsorCommands(this);
	public CommandExecutor SpawnsCommand = new SpawnsCommand(this);

	public me.Travja.HungerArena.ConfigManager ConfigManager = new ConfigManager(this);

	public boolean exists;
	public boolean restricted;

	public FileConfiguration config;
	public FileConfiguration spawns = null;
	public File spawnsFile = null;
	public FileConfiguration data = null;
	public File dataFile = null;
	public FileConfiguration management = null;
	public File managementFile = null;

	public FileConfiguration MyChests = null;
	public File ChestsFile = null;

	public ArrayList<ItemStack> Reward = new ArrayList<ItemStack>();
	public ArrayList<ItemStack> Cost = new ArrayList<ItemStack>();
	public ArrayList<ItemStack> Fee = new ArrayList<ItemStack>();

	public boolean vault = false;
	public boolean eco = false;
	public Economy econ = null;

	int i = 0;
	int v = 0;
	int start = 0;
	int a = 0;
	int grace = 0;

	@SuppressWarnings("deprecation")
	public void onEnable(){
		log = this.getLogger();

		config = this.getConfig();
		config.options().copyDefaults(true);
		if(!new File(this.getDataFolder(), "config.yml").exists())
			this.saveDefaultConfig();
		spawns = this.getSpawns();
		spawns.options().copyDefaults(true);
		if(!new File(this.getDataFolder(), "spawns.yml").exists())
			this.saveSpawns();
		data = this.getData();
		data.options().copyDefaults(true);
		if(!new File(this.getDataFolder(), "Data.yml").exists())
			this.saveData();
		management = this.getManagement();
		management.options().copyDefaults(true);
		if(!new File(this.getDataFolder(), "commandAndBlockManagement.yml").exists())
			this.saveManagement();
		MyChests = this.getChests();
		MyChests.options().copyDefaults(true);
		if(!new File(this.getDataFolder(), "Chests.yml").exists())
			this.saveChests();
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
		getServer().getPluginManager().registerEvents(WorldChange, this);
		getServer().getPluginManager().registerEvents(Boundaries, this);
		getServer().getPluginManager().registerEvents(spawnsListener, this);

		getCommand("Ha").setExecutor(HaCommands);
		getCommand("Sponsor").setExecutor(SponsorCommands);
		getCommand("Startpoint").setExecutor(SpawnsCommand);

		for(File file: getDataFolder().listFiles()){
			String filename = file.getName();
			if(filename != "commandAndBlockManagement" && filename != "config" && filename != "Data" && filename != "spawns")
				needInv.add(filename);
		}

		i = 1;
		if(spawns.getConfigurationSection("Spawns")!= null){
			Map<String, Object> temp = spawns.getConfigurationSection("Spawns").getValues(false);
			for(Entry<String, Object> entry: temp.entrySet()){
				if(spawns.getConfigurationSection("Spawns." + entry.getKey())!= null){
					Map<String, Object> temp2 = spawns.getConfigurationSection("Spawns." + entry.getKey()).getValues(false);
					for(Map.Entry<String, Object> e: temp2.entrySet()){
						if(spawns.get("Spawns." + entry.getKey() + "." + e.getKey())!= null){
							if(!e.getKey().equals("Max") || !e.getKey().equals("Min")){
								String[] coords = ((String) spawns.get("Spawns." + entry.getKey() + "." + e.getKey())).split(",");
								Integer a = Integer.parseInt(entry.getKey());
								Integer s = Integer.parseInt(e.getKey());
								if(location.get(a)== null)
									location.put(a, new HashMap<Integer, Location>());
								log.info("Added spawn number " + s + " in arena " + a + "!");
								location.get(a).put(s, new Location(getServer().getWorld(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3])));
							}
						}
					}
				}
			}
		}

		for(i = 1; i <= location.size(); i++){
			if(location.get(i)!= null){
				log.info("Loaded " + location.get(i).size() + " tribute spawns for arena " + i + "!");
				Playing.put(i, new ArrayList<String>());
				Ready.put(i, new ArrayList<String>());
				Dead.put(i, new ArrayList<String>());
				Quit.put(i, new ArrayList<String>());
				Out.put(i, new ArrayList<String>());
				Watching.put(i, new ArrayList<String>());
				NeedConfirm.put(i, new ArrayList<String>());
				inArena.put(i, new ArrayList<String>());
				Frozen.put(i, new ArrayList<String>());
				arena.put(i, new ArrayList<String>());
				canjoin.put(i, false);
				if(location.get(i).size()== config.getInt("maxPlayers")){
					maxPlayers.put(i, location.get(i).size());
				}else if(location.size()< config.getInt("maxPlayers")){
					maxPlayers.put(i, location.get(i).size());
				}else if(location.size()> config.getInt("maxPlayers")){
					maxPlayers.put(i, config.getInt("maxPlayers"));
				}
				log.info("Max players is for arena " + i + " is " + maxPlayers.get(i));
				open.put(i, true);
			}
		}
		if (setupEconomy()) {
			log.info("Found Vault! Hooking in for economy!");
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
				log.info("We see that you have Vault on your server. To set economy support to true, enable it in the config.");
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
		ConfigManager.setup();
		scoreboardInit();
		log.info("Enabled v" + getDescription().getVersion());
	}

	public void onDisable(){
		log.info("Disabled v" + getDescription().getVersion());
	}

	public WorldEditPlugin hookWE() {
		Plugin wPlugin = getServer().getPluginManager().getPlugin("WorldEdit");

		if ((wPlugin == null) || (!(wPlugin instanceof WorldEditPlugin)))
			return null;

		return (WorldEditPlugin) wPlugin;
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
	public void reloadChests() {
		if (ChestsFile == null) {
			ChestsFile = new File(getDataFolder(), "Chests.yml");
		}
		MyChests = YamlConfiguration.loadConfiguration(ChestsFile);
	}
	public FileConfiguration getChests() {
		if (MyChests == null) {
			this.reloadChests();
		}
		return MyChests;
	}
	public void saveChests() {
		if (MyChests == null || ChestsFile == null) {
			return;
		}
		try {
			getChests().save(ChestsFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + ChestsFile, ex);
		}
	}
	File PFile = null;
	FileConfiguration PConfig= null;
	public void reloadPFile(String pname) {
		if (PFile == null) {
			PFile = new File(getDataFolder(), pname + ".yml");
		}
		PConfig = YamlConfiguration.loadConfiguration(PFile);

		InputStream defConfigStream = this.getResource(pname + ".yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			PConfig.setDefaults(defConfig);
		}
	}
	public FileConfiguration getPConfig(String pname) {
		this.reloadPFile(pname);
		return PConfig;
	}
	public void savePFile(String pname) {
		reloadPFile(pname);
		try {
			getPConfig(pname).save(PFile);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Could not save config to " + PFile, ex);
		}
	}
	@SuppressWarnings("deprecation")
	public void winner(final Integer a){
		String[] Spawncoords = spawns.getString("Spawn_coords").split(",");
		World spawnw = getServer().getWorld(Spawncoords[3]);
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
		if(Playing.get(a).size()== 1){
			if(canjoin.get(a)== true){
				//Announce winner
				for(i = 0; i < Playing.get(a).size(); i++){
					String winnername = Playing.get(a).get(i);
					final Player winner = getServer().getPlayerExact(winnername);
					String winnername2 = winner.getName();
					getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
					winner.getInventory().clear();
					winner.getInventory().setBoots(null);
					winner.getInventory().setChestplate(null);
					winner.getInventory().setHelmet(null);
					winner.getInventory().setLeggings(null);
					winner.setLevel(0);
					for(PotionEffect pe: winner.getActivePotionEffects()){
						PotionEffectType potion = pe.getType();
						winner.removePotionEffect(potion);
					}
					Tele.add(winner);
					needInv.add(winnername2);
					winner.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
					if(scoreboards.containsKey(winner.getName()))
						scoreboards.remove(winner.getName());
					if(Kills.containsKey(winner.getName()))
						Kills.remove(winner.getName());
					//final World w = winner.getWorld();
					winner.teleport(Spawn);


					////////////////////////////////////////////////////////
					////////////////////  FIREWORKS  ///////////////////////
					////////////////////////////////////////////////////////


					for(i = 0; i < 10; i++){
						Bukkit.getScheduler().runTaskLater(this, new Runnable(){
							public void run(){
								//Spawn the Firework, get the FireworkMeta.
								Firework fw = (Firework) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
								FireworkMeta fwm = fw.getFireworkMeta();

								//Our random generator
								Random r = new Random();   

								//Get the type
								int rt = r.nextInt(4) + 1;
								Type type = Type.BALL;       
								if (rt == 1) type = Type.BALL;
								if (rt == 2) type = Type.BALL_LARGE;
								if (rt == 3) type = Type.BURST;
								if (rt == 4) type = Type.CREEPER;
								if (rt == 5) type = Type.STAR;

								//Get our random colours   
								int r1i = r.nextInt(17) + 1;
								int r2i = r.nextInt(17) + 1;
								Color c1 = getColor(r1i);
								Color c2 = getColor(r2i);

								//Create our effect with this
								FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

								//Then apply the effect to the meta
								fwm.addEffect(effect);

								//Generate some random power and set it
								int rp = r.nextInt(2) + 1;
								fwm.setPower(rp);

								//Then apply this to our rocket
								fw.setFireworkMeta(fwm);
							}
						},20 + i*20L);
					}


					////////////////////////////////////////////////////////
					////////////////////////////////////////////////////////
					////////////////////////////////////////////////////////


					/*if(config.getBoolean("reloadWorld")){
					getServer().unloadWorld(w, false);
					getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
						public void run(){
							getServer().createWorld(new WorldCreator(w.getName()));
						}
					},200L);
				}*/
					if(!config.getBoolean("rewardEco.enabled")){
						for(ItemStack Rewards: Reward){
							winner.getInventory().addItem(Rewards);
						}
					}else{
						for(ItemStack Rewards: Reward){
							winner.getInventory().addItem(Rewards);
						}
						econ.depositPlayer(winner.getName(), config.getDouble("rewardEco.reward"));
					}
					if(deathtime.get(a)!= null)
						getServer().getScheduler().cancelTask(deathtime.get(a));
					getServer().getScheduler().cancelTask(grace);
					getServer().getScheduler().cancelTask(start);
				}
				Playing.get(a).clear();
				//Show spectators
				for(String s1: Watching.get(a)){
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
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart " + a);
						}
					}, 220L);
				}
			}else{
				//Announce winner
				for(i = 0; i < Playing.get(a).size(); i++){
					String winnername = Playing.get(a).get(i);
					Player winner = getServer().getPlayerExact(winnername);
					String winnername2 = winner.getName();
					winner.getInventory().clear();
					winner.getInventory().setBoots(null);
					winner.getInventory().setChestplate(null);
					winner.getInventory().setHelmet(null);
					winner.getInventory().setLeggings(null);
					winner.setLevel(0);
					for(PotionEffect pe: winner.getActivePotionEffects()){
						PotionEffectType potion = pe.getType();
						winner.removePotionEffect(potion);
					}
					Tele.add(winner);
					needInv.add(winnername2);
					//final World w = winner.getWorld();
					winner.teleport(Spawn);
					/*if(config.getBoolean("reloadWorld")){
					getServer().unloadWorld(w, false);
					getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
						public void run(){
							getServer().createWorld(new WorldCreator(w.getName()));
						}
					},200L);
				}*/
					Playing.get(a).clear();
					getServer().getScheduler().cancelTask(deathtime.get(a));
				}
				//Show spectators
				for(String s1: Watching.get(a)){
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
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart " + a);
						}
					}, 220L);
				}
			}
		}
	}



	private Color getColor(int i) {
		if(i==1)
			return Color.AQUA;
		else if(i==2)
			return Color.BLACK;
		else if(i==3)
			return Color.BLUE;
		else if(i==4)
			return Color.FUCHSIA;
		else if(i==5)
			return Color.GRAY;
		else if(i==6)
			return Color.GREEN;
		else if(i==7)
			return Color.LIME;
		else if(i==8)
			return Color.MAROON;
		else if(i==9)
			return Color.NAVY;
		else if(i==10)
			return Color.OLIVE;
		else if(i==11)
			return Color.ORANGE;
		else if(i==12)
			return Color.PURPLE;
		else if(i==13)
			return Color.RED;
		else if(i==14)
			return Color.SILVER;
		else if(i==15)
			return Color.TEAL;
		else if(i==16)
			return Color.WHITE;
		else
			return Color.YELLOW;
	}



	private void scoreboardInit(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			public void run(){
				for(Player pl: getServer().getOnlinePlayers()){
					updateScoreboard(pl);
				}
			}
		}, 20L, 10L);
	}

	@SuppressWarnings("deprecation")
	public void updateScoreboard(Player p){
		if(getArena(p)!= null){
			a = getArena(p);
			if(scoreboards.get(p.getName())!= null && scoreboards.get(p.getName()).getObjective("HA")!= null){
				Scoreboard sb = scoreboards.get(p.getName());
				Objective obj = sb.getObjective("HA");
				if(obj!= null){
					Score kills = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Kills"));
					Score players = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Players"));
					Score spectators = obj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Spectators"));
					players.setScore(Playing.get(a).size());
					if(Kills.containsKey(p.getName()))
						kills.setScore(Kills.get(p.getName()));
					if(Watching.get(a)!= null)
						spectators.setScore(Watching.get(a).size());
					if(config.getInt("DeathMatch")!= 0){
						if(timetodeath.get(a)!= null){
							if(timetodeath.get(a)> 0){
								String secs = String.valueOf((Integer.valueOf(timetodeath.get(a)-timetodeath.get(a)/60*60)< 10) ? "0" + Integer.valueOf(timetodeath.get(a)-timetodeath.get(a)/60*60) : Integer.valueOf(timetodeath.get(a)-timetodeath.get(a)/60*60));
								obj.setDisplayName(ChatColor.GREEN + "HA - DMTime: " + ChatColor.AQUA + Integer.valueOf(timetodeath.get(a)/60) + ":" + secs);
							}else{
								obj.setDisplayName(ChatColor.GREEN + "HA - " + ChatColor.RED + "DEATHMATCH");
							}
						}
					}else{
						obj.setDisplayName(ChatColor.GREEN + "HungerArena");
					}
					p.setScoreboard(sb);
				}
			}
		}
	}


	public HashMap<Integer, Integer> deathtime = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> timetodeath = new HashMap<Integer, Integer>();
	@SuppressWarnings("deprecation")
	public void startGames(final Integer a){
		String begin = ChatColor.translateAlternateColorCodes('&', config.getString("Start_Message"));
		final String msg = begin;
		if(config.getInt("Countdown_Timer") != 0) {
			i = config.getInt("Countdown_Timer") ;
		} else {
			i = 10;
		}
		for(String gn: Playing.get(a)){
			Scoreboard scoreboard = getServer().getScoreboardManager().getNewScoreboard();
			Objective sobj = scoreboard.registerNewObjective("HA", "HAData");
			sobj.setDisplayName(ChatColor.GREEN + "HA - Starting");
			Score skills = sobj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Kills"));
			skills.setScore(0);
			Score sdeaths = sobj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Spectators"));
			sdeaths.setScore(0);
			Score splayers = sobj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Players"));
			splayers.setScore(0);
			sobj.setDisplaySlot(DisplaySlot.SIDEBAR);
			Bukkit.getPlayer(gn).setScoreboard(scoreboard);
			scoreboards.put(Bukkit.getPlayer(gn).getName(), Bukkit.getPlayer(gn).getScoreboard());
		}
		getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha Refill " + a);
		getServer().getScheduler().cancelTask(start);
		if(config.getString("Countdown").equalsIgnoreCase("true")){
			start = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
				public void run(){
					if(i > 0){
						if(worlds.isEmpty()){
							if(config.getBoolean("broadcastAll")){
								getServer().broadcastMessage(ChatColor.AQUA + "Game " + a + " starting in: " + String.valueOf(i));
							}else{
								for(String gn: Playing.get(a)){
									Player g = getServer().getPlayer(gn);
									g.sendMessage(ChatColor.AQUA + "Game starting in: " + String.valueOf(i));
								}
							}
						}else{
							for(String world: worlds){
								World w = getServer().getWorld(world);
								if(config.getBoolean("broadcastAll")){
									for(Player wp: w.getPlayers()){
										wp.sendMessage(String.valueOf(i));
									}
								}else{
									for(String gn: Playing.get(a)){
										Player g = getServer().getPlayer(gn);
										g.sendMessage(String.valueOf(i));
									}
								}
							}
						}
					}
					i = i-1;
					canjoin.put(a, true);
					if(i== -1){
						for(String gn: Playing.get(a)){
							Scoreboard scoreboard = getServer().getScoreboardManager().getNewScoreboard();
							Objective sobj = scoreboard.registerNewObjective("HA", "HAData");
							sobj.setDisplayName(ChatColor.GREEN + "HA - Starting");
							Score skills = sobj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Kills"));
							skills.setScore(0);
							Score sdeaths = sobj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Spectators"));
							sdeaths.setScore(0);
							Score splayers = sobj.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Players"));
							splayers.setScore(0);
							sobj.setDisplaySlot(DisplaySlot.SIDEBAR);
							Bukkit.getPlayer(gn).setScoreboard(scoreboard);
							scoreboards.put(Bukkit.getPlayer(gn).getName(), Bukkit.getPlayer(gn).getScoreboard());
						}
						if(Frozen.get(a)!= null)
							Frozen.get(a).clear();
						if(config.getBoolean("broadcastAll")){
							getServer().broadcastMessage(msg);
						}else{
							for(String gn: Playing.get(a)){
								Player g = getServer().getPlayer(gn);
								g.sendMessage(msg);
							}
						}
						if(config.getInt("Grace_Period")!= 0){
							gp.put(a, config.getInt("Grace_Period"));
							grace = getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("HungerArena"), new Runnable(){
								public void run(){
									gp.put(a, gp.get(a)-1);
									if(gp.get(a) == 30 || gp.get(a) == 15 || (gp.get(a) < 11 && gp.get(a) != 0)){
										if(config.getBoolean("broadcastAll")){
											for(Player wp: location.get(a).get(1).getWorld().getPlayers()){
												wp.sendMessage(ChatColor.GREEN + "Grace period ends in " + gp.get(a) + " seconds!");
											}
										}else
											getServer().broadcastMessage(ChatColor.GREEN + "Grace period ends in " + gp.get(a) + " seconds!");
									}
									if(gp.get(a) == 0){
										if(config.getBoolean("broadcastAll")){
											for(Player wp: location.get(a).get(1).getWorld().getPlayers()){
												wp.sendMessage(ChatColor.GREEN + "Grace period is over, FIGHT!");
											}
										}else
											getServer().broadcastMessage(ChatColor.GREEN + "Grace period is over, FIGHT!");
										getServer().getScheduler().cancelTask(grace);
									}
								}
							},20L, 20L);
						}
						if(config.getInt("DeathMatch")!= 0){
							int death = config.getInt("DeathMatch");
							timetodeath.put(a, death*60);
							deathtime.put(a, getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("HungerArena"), new Runnable(){
								public void run(){
									timetodeath.put(a, timetodeath.get(a)-1);
									if(Integer.valueOf(timetodeath.get(a))%300== 0){
										if(config.getBoolean("broadcastAll")){
											for(Player wp: location.get(a).get(1).getWorld().getPlayers()){
												if(timetodeath.get(a)!= 0){
													wp.sendMessage(ChatColor.YELLOW + String.valueOf(timetodeath.get(a)/60) + ChatColor.RED + " mins till the death match!");
												}
											}
										}else{
											for(String gn: Playing.get(a)){
												Player g = getServer().getPlayer(gn);
												g.sendMessage(ChatColor.YELLOW + String.valueOf(timetodeath.get(a)) + ChatColor.RED + " mins till the death match!");
											}
										}
									}
									if(timetodeath.get(a)== 0){
										i = 1;
										for(String playing: Playing.get(a)){
											Player tribute = getServer().getPlayerExact(playing);
											tribute.teleport(location.get(a).get(i));
											i = i+1;
											for(PotionEffect pe: tribute.getActivePotionEffects()){
												PotionEffectType potion = pe.getType();
												tribute.removePotionEffect(potion);
											}
											if(tribute.getAllowFlight()){
												tribute.setAllowFlight(false);
											}
										}
										if(config.getBoolean("broadcastAll")){
											for(Player wp: location.get(a).get(1).getWorld().getPlayers()){
												wp.sendMessage(ChatColor.RED + "The final battle has begun! " + Playing.get(a).size() + " tributes will be facing off!");
											}
										}else{
											for(String gn: Playing.get(a)){
												Player g = getServer().getPlayer(gn);
												g.sendMessage(ChatColor.RED + "The final battle has begun! " + Playing.get(a).size() + " tributes will be facing off!");
											}
										}
										getServer().getScheduler().cancelTask(deathtime.get(a));
									}
								}
							}, 20L, 20L));
						}
					}
				}
			}, 20L, 20L);
		}else{
			Frozen.get(a).clear();
			if(config.getBoolean("broadcastAll")){
				getServer().broadcastMessage(msg);
			}else{
				for(String gn: Playing.get(a)){
					Player g = getServer().getPlayer(gn);
					g.sendMessage(msg);
				}
			}
			canjoin.put(a, true);
			getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha Refill " + a);
		}
	}
	public Integer getArena(Player p){
		for (int x: Playing.keySet()) {
			if (Playing.get(x).contains(p.getName())){
				return x;
			}
		}
		return null;
	}
	public boolean isSpectating(Player p){
		int x = 0;
		if(!Watching.isEmpty()){
			for(x= 1; x <= Watching.size(); x++){
				if(Watching.get(x).contains(p.getName())){
					x = Watching.size()+1;
					return true;
				}else if(Watching.size()== x)
					return false;
			}
		}
		return false;
	}
}
