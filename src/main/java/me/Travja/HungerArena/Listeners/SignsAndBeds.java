package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.Main;
import org.bukkit.ChatColor;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignsAndBeds implements Listener {
	public Main plugin;
	public SignsAndBeds(Main m) {
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
			//2019 1.14 Translate Materials:
			boolean foundSign=false;
			try {
				if (b.getState() instanceof org.bukkit.block.Sign){
					foundSign=true;
				}
			} catch (Exception e){
					if (!foundSign) {
						foundSign = Tag.SIGNS.isTagged(b.getType());
					}
			}
			if (foundSign) {
				org.bukkit.block.Sign sign = (org.bukkit.block.Sign) b.getState();
				String line1 = sign.getLine(0);
				String line2 = sign.getLine(1);
				String line3 = sign.getLine(2);
				String line4 = sign.getLine(3);
				if(line1.trim().equalsIgnoreCase(ChatColor.BLUE + "[HungerArena]") || line1.trim().equalsIgnoreCase(ChatColor.BLUE + "[HA]")){
					if(!line2.equals("") && line3.equals(""))
						p.performCommand("ha " + line2);
					else if(line2.equals("") && !line3.equals(""))	
						p.performCommand("ha " + line3);
					else if(!line2.equals("") && !line3.equals("")) {
						String commands = "close,join,kick,leave,list,open,ready,refill,reload,restart,rlist,tp,start,watch,warpall";
						if (commands.contains(line3.trim().toLowerCase())) {
							p.performCommand("ha " + line2);
							p.performCommand("ha " + line3);
						} else  p.performCommand("ha " + line2 + " " + line3);
					}
					else
						p.performCommand("ha");
				}
				if(line1.trim().equalsIgnoreCase(ChatColor.BLUE + "[Sponsor]")){
					p.performCommand("sponsor " + line2 + " " + line3 + " " + line4);
				}
			}
			
			if (plugin.config.getString("DenyBedUsage").equalsIgnoreCase("True")){
				boolean foundBed=false;
				try { 
					if (b.getState() instanceof org.bukkit.block.Bed) {
						foundBed=true;
					}
				} catch (Exception e){
					if (!foundBed) foundBed = Tag.BEDS.isTagged(b.getType());		
				}
				if (foundBed) {
					event.setCancelled(true);
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

