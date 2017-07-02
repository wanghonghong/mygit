package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by ME on 2016/8/17.
 */
@Data
public class UserForUpdateUo {


    private Integer userId;

    private Integer status; //审核状态：1,未审核 2，二次审核 3，已通过

    @ApiModelProperty("审核意见")
    private String reviewAdvice;

    private Date reviewTime;

    public UserForUpdateUo(){
        this.reviewTime = new Date();
    }
}
