package com.gj.gjchat2.ui.activity.addressbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.Constant;
import com.gj.gjchat2.huanxin.ui.ChatActivity;
import com.gj.gjlibrary.adapter.CommonRecyclerAdapter;
import com.gj.gjlibrary.adapter.CommonRecyclerAdapterHelper;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.List;

import butterknife.Bind;

/**
 * Created by guojing on 2016/10/8.
 * 群聊列表
 */
public class QunliaoListActivity extends BaseActivity {

    @Bind(R.id.rv_qunliao)
     RecyclerView rv_qunliao;

    private List<EMGroup> grouplist;

    private CommonRecyclerAdapter<EMGroup> groupAdapter;
    
    @Override
    protected void initData() {
        setTitle("群聊列表");
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        AbLog.i("grouplist.size(): "+grouplist.size());
        groupAdapter.replaceAll(grouplist);
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void initListener() {
        //rv_qunliao = (RecyclerView) findViewById(R.id.rv_qunliao);
        rv_qunliao.setLayoutManager(new LinearLayoutManager(this));
        initAdapter();
        rv_qunliao.setAdapter(groupAdapter);
        groupAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                //进入群聊天页面
                Bundle bundle = new Bundle();
                bundle.putString(Constant.EXTRA_USER_ID, grouplist.get(position).getGroupId());
                bundle.putInt(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                QunliaoListActivity.this.readyGoForResult(ChatActivity.class, 0, bundle);
            }

            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_qunliao_list;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    protected void initAdapter() {
        if (groupAdapter == null) {
            groupAdapter = new CommonRecyclerAdapter<EMGroup>(this,grouplist, R.layout.em_row_group) {
                @Override
                public void convert(CommonRecyclerAdapterHelper helper, EMGroup group) {
                    helper.setText(R.id.name, group.getGroupName());
                }
            };
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            grouplist = EMClient.getInstance().groupManager().getAllGroups();
            groupAdapter.replaceAll(grouplist);
        }
    }
}
