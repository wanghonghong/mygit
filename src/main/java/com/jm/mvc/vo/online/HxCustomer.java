package com.jm.mvc.vo.online;

import lombok.Data;

import java.io.Serializable;

@Data
public class HxCustomer implements Serializable{

    public String customer;
    public String shopAcc;
    public Integer shopId;
    public String scretKey;
}
