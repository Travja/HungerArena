package me.Travja.HungerArena;

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
	@EventHandler(priority = EventPriority.MONITOR)
	public void CatchCommand(PlayerCommandPreprocessEvent event){
		String cmd = event.getMessage();
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Playing.contains(pname) || plugin.Watching.contains(pname)){
			if(!p.hasPermission("HungerArena.UseCommands")){
				if(!plugin.management.getStringList("commands.whitelist").isEmpty() && !cmd.toLowerCase().contains("/ha")){
					for(String whitelist: plugin.management.getStringList("commands.whitelist")){
						if(!cmd.toLowerCase().contains(whitelist.toLowerCase()) && !cmd.toLowerCase().contains("/ha")){
							event.setCancelled(true);
							p.sendMessage(ChatColor.RED + "You are only allowed to perform the following commands:");
							p.sendMessage(ChatColor.AQUA + whitelist);
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
				}else if(!cmd.toLowerCase().contains("/ha")){
					event.setCancelled(true);
					p.sendMessage(ChatColor.RED + "You are only allowed to perform /ha commands!");
				}
			}else if(!cmd.toLowerCase().contains("/ha")){
				if(cmd.toLowerCase().contains("/spawn")){
					event.setCancelled(true);
					p.sendMessage("You have perms for all commands except this one!");
				}
			}
		}else if(cmd.toLowerCase().equals("/back") && plugin.Dead.contains(pname) && plugin.canjoin== true){
			plugin.Tele.add(p);
		}else if(!plugin.Playing.contains(pname) || !plugin.Watching.contains(pname)){
			if(cmd.contains("/tp") || cmd.contains("/tpa") || cmd.contains("/tpo")){
				String[] args = cmd.split(" ");
				if(args.length == 3){
					if(plugin.Playing.contains(plugin.getServer().getPlayer(args[1]).getName()) || plugin.Playing.contains(plugin.getServer().getPlayer(args[2]).getName())){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You can't teleport to tributes unless you've run /ha watch!");
					}
				}else if(args.length == 2){
					if(plugin.Playing.contains(plugin.getServer().getPlayer(args[1]).getName())){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You can't teleport to tributes unless you've run /ha watch!");
					}	
				}
			}
		}
	}
}
