package com.ctrlz.meetingcoming;

import com.ab.view.slidingmenu.SlidingMenu;
import com.ctrlz.meetingcoming.handler.MeetingsToHandler;
import com.ctrlz.meetingcoming.launch.LaunchNewMeetingFragment;
import com.ctrlz.meetingcoming.meetingpass.MeetingPass;
import com.ctrlz.meetingcoming.personal.PersonalActivity;
import com.ctrlz.meetingcoming.utils.StringUtils;
import com.ctrlz.meetingcoming.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ContentActivity extends FragmentActivity implements OnClickListener{

	SlidingMenu menu;
	
	RelativeLayout meeting_personal;
	RelativeLayout meeting_handle;
	RelativeLayout meeting_new;
	RelativeLayout meeting_pass;
	
	ImageView imageView;
	private PersonalActivity personalActivity;
	private MeetingsToHandler meetingHandler;
	private LaunchNewMeetingFragment newMeeting;
	private MeetingPass meetingPass;
	private int flag = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.content_activity);
		String temp = getIntent().getStringExtra("flag");
		if(!StringUtils.isEmpty(temp)){
			flag = Integer.parseInt(temp);
		}else{
			flag = 0;
		}
		
		personalActivity = new PersonalActivity();
		meetingHandler = new MeetingsToHandler();
		newMeeting = new LaunchNewMeetingFragment();
		meetingPass = new MeetingPass();
		
		getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, personalActivity).hide(personalActivity).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, meetingHandler).hide(meetingHandler).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, newMeeting).hide(newMeeting).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.content_fragment, meetingPass).hide(meetingPass).commit();
		
		Menu_Init();
		init();
		if(flag == 0){
			tabSelect(2);
		}else if(flag == 1){
			tabSelect(3);
		}
	}
	
	public void Menu_Init(){
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setBehindWidth(600);
		//menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.sliding_menu);
		menu.toggle();
		menu.showMenu();
		menu.showContent();
	}
	
	private void init(){
		meeting_personal = (RelativeLayout)findViewById(R.id.meeting_personal);
		meeting_personal.setOnClickListener(this);
		meeting_handle = (RelativeLayout)findViewById(R.id.meeting_to_hander_area);
		meeting_handle.setOnClickListener(this);
		meeting_new = (RelativeLayout)findViewById(R.id.meeting_new_area);
		meeting_new.setOnClickListener(this);
		meeting_pass = (RelativeLayout)findViewById(R.id.meeting_old_area);
		meeting_pass.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.meeting_personal:
			tabSelect(1);
			break;
		case R.id.meeting_to_hander_area:
			tabSelect(2);
			break;
		case R.id.meeting_new_area:
			tabSelect(3);
			break;
		case R.id.meeting_old_area:
			tabSelect(4);
			break;
		}
	}
	
	void clearAll(){
		getSupportFragmentManager().beginTransaction().hide(personalActivity).commit();
		getSupportFragmentManager().beginTransaction().hide(meetingHandler).commit();
		getSupportFragmentManager().beginTransaction().hide(newMeeting).commit();
		getSupportFragmentManager().beginTransaction().hide(meetingPass).commit();
	}
	
	void tabSelect(int id){
		clearAll();
		if(id ==1){
			getSupportFragmentManager().beginTransaction().hide(personalActivity).show(personalActivity).commit();
			menu.showContent();
		}else if(id ==2){
			getSupportFragmentManager().beginTransaction().hide(meetingHandler).show(meetingHandler).commit();
			menu.showContent();
		}else if(id ==3){
			getSupportFragmentManager().beginTransaction().hide(newMeeting).show(newMeeting).commit();
			menu.showContent();
		}else if(id ==4){
			getSupportFragmentManager().beginTransaction().hide(meetingPass).show(meetingPass).commit();
			menu.showContent();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent intent = new Intent("com.ctrlz.push.service.OnlineService");
		stopService(intent);
		System.out.println("Service 结束运行");
	}
	
	
	private long first_time = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			long second_time = System.currentTimeMillis();
			if(second_time - first_time>1500){
				ToastUtils.showShort(this, "再按一次返回键退出程序");
				first_time = second_time;
			}else{
				finish();
			}
			
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
}
