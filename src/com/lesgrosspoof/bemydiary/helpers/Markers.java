package com.lesgrosspoof.bemydiary.helpers;

import java.util.HashMap;

public class Markers {
	
	private static Markers instance;
	
	public static Markers getInstance() {
		if (instance == null)
			instance = new Markers();
		return instance;
	}
	
	private HashMap<String, Integer> markers;
	
	public Markers() {
		this.reset();
	}
	
	public void add(String markerId, int lieuId) {
		markers.put(markerId, lieuId);
	}
	
	public int get(String markerId) {
		return markers.get(markerId);
	}
	
	public void reset() {
		markers = new HashMap<String, Integer>();
	}
}
