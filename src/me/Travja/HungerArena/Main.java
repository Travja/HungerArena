package me.Travja.HungerArena;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	Logger log;
	public ArrayList<Player> Playing = new ArrayList<Player>();
	public ArrayList<Player> Ready = new ArrayList<Player>();
	public ArrayList<Player> Dead = new ArrayList<Player>();
	public ArrayList<Player> Quit = new ArrayList<Player>();
	public ArrayList<Player> Out = new ArrayList<Player>();
	public ArrayList<Player> Watching = new ArrayList<Player>();
	public ArrayList<Player> NeedConfirm = new ArrayList<Player>();
	public HashSet<Player> Frozen = new HashSet<Player>();
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
		getServer().getPluginManager().registerEvents(new DeathListener(this), this);
		Reward = new ItemStack(config.getInt("Reward.ID"), config.getInt("Reward.Amount"));
		Cost = new ItemStack(config.getInt("Sponsor_Cost.ID"), config.getInt("Sponsor_Cost.Amount"));
	}
	public void onDisable(){
		log = this.getLogger();
		log.info("HungerArena has been Disabled");
	}
	@SuppressWarnings("unchecked")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player p = (Player) sender;
		String pname = p.getName();
		if(cmd.getName().equalsIgnoreCase("Sponsor")){
			Player target = Bukkit.getPlayer(args[0]);
			if(!Playing.contains(p)){
				if(args.length== 0){
					p.sendMessage(ChatColor.RED + "You didn't specify a tribute!");
					return false;
				}
				if(args.length== 1){
					p.sendMessage(ChatColor.RED + "You didn't specify an item!");
				}
				if(args.length== 2){
					p.sendMessage(ChatColor.RED + "You didn't specify an amount!");
				}
				if(args.length== 3){
					if(args[1].equalsIgnoreCase("57") || args[1].equalsIgnoreCase("7")){
						p.sendMessage(ChatColor.RED + "You can't sponsor that item!");
					}else{
						int ID = Integer.parseInt(args[1]);
						int Amount = Integer.parseInt(args[2]);
						Cost = new ItemStack(config.getInt("Sponsor_Cost.ID"), config.getInt("Sponsor_Cost.Amount")*Amount);
						ItemStack sponsoritem = new ItemStack(ID, Amount);
						if(p.getInventory().contains(Cost)){
							if(!Playing.contains(target)){
								p.sendMessage(ChatColor.RED + "That person isn't playing!");
							}else{
								if(args[0].equalsIgnoreCase(pname)){
									p.sendMessage(ChatColor.RED + "You can't sponsor yourself!");
								}else{
									target.sendMessage(ChatColor.AQUA + "You have been Sponsored!");
									target.getInventory().addItem(sponsoritem);
									p.sendMessage("You have sponsored " + target.getName() + "!");
									p.getInventory().removeItem(Cost);
								}
							}
						}else{
							p.sendMessage(ChatColor.RED + "You don't have the necessary items to sponsor!");
						}
					}
				}
			}else{
				p.sendMessage(ChatColor.RED + "You are playing, you can't sponsor yourself!");
			}
		}
		if(cmd.getName().equalsIgnoreCase("Ha")){
			if(args.length== 0){
				p.sendMessage(ChatColor.GREEN + "[HungerArena] by " + ChatColor.AQUA + "travja!");
				return false;
			}
			if(args[0].equalsIgnoreCase("List")){
				if(p.hasPermission("HungerArena.GameMaker")){
					p.sendMessage(ChatColor.AQUA + "-----People Playing-----");
					for(Player players:Playing){
						p.sendMessage(ChatColor.GREEN + players.getDisplayName() + " Life: " + players.getHealth() + "/20");
					}
					if(Playing.size()== 0){
						p.sendMessage(ChatColor.GRAY + "No one is playing!");
					}
					p.sendMessage(ChatColor.AQUA + "----------------------");
				}else{
					p.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			}
			if(args[0].equalsIgnoreCase("SetSpawn")){
				if(p.hasPermission("HungerArena.SetSpawn")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Spawn_coords", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn for dead tributes!");
				}else{
					p.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			}
			String[] Spawncoords = config.getString("Spawn_coords").split(",");
			double spawnx = Double.parseDouble(Spawncoords[0]);
			double spawny = Double.parseDouble(Spawncoords[1]);
			double spawnz = Double.parseDouble(Spawncoords[2]);
			String spawnworld = Spawncoords[3];
			World spawnw = getServer().getWorld(spawnworld);
			Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
			if(args[0].equalsIgnoreCase("Join")){
				if(p.hasPermission("HungerArena.Join")){
					if(Playing.contains(p)){
						p.sendMessage(ChatColor.RED + "You are already playing!");
					}else if(Dead.contains(p) || Quit.contains(p)){
						p.sendMessage(ChatColor.RED + "You DIED/QUIT! You can't join again!");
					}else if(Playing.size()== 24){
						p.sendMessage(ChatColor.RED + "There are already 24 Tributes!");
					}else if(canjoin== true){
						p.sendMessage(ChatColor.RED + "The game is in progress!");
					}else{
						NeedConfirm.add(p);
						p.sendMessage(ChatColor.GOLD + "You're inventory will be cleared! Type /ha confirm to procede");
					}
				}else{
					p.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			}
			if(args[0].equalsIgnoreCase("Confirm") && NeedConfirm.contains(p)){
				Playing.add(p);
				NeedConfirm.remove(p);
				p.getInventory().clear();
				p.getInventory().setBoots(null);
				p.getInventory().setChestplate(null);
				p.getInventory().setHelmet(null);
				p.getInventory().setLeggings(null);
				getServer().broadcastMessage(ChatColor.AQUA + pname +  " has Joined the Game!");
			}
			if(args[0].equalsIgnoreCase("Ready")){
				if(Playing.contains(p)){
					Ready.add(p);
					p.sendMessage(ChatColor.AQUA + "You have marked yourself as READY!");
					if(Playing.size()== Ready.size()){
						p.performCommand("Ha Warpall");
					}
				}else if(!Playing.contains(p)){
					p.sendMessage(ChatColor.RED + "You aren't playing!");
				}
			}
			if(args[0].equalsIgnoreCase("Leave")){
				if(!Playing.contains(p)){
					p.sendMessage(ChatColor.RED + "You aren't playing!");
				}else{
					Playing.remove(p);
					Quit.add(p);
					p.sendMessage(ChatColor.AQUA + "You have left the game!");
					p.getServer().broadcastMessage(ChatColor.RED + pname + " Quit!");
					p.getInventory().clear();
					p.getInventory().setBoots(null);
					p.getInventory().setChestplate(null);
					p.getInventory().setHelmet(null);
					p.getInventory().setLeggings(null);
					p.teleport(Spawn);
					if(Frozen.contains(p)){
						Frozen.remove(p);
					}
					if(Playing.size()== 1 && canjoin== true){
						for(Player winner:Playing){
							String winnername = winner.getName();
							p.getServer().broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.getInventory().addItem(Reward);
						}
						for(Player spectator:Watching){
							spectator.setAllowFlight(false);
						}
						canjoin= false;
						Watching.clear();
					}
				}
			}
			if(args[0].equalsIgnoreCase("Watch")){
				if(p.hasPermission("HungerArena.Watch")){
					if(!Watching.contains(p) && !Playing.contains(p) && canjoin== true){
						Watching.add(p);
						for(Player online:getServer().getOnlinePlayers()){
							online.hidePlayer(p);
						}
						p.setAllowFlight(true);
						p.sendMessage(ChatColor.AQUA + "You can now spectate!");
					}else if(canjoin == false){
						p.sendMessage(ChatColor.RED + "The game isn't in progress!");
					}else if(Playing.contains(p)){
						p.sendMessage(ChatColor.RED + "You can't watch while you're playing!");
					}else if(Watching.contains(p)){
						Watching.remove(p);
						for(Player online:getServer().getOnlinePlayers()){
							online.showPlayer(p);
						}
						p.teleport(Spawn);
						p.setAllowFlight(false);
						p.sendMessage(ChatColor.AQUA + "You are not spectating anymore");
					}
				}else{
					p.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			}
			if(args[0].equalsIgnoreCase("Kick")){
				Player target = getServer().getPlayer(args[1]);
				if(p.hasPermission("HungerArena.Kick")){
					if(Playing.contains(target)){
						if(p.isOnline()){
							Playing.remove(target);
							getServer().broadcastMessage(ChatColor.RED + target.getName() + " was kicked from the game!");
							target.teleport(Spawn);
							target.getInventory().clear();
							target.getInventory().setBoots(null);
							target.getInventory().setChestplate(null);
							target.getInventory().setHelmet(null);
							target.getInventory().setLeggings(null);
							Quit.add(target);
							if(Playing.size()== 1 && canjoin== true){
								for(Player winner:Playing){
									String winnername = winner.getName();
									p.getServer().broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
									winner.getInventory().clear();
									winner.getInventory().addItem(Reward);
								}
								for(Player spectator:Watching){
									spectator.setAllowFlight(false);
								}
								canjoin= false;
								Watching.clear();
							}else{
								Playing.remove(target);
								Quit.add(target);
							}
						}
					}else{
						p.sendMessage(ChatColor.RED + "That player isn't in the game!");
					}
				}else{
					p.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			}
			if(args[0].equalsIgnoreCase("Refill")){
				int list056;
				list056 = 0;
				int limit;
				limit = getConfig().getStringList("StorageXYZ").size();
				while(limit > list056){
					String xyz2 = getConfig().getStringList("StorageXYZ").get(list056);
					int chestx = getConfig().getInt("Storage." + xyz2 + ".Location.X");
					int chesty = getConfig().getInt("Storage." + xyz2 + ".Location.Y");
					int chestz = getConfig().getInt("Storage." + xyz2 + ".Location.Z");
					String chestw = getConfig().getString("Storage." + xyz2 + ".Location.W");
					Block blockatlocation = Bukkit.getWorld(chestw).getBlockAt(chestx, chesty, chestz);  
					exists = false;
					if(blockatlocation.getState() instanceof Chest){
						exists = true;
						Chest chest = (Chest) blockatlocation.getState();
						chest.getInventory().clear();
						ItemStack[] itemsinchest = null;
						Object o = getConfig().get("Storage." + xyz2 + ".ItemsInStorage");
						if(o instanceof ItemStack[]){
							itemsinchest = (ItemStack[]) o;
						}else if(o instanceof List){
							itemsinchest = (ItemStack[]) ((List<ItemStack>) o).toArray(new ItemStack[0]);
						}else{
							try{
								throw new Exception();
							}catch (Exception e) {
								e.printStackTrace();
							}
							chest.getInventory().setContents(itemsinchest);
						}
					}
				}
			}
			if(args[0].equalsIgnoreCase("Restart")){
				if(p.hasPermission("HungerArena.Restart")){
					for(Player spectator:Watching){
						spectator.setAllowFlight(false);
					}
					Dead.clear();
					Playing.clear();
					Quit.clear();
					Watching.clear();
					Frozen.clear();
					canjoin = false;
					p.sendMessage(ChatColor.AQUA + "The games have been reset!");
				}else{
					p.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			}
			if(args[0].equalsIgnoreCase("Reload")){
				reloadConfig();
				p.sendMessage(ChatColor.AQUA + "HungerArena Reloaded!");
			}
			if(args[0].equalsIgnoreCase("WarpAll")){
				if(p.hasPermission("HungerArena.Warpall")){
					if(Playing.size()== 1){
						p.sendMessage(ChatColor.RED + "There are not enough players!");
					}
					if(Playing.size()>= 2){
						config.getString("Tribute_one_spawn");
						String[] onecoords = config.getString("Tribute_one_spawn").split(",");
						Player Tribute_one = Playing.get(0);
						double x = Double.parseDouble(onecoords[0]);
						double y = Double.parseDouble(onecoords[1]);
						double z = Double.parseDouble(onecoords[2]);
						String world = onecoords[3];
						World w = getServer().getWorld(world);
						Location oneloc = new Location(w, x, y, z);
						Tribute_one.teleport(oneloc);
						Frozen.add(Tribute_one);
						Tribute_one.setFoodLevel(20);
						config.getString("Tribute_two_spawn");
						String[] twocoords = config.getString("Tribute_two_spawn").split(",");
						Player Tribute_two = Playing.get(1);
						double twox = Double.parseDouble(twocoords[0]);
						double twoy = Double.parseDouble(twocoords[1]);
						double twoz = Double.parseDouble(twocoords[2]);
						String twoworld = twocoords[3];
						World twow = getServer().getWorld(twoworld);
						Location twoloc = new Location(twow, twox, twoy, twoz);
						Tribute_two.teleport(twoloc);
						Frozen.add(Tribute_two);
						Tribute_two.setFoodLevel(20);
						p.getWorld().setTime(0);
					}
					if(Playing.size()>= 3){
						config.getString("Tribute_three_spawn");
						String[] coords = config.getString("Tribute_three_spawn").split(",");
						Player Tribute_three = Playing.get(2);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_three.teleport(loc);
						Frozen.add(Tribute_three);
						Tribute_three.setFoodLevel(20);
					}
					if(Playing.size()>= 4){
						config.getString("Tribute_four_spawn");
						String[] coords = config.getString("Tribute_four_spawn").split(",");
						Player Tribute_four = Playing.get(3);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_four.teleport(loc);
						Frozen.add(Tribute_four);
						Tribute_four.setFoodLevel(20);
					}
					if(Playing.size()>= 5){
						config.getString("Tribute_five_spawn");
						String[] coords = config.getString("Tribute_five_spawn").split(",");
						Player Tribute_five = Playing.get(4);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_five.teleport(loc);
						Frozen.add(Tribute_five);
						Tribute_five.setFoodLevel(20);
					}
					if(Playing.size()>= 6){
						config.getString("Tribute_six_spawn");
						String[] coords = config.getString("Tribute_six_spawn").split(",");
						Player Tribute_six = Playing.get(5);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_six.teleport(loc);
						Frozen.add(Tribute_six);
						Tribute_six.setFoodLevel(20);
					}
					if(Playing.size()>= 7){
						config.getString("Tribute_seven_spawn");
						String[] coords = config.getString("Tribute_seven_spawn").split(",");
						Player Tribute_seven = Playing.get(6);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_seven.teleport(loc);
						Frozen.add(Tribute_seven);
						Tribute_seven.setFoodLevel(20);
					}
					if(Playing.size()>= 8){
						config.getString("Tribute_eight_spawn");
						String[] coords = config.getString("Tribute_eight_spawn").split(",");
						Player Tribute_eight = Playing.get(7);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_eight.teleport(loc);
						Frozen.add(Tribute_eight);
						Tribute_eight.setFoodLevel(20);
					}
					if(Playing.size()>= 9){
						config.getString("Tribute_nine_spawn");
						String[] coords = config.getString("Tribute_nine_spawn").split(",");
						Player Tribute_nine = Playing.get(8);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_nine.teleport(loc);
						Frozen.add(Tribute_nine);
						Tribute_nine.setFoodLevel(20);
					}
					if(Playing.size()>= 10){
						config.getString("Tribute_ten_spawn");
						String[] coords = config.getString("Tribute_ten_spawn").split(",");
						Player Tribute_ten = Playing.get(9);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_ten.teleport(loc);
						Frozen.add(Tribute_ten);
						Tribute_ten.setFoodLevel(20);
					}
					if(Playing.size()>= 11){
						config.getString("Tribute_eleven_spawn");
						String[] coords = config.getString("Tribute_eleven_spawn").split(",");
						Player Tribute_eleven = Playing.get(10);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_eleven.teleport(loc);
						Frozen.add(Tribute_eleven);
						Tribute_eleven.setFoodLevel(20);
					}
					if(Playing.size()>= 12){
						config.getString("Tribute_twelve_spawn");
						String[] coords = config.getString("Tribute_twelve_spawn").split(",");
						Player Tribute_twelve = Playing.get(11);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_twelve.teleport(loc);
						Frozen.add(Tribute_twelve);
						Tribute_twelve.setFoodLevel(20);
					}
					if(Playing.size()>= 13){
						config.getString("Tribute_thirteen_spawn");
						String[] coords = config.getString("Tribute_thirteen_spawn").split(",");
						Player Tribute_thirteen = Playing.get(12);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_thirteen.teleport(loc);
						Frozen.add(Tribute_thirteen);
						Tribute_thirteen.setFoodLevel(20);
					}
					if(Playing.size()>= 14){
						config.getString("Tribute_fourteen_spawn");
						String[] coords = config.getString("Tribute_fourteen_spawn").split(",");
						Player Tribute_fourteen = Playing.get(13);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_fourteen.teleport(loc);
						Frozen.add(Tribute_fourteen);
						Tribute_fourteen.setFoodLevel(20);
					}
					if(Playing.size()>= 15){
						config.getString("Tribute_fifteen_spawn");
						String[] coords = config.getString("Tribute_fifteen_spawn").split(",");
						Player Tribute_fifteen = Playing.get(14);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_fifteen.teleport(loc);
						Frozen.add(Tribute_fifteen);
						Tribute_fifteen.setFoodLevel(20);
					}
					if(Playing.size()>= 16){
						config.getString("Tribute_sixteen_spawn");
						String[] coords = config.getString("Tribute_sixteen_spawn").split(",");
						Player Tribute_sixteen = Playing.get(15);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_sixteen.teleport(loc);
						Frozen.add(Tribute_sixteen);
						Tribute_sixteen.setFoodLevel(20);
					}
					if(Playing.size()>= 17){
						config.getString("Tribute_seventeen_spawn");
						String[] coords = config.getString("Tribute_seventeen_spawn").split(",");
						Player Tribute_seventeen = Playing.get(16);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_seventeen.teleport(loc);
						Frozen.add(Tribute_seventeen);
						Tribute_seventeen.setFoodLevel(20);
					}
					if(Playing.size()>= 18){
						config.getString("Tribute_eighteen_spawn");
						String[] coords = config.getString("Tribute_eighteen_spawn").split(",");
						Player Tribute_eighteen = Playing.get(17);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_eighteen.teleport(loc);
						Frozen.add(Tribute_eighteen);
						Tribute_eighteen.setFoodLevel(20);
					}
					if(Playing.size()>= 19){
						config.getString("Tribute_nineteen_spawn");
						String[] coords = config.getString("Tribute_nineteen_spawn").split(",");
						Player Tribute_nineteen = Playing.get(18);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_nineteen.teleport(loc);
						Frozen.add(Tribute_nineteen);
						Tribute_nineteen.setFoodLevel(20);
					}
					if(Playing.size()>= 20){
						config.getString("Tribute_twenty_spawn");
						String[] coords = config.getString("Tribute_twenty_spawn").split(",");
						Player Tribute_twenty = Playing.get(19);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_twenty.teleport(loc);
						Frozen.add(Tribute_twenty);
						Tribute_twenty.setFoodLevel(20);
					}
					if(Playing.size()>= 21){
						config.getString("Tribute_twentyone_spawn");
						String[] coords = config.getString("Tribute_twentyone_spawn").split(",");
						Player Tribute_twentyone = Playing.get(20);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_twentyone.teleport(loc);
						Frozen.add(Tribute_twentyone);
						Tribute_twentyone.setFoodLevel(20);
					}
					if(Playing.size()>= 22){
						config.getString("Tribute_twentytwo_spawn");
						String[] coords = config.getString("Tribute_twentytwo_spawn").split(",");
						Player Tribute_twentytwo = Playing.get(21);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_twentytwo.teleport(loc);
						Frozen.add(Tribute_twentytwo);
						Tribute_twentytwo.setFoodLevel(20);
					}
					if(Playing.size()>= 23){
						config.getString("Tribute_twentythree_spawn");
						String[] coords = config.getString("Tribute_twentythree_spawn").split(",");
						Player Tribute_twentythree = Playing.get(22);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_twentythree.teleport(loc);
						Frozen.add(Tribute_twentythree);
						Tribute_twentythree.setFoodLevel(20);
					}
					if(Playing.size()>= 24){
						config.getString("Tribute_twentyfour_spawn");
						String[] coords = config.getString("Tribute_twentyfour_spawn").split(",");
						Player Tribute_twentyfour = Playing.get(23);
						double x = Double.parseDouble(coords[0]);
						double y = Double.parseDouble(coords[1]);
						double z = Double.parseDouble(coords[2]);
						String world = coords[3];
						World w = getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_twentyfour.teleport(loc);
						Frozen.add(Tribute_twentyfour);
						Tribute_twentyfour.setFoodLevel(20);
					}
				}
			}
			if(args[0].equalsIgnoreCase("Start")){
				String begin = config.getString("Start_Message");
				begin = begin.replaceAll("(&([a-f0-9]))", "\u00A7$2");
				final String msg = begin;
				if(p.hasPermission("HungerArena.Start")){
					if(config.getString("Countdown").equalsIgnoreCase("true")){
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("10");
							}
						}, 20L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("9");
							}
						}, 40L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("8");
							}
						}, 60L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("7");
							}
						}, 80L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("6");
							}
						}, 100L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("5");
							}
						}, 120L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("4");
							}
						}, 140L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("3");
							}
						}, 160L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("2");
							}
						}, 180L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								getServer().broadcastMessage("1");
							}
						}, 200L);
						getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
							public void run(){
								Frozen.clear();
								getServer().broadcastMessage(msg);
								canjoin = true;
							}
						}, 220L);
					}else{
						Frozen.clear();
						p.getServer().broadcastMessage(ChatColor.AQUA + "Let the Games Begin!!");
						canjoin = true;
					}
				}else{
					p.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("StartPoint")){
			if(p.hasPermission("HungerArena.StartPoint")){	
				if(args[0].equalsIgnoreCase("1")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_one_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute one!");
				}
				if(args[0].equalsIgnoreCase("2")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_two_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute two!");
				}
				if(args[0].equalsIgnoreCase("3")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_three_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute three!");
				}
				if(args[0].equalsIgnoreCase("4")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_four_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute four!");
				}
				if(args[0].equalsIgnoreCase("5")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_five_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute five!");
				}
				if(args[0].equalsIgnoreCase("6")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_six_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute six!");
				}
				if(args[0].equalsIgnoreCase("7")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_seven_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute seven!");
				}
				if(args[0].equalsIgnoreCase("8")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_eight_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eight!");
				}
				if(args[0].equalsIgnoreCase("9")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_nine_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute nine!");
				}
				if(args[0].equalsIgnoreCase("10")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_ten_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute ten!");
				}
				if(args[0].equalsIgnoreCase("11")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_eleven_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eleven!");
				}
				if(args[0].equalsIgnoreCase("12")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_twelve_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twelve!");
				}
				if(args[0].equalsIgnoreCase("13")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_thirteen_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute thirteen!");
				}
				if(args[0].equalsIgnoreCase("14")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_fourteen_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute fourteen!");
				}
				if(args[0].equalsIgnoreCase("15")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_fifteen_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute fifteen!");
				}
				if(args[0].equalsIgnoreCase("16")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_sixteen_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute sixteen!");
				}
				if(args[0].equalsIgnoreCase("17")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_seventeen_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute seventeen!");
				}
				if(args[0].equalsIgnoreCase("18")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_eighteen_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eighteen!");
				}
				if(args[0].equalsIgnoreCase("19")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_nineteen_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute nineteen!");
				}
				if(args[0].equalsIgnoreCase("20")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_twenty_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twenty!");
				}
				if(args[0].equalsIgnoreCase("21")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_twentyone_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentyone!");
				}
				if(args[0].equalsIgnoreCase("22")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_twentytwo_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentytwo!");
				}
				if(args[0].equalsIgnoreCase("23")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_twentythree_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentythree!");
				}
				if(args[0].equalsIgnoreCase("24")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					config.set("Tribute_twentyfour_spawn", x + "," + y + "," + z + "," + w);
					saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentyfour!");
				}
			}else{
				p.sendMessage(ChatColor.RED + "You don't have permission!");
			}
		}
		return true;
	}
}
class DeathListener implements Listener{
	public Main plugin;
	public DeathListener(Main m){
		this.plugin = m;
	}
	public FileConfiguration config;
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		if(p.getWorld().getFullTime()== 18000){
			for(Player dead:plugin.Dead){
				p.getServer().broadcastMessage(ChatColor.GREEN + dead.getName() + " was killed today!");
			}
		}
		if(plugin.Frozen.contains(p) && plugin.config.getString("Frozen_Teleport").equalsIgnoreCase("True")){
			event.setCancelled(true);
		}
	}
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = event.getPlayer();
		if(plugin.Dead.contains(p)){
			String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
			World spawnw = p.getWorld();
			double spawnx = Double.parseDouble(Spawncoords[0]);
			double spawny = Double.parseDouble(Spawncoords[1]);
			double spawnz = Double.parseDouble(Spawncoords[2]);
			Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
			event.setRespawnLocation(Spawn);
		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		Server s = p.getServer();
		String pname = p.getName();
		if(plugin.Playing.contains(p)){
			if(plugin.config.getString("Cannon_Death").equalsIgnoreCase("True")){
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0, 300);
			}
			plugin.Dead.add(p);
			plugin.Playing.remove(p);
			String leftmsg = ChatColor.BLUE + "There are now " + plugin.Playing.size() + " tributes left!";
			if(p.getKiller() instanceof Player){
				if(p.getKiller().getItemInHand().getType().getId()== 0){
					Player killer = p.getKiller();
					String killername = killer.getName();
					event.setDeathMessage("");
					s.broadcastMessage(ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with their FIST!");
					s.broadcastMessage(leftmsg);
					if(plugin.Playing.size()== 1){
						for(Player winner:plugin.Playing){
							String winnername = winner.getName();
							s.broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.getInventory().setBoots(null);
							winner.getInventory().setChestplate(null);
							winner.getInventory().setHelmet(null);
							winner.getInventory().setLeggings(null);
							winner.getInventory().addItem(plugin.Reward);
						}
						for(Player spectator:plugin.Watching){
							spectator.setAllowFlight(false);
						}
						if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
							plugin.Dead.clear();
							plugin.Playing.clear();
							plugin.Quit.clear();
							plugin.Watching.clear();
							plugin.Frozen.clear();
							plugin.canjoin = false;
						}
					}
				}else{
					Player killer = p.getKiller();
					String killername = killer.getName();
					Material weapon = killer.getItemInHand().getType();
					String msg = ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with a(n) " + weapon;
					event.setDeathMessage("");
					s.broadcastMessage(msg);
					s.broadcastMessage(leftmsg);
					if(plugin.Playing.size()== 1){
						for(Player winner:plugin.Playing){
							String winnername = winner.getName();
							s.broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.getInventory().setBoots(null);
							winner.getInventory().setChestplate(null);
							winner.getInventory().setHelmet(null);
							winner.getInventory().setLeggings(null);
							winner.getInventory().addItem(plugin.Reward);
						}
						for(Player spectator:plugin.Watching){
							spectator.setAllowFlight(false);
						}
						if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
							plugin.Dead.clear();
							plugin.Playing.clear();
							plugin.Quit.clear();
							plugin.Watching.clear();
							plugin.Frozen.clear();
							plugin.canjoin = false;
						}
					}
				}
			}else{
				event.setDeathMessage("");
				s.broadcastMessage(ChatColor.LIGHT_PURPLE + pname + " died of natural causes!");
				s.broadcastMessage(leftmsg);
				if(plugin.Playing.size()== 1){
					for(Player winner:plugin.Playing){
						String winnername = winner.getName();
						s.broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
						winner.getInventory().clear();
						winner.getInventory().setBoots(null);
						winner.getInventory().setChestplate(null);
						winner.getInventory().setHelmet(null);
						winner.getInventory().setLeggings(null);
						winner.getInventory().addItem(plugin.Reward);
					}
					for(Player spectator:plugin.Watching){
						spectator.setAllowFlight(false);
					}
					if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
						plugin.Dead.clear();
						plugin.Playing.clear();
						plugin.Quit.clear();
						plugin.Watching.clear();
						plugin.Frozen.clear();
						plugin.canjoin = false;
					}
				}
			}
		}
		if(plugin.Watching.contains(p)){
			for(Player online:plugin.getServer().getOnlinePlayers())
				online.showPlayer(p);
		}
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		final Player player = event.getPlayer();
		if(plugin.Out.contains(p)){
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					player.sendMessage(ChatColor.AQUA + "You have saved yourself from being ejected from the arena!");
				}
			}, 40L);
			plugin.Out.remove(p);
			plugin.Playing.add(p);
		}
		if(plugin.Quit.contains(p) || plugin.Dead.contains(p)){
			String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
			String w = Spawncoords[3];
			World spawnw = plugin.getServer().getWorld(w);
			double spawnx = Double.parseDouble(Spawncoords[0]);
			double spawny = Double.parseDouble(Spawncoords[1]);
			double spawnz = Double.parseDouble(Spawncoords[2]);
			final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					player.teleport(Spawn);
					player.sendMessage(ChatColor.RED + "You have been teleported to spawn because you quit/forfieted!");
				}
			}, 40L);
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		final Player p = event.getPlayer();
		if(plugin.Playing.contains(p) && plugin.canjoin== true){
			plugin.Out.add(p);
		}
		if(plugin.Playing.contains(p) && plugin.canjoin== false){
			plugin.Out.add(p);
			plugin.Playing.remove(p);
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				if(plugin.Playing.contains(p) && plugin.canjoin== true && plugin.Out.contains(p)){
					plugin.Playing.remove(p);
					plugin.Quit.add(p);
					plugin.Out.remove(p);
					if(plugin.Playing.size()== 1){
						for(Player winner:plugin.Playing){
							String winnername = winner.getName();
							p.getServer().broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.getInventory().setBoots(null);
							winner.getInventory().setChestplate(null);
							winner.getInventory().setHelmet(null);
							winner.getInventory().setLeggings(null);
							winner.getInventory().addItem(plugin.Reward);
						}
						for(Player spectator:plugin.Watching){
							spectator.setAllowFlight(false);
						}
						if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
							plugin.Dead.clear();
							plugin.Playing.clear();
							plugin.Quit.clear();
							plugin.Watching.clear();
							plugin.Frozen.clear();
							plugin.canjoin = false;
						}
					}
				}
			}
		}, 1200L);
	}
	@EventHandler
	public void BreakBlock(BlockBreakEvent event){
		Player p = event.getPlayer();
		if(plugin.Playing.contains(p)){
			if(plugin.config.getString("Protected_Arena").equalsIgnoreCase("True")){
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You can't break blocks when you're playing!");
			}
		}
		if(plugin.Watching.contains(p)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void Chat(PlayerChatEvent event){
		Player p = event.getPlayer();
		if(plugin.Playing.contains(p)){
			String msg = "<" + ChatColor.RED + "[Tribute] " + ChatColor.WHITE + p.getName() + ">" + " " + event.getMessage();
			if(plugin.config.getString("ChatClose").equalsIgnoreCase("True")){
				double radius = plugin.config.getDouble("ChatClose_Radius");
				List<Entity> near = p.getNearbyEntities(radius, radius, radius);
				p.sendMessage(msg);
				for(Entity e:near){
					if(e instanceof Player){
						((Player) e).sendMessage(msg);
					}else if(e instanceof Entity || !(e instanceof Player)){
						p.sendMessage("Why are you talking to an animal?");
					}else if(near.size()== 0){
						p.sendMessage("No one is near");
					}
				}
				event.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void PvP(EntityDamageByEntityEvent event){
		Entity p = event.getEntity();
		Entity offense = event.getDamager();
		if(p instanceof Player){
			if(plugin.Playing.contains(p) && plugin.canjoin== false){
				event.setCancelled(true);
			}
		}
		if(offense instanceof Player){
			Player Attacker = (Player) event.getDamager();
			if(plugin.Watching.contains(Attacker)){
				event.setCancelled(true);
				Attacker.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
			}
		}else if(event.getDamager() instanceof Projectile){
			Projectile arrow = (Projectile) offense;
			if(arrow.getShooter() instanceof Player){
				Player BowMan = (Player) arrow.getShooter();
				if(plugin.Watching.contains(BowMan)){
					event.setCancelled(true);
					BowMan.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}
	@EventHandler
	public void Drops(PlayerDropItemEvent event){
		Player p = event.getPlayer();
		if(plugin.Watching.contains(p)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void Interactions(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(plugin.Watching.contains(p)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void SpectatorItems(PlayerPickupItemEvent event){
		Player p = event.getPlayer();
		if(plugin.Watching.contains(p)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void ChestSaves(PlayerInteractEvent event){
		Block block = event.getClickedBlock();
		Player p = event.getPlayer();
		//currently crashes the server when refilling....
		//Kinda glitchy through all here...
		if(block.getState() instanceof Chest && p.getItemInHand().getType()== Material.BLAZE_ROD && event.getAction() == Action.LEFT_CLICK_BLOCK){

			ItemStack[] itemsinchest = ((Chest) block.getState()).getInventory().getContents();
			int blockx = block.getX();
			int blocky = block.getY();
			int blockz = block.getZ();
			String blockw = block.getWorld().getName().toString();
			if(!plugin.getConfig().contains("Storage." + blockx + "," + blocky + "," + blockz + ".Location.X")){
				plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.X", blockx);
				plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Y", blocky);
				plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Z",blockz);
				plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.W", blockw);
				plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".ItemsInStorage", itemsinchest);
			}else{
				plugin.getConfig().set("Storage." + blockx + "," + blocky+ "," + blockz + ".Location.X",blockx);
				plugin.getConfig().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Y", blocky);
				plugin.getConfig().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Z", blockz);
				plugin.getConfig().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.W", blockw);
				plugin.getConfig().set("Storage." + blockx + "," + blocky + "," + blockz + ".ItemsInStorage", itemsinchest);
			}
			List<String> list2 = plugin.getConfig().getStringList("StorageXYZ");
			list2.add(blockx + "," + blocky + "," + blockz);
			plugin.getConfig().set("StorageXYZ", list2);
			plugin.getConfig().options().copyDefaults(true);
			plugin.saveConfig();
		}
	}
}
