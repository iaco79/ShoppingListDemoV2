/**
 * Helper class to manage the image being dragged 
 */
package com.iaco.testapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

public class ViewBitmapHandler {
	
		public Bitmap mViewBitmap;
	    public Point m_loc;
	    public Point m_delta;
	    public int m_width;
	    public int m_height;
	    
	    
	    public ViewBitmapHandler() 
	    {
	    }
	    
	    public static Bitmap getBitmapFromView(View view, int w, int h) {
	    	
	        Bitmap returnedBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(returnedBitmap);
	        Drawable bgDrawable =view.getBackground();
	        if (bgDrawable!=null) 
	            bgDrawable.draw(canvas);
	        else 
	            canvas.drawColor(Color.TRANSPARENT);
	        view.draw(canvas);
	        return returnedBitmap;
	    }
	    
	    
	    
	    /*
	     * This function gets a copy of the bitmap inside the image view	     
	     */
	    public void createBitmap(int position,  View v, Point startPoint) 
	    {
	        if (v == null) {
	            return;
	        }
	        ViewGroup parent = (ViewGroup)v.getParent();
	        
	        
	        int padLeft = (int)parent.getPaddingLeft();
	        int padTop =  (int)parent.getPaddingTop();
	        m_delta = startPoint;
	        
	        m_loc = new Point(
	        		(int)parent.getX() + (int)padLeft
	        		, 
	        		(int)parent.getY() + (int)padTop
	          );
	        
	        	
	        v.setPressed(false);

	        m_width = v.getWidth() 
	        - (int)parent.getPaddingTop()
	        - (int)parent.getPaddingBottom();
	        
	        m_height = v.getHeight() 
	        -(int)parent.getPaddingLeft()
	        -(int)parent.getPaddingRight();
	        
	        mViewBitmap = getBitmapFromView(v, m_width, m_height);
	      
	    }

	    /**
	     * 
	     * Removes the Bitmap from the ImageView created in
	     * onCreateFloatView() and tells the system to recycle it.
	     */
	    public void destroyBitmap() 
	    {
	        mViewBitmap.recycle();
	        mViewBitmap = null;
	    }

}

