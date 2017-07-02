package com.jm.staticcode.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Created by cj on 2016/7/20 16:38
 */
public class StringUtil {

	public static String[] toStringArray(String arraySTR,String mode){
		String[] result = null;
		if(arraySTR != null && mode != null){
			arraySTR = arraySTR.trim();
		    result = arraySTR.split(mode);
		}
		return result;
	} 
	
	public static boolean isNull(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	public static java.lang.Long format2Long(String str) {
		if (isNull(str)) {
			return null;
		} else {
			return Long.valueOf(str);
		}
	}
	public static String formatNull(Object str) {
		if (str == null || isNull(str.toString())) {
			return "";
		} else
			return str.toString();
	}
	public static String join(AbstractCollection<String> s, String delimiter) {
		if (s.isEmpty())
			return "";
		Iterator<String> iter = s.iterator();
		StringBuffer buffer = new StringBuffer(iter.next());
		while (iter.hasNext())
			buffer.append(delimiter).append(iter.next());
		return buffer.toString();
	}

	public static String join(String[] s, String delimiter) {
		if (s.length == 0)
			return "";
		AbstractCollection<String> sl = new ArrayList<String>();
		for (int i = 0; i < s.length; i++) {
			sl.add(s[i]);
		}
		return join(sl, delimiter);
	}
	/**
	 * Split the given String into tokens.
	 * <P>
	 * This method is meant to be similar to the split function in other
	 * programming languages but it does not use regular expressions. Rather the
	 * String is split on a single String literal.
	 * <P>
	 * Unlike java.util.StringTokenizer which accepts multiple character tokens
	 * as delimiters, the delimiter here is a single String literal.
	 * <P>
	 * Each null token is returned as an empty String. Delimiters are never
	 * returned as tokens.
	 * <P>
	 * If there is no delimiter because it is either empty or null, the only
	 * element in the result is the original String.
	 * <P>
	 * StringHelper.split("1-2-3", "-");<br>
	 * result: {"1","2","3"}<br>
	 * StringHelper.split("-1--2-", "-");<br>
	 * result: {"","1","","2",""}<br>
	 * StringHelper.split("123", "");<br>
	 * result: {"123"}<br>
	 * StringHelper.split("1-2---3----4", "--");<br>
	 * result: {"1-2","-3","","4"}<br>
	 * 
	 * @param s
	 *            String to be split.
	 * @param delimiter
	 *            String literal on which to split.
	 * @return an array of tokens.
	 * @throws NullPointerException
	 *             if s is null.
	 * 
	 * @since ostermillerutils 1.00.00
	 */
	public static String[] split(String s, String delimiter) {
		int delimiterLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		int stringLength = s.length();
		if (delimiter == null || (delimiterLength = delimiter.length()) == 0) {
			// it is not inherently clear what to do if there is no delimiter
			// On one hand it would make sense to return each character because
			// the null String can be found between each pair of characters in
			// a String. However, it can be found many times there and we don'
			// want to be returning multiple null tokens.
			// returning the whole String will be defined as the correct
			// behavior
			// in this instance.
			return new String[] { s };
		}

		// a two pass solution is used because a one pass solution would
		// require the possible resizing and copying of memory structures
		// In the worst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.

		int count;
		int start;
		int end;

		// Scan s and count the tokens.
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1) {
			count++;
			start = end + delimiterLength;
		}
		count++;

		// allocate an array to return the tokens,
		// we now know how big it should be
		String[] result = new String[count];

		// Scan s again, but this time pick out the tokens
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1) {
			result[count] = (s.substring(start, end));
			count++;
			start = end + delimiterLength;
		}
		end = stringLength;
		result[count] = s.substring(start, end);

