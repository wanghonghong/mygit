package com.jm.mvc.vo.qo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class JmRechargeOrderQo {
	
	@ApiModelProperty(value = "当前页")
    private Integer curPage;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    
    private int type;
    
    private Integer userId;

    private String appid;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endtDate;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "电话")
    private String phoneNumber;

    public JmRechargeOrderQo(){
    	this.curPage = 0;
    	this.pageSize = 10;
    }

}
