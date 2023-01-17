package fr.ServeurManageur.Updater;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
            String jar1 = "/plugins/ServeurManageur.jar";
    		String jar2 = "/plugins/ServeurManageur/ServeurManageur.jar";
    		JarFile jarFile1 = new JarFile(jar1);
			JarFile jarFile2 = new JarFile(jar2);
			
			if (jarFile1.size() != jarFile2.size()) {
				return false;
			}
			
			Enumeration<JarEntry> entries1 = jarFile1.entries();
			Enumeration<JarEntry> entries2 = jarFile2.entries();
			
			while (entries1.hasMoreElements() && entries2.hasMoreElements()) {
				JarEntry entry1 = entries1.nextElement();
				JarEntry entry2 = entries2.nextElement();
					
				if (!entry1.getName().equals(entry2.getName())) {
					return false;
				
				}
				if (entry1.getSize() != entry2.getSize()) {
					return false;
				}
				InputStream is1 = jarFile1.getInputStream(entry1);
				InputStream is2 = jarFile2.getInputStream(entry2);
						
				if (!isStreamsEqual(is1, is2)) {
					return false;
						
				}
					
					
				}    		
			

    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return true;
    	
    	
    }
    
    public static boolean isStreamsEqual(InputStream is1, InputStream is2) throws IOException {
    	int b1 = 0, b2 = 0;
    	
    	while (b1 != -1 && b2 != -1) {
    		b1 = is1.read();
    		b2 = is2.read();
    		
    		if (b1 != b2) {
    			return false;
    		}
    	}
    	
    	return b1 == -1 && b2 == -1;
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