		return (result);
	}

	/**
	 * Split the given String into tokens. Delimiters will be returned as
	 * tokens.
	 * <P>
	 * This method is meant to be similar to the split function in other
	 * programming languages but it does not use regular expressions. Rather the
	 * String is split on a single String literal.
	 * <P>
	 * Unlike java.util.StringTokenizer which accepts multiple character tokens
	 * as delimiters, the delimiter here is a single String literal.
	 * <P>
	 * Each null token is returned as an empty String. Delimiters are never
	 * returned as tokens.
	 * <P>
	 * If there is no delimiter because it is either empty or null, the only
	 * element in the result is the original String.
	 * <P>
	 * StringHelper.split("1-2-3", "-");<br>
	 * result: {"1","-","2","-","3"}<br>
	 * StringHelper.split("-1--2-", "-");<br>
	 * result: {"","-","1","-","","-","2","-",""}<br>
	 * StringHelper.split("123", "");<br>
	 * result: {"123"}<br>
	 * StringHelper.split("1-2--3---4----5", "--");<br>
	 * result: {"1-2","--","3","--","-4","--","","--","5"}<br>
	 * 
	 * @param s
	 *            String to be split.
	 * @param delimiter
	 *            String literal on which to split.
	 * @return an array of tokens.
	 * @throws NullPointerException
	 *             if s is null.
	 * 
	 * @since ostermillerutils 1.05.00
	 */
	public static String[] splitIncludeDelimiters(String s, String delimiter) {
		int delimiterLength;
		// the next statement has the side effect of throwing a null pointer
		// exception if s is null.
		int stringLength = s.length();
		if (delimiter == null || (delimiterLength = delimiter.length()) == 0) {
			// it is not inherently clear what to do if there is no delimiter
			// On one hand it would make sense to return each character because
			// the null String can be found between each pair of characters in
			// a String. However, it can be found many times there and we don'
			// want to be returning multiple null tokens.
			// returning the whole String will be defined as the correct
			// behavior
			// in this instance.
			return new String[] { s };
		}

		// a two pass solution is used because a one pass solution would
		// require the possible resizing and copying of memory structures
		// In the worst case it would have to be resized n times with each
		// resize having a O(n) copy leading to an O(n^2) algorithm.

		int count;
		int start;
		int end;

		// Scan s and count the tokens.
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1) {
			count += 2;
			start = end + delimiterLength;
		}
		count++;

		// allocate an array to return the tokens,
		// we now know how big it should be
		String[] result = new String[count];

		// Scan s again, but this time pick out the tokens
		count = 0;
		start = 0;
		while ((end = s.indexOf(delimiter, start)) != -1) {
			result[count] = (s.substring(start, end));
			count++;
			result[count] = delimiter;
			count++;
			start = end + delimiterLength;
		}
		end = stringLength;
		result[count] = s.substring(start, end);

		return (result);
	}
	
	/**
	 * 替换字符串函数
	 * @param strSource - 源字符串
	 * @param strFrom - 要替换的子串
	 * @param strTo - 替换为的字符串
	 * @return
	 */
	public static String replace(String strSource, String strFrom, String strTo) {
		
		// 如果要替换的子串为空，则直接返回源串
		if (isNull(strFrom)) return strSource;
		
		String strDest = "";
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest + strSource.substring(0, intPos);
			// 加上替换后的子串
			strDest = strDest + strTo;
			// 修改源串为匹配子串后的子串
			strSource = strSource.substring(intPos + intFromLen);
		}
		// 加上没有匹配的子串
		strDest = strDest + strSource;
		return strDest;
	}
	
	/**
	 * 替换字符串函数
	 * @param strSource - 源字符串
	 * @param strFrom - 要替换的子串
	 * @param strTo - 替换为的字符串
	 * @return
	 */
	public static StringBuffer replace(StringBuffer strSource, String strFrom, String strTo) {
		
		// 如果要替换的子串为空，则直接返回源串
		if (isNull(strFrom)) return strSource;
		
		StringBuffer strDest = new StringBuffer();
		// 要替换的子串长度
		int intFromLen = strFrom.length();
		int intPos;
		
		// 循环替换字符串
		while ((intPos = strSource.indexOf(strFrom)) != -1) {
			// 获取匹配字符串的左边子串
			strDest = strDest.append(strSource.substring(0, intPos));
			// 加上替换后的子串
			strDest = strDest.append(strTo);
			// 修改源串为匹配子串后的子串
			strSource = new StringBuffer(strSource.substring(intPos + intFromLen));
		}
		// 加上没有匹配的子串
		strDest = strDest.append(strSource);
		return strDest;
	}
	
	/**
	 * 根据正则表达式替换字符串函数
	 * @param strSource - 源字符串
	 * @param regexp - 正则表达式
	 * @param strTo - 替换为的字符串
	 * @return
	 */
	public static String replace2(String strSource, String regexp, String strTo) {
		
		// 如果要替换的子串为空，则直接返回源串
		if (isNull(regexp)) return strSource;
		
		String regex = regexp;
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(strSource);
	    
	    if(matcher.find()) {
	    	strSource = replace(strSource, matcher.group(), strTo);
	    	return replace2(strSource, regexp, strTo);
	    } else {
	        return strSource;
	    }
	}
	
	public static String substringAfter(String str, String separator) {
		return StringUtils.substringAfter(str, separator);
	}
	
	public static String substringAfterLast(String str, String separator) {
		return StringUtils.substringAfterLast(str, separator);
	}
	
	public static String substringBefore(String str, String separator) {
		return StringUtils.substringBefore(str, separator);
	}
	
	public static String substringBeforeLast(String str, String separator) {
		return StringUtils.substringBeforeLast(str, separator);
	}
	
	public static String substringBetween(String str, String tag) {
		return StringUtils.substringBetween(str, tag);
	}
	
	public static String substringBetween(String str, String open, String close) {
		return StringUtils.substringBetween(str, open, close);
	}
	
	public static String[] substringsBetween(String str, String open, String close) {
		return StringUtils.substringsBetween(str, open, close);
	}
	
	public static boolean isNumeric(String str) {
		return StringUtils.isNumeric(str);
	}

	/**
	 *  按字节截取字符串
	 * @param str  字符串
	 * @param subSLength  截取到的字节
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String subStr(String str, int subSLength)
			throws UnsupportedEncodingException {
		if (str == null)
			return "";
		else{
			int tempSubLength = subSLength;//截取字节数
			String subStr = str.substring(0, str.length()<subSLength ? str.length() : subSLength);//截取的子串
			int subStrByetsL = subStr.getBytes("GBK").length;//截取子串的字节长度
			// 说明截取的字符串中包含有汉字
			while (subStrByetsL > tempSubLength){
				int subSLengthTemp = --subSLength;
				subStr = str.substring(0, subSLengthTemp>str.length() ? str.length() : subSLengthTemp);
				subStrByetsL = subStr.getBytes("GBK").length;
			}
			return subStr;
		}
	}
}

