package com.jm.business.service.zb;

import com.jm.repository.jpa.zb.system.ZbSoftRepository;
import com.jm.repository.jpa.zb.system.ZbSoftRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 软件版本
 */
@Service
public class ZbSoftRoleService {
    @Autowired
    private ZbSoftRoleRepository zbSoftRoleRepository;

}
