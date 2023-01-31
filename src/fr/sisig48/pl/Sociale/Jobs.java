package fr.sisig48.pl.Sociale;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public enum Jobs {
    NOT("Chaumage", "NOT", false, 0),
	HUNTER("Hunter", "HUNTER", false, 1),
    PECHEUR("Pécheur", "PECHEUR", false, 2),
	FARMEUR("Farmeur", "FARMEUR", false, 3),
	ENCHANTEUR("Enchanteur", "ENCHANTEUR", false, 4),
	ALCHIMIST("Alchimiste", "ALCHIMIST", false, 5),
	MARCHANT("Marchant", "MARCHANT", false, 6),
	MAIRE("Maire", "MAIRE", false, 7),
	BANQUIER("Banquier", "BANQUIER", false, 8),
	MEDECIN("Médecin", "MEDECIN", false, 9),
	BOULANGER("Boulanger", "BOULANGER", false, 10),
	BOUCHER("Boucher", "BOUCHER", false, 11),
	FORGERON("Forgeron", "FORGERON", false, 12),
	CHARPENTIER("Charpentier", "CHARPENTIER", false, 13),
	AUBERGISTE("Aubergiste", "AUBERGISTE", false, 14),
	MINEUR("Mineur", "MINEUR", false, 15);
	
    private String name;
    private Boolean enable;
    private String jobs;
    private int id;
	static ArrayList<String> line = new ArrayList<String>();
    
    Jobs(String name, String jobs, Boolean enable, int id) {
        this.name = name;
        this.enable = enable;
        this.jobs = jobs;
        this.id = id;
        try {
			JobsInfoInit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
    
    
    
    
    private void JobsInfoInit() throws IOException {
			if(!getFile().exists()) {
				getFile().createNewFile();
				line = new ArrayList<String>();
				line.add("?JobSettings");
				line.add("Jobs: " + jobs);
				line.add("Name: " + name);
				line.add("Id: " + String.valueOf(id));
				line.add("Enable: " + String.valueOf(enable));
				save();
			} else load();
			JobsInfo();
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
		        // ajoute la ligne au buffer
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
    }
    
    private String[] JobsInfo() {
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
    		}
    	}
    	return null;
    }
    
}