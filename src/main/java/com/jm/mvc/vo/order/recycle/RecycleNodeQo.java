package com.jm.mvc.vo.order.recycle;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>回收、公益网点</p>
 *
 * @author Administrator
 * @version latest
 * @date 2017/4/6
 */
@Data
public class RecycleNodeQo {

    private Integer id;

    @ApiModelProperty("店铺Id")
    private Integer shopId;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    @ApiModelProperty(value = "type 0：表示回收网点，1：表示公益网点")
    private Integer type;

    public RecycleNodeQo(){
        this.curPage = 0;
        this.pageSize = 10;
    }

}
