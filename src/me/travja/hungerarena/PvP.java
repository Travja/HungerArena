package me.Travja.HungerArena;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvP implements Listener {
	public Main plugin;
	public PvP(Main m) {
		this.plugin = m;
	}
	@EventHandler(priority= EventPriority.MONITOR)
	public void PlayerPvP(EntityDamageByEntityEvent event){
		Entity p = event.getEntity();
		Entity d = event.getDamager();
		if(p instanceof Player && d instanceof Player){
			String pname = ((Player) p).getName();
			String dname = ((Player) d).getName();
			if(plugin.Playing.contains(pname) && plugin.Playing.contains(dname)){
				if(plugin.canjoin){
					if(event.isCancelled()){
						event.setCancelled(false);
					}
				}
			}else if(plugin.Playing.contains(pname)){
				if(!plugin.canjoin){
					if(!event.isCancelled()){
						event.setCancelled(true);
					}
				}
			}else if(!plugin.Playing.contains(pname) && plugin.Playing.contains(dname)){
				if(!event.isCancelled()){
					event.setCancelled(true);
				}
			}
		}else if(p instanceof Player && d instanceof Projectile){
			Projectile projectile = (Projectile) d;
			String pname = ((Player) p).getName();
			if(projectile.getShooter() instanceof Player){
				if(plugin.Playing.contains(pname)){
					Player shooter = (Player) projectile.getShooter();
					if(plugin.Playing.contains(shooter.getName())){
						event.setCancelled(false);
					}
				}
			}else if(projectile.getShooter() instanceof Entity){
				Entity e = projectile.getShooter();
				if(e instanceof Skeleton){
					if(plugin.Playing.contains(pname)){
						event.setCancelled(false);
					}
				}
			}
		}
	}
}
