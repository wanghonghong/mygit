package com.jm.comtroller;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.jm.application.main.WebApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


/**
 * <p>单元测试</p>
 * @author wukf
 * @version latest
 * @date 2016/4/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WebApplication.class)
@WebAppConfiguration
public class BaseControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true);
        characterEncodingFilter.setEncoding("UTF-8");
        this.mockMvc = webAppContextSetup(this.wac).addFilters(characterEncodingFilter).build();
    }

    protected void get(String url) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    protected void put(String url,String jsonContent) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    protected void post(String url,String jsonContent) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    protected void delete(String url,String jsonContent) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());
    }

    protected ResultActions getResult(String url) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.get(url)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON));
    }

    protected ResultActions putResult(String url,String jsonContent) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.put(url)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent));
    }

    protected ResultActions postResult(String url,String jsonContent) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.post(url)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent));
    }

    protected ResultActions deleteResult(String url,String jsonContent) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent));
    }


    protected String getStrResult(String url) throws Exception {
        ResultActions ra = getResult(url);
        return ra.andReturn().getResponse().getContentAsString();
    }

    protected String deleteStrResult(String url,String jsonContent) throws Exception {
        ResultActions ra = deleteResult(url,jsonContent);
        return ra.andReturn().getResponse().getContentAsString();
    }

    protected String putStrResult(String url,String jsonContent) throws Exception {
        ResultActions ra = putResult(url,jsonContent);
        return ra.andReturn().getResponse().getContentAsString();
    }

    protected String postStrResult(String url,String jsonContent) throws Exception {
        ResultActions ra = postResult(url,jsonContent);
        return ra.andReturn().getResponse().getContentAsString();
    }

    @Test
    public void test() throws Exception {
        System.out.print("吴克府");
    }

    protected JavaType getType(Class cls){
        return this.objectMapper.getTypeFactory().constructType(cls);
    }

    protected JavaType getType(Type type, Type subType){
        JavaType javaType = null;
        if(subType instanceof JavaType){
            javaType = objectMapper.getTypeFactory().
                    constructParametricType((Class) type, (JavaType) subType);
        }else if (subType instanceof Class) {
            javaType = objectMapper.getTypeFactory().
                    constructParametricType((Class) type, (Class) subType);
        }
        return javaType;
    }

    /*protected <T> T jsonToBeanForUnderLine(String jsonStr, JavaType javaType) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return jsonToBean(jsonStr, javaType);
    }

    protected <T> T jsonToBeanForPascal(String jsonStr, JavaType javaType) {
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.PASCAL_CASE_TO_CAMEL_CASE);
        return jsonToBean(jsonStr, javaType);
    }

    private <T> T jsonToBean(String jsonStr, JavaType javaType) {
        try {
            return this.objectMapper.readValue(jsonStr, javaType);
        } catch (JsonParseException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (JsonMappingException e) {
            LOGGER.error(e.getMessage(), e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }*/

}

