package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>h5主题模板菜单</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/6 10:26
 */
@Data
public class ThemeMenuRo {

    private Integer id;

    private int pid;

    @ApiModelProperty(value = "H5模板详情内容")
    private String menuName;
}
