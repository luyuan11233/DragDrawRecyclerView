package com.linkheart.dragdrawrecyclerview;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.linkheart.dragdrawrecyclerview.adapter.DragMarketAdapter;
import com.linkheart.dragdrawrecyclerview.bean.CoinInfoBean;
import com.linkheart.dragdrawrecyclerview.view.CustomHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements DragMarketAdapter.OnContentScrollListener{

    RecyclerView recyclerView;
    CustomHorizontalScrollView tabScrollView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_market_new);
        initView();
        initData();
    }

    private void initData() {
        List<CoinInfoBean> mDataModels = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            CoinInfoBean coinInfo = new CoinInfoBean();
            coinInfo.name = "USDT";
            coinInfo.priceLast = "20.0";
            coinInfo.riseRate24 = "0.2";
            coinInfo.vol24 = "10020";
            coinInfo.close = "22.2";
            coinInfo.open = "40.0";
            coinInfo.bid = "33.2";
            coinInfo.ask = "19.0";
            coinInfo.amountPercent = "33.3%";
            mDataModels.add(coinInfo);
        }
        adapter.setNewInstance(mDataModels);
    }
    private DragMarketAdapter adapter;
    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
        tabScrollView = findViewById(R.id.hor_item_scrollview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        adapter = new DragMarketAdapter(R.layout.item_drag_market, null);
        adapter.addChildClickViewIds(R.id.id_name,R.id.id_tv_price_last,R.id.id_tv_rise_rate24,R.id.id_tv_vol24,R.id.id_tv_close,R.id.id_tv_open,R.id.id_tv_bid,R.id.id_tv_ask,R.id.id_tv_percent);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                Toast.makeText(MainActivity.this,"click->"+position,Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Toast.makeText(MainActivity.this,"click->"+position,Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.setOnContentScrollListener(this);
        tabScrollView.setOnCustomScrollChangeListener((listener, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (adapter.getScrollState()) {
                return;
            }
            adapter.setScrollState(true);
            List<CustomHorizontalScrollView> viewHolderCacheList = adapter.getContentScrollViewList();
            adapter.setNeedScrollOffsetX(scrollX);
            tabScrollView.scrollTo(scrollX, 0);
            if (null != viewHolderCacheList) {
                int size = viewHolderCacheList.size();
                for (int i = 0; i < size; i++) {
                    viewHolderCacheList.get(i).scrollTo(scrollX, 0);
                }
            }
            adapter.setScrollState(false);
        });
    }

    @Override
    public void onScroll(int offsetX) {
        //处理单个item滚动时,顶部tab需要联动
        if (null != tabScrollView) tabScrollView.scrollTo(offsetX, 0);
    }
}