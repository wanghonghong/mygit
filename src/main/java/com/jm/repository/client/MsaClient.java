package com.jm.repository.client;


import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.dispatch.DispatchJoinQo;
import com.jm.mvc.vo.zb.join.CheckUo;
import com.jm.mvc.vo.zb.join.ZbClassDataVo;
import com.jm.mvc.vo.zb.join.ZbJoinClassVo;
import com.jm.mvc.vo.zb.join.ZbJoinVo;
import com.jm.mvc.vo.zb.shop.ShopUo;
import com.jm.repository.client.dto.zb.*;
import com.jm.staticcode.constant.ZbConstant;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by ME on 2016/9/21.
 */
@Slf4j
@Repository
public class MsaClient extends BaseClient {

//    public PageItem queryJoinList(ZbJoinVo zbJoinVo) throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/join/list";
//        PageItem joins = restTemplate.postForObject(url, zbJoinVo,PageItem.class);
//        return joins;
//    }
//
//    public ZbJoinDto queryJoin(ZbJoinVo zbJoinVo) throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/join";
//        ZbJoinDto zbJoinDto = restTemplate.postForObject(url, zbJoinVo,ZbJoinDto.class);
//        return zbJoinDto;
//    }

//    public JmMessage saveCheck(CheckUo checkUo) throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/join/check";
//        JmMessage message = restTemplate.postForObject(url,checkUo,JmMessage.class);
//        return message;
//    }
//
//    public JmMessage deleteJoin(ZbJoinVo zbJoinVo) throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/join/delete";
//        JmMessage message = restTemplate.postForObject(url, zbJoinVo,JmMessage.class);
//        return message;
//    }

    public PageItem queryClassList(ZbJoinClassVo zbJoinClassVo) throws IOException {
        String url  = ZbConstant.ERP_SERVICE+"/join/customers";
        PageItem joins = restTemplate.postForObject(url, zbJoinClassVo,PageItem.class);
        return joins;
    }

    @ApiOperation("获取总部平台代理类编辑弹窗基本资料Dto" )
    public ZbClassDataDto queryClassData(ZbJoinVo zbJoinVo) throws IOException {
        String url  = ZbConstant.ERP_SERVICE+"/join/data";
        ZbClassDataDto zbClassDataDto = restTemplate.postForObject(url, zbJoinVo,ZbClassDataDto.class);
        return zbClassDataDto;
    }

    @ApiOperation("获取总部平台代理类编辑弹窗基本资料保存" )
    public JmMessage updateClassData(ZbClassDataVo zbClassDataVo) throws IOException {
        String url  = ZbConstant.ERP_SERVICE+"/join/save";
        JmMessage message = restTemplate.postForObject(url, zbClassDataVo,JmMessage.class);
        return  message;
    }

//    @ApiOperation("获取商家系统服务商角色信息" )
//    public List queryRoles() throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/join/roles";
//        List list = restTemplate.postForObject(url,null,List.class);
//        return  list;
//    }
    @ApiOperation("获取总部派单商家系统加盟信息分页" )
    public PageItem queryJoins(DispatchJoinQo dispatchJoinQo) throws IOException {
        String url  = ZbConstant.ERP_SERVICE+"/join/dispatch/list";
        PageItem joins = restTemplate.postForObject(url,dispatchJoinQo,PageItem.class);
        return joins;
    }

//    public ZbDispatchDto queryDispatchVo() throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/join/dispatch/roles";
//        ZbDispatchDto zbDispatchDto = restTemplate.postForObject(url,null,ZbDispatchDto.class);
//        return zbDispatchDto;
//    }
//    @ApiOperation("获取商家系统商家类信息" )
//    public PageItem queryBusinessList(BusinessDto businessDto) throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/shop/erpGetShopList";
//        PageItem businessDtos = restTemplate.postForObject(url,businessDto,PageItem.class);
//        return businessDtos;
//    }

    @ApiOperation("总部系统根据用户编号获取店铺列表" )
    public PageItem queryManageList(BusinessDto businessDto) throws IOException {
        String url  = ZbConstant.ERP_SERVICE+"/shop/erpUserShops";
        PageItem businessDtos = restTemplate.postForObject(url,businessDto,PageItem.class);
        return businessDtos;
    }
//    @ApiOperation("总部系统 开店状态列表" )
//    public PageItem queryShopList(BusinessDto businessDto) throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/shop/erpShops";
//        PageItem businessDtos = restTemplate.postForObject(url,businessDto,PageItem.class);
//        return businessDtos;
//    }
//    @ApiOperation("总部系统 店铺审核列表" )
//    public PageItem queryShopReviewList(BusinessDto businessDto) throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/shop/erpAuditShops";
//        PageItem businessDtos = restTemplate.postForObject(url,businessDto,PageItem.class);
//        return businessDtos;
//    }
//    @ApiOperation("总部系统 公众号列表" )
//    public PageItem queryWXUserList(ZbWxUserDto zbWxUserDto) throws IOException {
//        String url  = ZbConstant.ERP_SERVICE+"/wxuser/erpWxUsers";
//        PageItem businessDtos = restTemplate.postForObject(url, zbWxUserDto,PageItem.class);
//        return businessDtos;
//    }
//    @ApiOperation(value = "店铺关停切换")
//    public JmMessage setShopStatus(@RequestBody @Valid ShopUo uo) throws IOException{
//        String url  = ZbConstant.ERP_SERVICE+"/shop/setShopStatus";
//        JmMessage message = restTemplate.postForObject(url,uo,JmMessage.class);
//        return message;
//    }
}
