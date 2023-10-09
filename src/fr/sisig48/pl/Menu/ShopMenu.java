package fr.sisig48.pl.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.logs;
import fr.sisig48.pl.Economie.Economie;
import fr.sisig48.pl.Economie.EconomieMarchant;
import fr.sisig48.pl.Economie.ObjectPrice;
import fr.sisig48.pl.JobsHouse.HouseData;
import fr.sisig48.pl.JobsHouse.HouseList;
import fr.sisig48.pl.JobsHouse.HouseListInfo;
import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Utils.Item;

public class ShopMenu {
	
	//Object
	private static Material[] survival = SurvilalItem();
	
	//Main inventaire
	private static ItemStack admin = Item.GiveItem(Material.PAINTING, 1, "§dAdmin shop", null);
	private static ItemStack marchant = Item.GiveItem(Material.WITHER_SKELETON_SKULL, 1, "§4Marchant", "uniquement disponible si aucun shop marchant n'est fonctionelle");
	private static ItemStack shop = Item.GiveItem(Material.PAPER, 1, "§dShop", null);
	private static Inventory main = mainMenu();
	
	//Navigation
	private static ItemStack indisponible = Item.GiveItem(Material.PAPER, 1, "§4Indisponible", null);
	private static ItemStack prochainePage = Item.GiveItem(Material.ARROW, 1, "§eSuivant", null);
	private static ItemStack précédentePage = Item.GiveItem(Material.ARROW, 1, "§ePrécédent", null);
	private static ItemStack retour = Item.GiveItem(Material.BARRIER, 1, "§d§oRetour", null);
	
	//AdminShop menu
	private static Inventory adminMenu = adminMenu();
	
	//Marchant menu
	private static ArrayList<Inventory> marchantMenu = marchantMenu();
	private static ArrayList<Inventory> marchantMenuDetail = marchantDetailMenu();
	private static ArrayList<ArrayList<Inventory>> marchantVueVendeur = marchantVueVendeur();
	private static ArrayList<Inventory> marchantMenuVendre = new ArrayList<Inventory>();
	
	
	//Shop menu
	private static ArrayList<Inventory> shopMenu = shopMenu();
	private static ArrayList<Inventory> shopMenuDetail = shopDetailMenu();
	private static ArrayList<Inventory> shopMenuVendre = ShopSellMenu();
	
	
	//Ouverture shop
	public static void OpenShop(Player p) {
		p.openInventory(main);
	}

	public static void OpenAdminShop(Player p) {
		p.openInventory(adminMenu);
	}
	
	public static void OpenMarchantShop(Player p, int page) {
		p.openInventory(marchantMenu.get(page-1));
	}
	
	public static void OpenShopMenu(Player p, int page) {
		p.openInventory(shopMenu.get(page-1));
	}
	
	public static void OpenMarchantShop(Player p, Material m) {
		p.openInventory(marchantMenuDetail.get(Arrays.binarySearch(survival, m)));
	}
	public static void OpenMarchantTrade(Player p, Material m, int page) {
		p.openInventory(marchantVueVendeur.get(Arrays.binarySearch(survival, m)).get(page - 1));
	}
	
	public static void OpenShopMenu(Player p, Material m) {
		p.openInventory(shopMenuDetail.get(Arrays.binarySearch(survival, m)));
	}
	
	public static void OpenShopSellMenu(Player p, Material m) {
		p.openInventory(shopMenuVendre.get(Arrays.binarySearch(survival, m)));
	}
	
	
	
	
	
	private static Inventory mainMenu() {
		Inventory e = Bukkit.createInventory(null , 27, "Shop");
		Item.GrayExGlass(e, 27);
		
		//Elements
		e.setItem(11, admin);
		e.setItem(13, marchant);
		e.setItem(15, shop);
		return e;
	}

	private static Inventory adminMenu() {
		Inventory e = Bukkit.createInventory(null , 27, "Admin Shop");
		Item.GrayExGlass(e, 27);
		
		//Elements
		e.setItem(13, indisponible);
		return e;
	}
	
