package com.ctrlz.meetingcoming.personal;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.login.LoginActivity;
import com.ctrlz.meetingcoming.utils.ToastUtils;

public class PersonalActivity extends Fragment implements OnClickListener{

	private Dialog showLogoutDialog;
	private TextView personal_data_launch_num;
	private TextView personal_data_more;
	private TextView personal_name;
	private TextView personal_position;
	private ImageView head_view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.personal_activity, container, false);
		init(view);
		return view;
	}

	private void init(View view){
		RelativeLayout personal_news_area = (RelativeLayout)view.findViewById(R.id.personal_news_area);
		personal_news_area.setOnClickListener(this);
		
		RelativeLayout personal_title = (RelativeLayout)view.findViewById(R.id.personal_title);
		TextView tv_title = (TextView)personal_title.findViewById(R.id.tv_title);
		tv_title.setText("个人主页");
		
		RelativeLayout check_new_area = (RelativeLayout)view.findViewById(R.id.check_new_area);
		check_new_area.setOnClickListener(this);
		
		TextView personal_logout = (TextView)view.findViewById(R.id.personal_logout);
		personal_logout.setOnClickListener(this);
		
		personal_data_launch_num = (TextView)view.findViewById(R.id.personal_data_launch_num);
		personal_data_more = (TextView)view.findViewById(R.id.personal_data_more);
		
		personal_name = (TextView)view.findViewById(R.id.personal_name);
		
		personal_position = (TextView)view.findViewById(R.id.personal_position);
		
		head_view = (ImageView)view.findViewById(R.id.head_view);
		
	}
	
	

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden){
			personal_data_launch_num.setText("共发起了" + MCApplication.getLaunchNum()+ "次会议");
			personal_data_more.setText("参加了"+ MCApplication.getAttendNum()+"次，" + "没有参加" + MCApplication.getUnattendNum()+ "次");
			personal_name.setText("姓名：" + MCApplication.getName());
			personal_position.setText("职位：" + MCApplication.getDeparment()+MCApplication.getPosition());
			
			if((MCApplication.getGender()).equals("男")){
				head_view.setImageResource(R.drawable.men_pic);
			}else{
				head_view.setImageResource(R.drawable.lady_pic);
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.personal_news_area:
			Intent intent = new Intent();
			intent.setClass(getActivity(), MessagesActivity.class);
			startActivity(intent);
			break;
		case R.id.personal_logout:
			showLogoutDialog();
			break;
		case R.id.check_new_area:
			ToastUtils.showShort(getActivity(), "当前软件已是最新版");
			break;
		}
		
	}
	
	/**
	 * 注销dialog
	 */
	private void showLogoutDialog() {
		showLogoutDialog = new Dialog(getActivity(), R.style.LoadingDialog);
		showLogoutDialog.setContentView(R.layout.dialog_logout);
		TextView sureBtn = (TextView) showLogoutDialog.findViewById(R.id.dialog_sure);
		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		TextView cancelBtn = (TextView) showLogoutDialog.findViewById(R.id.dialog_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showLogoutDialog.dismiss();
			}
		});
		showLogoutDialog.setCanceledOnTouchOutside(false);
		showLogoutDialog.show();
	}
	
	
}
