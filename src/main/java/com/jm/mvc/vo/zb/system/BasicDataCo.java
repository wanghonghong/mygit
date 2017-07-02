package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * Created by ME on 2016/8/17.
 */
@Data
@ApiModel(description = "基础资料新增")
public class BasicDataCo {

    private Integer userId;

    private String userName;

    private String phoneNumber;

    private Integer sex;

    private String email;

    private String headSculpture; //头像

    private String WebchatNumber; //微信号

    private String empno; //员工号

    private String department; //部门

}
