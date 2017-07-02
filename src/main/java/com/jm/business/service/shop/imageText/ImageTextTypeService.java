package com.jm.business.service.shop.imageText;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.imageText.ImageTextTypeQo;
import com.jm.mvc.vo.shop.imageText.ImageTextTypeRos;
import com.jm.mvc.vo.shop.imageText.ImageTextTypeUo;
import com.jm.repository.jpa.shop.imageText.ImageTextTypeRepository;
import com.jm.repository.jpa.shop.ShopRepository;
import com.jm.repository.po.shop.Shop;
import com.jm.repository.po.shop.imageText.ImageTextType;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.imageText.ImageTextTypeConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>官方图文分类</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 17:50
 */
@Service
public class ImageTextTypeService {
    @Autowired
    ImageTextTypeRepository imageTextTypeRepository;
    @Autowired
    ShopRepository shopRepository;

    public List<ImageTextType> findImageTextTypeByShopId(Integer shopId, List typeIds) {
       return  imageTextTypeRepository.findImageTextTypeByShopId(shopId,typeIds);
    }
    public Page<ImageTextType> findAll(Integer shopId, List typeIds,String typeName, Pageable pageable) {
       return  imageTextTypeRepository.findAll(shopId,typeIds,typeName,pageable);
    }
    public Integer findMaxSortByShopId(Integer shopId){
        return imageTextTypeRepository.findMaxSortByShopId(shopId);
    }
    @Transactional
    public void save(ImageTextType imageTextType) {
        imageTextTypeRepository.save(imageTextType);
    }
    @Transactional
    public void delete(Integer id) {
        imageTextTypeRepository.delete(id);
    }

    public ImageTextType findImageTextTypeById(Integer id) {
        return imageTextTypeRepository.findOne(id);
    }

    @Transactional
    public void saveShowFormat(Integer showFormat, Integer shopId, Integer typeId) {
        if(1==typeId){
            shopRepository.saveShowFormatLx(showFormat,shopId);
        }else if(2==typeId){
            shopRepository.saveShowFormatLx(showFormat,shopId);
        }else if(3==typeId){
            shopRepository.saveShowFormatPx(showFormat,shopId);
        }

    }

    @Transactional
    public void saveImgTextType(JmUserSession user, ImageTextType imageTextType) {
        String imageUrl = ImgUtil.substringUrl(imageTextType.getImageUrl());
        imageTextType.setImageUrl(imageUrl);
        if(user != null){
            imageTextType.setShopId(Toolkit.parseObjForInt(user.getShopId()));
            imageTextType.setCreateStaffId(Toolkit.parseObjForInt(user.getUserId()));
        }
        Integer sortMax = this.findMaxSortByShopId(Toolkit.parseObjForInt(user.getShopId()));
        Integer sort = Toolkit.parseObjForInt(sortMax)+1;
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        imageTextType.setSort(sort);
        imageTextType.setCreateTime(createTime);
        this.save(imageTextType);
    }
    @Transactional
    public JmMessage updateImageTextType(ImageTextTypeUo imageTextTypeUo) {
        Integer id = Toolkit.parseObjForInt(imageTextTypeUo.getId());
        ImageTextType imageTextType = this.findImageTextTypeById(id);
        if(imageTextType != null){
            ImageTextType imageTextTypeNew = ImageTextTypeConverter.toImageTextType(imageTextType,imageTextTypeUo);
            String imageUrl = ImgUtil.substringUrl(imageTextTypeNew.getImageUrl());
            imageTextTypeNew.setImageUrl(imageUrl);
            Timestamp updateTime = new Timestamp(System.currentTimeMillis());
            imageTextTypeNew.setUpdateTime(updateTime);
            this.save(imageTextTypeNew);
            return new JmMessage(0,"编辑分类成功!");
        }else{
            return new JmMessage(1,"编辑分类失败，该图文分类已删除!");
        }
    }

    public ImageTextTypeRos getImageTextList(Integer shopId, Integer typeId) {
        int typeid = typeId.intValue();
        List typeIds = new ArrayList();
        if(typeid == 1 || typeid == 2){
            typeIds.add(1);
            typeIds.add(2);
        }else{
            typeIds.add(3);
        }
        List<ImageTextType> imageTextTypeList = this.findImageTextTypeByShopId(shopId,typeIds);
        List<ImageTextTypeUo>  imageTextTypes = new ArrayList<>();
        for (ImageTextType imageTextType: imageTextTypeList) {
            String noImgUrl = Constant.STATIC_URL+"/css/pc/img/no_picture.png";
            String imageUrl = imageTextType.getImageUrl();
            if(!"".equals(imageUrl)&&!noImgUrl.equals(imageUrl)){
                imageTextType.setImageUrl(ImgUtil.appendUrl(imageUrl));
            }
            ImageTextTypeUo imageTextTypeUo = new ImageTextTypeUo();
            BeanUtils.copyProperties(imageTextType,imageTextTypeUo);
            imageTextTypes.add(imageTextTypeUo);
        }
        ImageTextTypeRos i = new ImageTextTypeRos();
        i.setImageTextTypeList(imageTextTypes);
        return i;
    }

    public ImageTextTypeRos getImageTextPage(Integer shopId, Integer typeId, ImageTextTypeQo imageTextTypeQo) {
        Shop shop = shopRepository.findShopByShopId(shopId);
        Sort sort=new Sort(Sort.Direction.ASC, "id","sort");
        PageRequest pageRequest = new PageRequest(imageTextTypeQo.getCurPage(),imageTextTypeQo.getPageSize(),sort);
        String typeName = imageTextTypeQo.getTypeName()==null ? "%%" : "%"+imageTextTypeQo.getTypeName()+"%";
        int typeid = typeId.intValue();
        List typeIds = new ArrayList();
        if(typeid == 1 || typeid == 2){
            typeIds.add(1);
            typeIds.add(2);
        }else{
            typeIds.add(3);
        }
        Page page = this.findAll(shopId,typeIds,typeName,pageRequest);
        List<ImageTextType> imageTextTypeList = page.getContent();//imageTextTypeService.findImageTextTypeByShopId(shopId);
        List<ImageTextTypeUo>  imageTextTypes = new ArrayList<>();
        for (ImageTextType imageTextType: imageTextTypeList) {
            String noImgUrl = Constant.STATIC_URL+"/css/pc/img/no_picture.png";
            String imageUrl = imageTextType.getImageUrl();
            if(!"".equals(imageUrl)&&!noImgUrl.equals(imageUrl)){
                imageUrl = ImgUtil.appendUrl(imageTextType.getImageUrl());
            }
            imageTextType.setImageUrl(imageUrl);
            ImageTextTypeUo imageTextTypeUo = new ImageTextTypeUo();
            BeanUtils.copyProperties(imageTextType,imageTextTypeUo);
            imageTextTypes.add(imageTextTypeUo);
        }
        PageItem pageItem = new PageItem();
        pageItem.setItems(imageTextTypes);
        pageItem.setCount(Toolkit.parseObjForInt(page.getTotalElements()));
        ImageTextTypeRos i = new ImageTextTypeRos();
        i.setImageTextTypes(pageItem);
        if(shop != null){
            if(1==typeId){
                i.setShowFormat(Toolkit.parseObjForInt(shop.getShowFormatXm()));
            }else if(2==typeId){
                i.setShowFormat(Toolkit.parseObjForInt(shop.getShowFormatLx()));
            }else if(3==typeId){
                i.setShowFormat(Toolkit.parseObjForInt(shop.getShowFormatPx()));
            }

        }
        return i;
    }
}
