package com.jm.mvc.vo.order.recycle;

import com.jm.repository.po.order.recycle.RecycleWeightConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>奖励方式配置</p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
public class RecycleWeightConfigVo {

    private String weight;

    @ApiModelProperty("上级奖励数量")
    private int upperCount;

    @ApiModelProperty("本人奖励数量")
    private int userCount;

}
