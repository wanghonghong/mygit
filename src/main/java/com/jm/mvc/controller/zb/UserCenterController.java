package com.jm.mvc.controller.zb;

import com.jm.business.service.shop.ShopService;
import com.jm.business.service.system.JmJoinService;
import com.jm.business.service.system.UserService;
import com.jm.business.service.wx.WxUserService;
import com.jm.business.service.zb.BasicDataService;
import com.jm.business.service.zb.ZbUserService;
import com.jm.business.service.zb.system.CustomerCenterService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.erp.*;
import com.jm.mvc.vo.join.ClassDataVo;
import com.jm.mvc.vo.join.JoinClassVo;
import com.jm.mvc.vo.zb.join.ZbClassDataVo;
import com.jm.mvc.vo.zb.join.ZbJoinClassVo;
import com.jm.mvc.vo.zb.join.ZbJoinVo;
import com.jm.mvc.vo.zb.notice.*;
import com.jm.mvc.vo.zb.system.BasicDataCo;
import com.jm.mvc.vo.zb.system.BasicDataRo;
import com.jm.mvc.vo.zb.system.BasicDataUo;
import com.jm.repository.po.system.JmJoin;
import com.jm.repository.po.system.user.User;
import com.jm.repository.po.zb.system.ZbDepartment;
import com.jm.repository.po.zb.user.ZbUser;
import com.jm.staticcode.constant.ZbConstant;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Created by ME on 2016/8/18.
 */
@RestController
@RequestMapping(value = "/zb")
public class UserCenterController {
    @Autowired
    private BasicDataService basicDataService;
    @Autowired
    private CustomerCenterService customerCenterService;
    @Autowired
    private ZbUserService zbUserService;
    @Autowired
    private JmJoinService jmJoinService;
    @Autowired
    private WxUserService wxUserService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private UserService userService;

    @ApiOperation("首页")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(@ApiParam(hidden=true) HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        view.setViewName("/pc/shop/shop_manager");
        return view;
    }

    @RequestMapping(value = "/basicData",method = RequestMethod.POST)
    public ZbUser addUser(@RequestBody @Valid BasicDataCo basicDataCo){
        return basicDataService.add(basicDataCo);
}

    @RequestMapping(value = "/basicData/{id}",method = RequestMethod.PUT)
    public void update(@PathVariable("id") int id,@RequestBody @Valid BasicDataUo basicDataUo){
          basicDataService.update(id,basicDataUo);
    }

    @RequestMapping(value = "/basicData/{id}",method = RequestMethod.GET)
    public BasicDataRo getUser(@PathVariable("id") int id){
        return basicDataService.getUser(id);
    }

    @RequestMapping(value = "/basicData", method = RequestMethod.GET)
    public List<BasicDataRo> queryUser(){
        return basicDataService.queryBasicData();
    }

    @ApiOperation("获取商家系统平台加盟列表")
    @RequestMapping(value = "/customer_center/list", method = RequestMethod.POST)
    public PageItem getClassList(@RequestBody @Valid ZbJoinClassVo zbJoinClassVo) throws Exception {
        PageItem<JoinClassVo> pageItem = jmJoinService.queryClassVos(zbJoinClassVo);
        return pageItem;
    }

    @ApiOperation("获取总部平台代理类编辑弹窗基本资料Dto")
    @RequestMapping(value = "/class_data", method = RequestMethod.POST)
    public ClassDataVo getClassDto(@RequestBody @Valid ZbJoinVo zbJoinVo) throws IOException {
        ClassDataVo classDataVo = jmJoinService.getClassVo(zbJoinVo);
        return classDataVo;
    }

    @ApiOperation("总部平台代理类编辑弹窗基本资料保存")
    @RequestMapping(value = "/class_data/update", method = RequestMethod.POST)
    public JmMessage updateClassDto(@RequestBody @Valid ZbClassDataVo zbClassDataVo) throws IOException {
        JmJoin jmJoin = jmJoinService.findJmJoin(zbClassDataVo.getUserId(),zbClassDataVo.getType());
        User user = userService.getUser(zbClassDataVo.getUserId());
        jmJoinService.saveClassData(jmJoin,user,zbClassDataVo);
        return new JmMessage(0 ,"保存成功！");
    }

