package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.Main;
import me.Travja.HungerArena.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Signs implements Listener {
    public Main plugin;

    public Signs(Main m) {
        this.plugin = m;
    }

    @EventHandler
    public void Sign(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Block b = event.getClickedBlock();
        if (b == null) {
            return;
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (b.getType().name().contains("SIGN")) {
                Utils.useSign(b, p);
            }
        }
    }

    @EventHandler
    public void Create(SignChangeEvent event) {
        String top = event.getLine(0);
        if (top.equalsIgnoreCase("[HungerArena]") || top.equalsIgnoreCase("[HA]") || top.equalsIgnoreCase("[Sponsor]")) {
            event.setLine(0, ChatColor.BLUE + top);
        }
    }
}
