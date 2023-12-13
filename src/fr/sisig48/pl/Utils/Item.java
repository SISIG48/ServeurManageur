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

import fr.sisig48.pl.logs;

public class Item {
	private static Material[] survival = SurvilalItem();
	private static Material[] SurvilalItem() {
		ArrayList<Material> m = new ArrayList<Material>(Arrays.asList(Material.values()));
		String[] list = ("AIR,LIGHT,COMMAND_BLOCK,COMMAND_BLOCK_MINECART,CHAIN_COMMAND_BLOCK,REPEATING_COMMAND_BLOCK,BARRIER," +
				"STRUCTURE_VOID,STRUCTURE_BLOCK,JIGSAW,DEBUG_STICK,KNOWLEDGE_BOOK,WATER,LAVA,TALL_SEAGRASS,PISTON_HEAD," +
				"MOVING_PISTON,WALL_TORCH,FIRE,SOUL_FIRE,REDSTONE_WIRE,OAK_WALL_SIGN,SPRUCE_WALL_SIGN,BIRCH_WALL_SIGN," +
				"ACACIA_WALL_SIGN,JUNGLE_WALL_SIGN,DARK_OAK_WALL_SIGN,MANGROVE_WALL_SIGN,REDSTONE_WALL_TORCH,SOUL_WALL_TORCH,NETHER_PORTAL," +
				"ATTACHED_PUMPKIN_STEM,ATTACHED_MELON_STEM,PUMPKIN_STEM,MELON_STEM,WATER_CAULDRON,LAVA_CAULDRON,POWDER_SNOW_CAULDRON," +
				"END_PORTAL,COCOA,TRIPWIRE,POTTED_OAK_SAPLING,POTTED_SPRUCE_SAPLING,POTTED_BIRCH_SAPLING,POTTED_JUNGLE_SAPLING," +
				"POTTED_ACACIA_SAPLING,POTTED_DARK_OAK_SAPLING,POTTED_MANGROVE_PROPAGULE,POTTED_FERN,POTTED_DANDELION,POTTED_POPPY,POTTED_BLUE_ORCHID," +
				"POTTED_ALLIUM,POTTED_AZURE_BLUET,POTTED_RED_TULIP,POTTED_ORANGE_TULIP,POTTED_WHITE_TULIP,POTTED_PINK_TULIP,POTTED_OXEYE_DAISY," +
				"POTTED_CORNFLOWER,POTTED_LILY_OF_THE_VALLEY,POTTED_WITHER_ROSE,POTTED_RED_MUSHROOM,POTTED_BROWN_MUSHROOM,POTTED_DEAD_BUSH,POTTED_CACTUS," +
				"CARROTS,POTATOES,SKELETON_WALL_SKULL,WITHER_SKELETON_WALL_SKULL,ZOMBIE_WALL_HEAD,PLAYER_WALL_HEAD,CREEPER_WALL_HEAD," +
				"DRAGON_WALL_HEAD,WHITE_WALL_BANNER,ORANGE_WALL_BANNER,MAGENTA_WALL_BANNER,LIGHT_BLUE_WALL_BANNER,YELLOW_WALL_BANNER,LIME_WALL_BANNER," +
				"PINK_WALL_BANNER,GRAY_WALL_BANNER,LIGHT_GRAY_WALL_BANNER,CYAN_WALL_BANNER,PURPLE_WALL_BANNER,BLUE_WALL_BANNER,BROWN_WALL_BANNER," +
				"GREEN_WALL_BANNER,RED_WALL_BANNER,BLACK_WALL_BANNER,BEETROOTS,END_GATEWAY,FROSTED_ICE,KELP_PLANT,DEAD_TUBE_CORAL_WALL_FAN," +
				"DEAD_BRAIN_CORAL_WALL_FAN,DEAD_BUBBLE_CORAL_WALL_FAN,DEAD_FIRE_CORAL_WALL_FAN,DEAD_HORN_CORAL_WALL_FAN,TUBE_CORAL_WALL_FAN,BRAIN_CORAL_WALL_FAN," +
				"BUBBLE_CORAL_WALL_FAN,FIRE_CORAL_WALL_FAN,HORN_CORAL_WALL_FAN,BAMBOO_SAPLING,POTTED_BAMBOO,VOID_AIR,CAVE_AIR,BUBBLE_COLUMN," +
				"SWEET_BERRY_BUSH,WEEPING_VINES_PLANT,TWISTING_VINES_PLANT,CRIMSON_WALL_SIGN,WARPED_WALL_SIGN,POTTED_CRIMSON_FUNGUS,POTTED_WARPED_FUNGUS," +
				"POTTED_CRIMSON_ROOTS,POTTED_WARPED_ROOTS,CANDLE_CAKE,WHITE_CANDLE_CAKE,ORANGE_CANDLE_CAKE,MAGENTA_CANDLE_CAKE,LIGHT_BLUE_CANDLE_CAKE," +
				"YELLOW_CANDLE_CAKE,LIME_CANDLE_CAKE,PINK_CANDLE_CAKE,GRAY_CANDLE_CAKE,LIGHT_GRAY_CANDLE_CAKE,CYAN_CANDLE_CAKE,PURPLE_CANDLE_CAKE," +
				"BLUE_CANDLE_CAKE,BROWN_CANDLE_CAKE,GREEN_CANDLE_CAKE,RED_CANDLE_CAKE,BLACK_CANDLE_CAKE,POWDER_SNOW,CAVE_VINES,CAVE_VINES_PLANT," +
				"BIG_DRIPLEAF_STEM,POTTED_AZALEA_BUSH,POTTED_FLOWERING_AZALEA_BUSH,BAMBOO_WALL_HANGING_SIGN,BAMBOO_WALL_SIGN").split(",");
		
		for(String s : list) {
			try {
				m.remove(Material.valueOf(s));
			} catch (IllegalArgumentException e) {
				fr.sisig48.pl.logs.send("§8" + s + " n'existe pas dans votre version (" + fr.sisig48.pl.Main.Plug.getServer().getBukkitVersion() + ")");
			}
		}
		
		return m.toArray(new Material[0]);
	}
	
	public static Material[] getMaterials() {
		return survival;
	}
	

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
	
	public static boolean isInventoryFull(Inventory inventory, float EmptySlots) {
		int space = 0;
		for (ItemStack item : inventory.getStorageContents()) if(item == null || item.getType() == Material.AIR) space++;
		return space < EmptySlots;
	}
	
	public static ItemStack addItemFlags(ItemStack it, ItemFlag flags) {
		ItemMeta meta = it.getItemMeta();
		meta.addItemFlags(flags);
		it.setItemMeta(meta);
		return it;
	}
	
}
