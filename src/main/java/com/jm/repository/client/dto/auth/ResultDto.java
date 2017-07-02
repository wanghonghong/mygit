package com.jm.repository.client.dto.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/24/024
 */
@Data
public class ResultDto<T>  implements Serializable {

    private int errcode;

    private String errmsg;

    private T data;
}
