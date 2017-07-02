package com.jm.repository.po.shop.imageText;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>官方图文分类</p>
 *
 * @version latest
 * @Author cj
 * @Date 2016/8/2 17:49
 */
@Data
@Entity
@ApiModel(discriminator = "官方图文分类")
public class ImageTextType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("店铺id")
    private Integer shopId;

    @ApiModelProperty("1:项目图文分类 2：乐享图文分类 3:培训通知")
    private Integer typeId;

    @ApiModelProperty("分类名称")
    private String typeName;

    @ApiModelProperty("分享引导语")
    private String shareText;

    @ApiModelProperty("分类主图")
    private String imageUrl;

    @ApiModelProperty("创建人员")
    private Integer createStaffId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后一次编辑时间")
    private Date updateTime;

    @ApiModelProperty("排序")
    private  Integer sort;

    @ApiModelProperty("1:不可编辑分类名称")
    private Integer isEdit;

    @ApiModelProperty(value = "文章类型 0默认类型 1 品牌故事 2 购买须知  3 分销招商 ")
    private  Integer articleType;
}
