package fr.sisig48.pl.Economie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Utils.Uconfig;

public class XpCounter {
	private static String path = getPath();
	private static int maxLine = 10000;
	private static ArrayList<String> lines = new ArrayList<String>();
	
	public static void Count() {
		delLines();
		read();
	}
	
	public static float getXp(Material m) {
		return new ItemXp(m, 0).getXp();
	}
	
	private static void read() {
		File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
            	FileReader MyFileR;
				try {
					MyFileR = new FileReader(file);
					BufferedReader br = new BufferedReader(MyFileR);
					String r;
					while((r = br.readLine()) != null) {
						new ItemXp(Material.getMaterial(r.split("/")[0]), Integer.valueOf(r.split("/")[1]));
						lines.add(r);
					}
					br.close();
				} catch (FileNotFoundException e) {
				} catch (NumberFormatException e) {
				} catch (IOException e) {
				}
            }
        }
	}
	
	public static void addItem(ItemStack it) {
		lines.add(it.getType() + "/" + it.getAmount());
		delLines();
		new ItemXp(it.getType(), it.getAmount());
	}
	
	public static void delItem(ItemStack it) {
		lines.remove(it.getType() + "/" + it.getAmount());
		delLines();
		new ItemXp(it.getType(), 0).subQtn(it.getAmount());
	}
	
	public static void save() {
		try {
            // Ouvrir le fichier en mode d'ajout
            FileWriter fileWriter = new FileWriter(getFile(), false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Écrire les nouvelles lignes dans le fichier
            for (String ligne : lines) {
                bufferedWriter.write(ligne);
                bufferedWriter.newLine(); // Ajouter un saut de ligne
            }

            // Fermer le fichier
            bufferedWriter.close();
        } catch (IOException e) {}
	}
	
	
	private static File getFile() {
		return new File(path + "xp.itemSave");
	}

	private static String getPath() {
		String path = Main.Plug.getDataFolder() + "/data/item/";
		File dir = new File(path);
		if(!dir.exists()) dir.mkdir();
		return path;
	}
	
	private static void delLines() {
		while(lines.size() > maxLine) lines.remove(lines.size() - 1);
	}
}


class ItemXp {
	private static ArrayList<ItemXp> ixs = new ArrayList<ItemXp>();
	private static int maxXp = maxXp();
	private static float qtnNullXp = qtnNullXp();
	private static float maxNullXp = maxNullXp();
	private static float maxGainXp = maxGainXp();
	private static int total = 0;
	private int qtn = 0;
	private Material it;
	private ItemXp ix;
	private float xp;
	public ItemXp(Material it, int qtn) {
		total = total + qtn;
		ix = this;
		
		
		//Recupère l'instance existance
		for(ItemXp i : ixs) {
			if(i.qtn != 0) i.xp = (total - i.qtn) * maxXp / total; else i.xp = 0;
			if(i.getMaterial().equals(it)) ix = i;
		}
		
		ix.qtn = qtn + ix.qtn;
		if(ix.qtn != 0) ix.xp = (total - ix.qtn) * maxXp / total;
		else ix.xp = 0;
		
		if(ix != this) return;
		
		//Crée une instance
		ixs.add(this);
		ix.it = it;
	}
	
	private static float maxNullXp() {
		if(!Uconfig.isSet("xp.min")) Uconfig.setConfig("xp.min", "1");
		return Float.valueOf(Uconfig.getConfig("xp.min"));
	}
	
	private static float qtnNullXp() {
		if(!Uconfig.isSet("xp.qtn")) Uconfig.setConfig("xp.qtn", "40");
		return (Integer.valueOf(Uconfig.getConfig("xp.qtn")) * Material.values().length / 100);
	}
	
	private static int maxXp() {
		if(!Uconfig.isSet("xp.base")) Uconfig.setConfig("xp.base", "10000");
		return Integer.valueOf(Uconfig.getConfig("xp.base"));
	}

	private static float maxGainXp() {
		if(!Uconfig.isSet("xp.max")) Uconfig.setConfig("xp.max", "7.5");
		return Float.valueOf(Uconfig.getConfig("xp.max"));
	}
	
	public int getQtn() {
		return ix.qtn;
	}
	
	public Material getMaterial() {
		return ix.it;
	}
	
	public float getXp() {
		if(ixs.size() < qtnNullXp) return maxNullXp;
		if(ixs.size() < maxGainXp) return maxGainXp;
		else return ix.xp;
	}
	
	public void subQtn(int qtn) {
		ix.qtn = ix.qtn - qtn;
		total = total - qtn;
		for(ItemXp i : ixs) i.xp = (total - i.qtn) * maxXp / total;
	}
}