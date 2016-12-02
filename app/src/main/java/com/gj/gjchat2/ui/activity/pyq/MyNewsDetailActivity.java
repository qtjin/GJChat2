package com.gj.gjchat2.ui.activity.pyq;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.entity.MyNewsDetailEntitiy;
import com.gj.gjchat2.model.MyNewsDetailModel;
import com.gj.gjchat2.model.MyNewsModel;
import com.gj.gjchat2.ui.widget.PopupWindowUtil;
import com.gj.gjchat2.ui.widget.pyq.MyNewsDetailCommentAdapter;
import com.gj.gjchat2.ui.widget.pyq.MyNewsDetailGridViewAdapter;
import com.gj.gjchat2.ui.widget.pyq.NoScrollGridView;
import com.gj.gjchat2.ui.widget.pyq.NoScrollListView;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.AbDateUtils;
import com.gj.gjlibrary.util.ImageLoaderHelper;
import com.gj.gjlibrary.util.logger.AbLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.Bind;


/**
 * Created by guojing on 2016/3/18.
 * 我的动态列表详情
 */
public class MyNewsDetailActivity extends BaseActivity {

    @Bind(R.id.iv_userhead)
     ImageView iv_userhead;

    @Bind(R.id.tv_nickname)
     TextView tv_nickname;

    @Bind(R.id.tv_time)
     TextView tv_time;

    @Bind(R.id.tv_content)
     TextView tv_content;

    @Bind(R.id.gv_photo)
     NoScrollGridView gv_photo;

    @Bind(R.id.rb_zan)
    public TextView rb_zan;

    @Bind(R.id.rb_pinglun)
     TextView rb_pinglun;

    @Bind(R.id.ll_zan)
     LinearLayout ll_zan;

    @Bind(R.id.ll_pinglun)
     LinearLayout ll_pinglun;

    @Bind(R.id.tv_zan)
     TextView tv_zan;

    @Bind(R.id.lv_pinglun)
     NoScrollListView lv_pinglun ;

    private MyNewsDetailModel model;

    private DisplayImageOptions options;

    public String wqId; //动态id

    public String userId;

    @Override
    protected void initData() {
        setTitle("详情");
        userId = CacheUtil.getUserId(MyNewsDetailActivity.this);
        if(model==null){
            model = new MyNewsDetailModel(MyNewsDetailActivity.this);
        }
        model.getMyNewsDetail(CacheUtil.getUserId(this), wqId);
    }

