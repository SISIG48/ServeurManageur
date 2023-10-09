package fr.sisig48.pl.Menu;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class NetherStarMenu {
	
	public static Boolean HasMenu(Player player, ItemStack current) {
		if(GetMenu(player).equals(current)) {
			player.closeInventory();
			MenuPP.OpenMainMenu(player);
			return true;
		}
		return false;
	}
	
	public static void GiveMenu(Player player) {
		player.getInventory().setItem(8, netherStar(player));
	}
	
	public static ItemStack GetMenu(Player player) {
		return netherStar(player);
	}
	
	private static ItemStack netherStar(Player p) {
		ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta customM = item.getItemMeta();
		customM.setUnbreakable(true);
		customM.setDisplayName("§cMenu");
		customM.setLore(Arrays.asList("§8Ouvrir le menu", p.getName()));
		item.setItemMeta(customM);
		return item;
	}
}