	private static ArrayList<Inventory> marchantMenu() {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		
		//Elements
		int actual = 0;
		Inventory e;
		while((actual) < survival.length) {
			invs.add((e = Bukkit.createInventory(null , 54, "Marchant - page " + (invs.size()+1))));
			Item.GrayExGlass(e, 54);
			if(actual != 0) e.setItem(45, précédentePage);
			if((actual + 40) < survival.length) e.setItem(53, prochainePage);
			e.setItem(49, retour);
			for(int i = 9; i != 45 && (actual) < survival.length; i++) e.setItem(i, new ItemStack(survival[actual++]));
			
		}
		return invs;
	}
	
	private static ArrayList<Inventory> shopMenu() {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		
		//Elements
		String temp = " §a";
		int actual = 0;
		Inventory e;
		while(actual < survival.length) {
			invs.add((e = Bukkit.createInventory(null , 54, "Shop - page " + (invs.size()+1))));
			Item.GrayExGlass(e, 54);
			if(actual != 0) e.setItem(45, précédentePage);
			if((actual + 40) < survival.length) e.setItem(53, prochainePage);
			e.setItem(49, retour);
			for(int i = 9; i != 45 && (actual) < survival.length; i++) {
				e.setItem(i, new ItemStack(survival[actual++]));
				if(actual > 1120 && actual < survival.length) temp = temp + "Material." + survival[actual].toString() + ",";
			}
		}
		return invs;
	}
	

	
	private static ArrayList<Inventory> marchantDetailMenu() {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		
		//Elements
		Inventory e;
		for(Material m : survival) {
			invs.add((e = Bukkit.createInventory(null, 18, "Marchant : détail - page " + (invs.size()+1))));
			Item.GrayExGlass(e, 9);
			e.setItem(4, new ItemStack(m));
			if(invs.size() > 1) e.setItem(0, précédentePage);
			if(invs.size() != survival.length) e.setItem(8, prochainePage);
			e.setItem(10, AchetertoItem(m));
			e.setItem(13, VoiretoItem(m));
			e.setItem(16, VendretoItem(m));
			e.setItem(17, retour);
		}
		return invs;
	}
	
	private static ArrayList<Inventory> shopDetailMenu() {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		
		//Elements
		Inventory e;
		for(Material m : survival) {
			invs.add((e = Bukkit.createInventory(null, 18, "Shop : détail - page " + (invs.size()+1))));
			Item.GrayExGlass(e, 9);
			e.setItem(4, new ItemStack(m));
			if(invs.size() > 1) e.setItem(0, précédentePage);
			if(invs.size() != survival.length) e.setItem(8, prochainePage);
			ItemStack white = Item.GiveItem(Material.WHITE_STAINED_GLASS_PANE, 1, "", null);
			
			e.setItem(9, white);
			e.setItem(10, white);
			e.setItem(11, AchatVendretoItem(m));
			e.setItem(12, white);
			e.setItem(13, white);
			e.setItem(14, white);
			e.setItem(15, VoiretoItem(m));
			e.setItem(16, white);
			e.setItem(17, retour);
		}
		return invs;
	}
	
	
	
	
	
	//Récupéré les items info
	private static ItemStack AchetertoItem(Material m) {
		ItemStack it = new ItemStack(Material.GREEN_DYE);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName("§aAcheter - Prix moyen unitaire : " + Economie.roundPrice(ObjectPrice.getObject(m).getPrice()));
		it.setItemMeta(meta);
		return it;
	}
	
	private static ItemStack VoiretoItem(Material m) {
		
		
		ItemStack it = new ItemStack(Material.ORANGE_DYE);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName("§6Voire - Prix moyen unitaire : " + Economie.roundPrice(ObjectPrice.getObject(m).getPrice()));
		it.setItemMeta(meta);
		return it;
	}
	
	private static ItemStack VendretoItem(Material m) {
		ItemStack it = new ItemStack(Material.RED_DYE);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName("§4Vendre - Prix moyen unitaire : " + Economie.roundPrice(ObjectPrice.getObject(m).getPrice()));
		it.setItemMeta(meta);
		return it;
	}
	
