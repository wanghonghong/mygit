package com.jm.staticcode.util.wx;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 *<p>MD5工具类</p>
 *
 * @author chenyy
 * @version latest
 * @data 2016年6月4日
 */
public class MD5Util {
	
	@SuppressWarnings("unused")
	private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) n += 256;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static String MD5Encode(String origin, String charsetname) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(origin.getBytes("UTF-8"));
            BigInteger hash = new BigInteger(1, md.digest());
            resultString = hash.toString(16);
            if ((resultString.length() % 2) != 0) {
                resultString = "0" + resultString;
            }
        } catch (Exception exception) {
        }
        return resultString;
    }

    private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
            "e", "f"                       };

}
