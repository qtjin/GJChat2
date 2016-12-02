package com.gj.gjchat2.model;

import android.content.Context;

import com.gj.gjchat2.R;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.HuanxinUserInfoCacheEntity;
import com.hyphenate.easeui.utils.HuanxinUserInfoCacheUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by guojing on 2016/7/7.
 * 环信缓存昵称头像模型工具类
 */
public class HuanxinCacheModel {


    public interface CallbackListener {
        public void callback(boolean success); //回调，通知列表刷新
        public void callback(EaseUser easeUser);
}

    /**
     * 判断是否缓存
     * @param context
     * @param huanxin_account
     * @return
     */
    public static boolean isCache(Context context,String huanxin_account){
        HuanxinUserInfoCacheEntity huanxinUserInfoCacheEntity = HuanxinUserInfoCacheUtil.getHuanxinUserInfoCacheEntity(context);
        if(huanxinUserInfoCacheEntity!=null&&huanxinUserInfoCacheEntity.map!=null&&huanxinUserInfoCacheEntity.map.size()>0){ //本地缓存里有
            EaseUser easeUser = huanxinUserInfoCacheEntity.map.get(huanxin_account);
            if(null!=easeUser){
                return true;
            }
        }else { //本地缓存里没有
            return false;
        }
        return false;
    }


    public static void getHXUserIdentityMoni(Context context,List<String> huanxin_account_list) {

        /*******************调用环信本地缓存对象资料（群头像、昵称）********************/
        HuanxinUserInfoCacheEntity huanxinUserInfoCacheEntity =  HuanxinUserInfoCacheUtil.getHuanxinUserInfoCacheEntity(context);
        if(huanxinUserInfoCacheEntity==null||huanxinUserInfoCacheEntity.map==null){
            huanxinUserInfoCacheEntity = new HuanxinUserInfoCacheEntity();
            huanxinUserInfoCacheEntity.map = new HashMap<String,EaseUser>();
        }

        String[] nicknameData =  context.getResources().getStringArray(R.array.addressbook); //模拟向保存用户昵称等信息的服务端获取数据

        for (int i = 0; i < huanxin_account_list.size(); i++) {
            String huanxin_account = huanxin_account_list.get(i);
            for (int j = 0; j < nicknameData.length; j++) {
                String s_huanxin_account = nicknameData[j].split("\\|")[0];
                String nickname = nicknameData[j].split("\\|")[1];
                String resid = nicknameData[j].split("\\|")[2];
                if(huanxin_account.equals(s_huanxin_account)){
                    //AbLog.i("getHXUserIdentity 缓存对象姓名为 " + nickname);
                    EaseUser easeUser = new EaseUser(huanxin_account);
                    easeUser.setUserId(huanxin_account);
                    easeUser.setNick(nickname);
                    easeUser.setAvatar(resid);
                    //easeUser.setRole(hxUserIndentifyEntity.data.idenCode);
                    huanxinUserInfoCacheEntity.map.put(huanxin_account, easeUser);
                    break;
                }
            }
        }
        HuanxinUserInfoCacheUtil.saveHuanxinUserInfoCacheEntity(context, huanxinUserInfoCacheEntity);
    }
    public static EaseUser getHXUserIdentityMoni(Context context,String huanxin_account) {

        /*******************调用环信本地缓存对象资料（群头像、昵称）********************/
        HuanxinUserInfoCacheEntity huanxinUserInfoCacheEntity =  HuanxinUserInfoCacheUtil.getHuanxinUserInfoCacheEntity(context);
        if(huanxinUserInfoCacheEntity==null||huanxinUserInfoCacheEntity.map==null){
            huanxinUserInfoCacheEntity = new HuanxinUserInfoCacheEntity();
            huanxinUserInfoCacheEntity.map = new HashMap<String,EaseUser>();
        }

        String[] nicknameData =  context.getResources().getStringArray(R.array.addressbook); //模拟向保存用户昵称等信息的服务端获取数据
        EaseUser easeUser = null;
        for (int j = 0; j < nicknameData.length; j++) {
            String s_huanxin_account = nicknameData[j].split("\\|")[0];
            String nickname = nicknameData[j].split("\\|")[1];
            String resid = nicknameData[j].split("\\|")[2];
            if(huanxin_account.equals(s_huanxin_account)){
                //AbLog.i("getHXUserIdentity 缓存对象姓名为 " + nickname);
                easeUser = new EaseUser(huanxin_account);
                easeUser.setUserId(huanxin_account);
                easeUser.setNick(nickname);
                easeUser.setAvatar(resid);
                //easeUser.setRole(hxUserIndentifyEntity.data.idenCode);
                huanxinUserInfoCacheEntity.map.put(huanxin_account, easeUser);
                break;
            }
        }
        HuanxinUserInfoCacheUtil.saveHuanxinUserInfoCacheEntity(context, huanxinUserInfoCacheEntity);
        return easeUser;
    }



}
