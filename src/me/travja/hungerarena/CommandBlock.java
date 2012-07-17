package me.Travja.HungerArena;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlock implements Listener {
	public Main plugin;
	public CommandBlock(Main m) {
		this.plugin = m;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void CatchCommand(PlayerCommandPreprocessEvent event){
		String cmd = event.getMessage();
		Player p = event.getPlayer();
		String pname = p.getName();
		if(!cmd.contains("/ha") && plugin.Playing.contains(pname) && plugin.canjoin== true){
			if(!p.hasPermission("HungerArena.UseCommands")){
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You are only allowed to use /ha commands!");
			}
		}
	}
}
