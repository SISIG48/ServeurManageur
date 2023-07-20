package fr.sisig48.pl.Economie;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.noise.OctaveGenerator;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Menu.Interface;
import fr.sisig48.pl.Sociale.PlayerRank;
import fr.sisig48.pl.Utils.Item;
import fr.sisig48.pl.Utils.Uconfig;
import net.ess3.api.Economy;

public class LootBox {
	protected final static ArrayList<String> Rmythique = new ArrayList<String>(), Rlegend = new ArrayList<String>(), Rrare = new ArrayList<String>(), Runcommon = new ArrayList<String>(), Rcommon = new ArrayList<String>();
	private static String path = "nextGive";
	private final static int nextIn = Calendar.DAY_OF_MONTH;
	private final static int nextInCount = 8;
	final static Uconfig config = LootBoxMenu.config;
	private static Date nextMonthDate = configDate();
	private static long dayRemaining = initUpdate();
	protected final static String pathActual = "loot.this";
	protected final static String pathPossible = "loot.possible";
	protected final static String split = "%";
	
	public static void openInv(Player player) {
		LootBoxMenu.open(player, dayRemaining);
	}
	
	public static void openLootBox(Player player, int lootNum) {
		LootBoxMenu.openLootBox(player, lootNum);
	}
	
	
	//Detection du prochain mois
	@SuppressWarnings("deprecation")
	private static Date configDate() {
		init();
		String dateString = config.get(path);
        String pattern = "yyyy-MM-dd";
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if(!config.AlreadySet(path) || dateString == null) {
        	Calendar calendar = Calendar.getInstance();
        	calendar.add(Calendar.MONTH, 1);
            calendar.add(nextIn, nextInCount);
            String date = (calendar.getTime().getYear() + 1900) + "-" + calendar.getTime().getMonth() + "-" + calendar.getTime().getDate();
    		config.set(path, date);
            return calendar.getTime();
        }
        
        try {return dateFormat.parse(dateString);} catch (ParseException e) {e.printStackTrace();}
        return null;
	}
	
	private static void init() {
		if(config.AlreadySet(pathActual + ".1")) {
			for (String value : config.getConfig().getConfigurationSection(pathActual).getKeys(false)) {
				for (String type : config.getConfig().getConfigurationSection(pathActual + "." + value).getKeys(false)) {
					if(value.equals("1")) Rcommon.add(type + split + config.get(pathActual + "." + value + "." + type));
					if(value.equals("2")) Runcommon.add(type + split + config.get(pathActual + "." + value + "." + type));
					if(value.equals("3")) Rrare.add(type + split + config.get(pathActual + "." + value + "." + type));
					if(value.equals("4")) Rlegend.add(type + split + config.get(pathActual + "." + value + "." + type));
					if(value.equals("5")) Rmythique.add(type + split + config.get(pathActual + "." + value + "." + type));
				}
			}
		}
	}

	//Vérification fu prochain mois
	private static long initUpdate() {
        // Calculer le nombre de jours restants
        long milliseconds = nextMonthDate.getTime() - Calendar.getInstance().getTime().getTime();
        long daysRemaining = milliseconds / 86400000;
        
        if (daysRemaining > 0) return daysRemaining;
        else resetBox();
        return 30;
	}
	
