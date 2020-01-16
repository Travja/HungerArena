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

	@EventHandler(priority = EventPriority.MONITOR)
	public void CatchCommand(PlayerCommandPreprocessEvent event){
		String cmd = event.getMessage();
		Player p = event.getPlayer();
		String pname = p.getName();
		for(int x : plugin.watching.keySet()){
			if(plugin.watching.get(x).contains(p.getName())){
				if (handleIngameCommands(p, cmd)==true) event.setCancelled(true);
			}
		}
		if(plugin.getArena(p)!= null){
			if (handleIngameCommands(p, cmd)==true) event.setCancelled(true);
		}else if(cmd.toLowerCase().trim().equals("/back")){
			for(int u : plugin.Dead.keySet()){
				if(plugin.Dead.get(u).contains(pname) && plugin.canjoin.get(u))
					plugin.tele.add(p);
			}
		}
		if(cmd.startsWith("/tp") || cmd.startsWith("/telep") || cmd.contains(":tp") || cmd.contains(":telep")){
			String[] args = cmd.split(" "); 
			Player player1 = null;
			Player player2 = null;
			if(args.length >= 2){
				if(Bukkit.getPlayer(args[1]) != null){
					player1 = Bukkit.getPlayer(args[1]);
					if (args.length >= 3 && Bukkit.getPlayer(args[2]) != null) player2 = Bukkit.getPlayer(args[2]);
					if(plugin.isSpectating(p)){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "Invalid command for spectating, using /ha tp " + (player2!=null?player2:player1));
						p.performCommand("/ha tp " + (player2!=null?player2:player1));
					}else if(plugin.getArena(player1)!=null || (args.length == 2 && plugin.getArena(p)!=null) || (args.length >= 3 && plugin.getArena(player2)!= null)){
						event.setCancelled(true);
						p.sendMessage(ChatColor.RED + "You can't teleport to other tributes!");
					}
				}
			}
		}
	}
	private boolean handleIngameCommands(Player p, String cmd){
		if(!p.hasPermission("HungerArena.UseCommands")){
			if(!plugin.management.getStringList("commands.whitelist").isEmpty()){
				for(String whitelist: plugin.management.getStringList("commands.whitelist")){
					if(cmd.toLowerCase().startsWith(whitelist.toLowerCase())){
						return false;
					}
				}
			}
			if(!cmd.toLowerCase().startsWith("/ha")){
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
				//p.sendMessage(ChatColor.AQUA + "/ha newarena");  //not during game...
				p.sendMessage(ChatColor.AQUA + "/ha setspawn");
				p.sendMessage(ChatColor.AQUA + "/ha start");
				p.sendMessage(ChatColor.AQUA + "/ha tp");
				p.sendMessage(ChatColor.AQUA + "/ha watch");
				p.sendMessage(ChatColor.AQUA + "/ha warpall");
				return true;
			}
		}else if(!cmd.toLowerCase().startsWith("/ha")){
			if(cmd.toLowerCase().startsWith("/spawn") || cmd.toLowerCase().startsWith("/back")){
				p.sendMessage("You have perms for all commands except this one!");
				return true;
			}
		}
		return false;
	}
}
