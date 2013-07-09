package com.lesgrosspoof.bemydiary.map;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.helpers.Markers;
import com.lesgrosspoof.bemydiary.models.Boards;
import com.lesgrosspoof.bemydiary.models.Items;
import com.lesgrosspoof.bemydiary.views.Infobulle;

public class CustomInfoWindowAdapter implements InfoWindowAdapter, OnClickListener {
	
	private Context context;
	
	private LinearLayout infobulle;
	
	private Button addMedia;
	private Button close;
	private Button goLeft;
	private Button goRight;
	
	public CustomInfoWindowAdapter(Context context) {
		super();
		this.context = context;
	}
	
	public View getInfoContents(Marker marker) {
		return null;
	}
	
	public View getInfoWindow(Marker marker) {
		
		int currentId = Markers.getInstance().get(marker.getId());
		List<ItemSelection> items = Board.getCurrentBoard().getSelection();
		ItemSelection currentItem = null;
		
		for(ItemSelection item : items) {
			if (item.getId() == currentId) {
				currentItem = item;
				break;
			}
		}
		
		if (currentItem == null)
			return null;
		
		Board.setCurrentItemId(currentItem.getId());
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        FrameLayout _infobulle = (FrameLayout) inflater.inflate(R.layout.infobulle, null);
        
        ImageView picto = (ImageView) _infobulle.findViewById(R.id.infobulleIcon);
		picto.setImageDrawable(context.getResources().getDrawable(Items.getInstance().getDrawableIdForType(currentItem.getType())));
		
		TextView title = (TextView) _infobulle.findViewById(R.id.infobulleTitle);
		title.setText(currentItem.getNom());
		title.setTextColor(Color.parseColor("#F99031"));
		
		TextView address = (TextView) _infobulle.findViewById(R.id.infobulleAddress);
		address.setText(currentItem.getCode_postal() + " " +currentItem.getVille());
		
        return _infobulle;
	}

	public void onClick(View arg0) 
	{
		System.out.println("clicked on popup");
		
	}
	
}
