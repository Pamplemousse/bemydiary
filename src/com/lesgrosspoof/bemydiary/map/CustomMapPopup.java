package com.lesgrosspoof.bemydiary.map;

import java.util.List;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;
import com.lesgrosspoof.bemydiary.CarteActivity;
import com.lesgrosspoof.bemydiary.MediaActivity;
import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.helpers.Markers;
import com.lesgrosspoof.bemydiary.models.Items;

public class CustomMapPopup extends FrameLayout implements OnClickListener
{
	private final Context context;
	
	private LinearLayout infobulle;
	
	private Button addMedia;
	private Button close;
	private Button goLeft;
	private Button goRight;
	
	public CustomMapPopup(final Context context, Marker marker) 
	{
		super(context);
		this.context = context;

		int currentId = Markers.getInstance().get(marker.getId());
		List<ItemSelection> items = Board.getCurrentBoard().getSelection();
		ItemSelection currentItem = null;
		
		for(ItemSelection item : items) {
			if (item.getId() == currentId) {
				currentItem = item;
				break;
			}
		}
		
		if (currentItem != null)
		{
			Board.setCurrentItemId(currentItem.getId());
			
			infobulle = new LinearLayout(context);
			infobulle.setLayoutParams(new LinearLayout.LayoutParams(560,400));
			
			LinearLayout row = new LinearLayout(context);
			row.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 120));
			row.setOrientation(LinearLayout.HORIZONTAL);
			
		    Drawable image = context.getResources().getDrawable(R.drawable.infobulle);
			infobulle.setBackgroundDrawable(image);
			infobulle.setOrientation(LinearLayout.VERTICAL);
			infobulle.setPadding(25, 8, 15, 0);
			infobulle.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CarteActivity activity = (CarteActivity) context;
					activity.closeInfoPopup();
				}
	        });
			
			ImageView picto = new ImageView(context);
			picto.setImageDrawable(context.getResources().getDrawable(Items.getInstance().getDrawableIdForType(currentItem.getType())));
			picto.setPadding(0, 20, 10, 0);
			row.addView(picto);
			
			TextView title = new TextView(context);
			title.setLayoutParams(new LayoutParams(340, 120));
			title.setGravity(Gravity.CENTER);
			title.setText(currentItem.getNom());
			title.setTextSize(20);
			title.setTextColor(Color.WHITE);
			title.setLines(2);
			title.setMaxLines(2);
			title.setEllipsize(TextUtils.TruncateAt.END);
			title.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT));
			row.addView(title);
			
			close = new Button(context);
			close.setLayoutParams(new LayoutParams(40, 40));
			close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.close));
			close.setPadding(50, 0, 0, 0);
			close.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CarteActivity activity = (CarteActivity) context;
					activity.closeInfoPopup();
				}
	        });
			
			infobulle.addView(row);
			
			TextView address = new TextView(context);
			address.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 90));
			address.setText(currentItem.getAdresse() + " " + currentItem.getCode_postal() + " " + currentItem.getVille());
			address.setEllipsize(TextUtils.TruncateAt.END);
			address.setPadding(0, 15, 0, 0);
			address.setTextColor(Color.WHITE);
			address.setLines(3);
			address.setMaxLines(3);
			address.setGravity(Gravity.CENTER);
			infobulle.addView(address);
			
			LinearLayout row2 = new LinearLayout(context);
			row2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			row2.setPadding(0, 0, 0, 50);
			row2.setOrientation(LinearLayout.HORIZONTAL);
			row2.setGravity(Gravity.CENTER);
			
			addMedia = new Button(context);
			addMedia.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CarteActivity activity = (CarteActivity) context;
					activity.showMedia();
				}
	        });
			addMedia.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bt_media));
			addMedia.setLayoutParams(new LayoutParams(258, 74));
			row2.addView(addMedia);
			
			goLeft = new Button(context);
			goLeft.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bt_previous));
			goLeft.setLayoutParams(new LayoutParams(64, 64));
			goLeft.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CarteActivity activity = (CarteActivity) context;
					activity.previousMarker();
				}
	        });
			
			row2.addView(goLeft);
			
			goRight = new Button(context);
			goRight.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bt_next));
			goRight.setLayoutParams(new LayoutParams(64, 64));
			goRight.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					CarteActivity activity = (CarteActivity) context;
					activity.nextMarker();
				}
	        });
			
			row2.addView(goRight);
			
			infobulle.addView(row2);
			
			this.addView(infobulle);
		}
	}

	public void onClick(View v)
	{
		System.out.println("clicked on popup");
		
	}

}
