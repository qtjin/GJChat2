package com.gj.gjchat2.huanxin.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.HuanxinHelper;
import com.gj.gjchat2.huanxin.domain.EmojiconExampleGroupData;
import com.gj.gjchat2.huanxin.domain.RobotUser;
import com.gj.gjchat2.model.HuanxinCacheModel;
import com.gj.gjchat2.ui.activity.addressbook.GroupDetailActivity;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconMenu;
import com.hyphenate.util.PathUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by qiyunfeng on 15/11/26.
 */
public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentListener {

    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

    /**
     * 是否为环信小助手
     */
    private boolean isRobot;

    private String toChatHuanxinId,type;
    private String userId,nickname,role;

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);
        super.setUpView();
        //titleBar.setBackgroundColor(getResources().getColor(R.color.topbar_bg));

        Bundle arguments = getArguments();
        toChatHuanxinId = arguments.getString(Constant.EXTRA_USER_ID);
        if(chatType == Constant.CHATTYPE_SINGLE){
            if(!HuanxinCacheModel.isCache(getActivity(),toChatHuanxinId)) { //该环信account没有缓存在本地
//                HuanxinCacheModel.getHXUserIdentity(getActivity(), toChatHuanxinId, new HuanxinCacheModel.CallbackListener() {
//                    @Override
//                    public void callback(boolean success) {
//
//                    }
//
//                    @Override
//                    public void callback(EaseUser easeUser) {
//                        if (null!=easeUser) {
//                            userId = easeUser.getUserId();
//                            nickname = easeUser.getNick();
//                            titleBar.setTitle(nickname);
//                            role = easeUser.getRole();
//                        }
//                    }
//                });
                EaseUser easeUser =  HuanxinCacheModel.getHXUserIdentityMoni(getActivity(), toChatHuanxinId);
                userId = easeUser.getUserId();
                nickname = easeUser.getNick();
                titleBar.setTitle(nickname);
                role = easeUser.getRole();
            }
        }else{
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatHuanxinId); //获取群组名称
            nickname = group.getGroupName();
            titleBar.setTitle(nickname);
        }


        setChatFragmentListener(this);
        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String,RobotUser> robotMap = HuanxinHelper.getInstance().getRobotList();
            if(robotMap!=null && robotMap.containsKey(toChatUsername)){
                isRobot = true;
            }
        }
        super.setUpView();
        ((EaseEmojiconMenu)inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        if(null!=nickname&&!"".equals(nickname)){
            titleBar.setTitle(nickname);
        }
    }


    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();

        if(chatType == Constant.CHATTYPE_SINGLE){
            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                default:
                    break;
            }
        }else if(resultCode== AppContext.NEW_GROUP_NAME){
            String newGroupName = data.getStringExtra("newGroupName");
            nickname = newGroupName;
            if(null!=newGroupName&&!"".equals(newGroupName)){
                titleBar.setTitle(newGroupName);
            }
        }

    }

    /**
     * 设置消息扩展属性
     */
    @Override
    public void onSetMessageAttributes(EMMessage message) {
        if(isRobot){
            //设置消息扩展属性
            message.setAttribute("em_robot_message", isRobot);
        }
    }

    /**
     * 进入会话详情
     */
    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            startActivityForResult(
                    (new Intent(getActivity(), GroupDetailActivity.class).putExtra("groupId", toChatUsername)),
                    REQUEST_CODE_GROUP_DETAIL);
        }/*else if(chatType == Constant.CHATTYPE_CHATROOM){
        	startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }*/
    }

    /**
     * 用户头像点击事件
     * @param huanxin_account
     */
    @Override
    public void onAvatarClick(String huanxin_account) {
        AbLog.i("huanxin_account: " + huanxin_account);
        if(chatType == Constant.CHATTYPE_SINGLE) {
            if (toChatHuanxinId.equals(huanxin_account)) {
                if (null != userId && !"".equals(userId)) {
                    //头像点击事件
                    Intent intent = null;
                    switch (role) {
                        case "teacher":
                            //intent = new Intent(getActivity(), TeacherInfoActivity.class);
                            break;
                        case "student":
                            //intent = new Intent(getActivity(), StudentInfoActivity.class);
                            break;
                        default:
                            // = new Intent(getActivity(), ParentInfoActivity.class);
                            break;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("userId", userId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        }
    }

    /**
     * 消息气泡框点击事件
     */
    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        return false;
    }

    /**
     * 消息气泡框长按事件
     */
    @Override
    public void onMessageBubbleLongClick(EMMessage message) {

    }


    /**
     * 扩展输入栏item点击事件,如果要覆盖EaseChatFragment已有的点击事件，return true
     * @param view
     * @param itemId
     * @return
     */
    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        if(itemId==ITEM_VIDEO_CALL){
            startVideoCall();
        }
        return false;
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * 设置自定义chatrow提供者
     * @return
     */
    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    /**
     * chat row provider
     *
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //音、视频通话发送、接收共4种
            return 4;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if(message.getType() == EMMessage.Type.TXT){
                //语音通话类型
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                }else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    //视频通话
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                }
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if(message.getType() == EMMessage.Type.TXT){
                // 语音通话,  视频通话
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
                }
            }
            return null;
        }
    }

    public class ChatRowVoiceCall extends EaseChatRow{

        private TextView contentvView;
        private ImageView iconView;

        public ChatRowVoiceCall(Context context, EMMessage message, int position, BaseAdapter adapter) {
            super(context, message, position, adapter);
        }

        @Override
        protected void onInflatView() {
            if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                        R.layout.ease_row_received_voice_call : R.layout.ease_row_sent_voice_call, this);
                // 视频通话
            }else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                        R.layout.ease_row_received_video_call : R.layout.ease_row_sent_video_call, this);
            }
        }

        @Override
        protected void onFindViewById() {
            contentvView = (TextView) findViewById(R.id.tv_chatcontent);
            iconView = (ImageView) findViewById(R.id.iv_call_icon);
        }

        @Override
        protected void onSetUpView() {
/*            TextMessageBody txtBody = (TextMessageBody) message.getBody();
            contentvView.setText(txtBody.getMessage());*/
        }

        @Override
        protected void onUpdateView() {

        }

        @Override
        protected void onBubbleClick() {

        }
    }
}
