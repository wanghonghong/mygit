package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by ME on 2016/8/17.
 */
@Data
@ApiModel(description = "基础资料修改")
public class BasicDataUo {

    private Integer userId;

    private Integer sex;

    private String headSculpture; //头像

    private String WebchatNumber; //微信号

}
