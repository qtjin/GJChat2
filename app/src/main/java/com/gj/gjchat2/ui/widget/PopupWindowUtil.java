package com.gj.gjchat2.ui.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjlibrary.util.ToastUtils;


/**
 * Created by guojing on 2016/4/19.
 */
public class PopupWindowUtil {

    private Context context;
    private PopupWindow popupWindow;

    public interface CallbackListener {
        public void callback(String str); //回调方法
    }

    private CallbackListener callbackListener;

    public PopupWindowUtil(Context context) {
        this.context = context;
    }

    public PopupWindowUtil(Context context, CallbackListener callbackListener) {
        this.context = context;
        this.callbackListener = callbackListener;
    }

    public void setCallbackListener(CallbackListener callbackListener){
        this.callbackListener = callbackListener;
    }


    /**
     * 联系人操作 删除/黑名单
     */
    public void showLxrMenu(View view) {

        View layout = View.inflate(context, R.layout.popup_lxr_menu, null);

        if (popupWindow == null) {
            popupWindow = new PopupWindow(context);
            //popupWindow = new PopupWindow(layout, WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,false);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }

         LinearLayout ll_del = (LinearLayout) layout.findViewById(R.id.ll_del);
         LinearLayout ll_blacklist = (LinearLayout) layout.findViewById(R.id.ll_blacklist);

        ll_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(null!=callbackListener){
                    callbackListener.callback("delete");
                }
            }
        });

        ll_blacklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(null!=callbackListener){
                    callbackListener.callback("blacklist");
                }
            }
        });

        popupWindow.setContentView(layout);
        //popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
        popupWindow.showAsDropDown(view);


        popupWindow.update();

        //设置属性动画
        Animator animator = ObjectAnimator.ofFloat(layout, "scaleY", 0.0F, 1.0F);
        layout.setPivotY(0); //设置listview动画变换的地点在Y轴起始部分
        animator.setDuration(350);
        animator.start();
    }

    /**
     * 新增 联系人/群组
     */
    public void showLxrOrGroupAdd(View view) {

        View layout = View.inflate(context, R.layout.popup_lxr_add, null);

        if (popupWindow == null) {
            popupWindow = new PopupWindow(context);
            //popupWindow = new PopupWindow(layout, WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,false);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }

         LinearLayout ll_add_friend = (LinearLayout) layout.findViewById(R.id.ll_add_friend);
         LinearLayout ll_add_groupchat = (LinearLayout) layout.findViewById(R.id.ll_add_groupchat);

        ll_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(null!=callbackListener){
                    callbackListener.callback("addFriend");
                }
            }
        });

        ll_add_groupchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if(null!=callbackListener){
                    callbackListener.callback("addGroupchat");
                }
            }
        });

        popupWindow.setContentView(layout);
        //popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
        popupWindow.showAsDropDown(view);


        popupWindow.update();

        //设置属性动画
        Animator animator = ObjectAnimator.ofFloat(layout, "scaleY", 0.0F, 1.0F);
        layout.setPivotY(0); //设置listview动画变换的地点在Y轴起始部分
        animator.setDuration(350);
        animator.start();
    }

    /**
     * 显示输入回复内容的编辑框
     * String hintStr 默认输入显示
     */
    public void showPopupInput(String hintStr) {

        View layout = View.inflate(context, R.layout.dialog_input, null);

        if (popupWindow == null) {
            popupWindow = new PopupWindow(context);
            //popupWindow = new PopupWindow(layout, WindowManager.LayoutParams.FILL_PARENT,WindowManager.LayoutParams.WRAP_CONTENT,false);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }

        //防止虚拟软键盘被弹出菜单遮住
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);


/*        layout.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                AbLog.i("------------> onKey");
                dismiss();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return false;
            }
        });*/



        final EditText et_input = (EditText) layout.findViewById(R.id.et_input);
        et_input.setHint(hintStr);

        TextView tv_send = (TextView) layout.findViewById(R.id.tv_send);

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_input.getText().toString();
                if (null == content || "".equals(content)) {
                    ToastUtils.show(context, "评论内容不能为空");
                } else {
                    popupWindow.dismiss();
                    callbackListener.callback(content); //回调方法
                }
            }
        });



        popupWindow.setContentView(layout);
        popupWindow.showAtLocation(layout, Gravity.BOTTOM, 0, 0);
        //popupWindow.showAsDropDown(down_view);


        popupWindow.update();

/*        //设置属性动画
        Animator animator = ObjectAnimator.ofFloat(week_listview, "scaleY", 0.0F, 1.0F);
        week_listview.setPivotY(0); //设置listview动画变换的地点在Y轴起始部分
        animator.setDuration(350);
        animator.start();*/
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    //异步弹出键盘
    private void popupInputMethodWindow() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }
}
