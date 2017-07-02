package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>用户</p>
 */
@Data
public class UserRo {

    private Integer id;

    private Integer userId;

    private Integer roleId;

    private String userName;

    private String phoneNumber;

    private String headImg;

    private String roleName;

    private Integer type;

}
