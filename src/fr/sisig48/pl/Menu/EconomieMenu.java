package fr.sisig48.pl.Menu;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.api.Economy;

import fr.sisig48.pl.Economie.EconomieESS;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Utils.Item;





public class EconomieMenu {
	private static ArrayList<Inventory> inventory = new ArrayList<Inventory>();
	
	
	private static ItemStack classement = Item.GiveItem(Material.GOLDEN_APPLE, 1, "§eClassement", "Obtener les diférent classement du serveur");
	private static ItemStack personelle(Player player) {
		return Item.GiveOwnsPlayerHead(1, "Personelle", "null", player);
	}
	
	private static ItemStack amis(Player player) {
		ItemStack it;
		Friends pl = new Friends(player);
		it = Item.GiveItem(Material.BARRIER, 1, "§eAmis", "Economie avec vos amis");
		if(pl.get().size() > 0) it = Item.GiveOwnsPlayerHead(1, "§eAmis", "Economie avec vos amis", pl.get().get(Integer.valueOf((int) (Math.random() * (pl.get().size())))).getName());
		return it;
	}
	
	public static void OpenMenuEco(Player player) {
		player.closeInventory();
		Inventory e = Bukkit.createInventory(player, 27, "Economie");
		player.openInventory(e);
		inventory.add(e);
		
		//Economie personelle
		e.setItem(11, personelle(player));
		e.setItem(13, amis(player));
		e.setItem(15, classement);
		
		
		//Verre exterieur
		Item.GrayExGlass(e, 27);
	}
	
	
	
	public static void OpenMenuEcoPerso(Player player) throws Exception {
		player.closeInventory();
		Inventory e = Bukkit.createInventory(player, 27, "Economie personelle");
		player.openInventory(e);
		inventory.add(e);
		
		//Economie personelle
		e.setItem(11, Item.GiveItem(Material.ENDER_CHEST, 1, "§6Compte en banque", "Vous avez §4" + EconomieESS.getMoney(player)));
		Item.GrayExGlass(e, 27);
	}
	
	private static ItemStack argent = Item.GiveItem(Material.ENDER_CHEST, 1, "§6Argent", "Découvré le joueur avec le plus d'argent");
	public static void OpenMenuEcoPublic(Player player) {
		player.closeInventory();
		Inventory e = Bukkit.createInventory(player, 27, "Economie publique");
		player.openInventory(e);
		inventory.add(e);
		
		//Economie public
		e.setItem(11, argent);
		
		
		Item.GrayExGlass(e, 27);
	}
	
	private static ItemStack argentAmis = Item.GiveItem(Material.ENDER_CHEST, 1, "§6Argent", "Découvré votre ami avec le plus d'argent");
	public static void OpenMenuEcoFriends(Player player) {
		player.closeInventory();
		Inventory e = Bukkit.createInventory(player, 27, "Economie d'amis");
		player.openInventory(e);
		inventory.add(e);
		
		//Economie amis
		e.setItem(11, argentAmis);
		
		
		Item.GrayExGlass(e, 27);
	}
	
