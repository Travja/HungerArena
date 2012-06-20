package me.Travja.HungerArena;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class SpectatorListener implements Listener {
	public Main plugin;
	public SpectatorListener(Main m){
		this.plugin = m;
	}
	@EventHandler
	public void SpectatorDrops(PlayerDropItemEvent event){
		Player p = event.getPlayer();
		if(plugin.Watching.contains(p)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void SpectatorInteractions(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(plugin.Watching.contains(p)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void SpectatorItems(PlayerPickupItemEvent event){
		Player p = event.getPlayer();
		if(plugin.Watching.contains(p)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void SpectatorPvP(EntityDamageByEntityEvent event){
		Entity offense = event.getDamager();
		if(offense instanceof Player){
			Player Attacker = (Player) event.getDamager();
			if(plugin.Watching.contains(Attacker)){
				event.setCancelled(true);
				Attacker.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
			}
		}else if(event.getDamager() instanceof Projectile){
			Projectile arrow = (Projectile) offense;
			if(arrow.getShooter() instanceof Player){
				Player BowMan = (Player) arrow.getShooter();
				if(plugin.Watching.contains(BowMan)){
					event.setCancelled(true);
					BowMan.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}
	@EventHandler
	public void SpectatorBlocks(BlockBreakEvent event){
		Player p = event.getPlayer();
		if(plugin.Watching.contains(p)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void SpectatorJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		final Player player = event.getPlayer();
		if(plugin.Watching.contains(p)){
			if(plugin.canjoin== false){
				String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
				String w = Spawncoords[3];
				World spawnw = plugin.getServer().getWorld(w);
				double spawnx = Double.parseDouble(Spawncoords[0]);
				double spawny = Double.parseDouble(Spawncoords[1]);
				double spawnz = Double.parseDouble(Spawncoords[2]);
				final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
				for(Player everyone:plugin.getServer().getOnlinePlayers()){
					everyone.showPlayer(p);
				}
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
					public void run(){
						player.teleport(Spawn);
						player.sendMessage(ChatColor.RED + "You have been teleported to spawn because the game is over!");
					}
				}, 40L);
				plugin.Watching.remove(p);
			}else{
				p.setAllowFlight(true);
				p.setFlying(true);
				for(Player everyone:plugin.getServer().getOnlinePlayers()){
					everyone.hidePlayer(p);
				}
			}
		}
	}
	@EventHandler
	public void MobNerf(EntityTargetEvent event){
		Entity target = event.getTarget();
		if(target instanceof Player){
			if(plugin.Watching.contains(target)){
				event.setCancelled(true);
			}
		}
	}
}
