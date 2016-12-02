package com.gj.gjlibrary.base;

import android.widget.Toast;

import com.gj.gjlibrary.util.ToastUtils;
import com.gj.gjlibrary.util.logger.AbLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by guojing on 2016/11/25.
 * 业务模块模型基类
 */

public abstract class BaseModel<T> {

    protected BaseActivity baseActivity;

    protected String SUCCESS_STR;

    protected String ERROR_STR;

    public BaseModel(BaseActivity baseActivity){
        this.baseActivity = baseActivity;
    }

    public interface CallBackListener{ //访问网络获取JSON数据之后回调给UI界面的回调接口
        public void callBack(boolean result,String json);
    }

    /**
     * 返回实体类的
     * @param observable
     */
    protected void doNetWorkEntity(Observable<T> observable){
        baseActivity.showProgressDialog();
        Observer<T> observer = new Observer<T>() {

            @Override
            public void onCompleted() {
                baseActivity.hideProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                baseActivity.hideProgressDialog();
            }

            @Override
            public void onNext(T entity) {
                baseActivity.hideProgressDialog();
                baseActivity.pressData((BaseEntity) entity);
            }
        };

        baseActivity.subscription =
                 observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 返回JSON的
     * @param observable
     */
    protected void doNetWorkJson(Observable<ResponseBody> observable,final CallBackListener callBackListener){

        baseActivity.showProgressDialog();

        Observer<ResponseBody> observer = new Observer<ResponseBody>() {

            @Override
            public void onCompleted() {
                baseActivity.hideProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                baseActivity.hideProgressDialog();
                ToastUtils.show(baseActivity, ERROR_STR +e.toString(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                baseActivity.hideProgressDialog();
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
                        if (result) { //成功
                            if(null!=SUCCESS_STR&&!"".equals(SUCCESS_STR)){
                                ToastUtils.show(baseActivity, SUCCESS_STR, Toast.LENGTH_SHORT);
                            }
                        }else{
                            if(null!=ERROR_STR&&!"".equals(ERROR_STR)){
                                ToastUtils.show(baseActivity, ERROR_STR, Toast.LENGTH_SHORT);
                            }

                        }
                        if(null!=callBackListener){
                            callBackListener.callBack(result,json);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        baseActivity.subscription =
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);

    }

}
