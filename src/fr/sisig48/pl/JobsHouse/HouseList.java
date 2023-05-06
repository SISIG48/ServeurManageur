package fr.sisig48.pl.JobsHouse;

import java.util.ArrayList;

import org.bukkit.Location;

public class HouseList {
	private static ArrayList<HouseListInfo> house = new ArrayList<HouseListInfo>();
	public HouseList(Location loc) {
		house.add(new HouseListInfo(loc, true));
	}
	
	static HouseListInfo getHouseByLocation(Location loc) {
		for(HouseListInfo t : house) if(t.getLocation().equals(loc)) return t;
		return null;
	}
	
	static ArrayList<HouseListInfo> getAllHouse() {
		return house;
	}
}

class HouseListInfo {
	private static int num = 0;
	private Location location;
	private boolean isEnable;
	public HouseListInfo(Location loc, boolean enable) {
		num++;
		location = loc;
		isEnable = enable;
	}
	
	public int getSlot() {
		return num;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public boolean isEnable() {
		return isEnable;
	}
		
	public void isEnable(boolean enable) {
		isEnable = enable;
	}
}
