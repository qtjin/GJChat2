package com.gj.gjchat2.model;

import com.gj.gjchat2.entity.MyNewsMsgEntitiy;
import com.gj.gjchat2.network.Network;
import com.gj.gjchat2.ui.activity.pyq.MyNewsMsgActivity;
import com.gj.gjlibrary.base.BaseModel;
import com.gj.gjlibrary.util.logger.AbLog;


import rx.Observable;

/**
 * Created by guojing on 2016/3/18.
 */
public class MyNewsMsgModel extends BaseModel<MyNewsMsgEntitiy> {


    public MyNewsMsgModel(MyNewsMsgActivity myNewsMsgActivity){
       super(myNewsMsgActivity);
    }


    /**
     * 取动态消息列表
     */
    public void getMyNewsMessageList(String userId, String page, String pageSize){

        String[] strs = new String[]{"userId=" + userId,
                "page=" + page,
                "rows=" + pageSize
        };
        String sign = Network.encrypt(strs); //加密

        AbLog.i("sign: "+sign);


        Observable<MyNewsMsgEntitiy> observable = Network.getApi()
                .getMyNewsMessageList(userId,page,pageSize,sign);

        doNetWorkEntity(observable); //返回JavaBean的

    }

}
