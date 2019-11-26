package com.anzhi.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Scroller
import java.io.InputStream
import java.lang.reflect.Constructor

/**
 *
 * @ProjectName: MyApplication3
 * @Package: com.anzhi.myapplication
 * @ClassName: bigView
 * @Description: Kotlin类作用描述
 * @Author: yangff
 */
class BigView :View , GestureDetector.OnGestureListener,View.OnTouchListener  {

    lateinit var bitmapRegionDecoder: BitmapRegionDecoder
    lateinit var gestureDetector: GestureDetector
    lateinit var mRect: Rect
    lateinit var mOptions: BitmapFactory.Options
    private var mImageWidth: Int = 0
    private var mImageHeight: Int = 0
    var mBitmap: Bitmap? =null
    private var moveUp: Int = 0
    private var scale: Float = 0.toFloat()
    lateinit var mScroller: Scroller
    private var displayWidth: Int = 0
    private var displayHeight: Int = 0
    var mMatrix : Matrix = Matrix()
    lateinit var mContext:Context
    constructor(context: Context):this(context,null){

    }
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0){

    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0){

    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int):super(context, attrs, defStyleAttr, defStyleRes){
        mContext=context
        init()
    }
     fun init(){
        mRect= Rect();
        gestureDetector=GestureDetector(mContext,this)
        mOptions=BitmapFactory.Options()
        setOnTouchListener(this)
        mScroller= Scroller(mContext)
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        scale = (measuredWidth / mImageWidth).toFloat()
        if (scale < 0.1) {
            scale = 2f
        }
        displayWidth = mImageWidth / 2
        if (mImageWidth / 2 > measuredWidth) {
            displayWidth = measuredWidth
        }
        displayHeight = (measuredHeight / scale).toInt()
        mRect.top = 0
        mRect.left = 0
        mRect.bottom = displayHeight
        mRect.right = displayWidth
        mMatrix!!.setScale(scale,scale)
    }
    /*
    //重点说下isShareable：true：表示浅复制(shallow copy)，false：表示深度复制(deep copy)

        浅拷贝：是指在拷贝对象时，对于基本数据类型的变量会重新复制一份，而对于引用类型的变量只是对引用进行贝。深拷贝： 则是对对象及该对象关联的对象内容，都会进行一份拷贝(内存中创建新的副本)。
     */
    fun setImage(inputStream : InputStream){
        mOptions.inJustDecodeBounds=true//主要是获取bitmap一些信息例如宽高
        BitmapFactory.decodeStream(inputStream,null,mOptions)
        mImageHeight=mOptions.outHeight
        mImageWidth=mOptions.outWidth
        mOptions.inMutable=true//主要是做复用的
        mOptions.inPreferredConfig=Bitmap.Config.RGB_565//没有特殊要求的一般设置成这个占用内存小
        mOptions.inJustDecodeBounds=false
        bitmapRegionDecoder= BitmapRegionDecoder.newInstance(inputStream,false)
    }
    override fun onShowPress(p0: MotionEvent?) {

    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {

    }

    override fun onDown(p0: MotionEvent?): Boolean {
        if(!mScroller.isFinished){
            mScroller.forceFinished(true)
        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(bitmapRegionDecoder==null){
            return
        }
        mOptions.inBitmap=mBitmap
        mBitmap= bitmapRegionDecoder.decodeRegion(mRect,mOptions)
        canvas!!.drawBitmap(mBitmap!!,mMatrix,null)
    }
    /**
     * 处理惯性问题
     * @param e1 .手势起点的移动事件
     * @param e2   当前手势点的移动事件
     * @param p2   每秒移动的x点
     * @param p3   每秒移动的y点
     * @return
     */
    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        mScroller.fling(mRect.left,mRect.top, (-p2).toInt(), (-p3).toInt(),0,mImageWidth-displayWidth,0,mImageHeight-displayHeight)
        return false
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
       mRect.offset(p2.toInt(), p3.toInt())//直接改变Rect位置
        exceptionalaseCertain()
        return false
    }
     fun exceptionalaseCertain(){
        if( mRect.top<0){
            mRect.top=0
            mRect.bottom=displayHeight
        }
         if(mRect.bottom>mImageHeight){
             mRect.bottom=mImageHeight
             mRect.top=mImageHeight-displayHeight
         }
         if(mRect.left<0){
             mRect.left=0
             mRect.right=displayWidth
         }
         if(mRect.right>mImageWidth){
             mRect.left=mImageWidth-displayWidth
             mRect.right=mImageWidth
         }
         invalidate()
     }
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
       return gestureDetector.onTouchEvent(p1)
    }
}