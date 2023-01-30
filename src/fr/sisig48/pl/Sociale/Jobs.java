package fr.sisig48.pl.Sociale;

import java.io.File;

public enum Jobs {
    NOT("Chaumage", "NOT", false, 0),
	HUNTER("Devlopeur", "DEVELOPER", false, 1),
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
    
    Jobs(String name, String jobs, Boolean enable, int id) {
        this.name = name;
        this.enable = enable;
        this.jobs = jobs;
        this.id = id;
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
        File file = new File("plugin/ServeurManageur/data/jobs/" + name + ".jobs");
    	return file;
    }
   
    public Jobs getJobs() {
    	return valueOf(jobs);
    }
    
}