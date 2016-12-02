package com.gj.gjchat2.huanxin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.hyphenate.easeui.domain.EaseUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by guojing on 2016/9/23.
 * 过滤名称的Adapter
 */
public class FilterAdapter extends BaseAdapter{
    
    private NameFilter mNameFilter;
    private List<EaseUser> mArrayList;  //被过滤的list
    private List<EaseUser> mFilteredArrayList; //过滤之后的list
    private LayoutInflater mLayoutInflater;

    public FilterAdapter(Context context, List<EaseUser> arrayList) {
        mArrayList = arrayList;
        mLayoutInflater=LayoutInflater.from(context);
        mFilteredArrayList=new ArrayList<EaseUser>();
    }

    @Override
    public int getCount() {
        if (mFilteredArrayList == null) {
            return 0;
        } else {
            return (mFilteredArrayList.size());
        }

    }

    @Override
    public Object getItem(int position) {
        if (mFilteredArrayList == null) {
            return null;
        } else {
            return mFilteredArrayList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Filter getFilter() {
        if (mNameFilter == null) {
            mNameFilter = new NameFilter();
        }
        return mNameFilter;
    }

    public List<EaseUser> getFilterArrayList() {
        return mFilteredArrayList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = null;
        itemView = convertView;
        ViewHolder viewHolder = null;
        if (itemView == null) {
            itemView = mLayoutInflater.inflate(R.layout.item_address_book_result, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) itemView.findViewById(R.id.title);
            viewHolder.imageView = (ImageView) itemView.findViewById(R.id.iv_userhead);
            itemView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) itemView.getTag();
        }
        EaseUser easeUser = mFilteredArrayList.get(position);
        String nickname = easeUser.getNick();
        String avatar = easeUser.getAvatar();
        if (mArrayList != null) {
            if (viewHolder.textView != null) {
                viewHolder.textView.setText(nickname);
            }
            if(null!=avatar){
                Class drawable = com.hyphenate.easeui.R.drawable.class;
                Field field = null;
                try {
                    field = drawable.getField(avatar);
                    int r_id = field.getInt(field.getName());
                    viewHolder.imageView.setImageResource(r_id);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        convertView = itemView;
        return convertView;
    }


    private class ViewHolder {
        TextView textView;
        ImageView imageView;
    }


    //过滤数据
    class NameFilter extends Filter {
        //执行筛选
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();
            if(mFilteredArrayList.size()>0){
                mFilteredArrayList.clear();
            }
            for (Iterator<EaseUser> iterator = mArrayList.iterator(); iterator.hasNext();) {
                EaseUser easeUser = iterator.next();
                String nickname = easeUser.getNick();
                if (nickname.contains(charSequence)) {
                    mFilteredArrayList.add(easeUser);
                }
            }
            //filterResults.values = mFilteredArrayList;
            return filterResults;
        }

        //筛选结果
        @Override
        protected void publishResults(CharSequence arg0, FilterResults results) {
            //mFilteredArrayList = (List<EaseUser>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }}

}
