package com.jm.business.service.shop.imageText;

import com.google.zxing.common.BitMatrix;
import com.jm.business.service.shop.ShopResourceService;
import com.jm.business.service.shop.ShopService;
import com.jm.business.service.system.ResourceService;
import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.JmUserSession;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.enrolmentActivity.EnrolmentActivityVo;
import com.jm.mvc.vo.shop.imageText.*;
import com.jm.mvc.vo.shop.resource.ShopResourceRo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.shop.imageText.*;
import com.jm.repository.po.shop.ShopResource;
import com.jm.repository.po.shop.imageText.*;
import com.jm.repository.po.shop.Shop;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.shop.imageText.ImageTextConverter;
import com.jm.staticcode.converter.shop.shopResource.ShopResourceConverter;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import com.jm.staticcode.util.ZxingUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

/**
 * <p>官方图文</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 17:50
 */
@Log4j
@Service
public class ImageTextService {
    @Autowired
    ImageTextRepository imageTextRepository;
    @Autowired
    ImageTextRelateRepository imageTextRelateRepository;
    @Autowired
    ImageTextTypeRepository imageTextTypeRepository;
    @Autowired
    private ShopResourceService shopResourceService;
    @Autowired
    private ImageTextRelateService imageTextRelateService;
    @Autowired
    private ImageTextTypeService imageTextTypeService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private ImageTextTemplateRepository imageTextTemplateRepository;
    @Autowired
    private ThemeMenuRepository themeMenuRepository;
    @Autowired
    private ResourceService resourceService;

    public ImageText findImageTextById(Integer id) {
        return imageTextRepository.findOne(id);
    }

    @Cacheable(value = "image_text_cache", key = "#id")
    public ImageText getCacheImageText(Integer id) {
        return imageTextRepository.findOne(id);
    }

    @Transactional
    public void saveImageText(JmUserSession user, ImageTextCo imageTextCo) {
        ImageTextCo.ImgTextCo imgTextCo = imageTextCo.getImgTextCo();
        ImageText imageText = ImageTextConverter.toImageText(imgTextCo);
        if (user != null) {
            imageText.setShopId(Toolkit.parseObjForInt(user.getShopId()));
            imageText.setCreateStaffId(Toolkit.parseObjForInt(user.getUserId()));
        }
        Timestamp createTime = new Timestamp(System.currentTimeMillis());
        imageText.setCreateTime(createTime);
        imageText.setIsValid("Y");
        imageText.setImageUrl(ImgUtil.substringUrl(imageText.getImageUrl()));
        ImageText imageTextR = save(imageText);
        if (imgTextCo.getAudioResId() != null) {
            Integer audioResId = Toolkit.parseObjForInt(imgTextCo.getAudioResId());// 音频素材id
            String audioName = Toolkit.parseObjForStr(imgTextCo.getAudioTitle());  //本地上传音频重命名
            shopResourceService.updateShopResourceByResName(audioResId, audioName);
        }

        Integer formatCode = imageTextR.getFormatCode();  //3:H5
        if (3 == formatCode) {
            Integer id = Toolkit.parseObjForInt(imageTextR.getId());//官方图文id
            List<ImageTextRelateCo> imageTextRelList = imageTextCo.getImageTextRelateList(); //官方图文H5，按钮
            // h5 按钮保存
            imageTextRelateService.save(id, imageTextRelList);
        }
    }

    @Transactional
    public ImageText save(ImageText imageText) {
        return imageTextRepository.save(imageText);
    }

    @Transactional
    public void delete(List<ImageText> imageTextList) {
        log.info("---------------------  删除图文-service----------------");
        imageTextRepository.save(imageTextList);
        for (ImageText imageText : imageTextList) {
            int formatCode = Toolkit.parseObjForInt(imageText.getFormatCode());
            /*imageTextRepository.delete(imageText);*/
            //h5
            if (3 == formatCode) {
                imageTextRelateRepository.deleteImageTextRelByImageTextId(imageText.getId());
            }
        }
    }

    public List findImageTextList(Integer shopId) {
        return imageTextRepository.findImageTextByShopId(shopId);
    }

    public List findImageTextByShopIdAndTypeId(Integer shopId, Integer typeId) {
        return imageTextRepository.findImageTextByShopIdAndTypeId(shopId, typeId);
    }

    public Integer getCountByTypeId(Integer imageTextType) {
        return imageTextRepository.getCountByTypeId(imageTextType);
    }

