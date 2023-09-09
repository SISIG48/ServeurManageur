package fr.sisig48.pl.Economie;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.logs;
import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Utils.Uconfig;

public class XpCounter {
	private final static String path = getPath();
	private final static ArrayList<String> lines = new ArrayList<String>();
	private final static String fileName = "sell";
	private static int maxLine = maxLine();
	
	public static void Count() {
		read();
		regularLine.load();
		maxLine = maxLine + regularLine.getSup();
		delLines();
	}
	
	private static int maxLine() {
		if(!Uconfig.isSet("xp.line")) Uconfig.setConfig("xp.line", "" + Material.values().length * 10);
		return Integer.valueOf(Uconfig.getConfig("xp.line"));
	}
	
	public static float getXp(Material m, Jobs jobs) {
		return new ItemXp(m, jobs).getXp();
	}
	
	private static void read() {
		lines.clear();
        FileReader MyFileR;
		try {
			MyFileR = new FileReader(getFile());
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
	
	public static void addItem(ItemStack it) {
		lines.add(it.getType() + "/" + it.getAmount());
		delLines();
		new ItemXp(it.getType(), it.getAmount());
		regularLine.addSell();
	}
	
	public static void delItem(ItemStack it) {
		lines.remove(it.getType() + "/" + it.getAmount());
		delLines();
		new ItemXp(it.getType(), 0).subQtn(it.getAmount());
		regularLine.delSell();
	}
	
	public static void save() {
		try {
            // Sauvegarde le nombre de transaction xp
			regularLine.save();
			
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
	
	
	private final static File getFile() {
		return new File(path + fileName  + ".itemSave");
	}

	final static String getPath() {
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
	private int qtn = 0;
	private Material it;
	private ItemXp ix = this;
	private float xp = 0;
	private Jobs jobs; 
	
	private static String confPath = "xp.gain.";
	private final static ArrayList<ItemXp> ixs = new ArrayList<ItemXp>();
	private static int total = 0;
	private final static File fileXpInfo = new File(XpCounter.getPath() + "xp.itemSave");
	private final static ArrayList<String> tags = new ArrayList<String>(Arrays.asList("HS", "BAS", "MOYEN", "HAUT"));
	
	private static float qtnNullXp = qtnNullXp();
	private static float maxNullXp = maxNullXp();
	private static float maxGainXp = maxGainXp();
	
	@SuppressWarnings("unused")
	private static boolean confInti = initPerConfig();
	
	private static ArrayList<String> xpInfo = getXpInfo();
	
	public ItemXp(Material it, int qtn) {
		total = total + qtn;
		
		
		//Recupère l'instance existance
		for(ItemXp i : ixs) {
			if(i.qtn != 0) i.xp = (total - i.qtn) * maxXp() / total; else i.xp = 0;
			if(i.getMaterial().equals(it)) ix = i;
		}
		
		ix.qtn = qtn + ix.qtn;
		if(ix.qtn != 0) ix.xp = (total - ix.qtn) * maxXp() / total;
		else ix.xp = 0;
		
		if(ix != this) return;
		
		//Crée une instance
		ixs.add(this);
		ix.it = it;
	}
	
	@SuppressWarnings("resource")
	private static ArrayList<String> getXpInfo() {
		ArrayList<String> info = new ArrayList<String>();
		if(!fileXpInfo.exists()) {
			File source = new File("plugins/ServeurManageur.jar");
	        try (JarFile jar = new JarFile(source)) {
	            JarEntry entry = jar.getJarEntry("Material.xpJobs");
	            try (InputStream is = jar.getInputStream(entry)) {Files.copy(is, fileXpInfo.toPath());};
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileXpInfo));
			String r;
			while((r = br.readLine()) != null) {
				try {
					r = r.toUpperCase().replace(" ", "_");
					boolean find = false;
					
					Material.valueOf(r.split("/")[0]);
					for(int i = 1; i != r.split("/").length; i++) {
						if(tags.contains(r.split("/")[i])) find = true;
						else for(Jobs j : Jobs.values()) if(j.toString().equals(r.split("/")[i])) find = true;
					}
					
					if(find) info.add(r);
				} catch (IllegalArgumentException e) {
					logs.send("§4Erreur Material invalide : §a" + r.split("/")[0]);
				} catch (IndexOutOfBoundsException e) {
					logs.send("§4Erreur ligne invalide : §a" + r);
				}
				
			}
		} catch (FileNotFoundException e) {} catch (IOException e) {}
		return info;
	}

	public ItemXp(Material it, Jobs jobs) {
		this.jobs = jobs;
		for(ItemXp i : ixs) {
			if(i.qtn != 0) i.xp = (total - i.qtn) * maxXp() / total; else i.xp = 0;
			if(i.getMaterial().equals(it)) ix = i;
		}
	}
	
	private static float maxNullXp() {
		if(!Uconfig.isSet("xp.min")) Uconfig.setConfig("xp.min", "1");
		return Float.valueOf(Uconfig.getConfig("xp.min"));
	}
	
	private static float qtnNullXp() {
		if(!Uconfig.isSet("xp.qtn")) Uconfig.setConfig("xp.qtn", "40%");
		return (Integer.valueOf(Uconfig.getConfig("xp.qtn").split("%")[0]) * Material.values().length / 100);
	}
	
	private static float maxXp() {
		if(!Uconfig.isSet("xp.base")) Uconfig.setConfig("xp.base", "5");
		return Float.valueOf(Uconfig.getConfig("xp.base")) * ixs.size();
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
		float per = 1;
		Jobs j = jobs;
		for(String s : xpInfo) if(Material.getMaterial(s.split("/")[0]).equals(ix.it)) for(String st : s.split("/")) {
			if(st.equals(j.toString())) per = per * (Float.valueOf(Uconfig.getConfig(confPath + "jobs").split("%")[0]) / 100 + 1);
			else if(tags.contains(st)) for(String tag : tags) if(tag.equals(st)) per = per * (Float.valueOf(Uconfig.getConfig(confPath + tag).split("%")[0]) / 100 + 1);
		}
		logs.send("" + ix.xp);
		if(ix.xp > maxGainXp) return maxGainXp * per;
		//if(ixs.size() < qtnNullXp) return maxNullXp * per;
		else return ix.xp * per;
	}
	
	private static boolean initPerConfig() {
		for(String s : tags) if(!Uconfig.isSet(confPath + s)) Uconfig.setConfig(confPath + s,  "-100%");
		if(!Uconfig.isSet(confPath + "jobs")) Uconfig.setConfig(confPath + "jobs", "50%");
		return true;
	}
	
	public void subQtn(int qtn) {
		ix.qtn = ix.qtn - qtn;
		total = total - qtn;
		for(ItemXp i : ixs) i.xp = (total - i.qtn) * maxXp() / total;
	}
}

class regularLine {
	private final static String path = XpCounter.getPath();
	private final static String fileName = "sellCount";
	private final static ArrayList<String> lines = new ArrayList<String>();
	private static int previous = 0;
	private static int current = 0;
	private static int actualday = 0;
	private static final String date = getDate();
	public static void load() {
		lines.clear();
        FileReader MyFileR;
		try {
			MyFileR = new FileReader(getFile());
			BufferedReader br = new BufferedReader(MyFileR);
			String r;
			while((r = br.readLine()) != null) lines.add(r);
			br.close();
			
			// Date acuel
		    if(lines.size() == 0) lines.add(0, date);
			
			
			// Récupère les valeurs de la semaine précédent
			if(lines.size() == 1) lines.add("0");
			previous = Integer.valueOf(lines.get(1));
			
			// Crée le premier jour
			if(lines.size() == 2) lines.add("0");
			
			// récupère le jour actuel
			if(lines.get(0).equals(date)) {
				actualday = lines.size() - 1;
				current = Integer.valueOf(lines.get(actualday));
			} else {
				actualday = lines.size();
				lines.add("0");
			}
			
			// Crée une nouvelle semaine
			if(actualday > 9) {
				int m = 0;
				for(int i = 0; i > 7; i++) {
					m = m + Integer.valueOf(lines.get(i + 1));
					lines.remove(i + 2);
				}
				lines.set(1, "" + (m / 3.5));
				previous = m;
			}
		} catch (FileNotFoundException e) {e.printStackTrace();
		} catch (NumberFormatException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		}
	}
	
	private static String getDate() {
		return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	}

	public static void addSell() {
		current++;
	}
	
	public static void delSell() {
		current--;
	}
	
	public static int getSup() {
		return previous;
	}
	
	private static final File getFile() {
		File file = new File(path + fileName + ".itemSave");
		if(!file.exists()) try {file.createNewFile();} catch (IOException e) {e.printStackTrace();}
		return file;
	}
	
	public static void save() throws IOException {
		if(current < 20) return;
		lines.set(actualday, "" + current); //Modification de la valeur sur les ligne fichier
		lines.set(0, date); // Actualise le jour actuelle
		
		// Ouvrir le fichier
        FileWriter fileWriter = new FileWriter(getFile(), false);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        // Écrire les lignes dans le fichier
        for (String ligne : lines) {
            bufferedWriter.write(ligne);
            bufferedWriter.newLine(); // Ajouter un saut de ligne
        }

        // Fermer le fichier
        bufferedWriter.close();
	}
}