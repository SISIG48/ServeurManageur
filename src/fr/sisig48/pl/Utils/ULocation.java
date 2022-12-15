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
		Location loc = new Location(Bukkit.getWorld(e[3]), x, y, z);
		/*System.out.println(Arrays.asList(Bukkit.getWorlds()) + " " + Bukkit.getWorld(e[3]));
		System.out.println(loc.toString());
		System.out.println(e[0] + e[1] + e[2] + e[3]);*/
		return loc;
	}
}
