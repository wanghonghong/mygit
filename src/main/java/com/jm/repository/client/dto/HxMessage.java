package com.jm.repository.client.dto;

import lombok.Data;

import java.util.Map;

@Data
public class HxMessage {

    private String targetType;
    private String[] target;
    private Map<String,String> msg;
    private String from;
    private Map<String,String> ext;
}
