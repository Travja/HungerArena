package me.Travja.HungerArena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
	public Main plugin;
	public ChatListener(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void TributeChat(AsyncPlayerChatEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.getArena(p)!= null){
			String msg = "<" + ChatColor.RED + "[Tribute] " + ChatColor.WHITE + pname + ">" + " " + event.getMessage();
			if(plugin.config.getString("ChatClose").equalsIgnoreCase("True")){
				double radius = plugin.config.getDouble("ChatClose_Radius");
				List<Entity> near = p.getNearbyEntities(radius, radius, radius);
				event.setCancelled(true);
				if(!(near.size()== 0)){
					p.sendMessage(msg);
					for(Entity e:near){
						if(e instanceof Player)
							((Player) e).sendMessage(msg);
					}
				}else if(near.size()== 0){
					p.sendMessage(msg);
					p.sendMessage(ChatColor.YELLOW + "No one near!");
				}else if(!(near.size()== 0)){
					for(Entity en:near){
						if(!(en instanceof Player)){
							p.sendMessage(msg);
							p.sendMessage(ChatColor.YELLOW + "No one near!");
						}
					}
				}
			}else{
				event.setCancelled(true);
				plugin.getServer().broadcastMessage(msg);
			}
		}
	}
}
