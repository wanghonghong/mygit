package com.jm.business.service.wx;

import com.jm.repository.jpa.wx.WxUserAccountRepository;
import com.jm.repository.po.wx.WxUserAccount;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class WxUserAccountService {

    @Autowired
    private WxUserAccountRepository wxUserRepository;

    public WxUserAccount findWxUserAccountById(Integer userId,Integer accountType){

        return wxUserRepository.findByUserIdAndAccountType(userId,accountType);
    }

    public WxUserAccount saveWxUserAccount(WxUserAccount wxUserAccount){
        return  wxUserRepository.save(wxUserAccount);
    }
}
