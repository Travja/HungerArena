package me.Travja.HungerArena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SponsorCommands implements CommandExecutor {
	public Main plugin;
	public SponsorCommands(Main m) {
		this.plugin = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("Sponsor")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				String pname = p.getName();
				String epname = p.getDisplayName();
				if(p.hasPermission("HungerArena.Sponsor")){
					if(!plugin.Playing.contains(epname)){
						if(args.length== 0){
							p.sendMessage(ChatColor.RED + "You didn't specify a tribute!");
							return false;
						}
						if(args.length== 1){
							p.sendMessage(ChatColor.RED + "You didn't specify an item!");
						}
						if(args.length== 2){
							p.sendMessage(ChatColor.RED + "You didn't specify an amount!");
						}
						if(args.length== 3){
							Player target = Bukkit.getPlayer(args[0]);
							if(args[1].equalsIgnoreCase("57") || args[1].equalsIgnoreCase("7")){
								p.sendMessage(ChatColor.RED + "You can't sponsor that item!");
							}else{
								int ID = Integer.parseInt(args[1]);
								int Amount = Integer.parseInt(args[2]);
								ItemStack sponsoritem = new ItemStack(ID, Amount);
								if(p.getInventory().contains(plugin.config.getInt("Sponsor_Cost.ID"), plugin.config.getInt("Sponsor_Cost.Amount")*Amount)){
									if(!plugin.Playing.contains(target.getDisplayName())){
										p.sendMessage(ChatColor.RED + "That person isn't playing!");
									}else{
										if(args[0].equalsIgnoreCase(pname)){
											p.sendMessage(ChatColor.RED + "You can't sponsor yourself!");
										}else{
											target.sendMessage(ChatColor.AQUA + "You have been Sponsored!");
											target.getInventory().addItem(sponsoritem);
											p.sendMessage("You have sponsored " + target.getName() + "!");
											p.getInventory().removeItem(plugin.Cost);
										}
									}
								}else{
									p.sendMessage(ChatColor.RED + "You don't have the necessary items to sponsor!");
								}
							}
						}
					}else{
						p.sendMessage(ChatColor.RED + "You are playing, you can't sponsor yourself!");
					}
				}else{
					p.sendMessage(ChatColor.RED + "You don't have permission!");
				}
			}else if(sender instanceof ConsoleCommandSender){
				if(args.length== 0){
					sender.sendMessage(ChatColor.RED + "You didn't specify a tribute!");
					return false;
				}
				if(args.length== 1){
					sender.sendMessage(ChatColor.RED + "You didn't specify an item!");
				}
				if(args.length== 2){
					sender.sendMessage(ChatColor.RED + "You didn't specify an amount!");
				}
				if(args.length== 3){
					Player target = Bukkit.getPlayer(args[0]);
					if(args[1].equalsIgnoreCase("57") || args[1].equalsIgnoreCase("7")){
						sender.sendMessage(ChatColor.RED + "You can't sponsor that item!");
					}else{
						int ID = Integer.parseInt(args[1]);
						int Amount = Integer.parseInt(args[2]);
						ItemStack sponsoritem = new ItemStack(ID, Amount);
						if(!plugin.Playing.contains(target.getDisplayName())){
							sender.sendMessage(ChatColor.RED + "That person isn't playing!");
						}else{
							sender.sendMessage(ChatColor.RED + "You can't sponsor yourself!");
							target.sendMessage(ChatColor.AQUA + "You have been Sponsored!");
							target.getInventory().addItem(sponsoritem);
							sender.sendMessage("You have sponsored " + target.getName() + "!");
						}
					}
				}
			}
		}
		return false;
	}
}
