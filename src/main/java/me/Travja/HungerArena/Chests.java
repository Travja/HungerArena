package me.Travja.HungerArena;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
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
			if (plugin.getChests().getConfigurationSection("Storage").getKeys(false).contains(blockx + "," + blocky + "," + blockz)) {
				if(p.hasPermission("HungerArena.Chest.Break") && plugin.getArena(p)== null){
					plugin.getChests().set("Storage." + blockx + "," + blocky+ "," + blockz, null);
					plugin.saveChests();
					p.sendMessage("[HungerArena] Chest Removed!");
				} else {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "[HungerArena] That's a storage! You don't have permission to break it!");
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
				if(!plugin.restricted || (plugin.restricted && plugin.worldsNames.values().contains(p.getWorld().getName()))){
					if(block!= null){
						if(block.getState() instanceof InventoryHolder){
							ItemStack[] itemsinchest = ((InventoryHolder) block.getState()).getInventory().getContents().clone();
							int blockx = block.getX();
							int blocky = block.getY();
							int blockz = block.getZ();
							String blockw = block.getWorld().getName().toString();
							if(!plugin.getChests().contains("Storage." + blockx + "," + blocky + "," + blockz)){
								plugin.getChests().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.X", blockx);
								plugin.getChests().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Y", blocky);
								plugin.getChests().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.Z",blockz);
								plugin.getChests().set("Storage." + blockx + "," + blocky + "," + blockz + ".Location.W", blockw);
								plugin.getChests().set("Storage." + blockx + "," + blocky + "," + blockz + ".ItemsInStorage", itemsinchest);
								plugin.getChests().set("Storage." + blockx + "," + blocky + "," + blockz + ".Arena", a);
								plugin.saveChests();
								p.sendMessage(ChatColor.GREEN + "Thank you for finding this undiscovered item Storage, it has been stored!!");
								if (plugin.config.getBoolean("ChestPay.enabled")){
									for(ItemStack Rewards: plugin.ChestPay){
										p.getInventory().addItem(Rewards);
									}
								}
							}
							plugin.reloadChests();
						}
					}
				}
			}
		}
	}
}
