package fr.sisig48.pl.JobsHouse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.*;
import com.sk89q.worldedit.extent.clipboard.*;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.*;
import com.sk89q.worldedit.session.ClipboardHolder;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.sisig48.pl.Sociale.Jobs;
import fr.sisig48.pl.Sociale.PlayerJobs;

public class HouseData {
	protected ArrayList<String> line = new ArrayList<String>();
	public static ArrayList<String> lineLoc = new ArrayList<String>();
	private Jobs jobs;
	private int NumHouse;
	private static ArrayList<HouseData> hd = new ArrayList<HouseData>();
	private static ArrayList<Jobs> hdj = new ArrayList<Jobs>();
	public HouseData(Jobs j) {
		if(hdj.contains(j)) return;
		hdj.add(j);
		hd.add(this);
		j.setHouseData(this);
		jobs = j;
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public File getFile() {
		return new File("plugins/ServeurManageur/data/jobs/house/" + jobs.getName() + ".data");
	}
	
	public static File getLocFile() {
		return new File("plugins/ServeurManageur/data/jobs/house/location.data");
	}
	
	private Jobs jobs() {
		return jobs;
	}
	
	private void init() throws IOException {
		if(!getLocFile().exists()) {
			getLocFile().createNewFile();
			lineLoc.add("?HouseLocationData");
			FileWriter MyFileW = new FileWriter(getLocFile());
			BufferedWriter bufWriter = new BufferedWriter(MyFileW);
			bufWriter.write("?HouseLocationData");
			bufWriter.newLine();
			bufWriter.close();
			MyFileW.close();
		} else if(lineLoc.isEmpty()) loadLoc();
		
		if(!getFile().exists()) {
			int i = 1;
			getFile().createNewFile();
			line.add("?HouseData");
			while((new File("plugins/ServeurManageur/data/jobs/house/" + jobs.getName() + "_" + i +".schem")).exists()) {
				line.add(" ");
				line.add("%" + i + ":");
				i++;
			}
			save();
		}
		else load();
	}
	
	private void load() throws IOException {
		FileReader MyFileR = new FileReader(getFile());
		BufferedReader br = new BufferedReader(MyFileR);
		String r;
		line = new ArrayList<String>();
		while((r = br.readLine()) != null) line.add(r);  
		MyFileR.close();
		    
		int i = 0;
		int rs = line.size() -1;
		while(rs != 0 & i < rs & !line.contains("?HouseData")) {
			rs = line.size() -1;
		    if(line.get(i).contains("?HouseData")) line.remove(i);
		    else i++;
		}
		if(line.size() != 0 &&!line.get(0).equalsIgnoreCase("?HouseData")) line.add(0, "?HouseData");
		save();
		extrudeInfo();
	 }

	 private void extrudeInfo() {
		 String nt[] = {"1","2","3","4","5","6","7","8","9","0"};
		 ArrayList<String> num = new ArrayList<String>();
		 int n;
		 String t;
		 int i = 0;
		 for(String e : line) {
			 if(e.equals("?HouseData") || e == null || e.equals("")) break;
			 t = e.split("%")[0].split(":")[0];
			 if(((num.contains(t))) && (i++) != 0) n = Integer.valueOf(t);
			 else {
				 String[] temp = e.split(" ");
				 if(temp.length == 5);
				 Location loc = new Location(Bukkit.getWorld(temp[3]), Integer.valueOf(temp[0]), Integer.valueOf(temp[1]), Integer.valueOf(temp[2]));
				 Player p = Bukkit.getPlayer(UUID.fromString(temp[4]));
			 }
			 
		 }
		 NumHouse = i;
	}

	private void save() throws IOException {
		FileWriter MyFileW = new FileWriter(getFile());
		BufferedWriter bufWriter = new BufferedWriter(MyFileW);
		for(String e : line) {    
			bufWriter.write(e);
			bufWriter.newLine();
		}
	    bufWriter.close();
	    MyFileW.close();
	 }
	 
	 private void loadLoc() throws IOException {
		 FileReader MyFileR = new FileReader(getLocFile());
		 BufferedReader br = new BufferedReader(MyFileR);
		 String r;
		 lineLoc = new ArrayList<String>();
		 while((r = br.readLine()) != null) lineLoc.add(r);  
		 MyFileR.close();
		 
		 int i = 0;
		 int rs = lineLoc.size() -1;
		 while(rs != 0 & i < rs & !lineLoc.contains("?HouseLocationData")) {
			 rs = lineLoc.size() -1;
			 if(lineLoc.get(i).contains("?HouseLocationData")) line.remove(i);
			 else i++;
		 }
		 if(lineLoc.size() != 0 &&!lineLoc.get(0).equalsIgnoreCase("?HouseLocationData")) lineLoc.add(0, "?HouseLocationData");
		 save();
	 }
	 
	 private static void saveLoc() {
			FileWriter MyFileW;
			try {
				MyFileW = new FileWriter(getLocFile());
				BufferedWriter bufWriter = new BufferedWriter(MyFileW);
				for(String e : lineLoc) {    
					bufWriter.write(e);
					bufWriter.newLine();
				}
			    bufWriter.close();
			    MyFileW.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		 }
	 
	 public static void addLoc(Location loc) {
		 lineLoc.add(String.valueOf(loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getWorld().getName()));
		 saveLoc();
	 }
	 
	 public static void delLoc(Location loc) {
		 lineLoc.remove(String.valueOf(loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getWorld().getName()));
		 saveLoc();
	 }
	 
	 public static Location[] getLoc() {
		 ArrayList<Location> locs = new ArrayList<Location>();
		 
		 String[] s;
		 for(String t : lineLoc) {
			 s = t.split(" ");
			 if(s.length < 4) break;
			 locs.add(new Location(Bukkit.getWorld(s[3]), Double.valueOf(s[0]), Double.valueOf(s[1]), Double.valueOf(s[2])));
		 }
		 return (Location[]) locs.toArray();
	 }

	@SuppressWarnings("deprecation")
	private static void InvokeHouse(Location location, Player p, int r) {
		Jobs j = new PlayerJobs(p).get();
		HouseData hd = j.getHouseData();
		Bukkit.getConsoleSender().sendMessage("§4House Create in " + location.getWorld().getName());
		File filePath = new File("plugins/ServeurManageur/data/jobs/house/" + hd.jobs.getName() + "_" + r +".schem");
		if(!filePath.exists()) {
			p.sendMessage("House does not exist");
			return;
		}
		try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), -1)) {
	        ClipboardFormat clipboardFormat = ClipboardFormats.findByFile(filePath);
	        ClipboardReader reader = clipboardFormat.getReader(new FileInputStream(filePath));
	        Clipboard clipboard = reader.read();
	        Operation operation = new ClipboardHolder(clipboard)
	                .createPaste(editSession)
	                .to(BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ()))
	                .ignoreAirBlocks(false)
	                .build();
	        Operations.complete(operation);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	public static void delHouse(Location loc, Player p) {
		Jobs j = new PlayerJobs(p).get();
		int i = -1;
		ArrayList<String> line = new ArrayList<String>(j.getHouseData().line);
		for(String e : line) {
			i++;
			String[] t = e.split(" ");
			if(t.length == 5) if(t[4].equals(String.valueOf(p.getUniqueId())) && t[3].equals(loc.getWorld().getName()) && t[0].equals(String.valueOf(loc.getX())) && t[1].equals(String.valueOf(loc.getY())) && t[2].equals(String.valueOf(loc.getZ()))) break;
		}
		j.getHouseData().line.remove(i);
		try {
			j.getHouseData().save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		double rx = loc.getX();
		double ry = loc.getY();
		double rz = loc.getZ();
		for(double y = 0 ; y != 25 ; y++) for(double x = 0 ; x != 12 ; x++) for(double z = 0 ; z != 15 ; z++) {
			Block block = loc.getWorld().getBlockAt(new Location(loc.getWorld(), rx - x , ry + y, rz - z));
			Material blockType = Material.AIR;
			block.setType(blockType);
		}
	}
	
	public static void delAllHouse(Player p) {
		Jobs j = new PlayerJobs(p).get();
		int i = -1;
		ArrayList<String> line = new ArrayList<String>(j.getHouseData().line);
		for(String e : line) {
			i++;
			String[] t = e.split(" ");
			if(t.length == 5) if(t[4].equals(String.valueOf(p.getUniqueId()))) {
				Location loc = new Location(Bukkit.getWorld(t[3]), Double.valueOf(t[0]), Double.valueOf(t[1]), Double.valueOf(t[2]));
				if(!j.getHouseData().line.remove(e)) return;
				try {
					j.getHouseData().save();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				double rx = loc.getX();
				double ry = loc.getY();
				double rz = loc.getZ();
				for(double y = 0 ; y != 25 ; y++) for(double x = 0 ; x != 12 ; x++) for(double z = 0 ; z != 15 ; z++) {
					Block block = loc.getWorld().getBlockAt(new Location(loc.getWorld(), rx - x , ry + y, rz - z));
					Material blockType = Material.AIR;
					block.setType(blockType);
				}
			}
		}
		
	}
	
	public static void addHouse(Player p, Location loc) {
		Jobs j = new PlayerJobs(p).get();
		int n = j.getHouseData().NumHouse;
		n = (int) (Math.random() * n);
		if(n > j.getHouseData().NumHouse) n = j.getHouseData().NumHouse;
		if(n <= 0) n = 1;
		HouseData hd = j.getHouseData();
		String t;
		int i = 0;
		Boolean find = false;
		ArrayList<String> lineC = new ArrayList<String>(hd.line);
		for(String e : lineC) {
			i++; 
			if(e.equals("%" + String.valueOf(n) + ":")) find = true;
			if(find) if((find = false) == false) hd.line.add(i, String.valueOf(loc.getX()) + " " + String.valueOf(loc.getY()) + " " + String.valueOf(loc.getZ()) + " " + loc.getWorld().getName() + " " + String.valueOf(p.getUniqueId()));
			
		}
		InvokeHouse(loc, p, n);
		try {
			hd.save();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}


