package com.linkheart.dragdrawrecyclerview.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;


import androidx.annotation.NonNull;


/**
 * <pre>
 *     author :
 *     e-mail : luyuan11233@163.com
 *     time   : 2021/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {
    /**
     * 测量惯性的间隔时间
     */
    private static final int SCROLL_TIMER = 50;
    /**
     * 滚动间隔时间
     */
    private static final int SCROLL_TIMER_AUTO = 20;
    /**
     * 惯性的上次坐标
     */
    private int lastX = 0;
    /**
     * 补全tab动画总时间
     */
    private final int animTime = 300;
    /**
     * 补全tab动画 滚动次数
     */
    private int timerCount = 0;
    /**
     * 补全tab动画 每次滚动距离
     */
    private int timerSpace;
    /**
     * 每个item的宽度 数量
     */
    private int tabWidth = 60;
    private int tabCount = 8;
    /**
     * MotionEvent.ACTION_DOWN 取消惯性和自动补全tab事件
     */
    private boolean isPress = false;
    private OnCustomScrollChangeListener listener;
    /**
     * 监测惯性结束
     */
    private static final int MEASURE_SCROLL_WHAT = 111;
    /**
     * 滚动标识
     */
    private static final int INERTIA_SCROLL_WHAT = 112;
    private Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MEASURE_SCROLL_WHAT:
                    if(isPress) return;
                    int scrollX = getScrollX();
                    if (lastX == scrollX) {
                        lastX = 0;
                        startAnimAction();
                    } else {
                        lastX = scrollX;
                        handler.sendEmptyMessageDelayed(MEASURE_SCROLL_WHAT, SCROLL_TIMER);
                    }
                    break;
                case INERTIA_SCROLL_WHAT:
                    if (listener == null || isPress) return;
                    timerCount++;
                    if (timerCount >= animTime / SCROLL_TIMER_AUTO) {
                        timerCount = 0;
                    }
                    listener.onCustomScrollChange(CustomHorizontalScrollView.this, getScrollX() + timerSpace, 0, 0, 0);
                    if (timerCount != 0) {
                        handler.sendEmptyMessageDelayed(INERTIA_SCROLL_WHAT, SCROLL_TIMER_AUTO);
                    }
                    break;
            }
        }
    };

    public CustomHorizontalScrollView(Context context) {
        this(context, null);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * dip转换px
     */
    private int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale);
    }
    /**
     * 开启滚动动画
     */
    private void startAnimAction() {
        int rightLayoutWidth = getWidth();
        int rightTotalWidth = dip2px(tabWidth) * tabCount;
        int rightItemPx = dip2px(tabWidth);
        int mMoveOffsetX = getScrollX();
        /**
         * 补全tab动画 需要滚动的总距离
         */
        int moreScroll;
        if (mMoveOffsetX % rightItemPx >= rightItemPx / 2) {
            moreScroll = rightItemPx - mMoveOffsetX % rightItemPx;
            //当滑动大于最大宽度时，不在滑动（右边到头了）
            if ((rightLayoutWidth + mMoveOffsetX + moreScroll) >= rightTotalWidth) {
                moreScroll = rightTotalWidth - rightLayoutWidth - mMoveOffsetX;
            }
        } else {
            moreScroll = -mMoveOffsetX % rightItemPx;
        }
        timerSpace = moreScroll / (animTime / SCROLL_TIMER_AUTO);
        handler.sendEmptyMessage(INERTIA_SCROLL_WHAT);
    }

    public void setOnCustomScrollChangeListener(OnCustomScrollChangeListener listener) {
        this.listener = listener;
    }
    public void setTabWidth(int width){
        this.tabWidth = width;
    }

    public void setTabCount(int tabCount) {
        this.tabCount = tabCount;
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPress = true;
                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessage(MEASURE_SCROLL_WHAT);
                isPress = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != listener)
            listener.onCustomScrollChange(CustomHorizontalScrollView.this, l, t, oldl, oldt);
    }

    /**
     * 监听滑动
     */
    public interface OnCustomScrollChangeListener {
        void onCustomScrollChange(CustomHorizontalScrollView listener, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

}
