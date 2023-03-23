package fr.sisig48.pl.Sociale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum Jobs {
	NOT("Chaumage", "NOT", true, 0, 0, new ItemStack(Material.DIRT, 1), "FARMEUR,MINEUR,HUNTER,MEDECIN", 1),
	HUNTER("Hunter", "HUNTER", false, 1, 100, new ItemStack(Material.GUNPOWDER, 32), "NOT,PECHEUR,MINEUR,MEDECIN,FARMEUR", 10),
    PECHEUR("Pécheur", "PECHEUR", false, 2, 100, new ItemStack(Material.PUFFERFISH, 64), "NOT,BOUCHER,HUNTER,MINEUR,MEDECIN,FARMEUR", 10),
	FARMEUR("Farmeur", "FARMEUR", false, 3, 100, new ItemStack(Material.WHEAT, 128), "NOT,BOULANGER,MINEUR,MEDECIN,HUNTER", 10),
	MAGE("Mage", "MAGE", false, 4, 100, new ItemStack(Material.BLAZE_ROD, 64), "NOT,MEDECIN,MINEUR,HUNNTER,FARMER", 10),
	MARCHANT("Marchant", "MARCHANT", false, 6, 100, new ItemStack(Material.GOLD_INGOT, 64), "NOT,BANQUIER,AUBERGISTE,BOULANGER,BOUCHER,PECHEUR,FORGERON,CHARPENTIER,MINEUR,FARMEUR,HUNTER", 10),
	MAIRE("Maire", "MAIRE", false, 7, 100, new ItemStack(Material.BARRIER, 1), "NOT,BANQUIER,AUBERGISTE,BOULANGER,BOUCHER,PECHEUR,FORGERON,CHARPENTIER,MINEUR,FARMEUR,HUNTER,MAGE,MEDECIN,MARCHANT", 10),
	BANQUIER("Banquier", "BANQUIER", false, 8, 100, new ItemStack(Material.GOLD_INGOT, 64), "NOT,MAIRE,AUBERGISTE,BOULANGER,BOUCHER,PECHEUR,FORGERON,CHARPENTIER,MINEUR,FARMEUR,HUNTER,MARCHANT", 10),
	MEDECIN("Médecin", "MEDECIN", false, 9, 100, new ItemStack(Material.GOLDEN_APPLE, 8), "NOT,MAGE,MINEUR,HUNNTER,FARMER", 10),
	BOULANGER("Boulanger", "BOULANGER", false, 10, 100, new ItemStack(Material.BREAD, 64), "NOT,FARMEUR,AUBERGISTE,MINEUR,MEDECIN,HUNTER", 10),
	BOUCHER("Boucher", "BOUCHER", false, 11, 100, new ItemStack(Material.CHICKEN, 15), "NOT,MARCHANT,PECHEUR,HUNTER,MINEUR,MEDECIN,FARMEUR", 10),
	FORGERON("Forgeron", "FORGERON", false, 12, 100, new ItemStack(Material.IRON_SWORD, 10), "NOT,CHARPENTIER,MINEUR,FARMEUR,MEDECIN,HUNTER", 10),
	CHARPENTIER("Charpentier", "CHARPENTIER", false, 13, 100, new ItemStack(Material.OAK_PLANKS, 128), "NOT,MARCHANT,FORGERON,MINEUR,FARMER,HUNTER,MEDECIN", 10),
	AUBERGISTE("Aubergiste", "AUBERGISTE", false, 14, 100, new ItemStack(Material.COOKED_CHICKEN, 64), "NOT,MARCHANT,BOULANGER,FARMER,MINEUR,MEDECIN,HUNTER", 10),
	MINEUR("Mineur", "MINEUR", false, 15, 100, new ItemStack(Material.DIAMOND, 15), "NOT,FORGERON,FARMEUR,MEDECIN,HUNTER", 10);
	
	private int prix;
	private String name;
    public Boolean enable;
    private String jobs;
    private int id;
    private ArrayList<String> rules = new ArrayList<String>();
	private ItemStack item_cost;
    static ArrayList<String> line = new ArrayList<String>();
    public static Jobs[] All = Jobs.values();
	private int PayMent;
    private JobsItemXp JobsXp = new JobsItemXp(this);
	
    Jobs(String name, String jobs, Boolean enable, int id, int prix, ItemStack item_cost, String rule, int PayMent) {
    	this.name = name;
        this.enable = enable;
        this.jobs = jobs;
        this.id = id;
        this.prix = prix;
        this.item_cost = item_cost;
        this.PayMent = PayMent;
        for(String r : rule.split("\\s*,\\s*")) rules.add(r);
        try {
			JobsInfoInit();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public String getName() {
        return name;
    }
    
    public Float getXpGain(Material m) {
    	return JobsXp.getXpGain(m);
    }
    
    public int getNextPosibilities() {
        return rules.size();
    }
    
    public Boolean isEnable() {
        return enable;
    }
    
    public int getId() {
        return id;
    }
    
    public File getFile() {
        File file = new File("plugins/ServeurManageur/data/jobs/" + name + ".jobs");
    	return file;
    }
   
    public Jobs getJobs() {
    	return valueOf(jobs);
    }
    
    public int getPrice() {
    	return prix;
    }
    
    public int getPay() {
    	return PayMent;
    }
    
    public boolean isInRules(Jobs jobs) {
    	if(rules.contains(String.valueOf(jobs.getJobs()))) return true;
    	else return false;
    }
    
    public ItemStack getItemCost() {
    	return item_cost;
    }
    
    private void JobsInfoInit() throws IOException {
			if(!getFile().exists()) {
				getFile().createNewFile();
				line = new ArrayList<String>();
				line.add("?JobSettings");
				line.add("Jobs: " + jobs);
				line.add("Name: " + name);
				line.add("Id: " + String.valueOf(id));
				line.add("Enable: " + String.valueOf(enable));
				line.add("Price: " + String.valueOf(prix));
				line.add("ItemCost: " + String.valueOf(item_cost.getType() + "," + item_cost.getAmount()));
				line.add("Rules: " + rules);
				save();
			} else load();
			
    }
    public void reset() throws IOException {
    	getFile().delete();
    }
    
    private void save() throws IOException {
		FileWriter MyFileW = new FileWriter(getFile());
	    BufferedWriter bufWriter = new BufferedWriter(MyFileW);
	    for(String e : line) {    
	    	bufWriter.write(e);
	    	bufWriter.newLine();
	    }
        bufWriter.close();
        MyFileW.close();
    }
    
	public void saveFile(){
		try {
			getFile().delete();
			getFile().createNewFile();
			line = new ArrayList<String>();
			line.add("?JobSettings");
			line.add("Jobs: " + jobs);
			line.add("Name: " + name);
			line.add("Id: " + String.valueOf(id));
			line.add("Enable: " + String.valueOf(enable));
			line.add("Price: " + String.valueOf(prix));
			line.add("ItemCost: " + String.valueOf(item_cost.getType() + "," + item_cost.getAmount()));
			line.add("Rules: " + rules);
	    	FileWriter MyFileW = new FileWriter(getFile());
		    BufferedWriter bufWriter = new BufferedWriter(MyFileW);
		    for(String e : line) {    
		    	bufWriter.write(e);
		    	bufWriter.newLine();
		    }
	        bufWriter.close();
	        MyFileW.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
    }
    private void load() throws IOException {
			FileReader MyFileR = new FileReader(getFile());
		    BufferedReader br = new BufferedReader(MyFileR);
		    StringBuffer sb = new StringBuffer();    
		    String r;
		    line = new ArrayList<String>();
		    while((r = br.readLine()) != null) {
		        sb.append(r);      
		        sb.append("\\;\\");     
		      }
		    MyFileR.close();
		    for(String li : sb.toString().split("\\\\;\\\\")) line.add(li);
		    int i = 0;
		    int rs = line.size() -1;
		    while(rs != 0 & i < rs & !line.contains("?JobSettings")) {
		    	rs = line.size() -1;
		    	if(line.get(i).contains("?JobSettings")) {
		    		line.remove(i);
		    	} else {
		    		i++;
		    	}

		    }
		    if(!line.get(0).equalsIgnoreCase("?JobSettings")) line.add(0, "?JobSettings");
		    save();
		    JobsInfo();
    }
    
    private void JobsInfo() {
    	for (String e : line) {
    		String[] temp = e.split("\\s*:\\s*");
    		switch(temp[0]) {
    		case "Jobs" :
    			jobs = temp[1];
    			break;
    		case "Name" :
    			name = temp[1];
    			break;
    		case "Id" :
    			id = Integer.valueOf(temp[1]);
    			break;
    		case "Enable" :
    			enable = Boolean.valueOf(temp[1]);
    			break;
    		case "Price" :
    			prix = Integer.valueOf(temp[1]);
    			break;
    		case "ItemCost" :
    			String[] info = temp[1].split(",\\s*");
    			if(info.length != 2) return;
    			item_cost = new ItemStack(Material.valueOf(info[0]), Integer.valueOf(info[1]));
    			break;
    		case "Rules" :
    			String[] rulsInfo = temp[1].split(",\\s*");
    			if(rulsInfo.length < 1) return;
    			rules.clear();
    			for(String ri : rulsInfo) if(!ri.equalsIgnoreCase("null") && !rules.contains(ri)) rules.add(ri.replace("[", "").replace("]", ""));
    			break;
    		}
    	}
    	return;
    }
    
    public static void init() {
    	try {
			JobsItemXp.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}

class JobsItemXp {
	static boolean isInit = false;
	static ArrayList<InfoItemXp> MXP = new ArrayList<InfoItemXp>();
	ArrayList<Material> MAL = new ArrayList<Material>();
	ArrayList<Float> ALF = new ArrayList<Float>();
	public static File getFile() {
        File file = new File("plugins/ServeurManageur/data/jobs/Material.xpJobs");
    	return file;
    }
	
	public Float getXpGain(Material m) {
		for(InfoItemXp t : MXP) for(InfoItemXpUnit t2 : t.getInfoItemXpUnit()) Bukkit.getConsoleSender().sendMessage("§4§l" + t2.getJobs().getName() + " " + t2.getXp() + " §a" + t.getMaterial().name());
		if(MAL.contains(m)) return ALF.get(MAL.indexOf(m));
		else return Float.valueOf(0);
	}
	public JobsItemXp(Jobs jobs) {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			
			@SuppressWarnings({ "deprecation", "static-access" })
			@Override
			public void run() {
				while(!tE)
					try {
						Thread.currentThread().sleep(500);
					} catch (InterruptedException e) {}
				for(InfoItemXp t : MXP) {
					for(InfoItemXpUnit t1 : t.getInfoItemXpUnit()) {
						if(t1.getJobs().equals(jobs)) {
							MAL.add(t.getMaterial());
							Bukkit.getConsoleSender().sendMessage("--- §4§l"+t.getMaterial().name());
							ALF.add(t1.getXp());
						}
					}
				}
				Thread.currentThread().stop();
			}
		}).start();
			
	}
	static boolean tE = false;
	static void init() throws IOException {
		if(isInit) return;
		isInit = true;
		if(!getFile().exists()) {
			File source = new File("plugins/ServeurManageur.jar");
			
			try (JarFile jar = new JarFile(source)) {
	            JarEntry entry = jar.getJarEntry("Material.xpJobs");
	            try (InputStream is = jar.getInputStream(entry)) {
	                Files.copy(is, getFile().toPath());
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
		FileReader MyFileR = new FileReader(getFile());
	    BufferedReader br = new BufferedReader(MyFileR);
	    StringBuffer sb = new StringBuffer();    
	    String r;
	    ArrayList<String> line = new ArrayList<String>();
	    while((r = br.readLine()) != null) {
	        sb.append(r);      
	        sb.append("\\;\\");     
	    }
	    MyFileR.close();
	    for(String li : sb.toString().split("\\\\;\\\\")) line.add(li);
	    
		new Thread(new Runnable() {
			
			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void run() {
				try {
					Thread.currentThread().sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Material ma = Material.BARRIER;
			    ArrayList<InfoItemXpUnit> IIXP = new ArrayList<InfoItemXpUnit>();
			    ArrayList<InfoItemXp> IMXP = new ArrayList<InfoItemXp>();
			    line.remove("?JobXpSettings");
				for (String e : line) {
			    	Bukkit.getConsoleSender().sendMessage("§6§l"+e);
			    	String[] temp = e.split("\\s*:\\s*");
		    		if(temp.length == 1) {
		    			if(!ma.equals(Material.BARRIER)) IMXP.add(new InfoItemXp(ma, IIXP));
		    			ma = Material.valueOf(temp[0]);
		    			Bukkit.getConsoleSender().sendMessage("§4§l"+ma.name());
		    			IIXP.clear();
		    		}
		    		if(temp.length == 2) {
		    			Bukkit.getConsoleSender().sendMessage("§4§l" + temp[0] + " §a" + temp[1]);
		    			IIXP.add(new InfoItemXpUnit(Jobs.valueOf(temp[0]), Float.valueOf(temp[1])));
		    		}
		    	}
			    if(!ma.equals(Material.BARRIER)) IMXP.add(new InfoItemXp(ma, IIXP));
			    MXP = IMXP;
			    tE = true;
			    Thread.currentThread().stop();
			}
		}, "XpGain Thread").start();
	}
	
}

class InfoItemXp {
	private Material m; 
	private ArrayList<InfoItemXpUnit> j;
	public InfoItemXp(Material ma, ArrayList<InfoItemXpUnit> IIXP) {
		m = ma;
		j = IIXP;
	}
	
	Material getMaterial() {
		return m;
	}
	
	ArrayList<InfoItemXpUnit> getInfoItemXpUnit() {
		return j;
	}
	
}

class InfoItemXpUnit {
	private Jobs j;
	private Float x;
	public InfoItemXpUnit(Jobs jo, float xp) {
		j = jo;
		x = xp;
	}
	
	Jobs getJobs() {
		return j;
	}
	
	Float getXp() {
		return x;
	}
}