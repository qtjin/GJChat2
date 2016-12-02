package com.gj.gjchat2.entity;


import com.gj.gjlibrary.base.BaseEntity;

import java.util.List;

/**
 * Created by guojing on 2016/4/14.
 * 我的动态消息列表
 */
public class MyNewsMsgEntitiy extends BaseEntity<MyNewsMsgEntitiy.DataEntity> {


    /**
     * rows : [{"id":"9","rowId":"5","userId":"30299","toUserId":"30299","wqId":"16","commentId":"0","content":"","type":"1","createTime":"1460365621","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"8","rowId":"4","userId":"30299","toUserId":"30299","wqId":"16","commentId":"3","content":"You ","type":"2","createTime":"1460351323","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"6","rowId":"3","userId":"30299","toUserId":"30299","wqId":"14","commentId":"0","content":"","type":"1","createTime":"1460106637","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Ffgxcvgzzgffg ","image":""},{"id":"4","rowId":"3","userId":"30299","toUserId":"30299","wqId":"16","commentId":"0","content":"Uyhbvv","type":"2","createTime":"1460099853","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"3","rowId":"2","userId":"30299","toUserId":"30299","wqId":"16","commentId":"0","content":"Oops ","type":"2","createTime":"1460096758","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"2","rowId":"1","userId":"30299","toUserId":"30299","wqId":"16","commentId":"0","content":"YouTube ","type":"2","createTime":"1460096318","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"}]
     * total : 6
     */

    /**
     * data : {"rows":[{"id":"9","rowId":"5","userId":"30299","toUserId":"30299","wqId":"16","commentId":"0","content":"","type":"1","createTime":"1460365621","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"8","rowId":"4","userId":"30299","toUserId":"30299","wqId":"16","commentId":"3","content":"You ","type":"2","createTime":"1460351323","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"6","rowId":"3","userId":"30299","toUserId":"30299","wqId":"14","commentId":"0","content":"","type":"1","createTime":"1460106637","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Ffgxcvgzzgffg ","image":""},{"id":"4","rowId":"3","userId":"30299","toUserId":"30299","wqId":"16","commentId":"0","content":"Uyhbvv","type":"2","createTime":"1460099853","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"3","rowId":"2","userId":"30299","toUserId":"30299","wqId":"16","commentId":"0","content":"Oops ","type":"2","createTime":"1460096758","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"2","rowId":"1","userId":"30299","toUserId":"30299","wqId":"16","commentId":"0","content":"YouTube ","type":"2","createTime":"1460096318","terminalId":"0","url":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","compellation":"严辉福","wqContent":"Fhhfuhgjufhhjgf ","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"}],"total":6}
     * success : true
     * error_code : 0
     * error :
     */

    public static class DataEntity {
        public int total;
        /**
         * id : 9
         * rowId : 5
         * userId : 30299
         * toUserId : 30299
         * wqId : 16
         * commentId : 0
         * content :
         * type : 1
         * createTime : 1460365621
         * terminalId : 0
         * url : http://192.168.2.212:8006/upload/201604/20160411171315163.jpg
         * compellation : 严辉福
         * wqContent : Fhhfuhgjufhhjgf
         * image : http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg
         */

        public List<RowsEntity> rows;

        public static class RowsEntity {
            public String id;
            public String rowId;
            public String userId;
            public String toUserId;
            public String wqId;
            public String commentId;
            public String content;
            public String type;
            public String createTime;
            public String terminalId;
            public String url;
            public String compellation;
            public String wqContent;
            public String image;
        }
    }
}
