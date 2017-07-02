package com.jm.staticcode.util.wb;

import com.jm.staticcode.constant.Constant;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>微博签名</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/23 9:33
 */
public class WbSHA1 {

    private static String app_secret = Constant.CLIENT_SECRET; //appkey对应的secret，验证签名时使用。
    /**
     * 验证sha1签名，验证通过返回true，否则返回false
     * @param signature
     * @param nonce
     * @param timestamp
     * @return
     */
    public static boolean ValidateSHA(String signature, String nonce,
                                      String timestamp) {

        if (signature == null || nonce == null || timestamp == null) {
            return false;
        }
        String sign = sha1(getSignContent(nonce, timestamp, app_secret));
        if (!signature.equals(sign)) {
            return false;
        }
        return true;
    }

    /**
     * 生产sha1签名
     * @param strSrc
     * @return
     */
    private static String sha1(String strSrc) {
        MessageDigest md = null;
        String strDes = null;

        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            //TODO
            e.printStackTrace();
        }
        return strDes;
    }

    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));

            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }


    /**
     * 对非空参数按字典顺序升序构造签名串
     * @param params
     * @return
     */

    private static String getSignContent(String... params) {
        List<String> list = new ArrayList(params.length);
        for(String temp : params){
            if(!StringUtils.isEmpty(temp)){
                list.add(temp);
            }
        }
        Collections.sort(list);
        StringBuilder strBuilder = new StringBuilder();
        for (String element : list) {
            strBuilder.append(element);
        }
        return strBuilder.toString();
    }

    /**
     * 输出返回内容
     * @param response
     * @param msg
     * @throws IOException
     */
    public static void output(HttpServletResponse response, String msg)
            throws IOException {
        if(msg != null){
            response.getOutputStream().write(msg.getBytes());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
    }

}