	private static ItemStack AchatVendretoItem(Material m) {
		ItemStack it = new ItemStack(Material.GREEN_DYE);
		ItemMeta meta = it.getItemMeta();
		meta.setDisplayName("§4Vendre/Acheter - Prix moyen unitaire : " + Economie.roundPrice(ObjectPrice.getObject(m).getPrice()));
		it.setItemMeta(meta);
		return it;
	}
	
	
	
	//Marchant vue par objet pour market
	private static ArrayList<ArrayList<Inventory>> marchantVueVendeur() {
		ArrayList<ArrayList<Inventory>> item = new ArrayList<ArrayList<Inventory>>();
		for(Material material : survival) {
			ArrayList<EconomieMarchant> sell = new ArrayList<EconomieMarchant>();
			ArrayList<Inventory> actual = new ArrayList<Inventory>();
			try {sell.addAll(Arrays.asList(EconomieMarchant.getSell(material)));} catch (NullPointerException e) {}
			item.add(actual);
			
			
			Inventory inv = Bukkit.createInventory(null, 54, "Marchant - page 1");
			actual.add(inv);
			baseInventorieMaterial(inv, material);
			for(EconomieMarchant em : sell) {
				if(em.getPlayer() == null) continue;
				int index = sell.indexOf(em);
				int actualIn = (int) (index - Math.round((actual.size()-1) * 36));
				if(actualIn > 35) {
					actual.add((inv = Bukkit.createInventory(null, 54, "Marchant - page " + (actual.size() + 1))));
					actualIn = actualIn - 36;
					baseInventorieMaterial(inv, material);
				}
				//Ajout du vendeur dans la page
				inv.setItem(actualIn+9, Item.GiveOwnsPlayerHead(em.getItem().getAmount(), "§pPrix : " + Economie.roundPrice(em.getPrice()) + " §8+ taxe (" + (int) (EconomieMarchant.buyPrice(100)-100) + "%) : " + Economie.roundPrice(EconomieMarchant.buyPrice(em.getPrice())), ObjectPrice.getObject(material).getChange(em.getPrice()/em.getItem().getAmount()) + "% de diférence avec la moyenne", em.getPlayer()));
			}
		}
		return item;
	}
	
	private static void marchantReload(Material material) {
		
		ArrayList<Inventory> InventoryMaterial = marchantVueVendeur.get(Arrays.binarySearch(survival, material));
		ArrayList<EconomieMarchant> sell = new ArrayList<EconomieMarchant>();
		try {sell.addAll(Arrays.asList(EconomieMarchant.getSell(material)));} catch (NullPointerException e) {}
		
		for(Inventory inv : InventoryMaterial) baseInventorieMaterial(inv, material);
			
		
		Inventory inv = InventoryMaterial.get(0);
		int i = -1;
		for(EconomieMarchant em : sell) {
			if(em.getPlayer() == null) continue;
			int index = sell.indexOf(em);
			
			if(i++ >= 35) {
				i = i - 36;
				if(InventoryMaterial.size() > (index / 36)) inv = InventoryMaterial.get(index / 36);
				else {
					inv = Bukkit.createInventory(null, 54, "Marchant - page " + (InventoryMaterial.size() + 1));
					InventoryMaterial.add(inv);
					baseInventorieMaterial(inv, material);
				}
			}
			
			//Ajout du vendeur dans la page
			inv.setItem(i + 9, Item.GiveOwnsPlayerHead(em.getItem().getAmount(), "§pPrix : " + Economie.roundPrice(em.getPrice()) + " §8+ taxe (" + (int) (EconomieMarchant.buyPrice(100) - 100) + "%) : " + Economie.roundPrice(EconomieMarchant.buyPrice(em.getPrice())), ObjectPrice.getObject(material).getChange(em.getPrice()/em.getItem().getAmount()) + "% de diférence avec la moyenne", em.getPlayer()));
				
		}
		//Detail marchant
		int in = Arrays.binarySearch(survival, material);
		Inventory e = marchantMenuDetail.get(in);
		e.setItem(10, AchetertoItem(material));
		e.setItem(13, VoiretoItem(material));
		e.setItem(16, VendretoItem(material));
		
		//Detail shop
		e = shopMenuDetail.get(in);
		e.setItem(11, AchatVendretoItem(material));
		e.setItem(15, VoiretoItem(material));
		
		//Shop acheter
		e = shopMenuVendre.get(in);
		e.setItem(4, Item.GiveItemLore(Material.NAME_TAG, 1, "§dVendre/Acheter", Arrays.asList("§8Prix unitaire à la vente : " + Economie.getItemPriceSellShop(material), "§8Prix unitaire à l'achat : " + Economie.getItemPriceBuyShop(material))));
		int unit = material.getMaxStackSize() / 4;
		for(i = 1; (i*unit) <= material.getMaxStackSize(); i++) {
			float prix = Economie.getItemPriceBuyShop(material);
			int nb = i*unit;
			if((prix*nb) > 0) e.setItem(i*2+8, Item.GiveItem(material, nb, "§8Acheter - §ax§l" + nb, "§8Prix : " + (Economie.roundPrice(prix*nb)) + " - §d" + Math.abs(ObjectPrice.getObject(material).getChange(prix)) + "% §8de différence avec la moyenne"));
		}
	}

	
	//Navigation par défaut market
	private static void baseInventorieMaterial(Inventory inv, Material material) {
		int size = inv.getSize();
		for(int i = 0; i != inv.getSize(); i++) inv.setItem(i, Item.GiveItem(Material.RED_STAINED_GLASS_PANE, 1, "", null));
		Item.GrayExGlass(inv, size);
		inv.setItem(4, new ItemStack(material));
		inv.setItem(8, prochainePage);
		inv.setItem(0, précédentePage);
		inv.setItem(size - 1, retour);
		inv.setItem(9, retour);
	}
	
