package fr.sisig48.pl.Menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Economie.EconomieESS;
import fr.sisig48.pl.Utils.Item;




public class EconomieMenu {
	
	public static Inventory inventory;
	public static Player player;
	public static ItemStack current;
	
	public static void OpenMenuEco(Player player) throws Exception {
		Inventory e = Bukkit.createInventory(player, 27, "Economie");
		player.openInventory(e);
		Interface.inventory.add(e);
		
		
		//Economie personelle
		ItemStack it;
		it = Item.GiveOwnsPlayerHead(1, "Personelle", null, player.getName(), 124);
		e.setItem(11, it);
		it = Item.GiveItem(Material.GOLDEN_APPLE, 1, "Classement", "Obtener les diférent classement du serveur");
		e.setItem(15, it);
		
		
		//Verre exterieur
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);
	}
	
	
	
	public static void OpenMenuEcoPerso(Player player) throws Exception {
		
		Inventory e = Bukkit.createInventory(player, 27, "Economie personelle");
		player.openInventory(e);
		Interface.inventory.add(e);
		
		//Economie personelle
		ItemStack it;
		it = Item.GiveItem(Material.ENDER_CHEST, 1, "Compte en banque", "Vous avez " + EconomieESS.getMoney(player) + "$");
		e.setItem(11, it);
		
		
		//Verre exterieur
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);
	}
	
	public static void OpenMenuEcoPublic(Player player) throws Exception {
		
		Inventory e = Bukkit.createInventory(player, 27, "Economie publique");
		player.openInventory(e);
		Interface.inventory.add(e);
		
		//Economie public
		ItemStack it;
		it = Item.GiveItem(Material.ENDER_CHEST, 1, "Argent", "Découvré le joueur avec le plus d'argent");
		e.setItem(11, it);
		
		
		//Verre exterieur
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(18, it); e.setItem(19, it); e.setItem(20, it); e.setItem(21, it); e.setItem(22, it); e.setItem(23, it); e.setItem(24, it); e.setItem(25, it); e.setItem(26, it);
	}
	
	public static void OpenMenuEcoPublicMoney(Player player) throws Exception{
		
		
		
	}

	
	
	
}

