package fr.sisig48.pl.NetherStar;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Menu.MenuPP;
import fr.sisig48.pl.Utils.Item;

public class NetherStarMenu {
	
	public static Boolean HasMenu(Player player, int i, ItemStack current) {
		String cun = current.getItemMeta().getDisplayName();
		String cul = current.getItemMeta().getDisplayName();
		if(i == 123 || cun == "§cMenu" && cul == "Menu du survival, " + player.getDisplayName()) {
			player.closeInventory();
			MenuPP.OpenMainMenu(player);
			return true;
		}
		
		return false;
	}
	
	public static void GiveMenu(Player player) {
		Item.SetItem(8, Material.NETHER_STAR, 1, "§cMenu", "Menu du survival, " + player.getDisplayName() , player, true);
	}
	
	public static Boolean HasMenu(Player player, int i) {
		if(i == 123) {
			MenuPP.OpenMainMenu(player);
			return true;
		}
		
		return false;
	}
	
}
