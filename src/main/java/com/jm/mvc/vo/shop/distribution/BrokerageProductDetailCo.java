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
public class BrokerageProductDetailCo {

    private List<BrokerageProductCo> brokerageProductCos;


}
