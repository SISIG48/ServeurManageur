package fr.sisig48.pl.JobsHouse;

import org.bukkit.Location;

public class HouseListInfo {
	private static int num = 0;
	private Location location;
	private boolean isEnable;
	private int slot;
	public HouseListInfo(Location loc, boolean enable) {
		num++;
		slot = num;
		location = loc;
		isEnable = enable;
	}
	
	public int getSlot() {
		return slot;
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
	
	public static int getSlots() {
		return num;
	}
	
}