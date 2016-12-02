package com.gj.gjchat2.entity;


import com.gj.gjlibrary.base.BaseEntity;

import java.util.List;

/**
 * Created by guojing on 2016/4/14.
 * 我的动态详情
 */
public class MyNewsDetailEntitiy extends BaseEntity<MyNewsDetailEntitiy.DataEntity> {


    /**
     * id : 16
     * userId : 30299
     * content : Fhhfuhgjufhhjgf
     * status : 1
     * depId : 26
     * bjId : 8
     * postId : 26
     * createTime : 1459994715
     * commentCount : 4
     * supportCount : 1
     * terminalId : 0
     * compellation : 严辉福
     * image : http://192.168.2.212:8006/upload/201604/20160411171315163.jpg
     * imageList : [{"id":"19","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"18","userId":"30299","fileId":"449","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160405/20160405162020757.jpg"},{"id":"17","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"16","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"15","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"14","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"13","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"12","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"11","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"}]
     * supportList : [{"id":"5","userId":"30299","wqId":"16","createTime":"1460365621","toUserId":"30299","terminalId":"0","compellation":"严辉福"}]
     * commentList : [{"id":"1","userId":"30299","wqId":"16","content":"YouTube ","createTime":"1460096318","pid":"0","toUserId":"30299","status":"1","terminalId":"0","compellation":"严辉福","toCompellation":"严辉福"},{"id":"2","userId":"30299","wqId":"16","content":"Oops ","createTime":"1460096758","pid":"0","toUserId":"30299","status":"1","terminalId":"0","compellation":"严辉福","toCompellation":"严辉福"},{"id":"3","userId":"30299","wqId":"16","content":"Uyhbvv","createTime":"1460099853","pid":"0","toUserId":"30299","status":"1","terminalId":"0","compellation":"严辉福","toCompellation":"严辉福"},{"id":"4","userId":"30299","wqId":"16","content":"You ","createTime":"1460351323","pid":"3","toUserId":"30299","status":"1","terminalId":"0","compellation":"严辉福","toCompellation":"严辉福"}]
     * isSupport : 1
     */

    /**
     * data : {"id":"16","userId":"30299","content":"Fhhfuhgjufhhjgf ","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459994715","commentCount":"4","supportCount":"1","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"19","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"18","userId":"30299","fileId":"449","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160405/20160405162020757.jpg"},{"id":"17","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"16","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"15","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"14","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"13","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"12","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"11","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"}],"supportList":[{"id":"5","userId":"30299","wqId":"16","createTime":"1460365621","toUserId":"30299","terminalId":"0","compellation":"严辉福"}],"commentList":[{"id":"1","userId":"30299","wqId":"16","content":"YouTube ","createTime":"1460096318","pid":"0","toUserId":"30299","status":"1","terminalId":"0","compellation":"严辉福","toCompellation":"严辉福"},{"id":"2","userId":"30299","wqId":"16","content":"Oops ","createTime":"1460096758","pid":"0","toUserId":"30299","status":"1","terminalId":"0","compellation":"严辉福","toCompellation":"严辉福"},{"id":"3","userId":"30299","wqId":"16","content":"Uyhbvv","createTime":"1460099853","pid":"0","toUserId":"30299","status":"1","terminalId":"0","compellation":"严辉福","toCompellation":"严辉福"},{"id":"4","userId":"30299","wqId":"16","content":"You ","createTime":"1460351323","pid":"3","toUserId":"30299","status":"1","terminalId":"0","compellation":"严辉福","toCompellation":"严辉福"}],"isSupport":1}
     * success : true
     * error_code : 0
     */

    public static class DataEntity {
        public String id;
        public String userId;
        public String content;
        public String status;
        public String depId;
        public String bjId;
        public String postId;
        public String createTime;
        public String commentCount;
        public String supportCount;
        public String terminalId;
        public String compellation;
        public String image;
        public int isSupport;
        /**
         * id : 19
         * userId : 30299
         * fileId : 445
         * wqId : 16
         * createTime : 1459994715
         * terminalId : 0
         * image : http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg
         */

        public List<ImageListEntity> imageList;
        /**
         * id : 5
         * userId : 30299
         * wqId : 16
         * createTime : 1460365621
         * toUserId : 30299
         * terminalId : 0
         * compellation : 严辉福
         */

        public List<SupportListEntity> supportList;
        /**
         * id : 1
         * userId : 30299
         * wqId : 16
         * content : YouTube
         * createTime : 1460096318
         * pid : 0
         * toUserId : 30299
         * status : 1
         * terminalId : 0
         * compellation : 严辉福
         * toCompellation : 严辉福
         */

        public List<CommentListEntity> commentList;

        public static class ImageListEntity {
            public String id;
            public String userId;
            public String fileId;
            public String wqId;
            public String createTime;
            public String terminalId;
            public String image;
        }

        public static class SupportListEntity {
            public String id;
            public String userId;
            public String wqId;
            public String createTime;
            public String toUserId;
            public String terminalId;
            public String compellation;
        }

        public static class CommentListEntity {
            public String id;
            public String userId;
            public String wqId;
            public String content;
            public String createTime;
            public String pid;
            public String toUserId;
            public String status;
            public String terminalId;
            public String compellation;
            public String toCompellation;
        }
    }
}
