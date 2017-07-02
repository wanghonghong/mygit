package com.jm.mvc.controller.system;

import com.jm.business.service.wx.WxService;
import com.jm.business.service.wx.WxUserQrcodeService;
import com.jm.business.service.wx.WxUserService;
import com.jm.mvc.vo.activities.QrcodePosterCo;
import com.jm.mvc.vo.activities.QrcodePosterRo;
import com.jm.mvc.vo.activities.QrcodePosterVo;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Pic;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

import java.awt.image.BufferedImage;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.jm.business.service.wx.WxQrcodeService;
import com.jm.business.service.shop.ShopService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.activities.QrcodePosterForCreateVo;
import com.jm.repository.po.shop.QrcodePoster;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.activities.QrcodePosterConverter;

/**
 * <p>二维码海报</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/5/26
 */

@Api
@RestController
@RequestMapping(value = "/qrcodePoster")
public class QrcodePosterController {
	

	@Autowired
	private WxQrcodeService qrcodePosterService;
	@Autowired
	private WxUserService wxUserService;

    /**
     * 二维码海报名称保存
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JmMessage add (@ApiParam("二维码海报创建Vo") @RequestBody @Valid QrcodePosterForCreateVo qrcodeVo,
    						@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        qrcodeVo.setShopId(user.getShopId());
    		//新增一条二维码海报
    	QrcodePoster qr =qrcodePosterService.createQrcodePoster(QrcodePosterConverter.toQrcodePoster(qrcodeVo));
    	if(null!=qr){
    	    qr.setImageSrc(ImgUtil.substringUrl(qr.getImageSrc()));
    		wxUserService.updateAllWxUser(user.getAppId());
    		return new JmMessage(0,"成功");
    	}
    	
        return new JmMessage(1,"失败");
    }


//**********************************************************************************************************

    /**
     * 二维码海报名称保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JmMessage save (@ApiParam("二维码海报创建Vo") @RequestBody @Valid QrcodePosterVo qrcodeVo,
                          @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        return qrcodePosterService.saveQrcodePoster(qrcodeVo,user);
    }


    /**
     * 二维码海报回填
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public List<QrcodePosterRo> data (@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        List<QrcodePosterRo> qrlist = qrcodePosterService.findQrcodePosterRoByShopId(user.getShopId());
        return qrlist;
    }


    /**
     * 二维码海报列表 只获取有效的，底图必须存在
     */
    @RequestMapping(value = "/data_list", method = RequestMethod.POST)
    public List<QrcodePosterRo> dataList (@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        List<QrcodePosterRo> qrlist = qrcodePosterService.findQrcodePosterRosByShopId(user.getShopId());
        return qrlist;
    }


    /**
     * 聚客二维码海报回填
     */
    @RequestMapping(value = "/jkdata", method = RequestMethod.POST)
    public List<QrcodePosterRo> jkdata (@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        List<QrcodePosterRo> qrlist = qrcodePosterService.findQrcodePosterRoByAppId(user.getAppId());
        return qrlist;
    }


    /*******************************************************************************************************************/

    @ApiModelProperty(value = "二维码海报编辑数据")
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public QrcodePosterRo edit(@ApiParam("海报编号")  @PathVariable("id") Integer id) throws Exception {
        QrcodePoster qr = qrcodePosterService.findById(id);
        QrcodePosterRo ro =  QrcodePosterConverter.p2v(qr);
        if(ro.getCodeFormat()==1){
            ro.setFontColor(Integer.toHexString(qr.getFontColor()));
        }
        ro.setImageSrc(ImgUtil.appendUrl(qr.getImageSrc()));
        return ro;
    }


    @ApiModelProperty(value = "二维码海报 新增/修改")
    @RequestMapping(value = "/new_save", method = RequestMethod.POST)
    public JmMessage new_save (@ApiParam("二维码海报创建Vo") @RequestBody @Valid QrcodePosterCo co,
                           @ApiParam(hidden=true) HttpServletRequest request) throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        QrcodePoster qrcodePoster =  QrcodePosterConverter.c2p(co);
        qrcodePoster.setShopId(user.getShopId());
        if( qrcodePoster.getImageSrc()==null || qrcodePoster.getImageSrc().equals("")){
                return new JmMessage(1,"请上传图片");
        }else{
            String imgurl = ImgUtil.substringUrl(qrcodePoster.getImageSrc());
            Pic pic = new Pic();
            BufferedImage img =  pic.loadImageUrl(ImgUtil.appendUrl(imgurl));
            if(img!=null){
                if(img.getWidth()==750 && img.getHeight()==1334 ){
                    qrcodePoster.setImageSrc(ImgUtil.substringUrl(qrcodePoster.getImageSrc()));
                }else{
                    return new JmMessage(1,"海报模板尺寸不对");
                }
            }else{
                return new JmMessage(1,"图片上传失败，请重新上传");
            }
        }
        qrcodePosterService.createQrcodePoster(qrcodePoster);
        return  new JmMessage(0,"成功");
    }


    /**
     * 二维码海报回填
     */
    @RequestMapping(value = "/new_list", method = RequestMethod.POST)
    public List<QrcodePosterRo> newlist (@ApiParam(hidden=true) HttpServletRequest request) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        List<QrcodePosterRo> qrlist = qrcodePosterService.findByShopIdAndIsDel(user.getShopId(),0);
        return qrlist;
    }


    /**
     * 删除
     */
    @RequestMapping(value = "/del/{id}", method = RequestMethod.DELETE)
    public JmMessage del (@ApiParam(hidden=true) HttpServletRequest request,
                                     @ApiParam("海报编号")  @PathVariable("id") Integer id) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(user.getRoleId().equals(2)||user.getRoleId().equals(25)){
            qrcodePosterService.modifyIsDel(id);
            return  new JmMessage(0,"删除成功");
        }else{
            return  new JmMessage(1,"无权限删除");
        }
    }

    /**
     * 设为默认
     */
    @RequestMapping(value = "/setDefault/{id}", method = RequestMethod.GET)
    public JmMessage setDefault(@ApiParam(hidden=true) HttpServletRequest request,
                          @ApiParam("海报编号")  @PathVariable("id") Integer id) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(user.getRoleId().equals(2)||user.getRoleId().equals(25)){
            qrcodePosterService.setDefault(id,user.getShopId());
            return  new JmMessage(0,"操作成功");
        }else{
            return  new JmMessage(1,"无权限删除");
        }
    }


    /**
     * 轮播选用
     */
    @RequestMapping(value = "/setSel/{id}", method = RequestMethod.GET)
    public JmMessage setSel(@ApiParam(hidden=true) HttpServletRequest request,
                                @ApiParam("海报编号")  @PathVariable("id") Integer id) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        if(user.getRoleId().equals(2)||user.getRoleId().equals(25)){
            qrcodePosterService.setSel(id);
            return  new JmMessage(0,"操作成功");
        }else{
            return  new JmMessage(1,"无权限删除");
        }
    }


    /**
     * 设置顺序
     */
    @RequestMapping(value = "/setSort/{id}", method = RequestMethod.GET)
    public JmMessage setSort(@ApiParam(hidden=true) HttpServletRequest request,
                            @ApiParam("海报编号")  @PathVariable("id") Integer id) {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        String sort = request.getParameter("sort");//顺序
        if(user.getRoleId().equals(2)||user.getRoleId().equals(25)){
            qrcodePosterService.setSort(id, Toolkit.parseObjForInt(sort));
            return  new JmMessage(0,"操作成功");
        }else{
            return  new JmMessage(1,"无权限删除");
        }
    }

}
