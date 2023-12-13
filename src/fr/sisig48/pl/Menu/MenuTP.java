package fr.sisig48.pl.Menu;


import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.State.Spawn;
import fr.sisig48.pl.Utils.Item;
import fr.sisig48.pl.Utils.Uconfig;

public class MenuTP {
	private static ItemStack spawn = Item.GiveItem(Material.ENDER_PEARL, 1, "§aSpawn", "Téléportation vers le spawn");
	private static ItemStack mine = Item.GiveItem(Material.DEEPSLATE_GOLD_ORE, 1, "§aMine", "Téléportation vers la mine");
	private static Inventory menu = Menu();
	public static void OpenMenuTP(Player player) {
		player.closeInventory();
		player.openInventory(menu);
	}
	
	private static Inventory Menu() {
		Inventory e = Bukkit.createInventory(null, 36, "Téléporteur");
		
		//Elements
		e.setItem(11, spawn);
		e.setItem(22, mine);
		Item.GrayExGlass(e, 36);
		return e;
	}
	
	public static boolean TcheckTPMenuAction(Player player, ItemStack it, Inventory inv, boolean playerInventory) {
		if(menu.equals(inv)) {
			if(playerInventory) return true;
			if(spawn.equals(it)) {
				try {
					if(player.getLocation().getWorld().getName().equals(Uconfig.getConfig("location.mine.in.w")) ) {
						player.teleport(Spawn.GetMineOutSpawnLocation());
						player.sendMessage("§aVous avez été tp au §4spawn");
					} else {
						player.teleport(Spawn.GetSpawnLocation());
						player.sendMessage("§aVous avez été tp au §4spawn");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			else if(mine.equals(it)) {
				try {
					player.teleport(Spawn.GetMineInSpawnLocation());
					player.sendMessage("§aVous avez été tp a la §4mine");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		return false;
	}
}
