package com.jm.repository.client;

import com.jm.mvc.vo.wx.QrcodeVo;
import com.jm.repository.client.dto.Menu;
import com.jm.repository.client.dto.MenuDto;
import com.jm.repository.client.dto.ResultMsg;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WxUrl;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>微信消息接口调用</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016/8/24
 */
@Log4j
@Repository
public class WxMessageClient extends BaseClient {

    //客服接口发消息
    public String sendWxMsg(Map<String,Object> map,String accessToken){
        String url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
        url = url.replace("ACCESS_TOKEN", accessToken);
    	return restTemplate.postForObject(url, map, String.class);
    }


}
