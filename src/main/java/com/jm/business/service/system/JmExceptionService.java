package com.jm.business.service.system;

import com.jm.repository.jpa.system.LogJmExceptionRepository;
import com.jm.repository.po.system.LogJmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @author zww
 * @version latest
 * @date 2016-7-27 15:42:51
 */
@Service
public class JmExceptionService {

    @Autowired
    private LogJmExceptionRepository logJmExceptionRepository;

    public void saveJmException(LogJmException jmExceptionLog) {
        logJmExceptionRepository.save(jmExceptionLog);
    }
}
