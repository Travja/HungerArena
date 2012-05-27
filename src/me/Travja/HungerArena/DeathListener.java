package me.Travja.HungerArena;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class DeathListener implements Listener{
	public Main plugin;
	public DeathListener(Main m){
		this.plugin = m;
	}
	public FileConfiguration config;
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		Player p = event.getPlayer();
		if(plugin.Dead.contains(p)){
			String[] Spawncoords = plugin.config.getString("Spawn_coords").split(",");
			World spawnw = p.getWorld();
			double spawnx = Double.parseDouble(Spawncoords[0]);
			double spawny = Double.parseDouble(Spawncoords[1]);
			double spawnz = Double.parseDouble(Spawncoords[2]);
			Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
			event.setRespawnLocation(Spawn);
		}
	}
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player p = event.getEntity();
		Server s = p.getServer();
		String pname = p.getName();
		if(plugin.Playing.contains(p)){
			if(plugin.config.getString("Cannon_Death").equalsIgnoreCase("True")){
				p.getWorld().playEffect(p.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 0, 300);
			}
			plugin.Dead.add(p);
			plugin.Playing.remove(p);
			String leftmsg = ChatColor.BLUE + "There are now " + plugin.Playing.size() + " tributes left!";
			if(p.getKiller() instanceof Player){
				if(p.getKiller().getItemInHand().getType().getId()== 0){
					Player killer = p.getKiller();
					String killername = killer.getName();
					event.setDeathMessage("");
					s.broadcastMessage(ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with their FIST!");
					s.broadcastMessage(leftmsg);
					if(plugin.Playing.size()== 1){
						for(Player winner:plugin.Playing){
							String winnername = winner.getName();
							s.broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.getInventory().setBoots(null);
							winner.getInventory().setChestplate(null);
							winner.getInventory().setHelmet(null);
							winner.getInventory().setLeggings(null);
							winner.getInventory().addItem(plugin.Reward);
						}
						for(Player spectator:plugin.Watching){
							spectator.setAllowFlight(false);
						}
						if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
							plugin.Dead.clear();
							plugin.Playing.clear();
							plugin.Quit.clear();
							plugin.Watching.clear();
							plugin.Frozen.clear();
							plugin.canjoin = false;
						}
					}
				}else{
					Player killer = p.getKiller();
					String killername = killer.getName();
					Material weapon = killer.getItemInHand().getType();
					String msg = ChatColor.LIGHT_PURPLE + "**BOOM** Tribute " + pname + " was killed by tribute " + killername + " with a(n) " + weapon;
					event.setDeathMessage("");
					s.broadcastMessage(msg);
					s.broadcastMessage(leftmsg);
					if(plugin.Playing.size()== 1){
						for(Player winner:plugin.Playing){
							String winnername = winner.getName();
							s.broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
							winner.getInventory().clear();
							winner.getInventory().setBoots(null);
							winner.getInventory().setChestplate(null);
							winner.getInventory().setHelmet(null);
							winner.getInventory().setLeggings(null);
							winner.getInventory().addItem(plugin.Reward);
						}
						for(Player spectator:plugin.Watching){
							spectator.setAllowFlight(false);
						}
						if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
							plugin.Dead.clear();
							plugin.Playing.clear();
							plugin.Quit.clear();
							plugin.Watching.clear();
							plugin.Frozen.clear();
							plugin.canjoin = false;
						}
					}
				}
			}else{
				event.setDeathMessage("");
				s.broadcastMessage(ChatColor.LIGHT_PURPLE + pname + " died of natural causes!");
				s.broadcastMessage(leftmsg);
				if(plugin.Playing.size()== 1){
					for(Player winner:plugin.Playing){
						String winnername = winner.getName();
						s.broadcastMessage(ChatColor.GREEN + winnername + " is the victor of this Hunger Games!");
						winner.getInventory().clear();
						winner.getInventory().setBoots(null);
						winner.getInventory().setChestplate(null);
						winner.getInventory().setHelmet(null);
						winner.getInventory().setLeggings(null);
						winner.getInventory().addItem(plugin.Reward);
					}
					for(Player spectator:plugin.Watching){
						spectator.setAllowFlight(false);
					}
					if(plugin.config.getString("Auto_Restart").equalsIgnoreCase("True")){
						plugin.Dead.clear();
						plugin.Playing.clear();
						plugin.Quit.clear();
						plugin.Watching.clear();
						plugin.Frozen.clear();
						plugin.canjoin = false;
					}
				}
			}
		}
		if(plugin.Watching.contains(p)){
			for(Player online:plugin.getServer().getOnlinePlayers())
				online.showPlayer(p);
		}
	}
}
