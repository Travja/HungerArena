package me.Travja.HungerArena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BlockStorage implements Listener {
	public Main plugin;
	public BlockStorage(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void BlockBreak(BlockBreakEvent event){
		Block b = event.getBlock();
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Playing.contains(pname)){
			if(plugin.config.getString("Protected_Arena").equalsIgnoreCase("True")){
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You can't break blocks when you're playing!");
			}
			if(plugin.canjoin){
				if(plugin.config.getStringList("worlds").isEmpty() || (!plugin.config.getStringList("worlds").isEmpty() && plugin.config.getStringList("worlds").contains(p.getWorld().getName()))){
					String w = b.getWorld().getName();
					int x = b.getX();
					int y = b.getY();
					int z = b.getZ();
					int d = b.getTypeId();
					byte m = b.getData();
					String coords = w + "," + x + "," + y + "," + z + "," + d + "," + m;
					List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
					if(!plugin.data.getStringList("Blocks_Placed").contains(w + "," + x + "," + y + "," + z)){
						blocks.add(coords);
						plugin.data.set("Blocks_Destroyed", blocks);
						plugin.saveData();
					}
				}
			}
		}
	}
	@EventHandler
	public void Explosion(EntityExplodeEvent event){
		List<Block> blocksd = event.blockList();
		Entity e = event.getEntity();
		if(plugin.canjoin){
			if(plugin.config.getStringList("worlds").isEmpty() || (!plugin.config.getStringList("worlds").isEmpty() && plugin.config.getStringList("worlds").contains(event.getEntity().getWorld().getName()))){
				if(e.getType()== EntityType.PRIMED_TNT){
					if(!plugin.data.getStringList("Blocks_Placed").contains(e.getLocation().getWorld() + "," + e.getLocation().getX() + "," + e.getLocation().getY() + "," + e.getLocation().getZ()) /*|| !plugin.data.getStringList("Blocks_Destroyed").contains(e.getLocation().getWorld() + "," + e.getLocation().getX() + "," + e.getLocation().getY() + "," + e.getLocation().getZ())*/){
						List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
						blocks.add(e.getLocation().getWorld().getName() + "," + e.getLocation().getX() + "," + e.getLocation().getY() + "," + e.getLocation().getZ() + ",46" + ",0");
						plugin.data.set("Blocks_Destroyed", blocks);
						plugin.saveData();
						plugin.getServer().broadcastMessage("TNT blew up!");
					}
				}
				for(Block b:blocksd){
					String w = event.getEntity().getWorld().getName();
					int x = b.getX();
					int y = b.getY();
					int z = b.getZ();
					int d = b.getTypeId();
					byte m = b.getData();
					String coords = w + "," + x + "," + y + "," + z + "," + d + "," + m;
					List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
					if(!plugin.data.getStringList("Blocks_Placed").contains(w + "," + x + "," + y + "," + z) || !plugin.data.getStringList("Blocks_Destroyed").contains(w + "," + x + "," + y + "," + z)){
						blocks.add(coords);
						plugin.data.set("Blocks_Destroyed", blocks);
						plugin.saveData();
					}
				}
			}
		}	
	}
	@EventHandler
	public void burningBlocks(BlockBurnEvent event){
		Block b = event.getBlock();
		if(plugin.canjoin== true){
			if(plugin.config.getStringList("worlds").isEmpty() || (!plugin.config.getStringList("worlds").isEmpty() && plugin.config.getStringList("worlds").contains(b.getWorld().getName()))){
				String w = b.getWorld().getName();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				int d = b.getTypeId();
				byte m = b.getData();
				String coords = w + "," + x + "," + y + "," + z + "," + d + "," + m;
				List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
				if(!plugin.data.getStringList("Blocks_Placed").contains(w + "," + x + "," + y + "," + z)){
					blocks.add(coords);
					plugin.data.set("Blocks_Destroyed", blocks);
					plugin.saveData();
				}
			}
		}
	}
	@EventHandler
	public void blockPlace(BlockPlaceEvent event){
		Block b = event.getBlock();
		Player p = event.getPlayer();
		if(plugin.Playing.contains(p.getName())){
			if(plugin.canjoin){
				if(plugin.config.getStringList("worlds").isEmpty() || (!plugin.config.getStringList("worlds").isEmpty() && plugin.config.getStringList("worlds").contains(b.getWorld().getName()))){
					//TODO
					if((b.getType()== Material.SAND || b.getType()== Material.GRAVEL) && (b.getRelative(BlockFace.DOWN).getType()== Material.AIR || b.getRelative(BlockFace.DOWN).getType()== Material.WATER || b.getRelative(BlockFace.DOWN).getType()== Material.LAVA)){
						int n = b.getY() -1;
						while(b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()== Material.AIR || b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()== Material.WATER || b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()== Material.LAVA){
							n = n -1;
							event.getPlayer().sendMessage(b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType().toString().toLowerCase());
							if(b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()!= Material.AIR || b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()!= Material.WATER || b.getWorld().getBlockAt(b.getX(), n, b.getZ()).getType()!= Material.LAVA){
								int l = n +1;
								Block br = b.getWorld().getBlockAt(b.getX(), l, b.getZ());
								String w = br.getWorld().getName();
								int x = br.getX();
								int y = br.getY();
								int z = br.getZ();
								String coords = w + "," + x + "," + y + "," + z;
								p.sendMessage(ChatColor.GREEN + "Sand/Gravel will land at " + coords);
								List<String> blocks = plugin.data.getStringList("Blocks_Placed");
								blocks.add(coords);
								plugin.data.set("Blocks_Placed", blocks);
								plugin.saveData();
							}
						}
					}else{
						if(b.getType()!= Material.SAND || b.getType()!= Material.GRAVEL){
							String w = b.getWorld().getName();
							int x = b.getX();
							int y = b.getY();
							int z = b.getZ();
							String coords = w + "," + x + "," + y + "," + z;
							List<String> blocks = plugin.data.getStringList("Blocks_Placed");
							blocks.add(coords);
							plugin.data.set("Blocks_Placed", blocks);
							plugin.saveData();
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void bucketEmpty(PlayerBucketEmptyEvent event){
		if(plugin.canjoin){
			if(plugin.Playing.contains(event.getPlayer().getName())){
				if(plugin.config.getStringList("worlds").isEmpty() || (!plugin.config.getStringList("worlds").isEmpty() && plugin.config.getStringList("worlds").contains(event.getPlayer().getWorld().getName()))){
					Block b = event.getBlockClicked().getRelative(event.getBlockFace());
					String w = b.getWorld().getName();
					int x = b.getX();
					int y = b.getY();
					int z = b.getZ();
					String coords = w + "," + x + "," + y + "," + z;
					List<String> blocks = plugin.data.getStringList("Blocks_Placed");
					blocks.add(coords);
					plugin.data.set("Blocks_Placed", blocks);
					plugin.saveData();
				}
			}
		}
	}
	@EventHandler
	public void bucketFill(PlayerBucketFillEvent event){
		if(plugin.canjoin){
			if(plugin.Playing.contains(event.getPlayer().getName())){
				if(plugin.config.getStringList("worlds").isEmpty() || (!plugin.config.getStringList("worlds").isEmpty() && plugin.config.getStringList("worlds").contains(event.getPlayer().getWorld().getName()))){
					Block b = event.getBlockClicked().getRelative(event.getBlockFace());
					String w = b.getWorld().getName();
					int x = b.getX();
					int y = b.getY();
					int z = b.getZ();
					int d = b.getTypeId();
					byte m = b.getData();
					String coords = w + "," + x + "," + y + "," + z + "," + d + "," + m;
					List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
					if(!plugin.data.getStringList("Blocks_Placed").contains(w + "," + x + "," + y + "," + z)){
						blocks.add(coords);
						plugin.data.set("Blocks_Destroyed", blocks);
						plugin.saveData();
					}
				}
			}
		}
	}
	@EventHandler
	public void blockMelt(BlockFadeEvent event){
		if(plugin.canjoin){
			if(plugin.config.getStringList("worlds").isEmpty() || (!plugin.config.getStringList("worlds").isEmpty() && plugin.config.getStringList("worlds").contains(event.getBlock().getWorld().getName()))){
				Block b = event.getBlock();
				String w = b.getWorld().getName();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				int d = b.getTypeId();
				byte m = b.getData();
				String coords = w + "," + x + "," + y + "," + z + "," + d + "," + m;
				List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
				if(!plugin.data.getStringList("Blocks_Placed").contains(w + "," + x + "," + y + "," + z)){
					blocks.add(coords);
					plugin.data.set("Blocks_Destroyed", blocks);
					plugin.saveData();
				}
			}
		}
	}
	/*@EventHandler
	public void blockGrow(BlockGrowEvent event){
		if(plugin.canjoin== true){
			Block b = event.getBlock();
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			System.out.println("Grow: " + coords);
			List<String> blocks = plugin.data.getStringList("Blocks_Placed");
			blocks.add(coords);
			plugin.data.set("Blocks_Destroyed", blocks);
			plugin.saveConfig();
		}
	}
	@EventHandler
	public void blockForm(BlockFormEvent event){
		if(plugin.canjoin== true){
			Block b = event.getBlock();
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			System.out.println("Snowfall: " + coords);
			List<String> blocks = plugin.data.getStringList("Blocks_Placed");
			blocks.add(coords);
			plugin.data.set("Blocks_Destroyed", blocks);
			plugin.saveConfig();
		}
	}
	@EventHandler
	public void pistonPush(BlockPistonExtendEvent event){
		if(plugin.canjoin== true){
			for(Block b:event.getBlocks()){
				String w = b.getWorld().getName();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				int d = b.getTypeId();
				String coords = w + "," + x + "," + y + "," + z + "," + d;
				System.out.println("Piston: " + coords);
				List<String> blocks = plugin.data.getStringList("Blocks_Destroyed");
				blocks.add(coords);
				plugin.data.set("Blocks_Destroyed", blocks);
				plugin.saveConfig();
			}
		}
	}
	@EventHandler
	public void onChange(BlockPhysicsEvent event){
		Block block = event.getBlock();
		Material changed = event.getChangedType();
		if (block.getType() == Material.LAVA){
			if (changed == Material.LAVA){
				Block b = event.getBlock();
				String w = b.getWorld().getName();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				int d = b.getTypeId();
				String coords = w + "," + x + "," + y + "," + z + "," + d;
				System.out.println("Lava Change: " + coords);
				List<String> blocks = plugin.data.getStringList("Blocks_Placed");
				blocks.add(coords);
				plugin.data.set("Blocks_Destroyed", blocks);
				plugin.saveConfig();
			}else if(changed == Material.WATER){
				Block b = event.getBlock();
				String w = b.getWorld().getName();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				int d = b.getTypeId();
				String coords = w + "," + x + "," + y + "," + z + "," + d;
				System.out.println("Water Change: " + coords);
				List<String> blocks = plugin.data.getStringList("Blocks_Placed");
				blocks.add(coords);
				plugin.data.set("Blocks_Destroyed", blocks);
				plugin.saveConfig();
			}
		}else if (block.getType() == Material.SAND || block.getType() == Material.GRAVEL) {
			if (changed == Material.AIR) {
				Block b = event.getBlock();
				String w = b.getWorld().getName();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				int d = b.getTypeId();
				String coords = w + "," + x + "," + y + "," + z + "," + d;
				System.out.println("Sand/Gravel Fall: " + coords);
				List<String> blocks = plugin.data.getStringList("Blocks_Placed");
				blocks.add(coords);
				plugin.data.set("Blocks_Destroyed", blocks);
				plugin.saveConfig();
			}
		}
	}*/
}