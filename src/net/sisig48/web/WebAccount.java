package net.sisig48.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fr.sisig48.pl.Automating.AutoSave;

@SuppressWarnings("unchecked")
public class WebAccount implements Serializable {
	private static final long serialVersionUID = -5931041720733955615L;
	private static String path = WebServer.getPath();
	private static Map<Long, WebAccount> accountList = new HashMap<Long, WebAccount>();
	private WebAccount wa = this;
	private long id;
	private String password;
	private String perm = "user.member";
	private String tag;
	private UUID uuid = UUID.randomUUID();
	private InetAddress ip;
	private static List<String> utf8Characters = Arrays.asList(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "@", "$", "!", "§", "?","*", "/", "-", "'", "\"", "."
		);
	
	
	public WebAccount(long id, String password, String tag) {
		WebAccount a = accountList.get(id);
		if(a != null && a.id == id && a.password.equals(password)) {
				wa = a;
				return;
		}
		wa.uuid = UUID.randomUUID();
		wa.id = createID();
		wa.password = createPassword(getId());
		wa.tag = tag;
		accountList.put(wa.id, wa);
	}
	
	public WebAccount(String tag) {
		wa.uuid = UUID.randomUUID();
		wa.id = createID();
		wa.password = createPassword(getId());
		wa.tag = tag;
		accountList.put(wa.id, wa);
	}
	
	public long getId() {
		return wa.id;
	}
	
	public static void delAccount(WebAccount account) {
		accountList.remove(account.getId());
	}
	
	public String getPassword() {
		return wa.password;
	}
	
	public String getTag() {
		return wa.tag;
	}
	
	public static long createID() {
		Long id = Math.round((Math.random() * Long.valueOf("9999999999")) + ((Math.random() + 1) * 9999));
		if(AlreadySet(id)) return createID();
		return id; 
	}
	
	public static String createPassword(long id) {
		String pass = "";
		for(int i = 0; i <= 16; i++) pass = pass + utf8Characters.get((int) ((utf8Characters.size()-1) * Math.random()));
		return pass;
	}
	
	private static boolean AlreadySet(long id) {
		if(accountList.containsKey(id)) return true;
		return false;
	}
	
	public void setPerm(String perm) {
		wa.perm = perm;
	}
	
	public String getPerm() {
		return wa.perm;
	}
	
	public boolean hasPermission(String permission) {
		return permission.equals(getPerm()) || permission.equals(String.valueOf(id));
	}
	
	public boolean hasPermission(List<String> permission) {
		return permission.contains(getPerm()) || permission.contains(String.valueOf(id));
	}
	
	public static WebAccount getUser(long id, String password) {
		WebAccount a = accountList.get(id);
		if(a != null && a.password.equals(password)) return a;
		return null;
	}
	
	public static WebAccount getUser(long id, String password, UUID uuid, InetAddress ip) {
		WebAccount a = accountList.get(id);
		if(a != null && a.password.equals(password) && a.uuid.equals(uuid) && a.ip.equals(ip)) return a;
		return null;
	}
	
	public static WebAccount getUser(UUID uuid) {
		for(WebAccount a : accountList.values()) if(a.uuid != null && a.uuid.equals(uuid)) return a;
		return null;
	}
	
	public static WebAccount getUser(String tag) {
		for(WebAccount a : accountList.values()) if(a.tag.equals(tag)) return a;
		return null;
	}
	
	public UUID getUUID() {
		return wa.uuid;
	}
	
	public void setUUID(UUID uuid) {
		wa.uuid = uuid;
	}
	
	public InetAddress getIp() {
		return wa.ip;
	}
	
	public void setIp(InetAddress ip) {
		wa.ip = ip;
	}
	
	public static void save() {
		AutoSave.SerializableSave(new File(path + "/users.data"), accountList);
	}
	
	static {
		File users = new File(path + "/users.data");
		if(users.exists()) {
			try {
				FileInputStream fichierEntree = new FileInputStream(users);
				ObjectInputStream entreeObjet = new ObjectInputStream(fichierEntree);
				
				accountList = (Map<Long, WebAccount>) entreeObjet.readObject();
				
				entreeObjet.close();
				fichierEntree.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
