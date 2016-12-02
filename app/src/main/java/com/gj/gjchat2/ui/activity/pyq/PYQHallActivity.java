package com.gj.gjchat2.ui.activity.pyq;

import android.Manifest;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.entity.FriendNewsEntitiy;
import com.gj.gjchat2.model.PYQHallModel;
import com.gj.gjchat2.ui.widget.MyPictureSelection.LocalImageHelper;
import com.gj.gjchat2.ui.widget.PopupWindowUtil;
import com.gj.gjchat2.ui.widget.pyq.CommentAdapter;
import com.gj.gjchat2.ui.widget.pyq.GridViewAdapter;
import com.gj.gjchat2.ui.widget.pyq.NoScrollGridView;
import com.gj.gjchat2.ui.widget.pyq.NoScrollListView;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.adapter.CommonHeaderRecyclerAdapter;
import com.gj.gjlibrary.adapter.CommonHeaderRecyclerAdapterHelper;
import com.gj.gjlibrary.base.BaseAutoRecylerListActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.AbDateUtils;
import com.gj.gjlibrary.util.ImageLoaderHelper;
import com.gj.gjlibrary.util.PermissionGrantUtil;
import com.gj.gjlibrary.widget.LoadMoreRecyclerView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.lang.reflect.Field;
import java.util.List;

import butterknife.Bind;


