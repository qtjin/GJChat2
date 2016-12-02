package com.gj.gjchat2.ui.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;

import butterknife.Bind;

/**
 * Created by guojing on 2016/10/17.
 */
public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webView)
     WebView webView;

    private String urlStr;

    @Override
    protected void getBundleExtras(Bundle extras) {
        urlStr = extras.getString("url");
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initListener() {
        //设置编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        //支持js
        webView.getSettings().setJavaScriptEnabled(true);
        //设置监听事件
        webView.setWebViewClient(new JieWewViewClient());
        //设置本地调用对象及其接口
        webView.addJavascriptInterface(new JavaScriptObject(getApplicationContext()), "toAndroid");
        //载入js
        if(null!=urlStr){
            webView.loadUrl(urlStr);
        }
        //webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }


    public void javaCallJsNoParams(View view){
        webView.loadUrl("javascript:javaCallJsNoParamsMethod()");
    }

    public void javaCallJsHasParams(View view){
        webView.loadUrl("javascript:javaCallJsHasParamsMethod('" + 123 + "')");
    }

    public class JavaScriptObject {

        Context mContxt;

        public JavaScriptObject(Context mContxt) {
            this.mContxt = mContxt;
        }

        @JavascriptInterface //sdk17版本以上加上注解
        public void jsCallJavaNoParams() {
            Toast.makeText(mContxt, "Js调用Java方法(无参)", Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface //sdk17版本以上加上注解
        public void jsCallJavaHasParams(String params) {
            Toast.makeText(mContxt, "Js调用Java方法(有参):" + params, Toast.LENGTH_SHORT).show();
        }
    }

    class JieWewViewClient extends WebViewClient {
        /**
         *  如果紧跟着
         *  webView.loadUrl("file:///android_asset/index.html");
         *  调用Js中的方法是不起作用的，必须页面加载完成才可以
         */

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webView.loadUrl("javascript:javaCallJsHasParamsMethod('" + 123 + "')");
        }
    }

}
