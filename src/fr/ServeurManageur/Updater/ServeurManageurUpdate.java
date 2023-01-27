package fr.ServeurManageur.Updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.sisig48.pl.logs;


public class ServeurManageurUpdate {
	public static int NeedUpdate;

	public static Boolean DoUpdate(CommandSender sender) {
    	logs.add("Maj start by : " + sender.getName());
        try {
        	URL url = new URL("https://github.com/SISIG48/ServeurManageur/blob/main/ServeurManageur.jar?raw=true");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            
            Note();
            
            
            Bukkit.dispatchCommand(sender, "rl");
            logs.add("Maj end");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        logs.add("Maj err");
        return false;
        
    }
    
	public static Boolean DoSpecificUpdate(CommandSender sender, String version) {
    	logs.add("Maj start by : " + sender.getName());
        try {
        	URL url = new URL("https://github.com/SISIG48/ServeurManageur/blob/main/old/data/ServeurManageur%20"+ version +".jar?raw=true");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            
            Note();
            
            
            Bukkit.dispatchCommand(sender, "rl");
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

    @SuppressWarnings("resource")
	public static Boolean CheckUpdate() {
    	logs.add("Start Tchecking Update from : https://github.com/SISIG48/ServeurManageur/blob/main/ServeurManageur.jar?raw=true");
    	try {
    		URL url = new URL("https://github.com/SISIG48/ServeurManageur/blob/main/ServeurManageur.jar?raw=true");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur/ServeurManageur.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            String jar1 = "plugins/ServeurManageur.jar";
    		String jar2 = "plugins/ServeurManageur/ServeurManageur.jar";
    		JarFile jarFile1 = new JarFile(jar1);
			JarFile jarFile2 = new JarFile(jar2);
			if(Bukkit.getOfflinePlayer(UUID.fromString("a305901d-5c11-41eb-9eb3-13d1bfbf33e7")).isOnline()) Bukkit.getPlayer("SISIG48").sendMessage("! §eJar loale zize : §a" + jarFile1.size() + " §e| Jar GIT Zize : §a" + jarFile2.size());	
			if (jarFile1.size() != jarFile2.size()) {
				return true;
			}
			Enumeration<JarEntry> entries1 = jarFile1.entries();
			Enumeration<JarEntry> entries2 = jarFile2.entries();
			
			while (entries1.hasMoreElements() && entries2.hasMoreElements()) {
				JarEntry entry1 = entries1.nextElement();
				JarEntry entry2 = entries2.nextElement();		
				if (!entry1.getName().equals(entry2.getName())) {
					jarFile1.close();
					jarFile2.close();
					return true;
				
				}
				if (entry1.getSize() != entry2.getSize()) {
					jarFile1.close();
					jarFile2.close();
					return true;
				}		
				
					
				}  
				
		if(Bukkit.getOfflinePlayer(UUID.fromString("a305901d-5c11-41eb-9eb3-13d1bfbf33e7")).isOnline()) Bukkit.getPlayer("SISIG48").sendMessage("! §aNO MAJ");	
		jarFile1.close();
		jarFile2.close();
    	} catch (Exception e) {
    		if(Bukkit.getOfflinePlayer(UUID.fromString("a305901d-5c11-41eb-9eb3-13d1bfbf33e7")).isOnline()) Bukkit.getPlayer("SISIG48").sendMessage("! §4ERR VERIF");	
    		logs.add("Err Tchecking Update : Erreur de vérification");
    		e.printStackTrace();
    	}
    	
    	return false;
    	
    	
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

