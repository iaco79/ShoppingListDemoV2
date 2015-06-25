package com.iaco.testapp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public interface AfterCreateHandler {
		void AfterCreate(SQLiteDatabase db);
	}

	public static final String TABLE_ITEM_CATALOG = "item";
	public static final String COLUMN_ITEMID = "itemid";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_IMAGENAME = "imagename";
	public static final String COLUMN_STATUS = "status";
	
	private static final String DATABASE_NAME = "grosery.db";
	private static final int DATABASE_VERSION = 1;

	private static java.lang.Class<? extends DBHelper.AfterCreateHandler> afterHandlerClass;

	// table creation statement
	private static final String DATABASE_CREATE = 
			  "CREATE TABLE "
			+ TABLE_ITEM_CATALOG 
			+ "(" 
			+ COLUMN_ITEMID + " INTEGER PRIMARY KEY   AUTOINCREMENT," 
			+ COLUMN_NAME   + " TEXT NOT NULL," 
			+ COLUMN_DESCRIPTION + " TEXT NOT NULL," 
			+ COLUMN_IMAGENAME + " TEXT NOT NULL, "
			+ COLUMN_STATUS + " INTEGER NOT NULL "
			+ ");";

	public DBHelper(Context context,
			java.lang.Class<? extends DBHelper.AfterCreateHandler> handlerClass) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		afterHandlerClass = handlerClass;

	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		
		boolean ok = true;

		try {
			database.execSQL(DATABASE_CREATE);
			
			
		} catch (Exception x) {
			ok = false;
			Log.w(DBHelper.class.getName(), "Error initializing db");
		}
		
		if (ok == true && afterHandlerClass != null) {
			try {
				AfterCreateHandler handler = afterHandlerClass.newInstance();
				handler.AfterCreate(database);
			} catch (Exception x) {
				Log.w(DBHelper.class.getName(), "Error initializing data");
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DBHelper.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM_CATALOG);
		onCreate(db);
	}

}
