package fr.sisig48.pl.Menu;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Economie.LootBox;
import fr.sisig48.pl.Utils.Item;

public class MenuPP {
	private static ArrayList<Inventory> inventory = new ArrayList<Inventory>();
	
	private static Inventory ProfileMenu = ProfileMenu();
	
	private static ItemStack economie = Item.GiveItem(Material.PAPER, 1, "§6Economie", "Voir votre economie, et celle du serveur");
	private static ItemStack teleport = Item.GiveItem(Material.ITEM_FRAME, 1, "§6Téléporteur", "Téléportez vous a des point de téléportation");
	private static ItemStack profile(Player player) {
		return Item.GiveOwnsPlayerHead(1, "§6Profile", "Voir votre profile et vos statistique", player);
	}
	
	public static void OpenMainMenu(Player player) {
		player.closeInventory();

		Inventory e = Bukkit.createInventory(player, 54, "Menu");
		inventory.add(e);
		
		//Verre exterieur
		Item.GrayExGlass(e, 54);
		
		//Etoile centrale
		e.setItem(4, NetherStarMenu.GetMenu(player));
		
		
		e.setItem(21, economie);
		e.setItem(22, profile(player));
		e.setItem(30, teleport);
		
		player.openInventory(e);
		
	}
	
	public static void OpenProfileMenu(Player player) {
		player.closeInventory();
		player.openInventory(ProfileMenu);
	}
	
	private static ItemStack lootbox = Item.GiveItem(Material.CHEST, 1, "§dLoot box", "§8Ouvre t-a loot box");
	private static Inventory ProfileMenu() {
		Inventory e = Bukkit.createInventory(null, 27, "Menu - Profile");
		
		//Element
		e.setItem(13, lootbox);
		
		Item.GrayExGlass(e, 27);
		return e;
	}
	
	public static boolean TcheckMainMenuAction(Player player, ItemStack it, Inventory inv, boolean playerInventory) {
		if(inventory.contains(inv)) {
			if(playerInventory) return true;
			if(profile(player).equals(it)) MenuPP.OpenProfileMenu(player);
			else if(economie.equals(it)) EconomieMenu.OpenMenuEco(player);
			else if(teleport.equals(it)) MenuTP.OpenMenuTP(player);
			return true;
		}
		if(ProfileMenu.equals(inv)) {
			if(playerInventory) return true;
			if(lootbox.equals(it)) LootBox.openInv(player);
			return true;
		}
		return false;
	}
	
	public static boolean delInventory(Inventory inv) {
		return inventory.remove(inv);
	}
}
