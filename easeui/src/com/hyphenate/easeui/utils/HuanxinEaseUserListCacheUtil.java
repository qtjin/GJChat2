package com.hyphenate.easeui.utils;

import android.content.Context;
import android.text.TextUtils;

import com.gj.gjlibrary.util.AbGsonUtil;
import com.gj.gjlibrary.util.PreferencesUtils;
import com.hyphenate.easeui.domain.HuanxinEaseUserListCacheEntity;

/**
 * Created by guojing on 15/11/16.
 */
public class HuanxinEaseUserListCacheUtil {

    /**
     *  环信用户个人资料缓存
     */
    public static HuanxinEaseUserListCacheEntity getHuanxinEaseUserListCacheEntity(Context context) {
        String json = PreferencesUtils.getString(context, "huanxin_ease_user_list_cache_entity", "");
        if(TextUtils.isEmpty(json)) {
            return null;
        } else {
            return AbGsonUtil.json2Bean(json, HuanxinEaseUserListCacheEntity.class);
        }
    }

    /**
     * 缓存searchHistoryEntity信息
     */
    public static void saveHuanxinEaseUserListCacheEntity(Context context, HuanxinEaseUserListCacheEntity huanxinEaseUserListCacheEntity) {
        String json = AbGsonUtil.bean2Json(huanxinEaseUserListCacheEntity);
        PreferencesUtils.putString(context, "huanxin_ease_user_list_cache_entity", json);
    }

    /**
     * 清除searchHistoryEntity信息
     */
    public static void clearHuanxinEaseUserListCacheEntity(Context context) {
        PreferencesUtils.putString(context, "huanxin_ease_user_list_cache_entity", "");
    }

}
