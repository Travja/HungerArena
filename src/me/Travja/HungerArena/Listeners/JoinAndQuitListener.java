package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.HaCommands;
import me.Travja.HungerArena.Main;

import org.bukkit.Bukkit;
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
	int i = 0;
	int a = 0;
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent evt) {
		Player p = evt.getPlayer();
		for(i = 1; i <= plugin.Watching.size(); i++){
			for (String s : plugin.Watching.get(i)) {
				Player spectator = Bukkit.getServer().getPlayerExact(s);
				p.hidePlayer(spectator);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		final Player p = event.getPlayer();
		final String pname = p.getName();
		boolean pfound = false;
		for(i = 1; i <= plugin.Watching.size(); i++){
			for(String s: plugin.Watching.get(i)){
				Player spectator = plugin.getServer().getPlayerExact(s);
				p.hidePlayer(spectator);
			}
		}
		for(i = 1; i <= plugin.Out.size(); i++){
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
		for(i = 1; i <= plugin.Quit.size(); i++){
			if(plugin.Quit.get(i).contains(pname)){
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
				String w = Spawncoords[3];
				World spawnw = plugin.getServer().getWorld(w);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						p.teleport(Spawn);
						p.sendMessage(ChatColor.RED + "You have been teleported to spawn because you quit/forfeited!");
					}
				}, 40L);
				plugin.Quit.get(i).remove(pname); //Jeppa: fix
				pfound = true;
			}
		}
		for(i = 1; i <= plugin.Dead.size(); i++){
			if(plugin.Dead.get(i).contains(pname)){
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
				String w = Spawncoords[3];
				World spawnw = plugin.getServer().getWorld(w);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						p.teleport(Spawn);
						p.sendMessage(ChatColor.RED + "You have been teleported to spawn because you quit/forfeited!!");
					}
				}, 40L);
				plugin.Dead.get(i).remove(pname); //Jeppa: fix
				pfound = true;
			}
		}
		for(i = 1; i <= plugin.inArena.size(); i++){
			if(plugin.inArena.get(i)!= null){
				if(plugin.inArena.get(i).contains(pname)){
					String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
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
						}
					}, 40L);
					plugin.inArena.get(i).remove(pname); //Jeppa: fix
					pfound = true;
				}
			}
		}
		//Jeppa: New routine to check if the player reconnected and is unknown to the server!
		if((plugin.restricted && plugin.worlds.contains(p.getWorld().getName())) || !plugin.restricted){
			if (!pfound && plugin.config.getString("Force_Players_toSpawn").equalsIgnoreCase("True")) { //Jeppa: Player is in non of the obove lists.. so he is new to the server due to restart of the server and reconnect of the player...(something like that...)
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
				String w = Spawncoords[3];
				World spawnw = plugin.getServer().getWorld(w);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						p.teleport(Spawn);
						p.sendMessage(ChatColor.RED + "You have been teleported to spawn!!");
					}
				}, 40L);
			}
		}
		//^^

	}
	@EventHandler
	public void onQuit(PlayerQuitEvent evt) {
		Player p = evt.getPlayer();
		String pname = p.getName();
		for(i = 1; i <= plugin.Frozen.size(); i++){
			if (plugin.Frozen.get(i).contains(pname)) {
				plugin.Frozen.remove(pname);
				String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
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
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){ // Jeppa: how long is this delay?
				public void run(){
					if(plugin.Out.get(a).contains(pname)){
						plugin.Quit.get(a).add(pname);
						plugin.Out.get(a).remove(pname); //Jeppa: fix
						p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
						if(plugin.scoreboards.containsKey(p.getName()))
							plugin.scoreboards.remove(p.getName());
						if(plugin.Kills.containsKey(p.getName()))
							plugin.Kills.remove(p.getName());
						plugin.winner(a);
						plugin.inArena.get(a).add(pname); //Jeppa: add him to Quit and to inArena ?
					}else if(plugin.getArena(p)== null){
						plugin.Quit.get(a).add(pname);
					}
				}
			}, 1200L);
		}
	}
}
