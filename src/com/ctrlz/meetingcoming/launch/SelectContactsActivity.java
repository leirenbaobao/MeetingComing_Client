package com.ctrlz.meetingcoming.launch;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ctrlz.meetingcoming.MCApplication;
import com.ctrlz.meetingcoming.ProgressDialog;
import com.ctrlz.meetingcoming.R;
import com.ctrlz.meetingcoming.httpaction.HttpInterface;
import com.ctrlz.meetingcoming.utils.ToastUtils;

public class SelectContactsActivity extends Activity implements OnClickListener {
	private ListView listView;
	private List<People> listPeople = new ArrayList<People>();
	private List<String> selectNames = new ArrayList<String>();
	private TextView choose_duration;

	ProgressDialog progressDialog;

	private LaunchMode lanuchMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_people_layout);
		initView();

	}

	private void initView() {
		progressDialog = new ProgressDialog(this);

		lanuchMode = MCApplication.getLanuchMode();

		// 执行查询通讯录的任务
		new QueryMailListTask().execute();

		RelativeLayout relative = (RelativeLayout) findViewById(R.id.selectpeople_title);
		TextView title = (TextView) relative.findViewById(R.id.tv_title);
		ImageView backBtn = (ImageView) relative.findViewById(R.id.back_btn);
		backBtn.setVisibility(View.VISIBLE);
		title.setText("通讯录");
		backBtn.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.listview);
		choose_duration = (TextView) findViewById(R.id.choose_duration);
		choose_duration.setOnClickListener(this);
	}

	class PeopleAdapter extends BaseAdapter {

		private Context context;
		private List<People> list;

		public PeopleAdapter(Context context, List<People> list) {
			this.context = context;
			this.list = list;
		}

		@Override
		public int getCount() {
			return list == null ? 0 : list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.select_people_item, null);
			}
			final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
			TextView nameTxt = (TextView) convertView.findViewById(R.id.name);
			TextView positionTxt = (TextView) convertView.findViewById(R.id.jobs);
			TextView departmentTxt = (TextView)convertView.findViewById(R.id.deparment);
			nameTxt.setText("姓名：" + listPeople.get(position).getName());
			positionTxt.setText("职位：" + listPeople.get(position).getPosition());
			departmentTxt.setText("部门：" + listPeople.get(position).getDepartment());
			final String name = list.get(position).getName();
			final String staffId = list.get(position).getStaffId();

			checkBox.setChecked(list.get(position).isChecked);
			// pwdText.setText(psw);
			checkBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (v instanceof CheckBox) {
						CheckBox ck = (CheckBox) v;
						if (ck.isChecked()) {
							selectNames.add(staffId);
						} else {
							selectNames.remove(staffId);
						}
						checkBox.setChecked(ck.isChecked());
					}
				}
			});

			return convertView;
		}
	}

	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.choose_duration:
			Intent intent = new Intent();
			intent.setClass(SelectContactsActivity.this, ChooseDuration.class);

			if(selectNames.size()==0){
				ToastUtils.showShort(SelectContactsActivity.this, "请选择与会人员");
				break;
			}
			
			String str = MCApplication.getHolderStaffId() + ",";
			for (int i = 0; i < selectNames.size(); i++) {
				str += selectNames.get(i) + ",";
			}
			// 减去最后一位“,”
			str = str.substring(0, str.length() - 1);

			lanuchMode.setIds(str);
			startActivity(intent);

			break;
		default:
			break;
		}
	}

	private class QueryMailListTask extends AsyncTask<String, Integer, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			progressDialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String str = HttpInterface.MeetingLanuchNewMailList();
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(result);
				String ret = jsonObject.optString("code");

				if (ret.equals("0")) {
					JSONArray jsonArr = jsonObject.optJSONArray("value");
						
						for (int j = 0; j < jsonArr.length(); j++) {
							String position = jsonArr.getJSONObject(j).getString("position");
							String staffId = jsonArr.getJSONObject(j).optString("staffId");
							String name = jsonArr.getJSONObject(j).optString("name");
							String department = jsonArr.getJSONObject(j).optString("department");
							
							if(staffId.equals(MCApplication.getHolderStaffId())){
								continue;
							}
							
							People people = new People();
							people.setDepartment(department);
							people.setName(name);
							people.setStaffId(staffId);
							people.setPosition(position);
							listPeople.add(people);
						}
						PeopleAdapter adapter = new PeopleAdapter(SelectContactsActivity.this, listPeople);
						listView.setAdapter(adapter);
					}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}