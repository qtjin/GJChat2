package com.gj.gjchat2.huanxin;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.db.InviteMessgeDao;
import com.gj.gjchat2.huanxin.db.UserDao;
import com.gj.gjchat2.huanxin.domain.InviteMessage;
import com.gj.gjchat2.huanxin.ui.ChatActivity;
import com.gj.gjchat2.ui.activity.main.MainActivity;
import com.gj.gjchat2.ui.activity.me.LoginActivity;
import com.gj.gjchat2.ui.fragment.XiaoxiFragment;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EMLog;

import java.util.List;

/**
 * Created by guojing on 2016/3/28.
 * 环信工具类
 */
public class HuanxinUtil {

    // 当前fragment的index
    //private int currentTabIndex;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    public boolean isCurrentAccountRemoved = false;

    private InviteMessgeDao inviteMessgeDao;
    private UserDao userDao;

    public android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    public boolean isConflictDialogShow;
    public boolean isAccountRemovedDialogShow;
    public BroadcastReceiver internalDebugReceiver;
    private XiaoxiFragment xiaoxiFragment;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;


    private MainActivity mainActivity;

    public HuanxinUtil(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void setXiaoxiFragment(XiaoxiFragment xiaoxiFragment){
        this.xiaoxiFragment = xiaoxiFragment;
    }

    public void init(Bundle savedInstanceState){
        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            HuanxinHelper.getInstance().logout(true,null);
            mainActivity.finish();
            mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            mainActivity.finish();
            mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
            return;
        }
        if (mainActivity.getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (mainActivity.getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
        inviteMessgeDao = new InviteMessgeDao(mainActivity);
        userDao = new UserDao(mainActivity);

        //注册local广播接收者，用于接收demohelper中发出的群组通讯录的变动通知
        registerBroadcastReceiver();

        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());
    }

    /**
     * 新消息的通知
     */
    public EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // 提示新消息
            for (EMMessage message : messages) {
                HuanxinHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {}
    };

    private void refreshUIWithMessage() {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
                //if (currentTabIndex == 0) {
                // 当前页面如果为聊天历史页面，刷新此页面
                if (xiaoxiFragment != null) {
                    xiaoxiFragment.refresh();
                }
                //}
            }
        });
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(mainActivity);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLable();
                //if (currentTabIndex == 0) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (xiaoxiFragment != null) {
                        xiaoxiFragment.refresh();
                    }
                //}
                /*else if (currentTabIndex == 1) {
                    if(contactListFragment != null) {
                        contactListFragment.refresh();
                    }
                }*/
/*                String action = intent.getAction();
                if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
                    if (EaseCommonUtils.getTopActivity(mainActivity).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }*/
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private boolean isAgreed=false;

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
            AbLog.i("************************ onContactAdded ************************");
            if(!isAgreed){
                String msg = "已经成功添加"+username +"为好友";
                mainActivity.myFragmentPagerAdapter.getAddressBookFragment().refreshAddressBook();
                notifyMsg(msg);
            }
        }
        @Override
        public void onContactDeleted(final String username) {
            mainActivity.runOnUiThread(new Runnable() {
                public void run() {
                    //String st10 = mainActivity.getResources().getString(R.string.have_you_removed);
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
//                        Toast.makeText(mainActivity, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_SHORT)
//                                .show();
                        ChatActivity.activityInstance.finish();
                    }
                    //notifyMsg(username + st10);
                    //EventBus.getDefault().post(new EventCenter(Constant.REFRESH_ADDRESS_LIST));

                    if(null!=mainActivity){
                        Intent intent = new Intent("android.intent.action.REFRESH_ADDRESS_LIST");
                        mainActivity.sendBroadcast(intent);
                    }
                }
            });

        }
        @Override
        public void onContactInvited(String username, String reason) {
            AbLog.i("huanxinUtil " + username + "请求加你为好友,reason: " + reason);
            String msg =   username + "请求加你为好友,原因: " + reason;
            notifyMsg(msg);
            mainActivity.myFragmentPagerAdapter.getAddressBookFragment().refreshRedDot();

        }
        @Override
        public void onContactAgreed(String username) {
            AbLog.i("************************ onContactAgreed ************************");
            mainActivity.myFragmentPagerAdapter.getAddressBookFragment().refreshAddressBook();
            String msg =   username + "同意了你的好友请求";
            notifyMsg(msg);
            isAgreed = true;
        }
        @Override
        public void onContactRefused(String username) {
            String msg =   username + "拒绝了你的好友请求";
            notifyMsg(msg);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notifyMsg(String msg){
        NotificationManager nm = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(mainActivity).setAutoCancel(true).setTicker(msg)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle("一条新通知").setContentText(msg).build();
        nm.notify(0x123,notify);
    }

    public void unregisterBroadcastReceiver(){
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            mainActivity.tv_message_count.setVisibility(View.VISIBLE);
            mainActivity.tv_message_count.setText(String.valueOf(count));
        } else {
            mainActivity.tv_message_count.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLable() {
        mainActivity.runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                String countStr = String.valueOf(count);
                if(null!=mainActivity.myFragmentPagerAdapter&&null!=mainActivity.myFragmentPagerAdapter.getAddressBookFragment()){
                    TextView tv_dot_addfriend = mainActivity.myFragmentPagerAdapter.getAddressBookFragment().tv_dot_addfriend;
                    TextView tv_invite = mainActivity.tv_invite;
                    if (count > 0) {
                        if(tv_dot_addfriend!=null) {
                            tv_dot_addfriend.setText(countStr);
                            tv_dot_addfriend.setVisibility(View.VISIBLE);
                        }
                        if(tv_invite!=null) {
                            tv_invite.setText(countStr);
                            tv_invite.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if(tv_dot_addfriend!=null){
                            tv_dot_addfriend.setVisibility(View.INVISIBLE);
                        }
                        if(tv_invite!=null){
                            tv_invite.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        });

    }

    /**
     * 获取未读申请与通知消息
     *
     * @return
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal = 0;
        unreadAddressCountTotal = inviteMessgeDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal = 0;
        int chatroomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for(EMConversation conversation:EMClient.getInstance().chatManager().getAllConversations().values()){
            if(conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatroomUnreadMsgCount=chatroomUnreadMsgCount+conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal-chatroomUnreadMsgCount;
    }

    /**
     * 保存提示新消息
     *
     * @param msg
     */
    private void notifyNewIviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        HuanxinHelper.getInstance().getNotifier().vibrateAndPlayTone(null);

        // 刷新bottom bar消息未读数
        updateUnreadAddressLable();
        // 刷新好友页面ui
        //if (currentTabIndex == 1) {
            //contactListFragment.refresh();
        //}
    }

    /**
     * 保存邀请等msg
     *
     * @param msg
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessgeDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessgeDao.saveUnreadMessageCount(1);
    }

    /**
     * 显示帐号在别处登录dialog
     */
    public void showConflictDialog() {
        isConflictDialogShow = true;
        HuanxinHelper.getInstance().logout(false, null);
        String st = mainActivity.getResources().getString(R.string.Logoff_notification);
        if (!mainActivity.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(mainActivity);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(R.string.connect_conflict);
                conflictBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        mainActivity.finish();
                        Intent intent = new Intent(mainActivity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        mainActivity.startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e("", "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    public void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        HuanxinHelper.getInstance().logout(false, null);
        String st5 = mainActivity.getResources().getString(R.string.Remove_the_notification);
        if (!mainActivity.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(mainActivity);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage("该用户已被移除");
                accountRemovedBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        mainActivity.finish();
                        mainActivity.startActivity(new Intent(mainActivity, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e("", "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }

}
