package com.gj.gjchat2.network;

import com.gj.gjchat2.entity.FriendNewsEntitiy;
import com.gj.gjchat2.entity.MyNewsDetailEntitiy;
import com.gj.gjchat2.entity.MyNewsEntitiy;
import com.gj.gjchat2.entity.MyNewsMsgEntitiy;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by guojing on 2016/11/25.
 * 业务接口
 */

public interface Api {

    /***********************************  朋友圈  ***********************************/
    /**
     * 朋友圈大厅（好友动态列表）
     */
   @GET("app/app/weiquan/steacher")
    Observable<FriendNewsEntitiy> getHallList(@Query("userId") String userId, @Query("page") String page, @Query("rows") String rows, @Query("sign") String sign);

    /**
     * 点赞
     */
    @FormUrlEncoded
    @POST("app/app/support/add")
    Observable<ResponseBody> addZan(@Field("userId") String userId, @Field("wqId") String wqId, @Field("sign") String sign);

    /**
     * 取消赞
     */
    @FormUrlEncoded
    @POST("app/app/support/cancle")
    Observable<ResponseBody> cancleZan(@Field("userId") String userId, @Field("wqId") String wqId, @Field("sign") String sign);

    /**
     * 添加评论
     */
    @FormUrlEncoded
    @POST("app/app/comment/add")
    Observable<ResponseBody> addCommment(@Field("userId") String userId, @Field("wqId") String wqId,
                                         @Field("content") String content, @Field("commentId") String commentId, @Field("sign") String sign);
   /**
    * 我的动态列表
    */
   @FormUrlEncoded
   @POST("app/app/weiquan/my")
   Observable<MyNewsEntitiy> getMyNewsList(@Field("userId") String userId, @Field("page") String page, @Field("rows") String rows, @Field("sign") String sign);

   /**
    * 我的动态详情
    */
   @FormUrlEncoded
   @POST("app/app/weiquan/detail")
   Observable<MyNewsDetailEntitiy> getMyNewsDetail(@Field("userId") String userId, @Field("wqId") String wqId, @Field("sign") String sign);

   /**
    * 我的动态消息列表
    */
   @FormUrlEncoded
   @POST("app/app/sxqmessage/index")
   Observable<MyNewsMsgEntitiy> getMyNewsMessageList(@Field("userId") String userId, @Field("page") String page, @Field("rows") String rows, @Field("sign") String sign);

   /**
    * 发朋友圈
    */
   @FormUrlEncoded
   @POST("app/app/weiquan/add")
   Observable<ResponseBody> addNews(@Field("userId") String userId, @Field("content") String content, @Field("fileId") String fileId, @Field("sign") String sign);

    /***********************************  朋友圈  ***********************************/

    /**
     * 上传图片
     */
    @Multipart
    @POST("app/app/upload/image")
    Observable<ResponseBody> uploadImage(@Part("userId") RequestBody requestUserId,@Part MultipartBody.Part file,@Part("sign") RequestBody requestSign);


}
