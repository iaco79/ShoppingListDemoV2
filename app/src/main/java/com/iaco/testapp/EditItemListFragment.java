/**
 * List Fragment that contains the item list
 */
package com.iaco.testapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.iaco.testapp.dao.ItemDao;
import com.iaco.testapp.dto.Item;

import android.widget.AdapterView;
import android.content.Intent;
public class EditItemListFragment extends ListFragment {


	private EditItemListAdapter m_adapter;
	private ItemDao m_dao = null;
	boolean needsRefresh = false;

	protected int getLayout() {
		return R.layout.editlistfragment;
	}
	

	
	public void setListAdapter() {
		
		//Initialize dao to retrieve Items from DB
		m_dao = new ItemDao(this.getActivity());
		
		this.m_adapter = new EditItemListAdapter(
				this.getActivity(),
				R.layout.row,
				m_dao.getList());



		setListAdapter(this.m_adapter);

		this.m_adapter.setOnEditItemClickListener(
				new EditItemListAdapter.OnEditItemClickListener() {

					/**
					 * Update the item status in the DB table
					 */
					@Override
					public void OnEditItemClick(Item item) {
						Intent intent = new Intent(getActivity(), com.iaco.testapp.EditActivity.class);
						startActivityForResult(intent, 1001);

					}
				});


		
	}


	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ListView listView  =  (ListView) inflater.inflate(getLayout(), container,
				false);




		return listView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setListAdapter();
	}

	@Override
	public void onResume()
	{
		super.onResume();


		if(needsRefresh)
		{

			this.m_adapter.items.clear();
			this.m_adapter.items.addAll(m_dao.getList());
			this.m_adapter.notifyDataSetChanged();


		}

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		if (requestCode == 1001) {
			// Make sure the request was successful
			if (resultCode == Activity.RESULT_OK) {
				// The user picked a contact.
				// The Intent's data Uri identifies which contact was selected.



				needsRefresh=true;


			}
		}
	}

    
}
