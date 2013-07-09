package com.lesgrosspoof.bemydiary;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.Item;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.entities.SearchLayout;
import com.lesgrosspoof.bemydiary.models.Boards;
import com.lesgrosspoof.bemydiary.models.Items;
import com.lesgrosspoof.bemydiary.views.CurrentListRow;

public class CurrentListActivity extends AbstractActivity implements OnFocusChangeListener {
	private TextView searchBox;
	private Button toTheMap;
	private SearchLayout searchLayout;
	private List<ItemSelection> currentList;
	private LinearLayout liste;
	
	private boolean searchBoxIsEmpty;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_current_list);
        searchBox = (TextView)findViewById(R.id.searchBox);
        searchLayout = (SearchLayout)findViewById(R.id.searchLayout);
        searchLayout.init(this);
        currentList = Board.getCurrentBoard().getSelection();
        liste = (LinearLayout)findViewById(R.id.liste);
        
        searchBoxIsEmpty = true;
        
        searchBox.setOnFocusChangeListener(this);
        searchBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	
            	if(searchBox.getText().length() > 0)
            	{
            		searchBoxIsEmpty = false;
            	}

        		if(searchBox.getText().length() >= 3 && searchLayout.selected > 0){
            		searchLayout.fillIt(Items.getInstance().search(searchLayout.selected, searchBox.getText().toString()));
        		}
            }
           
        });
        searchBox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            	
            	if(keyCode == 67) // backspace
            	{
            		if(searchBoxIsEmpty)
            		{
            			showFilterMenu();
            		}
            		
            	}
            	
            	if(searchBox.getText().length() <= 0)
        		{
        			searchBoxIsEmpty = true;
        		}
            	else
            	{
            		searchBoxIsEmpty = false;
            	}
            	
                return false;
            }

        });
        
        toTheMap = (Button)findViewById(R.id.toTheMap);
        toTheMap.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(getBaseContext(), CarteActivity.class);
				startActivityForResult(myIntent, 0);
			}
		});
        
        searchBox.clearFocus();
        
        fillListe();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	if (Boards.getInstance().isFinish(Board.currentIndexBoard) == true) { 
    		Intent myIntent = new Intent(getBaseContext(), MainMenuActivity.class);
			startActivityForResult(myIntent, 0);
    	}
    	else {
	    	currentList = Board.getCurrentBoard().getSelection();
	    	fillListe();
    	}
    }
    
    public void fillListe(){
    	liste.removeAllViews();
    	
    	for(ItemSelection lieu : currentList){
    		liste.addView(CurrentListRow.createFromItem(this, lieu));
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_current_list, menu);
        return true;
    }
    
    public void addItem(Item _lieu){
    	Board.getCurrentBoard().add(_lieu.getId());
    	Toast.makeText(this, "'" + _lieu.getNom() + "' a été ajouté à la liste", 80).show();
    	currentList = Board.getCurrentBoard().getSelection();
    	fillListe();
    }
    
    public void removeItem(int id_selection) {
    	for(ItemSelection lieu : currentList) {
    		if(lieu.getId_selection() == id_selection){
    			currentList.remove(lieu);
    			break;
    		}
    	}
    	
    	Board.getCurrentBoard().remove(id_selection);
    	fillListe();
    }
    
    public boolean isInList(Item item){
    	for(ItemSelection lieu : currentList) {
    		if(lieu.getId() == item.getId()){
    			return true;
    		}
    	}
    	return false;
    }
    
    public void upItem(int id_selection) {
    	for(ItemSelection lieu : currentList) {
    		if(lieu.getId_selection() == id_selection){
    			int index = currentList.indexOf(lieu);
    			if(index > 0){
	    			currentList.remove(lieu);
	    			currentList.add(index - 1, lieu);
	    			Board.getCurrentBoard().up(id_selection);
    			}
    			break;
    		}
    	}
    	
    	fillListe();
    }
    
    public void downItem(int id_selection) {
    	for(ItemSelection lieu : currentList) {
    		if(lieu.getId_selection() == id_selection){
    			int index = currentList.indexOf(lieu);
    			if(index < currentList.size() - 1){
	    			currentList.remove(lieu);
	    			currentList.add(index + 1, lieu);
	    			Board.getCurrentBoard().down(id_selection);
    			}
    			break;
    		}
    	}
    	
    	fillListe();
    }

	public void onFocusChange(View v, boolean hasFocus) {
		
		if(hasFocus){
			if(searchLayout.selected == 0) // pas de filtre choisi
			{
				searchLayout.open(this);
				searchBox.setEnabled(false);
			}
			else
			{
				searchLayout.open(this);
				this.enableSearchBox();
			}
		}
		else{
			searchLayout.close();
			InputMethodManager imm = (InputMethodManager)getSystemService(
				      Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
		}
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
			searchLayout.close();
			searchBox.clearFocus();
	    }
	}
	
	public List<ItemSelection> getCurrentList() {
		return currentList;
	}
	
	public void showFilterMenu()
	{
		disableSearchBox();
		searchLayout.open(this);
	}
	
	public void enableSearchBox()
	{
		searchBox.setEnabled(true);
		
		searchBox.setHint("");
		
		TextView filter = (TextView) findViewById(R.id.filter);
		
		float density = getResources().getDisplayMetrics().densityDpi / 160f;

		if(searchLayout.selected == Items.$TYPE_ID_MONUMENT)
		{
			filter.setText("Musées : ");
			searchBox.setPadding((int) (88 * density), 0, (int)(55 * density), 0);
		}
		else if(searchLayout.selected == Items.$TYPE_ID_PARC)
		{
			filter.setText("Parcs : ");
			searchBox.setPadding((int) (70 * density), 0, (int)(55 * density), 0);
		}
		else if(searchLayout.selected == Items.$TYPE_ID_VIN)
		{
			filter.setText("Vin : ");
			searchBox.setPadding((int) (50 * density), 0, (int)(55 * density), 0);
		}
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
        
        ((ViewGroup) searchLayout.findViewById(R.id.searchList)).removeAllViews();
        
		searchBox.requestFocus();
	}
	
	public void disableSearchBox()
	{
		TextView filter = (TextView) findViewById(R.id.filter);
		filter.setText("");
		
		searchBox.setHint("Choisir un filtre...");
		searchBox.setEnabled(false);
		searchBox.setPadding(15, 0, 55, 0);
	}
	
	@Override
	public void onBackPressed()
	{
		if(searchLayout.isOpen())
		{
			searchLayout.close();
			searchLayout.selected = 0;
			
			searchBox.clearFocus();
			
			TextView filter = (TextView) findViewById(R.id.filter);
			filter.setText("");
			
			searchBox.setHint("Choisir un filtre...");
			searchBox.setPadding(15, 0, 55, 0);
			
			searchBox.setEnabled(true);
		}
		else
		{
			super.onBackPressed();
		}
	}
	
	public TextView getSearchBox() {
		return searchBox;
	}
}
