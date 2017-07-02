package com.jm.mvc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>查询列表返回</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/11
 */
@Data
@ApiModel(description = "查询列表")
public class PageItemExt<T,E>  {

    @ApiModelProperty(value = "数量")
    private int count;

    @ApiModelProperty(value = "列表")
    private List<T> items;

    private E ext;

}
