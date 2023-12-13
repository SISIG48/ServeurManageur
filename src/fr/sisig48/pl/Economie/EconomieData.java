package fr.sisig48.pl.Economie;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Automating.AutoSave;

@SuppressWarnings({ "unchecked" })
public class EconomieData implements Serializable {
	private static final long serialVersionUID = 1L;
	private final transient static File saves = new File(Main.Plug.getDataFolder() + "/data/item/oldData.save");
	
	private Material material;
	private static List<EconomieData> list = new ArrayList<EconomieData>();
	private List<EconomieInfo> edi = new ArrayList<EconomieInfo>();
	private static List<Material> ms = new ArrayList<Material>();
	private EconomieData ei = this;
	
	
	static {
		if(saves.exists()) {
			try {
				FileInputStream fichierEntree = new FileInputStream(saves);
				ObjectInputStream entreeObjet = new ObjectInputStream(fichierEntree);
				
				list = (List<EconomieData>) entreeObjet.readObject();
				
				entreeObjet.close();
				fichierEntree.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		for(EconomieData ed : list) {
			List<EconomieInfo> r = new ArrayList<EconomieInfo>();
			for(EconomieInfo ei : ed.edi) r.add(0, ei);
			
			Date last = Calendar.getInstance().getTime();
			for(EconomieInfo ei : r) {
				long end = ei.getEnd(ed.edi).getTime();
				long diff = (end - last.getTime()) / (24 * 60 * 60 * 1000);
				long startdiff = ((end - Calendar.getInstance().getTime().getTime()) / (24 * 60 * 60 * 1000));
				if(startdiff > 5) {
					//Si il date de plus de 5 jours
					if(diff > 1) {
						//Si il date de plus 1 jours comparré au dernier sauvegarder
						if(startdiff > 30 && diff < 30) ed.edi.remove(ei); //Si il a moins d'un mois avec le précédent
						else last = ei.getEnd(ed.edi);
					} else ed.edi.remove(ei);
				} else last = ei.getEnd(ed.edi);
			}
		}
	}
	
	public Material getType() {
		return material;
	}
	
	public static EconomieData[] getSaves() {
		return list.toArray(new EconomieData[0]);
	}
	
	public EconomieInfo[] getInfo() {
		return edi.toArray(new EconomieInfo[0]);
	}
	
	public static void addChange(Material m) {
		ms.add(m);
	}
	
	public static File getDataFolder() {
		return saves;
	}
	
	private EconomieData(Material m) {
		//Vérifie les instances éxistantes
		for(EconomieData eis : list) if(eis.material.equals(m)) {
			ei = eis;
			ei.edi.add(new EconomieInfo(m));
			return;
		}
		
		//Nouvelle Instance
		material = m;
		list.add(ei);
		
		ei.edi.add(new EconomieInfo(m));
	}
	
	public static void saveInfo() {
		for(Material m : ms) new EconomieData(m);
		AutoSave.SerializableSave(saves, list);
	}
}