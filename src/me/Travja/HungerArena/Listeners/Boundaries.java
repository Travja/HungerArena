package me.Travja.HungerArena.Listeners;

import java.util.Map;
import java.util.Map.Entry;

import me.Travja.HungerArena.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class Boundaries implements Listener{
	public Main plugin;
	public Boundaries(Main m){
		this.plugin = m;
	}

	@EventHandler
	public void boundsCheck(PlayerMoveEvent event){
		Player p = event.getPlayer();
		Boolean inGame = plugin.getArena(p) != null;
		Boolean spectating = plugin.isSpectating(p);
		if(plugin.config.getBoolean("WorldEdit"))
			if(insideBounds(p.getLocation()))
				if(inGame || spectating)
					event.setCancelled(true);
	}
	@EventHandler
	public void blockBounds(BlockBreakEvent event){
		Player p = event.getPlayer();
		if(plugin.getArena(p)== null)
			if(plugin.config.getBoolean("WorldEdit"))
				if(insideBounds(event.getBlock().getLocation())){
					p.sendMessage(ChatColor.RED + "That block is protected by HungerArena!");
					event.setCancelled(true);
				}
	}
	public boolean insideBounds(Location l){
		Location minl = null;
		Location maxl = null;
		if(plugin.spawns.get("Arena")!= null){
			Map<String, Object> temp = plugin.spawns.getConfigurationSection("Arena").getValues(false);
			for(Entry<String, Object> entry: temp.entrySet()){
				if(plugin.spawns.getConfigurationSection("Arena." + entry.getKey())!= null){
					String[] min = ((String) plugin.spawns.get("Arena." + entry.getKey()) + ".Min").split(",");
					String[] max = ((String) plugin.spawns.get("Arena." + entry.getKey()) + ".Max").split(",");
					try{
						World world = Bukkit.getWorld(min[0]);
						double x = Double.parseDouble(min[1]);
						double y = Double.parseDouble(min[2]);
						double z = Double.parseDouble(min[3]);
						minl = new Location(world, x, y, z);
						World world2 = Bukkit.getWorld(max[0]);
						double x2 = Double.parseDouble(max[1]);
						double y2 = Double.parseDouble(max[2]);
						double z2 = Double.parseDouble(max[3]);
						minl = new Location(world2, x2, y2, z2);
					}catch(Exception e){
						System.out.println(e);
						return false;
					}
					if(minl!= null && maxl!= null){
						return l.getX() >= minl.getBlockX()
								&& l.getX() < maxl.getBlockX() + 1 && l.getY() >= minl.getBlockY()
								&& l.getY() < maxl.getBlockY() + 1 && l.getZ() >= minl.getBlockZ()
								&& l.getZ() < maxl.getBlockZ() + 1;  
					}
				}
			}
		}
		return false;
	}
}
