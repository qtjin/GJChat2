package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.LatLng;

public class EaseChatRowLocation extends EaseChatRow{

    private TextView locationView;
    private EMLocationMessageBody locBody;

	public EaseChatRowLocation(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

	public EaseChatRowLocation(Context context, EMMessage message, int position, BaseAdapter adapter,int bgId) {
        super(context, message, position, adapter,bgId);
    }

    @Override
    protected void onInflatView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_location : R.layout.ease_row_sent_location, this);
        if(message.direct() == EMMessage.Direct.SEND){
            if(bgId!=0){
                RelativeLayout bubble = (RelativeLayout) findViewById(R.id.bubble);
                bubble.setBackgroundResource(bgId);
            }
        }
    }

    @Override
    protected void onFindViewById() {
    	locationView = (TextView) findViewById(R.id.tv_location);
    }


    @Override
    protected void onSetUpView() {
		locBody = (EMLocationMessageBody) message.getBody();
		locationView.setText(locBody.getAddress());

		// deal with send message
		if (message.direct() == EMMessage.Direct.SEND) {
		    setMessageSendCallback();
            switch (message.status()) {
            case CREATE: 
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                // 发送消息
//                sendMsgInBackground(message);
                break;
            case SUCCESS: // 发送成功
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.GONE);
                break;
            case FAIL: // 发送失败
                progressBar.setVisibility(View.GONE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS: // 发送中
                progressBar.setVisibility(View.VISIBLE);
                statusView.setVisibility(View.GONE);
                break;
            default:
               break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }
    
    @Override
    protected void onBubbleClick() {
        Intent intent = new Intent(context, EaseBaiduMapActivity.class);
        intent.putExtra("latitude", locBody.getLatitude());
        intent.putExtra("longitude", locBody.getLongitude());
        intent.putExtra("address", locBody.getAddress());
        activity.startActivity(intent);
    }
    
    /*
	 * 点击地图消息listener
	 */
	protected class MapClickListener implements View.OnClickListener {

		LatLng location;
		String address;

		public MapClickListener(LatLng loc, String address) {
			location = loc;
			this.address = address;

		}

		@Override
		public void onClick(View v) {
		   
		}

	}


}