package com.jm.mvc.vo.zb.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>角色Qo</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class RoleQo {

    @ApiModelProperty(value = "创建人id")
    private Integer userId;

    @ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;

    public RoleQo() {
        this.curPage = 0;
        this.pageSize = 20;
    }

}
