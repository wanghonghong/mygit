package com.jm.mvc.vo.activities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * <p>二维码创建Vo</p>
 *
 * @author hantp
 * @version latest
 * @date 2016/6/22
 */
@Data
@ApiModel(description = "二维码创建Vo")
public class QrcodePosterForCreateVo {
	
	private Integer qrcodeId;

    private Integer type;
	 /**
     * 海报名称
     */
    private String posterName;
    
    /**
     * 海报名称
     */
    private String imageSrc;

    /**
     * 店铺编号
     */
    private Integer shopId;


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

    @ApiModelProperty(value = "二维码版式  （默认）A版:0  B版：1 ")
    private int codeFormat;

    @ApiModelProperty(value = "昵称基于左边的位置")
    private int nickNameleftPosition;

    @ApiModelProperty(value = "昵称基于头部的位置")
    private int nickNametopPosition;

    @ApiModelProperty(value = "上传日期")
    private Date uploadTime;

}
