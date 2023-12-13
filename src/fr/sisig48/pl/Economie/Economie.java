package fr.sisig48.pl.Economie;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Item;
import fr.sisig48.pl.Utils.Uconfig;
import net.ess3.api.MaxMoneyException;

public class Economie {
	public static boolean sellItemMarket(Player p, ItemStack it, float price) {
		try {
			//Récupère la taxe
			BigDecimal taxe = new BigDecimal(getItemPriceSellMarket(price));
			
			//Vérifie si le joueur est en possession de cette item et de la taxe
			if(!Economy.hasEnough(p.getUniqueId(), taxe)) {
				p.sendMessage("§4§lArgent insuffisant");
				return false;
			}
			
			//Taxe le joueur
			Economy.subtract(p.getUniqueId(), taxe);
			
			//Ajoute l'item aux marchant
			EconomieMarchant.sellItem(it, price, p);
		} catch (NoLoanPermittedException | ArithmeticException | UserDoesNotExistException | MaxMoneyException e) {return false;}

		return true;
	}
	
	public static boolean buyItemMarket(Player p, ItemStack it, UUID sell) {
		
		try {
			//Récupère le prix
			BigDecimal price = new BigDecimal(getItemPriceBuyMarket(it, sell));
			
			//Récupère l'annonce
			EconomieMarchant em = EconomieMarchant.getSell(sell, it);
			if(em.getItem().getType().equals(Material.AIR)) return false;
			
			//Vérifie l'argent du joueur et l'etat de son inventaire
			if(!Economy.hasEnough(p.getUniqueId(), price)) {
				p.sendMessage("§4§lArgent insuffisant");
				return false;
			}
			if(Item.isInventoryFull(p.getInventory())) {
				p.sendMessage("§4§lEspace insuffisant");
				return false;
			}			
			//Suprime l'annonce
			float i = em.getPrice();
			ItemStack item = em.getItem();
			EconomieMarchant.buyItem(em);
			
			//Modifie le prix moyen
			ObjectPrice.addObject(item, price.floatValue());
			
			//Fait payer le joueur
			Economy.subtract(p.getUniqueId(), price);
			
			//Met dans l'inventaire du joueur
			p.getInventory().addItem(item);
			
			//Paye le joueur qui envoie l'anonce
			OfflinePlayer sender = em.getPlayer();
			new PlayerJobs(sender).MaterialAddXp(item);
			Economy.add(sender.getUniqueId(), new BigDecimal(i));
			if(sender.isOnline()) sender.getPlayer().sendMessage("§a§lUn joueur vous a acheter : §d" + item.getType().name().replace("_", " ").toLowerCase() + " x" + item.getAmount() + "§a§l pour §4" +  Economie.roundPrice(i));
			return true;
		} catch (ArithmeticException | UserDoesNotExistException | NoLoanPermittedException | MaxMoneyException e) {return false;}
		
	}
	
	public static boolean sellItemShop(ItemStack it, Player p) {
		try {
			ItemStack item = new ItemStack(it.getType(), it.getAmount());
			
			//Vérie si le joueur est en posséssion de cet item
			if(!Arrays.asList(p.getInventory().getStorageContents()).contains(item)) {
				p.sendMessage("§4§lVous ne possédez pas cet item §8(x" + it.getAmount() +")");
				return false;
			}
			
			//Récupère le prix modifier
			BigDecimal price = new BigDecimal(getItemPriceSellShop(it.getType())).multiply(new BigDecimal(it.getAmount()));
			
			//Suprime l'item au joueur
			p.getInventory().removeItem(item);
			
			//Xp
			new PlayerJobs(p).MaterialAddXp(item);
			
			//Paye le joueur
			Economy.add(p.getUniqueId(), price);
			p.sendMessage("§a§lVous avez gagner : §d" + Economie.roundPrice(price.floatValue()));
			return true;
		} catch (ArithmeticException | UserDoesNotExistException | NoLoanPermittedException | MaxMoneyException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public static boolean buyItemShop(ItemStack it, Player p) {
		try {
			//Récupère le prix
			BigDecimal price = new BigDecimal(getItemPriceBuyShop(it.getType())).multiply(new BigDecimal(it.getAmount()));
			
			//Vérifie l'argent du joueur et la place dans son inventaire
			if(!Economy.hasEnough(p.getUniqueId(), price)) {
				p.sendMessage("§4§lArgent insuffisant");
				return false;
			}
			if(Item.isInventoryFull(p.getInventory())) {
				p.sendMessage("§4§lEspace insuffisant");
				return false;
			}
			
			//Retire l'argent au joueur
			Economy.subtract(p.getUniqueId(), price);
			
			//Modifie la moyenne
			ObjectPrice.addObject(new ItemStack(it.getType(), it.getAmount()/2), price.floatValue()/2);
			
			//Ajoute l'item au joueur
			p.getInventory().addItem(new ItemStack(it.getType(), it.getAmount()));
			return true;
		} catch (ArithmeticException | UserDoesNotExistException | NoLoanPermittedException | MaxMoneyException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static float roundPrice(float price) {
		return Float.valueOf(new DecimalFormat("#.##").format(price).replace(",", "."));
	}
	
	private static float getItemPriceSellMarket(float price) {
		return EconomieMarchant.sellTaxe(price);
	}
	
	private static float getItemPriceBuyMarket(ItemStack it, UUID sell) {
		return EconomieMarchant.buyPrice(EconomieMarchant.getSell(sell, it).getPrice());
	}
	
	private final static float baseShop = Float.valueOf(Uconfig.getConfig("shop.shop.base").replace("%", "")) / 100;
	private final static float baseShopSell = baseShop * (Float.valueOf(Uconfig.getConfig("shop.shop.sell").replace("%", "")) / 100);
	private final static float baseShopBuy = (float) (baseShop+1) * (Float.valueOf(Uconfig.getConfig("shop.shop.buy").replace("%", "")) + 100) / 100;
	public static float getItemPriceSellShop(Material it) {
		return ObjectPrice.getObject(it).getPrice() * baseShopSell;
	}
	
	public static float getItemPriceBuyShop(Material it) {
		return ObjectPrice.getObject(it).getPrice() * baseShopBuy;
	}
	
}
