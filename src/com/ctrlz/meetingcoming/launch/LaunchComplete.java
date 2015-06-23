package com.ctrlz.meetingcoming.launch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrlz.meetingcoming.ContentActivity;
import com.ctrlz.meetingcoming.R;

public class LaunchComplete extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_complete);
		
		initView();
	}
	
	private void initView(){
		RelativeLayout launchCompleteTitle = (RelativeLayout)findViewById(R.id.launch_new_result_title);
		TextView tv_title = (TextView)launchCompleteTitle.findViewById(R.id.tv_title);
		tv_title.setText("发起会议结果");
		TextView meeting_new_success_sure = (TextView)findViewById(R.id.meeting_new_success_sure);
		meeting_new_success_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LaunchComplete.this, ContentActivity.class);
				intent.putExtra("flag", String.valueOf(1));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}
}
