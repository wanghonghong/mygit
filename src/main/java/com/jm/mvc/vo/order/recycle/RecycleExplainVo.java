package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * <p>回收说明</p>
 *
 * @author liangrs
 * @version latest
 * @date 2017/4/6
 */

@Data
@ApiModel("回收说明")
public class RecycleExplainVo {

    private Integer id;

    @ApiModelProperty("图片地址")
    private String imgUrl;

    @ApiModelProperty("描述")
    private String explain;
}
