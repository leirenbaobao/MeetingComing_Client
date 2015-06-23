package com.ctrlz.meetingcoming.login;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrlz.meetingcoming.AppConstants;
import com.ctrlz.meetingcoming.BaseActivity;
import com.ctrlz.meetingcoming.ContentActivity;
import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.ProgressDialog;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.httpaction.HttpInterface;
import com.ctrlz.meetingcoming.utils.StringUtils;
import com.ctrlz.meetingcoming.utils.ToastUtils;
import com.huawei.svn.sdk.mdm.DeviceIdInfo;
import com.huawei.svn.sdk.server.SvnApiService;
import com.huawei.svn.sdk.server.SvnCallBack;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_loginName;
	private EditText et_loginName;
	private ImageView btn_clear_loginName;

	private ImageView iv_loginPasswd;
	private EditText et_loginPasswd;
	private ImageView btn_clear_loginpasswd;

	private TextView btn_login;

	private SharedPreferences sp;
	private boolean isPwd = false;
	private CheckBox remember_code;
	
	private ProgressDialog progressDialog;

	private Map<String, String> mapForLogin = new HashMap<String, String>();
	private String loginId;
	private String loginPwd;
	
	private boolean isSvnSuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();

		// 初始化VPN
		VPNLoginIN();
	}

	private void initView() {
		
		RelativeLayout RL = (RelativeLayout)findViewById(R.id.login_title);
		TextView right_title = (TextView)RL.findViewById(R.id.right_title);
		right_title.setVisibility(View.VISIBLE);
		right_title.setText("SVN设置");
		right_title.setOnClickListener(this);
		
		progressDialog = new ProgressDialog(LoginActivity.this);
		
		sp = getSharedPreferences(AppConstants.USER_DATA, MODE_PRIVATE);
		TextView state_helper = (TextView) findViewById(R.id.state_helper);
		state_helper.setOnClickListener(this);

		iv_loginName = (ImageView) findViewById(R.id.iv_loginName);
		et_loginName = (EditText) findViewById(R.id.et_loginName);

		String loginName = sp.getString(AppConstants.LOGIN_NAME, "");
		String loginPwd = sp.getString(AppConstants.LOGIN_PASSWORD, "");
		isPwd = sp.getBoolean(AppConstants.IS_PASSWORD, false);

		if (loginName.trim().length() > 0) {
			et_loginName.setText(loginName);
			iv_loginName.setBackgroundResource(R.drawable.login_name_focus);
		}
		et_loginName.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				iv_loginName.setBackgroundResource(R.drawable.login_name_focus);

				return false;
			}
		});
		et_loginName.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// 失去焦点时判断有无输入
				if (!hasFocus) {
					if (et_loginName.getText().toString().trim().length() == 0)
						iv_loginName.setBackgroundResource(R.drawable.login_name);
				}
			}
		});

		// 密码
		iv_loginPasswd = (ImageView) findViewById(R.id.iv_loginPasswd);
		et_loginPasswd = (EditText) findViewById(R.id.et_loginPasswd);
		et_loginPasswd.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				iv_loginPasswd.setBackgroundResource(R.drawable.login_passwd_focus);

				return false;
			}
		});
		et_loginPasswd.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// 失去焦点时判断有无输入
				if (!hasFocus) {
					if (et_loginPasswd.getText().toString().trim().length() == 0)
						iv_loginPasswd.setBackgroundResource(R.drawable.login_passwd);
				}
			}
		});
		btn_clear_loginName = (ImageView) findViewById(R.id.btn_clear_loginName);
		btn_clear_loginpasswd = (ImageView) findViewById(R.id.btn_clear_loginpasswd);
		
		btn_login = (TextView) findViewById(R.id.btn_login);
		btn_clear_loginName.setOnClickListener(this);
		btn_clear_loginpasswd.setOnClickListener(this);
		btn_login.setOnClickListener(this);

		remember_code = (CheckBox) findViewById(R.id.remember_code);

		if (isPwd && loginPwd.trim().length() > 0) {
			et_loginPasswd.setText(loginPwd);
			iv_loginPasswd.setBackgroundResource(R.drawable.login_passwd_focus);
			remember_code.setChecked(isPwd);
		} else {
			remember_code.setChecked(isPwd);
		}

		remember_code.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					isPwd = true;
				}else{
					isPwd = false;
				}
			}
		});

		if (isPwd) {
			if (loginPwd.trim().length() > 0) {
				et_loginPasswd.setText(loginPwd);
				iv_loginPasswd.setBackgroundResource(R.drawable.login_passwd_focus);
				remember_code.setChecked(isPwd);
			} else {
				remember_code.setChecked(false);
			}
		} else {
			remember_code.setChecked(isPwd);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.state_helper:
			intent = new Intent();
			intent.setClass(LoginActivity.this, HelpActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_clear_loginName:
			et_loginName.setText("");
			break;
		case R.id.btn_clear_loginpasswd:
			et_loginPasswd.setText("");
			break;
		case R.id.right_title:
			
			intent = new Intent();
			intent.setClass(LoginActivity.this, SVNStore.class);
			startActivityForResult(intent, 100);
			
			break;
		case R.id.btn_login:
			
			if (!isSvnSuccess) {
				ToastUtils.showShort(LoginActivity.this, "请先登录SVN");
				break;
			}
			
			loginId = et_loginName.getText().toString();
			loginPwd = et_loginPasswd.getText().toString();
			if (StringUtils.isEmpty(loginId)) {
				ToastUtils.showShort(LoginActivity.this, "请输入用户名");
				return;
			} else if (StringUtils.isEmpty(loginPwd)) {
				ToastUtils.showShort(LoginActivity.this, "请输入密码");
				return;
			} else {
				Editor editor = sp.edit();
				if (isPwd) {
					editor.putString(AppConstants.LOGIN_PASSWORD, loginPwd);
				} else {
					editor.putString(AppConstants.LOGIN_PASSWORD, "");
				}
				editor.putString(AppConstants.LOGIN_NAME, loginId);
				editor.putBoolean(AppConstants.IS_PASSWORD, isPwd);
				editor.commit();

				mapForLogin.put("loginId", loginId);
				mapForLogin.put("password", loginPwd);

				new LoginInTask().execute();
			}
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == 20){
		
				String str = arg2.getStringExtra("flag");
				if(str.equals("reSVN")){
					VPNLoginIN();
				}
		}
	}

	private class LoginInTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String str = HttpInterface.HttpLogin(mapForLogin);
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			super.onPostExecute(result);
			try {
				JSONObject jsonObject = new JSONObject(result);
				String ret = jsonObject.optString("code");
				if (ret.equals("0")) {
					JSONObject json = new JSONObject(jsonObject.optString("value"));
					
					String name = json.optString("name");
					String position = json.optString("position");
					String attendNum = json.optString("attendNum");
					String launchNum = json.optString("launchNum");
					String unattendNum = json.optString("unattendNum");
					String gender = json.optString("gender");
					String department = json.optString("department");
					String holderStaffId = json.optString("staffId");
					
					//设置用户名
					MCApplication.setLoginId(loginId);
					
					MCApplication.setName(name);
					MCApplication.setPosition(position);
					MCApplication.setAttendNum(attendNum);
					MCApplication.setLaunchNum(launchNum);
					MCApplication.setUnattendNum(unattendNum);
					MCApplication.setGender(gender);
					MCApplication.setDeparment(department);
					MCApplication.setHolderStaffId(holderStaffId);

					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, ContentActivity.class);
					startActivity(intent);

					ToastUtils.showShort(LoginActivity.this, "登陆成功");
					finish();
				} else if(ret.equals(-1)){
					ToastUtils.showShort(LoginActivity.this, "用户名或密码错误");
				}else {
					ToastUtils.showShort(LoginActivity.this, "登陆失败");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtils.showShort(LoginActivity.this, AppConstants.NETWORK_ERROR);
			}
		}

	}
	
	private void VPNLogOff(){
		SvnApiService.logout();
	}

	@SuppressLint("SdCardPath")
	private void VPNLoginIN() {
		
		
		String SVNAddress = sp.getString(AppConstants.SVN_ADDRESS, "");
		String SVNName = sp.getString(AppConstants.SVN_NAME, "");
		String SVNCOde = sp.getString(AppConstants.SVN_CODE, "");
		
		if(StringUtils.isEmpty(SVNAddress)||StringUtils.isEmpty(SVNName)||StringUtils.isEmpty(SVNCOde)){
			ToastUtils.showShort(LoginActivity.this, "请先设置相关SVN信息");
			return;
		}
		
		
		/******* 集成 eSDK BYOD开发包 ******/
		System.out.println(getPackageName());
		String filePath = "/data/data/" + getPackageName() + "/file/";
		File file = new File(filePath);
		if (!file.exists()) {
			boolean res = file.mkdirs();
			if (!res) {
				Toast.makeText(LoginActivity.this, "创建失败", 3000).show();
				return;
			}
		}

		SvnApiService.setFileEnable("deviceid", "/data/data/" + getPackageName() + "/file/", true);
		SvnApiService.setCallBack(iCallBack);
		SvnApiService.undoCAChecking();

		String deviceId = new DeviceIdInfo(getBaseContext()).getDeviceId();
		int ret = SvnApiService.login(SVNName, SVNCOde, SVNAddress, "/data/data/" + getPackageName(), getPackageName(), deviceId);

		String res;
		if (0 == ret) {
			res = "SVN登陆成功";
			isSvnSuccess = true;
			ToastUtils.showShort(LoginActivity.this, res);
			System.out.println("res");
		} else {
			res = "SVN登陆失败";
			ToastUtils.showShort(LoginActivity.this, res);
			System.out.println("ret------------->" + ret);
		}
		progressDialog.dismiss();
	}

	// VPN回调处理函数
	private SvnCallBack iCallBack = new SvnCallBack() {
		@Override
		public void statusNotify(int arg0, int arg1) {
			/*
			 * if (isCreateFromAnyOffice) { isCreateFromAnyOffice = false;
			 * return; } Message msg = new Message();
			 * 
			 * msg.what = SvnCallbackImpl.SVN_LOAD_STATUS; msg.arg1 = arg0;
			 * msg.obj = arg1; myHandler.sendMessage(msg);
			 * Log.d("LoginActivity", "VPNStatus = " +
			 * SvnApiService.getVPNStatus() + ", iStatus = " + arg0 + ",iError="
			 * + arg1);
			 */
		}

		@Override
		public void writeLog(String strLog, int iLevel) {
			Log.d("LoginActivity", "" + strLog);
		}
	};
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		progressDialog.dismiss();
	}

}