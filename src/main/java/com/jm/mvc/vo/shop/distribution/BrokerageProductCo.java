package com.jm.mvc.vo.shop.distribution;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>佣金设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@ApiModel(description = "体验装佣金")
public class BrokerageProductCo {

    @ApiModelProperty(value = "主键ID")
    private int id;

    @ApiModelProperty(value = "商品ID")
    private int pid;

    @ApiModelProperty(value = "1级佣金")
    private int oneBrokerage;

    @ApiModelProperty(value = "2级佣金")
    private int twoBrokerage;

    @ApiModelProperty(value = "去佣类型：1:分销商 2：代理商，可多选，多个角色以逗号分割的形式保存 ")
    private String excludeRole;

}
