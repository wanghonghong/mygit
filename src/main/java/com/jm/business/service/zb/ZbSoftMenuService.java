package com.jm.business.service.zb;

import com.jm.mvc.vo.JmMessage;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.*;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.zb.system.*;
import com.jm.repository.po.zb.system.ZbDepartment;
import com.jm.repository.po.zb.system.ZbLoginLog;
import com.jm.repository.po.zb.system.ZbPost;
import com.jm.repository.po.zb.system.ZbSoftMenu;
import com.jm.repository.po.zb.user.ZbRole;
import com.jm.repository.po.zb.user.ZbUser;
import com.jm.repository.po.zb.user.ZbUserRole;
import com.jm.staticcode.constant.Constant;
import com.jm.staticcode.converter.zb.ZbUserConverter;
import com.jm.staticcode.util.AddressUtil;
import com.jm.staticcode.util.ImgUtil;
import com.jm.staticcode.util.Toolkit;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Constraint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 软件版本
 */
@Service
public class ZbSoftMenuService {
    @Autowired
    private ZbSoftMenuRepository zbSoftMenuRepository;

    public List<ZbSoftMenu> findBySoftId(Integer softId){
        return zbSoftMenuRepository.findBySoftId(softId);
    }

    public ZbSoftMenu save(ZbSoftMenu zbSoftMenu){
        return zbSoftMenuRepository.save(zbSoftMenu);
    }


    public List<ZbSoftMenu> save(List<ZbSoftMenu> zbSoftMenu){
        return zbSoftMenuRepository.save(zbSoftMenu);
    }

    @Transactional
    public void deleteBySoftId(Integer softId){
        zbSoftMenuRepository.deleteBySoftId(softId);
    }

}
