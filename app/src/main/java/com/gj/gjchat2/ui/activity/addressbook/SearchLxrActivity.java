package com.gj.gjchat2.ui.activity.addressbook;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.adapter.FilterAdapter;
import com.gj.gjchat2.model.AddressBookModel;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.HuanxinEaseUserListCacheEntity;
import com.hyphenate.easeui.utils.HuanxinEaseUserListCacheUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by guojing on 2016/9/23.
 * 搜索联系人
 */
public class SearchLxrActivity extends BaseActivity {

    @Bind(R.id.et_input)
     EditText et_input;

    @Bind(R.id.ib_clear)
     ImageButton ib_clear;

    @Bind(R.id.lv_result)
     ListView lv_result;

    private List<EaseUser> easeUserlist;

    private AddressBookModel addressBookModel;

    private FilterAdapter adapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_lxr_search;
    }

    @Override
    protected void initData() {
        getAddressBookCache();
    }

    public void getAddressBookCache(){
        HuanxinEaseUserListCacheEntity huanxinEaseUserListCacheEntity =  HuanxinEaseUserListCacheUtil.getHuanxinEaseUserListCacheEntity(this);
        if(huanxinEaseUserListCacheEntity!=null&&huanxinEaseUserListCacheEntity.list!=null&&huanxinEaseUserListCacheEntity.list.size()>0){
            easeUserlist = huanxinEaseUserListCacheEntity.list;
            initAdapter();
            lv_result.setAdapter(adapter);
        }else{
            //本地没有缓存，去环信服务器取联系人数据
            initAddressBookModel();
            addressBookModel.getAddressBook();
        }
    }


    /**
     * 解析获取的通讯录用户数据
     * @param huanxinEaseUserListCacheEntity
     */
    public void pressEaseUser(HuanxinEaseUserListCacheEntity huanxinEaseUserListCacheEntity){
        easeUserlist =  huanxinEaseUserListCacheEntity.list;
        initAdapter();
        lv_result.setAdapter(adapter);
    }

    public void initAdapter(){
        if(null==adapter){
            adapter = new FilterAdapter(this,easeUserlist);
        }
    }

    public void initAddressBookModel(){
        if(null==addressBookModel){
            addressBookModel = new AddressBookModel(this);
        }
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void initListener() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_input.getWindowToken(),0);
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ib_clear.setVisibility(View.GONE);//当文本框为空时，则叉叉消失
                } else {
                    ib_clear.setVisibility(View.VISIBLE);//当文本框不为空时，出现叉叉
                    initAdapter();
                    adapter.getFilter().filter(s);
                }
            }
        });

        ib_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_input.setText("");
            }
        });
        lv_result.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(null!=adapter){
                    List<EaseUser> mFilteredArrayList =  adapter.getFilterArrayList();
                    if (mFilteredArrayList!=null&&mFilteredArrayList.size()>0) {
                        initAddressBookModel();
                        addressBookModel.goChatActivity(mFilteredArrayList.get(position).getUserId(), Constant.CHATTYPE_SINGLE);
                    }
                }
            }
        });
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }
}
