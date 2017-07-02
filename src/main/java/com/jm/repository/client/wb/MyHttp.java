package com.jm.repository.client.wb;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * android客户端HTTP方式连接服务器端工具<BR>
 * 此类提供两种方式连接<BR>
 * <li>1.HttpURLConnection方式连接<BR> <li>2.HttpClient方式连接<BR>
 * 
 * @author ZW
 * 
 */
public class MyHttp {

	// //////////////////////////////////////////////////////////////////////////////////
	// ////////////////////////HttpURLConnection方式连接////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////

	private static final int CONNECT_TIMEOUT = 8 * 1000; // 设置连接超时间为8秒
	private static String mSessionId = null;	//请求SessionId
	public final static String POST_PARAM_NAME="params";	//post参数的key值

	/**
	 * GET方式连接服务，获取数据<BR>
	 * 使用的类：HttpURLConnection
	 * 
	 * @param url
	 *            URL地址
	 * @return 服务器返回的数据
	 * @throws IOException
	 */
	public static String doGet(String url) throws IOException {
		return getDataByConn(url, null, false);
	}

	/**
	 * POST方式连接服务，获取数据<BR>
	 * 使用的类：HttpURLConnection
	 * 
	 * @param url
	 *            URL地址
	 * @param param
	 *            post参数集合
	 * @return 服务器返回的数据
	 * @throws IOException
	 */
	public static String doPost(String url, String param) throws IOException {
		return getDataByConn(url, param, true);
	}

	/**
	 * HTTP方式连接服务获取数据，支持get和post<BR>
	 * 使用的类：HttpURLConnection
	 * @param url URL地址
	 * @param param post参数集合
	 * @param isPost 是否为post方式 true是post false是get
	 * @return 服务器返回的数据
	 * @throws IOException
	 */
	private static String getDataByConn(String url, String param, boolean isPost)
			throws IOException {
		// 1.参数检查
		if (url == null || url.trim().equals("")) {
			return "url can not be null";
		}

		// 2.变量定义
		String result = "";
		String line = null;
		InputStream is = null; // 输入流
		OutputStream os = null; // 输出流

		// 3.连接服务器的设置
		URL myURL = new URL(url); // 创建URL对象
		HttpURLConnection httpConn = (HttpURLConnection) myURL.openConnection(); // 创建连接对象
		httpConn.setDoOutput(true); // 设置可以输出
		httpConn.setDoInput(true); // 设置可以输入
		httpConn.setUseCaches(false); // 设置不使用缓存
		httpConn.setConnectTimeout(CONNECT_TIMEOUT); // 设置连接超时间
		httpConn.setRequestMethod(isPost ? "POST" : "GET"); // 设置提交方式为
		httpConn.setInstanceFollowRedirects(false);
		if(mSessionId!=null && mSessionId.trim().length()>0){
			//将上次的session存于request中，用于验证用户是否登录
			httpConn.setRequestProperty("cookie",mSessionId);
		}

		// 4.连接服务器，post或get
		if (isPost) {
			try {
				os = httpConn.getOutputStream();
				PrintWriter writer = new PrintWriter(os);
				writer.print(POST_PARAM_NAME+"="+param);
				writer.flush();
			} finally {
				if (os != null) {
					os.close(); // 关闭输出流
				}
			}
		} else {
			httpConn.connect(); // 建立实际连接
		}
		
		// 5.读取服务器返回的数据，并释放资源
		String cookieval = httpConn.getHeaderField("set-cookie");
		if(cookieval != null) {
			//请求成功后，保存SessionId
			mSessionId = cookieval.substring(0, cookieval.indexOf(";"));
		}
		
		/*int code = httpConn.getResponseCode();
		String msg = httpConn.getResponseMessage();
		if(code == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
			//连接超时
		}else if(code == HttpURLConnection.HTTP_NOT_FOUND){
			//地址找不到
		}else if(code == HttpURLConnection.HTTP_OK){
			//连接成功
		}*/
		
		try {
			is = httpConn.getInputStream(); // 获取输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(is)); // 创建读取数据对象
			while ((line = reader.readLine()) != null) {
				result += line; // 每次读取一行数据
			}
			
		} finally {
			if (is != null) {
				is.close(); // 关闭输入流
			}
		}
		return result;
	}
	
	
	
	
	/**
	 * HTTP方式连接服务获取数据，支持get和post<BR>
	 * 使用的类：HttpURLConnection
	 * @param url URL地址
	 * @param param post参数集合
	 * @param file 上传文件
	 * @return 服务器返回的数据
	 * @throws IOException
	 */
	public static String doPostWithFile(String url, String param, File file) throws IOException {
		// 1.参数检查
		if (url == null || url.trim().equals("")) {
			return "url can not be null";
		}
		String end ="\r\n";
		String twoHyphens ="--";
		String boundary ="*****";

		// 2.变量定义
		String result = "";
		String line = null;
		InputStream is = null; // 输入流
		OutputStream os = null; // 输出流

		// 3.连接服务器的设置
		URL myURL = new URL(url); // 创建URL对象
		HttpURLConnection httpConn = (HttpURLConnection) myURL.openConnection(); // 创建连接对象
		httpConn.setDoOutput(true); // 设置可以输出
		httpConn.setDoInput(true); // 设置可以输入
		httpConn.setUseCaches(false); // 设置不使用缓存
		httpConn.setConnectTimeout(CONNECT_TIMEOUT); // 设置连接超时间
		httpConn.setRequestMethod("POST"); // 设置提交方式为POST
		httpConn.setRequestProperty("Connection", "Keep-Alive");
		httpConn.setRequestProperty("Charset", "UTF-8");
		httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
		
		
		httpConn.setInstanceFollowRedirects(false);
		if(mSessionId!=null && mSessionId.trim().length()>0){
			//将上次的session存于request中，用于验证用户是否登录
			httpConn.setRequestProperty("cookie",mSessionId);
		}

		// 4.连接服务器post
		os = httpConn.getOutputStream();
		try {
			//上传文件
			DataOutputStream ds = new DataOutputStream(os);
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "+"name=\"file1\";filename=\""+ file.getName() +"\""+ end);
			ds.writeBytes(end);
			FileInputStream fStream =new FileInputStream(file);
			int bufferSize =1024;
			byte[] buffer =new byte[bufferSize];
			int length =-1;
			while((length = fStream.read(buffer)) !=-1){
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			
			//上传parmas
			String datas = end
                    + "--" + boundary + end
                    + "Content-Type: text/plain" + end
                    + "Content-Disposition: form-data; name=\""+POST_PARAM_NAME+"\"" + end + end
                    + param + end
                    + "--" + boundary + "--";
			
			ds.writeBytes("Content-Disposition: form-data; "+"name=\"file1\";filename=\""+ file.getName() +"\""+ end);
			ds.writeBytes(datas);
			
			fStream.close();
			ds.flush();
			ds.close();
			
		} finally {
			if (os != null) {
				os.close(); // 关闭输出流
			}
		}
		
		// 5.读取服务器返回的数据，并释放资源
		String cookieval = httpConn.getHeaderField("set-cookie");
		if(cookieval != null) {
			//请求成功后，保存SessionId
			mSessionId = cookieval.substring(0, cookieval.indexOf(";"));
		}
		
		try {
			is = httpConn.getInputStream(); // 获取输入流
			BufferedReader reader = new BufferedReader(new InputStreamReader(is)); // 创建读取数据对象
			while ((line = reader.readLine()) != null) {
				result += line; // 每次读取一行数据
			}
		} finally {
			if (is != null) {
				is.close(); // 关闭输入流
			}
		}
		return result;
	}
	
	
	
