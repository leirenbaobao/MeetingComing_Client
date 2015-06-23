package com.ctrlz.push.receiver;

import com.ctrlz.push.service.OnlineService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootAlarmReceiver extends BroadcastReceiver {

	public BootAlarmReceiver() {

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startSrv = new Intent(context, OnlineService.class);
		startSrv.putExtra("CMD", "TICK");
		context.startService(startSrv);
	}

}
