package fr.sisig48.pl.Menu;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Utils.Item;

public class Interface {

	public static ArrayList<Inventory> inventory = new ArrayList<Inventory>();
	private static Player player;
	
	
	public static Boolean GetActonIfInMenuEco(ItemStack it, Player player) {
		if(it == null) {return false;}
		ItemStack i = Item.GiveOwnsPlayerHead( 1, "Personelle", null, player.getName(), 124);
		if(it.equals(i)) {
			try {
				player.closeInventory();
				EconomieMenu.OpenMenuEcoPerso(player);
				
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;	
	}
	
	public static Boolean GetActonIfInMainMenu(Player players, ItemStack current, Inventory inv) {
		
		if(players == null) {return false;}
		player = players;
		if(current == null) {return false;}
		if(inventory == null) {
			player.closeInventory();
			return false;  
		}
		if(!inventory.contains(inv)) return GetActonIfInMenuEco(current, player);;
		switch(current.getType()) {
			
			case PAPER :
				
				try {
					CloseMenu();
					EconomieMenu.OpenMenuEco(player);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
				break ;
				
				
			case PLAYER_HEAD :
			try {
				EconomieMenu.OpenMenuEcoPerso(players);
			} catch (Exception e) {
				e.printStackTrace();
			}	
				break ;
			
			
			case GOLDEN_APPLE :
			try {
				EconomieMenu.OpenMenuEcoPublic(players);
			} catch (Exception e) {
				e.printStackTrace();
			}
				break;
			
			
			case ENDER_CHEST :
			try {
				player.sendMessage(EconomieMenu.OpenMenuEcoPublicMoney(players).toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
			
			default:
				return true;
				
		}
		
		return true;
		
		
		
	}
	
	public static void CloseMenu() {
		player.closeInventory();
		
	}
}