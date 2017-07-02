package com.jm.repository.client.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/19/019
 */
@Data
public class CosResourceDto {

    private Integer code;

    private String message;

    private String fileId;

    private CosUrlDto data;


}
