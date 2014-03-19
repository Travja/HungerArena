package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.Main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class spawnsListener implements Listener{
	public Main plugin;
	public spawnsListener(Main m){
		this.plugin = m;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void interact(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(plugin.setting.containsKey(p.getName())){
			if(event.getAction()==Action.RIGHT_CLICK_BLOCK){
				Location l = event.getClickedBlock().getLocation();
				if(p.getItemInHand().getTypeId()== plugin.config.getInt("spawnsTool")){
					String[] info = plugin.setting.get(p.getName()).split("-");
					if(Integer.parseInt(info[1])!= plugin.config.getInt("maxPlayers")+1){
						String coords = l.getWorld() + " " + (l.getX()+.5) + " " + (l.getY()+1) + " " + (l.getZ()+.5);
						p.performCommand("startpoint " + info[0] + " " + info[1] + " " + coords);
						if(Integer.parseInt(info[1])>= plugin.config.getInt("maxPlayers")){
							p.sendMessage(ChatColor.DARK_AQUA + "[HungerArena] " + ChatColor.RED + "All spawns set!");
							plugin.setting.remove(p.getName());
						}else{
							plugin.setting.put(p.getName(), info[0] + "-" + (Integer.parseInt(info[1])+1));
							p.sendMessage(ChatColor.DARK_AQUA + "[HungerArena] " + ChatColor.RED + "Next starting point: " + (Integer.parseInt(info[1])+1));
						}
					}
				}
			}
		}
	}
}