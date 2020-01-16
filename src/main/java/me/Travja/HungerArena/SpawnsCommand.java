package me.Travja.HungerArena;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnsCommand implements CommandExecutor {
	public Main plugin;
	int i = 0;
	int a = 0;
	boolean NoPlayerSpawns;
	public SpawnsCommand(Main m) {
		this.plugin = m;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		String ThisWorld = p.getWorld().getName();
		NoPlayerSpawns = true;
		for(int i : plugin.worldsNames.keySet()){
			if(plugin.worldsNames.get(i)!= null){
				if (plugin.worldsNames.get(i).equals(ThisWorld)){ 
					a=i;
					NoPlayerSpawns = false;
					break;
				}
			}
		}
		if(cmd.getName().equalsIgnoreCase("StartPoint")){
			if(p.hasPermission("HungerArena.StartPoint")){
					Location loc = null;
					double x;
					double y;
					double z;
					if(args.length == 6){
						try{
							i = Integer.valueOf(args[1]);
							a = Integer.valueOf(args[0]);
						}catch(Exception e){
							p.sendMessage(ChatColor.RED + "Argument not an integer!");
							return true;
						}

						String world = args[2];
						x = Double.parseDouble(args[3]);
						y = Double.parseDouble(args[4]);
						z = Double.parseDouble(args[5]);
						loc = new Location(Bukkit.getWorld(world), x, y, z);
						if(plugin.location.get(a)!= null){
							plugin.location.get(a).put(i, loc);
						}else{
							plugin.location.put(a, new HashMap<Integer, Location>());	
							plugin.location.get(a).put(i, loc);
							plugin.Playing.put(a, new ArrayList<String>());
							plugin.Ready.put(a, new ArrayList<String>());
							plugin.Dead.put(a, new ArrayList<String>());
							plugin.quit.put(a, new ArrayList<String>());
							plugin.out.put(a, new ArrayList<String>());
							plugin.watching.put(a, new ArrayList<String>());
							plugin.needConfirm.put(a, new ArrayList<String>());
							plugin.inArena.put(a, new ArrayList<String>());
							plugin.frozen.put(a, new ArrayList<String>());
							plugin.arena.put(a, new ArrayList<String>());
							plugin.canjoin.put(a, false);
							plugin.matchRunning.put(a, null);
							plugin.open.put(a, true);
						}
						String coords = loc.getWorld().getName() + "," + (loc.getX()) + "," + loc.getY() + "," + (loc.getZ()); 
						p.sendMessage(coords);
						plugin.spawns.set("Spawns." + a + "." + i, coords);
						plugin.worldsNames.put(a, loc.getWorld().getName());
						plugin.saveSpawns();
						plugin.maxPlayers.put(a, plugin.location.get(a).size());
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute " + i + " in arena " + a + "!");
						this.plugin.reloadSpawnpoints(false); 
						return true;
					}
					if(args.length>= 2){
						try{
							i = Integer.valueOf(args[1]);
							a = Integer.valueOf(args[0]);
						}catch(Exception e){
							p.sendMessage(ChatColor.RED + "Argument not an integer!");
							return true; 
						}
						if(i >= 1 && i <= plugin.config.getInt("maxPlayers")){
							if(!plugin.worldsNames.values().contains(p.getWorld().getName())){
								p.sendMessage(ChatColor.GOLD + "You've added this world to the config ...");
							}
							loc = p.getLocation().getBlock().getLocation();
							x = loc.getX()+.5;
							y = loc.getY();
							z = loc.getZ()+.5;
							loc = new Location(loc.getWorld(), x, y, z);
							if(plugin.location.get(a)!= null){
								plugin.location.get(a).put(i, loc);
							}else{
								plugin.location.put(a, new HashMap<Integer, Location>());	
								plugin.location.get(a).put(i, loc);
								plugin.Playing.put(a, new ArrayList<String>());
								plugin.Ready.put(a, new ArrayList<String>());
								plugin.Dead.put(a, new ArrayList<String>());
								plugin.quit.put(a, new ArrayList<String>());
								plugin.out.put(a, new ArrayList<String>());
								plugin.watching.put(a, new ArrayList<String>());
								plugin.needConfirm.put(a, new ArrayList<String>());
								plugin.inArena.put(a, new ArrayList<String>());
								plugin.frozen.put(a, new ArrayList<String>());
								plugin.arena.put(a, new ArrayList<String>());
								plugin.canjoin.put(a, false);
								plugin.matchRunning.put(a, null);
								plugin.open.put(a, true);
							}
							String coords = loc.getWorld().getName() + "," + (loc.getX()) + "," + loc.getY() + "," + (loc.getZ()); 
							p.sendMessage(coords);
							plugin.spawns.set("Spawns." + a + "." + i, coords);
							plugin.worldsNames.put(a, loc.getWorld().getName());
							plugin.saveSpawns();
							plugin.maxPlayers.put(a, plugin.location.get(a).size());
							p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute " + i + " in arena " + a + "!");
							this.plugin.reloadSpawnpoints(false);
						}else{
							p.sendMessage(ChatColor.RED + "You can't go past " + plugin.config.getInt("maxPlayers") + " players!");
						}
					}else if(args.length == 1){	
						if (sender instanceof Player){
							p = (Player) sender;
							if(NoPlayerSpawns){
								try{
									a = Integer.parseInt(args[0]);
								}catch(Exception e){
									p.sendMessage(ChatColor.RED + "Argument not an integer!"); 
									return true;
								}
							}
							if (plugin.spawns.get("Spawns." + a) != null){
								int start = 1;
								while(start<plugin.config.getInt("maxPlayers")+2){
									if (plugin.spawns.get("Spawns." + a + "." + start) != null){
										start = start+1;
										if(start== plugin.config.getInt("maxPlayers")+1){
											p.sendMessage(ChatColor.DARK_AQUA + "[HungerArena] " + ChatColor.GREEN + "All spawns set, type /startpoint [Arena #] [Spawn #] to over-ride previous points!");
											return true;
										}
									}else{
										int sloc = start;
										start = plugin.config.getInt("maxPlayers")+1;
										p.sendMessage(ChatColor.DARK_AQUA + "[HungerArena] " + ChatColor.RED + "Begin Setting For Arena " + a + " Starting From Point " + sloc);
										plugin.setting.put(p.getName(), a + "-" + sloc);
										return true;
									}
								}
							}
							p.sendMessage(ChatColor.DARK_AQUA + "[HungerArena] " + ChatColor.RED + "Begin Setting For Arena " + a + " Starting From Point " + 1);
							plugin.setting.put(p.getName(), a + "-" + 1);
							return true;
						}else{
							sender.sendMessage(ChatColor.BLUE + "This Can Only Be Sent As A Player");
						}
					} else {
						p.sendMessage(ChatColor.RED + "No argument given! \nUse command like this:\n/startpoint [Arena #] [Startpoint #] for setting your position as a startpoint.\n/startpoint [Arena #] [Startpoint #] [Mapname] [x] [y] [z] \nOr you can use /startpoint [Arena #] to use the 'spawntool': ID"+ plugin.config.getInt("spawnsTool") +" for setting the startpoints!"); 
						return false;
					}
			}else{
				p.sendMessage(ChatColor.RED + "You don't have permission!");
			}
		}
		return false;
	}

}
