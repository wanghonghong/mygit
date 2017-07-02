package com.jm.mvc.vo.shop.imageText;

import com.jm.mvc.vo.PageItem;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 15:05
 */
@Data
public class ImageTextTypeRos {
     private PageItem<ImageTextTypeUo> imageTextTypes;
     private List<ImageTextTypeUo> imageTextTypeList;
     private Integer showFormat;
}
