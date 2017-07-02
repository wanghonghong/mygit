package com.jm.repository.po.shop.activity.question;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>活动</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@Entity
@ApiModel(description = "问卷调查问题")
public class ShopQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty(value = "商家id")
    private Integer shopSignId;

    @ApiModelProperty(value = "分组id")
    private Integer groupId;

    @ApiModelProperty(value = "问题")
    private String question;

    @ApiModelProperty(value = "问题类型0单选  1多选")
    private Integer type;

    @ApiModelProperty(value = "答案1")
    private String answer1;

    @ApiModelProperty(value = "答案2")
    private String answer2;

    @ApiModelProperty(value = "答案3")
    private String answer3;

    @ApiModelProperty(value = "答案4")
    private String answer4;

    @ApiModelProperty(value = "答案5")
    private String answer5;

    @ApiModelProperty(value = "答案6")
    private String answer6;

    @ApiModelProperty(value = "图片1")
    private String image1;

    @ApiModelProperty(value = "图片2")
    private String image2;

    @ApiModelProperty(value = "图片3")
    private String image3;

    @ApiModelProperty(value = "图片4")
    private String image4;

    @ApiModelProperty(value = "图片5")
    private String image5;

    @ApiModelProperty(value = "图片6")
    private String image6;

    @ApiModelProperty(value = "二维码图片")
    private String qrcodeUrl;

    @ApiModelProperty(value = "状态")
    private int status;//0正常 9删除

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public ShopQuestion(){
        this.createTime=new Date();
    }
}
