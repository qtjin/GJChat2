package com.gj.gjlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class CommonRecyclerAdapter<T> extends RecyclerView.Adapter{

    protected static final String TAG = CommonRecyclerAdapter.class.getSimpleName();

    protected final Context mContext;
    private final int layoutId;
    protected List<T> mList;
    private LayoutInflater inflater;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View itemView, int position);
        void onItemLongClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }

    public CommonRecyclerAdapter(Context context, List<T> list, int layoutId) {
        this.mContext = context;
        this.layoutId = layoutId;
        if (list == null) {
            this.mList = new ArrayList<>();
        } else {
            this.mList = list;
        }
        this.inflater = LayoutInflater.from(mContext);
    }
    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        CommonAdapterHelper helper = CommonAdapterHelper.get(mContext, convertView, parent, layoutId, position);
//        convert(helper, getItem(position));
//        return helper.getConvertView();
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new MyViewHolder(inflater.inflate(layoutId, parent ,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        MyViewHolder mHolder = (MyViewHolder) viewHolder;
        if(mOnItemClickListener!=null){
            final View itemView = mHolder.itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(itemView, position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(itemView,position);
                    return false;
                }
            });
        }
        CommonRecyclerAdapterHelper helper = new CommonRecyclerAdapterHelper(mContext,mHolder,layoutId,position);
        convert(helper, getItem(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public abstract void convert(CommonRecyclerAdapterHelper helper, T t); //抽象方法，外部实现 目的是为item布局组件元素设置显示的数据


    public T getItem(int position) {
        return mList.get(position);
    }


    // 对mList集合进行操作

    public List<T> getList() {
        return mList;
    }

    public void add(T elem) {
        mList.add(elem);
        notifyDataSetChanged();
    }

    public void add(int location, T elem) {
        mList.add(location, elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elemList) {
        mList.addAll(elemList);
        notifyDataSetChanged();
    }

    public void addAll(int location, List<T> elemList) {
        mList.addAll(location, elemList);
        notifyDataSetChanged();
    }

    public void set(int location, T elem) {
        mList.set(location, elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(mList.indexOf(oldElem), newElem);
    }

    public void remove(T elem) {
        mList.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int location) {
        mList.remove(location);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return mList.contains(elem);
    }

    public void replaceAll(List<T> elemList) {
        mList.clear();
        mList.addAll(elemList);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
