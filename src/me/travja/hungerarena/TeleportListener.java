package me.Travja.HungerArena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

/**
 *
 * @author YoshiGenius
 */
public class TeleportListener implements Listener {
    
    public Main plugin;
    public TeleportListener(Main m) {
        this.plugin = m;
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
