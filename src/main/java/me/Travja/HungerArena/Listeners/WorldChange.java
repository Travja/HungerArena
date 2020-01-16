package me.Travja.HungerArena.Listeners;

import me.Travja.HungerArena.Main;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChange implements Listener {
	public Main plugin;
	public WorldChange(Main m) {
		plugin = m;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void worldChangeLow(PlayerChangedWorldEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		String ThisWorld = p.getWorld().getName();
		String FromWorld = event.getFrom().getName();
		if (!plugin.worldsNames.values().contains(ThisWorld) && plugin.worldsNames.values().contains(FromWorld)) {
				plugin.RestoreInv(p, pname);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH) 
	public void worldChangeHigh(PlayerChangedWorldEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		String ThisWorld = p.getWorld().getName();
		int a=0;
		for(int i : plugin.worldsNames.keySet()){
			if(plugin.worldsNames.get(i)!= null){
				if (plugin.worldsNames.get(i).equals(ThisWorld)){
					a=i;
					if(plugin.frozen.get(a)!=null && plugin.frozen.get(a).contains(pname)){
						return;
					}else{
						plugin.RestoreInv(p, pname);
						if (plugin.config.getString("joinTeleport").equalsIgnoreCase("true")) {
							String[] Spawncoords = plugin.spawns.getString("Spawn_coords."+a).split(",");
							double spawnx = Double.parseDouble(Spawncoords[0]);
							double spawny = Double.parseDouble(Spawncoords[1]);
							double spawnz = Double.parseDouble(Spawncoords[2]);
							Location Spawn = new Location(p.getWorld(), spawnx, spawny, spawnz);
							if (!p.getLocation().getBlock().equals(Spawn.getBlock())){
								p.teleport(Spawn);
							}
						}
					}
				}
			}
		}
	}
}
