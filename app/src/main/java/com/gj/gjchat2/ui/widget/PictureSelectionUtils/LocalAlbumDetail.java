package com.gj.gjchat2.ui.widget.PictureSelectionUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gj.gjchat2.R;
import com.gj.gjchat2.base.AppContext;
import com.gj.gjchat2.ui.widget.MyPictureSelection.AlbumViewPager;
import com.gj.gjchat2.ui.widget.MyPictureSelection.FilterImageView;
import com.gj.gjchat2.ui.widget.MyPictureSelection.LocalImageHelper;
import com.gj.gjchat2.ui.widget.MyPictureSelection.MatrixImageView;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;
import com.gj.gjlibrary.util.AbAppManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import butterknife.Bind;


/**
 * @author linjizong
 * @Description:图片列表选择界面
 * @date 2015-4-11
 */
public class LocalAlbumDetail extends BaseActivity implements MatrixImageView.OnSingleTapListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.im_left_back)
    FilterImageView im_left_back;

    @Bind(R.id.gv_gridview)
     GridView gv_gridview;

    @Bind(R.id.tv_top_title)
     TextView tv_top_title;

    @Bind(R.id.tv_top_right_finish)
     TextView tv_top_right_finish;

    @Bind(R.id.fm_tv_header_bar_photo_count)
     TextView fm_tv_header_bar_photo_count;

    @Bind(R.id.fm_tv_header_finish)
     TextView fm_tv_header_finish;

    @Bind(R.id.pv_pagerview)
     FrameLayout pv_pagerview;

    @Bind(R.id.rl_title_bar_wai)
     RelativeLayout rl_title_bar_wai;

    @Bind(R.id.rl_album_item_header_bar)
     RelativeLayout rl_album_item_header_bar;

    @Bind(R.id.fm_albumviewpager)
    AlbumViewPager fm_albumviewpager;

    @Bind(R.id.cb_checkbox)
     CheckBox cb_checkbox;

    @Bind(R.id.iv_header_bar_photo_back)
     ImageView iv_header_bar_photo_back;

    @Bind(R.id.top)
     View top;

    //    View titleBar;//标题栏
    String folder;
    List<LocalImageHelper.LocalFile> currentFolder = null;

    LocalImageHelper helper = LocalImageHelper.getInstance();
    List<LocalImageHelper.LocalFile> checkedItems;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_picture_detail_list;
    }

    @Override
    protected void initData() {
        checkedItems = helper.getCheckedItems();

        if (!LocalImageHelper.getInstance().isInited()) {
            finish();
            return;
        }
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void initListener() {
        im_left_back.setOnClickListener(this);
        iv_header_bar_photo_back.setOnClickListener(this);
        fm_albumviewpager.setOnPageChangeListener(pageChangeListener);
        fm_albumviewpager.setOnSingleTapListener(this);

        cb_checkbox.setOnCheckedChangeListener(this);
//        mBackView.setOnClickListener(this);
        tv_top_right_finish.setOnClickListener(this);
        fm_tv_header_finish.setOnClickListener(this);

        folder = getIntent().getExtras().getString(AppContext.getAppContext().LOCAL_FOLDER_NAME);
        new Thread(new Runnable() {
            @Override
            public void run() {
                //防止停留在本界面时切换到桌面，导致应用被回收，图片数组被清空，在此处做一个初始化处理
                helper.initImage();
                //获取该文件夹下地所有文件
                final List<LocalImageHelper.LocalFile> folders = helper.getFolder(folder);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (folders != null) {
                            currentFolder = folders;
                            MyAdapter adapter = new MyAdapter(LocalAlbumDetail.this, folders);
                            tv_top_title.setText(folder);
                            gv_gridview.setAdapter(adapter);
                            //设置当前选中数量
                            if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() > 0) {
                                tv_top_right_finish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize()) + "/"+LocalImageHelper.getInstance().getMaxPictureNumber()+")");
                                tv_top_right_finish.setEnabled(true);
                                tv_top_right_finish.setTextColor(getResources().getColor(R.color.white));
                                //设置帧视图上面的图标的
                                fm_tv_header_finish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize()) + "/"+LocalImageHelper.getInstance().getMaxPictureNumber()+")");
                                fm_tv_header_finish.setEnabled(true);
                                tv_top_right_finish.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                tv_top_right_finish.setText("完成");
                                tv_top_right_finish.setTextColor(getResources().getColor(R.color.white));
//                                finish.setEnabled(false);
                                //
                                fm_tv_header_finish.setText("完成");
                                tv_top_right_finish.setTextColor(getResources().getColor(R.color.white));
//                                headerFinish.setEnabled(false);
                            }
                        }
                    }
                });
            }
        }).start();
        checkedItems = helper.getCheckedItems();
        LocalImageHelper.getInstance().setResultOk(false);
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    //显示查看大图的
    private void showViewPager(int index) {
        pv_pagerview.setVisibility(View.VISIBLE);
        gv_gridview.setVisibility(View.GONE);
        rl_title_bar_wai.setVisibility(View.GONE);
        top.setBackgroundColor(getResources().getColor(R.color.black_picture));

        fm_albumviewpager.setAdapter(fm_albumviewpager.new LocalViewPagerAdapter(currentFolder));
        fm_albumviewpager.setCurrentItem(index);

        fm_tv_header_bar_photo_count.setText((index + 1) + "/" + currentFolder.size());
        //第一次载入第一张图时，需要手动修改
        if (index == 0) {
            cb_checkbox.setTag(currentFolder.get(index));
            cb_checkbox.setChecked(checkedItems.contains(currentFolder.get(index)));
        }

        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1, (float) 0.9, 1, pv_pagerview.getWidth() / 2, pv_pagerview.getHeight() / 2);
        scaleAnimation.setDuration(300);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pv_pagerview.startAnimation(set);
    }

    //设置查看大图的  关闭之后的动画
    private void hideViewPager() {
        pv_pagerview.setVisibility(View.GONE);
        gv_gridview.setVisibility(View.VISIBLE);
        rl_title_bar_wai.setVisibility(View.VISIBLE);
        top.setBackgroundColor(getResources().getColor(R.color.orange_fd885c));

        AnimationSet set = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1, (float) 0.9, pv_pagerview.getWidth() / 2, pv_pagerview.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        pv_pagerview.startAnimation(set);
        ((BaseAdapter) gv_gridview.getAdapter()).notifyDataSetChanged();
    }

    //设置滑动查看大图
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            if (fm_albumviewpager.getAdapter() != null) {
                String text = (position + 1) + "/" + fm_albumviewpager.getAdapter().getCount();
                fm_tv_header_bar_photo_count.setText(text);
                cb_checkbox.setTag(currentFolder.get(position));
                cb_checkbox.setChecked(checkedItems.contains(currentFolder.get(position)));
            } else {
                fm_tv_header_bar_photo_count.setText("0/0");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onSingleTap() {
        if (rl_album_item_header_bar.getVisibility() == View.VISIBLE) {
            AlphaAnimation animation = new AlphaAnimation(1, 0);
            animation.setDuration(300);
            rl_album_item_header_bar.startAnimation(animation);
            rl_album_item_header_bar.setVisibility(View.GONE);
        } else {
            rl_album_item_header_bar.setVisibility(View.VISIBLE);
            AlphaAnimation animation = new AlphaAnimation(0, 1);
            animation.setDuration(300);
            rl_album_item_header_bar.startAnimation(animation);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_header_bar_photo_back:
                hideViewPager();
                break;
            case R.id.tv_top_right_finish:
                //右上角的完成

            case R.id.fm_tv_header_finish:
                //右上角的完成
                AbAppManager.getAbAppManager().finishActivity(LocalAlbum.class);
                LocalImageHelper.getInstance().setResultOk(true);
                finish();
                break;
            case R.id.im_left_back:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (pv_pagerview.getVisibility() == View.VISIBLE) {
            hideViewPager();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 选中图片右上角的按钮状态
     * @param compoundButton
     * @param b
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            if (checkedItems.contains(compoundButton.getTag())) {
                checkedItems.remove(compoundButton.getTag());
            }
        } else {
            if (!checkedItems.contains(compoundButton.getTag())) {
                //选择图片界面设置图片最多可选数量
                if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() >= LocalImageHelper.getInstance().getMaxPictureNumber()) {
                    Toast.makeText(this, "最多选择"+ LocalImageHelper.getInstance().getMaxPictureNumber() +"张图片", Toast.LENGTH_SHORT).show();
                    compoundButton.setChecked(false);
                    return;
                }
                checkedItems.add((LocalImageHelper.LocalFile) compoundButton.getTag());
            }
        }
        if (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize() > 0) {
            tv_top_right_finish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize()) + "/" + LocalImageHelper.getInstance().getMaxPictureNumber() + ")");
            tv_top_right_finish.setEnabled(true);

            fm_tv_header_finish.setText("完成(" + (checkedItems.size() + LocalImageHelper.getInstance().getCurrentSize()) + "/"+LocalImageHelper.getInstance().getMaxPictureNumber()+")");
            fm_tv_header_finish.setEnabled(true);
        } else {
            tv_top_right_finish.setText("完成");
            tv_top_right_finish.setEnabled(false);

            fm_tv_header_finish.setText("完成");
            fm_tv_header_finish.setEnabled(false);
        }
    }

    public class MyAdapter extends BaseAdapter {
        private Context m_context;
        private LayoutInflater miInflater;
        DisplayImageOptions options;
        List<LocalImageHelper.LocalFile> paths;

        public MyAdapter(Context context, List<LocalImageHelper.LocalFile> paths) {
            m_context = context;
            this.paths = paths;
            options = LocalImageHelper.getImageOptions();
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public LocalImageHelper.LocalFile getItem(int i) {
            return paths.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = new ViewHolder();
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = getLayoutInflater();
                convertView = inflater.inflate(R.layout.item_image_checkbox, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                viewHolder.checkBox.setOnCheckedChangeListener(LocalAlbumDetail.this);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageView imageView = viewHolder.imageView;
            LocalImageHelper.LocalFile localFile = paths.get(i);
//            FrescoLoader.getInstance().localDisplay(localFile.getThumbnailUri(), imageView, options);
            ImageLoader.getInstance().displayImage(localFile.getThumbnailUri(), new ImageViewAware(viewHolder.imageView), options,
                    loadingListener, null);
            viewHolder.checkBox.setTag(localFile);
            viewHolder.checkBox.setChecked(checkedItems.contains(localFile));
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showViewPager(i);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            ImageView imageView;
            CheckBox checkBox;
        }
    }

    SimpleImageLoadingListener loadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingComplete(String imageUri, View view, final Bitmap bm) {
            if (TextUtils.isEmpty(imageUri)) {
                return;
            }
            //由于很多图片是白色背景，在此处加一个#eeeeee的滤镜，防止checkbox看不清
            try {
                ((ImageView) view).getDrawable().setColorFilter(Color.argb(0xff, 0xee, 0xee, 0xee), PorterDuff.Mode.MULTIPLY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
