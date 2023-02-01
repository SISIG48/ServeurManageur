package fr.sisig48.pl.Sociale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public enum Jobs {
    NOT("Chaumage", "NOT", true, 0, 0),
	HUNTER("Hunter", "HUNTER", false, 1, 100),
    PECHEUR("Pécheur", "PECHEUR", false, 2, 100),
	FARMEUR("Farmeur", "FARMEUR", false, 3, 100),
	ENCHANTEUR("Enchanteur", "ENCHANTEUR", false, 4, 100),
	ALCHIMIST("Alchimiste", "ALCHIMIST", false, 5, 100),
	MARCHANT("Marchant", "MARCHANT", false, 6, 100),
	MAIRE("Maire", "MAIRE", false, 7, 100),
	BANQUIER("Banquier", "BANQUIER", false, 8, 100),
	MEDECIN("Médecin", "MEDECIN", false, 9, 100),
	BOULANGER("Boulanger", "BOULANGER", false, 10, 100),
	BOUCHER("Boucher", "BOUCHER", false, 11, 100),
	FORGERON("Forgeron", "FORGERON", false, 12, 100),
	CHARPENTIER("Charpentier", "CHARPENTIER", false, 13, 100),
	AUBERGISTE("Aubergiste", "AUBERGISTE", false, 14, 100),
	MINEUR("Mineur", "MINEUR", false, 15, 100);
	
	private int prix;
	private String name;
    private Boolean enable;
    private String jobs;
    private int id;
	static ArrayList<String> line = new ArrayList<String>();
    public static Jobs[] All = Jobs.values();
	
    Jobs(String name, String jobs, Boolean enable, int id, int prix) {
    	this.name = name;
        this.enable = enable;
        this.jobs = jobs;
        this.id = id;
        this.prix = prix;
        try {
			JobsInfoInit();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }

    public String getName() {
        return name;
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
    			System.out.println(String.valueOf(temp[1]) + "///////");
    			prix = Integer.valueOf(temp[1]);
    			break;
    		}
    	}
    	return;
    }
    
}