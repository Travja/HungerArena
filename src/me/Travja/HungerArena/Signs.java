package me.Travja.HungerArena;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
				if(lines[0].equalsIgnoreCase("[HungerArena]")){
					if(lines[1].equalsIgnoreCase("Join")){
						p.performCommand("ha join");
					}
					if(lines[1].equalsIgnoreCase("Confirm")){
						p.performCommand("ha confirm");
					}
					if(lines[1].equalsIgnoreCase("Leave")){
						p.performCommand("ha leave");
					}
				}
				if(lines[0].equalsIgnoreCase("[Sponsor]")){
					p.performCommand("sponsor" + " " + lines[1] + " " + lines[2] + " " + lines[3]);
				}
			}
		}
	}
}
