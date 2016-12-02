package com.gj.gjchat2.model;


import android.os.Bundle;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.db.HuanxinDBManager;
import com.gj.gjchat2.huanxin.ui.ChatActivity;
import com.gj.gjchat2.ui.activity.addressbook.AddressBookPickActivity;
import com.gj.gjchat2.ui.activity.addressbook.GroupAddMemberActivity;
import com.gj.gjchat2.ui.activity.addressbook.SearchLxrActivity;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseModel;
import com.gj.gjlibrary.util.ToastUtils;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.HuanxinEaseUserListCacheEntity;
import com.hyphenate.easeui.domain.HuanxinUserInfoCacheEntity;
import com.hyphenate.easeui.utils.HuanxinEaseUserListCacheUtil;
import com.hyphenate.easeui.utils.HuanxinUserInfoCacheUtil;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guojing on 2016/3/25.
 * 通讯录
 */
public class AddressBookModel extends BaseModel {

    private AddressBookPickActivity addressBookPickActivity;
    private GroupAddMemberActivity groupAddMemberActivity;
    private SearchLxrActivity searchLxrActivity;


    public AddressBookModel(SearchLxrActivity searchLxrActivity){
        super(searchLxrActivity);
        this.searchLxrActivity = searchLxrActivity;
    }

    public AddressBookModel(AddressBookPickActivity addressBookPickActivity){
        super(addressBookPickActivity);
        this.addressBookPickActivity =addressBookPickActivity;
    }

    public AddressBookModel(GroupAddMemberActivity groupAddMemberActivity){
        super(groupAddMemberActivity);
        this.groupAddMemberActivity = groupAddMemberActivity;
    }


