package com.gj.gjchat2.ui.widget.pyq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.entity.FriendNewsEntitiy;

import java.util.List;


/**
 * Created by guojing on 2016/4/14.
 * 实习圈评论
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    public List<FriendNewsEntitiy.DataEntity.RowsEntity.CommentListEntity> commentList;

    public CommentAdapter(Context context, List<FriendNewsEntitiy.DataEntity.RowsEntity.CommentListEntity> commentList){
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList==null?0:commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList==null?null:commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sxq_comment, null);
            holder.tv_comment_name = (TextView) convertView.findViewById(R.id.tv_comment_name);
            holder.tv_huifu = (TextView) convertView.findViewById(R.id.tv_huifu);
            holder.tv_be_comment_name = (TextView) convertView.findViewById(R.id.tv_be_comment_name);
            holder.tv_comment_content = (TextView) convertView.findViewById(R.id.tv_comment_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_comment_name.setText(commentList.get(position).compellation);
        String toCompellation = commentList.get(position).toCompellation;

        String pid = commentList.get(position).pid;
        if(null!=pid&&!"".equals(pid)&&!"0".equals(pid)){
            holder.tv_huifu.setVisibility(View.VISIBLE);
            holder.tv_be_comment_name.setVisibility(View.VISIBLE);
            holder.tv_be_comment_name.setText(toCompellation);
            holder.tv_comment_content.setText(":"+commentList.get(position).content);
        }else{
            holder.tv_huifu.setVisibility(View.GONE);
            holder.tv_be_comment_name.setVisibility(View.GONE);
            holder.tv_comment_content.setText(":"+commentList.get(position).content);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tv_comment_name,tv_huifu,tv_be_comment_name,tv_comment_content;
    }
}
