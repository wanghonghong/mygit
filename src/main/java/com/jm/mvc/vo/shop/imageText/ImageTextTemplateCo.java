package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Lob;
import java.util.Date;

/**
 * <p>h5模板</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/4/6 10:19
 */
@Data
@ApiModel(description = "h5模板")
public class ImageTextTemplateCo {
    @Lob
    @ApiModelProperty(value = "H5模板详情内容")
    private String detailJson;

    @ApiModelProperty("是否生效 Y/N")
    private String isValid;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "主题菜单名称")
    private String menuName;

    @ApiModelProperty(value = "主题菜单id")
    private Integer menuId;

    @ApiModelProperty(value = "主题菜单父类id")
    private int pid;

    @ApiModelProperty(value = "店铺id")
    private Integer shopId;

    public ImageTextTemplateCo(){
        this.isValid = "Y";
        this.createDate = new Date();
    }

}
