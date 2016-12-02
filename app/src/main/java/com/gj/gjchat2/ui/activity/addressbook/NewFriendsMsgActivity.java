/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gj.gjchat2.ui.activity.addressbook;

import android.os.Bundle;
import android.widget.ListView;

import com.gj.gjchat2.R;
import com.gj.gjchat2.huanxin.adapter.NewFriendsMsgAdapter;
import com.gj.gjchat2.huanxin.db.InviteMessgeDao;
import com.gj.gjchat2.huanxin.domain.InviteMessage;
import com.gj.gjlibrary.base.BaseActivity;
import com.gj.gjlibrary.base.BaseEntity;

import java.util.List;

import butterknife.Bind;

/**
 * Application and notification
 *
 */
public class NewFriendsMsgActivity extends BaseActivity {

	@Bind(R.id.list)
	 ListView listView;

	@Override
	protected void initData() {
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();

		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		dao.saveUnreadMessageCount(0);
	}

	@Override
	public void pressData(BaseEntity baseEntity) {

	}

	@Override
	protected void initListener() {
		setTitle("新的朋友");
	}


	@Override
	protected int getContentViewLayoutId() {
		return R.layout.activity_new_friends_msg;
	}

	@Override
	protected void getBundleExtras(Bundle extras) {

	}

}
