package com.hyphenate.easeui.utils;

import android.content.Context;
import android.text.TextUtils;

import com.gj.gjlibrary.util.AbGsonUtil;
import com.gj.gjlibrary.util.PreferencesUtils;
import com.hyphenate.easeui.domain.HuanxinUserInfoCacheEntity;

/**
 * Created by guojing on 15/11/16.
 */
public class HuanxinUserInfoCacheUtil {

    /**
     * 获取当前用户头像
     */
    public static String getCurrentUserHead(Context context){
        return PreferencesUtils.getString(context, "huanxin_current_user_head", "");
    }

    /**
     * 缓存当前用户头像
     */
    public static void saveCurrentUserHead(Context context, String currentUserHead) {
        PreferencesUtils.putString(context, "huanxin_current_user_head", currentUserHead);
    }

    /**
     *  环信用户个人资料缓存
     */
    public static HuanxinUserInfoCacheEntity getHuanxinUserInfoCacheEntity(Context context) {
        String json = PreferencesUtils.getString(context, "huanxin_user_info_cache_entity", "");
        if(TextUtils.isEmpty(json)) {
            return null;
        } else {
            return AbGsonUtil.json2Bean(json, HuanxinUserInfoCacheEntity.class);
        }
    }

    /**
     * 缓存searchHistoryEntity信息
     */
    public static void saveHuanxinUserInfoCacheEntity(Context context, HuanxinUserInfoCacheEntity huanxinUserInfoCacheEntity) {
        String json = AbGsonUtil.bean2Json(huanxinUserInfoCacheEntity);
        PreferencesUtils.putString(context, "huanxin_user_info_cache_entity", json);
    }

    /**
     * 清除searchHistoryEntity信息
     */
    public static void clearHuanxinUserInfoCacheEntity(Context context) {
        PreferencesUtils.putString(context, "huanxin_user_info_cache_entity", "");
    }

}
