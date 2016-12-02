package com.gj.gjchat2.huanxin.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.widget.EaseConversationList;
import com.gj.gjchat2.model.HuanxinCacheModel;
import com.gj.gjlibrary.util.logger.AbLog;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseBaseFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 会话列表fragment
 *
 */
public class EaseConversationListFragment extends EaseBaseFragment{
	private final static int MSG_REFRESH = 2;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;

    protected boolean isConflict;
    
    protected EMConversationListener convListener = new EMConversationListener(){

		@Override
		public void onCoversationUpdate() {
			refresh();
		}
    	
    };

    protected TextView tv_nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        //titleBar.setBackgroundColor(getResources().getColor(R.color.topbar_bg));
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_nodata = (TextView) getActivity().findViewById(R.id.tv_nodata);
        //会话列表控件
        conversationListView = (EaseConversationList) getView().findViewById(R.id.list);
        errorItemContainer = (FrameLayout) getView().findViewById(R.id.fl_error_item);
    }
    
    @Override
    protected void setUpView() {

        conversationList.clear();
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);

        if(conversationList.size()==0){
            tv_nodata.setVisibility(View.VISIBLE);
        }else{
            tv_nodata.setVisibility(View.GONE);
        }

        if(listItemClickListener != null){
            conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    listItemClickListener.onListItemClicked(conversation);
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);


        conversationListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }
    
    
    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
               handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };

    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case 0:
                onConnectionDisconnected();
                break;
            case 1:
                onConnectionConnected();
                break;

            case MSG_REFRESH:
	            {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    if(conversationList.size()==0){
                        tv_nodata.setVisibility(View.VISIBLE);
                    }else{
                        tv_nodata.setVisibility(View.GONE);
                    }
	                conversationListView.refresh();
	                break;
	            }
            default:
                break;
            }
        }
    };
    
    /**
     * 连接到服务器
     */
    protected void onConnectionConnected(){
        errorItemContainer.setVisibility(View.GONE);
    }
    
    /**
     * 连接断开
     */
    protected void onConnectionDisconnected(){
        errorItemContainer.setVisibility(View.VISIBLE);
    }
    

    /**
     * 刷新页面
     */
    public void refresh() {
    	if(!handler.hasMessages(MSG_REFRESH)){
    		handler.sendEmptyMessage(MSG_REFRESH);
    	}
    }
    

    /**
     * 获取会话列表
     *
     * @return
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        +    */
    protected List<EMConversation> loadConversationList(){
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

        // 过滤掉messages size为0的conversation
        //AbLog.i("conversations.size(): "+conversations.size());
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化
         * 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变
         * 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            List<String> huanxin_account_list = new ArrayList<String>(); //未缓存到本地的环信account列表
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
                String username = conversation.getUserName();
                if(!HuanxinCacheModel.isCache(getActivity(), username)){ //该环信account没有缓存在本地
                    AbLog.i(username+"该环信account没有缓存在本地");
                    huanxin_account_list.add(username);
                }
            }
            if(null!=huanxin_account_list&&huanxin_account_list.size()>0){
                HuanxinCacheModel.getHXUserIdentityMoni(getActivity(),huanxin_account_list);
                //refresh();
//                HuanxinCacheModel.getHXUserIdentity(getActivity(),0,huanxin_account_list,new HuanxinCacheModel.CallbackListener(){
//
//                    @Override
//                    public void callback(boolean success) { //递归调用，获取到最后一条用户的接口获取昵称和头像之后才会调用该回调方法
//                        if(success){
//                            AbLog.i("缓存成功 通知列表刷新");
//                            refresh();
//                        }
//                    }
//
//                    @Override
//                    public void callback(EaseUser easeUser) {
//
//                    }
//                });
            }
        }

        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     * 
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
    
   protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        AbLog.i("EaseConversationListFragment onResume");
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(isConflict){
            outState.putBoolean("isConflict", true);
        }
    }
    
    public interface EaseConversationListItemClickListener {
        /**
         * 会话listview item点击事件
         * @param conversation 被点击item所对应的会话
         */
        void onListItemClicked(EMConversation conversation);
    }
    
    /**
     * 设置listview item点击事件
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }

}
