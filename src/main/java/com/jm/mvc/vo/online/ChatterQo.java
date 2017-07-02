package com.jm.mvc.vo.online;

import lombok.Data;

@Data
public class ChatterQo {

    private String customer;

    private String service;

    private int currPage;

    private int pageSize;

    public ChatterQo(){
        this.currPage = 0;
        this.pageSize =1;
    }


}
