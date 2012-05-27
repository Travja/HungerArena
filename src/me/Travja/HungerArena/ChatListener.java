package me.Travja.HungerArena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class ChatListener implements Listener {
	public Main plugin;
	public ChatListener(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void TributeChat(PlayerChatEvent event){
		Player p = event.getPlayer();
		if(plugin.Playing.contains(p)){
			String msg = "<" + ChatColor.RED + "[Tribute] " + ChatColor.WHITE + p.getName() + ">" + " " + event.getMessage();
			if(plugin.config.getString("ChatClose").equalsIgnoreCase("True")){
				double radius = plugin.config.getDouble("ChatClose_Radius");
				List<Entity> near = p.getNearbyEntities(radius, radius, radius);
				event.setCancelled(true);
				if(near.size()== 0){
					p.sendMessage(msg);
					p.sendMessage(ChatColor.YELLOW + "No one near!");
				}else if(!(near.size()== 0)){
					for(Entity en:near){
						if(!(en instanceof Player)){
							p.sendMessage(msg);
							p.sendMessage(ChatColor.YELLOW + "No one near!");
						}
					}
				}else{
					for(Entity e:near){
						if(e instanceof Player){
							((Player) e).sendMessage(msg);
						}
					}
				}
			}else{
				plugin.getServer().broadcastMessage(msg);
			}
		}
	}
}
