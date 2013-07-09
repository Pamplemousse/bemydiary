package com.lesgrosspoof.bemydiary.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.lesgrosspoof.bemydiary.entities.Board;
import com.lesgrosspoof.bemydiary.entities.ItemSelection;
import com.lesgrosspoof.bemydiary.entities.Media;

public class Medias extends Model {
	public static int PHOTO = 1;
	public static int VIDEO = 2;
	public static int COMMENT = 3;
	private static Medias _instance;
	
	private static final String TABLE = "media";
	
	private Medias() {
		super();
	}
	
	public void add(int id_board, int id_liste, int type, String content) {
		ContentValues values = new ContentValues();
		
		values.put("id_board", id_board);
		values.put("id_liste", id_liste);
		
		if(type != Medias.COMMENT){
			// To do : Moving Stuffs into our folders
		}
		
		values.put("type", type);
		values.put("content", content);
		
		db.insert(TABLE, null, values);
	}
	
	public void delete(int id_media) {
		
		db.delete(TABLE, "_id='"+ id_media +"'", null);
	}
	
	public List<Media> get(int id_board, int id_liste, int type) {
		
		List<Media> items = new ArrayList<Media>();
		
		String whereType = "";
		
		if(type > 0)
			whereType = " AND type="+type;
		

		try {
			Cursor c = db.query(TABLE, new String[]{"_id", "_id_site", "id_board", "id_liste", "type", "content"}, "id_board=" + id_board +" AND id_liste="+ id_liste + whereType, null, null, null, null);
			
			c.moveToFirst();
			
			for(int i = 0; i < c.getCount(); i++) {
				File file = new File(c.getString(5));
				if (file.exists()) {
					items.add(new Media(c.getInt(0),c.getInt(2),c.getInt(3),c.getInt(4),c.getString(5),c.getString(1)));
					c.moveToNext();
				}
				else{
					System.out.println("File doesn't exist anymore");
					delete(c.getInt(0));
				}
			}
			
			c.close();
		}
		catch(Exception e) {
			System.out.println("ERROR: Medias.java get()");
		}
		
		return items;
	}
	public List<Media> getPhotos(int _id_board, int id_liste){
		return get(_id_board, id_liste, PHOTO);
	}
	public List<Media> getVideos(int _id_board, int id_liste){
		return get(_id_board, id_liste, VIDEO);
	}
	public List<Media> getComments(int _id_board, int id_liste){
		return get(_id_board, id_liste, COMMENT);
	}
	
	public void update(Media media) {

		try {
			String query = "UPDATE "+ TABLE +" SET _id_site='"+ media.getId_site() +"' WHERE _id ="+ media.get_id();
			db.execSQL(query);
			//System.out.println("query : "+query);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	// Thumbnails

	public HashMap<Integer, HashMap<Integer, List<Media>>> getMedias(int _id_board) {
		
		HashMap<Integer, HashMap<Integer, List<Media>>> _return = new HashMap<Integer, HashMap<Integer,List<Media>>>();
		
		List<ItemSelection> selections = Board.getCurrentBoard().getSelection();
		
		for(ItemSelection item : selections) {
			
			HashMap<Integer, List<Media>> temp = new HashMap<Integer, List<Media>>();
			
			temp.put(Medias.PHOTO, getPhotos(_id_board, item.getId()));
			temp.put(Medias.VIDEO, getVideos(_id_board, item.getId()));
			temp.put(Medias.COMMENT, getComments(_id_board, item.getId()));
			
			_return.put(item.getId(), temp);
		}
		
		return _return;
	}
	
	// Thumbnails
	public Bitmap getPhotoThumbnail(Media photo){
		try {
	    	return this.decodeFile(new File(photo.getContent()));
    	}
    	catch(Exception e) { }
		
		return null;
	}
	
	private Bitmap decodeFile(File f) {
		try {
			//Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f),null,o);
			
			//The new size we want to scale to
			final int REQUIRED_SIZE=60;
			
			//Find the correct scale value. It should be the power of 2.
			int width_tmp=o.outWidth, height_tmp=o.outHeight;
			int scale=1;
			
			while(true) {
				if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
					break;
				width_tmp/=2;
				height_tmp/=2;
				scale*=2;
			}
			
			//Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize=scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		}
		catch (FileNotFoundException e) {}
		
		return null;
	}
	
	public static Medias getInstance() {
		if (_instance == null)
			_instance = new Medias();
		return _instance;
	}
}
