package com.gj.gjlibrary.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gj.gjlibrary.R;

import butterknife.ButterKnife;
import rx.Subscription;


/**
 * Created by guojing on 15/11/6.
 */
public abstract class BaseFragment extends Fragment {

    public Subscription subscription;

    protected abstract int getContentViewLayoutId();

    protected abstract void initListener();

    protected abstract void initData();

    public abstract void pressData(BaseEntity baseEntity);

    protected View mRootView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ;if (getContentViewLayoutId() != 0) {
            mRootView = inflater.inflate(getContentViewLayoutId(), container, false);
            ButterKnife.bind(this, mRootView);
            initListener();
            initData();
            return mRootView;
        } else {
            mRootView = super.onCreateView(inflater, container, savedInstanceState);
            ButterKnife.bind(this, mRootView);
            initListener();
            initData();
            return mRootView;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unsubscribe();
        ButterKnife.unbind(this);//解绑
    }


    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    /**
     * startActivity
     *
     * @param clazz class
     */
    public void readyGo(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz  class
     * @param bundle bundle
     */
    public void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * startActivityForResult
     *
     * @param clazz       class
     * @param requestCode requestCode
     */
    public void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz       clazz
     * @param requestCode requestCode
     * @param bundle      bundle
     */
    public void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 进度条
     */
    private ProgressDialog mProgressDialog;
    /**
     * 界面是否可见
     */
    protected boolean mIsVisible;

    public ProgressDialog showProgressDialog() {
        return showProgressDialog(getString(R.string.loading), true);
    }

    public ProgressDialog showProgressDialog(String text) {
        return showProgressDialog(text, true);
    }
    public ProgressDialog showProgressDialog(String text, boolean isCancelable) {
        if (mIsVisible) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(getActivity());
            }
            mProgressDialog.setMessage(text);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(isCancelable);
            mProgressDialog.show();
            return mProgressDialog;
        }
        return null;
    }

    public void hideProgressDialog() {
        if (mIsVisible && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
