package fr.sisig48.pl.Utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;


public class Utils {
	private static Map<UUID, BufferedImage> pplist = new HashMap<UUID, BufferedImage>();
	public static String getBase64Profile(UUID uuid) {
		if(!pplist.containsKey(uuid)) try {
			pplist.put(uuid, ImageIO.read(new URL("https://minotar.net/avatar/" + uuid)));
        } catch (Exception e) {}
		return new String(encodeImageToBase64(pplist.get(uuid)));
	}
	
	private static byte[] encodeImageToBase64(BufferedImage image) {
        try {
            // Convertir l'image en tableau de bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // Encoder le tableau de bytes en Base64
            return Base64.getEncoder().encode(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
