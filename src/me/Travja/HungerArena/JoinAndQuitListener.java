package me.Travja.HungerArena;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinAndQuitListener implements Listener {
	public Main plugin;
	public JoinAndQuitListener(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		final Player player = event.getPlayer();
		for(Player spectator:plugin.Watching){
			p.hidePlayer(spectator);
		}
		if(plugin.Out.contains(p)){
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					player.sendMessage(ChatColor.AQUA + "You have saved yourself from being ejected from the arena!");
				}
			}, 40L);
			plugin.Out.remove(p);
		}
		if(plugin.Quit.contains(p) || plugin.Dead.contains(p)){
			String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
			String w = Spawncoords[3];
			World spawnw = plugin.getServer().getWorld(w);
			double spawnx = Double.parseDouble(Spawncoords[0]);
			double spawny = Double.parseDouble(Spawncoords[1]);
			double spawnz = Double.parseDouble(Spawncoords[2]);
			final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					player.teleport(Spawn);
					player.sendMessage(ChatColor.RED + "You have been teleported to spawn because you quit/forfieted!");
				}
			}, 40L);
		}
	}
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		final Player p = event.getPlayer();
		String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
		String w = Spawncoords[3];
		World spawnw = plugin.getServer().getWorld(w);
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
		if(plugin.Playing.contains(p)){
			plugin.Out.add(p);
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				if(plugin.Playing.contains(p) && plugin.Out.contains(p)){
					if(plugin.canjoin== true){
						plugin.Playing.remove(p);
						plugin.Quit.add(p);
						plugin.Out.remove(p);
						if(plugin.Playing.size()== 1){
							for(Player winner:plugin.Playing){
								String winnername = winner.getName();
								p.getServer().broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
								winner.getInventory().clear();
								winner.getInventory().setBoots(null);
								winner.getInventory().setChestplate(null);
								winner.getInventory().setHelmet(null);
								winner.getInventory().setLeggings(null);
								winner.getInventory().addItem(plugin.Reward);
							}
							for(Player spectator:plugin.Watching){
								spectator.setAllowFlight(false);
								spectator.teleport(Spawn);
							}
							if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
								plugin.Dead.clear();
								plugin.Playing.clear();
								plugin.Quit.clear();
								plugin.Watching.clear();
								plugin.Frozen.clear();
								plugin.canjoin = false;
							}
						}
					}else if(plugin.canjoin== false){
						plugin.Playing.remove(p);
						plugin.Quit.add(p);
						plugin.Out.remove(p);
					}
				}
			}
		}, 1200L);
	}
}
