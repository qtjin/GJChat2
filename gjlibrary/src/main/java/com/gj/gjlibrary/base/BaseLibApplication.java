package com.gj.gjlibrary.base;

import android.app.Application;

import com.gj.gjlibrary.util.AppConfig;
import com.gj.gjlibrary.util.ImageLoaderHelper;
import com.gj.gjlibrary.util.logger.AbLog;
import com.gj.gjlibrary.util.logger.LogLevel;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Author:      qiyunfeng
 * Email:       510568444@qq.com | qiyunfeng0618@163.com
 * Date:        15/9/28 下午3:55
 * Description: EasyFrame
 */
public abstract class BaseLibApplication extends Application {

    private static BaseLibApplication instance;

    /**
     * 获得当前app运行的AppContext
     *
     * @return BaseLibApplication
     */
    public static BaseLibApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
    }

    private void init() {
        // 错误信息捕获
        /*Thread.setDefaultUncaughtExceptionHandler(AppException
            .getAppExceptionHandler(this));*/
        initLogger(true);
        initImageLoader(true);
    }

    /**
     * 初始化日志打印工具类
     */
    private void initLogger(boolean isNeed) {
        if (isNeed && AppConfig.IS_DEBUG) {
            AbLog
                .init(AppConfig.APP_NAME)     // default PRETTYLOGGER or use just init()
//            .setMethodCount(2)          // default 2
//            .hideThreadInfo()           // default shown
//            .setMethodOffset(2)         // default 0
                .setLogLevel(LogLevel.FULL);  // default LogLevel.FULL
        } else {
            AbLog
                .init(AppConfig.APP_NAME)     // default PRETTYLOGGER or use just init()
                .setLogLevel(LogLevel.NONE);  // default LogLevel.FULL
        }

    }


    protected void initImageLoader(boolean isNeed) {
        if (!isNeed) {
            return;
        }
        // 初始化ImageLoader
        ImageLoader.getInstance().init(
            ImageLoaderHelper.getInstance(this).getImageLoaderConfiguration());
    }
}
