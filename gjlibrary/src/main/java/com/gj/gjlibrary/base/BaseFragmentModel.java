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

public abstract class BaseFragmentModel<T> {

    protected BaseFragment baseFragment;

    protected String SUCCESS_STR;

    protected String ERROR_STR;

    public BaseFragmentModel(BaseFragment baseFragment){
        this.baseFragment = baseFragment;
    }

    public interface CallBackListener{ //访问网络获取JSON数据之后回调给UI界面的回调接口
        public void callBack(boolean result);
    }

    /**
     * 返回实体类的
     * @param observable
     */
    protected void doNetWorkEntity(Observable<T> observable){
        baseFragment.showProgressDialog();
        Observer<T> observer = new Observer<T>() {

            @Override
            public void onCompleted() {
                baseFragment.hideProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                baseFragment.hideProgressDialog();
            }

            @Override
            public void onNext(T entity) {
                baseFragment.hideProgressDialog();
                baseFragment.pressData((BaseEntity) entity);
            }
        };

        baseFragment.subscription =
                 observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    /**
     * 返回JSON的
     * @param observable
     */
    protected void doNetWorkJson(Observable<ResponseBody> observable,final CallBackListener callBackListener){

        baseFragment.showProgressDialog();

        Observer<ResponseBody> observer = new Observer<ResponseBody>() {

            @Override
            public void onCompleted() {
                baseFragment.hideProgressDialog();
            }

            @Override
            public void onError(Throwable e) {
                baseFragment.hideProgressDialog();
                ToastUtils.show(baseFragment.getActivity(), ERROR_STR +e.toString(), Toast.LENGTH_SHORT);
            }

            @Override
            public void onNext(ResponseBody responseBody) {
                baseFragment.hideProgressDialog();
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
                        if (result) { //点赞成功
                            ToastUtils.show(baseFragment.getActivity(), SUCCESS_STR, Toast.LENGTH_SHORT);
                            callBackListener.callBack(true);
                        }else{
                            ToastUtils.show(baseFragment.getActivity(), ERROR_STR, Toast.LENGTH_SHORT);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        baseFragment.subscription =
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(observer);

    }

}
