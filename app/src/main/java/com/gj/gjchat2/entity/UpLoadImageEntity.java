package com.gj.gjchat2.entity;

import com.gj.gjlibrary.base.BaseEntity;

import java.util.List;

/**
 * Created by caizhongchi on 2016/4/5.
 *
 * 上传图片返回结果的实体
 *
 */
public class UpLoadImageEntity extends BaseEntity<UpLoadImageEntity.DataEntity> {

    public static class DataEntity {

        public List<RowsEntity> rows;
        //实习工作详情里面的参数
        public String fileId;  //文件id
        public String fileUrl;  //文件url
        public String url;      //全路径，展示用

        public String userId;

        public static class RowsEntity {
            public String id;  //沟通方式id
            public String title;  //沟通方式名称
        }
    }
}
