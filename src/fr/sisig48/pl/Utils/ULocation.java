package fr.sisig48.pl.Utils;

//import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ULocation {
	public static Location StringToLocation(String[] location) {
		String[] e = location;
		double x = Double.valueOf(e[0]);
		double y = Double.valueOf(e[1]);
		double z = Double.valueOf(e[2]);
		float s;
		float a;
		if (!(e[4] == null)) {
			s = Float.valueOf(e[4]);
		} else {
			s = 0;
		}
		
		if (!(e[5] == null)) {
			a = Float.valueOf(e[5]);
		} else {
			a = 0;
		}
		
		Location loc = new Location(Bukkit.getWorld(e[3]), x, y, z, s, a);
		/*System.out.println(Arrays.asList(Bukkit.getWorlds()) + " " + Bukkit.getWorld(e[3]));
		System.out.println(loc.toString());
		System.out.println(e[0] + e[1] + e[2] + e[3]);*/
		return loc;
	}
	
	public static Location ToLocation(double x, double y, double z, String w) {
		Location loc = new Location(Bukkit.getWorld(w), x, y, z);
		return loc;
	}
}
