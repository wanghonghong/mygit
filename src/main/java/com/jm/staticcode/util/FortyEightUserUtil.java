package com.jm.staticcode.util;



import com.jm.mvc.vo.wx.wxuser.FortyEightUser;
import com.jm.repository.jpa.JdbcUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Log4j
public class FortyEightUserUtil {

    private static Set<FortyEightUser> userSet;

    private static Set<String> callIngUser;

    private static final Integer USER_LIMIT = 200;

    public static void addFortyEightUser(FortyEightUser user) throws Exception {
        if(userSet==null){
            userSet = new HashSet<>();
        }
        userSet.add(user);
    }

    public  static void addCallingUser(String openid){
        if(callIngUser==null){
            callIngUser = new HashSet<>();
        }
        callIngUser.add(openid);
    }


    public static void flushData() throws Exception {
        if(userSet==null||userSet.size()<=0){
            return;
        }

        JdbcUtil jdbcUtil = ApplicationContextUtil.getApplicationContext().getBean(JdbcUtil.class);
        List<FortyEightUser> users = new ArrayList<>(userSet);
        while(users.size()>0){
            StringBuffer buffer = new StringBuffer("UPDATE wx_user SET last_control_time = now() WHERE 1=1 ");
            int size = users.size()>=USER_LIMIT?USER_LIMIT: users.size();
            List<FortyEightUser> subList = users.subList(0,size);
            String conditionSql = jdbcUtil.getInSql("openid",users,FortyEightUser.class,"Openid");
            buffer.append(conditionSql);
            jdbcUtil.update(buffer.toString());
            subList.clear();
        }
    }


    public static void updateCallingUser(String openid){
        String sql = "UPDATE hx_user hu SET hu.is_reply = 1 WHERE EXISTS ( SELECT 1 FROM wx_user wu WHERE wu.user_id = hu.user_id AND wu.openid = '"+openid+"' )";
        log.info(sql);
        JdbcUtil jdbcUtil = ApplicationContextUtil.getApplicationContext().getBean(JdbcUtil.class);
        jdbcUtil.update(sql);
    }

    public static void isCalling(){
        if(callIngUser==null||callIngUser.size()<1){
            return ;
        }
        JdbcUtil jdbcUtil = ApplicationContextUtil.getApplicationContext().getBean(JdbcUtil.class);

        List<String> openids = new ArrayList<>(callIngUser);
        while(openids.size()>0){
            StringBuffer buffer = new StringBuffer("UPDATE hx_user hu SET hu.is_reply = 1 WHERE EXISTS (SELECT 1 FROM wx_user wu WHERE wu.user_id = hu.user_id AND wu.openid in ( ");
            int size = openids.size()>USER_LIMIT?USER_LIMIT:openids.size();
            List<String> subList = openids.subList(0,size);
            for(int i=0;i<subList.size();i++){
                String openid = subList.get(i);
                if(i>0){
                    buffer.append(",");
                }
                buffer.append("'"+openid+"'");
            }
            buffer.append(" ))");
            jdbcUtil.update(buffer.toString());
            subList.clear();
        }

    }

}
