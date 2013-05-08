package me.Travja.HungerArena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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
		for(i = 1; i < plugin.Dead.size(); i++){
			if(plugin.Dead.get(i).contains(pname)){
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
				World spawnw = plugin.getServer().getWorld(Spawncoords[3]);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						p.teleport(Spawn);
					}
				}, 10L);
			}
		}
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onPlayerDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		Server s = p.getServer();
		String pname = p.getName();
		int players = plugin.Playing.size()-1;
		String leftmsg = null;
		for(i = 1; i < plugin.Frozen.size(); i++){
			if(plugin.Frozen.get(i).contains(pname)){
				if(plugin.getArena(p)!= null){
					a = plugin.getArena(p);
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
					plugin.Dead.get(a).add(pname);
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
			}else if(plugin.getArena(p)!= null){
				a = plugin.getArena(p);
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
					if(p.getKiller().getItemInHand().getType().getId()== 0){
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
						plugin.winner(a);
					}else{
						Player killer = p.getKiller();
						String killername = killer.getName();
						String weapon = killer.getItemInHand().getType().toString().replace('_', ' ');
						String msg = ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with a(n) " + weapon;
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
							g.sendMessage(ChatColor.LIGHT_PURPLE + pname + " died of natural causes!");
							g.sendMessage(leftmsg);
						}
					}
					plugin.winner(a);
				}
			}
		}
	}
}
