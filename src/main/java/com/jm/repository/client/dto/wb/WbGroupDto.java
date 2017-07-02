package com.jm.repository.client.dto.wb;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>微博用户分组表</p>
 *
 * @author whh
 * @version latest
 * @date 2017/3/17
 */
@Data
public class WbGroupDto {

    @ApiModelProperty(value = "分组id")
    private Long id;

    @ApiModelProperty(value = "组名")
    private String name;

    @ApiModelProperty(value = "当前组人数")
    private int count;

    @ApiModelProperty(value = "分组类型，0为关键词分组，2为自定义分组；")
    private int ruleType;

    @ApiModelProperty(value = "错误信息")
    private String error;

    @ApiModelProperty(value = "错误代码")
    private String error_code;

    @ApiModelProperty(value = "请求")
    private String request;

    List<WbGroupDto> groups;
}
