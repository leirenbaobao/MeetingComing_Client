package com.ctrlz.meetingcoming;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;


public class LoadingDialog extends Dialog {

	public LoadingDialog(Context context)
	{
		super(context);
	}
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loading_dialog);
		this.setCancelable(false);
		ImageView iv_search_progress = (ImageView) findViewById(R.id.loading_img);
		iv_search_progress.setBackgroundResource(R.drawable.search_pg);
		AnimationDrawable anim = (AnimationDrawable) iv_search_progress.getBackground();
		anim.start();
	}

	
}
