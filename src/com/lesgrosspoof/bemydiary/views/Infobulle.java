package com.lesgrosspoof.bemydiary.views;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.maps.MapView;

public class Infobulle extends View implements OnClickListener {
	
	public Infobulle(Context context, MapView mapView) {
		super(context);
		
		setOnClickListener(this);
	}
	
	public void onClick(View arg0) {
		
	}
}
