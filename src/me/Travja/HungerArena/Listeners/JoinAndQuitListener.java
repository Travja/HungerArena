package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.HaCommands;
import me.Travja.HungerArena.Main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;

public class JoinAndQuitListener implements Listener {
	public Main plugin;
	public JoinAndQuitListener(Main m) {
		this.plugin = m;
	}
	public HaCommands commands;
	public JoinAndQuitListener(HaCommands h){
		this.commands = h;
	}
	int a = 0;

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		final Player p = event.getPlayer();
		final String pname = p.getName();
		boolean pfound = false;
		for(int i : plugin.Watching.keySet()){
			for(String s: plugin.Watching.get(i)){
				Player spectator = plugin.getServer().getPlayerExact(s);
				p.hidePlayer(plugin,spectator);
			}
		}
		for(int i : plugin.Out.keySet()){
			if(plugin.Out.get(i).contains(pname)){
				plugin.Playing.get(i).add(pname);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						p.sendMessage(ChatColor.AQUA + "You have saved yourself from being ejected from the arena!");
					}
				}, 40L);
				plugin.Out.get(i).remove(pname);
				pfound = true;
			}
		}

		for(final int i : plugin.Quit.keySet()){
			if(plugin.Quit.get(i).contains(pname)){
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords."+i).split(","); 
				String w = Spawncoords[3];
				World spawnw = plugin.getServer().getWorld(w);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						p.teleport(Spawn);
						p.sendMessage(ChatColor.RED + "You have been teleported to last spawn because you quit/forfeited!");
						plugin.RestoreInv(p, p.getName()); 
						if (plugin.Quit.get(i)!= null) plugin.Quit.get(i).remove(p.getName());
					}
				}, 40L);
				pfound = true;
			}
		}
		for(final int i : plugin.Dead.keySet()){
			if(plugin.Dead.get(i).contains(pname)){
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords."+i).split(",");
				String w = Spawncoords[3];
				World spawnw = plugin.getServer().getWorld(w);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						p.teleport(Spawn);
						p.sendMessage(ChatColor.RED + "You have been teleported to spawn because you quit/died/forfeited!!");
						plugin.RestoreInv(p, p.getName());
						if (plugin.Dead.get(i)!= null) plugin.Dead.get(i).remove(p.getName());
					}
				}, 40L);
				pfound = true;
			}
		}
		for(final int i : plugin.inArena.keySet()){
			if(plugin.inArena.get(i)!= null){
				if(plugin.inArena.get(i).contains(pname)){
					String[] Spawncoords = plugin.spawns.getString("Spawn_coords."+i).split(",");
					String w = Spawncoords[3];
					World spawnw = plugin.getServer().getWorld(w);
					double spawnx = Double.parseDouble(Spawncoords[0]);
					double spawny = Double.parseDouble(Spawncoords[1]);
					double spawnz = Double.parseDouble(Spawncoords[2]);
					final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
						public void run(){
							p.teleport(Spawn);
							p.getInventory().clear();
							p.getInventory().setBoots(null);
							p.getInventory().setLeggings(null);
							p.getInventory().setChestplate(null);
							p.getInventory().setHelmet(null);
							plugin.inArena.remove(pname);
							p.sendMessage(ChatColor.RED + "You were still in the arena when you left and now the games are over.");
							plugin.RestoreInv(p, p.getName());
							if (plugin.inArena.get(i)!= null) plugin.inArena.get(i).remove(p.getName());
						}
					}, 40L);
					pfound = true;
				}
			}
		}
		if((plugin.restricted && plugin.worldsNames.values().contains(p.getWorld().getName())) || !plugin.restricted){
			if (!pfound && plugin.config.getString("Force_Players_toSpawn").equalsIgnoreCase("True") && (plugin.spawns.getString("Spawn_coords.0")!=null)) { 
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords.0").split(",");  		
				String w = Spawncoords[3];
				World spawnw = plugin.getServer().getWorld(w);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				plugin.RestoreInv(p, p.getName()); 
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						p.teleport(Spawn);
						p.sendMessage(ChatColor.RED + "You have been teleported to spawn!!");
					}
				}, 40L);
			}
		}
	}
	@EventHandler
	public void onQuit(PlayerQuitEvent evt) {
		Player p = evt.getPlayer();
		String pname = p.getName();
		for(int i : plugin.Frozen.keySet()){
			if (plugin.Frozen.get(i).contains(pname)) {
				plugin.Frozen.remove(pname);
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords.0").split(","); 
				String w = Spawncoords[3];
				World spawnw = plugin.getServer().getWorld(w);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				p.teleport(Spawn);
				p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				if(plugin.scoreboards.containsKey(p.getName()))
					plugin.scoreboards.remove(p.getName());
				if(plugin.Kills.containsKey(p.getName()))
					plugin.Kills.remove(p.getName());
			}
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		final Player p = event.getPlayer();
		final String pname = p.getName();
		if(plugin.getArena(p)!= null){
			a = plugin.getArena(p);
			plugin.Out.get(a).add(pname);
			plugin.Playing.get(a).remove(pname);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){ 
				public void run(){
					if(plugin.Out.get(a).contains(pname)){
						plugin.Quit.get(a).add(pname);
						plugin.Out.get(a).remove(pname); 
						p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
						if(plugin.scoreboards.containsKey(p.getName()))
							plugin.scoreboards.remove(p.getName());
						if(plugin.Kills.containsKey(p.getName()))
							plugin.Kills.remove(p.getName());
						plugin.winner(a);
						plugin.inArena.get(a).add(pname); 
					}else if(plugin.getArena(p)== null){
						plugin.Quit.get(a).add(pname);
					}
				}
			}, 1200L);
		}
	}
}
