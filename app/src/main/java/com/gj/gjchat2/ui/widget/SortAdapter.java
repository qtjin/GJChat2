package com.gj.gjchat2.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.hyphenate.easeui.domain.EaseUser;

import java.lang.reflect.Field;
import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
	
	private List<EaseUser> easeUserlist = null;
	
	private Context mContext;
	
	public SortAdapter(Context mContext, List<EaseUser> easeUserlist){
		this.mContext = mContext;
		this.easeUserlist = easeUserlist;
	}
	public void updateListView(List<EaseUser> easeUserlist){
		this.easeUserlist = easeUserlist;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return this.easeUserlist!=null?this.easeUserlist.size():0;
	}

	@Override
	public Object getItem(int position) {
		return this.easeUserlist!=null?easeUserlist.get(position):0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		final EaseUser mContent = easeUserlist.get(position);
		if (convertView== null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_book, null);
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
			//viewHolder.grayLine = (ImageView) convertView.findViewById(R.id.iv_gray_line);
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_userhead);
			viewHolder.grayLine2 = (ImageView) convertView.findViewById(R.id.iv_gray_line2);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getInitialLetter());
			viewHolder.grayLine2.setVisibility(View.VISIBLE);
		}else {
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
		viewHolder.tvTitle.setText(mContent.getNick());
		String avatar = mContent.getAvatar();
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
		return convertView;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@Override
	public int getPositionForSection(int sectionIndex) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = easeUserlist.get(i).getInitialLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == sectionIndex) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	@Override
	public int getSectionForPosition(int position) {
		return easeUserlist.get(position).getInitialLetter().charAt(0);
	}

	
	final static class ViewHolder{
		TextView tvLetter;
		TextView tvTitle;
		ImageView imageView,grayLine,grayLine2;
	}
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	
}
