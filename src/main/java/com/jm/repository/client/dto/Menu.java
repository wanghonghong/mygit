package com.jm.repository.client.dto;

import com.jm.mvc.vo.system.ButtonVo;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/19/019
 */
@Data
public class Menu {
    private List<ButtonVo> button;
}
