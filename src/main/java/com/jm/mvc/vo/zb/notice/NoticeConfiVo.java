package com.jm.mvc.vo.zb.notice;


import com.jm.repository.po.zb.system.ZbDepartment;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * Created by ME on 2016/8/17.
 */
@Data
public class NoticeConfiVo {

    @ApiModelProperty(value = "消息可查看部们列表")
    private String departments;

    @ApiModelProperty(value = "部们列表")
    private List<ZbDepartment> zbDepartmentList;

}
