package com.gj.gjchat2.ui.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gj.gjchat2.ui.activity.main.MainActivity;


/**
 * Created by guojing on 2016/11/16.
 */

public class MyBroadcastReceiver extends BroadcastReceiver{

    private MainActivity mainActivity;

    public MyBroadcastReceiver(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if("android.intent.action.ER_WEI_MA".equals(action)){
            mainActivity.doErWeiMa(intent);
        }else if("android.intent.action.REFRESH_ADDRESS_LIST".equals(action)){
            mainActivity.myFragmentPagerAdapter.getAddressBookFragment().refreshAddressBook();
        }

    }


}
