package me.travja.hungerarena;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class Blocks implements Listener {
	public Main plugin;
	public Blocks(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void BreakBlock(BlockBreakEvent event){
		Player p = event.getPlayer();
		String pname = p.getDisplayName();
		if(plugin.Playing.contains(pname)){
			if(plugin.config.getString("Protected_Arena").equalsIgnoreCase("True")){
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You can't break blocks when you're playing!");
			}
		}
	}
}
