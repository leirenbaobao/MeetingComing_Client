package com.ctrlz.meetingcoming.meetingpass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.ProgressDialog;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.httpaction.HttpInterface;
import com.ctrlz.meetingcoming.utils.DateFormatUtils;
import com.ctrlz.meetingcoming.utils.ToastUtils;

public class MeetingPass extends Fragment{
	
	private List<MeetingPassMode> meetingPassList = new ArrayList<MeetingPassMode>();
	
	private AbPullToRefreshView refreshView;
	
	private Map<String, String> mapInfosForQueryPass= new HashMap<String, String>();

	private ListView passList;
	
	private ProgressDialog progressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.meeting_pass, container, false);
		init(view);
		return view;
		
	}
	
	private void init(View view){
		RelativeLayout meeting_pass_title = (RelativeLayout)view.findViewById(R.id.meeting_pass_title);
		TextView tv_title = (TextView)meeting_pass_title.findViewById(R.id.tv_title);
		tv_title.setText("已结束会议");
		
		progressDialog = new ProgressDialog(getActivity());
		
		mapInfosForQueryPass.put("staffId", MCApplication.getHolderStaffId());
		
		passList = (ListView)view.findViewById(R.id.meeting_pass_list);
		/*MeetingPassAdapter meetingPassAdapter = new MeetingPassAdapter(meetingPassList);
		passList.setAdapter(meetingPassAdapter);*/
		passList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				
				intent.putExtra("Infos", meetingPassList.get(arg2));
				intent.setClass(getActivity(), Summary.class);
				startActivity(intent);
			}
		});
		
		
		refreshView = (AbPullToRefreshView)view.findViewById(R.id.meeting_pass_refresh);
		refreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
					
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				ToastUtils.showShort(getActivity(), "没有更多已结束会议了");
				refreshView.onFooterLoadFinish();
			}
		});
		refreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
//				此处执行更新消息的任务
				refreshView.onHeaderRefreshFinish();
				new QueryMeetingPassTask().execute();
			}
		});
	}
	
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden){
			
			new QueryMeetingPassTask().execute();
		}
	}



	private class MeetingPassHolder {
		TextView meetingPassName;
		TextView meetingPassPlace;
		TextView meetingPassTime;
		TextView meetingPassLanucher;
	}
	
	private class MeetingPassAdapter extends BaseAdapter{
		private List<MeetingPassMode> meetingPassList;
		
		
		public MeetingPassAdapter(List<MeetingPassMode> _meetingPassList) {
			// TODO Auto-generated constructor stub
			meetingPassList = _meetingPassList;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return meetingPassList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return meetingPassList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			MeetingPassHolder meetingPassHolder;
			if(arg1==null){
				arg1 = LayoutInflater.from(getActivity()).inflate(R.layout.meeting_pass_items, null);
				meetingPassHolder = new MeetingPassHolder();
				meetingPassHolder.meetingPassLanucher = (TextView)arg1.findViewById(R.id.meeting_over_lanucher);
				meetingPassHolder.meetingPassName = (TextView)arg1.findViewById(R.id.meeting_over_name);
				meetingPassHolder.meetingPassPlace = (TextView)arg1.findViewById(R.id.meeting_over_place);
				meetingPassHolder.meetingPassTime = (TextView)arg1.findViewById(R.id.meeting_over_time);
				arg1.setTag(meetingPassHolder);
			}else{
				meetingPassHolder = (MeetingPassHolder)arg1.getTag();
			}
			
			meetingPassHolder.meetingPassLanucher.setText("发起人："+ meetingPassList.get(arg0).getMeetingPassLauncher());
			meetingPassHolder.meetingPassName.setText("会议名称：" + meetingPassList.get(arg0).getMeetingPassName());
			meetingPassHolder.meetingPassPlace.setText("地点：" + meetingPassList.get(arg0).getMeetingPassRoom());
			meetingPassHolder.meetingPassTime .setText("时间：" + DateFormatUtils.getDate(meetingPassList.get(arg0).getMeetingPassStartTime()));
			
			return arg1;
		}
		
	}
	
	/**
	 * @author David
	 * 查询已结束会议的任务
	 */
	public class QueryMeetingPassTask extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String str = HttpInterface.MeetingFinished(mapInfosForQueryPass);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			meetingPassList.clear();
			try {
				JSONObject jsonObject = new JSONObject(result);
				String ret = jsonObject.optString("code");
				JSONArray jsonArray = jsonObject.optJSONArray("value");
				for(int i = 0; i<jsonArray.length(); i++){
					
					String subject = jsonArray.getJSONObject(i).optString("subject");
					String emergency = jsonArray.getJSONObject(i).optString("emergency");
					String number = jsonArray.getJSONObject(i).optString("number");
					String remarks = jsonArray.getJSONObject(i).optString("remarks");
					String sponsor = jsonArray.getJSONObject(i).optString("sponsor");
					String endTime = jsonArray.getJSONObject(i).optString("endTime");
					String sign = jsonArray.getJSONObject(i).optString("sign");
					String startTime = jsonArray.getJSONObject(i).optString("startTime");
					String duration = jsonArray.getJSONObject(i).optString("duration");
					String leave = jsonArray.getJSONObject(i).optString("leave");
					String name = jsonArray.getJSONObject(i).optString("name");
					String pass_place = jsonArray.getJSONObject(i).optString("room");
					String pass_meeting_id = jsonArray.getJSONObject(i).optString("meetingId");
					String pass_meeting_summary = jsonArray.getJSONObject(i).optString("summary");
					
					MeetingPassMode meetingPassMode = new MeetingPassMode();
					meetingPassMode.setMeetingPassSubject(subject);
					meetingPassMode.setMeetingPassEmergency(emergency);
					meetingPassMode.setMeetingPassRemarks(remarks);
					meetingPassMode.setMeetingPassLauncher(sponsor);
					meetingPassMode.setMeetingPassEndTime(endTime);
					meetingPassMode.setMeetingPassSign(sign);
					meetingPassMode.setMeetingPassStartTime(startTime);
					meetingPassMode.setMeetingPassDuration(duration);
					meetingPassMode.setMeetingPassLeave(leave);
					meetingPassMode.setMeetingPassName(name);
					meetingPassMode.setMeetingPassRoom(pass_place);
					meetingPassMode.setMeetingPassId(pass_meeting_id);
					meetingPassMode.setMeetingPassSummary(pass_meeting_summary);
					
					meetingPassList.add(meetingPassMode);
				}
				MeetingPassAdapter meetingPassAdapter = new MeetingPassAdapter(meetingPassList);
				passList.setAdapter(meetingPassAdapter);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
