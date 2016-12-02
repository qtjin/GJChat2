package com.gj.gjchat2.ui.activity.me;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.model.LoginModel;
import com.gj.gjchat2.network.Network;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjchat2.util.tencent.QQLoginHelper;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.ToastUtils;

import butterknife.Bind;

/**
 * Created by guojing on 2016/3/22.
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.tv_qq_login)
     TextView tv_qq_login; //登录

    @Bind(R.id.bt_login)
     Button bt_login; //登录

    @Bind(R.id.et_ip)
     EditText et_ip; //设置ip

    @Bind(R.id.et_user)
     EditText et_user; //帐号

    @Bind(R.id.et_password)
     EditText et_password; //密码

    @Bind(R.id.iv_cancel)
     ImageView iv_cancel;

    @Bind(R.id.iv_cancel2)
     ImageView iv_cancel2;

    @Bind(R.id.cb_eye)
     CheckBox cb_eye;

    public String username,password;

    private LoginModel loginModel;


    @Override
    protected void initData() {
        et_ip.setText(CacheUtil.getIP(LoginActivity.this));
    }

    @Override
    public void pressData(BaseEntity baseEntity) {}

    @Override
    protected void initListener() {
        et_ip.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    iv_cancel.setVisibility(View.VISIBLE);
                }else{
                    iv_cancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    iv_cancel2.setVisibility(View.VISIBLE);
                } else {
                    iv_cancel2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_qq_login.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
        iv_cancel2.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        cb_eye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    // 显示为普通文本
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    // 使光标始终在最后位置
                    Editable etable = et_password.getText();
                    Selection.setSelection(etable, etable.length());
                } else {
                    // 显示为密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    // 使光标始终在最后位置
                    Editable etable = et_password.getText();
                    Selection.setSelection(etable, etable.length());
                }
            }
        });
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cancel:
                et_ip.setText("");
             break;
            case R.id.iv_cancel2:
                et_user.setText("");
             break;
            case R.id.bt_login:
                //登录
                username = et_user.getText().toString().trim();
                password = et_password.getText().toString().trim();
                String ip = et_ip.getText().toString().trim();
                if(username == null ||  password ==null || username.isEmpty() || password.isEmpty()){
                    ToastUtils.show(LoginActivity.this, "用户名或密码不能为空");
                }else if(ip == null || ip.isEmpty()) {
                    ToastUtils.show(LoginActivity.this, "请设置ip");
                }else{
                    CacheUtil.saveIP(LoginActivity.this, ip);
                    Network.setBaseURL(ip);
                    if(loginModel==null){
                        loginModel = new LoginModel(LoginActivity.this);
                    }
                    loginModel.getData(); //登录
                }
             break;
            case R.id.tv_qq_login:
                if(null==helper){
                   helper = new QQLoginHelper(this,tv_qq_login);
                }
                helper.start();
                break;
        }
    }

    /****************QQ第三方登录*********************/
    private QQLoginHelper helper;
    /****************QQ第三方登录*********************/
}
