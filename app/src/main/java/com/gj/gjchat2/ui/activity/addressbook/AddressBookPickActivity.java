package com.gj.gjchat2.ui.activity.addressbook;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.model.AddressBookModel;
import com.gj.gjchat2.ui.widget.SiderBar;
import com.gj.gjchat2.ui.widget.SortSelectAdapter;
import com.gj.gjchat2.util.CharacterParser;
import com.gj.gjchat2.util.PinyinComparator;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.ToastUtils;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.HuanxinEaseUserListCacheEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;

/**
 * Created by guojing on 2016/3/24.
 * 选择通讯录联系人
 */
public class AddressBookPickActivity extends BaseActivity {

    @Bind(R.id.tv_right)
     TextView tv_right;

    @Bind(R.id.lv_address_book)
     ListView lv_address_book;

    @Bind(R.id.siderbar)
     SiderBar siderbar;

    @Bind(R.id.dialog)
     TextView dialog;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    private SortSelectAdapter adapter;

    private List<EaseUser> easeUserlist;

    private AddressBookModel addressBookModel;

    private HashMap<Integer,String> selectedMap;

    public String groupName; //群组名称
    public List<String> members; //有环信号的联系人集合
    //public List<String> userIdList; //没有注册环信号联系人集合


    @Override
    protected void initData() {
        members = new ArrayList<String>();
        refreshAddressBook();
        setTitle("选择联系人");
        showTopRight();
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("确定");
    }

    public void refreshAddressBook(){
        if(null==addressBookModel){
            addressBookModel = new AddressBookModel(this);
        }
        addressBookModel.getAddressBook();
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
    protected void initListener() {
        //initAdapter();
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=adapter) {
                    selectedMap = adapter.getSelectedMap();
                    if(null!=selectedMap&&selectedMap.size() > 0){
                        createGroupChat(); //创建群聊
                    }else {
                        ToastUtils.show(AddressBookPickActivity.this, "请选择联系人");
                    }
                }
            }
        });


        adapter = new SortSelectAdapter(this, easeUserlist);
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
//        lv_address_book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                //head 占用第0个 position
//                AbLog.i("position: " + position);
//                if (easeUserlist != null && easeUserlist.size() > 0) {
//                    EaseUser easeUser = easeUserlist.get(position);
//                    Bundle mBundle = new Bundle();
//                    mBundle.putString("huanxin_account", easeUser.getUserId());
//                    mBundle.putString("nickname", easeUser.getNick());
//                    mBundle.putString("userHead", easeUser.getAvatar());
//                    readyGo(LxrInfoActivity.class, mBundle);
//                    //addressBookModel.goChatActivity(easeUserlist.get(position - 1).getUserId(), Constant.CHATTYPE_SINGLE);
//                }
//            }
//        });
//        lv_address_book.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                return false;
//            }
//        });
    }

 //   protected void initAdapter() {
//        if(adapter==null){
//            adapter = new CommonAdapter<AddressBookEntity.DataEntity.RowsEntity>(AddressBookPickActivity.this,easeUserlist, R.layout.item_address_book_pick) {
//                @Override
//                public void convert(final CommonAdapterHelper helper,final AddressBookEntity.DataEntity.RowsEntity rowsEntity) {
                    //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(TestActivity.this));
//                    if(null!=rowsEntity.image&&!"".equals(rowsEntity.image)){
//                        helper.setImageUrl(R.id.iv_userhead,rowsEntity.image);
//                    }
//                    helper.setText(R.id.tv_nickname,rowsEntity.compellation);
//                    helper.setText(R.id.tv_classname, rowsEntity.className);
//
//                    CheckBox cb_address_book = helper.getView(R.id.cb_address_book);
//
//                    if(selectedMap.get(helper.getPosition())!=null){
//                        cb_address_book.setChecked(true);
//                    }else{
//                        cb_address_book.setChecked(false);
//                    }
//
//
//                    cb_address_book.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            if (isChecked) {
//                                selectedMap.put(helper.getPosition(),true);
//                            }else{
//                                selectedMap.remove(helper.getPosition());
//                            }
//                        }
//                    });

//                }
//            };
//        }
//    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        groupName = extras.getString("groupName");
        AbLog.i("groupName: " + groupName);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_address_book;
    }

    /**
     * 创建群聊
     */
    public void createGroupChat(){
        //遍历保存选中的成员的map集合
        Iterator iterator =  selectedMap.keySet().iterator();
        while (iterator.hasNext()){
            Integer key = (Integer) iterator.next();
            String huanxin_account = selectedMap.get(key);

            if(huanxin_account!=null&&!"".equals(huanxin_account)){
                members.add(huanxin_account); //有环信号的联系人集合
            }/*else{
                userIdList.add(userId); //没有注册环信号联系人集合
            }*/
        }
        addressBookModel.createGroupChat(groupName,members.toArray(new String[0])); //创建群组

//        if(userIdList.size()>0){
//            loginModel.huanxinRegisterForAddressBookPick(index); //递归调用环信注册账号接口
//        }else{
//            loginModel.createGroupChat(groupName,members.toArray(new String[0])); //创建群组
//        }
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        AbLog.i("AddressBookPickActivity onActivityResult resultCode" + resultCode);
//        if(resultCode== AppContext.INIT_GROUP_CHAT){
//            setResult(AppContext.INIT_GROUP_CHAT);
//            finish();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
}
