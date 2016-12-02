package com.gj.gjchat2.ui.widget.pyq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.entity.MyNewsDetailEntitiy;
import com.gj.gjchat2.ui.widget.DialogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guojing on 2016/4/14.
 */
public class MyNewsDetailGridViewAdapter extends BaseAdapter {

    private Context context;
    public List<MyNewsDetailEntitiy.DataEntity.ImageListEntity> imageList;
    private List<String> imgUrls;

    public MyNewsDetailGridViewAdapter(Context context, List<MyNewsDetailEntitiy.DataEntity.ImageListEntity> imageList){
        this.context = context;
        this.imageList = imageList;
        imgUrls = new ArrayList<String>();
        for (MyNewsDetailEntitiy.DataEntity.ImageListEntity imageEntity:imageList) {
            String imgUrl="";
            if(null!=imageEntity.image&&!"".equals(imageEntity.image)){
                imgUrl = imageEntity.image;
            }
            imgUrls.add(imgUrl);
        }
    }
    private DialogUtil dialogUtil;


    @Override
    public int getCount() {
        return imageList==null?0:imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList==null?null:imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sxq_grid_view, null);
            holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final String imgUrl = imageList.get(position).image;
        if(null!=imgUrl&&!"".equals(imgUrl)){
            ImageLoader.getInstance().displayImage(imgUrl, holder.iv_photo);
        }
        holder.iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=imgUrls&&imgUrls.size()>0){
                    if(dialogUtil==null){
                        dialogUtil = new DialogUtil(context);
                    }
                    dialogUtil.showBigImageList(imgUrls,position);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView iv_photo;
    }
}
