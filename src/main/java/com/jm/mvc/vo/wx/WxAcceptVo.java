package com.jm.mvc.vo.wx;

import com.jm.staticcode.util.aes.WXBizMsgCrypt;
import lombok.Data;

import java.util.Map;

/**
 * <p></p>
 *
 * @author wukf
 * @version 1.1
 * @date 19
 */
@Data
public class WxAcceptVo {

    private String appid;

    private String openid;

    private String timestamp;

    private String nonce;

    private String encryptType;

    private String msgSignature;

    private String signature;


    private String toUserName;

    private String encrypt;

    private Map<String,String> map;
    
    private String msgId;

    /*private String infoType;

    private String componentVerifyTicket;

    private String authorizationCode;

    private String authorizerAppid;*/

}