	//Vendre
	private static ItemStack ValideSell = Item.GiveItem(Material.GREEN_WOOL, 1, "§eConfirmé vos objets", "§8Définir le prix pour le tout");
	
	private static ItemStack sellInfo(Material m) {
		return Item.GiveItem(Material.BOOK, 1, "§dVendre vos objets", "§8Place les item que tu ve vendre (" + m.name().toLowerCase().replace("_", " ") + ")");
	}
	
	public static void OpenSelectSellObject(Player player, Material material) {
		player.closeInventory();
		Inventory e = Bukkit.createInventory(player, 27, "Vendre");
		marchantMenuVendre.add(e);
		
		Item.GrayExGlass(e, 27);
		e.setItem(4, sellInfo(material));
		e.setItem(18, retour);
		player.openInventory(e);
	}
	
	public static void OpenPriceSell(Player player, ItemStack it) {
		player.closeInventory();
		Inventory e = Bukkit.createInventory(player, InventoryType.ENDER_CHEST, "Prix - " + it.getType().name().toLowerCase().replace("_", " ") + "  x" + it.getAmount());
		marchantMenuVendre.add(e);
		
		Item.GrayExGlass(e, 27);
		ObjectPrice op = ObjectPrice.getObject(it.getType());
		Float prix = Economie.roundPrice(op.getPrice()*it.getAmount());
		e.setItem(13, Item.GiveItem(Material.NAME_TAG, 1, "§ePrix: " + prix + " §6taxe a payer pour vous : " + Economie.roundPrice(EconomieMarchant.sellTaxe(prix)), "Différence avec la moyenne : 0%"));
		
		//plus
		e.setItem(14, Item.GiveItem(Material.GREEN_WOOL, 1, "§a+0.01", null));
		e.setItem(15, Item.GiveItem(Material.GREEN_WOOL, 1, "§a+0.10", null));
		e.setItem(16, Item.GiveItem(Material.GREEN_WOOL, 1, "§a+1.00", null));
		e.setItem(17, Item.GiveItem(Material.GREEN_WOOL, 1, "§a+10.00", null));
		
		//moins
		e.setItem(12, Item.GiveItem(Material.RED_WOOL, 1, "§4-0.01", null));
		e.setItem(11, Item.GiveItem(Material.RED_WOOL, 1, "§4-0.10", null));
		e.setItem(10, Item.GiveItem(Material.RED_WOOL, 1, "§4-1.00", null));
		e.setItem(9, Item.GiveItem(Material.RED_WOOL, 1, "§4-10.00", null));
		
		//retour
		e.setItem(18, retour);
		
		//accepter
		e.setItem(26, ValideSell);
		
		player.openInventory(e);
	}
	
