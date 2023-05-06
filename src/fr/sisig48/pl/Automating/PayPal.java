package fr.sisig48.pl.Automating;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Uconfig;
import net.ess3.api.MaxMoneyException;

public class PayPal {
	private static ArrayList<String> listInstance = new ArrayList<String>();
	private static int c = 0;
	public PayPal(Player player) {
		if(c != 0) listInstance.add(String.valueOf(player.getUniqueId() + " " + (c - 1)));
		else listInstance.add(String.valueOf(player.getUniqueId() + " " + 60));
	}
	private static long Actu = Long.valueOf(Uconfig.getConfig("IntervalePayment"))*1000;
	public static Thread thread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				Thread.sleep(5000);
				Bukkit.getConsoleSender().sendMessage("§6Strating Payment §8each§l§8 " + String.valueOf(Actu/1000) + "§8 minute");
				while(true) {
					for(c = 0; c != 60; c++) {
						for(Object e0 : listInstance.toArray()) {
							String e = String.valueOf(e0);
							String[] t = e.split(" ");
							if(t.length == 2 && t[1].equals(String.valueOf(c))) {
								Player p = Bukkit.getPlayer(UUID.fromString(t[0]));
								if(!p.isOnline()) listInstance.remove(e);
								BigDecimal pay = new PlayerJobs(p).getPay();
								if(pay == BigDecimal.valueOf(0)) break;
								Economy.add(p.getUniqueId(), pay);
								Bukkit.getConsoleSender().sendMessage("§aPay : " + pay);
								logs.add(Thread.currentThread().getName() + " Pay : §l§e" + pay);
								p.sendMessage("§aVous avez était payé (§l§e" + pay + "§a)");
							}
						}
						Thread.sleep(Actu);
					}
				}
			} catch (InterruptedException | NoLoanPermittedException | ArithmeticException | UserDoesNotExistException | MaxMoneyException e) {}

			
		}
	}, "Thread - Auto Pay");
	
	
	
	
	public static void clear(Player player) {
		for(String t : listInstance) if(Bukkit.getPlayer(UUID.fromString(t.split(" ")[0])).equals(player)) listInstance.remove(t);
	}
}
