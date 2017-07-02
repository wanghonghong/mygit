package com.jm.staticcode.converter.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.WxPubAccountVo;
import com.jm.mvc.vo.wx.content.WxContentSentVo;
import com.jm.repository.client.dto.auth.*;
import com.jm.repository.po.wx.WxPubAccount;

import com.jm.staticcode.util.JsonMapper;
import org.springframework.beans.BeanUtils;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/24/024
 */
public class WxPubAccountConverter {

    public static void toWxPubAccount(WxPubAccountDto wxPubAccountDto,WxPubAccount wxPubAccount) {
        AuthorizationInfo authorizationInfo = wxPubAccountDto.getAuthorizationInfo();
        AuthorizerInfo baseInfo = wxPubAccountDto.getAuthorizerInfo();
        BusinessInfo businessInfo = baseInfo.getBusinessInfo();
        //拷贝对象
        BeanUtils.copyProperties(baseInfo,wxPubAccount);
        wxPubAccount.setAppid(authorizationInfo.getAuthorizerAppid());
        wxPubAccount.setServiceTypeIinfo(baseInfo.getServiceTypeInfo().getId());
        wxPubAccount.setVerifyTypeInfo(baseInfo.getVerifyTypeInfo().getId());
        //保存权限列表
        FuncscopeCategory[] funcscopeCategorys = authorizationInfo.getFuncInfo();
        String funs ="";
        for (FuncscopeCategory funcscopeCategory : funcscopeCategorys){
            funs += funcscopeCategory.getFuncscopeCategory().getId()+",";
        }
        funs = funs.substring(0,funs.length()-1);
        wxPubAccount.setFunctions(funs);
        //设置功能
        wxPubAccount.setOpenCard(businessInfo.getOpenCard());
        wxPubAccount.setOpenPay(businessInfo.getOpenPay());
        wxPubAccount.setOpenScan(businessInfo.getOpenScan());
        wxPubAccount.setOpenShake(businessInfo.getOpenShake());
        wxPubAccount.setOpenStore(businessInfo.getOpenStore());
    }
    
    public static WxPubAccountVo p2v(WxPubAccount account){
    	WxPubAccountVo wxPubAccountVo  = new WxPubAccountVo();
    	BeanUtils.copyProperties(account, wxPubAccountVo);
    	return wxPubAccountVo;
    }
    
    public static List<WxPubAccountVo>ps2vs(List<WxPubAccount> accounts){
    	List<WxPubAccountVo> vos = new ArrayList<>();
    	for (WxPubAccount wxPubAccount : accounts) {
    		WxPubAccountVo wxPubAccountVo  = new WxPubAccountVo();
    		BeanUtils.copyProperties(wxPubAccount, wxPubAccountVo);
    		vos.add(wxPubAccountVo);
		}
    	return vos;
    }

    public static PageItem<WxPubAccountVo>pos2vos(PageItem<Map<String,Object>> pageItemMap) throws Exception {
        PageItem<WxPubAccountVo> pageItem = new PageItem<>();
        List<Map<String,Object>> maps = pageItemMap.getItems();
        List<WxPubAccountVo> list = new ArrayList<>();
        for (Map<String,Object> map : maps){
            WxPubAccountVo wxPubAccountVo = JsonMapper.map2Obj(map,WxPubAccountVo.class);
            list.add(wxPubAccountVo);
        }
        pageItem.setCount(pageItemMap.getCount());
        pageItem.setItems(list);
        return pageItem;
    }

}
