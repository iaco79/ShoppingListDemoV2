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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.iaco.testapp.dao.ItemDao;
import com.iaco.testapp.dto.Item;

import android.widget.AdapterView;
import android.content.Intent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class EditItemListFragment extends ListFragment {


	private EditItemListAdapter m_adapter;
	private ItemDao m_dao = null;
	boolean needsRefresh = false;

	protected int getLayout() {
		return R.layout.editlistfragment;
	}
	
	List<Item> toDelete;

	
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
					public void OnSelectItem(Item item) {

						if(!toDelete.contains(item))
							toDelete.add(item);
					}

					@Override
					public void OnDeselectItem(Item item) {
						if(toDelete.contains(item))
							toDelete.remove(item);

					}


				});


		
	}


	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		toDelete  = new ArrayList<Item>();

		RelativeLayout layout = (RelativeLayout)inflater.inflate(getLayout(), container,
				false);
		ListView listView  = (ListView) layout.findViewById(android.R.id.list);

		ImageButton addButton = (ImageButton) layout.findViewById(R.id.editAddButton);
		ImageButton removeButton = (ImageButton) layout.findViewById(R.id.editRemoveButton);

		addButton.setOnClickListener(new View.OnClickListener() {
										 @Override
										 public void onClick(View v) {
											 Intent intent = new Intent(getActivity(), com.iaco.testapp.EditActivity.class);
											 startActivityForResult(intent, 1001);


										 }
									 }
		);

		removeButton.setOnClickListener(new View.OnClickListener() {
											@Override
											public void onClick(View v) {

												for (Item item : toDelete) {
													m_dao.delete(item);
												}
												refreshList();

											}
										}
		);


		return layout;
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

			refreshList();


		}

	}

	void refreshList()
	{

		this.m_adapter.items.clear();
		this.m_adapter.items.addAll(m_dao.getList());
		this.m_adapter.notifyDataSetChanged();

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
