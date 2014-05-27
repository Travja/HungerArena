package me.Travja.HungerArena.Listeners;

import java.util.ArrayList;
import java.util.HashMap;

import me.Travja.HungerArena.Main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {
	public Main plugin;
	public FreezeListener(Main m) {
		this.plugin = m;
	}
	int i = 0;
	int a = 0;
	private HashMap<Integer, Boolean> timeUp= new HashMap<Integer, Boolean>();
	private ArrayList<Integer> timing = new ArrayList<Integer>();
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.getArena(p)!= null){
			a = plugin.getArena(p);
			if(plugin.Frozen.get(a).contains(pname) && plugin.config.getString("Frozen_Teleport").equalsIgnoreCase("True")){
				if(plugin.config.getString("Explode_on_Move").equalsIgnoreCase("true")){
					timeUp.put(a, false);
					for(String players: plugin.Playing.get(a)){
						Player playing = plugin.getServer().getPlayerExact(players);
						i = plugin.Playing.get(a).indexOf(players)+1;
						if(!timeUp.get(a) && !timing.contains(a)){
							timing.add(a);
							if(!playing.getLocation().getBlock().getLocation().equals(plugin.location.get(a).get(i).getBlock().getLocation())){
								playing.teleport(plugin.location.get(a).get(i));
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
									public void run(){
										if(!timeUp.get(a)){
											timeUp.put(a, true);
											timing.remove(a);
										}
									}
								},30L);
							}
						}else{
							if(!playing.getLocation().getBlock().getLocation().equals(plugin.location.get(a).get(i).getBlock().getLocation())){
								if(!plugin.Dead.get(a).contains(playing.getName())){
									plugin.Dead.get(a).add(playing.getName());
									World world = playing.getLocation().getWorld();
									world.createExplosion(playing.getLocation(), 0.0F, false);
									playing.setHealth(0);
								}
							}
						}
					}
					if(plugin.Dead.get(a).contains(pname) && plugin.Playing.get(a).contains(pname)){
						int players = plugin.Playing.get(a).size()-1;
						String leftmsg = ChatColor.BLUE + "There are now " + players + " tributes left!";
						if(plugin.config.getString("Cannon_Death").equalsIgnoreCase("True")){
							double y = p.getLocation().getY();
							double newy = y+200;
							double x = p.getLocation().getX();
							double z = p.getLocation().getZ();
							Location strike = new Location(p.getWorld(), x, newy, z);
							p.getWorld().strikeLightning(strike);
						}
						if(plugin.config.getBoolean("broadcastAll")){
							p.getServer().broadcastMessage(pname + ChatColor.LIGHT_PURPLE + " Stepped off their pedestal too early!");
						}else{
							for(String gn: plugin.Playing.get(a)){
								Player g = plugin.getServer().getPlayer(gn);
								g.sendMessage(pname + ChatColor.LIGHT_PURPLE + " Stepped off their pedestal too early!");
							}
						}
						plugin.Frozen.get(a).remove(pname);
						plugin.Playing.get(a).remove(pname);
						if(plugin.config.getBoolean("broadcastAll")){
							p.getServer().broadcastMessage(leftmsg);
						}else{
							for(String gn: plugin.Playing.get(a)){
								Player g = plugin.getServer().getPlayer(gn);
								g.sendMessage(leftmsg);
							}
						}
						plugin.winner(a);
					}
				}else{
					for(String players:plugin.Playing.get(a)){
						Player playing = plugin.getServer().getPlayerExact(players);
						i = plugin.Playing.get(a).indexOf(players)+1;
						if(!playing.getLocation().getBlock().getLocation().equals(plugin.location.get(a).get(i).getBlock().getLocation())){
							playing.teleport(plugin.location.get(a).get(i));
						}
					}
				}
			}
		}
	}
}
