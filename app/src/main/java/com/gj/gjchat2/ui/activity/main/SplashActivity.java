package com.gj.gjchat2.ui.activity.main;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gj.gjchat2.R;
import com.gj.gjchat2.ui.activity.me.LoginActivity;
import com.gj.gjchat2.util.CacheUtil;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;


/**
 * 第一次启动的页面
 */
public class SplashActivity extends BaseActivity {

    @Bind(R.id.viewpager) ViewPager viewpager;

    @Bind(R.id.point_group) ViewGroup point_group;

    private LinkedList<ImageView> pointViewList;

/*
    @Bind(R.id.indicator)
    private CircleIndicator indicator;
*/
    private int [] images = new int[] {R.drawable.ic_splash_1, R.drawable.ic_splash_2,R.drawable.ic_splash_3};

    private List<View> viewList = new ArrayList<View>();

    PagerAdapter pagerAdapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

    };

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initData() {
        if (pointViewList == null) {
            pointViewList = new LinkedList<ImageView>();
        }
        pointViewList.clear();

        for(int i=0; i<images.length; i++) {
            //图片背景
            ImageView im = new ImageView(SplashActivity.this);
            im.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            im.setBackgroundResource(images[i]);
            if(i==2){
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        readyGoThenKill(LoginActivity.class);
                    }
                });
            }
            viewList.add(im);
            //点
            ImageView pointView = new ImageView(SplashActivity.this);
            pointView.setLayoutParams(new ViewGroup.LayoutParams(20, 2));
            if (i == 0) {
                pointView.setBackgroundResource(R.drawable.ic_yindaoye_slider_lan);
            } else {
                pointView.setBackgroundResource(R.drawable.ic_yindaoye_slider_hui);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            pointViewList.add(pointView);
            point_group.addView(pointView, layoutParams);
        }

        viewpager.setAdapter(pagerAdapter);

        //indicator.setViewPager(viewpager);
    }

    @Override
    public void pressData(BaseEntity baseEntity) {

    }

    @Override
    protected void initListener() {

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            boolean isScrolled = false; //处于非滑动状态

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                AbLog.i("position: " + position);
//                if(position == images.length - 1) {
//                    readyGoThenKill(LoginActivity.class);
//                }
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    if (i == position) {
                        switch (position){
                            case 0:
                                pointViewList.get(i).setBackgroundResource(R.drawable.ic_yindaoye_slider_lan);
                                break;
                            case 1:
                                pointViewList.get(i).setBackgroundResource(R.drawable.ic_yindaoye_slider_lv);
                                break;
                            case 2:
                                pointViewList.get(i).setBackgroundResource(R.drawable.ic_yindaoye_slider_huang);
                                break;
                        }
                    } else {
                        pointViewList.get(i).setBackgroundResource(R.drawable.ic_yindaoye_slider_hui);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1:// 手势滑动
                        isScrolled = false; //处于非滑动状态
                        break;
                    case 2:// 界面切换
                        isScrolled = true;  //处于滑动状态
                        break;
                    case 0:// 滑动结束
                        int currentItem = viewpager.getCurrentItem();
                        //AbLog.i("currentItem: "+currentItem);
                        //AbLog.i("isScrolled: "+isScrolled);
                        // 当前为最后一张，此时从右向左滑，并且是处于非滑动状态的时候 跳转到登录页
                        if (currentItem == pointViewList.size() - 1&&!isScrolled) {
                            CacheUtil.Launched(); //已经初次使用过该app了
                            readyGoThenKill(LoginActivity.class);
                        }
                        break;
                }
            }
        });
    }


    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_splash;
    }



}
