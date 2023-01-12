package fr.sisig48.pl.Economie;


import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.UserDoesNotExistException;








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

	@SuppressWarnings("deprecation")
	public static double getMoney(OfflinePlayer e) throws UserDoesNotExistException {
		if(e.getName() == null) return 9999;
		return Economy.getMoney(e.getName());		
	} 
	
}
