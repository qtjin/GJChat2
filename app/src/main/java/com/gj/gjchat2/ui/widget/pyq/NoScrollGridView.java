package com.gj.gjchat2.ui.widget.pyq;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by guojing on 2016/4/14.
 */
public class NoScrollGridView extends GridView{

    public GridViewAdapter gridViewAdapter;
    public MyNewsGridViewAdapter myNewsGridViewAdapter;
    public MyNewsDetailGridViewAdapter myNewsDetailGridViewAdapter;

    public NoScrollGridView(Context context) {
        super(context);

    }
    public NoScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