    public void getAddressBook(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    //String currentUser = EMClient.getInstance().getCurrentUser();
                    //AbLog.i("getAddressBook currentUser: "+currentUser);
                    //从环信服务器获取所有联系人的 huanxin_account
                   final List<String> huanxin_account_list =  EMClient.getInstance().contactManager().getAllContactsFromServer();
                    huanxin_account_list.add(CacheUtil.getHuanxinAccount(baseActivity)); //添加一张自己的
                    baseActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (huanxin_account_list == null) {
                                ToastUtils.show(baseActivity, "获取所有联系人数据失败", Toast.LENGTH_SHORT);
                                return;
                            }
                            //AbLog.i("getAddressBook huanxin_account_list.size(): "+huanxin_account_list.size());
                            String[] nicknameData =  baseActivity.getResources().getStringArray(R.array.addressbook);

                            /*******************调用环信本地缓存对象资料（群头像、昵称）********************/
                            HuanxinUserInfoCacheEntity huanxinUserInfoCacheEntity =  HuanxinUserInfoCacheUtil.getHuanxinUserInfoCacheEntity(baseActivity);
                            if(huanxinUserInfoCacheEntity==null||huanxinUserInfoCacheEntity.map==null){
                                huanxinUserInfoCacheEntity = new HuanxinUserInfoCacheEntity();
                                huanxinUserInfoCacheEntity.map = new HashMap<String,EaseUser>();
                            }
                            HuanxinEaseUserListCacheEntity  huanxinEaseUserListCacheEntity =  HuanxinEaseUserListCacheUtil.getHuanxinEaseUserListCacheEntity(baseActivity);
                            if(huanxinEaseUserListCacheEntity==null||huanxinEaseUserListCacheEntity.list==null){
                                huanxinEaseUserListCacheEntity = new HuanxinEaseUserListCacheEntity();
                                huanxinEaseUserListCacheEntity.list = new ArrayList<EaseUser>();
                            }
                            huanxinUserInfoCacheEntity.map.clear(); //先清空联系人缓存列表再匹配填充新数据
                            huanxinEaseUserListCacheEntity.list.clear(); //先清空联系人缓存列表再匹配填充新数据
                            for (int i = 0; i < huanxin_account_list.size(); i++) {
                                String huanxin_account = huanxin_account_list.get(i);
                                for (int j = 0; j < nicknameData.length; j++) {
                                    String s_huanxin_account = nicknameData[j].split("\\|")[0];
                                    String nickname = nicknameData[j].split("\\|")[1];
                                    String resid = nicknameData[j].split("\\|")[2];
                                    if(huanxin_account.equals(s_huanxin_account)){
                                        //AbLog.i("getHXUserIdentity 缓存对象姓名为 " + nickname);
                                        EaseUser easeUser = new EaseUser(huanxin_account);
                                        easeUser.setUserId(huanxin_account);
                                        easeUser.setNick(nickname);
                                        easeUser.setAvatar(resid);
                                        //easeUser.setRole(hxUserIndentifyEntity.data.idenCode);
                                        huanxinUserInfoCacheEntity.map.put(huanxin_account, easeUser);
                                        if(!huanxinEaseUserListCacheEntity.list.contains(easeUser)){
                                            huanxinEaseUserListCacheEntity.list.add(easeUser);
                                        }
                                        break;
                                    }
                                }
                            }
                            HuanxinUserInfoCacheUtil.saveHuanxinUserInfoCacheEntity(baseActivity, huanxinUserInfoCacheEntity);
                            HuanxinEaseUserListCacheUtil.saveHuanxinEaseUserListCacheEntity(baseActivity, huanxinEaseUserListCacheEntity);
                             if(null!=searchLxrActivity){
                                searchLxrActivity.pressEaseUser(huanxinEaseUserListCacheEntity);
                            }else if(null!=addressBookPickActivity){
                                addressBookPickActivity.pressEaseUser(huanxinEaseUserListCacheEntity);
                            }else if(null!=groupAddMemberActivity){
                                groupAddMemberActivity.pressEaseUser(huanxinEaseUserListCacheEntity);
                            }
                        }
                    });

                } catch (Exception e) {
                    baseActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            ToastUtils.show(baseActivity, "访问环信服务器失败", Toast.LENGTH_SHORT);
                        }
                    });
                }
            }
        }).start();

    }


    /**
     * 进入聊天页面 从最新消息列表进
     */
    public void goChatActivity(String huanxin_account,int type) {
        // 进入聊天页面
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_USER_ID, huanxin_account);
        bundle.putInt(Constant.EXTRA_CHAT_TYPE, type);
        baseActivity.readyGo(ChatActivity.class, bundle);
    }


    /**
     * 创建群组
     *
     */
    public void createGroupChat(final String groupName,final String[] members){
        AbLog.i("groupName: " + groupName+" members: " + members);
        //新建群组
        baseActivity.showProgressDialog("正在创建群组,请稍后");

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用sdk创建群组方法
                try {
                    String desc = ""; //群组描述
                    EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                    option.maxUsers = 200;
                    String reason = "Invite to join the group";
                    reason  = EMClient.getInstance().getCurrentUser() + reason + groupName;
                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                    final EMGroup currentGroup =  EMClient.getInstance().groupManager().createGroup(groupName, desc, members, reason, option);
                    baseActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            baseActivity.hideProgressDialog();
                            ToastUtils.show(baseActivity, "创建群聊成功");
                            // 进入聊天页面
                            AbLog.i("currentGroup.getGroupId(): "+currentGroup.getGroupId());
                            AbLog.i("currentGroup.getGroupName(): " + currentGroup.getGroupName());

                            EaseUser easeUser = new EaseUser(currentGroup.getGroupId());
                            easeUser.setNick(currentGroup.getGroupName());
                            HuanxinDBManager.getInstance().saveContact(easeUser);
                            goGroupChatActivity(currentGroup.getGroupId(), Constant.CHATTYPE_GROUP);
                        }
                    });
                } catch (final HyphenateException e) {
                    baseActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            baseActivity.hideProgressDialog();
                            ToastUtils.show(baseActivity, "创建群聊失败：" + e.getLocalizedMessage());
                        }
                    });
                }

            }
        }).start();
    }

    /**
     * 进入群聊天页面 从最新消息列表进
     */
    public void goGroupChatActivity(String groupId,int type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.EXTRA_USER_ID, groupId);
        bundle.putInt(Constant.EXTRA_CHAT_TYPE, type);
        baseActivity.readyGoForResult(ChatActivity.class, 0, bundle);
    }

}
