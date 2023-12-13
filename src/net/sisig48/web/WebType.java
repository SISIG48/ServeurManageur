package net.sisig48.web;

public enum WebType {
	application("application/octet-stream", ".*"),
	video("video/mp4", ".mp4"),
	image("image/x-icon", ".png"),
	html("text/html", ".html"),
	text("text/plain", ".txt");
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
}
