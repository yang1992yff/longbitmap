package com.anzhi.myapplication;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

/**
 * @ProjectName: MyApplication3
 * @Package: com.anzhi.myapplication
 * @ClassName: bindView
 * @Description: java类作用描述
 * @Author: yangff
 */
public class BigJavaView extends View implements GestureDetector.OnGestureListener,View.OnTouchListener {
    private BitmapRegionDecoder bitmapRegionDecoder;
    private GestureDetector gestureDetector;
    private Rect mRect;
    private BitmapFactory.Options options;
    private int mImageWidth;
    private int mImageHeight;
    private Bitmap bitmap;
    private int moveUp;
    private float scale;
    private Scroller scroller;
    private int displayWidth;
    private int displayHeight;
    private Matrix matrix;
    private int witdth=0;
    private int heifht=0;
    private boolean widthBoundary =false;
    private boolean heightBoundary=false;
    //这个仅仅只是随便写一个动画移动的
    public void setMoveUp(int moveUp) {
        mRect.top=heifht;
        mRect.bottom=(int) ((getMeasuredHeight()+heifht));
        mRect.left=witdth;
        mRect.right= getMeasuredWidth()+witdth;
        if(!widthBoundary){
            if(mRect.right<mImageWidth){
                witdth+=1;
            } else{
                widthBoundary=true;
            }
        }else{
            if(mRect.left>0){
                witdth--;
            } else{
                widthBoundary=false;
            }
        }

        if(!heightBoundary){
            if(mRect.bottom<mImageHeight){
                heifht+=60*scale;
            }else{
                heightBoundary=true;
            }
        }
        else{
            if(mRect.top>0){
                heifht--;
            }else {
                heightBoundary=false;
            }
        }
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        scale=getMeasuredWidth()/mImageWidth;
        if(scale<0.1){
            scale=2f;
        }
        displayWidth=mImageWidth/2;
        if(mImageWidth/2>getMeasuredWidth()){
            displayWidth=getMeasuredWidth();
        }
        displayHeight= (int) (getMeasuredHeight()/scale);
        mRect.top=0;
        mRect.left=0;
        mRect.bottom=displayHeight;
        mRect.right=displayWidth;
        matrix=new Matrix();
        matrix.setScale(scale,scale);
    }

    public BigJavaView(Context context) {
        this(context,null);

    }

    public BigJavaView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public BigJavaView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public BigJavaView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mRect=new Rect();
        options= new BitmapFactory.Options();
        gestureDetector=new GestureDetector(context,this);
        setOnTouchListener(this);
        scroller=new Scroller(context);

    }

   public void init(InputStream inputStream){
       options.inJustDecodeBounds=true;
       BitmapFactory.decodeStream(inputStream,null,options);
       mImageHeight=options.outHeight;
       mImageWidth=options.outWidth;
       options.inMutable=true;
       //设置格式成RGB_565
       options.inPreferredConfig=Bitmap.Config.RGB_565;
       options.inJustDecodeBounds=false;
       try {
           bitmapRegionDecoder=BitmapRegionDecoder.newInstance(inputStream,false);
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果解码器拿不到，表示没有设置过要显示的图片
        if(null==bitmapRegionDecoder){
            return;
        }

        options.inBitmap=bitmap;
       bitmap=bitmapRegionDecoder.decodeRegion(mRect,options);

        canvas.drawBitmap(bitmap,matrix,null);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        if(!scroller.isFinished()){
            scroller.forceFinished(true);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        //上下移动的时候，需要改变显示区域   改mRect
        mRect.offset((int) v,(int)v1);
        exceptionalaseCertain();
        return false;
    }
    //极端情况达到的尾部或者顶部以及左边或者右边
    private void exceptionalaseCertain(){
        if(mRect.right>mImageWidth){
            mRect.right=mImageWidth;
            mRect.left= (int) (mImageWidth-displayWidth);
        }
        if(mRect.left<0){
            mRect.left=0;
            mRect.right=displayWidth;
        }
        if(mRect.bottom>mImageHeight){
            mRect.top=mImageHeight-displayHeight;
            mRect.bottom=mImageHeight;
        }
        if(mRect.top<0){
            mRect.top=0;
            mRect.bottom=displayHeight;
        }
        invalidate();
    }
    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        scroller.fling(mRect.left,mRect.top,(int)-v,(int)-v1,10,mImageWidth,10,mImageHeight-displayHeight);
        return false;
    }

    @Override
    public void computeScroll() {
        if(!scroller.isFinished()&&scroller.computeScrollOffset()){
            mRect.top=scroller.getCurrY();
            mRect.bottom=mRect.top+displayHeight;
            mRect.left=scroller.getCurrX();
            mRect.right= mRect.left+displayWidth;
            exceptionalaseCertain();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }
}
