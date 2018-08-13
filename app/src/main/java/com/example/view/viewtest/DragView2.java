package com.example.view.viewtest;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class DragView2 extends FrameLayout implements View.OnClickListener {

    private ViewDragHelper mDragHelper;
    private FrameLayout guessing_root;
    private ImageView guessing_content_view;
    private ImageView guessing_close_view;
    private View mItemView;

    private Point point = new Point();

    public DragView2(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public DragView2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                int padingLeft = getPaddingLeft();
                int childMaxRight = getWidth() - mItemView.getWidth();
                int newLeft = Math.min(Math.max(left, padingLeft), childMaxRight);
                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                int padingTop = getPaddingTop();
                int childMaxBotton = getHeight() - mItemView.getHeight();
                int newTop = Math.min(Math.max(top, padingTop), childMaxBotton);
                return newTop;
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                point.x = releasedChild.getLeft();
                point.y = releasedChild.getTop();

            }
        });

        initView(context);

    }

    public DragView2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.item_guessing_drag, this);

        guessing_root = findViewById(R.id.guessing_root);
        guessing_close_view = (ImageView) findViewById(R.id.guessing_close_view);
        guessing_content_view = (ImageView) findViewById(R.id.guessing_content_view);

        mItemView = guessing_root;

        guessing_close_view.setOnClickListener(this);
        guessing_content_view.setOnClickListener(this);

        FrameLayout.LayoutParams guessing_rootLp = (LayoutParams) guessing_root.getLayoutParams();
        guessing_rootLp.height = 360;
        guessing_rootLp.width = 360;
        guessing_rootLp.gravity = Gravity.RIGHT | Gravity.BOTTOM;

        FrameLayout.LayoutParams guessing_close_viewLP = (LayoutParams) guessing_close_view.getLayoutParams();
        guessing_close_viewLP.width = 72;
        guessing_close_viewLP.height = 72;
        guessing_close_viewLP.topMargin = 36;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("lsz", "DragView2 onTouchEvent");
        mDragHelper.processTouchEvent(event);
        int touchX = (int) event.getX();
        int touchY = (int) event.getY();
        if (mItemView.getVisibility() == VISIBLE && touchX > mItemView.getLeft() && touchX < mItemView.getRight() && touchY > mItemView.getTop() && touchX < mItemView.getBottom()) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int touchX = (int) ev.getX();
        int touchY = (int) ev.getY();
        if (mItemView.getVisibility() == VISIBLE && touchX > mItemView.getLeft() && touchX < mItemView.getRight() && touchY > mItemView.getTop() && touchX < mItemView.getBottom()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        Log.e("lsz", "DragView2 onInterceptTouchEvent");
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("lsz", "DragView2 dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {

    }
}
