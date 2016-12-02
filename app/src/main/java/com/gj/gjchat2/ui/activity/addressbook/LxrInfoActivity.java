package com.gj.gjchat2.ui.activity.addressbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.HuanxinHelper;
import com.gj.gjchat2.huanxin.db.UserDao;
import com.gj.gjchat2.huanxin.ui.ChatActivity;
import com.gj.gjchat2.huanxin.ui.VideoCallActivity;
import com.gj.gjchat2.ui.widget.PopupWindowUtil;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.lang.reflect.Field;

import butterknife.Bind;

/**
 * Created by guojing on 2016/9/26.
 */
public class LxrInfoActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.topToolbar)
    android.support.v7.widget.Toolbar topToolbar;

    @Bind(R.id.iv_userhead)
     ImageView iv_userhead;

    @Bind(R.id.tv_name)
     TextView tv_name;

    @Bind(R.id.tv_wechat_num)
     TextView tv_wechat_num;

    @Bind(R.id.tv_send_msg)
     TextView tv_send_msg;

    @Bind(R.id.tv_video_chat)
     TextView tv_video_chat;

    private String huanxin_account;
    private String nickname;
    private String userHead;

    private PopupWindowUtil popupWindowUtil;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_lxr_info;
    }

    @Override
    protected void initData() {

        setTitle("详细资料");
        showTopRight();
        tvRight.setBackgroundResource(R.drawable.ic_right_memu);

        if (!TextUtils.isEmpty(nickname)) {
            tv_name.setText(nickname);
        }

        if (!TextUtils.isEmpty(huanxin_account)) {
            tv_wechat_num.setText(huanxin_account);
        }

        Class drawable = com.hyphenate.easeui.R.drawable.class;
        if (!TextUtils.isEmpty(userHead)) {
            Field field = null;
            try {
                field = drawable.getField(userHead);
                int r_id = field.getInt(field.getName());
                iv_userhead.setImageResource(r_id);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void initListener() {
        tvRight.setOnClickListener(this);
        tv_send_msg.setOnClickListener(this);
        tv_video_chat.setOnClickListener(this);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        huanxin_account = extras.getString("huanxin_account");
        nickname = extras.getString("nickname");
        userHead = extras.getString("userHead");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_right:
                if(null==popupWindowUtil){
                    popupWindowUtil = new PopupWindowUtil(LxrInfoActivity.this);
                    popupWindowUtil.setCallbackListener(new PopupWindowUtil.CallbackListener() {
                        @Override
                        public void callback(String str) {
                            switch (str){
                                case "delete":
                                    deleteContact(huanxin_account);
                                    break;
                                case "blacklist":
                                    moveToBlacklist(huanxin_account);
                                    break;
                            }
                        }
                    });
                }
                popupWindowUtil.showLxrMenu(topToolbar);
                break;
            case R.id.tv_send_msg:
                // 进入聊天页面
                Bundle bundle = new Bundle();
                bundle.putString(Constant.EXTRA_USER_ID, huanxin_account);
                bundle.putInt(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_SINGLE);
                readyGo(ChatActivity.class, bundle);
                finish();
                break;
            case R.id.tv_video_chat:
                if (!EMClient.getInstance().isConnected())
                    Toast.makeText(LxrInfoActivity.this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                else {
                    startActivity(new Intent(LxrInfoActivity.this, VideoCallActivity.class).putExtra("username", huanxin_account)
                            .putExtra("isComingCall", false));
                }
                finish();
                break;
        }
    }


    /**
     * 删除
     * @param username
     */
    public void deleteContact(final String username) {
        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    // remove user from memory and database
                    UserDao dao = new UserDao(LxrInfoActivity.this);
                    dao.deleteContact(username);
                    HuanxinHelper.getInstance().getContactList().remove(username);
                    LxrInfoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            ToastUtils.show(LxrInfoActivity.this, "删除成功");
                            //EventBus.getDefault().post(new EventCenter(Constant.REFRESH_ADDRESS_LIST));
                            finish();
                        }
                    });
                } catch (final Exception e) {
                    LxrInfoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(LxrInfoActivity.this, st2 + e.getMessage(), Toast.LENGTH_LONG).show();
                            //EventBus.getDefault().post(new EventCenter(Constant.REFRESH_ADDRESS_LIST));
                            finish();
                        }
                    });
                }
            }
        }).start();
    }


    /**
     * 添加黑名单
     * move user to blacklist
     */
    protected void moveToBlacklist(final String username){
        final ProgressDialog pd = new ProgressDialog(this);
        String st1 = getResources().getString(com.hyphenate.easeui.R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(com.hyphenate.easeui.R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(com.hyphenate.easeui.R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //move to blacklist
                    EMClient.getInstance().contactManager().addUserToBlackList(username, false);
                    LxrInfoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(LxrInfoActivity.this, st2, Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    LxrInfoActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(LxrInfoActivity.this, st3, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }
}