    @Override
    protected void initListener() {
        options = ImageLoaderHelper.getInstance(MyNewsDetailActivity.this).getDisplayOptions(4 ,getResources().getDrawable(R.drawable.ic_grzx_icon_morentouxiang));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void pressData(BaseEntity baseEntity) {
        MyNewsDetailEntitiy myNewsDetailEntitiy = (MyNewsDetailEntitiy) baseEntity;
        final MyNewsDetailEntitiy.DataEntity dataEntity = myNewsDetailEntitiy.data;

        String imgUrl = dataEntity.image;
        if(null!=imgUrl&&!"".equals(imgUrl)){
            ImageLoader.getInstance().displayImage(imgUrl, iv_userhead, options);
        }
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname.setText(dataEntity.compellation);

        long sendTime = Long.parseLong(dataEntity.createTime)*1000;
        String sendTimeStr = AbDateUtils.convertMillis2DateStr(sendTime, "yyyy-MM-dd");
        tv_time.setText(sendTimeStr);

        String content = dataEntity.content;
        if(null!=content&&!"".equals(content)){
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(content);
        }else{
            tv_content.setVisibility(View.GONE);
        }

        if(gv_photo!=null&&dataEntity.imageList!=null&&dataEntity.imageList.size()>0){
            gv_photo.setVisibility(View.VISIBLE);
            gv_photo.myNewsDetailGridViewAdapter = new MyNewsDetailGridViewAdapter(MyNewsDetailActivity.this,dataEntity.imageList);
            gv_photo.setAdapter(gv_photo.myNewsDetailGridViewAdapter);
        }else{
            gv_photo.setVisibility(View.GONE);
        }
        String supportCount = dataEntity.supportCount;
        if(null!=supportCount&&!"".equals(supportCount)&&!"0".equals(supportCount)){
            rb_zan.setText("赞(" + supportCount + ")");
            ll_zan.setVisibility(View.VISIBLE);
        }else{
            rb_zan.setText("赞");
            ll_zan.setVisibility(View.GONE);
        }

        switch (dataEntity.isSupport){ //是否已点赞;1已点赞;0未点赞
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
                switch (dataEntity.isSupport){ //是否已点赞;1已点赞;0未点赞
                    case 0:
                        //点赞
                        model.addZan(userId, dataEntity.id, new MyNewsModel.CallBackListener() {
                            @Override
                            public void callBack(boolean result,String json) {
                                if (!result) { //点赞失败
                                    rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan, 0, 0, 0);
                                } else {
                                    rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan_light, 0, 0, 0);
                                    model.getMyNewsDetail(userId, dataEntity.id);
                                }
                            }
                        });
                        break;
                    case 1:
                        //取消赞
                        model.cancleZan(userId,dataEntity.id, new MyNewsModel.CallBackListener() {
                            @Override
                            public void callBack(boolean result,String json) {
                                if (!result) { //取消赞失败
                                    rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan_light, 0, 0, 0);
                                } else {
                                    rb_zan.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_sxq_zan, 0, 0, 0);
                                    model.getMyNewsDetail(userId, dataEntity.id);
                                }
                            }
                        });
                        break;
                }
            }
        });


        String commentCount = dataEntity.commentCount;
        if(null!=commentCount&&!"".equals(commentCount)&&!"0".equals(commentCount)){
            rb_pinglun.setText("评论(" + commentCount + ")");
            ll_pinglun.setVisibility(View.VISIBLE);
        }else{
            rb_pinglun.setText("评论");
            ll_pinglun.setVisibility(View.GONE);
        }

        rb_pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindowUtil popupWindowUtil = new PopupWindowUtil(MyNewsDetailActivity.this,new PopupWindowUtil.CallbackListener(){

                    @Override
                    public void callback(String content) {  //PopupWindow的回调
                        //提交评论
                        model.addComment(userId,dataEntity.id, content, "", new MyNewsModel.CallBackListener() {
                            @Override
                            public void callBack(boolean result,String json) { //评论的回调
                                if(result){ //如果评论成功，刷新页面
                                    model.getMyNewsDetail(userId, dataEntity.id);
                                }
                            }
                        });
                    }
                });
                popupWindowUtil.showPopupInput("");
            }
        });

        List<MyNewsDetailEntitiy.DataEntity.SupportListEntity> supportList = dataEntity.supportList;
        if(supportList!=null&&supportList.size()>0){
            String supportNames="";
            for (int i = 0; i < supportList.size(); i++) {
                if(i==supportList.size()-1){
                    supportNames+=supportList.get(i).compellation;
                }else{
                    supportNames+=supportList.get(i).compellation+"、";
                }
            }
            tv_zan.setText(supportNames);
        }
        if(lv_pinglun!=null&&dataEntity.commentList!=null&&dataEntity.commentList.size()>0){
            lv_pinglun.setVisibility(View.VISIBLE);
            lv_pinglun.myNewsDetailCommentAdapter = new MyNewsDetailCommentAdapter(MyNewsDetailActivity.this, dataEntity.commentList);
            lv_pinglun.setAdapter(lv_pinglun.myNewsDetailCommentAdapter);

            lv_pinglun.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    String nickname = lv_pinglun.myNewsDetailCommentAdapter.commentList.get(position).compellation;

                    PopupWindowUtil popupWindowUtil = new PopupWindowUtil(MyNewsDetailActivity.this, new PopupWindowUtil.CallbackListener() {

                        @Override
                        public void callback(String content) {  //PopupWindow的回调
                            //提交评论
                            String commentId = lv_pinglun.myNewsDetailCommentAdapter.commentList.get(position).id;
                            String wqId = lv_pinglun.myNewsDetailCommentAdapter.commentList.get(position).wqId;
                            model.addComment(userId,wqId, content, commentId, new MyNewsModel.CallBackListener() {
                                @Override
                                public void callBack(boolean result,String json) { //回复评论的回调
                                    if (result) { //如果回复评论成功，刷新页面
                                        model.getMyNewsDetail(userId, dataEntity.id);
                                    }
                                }
                            });
                        }
                    });
                    popupWindowUtil.showPopupInput("回复" + nickname +":");
                }
            });

        }else{
            lv_pinglun.setVisibility(View.GONE);
        }

    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        wqId = extras.getString("wqId");
        AbLog.i("wqId: " + wqId);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_sxq_my_news_detail;
    }


}
