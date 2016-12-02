package com.gj.gjchat2.entity;


import com.gj.gjlibrary.base.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guojing on 2016/3/25.
 */
public class AddressBookEntity extends BaseEntity<AddressBookEntity.DataEntity> {


    /**
     * rows : [{"id":"30260","username":"cteacher1","compellation":"马云祥","sex":"男","departmentid":"2","qq":"","mail":"","phone":"","jzdh":"","address":"","url":"","addtime":"2014-12-19 15:45:14","pnum":"0","classid":"0","lastloginip":"127.0.0.1","lastlogintime":"1448973590","logincount":"1","isconfirm":"1","companyId":"0","leixing":"0","huanxin_account":"","huanxin_password":"","userId":"30260","image":"","className":"企业指导教师"}]
     * total : 1
     */

    /**
     * data : {"rows":[{"id":"30260","username":"cteacher1","compellation":"马云祥","sex":"男","departmentid":"2","qq":"","mail":"","phone":"","jzdh":"","address":"","url":"","addtime":"2014-12-19 15:45:14","pnum":"0","classid":"0","lastloginip":"127.0.0.1","lastlogintime":"1448973590","logincount":"1","isconfirm":"1","companyId":"0","leixing":"0","huanxin_account":"","huanxin_password":"","userId":"30260","image":"","className":"企业指导教师"}],"total":"1"}
     * success : true
     * error_code : 0
     * error :
     */

    public static class DataEntity implements Serializable {
        public String total;
        /**
         * id : 30260
         * username : cteacher1
         * compellation : 马云祥
         * sex : 男
         * departmentid : 2
         * qq :
         * mail :
         * phone :
         * jzdh :
         * address :
         * url :
         * addtime : 2014-12-19 15:45:14
         * pnum : 0
         * classid : 0
         * lastloginip : 127.0.0.1
         * lastlogintime : 1448973590
         * logincount : 1
         * isconfirm : 1
         * companyId : 0
         * leixing : 0
         * huanxin_account :
         * huanxin_password :
         * userId : 30260
         * image :
         * className : 企业指导教师
         */

        public List<RowsEntity> rows;

        public static class RowsEntity  implements Serializable {
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
            public String userId;
            public String image;
            public String className;
            public String sortLetters;//显示数据拼音的首字母
        }
    }
}
