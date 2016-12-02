package com.gj.gjchat2.model;

import com.gj.gjchat2.entity.MyNewsDetailEntitiy;
import com.gj.gjchat2.network.Network;
import com.gj.gjchat2.ui.activity.pyq.MyNewsDetailActivity;
import com.gj.gjlibrary.base.BaseModel;
import com.gj.gjlibrary.util.logger.AbLog;

import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by guojing on 2016/3/18.
 * 我的实习圈
 */
public class MyNewsDetailModel extends BaseModel<MyNewsDetailEntitiy> {


    public MyNewsDetailModel(MyNewsDetailActivity myNewsDetailActivity){
        super(myNewsDetailActivity);
    }

    /**
     * 取动态详情
     */
    public void getMyNewsDetail(String userId, String wqId){

        String[] strs = new String[]{"userId=" + userId,
                "wqId=" + wqId
        };
        String sign = Network.encrypt(strs); //加密

        AbLog.i("sign: "+sign);


        Observable<MyNewsDetailEntitiy> observable = Network.getApi()
                .getMyNewsDetail(userId,wqId,sign);

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

    }

}
