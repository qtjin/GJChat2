package com.gj.gjchat2.ui.widget.PictureSelectionUtils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.ui.widget.MyPictureSelection.FilterImageView;
import com.gj.gjchat2.ui.widget.MyPictureSelection.LocalImageHelper;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.PermissionGrantUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by caizhongchi on 16/3/31.
 * 获取到  本地相册  list 集合列表查看
 */

public class LocalAlbum extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.im_left_back)
     FilterImageView im_left_back;

    @Bind(R.id.progress_bar)
     ImageView progress;

    @Bind(R.id.local_album_list)
     ListView listView;

    @Bind(R.id.iv_camera)
     FilterImageView iv_camera;

    LocalImageHelper helper;
    List<String> folderNames;

    protected boolean isDestroy;

    public static final String TAG = "LocalAlbum";
    private View mLayout;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_picture_select;
    }

    @Override
    protected void initData() {
        isDestroy = true;
        helper = LocalImageHelper.getInstance();
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void initListener() {
        iv_camera.setOnClickListener(this);
        im_left_back.setOnClickListener(this);

        iv_camera.setVisibility(View.GONE);
        ((View) progress.getParent()).setVisibility(View.GONE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启线程初始化本地图片列表，该方法是synchronized的，因此当AppContent在初始化时，此处阻塞
                LocalImageHelper.getInstance().initImage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //初始化完毕后，显示文件夹列表
                        initAdapter();
                        listView.setVisibility(View.VISIBLE);
                        iv_camera.setVisibility(View.VISIBLE);
                    }
                });
            }
        }).start();

        //给相册列表设置点击
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI ,LocalAlbum.this, LocalAlbumDetail.class);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(AppContext.getAppContext().LOCAL_FOLDER_NAME, folderNames.get(i));
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:

                if (LocalImageHelper.getInstance().getCurrentSize() + LocalImageHelper.getInstance().getCheckedItems().size() >= 9) {
                    Toast.makeText(LocalAlbum.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    /**
                     * 先检查是否获取相机拍照的权限
                     */
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        boolean isGrantPermission = PermissionGrantUtil.getInstance(LocalAlbum.this,mLayout).checkPermissionActivity(Manifest.permission.CAMERA,PermissionGrantUtil.CAMERA);
                        if(isGrantPermission){
                            goCamera();
                        }
                    }else{
                        goCamera();
                    }
                }
                break;
            case R.id.im_left_back:
                finish();
                break;
        }
    }

    /**
     * 访问相机
     */
    public void goCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //  拍照后保存图片的绝对路径
        String cameraPath = LocalImageHelper.getInstance().setCameraImgPath();
        File file = new File(cameraPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    public void initAdapter() {
        listView.setAdapter(new FolderAdapter(this, helper.getFolderMap()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                    String cameraPath = LocalImageHelper.getInstance().getCameraImgPath();
                    if (StringUtils.isEmpty(cameraPath)) {
                        Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    File file = new File(cameraPath);
                    if (file.exists()) {
                        Uri uri = Uri.fromFile(file);
                        LocalImageHelper.LocalFile localFile = new LocalImageHelper.LocalFile();
                        localFile.setThumbnailUri(uri.toString());
                        localFile.setOriginalUri(uri.toString());
                        localFile.setOrientation(getBitmapDegree(cameraPath));
                        LocalImageHelper.getInstance().getCheckedItems().add(localFile);
                        LocalImageHelper.getInstance().setResultOk(true);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        //这里本来有个弹出progressDialog的，在拍照结束后关闭，但是要延迟1秒，原因是由于三星手机的相机会强制切换到横屏，
                        //此处必须等它切回竖屏了才能结束，否则会有异常
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1000);
                    } else {
                        Toast.makeText(this, "图片获取失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 读取图片的旋转的角度，还是三星的问题，需要根据图片的旋转角度正确显示
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public class FolderAdapter extends BaseAdapter {
        Map<String, List<LocalImageHelper.LocalFile>> folders;
        Context context;
        DisplayImageOptions options;

        FolderAdapter(Context context, Map<String, List<LocalImageHelper.LocalFile>> folders) {
            this.folders = folders;
            this.context = context;
            folderNames = new ArrayList<>();

            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .showImageForEmptyUri(R.drawable.ic_default_picture)
                    .showImageOnFail(R.drawable.ic_default_picture)
                    .showImageOnLoading(R.drawable.ic_default_picture)
                    .bitmapConfig(Bitmap.Config.RGB_565)
//                    .setImageSize(new ImageSize(((AppContext) context.getApplicationContext()).getQuarterWidth(), 0))
                    .displayer(new SimpleBitmapDisplayer()).build();

            Iterator iter = folders.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                folderNames.add(key);
            }
            //根据文件夹内的图片数量降序显示
            Collections.sort(folderNames, new Comparator<String>() {
                public int compare(String arg0, String arg1) {
                    Integer num1 = helper.getFolder(arg0).size();
                    Integer num2 = helper.getFolder(arg1).size();
                    return num2.compareTo(num1);
                }
            });
        }

        @Override
        public int getCount() {
            return folders.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_albumfoler, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String name = folderNames.get(i);
            List<LocalImageHelper.LocalFile> files = folders.get(name);
            viewHolder.textView.setText(name + "(" + files.size() + ")");
            if (files.size() > 0) {
                ImageLoader.getInstance().displayImage(files.get(0).getThumbnailUri(), new ImageViewAware(viewHolder.imageView), options,
                        null, null);
            }
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionGrantUtil.CAMERA) {
            if (PermissionGrantUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                Snackbar.make(mLayout, R.string.permision_available,
                        Snackbar.LENGTH_SHORT)
                        .show();
                goCamera();
            } else {
                Log.i(TAG, "Contacts permissions were NOT granted.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}