    @ApiOperation("总部系统查询商家类列表")
    @RequestMapping(value = "/business_class/list", method = RequestMethod.POST)
    public PageItem getBusinessList(@RequestBody @Valid ErpShopQo qo) throws IOException {
        PageItem<ErpShopRo> pageItem =  shopService.erpGetShopList(qo);
        return pageItem;
    }
    @ApiOperation("总部系统根据用户编号获取店铺列表")
    @RequestMapping(value = "/business_class/manage_list", method = RequestMethod.POST)
    public PageItem queryManageList(@RequestBody @Valid ErpShopQo qo) throws IOException {
        PageItem<ErpUserShopRo> pageItem =  shopService.erpUserShops(qo);
        return pageItem;
    }
    @ApiOperation("总部系统 开店状态列表")
    @RequestMapping(value = "/business_class/shop_list", method = RequestMethod.POST)
    public PageItem queryShopList(@RequestBody @Valid ErpShopQo qo) throws IOException {
        PageItem<ErpUserShopRo> pageItem =  shopService.erpShops(qo);
        return pageItem;
    }
    @ApiOperation("总部系统 公众号列表")
    @RequestMapping(value = "/public_number/list", method = RequestMethod.POST)
    public PageItem queryWXUserList(@RequestBody @Valid ErpWxUserQo qo) throws IOException {
        PageItem<ErpWxUserRo>  ros = wxUserService.findErpWxUsers(qo);
        return ros;
    }
    @ApiOperation("公告新增")
    @RequestMapping(value = "/notice",method = RequestMethod.POST)
    public JmMessage saveNotice(@ApiParam(hidden = true) HttpServletRequest request,
                                  @RequestBody @Valid NoticeCo noticeCo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        noticeCo.setUserId(user.getUserId());
        basicDataService.saveNotice(noticeCo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("公告更新")
    @RequestMapping(value = "/notice",method = RequestMethod.PUT)
    public JmMessage updateNotices(@ApiParam(hidden = true) HttpServletRequest request,
                                     @RequestBody @Valid NoticeUo noticeUo){
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        noticeUo.setUserId(user.getUserId());
        basicDataService.updateNotices(noticeUo);
        return new JmMessage(0,"保存成功");
    }

    @ApiOperation("公告分页查询")
    @RequestMapping(value = "/notice/list", method = RequestMethod.POST)
    public PageItem<NoticeVo> queryNoticeList(@ApiParam(hidden = true) HttpServletRequest request,
                                              @RequestBody @Valid NoticeQo noticeQo) throws Exception {
        JmUserSession user= (JmUserSession) request.getSession().getAttribute(ZbConstant.SESSION_USER);
        noticeQo.setUserId(user.getUserId());
        Integer departmentId = zbUserService.getUserForUser(user.getUserId()).getDepartment();
        if (departmentId!=null){
            String departmentIdNew = "["+departmentId+"]";
            noticeQo.setDepartmentId(departmentIdNew);
        }
        PageItem<NoticeVo> notices = basicDataService.queryNoticeList(noticeQo);
        return notices;
    }

    @ApiOperation("单个公告查询")
    @RequestMapping(value = "/notice/{id}", method = RequestMethod.GET)
    public NoticeVo queryNotice(@ApiParam("公告ID") @PathVariable("id") Integer id) throws Exception {
        return  basicDataService.getNotice(id);
    }

    @ApiOperation("单个公告删除")
    @RequestMapping(value = "/notice/{id}", method = RequestMethod.DELETE)
    public JmMessage deleteNotice(@ApiParam("公告ID") @PathVariable("id") Integer id) throws Exception {
        return  basicDataService.deleteNotice(id);
    }

    @ApiOperation("公告撤回")
    @RequestMapping(value = "/notice/{id}", method = RequestMethod.PUT)
    public JmMessage updateNotice(@ApiParam("公告ID") @PathVariable("id") Integer id) throws Exception {
        return  basicDataService.updateNotice(id);
    }

    @ApiOperation("单个公告配置")
    @RequestMapping(value = "/notice_config/{id}", method = RequestMethod.GET)
    public NoticeConfiVo queryNoticeConfig(@ApiParam("公告ID") @PathVariable("id") Integer id) throws Exception {
        return  basicDataService.getNoticeConfig(id);
    }

    @ApiOperation("公告权限配置")
    @RequestMapping(value = "/notice_config", method = RequestMethod.PUT)
    public JmMessage updateNoticeConfig(@RequestBody @Valid NoticeConfiUo noticeConfiUo) throws Exception {
        return  basicDataService.updateNoticeConfig(noticeConfiUo);
    }

    @ApiOperation("用户中心公告权限查看")
    @RequestMapping(value = "/notice_right/{id}", method = RequestMethod.GET)
    public List<ZbDepartment> getNoticeConfig(@ApiParam("公告ID") @PathVariable("id") Integer id) throws Exception {
        return  basicDataService.queryNoticeConfig(id);
    }

    @ApiOperation("商家系统公告分页查询")
    @RequestMapping(value = "/notices", method = RequestMethod.POST)
    public PageItem<NoticeVo> queryNotices(@ApiParam(hidden = true) HttpServletRequest request,
                                             @RequestBody @Valid NoticeQo noticeQo) throws Exception {
        PageItem<NoticeVo> notices = basicDataService.queryNoticeList(noticeQo);
        return notices;
    }

    @ApiOperation("商家系统单个公告查询")
    @RequestMapping(value = "/join/notice", method = RequestMethod.POST)
    public NoticeVo getNotice(@RequestBody @Valid NoticeQo noticeQo) throws Exception {
        return  basicDataService.queryNotice(noticeQo.getId());
    }

}
