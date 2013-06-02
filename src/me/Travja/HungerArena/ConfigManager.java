package me.Travja.HungerArena;

public class ConfigManager {
	public static Main plugin;
	public ConfigManager(Main m){
		ConfigManager.plugin = m;
	}

	public static void setup(){
		if(plugin.config.get("worlds")== null)
			plugin.config.addDefault("worlds", "{}");
		if(plugin.config.get("DeathMatch")== null)
			plugin.config.addDefault("DeathMatch", "0");
		if(plugin.config.get("broadcastAll")== null)
			plugin.config.addDefault("broadcastAll", true);
		if(plugin.config.get("maxPlayers")== null)
			plugin.config.addDefault("maxPlayers", "24");
		if(plugin.config.get("Start_Message")== null)
			plugin.config.addDefault("Start_Message", "&bLet The Games Begin!");
		if(plugin.config.get("Auto_Restart")== null)
			plugin.config.addDefault("Auto_Restart", false);
		if(plugin.config.get("Auto_Start")== null)
			plugin.config.addDefault("Auto_Start", false);
		if(plugin.config.get("Need_Confirm")== null)
			plugin.config.addDefault("Need_Confirm", true);
		if(plugin.config.get("Countdown")== null)
			plugin.config.addDefault("Countdown", true);
		if(plugin.config.get("Countdown_Timer")== null)
			plugin.config.addDefault("Countdown_Timer", "15");
		if(plugin.config.get("Grace_Period")== null)
			plugin.config.addDefault("Grace_Period", "60");
		if(plugin.config.get("Protected_Arena")== null)
			plugin.config.addDefault("Protected_Arena", true);
		if(plugin.config.get("Protected_Arena_Always")== null)
			plugin.config.addDefault("Protected_Arena_Always", false);
		if(plugin.config.get("Force_Players_toSpawn")== null)
			plugin.config.addDefault("Force_Players_toSpawn", false);
		if(plugin.config.get("Frozen_Teleport")== null)
			plugin.config.addDefault("Frozen_Teleport", true);
		if(plugin.config.get("Explode_on_Move")== null)
			plugin.config.addDefault("Explode_on_Move", false);
		if(plugin.config.get("Cannon_Death")== null)
			plugin.config.addDefault("Cannon_Death", true);
		if(plugin.config.get("Reward")== null)
			plugin.config.addDefault("Reward", "- 264,10");
		if(plugin.config.get("Sponsor_Cost")== null)
			plugin.config.addDefault("Sponsor_Cost", "- 264,1");
		if(plugin.config.get("EntryFee")== null){
			plugin.config.addDefault("EntryFee.enabled", false);
			plugin.config.addDefault("EntryFee.eco", false);
			plugin.config.addDefault("EntryFee.cost", "50");
			plugin.config.addDefault("EntryFee.fee", "- 265,1");
		}else{
			if(plugin.config.get("EntryFee.enabled")== null)
				plugin.config.addDefault("EntryFee.enabled", false);
			if(plugin.config.get("EntryFee.eco")== null)
				plugin.config.addDefault("EntryFee.eco", false);
			if(plugin.config.get("EntryFee.cost")== null)
				plugin.config.addDefault("EntryFee.cost", "50");
			if(plugin.config.get("EntryFee.fee")== null)
				plugin.config.addDefault("EntryFee.fee", "- 265,1");
		}
		if(plugin.config.get("rewardEco")== null){
			plugin.config.addDefault("rewardEco.enabled", false);
			plugin.config.addDefault("rewardEco.reward", "100");
		}else{
			if(plugin.config.get("rewardEco.enabled")== null)
				plugin.config.addDefault("rewardEco.enabled", false);
			if(plugin.config.get("rewardEco.reward")== null)
				plugin.config.addDefault("rewardEco.reward", "100");
		}
		if(plugin.config.get("sponsorEco")== null){
			plugin.config.addDefault("sponsorEco.enabled", false);
			plugin.config.addDefault("sponsorEco.cost", "50");
		}else{
			if(plugin.config.get("sponsorEco.enabled")== null)
				plugin.config.addDefault("sponsorEco.enabled", false);
			if(plugin.config.get("sponsorEco.cost")== null)
				plugin.config.addDefault("sponsorEco.cost", "50");
		}
		if(plugin.config.get("ChatClose")== null)
			plugin.config.addDefault("ChatClose", true);
		if(plugin.config.get("ChatClose_Radius")== null)
			plugin.config.addDefault("ChatClose_Radius", "10");
		if(plugin.config.get("WorldEdit")== null)
			plugin.config.addDefault("WorldEdit", false);
		plugin.saveConfig();
	}
}
