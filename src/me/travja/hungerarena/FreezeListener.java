package me.Travja.HungerArena;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class FreezeListener implements Listener {
	public Main plugin;
	public FreezeListener(Main m) {
		this.plugin = m;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		Player p = event.getPlayer();
		String pname = p.getName();
		if(plugin.Frozen.contains(pname) && plugin.config.getString("Frozen_Teleport").equalsIgnoreCase("True")){
			String[] onecoords = plugin.config.getString("Tribute_one_spawn").split(",");
			double x = Double.parseDouble(onecoords[0]);
			double y = Double.parseDouble(onecoords[1]);
			double z = Double.parseDouble(onecoords[2]);
			World w = plugin.getServer().getWorld(onecoords[3]);
			Location onespwn = new Location(w,x,y,z);
			String[] twocoords = plugin.config.getString("Tribute_two_spawn").split(",");
			double twox = Double.parseDouble(twocoords[0]);
			double twoy = Double.parseDouble(twocoords[1]);
			double twoz = Double.parseDouble(twocoords[2]);
			World twow = plugin.getServer().getWorld(twocoords[3]);
			Location twospwn = new Location(twow,twox,twoy,twoz);
			String[] threecoords = plugin.config.getString("Tribute_three_spawn").split(",");
			double threex = Double.parseDouble(threecoords[0]);
			double threey = Double.parseDouble(threecoords[1]);
			double threez = Double.parseDouble(threecoords[2]);
			World threew = plugin.getServer().getWorld(threecoords[3]);
			Location threespwn = new Location(threew,threex,threey,threez);
			String[] fourcoords = plugin.config.getString("Tribute_four_spawn").split(",");
			double fourx = Double.parseDouble(fourcoords[0]);
			double foury = Double.parseDouble(fourcoords[1]);
			double fourz = Double.parseDouble(fourcoords[2]);
			World fourw = plugin.getServer().getWorld(fourcoords[3]);
			Location fourspwn = new Location(fourw,fourx,foury,fourz);
			String[] fivecoords = plugin.config.getString("Tribute_five_spawn").split(",");
			double fivex = Double.parseDouble(fivecoords[0]);
			double fivey = Double.parseDouble(fivecoords[1]);
			double fivez = Double.parseDouble(fivecoords[2]);
			World fivew = plugin.getServer().getWorld(fivecoords[3]);
			Location fivespwn = new Location(fivew,fivex,fivey,fivez);
			String[] sixcoords = plugin.config.getString("Tribute_six_spawn").split(",");
			double sixx = Double.parseDouble(sixcoords[0]);
			double sixy = Double.parseDouble(sixcoords[1]);
			double sixz = Double.parseDouble(sixcoords[2]);
			World sixw = plugin.getServer().getWorld(sixcoords[3]);
			Location sixspwn = new Location(sixw,sixx,sixy,sixz);
			String[] sevencoords = plugin.config.getString("Tribute_seven_spawn").split(",");
			double sevenx = Double.parseDouble(sevencoords[0]);
			double seveny = Double.parseDouble(sevencoords[1]);
			double sevenz = Double.parseDouble(sevencoords[2]);
			World sevenw = plugin.getServer().getWorld(sevencoords[3]);
			Location sevenspwn = new Location(sevenw,sevenx,seveny,sevenz);
			String[] eightcoords = plugin.config.getString("Tribute_eight_spawn").split(",");
			double eightx = Double.parseDouble(eightcoords[0]);
			double eighty = Double.parseDouble(eightcoords[1]);
			double eightz = Double.parseDouble(eightcoords[2]);
			World eightw = plugin.getServer().getWorld(eightcoords[3]);
			Location eightspwn = new Location(eightw,eightx,eighty,eightz);
			String[] ninecoords = plugin.config.getString("Tribute_nine_spawn").split(",");
			double ninex = Double.parseDouble(ninecoords[0]);
			double niney = Double.parseDouble(ninecoords[1]);
			double ninez = Double.parseDouble(ninecoords[2]);
			World ninew = plugin.getServer().getWorld(ninecoords[3]);
			Location ninespwn = new Location(ninew,ninex,niney,ninez);
			String[] tencoords = plugin.config.getString("Tribute_ten_spawn").split(",");
			double tenx = Double.parseDouble(tencoords[0]);
			double teny = Double.parseDouble(tencoords[1]);
			double tenz = Double.parseDouble(tencoords[2]);
			World tenw = plugin.getServer().getWorld(tencoords[3]);
			Location tenspwn = new Location(tenw,tenx,teny,tenz);
			String[] elevencoords = plugin.config.getString("Tribute_eleven_spawn").split(",");
			double elevenx = Double.parseDouble(elevencoords[0]);
			double eleveny = Double.parseDouble(elevencoords[1]);
			double elevenz = Double.parseDouble(elevencoords[2]);
			World elevenw = plugin.getServer().getWorld(elevencoords[3]);
			Location elevenspwn = new Location(elevenw,elevenx,eleveny,elevenz);
			String[] twelvecoords = plugin.config.getString("Tribute_twelve_spawn").split(",");
			double twelvex = Double.parseDouble(twelvecoords[0]);
			double twelvey = Double.parseDouble(twelvecoords[1]);
			double twelvez = Double.parseDouble(twelvecoords[2]);
			World twelvew = plugin.getServer().getWorld(twelvecoords[3]);
			Location twelvespwn = new Location(twelvew,twelvex,twelvey,twelvez);
			String[] thirteencoords = plugin.config.getString("Tribute_thirteen_spawn").split(",");
			double thirteenx = Double.parseDouble(thirteencoords[0]);
			double thirteeny = Double.parseDouble(thirteencoords[1]);
			double thirteenz = Double.parseDouble(thirteencoords[2]);
			World thirteenw = plugin.getServer().getWorld(thirteencoords[3]);
			Location thirteenspwn = new Location(thirteenw,thirteenx,thirteeny,thirteenz);
			String[] fourteencoords = plugin.config.getString("Tribute_fourteen_spawn").split(",");
			double fourteenx = Double.parseDouble(fourteencoords[0]);
			double fourteeny = Double.parseDouble(fourteencoords[1]);
			double fourteenz = Double.parseDouble(fourteencoords[2]);
			World fourteenw = plugin.getServer().getWorld(fourteencoords[3]);
			Location fourteenspwn = new Location(fourteenw,fourteenx,fourteeny,fourteenz);
			String[] fifteencoords = plugin.config.getString("Tribute_fifteen_spawn").split(",");
			double fifteenx = Double.parseDouble(fifteencoords[0]);
			double fifteeny = Double.parseDouble(fifteencoords[1]);
			double fifteenz = Double.parseDouble(fifteencoords[2]);
			World fifteenw = plugin.getServer().getWorld(fifteencoords[3]);
			Location fifteenspwn = new Location(fifteenw,fifteenx,fifteeny,fifteenz);
			String[] sixteencoords = plugin.config.getString("Tribute_sixteen_spawn").split(",");
			double sixteenx = Double.parseDouble(sixteencoords[0]);
			double sixteeny = Double.parseDouble(sixteencoords[1]);
			double sixteenz = Double.parseDouble(sixteencoords[2]);
			World sixteenw = plugin.getServer().getWorld(sixteencoords[3]);
			Location sixteenspwn = new Location(sixteenw,sixteenx,sixteeny,sixteenz);
			String[] seventeencoords = plugin.config.getString("Tribute_seventeen_spawn").split(",");
			double seventeenx = Double.parseDouble(seventeencoords[0]);
			double seventeeny = Double.parseDouble(seventeencoords[1]);
			double seventeenz = Double.parseDouble(seventeencoords[2]);
			World seventeenw = plugin.getServer().getWorld(seventeencoords[3]);
			Location seventeenspwn = new Location(seventeenw,seventeenx,seventeeny,seventeenz);
			String[] eighteencoords = plugin.config.getString("Tribute_eighteen_spawn").split(",");
			double eighteenx = Double.parseDouble(eighteencoords[0]);
			double eighteeny = Double.parseDouble(eighteencoords[1]);
			double eighteenz = Double.parseDouble(eighteencoords[2]);
			World eighteenw = plugin.getServer().getWorld(eighteencoords[3]);
			Location eighteenspwn = new Location(eighteenw,eighteenx,eighteeny,eighteenz);
			String[] nineteencoords = plugin.config.getString("Tribute_nineteen_spawn").split(",");
			double nineteenx = Double.parseDouble(nineteencoords[0]);
			double nineteeny = Double.parseDouble(nineteencoords[1]);
			double nineteenz = Double.parseDouble(nineteencoords[2]);
			World nineteenw = plugin.getServer().getWorld(nineteencoords[3]);
			Location nineteenspwn = new Location(nineteenw,nineteenx,nineteeny,nineteenz);
			String[] twentycoords = plugin.config.getString("Tribute_twenty_spawn").split(",");
			double twentyx = Double.parseDouble(twentycoords[0]);
			double twentyy = Double.parseDouble(twentycoords[1]);
			double twentyz = Double.parseDouble(twentycoords[2]);
			World twentyw = plugin.getServer().getWorld(twentycoords[3]);
			Location twentyspwn = new Location(twentyw,twentyx,twentyy,twentyz);
			String[] twentyonecoords = plugin.config.getString("Tribute_twentyone_spawn").split(",");
			double twentyonex = Double.parseDouble(twentyonecoords[0]);
			double twentyoney = Double.parseDouble(twentyonecoords[1]);
			double twentyonez = Double.parseDouble(twentyonecoords[2]);
			World twentyonew = plugin.getServer().getWorld(twentyonecoords[3]);
			Location twentyonespwn = new Location(twentyonew,twentyonex,twentyoney,twentyonez);
			String[] twentytwocoords = plugin.config.getString("Tribute_twentytwo_spawn").split(",");
			double twentytwox = Double.parseDouble(twentytwocoords[0]);
			double twentytwoy = Double.parseDouble(twentytwocoords[1]);
			double twentytwoz = Double.parseDouble(twentytwocoords[2]);
			World twentytwow = plugin.getServer().getWorld(twentytwocoords[3]);
			Location twentytwospwn = new Location(twentytwow,twentytwox,twentytwoy,twentytwoz);
			String[] twentythreecoords = plugin.config.getString("Tribute_twentythree_spawn").split(",");
			double twentythreex = Double.parseDouble(twentythreecoords[0]);
			double twentythreey = Double.parseDouble(twentythreecoords[1]);
			double twentythreez = Double.parseDouble(twentythreecoords[2]);
			World twentythreew = plugin.getServer().getWorld(twentythreecoords[3]);
			Location twentythreespwn = new Location(twentythreew,twentythreex,twentythreey,twentythreez);
			String[] twentyfourcoords = plugin.config.getString("Tribute_twentyfour_spawn").split(",");
			double twentyfourx = Double.parseDouble(twentyfourcoords[0]);
			double twentyfoury = Double.parseDouble(twentyfourcoords[1]);
			double twentyfourz = Double.parseDouble(twentyfourcoords[2]);
			World twentyfourw = plugin.getServer().getWorld(twentyfourcoords[3]);
			Location twentyfourspwn = new Location(twentyfourw,twentyfourx,twentyfoury,twentyfourz);
			if(plugin.config.getString("Explode_on_Move").equalsIgnoreCase("true")){
				if(plugin.Playing.size()>=1){
					String one = plugin.Playing.get(0);
					if(pname==one){
						Player tone = plugin.getServer().getPlayerExact(one);
						if(!tone.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(onespwn)){
							if(!plugin.Dead.contains(tone.getName())){
								World world = tone.getLocation().getWorld();
								world.createExplosion(tone.getLocation(), 0.0F, false);
								tone.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=2){
					String two = plugin.Playing.get(1);
					if(pname==two){
						Player ttwo = plugin.getServer().getPlayerExact(two);
						if(!ttwo.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twospwn)){
							if(!plugin.Dead.contains(ttwo.getName())){
								World world = ttwo.getLocation().getWorld();
								world.createExplosion(ttwo.getLocation(), 0.0F, false);
								ttwo.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=3){
					String three = plugin.Playing.get(2);
					if(pname==three){
						Player tthree = plugin.getServer().getPlayerExact(three);
						if(!tthree.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(threespwn)){
							if(!plugin.Dead.contains(tthree.getName())){
								World world = tthree.getLocation().getWorld();
								world.createExplosion(tthree.getLocation(), 0.0F, false);
								tthree.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=4){
					String four = plugin.Playing.get(3);
					if(pname==four){
						Player tfour = plugin.getServer().getPlayerExact(four);
						if(!tfour.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(fourspwn)){
							if(!plugin.Dead.contains(tfour.getName())){
								World world = tfour.getLocation().getWorld();
								world.createExplosion(tfour.getLocation(), 0.0F, false);
								tfour.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=5){
					String five = plugin.Playing.get(4);
					if(pname==five){
						Player tfive = plugin.getServer().getPlayerExact(five);
						if(!tfive.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(fivespwn)){
							if(!plugin.Dead.contains(tfive.getName())){
								World world = tfive.getLocation().getWorld();
								world.createExplosion(tfive.getLocation(), 0.0F, false);
								tfive.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=6){
					String six = plugin.Playing.get(5);
					if(pname==six){
						Player tsix = plugin.getServer().getPlayerExact(six);
						if(!tsix.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(sixspwn)){
							if(!plugin.Dead.contains(tsix.getName())){
								World world = tsix.getLocation().getWorld();
								world.createExplosion(tsix.getLocation(), 0.0F, false);
								tsix.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=7){
					String seven = plugin.Playing.get(6);
					if(pname==seven){
						Player tseven = plugin.getServer().getPlayerExact(seven);
						if(!tseven.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(sevenspwn)){
							if(!plugin.Dead.contains(tseven.getName())){
								World world = tseven.getLocation().getWorld();
								world.createExplosion(tseven.getLocation(), 0.0F, false);
								tseven.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=8){
					String eight = plugin.Playing.get(7);
					if(pname==eight){
						Player teight = plugin.getServer().getPlayerExact(eight);
						if(!teight.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(eightspwn)){
							if(!plugin.Dead.contains(teight.getName())){
								World world = teight.getLocation().getWorld();
								world.createExplosion(teight.getLocation(), 0.0F, false);
								teight.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=9){
					String nine = plugin.Playing.get(8);
					if(pname==nine){
						Player tnine = plugin.getServer().getPlayerExact(nine);
						if(!tnine.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(ninespwn)){
							if(!plugin.Dead.contains(tnine.getName())){
								World world = tnine.getLocation().getWorld();
								world.createExplosion(tnine.getLocation(), 0.0F, false);
								tnine.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=10){
					String ten = plugin.Playing.get(9);
					if(pname==ten){
						Player tten = plugin.getServer().getPlayerExact(ten);
						if(!tten.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(tenspwn)){
							if(!plugin.Dead.contains(tten.getName())){
								World world = tten.getLocation().getWorld();
								world.createExplosion(tten.getLocation(), 0.0F, false);
								tten.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=11){
					String eleven = plugin.Playing.get(10);
					if(pname==eleven){
						Player televen = plugin.getServer().getPlayerExact(eleven);
						if(!televen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(elevenspwn)){
							if(!plugin.Dead.contains(televen.getName())){
								World world = televen.getLocation().getWorld();
								world.createExplosion(televen.getLocation(), 0.0F, false);
								televen.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=12){
					String twelve = plugin.Playing.get(11);
					if(pname==twelve){
						Player ttwelve = plugin.getServer().getPlayerExact(twelve);
						if(!ttwelve.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twelvespwn)){
							if(!plugin.Dead.contains(ttwelve.getName())){
								World world = ttwelve.getLocation().getWorld();
								world.createExplosion(ttwelve.getLocation(), 0.0F, false);
								ttwelve.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=13){
					String thirteen = plugin.Playing.get(12);
					if(pname==thirteen){
						Player tthirteen = plugin.getServer().getPlayerExact(thirteen);
						if(!tthirteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(thirteenspwn)){
							if(!plugin.Dead.contains(tthirteen.getName())){
								World world = tthirteen.getLocation().getWorld();
								world.createExplosion(tthirteen.getLocation(), 0.0F, false);
								tthirteen.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=14){
					String fourteen = plugin.Playing.get(13);
					if(pname==fourteen){
						Player tfourteen = plugin.getServer().getPlayerExact(fourteen);
						if(!tfourteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(fourteenspwn)){
							if(!plugin.Dead.contains(tfourteen.getName())){
								World world = tfourteen.getLocation().getWorld();
								world.createExplosion(tfourteen.getLocation(), 0.0F, false);
								tfourteen.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=15){
					String fifteen = plugin.Playing.get(14);
					if(pname==fifteen){
						Player tfifteen = plugin.getServer().getPlayerExact(fifteen);
						if(!tfifteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(fifteenspwn)){
							if(!plugin.Dead.contains(tfifteen.getName())){
								World world = tfifteen.getLocation().getWorld();
								world.createExplosion(tfifteen.getLocation(), 0.0F, false);
								tfifteen.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=16){
					String sixteen = plugin.Playing.get(15);
					if(pname==sixteen){
						Player tsixteen = plugin.getServer().getPlayerExact(sixteen);
						if(!tsixteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(sixteenspwn)){
							if(!plugin.Dead.contains(tsixteen.getName())){
								World world = tsixteen.getLocation().getWorld();
								world.createExplosion(tsixteen.getLocation(), 0.0F, false);
								tsixteen.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=17){
					String seventeen = plugin.Playing.get(16);
					if(pname==seventeen){
						Player tseventeen = plugin.getServer().getPlayerExact(seventeen);
						if(!tseventeen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(seventeenspwn)){
							if(!plugin.Dead.contains(tseventeen.getName())){
								World world = tseventeen.getLocation().getWorld();
								world.createExplosion(tseventeen.getLocation(), 0.0F, false);
								tseventeen.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=18){
					String eighteen = plugin.Playing.get(17);
					if(pname==eighteen){
						Player teighteen = plugin.getServer().getPlayerExact(eighteen);
						if(!teighteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(eighteenspwn)){
							if(!plugin.Dead.contains(teighteen.getName())){
								World world = teighteen.getLocation().getWorld();
								world.createExplosion(teighteen.getLocation(), 0.0F, false);
								teighteen.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=19){
					String nineteen = plugin.Playing.get(18);
					if(pname==nineteen){
						Player tnineteen = plugin.getServer().getPlayerExact(nineteen);
						if(!tnineteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(nineteenspwn)){
							if(!plugin.Dead.contains(tnineteen.getName())){
								World world = tnineteen.getLocation().getWorld();
								world.createExplosion(tnineteen.getLocation(), 0.0F, false);
								tnineteen.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=20){
					String twenty = plugin.Playing.get(19);
					if(pname==twenty){
						Player ttwenty = plugin.getServer().getPlayerExact(twenty);
						if(!ttwenty.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentyspwn)){
							if(!plugin.Dead.contains(ttwenty.getName())){
								World world = ttwenty.getLocation().getWorld();
								world.createExplosion(ttwenty.getLocation(), 0.0F, false);
								ttwenty.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=21){
					String twentyone = plugin.Playing.get(20);
					if(pname==twentyone){
						Player ttwentyone = plugin.getServer().getPlayerExact(twentyone);
						if(!ttwentyone.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentyonespwn)){
							if(!plugin.Dead.contains(ttwentyone.getName())){
								World world = ttwentyone.getLocation().getWorld();
								world.createExplosion(ttwentyone.getLocation(), 0.0F, false);
								ttwentyone.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=22){
					String twentytwo = plugin.Playing.get(21);
					if(pname==twentytwo){
						Player ttwentytwo = plugin.getServer().getPlayerExact(twentytwo);
						if(!ttwentytwo.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentytwospwn)){
							if(!plugin.Dead.contains(ttwentytwo.getName())){
								World world = ttwentytwo.getLocation().getWorld();
								world.createExplosion(ttwentytwo.getLocation(), 0.0F, false);
								ttwentytwo.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()>=23){
					String twentythree = plugin.Playing.get(22);
					if(pname==twentythree){
						Player ttwentythree = plugin.getServer().getPlayerExact(twentythree);
						if(!ttwentythree.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentythreespwn)){
							if(!plugin.Dead.contains(ttwentythree.getName())){
								World world = ttwentythree.getLocation().getWorld();
								world.createExplosion(ttwentythree.getLocation(), 0.0F, false);
								ttwentythree.setHealth(0);
							}
						}
					}
				}
				if(plugin.Playing.size()==24){
					String twentyfour = plugin.Playing.get(23);
					if(pname==twentyfour){
						Player ttwentyfour = plugin.getServer().getPlayerExact(twentyfour);
						if(!ttwentyfour.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentyfourspwn)){
							if(!plugin.Dead.contains(ttwentyfour.getName())){
								World world = ttwentyfour.getLocation().getWorld();
								world.createExplosion(ttwentyfour.getLocation(), 0.0F, false);
								ttwentyfour.setHealth(0);
							}
						}
					}
				}
			}else{
				if(plugin.Playing.size()>=1){
					String one = plugin.Playing.get(0);
					if(pname==one){
						Player tone = plugin.getServer().getPlayerExact(one);
						if(!tone.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(onespwn)){
							tone.teleport(onespwn);
						}
					}
				}
				if(plugin.Playing.size()>=2){
					String two = plugin.Playing.get(1);
					if(pname==two){
						Player ttwo = plugin.getServer().getPlayerExact(two);
						if(!ttwo.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twospwn)){
							ttwo.teleport(twospwn);
						}
					}
				}
				if(plugin.Playing.size()>=3){
					String three = plugin.Playing.get(2);
					if(pname==three){
						Player tthree = plugin.getServer().getPlayerExact(three);
						if(!tthree.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(threespwn)){
							tthree.teleport(threespwn);
						}
					}
				}
				if(plugin.Playing.size()>=4){
					String four = plugin.Playing.get(3);
					if(pname==four){
						Player tfour = plugin.getServer().getPlayerExact(four);
						if(!tfour.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(fourspwn)){
							tfour.teleport(fourspwn);
						}
					}
				}
				if(plugin.Playing.size()>=5){
					String five = plugin.Playing.get(4);
					if(pname==five){
						Player tfive = plugin.getServer().getPlayerExact(five);
						if(!tfive.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(fivespwn)){
							tfive.teleport(fivespwn);
						}
					}
				}
				if(plugin.Playing.size()>=6){
					String six = plugin.Playing.get(5);
					if(pname==six){
						Player tsix = plugin.getServer().getPlayerExact(six);
						if(!tsix.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(sixspwn)){
							tsix.teleport(sixspwn);
						}
					}
				}
				if(plugin.Playing.size()>=7){
					String seven = plugin.Playing.get(6);
					if(pname==seven){
						Player tseven = plugin.getServer().getPlayerExact(seven);
						if(!tseven.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(sevenspwn)){
							tseven.teleport(sevenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=8){
					String eight = plugin.Playing.get(7);
					if(pname==eight){
						Player teight = plugin.getServer().getPlayerExact(eight);
						if(!teight.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(eightspwn)){
							teight.teleport(eightspwn);
						}
					}
				}
				if(plugin.Playing.size()>=9){
					String nine = plugin.Playing.get(8);
					if(pname==nine){
						Player tnine = plugin.getServer().getPlayerExact(nine);
						if(!tnine.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(ninespwn)){
							tnine.teleport(ninespwn);
						}
					}
				}
				if(plugin.Playing.size()>=10){
					String ten = plugin.Playing.get(9);
					if(pname==ten){
						Player tten = plugin.getServer().getPlayerExact(ten);
						if(!tten.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(tenspwn)){
							tten.teleport(tenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=11){
					String eleven = plugin.Playing.get(10);
					if(pname==eleven){
						Player televen = plugin.getServer().getPlayerExact(eleven);
						if(!televen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(elevenspwn)){
							televen.teleport(elevenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=12){
					String twelve = plugin.Playing.get(11);
					if(pname==twelve){
						Player ttwelve = plugin.getServer().getPlayerExact(twelve);
						if(!ttwelve.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twelvespwn)){
							ttwelve.teleport(twelvespwn);
						}
					}
				}
				if(plugin.Playing.size()>=13){
					String thirteen = plugin.Playing.get(12);
					if(pname==thirteen){
						Player tthirteen = plugin.getServer().getPlayerExact(thirteen);
						if(!tthirteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(thirteenspwn)){
							tthirteen.teleport(thirteenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=14){
					String fourteen = plugin.Playing.get(13);
					if(pname==fourteen){
						Player tfourteen = plugin.getServer().getPlayerExact(fourteen);
						if(!tfourteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(fourteenspwn)){
							tfourteen.teleport(fourteenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=15){
					String fifteen = plugin.Playing.get(14);
					if(pname==fifteen){
						Player tfifteen = plugin.getServer().getPlayerExact(fifteen);
						if(!tfifteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(fifteenspwn)){
							tfifteen.teleport(fifteenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=16){
					String sixteen = plugin.Playing.get(15);
					if(pname==sixteen){
						Player tsixteen = plugin.getServer().getPlayerExact(sixteen);
						if(!tsixteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(sixteenspwn)){
							tsixteen.teleport(sixteenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=17){
					String seventeen = plugin.Playing.get(16);
					if(pname==seventeen){
						Player tseventeen = plugin.getServer().getPlayerExact(seventeen);
						if(!tseventeen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(seventeenspwn)){
							tseventeen.teleport(seventeenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=18){
					String eighteen = plugin.Playing.get(17);
					if(pname==eighteen){
						Player teighteen = plugin.getServer().getPlayerExact(eighteen);
						if(!teighteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(eighteenspwn)){
							teighteen.teleport(eighteenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=19){
					String nineteen = plugin.Playing.get(18);
					if(pname==nineteen){
						Player tnineteen = plugin.getServer().getPlayerExact(nineteen);
						if(!tnineteen.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(nineteenspwn)){
							tnineteen.teleport(nineteenspwn);
						}
					}
				}
				if(plugin.Playing.size()>=20){
					String twenty = plugin.Playing.get(19);
					if(pname==twenty){
						Player ttwenty = plugin.getServer().getPlayerExact(twenty);
						if(!ttwenty.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentyspwn)){
							ttwenty.teleport(twentyspwn);
						}
					}
				}
				if(plugin.Playing.size()>=21){
					String twentyone = plugin.Playing.get(20);
					if(pname==twentyone){
						Player ttwentyone = plugin.getServer().getPlayerExact(twentyone);
						if(!ttwentyone.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentyonespwn)){
							ttwentyone.teleport(twentyonespwn);
						}
					}
				}
				if(plugin.Playing.size()>=22){
					String twentytwo = plugin.Playing.get(21);
					if(pname==twentytwo){
						Player ttwentytwo = plugin.getServer().getPlayerExact(twentytwo);
						if(!ttwentytwo.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentytwospwn)){
							ttwentytwo.teleport(twentytwospwn);
						}
					}
				}
				if(plugin.Playing.size()>=23){
					String twentythree = plugin.Playing.get(22);
					if(pname==twentythree){
						Player ttwentythree = plugin.getServer().getPlayerExact(twentythree);
						if(!ttwentythree.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentythreespwn)){
							ttwentythree.teleport(twentythreespwn);
						}
					}
				}
				if(plugin.Playing.size()==24){
					String twentyfour = plugin.Playing.get(23);
					if(pname==twentyfour){
						Player ttwentyfour = plugin.getServer().getPlayerExact(twentyfour);
						if(!ttwentyfour.getLocation().getBlock().getRelative(BlockFace.DOWN).equals(twentyfourspwn)){
							ttwentyfour.teleport(twentyfourspwn);
						}
					}
				}
			}
		}
	}
}
