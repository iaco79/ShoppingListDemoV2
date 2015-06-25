/**
 * Item array adapter
 */
package com.iaco.testapp;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.iaco.testapp.dto.Item;
import com.iaco.testapp.util.FileUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class ItemListAdapter extends ArrayAdapter<Item> {

    public List<Item> items;
    private OnItemViewChangeListener itemViewChangeListener;
    
    public interface OnItemViewChangeListener
    {
  	  void onChange(Item item);
    }
    
    public void setOnItemViewChangeListener(OnItemViewChangeListener itemChangeListener)
    {
    	itemViewChangeListener = itemChangeListener;
    }


    public ItemListAdapter(
    		Context context, 
    		int textViewResourceId, 
    		List<Item> items) {
            super(context, textViewResourceId, 
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
                
                v = vi.inflate(R.layout.row, null);
            }
            
            Item o = items.get(position);
            
            
            if (o != null) {
            		
            	    SlideToggle cr = (SlideToggle) v.findViewById(R.id.rowid);
                    cr.mTextTop.setText(o.getName());
                    cr.mTextBottom.setText(o.getDescription());
                    
                    //Load the images using a background task
                    //to make the listview scrolling smooth
                    new AsyncTask<Object,
                    			  Void, 
                    			  Drawable>() {
                    	ImageView imv1 = null;
                    	ImageView imv2 = null;
                        @Override
                        protected Drawable doInBackground(Object... params) {
                            String imageName = (String)params[0];
                            Drawable d= FileUtil.getImage(getContext(),imageName);
                            
                            imv1 =  (ImageView)params[1];
                            imv2 =  (ImageView)params[2];
                            
                            return d;
                        }

                        @Override
                        protected void onPostExecute(Drawable d) {
                            super.onPostExecute(d);
                            if (d!= null) {
                            	imv1.setImageDrawable(d);
                            	imv2.setImageDrawable(d);
                            }
                        }
                    }.execute(o.getPictureName(),cr.mImageLeft, cr.mImageRight);
                                        
                    
                    cr.resetToState(o.getStatus());
                    cr.setStateListener( position, new SlideToggle.StateChangeListener() 
           				{	
							@Override
							public void OnChange(int lposition, int state) 
							{	
								Item pd = items.get(lposition);
								pd.setStatus(state);
								
								if(itemViewChangeListener!=null)
									itemViewChangeListener.onChange(pd);
								
							};
           
           				}
                    );
            }
            return v;
    }
}