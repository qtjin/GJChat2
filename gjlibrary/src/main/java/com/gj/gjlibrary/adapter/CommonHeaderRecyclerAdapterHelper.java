package com.gj.gjlibrary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gj.gjlibrary.util.logger.AbLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


@SuppressWarnings("unused")
public class CommonHeaderRecyclerAdapterHelper {

    private CommonHeaderRecyclerAdapter.MyViewHolder mViewHolder;
    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;
    private Context mContext;
    private int mLayoutId;


    public CommonHeaderRecyclerAdapterHelper(Context context, CommonHeaderRecyclerAdapter.MyViewHolder viewHolder, int layoutId, int position) {
        this.mContext = context;
        this.mViewHolder = viewHolder;
        this.mConvertView = viewHolder.itemView;
        this.mLayoutId = layoutId;
        this.mPosition = position;
        this.mViews = new SparseArray<>();
    }


    public int getPosition() {
        return mPosition;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    /**
     * 通过viewId获取控件
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置TextView的值
     */
    public CommonHeaderRecyclerAdapterHelper setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setImageUrl(int viewId, String imageUrl) {
        ImageView view = getView(viewId);
        ImageLoader.getInstance().displayImage(imageUrl, view);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setImageUrl(int viewId, String imageUrl, DisplayImageOptions options) {
        ImageView view = getView(viewId);
        try {
            ImageLoader.getInstance().displayImage(imageUrl, view ,options);
        }catch (Exception ImageLoaderException){
            AbLog.i("ImageLoaderException"+ImageLoaderException.getMessage());
        }
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        //noinspection deprecation
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public CommonHeaderRecyclerAdapterHelper setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public CommonHeaderRecyclerAdapterHelper setOnClickListener(int viewId,
                                                                View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setOnTouchListener(int viewId,
                                                                View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public CommonHeaderRecyclerAdapterHelper setOnLongClickListener(int viewId,
                                                                    View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

}
