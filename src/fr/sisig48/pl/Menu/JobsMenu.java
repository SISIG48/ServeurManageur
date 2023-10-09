package fr.sisig48.pl.Menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.sisig48.pl.JobsHouse.*;
import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Item;
import net.ess3.api.MaxMoneyException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class JobsMenu {
	
	private static ArrayList<Inventory> JobsInventory = new ArrayList<Inventory>();
	
	//Jobs  Office item
	private static ItemStack metier = Item.addItemFlags(Item.GiveItem(Material.IRON_PICKAXE, 1, "§aChanger de métier", "Changé mon métier"), ItemFlag.HIDE_ATTRIBUTES);
	private static ItemStack AutreJoueurMetier = Item.GiveItem(Material.GOLDEN_APPLE, 1, "Autre Joueur", "Afficher les métier des autre joueur");
	private static ItemStack AchatShop = Item.GiveItem(Material.OAK_PLANKS, 1, "§aAchat shop", null);
	private static ItemStack JobsHead(Player player) {
		PlayerJobs pj = new PlayerJobs(player);
		return Item.GiveOwnsPlayerHead(1, "§aMon métier", Arrays.asList("Votre métier est : §4" + pj.get().getJobs().getName(), "Votre xp : §4" + String.valueOf(pj.getXp())), player);
	}
	
	//Choix de jobs
	private static ItemStack arbre = Item.GiveItem(Material.BELL, 1, "§aAfficher l'arbre des métiers", null);
	private static ArrayList<Inventory> MenuJobschange = ChangeJobsMenu();
	
	//Chagement de jobs finale
	private static ItemStack Retour = Item.GiveItem(Material.RED_WOOL, 1, "§4Refuser", "Retour en arière");
	private static ArrayList<Inventory> changeOk = changeOk();
	private static ArrayList<Inventory> changePasOk = changePasOk();
	
	public static void OpenJobsMenu(Player player) {
		player.closeInventory();
		Inventory e = Bukkit.createInventory(player, 27, "Jobs Office");
		player.openInventory(e);
		JobsInventory.add(e);
		
		Item.GrayExGlass(e, 27);
		
		e.setItem(11, JobsHead(player));
		e.setItem(13, metier);
		e.setItem(15, AutreJoueurMetier);
		e.setItem(22, AchatShop);
	}
	
	


	public static void OpenUnitChangeJobsMenu(Player player, Jobs job) {
		player.closeInventory();
		PlayerJobs pj = new PlayerJobs(player);
		Inventory e;
		try {
			int index = Arrays.binarySearch(Jobs.values(), job);
			if(Economy.getMoneyExact(player.getUniqueId()).doubleValue() >= Double.valueOf(job.getPrice() - 1) && player.getInventory().contains(job.getItemCost().getType(), job.getItemCost().getAmount()) && pj.getXp() >= 1000) e = changeOk.get(index);
			else e = changePasOk.get(index);
			
			player.openInventory(e);
		} catch (UserDoesNotExistException e1) {
			e1.printStackTrace();
		}
		
		
	}
	
	private static ArrayList<Inventory> changeOk() {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		for(Jobs job : Jobs.values()) {
			Inventory e = Bukkit.createInventory(null, 27, job.getName());
			e.setItem(11, getOk(job, true));
			e.setItem(15, Retour);
			Item.GrayExGlass(e, 27);
			invs.add(e);
		}
		return invs;
	}
	
	private static ArrayList<Inventory> changePasOk() {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		for(Jobs job : Jobs.values()) {
			Inventory e = Bukkit.createInventory(null, 27, job.getName());
			e.setItem(11, getOk(job, false));
			e.setItem(15, Retour);
			Item.GrayExGlass(e, 27);
			invs.add(e);
		}
		return invs;
	}
	
	private static ItemStack getOk(Jobs job, Boolean canGet) {
		if(canGet) {
			String[] lores = {"§6Prix : " + job.getPrice(), "§6Cout en item : " + job.getItemCost().getAmount() + " " + job.getItemCost().getType().name(),"§6Cout en Xp : 1000", "§4§lAttention l'xp actuele sera suprimé"};
			return Item.GiveItemLore(Material.GREEN_WOOL, 1, "§aAccepter", lores);
		} else {
			String[] lores = {"§4Prix : " + job.getPrice(), "§4Cout en item : " + job.getItemCost().getAmount() + " " + job.getItemCost().getType().name(),"§4Cout en Xp : 1000", "§4§lvous n'avez pas assez"}; 
			return Item.GiveItemLore(Material.ORANGE_WOOL, 1, "§aAccepter", lores);
		}
	}
	
	public static void OpenChangeJobsMenu(Player player) {
		player.closeInventory();
		player.openInventory(MenuJobschange.get(Arrays.binarySearch(Jobs.values(), new PlayerJobs(player).get())));
	}
	
	@SuppressWarnings("deprecation")
	private static ArrayList<Inventory> ChangeJobsMenu() {
		ArrayList<Inventory> invs = new ArrayList<Inventory>();
		for(Jobs jobs : Jobs.values()) {
			ItemStack it = new ItemStack(Material.BARRIER);
			int lenth = 0;
			
			//Crée l'inventaire
			for(Jobs j : Jobs.All) if(j.isEnable() && jobs.isInRules(j)) lenth++;
			double a = Double.valueOf(lenth / 9.0);
			BigDecimal bd = new BigDecimal(a);
			bd = bd.setScale(0, BigDecimal.ROUND_UP);
			int in = (bd.intValue()*9) + 18;
			if(in < 28) in = 27;
			Inventory e = Bukkit.createInventory(null, in, "Jobs Office");
			
			
			Item.GrayExGlass(e, in);
			e.setItem(in-1, arbre);
			
			int i = 8;
			for(Jobs j : Jobs.All) {
				if(j.isEnable() && (jobs.isInRules(j.getJobs()) || j.getJobs().equals(jobs))) {
					i++;
					if(j.getJobs() == jobs) {
						
						it = Item.GiveItem(Material.PAPER, 1, "§a" + j.getName() , "§8Votre jobs");
						ItemMeta meta = it.getItemMeta();
						meta.addEnchant(Enchantment.LOYALTY, 1, false);
						it.setItemMeta(meta);
						Item.addItemFlags(it, ItemFlag.HIDE_ENCHANTS);
						e.setItem(i, it);
						
					} else {
						e.setItem(i, Item.GiveItem(Material.PAPER, 1, "§a" + j.getName() , null));
					}
				}
			}
			invs.add(e);
		}
		return invs;
	}
	
	@SuppressWarnings({"deprecation"})
	public static boolean TcheckJobsMenuAction(Player player, ItemStack current, Inventory inv, Boolean playerInventory) {
			if(JobsInventory.contains(inv)) {
				if(playerInventory) return true;
				else if(metier.equals(current)) OpenChangeJobsMenu(player);
				else if(AutreJoueurMetier.equals(current));
				else if(AchatShop.equals(current)) OpenHouseBuyLots(player);
				else if(Retour.equals(current)) OpenJobsMenu(player);
				switch (current.getType()) {
				case GREEN_STAINED_GLASS_PANE :
					PlayerJobs pj = new PlayerJobs(player);
					try {
						if(pj.getHouse() == null && Economy.hasMore(player.getName(), pj.get().getHouseData().getHousePrice())) {
							int index = Arrays.asList(inv.getStorageContents()).indexOf(current);
							current.setType(Material.ORANGE_STAINED_GLASS_PANE);
							ItemMeta meta = current.getItemMeta();
							meta.setLore(Arrays.asList("§eConfirmé", meta.getLore().get(0), meta.getLore().get(1)));
							current.setItemMeta(meta);
							inv.setItem(index, current);
						}
					} catch (UserDoesNotExistException e) {e.printStackTrace();}
					return true;
				case ORANGE_STAINED_GLASS_PANE :
					String[] title = current.getItemMeta().getDisplayName().split(" §d");
					HouseListInfo hl = HouseList.getHouseBySlot(Integer.valueOf(title[1]));
					hl.isEnable(false);
					if(title[0].equalsIgnoreCase("§6Emplacement")) new PlayerJobs(player.getPlayer()).addHouse(hl.getLocation());
					current.setType(Material.RED_STAINED_GLASS_PANE);
					ItemMeta meta = current.getItemMeta();
					meta.setCustomModelData(125);
					meta.setLore(Arrays.asList("§4Indisponible ", "§dCette espace appartient a un joueur"));
					meta.addEnchant(Enchantment.MENDING, 1, false);
					current.setItemMeta(meta);
					try {Economy.subtract(player.getName(), new PlayerJobs(player).get().getHouseData().getHousePrice());} catch (NoLoanPermittedException | UserDoesNotExistException | MaxMoneyException e) {e.printStackTrace();}
					player.closeInventory();
					player.teleport(hl.getLocation());
					return true;
					
				case RED_STAINED_GLASS_PANE :
					String[] ti = current.getItemMeta().getDisplayName().split(" §d");
					if(ti[0].equalsIgnoreCase("§6Emplacement")) {
						HouseListInfo h = HouseList.getHouseBySlot(Integer.valueOf(ti[1]));
						Location loc = h.getLocation();
						loc.add(0, 2, 0.5);
						player.teleport(loc);
					}
					return true;
				case RED_BANNER : 
					new PlayerJobs(player).delHouse();
					player.sendMessage("§4Votre maison a été détruitre");
					player.closeInventory();
					return true;
				default : return true;
				
					
				}
			}
			
			if(MenuJobschange.contains(inv)) {
				if(arbre.equals(current)) {
					player.closeInventory();
					TextComponent msgl = new TextComponent("§e[§e§lAFFICHER L'ARBRE DES METIERS§e]");
					msgl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eVOIR").create()));
					msgl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://download1588.mediafire.com/kvoquicoqzhg3cQArRKdd9nkaMpRBPOz_H31Y6HZbFUyteP89jLY4kE7m3X_w0E-bzhLNrZNwHr78L0PexYHzqXPr2E/07cclf8s48jz1pm/Dragons+Economy+-+Jobs+Tree.pdf"));
					player.spigot().sendMessage(msgl);
				} else if(current.getEnchantments().size() == 0) OpenUnitChangeJobsMenu(player, Jobs.get(current.getItemMeta().getDisplayName().replace("§a", "")));
				return true;
			}
			
			if(changeOk().contains(inv)) {
				Jobs jobs = Jobs.values()[MenuJobschange.indexOf(inv)];
				if(getOk(jobs, true).equals(current)) {
					PlayerJobs pj = new PlayerJobs(player);
					pj.add(jobs);
					pj.close();
					try {
						Economy.subtract(player.getUniqueId(), BigDecimal.valueOf(pj.get().getPrice()));
						ItemStack x = jobs.getItemCost();
						int i = 0;
						while(i < player.getInventory().getSize()) {
							if(player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() == x.getType()) {
								int amount = player.getInventory().getItem(i).getAmount() - x.getAmount();
								int iamount = x.getAmount();
								x.setAmount(amount);
								player.getInventory().setItem(i, x);
								player.updateInventory();
								x.setAmount(iamount);
								OpenChangeJobsMenu(player);
								return true;
							}
							i++;
						}
							
					} catch (NoLoanPermittedException e) {
						e.printStackTrace();
					} catch (ArithmeticException e) {
						e.printStackTrace();
					} catch (UserDoesNotExistException e) {
						e.printStackTrace();
					} catch (MaxMoneyException e) {
						e.printStackTrace();
					}
							
				} else if(Retour.equals(current)) OpenChangeJobsMenu(player);
				return true;
			}
			
		
		return false;
		
		
	}
	
	private static ItemStack indisponible = Item.GiveItem(Material.BARRIER, 1, "§4Impossible de charger votre page", "§4Recharger là plus tard");
	private static ItemStack destruction = Item.GiveItem(Material.RED_BANNER, 1, "§4Suprimé votre shop", null);
	private static ItemStack disponible(Jobs jobs, int slot) {
		return Item.GiveItemLore(Material.GREEN_STAINED_GLASS_PANE, 1, "§6Emplacement §d" + slot, Arrays.asList("§aDisponible", "§dPrix : " + jobs.getHouseData().getHousePrice()));
	}

	@SuppressWarnings("deprecation")
	private static void OpenHouseBuyLots(Player player) {
		player.closeInventory();
		PlayerJobs jobs = new PlayerJobs(player);
		int lenth = HouseList.getSlots();
		double a = Double.valueOf(lenth / 9.0);
		BigDecimal bd = new BigDecimal(a);
		bd = bd.setScale(0, BigDecimal.ROUND_UP);
		int in = (bd.intValue()*9) + 18;
		if(in < 28) in = 27;
		Inventory e = Bukkit.createInventory(player, in, "Acheter votre shop");
		player.openInventory(e);
		JobsInventory.add(e);
		Item.GrayExGlass(e, in);
		
		e.setItem(9, indisponible);
		e.setItem(in - 5, destruction);
		e.setItem(in - 1, Retour);
		ItemMeta meta;
		ItemStack f = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		meta = f.getItemMeta();
		meta.setCustomModelData(121);
		meta.setLore(Arrays.asList("§4Indisponible ", "§dCette espace appartient a un joueur"));
		f.setItemMeta(meta);
		int t = 8;
		
		
		for(Location loc : HouseData.getLoc()) {
			t++;
			if(HouseData.isEnable(loc)) e.setItem(t, disponible(jobs.get(), HouseData.getSlots(loc)));
			else {
				meta = f.getItemMeta();
				Location temp = new PlayerJobs(player).getHouse();
				if(temp != null && temp.equals(loc)) {
					meta.addEnchant(Enchantment.MENDING, 1, false);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					meta.setLore(Arrays.asList("§4Indisponible ", "§dCette espace est à vous"));
				}
				meta.setDisplayName("§6Emplacement §d" + HouseData.getSlots(loc));
				f.setItemMeta(meta);
				e.setItem(t, f);
			}
		}
		
		
	}
	
	public static boolean delInventory(Inventory inv) {
		return JobsInventory.remove(inv);
	}
}
