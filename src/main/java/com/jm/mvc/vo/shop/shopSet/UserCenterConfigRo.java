package com.jm.mvc.vo.shop.shopSet;

import com.jm.repository.po.shop.shopSet.UserCenterConfig;
import com.jm.repository.po.shop.shopSet.UserCenterCustom;
import com.jm.repository.po.shop.shopSet.UserCenterFuns;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>自定义的会员中心设置</p>
 *
 */
@Data
@ApiModel(description = "自定义的会员中心设置")
public class UserCenterConfigRo {

    @ApiModelProperty(value = "会员设置选项")
    private List<UserCenterFunsRo> funs;

    @ApiModelProperty(value = "会员已配置的")
    private  List<UserCenterCustomRo> customs;

    private UserCenterConfig config;
}
