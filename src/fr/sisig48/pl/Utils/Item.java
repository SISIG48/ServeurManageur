package fr.sisig48.pl.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Item {

	private static ItemStack i;
	
	

	public static ItemStack GiveItem(Material material, int stack, String name, String lore) {

		
		i = new ItemStack(material, stack);
		ItemMeta Meta = i.getItemMeta(); 
		Meta.setDisplayName(name);
		if(lore != null && !lore.equalsIgnoreCase("null")) {
			lore = "§8" + lore;
			Meta.setLore(Arrays.asList(lore));
		}
		Meta.setCustomModelData(123);
		i.setItemMeta(Meta);
		return i;
	}
	

	public static ItemStack GiveItem(Material material, int stack, String name, String lore, int code) {

		
		i = new ItemStack(material, stack);
		ItemMeta Meta = i.getItemMeta(); 
		Meta.setDisplayName(name);
		if(lore != null && !lore.equalsIgnoreCase("null")) {
			lore = "§8" + lore;
			Meta.setLore(Arrays.asList(lore));
		}
		Meta.setCustomModelData(code);
		i.setItemMeta(Meta);
		return i;
	}
	
public static ItemStack GiveItemLore(Material material, int stack, String name, String[] lores, int code) {

		
		i = new ItemStack(material, stack);
		ItemMeta Meta = i.getItemMeta(); 
		Meta.setDisplayName(name);
		if(lores.length > 0) {
			ArrayList<String> lo = new ArrayList<String>();
			for(String lore : lores) lo.add("§8" + lore);
			Meta.setLore(lo);
		}
		Meta.setCustomModelData(code);
		i.setItemMeta(Meta);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack GiveOwnsPlayerHead(int stack, String name, String lore, String player, int Code) {
		
		
		i = new ItemStack(Material.PLAYER_HEAD, stack);
		SkullMeta Meta = (SkullMeta) i.getItemMeta();
		Meta.setDisplayName(name);
		Meta.setOwner(player);
		Meta.setCustomModelData(Code);
		if(lore != null && !lore.equalsIgnoreCase("null")) {
			lore = "§8" + lore;
			Meta.setLore(Arrays.asList(lore));
		}
		i.setItemMeta(Meta);
		return i;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack GiveOwnsPlayerHead(int stack, String name, String[] lores, String player, int Code) {
		
		
		i = new ItemStack(Material.PLAYER_HEAD, stack);
		SkullMeta Meta = (SkullMeta) i.getItemMeta();
		Meta.setDisplayName(name);
		Meta.setOwner(player);
		Meta.setCustomModelData(Code);
		if(lores.length > 0) {
			ArrayList<String> lo = new ArrayList<String>();
			for(String lore : lores) lo.add("§8" + lore);
			Meta.setLore(lo);
		}
		i.setItemMeta(Meta);
		return i;
	}
	

	public static void SetItem(int Position, Material typeMaterial, int Nombre, String Name,String LoreD, Player player, Boolean Unbreakable) {
		
		ItemStack item = new ItemStack(typeMaterial, Nombre);
		ItemMeta customM = item.getItemMeta();
		customM.setUnbreakable(Unbreakable);
		customM.setDisplayName(Name);
		if(LoreD != null && !LoreD.equalsIgnoreCase("null")) {
			LoreD = "§8" + LoreD;
			customM.setLore(Arrays.asList(LoreD));
		}
		customM.setCustomModelData(123);
		item.setItemMeta(customM);
		player.getInventory().setItem(Position, item);
		player.updateInventory();
		
	}
	
	public static void SetItem(int Position, Material typeMaterial, int Nombre, String Name,String[] lores, Player player, Boolean Unbreakable) {
		
		ItemStack item = new ItemStack(typeMaterial, Nombre);
		ItemMeta customM = item.getItemMeta();
		customM.setUnbreakable(Unbreakable);
		customM.setDisplayName(Name);
		if(lores.length > 0) {
			ArrayList<String> lo = new ArrayList<String>();
			for(String lore : lores) lo.add("§8" + lore);
			customM.setLore(lo);
		}
		customM.setCustomModelData(123);
		item.setItemMeta(customM);
		player.getInventory().setItem(Position, item);
		player.updateInventory();
		
	}
	

	public static void SetItem(int Position, Material typeMaterial, int Nombre, String Name,String LoreD, Player player, Boolean Unbreakable, int code) {
		
		ItemStack item = new ItemStack(typeMaterial, Nombre);
		ItemMeta customM = item.getItemMeta();
		customM.setUnbreakable(Unbreakable);
		customM.setDisplayName(Name);
		if(LoreD != null && !LoreD.equalsIgnoreCase("null")) {
			LoreD = "§8" + LoreD;
			customM.setLore(Arrays.asList(LoreD));
		}
		customM.setCustomModelData(code);
		item.setItemMeta(customM);
		player.getInventory().setItem(Position, item);
		player.updateInventory();
		
	}

	
	
	
}
