package com.lesgrosspoof.bemydiary.entities;

import com.lesgrosspoof.bemydiary.MyApp;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {
	public Database() {
		super(MyApp.getContext(), "mabase", null, 3);
	}
}
