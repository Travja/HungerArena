package me.Travja.HungerArena;

import java.util.List;

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
    	List<String> worlds = plugin.config.getStringList("worlds");
    	if(worlds.contains(event.getTo().getWorld().getName()) && plugin.Tele.contains(p)){
    		event.setCancelled(true);
    		p.sendMessage(ChatColor.RED + "You are a dead tribute... How are you supposed to get back into the arena....");
    		plugin.Tele.remove(p);
    	}else if(plugin.Tele.contains(p)){
    		if(event.isCancelled()){
    			event.setCancelled(false);
    			plugin.Tele.remove(p);
    		}
    	}
    }
    /*@EventHandler          Unwanted right now...
    public void onTP(PlayerTeleportEvent evt) {
        @SuppressWarnings("unused")
		Player p = evt.getPlayer();
        TeleportCause tc = evt.getCause();
        if (tc == TeleportCause.ENDER_PEARL) {
            return;
        }
        if (tc == TeleportCause.END_PORTAL) {
            return;
        }
        if (tc == TeleportCause.NETHER_PORTAL) {
            return;
        }
        evt.setCancelled(true);
    }*/

}
