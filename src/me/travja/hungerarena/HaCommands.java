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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HaCommands implements CommandExecutor {
	public Main plugin;
	public HaCommands(Main m) {
		this.plugin = m;
	}
	int i = 0;
	int start = 0;
	int cstart = 0;
	@SuppressWarnings("deprecation")
	private void clearInv(Player p){
		p.getInventory().clear();
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setHelmet(null);
		p.getInventory().setLeggings(null);
		p.updateInventory();
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(final CommandSender sender, Command cmd, String commandLabel, String[] args){
		String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		String spawnworld = Spawncoords[3];
		World spawnw = plugin.getServer().getWorld(spawnworld);
		Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
		if(sender instanceof Player){
			final Player p = (Player) sender;
			final String pname = p.getName();
			ChatColor c = ChatColor.AQUA;
			if(cmd.getName().equalsIgnoreCase("Ha")){
				if(args.length== 0){
					p.sendMessage(ChatColor.GREEN + "[HungerArena] by " + ChatColor.AQUA + "travja!");
					return false;
				}else if(args[0].equalsIgnoreCase("SetSpawn")){
					if(p.hasPermission("HungerArena.SetSpawn")){
						double x = p.getLocation().getX();
						double y = p.getLocation().getY();
						double z = p.getLocation().getZ();
						String w = p.getWorld().getName();
						plugin.spawns.set("Spawn_coords", x + "," + y + "," + z + "," + w);
						plugin.spawns.set("Spawns_set", "true");
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn for dead tributes!");
					}else{
						p.sendMessage(ChatColor.RED + "You don't have permission!");
					}
				}else if(args[0].equalsIgnoreCase("Help")){
					p.sendMessage(ChatColor.GREEN + "----HungerArena Help----");
					sender.sendMessage(c + "/ha - Displays author message!");
					sender.sendMessage(c + "/sponsor [Player] [ItemID] [Amount] - Lets you sponsor someone!");
					sender.sendMessage(c + "/startpoint [1,2,3,4,etc] - Sets the starting points of tributes!");
					sender.sendMessage(c + "/ha close - Prevents anyone from joining!");
					sender.sendMessage(c + "/ha help - Displays this screen!");
					sender.sendMessage(c + "/ha join - Makes you join the game!");
					sender.sendMessage(c + "/ha kick [Player] - Kicks a player from the arena!");
					sender.sendMessage(c + "/ha leave - Makes you leave the game!");
					sender.sendMessage(c + "/ha list - Shows a list of players in the game and their health!");
					sender.sendMessage(c + "/ha open - Opens the game allowing people to join!");
					sender.sendMessage(c + "/ha ready - Votes for the game to start!");
					sender.sendMessage(c + "/ha refill - Refills all chests!");
					sender.sendMessage(c + "/ha reload - Reloads the config!");
					sender.sendMessage(c + "/ha restart - Makes it so dead tributes can join again!");
					sender.sendMessage(c + "/ha rlist - See who's ready!");
					sender.sendMessage(c + "/ha setspawn - Sets the spawn for dead tributes!");
					sender.sendMessage(c + "/ha start - Unfreezes tributes allowing them to fight!");
					sender.sendMessage(c + "/ha watch - Lets you watch the tributes!");
					sender.sendMessage(c + "/ha warpall - Warps all tribute into position!");
					sender.sendMessage(ChatColor.GREEN + "----------------------");
				}else if(plugin.restricted && !plugin.worlds.contains(p.getWorld().getName())){
					p.sendMessage(ChatColor.RED + "That can't be run in this world!");
				}else if((plugin.restricted && plugin.worlds.contains(p.getWorld().getName())) || !plugin.restricted){
					//////////////////////////////////////// LISTING ///////////////////////////////////////////////
					if(args[0].equalsIgnoreCase("List")){
						if(p.hasPermission("HungerArena.GameMaker")){
							sender.sendMessage(ChatColor.AQUA + "-----People Playing-----");
							if(!plugin.Playing.isEmpty()){
								for(String playernames: plugin.Playing){
									Player players = plugin.getServer().getPlayerExact(playernames);
									sender.sendMessage(ChatColor.GREEN + playernames + " Life: " + players.getHealth() + "/20");
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
						////////////////////////////////////////////////////////////////////////////////////////////////
						///////////////////////////////////// JOINING/LEAVING //////////////////////////////////////////
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
							}else if(!plugin.open){
								p.sendMessage(ChatColor.RED + "The game is closed!");
							}else if(plugin.spawns.getString("Spawns_set").equalsIgnoreCase("false")){
								p.sendMessage(ChatColor.RED + "/ha setspawn hasn't been run!");
							}else if(plugin.NeedConfirm.contains(pname)){
								p.sendMessage(ChatColor.RED + "You need to do /ha confirm");
							}else if(plugin.config.getString("Need_Confirm").equalsIgnoreCase("true")){
								plugin.NeedConfirm.add(pname);
								p.sendMessage(ChatColor.GOLD + "You're inventory will be cleared! Type /ha confirm to procede");
							}else{
								plugin.Playing.add(pname);
								clearInv(p);
								plugin.getServer().broadcastMessage(ChatColor.AQUA + pname +  " has Joined the Game!");
								if(plugin.Playing.size()== 24){
									Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha warpall");
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
							clearInv(p);
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
								if(plugin.Playing.size()-4== plugin.Ready.size() || plugin.Playing.size()==plugin.Ready.size()){
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
							clearInv(p);
							p.teleport(Spawn);
							if(plugin.Frozen.contains(pname)){
								plugin.Frozen.remove(pname);
							}
						}else{
							plugin.Playing.remove(pname);
							plugin.Quit.add(pname);
							p.sendMessage(ChatColor.AQUA + "You have left the game!");
							p.getServer().broadcastMessage(ChatColor.RED + pname + " Quit!");
							clearInv(p);
							p.teleport(Spawn);
							if(plugin.Frozen.contains(pname)){
								plugin.Frozen.remove(pname);
							}
							if(plugin.Playing.size()== 1){
								//Announce the Winner
								for(i = 0; i < plugin.Playing.size(); i++){
									String winnername = plugin.Playing.get(i++);
									Player winner = plugin.getServer().getPlayerExact(winnername);
									String winnername2 = winner.getName();
									plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
									clearInv(winner);
									winner.teleport(Spawn);
									winner.getInventory().addItem(plugin.Reward);
									Bukkit.getServer().getPluginManager().callEvent(new PlayerWinGamesEvent(winner));
								}
								plugin.Playing.clear();
								//Show spectators
								for(i = 0; i < plugin.Watching.size(); i++){
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
						}
						////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
								p.sendMessage(ChatColor.AQUA + "You are not spectating any more");
							}
						}else{
							p.sendMessage(ChatColor.RED + "You don't have permission!");
						}
					}else if(args[0].equalsIgnoreCase("Kick")){
						if (args.length != 2) {
							return false;
						}
						Player target = plugin.getServer().getPlayer(args[1]);
						if(sender.hasPermission("HungerArena.Kick")){
							if(plugin.Playing.contains(target.getName())){
								plugin.Playing.remove(target.getName());
								plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " was kicked from the game!");
								clearInv(target);
								target.teleport(Spawn);
								plugin.Quit.add(target.getName());
								if(plugin.Playing.size()== 1 && plugin.canjoin== true){
									//Announce winner
									for(i = 0; i < plugin.Playing.size(); i++){
										String winnername = plugin.Playing.get(i++);
										Player winner = plugin.getServer().getPlayerExact(winnername);
										String winnername2 = winner.getName();
										plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
										clearInv(winner);
										winner.teleport(Spawn);
										winner.getInventory().addItem(plugin.Reward);
										plugin.Playing.clear();
									}
									//Show spectators
									for(String s1: plugin.Watching){
										Player spectator = plugin.getServer().getPlayerExact(s1);
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
									chest.update();
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
								for(i = 0; i < plugin.Watching.size(); i++){
									String s = plugin.Watching.get(i++);
									Player spectator = plugin.getServer().getPlayerExact(s);
									spectator.setAllowFlight(false);
									spectator.teleport(Spawn);
									for(Player online:plugin.getServer().getOnlinePlayers()){
										online.showPlayer(spectator);
									}
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
							List<String> blocksbroken = plugin.data.getStringList("Blocks_Destroyed");
							List<String> blocksplaced = plugin.data.getStringList("Blocks_Placed");
							for(String blocks:blocksplaced){
								String[] coords = blocks.split(",");
								World w = plugin.getServer().getWorld(coords[0]);
								double x = Double.parseDouble(coords[1]);
								double y = Double.parseDouble(coords[2]);
								double z = Double.parseDouble(coords[3]);
								int d = 0;
								byte m = 0;
								Location blockl = new Location(w, x, y, z);
								Block block = w.getBlockAt(blockl);
								block.setTypeIdAndData(d, m, true);
								block.getState().update();
							}
							for(String blocks:blocksbroken){
								String[] coords = blocks.split(",");
								World w = plugin.getServer().getWorld(coords[0]);
								double x = Double.parseDouble(coords[1]);
								double y = Double.parseDouble(coords[2]);
								double z = Double.parseDouble(coords[3]);
								int d = Integer.parseInt(coords[4]);
								byte m = Byte.parseByte(coords[5]);
								Location blockl = new Location(w, x, y, z);
								Block block = w.getBlockAt(blockl);
								block.setTypeIdAndData(d, m, true);
								block.getState().update();
							}
							blocksplaced.clear();
							blocksbroken.clear();
							plugin.data.set("Blocks_Destroyed", blocksbroken);
							plugin.data.set("Blocks_Placed", blocksplaced);
							plugin.data.options().copyDefaults();
							plugin.saveData();
							p.performCommand("ha refill");
							p.sendMessage(ChatColor.AQUA + "The games have been reset!");
						}else{
							p.sendMessage(ChatColor.RED + "You don't have permission!");
						}
						/////////////////////////////////// Toggle //////////////////////////////////////////////////
					}else if(args[0].equalsIgnoreCase("close")){
						if(p.hasPermission("HungerArena.toggle")){
							if(plugin.open){
								plugin.open = false;
								for(String players: plugin.Playing){
									Player tributes = plugin.getServer().getPlayerExact(players);
									tributes.teleport(tributes.getWorld().getSpawnLocation());
									clearInv(p);
								}
								for(String sname: plugin.Watching){
									Player spectators = plugin.getServer().getPlayerExact(sname);
									spectators.teleport(spectators.getWorld().getSpawnLocation());
									spectators.setAllowFlight(false);
									for(Player online:plugin.getServer().getOnlinePlayers()){
										online.showPlayer(spectators);
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
								p.performCommand("ha refill");
								p.sendMessage(ChatColor.GOLD + "Games Closed!");
							}else{
								p.sendMessage(ChatColor.RED + "Games already closed, type /ha open to re-open them!");
							}
						}else{
							p.sendMessage(ChatColor.RED + "No Perms!");
						}
					}else if(args[0].equalsIgnoreCase("open")){
						if(p.hasPermission("HungerArena.toggle")){
							if(!plugin.open){
								plugin.open = true;
								p.sendMessage(ChatColor.GOLD + "Games Opened!!");
							}else{
								p.sendMessage(ChatColor.RED + "Games already open, type /ha close to close them!");
							}
						}else{
							p.sendMessage(ChatColor.RED + "No Perms!");
						}
						////////////////////////////////////////////////////////////////////////////////////////////
					}else if(args[0].equalsIgnoreCase("Reload")){
						plugin.reloadConfig();
						p.sendMessage(ChatColor.AQUA + "HungerArena Reloaded!");
					}else if(args[0].equalsIgnoreCase("WarpAll")){
						if(p.hasPermission("HungerArena.Warpall")){
							if(plugin.spawns.getString("Spawns_set").equalsIgnoreCase("false")){
								sender.sendMessage(ChatColor.RED + "/ha setspawn hasn't been run!");
							}else{
								//TODO Make warping way more efficient
								if(plugin.Playing.size()== 1){
									sender.sendMessage(ChatColor.RED + "There are not enough players!");
								}else{
									if(plugin.config.getString("Auto_Start").equalsIgnoreCase("true")){
										plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
											public void run(){
												Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha start");
											}
										}, 20L);
									}
									for(String playing:plugin.Playing){
										Player tribute = plugin.getServer().getPlayerExact(playing);
										tribute.teleport(plugin.location.get(i));
										tribute.setHealth(20);
										tribute.setFoodLevel(20);
										tribute.setSaturation(20);
										clearInv(p);
										for(PotionEffect pe: tribute.getActivePotionEffects()){
											PotionEffectType potion = pe.getType();
											tribute.removePotionEffect(potion);
										}
										if(tribute.getAllowFlight()){
											tribute.setAllowFlight(false);
										}
										plugin.Frozen.add(tribute.getName());
										i = i+1;
									}
									plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
										public void run(){
											p.sendMessage(ChatColor.AQUA + "All Tributes warped!");
										}
									}, 20L);
								}
							}
						}
					}else if(args[0].equalsIgnoreCase("Start")){
						String begin = plugin.config.getString("Start_Message");
						begin = begin.replaceAll("(&([a-f0-9]))", "\u00A7$2");
						final String msg = begin;
						i = 10;
						if(p.hasPermission("HungerArena.Start")){
							if(plugin.config.getString("Countdown").equalsIgnoreCase("true")){
								start = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
									public void run(){
										for(Player wp: p.getWorld().getPlayers()){
											wp.sendMessage(String.valueOf(i));
										}
										i = i-1;
										plugin.canjoin = true;
										if(i== 0){
											plugin.Frozen.clear();
											plugin.getServer().broadcastMessage(msg);
											plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha Refill");
											plugin.getServer().getScheduler().cancelTask(start);
										}
									}
								}, 20L, 20L);
							}else{
								plugin.Frozen.clear();
								p.getServer().broadcastMessage(msg);
								plugin.canjoin = true;
								plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha Refill");
							}
						}else{
							p.sendMessage(ChatColor.RED + "You don't have permission!");
						}
					}else{
						p.sendMessage(ChatColor.RED + "Unknown command, type /ha help for a list of commands");
					}
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
					sender.sendMessage(c + "/sponsor [Player] [ItemID] [Amount] - Lets you sponsor someone!");
					sender.sendMessage(c + "/startpoint [1,2,3,4,etc] - Sets the starting points of tributes!");
					sender.sendMessage(c + "/ha close - Prevents anyone from joining!");
					sender.sendMessage(c + "/ha help - Displays this screen!");
					sender.sendMessage(c + "/ha join - Makes you join the game!");
					sender.sendMessage(c + "/ha kick [Player] - Kicks a player from the arena!");
					sender.sendMessage(c + "/ha leave - Makes you leave the game!");
					sender.sendMessage(c + "/ha list - Shows a list of players in the game and their health!");
					sender.sendMessage(c + "/ha open - Opens the game allowing people to join!");
					sender.sendMessage(c + "/ha ready - Votes for the game to start!");
					sender.sendMessage(c + "/ha refill - Refills all chests!");
					sender.sendMessage(c + "/ha reload - Reloads the config!");
					sender.sendMessage(c + "/ha restart - Makes it so dead tributes can join again!");
					sender.sendMessage(c + "/ha rlist - See who's ready!");
					sender.sendMessage(c + "/ha setspawn - Sets the spawn for dead tributes!");
					sender.sendMessage(c + "/ha start - Unfreezes tributes allowing them to fight!");
					sender.sendMessage(c + "/ha watch - Lets you watch the tributes!");
					sender.sendMessage(c + "/ha warpall - Warps all tribute into position!");
					sender.sendMessage(ChatColor.GREEN + "----------------------");
					return false;
				}else if(args[0].equalsIgnoreCase("List")){
					sender.sendMessage(ChatColor.AQUA + "-----People Playing-----");
					if(!plugin.Playing.isEmpty()){
						for(String playernames: plugin.Playing){
							Player players = plugin.getServer().getPlayerExact(playernames);
							sender.sendMessage(ChatColor.GREEN + players.getName() + " Life: " + players.getHealth() + "/20");
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
					if(plugin.Playing.contains(target.getName())){
						plugin.Playing.remove(target.getName());
						plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " was kicked from the game!");
						clearInv(target);
						target.teleport(Spawn);
						plugin.Quit.add(target.getName());
						if(plugin.Playing.size()== 1 && plugin.canjoin== true){
							//Announce winner
							for(i = 0; i < plugin.Playing.size(); i++){
								String winnername = plugin.Playing.get(i++);
								Player winner = plugin.getServer().getPlayerExact(winnername);
								String winnername2 = winner.getName();
								plugin.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
								clearInv(winner);
								winner.teleport(Spawn);
								winner.getInventory().addItem(plugin.Reward);
								plugin.Playing.clear();
							}
							//Show spectators
							for(String s1: plugin.Watching){
								Player spectator = plugin.getServer().getPlayerExact(s1);
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
							chest.update();
						}
					}
					if(limit== list056){
						sender.sendMessage(ChatColor.GREEN + "All chests refilled!");
					}
				}else if(args[0].equalsIgnoreCase("Restart")){
					if(!plugin.Watching.isEmpty()){
						for(i = 0; i < plugin.Watching.size(); i++){
							String s = plugin.Watching.get(i++);
							Player spectator = plugin.getServer().getPlayerExact(s);
							spectator.setAllowFlight(false);
							spectator.teleport(Spawn);
							for(Player online:plugin.getServer().getOnlinePlayers()){
								online.showPlayer(spectator);
							}
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
					List<String> blocksbroken = plugin.data.getStringList("Blocks_Destroyed");
					List<String> blocksplaced = plugin.data.getStringList("Blocks_Placed");
					for(String blocks:blocksplaced){
						String[] coords = blocks.split(",");
						World w = plugin.getServer().getWorld(coords[0]);
						double x = Double.parseDouble(coords[1]);
						double y = Double.parseDouble(coords[2]);
						double z = Double.parseDouble(coords[3]);
						int d = 0;
						byte m = 0;
						Location blockl = new Location(w, x, y, z);
						Block block = w.getBlockAt(blockl);
						block.setTypeIdAndData(d, m, true);
						block.getState().update();
					}
					for(String blocks:blocksbroken){
						String[] coords = blocks.split(",");
						World w = plugin.getServer().getWorld(coords[0]);
						double x = Double.parseDouble(coords[1]);
						double y = Double.parseDouble(coords[2]);
						double z = Double.parseDouble(coords[3]);
						int d = Integer.parseInt(coords[4]);
						byte m = Byte.parseByte(coords[5]);
						Location blockl = new Location(w, x, y, z);
						Block block = w.getBlockAt(blockl);
						block.setTypeIdAndData(d, m, true);
						block.getState().update();
					}
					blocksplaced.clear();
					blocksbroken.clear();
					plugin.data.set("Blocks_Destroyed", blocksbroken);
					plugin.data.set("Blocks_Placed", blocksplaced);
					plugin.data.options().copyDefaults();
					plugin.saveData();
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha refill");
					sender.sendMessage(ChatColor.AQUA + "The games have been reset!");
					/////////////////////////////////// Toggle //////////////////////////////////////////////////
				}else if(args[0].equalsIgnoreCase("close")){
					if(plugin.open){
						plugin.open = false;
						for(String players: plugin.Playing){
							Player tributes = plugin.getServer().getPlayerExact(players);
							tributes.teleport(tributes.getWorld().getSpawnLocation());
							clearInv(tributes);
						}
						for(String sname: plugin.Watching){
							Player spectators = plugin.getServer().getPlayerExact(sname);
							spectators.teleport(spectators.getWorld().getSpawnLocation());
							spectators.setAllowFlight(false);
							for(Player online:plugin.getServer().getOnlinePlayers()){
								online.showPlayer(spectators);
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
						Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha refill");
						sender.sendMessage(ChatColor.GOLD + "Games Closed!");
					}else{
						sender.sendMessage(ChatColor.RED + "Games alredy close, type /ha open to re-open them!");
					}
				}else if(args[0].equalsIgnoreCase("open")){
					if(!plugin.open){
						plugin.open = true;
						sender.sendMessage(ChatColor.GOLD + "Games Opened!!");
					}else{
						sender.sendMessage(ChatColor.RED + "Games already open, type /ha close to close them!");
					}
					////////////////////////////////////////////////////////////////////////////////////////////
				}else if(args[0].equalsIgnoreCase("Reload")){
					plugin.reloadConfig();
					sender.sendMessage(ChatColor.AQUA + "HungerArena Reloaded!");
				}else if(args[0].equalsIgnoreCase("WarpAll")){
					if(plugin.spawns.getString("Spawns_set").equalsIgnoreCase("false")){
						sender.sendMessage(ChatColor.RED + "/ha setspawn hasn't been run!");
					}else{
						//TODO Make warping way more efficient
						if(plugin.Playing.size()== 1){
							sender.sendMessage(ChatColor.RED + "There are not enough players!");
						}else{
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
							for(String playing:plugin.Playing){
								Player tribute = plugin.getServer().getPlayerExact(playing);
								tribute.teleport(plugin.location.get(i));
								tribute.setHealth(20);
								tribute.setFoodLevel(20);
								tribute.setSaturation(20);
								clearInv(tribute);
								for(PotionEffect pe: tribute.getActivePotionEffects()){
									PotionEffectType potion = pe.getType();
									tribute.removePotionEffect(potion);
								}
								if(tribute.getAllowFlight()){
									tribute.setAllowFlight(false);
								}
								plugin.Frozen.add(tribute.getName());
								i = i+1;
							}
						}

					}
				}else if(args[0].equalsIgnoreCase("Start")){
					String begin = plugin.config.getString("Start_Message");
					begin = begin.replaceAll("(&([a-f0-9]))", "\u00A7$2");
					final String msg = begin;
					if(plugin.config.getString("Countdown").equalsIgnoreCase("true")){
						cstart = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
							public void run(){
								List<String> worlds = plugin.config.getStringList("worlds");
								if(worlds.isEmpty()){
									sender.getServer().broadcastMessage(String.valueOf(i));
								}else{
									for(String world: worlds){
										World w = plugin.getServer().getWorld(world);
										for(Player wp: w.getPlayers()){
											wp.sendMessage(String.valueOf(i));
										}
									}
								}
								i = i-1;
								plugin.canjoin = true;
								if(i== 0){
									plugin.Frozen.clear();
									plugin.getServer().broadcastMessage(msg);
									plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha Refill");
									plugin.getServer().getScheduler().cancelTask(cstart);
								}
							}
						}, 20L, 20L);
					}else{
						plugin.Frozen.clear();
						sender.getServer().broadcastMessage(msg);
						plugin.canjoin = true;
						plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha Refill");
					}
				}else{
					sender.sendMessage(ChatColor.RED + "Unknown command, type /ha help to see all commands!");
				}
			}
		}
		return false;
	}
}
