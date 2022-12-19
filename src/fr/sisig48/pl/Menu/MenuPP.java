package fr.sisig48.pl.Menu;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;



import fr.sisig48.pl.Utils.Item;

public class MenuPP {

	public static void OpenMainMenu(Player player) {
		
		Inventory e = Bukkit.createInventory(player, 54, "Menu du survival");
		Interface.inventory.add(e);
		
		ItemStack it;
		
		//Etoile centrale
		it = Item.GiveItem(Material.NETHER_STAR, 1, "§cMenu", "Menu du survival, " + player.getDisplayName());
		e.setItem(4, it);
		
		//Verre exterieur
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(45, it); e.setItem(46, it); e.setItem(47, it); e.setItem(48, it); e.setItem(49, it); e.setItem(50, it); e.setItem(51, it); e.setItem(52, it); e.setItem(53, it);
		
		it = Item.GiveItem(Material.PAPER, 1, "§6Economie", "Voir votre economie, et celle du serveur");
		e.setItem(21, it);
		
		player.openInventory(e);
		
	}
	
	
	
	
	public static String inventoryRealName;
	public static ItemStack current;
}
