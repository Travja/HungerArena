package me.Travja.HungerArena;

import java.io.File;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;

public class WorldChange implements Listener {
	public Main plugin;
	public WorldChange(Main m) {
		plugin = m;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@EventHandler
	public void worldChange(PlayerChangedWorldEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(new File(plugin.getDataFolder(), pname + ".yml").exists()){
			FileConfiguration pinfo = plugin.getPConfig(pname);
			if(pinfo.getString("world")== p.getWorld().getName() && plugin.needInv.contains(pname)){
				try{
					int list056;
					list056 = 0;
					int limit = pinfo.getStringList("inv").size(); 
					while(limit > list056){
						p.getInventory().clear();
						ItemStack[] pinv = null;
						Object o = pinfo.get("inv");
						if(o instanceof ItemStack[]){
							pinv = (ItemStack[]) o;
						}else if(o instanceof List){
							pinv = (ItemStack[]) ((List<ItemStack>) o).toArray(new ItemStack[0]);
						}
						list056 = list056+1;
						p.getInventory().setContents(pinv);
						p.updateInventory();
					}
					list056 = 0;
					limit = pinfo.getStringList("armor").size(); 
					while(limit > list056){
						p.getInventory().clear();
						ItemStack[] parmor = null;
						Object o = pinfo.get("armor");
						if(o instanceof ItemStack[]){
							parmor = (ItemStack[]) o;
						}else if(o instanceof List){
							parmor = (ItemStack[]) ((List<ItemStack>) o).toArray(new ItemStack[0]);
						}
						list056 = list056+1;
						p.getInventory().setContents(parmor);
						p.updateInventory();
					}
					p.sendMessage(ChatColor.GOLD + "[HA] " + ChatColor.GREEN + "Your inventory has been restored!");
					new File(plugin.getDataFolder(), pname + ".yml").delete();
					plugin.needInv.remove(pname);
				}catch(Exception e){
					p.sendMessage(ChatColor.RED + "Something went wrong when trying to restore your inv, please contact an administrator.");
					System.out.println("Error occured when trying to restore the inv of " + pname + ":");
					System.out.println(e);
				}
			}
		}
	}
}
