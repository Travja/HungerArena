package me.Travja.HungerArena;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * based on YoshiGenius
 */
public class WinGamesListener implements Listener {
    
    public Main plugin;
    public WinGamesListener(Main m) {
	this.plugin = m;
    }
    
    @EventHandler
    public void onWin(PlayerWinGamesEvent evt) {
        Player p = evt.getPlayer();
        plugin.econ.depositPlayer(p, plugin.config.getDouble("EcoReward"));
    }

}
