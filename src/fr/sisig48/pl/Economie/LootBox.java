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
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Menu.Interface;
import fr.sisig48.pl.Menu.LootStorage;
import fr.sisig48.pl.Sociale.PlayerRank;
import fr.sisig48.pl.Utils.Item;
import fr.sisig48.pl.Utils.Uconfig;
import net.ess3.api.Economy;

@SuppressWarnings("deprecation")
public class LootBox {
	protected final static ArrayList<ItemStack> Rmythique = new ArrayList<ItemStack>(), Rlegend = new ArrayList<ItemStack>(), Rrare = new ArrayList<ItemStack>(), Runcommon = new ArrayList<ItemStack>(), Rcommon = new ArrayList<ItemStack>();
	private static String path = "nextGive";
	private final static int nextIn = Calendar.DAY_OF_MONTH;
	private final static int nextInCount = 8;
	final static Uconfig config = LootBoxMenu.config;
	protected final static String pathPossible = "loot.possible";
	protected final static String pathActual = "loot.this";
	protected final static String split = "%";
	private static Date nextMonthDate = configDate();
	private static long dayRemaining = initUpdate();
	
	public static void openInv(Player player) {
		LootBoxMenu.open(player, dayRemaining);
	}
	
	public static void openLootBox(Player player, int lootNum) {
		LootBoxMenu.openLootBox(player, lootNum);
	}
	
	
	//Detection du prochain mois
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
					int amount = Integer.valueOf(config.get(pathActual + "." + value + "." + type));
					if(value.equals("1")) Rcommon.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dCommun", "§8x" + amount).toArray(new String[0])));
					if(value.equals("2")) Runcommon.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dPas commun", "§8x" + amount).toArray(new String[0])));
					if(value.equals("3")) Rrare.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dRare", "§8x" + amount).toArray(new String[0])));
					if(value.equals("4")) Rlegend.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dLegendaire", "§8x" + amount).toArray(new String[0])));
					if(value.equals("5")) Rmythique.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dMythique", "§8x" + amount).toArray(new String[0])));
				}
			}
		}
	}
	
	public static boolean isLoot(ItemStack it) {
		if(Rcommon.contains(it) || Rlegend.contains(it) || Rmythique.contains(it) || Rrare.contains(it) || Runcommon.contains(it)) {
			return true;
		}
		return false;
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
		ArrayList<ItemStack> mythique = new ArrayList<ItemStack>();
		ArrayList<ItemStack> legend = new ArrayList<ItemStack>();
		ArrayList<ItemStack> rare = new ArrayList<ItemStack>();
		ArrayList<ItemStack> uncommon = new ArrayList<ItemStack>();
		ArrayList<ItemStack> common = new ArrayList<ItemStack>();
		
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
			if(config.getConfig().getConfigurationSection(pathPossible + "." + type) == null) continue;
			for (String value : config.getConfig().getConfigurationSection(pathPossible + "." + type).getKeys(false)) {
				int amount = Integer.valueOf(config.get(pathActual + "." + value + "." + type));
				if(value.equals("1")) common.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dCommun", "§8x" + amount).toArray(new String[0])));
				if(value.equals("2")) uncommon.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dPas commun", "§8x" + amount).toArray(new String[0])));
				if(value.equals("3")) rare.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dRare", "§8x" + amount).toArray(new String[0])));
				if(value.equals("4")) legend.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dLegendaire", "§8x" + amount).toArray(new String[0])));
				if(value.equals("5")) mythique.add(Item.GiveItemLore(Material.valueOf(type), amount, "§" + value + type, Arrays.asList("§dMythique", "§8x" + amount).toArray(new String[0])));
			}
		}
				
		ItemStack temp;
		//Prise aléatoire
		for(int i = 0; i < nbMythique && i <= mythique.size(); i++) {
			while(Rmythique.contains(temp = mythique.get((int) (Math.random() * (mythique.size() - 1)))));
			Rmythique.add(temp);
		}
		
		for(int i = 0; i < nbLegend && i <= legend.size(); i++) {
			while(Rlegend.contains(temp = legend.get((int) (Math.random() * (legend.size() - 1)))));
			Rlegend.add(temp);
		}
		
		for(int i = 0; i < nbRare && i <= rare.size(); i++) {
			while(Rrare.contains(temp = rare.get((int) (Math.random() * (rare.size() - 1)))));
			Rrare.add(temp);
		}
		
		for(int i = 0; i < nbUnCommon && i <= uncommon.size(); i++) {
			while(Runcommon.contains(temp = uncommon.get((int) (Math.random() * (uncommon.size() - 1)))));
			Runcommon.add(temp);
		}
		
		for(int i = 0; i < nbCommon && i <= common.size(); i++) {
			while(Rcommon.contains(temp = common.get((int) (Math.random() * (common.size() - 1)))));
			Rcommon.add(temp);
		}
		
		//Import dans les config
		for(ItemStack t : Rcommon) config.set(pathActual + ".1." + t.getType().name(), String.valueOf(t.getAmount()));
		for(ItemStack t : Runcommon) config.set(pathActual + ".2." + t.getType().name(), String.valueOf(t.getAmount()));
		for(ItemStack t : Rrare) config.set(pathActual + ".3." + t.getType().name(), String.valueOf(t.getAmount()));
		for(ItemStack t : Rlegend) config.set(pathActual + ".4." + t.getType().name(), String.valueOf(t.getAmount()));
		for(ItemStack t : Rmythique) config.set(pathActual + ".5." + t.getType().name(), String.valueOf(t.getAmount()));
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
	private final static ItemStack paysan = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aLoot box", Arrays.asList("§8" + PlayerRank.getRank(RankPaysan), "§dPrix : " + PricePaysan).toArray(new String[0]));
	private final static ItemStack ecuyer = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aLoot box", Arrays.asList("§8" + PlayerRank.getRank(RankEcuyer), "§dPrix : " + PriceEcuyer).toArray(new String[0]));
	private final static ItemStack chevalier = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aLoot box", Arrays.asList("§8" + PlayerRank.getRank(RankChevalier), "§dPrix : " + PriceChevalier).toArray(new String[0]));
	private final static ItemStack seigneur = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aLoot box", Arrays.asList("§8" + PlayerRank.getRank(RankSeigneur), "§dPrix : " + PriceSeigneur).toArray(new String[0]));
	private final static ItemStack modo = Item.GiveItemLore(Material.ENDER_CHEST, 1, "§aCrée un cadeau", Arrays.asList("§8" + PlayerRank.getRank(RankModo), "?").toArray(new String[0]));
	
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
		Item.GrayExGlass(e, size);
		int pr = PlayerRank.getPlayerInt(p);
		
		ItemStack it;  
		if(result == 1) it = Item.GiveItem(Material.CHEST, 1, "§aLoot box gratuite", "§e[RECUPERER]");
		else it = Item.GiveItem(Material.CHEST, 1, "§4Loot box gratuite", "§dDans : " + dayRemaining + " jours");
		
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
			if(lootNum == 1) if(!EconomieESS.HasEnought(player, PricePaysan)) {
				player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Tone.A));
				return; 
			} else Economy.subtract(player.getUniqueId(), new BigDecimal(PricePaysan));
			if(lootNum == 2) if(!EconomieESS.HasEnought(player, PriceEcuyer)) {
				player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Tone.A));
				return;
			} else Economy.subtract(player.getUniqueId(), new BigDecimal(PriceEcuyer));
			if(lootNum == 3) if(!EconomieESS.HasEnought(player, PriceChevalier)) {
				player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Tone.A));
				return; 
			} else Economy.subtract(player.getUniqueId(), new BigDecimal(PriceChevalier));
			if(lootNum == 4) if(!EconomieESS.HasEnought(player, PriceSeigneur)) {
				player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Tone.A));
				return;
			} else Economy.subtract(player.getUniqueId(), new BigDecimal(PriceSeigneur));
		} catch (Exception e) {player.playNote(player.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Tone.A));return;}
		
		
		float r = (float) (Math.random() * 100);
		float per = getPercent(lootNum, 1);
		int i = 1;
		for(i = 2 ; (r > per && i != 5) ; i++) per = per + getPercent(lootNum, i);
		spinLoot(player, i-1);
	}

	private static void spinLoot(Player p,  int value) {
		p.closeInventory();
		Inventory e = Bukkit.createInventory(p, 9, "");
		Item.GrayExGlass(e, 9);
		
		ItemStack obj = new ItemStack(Material.AIR);
		
		if(value == 1) obj = Rcommon.get((int) (Math.random() * (Rcommon.size() - 1)));
		if(value == 2) obj = Runcommon.get((int) (Math.random() * (Runcommon.size() - 1)));
		if(value == 3) obj = Rrare.get((int) (Math.random() * (Rrare.size() - 1)));
		if((value == 4 || (value == 5 && Rmythique.size() == 0))) obj = Rlegend.get((int) ((Math.random() * (Rlegend.size() - 1))));
		if(value == 5 && Rmythique.size() != 0) obj = Rmythique.get((int) (Math.random() * (Rmythique.size() - 1)));
		
		
		Material material = obj.getType();
		int amount = obj.getAmount();
		
		p.openInventory(e);				
		ArrayList<ItemStack> li = new ArrayList<>();
		li.addAll(Rcommon);
		li.addAll(Rlegend);
		li.addAll(Rrare);
		li.addAll(Runcommon);
		li.addAll(Rmythique);
		
		ItemStack it = obj;
		new Thread(new Runnable() {
			
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				try {
					int temp = li.size() - 1;
					while(temp > 0) {
						if(temp > 0) e.setItem(4, Item.GiveItem(li.get(temp).getType(), 1, " ", null));;
						temp--;
						p.playNote(p.getLocation(), Instrument.BASS_GUITAR, Note.flat(1, Tone.A));
						if(temp == 0) {
							e.setItem(4, it);
							if(Item.isInventoryFull(p.getInventory(), (Float.valueOf(it.getAmount()) / it.getMaxStackSize()))) new LootStorage(p).addItem(it);
							else p.getInventory().addItem(new ItemStack(material, amount));
							p.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, 100, 100);
							break;
						}
						Thread.currentThread().sleep(100);
					}
					temp = 0;
					ItemStack red = Item.GiveItem(Material.RED_STAINED_GLASS_PANE, 1, "", null);
					while(temp < 9 && e.getViewers().contains(p)) {
						if(temp == 4) temp++;
						e.setItem(temp, red);
						Thread.currentThread().sleep(625);
						p.playNote(p.getLocation(), Instrument.SNARE_DRUM, Note.flat(1, Tone.A));
						temp++;
					}
					
					Bukkit.getScheduler().runTask(Main.Plug, () -> LootBox.openInv(p));
				} catch (InterruptedException e) {}
				return;
			}
		}).start();
		p.updateInventory();
	}
	
	private static float getPercent(int lootbox, int level) {
		String conf = percentPath.replace("?", String.valueOf(lootbox)).replace("!", String.valueOf(level));
		if(!config.AlreadySet(conf)) config.set(conf, "0");
		return Float.valueOf(config.get(conf));
	}
	
	
	private static File initFile() {
		File file = new File("plugins/ServeurManageur/lootBox.yml");
		if(!file.exists()) try {file.createNewFile();} catch (IOException e) {e.printStackTrace();}
		return file;
	}
	
	static void init() {
		config.saving();
	}
}