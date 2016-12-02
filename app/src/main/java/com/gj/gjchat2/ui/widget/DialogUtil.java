package com.gj.gjchat2.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjlibrary.util.ImageLoaderHelper;
import com.gj.gjlibrary.util.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * Created by guojing on 2016/4/6.
 */
public class DialogUtil {

    private android.support.v7.app.AlertDialog goGroupChatDialog;
    private android.support.v7.app.AlertDialog alertDialog;
    private Context context;

    private String title;
    private String describe;


    public interface CallbackListener {
        public void callback(String str); //回调方法
    }
    private CallbackListener callbackListener;


    public interface CallbackPositionListener {
        public void callbackPosition(int position); //删除某张图片时的回调方法
    }
    private CallbackPositionListener callbackPositionListener;

    public DialogUtil(Context context) {
        this.context = context;
    }

    public DialogUtil(Context context, CallbackListener callbackListener) {
        this.context = context;
        this.callbackListener = callbackListener;
    }

    public DialogUtil(Context context, String title, String describe, CallbackListener callbackListener) {
        this.context = context;
        this.title = title;
        this.describe = describe;
        this.callbackListener = callbackListener;
    }

    public DialogUtil(Context context, CallbackPositionListener callbackPositionListener) {
        this.context = context;
        this.callbackPositionListener = callbackPositionListener;
    }



    public void showBigImage(String url) {
        //final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(context).create();
        final Dialog alertDialog = new Dialog(context, R.style.Dialog_Fullscreen);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.START | Gravity.TOP;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL; //让AlertDialog全屏显示
        params.width = AppContext.SCREEN_WIDTH;
        params.height = AppContext.SCREEN_HEIGHT;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_img);
        PhotoView mphotoView = (PhotoView) window.findViewById(R.id.mPhotoView);
        ImageLoader.getInstance().displayImage(url, mphotoView, ImageLoaderHelper.getInstance(context).getDisplayOptionsPic());

        mphotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() { //点击一次就关闭
            @Override
            public void onViewTap(View view, float x, float y) {
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 显示多张全屏图片
     * @param imgUrls
     */
    public void showBigImageList(List<String> imgUrls,int position) {
        //final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(context).create();
        final Dialog alertDialog = new Dialog(context,R.style.Dialog_Fullscreen);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //params.gravity = Gravity.START | Gravity.TOP;
        //params.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL; //让AlertDialog全屏显示
        params.width = AppContext.SCREEN_WIDTH;
        params.height = AppContext.SCREEN_HEIGHT;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_view_pager);
        MyViewPager vPager = (MyViewPager) window.findViewById(R.id.vPager);
        TextView tv_num = (TextView) window.findViewById(R.id.tv_num);

        new ViewPagerUtil(context,alertDialog,vPager,tv_num,imgUrls,position).dispalyImg();
    }

    /**
     * 显示多张全屏图片可删除
     * @param imgUrls
     */
    public void showBigImageListCanDelete(List<String> imgUrls,int position) {
        //final android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(context).create();
        final Dialog alertDialog = new Dialog(context,R.style.Dialog_Fullscreen);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        //params.gravity = Gravity.START | Gravity.TOP;
        //params.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL; //让AlertDialog全屏显示
        params.width = AppContext.SCREEN_WIDTH;
        params.height = AppContext.SCREEN_HEIGHT;
        alertDialog.getWindow().setAttributes(params);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_view_pager_can_delete);
        MyViewPager vPager = (MyViewPager) window.findViewById(R.id.vPager);
        TextView tv_num = (TextView) window.findViewById(R.id.tv_num);
        ImageView iv_delete = (ImageView) window.findViewById(R.id.iv_delete);
        new ViewPagerUtil(context, alertDialog, vPager, tv_num, iv_delete, imgUrls, position
                , new ViewPagerUtil.CallbackPositionListener() {
                    @Override
                    public void callbackPosition(int position) {
                           callbackPositionListener.callbackPosition(position); //将需要删除的图片回调给activity
                    }
           }).dispalyImg();
    }


    /**
     * 显示输入回复内容的编辑框
     * String hintStr 默认输入显示
     */
    public void showDialogInput(String hintStr) {
        if (alertDialog == null) {
            alertDialog = new android.support.v7.app.AlertDialog.Builder(context).create();
        }

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes(); // 获取对话框当前的参数值
        params.gravity = Gravity.BOTTOM;
        params.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL; //让AlertDialog全屏显示
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); //默认弹出软键盘
/*        //显示位置
        params.x= AppContext.SCREEN_WIDTH-dip2px(context,100+12);
        params.y= dip2px(context,paddingVal);*/

        //window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        LayoutInflater inflater = LayoutInflater.from(context);
        View textEntryView = inflater.inflate(R.layout.dialog_input, null);
        //alertDialog.setView(((Activity) context).getLayoutInflater().inflate(R.layout.dialog_input, null));
        alertDialog.setView(textEntryView, 0, 0, 0, 0);

        window.setContentView(textEntryView);

        final EditText et_input = (EditText) window.findViewById(R.id.et_input);

        TextView tv_send = (TextView) window.findViewById(R.id.tv_send);

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_input.getText().toString();
                if (null == content || "".equals(content)) {
                    ToastUtils.show(context, "回复内容不能为空");
                } else {
                    alertDialog.dismiss();
                    callbackListener.callback(content); //回调方法
                }
            }
        });
        alertDialog.show();
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 显示输入群聊名称的编辑框
     */
    public void showDialog() {
        if (alertDialog == null) {
            alertDialog = new android.support.v7.app.AlertDialog.Builder(context).create();
        }
/*        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.CENTER;
        //params.type = WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL; //让AlertDialog全屏显示
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        alertDialog.getWindow().setAttributes(params);*/

        LayoutInflater inflater = LayoutInflater.from(context);
        View textEntryView = inflater.inflate(R.layout.dialog_edit_group_chat_name, null);
        alertDialog.setView(((Activity) context).getLayoutInflater().inflate(R.layout.dialog_edit_group_chat_name, null));

        alertDialog.show();

        Window window = alertDialog.getWindow();
        window.setContentView(textEntryView);
        window.setBackgroundDrawableResource(R.drawable.shape_frame); //这一句很关键，不设置的话圆角出不来

        final EditText et_group_chat_name = (EditText) window.findViewById(R.id.et_group_chat_name);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        TextView tv_describe = (TextView) window.findViewById(R.id.tv_describe);
        TextView tv_cancel = (TextView) window.findViewById(R.id.tv_cancel);
        TextView tv_ok = (TextView) window.findViewById(R.id.tv_ok);

        if (!"".equals(title) && null != title) {
            tv_title.setText(title);
        }
        if (!"".equals(describe) && null != describe) {
            tv_describe.setText(describe);
        }

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_group_chat_name.getText().toString();
                if (null == name || "".equals(name)) {
                    ToastUtils.show(context, "名称不能为空");
                } else {
                    alertDialog.dismiss();
                    callbackListener.callback(name); //回调方法
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


}
