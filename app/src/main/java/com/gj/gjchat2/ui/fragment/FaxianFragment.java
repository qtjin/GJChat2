package com.gj.gjchat2.ui.fragment;

import android.view.View;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.ui.activity.main.WebViewActivity;
import com.gj.gjchat2.ui.activity.pyq.PYQHallActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.base.BaseFragment;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by guojing on 15/11/9.
 * 发现
 */
public class FaxianFragment extends BaseFragment{

    @Bind(R.id.tv_pyq)
     TextView tv_pyq;

    @Bind(R.id.tv_saoyisao)
     TextView tv_saoyisao;

    @Bind(R.id.tv_shake)
     TextView tv_shake;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_faxian;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
    }

    @Override
    public void pressData(BaseEntity baseEntity) {
    }

    @OnClick({ R.id.tv_pyq, R.id.tv_saoyisao, R.id.tv_shake })
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_pyq:
                readyGo(PYQHallActivity.class);
                break;
            case R.id.tv_saoyisao:
                readyGo(CaptureActivity.class);
                break;
            case R.id.tv_shake:
                readyGo(WebViewActivity.class);
                break;
        }
    }

}
