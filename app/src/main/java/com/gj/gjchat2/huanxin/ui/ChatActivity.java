package com.gj.gjchat2.huanxin.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.ui.activity.main.MainActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.hyphenate.util.EasyUtils;
import com.gj.gjlibrary.base.BaseActivity;


import butterknife.Bind;

/**
 * 聊天页面，需要fragment的使用
 *
 */
public class ChatActivity extends BaseActivity {

    @Bind(R.id.top)
     View top;

    public static ChatActivity activityInstance;
    private ChatFragment chatFragment;
    public String toChatUsername;
    Bundle bundle;

    @Override
    protected void initData() {
        activityInstance = this;
        //可以直接new EaseChatFratFragment使用
        chatFragment = new ChatFragment();
        //传入参数
        chatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        activityInstance = null;
        super.onDestroy();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        bundle = extras;
        //聊天人或群id
        toChatUsername = extras.getString(Constant.EXTRA_USER_ID);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.em_activity_chat;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra(Constant.EXTRA_USER_ID);
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void initListener() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            if (top != null)
                top.setVisibility(View.GONE);
        }
    }
}
