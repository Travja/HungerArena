package me.Travja.HungerArena;

public class ConfigManager {
	public static Main plugin;
	public ConfigManager(Main m){
		ConfigManager.plugin = m;
	}

	public void setup(){
		System.out.println("Setting up!");
		if(!plugin.config.contains("worlds"))
			plugin.config.addDefault("worlds", "{}");
		if(!plugin.config.contains("DeathMatch"))
			plugin.config.addDefault("DeathMatch", "0");
		if(!plugin.config.contains("broadcastAll"))
			plugin.config.addDefault("broadcastAll", true);
		if(!plugin.config.contains("maxPlayers"))
			plugin.config.addDefault("maxPlayers", "24");
		if(!plugin.config.contains("Start_Message"))
			plugin.config.addDefault("Start_Message", "&bLet The Games Begin!");
		if(!plugin.config.contains("Auto_Restart"))
			plugin.config.addDefault("Auto_Restart", false);
		if(!plugin.config.contains("Auto_Start"))
			plugin.config.addDefault("Auto_Start", false);
		if(!plugin.config.contains("Need_Confirm"))
			plugin.config.addDefault("Need_Confirm", true);
		if(!plugin.config.contains("Countdown"))
			plugin.config.addDefault("Countdown", true);
		if(!plugin.config.contains("Countdown_Timer"))
			plugin.config.addDefault("Countdown_Timer", "15");
		if(!plugin.config.contains("Grace_Period"))
			plugin.config.addDefault("Grace_Period", "60");
		if(!plugin.config.contains("Protected_Arena"))
			plugin.config.addDefault("Protected_Arena", true);
		if(!plugin.config.contains("Protected_Arena_Always"))
			plugin.config.addDefault("Protected_Arena_Always", false);
		if(!plugin.config.contains("Force_Players_toSpawn"))
			plugin.config.addDefault("Force_Players_toSpawn", false);
		if(!plugin.config.contains("Frozen_Teleport"))
			plugin.config.addDefault("Frozen_Teleport", true);
		if(!plugin.config.contains("Explode_on_Move"))
			plugin.config.addDefault("Explode_on_Move", false);
		if(!plugin.config.contains("Cannon_Death"))
			plugin.config.addDefault("Cannon_Death", true);
		if(!plugin.config.contains("Reward"))
			plugin.config.addDefault("Reward", "- 264,10");
		if(!plugin.config.contains("Sponsor_Cost"))
			plugin.config.addDefault("Sponsor_Cost", "- 264,1");
		if(!plugin.config.contains("EntryFee")){
			plugin.config.addDefault("EntryFee.enabled", false);
			plugin.config.addDefault("EntryFee.eco", false);
			plugin.config.addDefault("EntryFee.cost", "50");
			plugin.config.addDefault("EntryFee.fee", "- 265,1");
		}else{
			if(!plugin.config.contains("EntryFee.enabled"))
				plugin.config.addDefault("EntryFee.enabled", false);
			if(!plugin.config.contains("EntryFee.eco"))
				plugin.config.addDefault("EntryFee.eco", false);
			if(!plugin.config.contains("EntryFee.cost"))
				plugin.config.addDefault("EntryFee.cost", "50");
			if(!plugin.config.contains("EntryFee.fee"))
				plugin.config.addDefault("EntryFee.fee", "- 265,1");
		}
		if(!plugin.config.contains("rewardEco")){
			plugin.config.addDefault("rewardEco.enabled", false);
			plugin.config.addDefault("rewardEco.reward", "100");
		}else{
			if(!plugin.config.contains("rewardEco.enabled"))
				plugin.config.addDefault("rewardEco.enabled", false);
			if(!plugin.config.contains("rewardEco.reward"))
				plugin.config.addDefault("rewardEco.reward", "100");
		}
		if(!plugin.config.contains("sponsorEco")){
			plugin.config.addDefault("sponsorEco.enabled", false);
			plugin.config.addDefault("sponsorEco.cost", "50");
		}else{
			if(!plugin.config.contains("sponsorEco.enabled"))
				plugin.config.addDefault("sponsorEco.enabled", false);
			if(!plugin.config.contains("sponsorEco.cost"))
				plugin.config.addDefault("sponsorEco.cost", "50");
		}
		if(!plugin.config.contains("ChatClose"))
			plugin.config.addDefault("ChatClose", true);
		if(!plugin.config.contains("ChatClose_Radius"))
			plugin.config.addDefault("ChatClose_Radius", "10");
		if(!plugin.config.contains("WorldEdit"))
			plugin.config.addDefault("WorldEdit", false);
		plugin.saveConfig();
	}
}