package me.Travja.HungerArena;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
	public void Sign(PlayerInteractEvent event){
		Player p = event.getPlayer();
		Block b = event.getClickedBlock();
		if(event.getAction()== Action.RIGHT_CLICK_BLOCK){
			if(b.getType()== Material.SIGN || b.getType()==Material.SIGN_POST || b.getType()==Material.WALL_SIGN){
				org.bukkit.block.Sign sign = (org.bukkit.block.Sign) b.getState();
				String[] lines = sign.getLines();
				if(lines[0].equalsIgnoreCase(ChatColor.BLUE + "[HungerArena]")){
					if(lines[1].isEmpty()){
						p.performCommand("ha");
					}else{
						p.performCommand("ha " + lines[1]);
					}
				}
				if(lines[0].equalsIgnoreCase(ChatColor.BLUE + "[Sponsor]")){
					p.performCommand("sponsor " + lines[1] + " " + lines[2] + " " + lines[3]);
				}
			}
		}
	}
	@EventHandler
	public void Create(SignChangeEvent event){
		String[] lines = event.getLines();
		String top = lines[0];
		if(top.equalsIgnoreCase("[HungerArena]") || top.equalsIgnoreCase("[Sponsor]")){
			event.setLine(0, ChatColor.BLUE + top);
		}
	}
}
