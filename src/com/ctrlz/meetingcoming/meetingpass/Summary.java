package com.ctrlz.meetingcoming.meetingpass;

import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.utils.DateFormatUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Summary extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.summary_meeting);
		
		init();
	}

	
	private void init(){
		
		MeetingPassMode meetingPassMode = (MeetingPassMode) getIntent().getSerializableExtra("Infos");
		
		RelativeLayout summary_title = (RelativeLayout)findViewById(R.id.summary);
		TextView tv_title = (TextView)summary_title.findViewById(R.id.tv_title);
		tv_title.setText("会议纪要");
		ImageView back_btn = (ImageView)summary_title.findViewById(R.id.back_btn);
		back_btn.setVisibility(View.VISIBLE);
		back_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		TextView pass_name = (TextView)findViewById(R.id.pass_name);
		pass_name.setText(meetingPassMode.getMeetingPassName());
		
		TextView pass_room = (TextView)findViewById(R.id.pass_room);
		pass_room.setText(meetingPassMode.getMeetingPassRoom());
		
		TextView pass_time = (TextView)findViewById(R.id.pass_time);
		pass_time.setText(DateFormatUtils.getDate(meetingPassMode.getMeetingPassStartTime()));
		
		TextView pass_launcher = (TextView)findViewById(R.id.pass_launcher);
		pass_launcher.setText(meetingPassMode.getMeetingPassLauncher());
		
		TextView pass_content = (TextView)findViewById(R.id.pass_content);
		String str = meetingPassMode.getMeetingPassSummary();
		String str2 = "";
		String string[] = str.split(";");
		for(int i = 0; i < string.length; i++){
			string[i] = string[i] + "。\n";
		}
		for(int i = 0; i < string.length; i++){
			str2 = str2 + string[i];
		}
		pass_content.setText(str2);
		
	}

}
