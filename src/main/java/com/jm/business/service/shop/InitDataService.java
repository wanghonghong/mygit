package com.jm.business.service.shop;

import com.jm.mvc.vo.JmUserSession;
import com.jm.repository.jpa.shop.imageText.ImageTextRepository;
import com.jm.repository.jpa.shop.imageText.ImageTextTypeRepository;
import com.jm.repository.jpa.shop.ShopResGroupRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.ShopResGroup;
import com.jm.repository.po.shop.imageText.ImageText;
import com.jm.repository.po.shop.imageText.ImageTextType;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>创建店铺的时候初始化数据</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/11/1 10:39
 */
@Log4j
@Service
public class InitDataService {
    @Autowired
    private ShopResGroupRepository shopResGroupRepository;
    @Autowired
    ImageTextTypeRepository imageTextTypeRepository;
    @Autowired
    ImageTextRepository imageTextRepository;

    @Transactional
    public  void initData(Shop shop, JmUserSession user){
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        initImageText(createTime,shop,user);
        initShopResGroup(createTime,shop,user); //预置素材分组
    }
    @ApiOperation("预置素材分组")
    private void initShopResGroup(Timestamp createTime, Shop shop, JmUserSession user) {
        //新增图片分组
        ShopResGroup shopResGroupImg = new ShopResGroup();
        ShopResGroup shopResGroupVideo = new ShopResGroup();
        ShopResGroup shopResGroupAudio = new ShopResGroup();
        shopResGroupImg.setResType(1);
        shopResGroupImg.setGroupName("未分组");
        shopResGroupImg.setGroupFlag("N");
        shopResGroupImg.setShopId(shop.getShopId());

        shopResGroupVideo.setResType(2);
        shopResGroupVideo.setGroupName("未分组");
        shopResGroupVideo.setGroupFlag("N");
        shopResGroupVideo.setShopId(shop.getShopId());

        shopResGroupAudio.setResType(3);
        shopResGroupAudio.setGroupName("未分组");
        shopResGroupAudio.setGroupFlag("N");
        shopResGroupAudio.setShopId(shop.getShopId());
        List<ShopResGroup> shopResGroupList = new ArrayList();
        shopResGroupList.add(shopResGroupImg);
        shopResGroupList.add(shopResGroupVideo);
        shopResGroupList.add(shopResGroupAudio);
        shopResGroupRepository.save(shopResGroupList);
    }
    @ApiOperation("预置图文和图文分类")
    private void initImageText(Timestamp createTime, Shop shop, JmUserSession user) {
        //品牌故事
        ImageText imageText1 = new ImageText();
        ImageTextType imageTextType1 = new ImageTextType();
        imageTextType1.setCreateStaffId(Toolkit.parseObjForInt(user.getUserId()));
        imageTextType1.setCreateTime(createTime);
        imageTextType1.setImageUrl(Toolkit.parseObjForStr(shop.getImgUrl()));
        imageTextType1.setShareText("一个好品牌的故事");
        imageTextType1.setShopId(shop.getShopId());
        imageTextType1.setTypeName("品牌故事");
        imageTextType1.setSort(0);
        imageTextType1.setTypeId(2);
        imageTextType1.setIsEdit(1);//不可编辑分类名称
        imageTextType1.setArticleType(1);
        ImageTextType imageTextTypeR1 = imageTextTypeRepository.save(imageTextType1);
        imageText1.setCreateStaffId(Toolkit.parseObjForInt(user.getUserId()));
        imageText1.setAuthorName(Toolkit.parseObjForStr(shop.getShopName()));
        imageText1.setCreateTime(createTime);
        imageText1.setFormatCode(1);
        imageText1.setImageTextTile("品牌故事");
        imageText1.setImageTextType(imageTextTypeR1.getId());
        imageText1.setImageUrl(Toolkit.parseObjForStr(shop.getImgUrl()));
        imageText1.setIsValid("Y");
        imageText1.setShareText(Toolkit.parseObjForStr(shop.getShareLan1()));
        imageText1.setShopId(shop.getShopId());
        imageText1.setTypeId(2);
        imageText1.setSort(1);
        imageText1.setIsEdit(1);//不可重新编辑分类
        imageText1.setStatus(0);
        imageText1.setUpperTime(new Date());
        imageText1.setArticleType(1);
        //购买须知
        ImageText imageText2 = new ImageText();
        ImageTextType imageTextType2 = new ImageTextType();
        imageTextType2.setCreateStaffId(Toolkit.parseObjForInt(user.getUserId()));
        imageTextType2.setCreateTime(createTime);
        imageTextType2.setImageUrl(Toolkit.parseObjForStr(shop.getImgUrl()));
        imageTextType2.setShareText("购买须知");
        imageTextType2.setShopId(shop.getShopId());
        imageTextType2.setTypeName("购买须知");
        imageTextType2.setSort(0);
        imageTextType2.setTypeId(2);
        imageTextType2.setIsEdit(1);//不可编辑分类名称
        imageTextType2.setArticleType(2);
        ImageTextType imageTextTypeR2 = imageTextTypeRepository.save(imageTextType2);
        imageText2.setCreateStaffId(Toolkit.parseObjForInt(user.getUserId()));
        imageText2.setAuthorName(Toolkit.parseObjForStr(shop.getShopName()));
        imageText2.setCreateTime(createTime);
        imageText2.setFormatCode(1);
        imageText2.setImageTextTile("购买须知");
        imageText2.setImageTextType(imageTextTypeR2.getId());
        imageText2.setImageUrl(Toolkit.parseObjForStr(shop.getImgUrl()));
        imageText2.setIsValid("Y");
        imageText2.setShareText(Toolkit.parseObjForStr(shop.getShareLan1()));
        imageText2.setShopId(shop.getShopId());
        imageText2.setTypeId(2);
        imageText2.setSort(1);
        imageText2.setIsEdit(1);//不可重新编辑分类
        imageText2.setStatus(0);
        imageText2.setUpperTime(new Date());
        imageText2.setArticleType(2);
        //分销招商
        ImageText imageText3 = new ImageText();
        ImageTextType imageTextType3 = new ImageTextType();
        imageTextType3.setCreateStaffId(Toolkit.parseObjForInt(user.getUserId()));
        imageTextType3.setCreateTime(createTime);
        imageTextType3.setImageUrl(Toolkit.parseObjForStr(shop.getImgUrl()));
        imageTextType3.setShareText("分销招商");
        imageTextType3.setShopId(shop.getShopId());
        imageTextType3.setTypeName("分销招商");
        imageTextType3.setSort(0);
        imageTextType3.setTypeId(2);
        imageTextType3.setIsEdit(1);//不可编辑分类名称
        imageTextType3.setArticleType(3);
        ImageTextType imageTextTypeR3 = imageTextTypeRepository.save(imageTextType3);
        imageText3.setCreateStaffId(Toolkit.parseObjForInt(user.getUserId()));
        imageText3.setAuthorName(Toolkit.parseObjForStr(shop.getShopName()));
        imageText3.setCreateTime(createTime);
        imageText3.setFormatCode(1);
        imageText3.setImageTextTile("分销招商");
        imageText3.setImageTextType(imageTextTypeR3.getId());
        imageText3.setImageUrl(Toolkit.parseObjForStr(shop.getImgUrl()));
        imageText3.setIsValid("Y");
        imageText3.setShareText(Toolkit.parseObjForStr(shop.getShareLan1()));
        imageText3.setShopId(shop.getShopId());
        imageText3.setTypeId(2);
        imageText3.setSort(1);
        imageText3.setIsEdit(1);//不可重新编辑分类
        imageText3.setStatus(0);
        imageText3.setUpperTime(new Date());
        imageText3.setArticleType(3);
        List<ImageText> list = new ArrayList();
        list.add(imageText3);
        list.add(imageText2);
        list.add(imageText1);
        imageTextRepository.save(list);
    }
}
