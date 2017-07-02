package com.jm.service;

import com.jm.business.service.wx.WxAuthService;
import com.jm.repository.client.dto.auth.WxPubAccountDto;
import com.jm.staticcode.util.JsonMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/23/023
 */
public class WxServiceTest extends BaseServiceTest{

    @Autowired
    private WxAuthService wxAuthService;

    @Test
    public void test() throws Exception {
        String str = "{\"authorizer_info\":{\"nick_name\":\"聚米为谷\",\"service_type_info\":{\"id\":2},\"verify_type_info\":{\"id\":0},\"user_name\":\"gh_1084e209f8c6\",\"alias\":\"jvmiweigu\",\"qrcode_url\":\"324234234\",\"business_info\":{\"open_pay\":1,\"open_shake\":0,\"open_scan\":0,\"open_card\":0,\"open_store\":0},\"idc\":1},\"authorization_info\":{\"authorizer_appid\":\"wx46506c1578a96cac\",\"func_info\":[{\"funcscope_category\":{\"id\":1}},{\"funcscope_category\":{\"id\":15}},{\"funcscope_category\":{\"id\":4}},{\"funcscope_category\":{\"id\":7}},{\"funcscope_category\":{\"id\":2}},{\"funcscope_category\":{\"id\":3}},{\"funcscope_category\":{\"id\":11}}]}}";
        WxPubAccountDto wxPubAccountDto = JsonMapper.parse(str,WxPubAccountDto.class);//wxAuthService.getPubAccount("wx46506c1578a96cac");
        wxPubAccountDto.getAuthorizerInfo();
        //wxService.setComponentVerifyTicket("ticket@@@7-3HD6zTQNcza_7Lcjw26xY4gKh8geYqRSE14jqfbhbakiJ_mYjsAbjOQ4dGJMxnpCDXq0M4CtSDDrOLBMIfYA");
        /*PreAuthCode preAuthCode = wxService.getPreAuthCode();
        String authPage = WxUrl.AUTH_PAGE.replace(Constant.APP_ID,Constant.COMPONENT_APP_ID).replace(Constant.PRE_AUTH_CODE,preAuthCode.getPreAuthCode())
                .replace(Constant.REDIRECT_URI,"http://acyyhr.oicp.net/msa/authorization_code");
        System.out.print(authPage);*/
    }
}