    @Transactional
    public void updateImageTextByTypeId(Integer id) {
        imageTextRepository.updateImageTextByTypeId(id);
    }

    public PageItem<ImageTextRos> findAll(HttpServletRequest request, ImageTextQo imageTextQo, Integer typeId)  throws Exception {
        JmUserSession user = (JmUserSession) request.getSession().getAttribute(Constant.SESSION_USER);
        Integer shopId = 0;
        if (null != user) {
            shopId = user.getShopId();
        }
        String imageTextTile = imageTextQo.getImageTextTile() == null ? "%%" : "%" + imageTextQo.getImageTextTile() + "%";
        Integer curPage = imageTextQo.getCurPage() == null ? 0 : imageTextQo.getCurPage();
        Integer pageSize = imageTextQo.getPageSize() == null ? 20 : imageTextQo.getPageSize();
        Sort sort = new Sort(Sort.Direction.DESC, "sort", "id");
        PageRequest pageRequest = new PageRequest(curPage, pageSize, sort);
        String flag = Toolkit.parseObjForStr(imageTextQo.getFlag());
        PageItem<ImageTextRos> pageItem = new PageItem<>();
        List typeIds = new ArrayList();
        if (1 == typeId.intValue() || 2 == typeId.intValue()) {
            typeIds.add(1);
            typeIds.add(2);
        } else if (3 == typeId.intValue()) {
            typeIds.add(3);
        }
        if ("Y".equals(flag)) {
            Page page = imageTextRepository.findAllWap(shopId, imageTextTile, typeIds, pageRequest);
            pageItem = ImageTextConverter.toShopResourceRos(page);
        } else {
            StringBuilder sqlCondition = new StringBuilder();
            sqlCondition.append(JdbcUtil.appendAnd("i.shop_id",shopId));
            sqlCondition.append(JdbcUtil.appendLike("i.image_text_tile",imageTextQo.getImageTextTile()));
            String typeids ="";
            if(1 == typeId.intValue() || 2 == typeId.intValue()){
                typeids = "1,2";
            }else if(3 == typeId.intValue()){
                typeids ="3";
            }
            sqlCondition.append(JdbcUtil.appendIn("i.type_id",typeids));
            String sql = " SELECT i.*,u.user_name,t.type_name,ifnull(m.pv,0) as pv,ifnull(m.uv,0) as uv";
            String fromSql = " FROM image_text i left join  user u on i.create_staff_id = u.user_id " +
                    " left join image_text_type t on i.image_text_type = t.id" +
                    " left join (SELECT pid,ifnull(count(1),0) AS pv,ifnull(count(DISTINCT user_id),0) AS uv FROM jm_visit " +
                    " WHERE shop_id = " +shopId+
                    " and type=2 and ifnull(plat_form,0)=0 GROUP BY pid) m on i.id = m.pid " +
                    " WHERE 1=1 AND i.is_valid = 'Y' "+sqlCondition+" order by i.sort desc , i.id desc";
            pageItem = jdbcUtil.queryPageItem(sql+fromSql,imageTextQo.getCurPage(),imageTextQo.getPageSize(),ImageTextRos.class);
        }
        return pageItem;
    }

    public PageItem<ImageTextRos> findAllWap(Integer shopId, ImageTextQo imageTextQo, Integer typeId, Integer imageTextTypeId) {

        String imageTextTile = imageTextQo.getImageTextTile() == null ? "%%" : "%" + imageTextQo.getImageTextTile() + "%";
        Integer curPage = imageTextQo.getCurPage() == null ? 0 : imageTextQo.getCurPage();
        Integer pageSize = imageTextQo.getPageSize() == null ? 20 : imageTextQo.getPageSize();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        PageRequest pageRequest = new PageRequest(curPage, pageSize, sort);
        PageItem<ImageTextRos> pageItem = new PageItem<>();
        List typeIds = new ArrayList();
        if (1 == typeId.intValue() || 2 == typeId.intValue()) {
            typeIds.add(1);
            typeIds.add(2);
        } else if (3 == typeId.intValue()) {
            typeIds.add(3);
        }
        if (0 != imageTextTypeId) {
            Page page = imageTextRepository.findAllByImageTypeId(imageTextTypeId, pageRequest);
            pageItem = ImageTextConverter.toShopResourceRos(page);
        } else {
            Page page = imageTextRepository.findAllWap(shopId, imageTextTile, typeIds, pageRequest);
            pageItem = ImageTextConverter.toShopResourceRos(page);
        }

        return pageItem;
    }

