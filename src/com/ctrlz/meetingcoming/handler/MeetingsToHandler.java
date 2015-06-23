package com.ctrlz.meetingcoming.handler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ctrlz.Zxing.demo.CaptureActivity;
import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.ProgressDialog;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.httpaction.HttpInterface;
import com.ctrlz.meetingcoming.utils.DateFormatUtils;
import com.ctrlz.meetingcoming.utils.ToastUtils;

public class MeetingsToHandler extends Fragment {

	private List<MeetingsToHandlerMode> MeetingToHandlerList = new ArrayList<MeetingsToHandlerMode>();
	private Dialog showMeetingCancelDialog;
	private Dialog showMeetingUnattendDialog;
	private Dialog showMeetingPutOffDialog;
	// 下拉刷新
	private AbPullToRefreshView refreshListView;
	// 查询会议
	private Map<String, String> mapInfosForQuery = new HashMap<String, String>();
	// 推迟会议
	private Map<String, String> mapInfosForPutOff = new HashMap<String, String>();
	// 取消会议
	private Map<String, String> mapInfosForCancel = new HashMap<String, String>();
	// 不参加
	private Map<String, String> mapInfosForUnattend = new HashMap<String, String>();

	private String meetingIdChoosed;
	private String meetingSponsorChoosed;
	private String meetingImportantChoosed;
	private String meetingEmergencyChoosed;
	
	private String meetingTimeChoosed;

	private PopupWindow popupWindow;

