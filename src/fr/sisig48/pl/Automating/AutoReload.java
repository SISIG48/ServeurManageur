package fr.sisig48.pl.Automating;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;

public class AutoReload {
	public static void initiate() {
        // Créez une instance de Timer
        Timer timer = new Timer();

        // Obtenez la date et l'heure actuelles
        Calendar calendar = Calendar.getInstance();

        // Définissez l'heure d'exécution à minuit
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        // Si l'heure actuelle est déjà passée, ajoutez un jour pour fixer l'heure d'exécution au lendemain
        if (calendar.getTime().compareTo(new Date()) < 0) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Définissez la tâche à exécuter
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //23H59
            	Bukkit.broadcastMessage("§d/§4Avertissement§d\\ §aLe serveur va redémarrer dans §6une minute");
                Bukkit.broadcastMessage("§d/§4Warning§d\\ §aThe server will restart in §6a minute");
            	try {Thread.sleep(20000);} catch (InterruptedException e) {e.printStackTrace();}

            	//23H59 30s
            	Bukkit.broadcastMessage("§d/§4Avertissement§d\\ §aLe serveur va redémarrer dans §630 second");
            	Bukkit.broadcastMessage("§d/§4Warning§d\\ §aThe server will restart in §630 seconds");
            	try {Thread.sleep(30000);} catch (InterruptedException e) {e.printStackTrace();}
                
            	//0H00 - 10s : Restart
            	Bukkit.reload();
            }
        };

        // Planifiez l'exécution de la tâche à l'heure définie
        timer.schedule(task, calendar.getTime());
    }
}
