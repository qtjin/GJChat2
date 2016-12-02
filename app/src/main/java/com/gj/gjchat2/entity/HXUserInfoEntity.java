package com.gj.gjchat2.entity;


import com.gj.gjlibrary.base.BaseEntity;

/**
 * Created by guojing on 2016/7/5.
 */
public class HXUserInfoEntity extends BaseEntity<HXUserInfoEntity.HXUser> {

    public static class HXUser {
        public String huanxin_account;
        public String nickname;
        public String image;
    }

}
