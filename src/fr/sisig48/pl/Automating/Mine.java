package fr.sisig48.pl.Automating;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.State.Spawn;
import fr.sisig48.pl.Utils.Uconfig;

public class Mine extends Uconfig {
	public Mine(Main main) {
		super(main);
	}
	public static Thread AutoFill = new Thread(new Runnable() {
		
		
		@Override
		public void run() {
			String i = "0";
			isChange = true;
			try {
				
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while(true) {
				FillMine();
				
				try {
					i = Uconfig.getConfig("location.mine.zone.refreshTimeMinute");
					if(i == null) {
						Uconfig.setConfig("location.mine.zone.refreshTimeMinute", "30");
						i = "30";
					}
					
					
					Thread.sleep(Integer.valueOf(String.valueOf((int) (Double.valueOf(i) * 60000.0))));
				} catch (InterruptedException e) {}
			}
		}
	}, "AutoMineFill Thread");
	static Main Plug = null;

	public static void SetFillMineAt(Player player) {
		Uconfig.setConfig("location.mine.zone.w", String.valueOf(player.getLocation().getWorld().getName()));
		Uconfig.setConfig("location.mine.zone.at.x", String.valueOf((int) player.getLocation().getX()));
		Uconfig.setConfig("location.mine.zone.at.y", String.valueOf((int) player.getLocation().getY()));
		Uconfig.setConfig("location.mine.zone.at.z", String.valueOf((int) player.getLocation().getZ()));
		isChange = true;
		
	}
	public static void SetFillMineTo(Player player) {
		Uconfig.setConfig("location.mine.zone.to.x", String.valueOf((int) player.getLocation().getX()));
		Uconfig.setConfig("location.mine.zone.to.y", String.valueOf((int) player.getLocation().getY()));
		Uconfig.setConfig("location.mine.zone.to.z", String.valueOf((int) player.getLocation().getZ()));
		isChange = true;
	}
	public static boolean isChange = true;
	public static String x,y,z,x2,y2,z2;
	public static World world;
	@SuppressWarnings("static-access")
	public static void FillMine() {
		Bukkit.getConsoleSender().sendMessage("§6La mine se renove");
		Plug = main.getPlugin(main.getClass());
		Bukkit.getScheduler().runTask(Plug, () -> {
			if(isChange) {
				x = Uconfig.getConfig("location.mine.zone.at.x");
				y = Uconfig.getConfig("location.mine.zone.at.y");
				z = Uconfig.getConfig("location.mine.zone.at.z");
				world = Bukkit.getWorld(Uconfig.getConfig("location.mine.zone.w"));
				x2 = Uconfig.getConfig("location.mine.zone.to.x");
				y2 = Uconfig.getConfig("location.mine.zone.to.y");
				z2 = Uconfig.getConfig("location.mine.zone.to.z");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/world " + world.getName());
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos1 " + x + "," + y +"," + z);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/pos2 " + x2 + "," + y2 +"," + z2);
				isChange = false;
				String t;
				if(Integer.valueOf(x) < Integer.valueOf(x2)) {
					t = x;
					x = x2;
					x2 = t;
				}
				if(Integer.valueOf(y) < Integer.valueOf(y2)) {
					t = y;
					y = y2;
					y2 = t;
				}
				if(Integer.valueOf(z) < Integer.valueOf(z2)) {
					t = z;
					z = z2;
					z2 = t;
				}
			}
			clearPlayerMine();
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/set 29.5%stone,20%iron_ore,10%gold_ore,20%coal_ore,10%redstone_ore,10%lapis_ore,0.5%diamond_ore");
			Bukkit.getConsoleSender().sendMessage("§aLa mine est prete");
		});
	}
	public static void clearPlayerMine() {
		for(Player p : world.getPlayers()) {
			Location loc = p.getLocation();
			int px = (int) loc.getX();
			int py = (int) loc.getY();
			int pz = (int) loc.getZ();
			try {
				if((Integer.valueOf(x) >= px && Integer.valueOf(y) >= py && Integer.valueOf(z) >= pz) && (Integer.valueOf(x2) <= px && Integer.valueOf(y2) <= py && Integer.valueOf(z2) <= pz)) p.teleport(Spawn.GetMineInSpawnLocation());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
}
