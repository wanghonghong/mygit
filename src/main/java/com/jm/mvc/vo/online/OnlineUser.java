package com.jm.mvc.vo.online;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class OnlineUser implements Comparable,Serializable{

    //用户id
    private Integer userId;
    //权重
    private Integer weight = 0;
    //最后一跳时间
    private Long lastBeatTime;

    private String hxAcct;

    private Set<String> customers;

    public void plus(){
        this.weight++;
    }

    public void minus(){
        this.weight--;
    }

    public OnlineUser(){

    }

    public boolean equals(Object obj){
        if(obj instanceof OnlineUser){
            return false;
        }
        try{
            OnlineUser user = (OnlineUser)obj;
            if(user.getUserId()==this.getUserId()){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            return false;
        }

    }




    @Override
    public int compareTo(Object o) {
        OnlineUser user = (OnlineUser)o;
        return user.getWeight() - this.weight;
    }
}
