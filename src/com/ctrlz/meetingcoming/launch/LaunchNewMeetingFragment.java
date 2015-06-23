package com.ctrlz.meetingcoming.launch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.utils.StringUtils;
import com.ctrlz.meetingcoming.utils.ToastUtils;

public class LaunchNewMeetingFragment extends Fragment implements OnClickListener {

	private EditText meetingAddr, meetingTheme, meetingName, meetingMark;
	private TextView next_btn, meetingDate;
	private RatingBar importantBar, decisiveBar;
	private String selectDate;
	private SimpleDateFormat sdf = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.launch_newmeeting, null);
		initView(view);
		return view;

	}

	private void initView(View view) {
		TextView title = (TextView) view.findViewById(R.id.tv_title);
		title.setText("发起新会议");
		// ImageView backBtn = (ImageView)view.findViewById(R.id.back_btn);
		// backBtn.setVisibility(View.VISIBLE);
		// backBtn.setOnClickListener(this);
		meetingAddr = (EditText) view.findViewById(R.id.meeting_addr);
		meetingTheme = (EditText) view.findViewById(R.id.meeting_theme);
		next_btn = (TextView) view.findViewById(R.id.next_btn);
		meetingDate = (TextView) view.findViewById(R.id.meeting_date);
		next_btn.setOnClickListener(this);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = new Date();
		String dateString = sdf.format(date);
		meetingDate.setText(dateString);
		meetingDate.setOnClickListener(this);
		importantBar = (RatingBar) view.findViewById(R.id.imporment_level);
		decisiveBar = (RatingBar) view.findViewById(R.id.decisive_level);
		
		meetingName = (EditText)view.findViewById(R.id.meeting_name);
		meetingMark = (EditText)view.findViewById(R.id.meeting_mark);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 10:
			selectDate = data.getStringExtra("date");
			try {
				Date select = (Date) sdf.parse(selectDate);
				Date date =new Date();
				String dateString = sdf.format(date);
				Date currentDate = (Date) sdf.parse(dateString);
				if(!select.after(currentDate)){
					ToastUtils.showShort(getActivity(), "请选择当前时间以后的时间");
					return;
				}else{
					meetingDate.setText(selectDate);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.next_btn:
			String address = meetingAddr.getText().toString();
			String theme = meetingTheme.getText().toString();
			String name = meetingName.getText().toString();
			String mark = meetingMark.getText().toString();
 			if (StringUtils.isEmpty(address) || StringUtils.isEmpty(theme)||StringUtils.isEmpty(name)||StringUtils.isEmpty(mark)) {
				ToastUtils.showShort(getActivity(), "请填写会议事项");
			break;
			}
			
			LaunchMode lanuchMode = new LaunchMode();
			lanuchMode.setName(meetingName.getText().toString().trim());
			
			String str = String.valueOf(decisiveBar.getRating());
			str = str.substring(0, str.length()-2);
			lanuchMode.setEmergency(str);
			
			String string = String.valueOf(importantBar.getRating());
			string = string.substring(0, string.length()-2);
			
			if(str.equals("0")||string.equals("0")){
				ToastUtils.showShort(getActivity(), "请选择会议紧急与重要值");
				break;
			}
			
			lanuchMode.setImportance(string);
			
			lanuchMode.setRoom(meetingAddr.getText().toString().trim());
			lanuchMode.setSubject(meetingTheme.getText().toString().trim());
			lanuchMode.setRemarks(meetingMark.getText().toString().trim());
			lanuchMode.setStartTime(meetingDate.getText().toString().trim());
			
			intent = new Intent(getActivity(), SelectContactsActivity.class);
			
			MCApplication.setLanuchMode(lanuchMode);
			
			startActivity(intent);
			break;
		case R.id.back_btn:
			break;
		case R.id.meeting_date:
			intent = new Intent(getActivity(), CalendarViewActivity.class);
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}
	
}