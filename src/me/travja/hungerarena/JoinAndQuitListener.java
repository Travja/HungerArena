package me.Travja.HungerArena;

import org.bukkit.Bukkit;
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
	int i = 0;
        
        @EventHandler
        public void onJoin(PlayerJoinEvent evt) {
            Player p = evt.getPlayer();
            for (String s : plugin.Watching) {
                Player spectator = Bukkit.getServer().getPlayerExact(s);
                p.hidePlayer(spectator);
            }
        }
        
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		final Player p = event.getPlayer();
		String pname = p.getName();
		if(!plugin.Watching.isEmpty()){
			String s = plugin.Watching.get(i++);
			Player spectator = plugin.getServer().getPlayerExact(s);
			p.hidePlayer(spectator);
		}
		if(plugin.Out.contains(pname)){
			plugin.Playing.add(pname);
			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					p.sendMessage(ChatColor.AQUA + "You have saved yourself from being ejected from the arena!");
				}
			}, 40L);
			plugin.Out.remove(pname);
		}
		if(plugin.Quit.contains(pname) || plugin.Dead.contains(pname)){
			String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
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
		}
	}
        
        @EventHandler
        public void onQuit(PlayerQuitEvent evt) {
            Player p = evt.getPlayer();
            String pname = p.getName();
            if (plugin.Frozen.contains(pname)) {
                plugin.Frozen.remove(pname);
                String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
		String w = Spawncoords[3];
		World spawnw = plugin.getServer().getWorld(w);
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
                p.teleport(Spawn);
            }
        }
        
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		final Player p = event.getPlayer();
		final String pname = p.getName();
		String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
		String w = Spawncoords[3];
		World spawnw = plugin.getServer().getWorld(w);
		double spawnx = Double.parseDouble(Spawncoords[0]);
		double spawny = Double.parseDouble(Spawncoords[1]);
		double spawnz = Double.parseDouble(Spawncoords[2]);
		final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
		if(plugin.Playing.contains(pname)){
			plugin.Out.add(pname);
			plugin.Playing.remove(pname);
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				if(plugin.Out.contains(pname)){
					if(plugin.canjoin== true){
						plugin.Quit.add(pname);
						plugin.Out.remove(pname);
						if(plugin.Playing.size()== 1){
							//Announce Winner
							String winnername = plugin.Playing.get(i++);
							Player winner = plugin.getServer().getPlayerExact(winnername);
							String winnername2 = winner.getName();
							p.getServer().broadcastMessage(ChatColor.GREEN + winnername2 + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.getInventory().setBoots(null);
							winner.getInventory().setChestplate(null);
							winner.getInventory().setHelmet(null);
							winner.getInventory().setLeggings(null);
							winner.getInventory().addItem(plugin.Reward);
                                                        PlayerWinGamesEvent evt = new PlayerWinGamesEvent(winner);
                                                        Bukkit.getServer().getPluginManager().callEvent(evt);
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
					}
				}else{
					plugin.Quit.add(pname);
				}
			}
		}, 1200L);
	}
}
