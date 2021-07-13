package com.linkheart.dragdrawrecyclerview.adapter;

import android.view.View;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.linkheart.dragdrawrecyclerview.R;
import com.linkheart.dragdrawrecyclerview.bean.CoinInfoBean;
import com.linkheart.dragdrawrecyclerview.view.CustomHorizontalScrollView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author :
 *     e-mail : luyuan11233@163.com
 *     time   : 2021/07/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class DragMarketAdapter extends BaseQuickAdapter<CoinInfoBean, BaseViewHolder> {
    /**
     * item右侧Scrollview集合，需要联动滑动
     */
    private final List<CustomHorizontalScrollView> scrollViewList = new ArrayList<>();
    /**
     * scrollViewList是否已经在滑动，两次滑动冲突，避免再次滑动
     */
    private volatile boolean scrollState = false;
    /**
     * 初始化需要滑动的距离
     */
    private int needScrollOffsetX = 0;

    private OnContentScrollListener onContentScrollListener;

    public interface OnContentScrollListener {
        void onScroll(int offsetX);
    }

    public void setOnContentScrollListener(OnContentScrollListener onContentScrollListener) {
        this.onContentScrollListener = onContentScrollListener;
    }
    public List<CustomHorizontalScrollView> getContentScrollViewList(){
        return scrollViewList;
    }

    public void setScrollState(boolean scrollState){
        this.scrollState = scrollState;
    }
    public boolean getScrollState(){
        return scrollState;
    }
    public int getNeedScrollOffsetX(){
        return  needScrollOffsetX;
    }
    public void setNeedScrollOffsetX(int scrollOffsetX){
        this.needScrollOffsetX = scrollOffsetX;
    }

    public DragMarketAdapter(int layoutResId, @Nullable List data) {
        super(layoutResId, data);
    }

    @Override
    protected BaseViewHolder createBaseViewHolder(@NotNull View view) {
        BaseViewHolder holder = new BaseViewHolder(view);
        //获取可滑动的view布局  这一句才是核心代码，把要滑动的view放到mMoveViewList里
        CustomHorizontalScrollView  customHorizontalScrollView = holder.getView(R.id.hor_item_scrollview);

        customHorizontalScrollView.setOnCustomScrollChangeListener(new CustomHorizontalScrollView.OnCustomScrollChangeListener() {
            @Override
            public void onCustomScrollChange(CustomHorizontalScrollView listener, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollState){
                    return;
                }
                scrollState = true;
                for (int i = 0; i < scrollViewList.size(); i++) {
                    CustomHorizontalScrollView tempScrollView = scrollViewList.get(i);
//                    if (customHorizontalScrollView != tempScrollView) {
                        tempScrollView.scrollTo(scrollX, 0);
//                    }
                }
                //记录滑动距离,便于处理下拉刷新之后的还原操作
                if (null != onContentScrollListener) onContentScrollListener.onScroll(scrollX);
                needScrollOffsetX = scrollX;
                scrollState = false;
            }
        });
        customHorizontalScrollView.scrollTo(needScrollOffsetX,0);
        scrollViewList.add(customHorizontalScrollView);
        return super.createBaseViewHolder(view);

    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, CoinInfoBean data) {
        holder.setText(R.id.id_name, data.name)
                .setText(R.id.id_tv_price_last, data.priceLast)
                .setText(R.id.id_tv_rise_rate24, data.riseRate24)
                .setText(R.id.id_tv_vol24, data.vol24)
                .setText(R.id.id_tv_close, data.close)
                .setText(R.id.id_tv_open, data.open)
                .setText(R.id.id_tv_bid, data.bid)
                .setText(R.id.id_tv_ask, data.ask)
                .setText(R.id.id_tv_percent, data.amountPercent);

    }
}
