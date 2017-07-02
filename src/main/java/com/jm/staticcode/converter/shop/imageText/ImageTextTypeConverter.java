package com.jm.staticcode.converter.shop.imageText;

import com.jm.mvc.vo.shop.imageText.ImageTextTypeCo;
import com.jm.mvc.vo.shop.imageText.ImageTextTypeUo;
import com.jm.repository.po.shop.imageText.ImageTextType;
import org.springframework.beans.BeanUtils;

/**
 * <p>官方图文类型bean转化</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 11:57
 */
public class ImageTextTypeConverter {
    public static ImageTextType toImageTextType(ImageTextTypeCo imageTextTypeCo){
        ImageTextType imageTextType = new ImageTextType();
        BeanUtils.copyProperties(imageTextTypeCo,imageTextType);
        return imageTextType;
    }

    public static ImageTextType toImageTextType(ImageTextType imageTextType, ImageTextTypeUo imageTextTypeUo){
        BeanUtils.copyProperties(imageTextTypeUo,imageTextType);
        return imageTextType;
    }
}