    @ApiOperation("获取项目图文")
    public ImageTextRo getImageText(Integer id, Integer shopId) throws IOException {
        ImageText imageText = this.findImageTextById(id);
        Integer formatCode = Toolkit.parseObjForInt(imageText.getFormatCode());
        String imageUrl = Toolkit.parseObjForStr(imageText.getImageUrl());
        String noImgUrl = Constant.STATIC_URL + "/css/pc/img/no_picture.png";
        if (!"".equals(imageUrl) && !noImgUrl.equals(imageUrl)) {
            imageText.setImageUrl(ImgUtil.appendUrl(imageUrl));
        }
        ImageTextRo imageTextRo = new ImageTextRo();
        ImageTextRo.ImgTextRo imgTextRo = new ImageTextRo().new ImgTextRo();
        BeanUtils.copyProperties(imageText, imgTextRo);
        imageTextRo.setImgTextRo(imgTextRo); // return 官方图文
        int typeId = Toolkit.parseObjForInt(imageText.getTypeId());
        List typeIds = new ArrayList();
        if (typeId == 1 || typeId == 2) {
            typeIds.add(1);
            typeIds.add(2);
        } else {
            typeIds.add(3);
        }
        List<ImageTextType> imageTextTypes = imageTextTypeService.findImageTextTypeByShopId(shopId, typeIds);
        List imageTextTypesNew = new ArrayList();
        if (0 < imageTextTypes.size()) {
            for (ImageTextType imageTextTypeOld : imageTextTypes) {
                ImageTextRo.ImgTextTypeRo imageTextTypeRoNew = new ImageTextRo().new ImgTextTypeRo();
                BeanUtils.copyProperties(imageTextTypeOld, imageTextTypeRoNew);
                imageTextTypesNew.add(imageTextTypeRoNew);
            }
        }
        imageTextRo.setImageTextTypes(imageTextTypesNew); // return 官方图文分类

        ShopResourceRo audioResRo = new ShopResourceRo(); //音频资源
        if (imageText.getAudioResId() != null) {
            Integer audioResId = Toolkit.parseObjForInt(imageText.getAudioResId());
            ShopResource shopRes = shopResourceService.findShopResourceById(audioResId);
            BeanUtils.copyProperties(shopRes, audioResRo);
            audioResRo.setResUrl(Constant.COS_PATH + audioResRo.getResUrl());
        }
        imageTextRo.setAudioRes(audioResRo); // 图文：返回音频信息

        ShopResourceRo videoResRo = new ShopResourceRo();//视频封面图片资源
        if (imageText.getVideoImg() != null) {
            Integer vImg = Toolkit.parseObjForInt(imageText.getVideoImg());
            ShopResource shopRes = shopResourceService.findShopResourceById(vImg);
            BeanUtils.copyProperties(shopRes, videoResRo);
            videoResRo.setResUrl(ImgUtil.appendUrl(videoResRo.getResUrl()));
        }
        imageTextRo.setVideoRes(videoResRo);//图文：返回视频封面

        ShopResourceRo vResRo = new ShopResourceRo(); //视频资源
        if (imageText.getVideoId() != null) {
            Integer videoId = Toolkit.parseObjForInt(imageText.getVideoId());
            ShopResource shopRes = shopResourceService.findShopResourceById(videoId);
            BeanUtils.copyProperties(shopRes, vResRo);
            vResRo.setResUrl(Constant.COS_PATH + vResRo.getResUrl());
        }
        imageTextRo.setVideoResoure(vResRo);//图文：返回视频资源地址

        // 官方图文：类型H5时候返回图片素材信息
        if (3 == formatCode) {
            //h5 图库
            List<ShopResourceRo> shopResRoList = new ArrayList<>();
            String imgResIds = imageText.getImgResIds();
            if (!"".equals(imgResIds)) {
                List list = jdbcUtil.queryList("select * from shop_resource where id in(" + imgResIds + ") order by field(" + "id," + imgResIds + ")");
                shopResRoList = ShopResourceConverter.p2v(list);
            }
            imageTextRo.setImgResList(shopResRoList);
            List<ImageTextRelate> imageTextRelList = imageTextRelateService.findImageTextRelateByImageTextId(id);
            List<ImageTextRelateRo> imageTextRelRos = new ArrayList<>();
            for (ImageTextRelate imgTextRel : imageTextRelList) {
                ImageTextRelateRo imageTextRelRo = new ImageTextRelateRo();
                BeanUtils.copyProperties(imgTextRel, imageTextRelRo);
                imageTextRelRos.add(imageTextRelRo);
            }
            imageTextRo.setButtonList(imageTextRelRos);
        }
        return imageTextRo;
    }

