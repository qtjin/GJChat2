package com.gj.gjchat2.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.ui.activity.me.SystemActivity;
import com.gj.gjchat2.ui.activity.pyq.MyNewsActivity;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.base.BaseFragment;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by guojing on 15/11/9.
 * 我的
 */
public class MeFragment extends BaseFragment{

    @Bind(R.id.iv_userhead)
     ImageView iv_userhead;

    @Bind(R.id.tv_name)
     TextView tv_name;

    @Bind(R.id.tv_wechat_num)
     TextView tv_wechat_num;

    @Bind(R.id.tv_album)
     TextView tv_album;

    @Bind(R.id.tv_setting)
     TextView tv_setting;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initData() {
        tv_name.setText(CacheUtil.getNickname(getActivity()));
        tv_wechat_num.setText(CacheUtil.getHuanxinAccount(getActivity()));

        Class drawable = com.hyphenate.easeui.R.drawable.class;
        String userHead = CacheUtil.getUserHeadImageUri(getActivity());
        if(!TextUtils.isEmpty(userHead)){
            Field field = null;
            try {
                field = drawable.getField(userHead);
                int r_id = field.getInt(field.getName());
                iv_userhead.setImageResource(r_id);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void initListener() {
    }

    @Override
    public void pressData(BaseEntity baseEntity) {
    }


    @OnClick({ R.id.tv_album, R.id.tv_setting})
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_album:
                readyGo(MyNewsActivity.class);
                break;
            case R.id.tv_setting:
                readyGo(SystemActivity.class);
                break;
        }

    }
}
