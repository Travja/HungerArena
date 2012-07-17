package me.Travja.HungerArena;

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
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HaCommands implements CommandExecutor {
	public Main plugin;
	public HaCommands(Main m) {
		this.plugin = m;
	}
	int i = 0;
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String commandLabel, String[] args){
		String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		String spawnworld = Spawncoords[3];
		World spawnw = plugin.getServer().getWorld(spawnworld);
		Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
		if(sender instanceof Player){
			final Player p = (Player) sender;
			final String pname = p.getDisplayName();
			ChatColor c = ChatColor.AQUA;
			if(cmd.getName().equalsIgnoreCase("Ha")){
				if(args.length== 0){
					p.sendMessage(ChatColor.GREEN + "[HungerArena] by " + ChatColor.AQUA + "travja!");
					return false;
				}
				if(args[0].equalsIgnoreCase("Help")){
					p.sendMessage(ChatColor.GREEN + "----HungerArena Help----");
					p.sendMessage(c + "/ha - Displays author message!");
					sender.sendMessage(c + "/ha help - Displays this screen!");
					sender.sendMessage(c + "/ha join - Makes you join the game!");
					sender.sendMessage(c + "/ha ready - Votes for the game to start!");
					sender.sendMessage(c + "/ha leave - Makes you leave the game!");
					sender.sendMessage(c + "/ha watch - Lets you watch the tributes!");
					sender.sendMessage(c + "/sponsor [Player] [ItemID] [Amount] - Lets you sponsor someone!");
					sender.sendMessage(c + "/ha setspawn - Sets the spawn for dead tributes!");
					sender.sendMessage(c + "/ha kick [Player] - Kicks a player from the arena!");
					sender.sendMessage(c + "/ha restart - Makes it so dead tributes can join again!");
					sender.sendMessage(c + "/ha warpall - Warps all tribute into position!");
					sender.sendMessage(c + "/ha reload - Reloads the config!");
					sender.sendMessage(c + "/ha refill - Refills all chests!");
					sender.sendMessage(c + "/ha start - Unfreezes tributes allowing them to fight!");
					sender.sendMessage(c + "/ha list - Shows a list of players in the game and their health!");
					sender.sendMessage(c + "/ha rlist - See who's ready!");
					sender.sendMessage(c + "/startpoint [1,2,3,4,etc] - Sets the starting points of tributes!");
					sender.sendMessage(ChatColor.GREEN + "----------------------");
					return false;
				}else if(args[0].equalsIgnoreCase("List")){
					if(p.hasPermission("HungerArena.GameMaker")){
						sender.sendMessage(ChatColor.AQUA + "-----People Playing-----");
						if(!plugin.Playing.isEmpty()){
							for(String playernames: plugin.Playing){
								Player players = plugin.getServer().getPlayerExact(playernames);
								p.sendMessage(ChatColor.GREEN + playernames + " Life: " + players.getHealth() + "/20");
							}
						}else if(plugin.Playing.isEmpty()){
							p.sendMessage(ChatColor.GRAY + "No one is playing!");
						}
						p.sendMessage(ChatColor.AQUA + "----------------------");
					}else{
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else if(args[0].equalsIgnoreCase("rList")){
					if(p.hasPermission("HungerArena.GameMaker")){
						p.sendMessage(ChatColor.AQUA + "-----People Ready-----");
						if(!plugin.Ready.isEmpty()){
							for(String readyname: plugin.Ready){
								Player ready = plugin.getServer().getPlayerExact(readyname);
								p.sendMessage(ChatColor.GREEN + readyname + " Life: " + ready.getHealth() + "/20");
							}
						}else if(plugin.Ready.isEmpty()){
							p.sendMessage(ChatColor.GRAY + "No one is ready!");
						}
						p.sendMessage(ChatColor.AQUA + "---------------------");
					}else{
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else if(args[0].equalsIgnoreCase("SetSpawn")){
					if(p.hasPermission("HungerArena.SetSpawn")){
						double x = p.getLocation().getX();
						double y = p.getLocation().getY();
						double z = p.getLocation().getZ();
						String w = p.getWorld().getName();
						plugin.config.set("Spawn_coords", x + "," + y + "," + z + "," + w);
						plugin.config.set("Spawns_set", "true");
						plugin.saveConfig();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn for dead tributes!");
					}else{
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else if(args[0].equalsIgnoreCase("Join")){
					if(p.hasPermission("HungerArena.Join")){
						if(plugin.Playing.contains(pname)){
							p.sendMessage(ChatColor.RED + "You are already playing!");
						}else if(plugin.Dead.contains(pname) || plugin.Quit.contains(pname)){
							p.sendMessage(ChatColor.RED + "You DIED/QUIT! You can't join again!");
						}else if(plugin.Playing.size()== 24){
							p.sendMessage(ChatColor.RED + "There are already 24 Tributes!");
						}else if(plugin.canjoin== true){
							p.sendMessage(ChatColor.RED + "The game is in progress!");
						}else if(plugin.config.getString("Spawns_set").equalsIgnoreCase("false")){
							p.sendMessage(ChatColor.RED + "/ha setspawn hasn't been run!");
						}else if(plugin.NeedConfirm.contains(pname)){
							p.sendMessage(ChatColor.RED + "You need to do /ha confirm");
						}else if(plugin.config.getString("Need_Confirm").equalsIgnoreCase("true")){
							plugin.NeedConfirm.add(pname);
							p.sendMessage(ChatColor.GOLD + "You're inventory will be cleared! Type /ha confirm to procede");
						}else{
							plugin.Playing.add(pname);
							p.getInventory().clear();
							p.getInventory().setBoots(null);
							p.getInventory().setChestplate(null);
							p.getInventory().setHelmet(null);
							p.getInventory().setLeggings(null);
							plugin.getServer().broadcastMessage(ChatColor.AQUA + pname +  " has Joined the Game!");
							if(plugin.Playing.size()== 24){
								p.performCommand("ha warpall");
							}
						}
					}else{
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else if(args[0].equalsIgnoreCase("Confirm")){
					if(plugin.NeedConfirm.contains(pname)){
						plugin.Playing.add(pname);
						plugin.NeedConfirm.remove(pname);
						p.sendMessage(ChatColor.GREEN + "Do /ha ready to vote to start the games!");
						p.getInventory().clear();
						p.getInventory().setBoots(null);
						p.getInventory().setChestplate(null);
						p.getInventory().setHelmet(null);
						p.getInventory().setLeggings(null);
						plugin.getServer().broadcastMessage(ChatColor.AQUA + pname +  " has Joined the Game!");
						if(plugin.Playing.size()== 24){
							p.performCommand("ha warpall");
						}
					}
				}else if(args[0].equalsIgnoreCase("Ready")){
					if(plugin.Playing.contains(pname)){
						if(plugin.Ready.contains(pname)){
							p.sendMessage(ChatColor.RED + "You're already ready!");
						}else if(plugin.Playing.size()== 1){
							p.sendMessage(ChatColor.RED + "You can't be ready when no one else is playing!");
						}else{
							plugin.Ready.add(pname);
							p.sendMessage(ChatColor.AQUA + "You have marked yourself as READY!");
							if(plugin.Playing.size()-4== plugin.Ready.size()){
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha warpall");
							}
						}
					}else if(!plugin.Playing.contains(pname)){
						p.sendMessage(ChatColor.RED + "You aren't playing!");
					}
				}else if(args[0].equalsIgnoreCase("Leave")){
					if(!plugin.Playing.contains(pname)){
						p.sendMessage(ChatColor.RED + "You aren't playing!");
					}else if(plugin.canjoin== false){
						plugin.Playing.remove(pname);
						p.sendMessage(ChatColor.AQUA + "You have left the game!");
						p.getServer().broadcastMessage(ChatColor.RED + pname + " Quit!");
						p.getInventory().clear();
						p.teleport(Spawn);
						p.getInventory().setBoots(null);
						p.getInventory().setChestplate(null);
						p.getInventory().setHelmet(null);
						p.getInventory().setLeggings(null);
						if(plugin.Frozen.contains(pname)){
							plugin.Frozen.remove(pname);
						}
					}else{
						plugin.Playing.remove(pname);
						plugin.Quit.add(pname);
						p.sendMessage(ChatColor.AQUA + "You have left the game!");
						p.getServer().broadcastMessage(ChatColor.RED + pname + " Quit!");
						p.getInventory().clear();
						p.getInventory().setBoots(null);
						p.getInventory().setChestplate(null);
						p.getInventory().setHelmet(null);
						p.getInventory().setLeggings(null);
						p.teleport(Spawn);
						if(plugin.Frozen.contains(pname)){
							plugin.Frozen.remove(pname);
						}
						if(plugin.Playing.size()== 1){
							//Announce the Winner
							String winnername = plugin.Playing.get(i++);
							Player winner = plugin.getServer().getPlayerExact(winnername);
							String winnername2 = winner.getName();
							plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.teleport(Spawn);
							winner.getInventory().setBoots(null);
							winner.getInventory().setChestplate(null);
							winner.getInventory().setHelmet(null);
							winner.getInventory().setLeggings(null);
							winner.getInventory().addItem(plugin.Reward);
							plugin.Playing.clear();
							//Show spectators
							String s = plugin.Watching.get(i++);
							Player spectator = plugin.getServer().getPlayerExact(s);
							spectator.setAllowFlight(false);
							spectator.teleport(Spawn);
							for(Player online:plugin.getServer().getOnlinePlayers()){
								online.showPlayer(spectator);
							}
							if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart");
							}
						}
					}
				}else if(args[0].equalsIgnoreCase("Watch")){
					if(sender.hasPermission("HungerArena.Watch")){
						if(!plugin.Watching.contains(pname) && !plugin.Playing.contains(pname) && plugin.canjoin== true){
							plugin.Watching.add(pname);
							for(Player online:plugin.getServer().getOnlinePlayers()){
								online.hidePlayer(p);
							}
							p.setAllowFlight(true);
							p.sendMessage(ChatColor.AQUA + "You can now spectate!");
						}else if(plugin.canjoin== false){
							p.sendMessage(ChatColor.RED + "The game isn't in progress!");
						}else if(plugin.Playing.contains(pname)){
							p.sendMessage(ChatColor.RED + "You can't watch while you're playing!");
						}else if(plugin.Watching.contains(pname)){
							plugin.Watching.remove(pname);
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
				}else if(args[0].equalsIgnoreCase("Kick")){
					Player target = plugin.getServer().getPlayer(args[1]);
					if(sender.hasPermission("HungerArena.Kick")){
						if(plugin.Playing.contains(target.getDisplayName())){
							plugin.Playing.remove(target.getDisplayName());
							plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " was kicked from the game!");
							target.teleport(Spawn);
							target.getInventory().clear();
							target.getInventory().setBoots(null);
							target.getInventory().setChestplate(null);
							target.getInventory().setHelmet(null);
							target.getInventory().setLeggings(null);
							plugin.Quit.add(target.getDisplayName());
							if(plugin.Playing.size()== 1 && plugin.canjoin== true){
								String winnername = plugin.Playing.get(i++);
								Player winner = plugin.getServer().getPlayerExact(winnername);
								String winnername2 = winner.getName();
								plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
								winner.getInventory().clear();
								winner.teleport(Spawn);
								winner.getInventory().setBoots(null);
								winner.getInventory().setChestplate(null);
								winner.getInventory().setHelmet(null);
								winner.getInventory().setLeggings(null);
								winner.getInventory().addItem(plugin.Reward);
								plugin.Playing.clear();
								if(!plugin.Watching.isEmpty()){
									String s = plugin.Watching.get(i++);
									Player spectator = plugin.getServer().getPlayerExact(s);
									spectator.setAllowFlight(false);
									spectator.teleport(Spawn);
									for(Player online:plugin.getServer().getOnlinePlayers()){
										online.showPlayer(spectator);
									}
								}
								if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
									Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart");
								}
							}
						}else{
							sender.sendMessage(ChatColor.RED + "That player isn't in the game!");
						}
					}else{
						sender.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else if(args[0].equalsIgnoreCase("Refill")){
					if(p.hasPermission("HungerArena.Refill")){
						int list056;
						list056 = 0;
						int limit = plugin.config.getStringList("StorageXYZ").size();
						while(limit > list056){
							String xyz2 = plugin.getConfig().getStringList("StorageXYZ").get(list056);
							int chestx = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.X");
							int chesty = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.Y");
							int chestz = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.Z");
							String chestw = plugin.getConfig().getString("Storage." + xyz2 + ".Location.W");
							Block blockatlocation = Bukkit.getWorld(chestw).getBlockAt(chestx, chesty, chestz);  
							plugin.exists = false;
							if(blockatlocation.getState() instanceof Chest){
								plugin.exists = true;
								Chest chest = (Chest) blockatlocation.getState();
								chest.getInventory().clear();
								ItemStack[] itemsinchest = null;
								Object o = plugin.getConfig().get("Storage." + xyz2 + ".ItemsInStorage");
								if(o instanceof ItemStack[]){
									itemsinchest = (ItemStack[]) o;
								}else if(o instanceof List){
									itemsinchest = (ItemStack[]) ((List<ItemStack>) o).toArray(new ItemStack[0]);
								}
								list056 = list056+1;
								chest.getInventory().setContents(itemsinchest);
							}
						}
						if(limit== list056){
							sender.sendMessage(ChatColor.GREEN + "All chests refilled!");
						}
					}else{
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else if(args[0].equalsIgnoreCase("Restart")){
					if(p.hasPermission("HungerArena.Restart")){
						if(!plugin.Watching.isEmpty()){
							String s = plugin.Watching.get(i++);
							Player spectator = plugin.getServer().getPlayerExact(s);
							spectator.setAllowFlight(false);
							spectator.teleport(Spawn);
							for(Player online:plugin.getServer().getOnlinePlayers()){
								online.showPlayer(spectator);
							}
						}
						plugin.Dead.clear();
						plugin.Quit.clear();
						plugin.Watching.clear();
						plugin.Frozen.clear();
						plugin.Ready.clear();
						plugin.NeedConfirm.clear();
						plugin.Out.clear();
						plugin.Playing.clear();
						plugin.canjoin = false;
						p.performCommand("ha refill");
						p.sendMessage(ChatColor.AQUA + "The games have been reset!");
					}else{
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else if(args[0].equalsIgnoreCase("Reload")){
					plugin.reloadConfig();
					p.sendMessage(ChatColor.AQUA + "HungerArena Reloaded!");
				}else if(args[0].equalsIgnoreCase("WarpAll")){
					if(p.hasPermission("HungerArena.Warpall")){
						if(plugin.config.getString("Spawns_set").equalsIgnoreCase("false")){
							sender.sendMessage(ChatColor.RED + "/ha setspawn hasn't been run!");
						}else{
							if(plugin.Playing.size()== 1){
								sender.sendMessage(ChatColor.RED + "There are not enough players!");
							}
							if(plugin.Playing.size()>= 2){
								plugin.config.getString("Tribute_one_spawn");
								String[] onecoords = plugin.config.getString("Tribute_one_spawn").split(",");
								Player Tribute_one = plugin.getServer().getPlayerExact(plugin.Playing.get(0));
								double x = Double.parseDouble(onecoords[0]);
								double y = Double.parseDouble(onecoords[1]);
								double z = Double.parseDouble(onecoords[2]);
								String world = onecoords[3];
								World w = plugin.getServer().getWorld(world);
								Location oneloc = new Location(w, x, y, z);
								Tribute_one.teleport(oneloc);
								plugin.Frozen.add(Tribute_one.getDisplayName());
								Tribute_one.setFoodLevel(20);
								plugin.config.getString("Tribute_two_spawn");
								String[] twocoords = plugin.config.getString("Tribute_two_spawn").split(",");
								Player Tribute_two = plugin.getServer().getPlayerExact(plugin.Playing.get(1));
								double twox = Double.parseDouble(twocoords[0]);
								double twoy = Double.parseDouble(twocoords[1]);
								double twoz = Double.parseDouble(twocoords[2]);
								String twoworld = twocoords[3];
								World twow = plugin.getServer().getWorld(twoworld);
								Location twoloc = new Location(twow, twox, twoy, twoz);
								Tribute_two.teleport(twoloc);
								plugin.Frozen.add(Tribute_two.getDisplayName());
								Tribute_two.setFoodLevel(20);
								p.getWorld().setTime(0);
								if(plugin.config.getString("Auto_Start").equalsIgnoreCase("true")){
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
										public void run(){
											Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha start");
										}
									}, 20L);
								}
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
									public void run(){
										p.sendMessage(ChatColor.AQUA + "All Tributes warped!");
									}
								}, 20L);
							}
							if(plugin.Playing.size()>= 3){
								plugin.config.getString("Tribute_three_spawn");
								String[] coords = plugin.config.getString("Tribute_three_spawn").split(",");
								Player Tribute_three = plugin.getServer().getPlayerExact(plugin.Playing.get(2));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_three.teleport(loc);
								plugin.Frozen.add(Tribute_three.getDisplayName());
								Tribute_three.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 4){
								plugin.config.getString("Tribute_four_spawn");
								String[] coords = plugin.config.getString("Tribute_four_spawn").split(",");
								Player Tribute_four = plugin.getServer().getPlayerExact(plugin.Playing.get(3));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_four.teleport(loc);
								plugin.Frozen.add(Tribute_four.getDisplayName());
								Tribute_four.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 5){
								plugin.config.getString("Tribute_five_spawn");
								String[] coords = plugin.config.getString("Tribute_five_spawn").split(",");
								Player Tribute_five = plugin.getServer().getPlayerExact(plugin.Playing.get(4));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_five.teleport(loc);
								plugin.Frozen.add(Tribute_five.getDisplayName());
								Tribute_five.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 6){
								plugin.config.getString("Tribute_six_spawn");
								String[] coords = plugin.config.getString("Tribute_six_spawn").split(",");
								Player Tribute_six = plugin.getServer().getPlayerExact(plugin.Playing.get(5));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_six.teleport(loc);
								plugin.Frozen.add(Tribute_six.getDisplayName());
								Tribute_six.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 7){
								plugin.config.getString("Tribute_seven_spawn");
								String[] coords = plugin.config.getString("Tribute_seven_spawn").split(",");
								Player Tribute_seven = plugin.getServer().getPlayerExact(plugin.Playing.get(6));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_seven.teleport(loc);
								plugin.Frozen.add(Tribute_seven.getDisplayName());
								Tribute_seven.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 8){
								plugin.config.getString("Tribute_eight_spawn");
								String[] coords = plugin.config.getString("Tribute_eight_spawn").split(",");
								Player Tribute_eight = plugin.getServer().getPlayerExact(plugin.Playing.get(7));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_eight.teleport(loc);
								plugin.Frozen.add(Tribute_eight.getDisplayName());
								Tribute_eight.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 9){
								plugin.config.getString("Tribute_nine_spawn");
								String[] coords = plugin.config.getString("Tribute_nine_spawn").split(",");
								Player Tribute_nine = plugin.getServer().getPlayerExact(plugin.Playing.get(8));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_nine.teleport(loc);
								plugin.Frozen.add(Tribute_nine.getDisplayName());
								Tribute_nine.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 10){
								plugin.config.getString("Tribute_ten_spawn");
								String[] coords = plugin.config.getString("Tribute_ten_spawn").split(",");
								Player Tribute_ten = plugin.getServer().getPlayerExact(plugin.Playing.get(9));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_ten.teleport(loc);
								plugin.Frozen.add(Tribute_ten.getDisplayName());
								Tribute_ten.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 11){
								plugin.config.getString("Tribute_eleven_spawn");
								String[] coords = plugin.config.getString("Tribute_eleven_spawn").split(",");
								Player Tribute_eleven = plugin.getServer().getPlayerExact(plugin.Playing.get(10));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_eleven.teleport(loc);
								plugin.Frozen.add(Tribute_eleven.getDisplayName());
								Tribute_eleven.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 12){
								plugin.config.getString("Tribute_twelve_spawn");
								String[] coords = plugin.config.getString("Tribute_twelve_spawn").split(",");
								Player Tribute_twelve = plugin.getServer().getPlayerExact(plugin.Playing.get(11));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_twelve.teleport(loc);
								plugin.Frozen.add(Tribute_twelve.getDisplayName());
								Tribute_twelve.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 13){
								plugin.config.getString("Tribute_thirteen_spawn");
								String[] coords = plugin.config.getString("Tribute_thirteen_spawn").split(",");
								Player Tribute_thirteen = plugin.getServer().getPlayerExact(plugin.Playing.get(12));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_thirteen.teleport(loc);
								plugin.Frozen.add(Tribute_thirteen.getDisplayName());
								Tribute_thirteen.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 14){
								plugin.config.getString("Tribute_fourteen_spawn");
								String[] coords = plugin.config.getString("Tribute_fourteen_spawn").split(",");
								Player Tribute_fourteen = plugin.getServer().getPlayerExact(plugin.Playing.get(13));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_fourteen.teleport(loc);
								plugin.Frozen.add(Tribute_fourteen.getDisplayName());
								Tribute_fourteen.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 15){
								plugin.config.getString("Tribute_fifteen_spawn");
								String[] coords = plugin.config.getString("Tribute_fifteen_spawn").split(",");
								Player Tribute_fifteen = plugin.getServer().getPlayerExact(plugin.Playing.get(14));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_fifteen.teleport(loc);
								plugin.Frozen.add(Tribute_fifteen.getDisplayName());
								Tribute_fifteen.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 16){
								plugin.config.getString("Tribute_sixteen_spawn");
								String[] coords = plugin.config.getString("Tribute_sixteen_spawn").split(",");
								Player Tribute_sixteen = plugin.getServer().getPlayerExact(plugin.Playing.get(15));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_sixteen.teleport(loc);
								plugin.Frozen.add(Tribute_sixteen.getDisplayName());
								Tribute_sixteen.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 17){
								plugin.config.getString("Tribute_seventeen_spawn");
								String[] coords = plugin.config.getString("Tribute_seventeen_spawn").split(",");
								Player Tribute_seventeen = plugin.getServer().getPlayerExact(plugin.Playing.get(16));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_seventeen.teleport(loc);
								plugin.Frozen.add(Tribute_seventeen.getDisplayName());
								Tribute_seventeen.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 18){
								plugin.config.getString("Tribute_eighteen_spawn");
								String[] coords = plugin.config.getString("Tribute_eighteen_spawn").split(",");
								Player Tribute_eighteen = plugin.getServer().getPlayerExact(plugin.Playing.get(17));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_eighteen.teleport(loc);
								plugin.Frozen.add(Tribute_eighteen.getDisplayName());
								Tribute_eighteen.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 19){
								plugin.config.getString("Tribute_nineteen_spawn");
								String[] coords = plugin.config.getString("Tribute_nineteen_spawn").split(",");
								Player Tribute_nineteen = plugin.getServer().getPlayerExact(plugin.Playing.get(18));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_nineteen.teleport(loc);
								plugin.Frozen.add(Tribute_nineteen.getDisplayName());
								Tribute_nineteen.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 20){
								plugin.config.getString("Tribute_twenty_spawn");
								String[] coords = plugin.config.getString("Tribute_twenty_spawn").split(",");
								Player Tribute_twenty = plugin.getServer().getPlayerExact(plugin.Playing.get(19));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_twenty.teleport(loc);
								plugin.Frozen.add(Tribute_twenty.getDisplayName());
								Tribute_twenty.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 21){
								plugin.config.getString("Tribute_twentyone_spawn");
								String[] coords = plugin.config.getString("Tribute_twentyone_spawn").split(",");
								Player Tribute_twentyone = plugin.getServer().getPlayerExact(plugin.Playing.get(20));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_twentyone.teleport(loc);
								plugin.Frozen.add(Tribute_twentyone.getDisplayName());
								Tribute_twentyone.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 22){
								plugin.config.getString("Tribute_twentytwo_spawn");
								String[] coords = plugin.config.getString("Tribute_twentytwo_spawn").split(",");
								Player Tribute_twentytwo = plugin.getServer().getPlayerExact(plugin.Playing.get(21));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_twentytwo.teleport(loc);
								plugin.Frozen.add(Tribute_twentytwo.getDisplayName());
								Tribute_twentytwo.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 23){
								plugin.config.getString("Tribute_twentythree_spawn");
								String[] coords = plugin.config.getString("Tribute_twentythree_spawn").split(",");
								Player Tribute_twentythree = plugin.getServer().getPlayerExact(plugin.Playing.get(22));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_twentythree.teleport(loc);
								plugin.Frozen.add(Tribute_twentythree.getDisplayName());
								Tribute_twentythree.setFoodLevel(20);
							}
							if(plugin.Playing.size()>= 24){
								plugin.config.getString("Tribute_twentyfour_spawn");
								String[] coords = plugin.config.getString("Tribute_twentyfour_spawn").split(",");
								Player Tribute_twentyfour = plugin.getServer().getPlayerExact(plugin.Playing.get(23));
								double x = Double.parseDouble(coords[0]);
								double y = Double.parseDouble(coords[1]);
								double z = Double.parseDouble(coords[2]);
								String world = coords[3];
								World w = plugin.getServer().getWorld(world);
								Location loc = new Location(w, x, y, z);
								Tribute_twentyfour.teleport(loc);
								plugin.Frozen.add(Tribute_twentyfour.getDisplayName());
								Tribute_twentyfour.setFoodLevel(20);
							}
						}
					}
				}else if(args[0].equalsIgnoreCase("Start")){
					String begin = plugin.config.getString("Start_Message");
					begin = begin.replaceAll("(&([a-f0-9]))", "\u00A7$2");
					final String msg = begin;
					if(p.hasPermission("HungerArena.Start")){
						if(plugin.config.getString("Countdown").equalsIgnoreCase("true")){
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("10");
								}
							}, 20L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("9");
								}
							}, 40L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("8");
								}
							}, 60L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("7");
								}
							}, 80L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("6");
								}
							}, 100L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("5");
								}
							}, 120L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("4");
								}
							}, 140L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("3");
								}
							}, 160L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("2");
								}
							}, 180L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.getServer().broadcastMessage("1");
								}
							}, 200L);
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									plugin.Frozen.clear();
									plugin.getServer().broadcastMessage(msg);
									plugin.canjoin = true;
								}
							}, 220L);
						}else{
							plugin.Frozen.clear();
							p.getServer().broadcastMessage(msg);
							plugin.canjoin = true;
						}
					}else{
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else{
					p.sendMessage(ChatColor.RED + "Unknown command, type /ha help for a list of commands");
				}
			}
		}else if(sender instanceof ConsoleCommandSender){
			if(cmd.getName().equalsIgnoreCase("Ha")){
				if(args.length== 0){
					sender.sendMessage(ChatColor.GREEN + "[HungerArena] by " + ChatColor.AQUA + "travja!");
					return false;
				}
				if(args[0].equalsIgnoreCase("Help")){
					ChatColor c = ChatColor.AQUA;
					sender.sendMessage(ChatColor.GREEN + "----HungerArena Help----");
					sender.sendMessage(c + "/ha - Displays author message!");
					sender.sendMessage(c + "/ha help - Displays this screen!");
					sender.sendMessage(c + "/ha join - Makes you join the game!");
					sender.sendMessage(c + "/ha ready - Votes for the game to start!");
					sender.sendMessage(c + "/ha leave - Makes you leave the game!");
					sender.sendMessage(c + "/ha watch - Lets you watch the tributes!");
					sender.sendMessage(c + "/sponsor [Player] [ItemID] [Amount] - Lets you sponsor someone!");
					sender.sendMessage(c + "/ha setspawn - Sets the spawn for dead tributes!");
					sender.sendMessage(c + "/ha kick [Player] - Kicks a player from the arena!");
					sender.sendMessage(c + "/ha restart - Makes it so dead tributes can join again!");
					sender.sendMessage(c + "/ha warpall - Warps all tribute into position!");
					sender.sendMessage(c + "/ha reload - Reloads the config!");
					sender.sendMessage(c + "/ha refill - Refills all chests!");
					sender.sendMessage(c + "/ha start - Unfreezes tributes allowing them to fight!");
					sender.sendMessage(c + "/ha list - Shows a list of players in the game and their health!");
					sender.sendMessage(c + "/ha rlist - See who's ready!");
					sender.sendMessage(c + "/startpoint [1,2,3,4,etc] - Sets the starting points of tributes!");
					sender.sendMessage(ChatColor.GREEN + "----------------------");
					return false;
				}else if(args[0].equalsIgnoreCase("List")){
					sender.sendMessage(ChatColor.AQUA + "-----People Playing-----");
					if(!plugin.Playing.isEmpty()){
						for(String playernames: plugin.Playing){
							Player players = plugin.getServer().getPlayerExact(playernames);
							sender.sendMessage(ChatColor.GREEN + players.getDisplayName() + " Life: " + players.getHealth() + "/20");
						}
					}else if(plugin.Playing.isEmpty()){
						sender.sendMessage(ChatColor.GRAY + "No one is playing!");
					}
					sender.sendMessage(ChatColor.AQUA + "----------------------");
				}else if(args[0].equalsIgnoreCase("rList")){
					sender.sendMessage(ChatColor.AQUA + "-----People Ready-----");
					if(!plugin.Ready.isEmpty()){
						for(String readyname: plugin.Ready){
							Player ready = plugin.getServer().getPlayerExact(readyname);
							sender.sendMessage(ChatColor.GREEN + readyname + " Life: " + ready.getHealth() + "/20");
						}
					}else if(plugin.Ready.isEmpty()){
						sender.sendMessage(ChatColor.GRAY + "No one is ready!");
					}
					sender.sendMessage(ChatColor.AQUA + "---------------------");
				}else if(args[0].equalsIgnoreCase("SetSpawn") || args[0].equalsIgnoreCase("Join") || args[0].equalsIgnoreCase("Confirm") || args[0].equalsIgnoreCase("Ready") || args[0].equalsIgnoreCase("Leave") || args[0].equalsIgnoreCase("Watch")){
					sender.sendMessage(ChatColor.RED + "That can only be run by a player!");
				}else if(args[0].equalsIgnoreCase("Kick")){
					Player target = plugin.getServer().getPlayer(args[1]);
					if(plugin.Playing.contains(target)){
						plugin.Playing.remove(target.getDisplayName());
						plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " was kicked from the game!");
						target.teleport(Spawn);
						target.getInventory().clear();
						target.getInventory().setBoots(null);
						target.getInventory().setChestplate(null);
						target.getInventory().setHelmet(null);
						target.getInventory().setLeggings(null);
						plugin.Quit.add(target.getDisplayName());
						if(plugin.Playing.size()== 1 && plugin.canjoin== true){
							//Announce winner
							String winnername = plugin.Playing.get(i++);
							Player winner = plugin.getServer().getPlayerExact(winnername);
							String winnername2 = winner.getName();
							plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.teleport(Spawn);
							winner.getInventory().setBoots(null);
							winner.getInventory().setChestplate(null);
							winner.getInventory().setHelmet(null);
							winner.getInventory().setLeggings(null);
							winner.getInventory().addItem(plugin.Reward);
							plugin.Playing.clear();
							//Make spectators visible
							if(!plugin.Watching.isEmpty()){
								String s = plugin.Watching.get(i++);
								Player spectator = plugin.getServer().getPlayerExact(s);
								spectator.setAllowFlight(false);
								spectator.teleport(Spawn);
								for(Player online:plugin.getServer().getOnlinePlayers()){
									online.showPlayer(spectator);
								}
							}
							if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
								Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha restart");
							}
						}
					}else{
						sender.sendMessage(ChatColor.RED + "That player isn't in the game!");
					}
				}else if(args[0].equalsIgnoreCase("Refill")){
					int list056;
					list056 = 0;
					int limit = plugin.getConfig().getStringList("StorageXYZ").size();
					while(limit > list056){
						String xyz2 = plugin.getConfig().getStringList("StorageXYZ").get(list056);
						int chestx = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.X");
						int chesty = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.Y");
						int chestz = plugin.getConfig().getInt("Storage." + xyz2 + ".Location.Z");
						String chestw = plugin.getConfig().getString("Storage." + xyz2 + ".Location.W");
						Block blockatlocation = Bukkit.getWorld(chestw).getBlockAt(chestx, chesty, chestz);  
						plugin.exists = false;
						if(blockatlocation.getState() instanceof Chest){
							plugin.exists = true;
							Chest chest = (Chest) blockatlocation.getState();
							chest.getInventory().clear();
							ItemStack[] itemsinchest = null;
							Object o = plugin.getConfig().get("Storage." + xyz2 + ".ItemsInStorage");
							if(o instanceof ItemStack[]){
								itemsinchest = (ItemStack[]) o;
							}else if(o instanceof List){
								itemsinchest = (ItemStack[]) ((List<ItemStack>) o).toArray(new ItemStack[0]);
							}
							list056 = list056+1;
							chest.getInventory().setContents(itemsinchest);
						}
					}
					if(limit== list056){
						sender.sendMessage(ChatColor.GREEN + "All chests refilled!");
					}
				}else if(args[0].equalsIgnoreCase("Restart")){
					if(!plugin.Watching.isEmpty()){
						String s = plugin.Watching.get(i++);
						Player spectator = plugin.getServer().getPlayerExact(s);
						spectator.setAllowFlight(false);
						spectator.teleport(Spawn);
						for(Player online:plugin.getServer().getOnlinePlayers()){
							online.showPlayer(spectator);
						}
					}
					plugin.Dead.clear();
					plugin.Quit.clear();
					plugin.Watching.clear();
					plugin.Frozen.clear();
					plugin.Ready.clear();
					plugin.NeedConfirm.clear();
					plugin.Out.clear();
					plugin.Playing.clear();
					plugin.canjoin = false;
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha refill");
					sender.sendMessage(ChatColor.AQUA + "The games have been reset!");
				}else if(args[0].equalsIgnoreCase("Reload")){
					plugin.reloadConfig();
					sender.sendMessage(ChatColor.AQUA + "HungerArena Reloaded!");
				}else if(args[0].equalsIgnoreCase("WarpAll")){
					if(plugin.config.getString("Spawns_set").equalsIgnoreCase("false")){
						sender.sendMessage(ChatColor.RED + "/ha setspawn hasn't been run!");
					}else{
						if(plugin.Playing.size()<= 1){
							sender.sendMessage(ChatColor.RED + "There are not enough players!");
						}
						if(plugin.Playing.size()>= 2){
							plugin.config.getString("Tribute_one_spawn");
							String[] onecoords = plugin.config.getString("Tribute_one_spawn").split(",");
							Player Tribute_one = plugin.getServer().getPlayerExact(plugin.Playing.get(0));
							double x = Double.parseDouble(onecoords[0]);
							double y = Double.parseDouble(onecoords[1]);
							double z = Double.parseDouble(onecoords[2]);
							String world = onecoords[3];
							World w = plugin.getServer().getWorld(world);
							Location oneloc = new Location(w, x, y, z);
							Tribute_one.teleport(oneloc);
							plugin.Frozen.add(Tribute_one.getDisplayName());
							Tribute_one.setFoodLevel(20);
							plugin.config.getString("Tribute_two_spawn");
							String[] twocoords = plugin.config.getString("Tribute_two_spawn").split(",");
							Player Tribute_two = plugin.getServer().getPlayerExact(plugin.Playing.get(1));
							double twox = Double.parseDouble(twocoords[0]);
							double twoy = Double.parseDouble(twocoords[1]);
							double twoz = Double.parseDouble(twocoords[2]);
							String twoworld = twocoords[3];
							World twow = plugin.getServer().getWorld(twoworld);
							Location twoloc = new Location(twow, twox, twoy, twoz);
							Tribute_two.teleport(twoloc);
							plugin.Frozen.add(Tribute_two.getDisplayName());
							Tribute_two.setFoodLevel(20);
							Tribute_one.getWorld().setTime(0);
							if(plugin.config.getString("Auto_Start").equalsIgnoreCase("true")){
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
									public void run(){
										Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha start");
									}
								}, 20L);
							}
							plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
								public void run(){
									sender.sendMessage(ChatColor.AQUA + "All Tributes warped!");
								}
							}, 20L);
						}
						if(plugin.Playing.size()>= 3){
							plugin.config.getString("Tribute_three_spawn");
							String[] coords = plugin.config.getString("Tribute_three_spawn").split(",");
							Player Tribute_three = plugin.getServer().getPlayerExact(plugin.Playing.get(2));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_three.teleport(loc);
							plugin.Frozen.add(Tribute_three.getDisplayName());
							Tribute_three.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 4){
							plugin.config.getString("Tribute_four_spawn");
							String[] coords = plugin.config.getString("Tribute_four_spawn").split(",");
							Player Tribute_four = plugin.getServer().getPlayerExact(plugin.Playing.get(3));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_four.teleport(loc);
							plugin.Frozen.add(Tribute_four.getDisplayName());
							Tribute_four.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 5){
							plugin.config.getString("Tribute_five_spawn");
							String[] coords = plugin.config.getString("Tribute_five_spawn").split(",");
							Player Tribute_five = plugin.getServer().getPlayerExact(plugin.Playing.get(4));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_five.teleport(loc);
							plugin.Frozen.add(Tribute_five.getDisplayName());
							Tribute_five.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 6){
							plugin.config.getString("Tribute_six_spawn");
							String[] coords = plugin.config.getString("Tribute_six_spawn").split(",");
							Player Tribute_six = plugin.getServer().getPlayerExact(plugin.Playing.get(5));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_six.teleport(loc);
							plugin.Frozen.add(Tribute_six.getDisplayName());
							Tribute_six.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 7){
							plugin.config.getString("Tribute_seven_spawn");
							String[] coords = plugin.config.getString("Tribute_seven_spawn").split(",");
							Player Tribute_seven = plugin.getServer().getPlayerExact(plugin.Playing.get(6));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_seven.teleport(loc);
							plugin.Frozen.add(Tribute_seven.getDisplayName());
							Tribute_seven.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 8){
							plugin.config.getString("Tribute_eight_spawn");
							String[] coords = plugin.config.getString("Tribute_eight_spawn").split(",");
							Player Tribute_eight = plugin.getServer().getPlayerExact(plugin.Playing.get(7));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_eight.teleport(loc);
							plugin.Frozen.add(Tribute_eight.getDisplayName());
							Tribute_eight.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 9){
							plugin.config.getString("Tribute_nine_spawn");
							String[] coords = plugin.config.getString("Tribute_nine_spawn").split(",");
							Player Tribute_nine = plugin.getServer().getPlayerExact(plugin.Playing.get(8));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_nine.teleport(loc);
							plugin.Frozen.add(Tribute_nine.getDisplayName());
							Tribute_nine.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 10){
							plugin.config.getString("Tribute_ten_spawn");
							String[] coords = plugin.config.getString("Tribute_ten_spawn").split(",");
							Player Tribute_ten = plugin.getServer().getPlayerExact(plugin.Playing.get(9));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_ten.teleport(loc);
							plugin.Frozen.add(Tribute_ten.getDisplayName());
							Tribute_ten.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 11){
							plugin.config.getString("Tribute_eleven_spawn");
							String[] coords = plugin.config.getString("Tribute_eleven_spawn").split(",");
							Player Tribute_eleven = plugin.getServer().getPlayerExact(plugin.Playing.get(10));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_eleven.teleport(loc);
							plugin.Frozen.add(Tribute_eleven.getDisplayName());
							Tribute_eleven.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 12){
							plugin.config.getString("Tribute_twelve_spawn");
							String[] coords = plugin.config.getString("Tribute_twelve_spawn").split(",");
							Player Tribute_twelve = plugin.getServer().getPlayerExact(plugin.Playing.get(11));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_twelve.teleport(loc);
							plugin.Frozen.add(Tribute_twelve.getDisplayName());
							Tribute_twelve.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 13){
							plugin.config.getString("Tribute_thirteen_spawn");
							String[] coords = plugin.config.getString("Tribute_thirteen_spawn").split(",");
							Player Tribute_thirteen = plugin.getServer().getPlayerExact(plugin.Playing.get(12));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_thirteen.teleport(loc);
							plugin.Frozen.add(Tribute_thirteen.getDisplayName());
							Tribute_thirteen.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 14){
							plugin.config.getString("Tribute_fourteen_spawn");
							String[] coords = plugin.config.getString("Tribute_fourteen_spawn").split(",");
							Player Tribute_fourteen = plugin.getServer().getPlayerExact(plugin.Playing.get(13));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_fourteen.teleport(loc);
							plugin.Frozen.add(Tribute_fourteen.getDisplayName());
							Tribute_fourteen.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 15){
							plugin.config.getString("Tribute_fifteen_spawn");
							String[] coords = plugin.config.getString("Tribute_fifteen_spawn").split(",");
							Player Tribute_fifteen = plugin.getServer().getPlayerExact(plugin.Playing.get(14));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_fifteen.teleport(loc);
							plugin.Frozen.add(Tribute_fifteen.getDisplayName());
							Tribute_fifteen.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 16){
							plugin.config.getString("Tribute_sixteen_spawn");
							String[] coords = plugin.config.getString("Tribute_sixteen_spawn").split(",");
							Player Tribute_sixteen = plugin.getServer().getPlayerExact(plugin.Playing.get(15));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_sixteen.teleport(loc);
							plugin.Frozen.add(Tribute_sixteen.getDisplayName());
							Tribute_sixteen.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 17){
							plugin.config.getString("Tribute_seventeen_spawn");
							String[] coords = plugin.config.getString("Tribute_seventeen_spawn").split(",");
							Player Tribute_seventeen = plugin.getServer().getPlayerExact(plugin.Playing.get(16));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_seventeen.teleport(loc);
							plugin.Frozen.add(Tribute_seventeen.getDisplayName());
							Tribute_seventeen.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 18){
							plugin.config.getString("Tribute_eighteen_spawn");
							String[] coords = plugin.config.getString("Tribute_eighteen_spawn").split(",");
							Player Tribute_eighteen = plugin.getServer().getPlayerExact(plugin.Playing.get(17));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_eighteen.teleport(loc);
							plugin.Frozen.add(Tribute_eighteen.getDisplayName());
							Tribute_eighteen.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 19){
							plugin.config.getString("Tribute_nineteen_spawn");
							String[] coords = plugin.config.getString("Tribute_nineteen_spawn").split(",");
							Player Tribute_nineteen = plugin.getServer().getPlayerExact(plugin.Playing.get(18));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_nineteen.teleport(loc);
							plugin.Frozen.add(Tribute_nineteen.getDisplayName());
							Tribute_nineteen.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 20){
							plugin.config.getString("Tribute_twenty_spawn");
							String[] coords = plugin.config.getString("Tribute_twenty_spawn").split(",");
							Player Tribute_twenty = plugin.getServer().getPlayerExact(plugin.Playing.get(19));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_twenty.teleport(loc);
							plugin.Frozen.add(Tribute_twenty.getDisplayName());
							Tribute_twenty.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 21){
							plugin.config.getString("Tribute_twentyone_spawn");
							String[] coords = plugin.config.getString("Tribute_twentyone_spawn").split(",");
							Player Tribute_twentyone = plugin.getServer().getPlayerExact(plugin.Playing.get(20));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_twentyone.teleport(loc);
							plugin.Frozen.add(Tribute_twentyone.getDisplayName());
							Tribute_twentyone.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 22){
							plugin.config.getString("Tribute_twentytwo_spawn");
							String[] coords = plugin.config.getString("Tribute_twentytwo_spawn").split(",");
							Player Tribute_twentytwo = plugin.getServer().getPlayerExact(plugin.Playing.get(21));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_twentytwo.teleport(loc);
							plugin.Frozen.add(Tribute_twentytwo.getDisplayName());
							Tribute_twentytwo.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 23){
							plugin.config.getString("Tribute_twentythree_spawn");
							String[] coords = plugin.config.getString("Tribute_twentythree_spawn").split(",");
							Player Tribute_twentythree = plugin.getServer().getPlayerExact(plugin.Playing.get(22));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_twentythree.teleport(loc);
							plugin.Frozen.add(Tribute_twentythree.getDisplayName());
							Tribute_twentythree.setFoodLevel(20);
						}
						if(plugin.Playing.size()>= 24){
							plugin.config.getString("Tribute_twentyfour_spawn");
							String[] coords = plugin.config.getString("Tribute_twentyfour_spawn").split(",");
							Player Tribute_twentyfour = plugin.getServer().getPlayerExact(plugin.Playing.get(23));
							double x = Double.parseDouble(coords[0]);
							double y = Double.parseDouble(coords[1]);
							double z = Double.parseDouble(coords[2]);
							String world = coords[3];
							World w = plugin.getServer().getWorld(world);
							Location loc = new Location(w, x, y, z);
							Tribute_twentyfour.teleport(loc);
							plugin.Frozen.add(Tribute_twentyfour.getDisplayName());
							Tribute_twentyfour.setFoodLevel(20);
						}
					}
				}else if(args[0].equalsIgnoreCase("Start")){
					String begin = plugin.config.getString("Start_Message");
					begin = begin.replaceAll("(&([a-f0-9]))", "\u00A7$2");
					final String msg = begin;
					if(plugin.config.getString("Countdown").equalsIgnoreCase("true")){
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("10");
							}
						}, 20L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("9");
							}
						}, 40L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("8");
							}
						}, 60L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("7");
							}
						}, 80L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("6");
							}
						}, 100L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("5");
							}
						}, 120L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("4");
							}
						}, 140L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("3");
							}
						}, 160L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("2");
							}
						}, 180L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.getServer().broadcastMessage("1");
							}
						}, 200L);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
							public void run(){
								plugin.Frozen.clear();
								plugin.getServer().broadcastMessage(msg);
								plugin.canjoin = true;
							}
						}, 220L);
					}else{
						plugin.Frozen.clear();
						plugin.getServer().broadcastMessage(msg);
						plugin.canjoin = true;
					}
				}else{
					sender.sendMessage(ChatColor.RED + "Unknown command, type /ha help to see all commands!");
				}
			}
		}
		return false;
	}
}

