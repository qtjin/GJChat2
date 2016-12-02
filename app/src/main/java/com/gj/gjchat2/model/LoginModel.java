package com.gj.gjchat2.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.HuanxinHelper;
import com.gj.gjchat2.huanxin.db.HuanxinDBManager;
import com.gj.gjchat2.huanxin.ui.ChatActivity;
import com.gj.gjchat2.ui.activity.main.MainActivity;
import com.gj.gjchat2.ui.activity.main.SplashActivity;
import com.gj.gjchat2.ui.activity.main.StartActivity;
import com.gj.gjchat2.ui.activity.me.LoginActivity;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseModel;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.HuanxinUserInfoCacheUtil;
import com.hyphenate.exceptions.HyphenateException;



/**
 * Created by guojing on 2016/3/22.
 * 登录响应
 */
public class LoginModel extends BaseModel {

    private LoginActivity loginActivity;
    private StartActivity startActivity;

    public String huanxin_account; //环信账号
    public String huanxin_password; //环信密码


    public static final int TIME = 3 * 1000;

    public static long startTime;

    public LoginModel(LoginActivity loginActivity){
        super(loginActivity);
        this.loginActivity = loginActivity;
        startTime = System.currentTimeMillis();
    }

    public LoginModel(StartActivity startActivity){
        super(startActivity);
        this.startActivity = startActivity;
        startTime = System.currentTimeMillis();
    }

    public void getData() {  //登录

        //自动登录时验证账号密码是否正确
        baseActivity.showProgressDialog();
        huanxin_account = loginActivity.username;
        huanxin_password = loginActivity.password;

        if(huanxin_account!=null&&!"".equals(huanxin_account)
                &&huanxin_password!=null&&!"".equals(huanxin_password)){
            huanxinLogin(huanxin_account,huanxin_password); //登录环信
        }else{
            huanxinRegister(huanxin_account,huanxin_password); //注册环信
        }
       /* baseActivity.showProgressDialog();
        Api.login(loginActivity.username, loginActivity.password, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String json, Throwable throwable) {
                baseActivity.hideProgressDialog();
                AbLog.i("login json: " + json);
                AbLog.i("login statusCode: " + statusCode);
                ToastUtils.show(baseActivity, "登录失败");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String json) {

                AbLog.i("login json: " + json);
                AbLog.i("login statusCode: " + statusCode);

                LoginEntity loginEntity = AbGsonUtil.json2Bean(json, LoginEntity.class);
                if (loginEntity != null && loginEntity.data != null) {
                    if (loginEntity.success) {
                        baseActivity.hideProgressDialog();


                        CacheUtil.saveUsername(loginActivity, loginActivity.username);
                        CacheUtil.savePassword(loginActivity, loginActivity.password);
                        //保存用户ID
                        CacheUtil.saveUserId(loginActivity, loginEntity.data.userId);

                        boolean isGo = true;
                        List<String> roleCodes = loginEntity.data.groupCodes;
                        String roleStr = "";
                        if (roleCodes != null && roleCodes.size() > 0) {
                            for (String str : roleCodes) {
                                if ("teacher".equals(str)) {
                                    roleStr += "teacher"; //教职工
                                }
                                if ("bzr".equals(str)) {
                                    roleStr += "bzr"; //班主任
                                }
                                if ("12".equals(str)) {
                                    roleStr += "12"; //授课教师
                                }
                                if ("dormitory".equals(str)) {
                                    roleStr += "dormitory"; //宿管
                                }
                                if ("student".equals(str)) {
                                   ToastUtils.show(loginActivity, "该账号是学生，没有权限登录");
                                    isGo = false;
                                }
                                if ("parent".equals(str)) {
                                    ToastUtils.show(loginActivity, "该账号是家长，没有权限登录");
                                    isGo = false;
                                }
                            }
                        }
                        if(isGo){
                            CacheUtil.saveRole(loginActivity, roleStr);
                            AppContext.getAppContext().saveLoginEntity(loginEntity);

                            //huanxin_account = loginEntity.data.huanxin_account;
                            //huanxin_password = loginEntity.data.huanxin_password;
                            huanxin_account = loginEntity.data.userId;
                            huanxin_password = loginActivity.password;

                            //cacheAddressBook(); //缓存通讯录
                            if(huanxin_account!=null&&!"".equals(huanxin_account)
                                    &&huanxin_password!=null&&!"".equals(huanxin_password)){
                                huanxinLogin(huanxin_account,huanxin_password); //登录环信
                            }else{
                                huanxinRegister(huanxin_account,huanxin_password); //注册环信
                                //huanxinRegister(CacheUtil.getUserId(baseActivity)); //注册环信
                            }
                        }
                    } else {
                        baseActivity.hideProgressDialog();
                        ToastUtils.show(baseActivity, "用户名或密码错误");
                    }
                }
            }
        });*/
    }

