package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.Main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.DisplaySlot;

public class SpectatorListener implements Listener {
	public Main plugin;
	public SpectatorListener(Main m){
		this.plugin = m;
	}
	int i = 0;
	@EventHandler
	public void SpectatorDrops(PlayerDropItemEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		for(i = 0; i < plugin.Watching.size(); i++){
			if(plugin.Watching.get(i)!= null){
				if(plugin.Watching.get(i).contains(pname)){
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}
	@EventHandler
	public void SpectatorInteractBlock(PlayerInteractEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		for(i = 0; i < plugin.Watching.size(); i++){
			if(plugin.Watching.get(i)!= null){
				if(plugin.Watching.get(i).contains(pname)){
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}

	@EventHandler
	public void SpectatorInteractEntity(PlayerInteractEntityEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		for(i = 0; i < plugin.Watching.size(); i++){
			if(plugin.Watching.get(i)!= null){
				if(plugin.Watching.get(i).contains(pname)){
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}
	@EventHandler
	public void SpectatorItems(PlayerPickupItemEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		for(i = 0; i < plugin.Watching.size(); i++){
			if(plugin.Watching.get(i)!= null){
				if(plugin.Watching.get(i).contains(pname)){
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}
	@EventHandler
	public void SpectatorPvP(EntityDamageByEntityEvent event){
		Entity offense = event.getDamager();
		if(offense instanceof Player){
			Player Attacker = (Player) event.getDamager();
			String attackerName = Attacker.getName();
			for(i = 0; i < plugin.Watching.size(); i++){
				if(plugin.Watching.get(i)!= null){
					if(plugin.Watching.get(i).contains(attackerName)){
						event.setCancelled(true);
						Attacker.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
						return;
					}
				}
			}
			for(i = 0; i < plugin.Playing.size(); i++){
				if(plugin.Playing.get(i)!= null){
					if(plugin.Playing.get(i).contains(attackerName)){
						event.setCancelled(true);
					}
				}
			}
		}else if(event.getDamager() instanceof Projectile){
			Projectile arrow = (Projectile) offense;
			if(arrow.getShooter() instanceof Player){
				Player BowMan = (Player) arrow.getShooter();
				String bowManName = BowMan.getName();
				for(i = 0; i < plugin.Watching.size(); i++){
					if(plugin.Watching.get(i)!= null){
						if(plugin.Watching.get(i).contains(bowManName)){
							event.setCancelled(true);
							BowMan.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
							return;
						}
					}
				}
				for(i = 0; i < plugin.Playing.size(); i++){
					if(plugin.Playing.get(i)!= null){
						if(plugin.Playing.get(i).contains(bowManName)){
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void SpectatorBlockBreak(BlockBreakEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		for(i = 0; i < plugin.Watching.size(); i++){
			if(plugin.Watching.get(i)!= null){
				if(plugin.Watching.get(i).contains(pname)){
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}
	@EventHandler
	public void SpectatorBlockPlace(BlockPlaceEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		for(i = 0; i < plugin.Watching.size(); i++){
			if(plugin.Watching.get(i)!= null){
				if(plugin.Watching.get(i).contains(pname)){
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}
	@EventHandler
	public void SpectatorQuit(PlayerQuitEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		for(i = 0; i < plugin.Watching.size(); i++){
			if(plugin.Watching.get(i)!= null){
				if(plugin.Watching.get(i).contains(pname)){
					plugin.Watching.get(i).remove(pname);
					String[] Spawncoords = plugin.spawns.getString("Spawn_coords").split(",");
					String w = Spawncoords[3];
					World spawnw = plugin.getServer().getWorld(w);
					double spawnx = Double.parseDouble(Spawncoords[0]);
					double spawny = Double.parseDouble(Spawncoords[1]);
					double spawnz = Double.parseDouble(Spawncoords[2]);
					final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
					p.teleport(Spawn);
					p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
					if(plugin.scoreboards.containsKey(p.getName()))
						plugin.scoreboards.remove(p.getName());
					if(plugin.Kills.containsKey(p.getName()))
						plugin.Kills.remove(p.getName());
				}
			}
		}
	}

	@EventHandler
	public void MobNerf(EntityTargetEvent event){
		Entity target = event.getTarget();
		Entity e = event.getEntity();
		if (e instanceof Player) {
			return;
		}
		if(target instanceof Player){
			String targetName = ((Player) target).getName();
			for(i = 0; i < plugin.Watching.size(); i++){
				if(plugin.Watching.get(i)!= null){
					if(plugin.Watching.get(i).contains(targetName)){
						event.setTarget(null);
					}
				}
			}
		}
	}
}
