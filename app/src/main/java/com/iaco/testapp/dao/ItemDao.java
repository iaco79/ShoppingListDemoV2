package com.iaco.testapp.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.iaco.testapp.dto.Item;

public class ItemDao extends BaseDao<Item> {


	private final static String [] columns = {
		DBHelper.COLUMN_ITEMID,
		DBHelper.COLUMN_NAME, 
		DBHelper.COLUMN_DESCRIPTION, 
		DBHelper.COLUMN_IMAGENAME,
		DBHelper.COLUMN_STATUS
		};
	
	public ItemDao (Context context)  {
		super(context);
	}

	@Override
	public String getTableName() {

		return DBHelper.TABLE_ITEM_CATALOG;
	}

	@Override
	public String[] getColumns() {

		return columns;
	}

	@Override
	public String getIdQuery()
	{
		return DBHelper.COLUMN_ITEMID + "= ?"; 
		
	}
	
	@Override
	public String[] getIdValues(Item item)
	{
		return new String[] {String.valueOf(item.getItemId())};
	}

	
	
	@Override
	public ContentValues getUpdateValues(Item item)
	{
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_STATUS,item.getStatus());          

		return values;
	}

	public ContentValues getInsertValues (Item item)
	{
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_STATUS,item.getStatus());
		values.put(DBHelper.COLUMN_NAME,item.getName());
		values.put(DBHelper.COLUMN_DESCRIPTION,item.getDescription());
		values.put(DBHelper.COLUMN_IMAGENAME,item.getPictureName());

		return values;
	}
	public ContentValues getDeleteValues (Item item)
	{
		ContentValues values = new ContentValues();
		values.put(DBHelper.COLUMN_STATUS,item.getStatus());
		values.put(DBHelper.COLUMN_NAME,item.getName());
		values.put(DBHelper.COLUMN_DESCRIPTION,item.getDescription());
		values.put(DBHelper.COLUMN_IMAGENAME,item.getPictureName());

		return values;
	}



	@Override
	public Item createFrom(Cursor cursor) {
		
		Item def = new Item();
		def.setItemId(cursor.getShort(0));
		def.setName(cursor.getString(1));
		def.setDescription(cursor.getString(2));
		def.setPictureName(cursor.getString(3));
		def.setStatus(cursor.getInt(4));
		def.setDirty(false);
		
		
		return def;
		
	}
	
	
}

