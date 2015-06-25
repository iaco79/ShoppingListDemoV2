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
import android.widget.ImageView;
import android.widget.TextView;

import com.iaco.testapp.dto.Item;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EditItemListAdapter extends ArrayAdapter<Item> {

    public List<Item> items;

    private OnEditItemClickListener itemClickListener;

    public interface OnEditItemClickListener
    {
  	  void OnEditItemClick(Item item);
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
    
    public Drawable  getImageFromAssets(String imagename) 
    {
        try {

            InputStream ims = getContext().getAssets().open("images/" + imagename);
            Drawable d = Drawable.createFromStream(ims, null);
            return d;
        }
        catch(IOException ex) {
            return null;
        }
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
                Drawable d= getImageFromAssets(o.getPictureName());


                topText.setText(o.getName());
                bottomText.setText(o.getDescription());
                image.setImageDrawable(d);
                image.setTag(o);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(itemClickListener!= null)
                        {
                            ImageView img = (ImageView) v;
                            Item item = (Item)v.getTag();
                            itemClickListener.OnEditItemClick(item);


                        }
                    }
                });

            }


            return v;
    }
}