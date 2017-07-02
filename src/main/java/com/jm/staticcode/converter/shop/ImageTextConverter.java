package com.jm.staticcode.converter.shop;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.imageText.ImageTextCo;
import com.jm.mvc.vo.shop.imageText.ImageTextRo;
import com.jm.mvc.vo.shop.imageText.ImageTextRos;
import com.jm.mvc.vo.shop.imageText.ImageTextUo;
import com.jm.repository.po.shop.imageText.ImageText;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>官方图文bean转化</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 11:57
 */
public class ImageTextConverter {
    public static ImageText toImageText(ImageTextCo.ImgTextCo imgTextCo){
        ImageText imageText = new ImageText();
        BeanUtils.copyProperties(imgTextCo,imageText);
        return imageText;
    }
    public static ImageText toImageText(ImageTextRo imageTextRo){
        ImageText imageText = new ImageText();
        BeanUtils.copyProperties(imageTextRo,imageText);
        return imageText;
    }
    public static ImageText toImageText(ImageTextUo imageTextUo, ImageText imageText){
        ImageTextUo.ImgTextUo imgTextUo = imageTextUo.getImgTextUo();
        BeanUtils.copyProperties(imgTextUo,imageText);
        return imageText;
    }

    public static PageItem<ImageTextRos> toShopResourceRos(Page imageTextpage) {
        String noImgUrl = "../css/pc/img/no_picture.png";
        PageItem<ImageTextRos>  pageItem = new PageItem<>();
        List imageTextList = imageTextpage.getContent();
        List<ImageTextRos> imageTextRosList = new ArrayList();
        if(0<imageTextList.size()){
            for (Object object:imageTextList) {
                ImageTextRos imageTextRos = new ImageTextRos();
                Object[] obj = (Object[]) object;
                ImageText imageText = (ImageText) obj[0];
                String userName = Toolkit.parseObjForStr(obj[1]);
                String typeName = Toolkit.parseObjForStr(obj[2]);
                imageTextRos.setCreateTime(imageText.getCreateTime());
                imageTextRos.setFormatCode(imageText.getFormatCode());
                imageTextRos.setImageTextTile(imageText.getImageTextTile());
                imageTextRos.setAuthorName(imageText.getAuthorName());
                String imageUrl = Toolkit.parseObjForStr(imageText.getImageUrl());
                if(!"".equals(imageUrl)&&!noImgUrl.equals(imageUrl)){
                    /*imageTextRos.setImageUrl(Toolkit.webFileUrl(imageUrl));*/
                    imageTextRos.setImageUrl(ImgUtil.appendUrl(imageUrl,720));
                }
                imageTextRos.setShareText(imageText.getShareText());
                imageTextRos.setTypeName(typeName);
                imageTextRos.setUserName(userName);
                imageTextRos.setId(imageText.getId());
                imageTextRosList.add(imageTextRos);
            }
            pageItem.setItems(imageTextRosList);
            pageItem.setCount(Toolkit.parseObjForInt(imageTextpage.getTotalElements()));
            return pageItem;
        }
        pageItem.setCount(0);
        pageItem.setItems(imageTextRosList);
        return pageItem;
    }
}
