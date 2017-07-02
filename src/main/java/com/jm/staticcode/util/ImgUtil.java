package com.jm.staticcode.util;

import com.jm.staticcode.constant.Constant;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import java.util.Arrays;

@Log4j
public class ImgUtil {

	private static String[] imageFormat = new String[]{"jpg","gif","png"};

	/**
	 * 获取网络URL图片后缀没有后缀用gif
	 * @param url
	 * @return
	 */
	public static String getSuffix(String url){
		String suffix = "";
		if (url.lastIndexOf("?")>0){
			url = url.substring(0,url.lastIndexOf("?"));
		}
		if (url.lastIndexOf("/")>0){
			url = url.substring(url.lastIndexOf("/")+1);
		}
		if (url.lastIndexOf(".")>0){
			suffix = url.substring(url.lastIndexOf(".")+1);
		}
		return suffix;
	}

	/**
	 * 获取文件名
	 * @param url
	 * @return
	 */
	public static String getFileName(String url){
		String fileName = "";
		if (url.lastIndexOf("?")>0){
			url = url.substring(0,url.lastIndexOf("?"));
		}
		if (url.lastIndexOf("/")>0){
			fileName = url.substring(url.lastIndexOf("/")+1);
		}
		return fileName;
	}


	/**
	 * 获取网络URL图片后缀没有后缀用gif
	 * @param resUrl
	 * @return
	 */
	public static String getSuffix(String resUrl,String suffix){
		String wz = getSuffix(resUrl);
		if (StringUtils.isEmpty(wz)){
			return suffix;
		}
		if (!Arrays.asList(imageFormat).contains(wz)){
			wz = suffix;
		}
		return wz;
	}

	/**
	 * 获取压缩图片
	 * @param w
	 * @param q
	 * @return
	 */
	public static String compressUrl(String url,int w,int q,String suffix){
		if (StringUtils.isEmpty(suffix)){
			suffix = "gif";
		}
		if (!Arrays.asList(imageFormat).contains(suffix)){
			suffix = "gif";
		}
		return url+"?imageView2/2/w/"+w+"/q/"+q+"/format/"+suffix;
	}

	/**
	 * 压缩100
	 * @param url
	 * @return
	 */
	public static String compress100(String url){
		return compressUrl(url,100,75,getSuffix(url));
	}

	public static String compress240(String url){
		return compressUrl(url,240,75,getSuffix(url));
	}

	public static String compress720(String url){
		return compressUrl(url,720,75,getSuffix(url));
	}

	public static String compress(String url,int w,String suffix){
		return compressUrl(url,w,75,suffix);
	}


	/**
	 * 单图拼接前缀并压缩
	 * @param url
	 * @param w
	 * @return
	 */
	public static String appendUrl(String url,int w){
		if (StringUtils.isEmpty(url)){
			return "";
		}
		String resUrl = "";
		if(url.contains("/wx.qlogo.cn/")){
			return url;
		}if (url.contains("/image/")){
			resUrl = Constant.COS_PATH + url;
		}else if (url.contains("/voice/")){
			resUrl = Constant.COS_PATH + url;
		}else if(url.contains("qrcode/")){
			resUrl = Constant.COS_PATH +"/"+ url;
		}else{
			if (w==0){
				resUrl = Constant.IMAGE_URL+url;
			}else {
				resUrl = compress(Constant.IMAGE_URL+url,w,getSuffix(url));
			}
		}
		return resUrl;
	}

	/**
	 * 单图拼接前缀
	 * @param url
	 * @return
	 */
	public static String appendUrl(String url){
		return appendUrl(url,0);
	}

	/**
	 * 多图拼接前缀
	 * @param resUrl
	 * @return
	 */
	public static String appendUrls(String resUrl){
		if (StringUtils.isEmpty(resUrl)){
			return "";
		}
		String resUrls = "";
		String[] urls = resUrl.split(",");
		for (String url : urls) {
			resUrls += appendUrl(url,0)+",";
		}
		int n = resUrls.lastIndexOf(",");
		if(0<n){
			resUrls = resUrls.substring(0,n);
		}
		return resUrls;
	}

	/**
	 * 多图拼接前缀并压缩
	 * @param resUrl
	 * @param w
	 * @return
	 */
	public static String appendUrls(String resUrl,int w){
		if (StringUtils.isEmpty(resUrl)){
			return "";
		}
		String resUrls = "";
		String[] urls = resUrl.split(",");
		for (String url : urls) {
			resUrls += appendUrl(url,w)+",";
		}
		int n = resUrls.lastIndexOf(",");
		if(0<n){
			resUrls = resUrls.substring(0,n);
		}
		return resUrls;
	}

	/**
	 * 单图截取前缀
	 * @param url
	 * @return
	 */
	public static String substringUrl(String url){
		if (StringUtils.isEmpty(url)){
			return "";
		}
		if(url.contains("http://product-10046040.image.myqcloud.com/")){ //万象优图正式库
			url = url.replaceAll("http://product-10046040.image.myqcloud.com/","");
		}else if(url.contains("http://product-1253295418.file.myqcloud.com/")){ //COS 正式库
			url = url.replaceAll("http://product-1253295418.file.myqcloud.com/","");
		}else if(url.contains("http://test-1253295418.cossh.myqcloud.com/")){ //COS 测试库
			url = url.replaceAll("http://test-1253295418.cossh.myqcloud.com/","");
		}else if(url.contains("http://test-1253295418.file.myqcloud.com/")){ //COS 测试库
			url = url.replaceAll("http://test-1253295418.file.myqcloud.com/","");
		}else if(url.contains(Constant.IMAGE_URL)){
			url = url.replaceAll(Constant.IMAGE_URL,"");
		}else if (url.contains("/image/")){
			url = url.replaceAll(Constant.COS_PATH,"");
		}

		if (url.indexOf("?")>0){
			url = url.substring(0,url.indexOf("?"));
		}
		url = url.replaceAll(Constant.RE_IMAGE_URL,"");

		return url;
	}

	/**
	 * 多图截取前缀
	 * @param resUrl
	 * @return
	 */
	public static String substringUrls(String resUrl){
		if (StringUtils.isEmpty(resUrl)){
			return "";
		}
		String resUrls = "";
		String[] urls = resUrl.split(",");
		for (String url : urls) {
			resUrls += substringUrl(url)+",";
		}
		int n = resUrls.lastIndexOf(",");
		if(0<n){
			resUrls = resUrls.substring(0,n);
		}
		return resUrls;
	}

}
