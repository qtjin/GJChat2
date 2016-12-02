package com.gj.gjchat2.util.tencent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.gj.gjlibrary.util.logger.AbLog;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by guojing on 2016/9/30.
 * QQ第三方登录工具类
 */
public class QQLoginHelper {

    private Activity activity;
    private static Tencent mTencent;
    private static boolean isServerSideLogin = false;
    private TextView tv_qq_login;
//    private TextView mUserInfo;
//    private ImageView mUserLogo;
    private UserInfo mInfo;

    public static final String APP_ID = "222222";


    public QQLoginHelper(Activity activity,TextView tv_qq_login){
        this.activity = activity;
        this.tv_qq_login = tv_qq_login;
        init();
    }

    private void init(){
        if (mTencent == null) {
            mTencent = Tencent.createInstance(APP_ID, activity);
        }
    }

    public void start(){
        if (!mTencent.isSessionValid()) {
            mTencent.login(activity, "all", loginListener); //loginListener来监听登录成功或者失败的状态和回调方法
            isServerSideLogin = false;
            Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
        } else {
            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
                mTencent.logout(activity);
                mTencent.login(activity, "all", loginListener);
                isServerSideLogin = false;
                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
                return;
            }
            mTencent.logout(activity);
            updateUserInfo();
            updateLoginButton();
        }
    }


    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        //登录成功之后获得QQ昵称
                        AbLog.i("response  " + response.toString());
                        AbLog.i("response nickname " + response.getString("nickname"));
                        //mUserInfo.setVisibility(android.view.View.VISIBLE);
                        //mUserInfo.setText(response.getString("nickname"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                //mUserLogo.setImageBitmap(bitmap);
                //mUserLogo.setVisibility(android.view.View.VISIBLE);
            }
        }

    };

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
            initOpenidAndToken(values);
            updateUserInfo();
            updateLoginButton();
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(activity, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(activity, "返回为空", "登录失败");
                return;
            }
            //Util.showResultDialog(activity, response.toString(), "登录成功");
            // 有奖分享处理
            //handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(activity, "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(activity, "onCancel: ");
            Util.dismissDialog();
            if (isServerSideLogin) {
                isServerSideLogin = false;
            }
        }
    }

    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread(){

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject)response;
                            if(json.has("figureurl")){
                                Bitmap bitmap = null;
                                try {
                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {

                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(activity, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
//            mUserInfo.setText("");
//            mUserInfo.setVisibility(android.view.View.GONE);
//            mUserLogo.setVisibility(android.view.View.GONE);
        }
    }

    private void updateLoginButton() {
        if (mTencent != null && mTencent.isSessionValid()) {
            if (isServerSideLogin) {
                //tv_qq_login.setTextColor(Color.BLUE);
                tv_qq_login.setText("登录");
                //mServerSideLoginBtn.setTextColor(Color.RED);
                //mServerSideLoginBtn.setText("退出Server-Side账号");
            } else {
                //tv_qq_login.setTextColor(Color.RED);
                tv_qq_login.setText("退出帐号");
                //mServerSideLoginBtn.setTextColor(Color.BLUE);
                //mServerSideLoginBtn.setText("Server-Side登陆");
            }
        } else {
            //tv_qq_login.setTextColor(Color.BLUE);
            tv_qq_login.setText("登录");
            //mServerSideLoginBtn.setTextColor(Color.BLUE);
            //mServerSideLoginBtn.setText("Server-Side登陆");
        }
    }

}
