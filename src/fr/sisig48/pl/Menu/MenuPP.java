package fr.sisig48.pl.Menu;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Utils.Item;

public class MenuPP {

	public static void OpenMainMenu(Player player) {
		logs.add("Player : UUID : " + player.getUniqueId() + " | Name :" + player.getName() + " Enter to menuPP");
		Inventory e = Bukkit.createInventory(player, 54, "Menu du survival");
		Interface.inventory.add(e);
		
		ItemStack it;
		
		//Verre exterieur
		GrayExGlass(e, 54);
		
		//Etoile centrale
		it = Item.GiveItem(Material.NETHER_STAR, 1, "§cMenu", "Menu du survival, " + player.getDisplayName());
		e.setItem(4, it);
		
		
		e.setItem(21, Item.GiveItem(Material.PAPER, 1, "§6Economie", "Voir votre economie, et celle du serveur"));
		e.setItem(22, Item.GiveOwnsPlayerHead(1, "§6Profile", "Voir votre profile et vos statistique", player.getName(), 126));
		e.setItem(30, Item.GiveItem(Material.ITEM_FRAME, 1, "§6Téléporteur", "Téléportez vous a des point de téléportation", 127));
		
		player.openInventory(e);
		
	}
	
	public static void OpenProfileMenu(Player player) {
		Inventory e = Bukkit.createInventory(player, 27, "Menu du survival");
		Interface.inventory.add(e);
		
		e.setItem(13, Item.GiveItem(Material.CHEST, 1, "§dLoot box", "§8Ouvre t-a loot box", 132));
		
		GrayExGlass(e, 27);
		
		player.openInventory(e);
	}
	
	private static void GrayExGlass(Inventory e, int in) {
		ItemStack it;
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(in - 9, it); e.setItem(in - 8, it); e.setItem(in - 7, it); e.setItem(in - 6, it); e.setItem(in - 5, it); e.setItem(in - 4, it); e.setItem(in - 3, it); e.setItem(in - 2, it); e.setItem(in - 1, it);
		
	}
}
