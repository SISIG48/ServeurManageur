package fr.sisig48.pl.Economie;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.sisig48.pl.Main;
import fr.sisig48.pl.Automating.AutoSave;

@SuppressWarnings("unchecked")
public class ObjectPrice implements Serializable {
	private static final long serialVersionUID = 1L;
	private static ArrayList<ObjectPrice> ops = new ArrayList<ObjectPrice>();
	private static File saves = new File(Main.Plug.getDataFolder() + "/data/item/ObjectPrice.save");
	static {
		if(saves.exists()) {
			try {
				FileInputStream fichierEntree = new FileInputStream(saves);
				ObjectInputStream entreeObjet = new ObjectInputStream(fichierEntree);
				
				ops = (ArrayList<ObjectPrice>) entreeObjet.readObject();
				
				entreeObjet.close();
				fichierEntree.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Material material;
	private int quantité = 0;
	private float prix = 0;
	private ObjectPrice op = this;
	private ObjectPrice(Material m, float prix, int quantité) {
		//Initialise la variable
		op.material = m;
		
		//Récupère l'instance existante
		for(ObjectPrice op : ops) if(op.getMaterial().equals(m)) {
			//Ajoute le conte
			op.prix = prix + op.prix;
			op.quantité = quantité + op.quantité;
			this.op = op;
			
			//Sauvegarde les moyenes
			EconomieData.addChange(m);
			return;
		}
		
		//Crée un instance
		op.prix = prix;
		op.quantité = quantité;
		ops.add(this);
		
		//Sauvegarde les moyens
		EconomieData.addChange(m);
	}
	
	public Float getChange(Float price) {
		if(getPrice() == 0) return (float) 0.00;
		return Economie.roundPrice((price - getPrice()) / getPrice() * 100);
	}
	
	private ObjectPrice(Material m) {
		//Récupère l'instance existate
		for(ObjectPrice op : ops) if(op.getMaterial().equals(m)) this.op = op;
	}
	
	public Material getMaterial() {
		return op.material;
	}
	
	public float getPrice() {
		if(op.quantité != 0) return op.prix/op.quantité;
		else return op.prix;
	}
	
	public static ObjectPrice getObject(Material m) {
		return new ObjectPrice(m);
	}
	
	public static ObjectPrice addObject(ItemStack it, float prix) {
		return new ObjectPrice(it.getType(), prix, it.getAmount());
	}
	
	public static void save() {
		AutoSave.SerializableSave(saves, ops);
	}
	
}
