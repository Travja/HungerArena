package me.Travja.HungerArena;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnsCommand implements CommandExecutor {
	public Main plugin;
	public SpawnsCommand(Main m) {
		this.plugin = m;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("StartPoint")){
			if(p.hasPermission("HungerArena.StartPoint")){
				if(!plugin.restricted || (plugin.restricted && plugin.worlds.contains(p.getWorld().getName()))){
					try{
						int i = Integer.parseInt(args[0]);
						if(i >= 1 && i <= 24){
							if(plugin.restricted && !plugin.worlds.contains(p.getWorld().getName())){
								p.sendMessage(ChatColor.GOLD + "We ran the command, however, this isn't a world you defined in the config...");
								p.sendMessage(ChatColor.GOLD + "If this is the right world, please disregard this message.");
							}
							Location ploc = p.getLocation().getBlock().getLocation();
							List<String> locations = plugin.spawns.getStringList("Spawns");
							double x = ploc.getX();
							double y = ploc.getY();
							double z = ploc.getZ();
							String w = ploc.getWorld().getName();
							String coords = x + "," + y + "," + z + "," + w + ",true";
							locations.set(i-1, coords);
							plugin.spawns.set("Spawns", locations);
							plugin.saveSpawns();
							plugin.location.add(new Location(ploc.getWorld(), x, y, z));
							p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute " + i);
						}else{
							p.sendMessage(ChatColor.RED + "You can't go past 24 or below 1!");
						}
					}catch(Exception e){
						p.sendMessage(ChatColor.RED + "Argument not an integer!");
					}
				}
			}else{
				p.sendMessage(ChatColor.RED + "You don't have permission!");
			}
						/*if(args[0].equalsIgnoreCase("1")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_one_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute one!");
					}
					if(args[0].equalsIgnoreCase("2")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_two_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute two!");
					}
					if(args[0].equalsIgnoreCase("3")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_three_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute three!");
					}
					if(args[0].equalsIgnoreCase("4")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_four_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute four!");
					}
					if(args[0].equalsIgnoreCase("5")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_five_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute five!");
					}
					if(args[0].equalsIgnoreCase("6")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_six_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute six!");
					}
					if(args[0].equalsIgnoreCase("7")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_seven_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute seven!");
					}
					if(args[0].equalsIgnoreCase("8")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_eight_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eight!");
					}
					if(args[0].equalsIgnoreCase("9")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_nine_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute nine!");
					}
					if(args[0].equalsIgnoreCase("10")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_ten_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute ten!");
					}
					if(args[0].equalsIgnoreCase("11")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_eleven_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eleven!");
					}
					if(args[0].equalsIgnoreCase("12")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_twelve_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twelve!");
					}
					if(args[0].equalsIgnoreCase("13")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_thirteen_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute thirteen!");
					}
					if(args[0].equalsIgnoreCase("14")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_fourteen_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute fourteen!");
					}
					if(args[0].equalsIgnoreCase("15")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_fifteen_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute fifteen!");
					}
					if(args[0].equalsIgnoreCase("16")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_sixteen_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute sixteen!");
					}
					if(args[0].equalsIgnoreCase("17")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_seventeen_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute seventeen!");
					}
					if(args[0].equalsIgnoreCase("18")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_eighteen_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eighteen!");
					}
					if(args[0].equalsIgnoreCase("19")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_nineteen_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute nineteen!");
					}
					if(args[0].equalsIgnoreCase("20")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_twenty_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twenty!");
					}
					if(args[0].equalsIgnoreCase("21")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_twentyone_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentyone!");
					}
					if(args[0].equalsIgnoreCase("22")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_twentytwo_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentytwo!");
					}
					if(args[0].equalsIgnoreCase("23")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_twentythree_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentythree!");
					}
					if(args[0].equalsIgnoreCase("24")){
						double x = ploc.getX();
						double y = ploc.getY();
						double z = ploc.getZ();
						String w = ploc.getWorld().getName();
						plugin.spawns.set("Tribute_twentyfour_spawn", x + "," + y + "," + z + "," + w);
						plugin.saveSpawns();
						p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentyfour!");
					}*/
		}
		return false;
	}

}
