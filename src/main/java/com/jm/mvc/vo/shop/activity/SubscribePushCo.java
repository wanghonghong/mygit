package com.jm.mvc.vo.shop.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * <p>关注推送</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
@ApiModel(discriminator = "关注推送")
public class SubscribePushCo {

    @ApiModelProperty(value = "是否开通，0不开通，1开通")
    private Integer isOpen;

    @ApiModelProperty(value = "店铺ID")
    private Integer shopId;

    @ApiModelProperty("推送内容")
    private List<SubscribePushVo> subscribePushVos;

}
