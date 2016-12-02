package com.gj.gjchat2.ui.activity.addressbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.huanxin.ui.ChatActivity;
import com.gj.gjchat2.ui.widget.DialogUtil;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.adapter.CommonAdapter;
import com.gj.gjlibrary.adapter.CommonAdapterHelper;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.ToastUtils;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseExpandGridView;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by guojing on 2016/3/24.
 * 群聊详情
 */
public class GroupDetailActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.gridview)
     EaseExpandGridView gridview;

    @Bind(R.id.rl_change_group_name)
     RelativeLayout rl_change_group_name;

    @Bind(R.id.rl_expand)
     RelativeLayout rl_expand;

    @Bind(R.id.tv_member_count)
     TextView tv_member_count;

    @Bind(R.id.tv_group_name)
     TextView tv_group_name;

    @Bind(R.id.tv_sub)
     TextView tv_sub;

//    @Bind(R.id.iv_arrow)
//     ImageView iv_arrow;

    private LinkedList<String> memberAccountList;

    private List<String> newMembersAccountList;

    //private LinkedList<EaseUser> addressBookList;

    private CommonAdapter<String> adapter;

    private String mHuanxinAccount;

    private String groupId,newGroupName;
    private EMGroup group;
    private int role=0; //0:普通成员 1:群主

   //public boolean isInExpandMode=false; //是否伸展
    public boolean isInDeleteMode=false; //是否是删除模式

    private GroupChangeListener groupChangeListener;

    @Override
    protected void initData() {
        mHuanxinAccount = CacheUtil.getHuanxinAccount(this);
        setTitle("群聊信息");
        //addressBookList = new LinkedList<EaseUser>();
        gridview.setAdapter(adapter);
        groupId =  getIntent().getStringExtra("groupId");
        updateGroup();
        //group = EMClient.getInstance().groupManager().getGroup(groupId);
        /*if(group == null){
            finish();
            return;
        }

        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
            // 自己是群主
            role=1;
            tv_sub.setText("解散群聊");
        }else{
            //不是群主
            tv_sub.setText("退出群聊");
        }

        groupChangeListener = new GroupChangeListener();
        EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);

        tv_member_count.setText("全部群成员(" + group.getMembers().size() + ")");
        tv_group_name.setText(group.getGroupName());

        memberAccountList = group.getMembers();
        AbLog.i("initData memberAccountList.size(): " + memberAccountList.size());


        if(memberAccountList!=null&&memberAccountList.size()>0){
            if(addressBookModel==null){
                addressBookModel = new AddressBookModel(this);
            }
            addressBookModel.getAddressBookData(); //获取通讯录的信息
        }*/
    }

    @Override
    public void pressData(BaseEntity baseEntity) {
    }

    @Override
    protected void initListener() {
        initAdapter();
        tv_sub.setOnClickListener(this);
        rl_change_group_name.setOnClickListener(this);
        gridview.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isInDeleteMode) {
                            isInDeleteMode = false;
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_expand:
                //isInExpandMode=true;
                //updateGroup();
            break;
            case R.id.tv_sub:
                if (role == 1) {
                    deleteGrop(); //删除群组
                } else {
                    exitGrop(); //解散群组
                }
            break;
            case R.id.rl_change_group_name:
                if (role == 1) {
                    new DialogUtil(GroupDetailActivity.this, "修改名称", "请输入新名称", new DialogUtil.CallbackListener() {
                        @Override
                        public void callback(String str) {
                            updateGroupName(str); //修改群聊名称
                        }
                    }).showDialog();
                }
            break;
/*            case R.id.tv_left:
                getIntent().putExtra("newGroupName", newGroupName);
                setResult(AppContext.NEW_GROUP_NAME, getIntent());
                finish();
                break;*/
        }
    }

    /**
     * 导航栏返回键监听事件
     */
    protected void onNavigationOnClick(){
        getIntent().putExtra("newGroupName", newGroupName);
        setResult(AppContext.NEW_GROUP_NAME, getIntent());
        super.onNavigationOnClick();
    }

    protected void initAdapter() {
        if(adapter==null){
            adapter = new CommonAdapter<String>(GroupDetailActivity.this,memberAccountList,R.layout.em_grid) {
                @Override
                public void convert(CommonAdapterHelper helper, final String huanxinAccount) {
                    //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(TestActivity.this));
                    int position = helper.getPosition();
                    ImageView iv_avatar = helper.getView(R.id.iv_avatar);
                    TextView tv_name = helper.getView(R.id.tv_name);
                    ImageView badge_delete = helper.getView(R.id.badge_delete);
                    if (position == getCount()-1) {// 最后一个item，减人按钮
                        badge_delete.setVisibility(View.INVISIBLE);
                        if(role==1){
                            if(isInDeleteMode){ //是删除模式
                                helper.getConvertView().setVisibility(View.GONE);
                            }else{
                                helper.getConvertView().setVisibility(View.VISIBLE);
                                iv_avatar.setBackgroundResource(R.drawable.em_smiley_minus_btn_nor);
                                tv_name.setVisibility(View.INVISIBLE);
                                iv_avatar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AbLog.i("消减成员");
                                        isInDeleteMode = true;
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        }else{
                            helper.getConvertView().setVisibility(View.GONE);
                        }
                    }else if (position == getCount()-2) { // 添加群组成员按钮
                        badge_delete.setVisibility(View.INVISIBLE);
                        if(role==1){
                            if(isInDeleteMode){ //是删除模式
                                helper.getConvertView().setVisibility(View.GONE);
                            }else{
                                helper.getConvertView().setVisibility(View.VISIBLE);
                                tv_name.setVisibility(View.INVISIBLE);
                                iv_avatar.setBackgroundResource(R.drawable.em_smiley_add_btn);
                                iv_avatar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AbLog.i("添加成员");
                                        Intent intent = new Intent();
                                        intent.setClass(GroupDetailActivity.this, GroupAddMemberActivity.class);
                                        Bundle bundle = new Bundle();
                                        ArrayList<String> arrayList = new ArrayList(memberAccountList);
                                        intent.putStringArrayListExtra("memberAccountList", arrayList);
                                        GroupDetailActivity.this.startActivityForResult(intent, 1);
                                    }
                                });
                            }
                        }else{
                            helper.getConvertView().setVisibility(View.GONE);
                        }
                     }else{
                        if(isInDeleteMode) { //是删除模式
                            //显示小红点
                            badge_delete.setVisibility(View.VISIBLE);
                            badge_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AbLog.i("点击小红点删除该成员的环信ID： " + huanxinAccount);
                                    if(mHuanxinAccount.equals(huanxinAccount)){
                                        ToastUtils.show(GroupDetailActivity.this, "不能删除自己");
                                    }else{
                                        deleteMembersFromGroup(huanxinAccount);
                                    }
                                }
                            });
                        }else{
                            badge_delete.setVisibility(View.INVISIBLE);
                        }
                        tv_name.setVisibility(View.VISIBLE);

                        final String userHeadImg = EaseUserUtils.setUserAvatar(GroupDetailActivity.this, huanxinAccount, iv_avatar);
                        final String nickname =  EaseUserUtils.setUserNick(GroupDetailActivity.this, huanxinAccount, tv_name);

                        tv_name.setText(nickname);
                        iv_avatar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(!mHuanxinAccount.equals(huanxinAccount)){
                                    Bundle mBundle = new Bundle();
                                    mBundle.putString("huanxin_account", huanxinAccount);
                                    mBundle.putString("nickname", nickname);
                                    mBundle.putString("userHead", userHeadImg);
                                    readyGo(LxrInfoActivity.class, mBundle);
                                }
                            }
                        });
                    }
                }

            };
        }
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_group_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==AppContext.GROUP_ADD_MEMBER){
            newMembersAccountList = data.getStringArrayListExtra("members");
            AbLog.i("onActivityResult newMembersAccountList.size(): " + newMembersAccountList.size());
            addMembersToGroup(newMembersAccountList.toArray(new String[0])); //添加到讨论组
        }
    }

    private class GroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName,
                                         String inviter, String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName,
                                          String applyer, String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationAccept(String groupId, String groupName,
                                        String accepter) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName,
                                          String decliner, String reason) {

        }

        @Override
        public void onInvitationAccepted(String s, String s1, String s2) {
            runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    updateGroup();
                }

            });
        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee,
                                         String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            finish();

        }

        @Override
        public void onGroupDestroyed(String s, String s1) {
            finish();
        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 增加群成员
     *
     * @param newmembers
     */
    private void addMembersToGroup(final String[] newmembers) {
        AbLog.i("addMembersToGroup newmembers.length： " + newmembers.length);
        showProgressDialog();
        new Thread(new Runnable() {

            public void run() {
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupId); //从服务端获取
                    AbLog.i("EMClient.getInstance().getCurrentUser(): " + EMClient.getInstance().getCurrentUser());
                    AbLog.i("group.getOwner(): " + group.getOwner());
                    // 创建者调用add方法
                    if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
                        EMClient.getInstance().groupManager().addUsersToGroup(groupId, newmembers);
                    } else {
                        // 一般成员调用invite方法
                        EMClient.getInstance().groupManager().inviteUser(groupId, newmembers, null);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            //updateGroup();
                            //tv_member_count.setText("全部群成员(" + group.getMembers().size() + ")");
                            hideProgressDialog();
                            ToastUtils.show(GroupDetailActivity.this, "添加群成员邀请已发送，等待对方确认加入");
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(), "添加群成员失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 删除群成员
     *
     * @param username
     */
    protected void deleteMembersFromGroup(final String username) {
        showProgressDialog();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // 删除被选中的成员
                    EMClient.getInstance().groupManager().removeUserFromGroup(groupId, username);
                    isInDeleteMode = false;
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            hideProgressDialog();
                            updateGroup();
                            tv_member_count.setText("全部群成员(" + group.getMembers().size() + ")");
                        }
                    });
                } catch (final Exception e) {
                    hideProgressDialog();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "删除群成员失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).start();
    }
    /**
     * 退出群组
     *
     */
    private void exitGrop() {
        showProgressDialog();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().leaveGroup(groupId);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            hideProgressDialog();
                            setResult(RESULT_OK);
                            finish();
                            if(ChatActivity.activityInstance != null)
                                ChatActivity.activityInstance.finish();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(), "退出群组失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 解散群组
     *
     */
    private void deleteGrop() {
        showProgressDialog();
        final String st5 = getResources().getString(R.string.Dissolve_group_chat_tofail);
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().destroyGroup(groupId);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            hideProgressDialog();
                            setResult(RESULT_OK);
                            finish();
                            if(ChatActivity.activityInstance != null)
                                ChatActivity.activityInstance.finish();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(), "解散群组失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
    /**
     * 修改群组名称
     *
     */
    private void updateGroupName(final String groupName) {
                showProgressDialog();
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().changeGroupName(groupId, groupName);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    tv_group_name.setText(groupName);
                                    hideProgressDialog();
                                    newGroupName = groupName;
                                    getIntent().putExtra("newGroupName", newGroupName);
                                    setResult(AppContext.NEW_GROUP_NAME,getIntent());
                                    Toast.makeText(getApplicationContext(), "修改群聊成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (final HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    hideProgressDialog();
                                    Toast.makeText(getApplicationContext(), "修改群聊失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }).start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    /**
     * 从服务端获取最新群聊
     */
    protected void updateGroup() {
        adapter.clear(); //必须先清空，否则会出现列表显示错乱
        new Thread(new Runnable() {
            public void run() {
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupId); //从服务端获取

                    runOnUiThread(new Runnable() {
                        public void run() {
                            if(group == null){
                                finish();
                                return;
                            }

                            if (EMClient.getInstance().getCurrentUser().equals(group.getOwner())) {
                                // 自己是群主
                                role=1;
                                tv_sub.setText("解散群聊");
                            }else{
                                //不是群主
                                tv_sub.setText("退出群聊");
                            }

                            groupChangeListener = new GroupChangeListener();
                            EMClient.getInstance().groupManager().addGroupChangeListener(groupChangeListener);

                            tv_member_count.setText("全部群成员(" + group.getMembers().size() + ")");
                            tv_group_name.setText(group.getGroupName());

                            if(memberAccountList!=null){
                                memberAccountList=null;
                            }
                            memberAccountList = new LinkedList<String>(group.getMembers());
                            if(memberAccountList!=null&&memberAccountList.size()>0){
                                AbLog.i("updateGroup memberAccountList.size(): " + memberAccountList.size());
                                //String[] nicknameData =  getResources().getStringArray(R.array.addressbook);
//                                for (int i = 0; i < memberAccountList.size(); i++) {
//                                    String huanxin_account = memberAccountList.get(i);
//                                    for (int j = 0; j < nicknameData.length; j++) {
//                                        String s_huanxin_account = nicknameData[j].split("\\|")[0];
//                                        String nickname = nicknameData[j].split("\\|")[1];
//                                        String resid = nicknameData[j].split("\\|")[2];
//                                        if(huanxin_account.equals(s_huanxin_account)){
//                                            //AbLog.i("getHXUserIdentity 缓存对象姓名为 " + nickname);
//                                            EaseUser easeUser = new EaseUser(huanxin_account);
//                                            easeUser.setUserId(huanxin_account);
//                                            easeUser.setNick(nickname);
//                                            easeUser.setAvatar(resid);
//                                            addressBookList.add(easeUser);
//                                            break;
//                                        }
//                                    }
//                                }
//                                AbLog.i("updateGroup addressBookList.size(): " + addressBookList.size());
//                                addressBookList.add(new EaseUser("")); //加成员
//                                addressBookList.add(new EaseUser("")); //减成员
                                memberAccountList.addLast("");//加成员
                                memberAccountList.addLast("");//减成员
//                                if(memberAccountList.size()>8){ //当集合长度大于8时显示箭头
//                                    iv_arrow.setVisibility(View.VISIBLE);
//                                    rl_expand.setOnClickListener(GroupDetailActivity.this);
//                                }
                                adapter.replaceAll(memberAccountList);
                                AbLog.i("adapter.getCount(): "+adapter.getCount());
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        public void run() {


                        }
                    });
                }
            }
        }).start();
    }
}
