package com.jm.mvc.vo.shop.imageText;

import com.jm.mvc.vo.PageItem;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 15:24
 */
@Data
@ApiModel(discriminator = "官方图文展示列表")
public class ImageTextWapRo {
        PageItem<ImageTextRos> pageItems;
        private Integer showFormat;
        private Integer imageTypeId; // 图文分类id
}
