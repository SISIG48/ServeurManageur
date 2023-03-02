package fr.sisig48.pl.Economie;


import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Utils.Uconfig;

public class EconomieESS {


	//private static double Ballance;
	public static boolean HasEnought(Player player, double price) throws Exception {
		
		double Money = getMoney(player);
		if(Money >= price) return true;
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static double getMoney(Player player) throws Exception {
		if(player.getName() == null) return -1;
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Resquet to com.earth2me.essentials.api.Economy <get money> Respond : " + String.valueOf(Economy.getMoney(player.getName())));
		return Economy.getMoney(player.getName());
	}

	@SuppressWarnings("deprecation")
	public static double getMoney(OfflinePlayer e) throws UserDoesNotExistException {
		if(e.getName() == null) return -1;
		logs.add("Player : UUID : " + e.getUniqueId() + " | Name :" + e.getName() + " Resquet to com.earth2me.essentials.api.Economy <get money> [Offline player] Respond : " + String.valueOf(Economy.getMoney(e.getName())));
		return Economy.getMoney(e.getName());		
	}
	
	public static int DoTaxe(int price) {
		int t = price * 10 / 100;
		int a = Integer.valueOf(Uconfig.getConfig("bank.public")) + t;
		Uconfig.setConfig("bank.public", String.valueOf(a));
		return t * 9;
	}
	
	public static void GiveTaxe(int price) {
		int a = Integer.valueOf(Uconfig.getConfig("bank.public")) + price;
		Uconfig.setConfig("bank.public", String.valueOf(a));
	}
	
	public static void SubTaxe(int price) {
		int a = Integer.valueOf(Uconfig.getConfig("bank.public")) - price;
		Uconfig.setConfig("bank.public", String.valueOf(a));
	}
	
	public static boolean haveEnoughTaxe(int price) {
		int a = Integer.valueOf(Uconfig.getConfig("bank.public")) - price;
		if(a < 0) return false;
		else return true;
	}
	
	public static int getTaxeMoney(int price) {
		return Integer.valueOf(Uconfig.getConfig("bank.public"));
	}
	
	
}
