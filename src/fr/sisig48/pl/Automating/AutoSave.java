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

import net.sisig48.web.WebAccount;
import net.sisig48.web.WebResponses;

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
		try {WebResponses.save();} catch (Exception e) {e.printStackTrace();}
		try {WebAccount.save();} catch (Exception e) {e.printStackTrace();}
		try {Friends.saveAll();} catch (Exception e) {e.printStackTrace();}
		try {PlayerJobs.saveAll();} catch (Exception e) {e.printStackTrace();}
		try {XpCounter.save();} catch (Exception e) {e.printStackTrace();}
		try {EconomieData.saveInfo();} catch (Exception e) {e.printStackTrace();}
		try {WebAuto.SaveChange();} catch (Exception e) {e.printStackTrace();}
		try {EconomieMarchant.save();} catch (Exception e) {e.printStackTrace();}
		try {ObjectPrice.save();} catch (Exception e) {e.printStackTrace();}
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
