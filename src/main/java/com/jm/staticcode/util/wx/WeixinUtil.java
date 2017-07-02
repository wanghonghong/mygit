package com.jm.staticcode.util.wx;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.jm.staticcode.util.Pic;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.jm.mvc.vo.wx.WxMedia;
import com.jm.staticcode.constant.WxUrl;
import com.jm.staticcode.util.JsonMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import lombok.extern.log4j.Log4j;

@Log4j
public class WeixinUtil {

	/**
     * 发起https请求并获取结果
     * 
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
    	String jsonStr = "";
        StringBuffer buffer = new StringBuffer();
        try {

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            if (buffer.indexOf("<") == 0) {
            	jsonStr = JsonMapper.toJson(converXml2Json(buffer.toString()));
            } else {
            	jsonStr = JsonMapper.toJson(buffer.toString());
            }

        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonStr;
    }
    
    

    /**
     * 将XML格式的字符串转成Json对象，只支持单层转换
     * 
     * @param xmlDocumnet
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String,Object> converXml2Json(String xmlDocumnet) throws Exception {
        if(xmlDocumnet==null || "".equals(xmlDocumnet)){
            new Exception("xmlDocumnet is null");
            log.error("======xmlDocumnet is null=======");
            return null;
        }
    	Map<String,Object> map = new HashMap<String, Object>();
        SAXReader reader = new SAXReader();
        Document document;
        final InputStream in = new ByteArrayInputStream(xmlDocumnet.getBytes("UTF-8"));
        document = reader.read(in);
        Element root = document.getRootElement();
        Iterator<Element> iter = root.elements().iterator();
        while (iter.hasNext()) {
            Element current = iter.next();
           
            map.put(current.getName(), current.getText());
        }
        return map;

    }
    
    public static Element XmlToElement(String xmlDocumnet) throws Exception{
        SAXReader reader = new SAXReader();
        Document document;
        final InputStream in = new ByteArrayInputStream(xmlDocumnet.getBytes("UTF-8"));
        document = reader.read(in);
        Element root = document.getRootElement();
        return root;
    }
    
    
    /** 
     * 使用递归调用将多层级xml转为map 支持多层转换
     * @param map 
     * @param rootElement 
     */  
    public static void element2Map(Map<String, Object> map, Element rootElement) {  
          //获得当前节点的子节点  
          List<Element> elements = rootElement.elements();  
          if(elements.size()==0){
        	  map.put(rootElement.getName(),rootElement.getText());
        	 
          }else{
        	  for(Element element : elements){
        		  List<Element> childs = element.elements();
        		  if(childs.size()>0){
        			  Map<String, Object> child = new HashMap<String, Object>();
            		  element2Map(child,element);
            		  map.put(element.getName(), child);
        		  }else if(childs.size()==0){
        			  element2Map(map,element);
        		  }
        		  
        	  }
          }
    }  
    
    /**
	 *<p>保存网络图片到本地</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @throws IOException 
	 * @data 2016年6月14日
	 */
	public String savePic(String filePath) throws IOException{
		log.info("---------------savePic,picUrlIs："+URLDecoder.decode(filePath)+"------------------------");
		FileOutputStream fos = null;  
		 BufferedInputStream bis = null;  
		 HttpURLConnection httpUrl = null;  
		 String fileName = null;
		 URL url = null;  
		 int BUFFER_SIZE = 1024;  
		 byte[] buf = new byte[BUFFER_SIZE];  
		 int size = 0;  
		 try {  
		 url = new URL(URLDecoder.decode(filePath));  
		 httpUrl = (HttpURLConnection) url.openConnection();  
		 httpUrl.connect();  
		 bis = new BufferedInputStream(httpUrl.getInputStream()); 
		 DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		 fileName = format.format(new Date())+UUID.randomUUID()+".jpg";
		 String sss  = this.getClass().getResource("/temp/image").getPath();//request.getSession().getServletContext().getRealPath("/");
		 
		 fos = new FileOutputStream(sss+"/"+fileName);
		 while ((size = bis.read(buf)) != -1) {  
		 fos.write(buf, 0, size);  
		 }  
		 fos.flush();  
		 } catch (IOException e) {  
		 } catch (ClassCastException e) {  
		 } finally {  
			 fos.close();  
			 bis.close();  
			 httpUrl.disconnect();  
		 }
		 return fileName;//返回图片名称
	}
	
