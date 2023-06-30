package fr.sisig48.pl.Menu;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Item;

public class ShopMenu {
	public static void OpenShop(Player p) {
		int size = 27;
		Inventory e = Bukkit.createInventory(p, size, "Shop");
		p.openInventory(e);
		GrayExGlass(e, size);
		e.setItem(13, Item.GiveItem(Material.PAPER, 1, "§4Shop is not enable", "§8Please WAIT"));
		e.setItem(size - 5, Item.GiveItem(Material.HOPPER, 1, "§dVendre un item", null, 130));
	}
	
	private static void GrayExGlass(Inventory e, int in) {
		ItemStack it;
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(in - 9, it); e.setItem(in - 8, it); e.setItem(in - 7, it); e.setItem(in - 6, it); e.setItem(in - 5, it); e.setItem(in - 4, it); e.setItem(in - 3, it); e.setItem(in - 2, it); e.setItem(in - 1, it);
	}
	
	public static boolean TcheckShopMenuAction(Player p, ItemStack it) {
		Bukkit.getConsoleSender().sendMessage("exe");
		PlayerJobs pj = new PlayerJobs(p);
		if(it.getItemMeta().hasCustomModelData()) switch (it.getItemMeta().getCustomModelData()) {
		case 130: return true;
		case 131:
			
			//Count xp value
			Material m = it.getType();
			Float Gxp = pj.get().getXpGain(m);
			int i = 1;
			Float initial = pj.getXp();
			
			//Change xp
			pj.subXp((float) (((2.5/(0.75 + (pj.getXp()/1000))) * Gxp)));
			while((i++) != it.getAmount()) pj.subXp((float) ((2.5/(0.75 + (pj.getXp()/1000))) * Gxp));
			String round = String.valueOf(Math.round((pj.getXp()-initial)*1000));
			round = String.valueOf(Float.valueOf(round)/1000);
			
			//Undo item
			p.getOpenInventory().setItem(p.getOpenInventory().countSlots() - 5, it);
			p.getInventory().addItem(it);
			
			return true;
		}
		int invCo = p.getOpenInventory().countSlots() - p.getInventory().getSize();
		if(Item.GiveItem(Material.HOPPER, 1, "§dVendre un item", null, 130).equals(p.getOpenInventory().getItem(invCo - 5))) {
			Bukkit.getConsoleSender().sendMessage("ok");
			pj.MaterialAddXp(it.getType(), it.getAmount());
			ItemStack s;
			
			ItemMeta meta = it.getItemMeta();
			meta.setCustomModelData(131);
			it.setItemMeta(meta);
			
			p.getOpenInventory().setItem(invCo - 5, it);
			int n = 0;
			for(int i = 0; i < p.getInventory().getSize() && (s = p.getInventory().getItem(i)) != it; n = i++);
			p.getInventory().setItem(n, new ItemStack(Material.AIR));
			return true;
		} else Bukkit.getConsoleSender().sendMessage("no");
		return false;
	}
}
