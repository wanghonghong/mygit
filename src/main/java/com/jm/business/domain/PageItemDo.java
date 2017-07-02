package com.jm.business.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>多表分页查询领域对象模型</p>
 *
 * @version latest
 * @Author wukf
 * @Date 2016/10/17
 */
@Data
public class PageItemDo {

    @ApiModelProperty(value = "查询列表SQL")
    private String sqlList;

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    public PageItemDo(){}

    public PageItemDo(String sqlList){
        this.sqlList = sqlList;
    }

    public PageItemDo(String sqlList,Integer pageSize,Integer curPage){
        this.sqlList = sqlList;
        this.pageSize = pageSize;
        this.curPage = curPage;
    }

}
