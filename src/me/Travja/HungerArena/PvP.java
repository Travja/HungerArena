package me.Travja.HungerArena;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvP implements Listener {
	public Main plugin;
	public PvP(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void PlayerPvP(EntityDamageByEntityEvent event){
		Entity p = event.getEntity();
		if(p instanceof Player){
			if(plugin.Playing.contains(p) && plugin.canjoin== false){
				event.setCancelled(true);
			}
		}
	}
}