public class PYQHallActivity extends BaseAutoRecylerListActivity {

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.recycler_view)
    LoadMoreRecyclerView recyclerView;

    private PYQHallModel model;

    private List<FriendNewsEntitiy.DataEntity.RowsEntity> rows;

    private CommonHeaderRecyclerAdapter<FriendNewsEntitiy.DataEntity.RowsEntity> adapter;

    private DisplayImageOptions options;

    private ImageView ivSxqMainHeader;
    private TextView tvMasterName;

    public String userId;

    public static final String TAG = "PYQHallActivity";
    private View mLayout;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_pyqhall;
    }

    @Override
    protected void initListener() {
        mLayout = findViewById(R.id.root_layout);
        userId = CacheUtil.getUserId(PYQHallActivity.this);
        setTitle("朋友圈");
        showTopRight();
        tvRight.setBackgroundResource(R.drawable.ic_sxq_camera);

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 赋权操作
                 */
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                    boolean isGrantPermission = PermissionGrantUtil.getInstance(PYQHallActivity.this,mLayout).checkPermissionActivity(Manifest.permission.READ_EXTERNAL_STORAGE,PermissionGrantUtil.READ_EXTERNAL_STORAGE);
                    if(isGrantPermission){
                        //本地图片辅助类初始化
                        LocalImageHelper.init(AppContext.getAppContext());
                        readyGo(AddNewsActivity.class);
                    }
                }else{
                    //本地图片辅助类初始化
                    LocalImageHelper.init(AppContext.getAppContext());
                    readyGo(AddNewsActivity.class);
                }

            }
        });

        options = ImageLoaderHelper.getInstance(PYQHallActivity.this).getDisplayOptions(4 ,getResources().getDrawable(R.drawable.ic_grzx_icon_morentouxiang));
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
    protected void getModelData() {
        if(null==model){
            model = new PYQHallModel(this);
        }
        model.getHallList(userId,String.valueOf(page),String.valueOf(pageSize));
    }

    @Override
    protected void initAdapter() {
        if(adapter==null){
            adapter = new CommonHeaderRecyclerAdapter<FriendNewsEntitiy.DataEntity.RowsEntity>(PYQHallActivity.this,rows,R.layout.item_sxq_friend_news) {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void convert(CommonHeaderRecyclerAdapterHelper helper, final FriendNewsEntitiy.DataEntity.RowsEntity rowsEntity) {
                    //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(TestActivity.this));
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
                        //if(gv_photo.gridViewAdapter==null||gv_photo.gridViewAdapter.imageList.containsAll(rowsEntity.imageList)){
                        gv_photo.gridViewAdapter = new GridViewAdapter(PYQHallActivity.this,rowsEntity.imageList);
                        //}
                        gv_photo.setAdapter(gv_photo.gridViewAdapter);
                    }else{
                        gv_photo.setVisibility(View.GONE);
                    }
                    String supportCount = rowsEntity.supportCount;
                    LinearLayout ll_zan = helper.getView(R.id.ll_zan);
                    final TextView rb_zan = helper.getView(R.id.rb_zan);
                    if(null!=supportCount&&!"".equals(supportCount)&&!"0".equals(supportCount)){
                        ll_zan.setVisibility(View.VISIBLE);
                        rb_zan.setText("赞(" + supportCount + ")");
                    }else{
                        ll_zan.setVisibility(View.GONE);
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
                                    model.addZan(userId,rowsEntity.id, new PYQHallModel.CallBackListener() {
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
                                    model.cancleZan(userId,rowsEntity.id, new PYQHallModel.CallBackListener() {
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
                    LinearLayout ll_pinglun = helper.getView(R.id.ll_pinglun);
                    final TextView rb_pinglun = helper.getView(R.id.rb_pinglun);
                    if(null!=commentCount&&!"".equals(commentCount)&&!"0".equals(commentCount)){
                        ll_pinglun.setVisibility(View.VISIBLE);
                        rb_pinglun.setText("评论("+commentCount+")");
                    }else{
                        ll_pinglun.setVisibility(View.GONE);
                        rb_pinglun.setText("评论");
                    }

                    rb_pinglun.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PopupWindowUtil popupWindowUtil = new PopupWindowUtil(PYQHallActivity.this,new PopupWindowUtil.CallbackListener(){

                                @Override
                                public void callback(String content) {  //PopupWindow的回调
                                    //提交评论
                                    model.addComment(userId,rowsEntity.id, content, "", new PYQHallModel.CallBackListener() {
                                        @Override
                                        public void callBack(boolean result,String json) { //评论的回调
                                            if(result){ //如果评论成功，刷新页面
                                                getModelData();
                                            }
                                        }
                                    });
                                }
                            });
                            popupWindowUtil.showPopupInput("");
                        }
                    });

                    List<FriendNewsEntitiy.DataEntity.RowsEntity.SupportListEntity> supportList = rowsEntity.supportList;
                    if(supportList!=null&&supportList.size()>0){
                        String supportNames="";
                        for (int i = 0; i < supportList.size(); i++) {
                            if(i==supportList.size()-1){
                                supportNames+=supportList.get(i).compellation;
                            }else{
                                supportNames+=supportList.get(i).compellation+"、";
                            }
                        }
                        helper.setText(R.id.tv_zan, supportNames);
                    }
                    final NoScrollListView lv_pinglun = helper.getView(R.id.lv_pinglun);
                    if(lv_pinglun!=null&&rowsEntity.commentList!=null&&rowsEntity.commentList.size()>0){
                        lv_pinglun.setVisibility(View.VISIBLE);
                        //if(lv_pinglun.commentAdapter==null||lv_pinglun.commentAdapter.commentList.containsAll(rowsEntity.commentList)){
                        lv_pinglun.commentAdapter = new CommentAdapter(PYQHallActivity.this, rowsEntity.commentList);
                        // final CommentAdapter commentAdapter = new CommentAdapter(PYQHallActivity.this, rowsEntity.commentList);
                        //}
                        lv_pinglun.setAdapter(lv_pinglun.commentAdapter);

                        lv_pinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {

                                String nickname = lv_pinglun.commentAdapter.commentList.get(position).compellation;

                                PopupWindowUtil popupWindowUtil = new PopupWindowUtil(PYQHallActivity.this,new PopupWindowUtil.CallbackListener(){

                                    @Override
                                    public void callback(String content) {  //PopupWindow的回调
                                        //提交评论
                                        String commentId = lv_pinglun.commentAdapter.commentList.get(position).id;
                                        String wqId = lv_pinglun.commentAdapter.commentList.get(position).wqId;
                                        model.addComment(userId,wqId, content, commentId, new PYQHallModel.CallBackListener() {
                                            @Override
                                            public void callBack(boolean result,String json) { //回复评论的回调
                                                if(result){ //如果回复评论成功，刷新页面
                                                    getModelData();
                                                }
                                            }
                                        });
                                    }
                                });
                                popupWindowUtil.showPopupInput("回复"+nickname +":");
                            }
                        });

                    }else{
                        lv_pinglun.setVisibility(View.GONE);
                    }
                }
            };
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    public void pressData(BaseEntity baseEntity) {
        refreshLayout.setRefreshing(false);
        FriendNewsEntitiy friendNewsEntitiy = (FriendNewsEntitiy) baseEntity;
        rows = friendNewsEntitiy.data.rows;
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

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionGrantUtil.READ_EXTERNAL_STORAGE) {
            if (PermissionGrantUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                Snackbar.make(mLayout, R.string.permision_available,
                        Snackbar.LENGTH_SHORT)
                        .show();
                //本地图片辅助类初始化
                LocalImageHelper.init(AppContext.getAppContext());
                readyGo(AddNewsActivity.class);
            } else {
                Log.i(TAG, "Contacts permissions were NOT granted.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
