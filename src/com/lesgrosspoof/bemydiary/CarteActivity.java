package com.lesgrosspoof.bemydiary;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.helpers.Markers;
import com.lesgrosspoof.bemydiary.map.CustomInfoWindowAdapter;
import com.lesgrosspoof.bemydiary.map.CustomMapPopup;

public class CarteActivity extends FragmentActivity implements OnClickListener {
	
	private GoogleMap map;
	
	private MapView mapView;
	private RelativeLayout mapLayout;
	
	private CustomMapPopup popup;
	private Marker selectedMarker;
	
	private ArrayList<Marker> markers;
	
	int currentMarkerIndex = 0;
	
	private Button prev;
	private Button next;
	private Button medias;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_carte);

        
        Button retour = (Button)findViewById(R.id.retour);
        retour.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
        
        markers = new ArrayList<Marker>();
        
        mapView = (MapView) findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);
        try {
           MapsInitializer.initialize(this);
        } catch (GooglePlayServicesNotAvailableException e) {
           e.printStackTrace();
        }

        
        map = mapView.getMap();

	    // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
		
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(44.836395, -0.605072), 8));
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter(this.getApplicationContext()));
        
        prev = (Button) findViewById(R.id.mapPrevButton);
        prev.setOnClickListener(this);
        next = (Button) findViewById(R.id.mapNextButton);
        next.setOnClickListener(this);
        medias = (Button) findViewById(R.id.mapMediasButton);
        medias.setOnClickListener(this);
        
        if(Board.getCurrentBoard().getSelection().size() < 2)
        {
        	prev.setVisibility(View.INVISIBLE);
        	next.setVisibility(View.INVISIBLE);
        	
        	if(Board.getCurrentBoard().getSelection().size() <= 0)
            {
        		medias.setVisibility(View.INVISIBLE);
            }
        }
        
        
        this.displayMarkers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_carte, menu);
        return true;
    }
	
	public void displayMarkers() {
		
		Markers markerHelper = Markers.getInstance();
		
		List<ItemSelection> items = Board.getCurrentBoard().getSelection();
        
        for(ItemSelection item : items)
        {
        	MarkerOptions options = new MarkerOptions()
        		.position(new LatLng(item.getLongitude(), item.getLatitude()))
        		.title(item.getNom())
        		.snippet(item.getAdresse() + " " + item.getCode_postal() + " " + item.getVille())
        		.icon(BitmapDescriptorFactory.fromResource(R.drawable.carte_marqueur_1));
        	
        	Marker marker = map.addMarker(options);
        	
        	markers.add(marker);
        	
        	markerHelper.add(marker.getId(), item.getId());
        }
	}
	
	public void Onclick(View v)
	{
		
	}

	public boolean onMarkerClick(Marker marker)
	{
		return true;
	}

	public void onCameraChange(CameraPosition camera) {

	}
	
	public void closeInfoPopup() {

	}
	
	public void previousMarker() {

	}
	
	public void nextMarker() {

	}
	
	public void showCurrentMarker()
	{
		Marker marker = markers.get(currentMarkerIndex);
		int animDuration = 300;
		marker.showInfoWindow();
		map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()), animDuration, null);
	}
	
	public void showMedia()
	{
		Intent myIntent = new Intent(getApplicationContext(), MediaActivity.class);
		startActivityForResult(myIntent, 0);
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    mapView.onPause();
	}

	@Override
	public void onResume() {
	   super.onResume();
	   mapView.onResume();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    mapView.onDestroy();
	}

	public void onClick(View v) {
		if((Button) v == prev)
		{
			currentMarkerIndex--;
			
			if(currentMarkerIndex < 0) {
				currentMarkerIndex = (markers.size() - 1);
			}
			
			showCurrentMarker();
		}
		else if((Button) v == next)
		{
			currentMarkerIndex++;
			
			if(currentMarkerIndex >= markers.size()) {
				currentMarkerIndex = 0;
			}
			
			showCurrentMarker();
		}
		else if((Button) v == medias)
		{
			showMedia();
		}
		
	}
}
