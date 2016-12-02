package com.gj.gjchat2.model;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.gj.gjchat2.entity.UpLoadImageEntity;
import com.gj.gjchat2.network.Network;
import com.gj.gjchat2.ui.activity.pyq.AddNewsActivity;
import com.gj.gjlibrary.base.BaseModel;
import com.gj.gjlibrary.util.AbGsonUtil;
import com.gj.gjlibrary.util.ToastUtils;
import com.gj.gjlibrary.util.logger.AbLog;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;


/**
 * Created by guojing on 2016/3/18.
 * 我的实习圈
 */
public class AddNewsModel extends BaseModel {

    private AddNewsActivity addNewsActivity;

    public AddNewsModel(AddNewsActivity addNewsActivity){
        super(addNewsActivity);
        this.addNewsActivity = addNewsActivity;
    }

    private String picturePath;
    private StringBuffer buffer;
    /**
     * 上传图片
     */
    public void upLoadImage(final String userId,int index){
        //String url = addNewsActivity.pictures.get(index).getOriginalUri();
        String url = addNewsActivity.pictures.get(index).getThumbnailUri(); //缩略图
        //    路径/storage/emulated/0/C`bizhi/06.jpg
        Uri uu2 = Uri.parse(url);
        String[] filePathColumns = {MediaStore.Images.Media.DATA};
        Cursor c = addNewsActivity.getContentResolver().query(uu2, filePathColumns, null, null, null);
        if(null != c){
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            picturePath = c.getString(columnIndex);
            c.close();
        }
        //如果图片路径存在,才去申请
        if(null != picturePath){
            File file = new File(picturePath);

                String[] strs = new String[]{"userId=" + userId};
                String sign = Network.encrypt(strs); //加密


                //super.SUCCESS_STR = "上传成功";
                super.ERROR_STR = "上传失败";



                AbLog.i("upLoadImage picturePath: "+picturePath);
                AbLog.i("upLoadImage file.getName(): "+file.getName());
                AbLog.i("upLoadImage userId: "+userId);
                AbLog.i("upLoadImage sign: "+sign);

/*            String descriptionString = "userId="+userId+"&sign="+sign;
            RequestBody description =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), descriptionString);*/

            RequestBody requestFile  = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
            MultipartBody.Part imagPart =
                        MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            RequestBody requestUserId  = RequestBody.create(okhttp3.MediaType.parse("multipart/form-data"), userId);

            RequestBody requestSign  = RequestBody.create(okhttp3.MediaType.parse("multipart/form-data"), sign);

            Observable<ResponseBody> observable = Network.getApi().uploadImage(requestUserId,imagPart,requestSign);



                doNetWorkJson(observable, new CallBackListener() {
                    @Override
                    public void callBack(boolean result,String json) {
                        if(result){
                            UpLoadImageEntity entity = AbGsonUtil.json2Bean(json, UpLoadImageEntity.class);
                            if (buffer == null) {
                                buffer = new StringBuffer();
                            }
                            if(entity.data.fileId != null){
                                //addNewsActivity.hideProgressDialog();
                                if(addNewsActivity.index==addNewsActivity.pictures.size()-1){ //最后一张图片
                                    //addNewsActivity.hideProgressDialog();
                                    buffer.append(entity.data.fileId);
                                    addNewsActivity.fileId = buffer.toString();
                                    addNewsActivity.index=0;
                                    //发布实习动态
                                    addNews(userId,addNewsActivity.content,addNewsActivity.fileId ,new CallBackListener() {
                                        @Override
                                        public void callBack(boolean result, String json) {
                                            if(result){
                                                addNewsActivity.finish();
                                            }
                                        }
                                    });
                                }else{
                                    buffer.append(entity.data.fileId);
                                    buffer.append(",");
                                    upLoadImage(userId,++addNewsActivity.index);  //递归调用
                                }
                            }else{
                                //addNewsActivity.hideProgressDialog();
                                //ToastUtils.show(addNewsActivity, "图片上传失败,原因：fileId为空");
                                addNewsActivity.index=0;
                            }
                        }else{
                            addNewsActivity.index=0;
                        }
                    }
                }); //返回Json的
            }else{
                //addNewsActivity.hideProgressDialog();
                ToastUtils.show(addNewsActivity, "图片路径不存在");
                addNewsActivity.index=0;
            }
    }





    /**
     * 发朋友圈
     */
    public void addNews(String userId, String content,String fileId,final BaseModel.CallBackListener callBackListener){
        AbLog.i("addNews userId"+userId);
        AbLog.i("addNews content"+content);
        AbLog.i("addNews fileId"+fileId);
        String[] strs;
        if(null!=fileId&&!"".equals(fileId)){
            if(null!=content&&!"".equals(content)) {
                strs = new String[]{"userId=" + userId,
                        "content=" + content,
                        "fileId=" + fileId
                };
            }else{
                strs = new String[]{"userId=" + userId,
                        "fileId=" + fileId
                };
            }
        }else{
            strs = new String[]{"userId=" + userId,
                    "content=" + content
            };
        }
        String sign = Network.encrypt(strs); //加密

        AbLog.i("sign: "+sign);

        super.SUCCESS_STR = "发布成功";
        super.ERROR_STR = "发布失败";

        Observable<ResponseBody> observable = Network.getApi().addNews(userId,content,fileId,sign);

        doNetWorkJson(observable,callBackListener); //返回Json的

    }





}
