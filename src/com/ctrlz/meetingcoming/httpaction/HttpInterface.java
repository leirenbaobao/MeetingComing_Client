package com.ctrlz.meetingcoming.httpaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ctrlz.meetingcoming.AppConstants;

public class HttpInterface {
	
	/**
	 * 1
	 * 登陆接口
	 * @param map
	 * @return
	 */
	public static String HttpLogin(Map<String, String> map){
		String loginId = map.get("loginId");
		String password = map.get("password");
		String strParam = "loginId=" +loginId  + "&password=" + password;  
		String strURL = AppConstants.getUrl(AppConstants.LOGIN) + "?" + strParam;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		
		/*List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); 
		params.add(new BasicNameValuePair("loginId", loginId)); 
		params.add(new BasicNameValuePair("password", password));
		String str = HttpAction.httpActionClientPostInVpn(strURL, params);*/
		return str;
	}
	
	/**
	 * 2
	 * 待办会议获取
	 * @param map
	 * @return
	 */
	public static String MeetingHandler(Map<String, String> map){
		String staffId = map.get("staffId");
		String strParam = "staffId=" + staffId;
		String strURL = AppConstants.getUrl(AppConstants.QUERY_HANDLE) + "?" + strParam;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		
		/*List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); 
		params.add(new BasicNameValuePair("staffId", staffId)); 
		String str = HttpAction.httpActionClientPostInVpn(strURL, params);*/
		return str;
	}
	
	/**
	 * 3
	 * 获取已结束的会议
	 * @param map
	 * @return
	 */
	public static String MeetingFinished(Map<String, String> map){
		String staffId = map.get("staffId");
		String strParam = "staffId=" + staffId;
		String strURL = AppConstants.getUrl(AppConstants.MEETING_FINISHED) + "?" + strParam;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		
		/*List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); 
		params.add(new BasicNameValuePair("staffId", staffId)); 
		String str = HttpAction.httpActionClientPostInVpn(strURL, params);*/
		return str;
	}
	
	/**
	 * 4
	 * 推迟会议
	 * @return
	 */
	public static String MeetingPutOff(Map<String, String> map){
		
		String meetingId = map.get("meetingId");
		String time = map.get("time");
		String strParam = "meetingId=" + meetingId + "&time=" + time;
		String strURL = AppConstants.getUrl(AppConstants.MEETING_PUT_OFF) + "?" + strParam;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		
		/*List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); 
		params.add(new BasicNameValuePair("meetingId", meetingId)); 
		params.add(new BasicNameValuePair("time", time)); 
		String str = HttpAction.httpActionClientPostInVpn(strURL, params);*/
		return str;
	}
	
	/**
	 * 5
	 * 取消会议
	 * @param map
	 * @return
	 */
	public static String MeetingCancel(Map<String, String> map){
		
		String meetingId = map.get("meetingId");
		String strParam = "meetingId=" + meetingId;
		String strURL = AppConstants.getUrl(AppConstants.MEETING_CANCEL) + "?" + strParam;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		
		/*List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); 
		params.add(new BasicNameValuePair("meetingId", meetingId)); 
		String str = HttpAction.httpActionClientPostInVpn(strURL, params);*/
		return str;
	}
	
	/**
	 * 6
	 * 不参加会议
	 * @param map
	 * @return
	 */
	public static String MeetingUnAttend(Map<String, String> map){
		String meetingId = map.get("meetingId");
		String staffId = map.get("staffId");
		String strParam = "meetingId=" + meetingId + "&staffId=" + staffId;
		String strURL = AppConstants.getUrl(AppConstants.MEETING_UNATTEND) + "?" + strParam;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		
		/*List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); 
		params.add(new BasicNameValuePair("meetingId", meetingId)); 
		params.add(new BasicNameValuePair("staffId", staffId)); 
		String str = HttpAction.httpActionClientPostInVpn(strURL, params);*/
		
		return str;
		
	}
	
	/**
	 * 7
	 * 获取联系人
	 * @param map
	 * @return
	 */
	public static String MeetingLanuchNewMailList(){
		String strURL = AppConstants.getUrl(AppConstants.CONTACTS);
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		return str;
	}
	
	/**
	 * 8
	 * 发起新会议
	 * @param map
	 * @return
	 */
	public static String MeetingLaunchNew(Map<String, String> map){
		
		String staffIds = map.get("staffIds");
		String name = map.get("name");
		String subject = map.get("subject");
		String sponsor = map.get("sponsor");
		String startTime = map.get("startTime");
		String duration = map.get("duration");
		String emergency = map.get("emergency");
		String importance = map.get("importance");
		String remarks = map.get("remarks");
		String room = map.get("room");
		
		String strParam = "staffIds=" + staffIds + "&name=" + name +"&subject=" +subject + "&sponsor=" + sponsor +
					"&startTime=" + startTime + "&duration=" + duration + "&emergency=" + emergency + "&importance=" + importance + 
					"&remarks=" + remarks + "&room=" + room;  
		
//		String strURL = AppConstants.getUrl(AppConstants.LAUNCH_MEETING) +"?"+ strParam;
		String strURL = AppConstants.getUrl(AppConstants.LAUNCH_MEETING);
//		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(); 
		params.add(new BasicNameValuePair("staffIds", staffIds)); 
		params.add(new BasicNameValuePair("name", name)); 
		params.add(new BasicNameValuePair("subject", subject)); 
		params.add(new BasicNameValuePair("sponsor", sponsor)); 
		params.add(new BasicNameValuePair("startTime", startTime)); 
		params.add(new BasicNameValuePair("duration", duration)); 
		params.add(new BasicNameValuePair("emergency", emergency)); 
		
		params.add(new BasicNameValuePair("importance", importance)); 
		params.add(new BasicNameValuePair("remarks", remarks)); 
		params.add(new BasicNameValuePair("room", room)); 
		String str = HttpAction.httpActionClientPostInVpn(strURL, params);
		
		return str;
	}
	
	/**
	 * 9
	 * 获取消息
	 * @param map
	 * @return
	 */
	public static String GetMessage(Map<String, String> map){
		String loginId = map.get("staffId");
		String strParam = "staffId=" + loginId;
		String strURL = AppConstants.getUrl(AppConstants.QUERY_MESSAGES) + "?" + strParam;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		return str;
	}
	
	/**
	 * 10
	 * 删除消息
	 * @param map
	 * @return
	 */
	public static String DeleteMessage(Map<String, String> map){
		String messageId = map.get("messageId");
		String strParam = "messageId=" + messageId;
		String strURL = AppConstants.getUrl(AppConstants.DELETE_MESSAGES) + "?" + strParam;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
//		String str = HttpAction.httpActionClientGet(strURL);
		return str;
	}
	
	/**
	 * 二维码扫描签到
	 * @param map
	 * @return
	 */
	public static String Sign(Map<String, String> map){
		String staffId = map.get("staffId"); 
		String scanUrl = map.get("scanUrl");
		String strURL = scanUrl + "&staffId=" + staffId;
		String str = HttpAction.httpActionClientGetInVpn(strURL);
		return str;
	}
	
}
