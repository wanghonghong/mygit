package com.jm.repository.client.dto;

import lombok.Data;

@Data
public class HxToken {
    private String access_token;

    private Long expires_in;

    private String application;

    private String user;
}
