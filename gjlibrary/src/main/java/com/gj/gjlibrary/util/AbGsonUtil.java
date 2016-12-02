package com.gj.gjlibrary.util;

import com.google.gson.Gson;

/**
 * Author:      qiyunfeng
 * Email:       510568444@qq.com | qiyunfeng0618@163.com
 * GitHub:      https://github.com/qiyunfeng0618
 * Date:        15/10/15 上午12:08
 * Description: EasyFrame
 */
public class AbGsonUtil {
    private static Gson gson = new Gson();

    /**
     * json转换成Bean
     */
    public static <T> T json2Bean(String json, Class<T> clazz) {
        T t = gson.fromJson(json, clazz);
        return t;
    }

    /**
     * Bean转换json
     */
    public static String bean2Json(Object bean) {
        return gson.toJson(bean);
    }
}