	//Re planification de la box
	@SuppressWarnings("deprecation")
	public static void resetBox() {
		Calendar calendar = Calendar.getInstance();
        
        // Définir la date pour le prochain mois
		calendar.add(Calendar.MONTH, 1);
        calendar.add(nextIn, nextInCount);
        nextMonthDate = calendar.getTime();
		String date = (nextMonthDate.getYear() + 1900) + "-" + nextMonthDate.getMonth() + "-" + nextMonthDate.getDate();
		config.set(path, date);
		for(OfflinePlayer p : Bukkit.getOfflinePlayers()) {
			config.set("players." + p.getUniqueId().toString(), "1");
		}
		
		//Calcule des states loot box
		int nbMythique = getRandomNumber(6);
		int nbLegend = 1 + getRandomNumber(4);
		int nbRare = 2 + getRandomNumber(2);
		int nbUnCommon = 4 + getRandomNumber(3);
		int nbCommon = 15 - (nbUnCommon + nbLegend + nbMythique + nbRare);
		
		//Variable de trie
		ArrayList<String> mythique = new ArrayList<String>();
		ArrayList<String> legend = new ArrayList<String>();
		ArrayList<String> rare = new ArrayList<String>();
		ArrayList<String> uncommon = new ArrayList<String>();
		ArrayList<String> common = new ArrayList<String>();
		
		//Reset var
		Rmythique.clear();
		Rlegend.clear();
		Rrare.clear();
		Runcommon.clear();
		Rcommon.clear();
		
		
		//trie des valeur
		config.set(pathActual, "");
		if(!config.AlreadySet(pathPossible)) config.set(pathPossible, "");
		for (String type : config.getConfig().getConfigurationSection(pathPossible).getKeys(false)) {
			for (String value : config.getConfig().getConfigurationSection(pathPossible + "." + type).getKeys(false)) {
				if(value.equals("1")) common.add(type + split + config.get(pathPossible + "." + type + "." + value));
				if(value.equals("2")) uncommon.add(type + split + config.get(pathPossible + "." + type + "." + value));
				if(value.equals("3")) rare.add(type + split + config.get(pathPossible + "." + type + "." + value));
				if(value.equals("4")) legend.add(type + split + config.get(pathPossible + "." + type + "." + value));
				if(value.equals("5")) mythique.add(type + split + config.get(pathPossible + "." + type + "." + value));
				
			}
		}
				
		String temp;
		//Prise aléatoire
		for(int i = 0; i < nbMythique; i++) {
			while(Rmythique.contains(temp = mythique.get((int) (Math.random() * (mythique.size() - 1)))));
			Rmythique.add(temp);
		}
		
		for(int i = 0; i < nbLegend; i++) {
			while(Rlegend.contains(temp = legend.get((int) (Math.random() * (legend.size() - 1)))));
			Rlegend.add(temp);
		}
		
		for(int i = 0; i < nbRare; i++) {
			while(Rrare.contains(temp = rare.get((int) (Math.random() * (rare.size() - 1)))));
			Rrare.add(temp);
		}
		
		for(int i = 0; i < nbUnCommon; i++) {
			while(Runcommon.contains(temp = uncommon.get((int) (Math.random() * (uncommon.size() - 1)))));
			Runcommon.add(temp);
		}
		
		for(int i = 0; i < nbCommon; i++) {
			while(Rcommon.contains(temp = common.get((int) (Math.random() * (common.size() - 1)))));
			Rcommon.add(temp);
		}
		
		//Import dans les config
		for(String t : Rcommon) config.set(pathActual + ".1." + t.split(split)[0], t.split(split)[1]);
		for(String t : Runcommon) config.set(pathActual + ".2." + t.split(split)[0], t.split(split)[1]);
		for(String t : Rrare) config.set(pathActual + ".3." + t.split(split)[0], t.split(split)[1]);
		for(String t : Rlegend) config.set(pathActual + ".4." + t.split(split)[0], t.split(split)[1]);
		for(String t : Rmythique) config.set(pathActual + ".5." + t.split(split)[0], t.split(split)[1]);
	}
	
	
	protected static int getRandomNumber(int chanceOutOf) {
        Random random = new Random();
        int randomNumber = random.nextInt(chanceOutOf); // Génère un nombre entre 0 (inclus) et chanceOutOf (exclus)
        if(randomNumber == 0) return 1;
        return 0;
    }
	public static int getLootBox(ItemStack it) {
		return LootBoxMenu.getLootBox(it);
	}
}



