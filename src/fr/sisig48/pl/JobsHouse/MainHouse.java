package fr.sisig48.pl.JobsHouse;

import fr.sisig48.pl.Sociale.Jobs;

public class MainHouse {
	public static void init() {
		for(Jobs j : Jobs.values()) new HouseData(j);
	}
}
