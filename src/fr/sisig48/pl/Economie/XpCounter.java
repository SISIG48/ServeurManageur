package fr.sisig48.pl.Economie;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

public class XpCounter {
	private static ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
	public static void Count() {
		Boolean isFind = false;
		for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			Bukkit.getConsoleSender().sendMessage("§a" + p.getName());
			for(ItemStack pi : p.getPlayer().getInventory().getStorageContents()) {
				isFind = false;
				for(ItemStack it : itemList ) {
					int i = itemList.indexOf(it);
					if(pi.getType().equals(it.getType())) {
						it.setAmount(it.getAmount() + pi.getAmount());
						itemList.set(i, it);
						isFind = true;
					}
				}
				if(!isFind) itemList.add(pi);
			}
		}
		
		for(ItemStack it : itemList) Bukkit.getConsoleSender().sendMessage(("§a" + it.getItemMeta().getDisplayName() + " §d" + String.valueOf(it.getAmount())));
	}
}
