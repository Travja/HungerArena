package me.Travja.HungerArena.Listeners;

import java.util.HashMap;
import java.util.List;

import me.Travja.HungerArena.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

public class DeathListener implements Listener{
	public Main plugin;
	public DeathListener(Main m){
		this.plugin = m;
	}
	public FileConfiguration config;
	int i = 0;
	int a = 0;
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event){
		final Player p = event.getPlayer();
		String pname = p.getName();

		//get the arena the player has died in...	(may be not the one he joined...)
		String ThisWorld = p.getWorld().getName();
		for(int z : plugin.worldsNames.keySet()){
			if(plugin.worldsNames.get(z)!= null){	
				if (plugin.worldsNames.get(z).equals(ThisWorld)){
					a=z;
				}
			}
		}
		
		//Jeppa: Fix for lonely players :)
		for(int i : plugin.Dead.keySet()){ 
			if ((plugin.Dead.get(i) != null) &&	(plugin.Dead.get(i).contains(pname)) &&	(plugin.MatchRunning.get(i) == null)) { 
				plugin.Dead.get(i).clear();
			}
		}
		
		for(int i = 0; i < plugin.needInv.size(); i++){ 
			if(plugin.needInv.contains(pname)){
				RespawnDeadPlayer(p,a); 
			}
		}
	}
	private void RespawnDeadPlayer(Player p, int a){
		final Player player = p;
		String[] Spawncoords = plugin.spawns.getString("Spawn_coords."+a).split(","); 
		World spawnw = plugin.getServer().getWorld(Spawncoords[3]);
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				player.teleport(Spawn);
				plugin.RestoreInv(player, player.getName()); 
			}
		}, 10L);
	}

	@EventHandler (priority = EventPriority.HIGHEST) 
	public void onPlayerDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		Server s = p.getServer();
		String pname = p.getName();
		if(plugin.getArena(p)!= null){
			a = plugin.getArena(p);
			int players = plugin.Playing.get(a).size()-1;
			String leftmsg = null;
			clearInv(p);
			event.getDrops().clear();
			p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			plugin.scoreboards.remove(p.getName());
			if(!plugin.Frozen.get(a).isEmpty()){
				if(plugin.Frozen.get(a).contains(pname)){
					if(!(p.getKiller() instanceof Player)){
						players = plugin.Playing.get(a).size()-1;
						leftmsg = ChatColor.BLUE + "There are now " + players + " tributes left!";
						if(plugin.config.getString("Cannon_Death").equalsIgnoreCase("True")){
							double y = p.getLocation().getY();
							double newy = y+200;
							double x = p.getLocation().getX();
							double z = p.getLocation().getZ();
							Location strike = new Location(p.getWorld(), x, newy, z);
							p.getWorld().strikeLightning(strike);
						}
						event.setDeathMessage("");
						if(plugin.config.getBoolean("broadcastAll")){
							p.getServer().broadcastMessage(pname + ChatColor.LIGHT_PURPLE + " Stepped off their pedestal too early!");
						}else{
							for(String gn: plugin.Playing.get(a)){
								Player g = plugin.getServer().getPlayer(gn);
								g.sendMessage(pname + ChatColor.LIGHT_PURPLE + " Stepped off their pedestal too early!");
							}
						}
						plugin.Frozen.get(a).remove(pname);
						plugin.Playing.get(a).remove(pname);
						if(plugin.config.getBoolean("broadcastAll")){
							p.getServer().broadcastMessage(leftmsg);
						}else{
							for(String gn: plugin.Playing.get(a)){
								Player g = plugin.getServer().getPlayer(gn);
								g.sendMessage(leftmsg);
							}
						}
						plugin.winner(a);
					}
				}
			}else{
				players = plugin.Playing.get(a).size()-1;
				leftmsg = ChatColor.BLUE + "There are now " + players + " tributes left!";
				if(plugin.config.getString("Cannon_Death").equalsIgnoreCase("True")){
					double y = p.getLocation().getY();
					double newy = y+200;
					double x = p.getLocation().getX();
					double z = p.getLocation().getZ();
					Location strike = new Location(p.getWorld(), x, newy, z);
					p.getWorld().strikeLightning(strike);
				}
				plugin.Dead.get(a).add(pname);
				plugin.Playing.get(a).remove(pname);
				if(p.getKiller() instanceof Player){
					if(p.getKiller().getInventory().getItemInMainHand().getType()== Material.AIR){ 
						Player killer = p.getKiller();
						String killername = killer.getName();
						event.setDeathMessage("");
						if(plugin.config.getBoolean("broadcastAll")){
							s.broadcastMessage(ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with their FIST!");
							s.broadcastMessage(leftmsg);
						}else{
							for(String gn: plugin.Playing.get(a)){
								Player g = plugin.getServer().getPlayer(gn);
								g.sendMessage(ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with their FIST!");
								g.sendMessage(leftmsg);
							}
						}
						if(plugin.Kills.containsKey(killername))
							plugin.Kills.put(killername, plugin.Kills.get(killername)+1);
						else plugin.Kills.put(killername, 1);
						if(plugin.Kills.containsKey("__SuM__"))
							plugin.Kills.put("__SuM__", plugin.Kills.get("__SuM__")+1);
						else plugin.Kills.put("__SuM__", 1);
						plugin.winner(a);
					}else{
						Player killer = p.getKiller();
						String killername = killer.getName();
						String weapon = "a(n) " + killer.getInventory().getItemInMainHand().getType().name().replace('_', ' ');
						if(killer.getInventory().getItemInMainHand().hasItemMeta())
							if(killer.getInventory().getItemInMainHand().getItemMeta().hasDisplayName())
								weapon = killer.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
						String msg = ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with " + weapon;
						event.setDeathMessage("");
						if(plugin.config.getBoolean("broadcastAll")){
							s.broadcastMessage(msg);
							s.broadcastMessage(leftmsg);
						}else{
							for(String gn: plugin.Playing.get(a)){
								Player g = plugin.getServer().getPlayer(gn);
								g.sendMessage(msg);
								g.sendMessage(leftmsg);
							}
						}
						if(plugin.Kills.containsKey(killername))
							plugin.Kills.put(killername, plugin.Kills.get(killername)+1);
						else plugin.Kills.put(killername, 1);
						if(plugin.Kills.containsKey("__SuM__"))
							plugin.Kills.put("__SuM__", plugin.Kills.get("__SuM__")+1);
						else plugin.Kills.put("__SuM__", 1);
						plugin.winner(a);
					}
				}else{
					event.setDeathMessage("");
					if(plugin.config.getBoolean("broadcastAll")){
						s.broadcastMessage(ChatColor.LIGHT_PURPLE + pname + " died of natural causes!");
						s.broadcastMessage(leftmsg);
					}else{
						for(String gn: plugin.Playing.get(a)){
							Player g = plugin.getServer().getPlayer(gn);
							g.sendMessage(ChatColor.LIGHT_PURPLE + pname + " died of " + ChatColor.ITALIC + " probably " + ChatColor.LIGHT_PURPLE + "natural causes!");
							g.sendMessage(leftmsg);
						}
					}
					plugin.winner(a);
				}
			}
		}
	}
	private void clearInv(Player p){
		p.getInventory().clear();
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setHelmet(null);
		p.getInventory().setLeggings(null);
		p.updateInventory();
	}
}