class LootBoxMenu extends LootBox {
	final static int RankPaysan = 1, RankEcuyer = 2, RankChevalier = 3, RankSeigneur = 4, RankModo = 9;
	private final static int PricePaysan = 100, PriceEcuyer = 200, PriceChevalier = 300, PriceSeigneur = 400;
	private final static ItemStack paysan = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aLoot box", Arrays.asList("§8" + PlayerRank.getRank(RankPaysan), "§dPrix : " + PricePaysan).toArray(new String[0]), 134);
	private final static ItemStack ecuyer = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aLoot box", Arrays.asList("§8" + PlayerRank.getRank(RankEcuyer), "§dPrix : " + PriceEcuyer).toArray(new String[0]), 134);
	private final static ItemStack chevalier = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aLoot box", Arrays.asList("§8" + PlayerRank.getRank(RankChevalier), "§dPrix : " + PriceChevalier).toArray(new String[0]), 134);
	private final static ItemStack seigneur = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aLoot box", Arrays.asList("§8" + PlayerRank.getRank(RankSeigneur), "§dPrix : " + PriceSeigneur).toArray(new String[0]), 134);
	private final static ItemStack modo = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aCrée un cadeau", Arrays.asList("§8" + PlayerRank.getRank(RankModo), "?").toArray(new String[0]), 134);
	
	private static File file = initFile();
	final static Uconfig config = new Uconfig(file);
	
	private final static String percentPath = "%.?.!";
	
	
	public static void open(Player p, long dayRemaining) {
		String path = "players." + p.getUniqueId().toString();
		if(config.get(path) == null) config.set(path, "1");
		int result = Integer.valueOf(config.get(path));
		int size = 27;
		Inventory e = Bukkit.createInventory(p, size, "LootBox");
		p.openInventory(e);
		Interface.inventory.add(e);
		GrayExGlass(e, size);
		int pr = PlayerRank.getPlayerInt(p);
		
		ItemStack it;  
		if(result == 1) it = Item.GiveItem(Material.CHEST, 1, "§aLoot box gratuite", "§e[RECUPERER]", 133);
		else it = Item.GiveItem(Material.CHEST, 1, "§4Loot box gratuite", "§dDans : " + dayRemaining + " jours", 125);
		
		e.setItem(4, it);
		e.setItem((9 + RankPaysan), paysan); //Paysan
		e.setItem((9 + RankEcuyer), ecuyer); //Ecuyer
		e.setItem((9 + RankChevalier), chevalier); //Chevalier
		e.setItem((9 + RankSeigneur ), seigneur); //Seigneur
		if(pr == RankModo) e.setItem((9 + RankModo), modo); //Modo

	}
	
	public static int getLootBox(ItemStack it) {
		if(it.equals(paysan)) return RankPaysan;
		if(it.equals(ecuyer)) return RankEcuyer;
		if(it.equals(chevalier)) return RankChevalier;
		if(it.equals(seigneur)) return RankSeigneur;
		if(it.equals(modo)) return RankModo;
		return 0;
	}
	
	@SuppressWarnings("deprecation")
	public static void openLootBox(Player player, int lootNum) {
		if(PlayerRank.getPlayerInt(player) < lootNum) return;
		try {
			if(lootNum == 0) config.set("players." + player.getUniqueId().toString(), "0");
			if(lootNum == 1) if(!EconomieESS.HasEnought(player, PricePaysan)) return; else Economy.subtract(player.getUniqueId(), new BigDecimal(PricePaysan));
			if(lootNum == 2) if(!EconomieESS.HasEnought(player, PriceEcuyer)) return; else Economy.subtract(player.getUniqueId(), new BigDecimal(PriceEcuyer));
			if(lootNum == 3) if(!EconomieESS.HasEnought(player, PriceChevalier)) return; else Economy.subtract(player.getUniqueId(), new BigDecimal(PriceChevalier));
			if(lootNum == 4) if(!EconomieESS.HasEnought(player, PriceSeigneur)) return; else Economy.subtract(player.getUniqueId(), new BigDecimal(PriceSeigneur));
		} catch (Exception e) {return;}
		
		
		float r = new Random().nextFloat(100);
		float per = getPercent(lootNum, 1);
		int i = 1;
		for(i = 2 ; (r > per && i != 5) ; i++) per = per + getPercent(lootNum, i);
		spinLoot(player, i-1);
	}
	
