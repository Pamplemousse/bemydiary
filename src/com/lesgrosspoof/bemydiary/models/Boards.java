package com.lesgrosspoof.bemydiary.models;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

import com.lesgrosspoof.bemydiary.entities.Board;

public class Boards extends Model {
	private static Boards _instance;
	
	private static final String TABLE = "board";
	
	private Boards(){
		super();
	}
	
	private Board cursorToBoard(Cursor c) {
		
		if (c.getCount() <= 0) {
			c.close();
			return null;
		}
		
		c.moveToFirst();
		
		Board board = new Board();
		board.setId(c.getInt(0));
		board.setIdSite(c.getString(1));
		board.setPassword(c.getString(2));
		board.setIsPublish(c.getInt(3));
		board.setIsFinish(c.getInt(4));
		board.setDate_create(c.getString(5));
		board.setDate_end(c.getString(6));
		board.refreshSelection();
		
		c.close();
		
		return board;
	}
	
	private List<Board> cursorToBoards(Cursor c) {
		
		c.moveToFirst();
		
		List<Board> boards = new ArrayList<Board>();
		
		for(int i = 0; i < c.getCount(); i++) {
			
			Board board = new Board();
			
			board.setId(c.getInt(0));
			board.setIdSite(c.getString(1));
			board.setPassword(c.getString(2));
			board.setIsPublish(c.getInt(3));
			board.setIsFinish(c.getInt(4));
			board.setDate_create(c.getString(5));
			board.setDate_end(c.getString(6));
			board.refreshSelection();
			
			boards.add(board);
			
			c.moveToNext();
		}
		
		c.close();
		
		return boards;
	}
	
	public Board get(int id) {
		String where = "_id = " + id;
		
		Cursor c = db.query(TABLE, new String[]{"_id", "_id_site", "password", "isPublish", "isFinish", "date_create", "date_end"}, where, null, null, null, null);
		
		return cursorToBoard(c);
	}
	
	public List<Board> get() {
		
		Cursor c = db.query(TABLE, new String[]{"_id", "_id_site", "password", "isPublish", "isFinish", "date_create", "date_end"}, "", null, null, null, null);
		
		return cursorToBoards(c);
	}
	
	public boolean existOpenBoard() {
		
		int resultCount = 0;
		
		try {
			Cursor c = db.query(TABLE, new String[]{"_id"}, "isFinish IS NULL OR isFinish = 0", null, null, null, null);
		
			resultCount = c.getCount();
		
			c.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		if (resultCount <= 0)
			return false;
		
		return true;
	}
	
	public boolean existOldBoard() {
		
		int resultCount = 0;
		
		try {
			Cursor c = db.query(TABLE, new String[]{"_id"}, "isFinish = 1", null, null, null, null);
			resultCount = c.getCount();
			
			c.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		if (resultCount <= 0)
			return false;
		
		return true;
	}
	
	public void create() {
		try {
			db.execSQL("INSERT INTO "+ TABLE +" (_id_site, isPublish, isFinish, date_create, date_end, password) VALUES (NULL, 0, 0, datetime('now'),NULL, NULL)");
			// TODO : utiliser un db.insert pour récupérer l'id autogénéré
			ContentValues values = new ContentValues();
			values.put("_id_site", "NULL");
			int id = (int) db.insert(TABLE, null, values);
			
			
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void update(Board board) {

		try {
			String query = "UPDATE "+ TABLE +" SET _id_site='"+ board.getIdSite() +"', isPublish="+ board.getIsPublish() +", isFinish="+ board.getIsFinish() +", password='"+ board.getPassword() +"' WHERE _id ="+ board.getId();
			db.execSQL(query);
			//System.out.println("query : "+query);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public int getCurrentBoardId() {
		Cursor c = db.query(TABLE, new String[]{"_id"}, "isFinish IS NULL OR isFinish = 0", null, null, null, null);
		
		c.moveToFirst();
		int value = c.getInt(0);
		c.close();
		
		return value;
	}
	
	public void closeCurrentBoard() {
		try {
			db.execSQL("UPDATE "+ TABLE +" SET isFinish=1, date_end=datetime('now') WHERE isFinish IS NULL OR isFinish=0;");
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public List<Board> getOldsBoard() {
		Cursor c = db.query(TABLE, new String[]{"_id", "_id_site", "password", "isPublish", "isFinish", "date_create", "date_end"}, "isFinish = 1", null, null, null, null);
		
		return cursorToBoards(c);
	}
	
	public boolean isFinish(int id_board) {
		
		int result = 0;
		
		try {
			Cursor c = db.query(TABLE, new String[]{"isFinish"}, "_id = " + id_board, null, null, null, null);
			c.moveToFirst();
			result = c.getInt(0);
			
			c.close();
		}
		catch(Exception e) {
			System.out.println("ERROR DATABASE QUERY : " + e.getMessage());
		}
		
		if (result == 1)
			return true;
		
		return false;
	}
	
	public static Boards getInstance() {
		if (_instance == null)
			_instance = new Boards();
		return _instance;
	}
}