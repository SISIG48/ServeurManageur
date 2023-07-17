package fr.sisig48.pl.Sociale;

import org.bukkit.Bukkit;

import nl.svenar.PowerRanks.api.*;

@SuppressWarnings("deprecation")
public class PlayerRank {
	private static PowerRanksAPI powerRankAPI = new PowerRanksAPI();
	public static String GetRank(String playerName) {
		return powerRankAPI.getPlayerRank(Bukkit.getPlayer(playerName));
	}
}
