package fr.sisig48.pl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.entity.Player;

public class logs {
	public static void add(String info) {

	    
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    Date date = new Date();
	    
	    String content = "Logs - At : " + s.format(date) + " - Logs : " + info;
	    FileWriter MyFile;
		try {
			MyFile = new FileWriter("plugins/ServeurManageur/logs.txt", true);
			BufferedWriter bufWriter = new BufferedWriter(MyFile);
	        bufWriter.newLine();
	        bufWriter.write(content);
	        bufWriter.close();
	        MyFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
	public static void reportBug(String bug, String from) throws IOException {
		
		
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    Date date = new Date();
	    
	    String content = "Bugs - At : " + s.format(date) + " - Report by : " + from + " - Reported bug : " + bug;
	    FileWriter MyFile= new FileWriter("plugins/ServeurManageur/bug.txt", true);
	    BufferedWriter bufWriter = new BufferedWriter(MyFile);
        bufWriter.newLine();
        bufWriter.write(content);
        bufWriter.close();
        MyFile.close();
		
		content = "Bugs - At : " + s.format(date) + " - Report by : " + from + " - Reported bug : " + bug;
		MyFile = new FileWriter("plugins/ServeurManageur/logs.txt", true);
		bufWriter = new BufferedWriter(MyFile);
        bufWriter.newLine();
        bufWriter.write(content);
        bufWriter.close();
		MyFile.close();
	}
	
	public static void PlayerConect(Player player) throws IOException {
		
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    Date date = new Date();
	    
	    String content = "Conection - At : " + s.format(date) + " - UUID : " + String.valueOf(player.getUniqueId()) + " - Name : " + player.getName() + " - Join";
	    FileWriter MyFile= new FileWriter("plugins/ServeurManageur/conection.txt", true);
	    BufferedWriter bufWriter = new BufferedWriter(MyFile);
        bufWriter.newLine();
        bufWriter.write(content);
        bufWriter.close();
        MyFile.close();
        
        MyFile= new FileWriter("plugins/ServeurManageur/logs.txt", true);
	    bufWriter = new BufferedWriter(MyFile);
        bufWriter.newLine();
        bufWriter.write(content);
        bufWriter.close();
        MyFile.close();
	}
	
	public static void PlayerLeave(Player player) throws IOException {
		
		SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    Date date = new Date();
	    
	    String content = "Conection - At : " + s.format(date) + " - UUID : " + String.valueOf(player.getUniqueId()) + " - Name : " + player.getName() + " - Leave";
	    FileWriter MyFile= new FileWriter("plugins/ServeurManageur/conection.txt", true);
	    BufferedWriter bufWriter = new BufferedWriter(MyFile);
        bufWriter.newLine();
        bufWriter.write(content);
        bufWriter.close();
        MyFile.close();
        
        MyFile= new FileWriter("plugins/ServeurManageur/logs.txt", true);
	    bufWriter = new BufferedWriter(MyFile);
        bufWriter.newLine();
        bufWriter.write(content);
        bufWriter.close();
        MyFile.close();
	}
	
}
