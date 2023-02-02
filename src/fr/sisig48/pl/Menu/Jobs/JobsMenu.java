package fr.sisig48.pl.Menu.Jobs;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Item;
import net.ess3.api.MaxMoneyException;

public class JobsMenu {
	public static ArrayList<Inventory> JobsInventory = new ArrayList<Inventory>();
	private static ArrayList<String> JobsPaper = new ArrayList<String>();
	public static void OpenJobsMenu(Player player) {
		Inventory e = Bukkit.createInventory(player, 27, "Jobs Office");
		player.openInventory(e);
		JobsInventory.add(e);
		PlayerJobs pj = new PlayerJobs(player);
		ItemStack it;
		String[] lore = {"Votre métier est : §4" + pj.get().getJobs().getName(), "Votre xp : §4" + String.valueOf(pj.getXp())};
		it = Item.GiveOwnsPlayerHead(1, "§aMon métier", lore, player.getName(), 125);
		e.setItem(11, it);

		it = Item.GiveItem(Material.IRON_PICKAXE, 1, "§aChanger de métier", "Changé mon métier", 129);
		ItemMeta meta = it.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		it.setItemMeta(meta);
		
		e.setItem(13, it);
		
		it = Item.GiveItem(Material.GOLDEN_APPLE, 1, "Autre Joueur", "Afficher les métier des autre joueur", 129);
		e.setItem(15, it);
		
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);	
	}
	
	public static void OpenUnitChangeJobsMenu(Player player, Jobs job, int i) {
		Inventory e = Bukkit.createInventory(player, 27, job.getName());
		player.openInventory(e);
		JobsInventory.add(e);
		//PlayerJobs pj = new PlayerJobs(player);
		ItemStack it = null;
		try {
			if(Economy.getMoneyExact(player.getUniqueId()).doubleValue() >= Double.valueOf(job.getPrice() - 1)) it = Item.GiveItem(Material.GREEN_WOOL, 1, "§aAccepter §6Prix : " + job.getPrice(), "§4§lAttention l'xp actuele sera suprimé", i);
			else it = Item.GiveItem(Material.GREEN_WOOL, 1, "§aAccepter §4§lPrix : " + job.getPrice() + ", §4vous n'avez pas assez", "§4§lAttention l'xp actuele sera suprimé", 125);
			if(new PlayerJobs(player).get() == job) it = Item.GiveItem(Material.ORANGE_WOOL, 1, "§aAccepter §4§lPrix : " + job.getPrice() + ", §6c'est déja votre métier", "§4§lAction imposible", 125);;
		} catch (UserDoesNotExistException e1) {
			e1.printStackTrace();
		}
		e.setItem(11, it);
		
		it = Item.GiveItem(Material.RED_WOOL, 1, "§4Refuser", "Retour en arière", i);
		e.setItem(15, it);
		
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);	
	}
	@SuppressWarnings("deprecation")
	public static void OpenChangeJobsMenu(Player player) {
		int lenth = 0;
		for(Jobs j : Jobs.All) {
			if(!j.isEnable()) break;
			lenth++;
		}
		double a = Double.valueOf(lenth / 9.0);
		BigDecimal bd = new BigDecimal(a);
		bd = bd.setScale(0, BigDecimal.ROUND_UP);
		int in = (bd.intValue()*9) + 18;
		Inventory e = Bukkit.createInventory(player, in, "Jobs Office");
		player.openInventory(e);
		JobsInventory.add(e);
		
		PlayerJobs jobs = new PlayerJobs(player);
		ItemStack it;
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(in - 9, it); e.setItem(in - 8, it); e.setItem(in - 7, it); e.setItem(in - 6, it); e.setItem(in - 5, it); e.setItem(in - 4, it); e.setItem(in - 3, it); e.setItem(in - 2, it); e.setItem(in - 1, it);
		int i = 8;
		for(Jobs j : Jobs.All) {
			if(j.isEnable()) {
				i++;
				JobsPaper.add(String.valueOf(j.getJobs() + "/" + (120 + i)));
				if(j.getJobs() == jobs.get().getJobs()) {
					
					it = Item.GiveItem(Material.PAPER, 1, "§a" + j.getName() , null, (120 + i));
					ItemMeta meta = it.getItemMeta();
					meta.addEnchant(Enchantment.LOYALTY, 1, false);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					it.setItemMeta(meta);
					e.setItem(i, it);
				} else {
					it = Item.GiveItem(Material.PAPER, 1, "§a" + j.getName() , null, (120 + i));
					e.setItem(i, it);
				}
			}
		}

		
	}
	
	@SuppressWarnings("incomplete-switch")
	public static boolean TcheckJobsMenuAction(Player player, ItemStack current) {
		
			switch(current.getType()) {
			case IRON_PICKAXE :
				if(current.getItemMeta().getCustomModelData() == 129) {
					OpenChangeJobsMenu(player);
					return true;
				}
			case GOLDEN_APPLE :
				if(current.getItemMeta().getCustomModelData() == 129) {
					return true;
				}
			case PAPER :
				if(current.getItemMeta().getCustomModelData() > 128 && current.getItemMeta().getCustomModelData() < 151) {
					for(String t : JobsPaper) {
						String[] ts = t.split("\\/");
						if(current.getItemMeta().getCustomModelData() == Integer.valueOf(ts[1])) OpenUnitChangeJobsMenu(player, Jobs.valueOf(ts[0]), Integer.valueOf(ts[1]));
					}
					return true;
				}
			case GREEN_WOOL :
				if(current.getItemMeta().getCustomModelData() > 128 && current.getItemMeta().getCustomModelData() < 151) {
					for(String t : JobsPaper) {
						String[] ts = t.split("\\/");
						if(current.getItemMeta().getCustomModelData() == Integer.valueOf(ts[1])) {
							PlayerJobs pj = new PlayerJobs(player);
							pj.add(Jobs.valueOf(ts[0]));
							pj.close();
							try {
								Economy.subtract(player.getUniqueId(), BigDecimal.valueOf(pj.get().getPrice()));
							} catch (NoLoanPermittedException | ArithmeticException | UserDoesNotExistException
									| MaxMoneyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							OpenChangeJobsMenu(player);
						}
					}
					return true;
				}
			case RED_WOOL :
				if(current.getItemMeta().getCustomModelData() > 128 && current.getItemMeta().getCustomModelData() < 151) {
					for(String t : JobsPaper) {
						String[] ts = t.split("\\/");
						if(current.getItemMeta().getCustomModelData() == Integer.valueOf(ts[1])) OpenChangeJobsMenu(player);
					}
					return true;
				}
			}
		
		return false;
		
		
	}
}
