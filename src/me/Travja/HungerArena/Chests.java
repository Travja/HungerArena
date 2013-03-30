package me.Travja.HungerArena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Chests implements Listener {
	public Main plugin;
	public Chests(Main m) {
		this.plugin = m;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void ChestBreak(BlockBreakEvent event){
		Player p = event.getPlayer();

		Block block = event.getBlock();
		if(p.hasPermission("HungerArena.Chest.Break")){
			Location blocklocation = block.getLocation();
			int blockx = blocklocation.getBlockX();
			int blocky = blocklocation.getBlockY();
			int blockz = blocklocation.getBlockZ();
			if (plugin.getConfig().getStringList("StorageXYZ").contains(blockx + "," + blocky + "," + blockz)) {
				if(p.hasPermission("HungerArena.Chest.Break") && plugin.getArena(p)== null){
					List<String> list2 = plugin.getConfig().getStringList("StorageXYZ");
					list2.remove(blockx + "," + blocky + "," + blockz);
					plugin.getConfig().set("Storage." + blockx + "," + blocky+ "," + blockz, null);
					plugin.getConfig().set("StorageXYZ", list2);
					plugin.getConfig().options().copyDefaults(true);
					plugin.saveConfig();
					p.sendMessage("[HungerArena] Chest Removed!");
				} else {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "[HungerArena] That's a storage chest! You don't have permission to break it!");
				}
			}
		}
	}
	@EventHandler
	public void ChestSaves(PlayerInteractEvent event){
		Block block = event.getClickedBlock();
		Player p = event.getPlayer();
		if(plugin.getArena(p)!= null){
			int a = plugin.getArena(p);
			if(plugin.Playing.get(a).contains(p.getName()) && plugin.canjoin.get(a)){
				if(!plugin.restricted || (plugin.restricted && plugin.worlds.contains(p.getWorld().getName()))){
					if(block!= null){
						if(block.getState() instanceof Chest){
							ItemStack[] itemsinchest = ((Chest) block.getState()).getInventory().getContents();
							int blockx = block.getX();
							int blocky = block.getY();
							int blockz = block.getZ();
							String blockw = block.getWorld().getName().toString();
							if(!plugin.getConfig().contains("Storage." + blockx + "," + blocky + "," + blockz + ".Location.X")){
								plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.X", blockx);
								plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Y", blocky);
								plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Z",blockz);
								plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Location.W", blockw);
								plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".ItemsInStorage", itemsinchest);
								plugin.getConfig().addDefault("Storage." + blockx + "," + blocky + "," + blockz + ".Arena", a);
							}
							List<String> list2 = plugin.getConfig().getStringList("StorageXYZ");
							if(!list2.contains(blockx + "," + blocky + "," + blockz)){
								list2.add(blockx + "," + blocky + "," + blockz);
								plugin.getConfig().set("StorageXYZ", list2);
								plugin.getConfig().options().copyDefaults(true);
								plugin.saveConfig();
								p.sendMessage(ChatColor.GREEN + "Thank you for finding this undiscovered chest, it has been stored!!");
							}
						}
					}
				}
			}
		}
	}
}
