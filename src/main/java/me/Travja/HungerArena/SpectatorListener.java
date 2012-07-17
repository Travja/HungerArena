package me.travja.hungerarena;

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

public class SpectatorListener implements Listener {
	public Main plugin;
	public SpectatorListener(Main m){
		this.plugin = m;
	}
	@EventHandler
	public void SpectatorDrops(PlayerDropItemEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Watching.contains(pname)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void SpectatorInteractBlock(PlayerInteractEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Watching.contains(pname)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
        
        @EventHandler
	public void SpectatorInteractEntity(PlayerInteractEntityEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Watching.contains(pname)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void SpectatorItems(PlayerPickupItemEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Watching.contains(pname)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
	@EventHandler
	public void SpectatorPvP(EntityDamageByEntityEvent event){
		Entity offense = event.getDamager();
		if(offense instanceof Player){
			Player Attacker = (Player) event.getDamager();
			String attackerName = Attacker.getName();
			if(plugin.Watching.contains(attackerName)){
				event.setCancelled(true);
				Attacker.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
			}
		}else if(event.getDamager() instanceof Projectile){
			Projectile arrow = (Projectile) offense;
			if(arrow.getShooter() instanceof Player){
				Player BowMan = (Player) arrow.getShooter();
				String bowManName = BowMan.getName();
				if(plugin.Watching.contains(bowManName)){
					event.setCancelled(true);
					BowMan.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
				}
			}
		}
	}
	@EventHandler
	public void SpectatorBlockBreak(BlockBreakEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Watching.contains(pname)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
        
        @EventHandler
	public void SpectatorBlockPlace(BlockPlaceEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Watching.contains(pname)){
			event.setCancelled(true);
			p.sendMessage(ChatColor.RED + "You are spectating, you can't interfere with the game!");
		}
	}
        
        @EventHandler
	public void SpectatorQuit(PlayerQuitEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Watching.contains(pname)){
                    plugin.Watching.remove(pname);
                    String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
		    String w = Spawncoords[3];
		    World spawnw = plugin.getServer().getWorld(w);
		    double spawnx = Double.parseDouble(Spawncoords[0]);
		    double spawny = Double.parseDouble(Spawncoords[1]);
		    double spawnz = Double.parseDouble(Spawncoords[2]);
		    final Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
                    p.teleport(Spawn);
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
			if(plugin.Watching.contains(targetName)){
			        event.setTarget(null);
			}
		}
	}
}
