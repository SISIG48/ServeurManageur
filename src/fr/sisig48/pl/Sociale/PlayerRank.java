package fr.sisig48.pl.Sociale;

import org.bukkit.entity.Player;

import nl.svenar.PowerRanks.api.PowerRanksAPI;

@SuppressWarnings("deprecation")
public class PlayerRank {
	private static PowerRanksAPI powerRankAPI = new PowerRanksAPI();
	public static String GetRank(Player player) {
		return powerRankAPI.getPlayerRank(player);
	}
	
	public static int getInt(String rank) {
		int i = 0;
		for(RankList t : RankList.values()) if(rank.equals(t.getName())) i = t.getId();
		return i;
	}
	
	public static int getPlayerInt(Player player) {
		int i = 0;
		String rank = powerRankAPI.getPlayerRank(player); 
		for(RankList t : RankList.values()) if(rank.equals(t.getName())) i = t.getId();
		return i;
	}
	
	public static String getRank(int i) {
		String rank = "none";
		for(RankList t : RankList.values()) if(i == t.getId()) rank = t.getName();
		return rank;
	}
	
}

enum RankList {
	YT("YT", 5),
	ADMIN("Admin", 9),
	OWNER("Owner", 9),
	TEST("Testeur", 9),
	PAYSAN("Paysan", 1),
	ECUYER("Ecuyer", 2),
	MODO("Moderator", 9),
	BUILDER("Builder", 9),
	CHEVAL("Chevalier", 3),
	SEIGNEUR("Seigneur", 4),
	RESPONSABLE("Responsable", 9);
	
	private String name;
	private int id;
	private RankList(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
}
