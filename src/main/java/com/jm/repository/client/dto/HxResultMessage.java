package com.jm.repository.client.dto;

import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

@Data
public class HxResultMessage {

    private String action;
    private String application;
    private String uri;
    private JSONArray entities;
    private JSONObject data;
    private Long timestamp;
    private Integer duration;
    private String organization;
    private String applicationName;
    private JSONObject params;
    private String path;
    private String message;
    private String code;

    private String error;
    private String exception;
    private String error_description;
}
