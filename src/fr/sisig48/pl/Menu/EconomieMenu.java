package fr.sisig48.pl.Menu;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Economie.EconomieESS;
import fr.sisig48.pl.Utils.Item;





public class EconomieMenu {
	
	public static Inventory inventory;
	public static Player player;
	public static ItemStack current;
	
	public static void OpenMenuEco(Player player) throws Exception {
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Enter to MenuEco");
		Inventory e = Bukkit.createInventory(player, 27, "Economie");
		player.openInventory(e);
		Interface.inventory.add(e);
		
		
		//Economie personelle
		ItemStack it;
		it = Item.GiveOwnsPlayerHead(1, "Personelle", null, player.getName(), 124);
		e.setItem(11, it);
		it = Item.GiveItem(Material.GOLDEN_APPLE, 1, "§eClassement", "Obtener les diférent classement du serveur");
		e.setItem(15, it);
		
		
		//Verre exterieur
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);
	}
	
	
	
	public static void OpenMenuEcoPerso(Player player) throws Exception {
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Enter to MenuEcoPerso");
		Inventory e = Bukkit.createInventory(player, 27, "Economie personelle");
		player.openInventory(e);
		Interface.inventory.add(e);
		
		//Economie personelle
		ItemStack it;
		it = Item.GiveItem(Material.ENDER_CHEST, 1, "§6Compte en banque", "Vous avez §4" + EconomieESS.getMoney(player) + "$", 125);
		e.setItem(11, it);
		
		
		//Verre exterieur
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);
	}
	
	public static void OpenMenuEcoPublic(Player player) throws Exception {
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Enter to MenuEcoPublic");
		Inventory e = Bukkit.createInventory(player, 27, "Economie publique");
		player.openInventory(e);
		Interface.inventory.add(e);
		
		//Economie public
		ItemStack it;
		it = Item.GiveItem(Material.ENDER_CHEST, 1, "§6Argent", "Découvré le joueur avec le plus d'argent", 123);
		e.setItem(11, it);
		
		
		//Verre exterieur
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);
	}
	
	public static void OpenMenuEcoPublicMoney(Player player) throws Exception{
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Enter to MenuEcoPublicMoney");
		int first = 0;
		int second = 0;
		int three = 0;
		String firstNAME = "noob";
		String secondNAME = "noob";
		String threeNAME = "noob";
		/*
		for (Player e : Bukkit.getOnlinePlayers()) {
			double mo = EconomieESS.getMoney(e);
			if (mo > three) {
				if (mo > second) {
					if (mo > first) {
						three = second;
						threeNAME = secondNAME;
						second = first;
						secondNAME = firstNAME;
						first = (int) mo ;
						firstNAME =e.getCustomName();
					} else {
						three = second;
						threeNAME = secondNAME;
						second = (int) mo;
						secondNAME = e.getCustomName();
					}
				} else {
					three = (int) mo;
					threeNAME = e.getCustomName();
				}
			}
		}
		*/
		///give @p skull 1 3 {display:{Name:"1"},SkullOwner:{Id:"00684a88-5cc8-4713-9e91-7b1906",Properties:{textures:[{Value:"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFiYzJiY2ZiMmJkMzc1OWU2YjFlODZmYzdhNzk1ODVlMTEyN2RkMzU3ZmMyMDI4OTNmOWRlMjQxYmM5ZTUzMCJ9fX0="}]}}}
		for (OfflinePlayer e : Bukkit.getOfflinePlayers()) {
			if (threeNAME == e.getName() || secondNAME == e.getName() || firstNAME == e.getName()) break;
			double mo = EconomieESS.getMoney(e);
			if (mo >= three) {
				if (mo >= second) {
					if (mo >= first) {
						three = second;
						threeNAME = secondNAME;
						second = first;
						secondNAME = firstNAME;
						first = (int) mo ;
						firstNAME =e.getName();
					} else {
						three = second;
						threeNAME = secondNAME;
						second = (int) mo;
						secondNAME = e.getName();
					}
				} else {
					three = (int) mo;
					threeNAME = e.getName();
				}
			}
			
			
		}
		
		String[] name = {firstNAME, secondNAME, threeNAME};
		int[] money = {first, second, three};
		
		Inventory e = Bukkit.createInventory(player, 27, "Economie publique - Classement");
		player.openInventory(e);
		Interface.inventory.add(e);
		//Economie public
		ItemStack it;
		it = Item.GiveOwnsPlayerHead(1, "§e#1 §f- §a"+ name[0] + " §f- §4" + money[0], "C'est le joueur qui a le plus d'argent", name[0], 125);
		e.setItem(11, it);
		it = Item.GiveOwnsPlayerHead(1, "§e#2 §f- §a"+ name[1] + " §f- §4" + money[1], "C'est le deuxième joueur qui a le plus d'argent", name[1], 125);
		e.setItem(13, it);
		it = Item.GiveOwnsPlayerHead(1, "§e#3 §f- §a"+ name[2] + " §f- §4" + money[2], "C'est le troisième joueur qui a le plus d'argent", name[2], 125);
		e.setItem(15, it);
		//Verre exterieur
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);
		return;
		
		
		
	}

	
	
	
}

