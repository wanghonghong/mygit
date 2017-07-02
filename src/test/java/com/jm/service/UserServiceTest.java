package com.jm.service;


import com.jm.business.service.order.ShoppingCartService;
import com.jm.business.service.system.UserRoleService;
import com.jm.business.service.system.UserService;
import com.jm.repository.client.RedisClient;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>用户测试</p>
 * @author wukf
 * @version latest
 * @date 2016/4/26
 */
public class UserServiceTest extends BaseServiceTest{

    @Autowired
    private UserService userService ;

    @Autowired
    public RedisClient redisClient;

    @Autowired
    private ShoppingCartService shoppingCartService;
    
    @Autowired
	 private UserRoleService userRoleService ;

    @Test
    public void test() throws Exception {
        System.out.print("吴克府");
        /*List<ShoppingCartQo>  shoppingCarts = shoppingCartService.queryShoppingCart(1,1);
        Assert.assertTrue(shoppingCarts.size()>0);*/
    }
/*
    @Test
    public void test1() throws Exception {
        String encodingAesKey = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";
        String token = "pamtest";
        String timestamp = "1409304348";
        String nonce = "xxxxxx";
        String appId = "wxb11529c136998cb6";
        String replyMsg = "我是中文abcd123";
        String xmlFormat = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
        String afterAesEncrypt = "jn1L23DB+6ELqJ+6bruv21Y6MD7KeIfP82D6gU39rmkgczbWwt5+3bnyg5K55bgVtVzd832WzZGMhkP72vVOfg==";
        String randomStr = "aaaabbbbccccdddd";

        String replyMsg2 = "<xml><ToUserName><![CDATA[oia2Tj我是中文jewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";
        String afterAesEncrypt2 = "jn1L23DB+6ELqJ+6bruv23M2GmYfkv0xBh2h+XTBOKVKcgDFHle6gqcZ1cZrk3e1qjPQ1F4RsLWzQRG9udbKWesxlkupqcEcW7ZQweImX9+wLMa0GaUzpkycA8+IamDBxn5loLgZpnS7fVAbExOkK5DYHBmv5tptA9tklE/fTIILHR8HLXa5nQvFb3tYPKAlHF3rtTeayNf0QuM+UW/wM9enGIDIJHF7CLHiDNAYxr+r+OrJCmPQyTy8cVWlu9iSvOHPT/77bZqJucQHQ04sq7KZI27OcqpQNSto2OdHCoTccjggX5Z9Mma0nMJBU+jLKJ38YB1fBIz+vBzsYjrTmFQ44YfeEuZ+xRTQwr92vhA9OxchWVINGC50qE/6lmkwWTwGX9wtQpsJKhP+oS7rvTY8+VdzETdfakjkwQ5/Xka042OlUb1/slTwo4RscuQ+RdxSGvDahxAJ6+EAjLt9d8igHngxIbf6YyqqROxuxqIeIch3CssH/LqRs+iAcILvApYZckqmA7FNERspKA5f8GoJ9sv8xmGvZ9Yrf57cExWtnX8aCMMaBropU/1k+hKP5LVdzbWCG0hGwx/dQudYR/eXp3P0XxjlFiy+9DMlaFExWUZQDajPkdPrEeOwofJb";

        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        String afterEncrpt = pc.encryptMsg(replyMsg, timestamp, nonce);
        System.out.print(afterEncrpt);


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(afterEncrpt);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);
        Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("Encrypt");
        String encrypt = nodelist1.item(0).getTextContent();
        String fromXML = String.format(xmlFormat, encrypt);

        pc.decryptMsg("12345", timestamp, nonce, fromXML); // 这里签名错误
    }
*/


}

