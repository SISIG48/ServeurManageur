package fr.sisig48.pl.Automating;

import java.math.BigDecimal;
import java.util.ArrayList;

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
	private static ArrayList<Thread> listThread = new ArrayList<Thread>();
	private static ArrayList<PayPal> listInstance = new ArrayList<PayPal>();
	private Player player;
	public PayPal(Player player) {
		listInstance.add(this);
		this.player = player;
		Pj = new PlayerJobs(player);
		thread = new Thread(new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					Bukkit.getConsoleSender().sendMessage("§6Strating Payment §8each§l§8 " + (Actu/60000) + "§8 minute");
					while(true) {
						Thread.sleep(Actu);
						if(!player.isOnline()) thread.stop();
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
		listThread.add(thread);
	}
	private PlayerJobs Pj;
	private static long Actu = Long.valueOf(Uconfig.getConfig("IntervalePayment"))*60000;
	private Thread thread;
	
	@SuppressWarnings("deprecation")
	public void close() {
		listThread.remove(thread);
		listInstance.remove(this);
		thread.stop();
	}
	
	@SuppressWarnings("deprecation")
	public static void stop() {
		for(Thread t : listThread) t.stop();
		listInstance.clear();
	}
	
	public static PayPal get(Player player) {
		for(PayPal t : listInstance) if(t.player.equals(player)) return t;
		return new PayPal(player);
	}
}
