package com.iaco.testapp.dao;

import java.util.ArrayList;
import java.util.List;

import com.iaco.testapp.dto.BaseDto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public abstract class BaseDao<T extends BaseDto> implements IBaseDao<T> {

	protected static SQLiteOpenHelper sqlHelper = null;
	protected static Context dbcontext;



	static void getDB() {
		if (dbcontext != null) {
			sqlHelper = new DBHelper(
					dbcontext,
					InitializeCatalog.class);

		}
	}
	
	protected void opendb()
	{
		getDB();
	}
	
	
	protected void closedb()
	{
		sqlHelper.close();
	}
	
	
	public BaseDao(Context context) {
		if (dbcontext == null) {
			dbcontext = context;
		}
	}

	public int getNextId()
	{

		opendb();

		String tableName = getTableName();
		String[] columns = getColumns();
		String maxQuery = "MAX(" + columns[0] + ")";

		String[] idColumns = new String [] { maxQuery};
		int max=0;

		try
		{

			SQLiteDatabase db = sqlHelper.getReadableDatabase();


			Cursor cursor = db.query(
					tableName,
					idColumns,
					null,
					null,
					null,
					null,
					null);

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {

				 max = cursor.getInt(0);

				cursor.moveToNext();
			}
			cursor.close();
		}
		catch(Exception ex)
		{
			Log.w(BaseDao.class.getName(), "Error get all: " + ex.getMessage());

		}
		return max+1;
	}

	public int insert(T value)
	{
		try
		{
			int nextId = getNextId();

			opendb();
			SQLiteDatabase db = sqlHelper.getWritableDatabase();
			String[] columns = getColumns();

			ContentValues values = getInsertValues(value);


			values.put(columns[0], nextId);


			long i = db.insert(
					getTableName(),
					null,
					values);

			if(i>0)
			{
				Log.i(BaseDao.class.getName(), "Row inserted successfully");
			}
			else
			{
				return -1;

			}
			value.setDirty(false);

		}
		catch(Exception ex)
		{
			Log.w(BaseDao.class.getName(), "Error update: " + ex.getMessage());
			return -1;

		}

		return 0;


	}

	public int update(T value) {
		
		if(value.isDirty())
		{
			
			try
			{
				opendb();
				SQLiteDatabase db = sqlHelper.getWritableDatabase();
				
				
				long i = db.update(
						getTableName(), 
						getUpdateValues(value),
						getIdQuery(),
						getIdValues(value));
				
				if(i>0)
				{
					Log.i(BaseDao.class.getName(), "Row updated successfully");
				}
			
				value.setDirty(false);
				
			}
			catch(Exception ex)
			{
				Log.w(BaseDao.class.getName(), "Error update: " + ex.getMessage());
				return -1;

			}
		
		}
		return 0;
	}

	public int delete(T value) {


		try
		{
			opendb();
			SQLiteDatabase db = sqlHelper.getWritableDatabase();


			long i = db.delete(
					getTableName(),
					getIdQuery(),
					getIdValues(value));

			if(i>0)
			{
				Log.i(BaseDao.class.getName(), "Row delete successfully");
			}



		}
		catch(Exception ex)
		{
			Log.w(BaseDao.class.getName(), "Error update: " + ex.getMessage());
			return -1;

		}


		return 0;
	}

	public int get(T value) {
		return 0;
	}
	
	public List<T> getList() {
		
		opendb();
		List<T> list = new ArrayList<T>();
		  
		String tablename = getTableName();
		String[] columns = getColumns();
		
		try
		{
		
		SQLiteDatabase db = sqlHelper.getReadableDatabase();
		
		
		Cursor cursor = db.query(
			tablename,
		    columns, 
		    null, 
		    null, 
		    null, 
		    null, 
		    null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    			    	
		      T item = createFrom(cursor);
		      
		     
		      list.add(item);
		    
		      cursor.moveToNext();
		    }		   
		   cursor.close();
		}
		catch(Exception ex)
		{
			Log.w(BaseDao.class.getName(), "Error get all: " + ex.getMessage());


		}
		return list;
	}

}
