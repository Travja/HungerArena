package me.Travja.HungerArena;

//import java.io.File;
//import java.util.List;

//import org.bukkit.ChatColor;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
//import org.bukkit.inventory.ItemStack;

public class WorldChange implements Listener {
	public Main plugin;
	public WorldChange(Main m) {
		plugin = m;
	}

	//@SuppressWarnings({ "deprecation", "unchecked" })
	@EventHandler
	public void worldChange(PlayerChangedWorldEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		String ThisWorld = p.getWorld().getName();
		int i=0;
		int a=0;
		for(i = 1; i <= plugin.worldsNames.size(); i++){			//Jeppa: get the number of arena the player is moving in!
			if(plugin.worldsNames.get(i)!= null){			
				if (plugin.worldsNames.get(i).equals(ThisWorld)){
					a=i;											//now 'a' is the HA-arena the player has moved in now --> can be (or not) the one he joined, !!
				}
			}
		}
//Jeppa:
		if(plugin.Frozen.get(a)!=null){  							//Dont't call it when changing to non-HA-map!
			if(!plugin.Frozen.get(a).contains(pname)){ 				//Only give back the players inventory at mapchange if this mapchange is NOT a teleport from waitingarea to playgroud!! And don't remove him from "Playing" and "Ready" in this case!!!
																	//Jeppa : This may collide with other tools like MultiInv oder Multiverse Inv !!!???? but shouldn't !
				plugin.RestoreInv(p, pname); 						//Jeppa: This will also do a check thru all available arenas and remove the player from the lists as he is NOT playing...
			}
		}
	}
}