	// 等待对话框
	private ProgressDialog progressDialog;
	private ListView meetingToHandlerList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.meeting_to_handle, container, false);
		init(view);
		return view;
	}

	private void init(View view) {

		// 初始化消息推送
		Intent startSrv = new Intent("com.ctrlz.push.service.OnlineService");
		startSrv.putExtra("CMD", "RESET");
		getActivity().startService(startSrv);

		progressDialog = new ProgressDialog(getActivity(), R.style.LoadingDialog);

		RelativeLayout handler_title = (RelativeLayout) view.findViewById(R.id.handler_title);
		TextView tv_title = (TextView) handler_title.findViewById(R.id.tv_title);
		ImageView Scan = (ImageView) handler_title.findViewById(R.id.Scan);
		Scan.setVisibility(View.VISIBLE);
		Scan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getActivity(), CaptureActivity.class);
				startActivity(intent);

			}
		});

		tv_title.setText("待办会议");

		mapInfosForQuery.put("staffId", MCApplication.getHolderStaffId());

		meetingToHandlerList = (ListView) view.findViewById(R.id.meeting_todo_list);

		meetingToHandlerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub

				meetingSponsorChoosed = MeetingToHandlerList.get(arg2).getMeetingsToHandlerLanucher();
				meetingIdChoosed = MeetingToHandlerList.get(arg2).getMeetingsToHandlerID();
				meetingImportantChoosed = MeetingToHandlerList.get(arg2).getMeetingsToHandlerImportant();
				meetingEmergencyChoosed = MeetingToHandlerList.get(arg2).getMeetingsToHandlerUrgency();
				meetingTimeChoosed = MeetingToHandlerList.get(arg2).getMeetingsToHandlerTime();
				
				getPopupWindow();
				popupWindow.showAtLocation(arg0, Gravity.NO_GRAVITY, 700, 250);
			}
		});

		refreshListView = (AbPullToRefreshView) view.findViewById(R.id.meeting_handler_refresh);
		refreshListView.setOnFooterLoadListener(new OnFooterLoadListener() {

			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				ToastUtils.showShort(getActivity(), "没有更多会议了");
				refreshListView.onFooterLoadFinish();
			}
		});
		refreshListView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				refreshListView.onHeaderRefreshFinish();
				new QueryMeetingToHandler().execute();
			}
		});
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if (!hidden) {
			new QueryMeetingToHandler().execute();

		}
	}

	class Holder {
		TextView name;
		TextView place;
		TextView time;
		TextView plus;
		TextView lanucher;
		TextView important;
		TextView urgency;

		TextView putoff;
		TextView cancel;
		TextView unattend;
	}

	private void initPopupWindow() {
		// 获取自定义布局文件pop_window_layout.xml的视图
		View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.pop_window_layout, null, false);
		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		Button putOff = (Button) popupWindow_view.findViewById(R.id.putoff);
		putOff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (meetingEmergencyChoosed.equals("5") || meetingImportantChoosed.equals("5")) {
					ToastUtils.showShort(getActivity(), "五星会议，不能操作");
				} else {

					if (!meetingSponsorChoosed.equals(MCApplication.getLoginId())) {
						ToastUtils.showShort(getActivity(), "您不是该会议发起者，不能推迟会议");
					} else {
						showMeetingPutOffDialog();
					}
					popupWindow.dismiss();
				}

			}
		});

		Button unattend = (Button) popupWindow_view.findViewById(R.id.unattend);
		unattend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (meetingEmergencyChoosed.equals("5") || meetingImportantChoosed.equals("5")) {
					ToastUtils.showShort(getActivity(), "五星会议，不能操作");
				} else {
					if (meetingSponsorChoosed.equals(MCApplication.getLoginId())) {
						ToastUtils.showShort(getActivity(), "您是该会议发起者，必须参加");
					} else {
						showMeetingUnattendDialog();
					}
					popupWindow.dismiss();
				}
			}
		});

		Button cancel = (Button) popupWindow_view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (meetingEmergencyChoosed.equals("5") || meetingImportantChoosed.equals("5")) {
					ToastUtils.showShort(getActivity(), "五星会议，不能操作");
				} else {
					if (!meetingSponsorChoosed.equals(MCApplication.getLoginId())) {
						ToastUtils.showShort(getActivity(), "您不是该会议发起者，不能取消会议");
					} else {
						showMeetingCancalDialog();
					}
					popupWindow.dismiss();
				}
			}
		});

		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});
	}

	private void getPopupWindow() {
		if (popupWindow == null) {
			initPopupWindow();
		} else {
			popupWindow.dismiss();
		}
	}

	// 在界面上展示listview的class
	class MeetingHandlerShow {
		// 会议名称
		TextView MeetingName;
		// 会议地点
		TextView MeetingPlace;
		// 会议时间
		TextView MeetingTime;
		// 会议发起者
		TextView MeetingLanucher;
		// 会议备注
		TextView MeetingPlus;
		// 会议倒计时
		TextView MeetingCountDown;
		// 会议紧急值
		TextView MeetingUrgency;
		// 会议重要值
		TextView meetingImportant;
		// 会议倒计时
		TextView meeting_todo_duration;
		
		TextView meeting_todo_subject;
	}

	private class HandlerAdapter extends BaseAdapter {
		private List<MeetingsToHandlerMode> MeetingToHandlerList;

		HandlerAdapter(List<MeetingsToHandlerMode> _MeetingToHandlerList) {
			this.MeetingToHandlerList = _MeetingToHandlerList;
		}

		@Override
		public int getCount() {
			return MeetingToHandlerList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return MeetingToHandlerList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			MeetingHandlerShow meetingHandlerShow;
			if (arg1 == null) {
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.meeting_to_handle_list_items, null);
				meetingHandlerShow = new MeetingHandlerShow();
				meetingHandlerShow.MeetingTime = (TextView) arg1.findViewById(R.id.meeting_todo_time);
				meetingHandlerShow.MeetingName = (TextView) arg1.findViewById(R.id.meeting_todo_name);

				meetingHandlerShow.MeetingPlace = (TextView) arg1.findViewById(R.id.meeting_todo_place);
				meetingHandlerShow.MeetingPlus = (TextView) arg1.findViewById(R.id.meeting_todo_plus);
				meetingHandlerShow.MeetingUrgency = (TextView) arg1.findViewById(R.id.meeting_todo_urgency);
				meetingHandlerShow.meetingImportant = (TextView) arg1.findViewById(R.id.meeting_todo_important);
				meetingHandlerShow.MeetingLanucher = (TextView) arg1.findViewById(R.id.meeting_todo_lanucher);
				meetingHandlerShow.MeetingCountDown = (TextView) arg1.findViewById(R.id.meeting_todo_timeleft);
				meetingHandlerShow.meeting_todo_subject = (TextView) arg1.findViewById(R.id.meeting_todo_subject);
				meetingHandlerShow.meeting_todo_duration = (TextView) arg1.findViewById(R.id.meeting_todo_duration);
				arg1.setTag(meetingHandlerShow);

			} else {
				meetingHandlerShow = (MeetingHandlerShow) arg1.getTag();
			}

			meetingHandlerShow.MeetingName.setText("会议名称：" + MeetingToHandlerList.get(arg0).getMeetingsToHandlerName());
			meetingHandlerShow.MeetingTime.setText("开始时间：" + MeetingToHandlerList.get(arg0).getMeetingsToHandlerTime());

			meetingHandlerShow.MeetingPlace.setText("会议地点：" + MeetingToHandlerList.get(arg0).getMeetingsToHandlerPlace());
			if (!MeetingToHandlerList.get(arg0).getMeetingsToHandlerPlus().equals("null")) {
				meetingHandlerShow.MeetingPlus.setText("会议备注：" + MeetingToHandlerList.get(arg0).getMeetingsToHandlerPlus());
			} else {
				meetingHandlerShow.MeetingPlus.setText("会议备注：无");
			}
			meetingHandlerShow.MeetingUrgency.setText(MeetingToHandlerList.get(arg0).getMeetingsToHandlerUrgency() + "颗星");
			meetingHandlerShow.meetingImportant.setText(MeetingToHandlerList.get(arg0).getMeetingsToHandlerImportant() + "颗星");
			meetingHandlerShow.MeetingLanucher.setText("发起者：" + MeetingToHandlerList.get(arg0).getMeetingsToHandlerLanucher());
			meetingHandlerShow.MeetingCountDown.setText(MeetingToHandlerList.get(arg0).getMeetingsToHandlerTimeLeft());
			meetingHandlerShow.meeting_todo_subject.setText("会议内容：" + MeetingToHandlerList.get(arg0).getMeetingTodoSubject());
			meetingHandlerShow.meeting_todo_duration.setText(MeetingToHandlerList.get(arg0).getMeetingTodoDuration());

			return arg1;
		}
	}

	private void showMeetingUnattendDialog() {
		showMeetingUnattendDialog = new Dialog(getActivity(), R.style.LoadingDialog);
		showMeetingUnattendDialog.setContentView(R.layout.meeting_handler_unattended_dialog);
		TextView sureBtn = (TextView) showMeetingUnattendDialog.findViewById(R.id.meeting_handler_unattended_dialog_sure);
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				mapInfosForUnattend.put("meetingId", meetingIdChoosed);
				mapInfosForUnattend.put("staffId", MCApplication.getHolderStaffId());
				new UnattendMeetingToHandler().execute();
				
				showMeetingUnattendDialog.dismiss();
			}
		});
		ImageView imgBack = (ImageView) showMeetingUnattendDialog.findViewById(R.id.meeting_handler_unattended_dialog_imgback);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showMeetingUnattendDialog.dismiss();
			}
		});
		showMeetingUnattendDialog.setCanceledOnTouchOutside(false);
		showMeetingUnattendDialog.show();
	}

	private void showMeetingPutOffDialog() {
		showMeetingPutOffDialog = new Dialog(getActivity(), R.style.LoadingDialog);
		showMeetingPutOffDialog.setContentView(R.layout.meeting_handler_putoff_dialog);
		TextView sureBtn = (TextView) showMeetingPutOffDialog.findViewById(R.id.meeting_handler_putoff_dialog_sure);
		final TimePicker timpicker = (TimePicker) showMeetingPutOffDialog.findViewById(R.id.meeting_handler_putoff_dialog_time);
		timpicker.setIs24HourView(true);
		timpicker.setBackgroundResource(R.drawable.edit_input_bg);
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mapInfosForPutOff.put("meetingId", meetingIdChoosed);
				Integer hour = timpicker.getCurrentHour();
				Integer minute = timpicker.getCurrentMinute();

				String hourStr = String.valueOf(hour);
				String minuteStr = String.valueOf(minute);
				if (hourStr.length() == 1) {
					hourStr = "0" + hourStr;
				}
				
				if (minuteStr.length() == 1) {
					minuteStr = "0" + minuteStr;
				}
				
				String data[] = meetingTimeChoosed.split(" ");
				String total = data[0] + " " + hourStr + ":" + minuteStr;
				
				mapInfosForPutOff.put("time", "" + DateFormatUtils.getUnixDate(total));
				new PutoffMeetingToHandler().execute();

				showMeetingPutOffDialog.dismiss();
			}
		});
		ImageView imgBack = (ImageView) showMeetingPutOffDialog.findViewById(R.id.meeting_handler_putoff_dialog_imgback);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showMeetingPutOffDialog.dismiss();
			}
		});
		showMeetingPutOffDialog.setCanceledOnTouchOutside(false);
		showMeetingPutOffDialog.show();

	}

	private void showMeetingCancalDialog() {
		showMeetingCancelDialog = new Dialog(getActivity(), R.style.LoadingDialog);
		showMeetingCancelDialog.setContentView(R.layout.meeting_handler_cancal_dialog);
		TextView sureBtn = (TextView) showMeetingCancelDialog.findViewById(R.id.meeting_handler_cancel_dialog_sure);
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				mapInfosForCancel.put("meetingId", meetingIdChoosed);
				new CancelMeetingToHandler().execute();
				
				
				showMeetingCancelDialog.dismiss();
			}
		});
		ImageView imgBack = (ImageView) showMeetingCancelDialog.findViewById(R.id.meeting_handler_cancel_dialog_imgback);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showMeetingCancelDialog.dismiss();
			}
		});
		showMeetingCancelDialog.setCanceledOnTouchOutside(false);
		showMeetingCancelDialog.show();
	}

	/**
	 * @author David 查询待办会议的任务
	 */
	private class QueryMeetingToHandler extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String str = HttpInterface.MeetingHandler(mapInfosForQuery);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
			MeetingToHandlerList.clear();
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				String str = jsonObject.optString("code");
				if (str.equals("0")) {
					JSONArray jsonArr = jsonObject.optJSONArray("value");
					for (int i = 0; i < jsonArr.length(); i++) {

						String staffId = jsonArr.getJSONObject(i).optString("staffId");
						String subject = jsonArr.getJSONObject(i).optString("subject");
						String emergency = jsonArr.getJSONObject(i).optString("emergency");
						String remarks = jsonArr.getJSONObject(i).optString("remarks");
						String number = jsonArr.getJSONObject(i).optString("number");
						String sponsor = jsonArr.getJSONObject(i).optString("sponsor");
						String endTime = jsonArr.getJSONObject(i).optString("endTime");
						String startTime = jsonArr.getJSONObject(i).optString("startTime");
						String duration = jsonArr.getJSONObject(i).optString("duration");
						String importance = jsonArr.getJSONObject(i).optString("importance");
						String name = jsonArr.getJSONObject(i).optString("name");
						String meetingId = jsonArr.getJSONObject(i).optString("meetingId");
						String room = jsonArr.getJSONObject(i).optString("room");

						long curTime = System.currentTimeMillis();
						System.out.println(DateFormatUtils.getDate("" + curTime));
						System.out.println(DateFormatUtils.getDate(startTime));

						MeetingsToHandlerMode meetingsToHandlerMode = new MeetingsToHandlerMode();
						meetingsToHandlerMode.setMeetingsToHandlerLanucher(sponsor);
						meetingsToHandlerMode.setMeetingsToHandlerName(name);
						meetingsToHandlerMode.setMeetingsToHandlerPlace(room);
						meetingsToHandlerMode.setMeetingsToHandlerPlus(remarks);
						meetingsToHandlerMode.setMeetingsToHandlerTime(DateFormatUtils.getDate(startTime));
						meetingsToHandlerMode.setMeetingsToHandlerTimeLeft("倒计时：" + DateFormatUtils.changeS2H("" + (Long.parseLong(startTime) - curTime)));
						meetingsToHandlerMode.setMeetingsToHandlerImportant(importance);
						meetingsToHandlerMode.setMeetingsToHandlerUrgency(emergency);
						meetingsToHandlerMode.setMeeting_lanucher_login(staffId);
						meetingsToHandlerMode.setMeetingsToHandlerID(meetingId);
						meetingsToHandlerMode.setMeetingTodoSubject(subject);
						MeetingToHandlerList.add(meetingsToHandlerMode);

					}
					//按时间排序
					Compare comparator=new Compare();
					Collections.sort(MeetingToHandlerList, comparator);
					
					HandlerAdapter handlerAdapter = new HandlerAdapter(MeetingToHandlerList);
					meetingToHandlerList.setAdapter(handlerAdapter);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * @author Administrator 推迟会议的任务
	 */
	private class PutoffMeetingToHandler extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String str = HttpInterface.MeetingPutOff(mapInfosForPutOff);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				String ret = jsonObject.optString("code");
				if (ret.equals("0")) {
					String value = jsonObject.optString("value");
					if(value.equals("true")){
						ToastUtils.showShort(getActivity(), "推迟会议成功");
					}else{
						ToastUtils.showShort(getActivity(), "推迟会议失败");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @author Administrator 取消会议的任务
	 */
	private class CancelMeetingToHandler extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String str = HttpInterface.MeetingCancel(mapInfosForCancel);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				String ret = jsonObject.optString("code");
				if(ret.equals("0")){
					
					ToastUtils.showShort(getActivity(), "取消会议成功");
					new QueryMeetingToHandler().execute();
				}else{
					ToastUtils.showShort(getActivity(), "取消会议失败");
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * @author Administrator 不参加会议的任务
	 */
	private class UnattendMeetingToHandler extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			String str = HttpInterface.MeetingUnAttend(mapInfosForUnattend);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			progressDialog.dismiss();
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				String ret = jsonObject.optString("code");
				if (ret.equals("0")) {
					String value = jsonObject.optString("value");
					if(value.equals("true")){
						ToastUtils.showShort(getActivity(), "不参加会议成功");
					}else{
						ToastUtils.showShort(getActivity(), "不参加会议失败");
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}