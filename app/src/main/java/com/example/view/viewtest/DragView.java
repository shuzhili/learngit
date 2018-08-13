package com.example.view.viewtest;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DragView extends FrameLayout {

    private Context mContext;

    public DragView(@NonNull Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    public DragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    View childView;
    public int downX;
    public int downY;
    public int windowX;
    public int windowY;
    private Vibrator mVibrator;

    private void initView(Context context) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        TextView textView = new TextView(context);
        textView.setTextSize(30);
        textView.setText("hahah");
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.width = 300;
        layoutParams.height = 80;
        textView.setLayoutParams(layoutParams);
        addView(textView);
        childView = textView;
        invalidate();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) ev.getX();
            downY = (int) ev.getY();
            windowX = (int) ev.getX();
            windowY = (int) ev.getY();
            setOnItemClickListener(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                windowX = (int) event.getX();
                downY = (int) event.getY();
                windowY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                onDrag(x, y, (int) event.getRawX(), (int) event.getRawY());
                break;
            case MotionEvent.ACTION_UP:
                rawX = (int) event.getRawX();
                rawY = (int) event.getRawY();
                Log.e("lsz", "rawX=" + rawX);
                Log.e("lsz", "rawY=" + rawY);

                Log.e("lsz", "x=" + event.getX());
                Log.e("lsz", "y=" + event.getY());
                stopDrag();
                break;
        }
        return super.onTouchEvent(event);
    }

    int parentWidth;
    int parentHeight;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidth = getWidth();
        parentHeight = getHeight();

        Log.e("lsz", "parentWidth=" + parentWidth);
        Log.e("lsz", "parentHeight=" + parentHeight);

        Log.e("lsz", "getMeasuredWidth=" + getMeasuredWidth());
        Log.e("lsz", "getMeasuredHeight=" + getMeasuredHeight());
    }

    int rawX;
    int rawY;

    private void stopDrag() {

        windowManager.removeView(childView);
        childView = null;

        TextView textView = new TextView(mContext);
        textView.setTextSize(30);
        textView.setText("hahah");

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.width = 300;
        layoutParams.height = 80;
        layoutParams.topMargin = rawX;
        layoutParams.leftMargin = rawY;
        textView.setLayoutParams(layoutParams);
        addView(textView);
        childView = textView;
    }

    private void onDrag(int x, int y, int rawX, int rawY) {
        windowParams.x = rawX;
        windowParams.y = rawY;
        windowManager.updateViewLayout(childView, windowParams);
    }

    public void setOnItemClickListener(final MotionEvent event) {
        if (childView == null) {
            return;
        }
        childView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                mVibrator.vibrate(50);//设置震动时间
                startDrag(childView, (int) event.getRawX(), (int) event.getRawY());
                return false;
            }
        });
    }

    WindowManager windowManager;
    WindowManager.LayoutParams windowParams;

    public void startDrag(View dragBitmap, int x, int y) {
        Log.d("lsz", "startDrag=");
        windowParams = new WindowManager.LayoutParams();// 获取WINDOW界面的
        //Gravity.TOP|Gravity.LEFT;这个必须加
        windowParams.gravity = Gravity.TOP | Gravity.LEFT;
        //得到preview左上角相对于屏幕的坐标
        windowParams.x = x;
        windowParams.y = y;
        //设置拖拽item的宽和高
        windowParams.width = (int) (childView.getWidth());// 放大dragScale倍，可以设置拖动后的倍数
        windowParams.height = (int) (childView.getHeight());// 放大dragScale倍，可以设置拖动后的倍数
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        windowParams.format = PixelFormat.TRANSLUCENT;
        windowParams.windowAnimations = 0;
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);// "window"
        removeView(childView);
        windowManager.addView(childView, windowParams);
    }
}
