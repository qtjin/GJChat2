package com.gj.gjchat2.ui.activity.pyq;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.entity.MyNewsEntitiy;
import com.gj.gjchat2.model.MyNewsModel;
import com.gj.gjchat2.ui.widget.pyq.MyNewsGridViewAdapter;
import com.gj.gjchat2.ui.widget.pyq.NoScrollGridView;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.adapter.CommonHeaderRecyclerAdapter;
import com.gj.gjlibrary.adapter.CommonHeaderRecyclerAdapterHelper;
import com.gj.gjlibrary.base.BaseAutoRecylerListActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.AbDateUtils;
import com.gj.gjlibrary.util.ImageLoaderHelper;
import com.gj.gjlibrary.util.logger.AbLog;
import com.gj.gjlibrary.widget.LoadMoreRecyclerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;



/**
 * Created by guojing on 2016/3/18.
 * 我的动态列表
 */
public class MyNewsActivity extends BaseAutoRecylerListActivity {

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.recycler_view)
    LoadMoreRecyclerView recyclerView;

    private MyNewsModel myNewsModel;
    List<MyNewsEntitiy.DataEntity.RowsEntity> rows;

    private CommonHeaderRecyclerAdapter<MyNewsEntitiy.DataEntity.RowsEntity> adapter;

    private DisplayImageOptions options;

    private ImageView ivSxqMainHeader;
    private TextView tvMasterName;

    public String userId;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_sxq_my_news;
    }

    @Override
    protected void initListener() {
        userId = CacheUtil.getUserId(MyNewsActivity.this);

        options = ImageLoaderHelper.getInstance(MyNewsActivity.this).getDisplayOptions(4 ,getResources().getDrawable(R.drawable.ic_grzx_icon_morentouxiang));
        pageSize=10;

        recyclerView.setPageSize(pageSize);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLoadMoreListener(this);

        recyclerView.setAdapter(adapter);

        recyclerView.setHeaderEnable(true);
        recyclerView.addHeaderView(R.layout.listview_top, new LoadMoreRecyclerView.HeaderViewCallback() {
            @Override
            public void getHeaderView(View mHeaderView) {
                if(null!=mHeaderView){
                    initHeadViewData(mHeaderView);
                }
            }
        });

        adapter.setOnItemClickListener(new CommonHeaderRecyclerAdapter.OnItemClickListener() {
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
    protected void initData() {
        setTitle("我的分享");
        showTopRight();
        tvRight.setText("消息");

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(MyNewsMsgActivity.class);
            }
        });
    }


    @Override
    public void pressData(BaseEntity baseEntity) {
        refreshLayout.setRefreshing(false);
        MyNewsEntitiy myNewsEntitiy = (MyNewsEntitiy) baseEntity;
        rows = myNewsEntitiy.data.rows;
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
    protected void getBundleExtras(Bundle extras) {

    }




    @Override
    protected void getModelData() {
        if(myNewsModel==null){
            myNewsModel = new MyNewsModel(MyNewsActivity.this);
        }
        myNewsModel.getMyNewsList(userId,String.valueOf(page),String.valueOf(pageSize));
    }

    @Override
    protected void initAdapter(){
        if(adapter==null){
            adapter = new CommonHeaderRecyclerAdapter<MyNewsEntitiy.DataEntity.RowsEntity>(MyNewsActivity.this,rows,R.layout.item_sxq_my_news) {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void convert(CommonHeaderRecyclerAdapterHelper helper, final MyNewsEntitiy.DataEntity.RowsEntity rowsEntity) {
                    //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(TestActivity.this));
                    helper.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             AbLog.i("helper.getConvertView().setOnClickListene...");
                            Bundle bundle = new Bundle();
                            bundle.putString("wqId", rowsEntity.id);
                            readyGo(MyNewsDetailActivity.class,bundle);
                        }
                    });

                    ImageView iv_userhead = helper.getView(R.id.iv_userhead);
                    String imgUrl = rowsEntity.image;
                    if(null!=imgUrl&&!"".equals(imgUrl)){
                        helper.setImageUrl(R.id.iv_userhead,imgUrl,options);
                    }

                    helper.setText(R.id.tv_nickname,rowsEntity.compellation);

                    long sendTime = Long.parseLong(rowsEntity.createTime)*1000;
                    String sendTimeStr = AbDateUtils.convertMillis2DateStr(sendTime, "yyyy-MM-dd");
                    helper.setText(R.id.tv_time, sendTimeStr);

                    TextView tv_content = helper.getView(R.id.tv_content);
                    String content = rowsEntity.content;
                    if(null!=content&&!"".equals(content)){
                        tv_content.setVisibility(View.VISIBLE);
                        tv_content.setText(content);
                    }else{
                        tv_content.setVisibility(View.GONE);
                    }

                    NoScrollGridView gv_photo = helper.getView(R.id.gv_photo);
                    if(gv_photo!=null&&rowsEntity.imageList!=null&&rowsEntity.imageList.size()>0){
                        gv_photo.setVisibility(View.VISIBLE);
                        gv_photo.myNewsGridViewAdapter = new MyNewsGridViewAdapter(MyNewsActivity.this,rowsEntity.imageList);
                        gv_photo.setAdapter(gv_photo.myNewsGridViewAdapter);
                    }else{
                        gv_photo.setVisibility(View.GONE);
                    }
                    String supportCount = rowsEntity.supportCount;
                    final TextView rb_zan = helper.getView(R.id.rb_zan);
                    if(null!=supportCount&&!"".equals(supportCount)&&!"0".equals(supportCount)){
                        rb_zan.setText("赞(" + supportCount + ")");
                    }else{
                        rb_zan.setText("赞");
                    }

                    switch (rowsEntity.isSupport){ //是否已点赞;1已点赞;0未点赞
                        case 0:
                            rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan, 0, 0, 0);
                            break;
                        case 1:
                            rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan_light, 0, 0, 0);
                            break;
                    }

                    rb_zan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (rowsEntity.isSupport){ //是否已点赞;1已点赞;0未点赞
                                case 0:
                                    //点赞
                                    myNewsModel.addZan(userId,rowsEntity.id, new MyNewsModel.CallBackListener() {
                                        @Override
                                        public void callBack(boolean result,String json) {
                                            if (!result) { //点赞失败
                                                rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan, 0, 0, 0);
                                            } else {
                                                rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan_light, 0, 0, 0);
                                                getModelData();
                                            }
                                        }
                                    });
                                    break;
                                case 1:
                                    //取消赞
                                    myNewsModel.cancleZan(userId,rowsEntity.id, new MyNewsModel.CallBackListener() {
                                        @Override
                                        public void callBack(boolean result,String json) {
                                            if (!result) { //取消赞失败
                                                rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan_light, 0, 0, 0);
                                            } else {
                                                rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan, 0, 0, 0);
                                                getModelData();
                                            }
                                        }
                                    });
                                    break;
                            }
                        }
                    });


                    String commentCount = rowsEntity.commentCount;
                    TextView rb_pinglun = helper.getView(R.id.rb_pinglun);
                    if(null!=commentCount&&!"".equals(commentCount)&&!"0".equals(commentCount)){
                        rb_pinglun.setText("评论("+commentCount+")");
                    }else{
                        rb_pinglun.setText("评论");
                    }
                }

            };
        }
    }


    public void initHeadViewData(View mHeaderView){
        ivSxqMainHeader = (ImageView) mHeaderView.findViewById(R.id.iv_sxq_main_header);
        tvMasterName = (TextView) mHeaderView.findViewById(R.id.tv_master_name);

        ivSxqMainHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(MyNewsActivity.class);
            }
        });
        tvMasterName.setText(CacheUtil.getNickname(this));

        Class drawable = com.hyphenate.easeui.R.drawable.class;
        String userHead = CacheUtil.getUserHeadImageUri(this);
        if(!TextUtils.isEmpty(userHead)){
            Field field = null;
            try {
                field = drawable.getField(userHead);
                int r_id = field.getInt(field.getName());
                ivSxqMainHeader.setImageResource(r_id);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
