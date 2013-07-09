package com.lesgrosspoof.bemydiary.models;

import android.content.ContentValues;
import android.database.Cursor;

import com.lesgrosspoof.bemydiary.entities.User;

public class Users extends Model {

	private static Users _instance;
	
	private static final String TABLE = "user";
	
	private Users() {
		super();
	}
	
	public void add(String email, String uid, String provider) {
		ContentValues values = new ContentValues();
		
		values.put("email", email);
		values.put("uid", uid);
		values.put("provider", provider);
		
		db.insert(TABLE, null, values);
	}
	
	private User cursorToUser(Cursor c) {
		
		if (c.getCount() <= 0) {
			c.close();
			return null;
		}
		
		c.moveToFirst();
		
		User user = new User();
		user.setId(c.getInt(0));
		user.setEmail(c.getString(1));
		user.setProvider(c.getString(2));
		user.setUid(c.getString(3));
		
		c.close();
		
		return user;
	}
	
	public User get()
	{
		Cursor c = db.query(TABLE, new String[]{"_id", "email", "provider", "uid"}, "", null, null, null, null);
		return cursorToUser(c);
	}
	
	public static Users getInstance() {
		if (_instance == null)
			_instance = new Users();
		return _instance;
	}
}