    @Transactional
    public JmMessage updateImageText(JmUserSession user, ImageTextUo imageTextUo) {
        Integer id = Toolkit.parseObjForInt(imageTextUo.getImgTextUo().getId());
        ImageText imageText = this.findImageTextById(id);
        if (imageText != null) {
            Integer formatCode = imageTextUo.getImgTextUo().getFormatCode();  //3:H5
            if (imageTextUo.getImgTextUo().getAudioResId() != null) {
                Integer audioResId = Toolkit.parseObjForInt(imageTextUo.getImgTextUo().getAudioResId());// 音频素材id
                String audioName = Toolkit.parseObjForStr(imageTextUo.getImgTextUo().getAudioTitle());  //本地上传音频重命名
                shopResourceService.updateShopResourceByResName(audioResId, audioName);
            }
            ImageText imageTextNew = ImageTextConverter.toImageText(imageTextUo, imageText);
            String imageUrl = ImgUtil.substringUrl(imageTextNew.getImageUrl());
            imageTextNew.setImageUrl(imageUrl);
            Timestamp updateTime = new Timestamp(System.currentTimeMillis());
            imageTextNew.setUpdateTime(updateTime);
            ImageText imageTextR = this.save(imageTextNew);
            if (3 == formatCode.intValue()) {
                List<ImageTextRelateUo> imageTextRelateUos = imageTextUo.getImageTextRelateList();
                if (imageTextRelateUos != null) {
                    if (0 < imageTextRelateUos.size()) {
                        imageTextRelateService.update(imageTextRelateUos, id);
                    }
                }
            } else {
                imageTextRelateService.deleteByImageTextId(id);
            }

            return new JmMessage(0, "编辑成功!");
        } else {
            return new JmMessage(0, "编辑失败，该图文已删除!");
        }

    }

    @Transactional
    public void delImageText(String ids, Integer shopId) throws Exception {
        List<ImageText> imageTextList = new ArrayList();
        if (!"".equals(ids)) {
            String[] str = ids.split(",");
            for (Object obj : str) {
                Integer id = Toolkit.parseObjForInt(obj);
                ImageText imageText = this.findImageTextById(id);
                if (!imageText.getShopId().equals(shopId)) {
                    throw new Exception("无权限删除");
                } else {
                    imageText.setIsValid("N");
                    imageTextList.add(imageText);
                }
            }
        }
        this.delete(imageTextList);
    }

    public ImageTextWapRo getWapImageTextList(Integer shopId, ImageTextQo imageTextQo, Integer typeId) {
        Shop shop = shopService.findShopById(shopId);
        Integer imageTextTypeId = Toolkit.parseObjForInt(imageTextQo.getImageTextTypeId());
        PageItem<ImageTextRos> pageItem = this.findAllWap(shopId, imageTextQo, typeId, imageTextTypeId);
        ImageTextWapRo imageTextWapRo = new ImageTextWapRo();
        if (typeId == 0) {
            ImageTextType imageTextType = imageTextTypeService.findImageTextTypeById(imageTextTypeId);
            typeId = imageTextType.getTypeId();
        }
        if (0 != typeId) {
            imageTextWapRo.setImageTypeId(typeId);
        }
        if (1 == typeId) {
            imageTextWapRo.setShowFormat(Toolkit.parseObjForInt(shop.getShowFormatLx()));
        } else if (2 == typeId) {
            imageTextWapRo.setShowFormat(Toolkit.parseObjForInt(shop.getShowFormatLx()));
        } else if (3 == typeId) {
            imageTextWapRo.setShowFormat(Toolkit.parseObjForInt(shop.getShowFormatPx()));
        }
        imageTextWapRo.setPageItems(pageItem);
        return imageTextWapRo;
    }

