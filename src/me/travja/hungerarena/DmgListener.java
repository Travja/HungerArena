package me.Travja.HungerArena;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 *
 * @author YoshiGenius
 */
public class DmgListener implements Listener {
    
    public Main plugin;
    public DmgListener(Main m) {
        this.plugin = m;
    }
    
    @EventHandler
    public void onDmg(EntityDamageEvent evt) {
        Entity e = evt.getEntity();
        if (e instanceof Player) {
            Player p = (Player) e;
            String pn = p.getName();
            if (plugin.Frozen.contains(pn)) {
                evt.setCancelled(true);
            }
        }
    }

}
