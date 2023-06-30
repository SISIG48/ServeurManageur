package fr.sisig48.pl.State;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.entity.Villager.Type;

import fr.sisig48.pl.Utils.ULocation;
import fr.sisig48.pl.Utils.Uconfig;

public class ShopPNJ {
	private static ArrayList<UUID> uuids = new ArrayList<UUID>();
	private static Type type = Type.SNOW;
	private static Profession proffesion = Villager.Profession.CARTOGRAPHER;
	public static Location getLoc(int n) {
		String x = Uconfig.getConfig("location.pnj.shop." + n + ".x");
		String y = Uconfig.getConfig("location.pnj.shop." + n + ".y");
		String z = Uconfig.getConfig("location.pnj.shop." + n + ".z");
		String w = Uconfig.getConfig("location.pnj.shop." + n + ".w");
		String ya = Uconfig.getConfig("location.pnj.shop." + n + ".yaw");
		String[] loc = {x,y,z,w,ya,null};
		return ULocation.StringToLocation(loc);
	}
	
	protected static void setLoc(String x, String y, String z, String w, String yaw, int n) {
		Uconfig.setConfig("location.pnj.shop." + n + ".x", x);
		Uconfig.setConfig("location.pnj.shop." + n + ".y", y);
		Uconfig.setConfig("location.pnj.shop." + n + ".z", z);
		Uconfig.setConfig("location.pnj.shop." + n + ".w", w);
		Uconfig.setConfig("location.pnj.shop." + n + ".yaw", yaw);
	}
	
	protected static void setLoc(Location loc, int n) {
		Uconfig.setConfig("location.pnj.shop." + n + ".x", String.valueOf(loc.getX()));
		Uconfig.setConfig("location.pnj.shop." + n + ".y", String.valueOf(loc.getY()));
		Uconfig.setConfig("location.pnj.shop." + n + ".z", String.valueOf(loc.getZ()));
		Uconfig.setConfig("location.pnj.shop." + n + ".w", String.valueOf(loc.getWorld().getName()));
		Uconfig.setConfig("location.pnj.shop." + n + ".yaw", String.valueOf(loc.getYaw()));
	}
	
	public static void addUUID(UUID uuid) {
		uuids.add(uuid);
	}
	
	public static ArrayList<UUID> getUUIDS() {
		return uuids;
	}
	
	public static void SetType(Entity e) {
		Villager v = (Villager) e;
		v.setVillagerType(type);
		v.setProfession(proffesion);
	}
}
