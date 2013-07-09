package com.lesgrosspoof.bemydiary.models;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.Selection;

import com.lesgrosspoof.bemydiary.entities.Item;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;

public class Selections extends Model {
	
	private static Selections _instance;
	
	private static final String TABLE = "selection";
	
	private Selections(){
		super();
	}
	
	public static Selections getInstance() {
		if (_instance == null)
			_instance = new Selections();
		return _instance;
	}
	
	public int getLieuBySelection(int id_selection) {
		Cursor c = db.query(TABLE, new String[]{"id_liste"}, "_id = " + id_selection, null, null, null, null);
		c.moveToFirst();
		
		int id_liste = c.getInt(0);
		
		System.out.println("getLieuBySelection: " + id_liste);
		
		return id_liste;
	}
	
	public List<ItemSelection> get(int id_board)
	{
		Cursor c = db.query(TABLE, new String[]{"_id", "id_board", "id_liste", "isVisited", "position", "_id_site"}, "id_board = " + id_board, null, null, null, "position");
		c.moveToFirst();
		
		List<ItemSelection> items = new ArrayList<ItemSelection>();
		
		for(int i = 0; i < c.getCount(); i++) {
			
			Item item = Items.getInstance().get(c.getInt(2));
			
			ItemSelection itemSelection = ItemSelection.copyFromItem(item);
			itemSelection.setVisited(Boolean.parseBoolean(Integer.toString(c.getInt(3))));
			itemSelection.setPosition(c.getInt(4));
			itemSelection.setId_selection(c.getInt(0));
			
			itemSelection.setId_site(c.getString(5));

			items.add(itemSelection);
			
			c.moveToNext();
		}
		
		c.close();
		
		return items;
	}
	
	public void add(int id_board, int id_liste) {
		
		ContentValues values = new ContentValues();
		
		values.put("id_board", id_board);
		values.put("id_liste", id_liste);
		values.put("position", getNextPosition(id_board));
		
		db.insert(TABLE, null, values);
	}
	
	public void delete(int id_board, int id_selection) {
		db.execSQL("UPDATE "+ TABLE +" SET position=position-1 WHERE id_board="+ id_board +" AND position>=(SELECT position FROM "+ TABLE +" WHERE  id_board="+ id_board +" AND _id="+ id_selection +")+1");
		db.delete(TABLE, "id_board='"+ id_board +"' AND _id='"+ id_selection +"'", null);
	}
	
	public void up(int id_board, int id_selection) {
		db.execSQL("UPDATE "+ TABLE +" SET position=position+1 WHERE id_board="+ id_board +" AND position=(SELECT position FROM "+ TABLE +" WHERE  id_board="+ id_board +" AND _id="+ id_selection +")-1");
		db.execSQL("UPDATE "+ TABLE +" SET position=position-1 WHERE id_board="+ id_board +" AND _id="+ id_selection);
	}
	
	public void down(int id_board, int id_selection) {
		db.execSQL("UPDATE "+ TABLE +" SET position=position-1 WHERE id_board="+ id_board +" AND position=(SELECT position FROM "+ TABLE +" WHERE  id_board="+ id_board +" AND _id="+ id_selection +")+1");
		db.execSQL("UPDATE "+ TABLE +" SET position=position+1 WHERE id_board="+ id_board +" AND _id="+ id_selection);
	}
	
	public void setSiteId(String _id_site, int id_selection) {
		String query = "UPDATE "+ TABLE +" SET _id_site='"+_id_site+"' WHERE id_liste="+ id_selection;
		//System.out.println("update query : "+query);
		db.execSQL(query);
	}
	
	public int getNextPosition(int id_board) {
		Cursor c = db.rawQuery("SELECT MAX(position) FROM "+ TABLE +" WHERE id_board="+ id_board, null);	
		
		c.moveToFirst();
		
		if (c.getCount() == 0)
			return 1;
		
		return c.getInt(0)+1;
	}
}