    public void updData() {

        //自动登录时验证账号密码是否正确
        baseActivity.showProgressDialog();
        huanxin_account = CacheUtil.getUsername(startActivity);
        huanxin_password = CacheUtil.getPassword(startActivity);

        if(huanxin_account!=null&&!"".equals(huanxin_account)
                &&huanxin_password!=null&&!"".equals(huanxin_password)){
            huanxinLogin(huanxin_account,huanxin_password); //登录环信
        }else{
            huanxinRegister(huanxin_account,huanxin_password); //注册环信
        }

        //使用业务接口类ApiBiz调用其访问登录接口的方法,验证保存的用户名和密码是否正确
       /*
        String username = CacheUtil.getUsername(startActivity);
        final String password = CacheUtil.getPassword(startActivity);
       Api.login(username, password, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String json, Throwable throwable) {
                AbLog.i("login json: " + json);
                AbLog.i("login statusCode: " + statusCode);
                baseActivity.hideProgressDialog();
                isGoSplashActivity();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String json) {
                AbLog.i("login json: " + json);
                AbLog.i("login statusCode: " + statusCode);

                LoginEntity loginEntity = AbGsonUtil.json2Bean(json, LoginEntity.class);
                if (loginEntity != null && loginEntity.data != null) {
                    if (loginEntity.success) {
                        baseActivity.hideProgressDialog();


                        //保存用户ID
                        CacheUtil.saveUserId(baseActivity, loginEntity.data.userId);

                        boolean isGo = true;
                        List<String> roleCodes = loginEntity.data.groupCodes;
//                        String roleStr = "";
//                        if (roleCodes != null && roleCodes.size() > 0) {
//                            for (String str : roleCodes) {
//                                if ("teacher".equals(str)) {
//                                    roleStr += "teacher"; //教职工
//                                }
//                                if ("bzr".equals(str)) {
//                                    roleStr += "bzr"; //班主任
//                                }
//                                if ("12".equals(str)) {
//                                    roleStr += "12"; //授课教师
//                                }
//                                if ("dormitory".equals(str)) {
//                                    roleStr += "dormitory"; //宿管
//                                }
//                                if ("student".equals(str)) {
//                                    ToastUtils.show(baseActivity, "该账号是学生，没有权限登录");
//                                    isGo = false;
//                                }
//                                if ("parent".equals(str)) {
//                                    ToastUtils.show(baseActivity, "该账号是家长，没有权限登录");
//                                    isGo = false;
//                                }
//                            }
//                        }
                        if(isGo){
                            //CacheUtil.saveRole(baseActivity, roleStr);
                            AppContext.getAppContext().saveLoginEntity(loginEntity);

                            //huanxin_account = loginEntity.data.huanxin_account;
                            //huanxin_password = loginEntity.data.huanxin_password;
                            huanxin_account = loginEntity.data.userId;
                            huanxin_password = password;
                            //cacheAddressBook(); //缓存通讯录
                            if(huanxin_account!=null&&!"".equals(huanxin_account)
                                    &&huanxin_password!=null&&!"".equals(huanxin_password)){
                                huanxinLogin(huanxin_account,huanxin_password); //登录环信
                            }else{
                                huanxinRegister(huanxin_account,huanxin_password); //注册环信
                                //huanxinRegister(CacheUtil.getUserId(baseActivity)); //注册环信
                            }
                        }
                    } else {
                        baseActivity.hideProgressDialog();
                        isGoSplashActivity(); //判断是否是初次加载
                    }
                }
            }
        });*/
    }

    /**
     * 判断是否是初次加载
     */
    public void isGoSplashActivity() {
        long costTime = System.currentTimeMillis() - startTime;
//        AbLog.i("isGoSplashActivity costTime："+costTime);
//        AbLog.i("isGoSplashActivity TIME："+TIME);

        //等待sleeptime时长
        if (TIME - costTime > 0) {
            if(CacheUtil.getLaunched()) {
                sleepAndGo(TIME - costTime,0); //去登录页面
            }else{
                sleepAndGo(TIME - costTime,1); //去引导页
            }
        }
    }
    /**
     * 进入首页
     */
    public void goMainActivity() {


        /********************环信**********************/
        // ** 免登陆情况 加载所有本地群和会话
        //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
        //加上的话保证进了主页面会话和群组都已经load完毕
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
        /********************环信*********************/


        long costTime = System.currentTimeMillis() - startTime;

//        AbLog.i("goMainActivity costTime："+costTime);
//        AbLog.i("goMainActivity TIME："+TIME);

        //等待sleeptime时长
        if (TIME - costTime > 0) {
            sleepAndGo(TIME - costTime,2); //去首页
        }
    }


    /****************************************环信****************************************/

