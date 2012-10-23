package me.Travja.HungerArena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		final Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Dead.contains(pname)){
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
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		Server s = p.getServer();
		String pname = p.getName();
		int players = plugin.Playing.size()-1;
		String leftmsg = ChatColor.BLUE + "There are now " + players + " tributes left!";
		if(plugin.Frozen.contains(pname) && plugin.Playing.contains(pname)){
			if(plugin.config.getString("Cannon_Death").equalsIgnoreCase("True")){
				double y = p.getLocation().getY();
				double newy = y+200;
				double x = p.getLocation().getX();
				double z = p.getLocation().getZ();
				Location strike = new Location(p.getWorld(), x, newy, z);
				p.getWorld().strikeLightning(strike);
			}
			event.setDeathMessage("");
			p.getServer().broadcastMessage(pname + ChatColor.LIGHT_PURPLE + " Stepped off their pedestal too early!");
			plugin.Frozen.remove(pname);
			plugin.Playing.remove(pname);
			plugin.Dead.add(pname);
			s.broadcastMessage(leftmsg);
			plugin.winner();
		}else if(plugin.Playing.contains(pname)){
			if(plugin.config.getString("Cannon_Death").equalsIgnoreCase("True")){
				double y = p.getLocation().getY();
				double newy = y+200;
				double x = p.getLocation().getX();
				double z = p.getLocation().getZ();
				Location strike = new Location(p.getWorld(), x, newy, z);
				p.getWorld().strikeLightning(strike);
			}
			plugin.Dead.add(pname);
			plugin.Playing.remove(pname);
			if(p.getKiller() instanceof Player){
				if(p.getKiller().getItemInHand().getType().getId()== 0){
					Player killer = p.getKiller();
					String killername = killer.getName();
					event.setDeathMessage("");
					s.broadcastMessage(ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with their FIST!");
					s.broadcastMessage(leftmsg);
					plugin.winner();
				}else{
					Player killer = p.getKiller();
					String killername = killer.getName();
					Material weapon = killer.getItemInHand().getType();
					String msg = ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with a(n) " + weapon;
					event.setDeathMessage("");
					s.broadcastMessage(msg);
					s.broadcastMessage(leftmsg);
					plugin.winner();
				}
			}else{
				event.setDeathMessage("");
				s.broadcastMessage(ChatColor.LIGHT_PURPLE + pname + " died of natural causes!");
				s.broadcastMessage(leftmsg);
				plugin.winner();
			}
		}
	}
}
