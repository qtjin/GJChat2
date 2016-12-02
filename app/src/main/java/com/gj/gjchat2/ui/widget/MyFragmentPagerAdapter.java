package com.gj.gjchat2.ui.widget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gj.gjchat2.ui.fragment.AddressBookFragment;
import com.gj.gjchat2.ui.fragment.FaxianFragment;
import com.gj.gjchat2.ui.fragment.MeFragment;
import com.gj.gjchat2.ui.fragment.XiaoxiFragment;

/**
 * Created by guojing on 16/7/22.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private XiaoxiFragment xiaoxiFragment; //消息fragment
    public AddressBookFragment addressBookFragment; //通讯录fragment
    private FaxianFragment faxianFragment; //发现fragment
    private MeFragment meFragment; //我的fragment

    private String[] mTitles = new String[]{"消息","通讯录", "发现","我"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            if(null==addressBookFragment){
                addressBookFragment = new AddressBookFragment();
            }
            return addressBookFragment;
        } else if (position == 2) {
            if(null==faxianFragment){
                faxianFragment = new FaxianFragment();
            }
            return faxianFragment;
        }else if (position==3){
            if(null==meFragment){
                meFragment = new MeFragment();
            }
            return meFragment;
        }else{
            if(null==xiaoxiFragment){
                xiaoxiFragment = new XiaoxiFragment();
            }
            return xiaoxiFragment;
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }


    public AddressBookFragment getAddressBookFragment(){
        return addressBookFragment;
    }
}
