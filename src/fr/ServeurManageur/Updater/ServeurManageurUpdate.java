package fr.ServeurManageur.Updater;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;


import fr.sisig48.pl.Utils.OnlinePlayer;


public class ServeurManageurUpdate {
	public static int NeedUpdate;
    public static Boolean DoUpdate(CommandSender sender) {

        try {
        	URL url = new URL("https://github.com/SISIG48/ServeurManageur/blob/main/ServeurManageur.jar?raw=true");
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            Bukkit.dispatchCommand(sender, "rl");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    
    }
    

    public static Boolean CheckUpdate() {
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
			Bukkit.getPlayer("SISIG48").sendMessage("! §eJar loale zize : §a" + jarFile1.size() + " §e| Jar GIT Zize : §a" + jarFile2.size());	
			if (jarFile1.size() != jarFile2.size()) {
				return true;
			}
			
			Enumeration<JarEntry> entries1 = jarFile1.entries();
			Enumeration<JarEntry> entries2 = jarFile2.entries();
			
			while (entries1.hasMoreElements() && entries2.hasMoreElements()) {
				JarEntry entry1 = entries1.nextElement();
				JarEntry entry2 = entries2.nextElement();		
				if (!entry1.getName().equals(entry2.getName())) {
					return true;
				
				}
				if (entry1.getSize() != entry2.getSize()) {
					return true;
				}		
				
					
				}    		
		Bukkit.getPlayer("SISIG48").sendMessage("! §aNO MAJ");	

    	} catch (Exception e) {
    		Bukkit.getPlayer("SISIG48").sendMessage("! §4ERR VERIF");	
    		e.printStackTrace();
    	}
    	return false;
    	
    	
    }
    
    public static void SendMaj() {
    	for (String e : OnlinePlayer.OnlinePlayer) {
    		if(NeedUpdate == 0 || e == null) break;
    		if(Bukkit.getPlayer(UUID.fromString(e)) == null) break;
    		System.out.println(UUID.fromString(e));
    		if(Bukkit.getPlayer(UUID.fromString(e)).isOnline() && Bukkit.getPlayer(UUID.fromString(e)).isOp()) {
    			Bukkit.getPlayer(UUID.fromString(e)).sendMessage("§4You need update plugin </re>");
    		}
    	}
    }

}

