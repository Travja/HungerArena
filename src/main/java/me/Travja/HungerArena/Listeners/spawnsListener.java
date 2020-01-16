package me.Travja.HungerArena.Listeners;

import java.util.Collection;

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

	@EventHandler
	public void interact(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if(plugin.setting.containsKey(p.getName())){
			if(event.getAction()==Action.RIGHT_CLICK_BLOCK){
				Location l = event.getClickedBlock().getLocation();
				String HandItem;
				try {
					HandItem=p.getInventory().getItemInMainHand().getType().name();
				} catch (Exception e){
					HandItem=p.getItemInHand().getType().name();
				}
				if(HandItem == plugin.config.getString("spawnsTool")){ 
					String[] info = plugin.setting.get(p.getName()).split("-");
					if(Integer.parseInt(info[1])!= plugin.config.getInt("maxPlayers")+1){
						String coords = l.getWorld().getName() + " " + ((double)l.getX()+.5) + " " + ((double)l.getY()+1) + " " + ((double)l.getZ()+.5); 

						int arena = Integer.parseInt(info[0]);
						if (plugin.spawns.get("Spawns." + arena) != null){
							Collection<Object> temp = plugin.spawns.getConfigurationSection("Spawns."+arena).getValues(false).values();
							if (temp.contains(coords.trim().replace(' ', ','))){
								event.setCancelled(true);
								return;
							}
						}
						
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