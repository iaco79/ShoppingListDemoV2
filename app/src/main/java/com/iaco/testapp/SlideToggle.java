/**
 * Slide Toggle custom view  
 * This is used to set the status of a shopping item (available  / not available)
 * the user swipes the image from left to right or right to left to set the toggle state 
 * if the user is dragging an image and then cancels the gesture the image moves back to its original position   
 */
package com.iaco.testapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

public class SlideToggle extends LinearLayout implements OnTouchListener  {

  
  public ImageView mImageLeft;
  public ImageView mImageRight;
  public TextView mTextTop;
  public TextView mTextBottom;
  private Paint paint;
  private ViewBitmapHandler m_fvm = null;
  private GestureDetector m_gdt = null;
  protected DropAnimator mDropAnimator;


  //Flag to indicate that a dragging operation is in process  
  private boolean m_dragging = false;
  
  //Flag to indicate that a potential dragging operation is in process
  private boolean m_potential = false;
  private Point   m_potentialStart;
  
  //Flag to indicate that the user did a swipe and that item image was translated from one side to the other
  private boolean m_fling  = false;
  private int m_statePosition = 0;  
  private float mFlingSpeed = 500f;
  	
  View fromView = null;
  View toView = null;
  View mCurrentView = null;
  View container = null;
  int m_swipeOrigin = -1;
  int m_direction = 1;
 
  
  public interface StateChangeListener
  {
	  void OnChange(int position, int state);
  
  }
  
  StateChangeListener mStateListener = null;
  
  public void setStateListener(int statepos, StateChangeListener stateListener)
  {
	  m_statePosition = statepos;
	  mStateListener=stateListener;
  }
  
  
  private void startDragging(View view, int swipeOrigin, Point startPoint)
  {
	  m_dragging = true;
	  if(m_fvm.mViewBitmap == null )
	  {
		  if(swipeOrigin==0)
			  m_direction = 1;
		  else
		  if(swipeOrigin==1)
			  m_direction = -1;
		  
		  
		  m_fvm.createBitmap(0,((ViewGroup)fromView).getChildAt(1) , startPoint);
		  ((ViewGroup)fromView).getChildAt(1).setVisibility(INVISIBLE);
		  
		  invalidate();
		  
		  
	  }
  }
  
  private void continueDragging(Point position)
  {  
	  m_fvm.m_loc.x = position.x - m_fvm.m_delta.x;
	  invalidate();
    
  }
  
  private void stopDraggingAnimate(Point position)
  {  
	  mDropAnimator.mStopX  = position.x;
	  mDropAnimator.mIsFling = false;
	  mDropAnimator.start();
	  
  }
  
  private void stopDragging(Point position)
  {
	  
	  m_fvm.m_loc.x = position.x - m_fvm.m_delta.x;
	  
	  
	  if(m_fling)
	  {
		  
		  ((ViewGroup)fromView).getChildAt(1).setVisibility(INVISIBLE);
		  ((ViewGroup)toView).getChildAt(1).setVisibility(VISIBLE);
		 
		  invalidate();
		
		  //Notify the listener that the toggle status has changed
		  if (mStateListener != null)
		  {
			  mStateListener.OnChange(m_statePosition, m_direction==1?0:1);
		  }
	  }
	  else
	  {  
		  
		  ((ViewGroup)fromView).getChildAt(1).setVisibility(android.view.View.VISIBLE);
		  invalidate();
	  }
	  
	  destroyFloatView();
  
	  
	  m_dragging = false;
	  m_fling = false;
	  
	  
  }
  
  public void resetToState(int state)
  {
	  m_dragging = false;
	  m_fling = false;
	  m_potential = false;
	  fromView = null;
	  toView = null;
	  mCurrentView = null;
	  container = null;
	  m_swipeOrigin = -1;
	  
	  if(state==1)
	  {  
		  findViewById(R.id.iconLeft).setVisibility(VISIBLE);
		  findViewById(R.id.iconRight).setVisibility(INVISIBLE);
	  }
	  else
      if(state == 0)
      {  
    	  findViewById(R.id.iconLeft).setVisibility(INVISIBLE);
		  findViewById(R.id.iconRight).setVisibility(VISIBLE);
	  }
	  
  }
  
  
  
  private void destroyFloatView() {
      if (m_fvm.mViewBitmap != null) {
    	  
    	  m_fvm.destroyBitmap();
          
          invalidate();
      }
  }
  

