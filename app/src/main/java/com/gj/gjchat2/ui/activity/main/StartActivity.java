package com.gj.gjchat2.ui.activity.main;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.model.LoginModel;
import com.gj.gjchat2.network.Network;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.logger.AbLog;

import butterknife.Bind;

/**
 * Created by guojing on 2016/1/12.
 * 启动页
 */
public class StartActivity extends BaseActivity {

    @Bind(R.id.iv_bg)
     ImageView iv_bg;

     LoginModel loginModel;

    @Override
    protected void initData() {

        DisplayMetrics metric = new DisplayMetrics();
        StartActivity.this.getWindowManager().getDefaultDisplay()
                .getMetrics(metric);
        AppContext.SCREEN_WIDTH = metric.widthPixels; // 屏幕宽度（像素）
        AppContext.SCREEN_HEIGHT = metric.heightPixels; // 屏幕宽度（像素）
        AppContext.DENSITY = metric.density; // 像素密度

        String ip = CacheUtil.getIP(StartActivity.this);
        if(null!= ip){
            Network.setBaseURL(ip);
        }
        AbLog.i("ip: "+ip);
        AbLog.i("Network.getBaseURL(): "+Network.getBaseURL());
        if(loginModel==null){
            loginModel = new LoginModel(StartActivity.this);
        }
        if(CacheUtil.isLogin()&&!"".equals(ip)){
            loginModel.updData(); //自动登录
        }else{
            loginModel.isGoSplashActivity(); //判断是否是初次加载
        }

    }

    @Override
    public void pressData(BaseEntity baseEntity) {}

    @Override
    protected void initListener() {
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_start;
    }


}
