package fr.sisig48.pl.JobsHouse;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.sisig48.pl.Sociale.Jobs;

public class MainHouse {
	public static void init() {
		for(Jobs j : Jobs.values()) new HouseData(j);
		HouseData.addLoc(new Location(Bukkit.getWorld("world"), 0, 100, 0));
	}
}
