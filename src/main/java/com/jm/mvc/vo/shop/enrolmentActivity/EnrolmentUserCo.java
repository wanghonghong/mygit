package com.jm.mvc.vo.shop.enrolmentActivity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>活动报名人员信息</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/8 17:37
 */
@Data
@ApiModel(description = "报名人员")
public class EnrolmentUserCo {

    @ApiModelProperty(value = "活动id")
    private Integer activityId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "手机号码")
    private String telPhone;

    @ApiModelProperty(value = "性别")
    private int sex; //1:男 2：女

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "电邮")
    private String email;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "网址")
    private String webUrl;

    @ApiModelProperty(value = "报名时间")
    private Date createDate;

    public EnrolmentUserCo(){
        this.createDate = new Date();
    }

}
