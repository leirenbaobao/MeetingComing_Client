package com.ctrlz.meetingcoming.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrlz.meetingcoming.R;

public class ToastUtils {

	public static void showShort(Context context, String msg) {
		View view = LayoutInflater.from(context).inflate(R.layout.toast_self_define, null);
		TextView textView = (TextView) view.findViewById(R.id.toast_info);
		textView.setText(msg);

		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(1000);
		toast.setGravity(Gravity.CENTER, 0, 500);
		toast.show();
	}

}