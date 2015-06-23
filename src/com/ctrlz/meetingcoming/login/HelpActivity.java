package com.ctrlz.meetingcoming.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrlz.meetingcoming.BaseActivity;
import com.ctrlz.meetingcoming.R;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_activity);
		initView() ;
	}

	private void initView() {
		RelativeLayout relative = (RelativeLayout) findViewById(R.id.help_title);
		TextView title = (TextView) relative.findViewById(R.id.tv_title);
		title.setText(" π”√∞Ô÷˙");
		ImageView backBtn = (ImageView)relative.findViewById(R.id.back_btn);
		backBtn.setVisibility(View.VISIBLE);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}