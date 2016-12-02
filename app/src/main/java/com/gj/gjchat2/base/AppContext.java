package com.gj.gjchat2.base;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.view.Display;
import android.view.WindowManager;

import com.gj.gjchat2.entity.LoginEntity;
import com.gj.gjchat2.huanxin.HuanxinHelper;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseLibApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.File;

/**
 * Created by guojing on 2015/12/28.
 */
public class AppContext extends BaseLibApplication {

    public static AppContext appContext;

    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static float DENSITY; //像素密度

    public static boolean NO_WIFI_PLAY = false; //是否在非WIFI条件下播放

    public static final String LOCAL_FOLDER_NAME = "local_folder_name";//跳转到相册页的文件夹名称

    public LoginEntity loginEntity;

    private Display display;

    /***************环信***************/
//    public static Map<String, EaseUser> contactList; //保存单聊通讯录个人资料的集合
    public static final int GROUP_ADD_MEMBER=10001; //群聊添加新成员的resultCode
    public static final int NEW_GROUP_NAME=10002; //修改群组名称的resultCode
    public static final int INIT_GROUP_CHAT=10003; //新创建的群聊resultCode
    /***************环信***************/

    public static AppContext getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;

        ZXingLibrary.initDisplayOpinion(this); //二维码扫描器工具库初始化

        init();
        com.gj.gjchat2.huanxin.HuanxinHelper.getInstance().init(appContext);

        /**
         * "手机型号: " + android.os.Build.MODEL + ",\nSDK版本:"
         + android.os.Build.VERSION.SDK + ",\n系统版本:"
         + android.os.Build.VERSION.RELEASE
         */
       /* //初始化异常捕获
        Thread.setDefaultUncaughtExceptionHandler(new AppException(this,
                new AppException.CallbackListener() {
                    @Override
                    public void uploadServer(String msg) {
                        //调用app错误日志接口 向服务器端提交错误日志
                        Api.addExceptionLog(
                                "慕课移动端"   //sysName	string	所属系统名称	数字化校园
                                , "http://applog.zhijiao88.com/" //url	string	所属系统url	http://192.168.2.212：8888;接口请求地址
                                , "mooc" //appName	string	App名称	学生端
                                , "V" + VersionUtil.getVersionName(AppContext.this) //appVersion	string	App版本	V1.1
                                , "Android" //appSystem	string	App系统	android
                                , "Android " + android.os.Build.VERSION.RELEASE //sysVersion	string	App系统版本	Android 5.5
                                , HuanxinUserInfoCacheUtil.getUsername(AppContext.this) //account	string	用户账号	Admin
                                , HuanxinUserInfoCacheUtil.getNickname(AppContext.this) //nickname	string	用户昵称	管理员
                                , msg //remark	string	错误信息描述	.......
                                , new TextHttpResponseHandler() {
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        AbLog.i("response statusCode: " + statusCode);
                                        AbLog.i("response responseString: " + responseString);
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String json) {
                                        AbLog.i("response json: " + json);
                                    }
                                });
                    }
                }));*/
    }

    /**
     * 初始化  选择多张图片的辅助工具类
     */
    private void init() {
        initImageLoader(this);

        if (display == null) {
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(appContext, 5 * 1000, 5 * 1000));
        //		config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 保存用户登录之后返回的信息
     * @param loginEntity
     */
    public void saveLoginEntity(LoginEntity loginEntity){
        this.loginEntity = loginEntity;
        CacheUtil.saveUserId(this, loginEntity.data.userId);
        CacheUtil.saveUserHeadImageUri(this, loginEntity.data.image);
        CacheUtil.saveUsername(this, loginEntity.data.huanxin_account);
        CacheUtil.saveNickname(this, loginEntity.data.compellation);
        CacheUtil.savePhone(this, loginEntity.data.phone);
        CacheUtil.saveLoginEntity(this, loginEntity);
    }
    /**
     * 保存用户登录之后返回的信息
     */
    public LoginEntity getLoginEntity() {
        if(this.loginEntity == null) {
            this.loginEntity = CacheUtil.getLoginEntity(this);
            return CacheUtil.getLoginEntity(this);
        }else{
            return loginEntity;
        }
    }

    //缓存图片路径
    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = getExternalCacheDir();
        else
            cacheDir = getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * 退出登录
     */
    public void logout() {
        CacheUtil.cleanLogin();
        HuanxinHelper.getInstance().logout(true, null);
    }

    /***************** 分包 ********************/
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
    /***************** 分包 ********************/


}
