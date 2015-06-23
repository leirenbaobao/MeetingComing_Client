package com.ctrlz.meetingcoming.launch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrlz.meetingcoming.BaseActivity;
import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.ProgressDialog;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.httpaction.HttpInterface;
import com.ctrlz.meetingcoming.utils.DateFormatUtils;
import com.ctrlz.meetingcoming.utils.ToastUtils;

public class ChooseDuration extends BaseActivity implements OnClickListener{

	private Map<String, String> mapInfoForLanuchNew = new HashMap<String, String>();
	private ListView listView;
	private TextView launch_btn;
	
	private boolean isClearAll = false;
	
	private List<DurationMode> durListCheck = new ArrayList<DurationMode>();
	
	private ProgressDialog progressDialog;
	
	private LaunchMode lanuchMode;
	
	private String durationChoosed = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_duration_activity);
		
		initView();
	}
	
	private void initView() {
		
		progressDialog = new ProgressDialog(this);
		
		lanuchMode = MCApplication.getLanuchMode();
		
		for(Duration duration: Duration.values()){
			DurationMode durationMode = new DurationMode();
			durationMode.setChoosed(false);
			durationMode.setDuration_string(duration.getName());
			durationMode.setDuration_number(duration.getMinute() + "");
			durListCheck.add(durationMode);
		}
		
		RelativeLayout relative = (RelativeLayout) findViewById(R.id.duration_title);
		TextView title = (TextView) relative.findViewById(R.id.tv_title);
		ImageView backBtn = (ImageView) relative.findViewById(R.id.back_btn);
		backBtn.setVisibility(View.VISIBLE);
		title.setText("会议时长");
		backBtn.setOnClickListener(this);
		
		listView = (ListView) findViewById(R.id.duration_listview);
		DurationTimeAdapter adapter = new DurationTimeAdapter(durListCheck, ChooseDuration.this);
		listView.setAdapter(adapter);
		
		launch_btn = (TextView)findViewById(R.id.launch_btn);
		launch_btn.setOnClickListener(this);
	}
	
	
	
	private class DurationTimeAdapter extends BaseAdapter{
		
		private List<DurationMode> durListCheck;
		private Context context;
		
		public DurationTimeAdapter(List<DurationMode> durListCheck, Context context){
			this.durListCheck = durListCheck;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return durListCheck.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return durListCheck.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.duration_lv_items, null);
			}
			final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.check_box_item);
			TextView duration = (TextView) convertView.findViewById(R.id.duration_time_item);
			
			duration.setText(durListCheck.get(arg0).getDuration_string());
			
			checkbox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v instanceof CheckBox) {
						
						CheckBox ck = (CheckBox) v;
						if (ck.isChecked()) {
//							selectNames.add(psw);
							durationChoosed = durListCheck.get(arg0).getDuration_number();
							clearAllCheck();
						}
						checkbox.setChecked(true);
					}
				}
			});
			
			if(isClearAll){
				checkbox.setChecked(false);
			}
			
			return convertView;
		}
		
	}

	
	private class LanuchNewMeeting extends AsyncTask<String, Integer, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String str = HttpInterface.MeetingLaunchNew(mapInfoForLanuchNew);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			try {
				JSONObject jsonObject = new JSONObject(result);
				String value = jsonObject.optString("value");
				if(value.equals("true")){
					
						Intent intent = new Intent();
						intent.setClass(ChooseDuration.this, LaunchComplete.class);
						startActivity(intent);
				}else{
					ToastUtils.showShort(ChooseDuration.this, "发起会议失败");
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
		switch(arg0.getId()){
		case R.id.launch_btn:
			if (durationChoosed==null) {
				ToastUtils.showShort(ChooseDuration.this, "请选择会议时间");
				break;
			}
			
			mapInfoForLanuchNew.put("staffIds", lanuchMode.getIds());
			mapInfoForLanuchNew.put("name", lanuchMode.getName());
			mapInfoForLanuchNew.put("subject", lanuchMode.getSubject());
			mapInfoForLanuchNew.put("sponsor", MCApplication.getHolderStaffId());
			mapInfoForLanuchNew.put("startTime", DateFormatUtils.getUnixDateWithLine(lanuchMode.getStartTime()));
			mapInfoForLanuchNew.put("duration", durationChoosed);
			mapInfoForLanuchNew.put("emergency", lanuchMode.getEmergency());
			mapInfoForLanuchNew.put("importance", lanuchMode.getImportance());
			mapInfoForLanuchNew.put("remarks", lanuchMode.getRemarks());
			mapInfoForLanuchNew.put("room", lanuchMode.getRoom());
			
			new LanuchNewMeeting().execute();
			break;
		case R.id.back_btn:
			finish();
			break;
		}
	}
	
	
	//获取所有的view
	public List<View> getAllChildViews() {
		View view = this.getWindow().getDecorView();
		return getAllChildViews(view);
	}

	private List<View> getAllChildViews(View view) {

		List<View> allchildren = new ArrayList<View>();
		if (view instanceof ViewGroup) {
			ViewGroup vp = (ViewGroup) view;
			for (int i = 0; i < vp.getChildCount(); i++) {
				View viewchild = vp.getChildAt(i);
				allchildren.add(viewchild);
				allchildren.addAll(getAllChildViews(viewchild));
			}
		}
		return allchildren;
	}
	
	//遍历view清空
	private void clearAllCheck(){
		List<View> lists = new ArrayList<View>();
		lists = getAllChildViews();
		for(int i = 0; i< lists.size(); i++){
			if(lists.get(i) instanceof CheckBox){
				((CheckBox)lists.get(i)).setChecked(false);
				
			}
		}
	}
	
}
