package me.Travja.HungerArena;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Method;
import java.util.*;

public class HaCommands implements CommandExecutor {
    public Main plugin;

    public HaCommands(Main m) {
        this.plugin = m;
    }

    int i = 0;
    int a = 1;
    boolean NoPlayerSpawns = true;

    private void clearInv(Player p) {
        p.getInventory().clear();
        p.getInventory().setBoots(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setHelmet(null);
        p.getInventory().setLeggings(null);
        p.updateInventory();
    }

    private Location getArenaSpawn() {
        String[] Spawncoords;
        if (plugin.spawns.getString("Spawn_coords." + a) != null) {
            Spawncoords = plugin.spawns.getString("Spawn_coords." + a).split(",");
        } else {
            Spawncoords = plugin.spawns.getString("Spawn_coords.0").split(",");
        }
        double spawnx = Double.parseDouble(Spawncoords[0]);
        double spawny = Double.parseDouble(Spawncoords[1]);
        double spawnz = Double.parseDouble(Spawncoords[2]);
        String spawnworld = Spawncoords[3];
        World spawnw = plugin.getServer().getWorld(spawnworld);
        Location Spawn = new Location(spawnw, spawnx, spawny, spawnz);
        return Spawn;
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String commandLabel, String[] args) {

        boolean console = false;
        boolean playr = false;
        if (sender instanceof ConsoleCommandSender) console = true;
        else if (sender instanceof Player) playr = true;

        if (playr) {
            final Player p = (Player) sender;
            final String pname = p.getName();
            String ThisWorld = p.getWorld().getName();
            for (int i : plugin.worldsNames.keySet()) {
                if (plugin.worldsNames.get(i) != null) {
                    if (plugin.worldsNames.get(i).equals(ThisWorld)) {
                        a = i;
                        NoPlayerSpawns = false;
                    }
                }
            }

            if (plugin.getArena(p) != null) {
                a = plugin.getArena(p);
                NoPlayerSpawns = false;
            }
            if (cmd.getName().equalsIgnoreCase("Ha")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.GREEN + "[HungerArena] by " + ChatColor.AQUA + "travja! Version: " + plugin.getDescription().getVersion());
                    sender.sendMessage(ChatColor.GREEN + "[HungerArena] Update by " + ChatColor.AQUA + "Jeppa ! ");
                    return false;
                } else if (args[0].equalsIgnoreCase("NewArena")) {
                    if (p.hasPermission("HungerArena.StartPoint")) {
                        if (args.length < 2) {
                            p.sendMessage(ChatColor.AQUA + "You have to enter the arena number as 2nd argument");
                        } else {
                            try {
                                a = Integer.parseInt(args[1]);
                            } catch (Exception e) {
                                p.sendMessage(ChatColor.RED + "Argument not an integer!");
                                return false;
                            }
                            if (plugin.worldsNames.values().contains(ThisWorld)) {
                                p.sendMessage(ChatColor.RED + "This world already has an areana! \n (only one arena per world possible, yet!)");
                                return false;
                            }
                            ((Player) sender).performCommand("startpoint " + a);
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission!");
                    }
                } else if (args[0].equalsIgnoreCase("SetSpawn")) {
                    if (p.hasPermission("HungerArena.SetSpawn")) {
                        if ((NoPlayerSpawns) && (args.length < 2)) {
                            p.sendMessage(ChatColor.AQUA + "You have to set the playerspawns first! Use /ha newarena \nOr enter the arena number as 2nd argument, \n0 is default/fallback!");
                        } else {
                            double x = p.getLocation().getX();
                            double y = p.getLocation().getY();
                            double z = p.getLocation().getZ();
                            if (plugin.spawns.getString("Spawn_coords.0") == null) {
                                plugin.spawns.set("Spawn_coords.0", x + "," + y + "," + z + "," + ThisWorld);
                                plugin.spawns.set("Spawns_set.0", "true");
                            }
                            if (args.length >= 2) {
                                try {
                                    a = Integer.parseInt(args[1]);
                                } catch (Exception e) {
                                    p.sendMessage(ChatColor.RED + "Argument not an integer!");
                                    return false;
                                }
                            }
                            plugin.spawns.set("Spawn_coords." + a, x + "," + y + "," + z + "," + ThisWorld);
                            plugin.spawns.set("Spawns_set." + a, "true");
                            plugin.saveSpawns();
                            p.sendMessage(ChatColor.AQUA + "You have set the spawn for dead tributes!");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission!");
                    }
                } else if (args[0].equalsIgnoreCase("SetTorch")) {
                    if (p.hasPermission("HungerArena.SetSpawn")) {
                        if (args.length < 2) {
                            p.sendMessage(ChatColor.AQUA + "You have to enter the arena number as 2nd argument");
                        } else {
                            double x = p.getLocation().getX();
                            double y = p.getLocation().getY();
                            double z = p.getLocation().getZ();
                            if (args.length >= 2) {
                                try {
                                    a = Integer.parseInt(args[1]);
                                } catch (Exception e) {
                                    p.sendMessage(ChatColor.RED + "Argument not an integer!");
                                    return true;
                                }
                            }
                            plugin.spawns.set("Start_torch." + a, x + "," + y + "," + z + "," + ThisWorld);
                            plugin.saveSpawns();
                            p.sendMessage(ChatColor.AQUA + "You have set the start redstone torch for arena " + a + "!");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "You don't have permission!");
                    }

                } else if (args[0].equalsIgnoreCase("Help")) {
                    help_sub(sender, playr, console);

                } else if (plugin.restricted && !plugin.worldsNames.values().contains(ThisWorld)) {
                    p.sendMessage(ChatColor.RED + "That can't be run in this world!");
                } else if (!plugin.restricted || plugin.restricted && plugin.worldsNames.values().contains(ThisWorld)) {
                    //////////////////////////////////////// LISTING ///////////////////////////////////////////////
                    if (args[0].equalsIgnoreCase("List")) {
                        if (p.hasPermission("HungerArena.GameMaker") || plugin.watching.get(a).contains(pname) || p.hasPermission("HungerArena.List")) {
                            list_sub(p, sender, playr, console, args);

                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("rList")) {
                        if (p.hasPermission("HungerArena.GameMaker")) {
                            rList_sub(sender, args);

                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission!");
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        ///////////////////////////////////// JOINING/LEAVING //////////////////////////////////////////
                    } else if (args[0].equalsIgnoreCase("Join")) {
                        if (p.hasPermission("HungerArena.Join")) {
                            boolean needconfirm = false;
                            for (int i : plugin.needConfirm.keySet()) {
                                if (plugin.needConfirm.get(i).contains(pname)) {
                                    needconfirm = true;
                                    p.sendMessage(ChatColor.GOLD + "You need to run /ha confirm");
                                }
                            }
                            if (!needconfirm) {
                                if ((args.length >= 2) && checkarena(args[1], sender)) {
                                    a = Integer.parseInt(args[1]);
                                } else {
                                    if (NoPlayerSpawns) {
                                        for (int i : plugin.Playing.keySet()) {
                                            if (plugin.Playing.get(i).size() < plugin.maxPlayers.get(i)) {
                                                a = i;
                                                p.sendMessage(ChatColor.RED + "Found free slots in Arena " + a + " !");
                                            } else if (i == plugin.Playing.size()) {
                                                p.sendMessage(ChatColor.RED + "No free slots found / All games are full!");
                                            }
                                        }
                                    }

                                }
                                if ((plugin.Playing.get(a) != null) && (plugin.location.get(a).size() != 0)) {
                                    ;
                                    if (plugin.Playing.get(a).contains(pname))
                                        p.sendMessage(ChatColor.RED + "You are already playing!");
                                    else if (plugin.Dead.get(a).contains(pname) || plugin.quit.get(a).contains(pname))
                                        p.sendMessage(ChatColor.RED + "You DIED/QUIT! You can't join again!");
                                    else if (plugin.Playing.get(a).size() == plugin.maxPlayers.get(a))
                                        p.sendMessage(ChatColor.RED + "There are already " + plugin.maxPlayers.get(a) + " Tributes in that Arena!");
                                    else if (plugin.canjoin.get(a) == true)
                                        p.sendMessage(ChatColor.RED + "That game is in progress!");
                                    else if (!plugin.open.get(a))
                                        p.sendMessage(ChatColor.RED + "That game is closed!");
                                    else if ((plugin.spawns.getString("Spawns_set." + a) == null) || (plugin.spawns.getString("Spawns_set." + a).equalsIgnoreCase("false")))
                                        p.sendMessage(ChatColor.RED + "/ha setspawn for Arena " + a + " hasn't been run!");
                                    else if (plugin.getArena(p) != null)
                                        p.sendMessage(ChatColor.RED + "You are already in an arena!");
                                    else if (plugin.config.getString("Need_Confirm").equalsIgnoreCase("true")) {
                                        if (plugin.config.getBoolean("EntryFee.enabled") && plugin.config.getBoolean("EntryFee.eco")) {
                                            if (!(plugin.econ.getBalance(p) < plugin.config.getDouble("EntryFee.cost"))) {
                                                i = 0;
                                                for (ItemStack fee : plugin.fee) {
                                                    int total = plugin.fee.size();
                                                    if (p.getInventory().containsAtLeast(fee, fee.getAmount())) {
                                                        i = i + 1;
                                                        if (total == i) {
                                                            plugin.needConfirm.get(a).add(pname);
                                                            p.sendMessage(ChatColor.GOLD + "Your inventory will be cleared! Type /ha confirm to procede");
                                                        }
                                                    }
                                                }
                                                if (plugin.fee.size() > i) {
                                                    p.sendMessage(ChatColor.RED + "You are missing some items and can't join the games...");
                                                }
                                            } else {
                                                p.sendMessage(ChatColor.RED + "You don't have enough money to join!");
                                            }
                                        } else if (plugin.config.getBoolean("EntryFee.enabled") && !plugin.config.getBoolean("EntryFee.eco")) {
                                            i = 0;
                                            for (ItemStack fee : plugin.fee) {
                                                int total = plugin.fee.size();
                                                if (p.getInventory().containsAtLeast(fee, fee.getAmount())) {
                                                    i = i + 1;
                                                    if (total == i) {
                                                        plugin.needConfirm.get(a).add(pname);
                                                        p.sendMessage(ChatColor.GOLD + "Your inventory will be cleared! Type /ha confirm to procede");
                                                    }
                                                }
                                            }
                                            if (plugin.fee.size() > i) {
                                                p.sendMessage(ChatColor.RED + "You are missing some items and can't join the games...");
                                            }
                                        } else if (!plugin.config.getBoolean("EntryFee.enabled") && plugin.config.getBoolean("EntryFee.eco")) {
                                            if (!(plugin.econ.getBalance(p) < plugin.config.getDouble("EntryFee.cost"))) {
                                                plugin.needConfirm.get(a).add(pname);
                                                p.sendMessage(ChatColor.GOLD + "Your inventory will be cleared! Type /ha confirm to procede");
                                            } else {
                                                p.sendMessage(ChatColor.RED + "You don't have enough money to join!");
                                            }
                                        } else {
                                            plugin.needConfirm.get(a).add(pname);
                                            p.sendMessage(ChatColor.GOLD + "Your inventory will be cleared! Type /ha confirm to procede");
                                        }
                                    } else if (plugin.config.getString("Need_Confirm").equalsIgnoreCase("false")) {
                                        confirmSub(p, pname, ThisWorld);
                                    }

                                } else {
                                    p.sendMessage(ChatColor.RED + "That arena doesn't exist!");
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission!");
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////
                        ////////////////////////////////// CONFIRMATION ///////////////////////////////////////////////
                    } else if (args[0].equalsIgnoreCase("Confirm")) {
                        for (int v : plugin.needConfirm.keySet()) {
                            if (plugin.needConfirm.get(v).contains(pname)) {
                                a = v;
                                confirmSub(p, pname, ThisWorld);
                            } else if ((v == plugin.needConfirm.size()) && (plugin.config.getString("Need_Confirm").equalsIgnoreCase("true"))) {
                                p.sendMessage(ChatColor.RED + "You haven't joined any games!");
                            }
                        }
                    } else if (args[0].equalsIgnoreCase("Ready")) {
                        if (plugin.getArena(p) != null) {
                            a = plugin.getArena(p);
                            if (plugin.Playing.get(a).contains(pname)) {
                                if (plugin.Ready.get(a).contains(pname)) {
                                    p.sendMessage(ChatColor.RED + "You're already ready!");
                                } else if (plugin.Playing.get(a).size() == 1) {
                                    p.sendMessage(ChatColor.RED + "You can't be ready when no one else is playing!");
                                } else {
                                    plugin.Ready.get(a).add(pname);
                                    if (plugin.config.getBoolean("broadcastAll")) {
                                        plugin.getServer().broadcastMessage(ChatColor.AQUA + "[HungerArena] Game " + a + ": " + ChatColor.GRAY + String.valueOf(plugin.Ready.get(a).size()) + "/" + plugin.maxPlayers.get(a) + " Players ready!");
                                    } else {
                                        for (String gn : plugin.Playing.get(a)) {
                                            Player g = plugin.getServer().getPlayer(gn);
                                            g.sendMessage(ChatColor.GRAY + String.valueOf(plugin.Ready.get(a).size()) + "/" + plugin.maxPlayers.get(a) + " Players ready!");
                                        }
                                    }
                                    p.sendMessage(ChatColor.AQUA + "You have marked yourself as READY!");
                                    if (plugin.config.getBoolean("Auto_Warp")) {
                                        if (((double) plugin.Playing.get(a).size()) % 60 <= plugin.Ready.get(a).size() || plugin.Playing.get(a).size() == plugin.Ready.get(a).size()) {
                                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha warpall " + a);
                                        }
                                    }
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You aren't playing in any games!");
                        }
                    } else if (args[0].equalsIgnoreCase("Leave")) {
                        if (plugin.getArena(p) != null) {
                            a = plugin.getArena(p);
                            if (plugin.canjoin.get(a) == true) {
                                clearInv(p);
                                p.teleport(getArenaSpawn());
                                if (plugin.frozen.get(a).contains(pname)) {
                                    plugin.frozen.get(a).remove(pname);
                                }

                                if (plugin.Playing.get(a).size() != 1) plugin.RestoreInv(p, pname);

                                plugin.winner(a);
                            } else {
                                clearInv(p);
                                p.teleport(getArenaSpawn());

                                plugin.RestoreInv(p, pname);

                                if (plugin.config.getBoolean("EntryFee.enabled") && plugin.config.getBoolean("EntryFee.eco")) {
                                    plugin.econ.depositPlayer(p, plugin.config.getDouble("EntryFee.cost"));
                                    p.sendMessage(ChatColor.GOLD + "[HungerArena] " + ChatColor.GREEN + "$" + plugin.config.getDouble("EntryFee.cost") + " has been added to your account!");
                                    for (ItemStack fees : plugin.fee) {
                                        p.getInventory().addItem(fees);
                                        p.sendMessage(ChatColor.GOLD + "[HungerArena] " + ChatColor.GREEN + fees.getType().toString().toLowerCase().replace("_", " ") + " was refunded because you left the games.");
                                    }
                                } else if (plugin.config.getBoolean("EntryFee.enabled") && !plugin.config.getBoolean("EntryFee.eco")) {
                                    for (ItemStack fees : plugin.fee) {
                                        p.getInventory().addItem(fees);
                                        p.sendMessage(ChatColor.GOLD + "[HungerArena] " + ChatColor.GREEN + fees.getType().toString().toLowerCase().replace("_", " ") + " was refunded because you left the games.");
                                    }
                                } else if (!plugin.config.getBoolean("EntryFee.enabled") && plugin.config.getBoolean("EntryFee.eco")) {
                                    plugin.econ.depositPlayer(p, plugin.config.getDouble("EntryFee.cost"));
                                    p.sendMessage(ChatColor.GOLD + "[HungerArena] " + ChatColor.GREEN + "$" + plugin.config.getDouble("EntryFee.cost") + " has added to your account!");
                                }
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You aren't in any games!");
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        //////////////////////////////// SPECTATOR RELATED //////////////////////////////////
                    } else if (args[0].equalsIgnoreCase("Watch")) {
                        if (sender.hasPermission("HungerArena.Watch")) {
                            if (args.length >= 2) {
                                if (checkarena(args[1], sender)) {
                                    a = Integer.parseInt(args[1]);
                                    if (!plugin.watching.get(a).contains(pname) && plugin.getArena(p) == null && plugin.canjoin.get(a) == true) {
                                        plugin.watching.get(a).add(pname);
                                        p.teleport(Bukkit.getPlayer(plugin.Playing.get(a).get(0)));
                                        for (Player online : plugin.getServer().getOnlinePlayers()) {
                                            online.hidePlayer(plugin, p);
                                        }
                                        p.setAllowFlight(true);
                                        p.sendMessage(ChatColor.AQUA + "You can now spectate!");
                                        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
                                        Objective sobj;
                                        try {
                                            sobj = scoreboard.registerNewObjective("HA", "HAData", ChatColor.GREEN + "HA - Starting");
                                        } catch (NoSuchMethodError e) {
                                            sobj = scoreboard.registerNewObjective("HA", "HAData");
                                            sobj.setDisplayName(ChatColor.GREEN + "HA - Starting");
                                        }
                                        Score sdeaths = sobj.getScore((ChatColor.RED + "Spectators"));
                                        sdeaths.setScore(0);
                                        Score splayers = sobj.getScore((ChatColor.RED + "Players"));
                                        splayers.setScore(0);
                                        sobj.setDisplaySlot(DisplaySlot.SIDEBAR);
                                        p.setScoreboard(scoreboard);
                                        plugin.scoreboards.put(p.getName(), p.getScoreboard());
                                    } else if (plugin.canjoin.get(a) == false) {
                                        p.sendMessage(ChatColor.RED + "That game isn't in progress!");
                                    } else if (plugin.Playing.get(a).contains(pname)) {
                                        p.sendMessage(ChatColor.RED + "You can't watch while you're playing!");
                                    } else if (plugin.watching.get(a).contains(pname)) {
                                        plugin.watching.get(a).remove(pname);
                                        for (Player online : plugin.getServer().getOnlinePlayers()) {
                                            online.showPlayer(plugin, p);
                                        }
                                        p.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                                        plugin.scoreboards.remove(p.getName());
                                        p.teleport(getArenaSpawn());
                                        p.setAllowFlight(false);
                                        p.sendMessage(ChatColor.AQUA + "You are not spectating any more");
                                    }
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "Too few arguments!");
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission!");
                        }
                    } else if (args[0].equalsIgnoreCase("tp")) {
                        for (int i : plugin.watching.keySet()) {
                            if (plugin.watching.get(i).contains(pname)) {
                                if (plugin.getArena(Bukkit.getServer().getPlayer(args[1])) != null) {
                                    Player target = Bukkit.getServer().getPlayer(args[1]);
                                    p.teleport(target);
                                    p.sendMessage(ChatColor.AQUA + "You've been teleported to " + target.getName());
                                    return true;
                                } else {
                                    p.sendMessage(ChatColor.RED + "That person isn't in game!");
                                    return true;
                                }
                            } else {
                                if (i == plugin.watching.size()) {
                                    p.sendMessage(ChatColor.RED + "You have to be spectating first!");
                                    return true;
                                }
                            }
                        }
                        /////////////////////////////////////////////////////////////////////////////////
                    } else if (args[0].equalsIgnoreCase("addArena")) {
                        if (plugin.hookWE() != null) {
                            if (args.length != 2)
								return false;
                            if (p.hasPermission("HungerArena.AddArena")) {
                                WorldEditPlugin worldedit = plugin.hookWE();
                                World world = p.getWorld();
                                try {
                                    Region sel = worldedit.getSession(p).getSelection(worldedit.wrapPlayer(p).getWorld());
                                    if (sel == null)
                                        p.sendMessage(ChatColor.DARK_RED + "You must make a WorldEdit selection first!");
                                    else {
                                        BlockVector3 min = sel.getMinimumPoint();
                                        BlockVector3 max = sel.getMaximumPoint();
                                        plugin.spawns.set("Arenas." + args[1] + ".Max", world.getName() + "," + max.getX() + ","
                                                + max.getY() + "," + max.getZ());
                                        plugin.spawns.set("Arenas." + args[1] + ".Min", world.getName() + "," + min.getX() + ","
                                                + min.getY() + "," + min.getZ());
                                        plugin.saveConfig();
                                        p.sendMessage(ChatColor.GREEN + "Arena " + ChatColor.DARK_AQUA + args[1]
                                                + ChatColor.GREEN + " created with WorldEdit!");
                                        return true;
                                    }
                                } catch (IncompleteRegionException e) {
                                    p.sendMessage(ChatColor.RED + "Your selection is invalid.");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "You don't have permission!");
                                return true;
                            }
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have WorldEdit enabled for HungerArena!");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("Kick")) {
                        return kick_sub(sender, args, playr, console, getArenaSpawn());

                    } else if (args[0].equalsIgnoreCase("Refill")) {
                        if (p.hasPermission("HungerArena.Refill")) {
                            ReFill_sub(sender, args);
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission!");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("Restart")) {
                        if (p.hasPermission("HungerArena.Restart")) {
                            Restart_Close_sub(sender, args, playr, false);
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission!");
                        }
                        /////////////////////////////////// Toggle //////////////////////////////////////////////////
                    } else if (args[0].equalsIgnoreCase("close")) {
                        if (p.hasPermission("HungerArena.toggle")) {
                            Restart_Close_sub(sender, args, playr, true);
                        } else {
                            p.sendMessage(ChatColor.RED + "No Perms!");
                        }
                    } else if (args[0].equalsIgnoreCase("open")) {
                        if (p.hasPermission("HungerArena.toggle")) {
                            open_sub(sender, args);
                        } else {
                            p.sendMessage(ChatColor.RED + "No Perms!");
                        }
                        ////////////////////////////////////////////////////////////////////////////////////////////
                    } else if (args[0].equalsIgnoreCase("Reload")) {
                        if (p.hasPermission("HungerArena.Reload")) {
                            reload_sub(sender);
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission");
                        }
                    } else if (args[0].equalsIgnoreCase("WarpAll")) {
                        if (p.hasPermission("HungerArena.Warpall")) {
                            warpall_sub(sender, args);
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission");
                        }
                    } else if (args[0].equalsIgnoreCase("Start")) {
                        if (p.hasPermission("HungerArena.Start")) {
                            start_sub(sender, args);
                        } else {
                            p.sendMessage(ChatColor.RED + "You don't have permission!");
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Unknown command, type /ha help for a list of commands");
                    }
                }
            }

// Console ///////////////////////////////////
        } else if (sender instanceof ConsoleCommandSender) {
            if (cmd.getName().equalsIgnoreCase("Ha")) {
                if (args.length == 0) {
                    sender.sendMessage(ChatColor.GREEN + "[HungerArena] by " + ChatColor.AQUA + "travja! Version: " + plugin.getDescription().getVersion());
                    sender.sendMessage(ChatColor.GREEN + "[HungerArena] Update by " + ChatColor.AQUA + "Jeppa ! ");
                    return false;
                }
                if (args[0].equalsIgnoreCase("Help")) {
                    help_sub(sender, playr, console);
                    return false;
                } else if (args[0].equalsIgnoreCase("List")) {
                    list_sub(null, sender, playr, console, args);

                } else if (args[0].equalsIgnoreCase("rList")) {
                    rList_sub(sender, args);

                } else if (args[0].equalsIgnoreCase("SetSpawn") || args[0].equalsIgnoreCase("Join") || args[0].equalsIgnoreCase("Confirm") || args[0].equalsIgnoreCase("Ready") || args[0].equalsIgnoreCase("Leave") || args[0].equalsIgnoreCase("Watch") || args[0].equalsIgnoreCase("NewArena")) {
                    sender.sendMessage(ChatColor.RED + "That can only be run by a player!");
                } else if (args[0].equalsIgnoreCase("Kick")) {
                    return kick_sub(sender, args, playr, console, getArenaSpawn());

                } else if (args[0].equalsIgnoreCase("Refill")) {
                    ReFill_sub(sender, args);

                } else if (args[0].equalsIgnoreCase("Restart")) {
                    Restart_Close_sub(sender, args, playr, false);

                    /////////////////////////////////// Toggle //////////////////////////////////////////////////
                } else if (args[0].equalsIgnoreCase("close")) {
                    Restart_Close_sub(sender, args, playr, true);

                } else if (args[0].equalsIgnoreCase("open")) {
                    open_sub(sender, args);

                    ////////////////////////////////////////////////////////////////////////////////////////////
                } else if (args[0].equalsIgnoreCase("Reload")) {
                    reload_sub(sender);

                } else if (args[0].equalsIgnoreCase("WarpAll")) {
                    warpall_sub(sender, args);

                } else if (args[0].equalsIgnoreCase("Start")) {
                    start_sub(sender, args);

                } else {
                    sender.sendMessage(ChatColor.RED + "Unknown command, type /ha help to see all commands!");
                }
            }
        }
        return false;
    }

    ///////////////////////////// Subroutines //////////////////////////////////

    private void help_sub(CommandSender sender, boolean playr, boolean console) {
        ChatColor c = ChatColor.AQUA;
        sender.sendMessage(ChatColor.GREEN + "----HungerArena Help----");
        sender.sendMessage(c + "/ha - Displays author message!");
        sender.sendMessage(c + "/sponsor [Player] [ItemID] [Amount] - Lets you sponsor someone!");
        sender.sendMessage(c + "/startpoint [1,2,3,4,etc] [1,2,3,4,etc] - Sets the starting points of tributes in a specific arena!");
        sender.sendMessage(c + "/ha newarena [1,2,3,4,etc] - Add a new arena to the server and start setting up the starting points of tributes!\n Use the spawnsTool for setting up all starting points!");
        if (playr && plugin.hookWE() != null)
            sender.sendMessage(c + "/ha addArena [1,2,3,4,etc] - Creates an arena using your current WorldEdit selection.");
        sender.sendMessage(c + "/ha close (1,2,3,4,etc) - Prevents anyone from joining that arena! Numbers are optional");
        sender.sendMessage(c + "/ha help - Displays this screen!");
        sender.sendMessage(c + "/ha join [1,2,3,4,etc] - Makes you join the game!");
        sender.sendMessage(c + "/ha kick [Player] - Kicks a player from the arena!");
        sender.sendMessage(c + "/ha leave - Makes you leave the game!");
        sender.sendMessage(c + "/ha list (1,2,3,4,etc) - Shows a list of players in the game and their health! Numbers are optional.");
        sender.sendMessage(c + "/ha open (1,2,3,4,etc) - Opens the game allowing people to join! Numbers are optional");
        sender.sendMessage(c + "/ha ready - Votes for the game to start!");
        sender.sendMessage(c + "/ha refill (1,2,3,4,etc) - Refills all chests! Numbers are optional");
        sender.sendMessage(c + "/ha reload - Reloads the config!");
        sender.sendMessage(c + "/ha restart (1,2,3,4,etc) - Restarts the game! Numbers are optional");
        sender.sendMessage(c + "/ha rlist (1,2,3,4,etc) - See who's ready! Numbers are optional");
        sender.sendMessage(c + "/ha setspawn - Sets the spawn for dead tributes! You can add the arena # to this command: /setspawn [1,2,3,4,etc.]");
        sender.sendMessage(c + "/ha settorch - Sets the location for a redstone torch in an arena that will be placed at match start!");
        sender.sendMessage(c + "/ha tp [player] - Teleports you to a tribute!");
        sender.sendMessage(c + "/ha start [1,2,3,4,etc] - Unfreezes tributes allowing them to fight!");
        sender.sendMessage(c + "/ha watch [1,2,3,4,etc] - Lets you watch the tributes!");
        sender.sendMessage(c + "/ha warpall [1,2,3,4,etc] - Warps all tribute into position!");
        sender.sendMessage(ChatColor.GREEN + "----------------------");
    }

    private void list_sub(Player p, CommandSender sender, boolean playr, boolean console, String[] args) {
        if (args.length >= 2) {
            if (checkarena(args[1], sender)) {
                a = Integer.parseInt(args[1]);
                list_subsub(p, sender, console);
            }
        } else {
            if (console || (p != null && plugin.getArena(p) == null)) {
                a = 1;
                list_subsub(p, sender, console);
            } else {
                a = plugin.getArena(p);
                list_subsub(p, sender, console);
            }
        }
    }

    private void list_subsub(Player p, CommandSender sender, boolean console) {
        sender.sendMessage(ChatColor.AQUA + "----- Arena " + a + " -----");
        if (!plugin.Playing.get(a).isEmpty() && plugin.Playing.containsKey(a)) {
            for (String playernames : plugin.Playing.get(a)) {
                Player players = plugin.getServer().getPlayerExact(playernames);
                if (console || (p != null && p.hasPermission("HungerArena.GameMaker"))) {
                    double maxh;
                    try {
                        maxh = players.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    } catch (Exception e) {
                        maxh = ((Damageable) players).getMaxHealth();
                    }
                    sender.sendMessage(ChatColor.GREEN + playernames + " Life: " + ((Damageable) players).getHealth() + "/" + maxh);
                } else if (console || (p != null && p.hasPermission("HungerArena.List"))) {
                    sender.sendMessage(ChatColor.GREEN + playernames);
                }
            }
        } else {
            sender.sendMessage(ChatColor.GRAY + "No one is playing!");
        }
        sender.sendMessage(ChatColor.AQUA + "-------------------");
    }

    private void rList_sub(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            if (checkarena(args[1], sender)) {
                a = Integer.parseInt(args[1]);
                sender.sendMessage(ChatColor.AQUA + "----- Arena " + a + " -----");
                if (!plugin.Ready.get(a).isEmpty() && plugin.Ready.containsKey(a)) {
                    for (String playernames : plugin.Ready.get(a)) {
                        sender.sendMessage(ChatColor.GREEN + playernames);
                    }
                } else {
                    sender.sendMessage(ChatColor.GRAY + "No one is ready!");
                }
                sender.sendMessage(ChatColor.AQUA + "-------------------");
            }
        } else {
            sender.sendMessage(ChatColor.AQUA + "----- Arena 1 -----");
            if (!plugin.Ready.get(1).isEmpty() && plugin.Ready.containsKey(1)) {
                for (String playernames : plugin.Ready.get(1)) {
                    sender.sendMessage(ChatColor.GREEN + playernames);
                }
            } else {
                sender.sendMessage(ChatColor.GRAY + "No one is ready!");
            }
            sender.sendMessage(ChatColor.AQUA + "-------------------");
        }
    }

    private boolean kick_sub(CommandSender sender, String[] args, boolean playr, boolean console, Location Spawn) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "/ha kick [playername]!");
            return false;
        }
        Player target = Bukkit.getServer().getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Use playername !");
            return false;
        }
        if (console || (playr && ((Player) sender).hasPermission("HungerArena.Kick"))) {
            if (plugin.getArena(target) != null) {
                a = plugin.getArena(target);
                plugin.Playing.get(a).remove(target.getName());
                if (!plugin.config.getBoolean("broadcastAll")) {
                    plugin.getServer().broadcastMessage(ChatColor.RED + target.getName() + " was kicked from arena " + a + "!");
                } else {
                    for (String gn : plugin.Playing.get(a)) {
                        Player g = plugin.getServer().getPlayer(gn);
                        g.sendMessage(ChatColor.RED + target.getName() + " was kicked from the game!");
                    }
                }
                clearInv(target);
                target.teleport(Spawn);
                plugin.quit.get(a).add(target.getName());
                plugin.RestoreInv(target, target.getName());
                plugin.winner(a);
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "That player isn't in the game!");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.RED + "You don't have permission!");
            return true;
        }
    }

    private void ReFill_sub(CommandSender sender, String[] args) {
        plugin.reloadChests();
        int arena = 0;
        if (args.length >= 2) if (checkarena(args[1], sender)) arena = Integer.parseInt(args[1]);

        Set<String> StorList = plugin.getChests().getConfigurationSection("Storage").getKeys(false);
        for (String xyz2 : StorList) {
            int chestx = plugin.getChests().getInt("Storage." + xyz2 + ".Location.X");
            int chesty = plugin.getChests().getInt("Storage." + xyz2 + ".Location.Y");
            int chestz = plugin.getChests().getInt("Storage." + xyz2 + ".Location.Z");
            int chesta = plugin.getChests().getInt("Storage." + xyz2 + ".Arena");
            String chestw = plugin.getChests().getString("Storage." + xyz2 + ".Location.W");
            Block blockatlocation = Bukkit.getWorld(chestw).getBlockAt(chestx, chesty, chestz);
            plugin.exists = false;
            if (chesta == arena || arena == 0) {
                if (blockatlocation.getState() instanceof InventoryHolder) {
                    plugin.exists = true;
                    int ChunkX = ((new Double(blockatlocation.getX() / 16).intValue()));
                    int ChunkZ = ((new Double(blockatlocation.getZ() / 16).intValue()));
                    if (!blockatlocation.getWorld().isChunkLoaded(ChunkX, ChunkZ)) {
                        blockatlocation.getWorld().loadChunk(ChunkX, ChunkZ, true);
                    }
                    InventoryHolder chest = (InventoryHolder) blockatlocation.getState();
                    chest.getInventory().clear();
                    ItemStack[] itemsinchest = null;
                    Object o = plugin.getChests().get("Storage." + xyz2 + ".ItemsInStorage");
                    if (o instanceof ItemStack[]) {
                        itemsinchest = (ItemStack[]) o;
                    } else if (o instanceof List) {
                        itemsinchest = (ItemStack[]) ((List<ItemStack>) o).toArray(new ItemStack[]{});
                    }
                    if (chest.getInventory().getSize() == itemsinchest.length) {
                        chest.getInventory().setContents(itemsinchest);
                    } else {
                        for (ItemStack item : itemsinchest) {
                            if ((item != null) && (item.getType() != null)) chest.getInventory().addItem(item);
                        }
                    }
                }
            }
        }
        if (arena != 0) sender.sendMessage(ChatColor.GREEN + "All for arena " + arena + " refilled!");
        else sender.sendMessage(ChatColor.GREEN + "All chests refilled!");
    }

    private void Restart_Close_sub(CommandSender sender, String[] args, boolean playr, boolean closeit) {
        Set<Integer> list = plugin.open.keySet();
        if (args.length >= 2) {
            list = new HashSet<Integer>();
            if (checkarena(args[1], sender))
                list.add(Integer.parseInt(args[1]));
        }
        for (int a : list) {
            if (!closeit || (closeit && plugin.open.get(a))) {
                if (plugin.deathtime.get(a) != null) {
                    plugin.getServer().getScheduler().cancelTask(plugin.deathtime.get(a));
                    plugin.deathtime.put(a, null);
                }
                if (plugin.grace.get(a) != null) {
                    plugin.getServer().getScheduler().cancelTask(plugin.grace.get(a));
                    plugin.grace.put(a, null);
                }
                if (plugin.start.get(a) != null) {
                    plugin.getServer().getScheduler().cancelTask(plugin.start.get(a));
                    plugin.start.put(a, null);
                }

                if (plugin.timetodeath.get(a) != null)
                    plugin.timetodeath.remove(a);
                plugin.frozen.get(a).clear();
                if (plugin.Playing.get(a) != null) {
                    for (String players : plugin.Playing.get(a)) {
                        Player tributes = plugin.getServer().getPlayerExact(players);
                        clearInv(tributes);
                        tributes.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                        tributes.teleport(tributes.getWorld().getSpawnLocation());
                        plugin.scoreboards.remove(players);
                        plugin.kills.remove(players);
                        plugin.frozen.get(a).add(players);
                    }
                }
                if (plugin.kills.containsKey("__SuM__")) plugin.kills.remove("__SuM__");
                if (plugin.watching.get(a) != null) {
                    for (String sname : plugin.watching.get(a)) {
                        Player spectators = plugin.getServer().getPlayerExact(sname);
                        spectators.teleport(spectators.getWorld().getSpawnLocation());
                        spectators.setAllowFlight(false);
                        spectators.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
                        for (Player online : plugin.getServer().getOnlinePlayers()) {
                            online.showPlayer(plugin, spectators);
                        }
                        plugin.scoreboards.remove(sname);
                    }
                }
                if (plugin.frozen.get(a) != null) {
                    for (String sname : plugin.frozen.get(a)) {
                        Player player = plugin.getServer().getPlayerExact(sname);
                        plugin.RestoreInv(player, sname);
                    }
                }
                plugin.Dead.get(a).clear();
                plugin.quit.get(a).clear();
                plugin.watching.get(a).clear();
                plugin.frozen.get(a).clear();
                plugin.Ready.get(a).clear();
                plugin.needConfirm.get(a).clear();
                plugin.out.get(a).clear();
                plugin.Playing.get(a).clear();
                plugin.inArena.get(a).clear();

                if (closeit) {
                    plugin.open.put(a, false);
                } else {
                    plugin.canjoin.put(a, false);
                    plugin.open.put(a, true);
                }

                plugin.matchRunning.put(a, null);
                List<String> blocksbroken = plugin.data.getStringList("Blocks_Destroyed");
                List<String> blocksplaced = plugin.data.getStringList("Blocks_Placed");
                ArrayList<String> toremove = new ArrayList<String>();
                ArrayList<String> toremove2 = new ArrayList<String>();
                for (String blocks : blocksplaced) {
                    String[] coords = blocks.split(",");
                    World w = plugin.getServer().getWorld(coords[0]);
                    double x = Double.parseDouble(coords[1]);
                    double y = Double.parseDouble(coords[2]);
                    double z = Double.parseDouble(coords[3]);
                    int arena = Integer.parseInt(coords[4]);
                    Location blockl = new Location(w, x, y, z);
                    Block block = w.getBlockAt(blockl);
                    if (arena == a) {
                        block.setType(Material.AIR);
                        block.getState().update();
                        toremove.add(blocks);
                    }
                }
                for (String blocks : blocksbroken) {
                    String[] coords = blocks.split(blocks.contains(";") ? ";" : ",");
                    World w = plugin.getServer().getWorld(coords[0]);
                    double x = Double.parseDouble(coords[1]);
                    double y = Double.parseDouble(coords[2]);
                    double z = Double.parseDouble(coords[3]);
                    String d = coords[4];
                    byte m = 0;
                    BlockData newBlData = null;
                    try {
                        m = Byte.parseByte(coords[5]);
                    } catch (NumberFormatException Ex) {
                        newBlData = Bukkit.createBlockData(coords[5]);
                    }
                    int arena = Integer.parseInt(coords[6]);
                    Location blockl = new Location(w, x, y, z);
                    Block block = w.getBlockAt(blockl);
                    if (arena == a) {
                        if (newBlData == null) {
                            try {
                                int d2 = Integer.parseInt(d);
                                Method setD = Class.forName("org.bukkit.block.Block").getDeclaredMethod("setTypeIdAndData", int.class, byte.class, boolean.class);
                                setD.setAccessible(true);
                                setD.invoke(block, d2, m, true);
                            } catch (Exception | NoSuchMethodError e) {
                                if (d.replaceAll("[0-9]", "").equals("")) {
                                    int d2 = Integer.parseInt(d);
                                    d = plugin.findOldMaterial(d2, m).name();
                                }
                                block.setType(Material.getMaterial(d), true);
                                try {
                                    Method setD = Class.forName("org.bukkit.block.Block").getDeclaredMethod("setData", byte.class, boolean.class);
                                    setD.setAccessible(true);
                                    setD.invoke(block, m, true);
                                } catch (Exception | NoSuchMethodError ee) {
                                    try {
                                        BlockData BlData = block.getBlockData();
                                        if (BlData instanceof org.bukkit.block.data.type.Snow) {
                                            if (m > 0) ((org.bukkit.block.data.type.Snow) BlData).setLayers(m + 1);
                                        }
                                        if (BlData instanceof org.bukkit.block.data.type.Cake) {
                                            ((org.bukkit.block.data.type.Cake) BlData).setBites(m);
                                        }
                                        if (coords.length > 7 && (coords[7] != null || coords[7].isEmpty())) {
                                            String BlDir = coords[7];
                                            if (BlData instanceof org.bukkit.block.data.Directional) {
                                                ((org.bukkit.block.data.Directional) BlData).setFacing(BlockFace.valueOf(BlDir));
                                            }
                                        }
                                        block.setBlockData(BlData);
                                    } catch (Exception | NoSuchMethodError xx) {
                                    }
                                }
                            }
                        } else {
                            block.setBlockData(newBlData);
                        }
                        block.getState().update();
                        toremove2.add(blocks);
                    }
                }
                for (String blocks : toremove) {
                    blocksplaced.remove(blocks);
                }
                for (String blocks : toremove2) {
                    blocksbroken.remove(blocks);
                }
                toremove.clear();
                toremove2.clear();
                plugin.data.set("Blocks_Destroyed", blocksbroken);
                plugin.data.set("Blocks_Placed", blocksplaced);
                plugin.data.options().copyDefaults();
                plugin.saveData();

                plugin.setTorch(a, false);

                if (playr) ((Player) sender).performCommand("ha refill " + a);
                else plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "ha refill " + a);

                if (closeit) {
                    sender.sendMessage(ChatColor.GOLD + "Arena " + a + " Closed!");
                } else {
                    sender.sendMessage(ChatColor.AQUA + "Arena " + a + " has been reset!");
                }

            } else {
                sender.sendMessage(ChatColor.RED + "Arena " + a + " already closed, type /ha open to re-open them!");
            }
        }
    }

    private void open_sub(CommandSender sender, String[] args) {
        Set<Integer> list = plugin.open.keySet();
        if (args.length >= 2) {
            list = new HashSet<Integer>();
            if (checkarena(args[1], sender))
                list.add(Integer.parseInt(args[1]));
        }
        for (int i : list) {
            if (!plugin.open.get(i)) {
                plugin.open.put(i, true);
                sender.sendMessage(ChatColor.GOLD + "Arena " + i + " Open!");
            } else {
                sender.sendMessage(ChatColor.RED + "Arena " + i + " already open, type /ha close to close them!");
            }
        }
    }

    private void reload_sub(CommandSender sender) {
        for (Player online : plugin.getServer().getOnlinePlayers()) {
            if (plugin.getArena(online) != null) {
                a = plugin.getArena(online);
                plugin.RestoreInv(online, online.getName());
                online.teleport(getArenaSpawn());
            }
        }

        plugin.location.clear();
        plugin.reward.clear();
        plugin.cost.clear();
        plugin.fee.clear();
        plugin.chestPay.clear();
        plugin.spawns = null;
        plugin.worldsNames.clear();
        plugin.data = null;
        plugin.management = null;
        plugin.myChests = null;
        HandlerList.unregisterAll(plugin);
        plugin.reloadConfig();
        plugin.onEnable();
        sender.sendMessage(ChatColor.AQUA + "HungerArena Reloaded!");
        System.out.println(ChatColor.GREEN + sender.getName() + " reloaded HungerArena!");
    }

    private void warpall_sub(final CommandSender sender, String[] args) {
        if ((plugin.spawns.getString("Spawns_set.0") == null) || plugin.spawns.getString("Spawns_set.0").equalsIgnoreCase("false")) {
            sender.sendMessage(ChatColor.RED + "/ha setspawn hasn't ever been run!");
        } else {
            if (args.length >= 2) {
                if (checkarena(args[1], sender)) {
                    a = Integer.parseInt(args[1]);
                    if ((plugin.spawns.getString("Spawns_set." + a) == null) || (plugin.spawns.getString("Spawns_set." + a).equalsIgnoreCase("false"))) {
                        sender.sendMessage(ChatColor.RED + "/ha setspawn for Arena " + a + " hasn't been run!");
                    } else {
                        if (plugin.Playing.get(a).size() <= 1) {
                            sender.sendMessage(ChatColor.RED + "There are not enough players!");

                        } else if (plugin.canjoin.get(a) == true) {
                            sender.sendMessage(ChatColor.RED + "Game already in progress!");

                        } else {
                            plugin.setTorch(a, false);

                            if (plugin.config.getString("Auto_Start").equalsIgnoreCase("true")) {
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    public void run() {
                                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ha start " + a);
                                    }
                                }, 20L);
                            }
                            List<Integer> shuffle = new ArrayList<Integer>();
                            for (i = 1; i <= plugin.location.get(a).size(); shuffle.add(i++)) {
                            }
                            Collections.shuffle(shuffle);
                            i = 1;
                            for (String playing : plugin.Playing.get(a)) {
                                Player tribute = plugin.getServer().getPlayerExact(playing);
                                plugin.frozen.get(a).add(tribute.getName());
                                Location toLoc = plugin.location.get(a).get(shuffle.get(i));
                                tribute.setHealth(20);
                                tribute.setFoodLevel(20);
                                tribute.setSaturation(20);
                                tribute.setLevel(0);
                                clearInv(tribute);
                                for (PotionEffect pe : tribute.getActivePotionEffects()) {
                                    PotionEffectType potion = pe.getType();
                                    tribute.removePotionEffect(potion);
                                }
                                if (tribute.getAllowFlight()) {
                                    tribute.setAllowFlight(false);
                                }
                                Location opposite = toLoc.clone();
                                double dist = 0;
                                double dist2;
                                for (Location loc : plugin.location.get(a).values()) {
                                    dist2 = loc.distance(toLoc);
                                    if (dist2 > dist) {
                                        dist = dist2;
                                        opposite = loc.clone();
                                    }
                                }
                                toLoc.setDirection(opposite.toVector().subtract(toLoc.toVector()));
                                MetadataValue MdV_Location = new FixedMetadataValue(plugin, toLoc);
                                tribute.setMetadata("HA-Location", MdV_Location);
                                tribute.teleport(toLoc);
                                i += 1;
                            }
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                public void run() {
                                    sender.sendMessage(ChatColor.AQUA + "All Tributes warped!");
                                    plugin.matchRunning.put(a, "warped");
                                }
                            }, 10L);
                        }
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Too few arguments, specify an arena");
            }
        }
    }

    private void start_sub(CommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "You need to specify the arena to start!");
        } else {
            if (checkarena(args[1], sender)) {
                a = Integer.parseInt(args[1]);
                if (plugin.canjoin.get(a) == true)
                    sender.sendMessage(ChatColor.RED + "Game already in progress!");
                else if (plugin.Playing.get(a).size() == 1) {
                    sender.sendMessage(ChatColor.RED + "There are not enough players!");
                } else if (plugin.Playing.get(a).isEmpty())
                    sender.sendMessage(ChatColor.RED + "No one is in that game!");
                else if ((plugin.matchRunning.get(a) != null) && (plugin.matchRunning.get(a).equals("warped")))
                    plugin.startGames(a);
                else sender.sendMessage(ChatColor.RED + "First all players must be warped!");
            }
        }
    }

    private boolean checkarena(String Int, CommandSender sender) {
        a = 0;
        try {
            a = Integer.parseInt(Int);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "You have to enter the Arena number!");
            return false;
        }
        if (a > 0) {
            if (plugin.canjoin.containsKey(a)) {
                return true;
            }
        }
        sender.sendMessage(ChatColor.RED + "That arena doesn't exist!");
        return false;
    }

    private void confirmSub(Player p, String pname, String ThisWorld) {
        if (plugin.config.getBoolean("EntryFee.enabled")) {
            if ((!plugin.config.getBoolean("EntryFee.eco")) || (plugin.config.getBoolean("EntryFee.eco") && !(plugin.econ.getBalance(p) < plugin.config.getDouble("EntryFee.cost")))) {
                i = 0;
                for (ItemStack fee : plugin.fee) {
                    int total = plugin.fee.size();
                    if (p.getInventory().containsAtLeast(fee, fee.getAmount())) {
                        i += 1;
                        if (total == i) {
                            if (plugin.config.getBoolean("EntryFee.eco")) {
                                plugin.econ.withdrawPlayer(p, plugin.config.getDouble("EntryFee.cost"));
                                p.sendMessage(ChatColor.GOLD + "[HungerArena] " + ChatColor.GREEN + "$" + plugin.config.getDouble("EntryFee.cost") + " has been taken from your account!");
                            }
                            for (ItemStack fees : plugin.fee) {
                                String beginning = fees.getType().toString().substring(0, 1);
                                String item = beginning.toUpperCase() + fees.getType().toString().substring(1).toLowerCase().replace("_", " ");
                                int amount = fees.getAmount();
                                p.getInventory().removeItem(fees);
                                if (amount > 1)
                                    p.sendMessage(ChatColor.GOLD + "[HungerArena] " + ChatColor.GREEN + amount + " " + item + "s was paid to join the games.");
                                else
                                    p.sendMessage(ChatColor.GOLD + "[HungerArena] " + ChatColor.GREEN + amount + " " + item + " was paid to join the games.");
                            }
                            preparePlayer(p, pname, ThisWorld);
                        }
                    }
                }
                if (plugin.fee.size() > i) {
                    p.sendMessage(ChatColor.RED + "You are missing some items and can't join the games...");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have enough money to join!");
            }
        } else if (plugin.config.getBoolean("EntryFee.eco")) {
            if (!(plugin.econ.getBalance(p) < plugin.config.getDouble("EntryFee.cost"))) {
                plugin.econ.withdrawPlayer(p, plugin.config.getDouble("EntryFee.cost"));
                p.sendMessage(ChatColor.GOLD + "[HungerArena] " + ChatColor.GREEN + "$" + plugin.config.getDouble("EntryFee.cost") + " has been taken from your account!");
                preparePlayer(p, pname, ThisWorld);
            } else {
                p.sendMessage(ChatColor.RED + "You don't have enough money to join!");
            }
        } else {
            preparePlayer(p, pname, ThisWorld);
        }
    }

    private void preparePlayer(Player p, String pname, String ThisWorld) {
        plugin.Playing.get(a).add(pname);
        plugin.needConfirm.get(a).remove(pname);
        p.sendMessage(ChatColor.GREEN + "Do /ha ready to vote to start the games!");
        FileConfiguration pinfo = plugin.getPConfig(pname);
        pinfo.set("inv", p.getInventory().getContents());
        pinfo.set("armor", p.getInventory().getArmorContents());
        pinfo.set("world", ThisWorld);
        pinfo.set("player", pname);
        plugin.savePFile(pname);
        clearInv(p);
        plugin.needInv.add(pname);
        if (plugin.config.getBoolean("broadcastAll")) {
            plugin.getServer().broadcastMessage(ChatColor.AQUA + pname + " has Joined Arena " + a + "! " + ChatColor.GRAY + plugin.Playing.get(a).size() + "/" + plugin.maxPlayers.get(a));
        } else {
            for (String gn : plugin.Playing.get(a)) {
                Player g = plugin.getServer().getPlayer(gn);
                g.sendMessage(ChatColor.AQUA + pname + " has Joined the Game! " + ChatColor.GRAY + plugin.Playing.get(a).size() + "/" + plugin.maxPlayers.get(a));
            }
        }
        if (plugin.Playing.get(a).size() == plugin.maxPlayers.get(a)) {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "ha warpall " + a);
        }
    }
}
