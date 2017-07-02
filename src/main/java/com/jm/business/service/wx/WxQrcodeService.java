package com.jm.business.service.wx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.jm.business.service.shop.WxPubAccountService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.WxUserSession;
import com.jm.mvc.vo.activities.QrcodePosterForCreateVo;
import com.jm.mvc.vo.activities.QrcodePosterRo;
import com.jm.mvc.vo.activities.QrcodePosterVo;
import com.jm.repository.jpa.wx.WxUserRepository;
import com.jm.repository.po.wx.WxPubAccount;
import com.jm.repository.po.wx.WxUser;
import com.jm.staticcode.converter.activities.QrcodePosterConverter;
import com.jm.staticcode.util.ImgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jm.repository.jpa.activities.QrcodePosterRepository;
import com.jm.repository.po.shop.QrcodePoster;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>二维码海报</p>
 * @author hantp
 * @version latest
 * @date 2016/6/18
 */
@Service
public class WxQrcodeService {
	
	@Autowired
	private QrcodePosterRepository qrcodePosterRepository;

	@Autowired
	private WxUserService wxUserService;

	@Autowired
	private WxUserRepository wxUserRepository;

	@Autowired
	private WxUserQrcodeService wxUserQrcodeService;

	@Autowired
	private WxPubAccountService wxPubAccountService;

	/**
	 * 保存二维码海报
	 * @param qrcodePoster
	 * @return
	 */
	public QrcodePoster createQrcodePoster(QrcodePoster qrcodePoster) {
		qrcodePoster.setUploadTime(new Date());
		return qrcodePosterRepository.save(qrcodePoster);
	}

	public QrcodePoster findById(Integer id) {
		return qrcodePosterRepository.findOne(id);
	}
	
	/**
	 * 保存二维码海报
	 */
	@Transactional
	public JmMessage saveQrcodePoster(QrcodePosterVo qrcodeVo, JmUserSession user) throws Exception {

		List<QrcodePosterForCreateVo> qrcodePosters = qrcodeVo.getQrcodePosters();
		for (QrcodePosterForCreateVo vo:qrcodePosters) {
			if(vo.getType()==1){
				if( vo.getImageSrc()==null || vo.getImageSrc().equals("")){
					return new JmMessage(1,"请上传图片");
				}
			}
		}

		for (QrcodePosterForCreateVo vo:qrcodePosters) {
			if(vo.getType()==1){
				wxUserRepository.updateAllWxUser(user.getAppId());
				qrcodePosterRepository.updateTypeByShopId(user.getShopId());
			}
			QrcodePoster code = QrcodePosterConverter.toQrcodePoster(vo);
			code.setImageSrc(ImgUtil.substringUrl(vo.getImageSrc()));
			if(vo.getUploadTime()==null){
				code.setUploadTime(new Date());
			}
			if(qrcodeVo.getType()==0){
				code.setShopId(user.getShopId());
			}else{
				code.setAppId(user.getAppId());
				WxPubAccount pubAccount =wxPubAccountService.getWxPubAccount(user.getAppId());
				String qrcodeUrl = wxUserQrcodeService.composeQrcode(code,pubAccount.getPubQrcodeUrl());
				pubAccount.setQrcodePosterUrl(qrcodeUrl);
				wxPubAccountService.save(pubAccount);
			}
			qrcodePosterRepository.save(code);
		}
		return  new JmMessage(0,"成功");
	}





	
	/**
	 * 根据ID获取
	 * @param qrcodeId
	 * @return
	 */
	public QrcodePoster getQrcodePosterById(Integer qrcodeId) {
		return qrcodePosterRepository.findOne(qrcodeId);
	}


	/**
	 * 根据店铺编号获取二维码海报列表
	 * @param shopId
	 * @return
	 */
	public List<QrcodePosterRo> findQrcodePosterRoByShopId(Integer shopId) {
		List<QrcodePoster> qrcodePosters = qrcodePosterRepository.findQrcodePosterByShopIdAndIsDel(shopId,0);
		List<QrcodePosterRo> qrRos = new ArrayList<>();
		for (QrcodePoster qrcodePoster:qrcodePosters) {
			QrcodePosterRo ro = QrcodePosterConverter.p2v(qrcodePoster);
			ro.setImageSrc(ImgUtil.appendUrl(ro.getImageSrc(),0));
			if(ro.getCodeFormat()==1){
				ro.setFontColor(Integer.toHexString(qrcodePoster.getFontColor()));
			}
			qrRos.add(ro);
		}

		return qrRos;
	}


	/**
	 * 根据AppId获取二维码海报列表
	 * @return
	 */
	public List<QrcodePosterRo> findQrcodePosterRoByAppId(String appId) {
		List<QrcodePoster> qrcodePosters = qrcodePosterRepository.findQrcodePosterByAppId(appId);
		List<QrcodePosterRo> qrRos = new ArrayList<>();
		for (QrcodePoster qrcodePoster:qrcodePosters) {
			QrcodePosterRo ro = QrcodePosterConverter.p2v(qrcodePoster);
			ro.setImageSrc(ImgUtil.appendUrl(ro.getImageSrc(),0));
			if(ro.getCodeFormat()==1){
				ro.setFontColor(Integer.toHexString(qrcodePoster.getFontColor()));
			}
			qrRos.add(ro);
		}

		return qrRos;
	}


