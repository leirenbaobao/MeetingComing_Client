package com.ctrlz.meetingcoming;

public class AppConstants {

	//测试地址
//	public static final String DOMAINNAME = "http://31caabe4.ngrok.com/";
	//阿里云服务器地址
	public static final String DOMAINNAME = "http://www.lofver.com/";
	
	
	/**
	 * 服务器请求地址设定
	 */
	//登录
	public static final String LOGIN = "base/signIn";
	//查询待办会议
	public static final String QUERY_HANDLE = "meeting/query";
	//推迟会议 
	public static final String MEETING_PUT_OFF = "meeting/delay";
	//取消会议
	public static final String MEETING_CANCEL = "meeting/cancel";
	//不参加会议
	public static final String MEETING_UNATTEND = "meeting/refuse";
	//获取通讯录
	public static final String CONTACTS = "base/contacts";
	//已结束会议
	public static final String MEETING_FINISHED = "meeting/history";
	
	//查询消息
	public static final String QUERY_MESSAGES = "message/query";
	//删除消息
	public static final String DELETE_MESSAGES = "message/delete";
	
	//会议签到
	public static final String SIGN_MEETING = "meeting/sign";
	
	//发起会议
	public static final String LAUNCH_MEETING = "meeting/launch";
	
	//会议签到
	public static final String MEETING_SIGN = "sign?meetingId={";
	
	public static final String USER_DATA = "user_date";
	public static final String LOGIN_NAME = "login_name";
	public static final String LOGIN_PASSWORD = "login_password";
	public static final String IS_PASSWORD = "remember_word";
	
	
	public static final String SVN_ADDRESS = "svn_address";
	public static final String SVN_NAME = "svn_name";
	public static final String SVN_CODE = "svn_code";
	
	//网络异常
	public static final String NETWORK_ERROR = "网络异常";
	
	public static String getUrl(String urlName) {
		return DOMAINNAME  + urlName;
	}

}
