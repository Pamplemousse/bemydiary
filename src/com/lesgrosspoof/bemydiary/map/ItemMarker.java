package com.lesgrosspoof.bemydiary.map;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.lesgrosspoof.bemydiary.entities.Item;

public class ItemMarker extends OverlayItem {
	
	private Item item;
	
	public ItemMarker(GeoPoint point, String title, String snippet, Item item) {
		super(point, title, snippet);
		
		this.item = item;
		// TODO Auto-generated constructor stub
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

}
