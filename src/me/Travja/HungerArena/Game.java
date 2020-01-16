package me.Travja.HungerArena;


public class Game {
	public static Main plugin;
	public Game(Main m) {
		Game.plugin = m;
	}

	public static int countdown;
	public static int dm;
	public static int refill;
	public static int gp;
	public static int min;
	public static int max;

	public static void init(){
		countdown = plugin.config.getInt("Countdown");
		dm = plugin.config.getInt("DeathMatch");
		refill = plugin.config.getInt("refillTime");
		gp = plugin.config.getInt("Grace_Period");
		min = plugin.config.getInt("DeathMatch");
		max = plugin.config.getInt("DeathMatch");
	}
}
