package com.lesgrosspoof.bemydiary.map;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.lesgrosspoof.bemydiary.CarteActivity;
import com.lesgrosspoof.bemydiary.MediaActivity;
import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.models.Items;

public class ListItemizedOverlay extends ItemizedOverlay<OverlayItem> implements OnClickListener {
	
	public ArrayList<OverlayItem> arrayListOverlayItem = new ArrayList<OverlayItem>();
	
	private Context context;
	private MapView mapView;
	
	private LinearLayout infobulle;
	
	private Button addMedia;
	private Button close;
	private Button goLeft;
	private Button goRight;
	
	private int currentIndex;

	public ListItemizedOverlay(Drawable defaultMarker, Context context, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
		this.mapView = mapView;
		currentIndex = 0;
	}

	@Override
	protected OverlayItem createItem(int i) {
		return arrayListOverlayItem.get(i);
	}

	@Override
	public int size() {
		return arrayListOverlayItem.size();
	}
	
	public void addOverlayItem(OverlayItem overlay){
	 arrayListOverlayItem.add(overlay);
	 populate();
	}
	
	public void previous()
	{
		if(currentIndex > 0)
		{
			onTap(currentIndex - 1);
		}
	}
	
	public void next()
	{
		if(currentIndex < (arrayListOverlayItem.size()-1))
		{
			onTap(currentIndex + 1);
		}
	}
	
	@Override
	protected boolean onTap(int index)
	{
		currentIndex = index;
		
		ItemMarker marker = (ItemMarker) arrayListOverlayItem.get(index);
		
		if(infobulle != null)
		{
			mapView.removeView(infobulle);
			
			//TextView itemTitle = (TextView) infobulle.findViewById(R.id.itemTitle);
			
			infobulle = null;
	
			//if(itemTitle.getText() != marker.getItem().getNom())
			//{
				displayMarkerDetails(marker);
			//}
		}	
		else
		{
			displayMarkerDetails(marker);
		}

		return true;
	}
	
	public void displayMarkerDetails(ItemMarker marker)
	{	
		Board.setCurrentItemId(marker.getItem().getId());
		
		MapController myMapController = mapView.getController();
		//GeoPoint target = new GeoPoint(marker.getPoint().getLatitudeE6() - 100000, marker.getPoint().getLongitudeE6());
		myMapController.animateTo(marker.getPoint());
		
		infobulle = new LinearLayout(context);
		
		infobulle.setLayoutParams(new LinearLayout.LayoutParams(560,400));
		
		LinearLayout row = new LinearLayout(context);
		row.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 120));
		row.setOrientation(LinearLayout.HORIZONTAL);

	    Drawable image = context.getResources().getDrawable(R.drawable.popup_carte);
		infobulle.setBackgroundDrawable(image);
		infobulle.setOrientation(LinearLayout.VERTICAL);
		infobulle.setPadding(25, 25, 15, 0);
		infobulle.setOnClickListener(this);
		
		ImageView picto = new ImageView(context);
		picto.setImageDrawable(context.getResources().getDrawable(Items.getInstance().getDrawableIdForType(marker.getItem().getType())));
		picto.setPadding(0, 0, 10, 0);
		row.addView(picto);
		
		TextView title = new TextView(context);
		title.setLayoutParams(new LayoutParams(360, 120));
		title.setGravity(Gravity.CENTER);
		title.setText(marker.getItem().getNom());
		title.setTextSize(20);
		title.setTextColor(Color.BLACK);
		row.addView(title);
		
		close = new Button(context);
		close.setLayoutParams(new LayoutParams(40, 40));
		close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.close));
		close.setPadding(50, 0, 0, 0);
		close.setOnClickListener(this);
		
		row.addView(close);
		
		infobulle.addView(row);
		
		TextView address = new TextView(context);
		address.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 90));
		address.setText(marker.getItem().getAdresse() + " " + marker.getItem().getCode_postal() + " " + marker.getItem().getVille());
		address.setEllipsize(TextUtils.TruncateAt.END);
		address.setPadding(35, 15, 0, 0);
		address.setTextColor(Color.BLACK);
		infobulle.addView(address);
		
		LinearLayout row2 = new LinearLayout(context);
		row2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 160));
		row2.setPadding(0, 0, 0, 50);
		row2.setOrientation(LinearLayout.HORIZONTAL);
		row2.setGravity(Gravity.CENTER);
		
		addMedia = new Button(context);
		addMedia.setOnClickListener(this);
		addMedia.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.voir_media));
		addMedia.setLayoutParams(new LayoutParams((int)(148*1.25), (int)(45*1.25)));
		row2.addView(addMedia);

		goLeft = new Button(context);
		goLeft.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.app_fleche_g));
		goLeft.setLayoutParams(new LayoutParams(70, 70));
		goLeft.setOnClickListener(this);
		
		if(currentIndex == 0)
		{
			goLeft.setEnabled(false);
		}
		
		row2.addView(goLeft);
		
		goRight = new Button(context);
		goRight.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.app_fleche_d));
		goRight.setLayoutParams(new LayoutParams(70, 70));
		goRight.setOnClickListener(this);
		
		if(currentIndex == (arrayListOverlayItem.size() - 1))
		{
			goRight.setEnabled(false);
		}
		
		row2.addView(goRight);
		
		//row2.addView(row3);
		
		infobulle.addView(row2);		
		

		MapView.LayoutParams mapParams = new MapView.LayoutParams(560, 
                400,
                marker.getPoint(),
                -70,
                -70,
                MapView.LayoutParams.BOTTOM_CENTER);
		
		mapView.addView(infobulle, mapParams);
	}

	public void onClick(View v) {
		if(v == addMedia)
		{
			Intent myIntent = new Intent(context, MediaActivity.class);
			CarteActivity activity = (CarteActivity) context;
			activity.startActivityForResult(myIntent, 0);
			
			
		}
		else if(v == close)
		{
			mapView.removeView(infobulle);
			infobulle = null;
		}
		else if(v == goLeft)
		{
			previous();
		}
		else if(v == goRight)
		{
			next();
		}
		else
		{
			mapView.removeView(infobulle);
			infobulle = null;
		}
		
	}

}
