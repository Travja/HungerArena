package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.Main;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportListener implements Listener {
    
    public Main plugin;
    public TeleportListener(Main m) {
        this.plugin = m;
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onTP(PlayerTeleportEvent event){
    	Player p = event.getPlayer();
    	if(plugin.worldsNames.values().contains(event.getTo().getWorld().getName()) && plugin.tele.contains(p)){
    		event.setCancelled(true);
    		p.sendMessage(ChatColor.RED + "You are a dead tribute... How are you supposed to get back into the arena....");
    		plugin.tele.remove(p);
    	}else if(plugin.tele.contains(p)){
    		if(event.isCancelled()){
    			event.setCancelled(false);
    			plugin.tele.remove(p);
    		}
    	}
    }
}