	/**
	 *<p>上传图片到微信素材库</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @throws IOException 
	 * @data 2016年6月14日
	 */
	public String upMedia(String type,String accessToken,String filePath) throws IOException{
		//没有任何逻辑用处，完全打印使用
		if(null==filePath || "".equals(filePath)){
			log.info("----------------filePath is null..die Monkey....--------------");
		}
		String action = WxUrl.UP_METID.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
		BufferedReader reader = null;
		String result = null;
		String fileName = savePic(filePath);//获取保存的文件名称
		try {
			URL url = new URL(action);
			 String sss  = this.getClass().getResource("/temp/image").getPath();//request.getSession().getServletContext().getRealPath("/");
		    File file = new File(sss+"/"+fileName);
		    if (!file.exists() || !file.isFile()) {
		     throw new IOException("上传的文件不存在");
		     }
		    HttpURLConnection con = (HttpURLConnection) url.openConnection();
		       con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
		       con.setDoInput(true);
		       con.setDoOutput(true);
		       con.setUseCaches(false); // post方式不能使用缓存
		       
		    // 设置请求头信息
		       con.setRequestProperty("Connection", "Keep-Alive");
		       con.setRequestProperty("Charset", "UTF-8");

		       // 设置边界

		       String BOUNDARY = "----------" + System.currentTimeMillis();
		       con.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);

		       // 请求正文信息
		       // 第一部分：
		       StringBuilder sb = new StringBuilder();
		       sb.append("--"); // 必须多两道线
		       sb.append(BOUNDARY);
		       sb.append("\r\n");
		       sb.append("Content-Disposition: form-data;name=\"file\";filename=\""+ file.getName() + "\"\r\n");
		       sb.append("Content-Type:application/octet-stream\r\n\r\n");
		       byte[] head = sb.toString().getBytes("utf-8");

		       // 获得输出流
		       OutputStream out = new DataOutputStream(con.getOutputStream());

		       // 输出表头
		       out.write(head);
		       // 文件正文部分
		       // 把文件已流文件的方式 推入到url中
		       DataInputStream in = new DataInputStream(new FileInputStream(file));
		       int bytes = 0;
		       byte[] bufferOut = new byte[1024];
		       while ((bytes = in.read(bufferOut)) != -1) {
		    	   out.write(bufferOut, 0, bytes);
		       }
		       in.close();
		       
		    // 结尾部分
		       byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
		       out.write(foot);
		       out.flush();
		       out.close();
		       StringBuffer buffer = new StringBuffer();

		       // 定义BufferedReader输入流来读取URL的响应
	           reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
	           String line = null;
	           while ((line = reader.readLine()) != null) {
	               buffer.append(line);
	           }
	           if (result == null) {
	               result = buffer.toString();
	           }
		    
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
	           if (reader != null) {
	               reader.close();
	           }
	       }
		WxMedia media = JsonMapper.parse(result, WxMedia.class);
		return media.getMediaId();
	}
	
	/**
	 *<p>将对象转为xml</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年7月5日
	 */
	public static String objToXml(Object obj){
		  xstream.alias("xml", obj.getClass()); 
		return 	xstream.toXML(obj);
		
	}
	
	
    /** 
     * 扩展xstream，使其支持CDATA块 
     *  
     */  
    private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // 对所有xml节点的转换都增加CDATA标记  
                boolean cdata = true;  
  
                @SuppressWarnings("unchecked")  
                public void startNode(String name, Class clazz) {  
                    super.startNode(name, clazz);
//                	super.startNode(name);  
                }  
  
                protected void writeText(QuickWriter writer, String text) {  
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {  
                        writer.write(text);  
                    }  
                }  
            };  
        }  
    });


    /**
     * 模拟form表单的形式 ，上传文件 以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
     * @param pathUrL
     *            请求地址 form表单url地址
     * @param filePath
     *            文件在服务器保存路径
     * @return String url的响应信息返回值
     * @throws IOException
     */
    public static String sendToWeixin(String accessToken,String pathUrL, String filePath) throws IOException {
        String result = null;
        String sss = filePath.substring(filePath.lastIndexOf("/")+1);
        String fileName = UUID.randomUUID().toString();//用时间撮加随机数
        if(filePath==null || filePath==""){
           throw new IOException("文件不存在");
        }else{
        	if(sss.indexOf(".")<0){//没有有后缀
        		fileName+=".gif";//没有后缀默认加上gif
        	}else{
        		fileName+=sss.substring(sss.lastIndexOf("."));
        	}
        }

        /**
         * 第一部分
         */
        String action = pathUrL.replace("ACCESS_TOKEN", accessToken);
        URL url = new URL(action);
        // 连接
        //HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
        Pic pic = new Pic();
        URLConnection urlConnect = pic.loadImageLinkUrl(URLDecoder.decode(filePath, "UTF-8"));
        InputStream  inputStream = urlConnect.getInputStream(); //图片流

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        /**
         * 设置关键值
         */
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存
        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        // 设置边界
        String BOUNDARY = "---------------------------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
                + BOUNDARY);
        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"media\";filename=\""
                + fileName + "\"\r\n");
        System.out.println(fileName);
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");
        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());
        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = inputStream.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        inputStream.close();
        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);

        out.flush();
        out.close();

        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
            throw new IOException("数据读取异常");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }

    /**
     * 获取media id
     * @Param file 文件
     * */
    public String getMediaId(File file,String accessToken,String type) throws IOException {
        BufferedReader reader = null;
        String result = null;

        String action = WxUrl.UP_METID.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
        URL url = new URL(action);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存

        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        // 设置边界

        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");

        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());

        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();

        // 定义BufferedReader输入流来读取URL的响应
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        if (result == null) {
            result = buffer.toString();
        }
        WxMedia media = JsonMapper.parse(result, WxMedia.class);
        return media.getMediaId();
    }

    public String getMediaId(InputStream input,String accessToken,String type,String fileName) throws IOException {
        BufferedReader reader = null;
        String result = null;

        String action = WxUrl.UP_METID.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
        URL url = new URL(action);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false); // post方式不能使用缓存

        // 设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        // 设置边界

        String BOUNDARY = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

        // 请求正文信息
        // 第一部分：
        StringBuilder sb = new StringBuilder();
        sb.append("--"); // 必须多两道线
        sb.append(BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"\r\n");
        sb.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = sb.toString().getBytes("utf-8");

        // 获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());

        // 输出表头
        out.write(head);
        // 文件正文部分
        // 把文件已流文件的方式 推入到url中
        DataInputStream in = new DataInputStream(input);
        int bytes = 0;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        // 结尾部分
        byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
        out.write(foot);
        out.flush();
        out.close();
        StringBuffer buffer = new StringBuffer();

        // 定义BufferedReader输入流来读取URL的响应
        reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line = null;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        if (result == null) {
            result = buffer.toString();
        }
        WxMedia media = JsonMapper.parse(result, WxMedia.class);
        return media.getMediaId();
    }
}
