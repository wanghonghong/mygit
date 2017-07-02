package com.jm.mvc.vo.shop.enrolmentActivity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>报名活动</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/9 15:29
 */
@Data
public class EnrolmentActivityQo {

        @ApiModelProperty(value = "活动名称")
        private String activityName;
        
        @ApiModelProperty(value = "创建开始时间")
        private Date createStartDate;

        @ApiModelProperty(value = "创建结束时间")
        private Date createEndDate;

        @ApiModelProperty(value = "活动开始时间")
        private Date startDate;

        @ApiModelProperty(value = "活动结束时间")
        private Date endDate;

        @ApiModelProperty(value = "状态 -1:全部 0:未开始 1：执行中 2：已结束 3：删除 ")
        private int status;

        @ApiModelProperty(value = "当前页")
        private Integer curPage;

        @ApiModelProperty(value = "每页显示条数")
        private Integer pageSize;

        public EnrolmentActivityQo(){
                this.curPage = 0;
                this.pageSize = 20;
        }

}
