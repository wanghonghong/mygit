package com.jm.repository.client.dto.auth;

import lombok.Data;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/7/7/007
 */
@Data
public class TicketDto {

    private String ticket;

    private Integer expiresIn;

    private long expiresAt;

}
