package net.sisig48.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class WebAccount implements Serializable {
	private static final long serialVersionUID = -5931041720733955615L;
	private static String path = WebServer.getPath();
	private static Map<String, WebAccount> accountList = new HashMap<String, WebAccount>();
	private Object id;
	private String password;
	private List<String> perm = Arrays.asList("user.member");
	private Map<Object, Object> tags = new HashMap<Object, Object>();
	private String token = createToken();
	private InetAddress ip;
	private static List<String> utf8Characters = Arrays.asList(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "@", "$", "!", "*", "/", "-", "'", "\"", "."
		);
	
	
	private WebAccount(Object id, Object password, Map<Object, Object> tags) {
		this.id = id;
		this.password = hash(password);
		this.tags = tags;
		accountList.put(String(id), this);
	}
	
	private static String String(Object object) {
		return String.valueOf(object);
	}
	
	public static WebAccount createAccount(Object id, Object password, Map<Object, Object> tags) {
		if(AlreadySet(id)) return null;
		return new WebAccount(id, password, tags);
	}
	
	public Object getId() {
		return id;
	}
	
	public static String createToken() {
		return hash(UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString());
	}
	
	public static void delAccount(WebAccount account) {
		accountList.remove(String(account.getId()));
	}
	
	public Map<Object, Object> getTag() {
		return this.tags;
	}
	
	public static long createID() {
		Long id = Math.round((Math.random() * Long.valueOf("9999999999")) + ((Math.random() + 1) * 9999));
		if(AlreadySet(id)) return createID();
		return id; 
	}
	
	public static String createPassword() {
		String pass = "";
		for(int i = 0; i <= 16; i++) pass = pass + utf8Characters.get((int) ((utf8Characters.size()-1) * Math.random()));
		return pass;
	}
	
	public static boolean AlreadySet(Object id) {
		if(accountList.containsKey(id)) return true;
		return false;
	}
	
	public void setPerm(List<String> perm) {
		this.perm = perm;
	}
	
	public List<String> getPerm() {
		return this.perm;
	}
	
	
	
	public boolean hasPermission(String permission) {
		return getPerm().contains(permission) || permission.equals(String.valueOf(id));
	}
	
	public boolean hasPermission(List<String> permission) {
		for(String s : getPerm()) if(permission.contains(s)) return true;
		return permission.contains(String(id));
	}
	
	public static WebAccount getUser(Object id, Object password) {
		WebAccount a = accountList.get(String(id));
		if(a != null && String(a.password).equals(hash(password))) return a;
		return null;
	}
	
	public static WebAccount getUser(Object id, Object password, String token, InetAddress ip) {
		WebAccount a = accountList.get(id);
		if(a != null && String(a.password).equals(hash(password)) && a.token.equals(token) && a.ip.equals(ip)) return a;
		return null;
	}
	
	public static WebAccount getUserByTag(Object tag, Object value) {
		for(WebAccount a : accountList.values()) if(a.tags != null && a.tags.get(tag) != null && a.tags.get(tag).equals(value)) return a;
		return null;
	}
	
	public String getUUID() {
		return this.token;
	}
	
	public void setUUID(String token) {
		this.token = token;
	}
	
	public InetAddress getIp() {
		return this.ip;
	}
	
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	
	public static void save() {
		SerializableSave(new File(path + "/users.data"), accountList);
	}
	
	private static void SerializableSave(File out, Object object) {
		try {
			if(!out.exists()) {
				out.getParentFile().mkdir();
				out.createNewFile();
			}
			FileOutputStream fichierSortie = new FileOutputStream(out);
			ObjectOutputStream sortieObjet = new ObjectOutputStream(fichierSortie);
			
			sortieObjet.writeObject(object);
			
			sortieObjet.close();
			fichierSortie.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static {
		File users = new File(path + "/users.data");
		if(users.exists()) {
			try {
				FileInputStream fichierEntree = new FileInputStream(users);
				ObjectInputStream entreeObjet = new ObjectInputStream(fichierEntree);
				
				accountList = (Map<String, WebAccount>) entreeObjet.readObject();
				
				entreeObjet.close();
				fichierEntree.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static String hash(Object input) {
        try {
            // Créer un objet de MessageDigest avec l'algorithme SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Mettre la chaîne de caractères dans le message digest
            md.update(String(input).getBytes());

            // Récupérer le tableau de bytes résultant du hachage
            byte[] hashedBytes = md.digest();

            // Convertir les bytes en une représentation hexadécimale
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
