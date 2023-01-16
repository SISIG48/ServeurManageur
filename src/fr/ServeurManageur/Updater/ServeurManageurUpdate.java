package fr.ServeurManageur.Updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur-d.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
            boolean isSame = false;
            String jar1 = "/plugins/ServeurManageur.jar";
    		String jar2 = "/plugins/ServeurManageur-d.jar";
    		
    		try {
    			File file1 = new File(jar1);
    			File file2 = new File(jar2);
    			
    			FileInputStream fis1 = new FileInputStream(file1);
    			FileInputStream fis2 = new FileInputStream(file2);
    			
    			int bytes1, bytes2 = 0;
    			
    			while((bytes1 = fis1.read()) != -1  && (bytes2 = fis2.read()) != -1) {
    				if(bytes1 != bytes2) {
    					isSame = false;
    					break;
    				}
    			}
    			
    			if(bytes1 == -1 && bytes2 == -1) {
    				isSame = true;
    			}
    			
    			fis1.close();
    			fis2.close();
    			
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
            
            if(isSame) {
            	NeedUpdate = 0;
          		return false;
            } else {
            	NeedUpdate = 1;
            	return true;
            }
            

            
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	return true;
    	
    	
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

