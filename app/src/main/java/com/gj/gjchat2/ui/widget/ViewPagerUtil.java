package com.gj.gjchat2.ui.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gjlibrary.util.ImageLoaderHelper;
import com.gj.gjlibrary.util.logger.AbLog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.LinkedList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by guojing on 2015/11/10.
 * 创建时间：2015/11/10 10:57
 * 显示多张全屏图片
 */
@SuppressLint("ClickableViewAccessibility")
public class ViewPagerUtil {

    private MyViewPager viewPager;
    private LinkedList<PhotoView> imgViewList;
    private ViewPagerAdapter adapter;

    private String totalNum;
    private Context context;
    //private android.support.v7.app.AlertDialog alertDialog;
    private Dialog alertDialog;
    private List<String> imgUrls;
    private TextView tv_num;
    private ImageView iv_delete;
    private int position;

    private CallbackPositionListener callbackPositionListener;

    public interface CallbackPositionListener {
        public void callbackPosition(int position); //删除某张图片时的回调方法
    }

    public ViewPagerUtil(Context context, Dialog alertDialog, MyViewPager viewPager, TextView tv_num, List<String> imgUrls, int position) {
        this.context = context;
        this.viewPager = viewPager;
        this.alertDialog = alertDialog;
        this.tv_num = tv_num;
        this.imgUrls = imgUrls;
        this.position = position;
    }
    public ViewPagerUtil(Context context, Dialog alertDialog, MyViewPager viewPager
            , TextView tv_num, ImageView iv_delete, List<String> imgUrls, int position, CallbackPositionListener callbackPositionListener) {
        this.context = context;
        this.viewPager = viewPager;
        this.alertDialog = alertDialog;
        this.tv_num = tv_num;
        this.iv_delete = iv_delete;
        this.imgUrls = imgUrls;
        this.position = position;
        this.callbackPositionListener = callbackPositionListener;
    }


    public void dispalyImg() {
            if (imgViewList == null) {
                imgViewList = new LinkedList<PhotoView>();
            }
            imgViewList.clear();
            viewPager.removeAllViews();
            totalNum = String.valueOf(imgUrls.size());
            tv_num.setText(String.valueOf(position+1)+"/"+totalNum);
            for (int i = 0; i < imgUrls.size(); i++) {

                //图片
                PhotoView imageView = new PhotoView(context);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                String imgUrl = imgUrls.get(i);
                ImageLoader.getInstance().displayImage(imgUrl, imageView, ImageLoaderHelper.getInstance(context).getDisplayOptionsPic());
                imgViewList.add(imageView);
                imageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() { //点击一次就关闭
                      @Override
                      public void onViewTap(View view, float x, float y) {
                        alertDialog.dismiss();
                      }
                });
            }
            if (adapter == null) {
                //AbLog.i("imgViewList.size(): " + imgViewList.size());
                adapter = new ViewPagerAdapter(imgViewList);
            }
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                boolean isScrolled = false;

                @Override
                public void onPageSelected(int arg0) {
                    // TODO Auto-generated method stub
                    tv_num.setText(arg0 + 1 + "/" + totalNum);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub
                    //LogUtils.log("onPageScrolled arg0: "+arg0+" arg2: "+arg2);
                    //改善滑动体验
                    viewPager.getParent().requestDisallowInterceptTouchEvent(true);
                }

                @Override
                public void onPageScrollStateChanged(int status) {
                    // TODO Auto-generated method stub

                    switch (status) {
                        case 1:// 手势滑动
                            isScrolled = false;
                            break;
                        case 2:// 界面切换
                            isScrolled = true;
                            break;
                        case 0:// 滑动结束
                            position = viewPager.getCurrentItem();
                            // 当前为最后一张，此时从右向左滑，则切换到第一张
                            //if (viewPager.getCurrentItem() == jsonArray.length() - 1 && !isScrolled)
                            if (viewPager.getCurrentItem() == imgUrls.size() - 1 && !isScrolled) {
                                viewPager.setCurrentItem(0);
                            }
                            // 当前为第一张，此时从左向右滑，则切换到最后一张
                            else if (viewPager.getCurrentItem() == 0 && !isScrolled) {
                                //viewPager.setCurrentItem(jsonArray.length() - 1);
                                viewPager.setCurrentItem(imgUrls.size() - 1);
                            }
                            break;
                    }

                }
            });

            viewPager.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //改善滑动体验
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

           viewPager.setCurrentItem(position);

            if(iv_delete!=null){
                iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AbLog.i("util callbackPosition position: " + position);
                        //imgUrls.remove(position); 外面删了
                        adapter.imgViewList.remove(position);
                        adapter.notifyDataSetChanged();
                        callbackPositionListener.callbackPosition(position); //将需要删除的图片回调给dialog
                        alertDialog.dismiss();
                    }
                });
            }

    }


    //适配器
    class ViewPagerAdapter extends PagerAdapter {

        public LinkedList<PhotoView> imgViewList;

        public ViewPagerAdapter(LinkedList<PhotoView> imgViewList) {
            this.imgViewList = imgViewList;
        }

        @Override
        public int getCount() {
            if (this.imgViewList != null) {
                return this.imgViewList.size();
            } else {
                return 0;
            }
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(this.imgViewList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(this.imgViewList.get(position), 0);
            return this.imgViewList.get(position);
        }

    }

}

