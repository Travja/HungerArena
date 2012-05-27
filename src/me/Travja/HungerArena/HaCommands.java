package me.Travja.HungerArena;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HaCommands implements CommandExecutor {
	public Main plugin;
	public HaCommands(Main m) {
		this.plugin = m;
	}
	public FileConfiguration config = plugin.config;
	public ArrayList<Player> Playing = plugin.Playing;
	public ArrayList<Player> Ready = plugin.Ready;
	public ArrayList<Player> Dead = plugin.Dead;
	public ArrayList<Player> Quit = plugin.Quit;
	public ArrayList<Player> Out = plugin.Out;
	public ArrayList<Player> Watching = plugin.Watching;
	public ArrayList<Player> NeedConfirm = plugin.NeedConfirm;
	public HashSet<Player> Frozen = plugin.Frozen;
	public boolean canjoin = plugin.canjoin;
	public boolean exists = plugin.exists;
	public ItemStack Reward = plugin.Reward;
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player p = (Player) sender;
		String pname = p.getName();
		if(cmd.getName().equalsIgnoreCase("Ha")){
			if(config.getString("Spawns_set").equalsIgnoreCase("false")){
				p.sendMessage(ChatColor.RED + "/ha setspawn hasn't been run!");
			}
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
					plugin.saveConfig();
					config.set("Spawns_set", "true");
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
			World spawnw = plugin.getServer().getWorld(spawnworld);
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
			if(args[0].equalsIgnoreCase("Confirm")){
				if(NeedConfirm.contains(p)){
					Playing.add(p);
					NeedConfirm.remove(p);
					p.getInventory().clear();
					p.getInventory().setBoots(null);
					p.getInventory().setChestplate(null);
					p.getInventory().setHelmet(null);
					p.getInventory().setLeggings(null);
					plugin.getServer().broadcastMessage(ChatColor.AQUA + pname +  " has Joined the Game!");
					if(Playing.size()== 24){
						p.performCommand("ha warpall");
					}
				}
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
						for(Player online:plugin.getServer().getOnlinePlayers()){
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
						for(Player online:plugin.getServer().getOnlinePlayers()){
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
				Player target = plugin.getServer().getPlayer(args[1]);
				if(p.hasPermission("HungerArena.Kick")){
					if(Playing.contains(target)){
						if(p.isOnline()){
							Playing.remove(target);
							plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " was kicked from the game!");
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
				limit = plugin.getConfig().getStringList("StorageXYZ").size();
				while(limit > list056){
					String xyz2 = plugin.getConfig().getStringList("StorageXYZ").get(list056);
					int chestx = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.X");
					int chesty = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.Y");
					int chestz = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.Z");
					String chestw = plugin.getConfig().getString("Storage." + xyz2 + ".Location.W");
					Block blockatlocation = Bukkit.getWorld(chestw).getBlockAt(chestx, chesty, chestz);  
					exists = false;
					if(blockatlocation.getState() instanceof Chest){
						exists = true;
						Chest chest = (Chest) blockatlocation.getState();
						chest.getInventory().clear();
						ItemStack[] itemsinchest = null;
						Object o = plugin.getConfig().get("Storage." + xyz2 + ".ItemsInStorage");
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
				plugin.reloadConfig();
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
						World w = plugin.getServer().getWorld(world);
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
						World twow = plugin.getServer().getWorld(twoworld);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
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
						World w = plugin.getServer().getWorld(world);
						Location loc = new Location(w, x, y, z);
						Tribute_twentyfour.teleport(loc);
						Frozen.add(Tribute_twentyfour);
						Tribute_twentyfour.setFoodLevel(20);
					}
				}
			}
		}
		return false;

	}
}

