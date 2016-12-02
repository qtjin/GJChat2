package com.gj.gjlibrary.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by guojing on 2016/12/1.
 * 申请权限工具类
 */

public class PermissionGrantUtil {

    public static final int READ_CALENDAR = 0;
    public static final int CAMERA = 1;
    public static final int READ_CONTACTS = 2;
    public static final int ACCESS_COARSE_LOCATION = 3;
    public static final int RECORD_AUDIO = 4;
    public static final int READ_PHONE_STATE = 5;
    public static final int BODY_SENSORS = 6;
    public static final int READ_SMS = 7;
    public static final int READ_EXTERNAL_STORAGE = 8;


    private static PermissionGrantUtil permissionGrantUtil;
    private Context context;
    private Activity activity;
    private Fragment fragment;
    private View mLayout;

    public static PermissionGrantUtil getInstance(Activity activity,View mLayout){
        if(null==permissionGrantUtil){
            permissionGrantUtil = new PermissionGrantUtil(activity,mLayout);
        }
        return permissionGrantUtil;
    }

    public static PermissionGrantUtil getInstance(Fragment fragment,View mLayout){
        if(null==permissionGrantUtil){
            permissionGrantUtil = new PermissionGrantUtil(fragment,mLayout);
        }
        return permissionGrantUtil;
    }

    public PermissionGrantUtil(Activity activity,View mLayout){
        this.activity = activity;
        this.context = activity;
        this.mLayout = mLayout;
    }

    public PermissionGrantUtil(Fragment fragment,View mLayout){
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.mLayout = mLayout;
    }

    public boolean checkPermissionActivity(final String permissionStr,final int requestCode){
        if (ActivityCompat.checkSelfPermission(context, permissionStr)
                != PackageManager.PERMISSION_GRANTED) {
            //处理 “不再提醒”
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,permissionStr)) {
//                showMessageOKCancel("权限"+permissionStr+"需要被授权",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                requestPermissionActitity(permissionStr,requestCode);
//                            }
//                        });
//            }else{
                requestPermissionActitity(permissionStr,requestCode);
//            }
            return false;
        }else{
            return true;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context).setTitle("系统提示")//设置对话框标题
                .setMessage(message)//设置显示的内容
                .setPositiveButton("确定",okListener).setNegativeButton("返回",null).create().show();
    }

    public void requestPermissionActitity(final String permissionStr,final int requestCode){
        ActivityCompat.requestPermissions(activity, new String[]{permissionStr},
                requestCode);
    }


    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if(grantResults.length < 1){
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
