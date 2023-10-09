package fr.sisig48.pl.Menu;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Economie.LootBox;

public class Interface {

	public static ArrayList<Inventory> inventory = new ArrayList<Inventory>();
	
	public static Boolean GetActonInMenu(Player player, ItemStack current, Inventory inv, boolean playerInventory) {
		if(player == null || current == null) {return false;}
		if(!inventory.contains(inv)) return false;
		else if(playerInventory) return true;
		switch(current.getType()) {
			
			case ENDER_CHEST:
				LootBox.openLootBox(player, LootBox.getLootBox(current));
				break;
			
			case CHEST:
				LootBox.openLootBox(player, 0);
			
			default:
				return true;
				
		}
		return true;
		
		
		
	}
	
	public static void delInventory(Inventory e) {
		if(inventory.remove(e)
				|| EconomieMenu.delInventory(e)
				|| JobsMenu.delInventory(e)
				|| MenuPP.delInventory(e))
			return;
	}
}