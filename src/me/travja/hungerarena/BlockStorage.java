package me.Travja.HungerArena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class BlockStorage implements Listener {
	public Main plugin;
	public BlockStorage(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void creeperExplosion(EntityExplodeEvent event){
		if(plugin.canjoin== true){
			for(Block b:event.blockList()){
				String w = b.getWorld().getName();
				int x = b.getX();
				int y = b.getY();
				int z = b.getZ();
				int d = b.getTypeId();
				String coords = w + "," + x + "," + y + "," + z + "," + d;
				System.out.println("Explode: " + coords);
				List<String> blocks = plugin.config.getStringList("Blocks_Destroyed");
				blocks.add(coords);
				plugin.config.set("Blocks_Destroyed", blocks);
				plugin.saveConfig();
			}
		}
	}
	@EventHandler
	public void burningBlocks(BlockBurnEvent event){
		if(plugin.canjoin== true){
			Block b = event.getBlock();
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			System.out.println("Burn: " + coords);
			List<String> blocks = plugin.config.getStringList("Blocks_Destroyed");
			blocks.add(coords);
			plugin.config.set("Blocks_Destroyed", blocks);
			plugin.saveConfig();
		}
	}
	@EventHandler
	public void leafDecay(LeavesDecayEvent event){
		if(plugin.canjoin== true){
			Block b = event.getBlock();
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			System.out.println("Decay: " + coords);
			List<String> blocks = plugin.config.getStringList("Blocks_Destroyed");
			blocks.add(coords);
			plugin.config.set("Blocks_Destroyed", blocks);
			plugin.saveConfig();
		}
	}
	@EventHandler
	public void blockPlace(BlockPlaceEvent event){
		if(plugin.canjoin== true){
			Block b = event.getBlock();
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			if(!(d== 51) && !(d==12) && !(d==13)){
				System.out.println("Place: " + coords);
				List<String> blocks = plugin.config.getStringList("Blocks_Placed");
				blocks.add(coords);
				plugin.config.set("Blocks_Placed", blocks);
				plugin.saveConfig();
			}else if (d == 12 || d == 13) {
				System.out.println("Sand/Gravel");
				int newy = y;
				int replaced = 0;
				Location l = b.getLocation();
				while(l.getBlock().getRelative(BlockFace.DOWN).getType()== Material.AIR){
					newy = newy-1;
					replaced = 0;
					System.out.println(newy);
				}
				while(l.getBlock().getRelative(BlockFace.DOWN).getType()== Material.WATER){
					newy = newy-1;
					replaced = 8;
				}
				while(l.getBlock().getRelative(BlockFace.DOWN).getType()== Material.LAVA){
					newy = newy-1;
					replaced = 10;
				}
				if(l.getBlock().getRelative(BlockFace.DOWN).getType()!= Material.AIR || l.getBlock().getRelative(BlockFace.DOWN).getType()!= Material.WATER || l.getBlock().getRelative(BlockFace.DOWN).getType()!= Material.LAVA){
					event.getPlayer().sendMessage(ChatColor.GREEN + "Block will land at " + x + ", " + newy + ", " + z + " and replaced " + replaced);
				}
				/*Location location = b.getLocation();
				if (location.getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR) {
					int i = 0;
					for (i = location.getBlockY(); i > -1; i --) {
						location = new Location(location.getWorld(), location.getBlockX(), i,  location.getBlockZ());
						if (location.getBlock().getType() != Material.AIR && location.getBlock().getType() != Material.WATER && location.getBlock().getType() != Material.LAVA) {
							break;
						}
						event.getPlayer().sendMessage("Block will land at: " + location);
						System.out.println("Sand/Gravel Place: " + coords);
						List<String> blocks = plugin.config.getStringList("Blocks_Destroyed");
						blocks.add(coords);
						plugin.config.set("Blocks_Destroyed", blocks);
						plugin.saveConfig();
					}
				}*/
			}
		}
	}
	@EventHandler
	public void bucketEmpty(PlayerBucketEmptyEvent event){
		if(plugin.canjoin== true){
			Block clicked = event.getBlockClicked();
			BlockFace face = event.getBlockFace();
			Block b = clicked.getRelative(face);
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			System.out.println("Bucket Empty: " + coords);
			List<String> blocks = plugin.config.getStringList("Blocks_Placed");
			blocks.add(coords);
			plugin.config.set("Blocks_Destroyed", blocks);
			plugin.saveConfig();
		}
	}
	@EventHandler
	public void bucketFill(PlayerBucketFillEvent event){
		if(plugin.canjoin== true){
			Block b = event.getBlockClicked();
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			System.out.println("Bucket Fill: " + coords);
			List<String> blocks = plugin.config.getStringList("Blocks_Destroyed");
			blocks.add(coords);
			plugin.config.set("Blocks_Destroyed", blocks);
			plugin.saveConfig();
		}
	}
	@EventHandler
	public void blockBreak(BlockBreakEvent event){
		if(plugin.canjoin== true){
			Block b = event.getBlock();
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			System.out.println("Break: " + coords);
			List<String> blocks = plugin.config.getStringList("Blocks_Destroyed");
			blocks.add(coords);
			plugin.config.set("Blocks_Destroyed", blocks);
			plugin.saveConfig();
		}
	}
	@EventHandler
	public void blockMelt(BlockFadeEvent event){
		if(plugin.canjoin== true){
			Block b = event.getBlock();
			String w = b.getWorld().getName();
			int x = b.getX();
			int y = b.getY();
			int z = b.getZ();
			int d = b.getTypeId();
			String coords = w + "," + x + "," + y + "," + z + "," + d;
			if(d != 51 && d != 2){
				System.out.println("Fade: " + coords);
				List<String> blocks = plugin.config.getStringList("Blocks_Destroyed");
				blocks.add(coords);
				plugin.config.set("Blocks_Destroyed", blocks);
				plugin.saveConfig();
			}
		}
	}
	@EventHandler
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
			List<String> blocks = plugin.config.getStringList("Blocks_Placed");
			blocks.add(coords);
			plugin.config.set("Blocks_Destroyed", blocks);
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
			List<String> blocks = plugin.config.getStringList("Blocks_Placed");
			blocks.add(coords);
			plugin.config.set("Blocks_Destroyed", blocks);
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
				List<String> blocks = plugin.config.getStringList("Blocks_Destroyed");
				blocks.add(coords);
				plugin.config.set("Blocks_Destroyed", blocks);
				plugin.saveConfig();
			}
		}
	}
	/*@EventHandler
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
				List<String> blocks = plugin.config.getStringList("Blocks_Placed");
				blocks.add(coords);
				plugin.config.set("Blocks_Destroyed", blocks);
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
				List<String> blocks = plugin.config.getStringList("Blocks_Placed");
				blocks.add(coords);
				plugin.config.set("Blocks_Destroyed", blocks);
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
				List<String> blocks = plugin.config.getStringList("Blocks_Placed");
				blocks.add(coords);
				plugin.config.set("Blocks_Destroyed", blocks);
				plugin.saveConfig();
			}
		}
	}*/
}