  public SlideToggle(Context context, AttributeSet attrs) {
    super(context, attrs);

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    
    View v = inflater.inflate(R.layout.slidetoggle, this, true);

    container = v;
    mImageLeft = (ImageView)v.findViewById(R.id.iconLeft);
    mImageRight = (ImageView)v.findViewById(R.id.iconRight);
    mTextTop = (TextView)v.findViewById(R.id.toptext);
    mTextBottom = (TextView)v.findViewById(R.id.bottomtext);
    
    mImageLeft.setOnTouchListener(this);
    mImageRight.setOnTouchListener(this);
    v.setOnTouchListener(this);
    
    m_fvm = new ViewBitmapHandler();
	m_gdt = new GestureDetector(this.getContext(), new GestureListener());
	
	paint = new Paint();
	paint.setAntiAlias(true);
	paint.setFilterBitmap(true);
	paint.setDither(true);
	
	
	int dropAnimation = 150; 
	float smoothness = 0.5f;
	mDropAnimator = new DropAnimator(smoothness, dropAnimation);
    
	
    
  }
  
  @Override
  protected void dispatchDraw(Canvas canvas) 
  {
	    super.dispatchDraw(canvas);
	    
	    if (m_dragging && m_fvm.mViewBitmap != null) {
	    	
            int x = m_fvm.m_loc.x;
            int y = m_fvm.m_loc.y;
            
            canvas.save();
            canvas.translate(x,y);
            canvas.clipRect(0, 0, m_fvm.m_width, m_fvm.m_height);
            
            canvas.saveLayerAlpha(0, 0, 
            		m_fvm.m_width, 
            		m_fvm.m_height, 
            		160, Canvas.ALL_SAVE_FLAG);
            canvas.drawBitmap(m_fvm.mViewBitmap,0,0,paint);
            canvas.restore();
            
        }
  }
  
  /**
   * ViewGroup touch event handler
   */
  @Override
  public boolean onTouchEvent(MotionEvent ev)
  {  
	  boolean handled= false;

	  
	  switch (ev.getAction() & MotionEvent.ACTION_MASK) 
	  {
	      case MotionEvent.ACTION_CANCEL:
		      	{
		      		
		      		m_potential = false;
		      		m_swipeOrigin = -1;
		      		
		      		//if the user cancels the gesture then move the image to its original position
		          if (m_dragging && !m_fling) {
		        	  stopDraggingAnimate(new Point((int) ev.getX(),(int) ev.getY()));
		        	  
		          }
		      	}
	          break;
	      case MotionEvent.ACTION_UP:
		      	{	
		      		m_potential = false;
		      		m_swipeOrigin = -1;
		      		
		      		if (m_dragging && !m_fling) {
		      			
			      		//if the user cancels the gesture then move the image to its original position
		      			stopDraggingAnimate(new Point((int) ev.getX(),(int) ev.getY()));
		      			
		          }
		      		
		      		//allow list view scrolling again
		      		getParent().requestDisallowInterceptTouchEvent(false);
		      	}
	          break;
	          
	      case MotionEvent.ACTION_MOVE:
	      	{
	      		
	      		if(m_potential)
	      		{

	      			int dif = Math.abs(m_potentialStart.x - (int)ev.getX());
	      			
	      			//if the user has advanced 30 units horizontally 
	      			//inside the item image then start dragging
		  			if(dif>30)
		  			{
		  				
		  				//if an item is being dragged then avoid the list view scrolling 
		  				m_potential = false;
			      		getParent().requestDisallowInterceptTouchEvent(true);
		  				startDragging(fromView, m_swipeOrigin, new Point(
		  						(int)ev.getX()-(int)fromView.getX(),
		  						(int) ev.getY()-(int)fromView.getY()));
		  				return true;
		  			}

	      		
	      		}
	      		if (m_dragging && !m_fling) {
	      			
	      			
	      			
	      			continueDragging(new Point((int) ev.getX(),(int) ev.getY()));
	      			handled=true;
	      		}
	      	}
	          break;
	  }
	  if(!handled)
	  {
		 handled = super.onTouchEvent(ev);
	  }
	  
	  return true;
  }
  
  
  /**
   * We use this touch listener to get the current view being touched
   * leftimage / rightimage or the container (view group)
   * and call the gesturedetector to detect an ondown or onfling event
   */
  public boolean onTouch(final View view, final MotionEvent ev) 
  {	
	  	mCurrentView = view;
	  	
	  	if(!m_potential && !m_dragging)
	  	{
			if(view.getId() == R.id.iconLeft)
			{	
				
				m_swipeOrigin=0;
				
				fromView = (View)view.getParent();
				toView = (View)((View)view.getParent().getParent()).findViewById(R.id.iconRight).getParent();
				
				
			}
			if(view.getId() == R.id.iconRight)
			{	
				m_swipeOrigin=1;
				
				fromView = (View)view.getParent();	            			
				toView = (View)((View)view.getParent().getParent()).findViewById(R.id.iconLeft).getParent();
							
			}		
	  	}

		{
			
			m_gdt.onTouchEvent(ev);
		}
		
		return false;
  }

  
  private class GestureListener extends SimpleOnGestureListener {
  	

