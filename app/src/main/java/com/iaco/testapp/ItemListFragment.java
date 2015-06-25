/**
 * List Fragment that contains the item list
 */
package com.iaco.testapp;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.iaco.testapp.dao.ItemDao;
import com.iaco.testapp.dto.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ItemListFragment extends ListFragment {

	
	private ItemListAdapter m_adapter;
	private ItemDao m_dao = null;
	private int filter = -1;

	protected int getLayout() {
		return R.layout.listfragment;
	}
	


	
	public void setListAdapter() {
		
		//Initialize dao to retrieve Items from DB
		m_dao = new ItemDao(this.getActivity());
		
		this.m_adapter = new ItemListAdapter(
				this.getActivity(),
				R.layout.row,
				m_dao.getList());
		
		this.m_adapter.setOnItemViewChangeListener(
				new ItemListAdapter.OnItemViewChangeListener() {
					
					/**
					 * Update the item status in the DB table
					 */
					@Override
					public void onChange(Item item) {
						m_dao.update(item);

						if(filter>=0 && filter != item.getStatus())
						{

							m_adapter.items.remove(item);
							m_adapter.notifyDataSetChanged();

						}

						
					}
				});
		
		setListAdapter(this.m_adapter);
		
	}


	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		RelativeLayout layout = (RelativeLayout)inflater.inflate(getLayout(), container,
				false);
		ListView listView  = (ListView) layout.findViewById(android.R.id.list);


		TabHost tabHost = (TabHost) layout.findViewById(R.id.tabHost);
		TabHost.TabSpec tab1 = tabHost.newTabSpec("TabAll");
		TabHost.TabSpec tab2 = tabHost.newTabSpec("TabYes");
		TabHost.TabSpec tab3 = tabHost.newTabSpec("TabNo");
		tabHost.setup();


		View v= tabHost.getTabContentView();

		View v2= v.findViewById(R.id.dummyTab1);


		tab1.setIndicator("All");
		tab1.setContent(R.id.dummyTab1);

		tab2.setIndicator("Yes");
		tab2.setContent(R.id.dummyTab2);

		tab3.setIndicator("No");
		tab3.setContent(R.id.dummyTab3);

		tabHost.addTab(tab1);
		tabHost.addTab(tab2);
		tabHost.addTab(tab3);
		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {

				if (tabId == "TabAll") {
					filter = -1;
					refreshList();
				} else if (tabId == "TabYes") {
					filter = 1;
					refreshList();
				} else if (tabId == "TabNo") {

					filter = 0;
					refreshList();
				}


			}
		});

		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setListAdapter();
	}

	private void refreshList()
	{
		List<Item> list = new ArrayList<Item>();
		list = m_dao.getList();


		if(filter>=0)
		{
			for (Iterator<Item> iter = list.listIterator(); iter.hasNext(); )
			{
				Item item = iter.next();
				if (item.getStatus() != filter)
				{
					iter.remove();
				}
			}
		}

		this.m_adapter.items.clear();
		this.m_adapter.items.addAll(list);
		this.m_adapter.notifyDataSetChanged();
	}






}
