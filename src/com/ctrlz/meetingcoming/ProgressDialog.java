package com.ctrlz.meetingcoming;
/**
 * 等待框
 */

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

public class ProgressDialog extends Dialog
{

	public ProgressDialog(Context context)
	{
		this(context, R.style.LoadingDialog);
	}
	
	public ProgressDialog(Context context, int theme)
	{
		super(context, theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.progress_dialog);
		// 设置点击其他窗口外其他区域不取消对话框,返回键也不可退出
		this.setCancelable(false);
		ImageView iv_search_progress = (ImageView) findViewById(R.id.iv__progress);
		iv_search_progress.setBackgroundResource(R.drawable.search_pg);
		AnimationDrawable anim = (AnimationDrawable) iv_search_progress.getBackground();
		anim.start();
	}
	
	//防止网络down掉，设置返回键可以退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			this.setCancelable(true);
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
