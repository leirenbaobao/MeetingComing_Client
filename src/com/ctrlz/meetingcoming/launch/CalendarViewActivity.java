package com.ctrlz.meetingcoming.launch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.utils.StringUtils;

public class CalendarViewActivity extends Activity {

	private TextView meeting_new_success_sure;
	// private CalendarView calendarView;
	private Dialog showChooseTimeDialog;
	private CalendarView myCalendar;
	private String date, time;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_view_activity);
		meeting_new_success_sure = (TextView)findViewById(R.id.meeting_new_success_sure);
		meeting_new_success_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showChooseTimeDialog();
			}
		});
		
		RelativeLayout calendar_title = (RelativeLayout)findViewById(R.id.newmeeting_title_calendar);
		TextView tv_title = (TextView)calendar_title.findViewById(R.id.tv_title);
		tv_title.setText("会议时间选择");
		
		myCalendar = (CalendarView)findViewById(R.id.calendar_view);
		myCalendar.setOnDateChangeListener(new OnDateChangeListener() {
			
			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
		 		date =  year+"-"+(month+1)+"-"+dayOfMonth;
			}
		});
	}

	private void showChooseTimeDialog() {
		showChooseTimeDialog = new Dialog(CalendarViewActivity.this, R.style.LoadingDialog);
		showChooseTimeDialog.setContentView(R.layout.meeting_new_choose_time_dialog);
		TextView sureBtn = (TextView) showChooseTimeDialog.findViewById(R.id.meeting_new_dialog_sure);
		final TimePicker timpicker = (TimePicker) showChooseTimeDialog.findViewById(R.id.meeting_new_dialog_time_picker);
		timpicker.setIs24HourView(true);
		timpicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				Date dates = new Date(0, 0, 0, hourOfDay, minute);
				time = timeFormat.format(dates);
			}
		});

		sureBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showChooseTimeDialog.dismiss();
				Intent data = new Intent();
				if (StringUtils.isEmpty(date)) {
					date = getCalSelected();
				}
				if(StringUtils.isEmpty(time)){
					time = timeFormat.format(new Date());
				}
				date = date + " " + time;
				data.putExtra("date", date);
				setResult(10, data);
				finish();
			}
		});
		ImageView imgBack = (ImageView) showChooseTimeDialog.findViewById(R.id.meeting_new_dialog_imgback);
		imgBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showChooseTimeDialog.dismiss();
			}
		});
		showChooseTimeDialog.setCanceledOnTouchOutside(false);
		showChooseTimeDialog.show();
	}
	
	public String getCalSelected() {
		Calendar calSelected = Calendar.getInstance();
		final int iYear = calSelected.get(Calendar.YEAR);
		final int iMonth = calSelected.get(Calendar.MONTH)+1;
		final int iDay = calSelected.get(Calendar.DAY_OF_MONTH);
		return iYear+"-"+iMonth+"-"+iDay;
	}
}
