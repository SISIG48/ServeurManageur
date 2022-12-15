package fr.sisig48.pl.Economie;


import org.bukkit.entity.Player;

import com.earth2me.essentials.api.Economy;








public class EconomieESS {


	public static double Ballance;
	
	
	public static boolean HasEnought(Player player, double price) throws Exception {
		
		double Money = getMoney(player);
		if(Money >= price) return true;
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static double getMoney(Player player) throws Exception {
		if(player.getName() == null) return 9999;
		return Economy.getMoney(player.getName());
	} 
	
}
