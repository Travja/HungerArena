package me.Travja.HungerArena;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandBlock implements Listener {
	public Main plugin;
	public CommandBlock(Main m) {
		this.plugin = m;
	}
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void CatchCommand(PlayerCommandPreprocessEvent event){
		String cmd = event.getMessage();
		Player p = event.getPlayer();
		String pname = p.getName();
		int i = 0;
		int x = 0;
		boolean found = false;
		for(x = 1; x < plugin.Watching.size(); x++){
			if(plugin.Watching.get(x).contains(p.getName())){
				if(!p.hasPermission("HungerArena.UseCommands")){
					if(!plugin.management.getStringList("commands.whitelist").isEmpty()){
						for(String whitelist: plugin.management.getStringList("commands.whitelist")){
							if(cmd.toLowerCase().startsWith(whitelist.toLowerCase())){	// Jeppa: check for ALL whitelist commands
								found = true;
								i = plugin.management.getStringList("commands.whitelist").size()-1;
							}
							i = i+1;
							if(i== plugin.management.getStringList("commands.whitelist").size()){
								if(!found && !cmd.toLowerCase().startsWith("/ha")){	//Jeppa: must be && ;)
									event.setCancelled(true);
									p.sendMessage(ChatColor.RED + "You are only allowed to perform the following commands:");
									for(String whitelistfull: plugin.management.getStringList("commands.whitelist")){
										p.sendMessage(ChatColor.AQUA + whitelistfull);
									}
									p.sendMessage(ChatColor.AQUA + "/ha");
									p.sendMessage(ChatColor.AQUA + "/ha close");
									p.sendMessage(ChatColor.AQUA + "/ha help");
									p.sendMessage(ChatColor.AQUA + "/ha join");
									p.sendMessage(ChatColor.AQUA + "/ha kick [Player]");
									p.sendMessage(ChatColor.AQUA + "/ha leave");
									p.sendMessage(ChatColor.AQUA + "/ha list");
									p.sendMessage(ChatColor.AQUA + "/ha open");
									p.sendMessage(ChatColor.AQUA + "/ha ready");
									p.sendMessage(ChatColor.AQUA + "/ha refill");
									p.sendMessage(ChatColor.AQUA + "/ha reload");
									p.sendMessage(ChatColor.AQUA + "/ha restart");
									p.sendMessage(ChatColor.AQUA + "/ha rlist");
									p.sendMessage(ChatColor.AQUA + "/ha setspawn");
									p.sendMessage(ChatColor.AQUA + "/ha start");
									p.sendMessage(ChatColor.AQUA + "/ha tp");
									p.sendMessage(ChatColor.AQUA + "/ha watch");
									p.sendMessage(ChatColor.AQUA + "/ha warpall");
								}
							}
						}
					}else if(!cmd.toLowerCase().startsWith("/ha")){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You are only allowed to perform /ha commands!");
					}
				}else if(!cmd.toLowerCase().startsWith("/ha")){
					if(cmd.toLowerCase().startsWith("/spawn")){
						event.setCancelled(true);
						p.sendMessage("You have perms for all commands except this one!");
					}
				}	
			}
		}
		if(plugin.getArena(p)!= null){
			if(!p.hasPermission("HungerArena.UseCommands")){
				if(!plugin.management.getStringList("commands.whitelist").isEmpty()){
					for(String whitelist: plugin.management.getStringList("commands.whitelist")){
						if(cmd.toLowerCase().startsWith(whitelist.toLowerCase())){
							found = true;
							i = plugin.management.getStringList("commands.whitelist").size()-1;
						}
						i = i+1;
						if(i== plugin.management.getStringList("commands.whitelist").size()){	// Abbruch bei max. Anzahl
							if(!found && !cmd.toLowerCase().startsWith("/ha")){		// with the two invertet forms it must be && ;)
								event.setCancelled(true);
								p.sendMessage(ChatColor.RED + "You are only allowed to perform the following commands:");
								for(String whitelistfull: plugin.management.getStringList("commands.whitelist")){
									p.sendMessage(ChatColor.AQUA + whitelistfull);
								}
								p.sendMessage(ChatColor.AQUA + "/ha");
								p.sendMessage(ChatColor.AQUA + "/ha close");
								p.sendMessage(ChatColor.AQUA + "/ha help");
								p.sendMessage(ChatColor.AQUA + "/ha join");
								p.sendMessage(ChatColor.AQUA + "/ha kick [Player]");
								p.sendMessage(ChatColor.AQUA + "/ha leave");
								p.sendMessage(ChatColor.AQUA + "/ha list");
								p.sendMessage(ChatColor.AQUA + "/ha open");
								p.sendMessage(ChatColor.AQUA + "/ha ready");
								p.sendMessage(ChatColor.AQUA + "/ha refill");
								p.sendMessage(ChatColor.AQUA + "/ha reload");
								p.sendMessage(ChatColor.AQUA + "/ha restart");
								p.sendMessage(ChatColor.AQUA + "/ha rlist");
								p.sendMessage(ChatColor.AQUA + "/ha setspawn");
								p.sendMessage(ChatColor.AQUA + "/ha start");
								p.sendMessage(ChatColor.AQUA + "/ha tp");
								p.sendMessage(ChatColor.AQUA + "/ha watch");
								p.sendMessage(ChatColor.AQUA + "/ha warpall");
							}
						}
					}
				}else if(!cmd.toLowerCase().startsWith("/ha")){
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You are only allowed to perform /ha commands!");
				}
			}else if(!cmd.toLowerCase().startsWith("/ha")){
				if(cmd.toLowerCase().startsWith("/spawn")){
					event.setCancelled(true);
					p.sendMessage("You have perms for all commands except this one!");
				}
			}
//commands while not playing...
		}else if(cmd.toLowerCase().equals("/back")){
			for(i = 1; i < plugin.Dead.size(); i++){
				if(plugin.Dead.get(i).contains(pname) && plugin.canjoin.get(i))
					plugin.Tele.add(p);
			}
		}else if(cmd.startsWith("/tp") || cmd.startsWith("/tpa") || cmd.startsWith("/tpo")){
			String[] args = cmd.split(" ");
			Player arg1 = null;
			Player arg2 = null;
			if(args.length == 2){
				if(Bukkit.getPlayer(args[0]) != null && Bukkit.getPlayer(args[1]) != null){
					arg1 = Bukkit.getPlayer(args[0]);
					arg2 = Bukkit.getPlayer(args[1]);
					if(plugin.isSpectating(p)){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "Invalid command for spectating, using /ha tp " + arg2);
						p.performCommand("/ha tp " + arg2);
					}else if(plugin.getArena(arg1)!= null || plugin.getArena(arg2)!= null){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You can't teleport to other tributes!");
					}
				}
			}else if(args.length == 1){
				if(Bukkit.getPlayer(args[0]) != null){
					arg1 = Bukkit.getPlayer(args[0]);					
					if(plugin.isSpectating(p)){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "Invalid command for spectating, using /ha tp " + arg1);
						p.performCommand("/ha tp " + arg1);
					}else if(plugin.getArena(arg1)!= null || plugin.getArena(p)!= null){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You can't teleport to other tributes!");
					}
				}
			}
		}
	}
}
