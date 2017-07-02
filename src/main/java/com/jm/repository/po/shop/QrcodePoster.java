package com.jm.repository.po.shop;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * <p>二维码海报设置</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6/17
 */
@Data
@Entity
public class QrcodePoster {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer qrcodeId;

    @ApiModelProperty(value = "海报名称")
    private String posterName;
    
    @ApiModelProperty(value = "格式类型 1启用  0未启用")
    private Integer type;

    @ApiModelProperty(value = "海报地址")
    private String imageSrc;

    private Integer shopId;

    private String appId;

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

    @ApiModelProperty(value = "二维码版式  （默认）A版:0  B版：1 C版：2 ")
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
