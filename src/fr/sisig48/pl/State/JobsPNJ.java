package fr.sisig48.pl.State;

import org.bukkit.Location;

import fr.sisig48.pl.Utils.ULocation;
import fr.sisig48.pl.Utils.Uconfig;

public class JobsPNJ {
	public static Location getLoc() {
		String x = Uconfig.getConfig("location.pnj.jobs.x");
		String y = Uconfig.getConfig("location.pnj.jobs.y");
		String z = Uconfig.getConfig("location.pnj.jobs.z");
		String w = Uconfig.getConfig("location.pnj.jobs.w");
		String ya = Uconfig.getConfig("location.pnj.jobs.yaw");
		String[] loc = {x,y,z,w,ya,null};
		return ULocation.StringToLocation(loc);
	}
	
	public static void setLoc(String x, String y, String z, String w, String yaw) {
		Uconfig.setConfig("location.pnj.jobs.x", x);
		Uconfig.setConfig("location.pnj.jobs.y", y);
		Uconfig.setConfig("location.pnj.jobs.z", z);
		Uconfig.setConfig("location.pnj.jobs.w", w);
		Uconfig.setConfig("location.pnj.jobs.yaw", yaw);
	}
	public static void setLoc(Location loc) {
		Uconfig.setConfig("location.pnj.jobs.x", String.valueOf(loc.getX()));
		Uconfig.setConfig("location.pnj.jobs.y", String.valueOf(loc.getY()));
		Uconfig.setConfig("location.pnj.jobs.z", String.valueOf(loc.getZ()));
		Uconfig.setConfig("location.pnj.jobs.w", String.valueOf(loc.getWorld().getName()));
		Uconfig.setConfig("location.pnj.jobs.yaw", String.valueOf(loc.getYaw()));
	}
}
