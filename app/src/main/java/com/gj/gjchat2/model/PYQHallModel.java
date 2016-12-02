package com.gj.gjchat2.model;

import com.gj.gjchat2.entity.FriendNewsEntitiy;
import com.gj.gjchat2.network.Network;
import com.gj.gjchat2.ui.activity.pyq.PYQHallActivity;
import com.gj.gjlibrary.base.BaseModel;
import com.gj.gjlibrary.util.logger.AbLog;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by guojing on 2016/11/25.
 * 朋友圈大厅
 */

public class PYQHallModel extends BaseModel<FriendNewsEntitiy> {


    public PYQHallModel(PYQHallActivity hallActivity){
        super(hallActivity);
    }

    /**
     * 朋友圈大厅（好友动态列表）
     */
    public void getHallList(String userId, String page, String pageSize){

        String[] strs = new String[]{"userId=" + userId,
                "page=" + page,
                "rows=" + pageSize
        };
        String sign = Network.encrypt(strs); //加密

        AbLog.i("sign: "+sign);


        Observable<FriendNewsEntitiy> observable = Network.getApi()
                .getHallList(userId,page,pageSize,sign);

        doNetWorkEntity(observable); //返回JavaBean的

    }


    /**
     * 点赞
     */
    public void addZan(String userId, String wqId,final CallBackListener callBackListener){
        String[] strs = new String[]{"userId=" + userId,
                "wqId=" + wqId,
        };
        String sign = Network.encrypt(strs); //加密

        AbLog.i("sign: "+sign);

        super.SUCCESS_STR = "点赞成功";
        super.ERROR_STR = "点赞失败";

        Observable<ResponseBody> observable = Network.getApi().addZan(userId,wqId,sign);

        doNetWorkJson(observable,callBackListener); //返回Json的

    }
    /**
     * 取消赞
     */
    public void cancleZan(String userId, String wqId,final CallBackListener callBackListener){
        String[] strs = new String[]{"userId=" + userId,
                "wqId=" + wqId,
        };
        String sign = Network.encrypt(strs); //加密

        AbLog.i("sign: "+sign);


        super.SUCCESS_STR = "取消点赞成功";
        super.ERROR_STR = "取消点赞失败";

        Observable<ResponseBody> observable = Network.getApi().cancleZan(userId,wqId,sign);

        doNetWorkJson(observable,callBackListener); //返回Json的

    }
    /**
     * 添加评论
     */
    public void addComment(String userId, String wqId,String content,String commentId,final CallBackListener callBackListener){

        String[] strs;
        if(null!=commentId&&!"".equals(commentId)){
            strs = new String[]{"userId=" + userId,
                    "wqId=" + wqId,
                    "content=" + content,
                    "commentId=" + commentId
            };
        }else{
            strs = new String[]{"userId=" + userId,
                    "wqId=" + wqId,
                    "content=" + content,
            };
        }
        String sign = Network.encrypt(strs); //加密

        AbLog.i("sign: "+sign);

        super.SUCCESS_STR = "评论成功";
        super.ERROR_STR = "评论失败";

        Observable<ResponseBody> observable = Network.getApi().addCommment(userId,wqId,content,commentId,sign);

        doNetWorkJson(observable,callBackListener); //返回Json的



       /* Observer<ResponseBody> observer = new Observer<ResponseBody>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(hallActivity, "评论失败："+e.toString(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                BufferedSource source = responseBody.source();
                try {
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Buffer buffer = source.buffer();
                Charset UTF8 = Charset.forName("UTF-8");
                Charset charset = UTF8;
                MediaType contentType = responseBody.contentType();
                if(contentType != null){
                    charset = contentType.charset(UTF8);
                    String json = buffer.clone().readString(charset);
                    AbLog.i("json: "+json);
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        boolean result = jsonObject.getBoolean("success");
                        if (result) { //评论成功
                            ToastUtils.show(hallActivity, "评论成功", Toast.LENGTH_SHORT);
                            callBackListener.callBack(true);
                        }else{
                            ToastUtils.show(hallActivity, "评论失败", Toast.LENGTH_SHORT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        hallActivity.subscription = Network.getApi()
                .addCommment(userId,wqId,content,commentId,sign)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);*/

    }

}
