package com.lesgrosspoof.bemydiary.entities;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lesgrosspoof.bemydiary.CurrentListActivity;
import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.models.Items;
import com.lesgrosspoof.bemydiary.views.SearchListRow;

public class SearchLayout extends LinearLayout implements OnClickListener{
    private TableLayout table;
    private Context context;
    private EditText searchBox;
    private int selectedId = 0;
    private CurrentListActivity parent;
	private Drawable theLine;
	private Drawable musee;
	private Drawable jardin;
	private Drawable vin;
	
	private boolean open;
    
    private String[] buttons = {"musee","jardin","vin"};
    
    public int selected = 0;
    
    public SearchLayout(Context context) {
        super(context);
        init(context);
    }

    public SearchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
    	
        this.context = context;
        
        open = false;

        theLine = context.getResources().getDrawable(R.drawable.sep);
        musee = context.getResources().getDrawable(R.drawable.ic_musees);
        jardin = context.getResources().getDrawable(R.drawable.ic_parcs);
        vin = context.getResources().getDrawable(R.drawable.ic_vins);
        vin = context.getResources().getDrawable(R.drawable.ic_vins);
        
        searchBox = (EditText)findViewById(R.id.searchBox);
        table = (TableLayout)findViewById(R.id.searchList);
        setVisibility(INVISIBLE);
    }

    public void open(CurrentListActivity _parent){
        parent = _parent;
        open = true;
        if(table == null)
            table = (TableLayout)findViewById(R.id.searchList);
        setVisibility(VISIBLE);
        fillIt(null);
    }
    
    public void fillIt(List<Item> liste){
        if(table == null)
            table = (TableLayout)findViewById(R.id.searchList);
        table.removeAllViews();
        
        int i = 0;
        
        if(liste == null){
            for(String button : buttons){
            	i++;
            	
                TableRow tr = new TableRow(context);
                tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

    	        LinearLayout row = new LinearLayout(context);
    	        row.setOrientation(LinearLayout.HORIZONTAL);
    	        row.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    	        
    	        RelativeLayout left = new RelativeLayout(context);
    	        row.addView(left);
    	        RelativeLayout right = new RelativeLayout(context);
    	        right.setGravity(Gravity.CENTER);
    	        row.addView(right);
    	        
    	        tr.addView(row);
    	        tr.setClickable(true);
    	        
    	        boolean background = false;
    	        
    	        ImageView picto = new ImageView(context);
				if(button == "musee"){
					picto.setImageDrawable(musee);
					if(selected == Items.$TYPE_ID_MONUMENT)
						background = true;
				}
				else if(button == "jardin"){
					picto.setImageDrawable(jardin);
					if(selected == Items.$TYPE_ID_PARC)
						background = true;
				}
				else if(button == "vin"){
					picto.setImageDrawable(vin);
					if(selected == Items.$TYPE_ID_VIN)
						background = true;
				}
				
				picto.setPadding(20, 20, 20, 20);
				picto.setLayoutParams(new LayoutParams(100, 100));
				
    			left.addView(picto);

                TextView b = new TextView(context);
                b.setText(button.toUpperCase());
                b.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
                b.setTextSize(20f);
                b.setPadding(20, 20, 20, 20);
                b.setTextColor(Color.rgb(54, 47, 45));
                b.setShadowLayer(2, 0, 2, Color.WHITE);
                right.addView(b);
				
                if(background)
                	tr.setBackgroundColor(Color.argb(10, 0, 0, 0));
                
                table.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                

				ImageView line = new ImageView(context);
				line.setImageDrawable(theLine);
				table.addView(line);
				
                tr.setOnClickListener(this);
                tr.setContentDescription("cat-"+Integer.toString(i));
            }
        }
        else
        	for(Item lieu : liste){
            	table.addView(SearchListRow.createFromItem(context, lieu, this));
    		}
    }
    
    public void close(){
    	CurrentListActivity activity = (CurrentListActivity) context;
    	activity.getSearchBox().setText("");
    	open = false;
        setVisibility(GONE);
    }
    
    public void onClick(View v) {
    	
    	String value = (String)v.getContentDescription();
    	
    	if (value.contains("cat-")) {
    		value = value.replace("cat-", "");
    		selected = Integer.parseInt(value);
    		fillIt(null);

            CurrentListActivity activity = (CurrentListActivity) context;
            activity.enableSearchBox();
    	}
    }
    
    public void addItem(Item item){
		parent.addItem(Items.getInstance().get(item.getId()));
    }
    public boolean isInList(Item item){
    	return parent.isInList(item);
    }
    
    public int getSelectedId(){
        return selectedId;
    }
    
	public boolean isOpen() {
		return open;
	}   
}