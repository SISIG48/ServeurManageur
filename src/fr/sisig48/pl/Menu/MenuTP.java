package fr.sisig48.pl.Menu;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Utils.Item;

public class MenuTP {
	public static void OpenMenuTP(Player player) {
		Inventory e = Bukkit.createInventory(player, 36, "Téléporteur");
		player.openInventory(e);
		Interface.inventory.add(e);
		
		ItemStack it;
		it = Item.GiveItem(Material.ENDER_PEARL, 1, "§aSpawn", "Téléportation vers le spawn", 127);
		e.setItem(11, it);

		it = Item.GiveItem(Material.DEEPSLATE_GOLD_ORE, 1, "§aMine", "Téléportation vers la mine", 127);
		e.setItem(22, it);
		
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(27, it); e.setItem(28, it); e.setItem(29, it); e.setItem(30, it); e.setItem(31, it); e.setItem(32, it); e.setItem(33, it); e.setItem(34, it); e.setItem(35, it);
	}
}
