package com.gj.gjchat2.ui.activity.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.HuanxinHelper;
import com.gj.gjchat2.huanxin.HuanxinUtil;
import com.gj.gjchat2.ui.activity.addressbook.AddFriendActivity;
import com.gj.gjchat2.ui.activity.addressbook.AddressBookPickActivity;
import com.gj.gjchat2.ui.activity.addressbook.SearchLxrActivity;
import com.gj.gjchat2.ui.activity.me.LoginActivity;
import com.gj.gjchat2.ui.widget.DialogUtil;
import com.gj.gjchat2.ui.widget.HQViewPager;
import com.gj.gjchat2.ui.widget.MyBroadcastReceiver;
import com.gj.gjchat2.ui.widget.MyFragmentPagerAdapter;
import com.gj.gjchat2.ui.widget.PopupWindowUtil;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.ToastUtils;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.chat.EMClient;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.Bind;
import butterknife.OnClick;

import static com.uuzuche.lib_zxing.activity.CodeUtils.RESULT_TYPE;


/**
 * 首页
 */
public class MainActivity extends BaseActivity{

    @Bind(R.id.topToolbar)
    Toolbar topToolbar;

    @Bind(R.id.ib_search)
     ImageButton ib_search;

    @Bind(R.id.ib_add)
     ImageButton ib_add;

    @Bind(R.id.mViewPager)
    HQViewPager mViewPager;

    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    private TabLayout.Tab xiaoxiTab;
    private TabLayout.Tab lianxirenTab;
    private TabLayout.Tab faxianTab;
    private TabLayout.Tab woTab;

    public MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Bind(R.id.tv_message_count)
    public TextView tv_message_count;

    @Bind(R.id.tv_invite)
    public TextView tv_invite;

    private HuanxinUtil huanxinUtil;
    private Bundle savedInstanceState;

    private PopupWindowUtil popupWindowUtil;
    private DialogUtil dialogUtil;

    private MyBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void initData() {

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        xiaoxiTab = mTabLayout.getTabAt(0);
        lianxirenTab = mTabLayout.getTabAt(1);
        faxianTab = mTabLayout.getTabAt(2);
        woTab = mTabLayout.getTabAt(3);

        //设置Tab的图标
        xiaoxiTab.setIcon(R.drawable.rb_xiaoxi_selector);
        lianxirenTab.setIcon(R.drawable.rb_address_book_selector);
        faxianTab.setIcon(R.drawable.rb_faxian_selector);
        woTab.setIcon(R.drawable.rb_me_selector);

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({ R.id.ib_search, R.id.ib_add})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_search:
                readyGo(SearchLxrActivity.class);
                break;
            case R.id.ib_add:
                if(null==popupWindowUtil){
                    popupWindowUtil = new PopupWindowUtil(MainActivity.this);
                    popupWindowUtil.setCallbackListener(new PopupWindowUtil.CallbackListener() {
                        @Override
                        public void callback(String str) {
                            switch (str){
                                case "addFriend": //添加朋友
                                    readyGo(AddFriendActivity.class);
                                    break;
                                case "addGroupchat": //创建群组
                                    editGroupChatName();
                                    break;
                            }
                        }
                    });
                }
                popupWindowUtil.showLxrOrGroupAdd(topToolbar);
                break;
        }
    }

    public void editGroupChatName(){
        if(dialogUtil==null){
            dialogUtil = new DialogUtil(this, new DialogUtil.CallbackListener() {
                @Override
                public void callback(String str) {
                    AbLog.i("str: "+str);
                    // 进入选择联系人页面
                    Bundle bundle = new Bundle();
                    bundle.putString("groupName", str);
                    Intent intent = new Intent(MainActivity.this, AddressBookPickActivity.class);
                    if (null != bundle) {
                        intent.putExtras(bundle);
                    }
                    startActivityForResult(intent,0);
//                    //startActivity(intent);
                }
            });
        }
        dialogUtil.showDialog(); //弹窗
    }

    @Override
    public void pressData(BaseEntity baseEntity) {
    }

    @Override
    protected void initListener() {
    }

    public void doErWeiMa(Intent intent){
        AbLog.i("********************doErWeiMa********************");
        //处理扫描结果（在界面上显示）
        if (intent.getIntExtra(RESULT_TYPE,0) == CodeUtils.RESULT_SUCCESS) {
            String result = intent.getStringExtra(CodeUtils.RESULT_STRING);
            AbLog.i("解析结果:" + result);
            Bundle bundle2 = new Bundle();
            bundle2.putString("url", result);
            readyGo(WebViewActivity.class,bundle2);
        } else if (intent.getIntExtra(RESULT_TYPE,0) == CodeUtils.RESULT_FAILED) {
            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
        }
    }


    /****************************************环信****************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;

        mBroadcastReceiver = new MyBroadcastReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.REFRESH_ADDRESS_LIST"); //刷新通讯录列表通知
        filter.addAction("android.intent.action.ER_WEI_MA"); //二维码回调通知
        registerReceiver(mBroadcastReceiver, filter);

        if(huanxinUtil==null){
            huanxinUtil = new HuanxinUtil(MainActivity.this);
            huanxinUtil.init(savedInstanceState);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver);

        if (huanxinUtil.conflictBuilder != null) {
            huanxinUtil.conflictBuilder.create().dismiss();
            huanxinUtil.conflictBuilder = null;
        }
        huanxinUtil.unregisterBroadcastReceiver();
        try {
            unregisterReceiver(huanxinUtil.internalDebugReceiver);
        } catch (Exception e) {
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!huanxinUtil.isConflict && !huanxinUtil.isCurrentAccountRemoved) {
            huanxinUtil.updateUnreadLabel();
            huanxinUtil.updateUnreadAddressLable();
        }

        // unregister this event listener when this activity enters the
        // background
        HuanxinHelper sdkHelper = HuanxinHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(huanxinUtil.messageListener);
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(huanxinUtil.messageListener);
        HuanxinHelper sdkHelper = HuanxinHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", huanxinUtil.isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, huanxinUtil.isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !huanxinUtil.isConflictDialogShow) {
            //huanxinUtil.showConflictDialog();
            ToastUtils.show(MainActivity.this, "账号再别处登录");
            huanxinUtil.isConflict = true;
            readyGoThenKill(LoginActivity.class);
            AppContext.getAppContext().logout(); // 退出登录
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !huanxinUtil.isAccountRemovedDialogShow) {
            huanxinUtil.showAccountRemovedDialog();
        }
    }

    public HuanxinUtil getHuanxinUtil(){
        if(huanxinUtil==null){
            huanxinUtil = new HuanxinUtil(MainActivity.this);
            huanxinUtil.init(savedInstanceState);
        }
        return huanxinUtil;
    }
    /****************************************环信****************************************/


}
