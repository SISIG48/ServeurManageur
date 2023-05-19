package fr.sisig48.pl.JobsHouse;

import java.util.ArrayList;

import org.bukkit.Location;

public class HouseList {
	private static ArrayList<HouseListInfo> house = new ArrayList<HouseListInfo>();
	public HouseList(Location loc) {
		house.add(new HouseListInfo(loc, true));
	}
	
	public static HouseListInfo getHouseByLocation(Location loc) {
		for(HouseListInfo t : house) if(t.getLocation().equals(loc)) return t;
		return null;
	}
	
	public static HouseListInfo getHouseBySlot(int i) {
		for(HouseListInfo t : house) if(t.getSlot() == i) return t;
		return null;
	}
	
	static ArrayList<HouseListInfo> getAllHouse() {
		return house;
	}

	public static int getSlots() {
		return HouseListInfo.getSlots();
	}
}

