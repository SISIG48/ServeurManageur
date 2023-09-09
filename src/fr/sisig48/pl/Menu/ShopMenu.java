package fr.sisig48.pl.Menu;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.sisig48.pl.Economie.XpCounter;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Item;

public class ShopMenu {
	
	private static ItemStack sellItem = Item.GiveItem(Material.HOPPER, 1, "§dVendre un item", null, 130);
	public static void OpenShop(Player p) {
		int size = 27;
		Inventory e = Bukkit.createInventory(p, size, "Shop");
		p.openInventory(e);
		GrayExGlass(e, size);
		e.setItem(13, Item.GiveItem(Material.PAPER, 1, "§4Shop pas actif", "§8réessayer plus tard"));
		e.setItem(size - 5, sellItem);
	}
	
	private static void GrayExGlass(Inventory e, int in) {
		ItemStack it;
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(in - 9, it); e.setItem(in - 8, it); e.setItem(in - 7, it); e.setItem(in - 6, it); e.setItem(in - 5, it); e.setItem(in - 4, it); e.setItem(in - 3, it); e.setItem(in - 2, it); e.setItem(in - 1, it);
	}
	
	public static boolean TcheckShopMenuAction(Player p, ItemStack it) {
		
		//Inventory object var
		PlayerJobs pj = new PlayerJobs(p);
		InventoryView openInv = p.getOpenInventory();
		Inventory PlayerInv = p.getInventory();
		int invCo = openInv.countSlots() - PlayerInv.getSize();
		
		if(it.getItemMeta().hasCustomModelData()) switch (it.getItemMeta().getCustomModelData()) {
		case 130: return true;
		case 131:
			//Count xp value
			//Undo xp
			Material m = it.getType();
			pj.MaterialSubXp(m, it.getAmount());
			XpCounter.delItem(it);
			
			//Undo item
			p.getOpenInventory().setItem(invCo - 5, sellItem);
			
			ItemMeta meta = it.getItemMeta();
			meta.setCustomModelData(1251);
			meta.setLore(Arrays.asList("§dNon vendable"));
			it.setItemMeta(meta);
			p.getInventory().addItem(it);
			
			return true;
		}
		
		ItemMeta meta = openInv.getItem(invCo - 5).getItemMeta();
		//Get the good inventory
		//Sell
		if(sellItem.equals(openInv.getItem(invCo - 5)) || (meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 131)) {
			XpCounter.addItem(it);
			if(it.getItemMeta().hasCustomModelData() && it.getItemMeta().getCustomModelData() == 1251) return true;
			
			pj.MaterialAddXp(it.getType(), it.getAmount());
			
			it = new ItemStack(it.getType(), it.getAmount());
			
			//detect item from player inventory
			int n = -1;
			for(int i = 0; i < PlayerInv.getSize() && n == -1; i++) if(PlayerInv.getItem(i) != null && PlayerInv.getItem(i).equals(it)) n = i;
			
			//delete item
			PlayerInv.setItem(n, new ItemStack(Material.AIR));
			
			//Change item info
			meta = it.getItemMeta();
			meta.setCustomModelData(131);
			meta.setLore(Arrays.asList("§8Vendu"));
			it.setItemMeta(meta);

			//Place item in shop
			openInv.setItem(invCo - 5, it);
			
			return true;
			
		}
		return false;
	}
}