	public static String doPostWithFile2(String actionUrl, String param, File file){
		
		HttpURLConnection conn = null;
		DataOutputStream outStream = null;
		try{
           String BOUNDARY = "---------------------------7da2137580612";    //数据分界线
           
           URL url = new URL(actionUrl);
           
               //上传表单的文件
               StringBuilder emailSB = new StringBuilder();
               emailSB.append("--");
               emailSB.append(BOUNDARY);
               emailSB.append("\r\n");
               emailSB.append("Content-Disposition:form-data;name=\""+POST_PARAM_NAME+"\"\r\n\r\n");
               emailSB.append(param);
               emailSB.append("\r\n");
               
               StringBuilder bitmapSB = new StringBuilder();
               bitmapSB.append("--");
               bitmapSB.append(BOUNDARY);
               bitmapSB.append("\r\n");
               bitmapSB.append("Content-Disposition:form-data;name=\"image\";filename=\"image\"\r\n");
               bitmapSB.append("Content-Type:image/png\r\n\r\n");
               
               byte[] end_data =("--"+BOUNDARY+"--\r\n").getBytes();//数据结束标志
               
               
               long contentLenght = emailSB.toString().getBytes().length +  end_data.length + bitmapSB.toString().getBytes().length + file.length() + "\r\n".getBytes().length;
               
               conn = (HttpURLConnection)url.openConnection();
               conn.setDoInput(true);        //允许输入
               conn.setDoOutput(true);        //允许输出
               conn.setUseCaches(false);    //不使用caches
               conn.setRequestMethod("POST");
               conn.setRequestProperty("Connection","Keep-Alive");
               conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+BOUNDARY);
               conn.setRequestProperty("Content-Length",Long.toString(contentLenght));
               
               
               outStream = new DataOutputStream(conn.getOutputStream());  
               outStream.write(emailSB.toString().getBytes());
               outStream.write(bitmapSB.toString().getBytes());
               byte[] buffer = new byte[1024];
               int len = 0;
               FileInputStream fileIS = new FileInputStream(file);
               while((len = fileIS.read(buffer)) != -1){
            	   outStream.write(buffer, 0, len);
               }
               outStream.write("\r\n".getBytes());
               outStream.write(end_data);            
               outStream.flush();
               int cah = conn.getResponseCode();
               if(cah!=200){
				System.out.println("上传失败");
				return "上传失败";
               }
               InputStream is = conn.getInputStream();
               int ch;
               StringBuilder result = new StringBuilder();
               while((ch=is.read())!=-1){
            	   result.append((char)ch);
               }
               System.out.println("result :" + result.toString());
           } catch (IOException e) {
        	   e.printStackTrace();
           }finally{
	         try {
		         if(outStream != null){
		        	 outStream.close();
		         }
	         } catch (IOException e) {
	        	 e.printStackTrace();
	         }
		         if(conn != null){
			         conn.disconnect();
			         conn = null;
		         }
		   }
		return "上传成功";
		
	}
	
	
	public static String doPostWithFile3(String actionUrl, String param, File uploadFile){
		String end ="\r\n";
		String twoHyphens ="--";
		String boundary ="*****";
		try{
			URL url =new URL(actionUrl);
			HttpURLConnection con=(HttpURLConnection)url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "+"name=\"file1\";filename=\""+ uploadFile.getName() +"\""+ end);
			ds.writeBytes(end);
			FileInputStream fStream =new FileInputStream(uploadFile);
			int bufferSize =1024;
			byte[] buffer =new byte[bufferSize];
			int length =-1;
			while((length = fStream.read(buffer)) !=-1){
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			fStream.close();
			ds.flush();
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) !=-1 ){
				b.append( (char)ch );
			}
			ds.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	

	// //////////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////HttpClient方式连接////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////

	private static final int CLIENT_CONNECT_TIMEOUT = 8 * 1000;
	private static final int CLIENT_SO_TIMEOUT = 10 * 1000;

	/**
	 * POST方式连接服务，获取数据<BR>
	 * 使用的类：HttpClient
	 * 
	 * @param url
	 *            URL地址
	 * @return 服务器返回的数据
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendGet(String url) throws ClientProtocolException,
			IOException {
		return getDataByClient(url, null, false);
	}

	/**
	 * POST方式连接服务，获取数据<BR>
	 * 使用的类：HttpClient
	 * 
	 * @param url
	 *            URL地址
	 * @param param
	 *            post参数集合
	 * @return 服务器返回的数据
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String sendPost(String url, String param)
			throws ClientProtocolException, IOException {
		return getDataByClient(url, param, true);
	}

	/**
	 * HTTP方式连接服务获取数据，支持get和post<BR>
	 * 使用的类：HttpClient
	 * 
	 * @param url
	 *            URL地址
	 * @param param
	 *            post参数集合
	 * @param isPost
	 *            是否为post方式 true是post false是get
	 * @return 服务器返回的数据
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private static String getDataByClient(String url, String param,
			boolean isPost) throws ClientProtocolException, IOException {
		// 1.参数检查
		if (url == null || url.trim().equals("")) {
			return "url can not be null";
		}

		// 2.变量定义
		String result = "";
		String line = null;
		InputStream is = null; // 输入流

		// 3.创建HttpClient对象
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				CLIENT_CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, CLIENT_SO_TIMEOUT);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		// httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"gbk");
		httpClient.getParams().setParameter("charset", "gbk");

		// 4.连接服务器端
		HttpResponse response = null;
		if (isPost) {
			HttpPost request = new HttpPost(url);
			request.setEntity(new StringEntity(param));
			response = httpClient.execute(request);
		} else {
			HttpGet request = new HttpGet(url);
			response = httpClient.execute(request);
		}

		// 5.读取服务器返回的数据，并释放资源
		HttpEntity resEntity = response.getEntity();
		try {
			is = resEntity.getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is)); // 创建读取数据对象
			while ((line = reader.readLine()) != null) {
				result += line; // 每次读取一行数据
			}
		} finally {
			if (is != null) {
				is.close(); // 关闭输入流
			}
		}
		return result;
	}
	
	
	/**
	 * 下载文件操作,并将下载文件保存在SD卡.<br>
	 * @param url 下载地址
	 * @param path 保存文件
	 * @return 是否成功
	 * @author ZW
	 */
	public static boolean downLoadFile(String url, File file) {
		boolean res = false;
		InputStream is = null;
		FileOutputStream fileOutputStream = null;
		try {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,CLIENT_CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, CLIENT_SO_TIMEOUT);
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpGet get = new HttpGet(url);
			HttpResponse response;
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			if (is != null) {
				if (file.exists()) {
					file.delete();
				}
				file.createNewFile();
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
				}
			}
			res = true;
		} catch (Exception e) {
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (fileOutputStream != null)
				try {
					fileOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return res;
	}

}
