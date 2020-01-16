package me.Travja.HungerArena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
				if(p.hasPermission("HungerArena.Sponsor")){
					if(plugin.getArena(p)== null){
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
						if(args.length>= 3){
							Player target = Bukkit.getServer().getPlayer(args[0]);
							if(target==null || (target!=null && plugin.getArena(target)== null)){
								p.sendMessage(ChatColor.RED + "That person isn't playing!");
							}else{
								try{
									String ID = args[1].toUpperCase().trim(); 
									int amount = Integer.parseInt(args[2]);
									if((!plugin.management.getStringList("sponsors.blacklist").isEmpty() && !plugin.management.getStringList("sponsors.blacklist").contains(ID)) || plugin.management.getStringList("sponsors.blacklist").isEmpty()){
										handleItemsAndEco(p,args,ID,amount,target);
									}else{
										p.sendMessage(ChatColor.RED + "You can't sponsor that item!");
										p.sendMessage(ChatColor.GREEN + "Other items you can't sponsor are:");
										for(String blacklist: plugin.management.getStringList("sponsors.blacklist")){
											p.sendMessage(ChatColor.AQUA + blacklist);
										}
									}
								}catch(Exception e){
									p.sendMessage(ChatColor.RED + "Something went wrong there... Make sure that you do like this /sponsor [name] [item] [number]");
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
				if(args.length>= 3){
					Player target = Bukkit.getPlayer(args[0]);
					String ID = args[1].toUpperCase().trim(); 
					int Amount = Integer.parseInt(args[2]);
					try{
						if((!plugin.management.getStringList("sponsors.blacklist").isEmpty() && !plugin.management.getStringList("sponsors.blacklist").contains(ID)) || plugin.management.getStringList("sponsors.blacklist").isEmpty()){
							ItemStack sponsoritem = new ItemStack(org.bukkit.Material.getMaterial(ID), Amount);
							if(plugin.getArena(target)== null){
								sender.sendMessage(ChatColor.RED + "That person isn't playing!");
							}else{
								sender.sendMessage(ChatColor.RED + "You can't sponsor yourself!");
								target.sendMessage(ChatColor.AQUA + "You have been Sponsored!");
								target.getInventory().addItem(sponsoritem);
								sender.sendMessage("You have sponsored " + target.getName() + "!");
							}
						}else{
							sender.sendMessage(ChatColor.RED + "You can't sponsor that item!");
							sender.sendMessage(ChatColor.GREEN + "Other items you can't sponsor are:");
							for(String blacklist: plugin.management.getStringList("sponsors.blacklist")){
								sender.sendMessage(ChatColor.AQUA + blacklist);
							}
						}
					}catch(Exception e){
						sender.sendMessage(ChatColor.RED + "Something went wrong there... Make sure that you do like this /sponsor [name] [number] [number]");
					}
				}
			}
		}
		return false;
	}
	private void handleItemsAndEco(Player p,String[] args,String ID,int amount,Player target){
		Material sponsMat = plugin.getNewMaterial(ID,0);
		ItemStack sponsoritem;
		boolean done = false;
		if (sponsMat !=null) {
			sponsoritem = new ItemStack(sponsMat,amount);
		} else {
			p.sendMessage(ChatColor.RED + "That item does not exist !!");
			return;
		}
		for(ItemStack Costs: plugin.cost){
			if(!p.getInventory().containsAtLeast(Costs, Costs.getAmount()*amount)){
				p.sendMessage(ChatColor.RED + "You don't have the necessary items to sponsor!");
				return;
			}
		}
		if(args[0].equalsIgnoreCase(p.getName())){
			p.sendMessage(ChatColor.RED + "You can't sponsor yourself!");
		}else{
			if(plugin.config.getBoolean("sponsorEco.enabled")) {
				if(!(plugin.econ.getBalance(p) < (plugin.config.getDouble("sponsorEco.cost")*amount))){
					plugin.econ.withdrawPlayer(p, plugin.config.getDouble("sponsorEco.cost")*amount);
					done = true;
				} else {
					p.sendMessage(ChatColor.RED + "You don't have enough money to do that!");
					return;
				}
			}
			if(!plugin.cost.isEmpty()){
				for(ItemStack aCosts: plugin.cost){
					for (int x=1;x<=amount;x++){
						p.getInventory().removeItem(aCosts);
						done = true;
					}
				}
			}
			if (done){
				target.getInventory().addItem(sponsoritem);
				target.sendMessage(ChatColor.AQUA + "You have been Sponsored!");
				p.sendMessage("You have sponsored " + target.getName() + "!");
			} else p.sendMessage(ChatColor.RED + "Sponsoring is disabled!");
		}
	}
}