	private static ArrayList<Inventory> ShopSellMenu() {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		for(Material m : survival) {
			Inventory e = Bukkit.createInventory(null, 27);
			invs.add(e);
			int unit = 0;
			if(m.getMaxStackSize() > 4) unit = m.getMaxStackSize() / 4;
			else unit = m.getMaxStackSize();
			
			//Elements
			Item.GrayExGlass(e);
			e.setItem(4, Item.GiveItemLore(Material.NAME_TAG, 1, "§dVendre/Acheter", Arrays.asList("§8Prix unitaire à la vente : " + Economie.getItemPriceSellShop(m), "§8Prix unitaire à l'achat : " + Economie.getItemPriceBuyShop(m))));
			e.setItem(10, Item.GrayExGlass());
			e.setItem(12, Item.GrayExGlass());
			e.setItem(14, Item.GrayExGlass());
			e.setItem(16, Item.GrayExGlass());
			e.setItem(18, retour);
			for(int i = 1; (i*unit) <= m.getMaxStackSize(); i++) {
				float prix = Economie.getItemPriceBuyShop(m);
				int nb = i*unit;
				if((prix*nb) > 0) e.setItem(i*2+8, Item.GiveItem(m, nb, "§8Acheter - §ax§l" + nb, "§8Prix : " + (Economie.roundPrice(prix*nb)) + " - §d" + Math.abs(ObjectPrice.getObject(m).getChange(prix)) + "% §8de différence avec la moyenne"));
			}
		}
		return invs;
	}
	
	/* Inv:
	 * 0  1  2  3  4  5  6  7  8
	 * 9  10 11 12 13 14 15 16 17
	 * 18 19 20 21 22 23 24 25 26
	 * 27 28 29 30 31 32 33 34 35
	 * 36 37 38 39 40 41 42 43 44
	 * 45 46 47 48 49 50 51 52 53
	 */
	
