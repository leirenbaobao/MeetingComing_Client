package com.ctrlz.Zxing.demo;

import com.ctrlz.meetingcoming.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScanResult extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_result);
		
		initView();
	}
	
	private void initView(){
		RelativeLayout launchCompleteTitle = (RelativeLayout)findViewById(R.id.scan_result_title);
		TextView tv_title = (TextView)launchCompleteTitle.findViewById(R.id.tv_title);
		tv_title.setText("Ç©µ½³É¹¦");
		TextView scan_success_sure = (TextView)findViewById(R.id.scan_success_sure);
		scan_success_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	
}
