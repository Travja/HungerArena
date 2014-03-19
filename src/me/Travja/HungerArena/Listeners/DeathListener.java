package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
	public void damage(EntityDamageEvent event){
		Entity e = event.getEntity();
		if(e instanceof Player){
			Player p = (Player) e;
			if(plugin.getArena(p)!= null){
				a = plugin.getArena(p);
				if(plugin.Playing.get(a).size()== 2){
					if(event.getDamage()>= p.getHealth()){
						event.setCancelled(true);
						String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
						World spawnw = plugin.getServer().getWorld(Spawncoords[3]);
						double spawnx = Double.parseDouble(Spawncoords[0]);
						double spawny = Double.parseDouble(Spawncoords[1]);
						double spawnz = Double.parseDouble(Spawncoords[2]);
						final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
						p.setHealth((double) 20);
						p.setFoodLevel(20);
						p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
						plugin.scoreboards.remove(p.getName());
						plugin.Kills.remove(p.getName());
						clearInv(p);
						p.teleport(Spawn);
						plugin.Frozen.get(a).remove(p.getName());
						plugin.Playing.get(a).remove(p.getName());
						plugin.winner(a);
					}
				}
			}
		}
	}
	@EventHandler (priority = EventPriority.HIGHEST)
	public void damage(EntityDamageByEntityEvent event){
		Entity e = event.getEntity();
		Entity d = event.getEntity();
		if(e instanceof Player){
			Player p = (Player) e;
			if(plugin.getArena(p)!= null){
				a = plugin.getArena(p);
				if(plugin.Playing.get(a).size()== 2){
					if(event.getDamage()>= p.getHealth()){
						event.setCancelled(true);
						String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
						World spawnw = plugin.getServer().getWorld(Spawncoords[3]);
						double spawnx = Double.parseDouble(Spawncoords[0]);
						double spawny = Double.parseDouble(Spawncoords[1]);
						double spawnz = Double.parseDouble(Spawncoords[2]);
						final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
						p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
						plugin.scoreboards.remove(p.getName());
						plugin.Kills.remove(p.getName());
						clearInv(p);
						p.setHealth((double) 20);
						p.setFoodLevel(20);
						p.teleport(Spawn);
						if(d instanceof Player){
							Player k = (Player) d;
							if(plugin.getArena(k)!= null){
								if(plugin.Kills.containsKey(k.getName()))
									plugin.Kills.put(k.getName(), plugin.Kills.get(k.getName())+1);
							}
						}
						plugin.Frozen.get(a).remove(p.getName());
						plugin.Playing.get(a).remove(p.getName());
						plugin.winner(a);
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
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
						if(plugin.Kills.containsKey(killername))
							plugin.Kills.put(killername, plugin.Kills.get(killername)+1);
						plugin.winner(a);
					}else{
						Player killer = p.getKiller();
						String killername = killer.getName();
						String weapon = "a(n) " + killer.getItemInHand().getType().toString().replace('_', ' ');
						if(killer.getItemInHand().hasItemMeta())
							if(killer.getItemInHand().getItemMeta().hasDisplayName())
								weapon = killer.getItemInHand().getItemMeta().getDisplayName();
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
	@SuppressWarnings("deprecation")
	private void clearInv(Player p){
		p.getInventory().clear();
		p.getInventory().setBoots(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setHelmet(null);
		p.getInventory().setLeggings(null);
		p.updateInventory();
	}
}
