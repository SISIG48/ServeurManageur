package fr.ServeurManageur.Updater;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

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
            FileOutputStream fos = new FileOutputStream("plugins/ServeurManageur-d.jar");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
    		File file = new File("plugins/ServeurManageur-d.jar");
    		byte[] remoteFileBytes = Files.readAllBytes(Paths.get("plugins/ServeurManageur-d.jar"));
            byte[] localFileBytes = url.openStream().readAllBytes();
            if (Arrays.equals(remoteFileBytes, localFileBytes)) {
            	NeedUpdate = 0;
            	file.delete();
            	return false;
            }
            file.delete();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	
    	NeedUpdate = 1;
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

