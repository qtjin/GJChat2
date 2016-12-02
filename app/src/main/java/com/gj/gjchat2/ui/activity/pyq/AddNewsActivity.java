package com.gj.gjchat2.ui.activity.pyq;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.model.AddNewsModel;
import com.gj.gjchat2.ui.widget.DialogUtil;
import com.gj.gjchat2.ui.widget.MyPictureSelection.FilterImageView;
import com.gj.gjchat2.ui.widget.MyPictureSelection.LocalImageHelper;
import com.gj.gjchat2.ui.widget.PictureSelectionUtils.ImageUtils;
import com.gj.gjchat2.ui.widget.PictureSelectionUtils.LocalAlbum;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.base.BaseModel;
import com.gj.gjlibrary.util.ToastUtils;
import com.gj.gjlibrary.util.logger.AbLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;



/**
 * Created by guojing on 2016/3/18.
 * 实习动态发布
 */
public class AddNewsActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.tv_right)
     TextView tv_right;

    @Bind(R.id.et_content)
     EditText et_content;

    @Bind(R.id.ll_pic_container)
     LinearLayout ll_pic_container;

    @Bind(R.id.hs_scrollview)
     HorizontalScrollView hs_scrollview;

    @Bind(R.id.post_add_pic)
     FilterImageView post_add_pic;

    @Bind(R.id.tv_pic_number)
     TextView tv_pic_number;

    private AddNewsModel addNewsModel;

    public String content="";
    public String fileId=""; //fileId 文件上传返回的id;多个用“,”隔开

    public List<LocalImageHelper.LocalFile> pictures = new ArrayList<>();//图片路径数组

    private DisplayImageOptions options;

    public int index = 0; //递归索引，指向图片集合内的元素

    private List<String> imgUrls = new ArrayList<>();
    private DialogUtil dialogUtil;

    private String userId;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_sxq_add;
    }

    @Override
    protected void initData() {
        setTitle("编辑");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("发送");
    }

    @Override
    protected void initListener() {
        userId = CacheUtil.getUserId(AddNewsActivity.this);
        tv_right.setOnClickListener(this);
        post_add_pic.setOnClickListener(this);
        options = LocalImageHelper.getImageOptions();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                if (LocalImageHelper.getInstance().isResultOk()) {
                    LocalImageHelper.getInstance().setResultOk(false);
                    //获取选中的图片
                    List<LocalImageHelper.LocalFile> files = LocalImageHelper.getInstance().getCheckedItems();

                    /************************图片压缩*******************************/

                   /* String originalUri = files.get(0).getOriginalUri();
                    String thumbnailUri = files.get(0).getThumbnailUri();
                    AbLog.i("originalUri: "+originalUri);
                    AbLog.i("thumbnailUri: " + thumbnailUri);

                    String originalPath = PictureUtil.uriToSDcardPath(AddNewsActivity.this, originalUri);
                    String thumbnailPath = PictureUtil.uriToSDcardPath(AddNewsActivity.this, thumbnailUri);
                    AbLog.i("originalPath"+originalPath);
                    AbLog.i("thumbnailPath"+thumbnailPath);

                    File fileoriginal = new File(originalPath);
                    File filethumbnail = new File(thumbnailPath);

                    AbLog.i("fileoriginal size: " + fileoriginal.length());
                    AbLog.i("filethumbnail size: " + filethumbnail.length());

                    String originalSmallImgName = originalPath.substring(originalPath.lastIndexOf("/")+1);
                    String thumbnailSmallImgName = thumbnailPath.substring(originalPath.lastIndexOf("/")+1);

                    AbLog.i("originalSmallImgName: " + originalSmallImgName);
                    AbLog.i("thumbnailSmallImgName: " + thumbnailSmallImgName);

                    String originalSmallPath =  PictureUtil.yasuo(AddNewsActivity.this, originalSmallImgName, originalPath);
                    String thumbnailSmallPath =  PictureUtil.yasuo(AddNewsActivity.this, thumbnailSmallImgName, thumbnailPath);

                    AbLog.i("originalSmallPath"+originalSmallPath);
                    AbLog.i("thumbnailSmallPath"+thumbnailSmallPath);


                    File fileSmalloriginal = new File(originalSmallPath);
                    File fileSmallthumbnail = new File(thumbnailSmallPath);

                    AbLog.i("fileSmalloriginal size: " + fileSmalloriginal.length());
                    AbLog.i("fileSmallthumbnail size: " + fileSmallthumbnail.length());*/


                    /************************图片压缩*******************************/

                    for (int i = 0; i < files.size(); i++) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) getResources().getDimension(R.dimen.size_62dp)
                                , (int) getResources().getDimension(R.dimen.size_62dp));
                        params.rightMargin = (int) getResources().getDimension(R.dimen.padding_8);
                        FilterImageView imageView = new FilterImageView(this);
                        imageView.setLayoutParams(params);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        ImageLoader.getInstance().displayImage(files.get(i).getThumbnailUri(), new ImageViewAware(imageView), options,
                                null, null);
                        imageView.setOnClickListener(this);
                        pictures.add(files.get(i));
                        ll_pic_container.addView(imageView, ll_pic_container.getChildCount() - 1);
                        tv_pic_number.setText(pictures.size() + "/" + LocalImageHelper.getInstance().getMaxPictureNumber() + "");
                        LocalImageHelper.getInstance().setCurrentSize(pictures.size());

                        imgUrls.add(files.get(i).getThumbnailUri());
                    }
                    if (pictures.size() == LocalImageHelper.getInstance().getMaxPictureNumber()) {
                        post_add_pic.setVisibility(View.GONE);
                    } else {
                        post_add_pic.setVisibility(View.VISIBLE);
                    }
                    //清空选中的图片
                    files.clear();
                    //设置当前选中的图片数量
                    LocalImageHelper.getInstance().setCurrentSize(pictures.size());
                    //延迟滑动至最右边
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            hs_scrollview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                        }
                    }, 50L);
                }
                //清空选中的图片
                LocalImageHelper.getInstance().getCheckedItems().clear();
                break;
            default:
                break;
        }
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void getBundleExtras(Bundle extras) {
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.post_add_pic:
                //点击+号添加图片的
                Intent intent = new Intent(AddNewsActivity.this, LocalAlbum.class);
                //需要添加多选图片页面, 需要设置图片选择器,给他设置图片最大数量
                LocalImageHelper.getInstance().setMaxPictureNumber(9);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    //4.4及以上
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                } else {
                    //4.4以下
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                }
                startActivityForResult(intent, ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
                break;
            case R.id.tv_right:
                content = et_content.getText().toString();
                if((null==content||"".equals(content))&&pictures.size()==0){
                    ToastUtils.show(AddNewsActivity.this, "动态内容不能为空");
                }else{
                    if(addNewsModel==null){
                        addNewsModel = new AddNewsModel(AddNewsActivity.this);
                    }
                    if(null==content){
                        content="";
                    }
                    if(pictures.size()!=0){ //上传图文
                        //调用图片上传的接口获取图片url
                        //showProgressDialog("正在上传图片,请稍后");
                        addNewsModel.upLoadImage(userId,index);
                    }else if(null!=content&&!"".equals(content)){ //只上传文字
                        addNewsModel.addNews(userId,content,fileId, new BaseModel.CallBackListener() {
                            @Override
                            public void callBack(boolean result,String json) {
                                if(result){
                                    finish();
                                }
                            }
                        });
                    }
                }
                break;
            default:
                if (v instanceof FilterImageView) {
                    for (int i = 0; i < ll_pic_container.getChildCount(); i++) {
                        if (v == ll_pic_container.getChildAt(i)) {
                            //查看大图
                            if(null!=imgUrls&&imgUrls.size()>0){
                                if(dialogUtil==null){
                                    dialogUtil = new DialogUtil(AddNewsActivity.this, new DialogUtil.CallbackPositionListener() {
                                        @Override
                                        public void callbackPosition(int position) {
                                            AbLog.i("callbackPosition position: " + position);
                                            //当要删除图片的时候就会调用该回调方法
                                            pictures.remove(position);
                                            imgUrls.remove(position);
                                            ll_pic_container.removeViewAt(position);
                                            tv_pic_number.setText(pictures.size() + "/" + LocalImageHelper.getInstance().getMaxPictureNumber() + "");
                                            LocalImageHelper.getInstance().setCurrentSize(pictures.size());
                                            if (pictures.size() == LocalImageHelper.getInstance().getMaxPictureNumber()) {
                                                post_add_pic.setVisibility(View.GONE);
                                            } else {
                                                post_add_pic.setVisibility(View.VISIBLE);
                                            }
                                            hs_scrollview.invalidate(); //刷新
                                        }
                                    });
                                }
                                dialogUtil.showBigImageListCanDelete(imgUrls, i);
                            }
                        }
                    }
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空选中的图片
        LocalImageHelper.getInstance().clear();
    }
}
