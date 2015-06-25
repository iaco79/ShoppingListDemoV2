/**
 * Catalog initial data 
 */
package com.iaco.testapp.dao;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

public class InitializeCatalog implements DBHelper.AfterCreateHandler {
	
	final String[][] demoItems  = 
	{			
		{"Beer","brown ale / bitter 6 pack", "beer.jpeg"},
		{"Cereal","Corn pops / Frosted Sugar", "cereal.jpeg"},
		{"Avocados",  "1 ready to eat, 2 hard", "avocado.jpeg"},
		{"Fruit salad",  "Fresh fruit melon / pineapple", "fruit.jpeg"},
		{"Mashed potatoes",  "20 oz", "mashed.jpeg"},
		{"Sliced bread",  "Whole grain bread", "bread.jpeg"},
		{"Turkey ham",  "10 slices,low sodium", "ham.jpeg"},
		{"Frozen meals",  "5 packs", "frozenmeal.jpeg"},		
		{"Fish fillete",  "100 ", "fishfillet.jpeg"},
		{"Insant coffee",  "3 oz classic", "coffee.jpeg"},		
		{"Eggs",  "A dozen eggs", "eggs.jpeg"},
		{"Tortilla", "12 pack flour tortillas", "tortilla.jpeg"}
	};
	
	public void AfterCreate(SQLiteDatabase db)
	{
		db.execSQL("DELETE from item");
		
		for(int i=0;i<demoItems.length;i++)
		{
			int itemId= i;
			String name= demoItems[i][0];
			String description = demoItems[i][1];
			String imageName =  demoItems[i][2];
			
			db.execSQL(
					"INSERT INTO item (itemid, name, description, imagename, status) " 
				+	"VALUES (" 
						+ itemId
						+ ", '" + name + "'" 
						+ ", '" + description + "'"
						+ ", '" + imageName + "'"
						+ ", 1 "
						+ ")" 
				);



		}


	}
}
