package com.jm.repository.client.dto.wb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>微博用户分组表</p>
 *
 * @author whh
 * @version latest
 * @date 2017/3/17
 */
@Data
public class WbUserGroupDto {

    @ApiModelProperty(value = "微博用户所在分组id")
    private Long groupid;

    @ApiModelProperty(value = "微博用户所在分组id")
    private Long id;

    @ApiModelProperty(value = "分组名")
    private String name;

    @ApiModelProperty(value = "返现信息提示")
    private String error;

    @ApiModelProperty(value = "错误代码")
    private String error_code;

    @ApiModelProperty(value = "请求")
    private String request;

    @ApiModelProperty(value = "分组")
    private Map<String,String> group;

}
