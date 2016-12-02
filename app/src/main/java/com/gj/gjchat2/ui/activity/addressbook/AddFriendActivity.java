package com.gj.gjchat2.ui.activity.addressbook;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.HuanxinHelper;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.ToastUtils;
import com.hyphenate.chat.EMClient;

import butterknife.Bind;

/**
 * Created by guojing on 2016/9/20.
 */
public class AddFriendActivity extends BaseActivity {

    @Bind(R.id.et_input)
    EditText et_input;

    @Bind(R.id.ll_user)
     RelativeLayout ll_user;

    @Bind(R.id.tv_huanxin_account)
     TextView tv_huanxin_account;

    @Bind(R.id.btn_add)
     Button btn_add;

    private String huanxin_account_toadd;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_add_friend;
    }

    @Override
    protected void initListener() {
        setTitle("添加朋友");

        /**
         * 软键盘监听点击确定事件
         */
        et_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //AbLog.i("软键盘监听点击确定事件...");

                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(
                            et_input.getWindowToken(), 0);

                    huanxin_account_toadd = et_input.getText().toString().trim();
                    if (TextUtils.isEmpty(huanxin_account_toadd)) {
                        ToastUtils.show(AddFriendActivity.this, R.string.Please_enter_a_username);
                    } else {
                        tv_huanxin_account.setText(huanxin_account_toadd);
                        ll_user.setVisibility(View.VISIBLE);
                    }

                    return true;
                }
                return false;
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUser = EMClient.getInstance().getCurrentUser();
                if(currentUser.equals(huanxin_account_toadd)){
                    ToastUtils.show(AddFriendActivity.this, R.string.not_add_myself);
                    return;
                }

                if(HuanxinHelper.getInstance().getContactList().containsKey(huanxin_account_toadd)){
                    //let the user know the contact already in your contact list
                    if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(huanxin_account_toadd)){
                        ToastUtils.show(AddFriendActivity.this, R.string.user_already_in_contactlist);
                        return;
                    }
                    ToastUtils.show(AddFriendActivity.this, R.string.This_user_is_already_your_friend);
                    return;
                }

                //添加好友
                showProgressDialog();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            //demo use a hardcode reason here, you need let user to input if you like
                            String s = getResources().getString(R.string.Add_a_friend);
                            EMClient.getInstance().contactManager().addContact(huanxin_account_toadd, s);

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    hideProgressDialog();
                                    String s1 = getResources().getString(R.string.send_successful);
                                    Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        } catch (final Exception e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    hideProgressDialog();
                                    String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                                    Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            });
                        }
                    }
                }).start();
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
}
