package com.jm.mvc.vo.online;

import lombok.Data;

import java.io.Serializable;


@Data
public class Customer implements Serializable{

    private int shopId;

    private Long lastBeatTime;

    private String shopUserAcct;
}
