package com.jm.mvc.vo.activities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>二维码创建Co</p>
 *
 * @author hantp
 */
@Data
@ApiModel(description = "二维码创建Vo")
public class QrcodePosterCo {

    private Integer qrcodeId;

    @ApiModelProperty(value = "海报名称")
    private String posterName;

    @ApiModelProperty(value = "格式类型 1启用  0未启用")
    private Integer type;

    @ApiModelProperty(value = "海报地址")
    private String imageSrc;

    @ApiModelProperty(value = "图片上传日期")
    private Date uploadTime;

    @ApiModelProperty(value = "基于左边的位置")
    private Integer leftPosition;

    @ApiModelProperty(value = "基于上面的位置")
    private Integer topPosition;

    @ApiModelProperty(value = "昵称颜色")
    private int fontColor;

    @ApiModelProperty(value = "字体样式")
    private String fontType;

    @ApiModelProperty(value = "用户框基于左边的位置")
    private int userBoxleftPosition;

    @ApiModelProperty(value = "用户框基于头部的位置")
    private int userBoxtopPosition;

    @ApiModelProperty(value = "下级微客数量基于左边的位置")
    private int userCountleftPosition;

    @ApiModelProperty(value = "下级微客数量基于头部的位置")
    private int userCounttopPosition;

    @ApiModelProperty(value = "二维码版式  （默认）A版:0  B版：1 C版：2")
    private int codeFormat;

    @ApiModelProperty(value = "昵称基于左边的位置")
    private int nickNameleftPosition;

    @ApiModelProperty(value = "昵称基于头部的位置")
    private int nickNametopPosition;

    @ApiModelProperty(value = "是否删除 0未删除  1删除")
    private int isDel;

    @ApiModelProperty(value = "轮播选用 0未选 1选中")
    private int isSel;

    @ApiModelProperty(value = "顺序")
    private Integer sort;
}
