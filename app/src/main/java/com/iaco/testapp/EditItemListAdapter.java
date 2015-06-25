/**
 * Item array adapter
 */
package com.iaco.testapp;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.iaco.testapp.dto.Item;
import com.iaco.testapp.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EditItemListAdapter extends ArrayAdapter<Item> {

    public List<Item> items;

    private OnEditItemClickListener itemClickListener;

    public interface OnEditItemClickListener
    {
  	    void OnSelectItem(Item item);
        void OnDeselectItem(Item item);

    }

    public void setOnEditItemClickListener(OnEditItemClickListener pitemClickListener)
    {
    	itemClickListener = pitemClickListener;
    }


    public EditItemListAdapter(
            Context context,
            int itemResourceId,
            List<Item> items) {
            super(context, itemResourceId,
            items);
            this.items = items;
            
    }
    

        
    @Override
    public View getView(int position, 
    					View convertView, 
    					ViewGroup parent
    		) {
    	
            View v = convertView;
            if (v == null) {            	
                LayoutInflater vi = 
                		(LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                
                v = vi.inflate(R.layout.editrow, null);
            }
            
            Item o = items.get(position);
            
            
            if (o != null) {

                TextView topText = (TextView) v.findViewById(R.id.toptext);
                TextView bottomText = (TextView) v.findViewById(R.id.bottomtext);
                ImageView image = (ImageView) v.findViewById(R.id.itemimage);
                CheckBox checkbox = (CheckBox) v.findViewById(R.id.deletecheck);
                Drawable d= FileUtil.getImage(getContext(), o.getPictureName());

                topText.setText(o.getName());
                bottomText.setText(o.getDescription());
                image.setImageDrawable(d);
                checkbox.setTag(o);
                checkbox.setChecked(o.isToDelete());

                checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        Item item = (Item) buttonView.getTag();
                        if(isChecked)
                        {
                            item.setToDelete(true);
                            itemClickListener.OnSelectItem(item);
                        }
                        else
                        {

                            item.setToDelete(false);
                            itemClickListener.OnDeselectItem(item);

                        }
                    }
                });


            }


            return v;
    }
}