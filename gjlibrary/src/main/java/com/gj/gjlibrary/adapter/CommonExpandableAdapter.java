package com.gj.gjlibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public abstract class CommonExpandableAdapter<T,E> extends BaseExpandableListAdapter {

    protected static final String TAG = CommonExpandableAdapter.class.getSimpleName();

    protected final Context mContext;
    private final int groupLayoutId;
    private final int childLayoutId;
    protected List<T> mGroupList;
    protected Map<Integer,List<E>> mChildMap;
    private CommonAdapterHelper groupHelper;
    private CommonAdapterHelper childHelper;
    private Map<Integer,Boolean> isExpandMap; //某group是否展开 Integer代表group position

    public CommonExpandableAdapter(Context context, int groupLayoutId,int childLayoutId) {
        this.mContext = context;
        this.groupLayoutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
        this.mGroupList = new ArrayList<>();
        this.mChildMap = new HashMap<>();
        this.isExpandMap = new HashMap<>();
    }

    public CommonExpandableAdapter(Context context, List<T> mGroupList, Map<Integer,List<E>> mChildMap,int groupLayoutId,int childLayoutId) {
        this.mContext = context;
        this.groupLayoutId = groupLayoutId;
        this.childLayoutId = childLayoutId;
        if (mGroupList == null) {
            this.mGroupList = new ArrayList<>();
        } else {
            this.mGroupList = mGroupList;
        }
        if (mChildMap == null) {
            this.mChildMap = new HashMap<>();
        } else {
            this.mChildMap = mChildMap;
        }
        if (this.isExpandMap == null) {
            this.isExpandMap = new HashMap<>();
        }
    }

    @Override
    public View getGroupView(final int groupPosition,final boolean isExpanded, View convertView, ViewGroup parent) {
        groupHelper = CommonAdapterHelper.get(mContext, convertView, parent, groupLayoutId, groupPosition);
        convert(groupHelper, getGroup(groupPosition),isExpandMap);
        return groupHelper.getConvertView();
    }

    @Override
    public T getGroup(int groupPosition) {
        return mGroupList!=null?mGroupList.get(groupPosition):null;
    }


    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        childHelper = CommonAdapterHelper.get(mContext, convertView, parent, childLayoutId, childPosition);
        convertChild(groupPosition, groupHelper, childHelper, getChild(groupPosition, childPosition));
        return childHelper.getConvertView();
    }

    @Override
    public E getChild(int groupPosition, int childPosition) {
        return ((mChildMap!=null)&&(mChildMap.get(groupPosition)!=null))?mChildMap.get(groupPosition).get(childPosition) : null;
    }

    @Override
    public int getGroupCount() {
        return mGroupList!=null?mGroupList.size():0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return ((mChildMap!=null)&&(mChildMap.get(groupPosition)!=null))?mChildMap.get(groupPosition).size():0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public abstract void convert(CommonAdapterHelper helper, T t,Map<Integer,Boolean> isExpandMap);

    public abstract void convertChild(int groupPosition,CommonAdapterHelper groupHelper,CommonAdapterHelper childHelper, E e);


    public List<T> getGroupList() {
        return mGroupList;
    }

    public Map<Integer,List<E>> getChildList() {
        return mChildMap;
    }


    /**
     * 点击查看更多Group数据
     * @param mGroupList 临时GroupList
     */
    public void addGroup(List<T> mGroupList) {
        this.mGroupList = mGroupList;
        notifyDataSetChanged();
    }
    /**
     * 点击看更多Child数据
     * @param mChildMap 临时ChildMap
     */
    public void addChild(Map<Integer,List<E>> mChildMap) {
        this.mChildMap = mChildMap;
        notifyDataSetChanged();
    }


    public void addAll(List<T> mGroupList,Map<Integer,List<E>> mChildMap) {
        this.mGroupList.addAll(mGroupList);
        this.mChildMap.putAll(mChildMap);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> mGroupList,Map<Integer,List<E>> mChildMap) {
        this.mGroupList.clear();
        this.mChildMap.clear();
        this.mGroupList = mGroupList;
        this.mChildMap = mChildMap;
        notifyDataSetChanged();
    }

    public void clear() {
        this.mGroupList.clear();
        this.mChildMap.clear();
        notifyDataSetChanged();
    }

    public Map<Integer,Boolean> getExpandMap(){
        return isExpandMap;
    }
    public void setExpandMap(Map<Integer,Boolean> isExpandMap){
        this.isExpandMap = isExpandMap;
    }
}
