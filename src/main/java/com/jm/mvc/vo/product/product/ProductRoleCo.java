package com.jm.mvc.vo.product.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>商品</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/25
 */
@Data
@ApiModel(description = "商品角色限购表")
public class ProductRoleCo {

    @ApiModelProperty(value = "商品标识")
    private Integer pid;

    @ApiModelProperty(value = "代理商角色 0：所以用户  1:A档  2：b档  3：c档 4：d档  分销商角色 5:A档  6：b档  7：c档 8:分享客 ")
    private int agentRole ;

    @ApiModelProperty(value = "限购数量")
    private int limitCount;
}
