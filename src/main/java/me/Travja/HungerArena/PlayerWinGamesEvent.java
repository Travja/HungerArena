package me.travja.hungerarena;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 *
 * @author YoshiGenius
 */
public class PlayerWinGamesEvent extends PlayerEvent {
    
    private static final HandlerList handlers = new HandlerList();
    
    public PlayerWinGamesEvent(final Player p) {
        super(p);
    }
    
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