    public JmMessage updateImageTextStatus(Integer id, Integer status) {
        ImageText imageText = this.findImageTextById(Toolkit.parseObjForInt(id));
        if (imageText == null) {
            return new JmMessage(1, "找不到该图文!");
        } else {
            if (0 == status.intValue()) {
                imageText.setStatus(status);
                imageText.setUpperTime(new Date()); // 上架时间
                imageTextRepository.save(imageText);
                return new JmMessage(0, "上架成功!");
            } else if (1 == status.intValue()) {
                imageText.setStatus(status);
                imageTextRepository.save(imageText);
                return new JmMessage(0, "下架成功!");
            }
        }
        return null;
    }

    @ApiOperation("新增h5模板")
    @Transactional
    public JmMessage saveH5template(ImageTextTemplateCo imageTextTemplateCo, Integer shopId) {
        Map map = new HashMap();
        ImageTextTemplate template = new ImageTextTemplate();
        String menuName = imageTextTemplateCo.getMenuName();
        int menuPid = imageTextTemplateCo.getPid();
        ThemeMenu themeMenu = new ThemeMenu();
        themeMenu.setPid(menuPid);
        themeMenu.setMenuName(menuName);
        themeMenu.setIsValid("Y");
        themeMenu.setShopId(shopId);
        themeMenu = themeMenuRepository.save(themeMenu);
        if (themeMenu != null) {
            int menuId = themeMenu.getId();
            imageTextTemplateCo.setMenuId(menuId);
            imageTextTemplateCo.setShopId(shopId);
            BeanUtils.copyProperties(imageTextTemplateCo, template);
            imageTextTemplateRepository.save(template);
            return new JmMessage(0, "新增模板成功!!");
        } else {
            return new JmMessage(1, "新增模板失败!!");
        }
    }

    @ApiOperation("删除自定义主题模板")
    @Transactional
    public JmMessage delImageTextTemplate(Integer id, Integer shopId) {
        ThemeMenu themeMenu = themeMenuRepository.findOne(id);
        if (themeMenu != null) {
            themeMenuRepository.delete(id);
            imageTextTemplateRepository.updateImageTextTemplateByMenuId(id);
            return new JmMessage(0, "主题菜单删除成功!!");
        }
        return new JmMessage(1, "主题菜单已经删除!!");
    }

    @ApiOperation("获取主题模板")
    public ImageTextTemplateRo getImageTextTemplate(Integer menuId, Integer shopId) {
        ImageTextTemplate imageTextTemplate = imageTextTemplateRepository.findImageTextTemplateByMenuId(menuId);
        ImageTextTemplateRo imageTextTemplateRo = new ImageTextTemplateRo();
        BeanUtils.copyProperties(imageTextTemplate, imageTextTemplateRo);
        return imageTextTemplateRo;
    }

    @ApiOperation("获取主题菜单")
    public List<ThemeMenuRo> getThemeMenu(Integer shopId) {
        List<ThemeMenu> list = themeMenuRepository.findThemeMenuByShopId(shopId);
        List<ThemeMenuRo> listRo = new ArrayList();
        for (ThemeMenu themeMenu : list) {
            ThemeMenuRo themeMenuRo = new ThemeMenuRo();
            BeanUtils.copyProperties(themeMenu, themeMenuRo);
            listRo.add(themeMenuRo);
        }
        return listRo;
    }

    public JmMessage getQrcoce(String url) throws Exception {
//生成二维码
        BitMatrix bi = ZxingUtil.toQRCodeMatrix(url, null, null);
        BufferedImage ImageOne = ZxingUtil.writeToFile(bi);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(ImageOne, "jpg", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        JmMessage msg = resourceService.uploadFile(is);
        return msg;
    }

 /*   @ApiOperation("查询条件")
    public Specification<ImageText> getSpec(final ImageTextQo imageTextQo, final Integer finalShopId){

        Specification<ImageText> spec = new Specification<ImageText> () {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {

                Predicate predicate = cb.conjunction();
                List<Predicate> predicates = new ArrayList<>();
                if(null != imageTextQo){
                    if(!StringUtils.isEmpty(imageTextQo.getImageTextTile())){
                        predicates.add(cb.equal(root.get("imageTextTile").as(String.class), imageTextQo.getImageTextTile()));
                    }
                }

                predicates.add(cb.equal(root.get("shopId").as(Integer.class), finalShopId));
                predicates.add(cb.equal(root.get("isValid").as(String.class), "Y"));
                predicate.getExpressions().addAll(predicates);
                return predicate;
            }

        };
        return spec;
    }*/


}
