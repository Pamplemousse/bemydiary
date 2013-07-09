package com.lesgrosspoof.bemydiary.models;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.lesgrosspoof.bemydiary.R;
import com.lesgrosspoof.bemydiary.entities.Item;

public class Items extends Model {
	
	private static Items _instance;
	
	private static final String TABLE = "liste";
	
	public static int $TYPE_ID_MONUMENT = 1;
	public static int $TYPE_ID_PARC = 2;
	public static int $TYPE_ID_VIN = 3;
	
	private Items(){
		super();
	}
	
	private Item cursorToItem(Cursor c) {
		
		c.moveToFirst();
		
		Item item = new Item();
		item.setId(c.getInt(0));
		item.setType(c.getInt(1));
		item.setNom(c.getString(2));
		item.setAdresse(c.getString(3));
		item.setCode_postal(c.getString(4));
		item.setVille(c.getString(5));
		item.setTelephone(c.getString(6));
		item.setSite_web(c.getString(7));
		item.setLatitude(Double.parseDouble(c.getString(8)));
		item.setLongitude(Double.parseDouble(c.getString(9)));
		item.setHandicap(c.getString(10));
		item.setDetails(c.getString(11));
		
		c.close();
		
		return item;
	}
	
	private List<Item> cursorToItems(Cursor c) {
		c.moveToFirst();
		
		List<Item> items = new ArrayList<Item>();
		
		for(int i = 0; i < c.getCount(); i++) {
			
			Item item = new Item();
			item.setId(c.getInt(0));
			item.setType(c.getInt(1));
			item.setNom(c.getString(2));
			item.setAdresse(c.getString(3));
			item.setCode_postal(c.getString(4));
			item.setVille(c.getString(5));
			item.setTelephone(c.getString(6));
			item.setSite_web(c.getString(7));
			item.setLatitude(Double.parseDouble(c.getString(8)));
			item.setLongitude(Double.parseDouble(c.getString(9)));
			item.setHandicap(c.getString(10));
			item.setDetails(c.getString(11));
			
			items.add(item);
			
			c.moveToNext();
		}
		
		c.close();
		
		return items;
	}
	
	public Item get(int id) {

		String where = "_id = '" + id +"'";
		
		Cursor c = db.query(TABLE, new String[]{"_id", "type", "nom", "adresse", "code_postal", "ville", "telephone", "site_web", "longitude", "latitude", "handicap", "details"}, where, null, null, null, null);
		
		return cursorToItem(c);
	}
	
	public List<Item> search(int type_id, String query){
		
		query = query.replace('\'', ' ');
		query = query.replace('"', ' ');
		query = query.toLowerCase();
		query = Normalizer.normalize(query, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); // Supprime les accents
		
		String[] keywords = query.split(" ");
		
		String where = "";
		
		/**
		 * Type
		 */
		where += "type = '"+ type_id +"' AND (";
		
		
		/**
		 * Search text
		 */
		for(String word : keywords) {
			where += " search LIKE ";
			where += "'%"+ word +"%' AND ";
		}
		
		where += "1";
		
		where += " OR search LIKE '%";
		for(String word : keywords) {
			where += " "+ word;
		}
		
		where += "%')";
		
		Cursor c = db.query(TABLE, new String[]{"_id", "type", "nom", "adresse", "code_postal", "ville", "telephone", "site_web", "longitude", "latitude", "handicap", "details"}, where, null, null, null, null);
		
		return cursorToItems(c);
	}
	
	public List<Item> searchById(List<Integer> ids) {
		
		String where = "";
		for(int id : ids) {
			where += " _id = '"+ id +"' OR ";
		}
		where += "1=0";
		
		Cursor c = db.query(TABLE, new String[]{"_id", "type", "nom", "adresse", "code_postal", "ville", "telephone", "site_web", "longitude", "latitude", "handicap", "details"}, where, null, null, null, null);
		
		return cursorToItems(c);
	}
	
	public static int getDrawableIdForType(int type) {

		int pictoId = 0;
		
		if(type == Items.$TYPE_ID_MONUMENT)
		{
			pictoId = R.drawable.ic_musees;
		}
		else if(type == Items.$TYPE_ID_PARC)
		{
			pictoId = R.drawable.ic_parcs;
		}
		else if(type == Items.$TYPE_ID_VIN)
		{
			pictoId = R.drawable.ic_vins;
		}

		return pictoId;
	}
	
	public static String getTypeName(int type) {

		String name = "";
		
		if(type == Items.$TYPE_ID_MONUMENT)
		{
			name = "Musée";
		}
		else if(type == Items.$TYPE_ID_PARC)
		{
			name = "Jardin";
		}
		else if(type == Items.$TYPE_ID_VIN)
		{
			name = "Vin";
		}
		
		return name;
	}
	
	public static Items getInstance() {
		if (_instance == null)
			_instance = new Items();
		return _instance;
	}
}
