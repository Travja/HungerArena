package me.travja.hungerarena;

import org.bukkit.ChatColor;
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
				if(args[0].equalsIgnoreCase("1")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_one_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute one!");
				}
				if(args[0].equalsIgnoreCase("2")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_two_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute two!");
				}
				if(args[0].equalsIgnoreCase("3")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_three_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute three!");
				}
				if(args[0].equalsIgnoreCase("4")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_four_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute four!");
				}
				if(args[0].equalsIgnoreCase("5")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_five_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute five!");
				}
				if(args[0].equalsIgnoreCase("6")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_six_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute six!");
				}
				if(args[0].equalsIgnoreCase("7")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_seven_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute seven!");
				}
				if(args[0].equalsIgnoreCase("8")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_eight_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eight!");
				}
				if(args[0].equalsIgnoreCase("9")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_nine_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute nine!");
				}
				if(args[0].equalsIgnoreCase("10")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_ten_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute ten!");
				}
				if(args[0].equalsIgnoreCase("11")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_eleven_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eleven!");
				}
				if(args[0].equalsIgnoreCase("12")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_twelve_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twelve!");
				}
				if(args[0].equalsIgnoreCase("13")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_thirteen_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute thirteen!");
				}
				if(args[0].equalsIgnoreCase("14")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_fourteen_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute fourteen!");
				}
				if(args[0].equalsIgnoreCase("15")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_fifteen_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute fifteen!");
				}
				if(args[0].equalsIgnoreCase("16")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_sixteen_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute sixteen!");
				}
				if(args[0].equalsIgnoreCase("17")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_seventeen_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute seventeen!");
				}
				if(args[0].equalsIgnoreCase("18")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_eighteen_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute eighteen!");
				}
				if(args[0].equalsIgnoreCase("19")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_nineteen_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute nineteen!");
				}
				if(args[0].equalsIgnoreCase("20")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_twenty_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twenty!");
				}
				if(args[0].equalsIgnoreCase("21")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_twentyone_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentyone!");
				}
				if(args[0].equalsIgnoreCase("22")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_twentytwo_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentytwo!");
				}
				if(args[0].equalsIgnoreCase("23")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_twentythree_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentythree!");
				}
				if(args[0].equalsIgnoreCase("24")){
					double x = p.getLocation().getX();
					double y = p.getLocation().getY();
					double z = p.getLocation().getZ();
					String w = p.getWorld().getName();
					plugin.config.set("Tribute_twentyfour_spawn", x + "," + y + "," + z + "," + w);
					plugin.saveConfig();
					p.sendMessage(ChatColor.AQUA + "You have set the spawn location of Tribute twentyfour!");
				}
			}else{
				p.sendMessage(ChatColor.RED + "You don't have permission!");
			}
		}
		return false;
	}

}
