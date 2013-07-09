package com.lesgrosspoof.bemydiary.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lesgrosspoof.bemydiary.CurrentListActivity;
import com.lesgrosspoof.bemydiary.MediaActivity;
import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.models.Items;

public class CurrentListRow {
	
	public static View createFromItem(final Context context,ItemSelection item)
	{
		final CurrentListActivity activity = (CurrentListActivity) context;
		
		View row = (LinearLayout) View.inflate(context, R.layout.currentlistrow, null);
		
		// Picto
		
		ImageView picto = (ImageView)row.findViewById(R.id.picto);
		
		if(item.getType() == Items.$TYPE_ID_MONUMENT)
			picto.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_musees));
		else if(item.getType() == Items.$TYPE_ID_PARC)
			picto.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_parcs));
		else if (item.getType() == Items.$TYPE_ID_VIN)
			picto.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_vins));
		
		// Infos texte
        
        TextView title = (TextView) row.findViewById(R.id.itemTitle);
        title.setText(item.getNom());
        if(item.getNom().length() < 1)
        	title.setVisibility(View.GONE);
        
        TextView type = (TextView) row.findViewById(R.id.itemCategory);
        type.setText(Items.getTypeName(item.getType()));
        if(Items.getTypeName(item.getType()).length() < 1)
        	type.setVisibility(View.GONE);
        
        TextView address = (TextView) row.findViewById(R.id.itemAddress);
        address.setText(item.getAdresse());
        if(item.getAdresse().length() < 1)
        	address.setVisibility(View.GONE);

		
        // Bouton média
        
        final int id_lieu = item.getId();
        
        Button ajoutMedia = (Button) row.findViewById(R.id.mediaButton);
        
        ajoutMedia.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Board.setCurrentItemId(id_lieu);
				Intent myIntent = new Intent(context.getApplicationContext(), MediaActivity.class);
				activity.startActivityForResult(myIntent, 0);
			}
        });
        
        row.setContentDescription(String.format("%d", item.getId_selection()));
        
        row.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(final View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("Actions");
				builder.setItems(new String[] {"Monter","Médias", "Supprimer","Descendre"}, new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						int id_selection = Integer.parseInt((String)v.getContentDescription());
						
						CurrentListActivity activity = (CurrentListActivity) context;
						
						switch(which) {
							case 0:
								activity.upItem(id_selection);
							break;
							case 1:
								Board.setCurrentItemId(id_lieu);
								Intent myIntent = new Intent(activity.getBaseContext(), MediaActivity.class);
								myIntent.putExtra("id_lieu", id_lieu);
								activity.startActivityForResult(myIntent, 0);
							break;
							case 2:
								activity.removeItem(id_selection);
								break;
							case 3:
								activity.downItem(id_selection);
							break;
						}
					}

				});
				
				AlertDialog alert = builder.create();
				alert.show();
				
				return true;
		    }
		});

        title.setTextColor(Color.parseColor("#362F2D"));
        type.setTextColor(Color.parseColor("#362F2D"));
        address.setTextColor(Color.parseColor("#362F2D"));

	    return row;
	}

}
