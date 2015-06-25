/**
 * Shopping List Demo Main activity 
 * 
 * iaco79
 */
package com.iaco.testapp;

import com.iaco.testapp.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity 
{

	public static class TabPageAdapter extends SmartFragmentStatePagerAdapter {
		private static int NUM_ITEMS = 2;

		public TabPageAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0: // Fragment # 0 - This will show FirstFragment
					return new ItemListFragment();
				case 1: // Fragment # 0 - This will show FirstFragment different title
					return new EditItemListFragment();

				default:
					return null;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
				case 0: // Fragment # 0 - This will show FirstFragment
					return "Shopping";
				case 1: // Fragment # 0 - This will show FirstFragment different title
					return "Edit List";

				default:
					return null;
			}

		}

	}

	boolean _firstTime = true;
	int fragmentId = 0;

	TabPageAdapter pageAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		CustomViewPager pager = (CustomViewPager) findViewById(R.id.pager);

		PagerTabStrip  tabStrip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);

		tabStrip.setTabIndicatorColor(0x00bede);

		pageAdapter = new TabPageAdapter(getSupportFragmentManager());
		pager.setAdapter(pageAdapter);
		pager.setPagingEnabled(false);

		//showShop();

		pager.setCurrentItem(0);

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
			//	showShop();
				return true;
			}
			case R.id.action_edit: {
			//	showEdit();
				return true;
			}
		}

		return false;
	}

	void showShop()
	{

		/*
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
		*/
	}


	void showEdit()
	{
		/*
		if(fragmentId !=1) {
			fragmentId = 1;
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.ActivityMain_Layout, new EditItemListFragment());
			transaction.addToBackStack(null);
			transaction.commit();
		}
		*/
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
//		Fragment fragment = getSupportFragmentManager().findFragmentByTag("fragment1");

//		fragment.onActivityResult(requestCode, resultCode, data);
	}


}
