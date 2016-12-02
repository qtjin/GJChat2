package com.gj.gjchat2.entity;


import com.gj.gjlibrary.base.BaseEntity;

import java.util.List;

/**
 * Created by guojing on 2016/3/22.
 * 登录响应
 */
public class LoginEntity extends BaseEntity<LoginEntity.DataEntity> {


    /**
     * id : 30259
     * username : cteacher
     * compellation : 刘老师
     * sex : 男
     * departmentid : 2
     * qq :
     * mail :
     * phone : 8888888888888
     * jzdh :
     * address :
     * url :
     * addtime : 2014-12-19 15:44:13
     * pnum : 0
     * classid : 0
     * lastloginip : 192.168.2.93
     * lastlogintime : 1462426858
     * logincount : 1
     * isconfirm : 1
     * companyId : 0
     * leixing : 0
     * huanxin_account : f7a03432a57c7365d78043c0b75f8fd1_cteacher
     * huanxin_password : 452f636a8cc010e37e2f38cb500f5816
     * groupCodes : ["cteacher"]
     * image :
     * userId : 30259
     */

    /**
     * data : {"id":"30259","username":"cteacher","compellation":"刘老师","sex":"男","departmentid":"2","qq":"","mail":"","phone":"8888888888888","jzdh":"","address":"","url":"","addtime":"2014-12-19 15:44:13","pnum":"0","classid":"0","lastloginip":"192.168.2.93","lastlogintime":"1462426858","logincount":"1","isconfirm":"1","companyId":"0","leixing":"0","huanxin_account":"f7a03432a57c7365d78043c0b75f8fd1_cteacher","huanxin_password":"452f636a8cc010e37e2f38cb500f5816","groupCodes":["cteacher"],"image":"","userId":"30259"}
     * success : true
     * error_code : 0
     * url : 登录成功
     */

    public static class DataEntity {
        public String id;
        public String username;
        public String compellation;
        public String sex;
        public String departmentid;
        public String qq;
        public String mail;
        public String phone;
        public String jzdh;
        public String address;
        public String url;
        public String addtime;
        public String pnum;
        public String classid;
        public String lastloginip;
        public String lastlogintime;
        public String logincount;
        public String isconfirm;
        public String companyId;
        public String leixing;
        public String huanxin_account;
        public String huanxin_password;
        public String image;
        public String userId;
        public List<String> groupCodes;
    }
}
