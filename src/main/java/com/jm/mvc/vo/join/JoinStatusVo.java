package com.jm.mvc.vo.join;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>平台申请状态vo</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/8/30
 */
@Data
@ApiModel(description = "平台申请状态vo")
public class JoinStatusVo {

    private Integer id;

    private Integer userId;

    @ApiModelProperty("软件代理平台加盟类型：1、个人加盟 2、公司加盟")
    private Integer softwareType;

    @ApiModelProperty("运营服务平台加盟类型：1、个人加盟 2、公司加盟")
    private Integer operationType;

    @ApiModelProperty("申请状态：0:未申请审核 1、申请未审核 2、申请被驳回 3、已通过审核")
    private Integer softwareStatus;

    @ApiModelProperty("申请状态：0:未申请审核 1、申请未审核 2、申请被驳回 3、已通过审核")
    private Integer operationStatus;

}