	public static void OpenMenuEcoPublicMoney(Player player) throws Exception {
		player.closeInventory();
		int first = 0;
		int second = 0;
		int three = 0;
		String firstNAME = "noob";
		String secondNAME = "noob";
		String threeNAME = "noob";
		
		for (OfflinePlayer e : Bukkit.getOfflinePlayers()) {
			if(!Economy.playerExists(e.getUniqueId())) break;
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
		inventory.add(e);
		//Economie public
		ItemStack it;
		it = Item.GiveOwnsPlayerHead(1, "§e#1 §f- §a"+ name[0] + " §f- §4" + money[0], "C'est le joueur qui a le plus d'argent", name[0]);
		if(name[0].equalsIgnoreCase(player.getName())) it = Item.GiveOwnsPlayerHead(1, "§e#1 §f- §a"+ name[0] + " §8(MOI)" + " §f- §4" + money[0], "C'est le joueur qui a le plus d'argent", name[0]);
		e.setItem(11, it);
		it = Item.GiveOwnsPlayerHead(1, "§e#2 §f- §a"+ name[1] + " §f- §4" + money[1], "C'est le deuxième joueur qui a le plus d'argent", name[1]);
		if(name[1].equalsIgnoreCase(player.getName())) it = Item.GiveOwnsPlayerHead(1, "§e#2 §f- §a"+ name[1] + " §8(MOI)" + " §f- §4" + money[1], "C'est le deuxième joueur qui a le plus d'argent", name[1]);
		e.setItem(13, it);
		it = Item.GiveOwnsPlayerHead(1, "§e#3 §f- §a"+ name[2] + " §f- §4" + money[2], "C'est le troisième joueur qui a le plus d'argent", name[2]);
		if(name[1].equalsIgnoreCase(player.getName())) it = Item.GiveOwnsPlayerHead(1, "§e#3 §f- §a"+ name[2] + " §8(MOI)" + " §f- §4" + money[2], "C'est le troisieme joueur qui a le plus d'argent", name[2]);
		e.setItem(15, it);
		
		Item.GrayExGlass(e, 27);
		return;
		
		
		
	}
	
	public static void OpenMenuEcoFriendsMoney(Player player) throws Exception{
		player.closeInventory();

		int first = 0;
		int second = 0;
		int three = 0;
		String firstNAME = "noob";
		String secondNAME = "noob";
		String threeNAME = "noob";
		Friends pl = new Friends(player);
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
		for (OfflinePlayer e : pl.get()) {
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
		
		double mo = EconomieESS.getMoney(player);
		if (mo >= three) {
			if (mo >= second) {
				if (mo >= first) {
					three = second;
					threeNAME = secondNAME;
					second = first;
					secondNAME = firstNAME;
					first = (int) mo ;
					firstNAME = player.getName();
				} else {
					three = second;
					threeNAME = secondNAME;
					second = (int) mo;
					secondNAME = player.getName();
				}
			} else {
				three = (int) mo;
				threeNAME = player.getName();
			}
		}
		String[] name = {firstNAME, secondNAME, threeNAME};
		int[] money = {first, second, three};
		
		Inventory e = Bukkit.createInventory(player, 27, "Economie d'amis - Classement");
		player.openInventory(e);
		inventory.add(e);
		//Economie public
		ItemStack it;
		if(pl.get().size() >= 1) {
			it = Item.GiveOwnsPlayerHead(1, "§e#1 §f- §a"+ name[0] + " §f- §4" + money[0], "C'est le joueur qui a le plus d'argent", name[0]);
			if(name[0].equalsIgnoreCase(player.getName())) it = Item.GiveOwnsPlayerHead(1, "§e#1 §f- §a"+ name[0] + " §8(MOI)" + " §f- §4" + money[0], "C'est le joueur qui a le plus d'argent", name[0]);
			e.setItem(11, it);
			it = Item.GiveOwnsPlayerHead(1, "§e#2 §f- §a"+ name[1] + " §f- §4" + money[1], "C'est le deuxième joueur qui a le plus d'argent", name[1]);
			if(name[1].equalsIgnoreCase(player.getName())) it = Item.GiveOwnsPlayerHead(1, "§e#3 §f- §a"+ name[1] + " §8(MOI)" + " §f- §4" + money[1], "C'est le troisième joueur qui a le plus d'argent", name[1]);
			if(pl.get().size() >= 1 | name[1].equalsIgnoreCase(player.getName())) e.setItem(13, it);
			it = Item.GiveOwnsPlayerHead(1, "§e#3 §f- §a"+ name[2] + " §f- §4" + money[2], "C'est le troisième joueur qui a le plus d'argent", name[2]);
			if(name[2].equalsIgnoreCase(player.getName())) it = Item.GiveOwnsPlayerHead(1, "§e#3 §f- §a"+ name[2] + " §8(MOI)" + " §f- §4" + money[2], "C'est le troisième joueur qui a le plus d'argent", name[2]);
			if(pl.get().size() >= 2 | name[2].equalsIgnoreCase(player.getName())) e.setItem(15, it);
		} else {
			it = Item.GiveItem(Material.BARRIER, 1, "§4Vous n'avez pas d'amis", "§l§e[/friends add <PLAYER_NAME>] §8pour ajouter des amis");
			e.setItem(13, it);
		}
		Item.GrayExGlass(e, 27);
		return;
		
		
		
	}
	
	public static boolean TcheckEconomyMenuAction(Player player, ItemStack it, Inventory inv, boolean playerInventory) {
		if(inventory.contains(inv)) {
			try {
				if(playerInventory) return true;
				else if(personelle(player).equals(it)) OpenMenuEcoPerso(player);
				else if(it.getType().equals(Material.PLAYER_HEAD) || amis(player).equals(it)) OpenMenuEcoFriends(player);
				else if(argentAmis.equals(it)) OpenMenuEcoFriendsMoney(player);
				else if(classement.equals(it)) OpenMenuEcoPublic(player);
				else if(argent.equals(it)) OpenMenuEcoPublicMoney(player);
			} catch (Exception e) {}
			return true;
		} else return false;
	}
	
	public static boolean delInventory(Inventory inv) {
		return inventory.remove(inv);
	}
}

