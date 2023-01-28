package fr.sisig48.pl.Sociale;

public enum Jobs {
    NOT("Chaumage", "NOT", 0),
	HUNTER("Devlopeur", "DEVELOPER", 2),
    PECHEUR("Pécheur", "PECHEUR", 1),
	FARMEUR("Farmeur", "FARMEUR", 1),
	ENCHANTEUR("Enchanteur", "ENCHANTEUR", 1),
	ALCHIMIST("Alchimiste", "ALCHIMIST", 1);
	

    private String name;
    private int rank;
    private String jobs;

    Jobs(String name, String jobs, int rank) {
        this.name = name;
        this.rank = rank;
        this.jobs = jobs;
    }

    public String getName() {
        return name;
    }

    public int getRank() {
        return rank;
    }
    
    public Jobs getJobs() {
    	return valueOf(jobs);
    }
}