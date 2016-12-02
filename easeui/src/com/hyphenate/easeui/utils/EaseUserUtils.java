package com.hyphenate.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.HuanxinUserInfoCacheEntity;

import java.lang.reflect.Field;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;
    public static HuanxinUserInfoCacheEntity huanxinUserInfoCacheEntity;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * 根据username获取相应user
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username,Context context){
        if(huanxinUserInfoCacheEntity==null){
            huanxinUserInfoCacheEntity = HuanxinUserInfoCacheUtil.getHuanxinUserInfoCacheEntity(context);
        }
        if(huanxinUserInfoCacheEntity!=null&&huanxinUserInfoCacheEntity.map!=null&&huanxinUserInfoCacheEntity.map.size()>0){
            return huanxinUserInfoCacheEntity.map.get(username);
        }else if(userProvider != null){
            return userProvider.getUser(username);
        }
        return null;
    }
    /**
     * 根据username获取相应user
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
      if(userProvider != null){
            return userProvider.getUser(username);
        }
        return null;
    }

    /**
     * 设置用户头像
     * @param username
     */
    public static String setUserAvatar(Context context, String username, ImageView imageView){
    	//EaseUser user = getUserInfo(username);
        EaseUser user = null;
        /*******************调用环信本地缓存联系人资料（头像、昵称）********************/
        //if(huanxinUserInfoCacheEntity==null){ 不能加这个，因为huanxinUserInfoCacheEntity需要实时更新
             huanxinUserInfoCacheEntity = HuanxinUserInfoCacheUtil.getHuanxinUserInfoCacheEntity(context);
        //}
        if(huanxinUserInfoCacheEntity!=null&&huanxinUserInfoCacheEntity.map!=null&&huanxinUserInfoCacheEntity.map.size()>0){
            user = huanxinUserInfoCacheEntity.map.get(username);
        }else{
            user = getUserInfo(username);
        }

        if(user != null && user.getAvatar() != null){
            //AbLog.i("user.getUsername(): "+user.getUsername());
            //AbLog.i("user.getAvatar(): " + user.getAvatar());
            try {
                Class drawable = R.drawable.class;
                Field field = null;
                try {
                    field = drawable.getField(user.getAvatar());
                    int avatarResId = field.getInt(field.getName());
                    imageView.setImageResource(avatarResId);
                    //Glide.with(context).load(avatarResId).into(imageView);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //int avatarResId = Integer.parseInt(user.getAvatar());
                //Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //正常的string路径
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_grzx_icon_morentouxiang).into(imageView);
            }
            return user.getAvatar();
        }else{
            Glide.with(context).load(R.drawable.ic_grzx_icon_morentouxiang).into(imageView);
            return "";
        }
    }
    
    /**
     * 设置用户昵称
     */
    public static String setUserNick(Context context,String username,TextView textView){
        //AbLog.i("username: "+username);
        if(textView != null){
        	//EaseUser user = getUserInfo(username);
            EaseUser user = null;
            /*******************调用环信本地缓存联系人资料（头像、昵称）********************/
            //if(huanxinUserInfoCacheEntity==null){
                 huanxinUserInfoCacheEntity = HuanxinUserInfoCacheUtil.getHuanxinUserInfoCacheEntity(context);
            //}
            if(huanxinUserInfoCacheEntity!=null&&huanxinUserInfoCacheEntity.map!=null&&huanxinUserInfoCacheEntity.map.size()>0){
                //AbLog.i("setUserAvatar huanxinUserInfoCacheEntity.map.size()" + huanxinUserInfoCacheEntity.map.size());
                user = huanxinUserInfoCacheEntity.map.get(username);
            }else{
                user = getUserInfo(username);
            }
        	if(user != null && user.getNick() != null){
                //AbLog.i("user.getNick(): "+user.getNick());
        		textView.setText(user.getNick());
                return user.getNick();
        	}else{
        		textView.setText(username);
                return "";
        	}
        }
        return "";
    }
    
}
