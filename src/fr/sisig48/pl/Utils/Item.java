package fr.sisig48.pl.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Item {

	
	

	public static ItemStack GiveItem(Material material, int stack, String name, String lore) {

		
		ItemStack i = new ItemStack(material, stack);
		ItemMeta Meta = i.getItemMeta(); 
		Meta.setDisplayName(name);
		if(lore != null && !lore.equalsIgnoreCase("null")) {
			lore = "§8" + lore;
			Meta.setLore(Arrays.asList(lore));
		}
		i.setItemMeta(Meta);
		return i;
	}
	
	public static ItemStack GiveItemLore(Material material, int stack, String name, String[] lores) {
		
		
		ItemStack i = new ItemStack(material, stack);
		ItemMeta Meta = i.getItemMeta(); 
		Meta.setDisplayName(name);
		if(lores.length > 0) {
			ArrayList<String> lo = new ArrayList<String>();
			for(String lore : lores) lo.add("§8" + lore);
			Meta.setLore(lo);
		}
		i.setItemMeta(Meta);
		return i;
	}

	public static ItemStack GiveItemLore(Material material, int stack, String name, List<String> lores) {
		
		
		ItemStack i = new ItemStack(material, stack);
		ItemMeta Meta = i.getItemMeta(); 
		Meta.setDisplayName(name);
		if(lores.size() > 0) Meta.setLore(lores);
		i.setItemMeta(Meta);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack GiveOwnsPlayerHead(int stack, String name, String lore, String player) {
		
		
		ItemStack i = new ItemStack(Material.PLAYER_HEAD, stack);
		SkullMeta Meta = (SkullMeta) i.getItemMeta();
		Meta.setDisplayName(name);
		Meta.setOwner(player);
		if(lore != null && !lore.equalsIgnoreCase("null")) {
			lore = "§8" + lore;
			Meta.setLore(Arrays.asList(lore));
		}
		i.setItemMeta(Meta);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack GiveOwnsPlayerHead(int stack, String name, String[] lores, String player) {
		
		
		ItemStack i = new ItemStack(Material.PLAYER_HEAD, stack);
		SkullMeta Meta = (SkullMeta) i.getItemMeta();
		Meta.setDisplayName(name);
		Meta.setOwner(player);
		if(lores.length > 0) {
			ArrayList<String> lo = new ArrayList<String>();
			for(String lore : lores) lo.add("§8" + lore);
			Meta.setLore(lo);
		}
		i.setItemMeta(Meta);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack GiveOwnsPlayerHead(int stack, String name, String lore, Player player) {
		
		
		ItemStack i = new ItemStack(Material.PLAYER_HEAD, stack);
		SkullMeta Meta = (SkullMeta) i.getItemMeta();
		Meta.setDisplayName(name);
		Meta.setOwner(player.getName());
		if(lore != null && !lore.equalsIgnoreCase("null")) {
			lore = "§8" + lore;
			Meta.setLore(Arrays.asList(lore));
		}
		i.setItemMeta(Meta);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack GiveOwnsPlayerHead(int stack, String name, String lore, OfflinePlayer player) {
		
		
		ItemStack i = new ItemStack(Material.PLAYER_HEAD, stack);
		SkullMeta Meta = (SkullMeta) i.getItemMeta();
		Meta.setDisplayName(name);
		Meta.setOwner(player.getName());
		if(lore != null && !lore.equalsIgnoreCase("null")) {
			lore = "§8" + lore;
			Meta.setLore(Arrays.asList(lore));
		}
		i.setItemMeta(Meta);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack GiveOwnsPlayerHead(int stack, String name, List<String> lores, Player player) {
		
		ItemStack i = new ItemStack(Material.PLAYER_HEAD, stack);
		SkullMeta Meta = (SkullMeta) i.getItemMeta();
		Meta.setDisplayName(name);
		Meta.setOwner(player.getName());
		if(lores.size() > 0) Meta.setLore(lores);
		i.setItemMeta(Meta);
		return i;
	}
	
	public static void GrayExGlass(Inventory e, int in) {
		ItemStack it;
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null);
		for(int i = 1; i <= 9; i++) {
			e.setItem(i - 1, it);
			e.setItem(in - i, it);
		}
	}
	
	public static void GrayExGlass(Inventory e) {
		ItemStack it;
		int in = e.getSize();
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null);
		for(int i = 1; i <= 9; i++) {
			e.setItem(i - 1, it);
			e.setItem(in - i, it);
		}
	}
	public static ItemStack GrayExGlass() {
		return Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null);
	}
	
	public static boolean isInventoryFull(Inventory inventory) {
	    for (ItemStack item : inventory.getStorageContents()) if (item == null || item.getType() == Material.AIR) return false;
	    return true;
	}
	
	public static boolean isInventoryFull(Inventory inventory, int EmptySlots) {
		int space = 0;
		for (ItemStack item : inventory.getStorageContents()) if(item == null || item.getType() == Material.AIR) space++;
		return space >= EmptySlots;
	}
	
	public static ItemStack addItemFlags(ItemStack it, ItemFlag flags) {
		ItemMeta meta = it.getItemMeta();
		meta.addItemFlags(flags);
		it.setItemMeta(meta);
		return it;
	}
	
}