	//Action dans le menu
	public static boolean TcheckShopMenuAction(Player p, ItemStack it, Inventory inv, boolean playerInventory) {
		if(inv.equals(main)) {
			if(playerInventory) return true;
			
			//Event dans le menu principale shop
			if(it.equals(admin)) OpenAdminShop(p);
			else if(it.equals(marchant) && !Jobs.MARCHAND.haveHouse()) OpenMarchantShop(p, 1);
			else if(it.equals(shop)) OpenShopMenu(p, 1);
			return true;
		} 
		
		
		//Regarde les pages du shop
		if(shopMenu.contains(inv)){
			if(playerInventory) {
				if(!it.hasItemMeta()) OpenShopMenu(p, it.getType());
				return true;
			}
			
			//Event dans le menu shop
			int index = shopMenu.indexOf(inv);
			if(it.equals(prochainePage)) OpenShopMenu(p, index + 2);
			else if(it.equals(précédentePage)) OpenShopMenu(p, index);
			else if(it.equals(retour)) OpenShop(p);
			else if(!it.hasItemMeta()) OpenShopMenu(p, it.getType());
			return true;
		} 
		
		//Regarde les pages du marchant
		if(marchantMenu.contains(inv)){
			if(playerInventory) {
				if(!it.hasItemMeta()) OpenMarchantShop(p, it.getType());
				return true;
			}
			
			
			//Event dans le menu priciapale du marchant
			int index = marchantMenu.indexOf(inv);
			if(it.equals(prochainePage)) OpenMarchantShop(p, index + 2);
			else if(it.equals(précédentePage)) OpenMarchantShop(p, index);
			else if(it.equals(retour)) OpenShop(p);
			else if(!it.hasItemMeta()) OpenMarchantShop(p, it.getType());
			return true;
		}  
		if(adminMenu.equals(inv)) return true;
		
		//Navigue dans les items
		if(marchantMenuDetail.contains(inv)) {
			if(playerInventory) {
				OpenMarchantShop(p, it.getType());
				return true;
			}
			
			int index = marchantMenuDetail.indexOf(inv);
			Material m = survival[index];
			if(it.equals(prochainePage) && (index + 1) != marchantMenuDetail.size()) OpenMarchantShop(p, survival[index+1]);
			else if(it.equals(précédentePage) && index != 0) OpenMarchantShop(p, survival[index-1]);
			else if(it.equals(retour)) OpenMarchantShop(p, (int) (Math.nextUp((index) / 36) + 1));
			else if(it.equals(AchetertoItem(m))) OpenMarchantTrade(p, m, 1);
			else if(it.equals(VoiretoItem(m))) OpenMarchantTrade(p, m, 1);
			else if(it.equals(VendretoItem(m))) OpenSelectSellObject(p, m);
			return true;
		} 
		
		//Navigue dans les items
		if(shopMenuDetail.contains(inv)) {
			if(playerInventory) {
				OpenShopMenu(p, it.getType());
				return true;
			}
			
			int index = shopMenuDetail.indexOf(inv);
			Material m = survival[index];
			if(it.equals(prochainePage) && (index + 1) != shopMenuDetail.size()) OpenShopMenu(p, survival[index+1]);
			else if(it.equals(précédentePage) && index != 0) OpenShopMenu(p, survival[index-1]);
			else if(it.equals(retour)) OpenShopMenu(p, (int) (Math.nextUp((index) / 36) + 1));
			else if(it.equals(VoiretoItem(m))) return true;
			else if(it.equals(AchatVendretoItem(m))) OpenShopSellMenu(p, m);
			return true;
		}
		
		//A la recherche de vente
		for(ArrayList<Inventory> a : marchantVueVendeur) if(a.contains(inv)) {
			if(playerInventory) return true;
			
			//Event dans la vues marchant (par type)
			int index = a.indexOf(inv);
			Material m = survival[marchantVueVendeur.indexOf(a)];
			
			if(it.equals(retour)) OpenMarchantShop(p, m);
			else if(it.equals(prochainePage) && a.size() != (index + 1)) OpenMarchantTrade(p, m, index + 2);
			else if(it.equals(précédentePage) && index != 0) OpenMarchantTrade(p, m, index);
			else if(it.getType().equals(Material.PLAYER_HEAD) && Arrays.asList(inv.getStorageContents()).contains(it)) {
				int ad = index * 36 + Arrays.asList(inv.getContents()).indexOf(it) - 9;
				EconomieMarchant em = EconomieMarchant.getSell(m)[ad];
				if(Economie.buyItemMarket(p, em.getItem(), em.getUUID())) marchantReload(m);
			}
			return true;
		} 
		
		//En train de vendre
		if(marchantMenuVendre.contains(inv)) {
			if(inv.getType().equals(InventoryType.ENDER_CHEST)) {
				if(playerInventory || !inv.equals(p.getOpenInventory().getTopInventory())) return true;
				
				//prix
				String info = p.getOpenInventory().getTitle();
				Material invMaterial = Material.valueOf(info.replaceAll("Prix\\s-\\s|\\s*x\\d{1,}$|§8", "").toUpperCase().replace(" ", "_"));
				int amount = Integer.valueOf(info.replaceFirst("^.*\\sx", ""));
				float prix = Economie.roundPrice(Float.valueOf(inv.getItem(13).getItemMeta().getDisplayName().replace(",", ".").replaceAll("§ePrix:\\s|\\s§6(\\w*\\s*:?.?)*$", "")));
				
				//Action
				if(it.equals(ValideSell)) {
					//Vendre
					try {
						if(Economie.sellItemMarket(p, new ItemStack(invMaterial, amount), prix)) {
							p.sendMessage("§a§lVendue");
							if(marchantMenuVendre.remove(inv)) OpenMarchantShop(p, invMaterial);
							marchantReload(invMaterial);
						}
						return true;
					} catch (NumberFormatException e) {}
				}
				if(it.equals(retour)) {
					//Retour
					p.getInventory().addItem(new ItemStack(invMaterial, amount));
					OpenMarchantShop(p, invMaterial);
				} else if(it.getType().equals(Material.GREEN_WOOL) || it.getType().equals(Material.RED_WOOL)) {
					//Modif
					prix = prix + Float.valueOf(it.getItemMeta().getDisplayName().replaceAll("§.", ""));
					if(prix > 0) inv.setItem(13, Item.GiveItem(Material.NAME_TAG, 1, "§ePrix: " + Economie.roundPrice(prix) + " §6taxe a payer pour vous : " + Economie.roundPrice(EconomieMarchant.sellTaxe(prix)), "Différence avec la moyenne : " + ObjectPrice.getObject(invMaterial).getChange((prix/amount)) + "%"));
				}
				return true;
			}

			//Vendre ajout d'item
			Material invMaterial = Material.valueOf(inv.getContents()[4].getItemMeta().getLore().get(0).replaceAll("^.*\\(|\\).*$", "").toUpperCase().replace(" ", "_"));
			
			List<ItemStack> sell = Arrays.asList(Arrays.copyOfRange(inv.getStorageContents(), 9, 18));
			boolean used = false;
			for(ItemStack is : sell) if(is != null && !used) used = true;
			
			if(used) {
				inv.setItem(18, Item.GrayExGlass());
				inv.setItem(26, ValideSell);
			} else {
				inv.setItem(18, retour);
				inv.setItem(26, Item.GrayExGlass());
			}
			
			
			if(retour.equals(it) && !used) {
				marchantMenuVendre.remove(inv);
				OpenMarchantShop(p, invMaterial);
			} else if(ValideSell.equals(it) && used) {
				ItemStack s = new ItemStack(invMaterial, 0);
				for(ItemStack i : sell) if(i!=null) s.setAmount(s.getAmount()+i.getAmount());
				marchantMenuVendre.remove(inv);
				OpenPriceSell(p, s);
			}
			else if(invMaterial.equals(it.getType())) return false; else return true;
			return true;
		}
		
		//Shop trade
		if(shopMenuVendre.contains(inv)) {
			Material m = survival[shopMenuVendre.indexOf(inv)];
			if(it.getType().equals(m) && playerInventory) Economie.sellItemShop(it, p);
			else if(it.equals(retour)) OpenShopMenu(p, m);
			else if(it.getType().equals(m) && it.hasItemMeta() && Economie.buyItemShop(it, p)) marchantReload(m); 
			return true;
		}
		return false;
	}
	
