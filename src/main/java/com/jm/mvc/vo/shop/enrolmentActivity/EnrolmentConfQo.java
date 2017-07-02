package com.jm.mvc.vo.shop.enrolmentActivity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>报名活动配置</p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/5/9 15:29
 */
@Data
public class EnrolmentConfQo {

        @ApiModelProperty(value = "当前页")
        private Integer curPage;

        @ApiModelProperty(value = "每页显示条数")
        private Integer pageSize;

        public EnrolmentConfQo(){
                this.curPage = 0;
                this.pageSize = 20;
        }

}
