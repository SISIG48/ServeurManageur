package fr.sisig48.pl.Automating;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Economie.EconomieData;
import fr.sisig48.pl.Economie.EconomieMarchant;
import fr.sisig48.pl.Economie.ObjectPrice;
import fr.sisig48.pl.Economie.XpCounter;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Uconfig;

public class AutoSave {
	private static Thread task = new Thread(new Runnable() {
		
		@Override
		public void run() {
			try {
				while(true) {
					Thread.sleep((long) (Float.valueOf(Uconfig.getConfig("autoSave"))*60000));
					logs.send("§aSaving");
					logs.add("Saving");
					save();
				}
			} catch (NumberFormatException | InterruptedException e) {
				logs.send("§aSaving");
				logs.add("Saving");
				save();
			}
		}
	}, "Thread - AutoSave");
        
	
    public static void initiate() {
        task.start();
    }
	
	public static void del() {
		task.interrupt();
	}
	
	public static void save() {
		// Sauvegarde
		Friends.saveAll();
		PlayerJobs.saveAll();
		XpCounter.save();
		EconomieData.saveInfo();
		WebAuto.SaveChange();
		EconomieMarchant.save();
		ObjectPrice.save();
	}
	
	public static void SerializableSave(File out, Object object) {
		try {
			if(!out.exists()) {
				out.getParentFile().mkdir();
				out.createNewFile();
			}
			FileOutputStream fichierSortie = new FileOutputStream(out);
			ObjectOutputStream sortieObjet = new ObjectOutputStream(fichierSortie);
			
			sortieObjet.writeObject(object);
			
			sortieObjet.close();
			fichierSortie.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
