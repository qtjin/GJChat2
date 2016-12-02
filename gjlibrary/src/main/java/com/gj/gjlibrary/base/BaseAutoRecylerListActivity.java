package com.gj.gjlibrary.base;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.gj.gjlibrary.widget.LoadMoreRecyclerView;

/**
 * Created by guojing on 15/11/6.
 * Activity的基础类
 */
public abstract class BaseAutoRecylerListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        LoadMoreRecyclerView.LoadMoreListener {

    /**
     * 定义加载的枚举类型
     */
    public enum LoadType {
        INIT, //第一次加载
        REFERSH, //刷新
        LOADMORE; //查看更多
    }

    public int page=1;
    public int pageSize=10;

    public LoadType loadType =  LoadType.INIT;

    protected abstract void getModelData()
            ;
    protected abstract void initAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initAdapter();
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onLoadMore() {
        loadType = LoadType.LOADMORE;
        page++;
        getModelData();
    }

    @Override
    public void onRefresh() {
        loadType = LoadType.REFERSH;
        page=1;
        getModelData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getModelData();
    }
}
