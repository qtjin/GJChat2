package com.gj.gjchat2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.model.AddressBookFragmentModel;
import com.gj.gjchat2.ui.activity.addressbook.LxrInfoActivity;
import com.gj.gjchat2.ui.activity.addressbook.NewFriendsMsgActivity;
import com.gj.gjchat2.ui.activity.addressbook.QunliaoListActivity;
import com.gj.gjchat2.ui.activity.main.MainActivity;
import com.gj.gjchat2.ui.widget.SiderBar;
import com.gj.gjchat2.ui.widget.SortAdapter;
import com.gj.gjchat2.util.CharacterParser;
import com.gj.gjchat2.util.PinyinComparator;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.base.BaseFragment;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.HuanxinEaseUserListCacheEntity;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;

/**
 * Created by guojing on 15/11/9.
 * 通讯录
 */
public class AddressBookFragment extends BaseFragment implements View.OnClickListener{


    @Bind(R.id.lv_address_book)
     ListView lv_address_book;

    @Bind(R.id.siderbar)
     SiderBar siderbar;

    @Bind(R.id.dialog)
     TextView dialog;

    public TextView tv_dot_addfriend;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private SortAdapter adapter;

    private List<EaseUser> easeUserlist;

    private AddressBookFragmentModel addressBookFragmentModel;

    private MainActivity mainActivity;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_address_book;
    }

    @Override
    protected void initData() {
        refreshRedDot();
        refreshAddressBook();
    }

    public void refreshRedDot(){
        //刷新新的朋友的红点
        if(null==mainActivity){
            mainActivity = (MainActivity) getActivity();
        }
        mainActivity.getHuanxinUtil().updateUnreadAddressLable();
    }

    public void refreshAddressBook(){
        if(null==addressBookFragmentModel){
            addressBookFragmentModel = new AddressBookFragmentModel(this);
        }
        addressBookFragmentModel.getAddressBook();
    }

    @Override
    protected void initListener() {

        View header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_address_book_header, null);
        lv_address_book.addHeaderView(header);

        RelativeLayout rl_addfriend = (RelativeLayout) header.findViewById(R.id.rl_addfriend);
        RelativeLayout rl_qunliao = (RelativeLayout) header.findViewById(R.id.rl_qunliao);
        RelativeLayout rl_biaoqian = (RelativeLayout) header.findViewById(R.id.rl_biaoqian);
        RelativeLayout rl_public = (RelativeLayout) header.findViewById(R.id.rl_public);

        rl_addfriend.setOnClickListener(this);
        rl_qunliao.setOnClickListener(this);
        rl_biaoqian.setOnClickListener(this);
        rl_public.setOnClickListener(this);

        tv_dot_addfriend = (TextView) header.findViewById(R.id.tv_dot_addfriend);

        adapter = new SortAdapter(getActivity(), easeUserlist);
        lv_address_book.setAdapter(adapter);

        siderbar.setTextView(dialog);

        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        //设置右侧触摸监听
        siderbar.setOnTouchingLetterChangedListener(new SiderBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if(null!=adapter){
                    //该字母首次出现的位置
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        AbLog.i("onTouchingLetterChanged position: "+position);
                        lv_address_book.setSelection(position);
                    }
                }
            }
        });
        lv_address_book.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //head 占用第0个 position
                AbLog.i("position: " + position);
                if (easeUserlist != null && easeUserlist.size() > 0) {
                    EaseUser easeUser = easeUserlist.get(position - 1);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("huanxin_account", easeUser.getUserId());
                    mBundle.putString("nickname", easeUser.getNick());
                    mBundle.putString("userHead", easeUser.getAvatar());
                    readyGo(LxrInfoActivity.class, mBundle);
                    //addressBookFragmentModel.goChatActivity(easeUserlist.get(position - 1).getUserId(), Constant.CHATTYPE_SINGLE);
                }
            }
        });
        lv_address_book.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });
    }

    @Override
    public void pressData(BaseEntity baseEntity) {
    }

    /**
     * 解析获取的通讯录用户数据
     * @param huanxinEaseUserListCacheEntity
     */
    public void pressEaseUser(HuanxinEaseUserListCacheEntity huanxinEaseUserListCacheEntity){
        easeUserlist =  huanxinEaseUserListCacheEntity.list;
        easeUserlist = filledSortLetters(easeUserlist);
        AbLog.i("easeUserlist.size(): "+easeUserlist.size());
        // 根据a-z进行排序源数据
        Collections.sort(easeUserlist, pinyinComparator);
        adapter.updateListView(easeUserlist);
    }

    /**
     * 为easeUserlist的 显示数据拼音的首字母字段sortLetters 填充数据
     */
    private List<EaseUser> filledSortLetters(List<EaseUser> easeUserlist){
        for(int i=0; i<easeUserlist.size(); i++){
            EaseUser easeUser = easeUserlist.get(i);
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(easeUser.getNick());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                easeUser.setInitialLetter(sortString.toUpperCase());
            }else{
                easeUser.setInitialLetter(sortString.toUpperCase());
            }
        }
        return easeUserlist;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_addfriend:
                readyGo(NewFriendsMsgActivity.class);
                break;
            case R.id.rl_qunliao:
                readyGo(QunliaoListActivity.class);
                break;
            case R.id.rl_biaoqian:
                AbLog.i("rl_biaoqian");
                break;
            case R.id.rl_public:
                AbLog.i("rl_public");
                break;
        }
    }

}
