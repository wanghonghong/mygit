package com.jm.business.service.zb;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.zb.system.ZbSoftQo;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.zb.system.ZbSoftMenuRepository;
import com.jm.repository.jpa.zb.system.ZbSoftRepository;
import com.jm.repository.po.zb.system.ZbSoft;
import com.jm.repository.po.zb.system.ZbSoftMenu;
import com.jm.staticcode.util.wx.Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 软件版本
 */
@Service
public class ZbSoftService {
    @Autowired
    private ZbSoftRepository zbSoftRepository;

    @Autowired
    private JdbcUtil jdbcUtil;

    public ZbSoft save(ZbSoft zbSoft){
        return zbSoftRepository.save(zbSoft);
    }

    public ZbSoft getZbSoft(Integer id){
        return zbSoftRepository.findOne(id);
    }

    public void delete(Integer id){
         zbSoftRepository.delete(id);
    }


    public PageItem<ZbSoft> findZbSoft(ZbSoftQo qo) throws IllegalAccessException, IOException, InstantiationException {
        String sql = " select * from zb_soft where 1=1 ";
        sql += JdbcUtil.appendLike("name", qo.getName());
        PageItem<ZbSoft> zbSoftPageItem = jdbcUtil.queryPageItem(sql,qo.getCurPage(),qo.getPageSize(),ZbSoft.class);
        return zbSoftPageItem;
    }


}
