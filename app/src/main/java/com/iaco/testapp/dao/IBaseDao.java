package com.iaco.testapp.dao;

import java.util.List;

import com.iaco.testapp.dto.BaseDto;

import android.content.ContentValues;
import android.database.Cursor;

public interface IBaseDao<T extends BaseDto > {

	public String getTableName();

	public String[] getColumns();
	
	public String getIdQuery();
	
	public String[] getIdValues(T value);
	
	public ContentValues getUpdateValues( T value);
	public ContentValues getInsertValues (T value);

	public int update(T value);
	
	public int delete(T value);

	public int get(T value);

	public T createFrom(Cursor cursor);

	public List<T> getList();
}
