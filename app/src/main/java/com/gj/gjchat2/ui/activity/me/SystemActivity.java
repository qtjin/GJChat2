package com.gj.gjchat2.ui.activity.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.AbAppManager;
import com.gj.gjlibrary.util.AppUtils;
import com.hyphenate.easeui.utils.HuanxinEaseUserListCacheUtil;
import com.hyphenate.easeui.utils.HuanxinUserInfoCacheUtil;

import butterknife.Bind;


/**
 * Created by guojing on 2016/6/16 0016.
 */

public class SystemActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_password) TextView tv_password;

    @Bind(R.id.tv_about) TextView tv_about;

    @Bind(R.id.tv_exit) TextView tv_exit;

    @Bind(R.id.tv_cache) TextView tv_cache;

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_system_setting;
    }

    @Override
    protected void initData() {
        //获得系统缓存
        try {
            tv_cache.setText(AppUtils.getTotalCacheSize(this));
        } catch (Exception e) {
            tv_cache.setText("0 MB");
            e.printStackTrace();
        }
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void initListener() {
        setTitle("系统设置");
        tv_password.setOnClickListener(this);
        tv_cache.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        tv_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.tv_password):
                //readyGo(EditPasswordActivity.class);
                break;
            case (R.id.tv_cache):
                //清理缓存
                AppUtils.clearAllCache(this);
                Toast.makeText(this, "缓存清理成功", Toast.LENGTH_LONG).show();
                try {
                    tv_cache.setText(AppUtils.getTotalCacheSize(this));
                } catch (Exception e) {
                    tv_cache.setText("0 MB");
                    e.printStackTrace();
                }
                break;
            case (R.id.tv_about):
                //readyGo(AboutActivity.class);
                break;
            case (R.id.tv_exit):
                //清除环信联系人头像昵称的缓存
                HuanxinEaseUserListCacheUtil.clearHuanxinEaseUserListCacheEntity(SystemActivity.this);
                HuanxinUserInfoCacheUtil.clearHuanxinUserInfoCacheEntity(SystemActivity.this);
                AppContext.getAppContext().logout();
                readyGo(LoginActivity.class);
                AbAppManager.getAbAppManager().finishAllActivity();
                break;

        }
    }
}
