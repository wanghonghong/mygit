package com.jm.mvc.vo.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/3 15:24
 */
@Data
@ApiModel(discriminator = "官方图文展示列表")
public class ImageTextRos {

        private Integer id;
        @ApiModelProperty("图文标题")
        private String imageTextTile;
        @ApiModelProperty("作者")
        private String authorName;
        @ApiModelProperty("分享语")
        private String shareText;
        @ApiModelProperty("主图")
        private String imageUrl;
        @ApiModelProperty("分类名称")
        private String typeName;
        @ApiModelProperty("格式 /1:仿微信页 2：自由编辑...")
        private Integer formatCode;
        @ApiModelProperty("创建时间")
        private Date createTime;
        @ApiModelProperty("创建人")
        private String userName;
        @ApiModelProperty(value = "上下架")
        private int status;//0上架  1未上架
        @ApiModelProperty(value = "版本标记 0旧版 1新版")
        private  Integer editionSign;
        @ApiModelProperty(value = "文章类型 0默认类型 1 品牌故事 2 购买须知  3 分销招商 ")
        private  Integer articleType;
        @ApiModelProperty("1:不可编辑分类")
        private Integer isEdit;
        private Integer pv;
        private Integer uv;

}
