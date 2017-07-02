package com.jm.mvc.vo.shop.shopSet;

import com.jm.repository.po.shop.shopSet.UserCenterConfig;
import com.jm.repository.po.shop.shopSet.UserCenterCustom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import java.util.List;

/**
 * <p>自定义的会员中心设置</p>
 *
 */
@Data
@ApiModel(description = "自定义的会员中心设置")
public class UserCenterCustomCo {

    @ApiModelProperty(value = "会员设置选项Id")
    private List<UserCenterCustom> customs;

    private UserCenterConfig config;

}
