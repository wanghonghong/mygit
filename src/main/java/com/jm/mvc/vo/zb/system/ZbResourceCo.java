package com.jm.mvc.vo.zb.system;

import lombok.Data;

import java.util.List;

/**
 * <p>权限Co</p>
 *
 * @author whh
 * @version latest
 * @date 2016/8/24
 */
@Data
public class ZbResourceCo {

    private List<Integer> resourceIds;

    private Integer roleId;


}
