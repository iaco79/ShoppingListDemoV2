/**
 * Shopping List Demo Main activity 
 * 
 * iaco79
 */
package com.iaco.testapp;

import com.iaco.testapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity 
{

	boolean _firstTime = true;
	int fragmentId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		showShop();



	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection

		switch (item.getItemId()) {
			case R.id.action_shop: {
				showShop();
				return true;
			}
			case R.id.action_edit: {
				showEdit();
				return true;
			}
		}

		return false;
	}

	void showShop()
	{


		if(_firstTime) {
			getSupportFragmentManager().beginTransaction().add(
					R.id.ActivityMain_Layout,
					new ItemListFragment(),"fragment1"
			).commit();
		}
		else
		{
			if(fragmentId !=0) {
				fragmentId=0;
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.ActivityMain_Layout, new ItemListFragment(),"fragment1");
				transaction.addToBackStack(null);
				transaction.commit();
			}

		}
		_firstTime=false;

	}


	void showEdit()
	{
		if(fragmentId !=1) {
			fragmentId = 1;
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.ActivityMain_Layout, new EditItemListFragment());
			transaction.addToBackStack(null);
			transaction.commit();
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
//		Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment1");

//		fragment.onActivityResult(requestCode, resultCode, data);
	}


}
