package com.gj.gjchat2.model;


import android.os.Bundle;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.db.HuanxinDBManager;
import com.gj.gjchat2.huanxin.ui.ChatActivity;
import com.gj.gjchat2.ui.fragment.AddressBookFragment;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseFragmentModel;
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
public class AddressBookFragmentModel extends BaseFragmentModel {

    private AddressBookFragment addressBookFragment;

    public AddressBookFragmentModel(AddressBookFragment addressBookFragment){
        super(addressBookFragment);
        this.addressBookFragment = addressBookFragment;
    }

    public void getData() {
/*        AddressBookEntity addressBookEntity = CacheUtil.getAddressBookEntity(addressBookFragment.getActivity());
        if(addressBookEntity!=null&&addressBookEntity.data!=null
                &&addressBookEntity.data.rows!=null&&addressBookEntity.data.rows.size()>0){
            //说明缓存里有数据，并且是在LoginModel里面已经做了saveAddressUserInfo2DB操作
            AbLog.i("通讯录缓存里有数据，读取通讯录缓存");
            //saveAddressUserInfo2DB(addressBookEntity, EaseConstant.CHATTYPE_SINGLE, false);
            addressBookFragment.pressData(addressBookEntity);
        }else{
            getAddressBook();
        }*/


        HuanxinEaseUserListCacheEntity huanxinEaseUserListCacheEntity =  HuanxinEaseUserListCacheUtil.getHuanxinEaseUserListCacheEntity(addressBookFragment.getActivity());
        if(huanxinEaseUserListCacheEntity!=null&&huanxinEaseUserListCacheEntity.list!=null&&huanxinEaseUserListCacheEntity.list.size()>0){
            //本地缓存里有
            //AbLog.i("HuanxinEaseUserListCacheEntity 本地缓存里有");
            addressBookFragment.pressEaseUser(huanxinEaseUserListCacheEntity);
        }else {
            //本地缓存里没有
            //AbLog.i("HuanxinEaseUserListCacheEntity 本地缓存里没有");
            getAddressBook();
        }
    }


    public void getAddressBook(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    //String currentUser = EMClient.getInstance().getCurrentUser();
                    //AbLog.i("getAddressBook currentUser: "+currentUser);
                    //从环信服务器获取所有联系人的 huanxin_account
                   final List<String> huanxin_account_list =  EMClient.getInstance().contactManager().getAllContactsFromServer();
                    huanxin_account_list.add(CacheUtil.getHuanxinAccount(addressBookFragment.getActivity())); //添加一张自己的
                    addressBookFragment.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (huanxin_account_list == null) {
                                ToastUtils.show(addressBookFragment.getActivity(), "获取所有联系人数据失败", Toast.LENGTH_SHORT);
                                return;
                            }
                            //AbLog.i("getAddressBook huanxin_account_list.size(): "+huanxin_account_list.size());
                            String[] nicknameData =  addressBookFragment.getActivity().getResources().getStringArray(R.array.addressbook);

                            /*******************调用环信本地缓存对象资料（群头像、昵称）********************/
                            HuanxinUserInfoCacheEntity huanxinUserInfoCacheEntity =  HuanxinUserInfoCacheUtil.getHuanxinUserInfoCacheEntity(addressBookFragment.getActivity());
                            if(huanxinUserInfoCacheEntity==null||huanxinUserInfoCacheEntity.map==null){
                                huanxinUserInfoCacheEntity = new HuanxinUserInfoCacheEntity();
                                huanxinUserInfoCacheEntity.map = new HashMap<String,EaseUser>();
                            }
                            HuanxinEaseUserListCacheEntity  huanxinEaseUserListCacheEntity =  HuanxinEaseUserListCacheUtil.getHuanxinEaseUserListCacheEntity(addressBookFragment.getActivity());
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
                            HuanxinUserInfoCacheUtil.saveHuanxinUserInfoCacheEntity(addressBookFragment.getActivity(), huanxinUserInfoCacheEntity);
                            HuanxinEaseUserListCacheUtil.saveHuanxinEaseUserListCacheEntity(addressBookFragment.getActivity(), huanxinEaseUserListCacheEntity);
                            if(null!=addressBookFragment){
                                addressBookFragment.pressEaseUser(huanxinEaseUserListCacheEntity);
                            }
                        }
                    });

                } catch (Exception e) {
                    addressBookFragment.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            ToastUtils.show(addressBookFragment.getActivity(), "访问环信服务器失败", Toast.LENGTH_SHORT);
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
        addressBookFragment.readyGo(ChatActivity.class, bundle);
    }


    /**
     * 创建群组
     *
     */
    public void createGroupChat(final String groupName,final String[] members){
        AbLog.i("groupName: " + groupName+" members: " + members);
        //新建群组
        addressBookFragment.showProgressDialog("正在创建群组,请稍后");

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
                    addressBookFragment.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            addressBookFragment.hideProgressDialog();
                            ToastUtils.show(addressBookFragment.getActivity(), "创建群聊成功");
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
                    addressBookFragment.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            addressBookFragment.hideProgressDialog();
                            ToastUtils.show(addressBookFragment.getActivity(), "创建群聊失败：" + e.getLocalizedMessage());
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
        addressBookFragment.readyGoForResult(ChatActivity.class, 0, bundle);
    }

}
