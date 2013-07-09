package com.lesgrosspoof.bemydiary.models;

import android.database.sqlite.SQLiteDatabase;

import com.lesgrosspoof.bemydiary.entities.MyBase;

public class Model {
	
	protected static SQLiteDatabase db;
	
	public Model() {
		db = MyBase.getInstance().getDb();
	}
}
