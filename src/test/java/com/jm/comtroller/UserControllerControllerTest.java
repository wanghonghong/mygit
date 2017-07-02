package com.jm.comtroller;

import org.junit.Test;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/26
 */
public class UserControllerControllerTest extends BaseControllerTest {

    @Test
    public void test1() throws Exception {
        System.out.print("吴克府");
        /*//新增
        String inParam = "{\"userName\": \"吴克府\"}";
        String resJson = postStrResult("/user",inParam);
        User user = objectMapper.readValue(resJson,User.class);
        Assert.assertTrue("吴克府".equals(user.getUserName()));
        //修改
        String updateParam = "{\"userName\": \"吴克府1\"}";
        String updateResJson = putStrResult("/user/"+user.getUserId(),updateParam);
        User newUser = objectMapper.readValue(updateResJson,User.class);
        Assert.assertTrue("吴克府1".equals(newUser.getUserName()));
        //获取
        String getJson = getStrResult("/user/"+user.getUserId());
        User getUser = objectMapper.readValue(getJson,User.class);
        Assert.assertTrue(user.getUserId().equals(getUser.getUserId()));
        //删除
        String delResJson = deleteStrResult("/user/"+user.getUserId(),"");
        Assert.assertTrue("".equals(delResJson));*/
    }

}
