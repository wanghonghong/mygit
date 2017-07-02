package com.jm.staticcode.converter.wb;

import com.jm.mvc.vo.wb.WbButtonVo;
import com.jm.repository.po.wb.WbMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @version latest
 * @Author cj
 * @Date 2017/3/7 11:46
 */
public class WbMenuConverter {

    public static void converWbMenu(WbButtonVo[] buttonVos) {
        List<WbMenu> subList = new ArrayList<>();
        for(WbButtonVo buttonVo : buttonVos){
            buttonVo.setId(null);
            WbButtonVo[] subButtons = buttonVo.getSubButton();
            if(null!=subButtons && subButtons.length>0){
                for(WbButtonVo subButton : subButtons ){
                    subButton.setId(null);
                }
            }
        }
    }
}
