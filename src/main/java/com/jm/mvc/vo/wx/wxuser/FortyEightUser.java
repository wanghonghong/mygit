package com.jm.mvc.vo.wx.wxuser;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FortyEightUser implements Serializable{

    private Integer userId;
    private Date updateTime;
    private String appid;
    private String openid;


    @Override
    public int hashCode() {
        return openid.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof  FortyEightUser){
            FortyEightUser user = (FortyEightUser) obj;
            if(user!=null){
                if(user.getOpenid().equals(this.openid)){
                    return true;
                }
            }
        }
        return  false;
    }


    public FortyEightUser(String appid,String openid){
        this.appid = appid;
        this.openid = openid;
        this.updateTime = new Date();
    }

}