	private static int temp = 0;
	private static BukkitRunnable rouletteTask;
	private static void spinLoot(Player p,  int value) {
		Inventory e = Bukkit.createInventory(p, 9, "");
		GrayExGlass(e, 9);
		
		String lineOBJ = " " + split + " ";
		String name = "loretag";
		
		if(value == 1 && (name = "§dCommun") != null) lineOBJ = Rcommon.get((int) (Math.random() * (Rcommon.size() - 1)));
		if(value == 2 && (name = "§dPas-Commun") != null) lineOBJ = Runcommon.get((int) (Math.random() * (Runcommon.size() - 1)));
		if(value == 3 && (name = "§dRare") != null) lineOBJ = Rrare.get((int) (Math.random() * (Rrare.size() - 1)));
		if((value == 4 || (value == 5 && Rmythique.size() == 0))  && (name = "§dLegendaire") != null) lineOBJ = Rlegend.get((int) ((Math.random() * (Rlegend.size() - 1))));
		if(value == 5 && Rmythique.size() != 0 && (name = "§dMythique") != null) lineOBJ = Rmythique.get((int) (Math.random() * (Rmythique.size() - 1)));
		
		String[] info = lineOBJ.split(split);
		String m = info[0].replace(" ", "_");
		m = m.toUpperCase();
		
		
		Material material = Material.getMaterial(m);
		int amount = Integer.valueOf(info[1]);
		ItemStack is = Item.GiveItemLore(material, 1, "§" + value + m, Arrays.asList(name, "§8x" + amount).toArray(new String[0]), 125);
		
		p.openInventory(e);				
		ArrayList<String> li = new ArrayList<>();
		li.addAll(Rcommon);
		li.addAll(Rlegend);
		li.addAll(Rrare);
		li.addAll(Runcommon);
		li.addAll(Rmythique);
		temp = li.size() - 1;
		
		rouletteTask = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(temp > 0) spiner(e, li.get(temp));
				temp--;
				p.playNote(p.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Tone.A));
				if(temp < 0) {
					e.setItem(4, is);
					p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 100, 100);
					rouletteTask.cancel();
				}
				
			}
		};
		int speed = 2;
		rouletteTask.runTaskTimer(Main.Plug, speed, speed);
		
					
				
				
		p.getInventory().addItem(new ItemStack(material, amount));
		p.updateInventory();

		
		
	}
	
	private static void spiner(Inventory e, String t) {
		String[] info = t.split(split);
		String m = info[0].replace(" ", "_");
		m = m.toUpperCase();
			
		Material material = Material.getMaterial(m);
		ItemStack is = Item.GiveItem(material, 1, " ", null, 125);
		e.setItem(4, is);
			
	}
	
	
	private static float getPercent(int lootbox, int level) {
		String conf = percentPath.replace("?", String.valueOf(lootbox)).replace("!", String.valueOf(level));
		if(!config.AlreadySet(conf)) config.set(conf, "0");
		return Float.valueOf(config.get(conf));
	}
	
	
	
	
	//
	private static void GrayExGlass(Inventory e, int in) {
		ItemStack it = Item.GiveItem(Material.GRAY_STAINED_GLASS_PANE, 1, " ", null, 125);
		e.setItem(0, it); e.setItem(1, it); e.setItem(2, it); e.setItem(3, it); e.setItem(4, it); e.setItem(5, it); e.setItem(6, it); e.setItem(7, it); e.setItem(8, it);
		e.setItem(in - 9, it); e.setItem(in - 8, it); e.setItem(in - 7, it); e.setItem(in - 6, it); e.setItem(in - 5, it); e.setItem(in - 4, it); e.setItem(in - 3, it); e.setItem(in - 2, it); e.setItem(in - 1, it);
	}

		
	private static File initFile() {
		File file = new File("plugins/ServeurManageur/lootBox.yml");
		if(!file.exists())try {file.createNewFile();} catch (IOException e) {e.printStackTrace();}
		return file;
	}
	
	static void init() {
		config.saving();
	}
}