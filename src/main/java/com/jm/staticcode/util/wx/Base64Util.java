package com.jm.staticcode.util.wx;

import java.io.UnsupportedEncodingException;

import org.springframework.util.Base64Utils;

import sun.misc.BASE64Decoder;

public class Base64Util {
	
	/**
	 *<p>加密</p>
	 * @author chenyy
	 * @version latest
	 * @data 2016年6月15日
	 */
	public static String enCoding(String str){
		
		byte[] b = null;  
        try {
        	 b = str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			 e.printStackTrace();
		}
		return Base64Utils.encodeToString(b);
	}
	
	

	// 解密  
    public static String getFromBase64(String s) {  
        byte[] b = null;  
        String result = null;  
        if (s != null) {  
            BASE64Decoder decoder = new BASE64Decoder();  
            try {  
                b = decoder.decodeBuffer(s);  
                result = new String(b, "utf-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return result;  
    }  
}