	/**
	 * 根据店铺编号获取二维码海报列表  只获取有效的，底图必须存在
	 * @param shopId
	 * @return
	 */
	public List<QrcodePosterRo> findQrcodePosterRosByShopId(Integer shopId) {
		List<QrcodePoster> qrcodePosters = qrcodePosterRepository.findQrcodePosterByShopIdAndIsDel(shopId,0);
		List<QrcodePosterRo> qrRos = new ArrayList<>();
		for (QrcodePoster qrcodePoster:qrcodePosters) {
			if( qrcodePoster.getImageSrc()!=null && !qrcodePoster.getImageSrc().equals("") ){
				QrcodePosterRo ro = QrcodePosterConverter.p2v(qrcodePoster);
				ro.setImageSrc(ImgUtil.appendUrl(ro.getImageSrc(),0));
				if(ro.getCodeFormat()==1){
					ro.setFontColor(Integer.toHexString(qrcodePoster.getFontColor()));
				}
				qrRos.add(ro);
			}
		}
		return qrRos;
	}


	/**
	 * 获取默认二维码底图
	 */
	public QrcodePoster getDefault(Integer shopId) {
		return qrcodePosterRepository.findByShopIdAndTypeAndIsDel(shopId,1,0);
	}


	/**
	 * 根据Appid、状态  获取二维码
	 */
	public QrcodePoster findQrcodePosterByAppIdAndType(String appid,Integer type) {
		return qrcodePosterRepository.findQrcodePosterByAppIdAndType(appid,type);
	}


	/**
	 *
	 * @param wxUserSession
	 * @return
	 * @throws Exception
	 */
	public JmMessage getQrcode(WxUserSession wxUserSession) throws Exception{
		String openid = wxUserSession.getOpenid();
		String appid = wxUserSession.getAppid();
		Integer shareId = wxUserSession.getShareid();
		WxUser wxuser = null;
		String  qrcodeurl="";
		//获取微信用户
		if(openid!=null&&appid!=null){
			wxuser=wxUserService.findWxUserByAppidAndOpenid(appid, openid);
		}
		if(wxuser==null){
			qrcodeurl= wxUserService.getQrcodeUrl(shareId,appid);
		}else{
			qrcodeurl= wxUserService.getQrcodeUrl(wxuser.getUserId(),appid);
		}
		return new JmMessage(0,qrcodeurl);
	}


	/**
	 * 根据店铺编号获取二维码海报列表
	 * @param shopId
	 * @param isDel 是否删除
	 * @return
	 */
	public List<QrcodePosterRo> findByShopIdAndIsDel(Integer shopId,int isDel) {
		List<QrcodePoster> qrcodePosters = qrcodePosterRepository.findQrcodePosterByShopIdAndIsDel(shopId,isDel);
		List<QrcodePosterRo> qrRos = new ArrayList<>();
		for (QrcodePoster qrcodePoster:qrcodePosters) {
			QrcodePosterRo ro = QrcodePosterConverter.p2v(qrcodePoster);
			ro.setImageSrc(ImgUtil.appendUrl(ro.getImageSrc(),100));
			if(ro.getCodeFormat()==1){
				ro.setFontColor(Integer.toHexString(qrcodePoster.getFontColor()));
			}
			qrRos.add(ro);
		}
		return qrRos;
	}

	/**
	 * 删除
	 */
	public QrcodePoster modifyIsDel(Integer id) {
		QrcodePoster qrcodePoster = qrcodePosterRepository.findOne(id);
		qrcodePoster.setIsDel(1);
		return qrcodePosterRepository.save(qrcodePoster);
	}

	@Transactional
	public QrcodePoster setDefault(Integer id,Integer shopId) {
		qrcodePosterRepository.updateTypeByShopId(shopId);
		QrcodePoster qrcodePoster = qrcodePosterRepository.findOne(id);
		qrcodePoster.setType(1);
		return qrcodePosterRepository.save(qrcodePoster);
	}

	public QrcodePoster setSel(Integer id) {
		QrcodePoster qrcodePoster = qrcodePosterRepository.findOne(id);
		if(qrcodePoster.getIsSel() == 0){
			qrcodePoster.setIsSel(1);
		}else{
			qrcodePoster.setIsSel(0);
		}
		return qrcodePosterRepository.save(qrcodePoster);
	}

	public QrcodePoster setSort(Integer id,Integer sort) {
		QrcodePoster qrcodePoster = qrcodePosterRepository.findOne(id);
		qrcodePoster.setSort(sort);
		return qrcodePosterRepository.save(qrcodePoster);
	}

}
