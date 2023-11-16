package fr.sisig48.pl.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.sisig48.pl.Economie.LootBox;
import fr.sisig48.pl.Utils.Item;

public class LootStorage {
	private static List<LootStorage> instanceList = new ArrayList<LootStorage>();
	private Player player;
	private LootStorage e = this;
	private List<Inventory> invs = new ArrayList<Inventory>();
	
	//Navigation
	private static ItemStack prochainePage = Item.GiveItem(Material.ARROW, 1, "§eSuivant", null);
	private static ItemStack précédentePage = Item.GiveItem(Material.ARROW, 1, "§ePrécédent", null);
	private static ItemStack retour = Item.GiveItem(Material.BARRIER, 1, "§d§oRetour", null);
	
	/* Inv:
	 * 0  1  2  3  4  5  6  7  8
	 * 9  10 11 12 13 14 15 16 17
	 * 18 19 20 21 22 23 24 25 26
	 * 27 28 29 30 31 32 33 34 35
	 * 36 37 38 39 40 41 42 43 44
	 * 45 46 47 48 49 50 51 52 53
	 */
	
	public LootStorage(Player player) {
		for(LootStorage ls : instanceList) if(ls.getPlayer().equals(player)) {
				e = ls;
				return;
		}
		e.player = player;
		addInventory();
		instanceList.add(e);
	}
	
	public void openInv() {
		if(e.invs.size() == 0) return;
		e.player.openInventory(e.invs.get(0));
	}
	
	private Inventory addInventory() {
		Inventory inv = Bukkit.createInventory(null, 54, "Loot Storage");
		
		//Elements
		Item.GrayExGlass(inv);
		
		//NAV
		inv.setItem(45, précédentePage);
		inv.setItem(49, retour);
		inv.setItem(53, prochainePage);
		
		e.invs.add(inv);
		return inv;
	}
	
	@SuppressWarnings("deprecation")
	public void addItem(ItemStack is) {
		
		ItemStack it = new ItemStack(is);
		Inventory inv = e.invs.get(e.invs.size()-1);
		int amount = it.getAmount();
		int remaining = it.getAmount();
		
		Date date = Calendar.getInstance().getTime();
		if(it.hasItemMeta()) {
			ItemMeta meta = it.getItemMeta();
			List<String> lore = meta.getLore();
			lore.add("§8Ici depuis le §7" + date.getDate() + "/" + (date.getMonth()+1) + "/" + (date.getYear()+1900) + " §8à §7" + date.getHours() + ":" + date.getMinutes());
			meta.setLore(lore);
			it.setItemMeta(meta);
		}
		it.setAmount(it.getMaxStackSize());
		for(int slots = amount / it.getMaxStackSize(); slots >= 1; slots--) {
			if(Item.isInventoryFull(inv)) inv = e.addInventory();
			inv.addItem(it);
			remaining = remaining - it.getMaxStackSize();
		}
		if(remaining > 0) {	
			it.setAmount(remaining);
			if(Item.isInventoryFull(inv)) inv = e.addInventory();
			inv.addItem(it);
		}
	}
	
	public Player getPlayer() {
		return e.player;
	}
	
	public boolean TcheckLootMenuAction(Player player, ItemStack it, Inventory inv, boolean playerInventory) {
		if(!e.invs.contains(inv)) return false;
		if(playerInventory) return true;
		
		int index = e.invs.indexOf(inv);
		int size = e.invs.size();
		if(it.equals(prochainePage) && size > (index+1)) e.player.openInventory(e.invs.get(index+1));
		else if(it.equals(précédentePage) && index != 0) e.player.openInventory(e.invs.get(index-1));
		else if(it.equals(retour)) LootBox.openInv(e.player);
		else {
			List<ItemStack> contents = Arrays.asList(inv.getStorageContents());
			if(contents.indexOf(it) != -1 && !Item.isInventoryFull(player.getInventory())) {
				contents.set(contents.indexOf(it), new ItemStack(Material.AIR));
				player.getInventory().addItem(new ItemStack(it.getType(), it.getAmount()));
			}
			inv.setStorageContents(contents.toArray(new ItemStack[0]));
		}
		return true;
	}
}
