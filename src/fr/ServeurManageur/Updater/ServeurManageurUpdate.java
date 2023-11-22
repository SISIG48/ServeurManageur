package fr.ServeurManageur.Updater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.logs;
import fr.sisig48.pl.Utils.Uconfig;


public class ServeurManageurUpdate {
	public static int NeedUpdate;
	private static String branch = branch();
	private static String branch() {
		if(Uconfig.getConfig("isAhead-Version").equals("true")) return "https://github.com/SISIG48/ServeurManageur/blob/devlopement";
		else return "https://github.com/SISIG48/ServeurManageur/blob/main";
	}

	public static Boolean DoUpdate(CommandSender sender) {
    	logs.add("Maj start by : " + sender.getName());
        try {
        	URL url = new URL(branch + "/ServeurManageur.jar?raw=true");
            Bukkit.getConsoleSender().sendMessage("§aStart maj from : §d" + branch);
        	ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            
            Note();
            Thread.sleep(1000);
            Bukkit.dispatchCommand(sender, "stop");
            logs.add("Maj end");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
			e.printStackTrace();
		}
        logs.add("Maj err");
        return false;
        
    }
    

	public static Boolean DoSpecificUpdate(CommandSender sender, String version) {
    	logs.add("Maj start by : " + sender.getName() + " v: " + version);
        try {
        	URL url = new URL(branch + "/old/data/ServeurManageur%20"+ version +".jar?raw=true");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            
            Note();
            
            File dest = new File("plugins/ServeurManageur/version.dll");
            if(dest.exists()) dest.delete();
            Bukkit.reload();
            logs.add("Maj end");
            return true;
        } catch (IOException e) {
        }
        logs.add("Maj err");
        return false;
        
    }
	
	public static void Note() {
		File source = new File("plugins/ServeurManageur.jar");
        File dest = new File("plugins/ServeurManageur/note.txt");
        dest.delete();
        
        try (JarFile jar = new JarFile(source)) {
            JarEntry entry = jar.getJarEntry("note.txt");
            try (InputStream is = jar.getInputStream(entry)) {
                Files.copy(is, dest.toPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public static Boolean CheckUpdate() {
    	logs.add("Start Tchecking Update from : " + branch + "/version.dll?raw=true");
    	try {
    		URL url = new URL(branch + "/version.dll?raw=true");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            
            
            //Output
            String outPath = "plugins/ServeurManageur/versionN.dll";
            FileOutputStream fos = new FileOutputStream(outPath);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            
            File source = new File("plugins/ServeurManageur.jar");
            File dest = new File("plugins/ServeurManageur/version.dll");
            if(dest.exists()) dest.delete();
            
            try (JarFile jar = new JarFile(source)) {
                JarEntry entry = jar.getJarEntry("version.dll");
                try (InputStream is = jar.getInputStream(entry)) {
                    Files.copy(is, dest.toPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            File out = new File(outPath);
            
			//Check maj
            String l1 = getFirstLine(out);
            String l2 = getFirstLine(dest);
            Bukkit.getConsoleSender().sendMessage("§d" + l1.split("\\?")[1] + " " + " §4" + l2.split("\\?")[1]);
            if(l1.equals(l2)) return false;
            
			if(Bukkit.getOfflinePlayer(UUID.fromString("a305901d-5c11-41eb-9eb3-13d1bfbf33e7")).isOnline()) Bukkit.getPlayer("SISIG48").sendMessage("! §aNO MAJ");	
			
    	} catch (IOException  e) {
    		if(Bukkit.getOfflinePlayer(UUID.fromString("a305901d-5c11-41eb-9eb3-13d1bfbf33e7")).isOnline()) Bukkit.getPlayer("SISIG48").sendMessage("! §4ERR VERIF");	
    		logs.add("Err Tchecking Update : Erreur de vérification");
    		logs.add(e.getMessage());
    		e.printStackTrace();
    	}
    	
    	return true;
    	
    	
    }
    
	private static String getFirstLine(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String firstLine = reader.readLine();
        reader.close();
        return firstLine;
    }
    
    public static void SendMaj() {
    	for (Player e : Bukkit.getOnlinePlayers()) {
    		if(NeedUpdate == 0 || e == null || e.getUniqueId() == null) break;
    		System.out.println("UUID : " + e.getUniqueId() + " Name : " + e.getName());
    		
    		if(e.isOp()) {
    			e.sendMessage("§4You need update plugin </re>");
    			
    		}
    	}
    }

}

