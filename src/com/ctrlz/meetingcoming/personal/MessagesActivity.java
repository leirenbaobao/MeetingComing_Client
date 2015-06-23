package com.ctrlz.meetingcoming.personal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ctrlz.meetingcoming.BaseActivity;
import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.ProgressDialog;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.httpaction.HttpInterface;
import com.ctrlz.meetingcoming.utils.DateFormatUtils;
import com.ctrlz.meetingcoming.utils.ToastUtils;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

public class MessagesActivity extends BaseActivity implements OnClickListener {

	List<MessageMode> messageList = new ArrayList<MessageMode>();
	private TextView right_title;
	private MessagesAdapter messagesAdapter;
	private boolean isEdit = false;

	private SwipeListView swipeListView;

	private AbPullToRefreshView refreshListView;

	private Map<String, String> mapForGetMessage = new HashMap<String, String>();

	private Map<String, String> mapForDeleteMessage = new HashMap<String, String>();

	private ProgressDialog progressDialog;

	private int postion = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity);

		initView();
	}

	private void initView() {

		progressDialog = new ProgressDialog(MessagesActivity.this);

		RelativeLayout message_title = (RelativeLayout) findViewById(R.id.message_title);
		TextView tv_title = (TextView) message_title.findViewById(R.id.tv_title);
		tv_title.setVisibility(View.VISIBLE);
		tv_title.setText("消息列表");
		right_title = (TextView) message_title.findViewById(R.id.right_title);
		right_title.setVisibility(View.VISIBLE);
		right_title.setText("编辑");
		right_title.setOnClickListener(this);
		ImageView back_btn = (ImageView) message_title.findViewById(R.id.back_btn);
		back_btn.setVisibility(View.VISIBLE);
		back_btn.setOnClickListener(this);

		mapForGetMessage.put("staffId", MCApplication.getHolderStaffId());

		swipeListView = (SwipeListView) findViewById(R.id.id_swipelistview);
		// swipeListView.setVerticalScrollBarEnabled(false);
		refreshListView = (AbPullToRefreshView) findViewById(R.id.meeting_messages_refresh);
		refreshListView.setOnFooterLoadListener(new OnFooterLoadListener() {

			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				// Toast.makeText(MessagesActivity.this, "没有更多消息了！",
				// 1000).show();
				ToastUtils.showShort(MessagesActivity.this, "没有更多消息了");
				refreshListView.onFooterLoadFinish();
			}
		});
		refreshListView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				// 此处执行更新消息的任务
				refreshListView.onHeaderRefreshFinish();
				// 先清空前面留下的
				messageList.clear();
				new QueryMessagesTask().execute();
			}
		});

		new QueryMessagesTask().execute();

	}

	/**
	 * @author David Messages适配器
	 */
	private class MessagesAdapter extends BaseAdapter {

		private List<MessageMode> messageList;

		public MessagesAdapter(List<MessageMode> _messageList) {
			// TODO Auto-generated constructor stub
			this.messageList = _messageList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return messageList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return messageList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			MessageHolder messageHolder;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(MessagesActivity.this).inflate(R.layout.message_list_items, null);
				messageHolder = new MessageHolder();
				messageHolder.action_delete_left = (ImageView) arg1.findViewById(R.id.action_delete_left);
				messageHolder.message_content = (TextView) arg1.findViewById(R.id.action_info);
				messageHolder.message_date = (TextView) arg1.findViewById(R.id.action_date);
				messageHolder.action_delete_right = (Button) arg1.findViewById(R.id.action_delete_right);
				arg1.setTag(messageHolder);
			} else{
				messageHolder = (MessageHolder) arg1.getTag();
			}
			
			messageHolder.message_content.setText(messageList.get(arg0).getContent());
			/*String str = DateFormatUtils.getDate(messageList.get(arg0).getTime());
			System.out.println(str);*/
			messageHolder.message_date.setText(messageList.get(arg0).getTime());
			messageHolder.action_delete_left.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg01) {
					// TODO Auto-generated method stub

					postion = arg0;
					mapForDeleteMessage.put("messageId", messageList.get(arg0).getMessageId());
					new DeleteMssageTask().execute();
					/*
					 * swipeListView.closeAnimate(arg0);
					 * swipeListView.dismiss(arg0);
					 */
				}
			});
			messageHolder.action_delete_right.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg01) {
					// TODO Auto-generated method stub

					postion = arg0;
					mapForDeleteMessage.put("id", messageList.get(arg0).getMessageId());

					new DeleteMssageTask().execute();
					/*swipeListView.closeAnimate(arg0);
					swipeListView.dismiss(arg0);*/
				}
			});

			messageHolder.message_content.setText(messageList.get(arg0).getContent());
			messageHolder.message_date.setText(messageList.get(arg0).getTime());

			if (isEdit) {
				messageHolder.action_delete_left.setVisibility(View.VISIBLE);
				swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
			} else {
				messageHolder.action_delete_left.setVisibility(View.GONE);
				swipeListView.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
			}
			return arg1;
		}

	}

	class MessageHolder {

		ImageView action_delete_left;
		TextView message_content;
		TextView message_date;

		Button action_delete_right;
	}

	/**
	 * @author David 查询消息的任务
	 */
	public class QueryMessagesTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String str = HttpInterface.GetMessage(mapForGetMessage);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				String str = jsonObject.optString("code");
				if (str.equals("0")) {
					JSONArray jsonArray = jsonObject.getJSONArray("value");
					for (int i = 0; i < jsonArray.length(); i++) {
						String sender = jsonArray.getJSONObject(i).optString("sender");
						String time = jsonArray.getJSONObject(i).optString("time");
						String content = jsonArray.getJSONObject(i).optString("content");
						String title = jsonArray.getJSONObject(i).optString("title");
						String type = jsonArray.getJSONObject(i).optString("type");
						String messageId = jsonArray.getJSONObject(i).optString("messageId");

						MessageMode messageMode = new MessageMode();
						messageMode.setSender(sender);
						messageMode.setTime(DateFormatUtils.getDate(time));
						messageMode.setContent(sender + ":" + content);
						messageMode.setTitle(title);
						messageMode.setType(type);
						messageMode.setMessageId(messageId);

						messageList.add(messageMode);
					}
					messagesAdapter = new MessagesAdapter(messageList);
					swipeListView.setAdapter(messagesAdapter);
					swipeListView.setSwipeListViewListener(new ActionSwipeListViewListener());
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.right_title:
			if (right_title.getText().toString().equals("编辑")) {
				isEdit = true;
				right_title.setText("完成");
			} else if (right_title.getText().toString().equals("完成")) {
				// swipeAdapter.setEdit(false);
				right_title.setText("编辑");
				// for(int i = 0; i<actionHolders.size(); i++){
				// actionHolders.get(i).delete_left.setVisibility(View.GONE);
				// actionSwipe.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
				// }
				isEdit = false;
			}
			messagesAdapter.notifyDataSetChanged();
			break;
		case R.id.back_btn:
			finish();
			break;
		}

	}

	/**
	 * @author Administrator ActionSwipe的监听类
	 */
	class ActionSwipeListViewListener extends BaseSwipeListViewListener {

		@Override
		public void onClickFrontView(int position) {
			// Toast.makeText(getApplicationContext(),
			// messageList.get(position), Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {
			super.onDismiss(reverseSortedPositions);

			for (int position : reverseSortedPositions) {
				// actionHolders.remove(actionHolders.get(position));

				messageList.remove(position);
			}
			messagesAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * @author Administrator 删除消息
	 */
	private class DeleteMssageTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String str = HttpInterface.DeleteMessage(mapForDeleteMessage);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			super.onPostExecute(result);
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				String str = jsonObject.optString("code");

				if (str.equals("0")) {
					String value = jsonObject.optString("value");
					if (value.equals("true")) {
						swipeListView.closeAnimate(postion);
						swipeListView.dismiss(postion);
						ToastUtils.showShort(MessagesActivity.this, "删除消息成功");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
