package com.gj.gjchat2.ui.activity.pyq;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.entity.MyNewsMsgEntitiy;
import com.gj.gjchat2.model.MyNewsMsgModel;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.adapter.CommonRecyclerAdapter;
import com.gj.gjlibrary.adapter.CommonRecyclerAdapterHelper;
import com.gj.gjlibrary.base.BaseAutoRecylerListActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.AbDateUtils;
import com.gj.gjlibrary.widget.LoadMoreRecyclerView;

import java.util.List;

import butterknife.Bind;


/**
 * Created by guojing on 2016/3/24.
 * 动态消息
 */
public class MyNewsMsgActivity extends BaseAutoRecylerListActivity {

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.recycler_view)
    LoadMoreRecyclerView recyclerView;

    private List<MyNewsMsgEntitiy.DataEntity.RowsEntity> rows;

    private CommonRecyclerAdapter<MyNewsMsgEntitiy.DataEntity.RowsEntity> adapter;

    private MyNewsMsgModel myNewsMsgModel;

    private String userId;

    @Override
    protected void initData() {
        setTitle("动态消息");
    }

    @Override
    public void pressData(BaseEntity baseEntity) {
        refreshLayout.setRefreshing(false);
        MyNewsMsgEntitiy myNewsMsgEntitiy = (MyNewsMsgEntitiy) baseEntity;
        rows = myNewsMsgEntitiy.data.rows;
        if(null!=rows){
            if(loadType== LoadType.REFERSH){
                adapter.replaceAll(rows);
            }else if(loadType==LoadType.LOADMORE){
                adapter.addAll(rows);
            }else{
                adapter.replaceAll(rows);
            }
            recyclerView.setResultSize(rows.size());
        }
    }

    @Override
    protected void initListener() {
        userId = CacheUtil.getUserId(MyNewsMsgActivity.this);
        pageSize = 10;
        recyclerView.setPageSize(pageSize);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLoadMoreListener(this);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                //bLog.i("position: "+position);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });

    }

    @Override
    protected void getModelData() {
        if(myNewsMsgModel==null){
            myNewsMsgModel = new MyNewsMsgModel(this);
        }
        myNewsMsgModel.getMyNewsMessageList(userId,String.valueOf(page),String.valueOf(pageSize));
    }

    @Override
    protected void initAdapter() {
        if(adapter==null){
            adapter = new CommonRecyclerAdapter<MyNewsMsgEntitiy.DataEntity.RowsEntity>(MyNewsMsgActivity.this,rows, R.layout.item_sxq_message) {
                @Override
                public void convert(CommonRecyclerAdapterHelper helper, final MyNewsMsgEntitiy.DataEntity.RowsEntity rowsEntity) {
                    //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(TestActivity.this));
                    ImageView iv_userhead = helper.getView(R.id.iv_userhead);
                    if(null!=rowsEntity.image&&!"".equals(rowsEntity.image)){
                        helper.setImageUrl(R.id.iv_userhead,rowsEntity.image);
                    }
                    helper.setText(R.id.tv_nickname,rowsEntity.compellation);
                    switch (rowsEntity.type){
                        case "1":   //点赞
                            helper.setText(R.id.tv_type,"赞了我的动态");
                            break;
                        case "2":   //评论
                            helper.setText(R.id.tv_type,rowsEntity.content);
                            break;
                    }
                    long sendTime = Long.parseLong(rowsEntity.createTime)*1000;
                    String sendTimeStr = AbDateUtils.convertMillis2DateStr(sendTime, "yyyy-MM-dd");
                    helper.setText(R.id.tv_time, sendTimeStr);
                    TextView tv_content = helper.getView(R.id.tv_content);
                    ImageView iv_content_img = helper.getView(R.id.iv_content_img);
                    if(null!=rowsEntity.image&&!"".equals(rowsEntity.image)){
                        tv_content.setVisibility(View.GONE);
                        iv_content_img.setVisibility(View.VISIBLE);
                        helper.setImageUrl(R.id.iv_content_img, rowsEntity.image);
                    }else if(null!=rowsEntity.wqContent&&!"".equals(rowsEntity.wqContent)){
                        tv_content.setVisibility(View.VISIBLE);
                        iv_content_img.setVisibility(View.GONE);
                        helper.setText(R.id.tv_content, rowsEntity.wqContent);
                    }else{
                        tv_content.setVisibility(View.GONE);
                        iv_content_img.setVisibility(View.GONE);
                    }
                }
            };
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_sxq_my_news_msg;
    }
}
