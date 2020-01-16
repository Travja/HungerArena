package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.Main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
public class PvP implements Listener {
	public Main plugin;
	public PvP(Main m) {
		this.plugin = m;
	}
	int a = 0;
	@EventHandler(priority= EventPriority.MONITOR)
	public void PlayerPvP(EntityDamageByEntityEvent event){
		Entity pl = event.getEntity();
		Entity dl = event.getDamager();
		if(pl instanceof Player && dl instanceof Player){
			Player p = (Player) pl;
			Player d = (Player) dl;
			if(plugin.getArena(p)!= null && plugin.getArena(d)!= null){
				a = plugin.getArena(p);
				if(plugin.canjoin.get(a)){
					if(event.isCancelled()){
						event.setCancelled(false);
					}
					if(plugin.gp.get(plugin.getArena(p))!= null){	
						if(plugin.gp.get(plugin.getArena(p))!= 0){	
							event.setCancelled(true);
						}
					}
				}
			}
			if(plugin.getArena(p)!= null){
				a = plugin.getArena(p);
				if(!plugin.canjoin.get(a)){
					if(!event.isCancelled()){
						event.setCancelled(true);
					}
				}
			}
			if(plugin.getArena(p)== null && plugin.getArena(d)!= null){
				if(!event.isCancelled()){
					event.setCancelled(true);
				}
			}
		}else if(pl instanceof Player && dl instanceof Projectile){
			Projectile projectile = (Projectile) dl;
			Player p = (Player) pl;
			if(projectile.getShooter() instanceof Player){
				if(plugin.getArena(p) != null){
					Player shooter = (Player) projectile.getShooter();
					if(plugin.getArena(shooter)!= null){
						if(plugin.gp.get(plugin.getArena(p))!= null)
							if(plugin.gp.get(plugin.getArena(p))!= 0)
								event.setCancelled(true);
							else
								event.setCancelled(false);
					}
				}
			}else if(projectile.getShooter() instanceof Entity){
				Entity e = (Entity) projectile.getShooter();
				if(e instanceof Skeleton){
					if(plugin.getArena(p)!= null){
						if(plugin.gp.get(plugin.getArena((Player) e))!= null)
							if(plugin.gp.get(plugin.getArena(p))!= 0)
								event.setCancelled(true);
							else
								event.setCancelled(false);
					}
				}
			}
		}
	}
	@EventHandler
	public void PlayerDamage(EntityDamageEvent event){
		Entity e = event.getEntity();
		if(e instanceof Player) {
			Player p = (Player)e;
			if(plugin.getArena(p)!=null){
				a = plugin.getArena(p);
				if(plugin.gp.get(a)!= null && plugin.gp.get(a)!= 0)
					event.setCancelled(true);
				if (plugin.Frozen.get(a)!=null && plugin.Frozen.get(a).contains(p.getName())){
					event.setCancelled(true);
				}
			}
		}
	}

}
