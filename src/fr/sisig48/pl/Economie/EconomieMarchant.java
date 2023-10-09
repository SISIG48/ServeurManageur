package fr.sisig48.pl.Economie;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Automating.AutoSave;
import fr.sisig48.pl.Utils.Uconfig;

@SuppressWarnings("unchecked")
public class EconomieMarchant implements Serializable {
	private static final long serialVersionUID = 4204848563557493791L;
	private static ArrayList<EconomieMarchant> marchant = new ArrayList<EconomieMarchant>();
	private static File saves = new File(Main.Plug.getDataFolder() + "/data/item/marchantSell.save");
	static {
		if(saves.exists()) {
			try {
				FileInputStream fichierEntree = new FileInputStream(saves);
				ObjectInputStream entreeObjet = new ObjectInputStream(fichierEntree);
				
				marchant = (ArrayList<EconomieMarchant>) entreeObjet.readObject();
				
				entreeObjet.close();
				fichierEntree.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private static float baseMarchantSell = baseMarchantSell();
	private static float baseMarchantBuy = baseMarchantBuy();
	
	private Material type = Material.AIR;
	private int amount = 0;
	
	private float price = 0;
	private UUID uuid = UUID.randomUUID();
	@SuppressWarnings("deprecation")
	private UUID player = Bukkit.getOfflinePlayer("noob").getUniqueId();
	private EconomieMarchant(ItemStack it, float price, OfflinePlayer p) {
		type = it.getType();
		amount = it.getAmount();
		this.price = price;
		player = p.getUniqueId();
		marchant.add(this);
	}
	
	public static void save() {
		AutoSave.SerializableSave(saves, marchant);
	}
	

	private static float baseMarchantSell() {
		return Float.valueOf(Uconfig.getConfig("shop.marchant.sell").replace("%", "")) / 100;
	}
	
	private static float baseMarchantBuy() {
		return (Float.valueOf(Uconfig.getConfig("shop.marchant.buy").replace("%", "")) + 100) / 100;
	}

	public static void sellItem(ItemStack it, float price, OfflinePlayer p) {
		int max = it.getMaxStackSize();
		if(it.getAmount() > max) {
			float prix = price / it.getAmount();
			while(it.getAmount() > 0) {
				ItemStack is = it.clone();
				if(it.getAmount() > - max) is.setAmount(max);
				else is.setAmount(it.getAmount());
				new EconomieMarchant(is, prix * is.getAmount(), p);
				it.setAmount(it.getAmount() - max);
			}
		} else new EconomieMarchant(it, price, p);
	}
	
	public static float sellTaxe(float price) {
		return price * baseMarchantSell;
	}
	
	public static float buyPrice(float price) {
		return price * baseMarchantBuy;
	}
	
	public static void buyItem(EconomieMarchant em) {
		marchant.remove(em);
	}
	
	
	public UUID getUUID() {
		return uuid;
	}
	
	public float getPrice() {
		return price;
	}
	
	public OfflinePlayer getPlayer() {
		return Bukkit.getOfflinePlayer(player);
	}
	
	public ItemStack getItem() {
		return new ItemStack(type, amount);
	}
	
	public static EconomieMarchant getSell(UUID uuid, ItemStack it) {
		for(EconomieMarchant em : marchant) if(em.getUUID().equals(uuid) && em.getItem().equals(it)) return em;
		return null;
	}
	
	public static EconomieMarchant getSell(UUID uuid) {
		for(EconomieMarchant em : marchant) if(em.getUUID().equals(uuid)) return em;
		return null;
	}
	
	public static EconomieMarchant[] getSell() {
		return marchant.toArray(new EconomieMarchant[0]);
	}
	
	public static EconomieMarchant[] getSell(ItemStack it) {
		ArrayList<EconomieMarchant> sell = new ArrayList<EconomieMarchant>();
		for(EconomieMarchant em : marchant) if(em.getItem().equals(it)) sell.add(em);
		return sell.toArray(new EconomieMarchant[0]);
	}
	
	public static EconomieMarchant[] getSell(Material it) {
		ArrayList<EconomieMarchant> sell = new ArrayList<EconomieMarchant>();
		for(EconomieMarchant em : marchant) if(em.getItem().getType().equals(it)) sell.add(em);
		return sell.toArray(new EconomieMarchant[0]);
	}
	
	
	
}
