package com.jm.mvc.vo.online;

import lombok.Data;

@Data
public class ChatLogQo {

    private String from;

    private String to;

    private int currPage;

    private int pageSize;

    public ChatLogQo(){
        this.currPage = 0;
        this.pageSize = 10;
    }

    public ChatLogQo(String from,String to,int currPage,int pageSize){
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.from = from;
        this.to = to;
    }
}
