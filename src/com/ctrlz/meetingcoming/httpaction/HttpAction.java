package com.ctrlz.meetingcoming.httpaction;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionOperator;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.ctrlz.meetingcoming.AppConstants;
import com.ctrlz.meetingcoming.utils.ToastUtils;
import com.huawei.svn.sdk.thirdpart.SvnClientConnectionOperator;
import com.huawei.svn.sdk.thirdpart.SvnHttpSocketFactory;

public class HttpAction {

	/**
	 * post请求
	 * 
	 * @param strURL
	 * @param strParam
	 * @return
	 */
	public static String HttpActionDoPost(String strURL, String strParam) {
		String resultData = "";
		try {
			URL url = new URL(strURL);
			// 使用HttpURLConnection打开连接
			HttpURLConnection urlConn = null;
			DataOutputStream out = null;
			BufferedReader reader = null;
			try {
				urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				// 设置以POST方式
				urlConn.setRequestMethod("POST");
				// 设置连接超时
				urlConn.setConnectTimeout(10 * 1000);
				urlConn.setReadTimeout(10 * 1000);
				// Post 请求不能使用缓存
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(true);
				// 设置指定的请求头字段的值
				urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				urlConn.setRequestProperty("Charset", "utf-8");
				urlConn.connect();
				out = new DataOutputStream(urlConn.getOutputStream());

				// 编码设置
				// URLEncoder.encode(strParam, "UTF_8");
				// 将要上传的内容写入流中
				out.writeBytes(strParam);
				out.flush();
				// 获取数据
				reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));// 这句话报错
				String inputLine = null;
				while (((inputLine = reader.readLine()) != null)) {
					resultData += inputLine + "\n";
				}

			} catch (IOException e) {
				resultData = "operation failure !";
				e.printStackTrace();
			} finally {
				try {
					if (out != null)
						out.close();
					if (reader != null)
						reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (urlConn != null)
					urlConn.disconnect();
			}

		} catch (MalformedURLException e) {
			resultData = "Time out !";
			e.printStackTrace();
		}

