package com.jm.business.service.wb;

import com.jm.repository.client.dto.wb.WbAccessToken;
import com.jm.repository.client.wb.WbClient;
import com.jm.repository.jpa.wb.WbAuthRepository;
import com.jm.repository.po.wb.WbAuth;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.constant.WbUrl;
import com.jm.staticcode.util.StringUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.wb.WbSHA1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>微博授权</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/22 10:29
 */
@Slf4j
@Service
public class WbAuthService {
    @Autowired
    private WbClient wbClient;
    @Autowired
    private WbAuthRepository wbAuthRepository;

    /**
     * <p>获取第三方应用信息</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/3/4 11:02
     */
    public WbAuth getWbAuth(){
        List<WbAuth> list = wbAuthRepository.findAll();
        if (list.size()>0){
            return list.get(0);
        }
        return null;
    }
    /**
     * <p>授权前期获取code</p>
     * @Author cj
     * @version latest
     * @Date 2017/2/22 10:31
     */
    public void getCode(HttpServletResponse response,String scope,String uri) throws Exception{
        String url = this.getAuthorizeUrl("code",uri);
        if(!"".equals(scope)){
            url+="&scope="+scope;  //静默授权
        }
        response.sendRedirect(url);
    }

    /**
     * <p>网页授权完重定向到微信服务器，返回code</p>
     * @Author cj
     * @version latest
     * @Date 2017/2/23 9:39
     */
    public WbAccessToken oauth(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String code = request.getParameter("code").toString(); //微博获取的code根据code来获取token*/
        String uri = request.getRequestURI();
        if(StringUtil.isNotNull(uri)){
            uri = uri.replaceAll("/msa","");
        }
        return wbClient.getTokenByCode(code,uri);
    }

    /**
     * <p>前期申请微博授权页地址</p>
     * @Author cj
     * @version latest
     * @Date 2017/2/23 9:40
     */
    public String getAuthorizeUrl(String responseType,String uri) {
        Map map = new HashMap();
        map.put("client_id",Constant.CLIENT_ID);
        map.put("redirect_uri",Constant.PC_DOMAIN + uri);
        //map.put("redirect_uri","http://acyyhr.oicp.net/msa"+ Constant.REDIRECT_OAUTH);
        map.put("&response_type",responseType);
        return WbUrl.AUTHORIZE_URL.trim()+"?"+Toolkit.mapToParam(map);
    }
    /**
     * <p>粉丝服务-申请成为开发者模式</p>
     *
     * @Author cj
     * @version latest
     * @Date 2017/2/23 9:39
     */
    public void developWb(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nonce = request.getParameter("nonce");
        String echoStr = request.getParameter("echostr");
        String timestamp = request.getParameter("timestamp");
        String signature = request.getParameter("signature");
        String returnContent = ""; //返回的响应结果
        //验证签名
        boolean isValid = WbSHA1.ValidateSHA(signature, nonce, timestamp);
        if(isValid){ //签名正确时，如果存在echoStr就返回echoStr，否则接收数据
            if(!StringUtils.isEmpty(echoStr)){
                //存在echoStr，是首次配置时验证url可达性。
                returnContent =  echoStr; //返回内容为echoStr的内容
            }else{
                //正常推送消息时不会存在echoStr参数。
                //接收post过来的消息数据
                StringBuilder sb = new StringBuilder();
                BufferedReader in = request.getReader();
                String line;
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
            }
        }else{
            //TODO 异常信息
            returnContent = "sign error!";
        }
        WbSHA1.output(response, returnContent); //输出响应的内容。
    }


}
