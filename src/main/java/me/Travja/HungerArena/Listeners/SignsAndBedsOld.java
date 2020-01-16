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
public class SignsAndBedsOld implements Listener {
	public Main plugin;
	public SignsAndBedsOld(Main m) {
		this.plugin = m;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void Sign(PlayerInteractEvent event){
		Player p = event.getPlayer();
		Block b = event.getClickedBlock();
		if (b == null) {
			return;
		}
		if(event.getAction()== Action.RIGHT_CLICK_BLOCK){
			if (b.getState() instanceof org.bukkit.block.Sign){
				Utils.useSign(b, p);
			}
			
			if (plugin.config.getString("DenyBedUsage").trim().equalsIgnoreCase("true")){
				if (b.getState().getData() instanceof org.bukkit.material.Bed) {
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
