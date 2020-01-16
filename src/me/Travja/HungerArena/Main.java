package me.Travja.HungerArena;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import me.Travja.HungerArena.Listeners.BlockStorage;
import me.Travja.HungerArena.Listeners.WorldChange;
import me.Travja.HungerArena.Listeners.Boundaries;
import me.Travja.HungerArena.Listeners.ChatListener;
import me.Travja.HungerArena.Listeners.DeathListener;
import me.Travja.HungerArena.Listeners.FreezeListener;
import me.Travja.HungerArena.Listeners.JoinAndQuitListener;
import me.Travja.HungerArena.Listeners.PvP;
import me.Travja.HungerArena.Listeners.SignsAndBeds;
import me.Travja.HungerArena.Listeners.SignsAndBedsOld;
import me.Travja.HungerArena.Listeners.SpectatorListener;
import me.Travja.HungerArena.Listeners.SpectatorListenerOld;
import me.Travja.HungerArena.Listeners.TeleportListener;
import me.Travja.HungerArena.Listeners.spawnsListener;
import net.milkbowl.vault.economy.Economy;

import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Torch;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
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
	public HashMap<Integer, String> MatchRunning = new HashMap<Integer, String>();
	public HashMap<Integer, Boolean> canjoin = new HashMap<Integer, Boolean>();
	public HashMap<Integer, Boolean> open = new HashMap<Integer, Boolean>();
	private HashMap<Integer,Integer> CountT = new HashMap<Integer,Integer>();
	public HashMap<Integer, List<String>> Quit = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Out = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Watching = new HashMap<Integer, List<String>>();
	public HashMap<String, Integer> Kills = new HashMap<String, Integer>();
	public HashMap<Integer, List<String>> NeedConfirm = new HashMap<Integer, List<String>>();
	public HashMap<Integer, HashMap<Integer, Location>> location = new HashMap<Integer, HashMap<Integer, Location>>();
	public HashMap<Integer, List<String>> inArena = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> Frozen = new HashMap<Integer, List<String>>();
	public HashMap<Integer, List<String>> arena = new HashMap<Integer, List<String>>();
	public HashMap<Integer, Integer> maxPlayers = new HashMap<Integer, Integer>();
	public HashMap<String, String> setting = new HashMap<String, String>();
	public HashMap<Integer, Integer> gp = new HashMap<Integer, Integer>();
	public ArrayList<Player> Tele = new ArrayList<Player>();
	public ArrayList<String> needInv = new ArrayList<String>();
	public HashMap<Integer, String> worldsNames = new HashMap<Integer, String>();

    public HashMap<String, Scoreboard> scoreboards = new HashMap<String, Scoreboard>();

	Listener DeathListener = new DeathListener(this);
	Listener SpectatorListener = null;
	Listener SpectatorListenerOld = null;
	Listener FreezeListener = new FreezeListener(this);
	Listener JoinAndQuitListener = new JoinAndQuitListener(this);
	Listener ChatListener = new ChatListener(this);
	Listener Chests = new Chests(this);
	Listener PvP = new PvP(this);
	Listener CommandBlock = new CommandBlock(this);
	Listener Teleport = new TeleportListener(this);
	SignsAndBeds SignsAndBeds = null;
	SignsAndBedsOld SignsAndBedsOld = null;
	Listener BlockStorage = new BlockStorage(this);
	//Listener WinGames = new WinGamesListener(this);
	Listener WorldChange = new WorldChange(this);
	Listener Boundaries = new Boundaries(this);
	Listener spawnsListener = new spawnsListener(this);
	CommandExecutor HaCommands = new HaCommands(this);
	CommandExecutor SponsorCommands = new SponsorCommands(this);
	CommandExecutor SpawnsCommand = new SpawnsCommand(this);

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
	public ArrayList<ItemStack> ChestPay = new ArrayList<ItemStack>();
	
	public boolean vault = false;
	public boolean eco = false;
	public Economy econ = null;

	int i = 0;
	int v = 0;
	int a = 0;

	File PFilePath = new File(getDataFolder(), "/inventories");

	public void onEnable(){
		log = this.getLogger();

		getConfig().options().copyDefaults(true); 
		getConfig().options().copyHeader(true);
		saveDefaultConfig();
		saveConfig();
		config = getConfig();
		spawns = getSpawns();
		data = getData();
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
		getServer().getPluginManager().registerEvents(FreezeListener, this);
		getServer().getPluginManager().registerEvents(JoinAndQuitListener, this);
		getServer().getPluginManager().registerEvents(ChatListener, this);
		getServer().getPluginManager().registerEvents(Chests, this);
		getServer().getPluginManager().registerEvents(PvP, this);
		getServer().getPluginManager().registerEvents(CommandBlock, this);
		
		try {
			Class.forName("org.bukkit.Tag");
			SignsAndBeds = new SignsAndBeds(this);
			getServer().getPluginManager().registerEvents(SignsAndBeds, this);
			SpectatorListener = new SpectatorListener(this);
			getServer().getPluginManager().registerEvents(SpectatorListener, this);
			getLogger().info("Events 1.13+ enabled!");
			} catch (NoClassDefFoundError | ClassNotFoundException exp) {
				SignsAndBedsOld = new SignsAndBedsOld(this);
				getServer().getPluginManager().registerEvents(SignsAndBedsOld, this);
				SpectatorListenerOld = new SpectatorListenerOld(this);
				getServer().getPluginManager().registerEvents(SpectatorListenerOld, this);
				getLogger().info("Events 1.12- enabled!");
			} 
		getServer().getPluginManager().registerEvents(BlockStorage, this);
		//getServer().getPluginManager().registerEvents(WinGames, this);
		getServer().getPluginManager().registerEvents(WorldChange, this);
		getServer().getPluginManager().registerEvents(Boundaries, this);
		getServer().getPluginManager().registerEvents(spawnsListener, this);

		getCommand("Ha").setExecutor(HaCommands);
		getCommand("Sponsor").setExecutor(SponsorCommands);
		getCommand("Startpoint").setExecutor(SpawnsCommand);
					
		if (!PFilePath.exists()) {
			PFilePath.mkdirs();
		}
		for(File file: PFilePath.listFiles()){
			String filename = file.getName();
			int lastIndex = filename.lastIndexOf('.');
			filename = filename.substring(0, lastIndex >= 0 ? lastIndex : 0);
			needInv.add(filename); 
		}

		i = 1;
		
		this.reloadSpawnpoints(true);
				
		if (setupEconomy()) {
			log.info("Found Vault! Hooking in for economy!");
		}
		if (config.getDouble("config.version") != 1.4) {
			config.set("config.version", 1.4);
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
			List<String> RewardItemList = new ArrayList<String>();
			List<String> SponsorItemList = new ArrayList<String>();
			List<String> EntryfeeItemList = new ArrayList<String>();
			List<String> PayForChests = new ArrayList<String>();

			for(String rewards: config.getStringList("Reward")){
				String[] rinfo = rewards.split(",");
				Material NewMat = getNewMaterial(rinfo[0],0);
				if (NewMat != null) {
					Reward.add(new ItemStack(NewMat, Integer.parseInt(rinfo[1])));
					RewardItemList.add(NewMat.name()+","+Integer.parseInt(rinfo[1]));
				}
			}
			config.set("Reward", RewardItemList);
			
			for(String scost: config.getStringList("Sponsor_Cost")){
				String[] sinfo = scost.split(",");
				Material NewMat = getNewMaterial(sinfo[0],0);
				if (NewMat != null) {
					Cost.add(new ItemStack(NewMat, Integer.parseInt(sinfo[1])));
					SponsorItemList.add(NewMat.name()+","+Integer.parseInt(sinfo[1]));
				}
			}
			config.set("Sponsor_Cost", SponsorItemList);
			
			if(config.getBoolean("EntryFee.enabled")){
				for(String fee: config.getStringList("EntryFee.fee")){
					String[] finfo = fee.split(",");
					Material NewMat = getNewMaterial(finfo[0],0);
					if (NewMat != null) {
						Fee.add(new ItemStack(NewMat, Integer.parseInt(finfo[1])));
						EntryfeeItemList.add(NewMat.name()+","+Integer.parseInt(finfo[1]));
					}
				}
				config.set("EntryFee.fee", EntryfeeItemList);
			}
			
			if (config.getBoolean("ChestPay.enabled")){
				for(String paychests: config.getStringList("ChestPay.items")){
					String[] rew = paychests.split(",");
					Material NewMat = getNewMaterial(rew[0],0);
					if (NewMat != null) {
						ChestPay.add(new ItemStack(NewMat, Integer.parseInt(rew[1])));
						PayForChests.add(NewMat.name()+","+Integer.parseInt(rew[1]));
					}
				}
				config.set("ChestPay.items", PayForChests);
			}
			
			ArrayList<String> newList = new ArrayList<String>();
			for (String block: management.getStringList("blocks.whitelist")){
					Material NewMat = getNewMaterial(block,0);
					if (NewMat != null) newList.add(NewMat.name());
			}
			management.set("blocks.whitelist", newList);
			this.saveManagement(); 
		}catch(Exception e){
			log.warning("Could not add a reward/sponsor/entry cost or whitelist/blacklist! One of them is wrong!");
		}

		try {
			String spt = config.getString("spawnsTool");
			if (!spt.trim().toLowerCase().contains("[a-z]")) config.set("spawnsTool",getNewMaterial(spt,0).name());
		} catch (Exception e){}
		restricted = config.getBoolean("Restricted");
		saveConfig();
		scoreboardInit();
		log.info("Enabled v" + getDescription().getVersion());
	}
	public Material getNewMaterial(String base,int data){
		Material NewMat=null;
		if (Material.getMaterial(base)!=null){
			NewMat = Material.getMaterial(base);
		}
		else if (base.replaceAll("[0-9]","").equals("")){
			NewMat = findOldMaterial(Integer.parseInt(base),(byte)data);
		} else {
			try {
				NewMat = Material.getMaterial(base,true);
			} catch (NoSuchMethodError n) {
			}
		}
		return NewMat;
	}

	public Material findOldMaterial(int typeId, byte dataValue) { 
	    for(Material i : EnumSet.allOf(Material.class)) {
	    	try {
	    		if(i.getId() == typeId) return Bukkit.getUnsafe().fromLegacy(new MaterialData(i, dataValue));
	    	} catch (IllegalArgumentException | NoSuchMethodError e){
	    		try {
	    			if(i.getId() == typeId) {
	    				return new MaterialData(i, dataValue).getItemType();
	    			}
	    		} catch (IllegalArgumentException ee) {} 
	    	}
	    }
	    return null;
	}

	
	public void onDisable(){
		log.info("Disabled v" + getDescription().getVersion());
	}

	public void reloadSpawnpoints(boolean verbose){
		if(spawns.getConfigurationSection("Spawns")!= null){
			Map<String, Object> temp = spawns.getConfigurationSection("Spawns").getValues(false);
			for(Entry<String, Object> entry: temp.entrySet()){
				if(spawns.getConfigurationSection("Spawns." + entry.getKey())!= null){
					Integer a = Integer.parseInt(entry.getKey());
					worldsNames.put(a, "none_meening_this_is_not_a_map"); 
					if(location.get(a)== null) location.put(a, new HashMap<Integer, Location>());
					Map<String, Object> temp2 = spawns.getConfigurationSection("Spawns." + entry.getKey()).getValues(false);
					for(Map.Entry<String, Object> e: temp2.entrySet()){
						if(spawns.get("Spawns." + entry.getKey() + "." + e.getKey())!= null){
							if(!e.getKey().equals("Max") || !e.getKey().equals("Min")){
								String[] coords = ((String) spawns.get("Spawns." + entry.getKey() + "." + e.getKey())).split(",");
								Integer s = Integer.parseInt(e.getKey());
								location.get(a).put(s, new Location(getServer().getWorld(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Double.parseDouble(coords[3])));
								worldsNames.put(a, coords[0]);
							}
						}
					}
				}
			}
		}

		for(int i : location.keySet()){
			if(location.get(i).size()!= 0){
				if (verbose) log.info("Loaded " + location.get(i).size() + " tribute spawns for arena " + i + "!");
				Playing.put(i, new ArrayList<String>());
				Ready.put(i, new ArrayList<String>());
				Dead.put(i, new ArrayList<String>());
				MatchRunning.put(i, null);
				Quit.put(i, new ArrayList<String>());
				Out.put(i, new ArrayList<String>());
				Watching.put(i, new ArrayList<String>());
				NeedConfirm.put(i, new ArrayList<String>());
				inArena.put(i, new ArrayList<String>());
				Frozen.put(i, new ArrayList<String>());
				arena.put(i, new ArrayList<String>());
				canjoin.put(i, false);
				maxPlayers.put(i, location.get(i).size());
				open.put(i, true);
			}
		}
	}
	
	public WorldEditPlugin hookWE() {
		Plugin wPlugin = getServer().getPluginManager().getPlugin("WorldEdit");

    public void onDisable() {
        log.info("Disabled v" + getDescription().getVersion());
    }

    public static Main getInstance() {
        return instance;
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
        if (!spawnsFile.exists()) {          
            this.saveResource("spawns.yml", false);
        }	
		
		spawns = YamlConfiguration.loadConfiguration(spawnsFile);
		
		InputStream defConfigStream = this.getResource("spawns.yml");
		if (defConfigStream != null) {
            YamlConfiguration defConfig = loadConfigStream(defConfigStream);
            if (defConfig != null){
            	spawns.addDefaults(defConfig); 
            	spawns.options().copyHeader(true);
            	spawns.options().copyDefaults(true);
            	saveSpawns() ;
            }
		}
		if (spawns.getString("Spawns_set")!=null && (spawns.getString("Spawns_set").equalsIgnoreCase("false") || spawns.getString("Spawns_set").equalsIgnoreCase("true"))) {
			String temp = spawns.getString("Spawns_set");
			spawns.set("Spawns_set", null);
			spawns.set("Spawns_set.0", temp);
			temp = spawns.getString("Spawn_coords");
			spawns.set("Spawn_coords", null);
			spawns.set("Spawn_coords.0", temp);
			if(spawns.getConfigurationSection("Spawns")!= null){
				Set<String> temp2 = spawns.getConfigurationSection("Spawns").getValues(false).keySet(); 
				for(String entry: temp2){
					if (spawns.getString("Spawns_set_"+entry)!=null) {
						spawns.set("Spawns_set."+entry, spawns.getString("Spawns_set_"+entry));
						spawns.set("Spawns_set_"+entry, null);
					}
					if (spawns.getString("Spawn_coords_"+entry)!=null) {
						spawns.set("Spawn_coords."+entry, spawns.getString("Spawn_coords_"+entry));
						spawns.set("Spawn_coords_"+entry, null);
					}
				}
			}
			saveSpawns() ;
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
        if (!dataFile.exists()) {          
            this.saveResource("Data.yml", false);
        }	

        data = YamlConfiguration.loadConfiguration(dataFile);

		InputStream defConfigStream = this.getResource("Data.yml");
		if (defConfigStream != null) {
            YamlConfiguration defConfig = loadConfigStream(defConfigStream);
            if (defConfig != null) {
            	data.addDefaults(defConfig);
            	data.options().copyHeader(true); 
            	data.options().copyDefaults(true); 
            	saveData();
            }
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
        if (!managementFile.exists()) {          
            this.saveResource("commandAndBlockManagement.yml", false);
        }	

		management = YamlConfiguration.loadConfiguration(managementFile);

		InputStream defConfigStream = this.getResource("commandAndBlockManagement.yml");
		if (defConfigStream != null) {
            YamlConfiguration defConfig = loadConfigStream(defConfigStream);
            if (defConfig != null) {
            	management.addDefaults(defConfig);
            	management.options().copyHeader(true); 
            	management.options().copyDefaults(true); 
            	saveManagement();
            }
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
			PFile = new File(PFilePath, pname + ".yml");
		}
		PConfig = YamlConfiguration.loadConfiguration(PFile);
		InputStream defConfigStream = this.getResource("Player.yml");
		if (defConfigStream != null) {
            YamlConfiguration defConfig = loadConfigStream(defConfigStream);
            if (defConfig != null) this.PConfig.setDefaults(defConfig);
		}
	}
	
    private YamlConfiguration loadConfigStream(InputStream defConfigStream) { 
    	Reader defaultConfigReader = null;
    	YamlConfiguration defConfig = null;
        if (defConfigStream != null) {
            try {
              defaultConfigReader = new java.io.InputStreamReader(defConfigStream, "UTF-8");
            } catch (UnsupportedEncodingException e) {
              log.info("The embedded resource contained in the plugin jar file has an unsupported encoding.It should be encoded with UTF-8.");
            }
          }
  
        if (defaultConfigReader != null) {
        	defConfig = YamlConfiguration.loadConfiguration(defaultConfigReader);
        } else {
            log.warning("A default resource in the plugin jar could not be read.");
        }
        try {
        	if (defaultConfigReader != null) {
        		defaultConfigReader.close();
        	}
        } catch (IOException e) {
        	log.warning("An error occured while trying to close the resource file.");
        }
        return defConfig;
    }

	public FileConfiguration getPConfig(String pname) {
		PFile = null; 	
		this.reloadPFile(pname);
		return PConfig;
	}
	public void savePFile(String pname) {
		if (PConfig.getString("player").equals(pname)){
			try {
				this.PConfig.save(PFile);
			} catch (IOException ex) {
				this.getLogger().log(Level.SEVERE, "Could not save config to " + PFile, ex);
			}
		}else this.getLogger().log(Level.SEVERE, "Could not save config to " + pname + ".yml ! It's not this players inventory!?");
	}
	
	@SuppressWarnings("unchecked")
	public void RestoreInv(Player p, String pname){
		for(int u:Playing.keySet()){ 
			if(Playing.get(u)!=null){
				if(Playing.get(u).contains(pname)){
					Playing.get(u).remove(pname);										
					p.sendMessage(ChatColor.AQUA + "You have left the game!");
					if(config.getBoolean("broadcastAll")){
						p.getServer().broadcastMessage(ChatColor.RED + pname + " Left Arena " + u + "!");
					}else{
						for(String gn: Playing.get(u)){
							Player g = getServer().getPlayer(gn);
							g.sendMessage(ChatColor.RED + pname + " Quit!");
						}
					}
					if(canjoin.get(u)== true){
						p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
						scoreboards.remove(p.getName());
						Kills.remove(p.getName());
					}
					
				}
			}
			if(Ready.get(u)!=null){
				if(Ready.get(u).contains(pname)) Ready.get(u).remove(pname);
			}
		}
		if(new File(PFilePath, pname + ".yml").exists()){
			FileConfiguration pinfo = this.getPConfig(pname);
			if((pinfo.getString("player").equals(pname)) && (this.needInv.contains(pname))){
				try{
					ItemStack[] pinv = null;
					Object o = pinfo.get("inv");
					if(o instanceof ItemStack[]){
						pinv = (ItemStack[]) o;
					}else if(o instanceof List){
						pinv = (ItemStack[]) ((List<ItemStack>) o).toArray(new ItemStack[0]);
					}
					p.getInventory().setContents(pinv);
					p.updateInventory();
					
					ItemStack[] parmor = null;
					 o = pinfo.get("armor");
					if(o instanceof ItemStack[]){
						parmor = (ItemStack[]) o;
					}else if(o instanceof List){
						parmor = (ItemStack[]) ((List<ItemStack>) o).toArray(new ItemStack[0]);
					}
					p.getInventory().setArmorContents(parmor);
					p.updateInventory();
					
					p.sendMessage(ChatColor.GOLD + "[HA] " + ChatColor.GREEN + "Your inventory has been restored!");
					new File(PFilePath, pname + ".yml").delete();

					while(needInv.contains(pname)){ 
						needInv.remove(pname);
					}

				}catch(Exception e){
					p.sendMessage(ChatColor.RED + "Something went wrong when trying to restore your inv, please contact an administrator.");
					this.getLogger().warning("Error occured when trying to restore the inv of " + pname + ":");
					System.out.println(e);
				}
			}
		}
		if (p.hasMetadata("HA-Location")) p.removeMetadata("HA-Location", this);
	}
	
	public void winner(final Integer a){
		if(Playing.get(a).size()== 1){
			String[] Spawncoords;
			if (spawns.getString("Spawn_coords." + a) != null){
				Spawncoords = spawns.getString("Spawn_coords."+ a).split(",");	
			} else {
				Spawncoords = spawns.getString("Spawn_coords.0").split(",");	
			}
			World spawnw = getServer().getWorld(Spawncoords[3]);
			double spawnx = Double.parseDouble(Spawncoords[0]);
			double spawny = Double.parseDouble(Spawncoords[1]);
			double spawnz = Double.parseDouble(Spawncoords[2]);
			Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
			
				for(i = 0; i < Playing.get(a).size(); i++){
					String winnername = Playing.get(a).get(i);
					final Player winner = getServer().getPlayerExact(winnername);
					String winnername2 = winner.getName();
					if(canjoin.get(a)== true) getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
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
					
					winner.teleport(Spawn);
					
					this.RestoreInv(winner, winnername2);

					if(canjoin.get(a)== true){
					 winner.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
					 if(scoreboards.containsKey(winner.getName()))
						scoreboards.remove(winner.getName());
					 if(Kills.containsKey(winner.getName()))
						Kills.remove(winner.getName());

					////////////////////////////////////////////////////////
					////////////////////  FIREWORKS  ///////////////////////
					////////////////////////////////////////////////////////

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

					 for(i = 0; i < 10; i++){
						Bukkit.getScheduler().runTaskLater(this, new Runnable(){
							public void run(){
								//Spawn the Fireworks, get the FireworkMeta.
								Firework fw = (Firework) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
								FireworkMeta fwm = fw.getFireworkMeta();

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

								//Then apply this to our rocket
								fw.setFireworkMeta(fwm);
							}
						},20 + i*20L);
					 }

    public void reloadChests() {
        if (ChestsFile == null) {
            ChestsFile = new File(getDataFolder(), "Chests.yml");
        }
        MyChests = YamlConfiguration.loadConfiguration(ChestsFile);
    }

					////////////////////////////////////////////////////////
					////////////////////////////////////////////////////////
					////////////////////////////////////////////////////////
				
					 if(!config.getBoolean("rewardEco.enabled")){
						for(ItemStack Rewards: Reward){
							winner.getInventory().addItem(Rewards);
						}
					 }else{
						for(ItemStack Rewards: Reward){
							winner.getInventory().addItem(Rewards);
						}
						econ.depositPlayer(winner, config.getDouble("rewardEco.reward"));
					 }
					}
					
					if(deathtime.get(a)!= null){
						getServer().getScheduler().cancelTask(deathtime.get(a));
						deathtime.put(a, null);
					}
					if(grace.get(a)!= null){
						getServer().getScheduler().cancelTask(grace.get(a));
						grace.put(a, null);
					}
					if(start.get(a)!= null){
						getServer().getScheduler().cancelTask(start.get(a));
						start.put(a, null);
					}
				}
				Playing.get(a).clear();
				Quit.get(a).clear();
				Dead.get(a).clear();
				
				//Show spectators
				for(String s1: Watching.get(a)){
					Player spectator = getServer().getPlayerExact(s1);
					spectator.setAllowFlight(false);
					spectator.teleport(Spawn);
					for(Player online:getServer().getOnlinePlayers()){
						online.showPlayer(this,spectator);
					}
				}
				if(config.getString("Auto_Restart").equalsIgnoreCase("True")){
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
						public void run(){
							Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart " + a);
						}
					}, 220L);
				}
				MatchRunning.put(a, null);
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
				Collection<? extends Player> online = getServer().getOnlinePlayers();
				for(Player pl: online) {
					updateScoreboard(pl);
				}
			}
		}, 20L, 10L);
	}
	
	public void updateScoreboard(Player p){ 
		if(getArena(p)!= null || isSpectating(p)){ 
			if (getArena(p) !=null) a = getArena(p);
			else if (getSpectating(p) !=null) a = getSpectating(p);
			if(scoreboards.get(p.getName())!= null && scoreboards.get(p.getName()).getObjective("HA")!= null){
				Scoreboard sb = scoreboards.get(p.getName());
				Objective obj = sb.getObjective("HA");
				if(obj!= null){
					Score kills = obj.getScore(ChatColor.RED + "Kills");
					Score players = obj.getScore(ChatColor.RED + "Players");
					Score spectators = obj.getScore(ChatColor.RED + "Spectators");
					Score allkills = obj.getScore(ChatColor.RED + "Deaths");
					players.setScore(Playing.get(a).size());
					if(Kills.containsKey(p.getName())) {
						kills.setScore(Kills.get(p.getName()));
					} 
					if (Kills.containsKey("__SuM__")) 
						allkills.setScore(Kills.get("__SuM__"));					

					if(Watching.get(a)!= null)
						spectators.setScore(Watching.get(a).size());
					if(config.getInt("DeathMatch")!= 0){ 
						if(timetodeath.get(a)!= null){
							if(timetodeath.get(a)> 0){
								int ttd = Integer.valueOf(timetodeath.get(a)-timetodeath.get(a)/60*60);
								String secs = String.valueOf((ttd< 10) ? "0" + ttd : ttd);
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

	public HashMap<Integer, Integer> grace = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> start = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> deathtime = new HashMap<Integer, Integer>();
	public HashMap<Integer, Integer> timetodeath = new HashMap<Integer, Integer>();
	
	public void startGames(final int a){
		if ((MatchRunning.get(a)!=null) && (MatchRunning.get(a).equals("true"))){
			return; 
		}
		
		final String msg = ChatColor.translateAlternateColorCodes('&', config.getString("Start_Message"));
		for(String gn: Playing.get(a)){
			Scoreboard scoreboard = getServer().getScoreboardManager().getNewScoreboard();
			Objective sobj;
			try{
				sobj = scoreboard.registerNewObjective("HA", "HAData", ChatColor.GREEN + "HA - Starting");
			} catch (NoSuchMethodError e){
				sobj = scoreboard.registerNewObjective("HA", "HAData");
				sobj.setDisplayName(ChatColor.GREEN + "HA - Starting");
			}
			Score skills = sobj.getScore(ChatColor.RED + "Kills");
			skills.setScore(0);
			Score sdeaths = sobj.getScore(ChatColor.RED + "Spectators");
			sdeaths.setScore(0);
			Score splayers = sobj.getScore(ChatColor.RED + "Players");
			splayers.setScore(0);
			Score allkills = sobj.getScore(ChatColor.RED + "Deaths");
			allkills.setScore(0);
			sobj.setDisplaySlot(DisplaySlot.SIDEBAR);
			Bukkit.getPlayer(gn).setScoreboard(scoreboard);
			scoreboards.put(Bukkit.getPlayer(gn).getName(), Bukkit.getPlayer(gn).getScoreboard());
		}
		getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha Refill " + a);
		MatchRunning.put(a, "true");
		if(start.get(a)!= null) getServer().getScheduler().cancelTask(start.get(a));
		if(config.getString("Countdown").equalsIgnoreCase("true")){
			CountT.put(a,(config.getInt("Countdown_Timer")!=0 ? config.getInt("Countdown_Timer"):10));
			start.put(a, getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
				public void run(){
					if(CountT.get(a) > 0){
						if (!restricted){
							if(config.getBoolean("broadcastAll")){
								getServer().broadcastMessage(ChatColor.AQUA + "Game " + a + " starting in: " + String.valueOf(CountT.get(a)));
							}else{
								for(String gn: Playing.get(a)){
									Player g = getServer().getPlayer(gn);
									g.sendMessage(ChatColor.AQUA + "Game starting in: " + String.valueOf(CountT.get(a)));
								}
							}
						}else{
							if(config.getBoolean("broadcastAll")){
								for(String world: worldsNames.values()){
									World w = getServer().getWorld(world);
									for(Player wp: w.getPlayers()){
										wp.sendMessage(String.valueOf(CountT.get(a)));
									}
								}
							}else{
								for(String gn: Playing.get(a)){
									Player g = getServer().getPlayer(gn);
									g.sendMessage(String.valueOf(CountT.get(a)));
								}
							}
						}
					}
					CountT.put(a, CountT.get(a)-1);
					if(CountT.get(a)== -1){
						for(String gn: Playing.get(a)){
							Scoreboard scoreboard = getServer().getScoreboardManager().getNewScoreboard();
							Objective sobj;
							try{
								sobj = scoreboard.registerNewObjective("HA", "HAData", ChatColor.GREEN + "HA - Starting");
							} catch (NoSuchMethodError e){
								sobj = scoreboard.registerNewObjective("HA", "HAData");
								sobj.setDisplayName(ChatColor.GREEN + "HA - Starting");
							}
							Score skills = sobj.getScore(ChatColor.RED + "Kills");
							skills.setScore(0);
							Score sdeaths = sobj.getScore(ChatColor.RED + "Spectators");
							sdeaths.setScore(0);
							Score splayers = sobj.getScore(ChatColor.RED + "Players");
							splayers.setScore(0);
							Score allkills = sobj.getScore(ChatColor.RED + "Deaths");
							allkills.setScore(0);
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
							if(grace.get(a)== null) grace.put(a, getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("HungerArena"), new Runnable(){
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
									if(gp.get(a) <= 0){
										if(config.getBoolean("broadcastAll")){
											for(Player wp: location.get(a).get(1).getWorld().getPlayers()){
												wp.sendMessage(ChatColor.GREEN + "Grace period is over, FIGHT!");
											}
										}else
											getServer().broadcastMessage(ChatColor.GREEN + "Grace period is over, FIGHT!");
										getServer().getScheduler().cancelTask(grace.get(a));
										grace.put(a, null);
									}
								}
							},20L, 20L));
						}
						if(config.getInt("DeathMatch")!= 0){
							int death = config.getInt("DeathMatch");
							timetodeath.put(a, death*60);
							if(deathtime.get(a)== null) deathtime.put(a, getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("HungerArena"), new Runnable(){
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
									if(timetodeath.get(a)<= 0){
										int i = 1;
										for(String playing: Playing.get(a)){
											Player tribute = getServer().getPlayerExact(playing);
											
											Location pLoc=null;
											if (tribute.hasMetadata("HA-Location")){
												List<MetadataValue> l=tribute.getMetadata("HA-Location");
												if (l !=null && l.size()!=0 ){
													try {
														pLoc=(Location) l.get(0).value();
													} catch (Exception e){}
												}
												if (pLoc!=null) {
													tribute.teleport(pLoc); 					//random
												} else {
													tribute.teleport(location.get(a).get(i));	//row
												}
											}
											i+=1;
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
										StopTasksDelayed(deathtime.get(a));
										deathtime.put(a, null);
									}
								}
							}, 20L, 20L));
						}
						setTorch(a, true);
						StopTasksDelayed(start.get(a)); 
					}
				}
			}, 20L, 20L));
		}else{
			setTorch(a, true);
			Frozen.get(a).clear();
			if(config.getBoolean("broadcastAll")){
				getServer().broadcastMessage(msg);
			}else{
				for(String gn: Playing.get(a)){
					Player g = getServer().getPlayer(gn);
					g.sendMessage(msg);
				}
			}
		}
		canjoin.put(a, true);
	}
	private void StopTasksDelayed(final int task) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
			public void run() {
				Bukkit.getScheduler().cancelTask(task);
			}
		}, 10L);
	}
	
	public Integer getArena(Player p){
		for (int x: Playing.keySet()) {
			if (Playing.get(x).contains(p.getName())){
				return x;
			}
		}
		return null;
	}
	public Integer getSpectating(Player p){
			for(int x : Watching.keySet()){
				if(Watching.get(x).contains(p.getName())){
					return x;
				}
			}
		return null;
	}
	public boolean isSpectating(Player p){
			for(int x : Watching.keySet()){
				if(Watching.get(x).contains(p.getName())){
					return true;
				}
			}
		return false;
	}
	public void setTorch(int a, boolean set){
		String arena = String.valueOf(a);
		if (spawns.getString("Start_torch."+arena)!=null) {
			String[] Torchcoords = spawns.getString("Start_torch."+ arena).split(",");	
			double torchx = Double.parseDouble(Torchcoords[0]);
			double torchy = Double.parseDouble(Torchcoords[1]);
			double torchz = Double.parseDouble(Torchcoords[2]);
			String torchworld = Torchcoords[3];
			World torchw = getServer().getWorld(torchworld);
			Location TorchLoc = new Location(torchw, torchx, torchy, torchz);
			if (set) SetTorch(TorchLoc);
			else TorchLoc.getBlock().setType(Material.AIR);
		}
	}
	   private void SetTorch(Location Baseblock){
		   Block TorchBlock = Baseblock.getBlock();
		   try{
			   Material Torch = (org.bukkit.Material.valueOf("REDSTONE_TORCH"));
			   if (Torch != null) {
				   TorchBlock.setType(Torch);
			   }
		   }catch (Exception ex) {
			   TorchBlock.setType(Material.REDSTONE_TORCH);
		   }
	   }
}
