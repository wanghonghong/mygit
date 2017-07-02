package com.jm.mvc.vo.shop.integral;

/**
 * <p>积分信息</p>
 *
 * @author wukf
 * @version 1.1
 * @date 2016/9/18
 */

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.WxUserAccountVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "积分信息")
public class IntegralInfoVo {



    @ApiModelProperty(value = "是否充值，0不开通，1开通")
    private Integer isPay;

    @ApiModelProperty(value = "微信用户帐号")
    protected WxUserAccountVo wxUserAccountVo;

    @ApiModelProperty(value = "单位积分")
    private PageItem<IntegralRecordVo> integralRecordVos;

}
