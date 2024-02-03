package net.sisig48.web;

import java.util.HashMap;

public enum WebType {
	//Base
	application("application/octet-stream", ".*"),
	video("video/mp4", ".mp4"),
	image("image/x-icon", ".png"),
	image2("image/jpeg", ".jpg"),
	html("text/html", ".html"),
	text("text/plain", ".txt");
	
	private static HashMap<String, String> list = new HashMap<String, String>();
	private String data;
	private String ext;
	private WebType(String data, String ext) {
		this.data = data;
		this.ext = ext;
	}
	
	public String getInfo() {
		return data;
	}
	
	public String getExt() {
		return ext;
	}
	
	//Extentions static
	static {
		for(WebType type : values()) list.put(type.ext.substring(type.ext.indexOf(".")+1), type.data);
	}
	
	public static void addType(String extensions, String type) {
		list.put(extensions.substring(extensions.indexOf(".")+1), type);
	}
	
	public static String getByExtensions(String extensions) {
		return list.get(extensions);
	}
}
