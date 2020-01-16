package me.Travja.HungerArena.util;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Utils {

    public static void useSign(Block b, Player p) {
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign) b.getState();
        String[] lines = sign.getLines();
        if(lines[0].trim().equalsIgnoreCase(ChatColor.BLUE + "[HungerArena]") || lines[0].trim().equalsIgnoreCase(ChatColor.BLUE + "[HA]")){
            if(!lines[1].equals("") && lines[2].equals(""))
                p.performCommand("ha " + lines[1]);
            else if(lines[1].equals("") && !lines[2].equals(""))
                p.performCommand("ha " + lines[2]);
            else if(!lines[1].equals("") && !lines[2].equals("")) {
                String commands = "close,join,kick,leave,list,open,ready,refill,reload,restart,rlist,tp,start,watch,warpall";
                if (commands.contains(lines[2].trim().toLowerCase())) {
                    p.performCommand("ha " + lines[1]);
                    p.performCommand("ha " + lines[2]);
                } else  p.performCommand("ha " + lines[1] + " " + lines[2]);
            }
            else
                p.performCommand("ha");
        }
        if(lines[0].trim().equalsIgnoreCase(ChatColor.BLUE + "[Sponsor]")){
            p.performCommand("sponsor " + lines[1] + " " + lines[2] + " " + lines[3]);
        }
    }

}
