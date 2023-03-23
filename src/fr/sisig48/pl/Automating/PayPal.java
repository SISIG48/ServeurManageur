package fr.sisig48.pl.Automating;

import java.math.BigDecimal;

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
	@SuppressWarnings("unused")
	private Player player;
	public PayPal(Player player) {
		this.player = player;
		Pj = new PlayerJobs(player);
		thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					Bukkit.getConsoleSender().sendMessage("§6Strating Payment");
					while(true) {
						Thread.sleep(Actu);
						BigDecimal pay = Pj.getPay();
						if(pay == BigDecimal.valueOf(0)) break;
						Economy.add(player.getUniqueId(), pay);
						Bukkit.getConsoleSender().sendMessage("§aPay : " + pay);
						logs.add(Thread.currentThread().getName() + " Pay : §l§e" + pay);
						player.sendMessage("§aVous avez était payé (§l§e" + pay + "§a)");
					}
				} catch (InterruptedException | NoLoanPermittedException | ArithmeticException | UserDoesNotExistException | MaxMoneyException e) {}

				
			}
		}, "Thread - Auto Pay of : " + player.getUniqueId());
		thread.start();
	}
	private PlayerJobs Pj;
	private static long Actu = Long.valueOf(Uconfig.getConfig("IntervalePayment"))*60000;
	private Thread thread;
	
	@SuppressWarnings("deprecation")
	public void close() {
		thread.stop();
	}
}
