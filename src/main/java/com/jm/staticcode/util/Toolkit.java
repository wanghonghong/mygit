package com.jm.staticcode.util;
import lombok.extern.log4j.Log4j;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
/**
 * <p>工具类</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/6/14
 */
@Log4j
public class Toolkit {

    private static final char UNDERLINE = '_';

    public static Integer parseObjForInt(Object obj) {
        Integer res;
        if (obj == null) {
            res = 0;
        } else {
            res = Integer.valueOf(String.valueOf(obj));
        }
        return res;
    }

    public static Integer obj2Int(Object obj) {
        Integer res;
        if (obj == null) {
            res = 0;
        } else {
            res = Integer.valueOf(String.valueOf(obj));
        }
        return res;
    }

    public static Long obj2Long(Object obj) {
        Long res;
        if (obj == null) {
            res = 0L;
        } else {
            res = Long.valueOf(String.valueOf(obj));
        }
        return res;
    }

    public static Double obj2Double(Object obj) {
        Double res;
        if (obj == null) {
            res = 0.0;
        } else {
            res = Double.valueOf(String.valueOf(obj));
        }
        return res;
    }

    public static String parseObjForStr(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }
    }

    public static String obj2Str(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return String.valueOf(obj);
        }
    }

    public static Boolean obj2Bool(Object obj) {
        Boolean res;
        if (obj == null) {
            res = false;
        } else {
            res = Boolean.valueOf(String.valueOf(obj));
        }
        return res;
    }

    /**
     * 解析map转换成key1=value1&key2=value2格式字符串
     * @param paramMap
     * @return
     */
    public static String mapToParam(Map paramMap) {
        String paramUri = "";
        if (paramMap != null && !paramMap.isEmpty()) {
            Iterator iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object values = entry.getValue();
                if(values instanceof String[]){
                    for (String value : (String[])values){
                        paramUri += "&" + entry.getKey() + "=" + value;
                    }
                } else {
                    paramUri += "&" + entry.getKey() + "=" + values;
                }
            }
            if (paramUri.length() > 0) {
                paramUri = paramUri.substring(1);
            }
        }
        return paramUri;
    }


    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    //下划线后字母大写
    public static String toUpperCaseSplitChar(String str) {
        int idx = str.indexOf("_");
        if(idx>0) {
            String sub1 = str.substring(0,idx);
            String sub2 = str.substring(idx + 1, str.length());
            StringBuilder sb = new StringBuilder().append(sub1).append(toUpperCaseFirstOne(sub2));
            String retStr = sb.toString();
            retStr = toUpperCaseSplitChar(retStr);
            return retStr;
        } else {
            return str;
        }
    }

    /**
     * 字段名称下划线去除，转驼峰格式，my_name->myName
     * @param map
     * @return
     */
    public static Map map2UpperMap(Map map) {
        Map resMap = new HashMap();
        if (map != null && !map.isEmpty()) {
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = String.valueOf(entry.getKey());
                if (key.indexOf("_") > 0) {
                    resMap.put(toUpperCaseSplitChar(key), entry.getValue());
                } else {
                    resMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return resMap;
    }

    /**
     * 读取properties属性配置文件
     * @param resourcePath
     * @return
     */
    public static Properties getProperties(String resourcePath) {
        InputStream is = null;
        Properties properties = new Properties();
        try {
            is = Toolkit.class.getClassLoader().getResourceAsStream(resourcePath);
            if(is != null) {
                properties.load(is);
            }
        } catch (Exception var1) {
            log.error("Read " + resourcePath+" error.", var1);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("IOException", e);
                }
            }
        }
        return properties;
    }

    /**
     * 首字母转大写
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 驼峰转下划线格式
     * @param param
     * @return
     */
    public static String camelToUnderline(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰格式
     * @param param
     * @return
     */
    public static String underlineToCamel(String param){
        if (param==null||"".equals(param.trim())){
            return "";
        }
        int len=param.length();
        StringBuilder sb=new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c=param.charAt(i);
            if (c==UNDERLINE){
                if (++i<len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线后字母大写，转驼峰格式
     * @param str
     * @return
     */
    public static String myStrUnderlineToCamel(String str) {
        int idx = str.indexOf("_");
        if(idx>0) {
            String sub1 = str.substring(0,idx);
            String sub2 = str.substring(idx + 1, str.length());
            StringBuilder sb = new StringBuilder().append(sub1).append(toUpperCaseFirstOne(sub2));
            String retStr = sb.toString();
            retStr = myStrUnderlineToCamel(retStr);
            return retStr;
        } else {
            return str;
        }
    }

    /**
     * map转url转参数  key={key}
     * @param url
     * @param paramMap
     * @return
     */
    public static String mapToUri(String url,Map paramMap) {
        String paramUri = "";
        if (url!=null && paramMap != null && !paramMap.isEmpty()) {
            Iterator iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object values = entry.getValue();
                Object key = entry.getKey();
                if(values instanceof String[]){
                    for (String value : (String[])values){
                        paramUri += "&" + key + "={" +key+"}";
                    }
                }else {
                    paramUri += "&" + key + "={" + key+"}";
                }
            }
            if (paramUri.length() > 0) {
                paramUri = paramUri.substring(1);
            }
            if(url.contains("?")){
                url += "&" + paramUri;
            }else{
                url += "?" + paramUri;
            }
        }
        return url;
    }

    /**
     * map转Object数组
     * @param paramMap
     * @return
     */
    public static Object[] mapToObjects(Map paramMap,long... ids) {
        List<Object> objs = new ArrayList<>();
        if(ids!=null){
            for(long id : ids){
                objs.add(id);
            }
        }
        if ( paramMap != null && !paramMap.isEmpty()) {
            Iterator iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                Object values = entry.getValue();
                if(values instanceof String[]){
                    for (String value : (String[])values){
                        objs.add(value);
                    }
                }else {
                    objs.add(values);
                }
            }
        }
        return objs.toArray(new Object[objs.size()]);
    }

    /**
     * 计算a/b的结果，并保留scale位小数
     *
     * @param a 被除数
     * @param b 除数
     * @param scale 保留的小数位
     * @return
     */
    public static double divide(int a, int b, int scale) {
        if (b == 0) {
            return 0d;
        }
        BigDecimal bd1 = new BigDecimal(String.valueOf(a));
        BigDecimal bd2 = new BigDecimal(String.valueOf(b));
        BigDecimal result = bd1.divide(bd2, scale, RoundingMode.HALF_UP);
        return result.doubleValue();
    }

    /**
     * 获取名称为key的cookie。没有则返回null
     *
     * @param request
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request, String key) {
        String value = null;
        if (request.getCookies() != null && StringUtils.isNotBlank(key)) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(key)) {
                    value = cookie.getValue();
                }
            }
        }
        return value;
    }

    /**
     * 从cookie中设置名称为key的cookie
     *
     * @param response
     * @param key
     * @return
     */
    public static void setCookie(HttpServletResponse response, String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(1*24*60*60);//保存1天
        //cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 从cookie中删除名称为key的cookie
     *
     * @param request
     * @param key
     * @return
     */
    public static void delCookie(HttpServletRequest request,HttpServletResponse response, String key) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(key)) {
                    Cookie cookieDel = new Cookie(key, null);
                    cookieDel.setMaxAge(0);
                    cookieDel.setPath("/");
                    cookieDel.setHttpOnly(true);
                    response.addCookie(cookieDel);
                }
            }
        }
    }

    public static String getRandom(int length) {
        Random random = new Random();
        String str = "";
        for (int i = 0; i < length; i++) {
            str +=random.nextInt(10);
        }
        return str;
    }

    public static String getOrderNum(String str){
        return str+System.currentTimeMillis()+ getRandom(4);
    }

    /**
     * 判断是不是微信浏览器
     * @param request
     * @return
     */
    public static boolean isWxBrowser(HttpServletRequest request){
        String agent = request.getHeader("User-Agent");
        if(agent!=null && agent.toLowerCase().indexOf("micromessenger")>=0){ //是微信
            return true;
        }
        return false;
    }

    /**
     * 是否是ajax请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request){
        String requestType = request.getHeader("X-Requested-With");
        if(requestType!=null){ //ajax请求
            return true;
        }
        return false;
    }

    public static void writeJson(HttpServletResponse response,String json) {
        PrintWriter writer = null;
        try {
            response.setContentType("application/json;charset=UTF-8");
            writer = response.getWriter();
            writer.print(json);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } finally {
            if(writer != null){
                writer.close();
            }
        }
    }

    /**
     * 字符串转时间
     * @param strDate
     * @return
     * @throws Exception
     */
    public static Date strToDate(String strDate){
    	return strToDate(strDate,"yyyyMMddHHmmss");
    }

    public static Date strToDate2(String strDate){
        return strToDate(strDate,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date strToDate(String str,String formatStr) {
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间戳字符串转换成日期
     * @param timestamp
     * @return date
     */
    public static Date timestampToDate(String timestamp) {
        if (timestamp!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = strToDate2(format.format((Long.valueOf(timestamp)*1000)));
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            return date;
        }else {
            return null;
        }
    }

    /**
     * 国际标准时间转中国时间
     * @param inteTime
     * @return date
     */
    public static Date inteTimeToDate(Date inteTime) {
        if (inteTime!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = strToDate2(format.format(inteTime));
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            return date;
        }else {
            return null;
        }
    }

    /**
     * Object转换成字符串
     * @param date
     * @return str
     */
    public static String dateToStr(Object date,String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        String str = null;
        if (null!=date&&!"".equals(date)){
            str = sdf.format(date);
        }
        return str;
    }

    private static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static void copyPropertiesIgnoreNull(Object src, Object target){
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 正则匹配
     * @param reg
     * @param str
     * @return
     */
	public static String matcher(String reg, String str) {
		if(StringUtils.isEmpty(str)){
			return "";
		}
		Pattern p = Pattern.compile(reg); 
		Matcher m = p.matcher(str);
		str = m.replaceAll(m.replaceAll("").trim());
		return str;
	}

    public static byte[] File2byte(String filePath)
    {
        byte[] buffer = null;
        try
        {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void byte2File(byte[] buf, String filePath, String fileName)
    {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try
        {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory())
            {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (bos != null)
            {
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 比较两个日期之间的大小
     *
     * @param d1
     * @param d2
     * @return 前者大于后者返回true 反之false
     */
    public static boolean compareDate(java.util.Date d1, java.util.Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);

        int result = c1.compareTo(c2);
        if (result >= 0)
            return true;
        else
            return false;
    }

    /**
     * <p>json 大写转换为下划线 </p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/6 21:30
     */
    public static String underscoreName(String camelCaseName) {
        StringBuilder result = new StringBuilder();
        if (camelCaseName != null && camelCaseName.length() > 0) {
            result.append(camelCaseName.substring(0, 1).toLowerCase());
            for (int i = 1; i < camelCaseName.length(); i++) {
                char ch = camelCaseName.charAt(i);
                if (Character.isUpperCase(ch)) {
                    result.append("_");
                    result.append(Character.toLowerCase(ch));
                } else {
                    result.append(ch);
                }
            }
        }
        return result.toString();
    }


    /***
     * 聚合数据
     * @param maps
     * @param mapKey 聚合的mapkey
     * @param objectType 需要聚合的数据
     * @param key 和mapKey 做比较
     * @param valKey 数据值的key
     * @param putKey 填进map中的 key
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<Map<String,Object>> converge(List<Map<String,Object>> maps,String mapKey,List<T> objectType,String key,String valKey,String putKey) throws IOException {
        if(maps==null){
            return null;
        }
        if( objectType == null && objectType.size()==0 ){
            return maps;
        }
        for ( Map<String,Object> map:maps ) {
                 String obj =  Toolkit.parseObjForStr(map.get(mapKey));
                if (obj!=null){
                    for (T s:objectType) {
                        JSONObject myJsonObject = new JSONObject(s);
                        String objval = Toolkit.parseObjForStr(myJsonObject.get(key));
                        if(objval.equals(obj)){
                            try {
                                map.put(exChange(putKey),myJsonObject.get(valKey));
                            }catch (JSONException c){
                            }
                        }
                    }
                }
       }
        return maps;
    }

    public static <T> List<Map<String,Object>> converge(List<Map<String,Object>> maps,String mapKey,List<T> objectType,String key,String[] valKey,String[] putKey) throws IOException {
        if(maps==null){
            return null;
        }
        if( objectType == null && objectType.size()==0 ){
            return maps;
        }
        for ( Map<String,Object> map:maps ) {
            Object obj =  map.get(mapKey);
            if (obj!=null){
                for (T s:objectType) {
                    JSONObject myJsonObject = new JSONObject(s);
                    if(myJsonObject.get(key).equals(obj)){
                        for (int i=0;i<valKey.length;i++){
                            try {
                                    map.put(exChange(putKey[i]),myJsonObject.get(valKey[i]));
                            }catch (JSONException c){
                               // c.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return maps;
    }


    public static <T> List<Map<String,Object>> convergeListMap(List<Map<String,Object>> maps,String mapKey,List<Map<String,Object>> objectType,String key,String[] valKey,String[] putKey) throws IOException {
        if(maps==null){
            return null;
        }
        if( objectType == null && objectType.size()==0 ){
            return maps;
        }
        for ( Map<String,Object> map:maps ) {
            Object obj =  map.get(mapKey);
            if (obj!=null){
                for (Map<String,Object> s:objectType) {
                    if( s.get(key).equals(obj)){
                        for (int i=0;i<valKey.length;i++){
                            map.put(exChange(putKey[i]),s.get(valKey[i]));
                        }
                    }
                }
            }
        }
        return maps;
    }


    private static String exChange(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append("_"+Character.toLowerCase(c));
                }else if(Character.isLowerCase(c)){
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

}
