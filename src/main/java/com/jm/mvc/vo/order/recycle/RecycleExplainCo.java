package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>回收说明</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */

@Data
@ApiModel("回收说明")
public class RecycleExplainCo {

    @ApiModelProperty("店铺Id")
    private Integer  shopId;

    @ApiModelProperty("图片地址")
    private String imgUrl;

    @ApiModelProperty("启用模块1")
    private String module1;

    @ApiModelProperty("启用模块2")
    private String module2;

    @ApiModelProperty("启用的模块1； 0未选 1选中")
    private int usingModule1;

    @ApiModelProperty("启用的模块2； 0未选 1选中")
    private int usingModule2;

    @ApiModelProperty("描述")
    private String node;

    @ApiModelProperty("描述")
    private String node1;
}
