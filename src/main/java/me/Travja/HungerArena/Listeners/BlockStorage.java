package me.Travja.HungerArena.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.Travja.HungerArena.Main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.material.MaterialData;

public class BlockStorage implements Listener {
	public Main plugin;
	public BlockStorage(Main m) {
		this.plugin = m;
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void BlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		Player p = event.getPlayer();
		String pname = p.getName();
		boolean protall = false;
			if(!p.hasPermission("HungerArena.arena")){	
				protall = protall(); //true = protect allways!! 
			}
		if ((plugin.getArena(p) != null) || (protall)) { 
			//Jeppa: get a default arena if protall is true... (but use getArena if set...)
			int a = 1;
			if (protall) {
				String ThisWorld = p.getWorld().getName();
				for(int z : plugin.worldsNames.keySet()){
					if(plugin.worldsNames.get(z)!= null){	
						if (plugin.worldsNames.get(z).equals(ThisWorld)){
							a=z;
						}
					}
				}
			}
			if (plugin.getArena(p) != null) a = plugin.getArena(p);
			if ((!event.isCancelled()) && (((plugin.Playing.get(a)).contains(pname)) || (protall)))	
			{
				if (plugin.config.getString("Protected_Arena").equalsIgnoreCase("True")) {
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You can't break blocks while playing!");
				} else if (	(plugin.canjoin.get(a) || protall) && ((!plugin.restricted) || ((plugin.restricted) && (plugin.worldsNames.values().contains(p.getWorld().getName()))))) { 
					if (((plugin.management.getStringList("blocks.whitelist").isEmpty()) || ((!plugin.management.getStringList("blocks.whitelist").isEmpty()) && (!plugin.management.getStringList("blocks.whitelist").contains(String.valueOf(b.getType().name()))))) ^ (plugin.management.getBoolean("blocks.useWhitelistAsBlacklist"))) {
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "That is an illegal block!");
					} else {
						String w = b.getWorld().getName();
						addDestroyedBlockToList(b, w, a);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void Explosion(EntityExplodeEvent event){ 
		List<Block> blocksd = event.blockList();
		Entity e = event.getEntity();
		if(!event.isCancelled()){
			for(int i : plugin.canjoin.keySet()){ 
				if(plugin.canjoin.get(i) || protall()){ 
					String ThisWorld = e.getWorld().getName();
				    if ((!plugin.restricted) || ((plugin.restricted) && plugin.worldsNames.get(i)!=null && plugin.worldsNames.get(i).equalsIgnoreCase(ThisWorld))) {  
				    	if(e.getType()== EntityType.PRIMED_TNT){
							e.getLocation().getBlock().setType(Material.TNT);
							Block TNT = e.getLocation().getBlock();				
							addDestroyedBlockToList(TNT, ThisWorld, i);			
							TNT.setType(Material.AIR);							
						}
						for(Block b:blocksd){
							if (!b.getType().name().equalsIgnoreCase("AIR")){
								addDestroyedBlockToList(b, ThisWorld, i);
							}
							if(tnts.contains(e.getUniqueId()) || !plugin.config.getBoolean("explosionDamage")){
								event.blockList().clear();
							}else{
								for(Block b:blocksd){
									String w = event.getEntity().getWorld().getName();
									int x = b.getX();
									int y = b.getY();
									int z = b.getZ();
									int d = b.getTypeId();
									byte m = b.getData();
									String coords = w + "," + x + "," + y + "," + z + "," + d + "," + m + "," + i;
									List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
									if(!plugin.data.getStringList("Blocks_Placed").contains(w + "," + x + "," + y + "," + z + "," + i) || !plugin.data.getStringList("Blocks_Destroyed").contains(w + "," + x + "," + y + "," + z + "," + i)){
										blocks.add(coords);
										plugin.data.set("Blocks_Destroyed", blocks);
										plugin.saveData();
									}
								}
							}
						}
					}
				}
			//}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void burningBlocks(BlockBurnEvent event){ 
		Block b = event.getBlock();
		if(!event.isCancelled()){
			for(int i : plugin.canjoin.keySet()){
				if(plugin.canjoin.get(i) || protall()){
				    if ((!plugin.restricted) || ((plugin.restricted) && (plugin.worldsNames.get(i)!=null && plugin.worldsNames.get(i).equalsIgnoreCase(b.getWorld().getName())))) { 
						String w = b.getWorld().getName();
						addDestroyedBlockToList(b, w, i);
					}
				}
			}
		}
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockPlace(BlockPlaceEvent event){
		Block b = event.getBlock();
		Player p = event.getPlayer();
		boolean protall = false;
			if(!p.hasPermission("HungerArena.arena")){						
				protall = protall(); 
			}
		if ((plugin.getArena(p) != null) || (protall)) {
			int a = 1;
			if (protall) {
				String ThisWorld = p.getWorld().getName();
				for(int z : plugin.worldsNames.keySet()){
					if(plugin.worldsNames.get(z)!= null){	
						if (plugin.worldsNames.get(z).equals(ThisWorld)){
							a=z;
						}
					}
				}
			}
			if (plugin.getArena(p) != null) a = plugin.getArena(p);
			if(!event.isCancelled()){
				if (((plugin.Playing.get(a)).contains(p.getName())) || (protall)) {	
					if((plugin.canjoin.get(a)) || (protall)){
					    if ( (!plugin.restricted) || ((plugin.restricted) && (plugin.worldsNames.values().contains(b.getWorld().getName())))) { 
							if((b.getType()== Material.SAND || b.getType()== Material.GRAVEL) && (b.getRelative(BlockFace.DOWN).getType()== Material.AIR || b.getRelative(BlockFace.DOWN).getType()== Material.WATER || b.getRelative(BlockFace.DOWN).getType()== Material.LAVA)){
								int n = b.getY() -1;
								while(b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()== Material.AIR || b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()== Material.WATER || b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()== Material.LAVA){
									n = n -1;
									event.getPlayer().sendMessage(b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType().toString().toLowerCase());
									if(b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()!= Material.AIR || b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()!= Material.WATER || b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()!= Material.LAVA){
										int l = n +1;
										Block br = b.getWorld().getBlockAt(b.getX(), l, b.getZ());
										String w = br.getWorld().getName();

										addPlacedBlockToList(br, w, a);
									}
								}
							}else{
								if(b.getType()!= Material.SAND && b.getType()!= Material.GRAVEL){
									String w = b.getWorld().getName();

									addPlacedBlockToList(b, w, a);
								}
							}
						}
					}
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public void bucketEmpty(PlayerBucketEmptyEvent event){
		if(plugin.getArena(event.getPlayer())!= null){
			int a = plugin.getArena(event.getPlayer());
			if(!event.isCancelled()){
				if(plugin.canjoin.get(a)){
					if(plugin.Playing.get(a).contains(event.getPlayer().getName())){
					    if ( (!plugin.restricted) || ((plugin.restricted) && (plugin.worldsNames.values().contains(event.getPlayer().getWorld().getName())))) { 
							Block b = event.getBlockClicked().getRelative(event.getBlockFace());
							String w = b.getWorld().getName();
							addPlacedBlockToList(b, w, a);
						}
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void bucketFill(PlayerBucketFillEvent event){
		if(plugin.getArena(event.getPlayer())!= null){
			int a = plugin.getArena(event.getPlayer());
			if(!event.isCancelled()){
				if(plugin.canjoin.get(a)){
					if(plugin.Playing.get(a).contains(event.getPlayer().getName())){
					    if ( (!plugin.restricted) || ((plugin.restricted) && (plugin.worldsNames.values().contains(event.getPlayer().getWorld().getName())))) { 
							Block b = event.getBlockClicked().getRelative(event.getBlockFace());
							String w = b.getWorld().getName();
							addDestroyedBlockToList(b, w, a);
						}
					}
				}
			}
		}
	}
 
	@EventHandler(priority = EventPriority.MONITOR) 
	public void blockMelt(BlockFadeEvent event){
		if(!event.isCancelled()){
			for(int i:plugin.canjoin.keySet()){
				if(plugin.canjoin.get(i) || protall()){
				    if ( (!plugin.restricted) || ((plugin.restricted) && (plugin.worldsNames.get(i)!=null && plugin.worldsNames.get(i).equalsIgnoreCase(event.getBlock().getWorld().getName())))) { 
						Block b = event.getBlock();
						String w = b.getWorld().getName();
						String d = b.getType().name();
						if (d.equalsIgnoreCase("FIRE") || d.equalsIgnoreCase("AIR")) continue; 
						addDestroyedBlockToList(b, w, i);
					}
				}
			}
		}
	}
	
	//SubRoutines:
	private boolean protall(){
		if (plugin.config.getString("Protected_Arena_Always").equalsIgnoreCase("True")) { 
			return true;
		}
		return false;
	}
	
	
	private void addDestroyedBlockToList(Block b, String w, int a){
		int x = b.getX();
		int y = b.getY();
		int z = b.getZ();
		String d = b.getType().name(); 
		byte m=0;
		String BlDataString=null;
		String sp = ";";
		
		String dir="";
		try {
			if (b.getState().getBlockData() instanceof BlockData){
				BlDataString = b.getBlockData().getAsString(); 
			}
		} catch (Exception | NoSuchMethodError ex){
			m = b.getData(); 
			sp=",";
			MaterialData mData = b.getState().getData();
			if (mData instanceof org.bukkit.material.Directional) {
				BlockFace Dir = ((org.bukkit.material.Directional)mData).getFacing();
				if (Dir !=null) {
					dir=sp+(Dir.name());
				}
			}
		}
		String data = BlDataString!=null?BlDataString:String.valueOf(m);
		String coords = w + sp + x + sp + y + sp + z + sp + d + sp + data + sp + a + dir; 
		List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
		if (!plugin.data.getStringList("Blocks_Placed").contains(w + "," + x + "," + y + "," + z + "," + a)) {
			blocks.add(coords);
			plugin.data.set("Blocks_Destroyed", blocks);
			plugin.saveData();
		}
	}
	private void addPlacedBlockToList(Block br, String w, int a){
		int x = br.getX();
		int y = br.getY();
		int z = br.getZ();
		String coords = w + "," + x + "," + y + "," + z + "," + a;
		List<String> blocks = plugin.data.getStringList("Blocks_Placed");
		if (!blocks.contains(coords)){ 
			blocks.add(coords);
			plugin.data.set("Blocks_Placed", blocks);
			plugin.saveData();
		}
	}

}