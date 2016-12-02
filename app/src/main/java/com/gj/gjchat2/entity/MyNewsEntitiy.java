package com.gj.gjchat2.entity;


import com.gj.gjlibrary.base.BaseEntity;

import java.util.List;

/**
 * Created by guojing on 2016/4/14.
 * 我的动态列表
 */
public class MyNewsEntitiy extends BaseEntity<MyNewsEntitiy.DataEntity> {

    /**
     * rows : [{"id":"16","userId":"30299","content":"Fhhfuhgjufhhjgf ","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459994715","commentCount":"4","supportCount":"1","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"19","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"18","userId":"30299","fileId":"449","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160405/20160405162020757.jpg"},{"id":"17","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"16","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"15","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"14","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"13","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"12","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"11","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"}],"isSupport":1},{"id":"15","userId":"30299","content":"","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459931909","commentCount":"0","supportCount":"0","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"10","userId":"30299","fileId":"446","wqId":"15","createTime":"1459931909","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"}],"isSupport":0},{"id":"14","userId":"30299","content":"Ffgxcvgzzgffg ","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459931890","commentCount":"0","supportCount":"1","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[],"isSupport":1},{"id":"13","userId":"30299","content":"Ffgxcvgzzgffg ","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459931873","commentCount":"0","supportCount":"0","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[],"isSupport":0},{"id":"12","userId":"30299","content":"256376585","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459847367","commentCount":"0","supportCount":"0","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"9","userId":"30299","fileId":"449","wqId":"12","createTime":"1459847367","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160405/20160405162020757.jpg"}],"isSupport":0},{"id":"11","userId":"30299","content":" Fdbhdhcvhdhkddtsdfsff","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459847315","commentCount":"0","supportCount":"0","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"8","userId":"30299","fileId":"446","wqId":"11","createTime":"1459847315","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"7","userId":"30299","fileId":"445","wqId":"11","createTime":"1459847315","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"}],"isSupport":0}]
     * total : 10
     */

    /**
     * data : {"rows":[{"id":"16","userId":"30299","content":"Fhhfuhgjufhhjgf ","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459994715","commentCount":"4","supportCount":"1","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"19","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"18","userId":"30299","fileId":"449","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160405/20160405162020757.jpg"},{"id":"17","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"16","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"15","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"14","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"13","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"},{"id":"12","userId":"30299","fileId":"446","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"11","userId":"30299","fileId":"445","wqId":"16","createTime":"1459994715","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"}],"isSupport":1},{"id":"15","userId":"30299","content":"","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459931909","commentCount":"0","supportCount":"0","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"10","userId":"30299","fileId":"446","wqId":"15","createTime":"1459931909","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"}],"isSupport":0},{"id":"14","userId":"30299","content":"Ffgxcvgzzgffg ","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459931890","commentCount":"0","supportCount":"1","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[],"isSupport":1},{"id":"13","userId":"30299","content":"Ffgxcvgzzgffg ","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459931873","commentCount":"0","supportCount":"0","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[],"isSupport":0},{"id":"12","userId":"30299","content":"256376585","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459847367","commentCount":"0","supportCount":"0","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"9","userId":"30299","fileId":"449","wqId":"12","createTime":"1459847367","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160405/20160405162020757.jpg"}],"isSupport":0},{"id":"11","userId":"30299","content":" Fdbhdhcvhdhkddtsdfsff","status":"1","depId":"26","bjId":"8","postId":"26","createTime":"1459847315","commentCount":"0","supportCount":"0","terminalId":"0","compellation":"严辉福","image":"http://192.168.2.212:8006/upload/201604/20160411171315163.jpg","imageList":[{"id":"8","userId":"30299","fileId":"446","wqId":"11","createTime":"1459847315","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164621644.jpg"},{"id":"7","userId":"30299","fileId":"445","wqId":"11","createTime":"1459847315","terminalId":"0","image":"http://192.168.2.212:8006/app/upload/file/20160401/20160401164444715.jpg"}],"isSupport":0}],"total":10}
     * success : true
     * error_code : 0
     * error :
     */


    public static class DataEntity {
        public int total;
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
         * isSupport : 1
         */

        public List<RowsEntity> rows;

        public static class RowsEntity {
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

            public static class ImageListEntity {
                public String id;
                public String userId;
                public String fileId;
                public String wqId;
                public String createTime;
                public String terminalId;
                public String image;
            }
        }
    }
}
