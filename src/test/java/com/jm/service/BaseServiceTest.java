package com.jm.service;


import com.jm.application.main.WebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * <p>单元测试</p>
 * @author wukf
 * @version latest
 * @date 2016/4/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration
public class BaseServiceTest {
    @Test
    public void test() throws Exception {
        System.out.print("吴克府");
    }

}

