package com.jm.repository.client.dto.wb;

import com.jm.mvc.vo.wb.WbButtonVo;
import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/6 17:07
 */
@Data
public class WbMenuVo {
    private List<WbButtonVo> button;
}
