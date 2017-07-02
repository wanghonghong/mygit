package com.jm.staticcode.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 *<p>基础工具类</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年5月9日
 */
public class BaseUtil {
	
	/**
	 * 
	 *<p>获取工程路径</p>
	 *
	 * @author chenyy
	 * @version latest
	 * @data 2016年5月9日
	 */
	public static String getPath(HttpServletRequest request){
		//String path= request.getScheme()+"://" + request.getServerName() + (request.getServerPort()==80?"":(":" + request.getServerPort()))+ request.getContextPath() ;
		return request.getContextPath();
	}
	
	public static String getBasePath(HttpServletRequest request){
		String path= request.getScheme()+"://" + request.getServerName() + (request.getServerPort()==80?"":(":" + request.getServerPort()))+ request.getContextPath() ;
		return path;
	}
	
	
	
	
	//驼峰转下划线
	public static String trans(String str){
        
        List record =new ArrayList();
        for(int i=0;i<str.length();i++)
        {
            char tmp =str.charAt(i);
                
            if((tmp<='Z')&&(tmp>='A'))
            {
                record.add(i);//记录每个大写字母的位置
            }
             
        }
        record.remove(0);//第一个不需加下划线
         
        str= str.toLowerCase();
        char[] charofstr = str.toCharArray();
        String[] t =new String[record.size()];
        for(int i=0;i<record.size();i++)
        {
            t[i]="_"+charofstr[(int)record.get(i)];//加“_”
        }
        String result ="";
        int flag=0;
        for(int i=0;i<str.length();i++)
        {
            if((flag<record.size())&&(i==(int)record.get(flag))){
                result+=t[flag];
                flag++;
            }
            else
                result+=charofstr[i];
        }
         
        return result;
    }

}
