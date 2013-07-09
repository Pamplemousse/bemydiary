package com.lesgrosspoof.bemydiary.entities;

import android.database.sqlite.SQLiteDatabase;

public class MyBase {
	
	private static MyBase _instance;
	
	private static Database myBase;
	private static SQLiteDatabase db;
	
	private MyBase() {
		myBase = new Database();
		db = myBase.getReadableDatabase();
		
		_instance = this;
	}
	
	public SQLiteDatabase getDb() {
		return db;
	}
	
	public static MyBase getInstance() {
		if(_instance == null){
			_instance = new MyBase();
		}
		return _instance;
	}
}
