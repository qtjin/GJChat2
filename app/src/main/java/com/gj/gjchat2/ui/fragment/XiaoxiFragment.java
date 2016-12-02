package com.gj.gjchat2.ui.fragment;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.HuanxinUtil;
import com.gj.gjchat2.huanxin.db.InviteMessgeDao;
import com.gj.gjchat2.huanxin.ui.ChatActivity;
import com.gj.gjchat2.huanxin.ui.EaseConversationListFragment;
import com.gj.gjchat2.ui.activity.main.MainActivity;
import com.gj.gjchat2.ui.widget.DialogUtil;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.util.ToastUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.util.NetUtils;

/**
 * Created by guojing on 16/3/24.
 * 消息
 */
public class XiaoxiFragment extends EaseConversationListFragment {

    private HuanxinUtil huanxinUtil;
    private TextView errorText;
    //protected ImageView rightImage;
    private DialogUtil dialogUtil;

    private MainActivity mainActivity;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_error);
        //titleBar.setRightImageResource(com.hyphenate.easeui.R.drawable.ic_msg_add_group_chat);
        //rightImage = titleBar.getRightImage();
    }

    @Override
    protected void setUpView() {
        mainActivity = (MainActivity) getActivity();
        huanxinUtil = mainActivity.getHuanxinUtil();
        huanxinUtil.setXiaoxiFragment(XiaoxiFragment.this); //消息通知小红点
        // 注册上下文菜单
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final EMConversation conversation = conversationListView.getItem(position);
                final String huanxin_account = conversation.getUserName();
                //AbLog.i("username: "+username);
                if (huanxin_account.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面

                    int type = Constant.CHATTYPE_SINGLE;
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                            // it's group chat
                            type = Constant.CHATTYPE_CHATROOM;
                        } else {
                            type = Constant.CHATTYPE_GROUP;
                        }
                    }
                    if(null!=huanxin_account&&!"".equals(huanxin_account)){
                        if(type==Constant.CHATTYPE_SINGLE){
                            goChatActivity(huanxin_account, Constant.CHATTYPE_SINGLE);
                        }else{
                            goGroupChatActivity(huanxin_account, Constant.CHATTYPE_GROUP);
                        }
                    }else{
                        ToastUtils.show(getActivity(), "环信账号为空");
                    }

                }
            }
        });
        super.setUpView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);
    }


    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if(tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.getUserName());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.getUserName(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();
        huanxinUtil.updateUnreadLabel();
        return true;
    }

    /**
     * 进入聊天页面 从最新消息列表进
     */
    public void goChatActivity(String huanxin_account, int type) {
        // 进入聊天页面
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_USER_ID, huanxin_account);
        bundle.putInt(Constant.EXTRA_CHAT_TYPE, type);
        ((BaseActivity)getActivity()).readyGo(ChatActivity.class, bundle);
    }
    /**
     * 进入群聊天页面 从最新消息列表进
     */
    public void goGroupChatActivity(String groupId, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_USER_ID, groupId);
        bundle.putInt(Constant.EXTRA_CHAT_TYPE, type);
        ((BaseActivity)getActivity()).readyGoForResult(ChatActivity.class, 0, bundle);
    }

}
