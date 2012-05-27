package me.Travja.HungerArena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {
	public Main plugin;
	public FreezeListener(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		if(plugin.Frozen.contains(p) && plugin.config.getString("Frozen_Teleport").equalsIgnoreCase("True")){
			event.setCancelled(true);
		}
	}
}