		System.out.println("jsonResult:" + resultData);
		return resultData;
	}

	/**
	 * post请求 in Vpn
	 * 
	 * @param strURL
	 * @param strParam
	 * @return
	 */
	public static String HttpActionDoPostInVpn(String strURL, String strParam) {
		String resultData = "";
		try {
			URL url = new URL(strURL);
			// 使用HttpURLConnection打开连接
			HttpURLConnection urlConn = null;
			DataOutputStream out = null;
			BufferedReader reader = null;
			try {
				// 华为demo设置一行
				URLConnectionFactoryHelper.setURLStreamHandlerFactory();

				urlConn = (HttpURLConnection) url.openConnection();
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				// 设置以POST方式
				urlConn.setRequestMethod("POST");
				// 设置连接超时
				urlConn.setConnectTimeout(10 * 1000);
				urlConn.setReadTimeout(10 * 1000);
				// Post 请求不能使用缓存
				urlConn.setUseCaches(false);
				urlConn.setInstanceFollowRedirects(true);
				// 设置指定的请求头字段的值
				urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				urlConn.setRequestProperty("Charset", "utf-8");
				urlConn.connect();
				out = new DataOutputStream(urlConn.getOutputStream());

				// 编码设置
				// URLEncoder.encode(strParam, "UTF_8");
				// 将要上传的内容写入流中
				out.writeBytes(strParam);
				out.flush();
				// 获取数据
				reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));// 这句话报错
				String inputLine = null;
				while (((inputLine = reader.readLine()) != null)) {
					resultData += inputLine + "\n";
				}

			} catch (IOException e) {
				resultData = "operation failure !";
				e.printStackTrace();
			} finally {
				try {
					if (out != null)
						out.close();
					if (reader != null)
						reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (urlConn != null)
					urlConn.disconnect();
			}

		} catch (MalformedURLException e) {
			resultData = "Time out !";
			e.printStackTrace();
		}

		System.out.println("jsonResult:" + resultData);
		return resultData;
	}

	public static String HttpActionDoGet(String strURL) {
		String resultData = "";
		InputStreamReader in = null;
		HttpURLConnection urlConn = null;
		try {
			URL url = new URL(strURL);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(10 * 1000);
			urlConn.setReadTimeout(10 * 1000);
			in = new InputStreamReader(urlConn.getInputStream());
			BufferedReader buffer = new BufferedReader(in);
			String inputLine = "";
			while ((inputLine = buffer.readLine()) != null) {
				resultData += inputLine + "\n";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
		return resultData;
	}

	public static String HttpActionDoGetInVpn(String strURL) {
		String resultData = "";
		InputStreamReader in = null;
		HttpURLConnection urlConn = null;
		try {
			URL url = new URL(strURL);

			URLConnectionFactoryHelper.setURLStreamHandlerFactory();

			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(10 * 1000);
			urlConn.setReadTimeout(10 * 1000);
			in = new InputStreamReader(urlConn.getInputStream());
			BufferedReader buffer = new BufferedReader(in);
			String inputLine = "";
			while ((inputLine = buffer.readLine()) != null) {
				resultData += inputLine + "\n";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (urlConn != null) {
				urlConn.disconnect();
			}
		}
		return resultData;
	}

	public static String httpActionClientGet(String strURL) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet get = new HttpGet(strURL);
		HttpResponse httpResponse;
		String resultData = "";
		try {
			httpResponse = httpClient.execute(get);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				BufferedReader buffer = new BufferedReader(new InputStreamReader(entity.getContent()));
				String inputLine = "";
				while ((inputLine = buffer.readLine()) != null) {
					resultData += inputLine + "\n";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("网络异常");
		}
		return resultData;
	}

	public static String httpActionClientGetInVpn(String strURL) {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", new SvnHttpSocketFactory(), 80));
		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 30000);

		ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(params, schemeRegistry) {
			/* overide this for dns parse */
			@Override
			protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
				return new SvnClientConnectionOperator(schreg);
			}
		};

		HttpClient httpClient = new DefaultHttpClient(clientConnectionManager, params);
		HttpGet get = new HttpGet(strURL);
		HttpResponse httpResponse;
		String resultData = "";
		try {
			httpResponse = httpClient.execute(get);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				BufferedReader buffer = new BufferedReader(new InputStreamReader(entity.getContent()));
				String inputLine = "";
				while ((inputLine = buffer.readLine()) != null) {
					resultData += inputLine + "\n";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultData;
	}

	public static String httpActionClientPostInVpn(String strURL, List<BasicNameValuePair> nameList) {

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", new SvnHttpSocketFactory(), 80));
		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(params, 20000);
		HttpConnectionParams.setSoTimeout(params, 30000);

		ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(params, schemeRegistry) {
			/* overide this for dns parse */
			@Override
			protected ClientConnectionOperator createConnectionOperator(SchemeRegistry schreg) {
				return new SvnClientConnectionOperator(schreg);
			}
		};
		HttpClient httpClient = new DefaultHttpClient(clientConnectionManager, params);

//		start personal
//		httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8"); // 设置字符集
//		httpClient.setConnectionTimeout(5000);
//		httpClient.setTimeout(5000);
		HttpPost postMethod = new HttpPost(strURL);
//		postMethod.addRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");

		HttpResponse httpResponse;
		String resultData = "";
		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nameList, "utf-8"));
			
			httpResponse = httpClient.execute(postMethod);
			HttpEntity entity = httpResponse.getEntity();
			if (entity != null) {
				BufferedReader buffer = new BufferedReader(new InputStreamReader(entity.getContent()));
				String inputLine = "";
				while ((inputLine = buffer.readLine()) != null) {
					resultData += inputLine + "\n";
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultData;
	}
}
