package com.jm.repository.client.dto.wb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * <p>微博用户粉丝uid表</p>
 *
 * @author whh
 * @version latest
 * @date 2017/2/27
 */
@Data
@ApiModel(description = "微博用户粉丝uidDto")
public class WbUidDto {

    @ApiModelProperty(value = "总订阅用户数")
    private Integer total;

    @ApiModelProperty(value = "本次拉取的用户数，最大值为10000")
    private Integer count;

    @ApiModelProperty(value = "拉取列表的后一个用户的UID")
    private Long next_uid;

    @ApiModelProperty(value = "存uids列表")
    private Map<String,List<String>> data;

    @ApiModelProperty(value = "错误信息")
    private String error;

    @ApiModelProperty(value = "错误代码")
    private String error_code;

    @ApiModelProperty(value = "请求")
    private String request;
}
