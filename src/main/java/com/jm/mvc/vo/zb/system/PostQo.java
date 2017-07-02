package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>岗位Qo</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class PostQo {

    @ApiModelProperty(value = "创建人id")
    private Integer userId;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public PostQo() {
        this.curPage = 0;
        this.pageSize = 10;
    }

}
