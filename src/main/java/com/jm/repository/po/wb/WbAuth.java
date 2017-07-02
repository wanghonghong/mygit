package com.jm.repository.po.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * <p>微博第三方平台授权应用信息</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/2/28 9:41
 */
@Data
@Entity
@ApiModel(description = "微博第三方平台授权应用信息")
public class WbAuth {
    @Id
    @ApiModelProperty(value = "授权方AppKey")
    private String appKey;
    @ApiModelProperty(value = "授权方appSecret")
    private String appSecret;
    @ApiModelProperty(value = "备注")
    private String  remark;
}
