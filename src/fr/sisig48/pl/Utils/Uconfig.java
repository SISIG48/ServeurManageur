package fr.sisig48.pl.Utils;


import fr.sisig48.pl.Main;

public class Uconfig extends Main {

	//public static final
	public static String getConfig(String configPath) {
		System.out.print(configPath);
		String it = new Main().getConfig().getString(configPath);
		return it;
	}
	
	
	public void setConfig(String configPath, String it) {
		new Main().getConfig().set(configPath, it);
		return;
	}
}
