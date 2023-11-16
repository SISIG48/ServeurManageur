package fr.sisig48.pl.Economie;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;

public class EconomieInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private float price;
	private Material material;
	private Date from;
	
	public EconomieInfo(Material m) {
		price = ObjectPrice.getObject(m).getPrice();
		material = m;
		from = Calendar.getInstance().getTime();;
	}
	
	public float getPrice() {
		return price;
	}
	
	public Material getType() {
		return material;
	}
	
	public Date getFrom() {
		return from;
	}
	
	public Date getEnd(List<EconomieInfo> edi) {
		if((edi.indexOf(this)+1) < edi.size()) return edi.get(edi.indexOf(this) + 1).getFrom();
		return Calendar.getInstance().getTime();
	}
}
