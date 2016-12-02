package com.gj.gjchat2.ui.widget.MyPictureSelection;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.ui.widget.PictureSelectionUtils.StringUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created caizhongchi on 16/3/31.
 *
 * 操作图片集合的工具
 */
public class LocalImageHelper {
    private static LocalImageHelper instance;
    private final AppContext context;
    private static DisplayImageOptions options;
    private static DisplayImageOptions roundedOptions;

    List<LocalFile> checkedItems = new ArrayList<>();
    /**
     * 最大的可选图片数量
     */
    private int maxPictureNumber = 9;
    /**
     * 当前选中得图片个数
     */
    private int currentSize;

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    /**
     * 设置最大可选的图片数量, 不设置就是 9张
     * @param maxPictureNumber
     */
    public void setMaxPictureNumber (int maxPictureNumber) {
        this.maxPictureNumber = maxPictureNumber;
    }

    /**
     * 获取最大可选图片数量, 默认是 9张
     * @return
     */
    public int getMaxPictureNumber () {
       return  maxPictureNumber;
    }

    public String getCameraImgPath() {
        return CameraImgPath;
    }

    public static DisplayImageOptions getImageOptions() {
        return options;
    }

    public static DisplayImageOptions getHeadImageOptions() {
        return roundedOptions;
    }

    public String setCameraImgPath() {
        String foloder= AppContext.getAppContext().getCachePath() + "/PostPicture/";
        File savedir = new File(foloder);
        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        // 照片命名
        String picName =  timeStamp + ".jpg";
        //  裁剪头像的绝对路径
        CameraImgPath = foloder + picName;
        return  CameraImgPath;
    }

    //拍照时指定保存图片的路径
    private String CameraImgPath;
    //大图遍历字段
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.ORIENTATION
    };
    //小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    final List<LocalFile> paths = new ArrayList<>();

    final Map<String, List<LocalFile>> folders = new HashMap<>();

    private LocalImageHelper(AppContext context) {
        this.context = context;
    }

    public Map<String, List<LocalFile>> getFolderMap() {
        return folders;
    }

    public static LocalImageHelper getInstance() {
        return instance;
    }

    public static void init(AppContext context) {
        instance = new LocalImageHelper(context);

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageForEmptyUri(R.drawable.ic_default_picture)
                .showImageOnFail(R.drawable.ic_default_picture)
                .showImageOnLoading(R.drawable.ic_default_picture)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .displayer(new SimpleBitmapDisplayer()).build();

        roundedOptions = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片
                //.showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
                //.showImageOnFail(R.drawable.icon)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                        //.decodingOptions(new Options().)//设置图片的解码配置
                        //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                        //设置图片加入缓存前，对bitmap进行设置
                        //.preProcessor(BitmapProcessor preProcessor)
                        //.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(8))//是否设置为圆角，弧度为多少
                .build();//构建完成

        new Thread(new Runnable() {
            @Override
            public void run() {
                instance.initImage();
            }
        }).start();
    }

    public boolean isInited() {
        return paths.size() > 0;
    }

    public List<LocalFile> getCheckedItems() {
        return checkedItems;
    }

    private boolean resultOk;

    public boolean isResultOk() {
        return resultOk;
    }

    public void setResultOk(boolean ok) {
        resultOk = ok;
    }

    private boolean isRunning = false;

    public synchronized void initImage() {
        if (isRunning){
            return;
        }
        isRunning=true;
        if (isInited())
            return;
        //获取大图的游标
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  // 大图URI
                STORE_IMAGES,   // 字段
                null,         // No where clause
                null,         // No where clause
                MediaStore.Images.Media.DATE_TAKEN + " DESC"); //根据时间升序
        if (cursor == null)
            return;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);//大图ID
            String path = cursor.getString(1);//大图路径
            File file = new File(path);
            //判断大图是否存在
            if (file.exists()) {
                //小图URI
                String thumbUri = getThumbnail(id, path);
                //获取大图URI
                String uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                        appendPath(Integer.toString(id)).build().toString();
                if(StringUtils.isEmpty(uri))
                    continue;
                if (StringUtils.isEmpty(thumbUri))
                    thumbUri = uri;
                //获取目录名
                String folder = file.getParentFile().getName();

                LocalFile localFile = new LocalFile();
                localFile.setOriginalUri(uri);
                localFile.setThumbnailUri(thumbUri);
                int degree = cursor.getInt(2);
                if (degree != 0) {
                    degree = degree + 180;
                }
                localFile.setOrientation(360-degree);

                paths.add(localFile);
                //判断文件夹是否已经存在
                if (folders.containsKey(folder)) {
                    folders.get(folder).add(localFile);
                } else {
                    List<LocalFile> files = new ArrayList<>();
                    files.add(localFile);
                    folders.put(folder, files);
                }
            }
        }
        folders.put("所有图片", paths);
        cursor.close();
        isRunning=false;
    }

    private String getThumbnail(int id, String path) {
        //获取大图的缩略图
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                new String[]{id + ""},
                null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int thumId = cursor.getInt(0);
            String uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon().
                    appendPath(Integer.toString(thumId)).build().toString();
            cursor.close();
            return uri;
        }
        cursor.close();
        return null;
    }

    public List<LocalFile> getFolder(String folder) {
        return folders.get(folder);
    }

    public void clear(){
        checkedItems.clear();
        currentSize = 0;
        String foloder= AppContext.getAppContext().getCachePath() + "/PostPicture/";
        File savedir = new File(foloder);
        if (savedir.exists()) {
            deleteFile(savedir);
        }
    }
    public void deleteFile(File file) {

        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
        } else {
        }
    }
    public static class LocalFile {
        private String originalUri;//原图URI
        private String thumbnailUri;//缩略图URI
        private int orientation;//图片旋转角度

        public LocalFile(String url){
            originalUri = url;
        }
        public LocalFile(){

        }

        public String getThumbnailUri() {
            return thumbnailUri;
        }

        public void setThumbnailUri(String thumbnailUri) {
            this.thumbnailUri = thumbnailUri;
        }

        public String getOriginalUri() {
            return originalUri;
        }

        public void setOriginalUri(String originalUri) {
            this.originalUri = originalUri;
        }


        public int getOrientation() {
            return orientation;
        }

        public void setOrientation(int exifOrientation) {
            orientation =  exifOrientation;
        }

    }

}
