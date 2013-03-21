package me.Travja.HungerArena;

import java.util.HashMap;

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
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.getArena(p)!= null){
			a = plugin.getArena(p);
			if(plugin.Frozen.get(a).contains(pname) && plugin.config.getString("Frozen_Teleport").equalsIgnoreCase("True")){
				if(plugin.config.getString("Explode_on_Move").equalsIgnoreCase("true")){
					for(String players: plugin.Playing.get(a)){
						final Player playing = plugin.getServer().getPlayerExact(players);
						i = plugin.Playing.get(a).indexOf(players);
						if(!timeUp.get(a)){
							if(!playing.getLocation().getBlock().getLocation().equals(plugin.location.get(a).get(i))){
								playing.teleport(plugin.location.get(a).get(i));
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
									public void run(){
										if(!timeUp.get(a)){
											timeUp.put(a, true);
										}
									}
								},30L);
							}
						}else{
							if(!playing.getLocation().getBlock().getLocation().equals(plugin.location.get(a).get(i))){
								if(!plugin.Dead.get(a).contains(playing.getName())){
									World world = playing.getLocation().getWorld();
									world.createExplosion(playing.getLocation(), 0.0F, false);
									playing.setHealth(0);
								}
							}
						}
					}
				}else{
					for(String players:plugin.Playing.get(a)){
						Player playing = plugin.getServer().getPlayerExact(players);
						i = plugin.Playing.get(a).indexOf(players)+1;
						if(!playing.getLocation().getBlock().getLocation().equals(plugin.location.get(a).get(i))){
							playing.teleport(plugin.location.get(a).get(i));
						}
					}
				}
			}
		}
	}
}
