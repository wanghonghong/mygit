package com.jm.mvc.vo.join;

import com.jm.repository.po.system.user.Role;
import com.jm.repository.po.zb.system.ZbDispatchHistory;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * <p>派单加盟Vo</p>
 *
 * @version latest
 * @Author whh
 * @Date 2016/11/1
 */
@Data
@ApiModel(description = "加盟Vo")
public class DispatchVo {

    List<Role> roles;

    List<ZbDispatchHistory> zbDispatchHistoryList;

}