	public static boolean onClose(Inventory e, Player p) {
		if(marchantMenuVendre.remove(e)) {
			Material invMaterial = Material.AIR;
			int amount = 0;
			//Retour
			if(e.getType().equals(InventoryType.ENDER_CHEST)) {
				//info
				String info = p.getOpenInventory().getTitle();
				invMaterial = Material.valueOf(info.replaceAll("Prix\\s-\\s|\\s*x\\d{1,}$|§8", "").toUpperCase().replace(" ", "_"));
				amount = Integer.valueOf(info.replaceFirst("^.*\\sx", ""));
			} else {
				invMaterial = Material.valueOf(e.getContents()[4].getItemMeta().getLore().get(0).replaceAll("^.*\\(|\\).*$", "").toUpperCase().replace(" ", "_"));
				for(ItemStack i : Arrays.asList(Arrays.copyOfRange(e.getStorageContents(), 9, 18))) if(i!=null) amount = (amount+i.getAmount());
			}
			p.getInventory().addItem(new ItemStack(invMaterial, amount));
			OpenMarchantShop(p, invMaterial);
			return true;
		} else return false;
	}
	
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
				fr.sisig48.pl.logs.send("§8" + s + " n'existe pas dans votre version (" + Main.Plug.getServer().getBukkitVersion() + ")");
			}
		}
		
		return m.toArray(new Material[0]);
	}
	
}

