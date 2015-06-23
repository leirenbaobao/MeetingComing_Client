package com.ctrlz.meetingcoming.login;

import com.ctrlz.meetingcoming.AppConstants;
import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.utils.StringUtils;
import com.ctrlz.meetingcoming.utils.ToastUtils;
import com.huawei.svn.sdk.server.SvnApiService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SVNStore extends Activity {
	
	private SharedPreferences sp;
	private EditText svn_address;
	private EditText svn_name;
	private EditText svn_code;
	private TextView svn_store;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.svn_layout);
		
		init();
	}
	
	private void init(){
		
		sp = getSharedPreferences(AppConstants.USER_DATA, MODE_PRIVATE);
		editor = sp.edit();
		
		String SVNAddress = sp.getString(AppConstants.SVN_ADDRESS, "");
		String SVNName = sp.getString(AppConstants.SVN_NAME, "");
		String SVNCOde = sp.getString(AppConstants.SVN_CODE, "");
		
		RelativeLayout RL = (RelativeLayout)findViewById(R.id.svn_title);
		TextView svnTitle = (TextView)RL.findViewById(R.id.tv_title);
		svnTitle.setText("SVN设置");
		svnTitle.setVisibility(View.VISIBLE);
		
		svn_address = (EditText)findViewById(R.id.svn_address);
		svn_address.setText(SVNAddress);
		svn_name = (EditText)findViewById(R.id.svn_name);
		svn_name.setText(SVNName);
		svn_code = (EditText)findViewById(R.id.svn_code);
		svn_code.setText(SVNCOde);
		svn_store = (TextView)findViewById(R.id.svn_store);
		
		svn_store.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String address = svn_address.getText().toString().trim();
				String name = svn_name.getText().toString().trim();
				String code = svn_code.getText().toString().trim();
				
				if(StringUtils.isEmpty(address)||StringUtils.isEmpty(name)||StringUtils.isEmpty(code)){
					ToastUtils.showShort(SVNStore.this, "请填写相关信息");
				}else{
					
					/*MCApplication.setSVNAddress(address);
					MCApplication.setSVNName(name);
					MCApplication.setSVNCode(code);*/
				
					editor.putString(AppConstants.SVN_ADDRESS, address);
					editor.putString(AppConstants.SVN_NAME, name);
					editor.putString(AppConstants.SVN_CODE, code);
					editor.commit();
					ToastUtils.showShort(SVNStore.this, "保存成功");
					
					
					VPNLogOff();
					
					/*Intent intent = new Intent();
					intent.setClass(SVNStore.this, LoginActivity.class);
					startActivity(intent);*/
					Intent data=new Intent();  
		            data.putExtra("flag", "reSVN");  
		            //请求代码可以自己设置，这里设置成20  
		            setResult(20, data);
					
					finish();
				}
			}
		});
		
	}
	
	private void VPNLogOff(){
		SvnApiService.logout();
	}
	

}
