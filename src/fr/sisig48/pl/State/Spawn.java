package fr.sisig48.pl.State;

import java.io.IOException;


import org.bukkit.Location;

import fr.sisig48.pl.Utils.ULocation;
import fr.sisig48.pl.Utils.Uconfig;


public class Spawn {

	public static Location GetSpawnLocation() throws IOException  {
		
		String x = Uconfig.getConfig("location.spawn.x");
		String y = Uconfig.getConfig("location.spawn.y");
		String z = Uconfig.getConfig("location.spawn.z");
		String w = Uconfig.getConfig("location.spawn.w");
		String ya = Uconfig.getConfig("location.spawn.ya");
		String pi = Uconfig.getConfig("location.spawn.pi");
		String[] loc = {x, y, z, w, ya, pi};
		
		return ULocation.StringToLocation(loc);
	}
	
	public static void SetSpawnLocation(String[] location) {
		Uconfig.setConfig("location.spawn.x", location[0]);
		Uconfig.setConfig("location.spawn.y", location[1]);
		Uconfig.setConfig("location.spawn.z", location[2]);
		Uconfig.setConfig("location.spawn.w", location[3]);
		Uconfig.setConfig("location.spawn.ya", location[4]);
		Uconfig.setConfig("location.spawn.pi", location[5]);
		return;
	}
	
	
	
	public static Location GetMineInSpawnLocation()  throws IOException {
		

		String x = Uconfig.getConfig("location.mine.in.x");
		String y = Uconfig.getConfig("location.mine.in.y");
		String z = Uconfig.getConfig("location.mine.in.z");
		String w = Uconfig.getConfig("location.mine.in.w");
		String ya = Uconfig.getConfig("location.mine.in.ya");
		String pi = Uconfig.getConfig("location.mine.in.pi");
		String[] loc = {x, y, z, w, ya, pi};
		
		return ULocation.StringToLocation(loc);
	}
	
	public static void SetMineInSpawnLocation(String[] location) {
		Uconfig.setConfig("location.mine.in.x", location[0]);
		Uconfig.setConfig("location.mine.in.y", location[1]);
		Uconfig.setConfig("location.mine.in.z", location[2]);
		Uconfig.setConfig("location.mine.in.w", location[3]);
		Uconfig.setConfig("location.mine.in.ya", location[4]);
		Uconfig.setConfig("location.mine.in.pi", location[5]);
		return;
	}
	
	
	
	public static Location GetMineOutSpawnLocation() throws IOException {
		
		String x = Uconfig.getConfig("location.mine.out.x");
		String y = Uconfig.getConfig("location.mine.out.y");
		String z = Uconfig.getConfig("location.mine.out.z");
		String w = Uconfig.getConfig("location.mine.out.w");
		String ya = Uconfig.getConfig("location.mine.out.ya");
		String pi = Uconfig.getConfig("location.mine.out.pi");
		String[] loc = {x, y, z, w, ya, pi};
		
		return ULocation.StringToLocation(loc);
	}
	public static void SetMineOutSpawnLocation(String[] location) {
		Uconfig.setConfig("location.mine.out.x", location[0]);
		Uconfig.setConfig("location.mine.out.y", location[1]);
		Uconfig.setConfig("location.mine.out.z", location[2]);
		Uconfig.setConfig("location.mine.out.w", location[3]);
		Uconfig.setConfig("location.mine.out.ya", location[4]);
		Uconfig.setConfig("location.mine.out.pi", location[5]);
		
		return;
	}
}
