package fr.sisig48.pl.Automating;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import fr.sisig48.pl.Economie.XpCounter;
import fr.sisig48.pl.Sociale.Friends;
import fr.sisig48.pl.Sociale.PlayerJobs;
import fr.sisig48.pl.Utils.Uconfig;

public class AutoReload {
	private static TimerTask task = new TimerTask() {
        @Override
        public void run() {
        	// Sauvegarde
    		Uconfig.reloadConfig();
    		Uconfig.saveConfig();
    		Friends.saveAll();
    		PlayerJobs.saveAll();
    		XpCounter.save();
    		
    		initiate();
    		task.cancel();
        }
    };
	
    public static void initiate() {
        // Créez une instance de Timer
        Timer timer = new Timer();

        // Obtenez la date et l'heure actuelles
        Calendar calendar = Calendar.getInstance();

        // Définissez l'heure d'exécution à minuit
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 1);

        // Si l'heure actuelle est déjà passée, ajoutez un jour pour fixer l'heure d'exécution au lendemain
        if (calendar.getTime().compareTo(new Date()) < 0) calendar.add(Calendar.DAY_OF_MONTH, 1);

        

        // Planifiez l'exécution de la tâche à l'heure définie
        timer.schedule(task, calendar.getTime());
    }
	
	public static void delTimer() {
		task.cancel();
	}
}