    /**
     * 注册环信(客户端开放注册)
     */
    public void huanxinRegister(final String huanxin_account, final String huanxin_password) {
        AbLog.i("huanxinRegister 注册环信(客户端开放注册)");
        //注册失败会抛出HyphenateException
        new Thread(new Runnable() {
            public void run() {
                try {
                    // call method in SDK
                    AbLog.i("huanxin_account: "+huanxin_account);
                    AbLog.i("huanxin_password: "+huanxin_password);

                    EMClient.getInstance().createAccount(huanxin_account, huanxin_password);
                    baseActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (!baseActivity.isFinishing())
                               //baseActivity.hideProgressDialog();
                            // save current user

                            HuanxinHelper.getInstance().setCurrentUserName(huanxin_account);
                            Toast.makeText(baseActivity, baseActivity.getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                            huanxinLogin(huanxin_account, huanxin_password); //登录环信
                        }
                    });
                } catch (final HyphenateException e) {
                    baseActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (!baseActivity.isFinishing())
                                baseActivity.hideProgressDialog();
                            int errorCode = e.getErrorCode();
                            if (errorCode == EMError.NETWORK_ERROR) {
                                Toast.makeText(baseActivity, baseActivity.getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ALREADY_EXIST) {
                                Toast.makeText(baseActivity, baseActivity.getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_AUTHENTICATION_FAILED) {
                                Toast.makeText(baseActivity, baseActivity.getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                            } else if (errorCode == EMError.USER_ILLEGAL_ARGUMENT) {
                                Toast.makeText(baseActivity, baseActivity.getResources().getString(R.string.illegal_user_name), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(baseActivity, baseActivity.getResources().getString(R.string.Registration_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 登录环信
     */
    public void huanxinLogin(final String huanxin_account, final String huanxin_password){
        AbLog.i("************登录环信************");
        this.huanxin_account = huanxin_account;
        this.huanxin_password = huanxin_password;
        //判断是否已登录
        if(HuanxinHelper.getInstance().isLoggedIn()){
            baseActivity.hideProgressDialog();

            goMainActivity(); //跳转到首页
        }else{
            if (!EaseCommonUtils.isNetWorkConnected(baseActivity)) {
                baseActivity.hideProgressDialog();
                Toast.makeText(baseActivity, "当前网络不可用", Toast.LENGTH_SHORT).show();
                return;
            }
            HuanxinDBManager.getInstance().closeDB();
            // reset current user name before login
            HuanxinHelper.getInstance().setCurrentUserName(huanxin_account);

            // 调用sdk登陆方法登陆聊天服务器
            EMClient.getInstance().login(huanxin_account, huanxin_password, new EMCallBack() {

                @Override
                public void onSuccess() {
                    AbLog.i("************环信登录成功************");
                    baseActivity.hideProgressDialog();
                    //保存环信账号密码
                    CacheUtil.saveUserId(baseActivity, "30222");
                    CacheUtil.saveHuanxinAccount(baseActivity, LoginModel.this.huanxin_account);
                    CacheUtil.saveHuanxinPassword(baseActivity, LoginModel.this.huanxin_password);

                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and  以上两个方法是为了保证进入主页面后本地会话和群组都 load 完毕。
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();


                    //异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
                    HuanxinHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

                    CacheUtil.saveUsername(loginActivity, huanxin_account);
                    CacheUtil.savePassword(loginActivity, huanxin_password);

                    String[] nicknameData =  baseActivity.getResources().getStringArray(R.array.addressbook);
                    for (int i = 0; i < nicknameData.length; i++) {
                        String s_huanxin_account = nicknameData[i].split("\\|")[0];
                        String nickname = nicknameData[i].split("\\|")[1];
                        String userHead = nicknameData[i].split("\\|")[2];
                        if(huanxin_account.equals(s_huanxin_account)){
                            CacheUtil.saveNickname(loginActivity, nickname);
                            CacheUtil.saveUserHeadImageUri(loginActivity, userHead);
                            HuanxinUserInfoCacheUtil.saveCurrentUserHead(loginActivity,userHead);
                        }
                    }
                    goMainActivity(); //跳转到首页

                }

                @Override
                public void onProgress(int progress, String status) {
                }

                @Override
                public void onError(final int code, final String message) {
                    baseActivity.hideProgressDialog();
                    //ToastUtils.show(baseActivity, "聊天登录失败:" + message);
                    huanxinRegister(huanxin_account,huanxin_password);
                }
            });
        }
    }

    /**
     * 进入聊天页面 从最新消息列表进
     */
    public void goChatActivity(String huanxin_account, int type) {
        // 进入聊天页面
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_USER_ID, huanxin_account);
        bundle.putInt(Constant.EXTRA_CHAT_TYPE, type);
        baseActivity.readyGo(ChatActivity.class, bundle);
    }


    /**
     * 进入群聊天页面 从最新消息列表进
     */
    public void goGroupChatActivity(String groupId, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_USER_ID, groupId);
        bundle.putInt(Constant.EXTRA_CHAT_TYPE, type);
        baseActivity.readyGoForResult(ChatActivity.class, 0, bundle);
    }


    public void sleepAndGo(final long time,final int what){
        new Thread(new Runnable(){
            public void run(){
                try {
                    Thread.sleep(time);
                    Message msg = new Message();
                    msg.what = what;
                    handler.sendMessage(msg); //告诉主线程执行任务
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: //登录
                    baseActivity.readyGoThenKill(LoginActivity.class);
                    break;
                case 1: //引导页
                    baseActivity.readyGoThenKill(SplashActivity.class);
                    break;
                case 2: //首页
                    baseActivity.readyGoThenKill(MainActivity.class);
                    break;
            }
            super.handleMessage(msg);
        }

    };
}
