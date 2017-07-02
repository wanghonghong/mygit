package com.jm.mvc.vo.shop.enrolmentActivity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>报名活动配置</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/9 15:29
 */
@Data
public class EnrolmentUserQo {

        @ApiModelProperty(value = "活动id")
        private Integer activityId;

        @ApiModelProperty(value = "姓名")
        private String userName;

        @ApiModelProperty(value = "手机号码")
        private String telPhone;

        @ApiModelProperty(value = "当前页")
        private Integer curPage;

        @ApiModelProperty(value = "每页显示条数")
        private Integer pageSize;

        public EnrolmentUserQo(){
                this.curPage = 0;
                this.pageSize = 20;
        }

}
