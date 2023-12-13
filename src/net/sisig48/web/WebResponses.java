package net.sisig48.web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebResponses {
	private static String path = WebServer.getPath();
	private static File login;
	private static List<String> access;
	
	static {
		load();
	}
	
	

	private static List<WebResponsesListener> listener = new ArrayList<WebResponsesListener>();
    public static void AddWebResponsesListener(WebResponsesListener listener) {
        WebResponses.listener.add(listener);
    }
	
	WebResponses(Socket clientSocket) {
		Thread thread = new Thread(() -> {
    		try (
    				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    				OutputStream out = clientSocket.getOutputStream()
    				) {
    			@SuppressWarnings("unused")
    			File file;
    			List<String> requestList = new ArrayList<String>();
    			String r;
    			while(!(r = in.readLine()).equals("") && r != null) requestList.add(r);
    			WebResponsesData data = new WebResponsesData(clientSocket, requestList, in, out);
    			if(data.getType() != null) {
    				try{for(WebResponsesListener l : listener) if(l.onWebResponse(data)) return;} catch (Exception e) {e.printStackTrace();}
    				
    				String type = data.getType();
    				if(type.equals("GET")) {
    					
    					// Récupération du compte
    					WebAccount account = data.getAccount();
    					
    					// Récupération des information demmander
    					String request = data.getRequest();
    					
    					if(request.contains("?")) {
    						// Système de conextion au profile
    						if(request.startsWith("/login?")) {
    							if(request.startsWith("/login?out")) {
    								if(account == null) WebView.PageUnauthorized(out);
            						else WebView.returnText(Arrays.asList("id: " + account.getId(), "\r\nLogout"), out, "account=; pass=; uuid=; Expires=Thu, 01 Jan 1970 00:00:00 GMT");
    								return;
    							}
    							
    							try {
            						request = request.replace("/login?", "");
            						Object id = request.split(",")[0].replace("id=", "");
            						Object pass = request.split(",")[1].replace("pass=", "");
            						account = WebAccount.getUser(id, pass);
            						
            						if(account == null) {
            							WebView.PageUnauthorized(out);
            							return;
            						}
            						
            						account.setIp(clientSocket.getInetAddress());
        							WebView.returnText(Arrays.asList("id: " + account.getId(), "\r\npass: " + account.getTag()), out, "account=" + account.getId() + "; pass=" + account.getPassword() + "; uuid=" + account.getUUID().toString());
        							return;
    							} catch (Exception e) {
    								e.printStackTrace();
    								WebView.PageBadResquest(out);
    								return;
    							}
    						} else request = (String) request.subSequence(0, request.lastIndexOf("?")); // Système pour les paramètre dans les URL
    					}
    					
    					
    					
    					// Récupération locale
    					File Outfile = new File(path + request);
    					
    					// Vérification de la disponibilité local
    					if(access.contains("!" + request) || !Outfile.getPath().replace("\\", "/").startsWith(path)) {
    						WebView.PageForbidden(out);
    						return;
    					}
    					
    					// Redirection
    					if(access.contains("!!" + request)) {
    						int index = access.indexOf("!!" + request); 
    						if(access.get(index-1).split("\\s*to\\s*")[1].startsWith("$")) Outfile = new File(access.get(index-1).split(" to ")[1].replaceAll("\\$\\/?", ""));
    						else Outfile = new File(path + access.get(index-1).split("\\s*to\\s*")[1]);
    					} 
    					
    					// Vérification de l'autorisation
    					if(access.contains("??" + request)) {
    						int index = access.indexOf("??" + request);
    						if(account == null) {
    							WebView.returnData(login, out, WebType.html.getInfo());
    							return;
    						} else if(!account.hasPermission(Arrays.asList(access.get(index+1).replace("?", "").split("\\s*,\\s*")))) {
    							WebView.PageUnauthorized(out);
    							return;
    						}
    					}
    					
    					// Renvoie de la page
    					if(Outfile.exists() && Outfile.isFile()) {
    						for(WebType wt : WebType.values()) if(Outfile.getPath().endsWith(wt.getExt())) { 
    							WebView.returnData(Outfile, out, wt.getInfo());
    							return;
    						}
    						WebView.returnData(Outfile, out, WebType.text.getInfo());
    						return;
    					}
    					
    				}
    				WebView.PageNotFound(out);
    				return;
    			} else {
    				WebView.PageBadResquest(out);
    			}
    		} catch (Exception e) {}
    		
    		try {clientSocket.close();} catch (Exception e) {}
    	});
    	
    	thread.start();
	}
	
	public static void load() {
		access = getFiles();
		save();
	}

	public static void addAccess(String data) {
		access.add(data);
	}
	
	public static boolean dellAccess(String data) {
		return access.remove(data);
	}
	
	public static void save() {
		File file = new File(path + "/access.txt");
		if(!file.exists()) {
			try {
				file.getParentFile().mkdir();
				file.createNewFile();
				FileWriter MyFileW = new FileWriter(file);
				BufferedWriter bufWriter = new BufferedWriter(MyFileW);
				
				List<String> line = new ArrayList<String>();
				line.add("//Denied file access, write \"!/path/file.ext\"");
				line.add("//To create a file shortcut, write \"/file/shortcut to /path/file.ext\" ; add \"$\" before file path for set absolute path of the file");
				line.add("//To define restricted access to a file, write \"/file/shortcut need perm.acces\". ; add \"$\" before file path for set absolute path of the file");
				line.add("//Use \"//\" to comment out this file");
				line.addAll(access);
				
				for(String s : line) {
			    	bufWriter.write(s);
			    	bufWriter.newLine();
			    }
			    
		        bufWriter.close();
		        MyFileW.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static List<String> getFiles() {
		List<String> l = new ArrayList<String>();
		File file = new File(path + "/access.txt");
		
		try {
			if(!file.exists()) {
				file.getParentFile().mkdir();
				file.createNewFile();
				FileWriter MyFileW = new FileWriter(file);
			    BufferedWriter bufWriter = new BufferedWriter(MyFileW);
			    
			    List<String> line = new ArrayList<String>();
			    line.add("//Denied file access, write \"!/path/file.ext\"");
			    line.add("//To create a file shortcut, write \"/file/shortcut to /path/file.ext\" ; add \"$\" before file path for set absolute path of the file");
			    line.add("//To define restricted access to a file, write \"/file/shortcut need perm.acces (, perm2.acces, ...)\". ; add \"$\" before file path for set absolute path of the file");
			    line.add("//Use \"//\" to comment out this file");
			    line.add("\n\r");
			    line.add("#Password: none");
			    line.add("#Login-page: /login.html");
			    
			    for(String s : line) {
			    	bufWriter.write(s);
			    	bufWriter.newLine();
			    }
			    
		        bufWriter.close();
		        MyFileW.close();
			}
			
			FileReader MyFileR;
			MyFileR = new FileReader(file);
			BufferedReader br = new BufferedReader(MyFileR);
			String r;
			@SuppressWarnings("unused")
			ArrayList<String> line = new ArrayList<String>();
			while((r = br.readLine()) != null) if(r.startsWith("//")) continue; else {
				if(r.startsWith("#Password:")) {
					WebServer.setPassword(r.split("#Password: \\s*")[1].toCharArray());
					continue;
				}
				
				if(r.startsWith("#Login-page:")) {
					login = new File(path + r.split("#Login-page:\\s*")[1]);
					if(!login.exists()) login.createNewFile();
					continue;
				}
				
				l.add(r);
				if(!r.startsWith("!")) {
					if(r.contains(" to ")) {
						l.add("!!" + r.split("\\s*to\\s*")[0]);
						l.add("!" + r.split("\\s*to\\s*")[1]);
					} else if(r.contains(" need ")) {
						l.add("??" + r.split("\\s*need\\s*")[0]);
						l.add("?" + r.split("\\s*need\\s*")[1]);
					}
				}
			}
			MyFileR.close();
			if(!l.contains("!/access.txt")) l.add("!/access.txt");
			if(!l.contains("!/keystore.jks")) l.add("!/keystore.jks");
			if(!l.contains("!/users.data")) l.add("!/users.data");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return l;
	}
}