	/**
	 * Here we detect if the user tapped one of the image views
	 * if so then determine if it was the left or right image
	 * and activate the flag that a potential dragging is about to occur
	 */
  	@Override
  	public boolean onDown(MotionEvent e)
  	{	

  		if(!m_potential && (!m_dragging && m_swipeOrigin >=0))
  		{
  			m_potential = true;
  			m_potentialStart = new Point( 
  								(int)fromView.getX()+ (int)e.getX(),
  								(int)fromView.getY()+ (int) e.getY());
  			
  			
  		}
  		
  		return false;
  	}
  	 @Override
     public final boolean onFling(
    		 MotionEvent e1, 
    		 MotionEvent e2, 
    		 float velocityX,
             float velocityY) 
  	 {
         
         if (m_dragging && !m_fling && mCurrentView!=null && mCurrentView.getId() == R.id.rowid) {
        	 
             
	      	//if the user swipes the image to the other side then
        	//move the image from its current location to the other side 
        	//and change the toggle status 
        	 
             if (m_swipeOrigin ==0 && velocityX > mFlingSpeed) 
             {
 
                 { 
                	 
                	 m_fling = true;
                	 mDropAnimator.mIsFling = true;
                	 mDropAnimator.mStopX  =   e2.getX();
               	  	 mDropAnimator.start();
                     
                 }
             }
             
             if (m_swipeOrigin ==1 && velocityX < -mFlingSpeed) 
             { 
            	 m_fling = true;
            	 mDropAnimator.mIsFling = true;
            	 
            	 mDropAnimator.mStopX  =   e2.getX();
           	  	 mDropAnimator.start();
          
             }
             
             return true;
             
         }
         return false;
     }    
  }
  
  private class SmoothAnimator implements Runnable {
      protected long mStartTime;

      private float mDurationF;

      private float mAlpha;
      private float mA, mB, mC, mD;

      private boolean mCanceled;

      public SmoothAnimator(float smoothness, int duration) {
          mAlpha = smoothness;
          mDurationF = (float) duration;
          mA = mD = 1f / (2f * mAlpha * (1f - mAlpha));
          mB = mAlpha / (2f * (mAlpha - 1f));
          mC = 1f / (1f - mAlpha);
      }

      public float transform(float frac) {
          if (frac < mAlpha) {
              return mA * frac * frac;
          } else if (frac < 1f - mAlpha) {
              return mB + mC * frac;
          } else {
              return 1f - mD * (frac - 1f) * (frac - 1f);
          }
      }

      public void start() {
          mStartTime = SystemClock.uptimeMillis();
          mCanceled = false;
          onStart();
          post(this);
      }

     
      public void onStart() {
          // stub
      }

      public void onUpdate(float frac, float smoothFrac) {
          // stub
      }

      public void onStop() {
          // stub
      }

      @Override
      public void run() {
          if (mCanceled) {
              return;
          }

          float fraction = ((float) (SystemClock.uptimeMillis() - mStartTime)) / mDurationF;

          if (fraction >= 1f) {
              onUpdate(1f, 1f);
              onStop();
          } else {
              onUpdate(fraction, transform(fraction));
              post(this);
          }
      }
  }
  
  private class DropAnimator extends SmoothAnimator {

	  public  float mStopX;
	  private boolean mIsFling;
	  
      private float mTargetX;
      private float mCurrentX;
      private float mDeltaX;
      

      public DropAnimator(float smoothness, int duration) {
          super(smoothness, duration);
      }

      @Override
      public void onStart() {
    	  mCurrentX = mStopX;
           
    	  if(!mIsFling)
    		  mTargetX = fromView.getX()+ m_fvm.m_delta.x  + fromView.getPaddingLeft();
    	  else
    		  mTargetX = toView.getX()+ m_fvm.m_delta.x + toView.getPaddingLeft();

          mDeltaX =  mCurrentX - mTargetX;
      }
     
      @Override
      public void onUpdate(float frac, float smoothFrac) {
          
          final float deltaX =  mCurrentX - mTargetX;
          final float f = 1f - smoothFrac;
          if ( f < Math.abs(deltaX / mDeltaX)) 
          { 
        	  mCurrentX = mTargetX + (mDeltaX * f);
        	  continueDragging(new Point((int)mCurrentX, 0)); 
          }
      }

      @Override
      public void onStop() {
    	  
    	  stopDragging(new Point((int)mCurrentX, 0));
    	  
      }

  }

  
  

} 