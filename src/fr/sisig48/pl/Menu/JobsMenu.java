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

import fr.sisig48.pl.JobsHouse.HouseData;
import fr.sisig48.pl.JobsHouse.HouseList;
import fr.sisig48.pl.JobsHouse.HouseListInfo;
import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Item;
import net.ess3.api.MaxMoneyException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class JobsMenu {
	public static ArrayList<Inventory> JobsInventory = new ArrayList<Inventory>();
	private static ArrayList<String> JobsPaper = new ArrayList<String>();
	public static void OpenJobsMenu(Player player) {
		Inventory e = Bukkit.createInventory(player, 27, "Jobs Office");
		player.openInventory(e);
		JobsInventory.add(e);
		PlayerJobs pj = new PlayerJobs(player);
		ItemStack it;
		String[] lore = {"Votre métier est : §4" + pj.get().getJobs().getName(), "Votre xp : §4" + String.valueOf(pj.getXp())};
		it = Item.GiveOwnsPlayerHead(1, "§aMon métier", lore, player.getName(), 125);
		e.setItem(11, it);

		it = Item.GiveItem(Material.IRON_PICKAXE, 1, "§aChanger de métier", "Changé mon métier", 129);
		ItemMeta meta = it.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		it.setItemMeta(meta);
		
		e.setItem(13, it);
		
		it = Item.GiveItem(Material.GOLDEN_APPLE, 1, "Autre Joueur", "Afficher les métier des autre joueur", 129);
		e.setItem(15, it);
		
		GrayExGlass(e, 27);
		
		it = Item.GiveItem(Material.OAK_PLANKS, 1, "Accha shop", null, 121);
		e.setItem(22, it);
	}
	
	public static void OpenUnitChangeJobsMenu(Player player, Jobs job, int i) {
		JobsInventory.clear();
		Inventory e = Bukkit.createInventory(player, 27, job.getName());
		player.openInventory(e);
		JobsInventory.add(e);
		PlayerJobs pj = new PlayerJobs(player);
		ItemStack it = null;
		try {
			if(Economy.getMoneyExact(player.getUniqueId()).doubleValue() >= Double.valueOf(job.getPrice() - 1) && player.getInventory().contains(job.getItemCost().getType(), job.getItemCost().getAmount()) && pj.getXp() >= 1000) {
				String[] lores = {"§6Prix : " + job.getPrice(), "§6Cout en item : " + job.getItemCost().getAmount() + " " + job.getItemCost().getType().name(),"§6Cout en Xp : 1000", "§4§lAttention l'xp actuele sera suprimé"};
				it = Item.GiveItemLore(Material.GREEN_WOOL, 1, "§aAccepter", lores, i);
			}
			else {
				String[] lores = {"§4Prix : " + job.getPrice(), "§4Cout en item : " + job.getItemCost().getAmount() + " " + job.getItemCost().getType().name(),"§4Cout en Xp : 1000", "§4§lvous n'avez pas assez"};
				it = Item.GiveItemLore(Material.GREEN_WOOL, 1, "§aAccepter", lores, 125);
			}
			if(new PlayerJobs(player).get() == job) it = Item.GiveItem(Material.ORANGE_WOOL, 1, "§6C'est déja votre métier", "§4§lAction imposible", 125);;
		} catch (UserDoesNotExistException e1) {
			e1.printStackTrace();
		}
		e.setItem(11, it);
		
		it = Item.GiveItem(Material.RED_WOOL, 1, "§4Refuser", "Retour en arière", i);
		e.setItem(15, it);
		
		GrayExGlass(e, 27);	
	}
	@SuppressWarnings("deprecation")
	public static void OpenChangeJobsMenu(Player player) {
		PlayerJobs jobs = new PlayerJobs(player);
		ItemStack it = new ItemStack(Material.BARRIER);
		int lenth = 0;
		for(Jobs j : Jobs.All) if(j.isEnable() && jobs.canChangeFor(j)) lenth++;
		double a = Double.valueOf(lenth / 9.0);
		BigDecimal bd = new BigDecimal(a);
		bd = bd.setScale(0, BigDecimal.ROUND_UP);
		int in = (bd.intValue()*9) + 18;
		if(in < 28) in = 27;
		Inventory e = Bukkit.createInventory(player, in, "Jobs Office");
		player.openInventory(e);
		JobsInventory.add(e);
		
		
		GrayExGlass(e, in);
		it = Item.GiveItem(Material.BELL, 1, "§aAfficher l'arbre des métiers", null, 129);
		e.setItem(in-1, it);
		
		int i = 8;
		for(Jobs j : Jobs.All) {
			if(j.isEnable() && (jobs.canChangeFor(j.getJobs()) || j.getJobs().equals(jobs.get()))) {
				i++;
				JobsPaper.add(String.valueOf(j.getJobs() + "/" + (120 + i)));
				if(j.getJobs() == jobs.get().getJobs()) {
					
					it = Item.GiveItem(Material.PAPER, 1, "§a" + j.getName() , null, (120 + i));
					ItemMeta meta = it.getItemMeta();
					meta.addEnchant(Enchantment.LOYALTY, 1, false);
					meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
					it.setItemMeta(meta);
					e.setItem(i, it);
				} else {
					it = Item.GiveItem(Material.PAPER, 1, "§a" + j.getName() , null, (120 + i));
					e.setItem(i, it);
				}
			}
		}

	}
	
	@SuppressWarnings({"incomplete-switch", "unchecked", "deprecation"})
	public static boolean TcheckJobsMenuAction(Player player, ItemStack current) {
			ArrayList<String> JobPaper = (ArrayList<String>) JobsPaper.clone();
			switch(current.getType()) {
			case IRON_PICKAXE :
				if(current.getItemMeta().getCustomModelData() == 129) {
					OpenChangeJobsMenu(player);
					return true;
				}
			case GOLDEN_APPLE :
				if(current.getItemMeta().getCustomModelData() == 129) {
					return true;
				}
			case PAPER :
				if(current.getItemMeta().getCustomModelData() > 128 && current.getItemMeta().getCustomModelData() < 151) {
					for(String t : JobPaper) {
						String[] ts = t.split("\\/");
						if(current.getItemMeta().getCustomModelData() == Integer.valueOf(ts[1])) OpenUnitChangeJobsMenu(player, Jobs.valueOf(ts[0]), Integer.valueOf(ts[1]));
					}
					return true;
				}
			case GREEN_WOOL :
				if(current.getItemMeta().getCustomModelData() > 128 && current.getItemMeta().getCustomModelData() < 151) {
					for(String t : JobPaper) {
						String[] ts = t.split("\\/");
						if(current.getItemMeta().getCustomModelData() == Integer.valueOf(ts[1])) {
							PlayerJobs pj = new PlayerJobs(player);
							pj.add(Jobs.valueOf(ts[0]));
							pj.close();
							try {
								Economy.subtract(player.getUniqueId(), BigDecimal.valueOf(pj.get().getPrice()));
								ItemStack x = Jobs.valueOf(ts[0]).getItemCost();
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
							
						}
					}
					return true;
				}
			case RED_WOOL :
				if(current.getItemMeta().getCustomModelData() > 128 && current.getItemMeta().getCustomModelData() < 151) {
					for(String t : JobPaper) {
						String[] ts = t.split("\\/");
						if(current.getItemMeta().getCustomModelData() == Integer.valueOf(ts[1])) OpenChangeJobsMenu(player);
					}
					return true;
				}
			case BELL :
				if(current.getItemMeta().getCustomModelData() == 129) {
					player.closeInventory();
					TextComponent msgl = new TextComponent("§e[§e§lAFFICHER L'ARBRE DES METIERS§e]");
					msgl.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eVOIR").create()));
					msgl.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://download1588.mediafire.com/kvoquicoqzhg3cQArRKdd9nkaMpRBPOz_H31Y6HZbFUyteP89jLY4kE7m3X_w0E-bzhLNrZNwHr78L0PexYHzqXPr2E/07cclf8s48jz1pm/Dragons+Economy+-+Jobs+Tree.pdf"));
					player.spigot().sendMessage(msgl);
					return true;
				}
				break;
			case OAK_PLANKS :
				OpenHouseBuyLots(player);
				return true;
			case GREEN_STAINED_GLASS_PANE :
				PlayerJobs pj = new PlayerJobs(player);
				try {
					if(pj.getHouse() == null && Economy.hasMore(player.getName(), pj.get().getHouseData().getHousePrice())) {
						current.setType(Material.ORANGE_STAINED_GLASS_PANE);
						ItemMeta meta = current.getItemMeta();
						meta.setLore(Arrays.asList("§eConfirmé", meta.getLore().get(0), meta.getLore().get(1)));
						current.setItemMeta(meta);
					}
				} catch (UserDoesNotExistException e) {e.printStackTrace();}
				return true;
			case ORANGE_STAINED_GLASS_PANE :
				String[] title = current.getItemMeta().getDisplayName().split(" §d");
				HouseListInfo hl = HouseList.getHouseBySlot(Integer.valueOf(title[1]));
				if(title[0].equalsIgnoreCase("§6Emplacement")) new PlayerJobs(player.getPlayer()).addHouse(hl.getLocation());
				current.setType(Material.RED_STAINED_GLASS_PANE);
				ItemMeta meta = current.getItemMeta();
				meta.setCustomModelData(125);
				meta.setLore(Arrays.asList("§4Indisponible ", "§dCette espace appartient a un joueur"));
				meta.addEnchant(Enchantment.MENDING, 1, false);
				current.setItemMeta(meta);
				hl.isEnable(false);
				try {Economy.subtract(player.getName(), new PlayerJobs(player).get().getHouseData().getHousePrice());} catch (NoLoanPermittedException | UserDoesNotExistException | MaxMoneyException e) {e.printStackTrace();}
				player.closeInventory();
				player.teleport(hl.getLocation());
				return true;
				
			case RED_STAINED_GLASS_PANE :
				String[] ti = current.getItemMeta().getDisplayName().split(" §d");
				if(ti[0].equalsIgnoreCase("§6Emplacement")) {
					HouseListInfo h = HouseList.getHouseBySlot(Integer.valueOf(ti[1]));
					Location loc = h.getLocation();
					loc.add(-1, 3, -1);
					player.teleport(loc);
				}
				return true;
			case RED_BANNER : 
				new PlayerJobs(player).delHouse();
				player.sendMessage("§4Votre maison a été détruitre");
				player.closeInventory();
				return true;
			}
		
		return false;
		
		
	}
	
	private static void GrayExGlass(Inventory e, int in) {
		ItemStack it;
		it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(in - 9, it); e.setItem(in - 8, it); e.setItem(in - 7, it); e.setItem(in - 6, it); e.setItem(in - 5, it); e.setItem(in - 4, it); e.setItem(in - 3, it); e.setItem(in - 2, it); e.setItem(in - 1, it);
		
	}
	
	@SuppressWarnings("deprecation")
	private static void OpenHouseBuyLots(Player player) {
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
		GrayExGlass(e, in);
		
		ItemStack i = new ItemStack(Material.BARRIER);
		ItemMeta meta = i.getItemMeta();
		meta.setCustomModelData(125);
		meta.setDisplayName("§4Imposible de charger votre page");
		meta.setLore(Arrays.asList("§4Recharger là plus tard"));
		i.setItemMeta(meta);
		e.setItem(9, i);

		ItemStack del = new ItemStack(Material.RED_BANNER);
		meta = del.getItemMeta();
		meta.setCustomModelData(121);
		meta.setDisplayName("§4Suprimé votre shop");
		meta.setLore(Arrays.asList(""));
		del.setItemMeta(meta);
		e.setItem(in - 5, del);
		
		ItemStack it = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		meta = it.getItemMeta();
		meta.setCustomModelData(121);
		meta.setLore(Arrays.asList("§aDisponible ", "§dPrix : " + jobs.get().getHouseData().getHousePrice()));
		it.setItemMeta(meta);
		
		ItemStack f = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		meta = f.getItemMeta();
		meta.setCustomModelData(121);
		meta.setLore(Arrays.asList("§4Indisponible ", "§dCette espace appartient a un joueur"));
		f.setItemMeta(meta);
		int t = 8;
		
		
		for(Location loc : HouseData.getLoc()) {
			t++;
			if(HouseData.isEnable(loc)) {
				meta = it.getItemMeta();
				meta.setDisplayName("§6Emplacement §d" + HouseData.getSlots(loc));
				it.setItemMeta(meta);
				e.setItem(t, it);
			} else {
				meta = f.getItemMeta();
				Location temp = new PlayerJobs(player).getHouse();
				if(temp != null && temp.equals(loc)) {
					meta.addEnchant(Enchantment.MENDING, 1, true);
					meta.setLore(Arrays.asList("§4Indisponible ", "§dCette espace est à vous"));
				}
				meta.setDisplayName("§6Emplacement §d" + HouseData.getSlots(loc));
				f.setItemMeta(meta);
				e.setItem(t, f);
			}
		}
		
		
	}
}
