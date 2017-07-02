package com.jm.repository.po.shop.brokerage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * <p>佣金设置</p>
 *
 * @author zww
 * @version latest
 * @date 2016-9-12 11:47:32
 */
@Data
@Entity
@ApiModel(description = "体验装佣金")
public class BrokerageProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
