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
		if (b == null) {
			return;
		}
		if(event.getAction()== Action.RIGHT_CLICK_BLOCK){
			if(b.getType()== Material.SIGN || b.getType()==Material.SIGN_POST || b.getType()==Material.WALL_SIGN){
				org.bukkit.block.Sign sign = (org.bukkit.block.Sign) b.getState();
				String line1 = sign.getLine(0);
				String line2 = sign.getLine(1);
				String line3 = sign.getLine(2);
				String line4 = sign.getLine(3);
				if(line1.equalsIgnoreCase(ChatColor.BLUE + "[HungerArena]") || line1.equalsIgnoreCase(ChatColor.BLUE + "[HA]")){
					if(!line2.equals(""))
						p.performCommand("ha " + line2);
					else if(!line3.equals(""))
						p.performCommand("ha " + line2 + " " + line3);
					else
						p.performCommand("ha");
				}
				if(line1.equalsIgnoreCase(ChatColor.BLUE + "[Sponsor]")){
					p.performCommand("sponsor " + line2 + " " + line3 + " " + line4);
				}
			}
		}
	}
	@EventHandler
	public void Create(SignChangeEvent event){
		String top = event.getLine(0);
		if(top.equalsIgnoreCase("[HungerArena]") || top.equalsIgnoreCase("[HA]") || top.equalsIgnoreCase("[Sponsor]")){
			event.setLine(0, ChatColor.BLUE + top);
		}
	}
}
