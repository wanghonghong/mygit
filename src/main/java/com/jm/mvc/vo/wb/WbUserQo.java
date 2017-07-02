package com.jm.mvc.vo.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>微博用户</p>
 */
@Data
public class WbUserQo {

    @ApiModelProperty(value = "每页大小")
    private Integer pageSize;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    private int lastType;

    public WbUserQo(){
        this.curPage = 0;
        this.pageSize = 20;
    }

}
