package me.Travja.HungerArena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Boundaries implements Listener{
	public Main plugin;
	public Boundaries(Main m){
		this.plugin = m;
	}

	@EventHandler
	public void boundsCheck(PlayerMoveEvent event){
		Player p = event.getPlayer();
		Boolean inGame = plugin.getArena(p) != null;
		Boolean spectating = plugin.isSpectating(p);
		if(insideBounds(p)){
			if(inGame || spectating){
				//Tele somewhere?
			}
		}
	}
	public boolean insideBounds(Player p){
		/*
		 * Get players location
		 * Get highs and lows of all arenas
		 * Check if player is inside an arena
		 * return true if inside
		 */
		return false;
	}
}
