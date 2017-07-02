package com.jm.repository.jpa;


import com.jm.mvc.vo.PageItem;
import com.jm.repository.po.online.ChatLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository
public class MongoUtil {

    @Autowired
    private MongoTemplate mongoTemplate;

    public <T> void  saveList(List<T> list,Class<T> tClass){
        mongoTemplate.insert(list,tClass);
    }

    public <T> void save(T t){
        mongoTemplate.save(t);
    }

    public <T>List<T> findAll(Class<T> tClass){
        return mongoTemplate.findAll(tClass);
    }

    public <T>List<T> query(Query query,Class<T> tClass){
       return  mongoTemplate.find(query,tClass);
    }

    public <T>PageItem<T> queryPageItem(Query query,Class<T> tClass,int currPage,int pageSize){
        Long count = mongoTemplate.count(query,tClass);
        int skip = 0;
        if(currPage>0){
            skip = (currPage)*pageSize;
        }
        query.skip(skip);
        query.limit(pageSize);
        List<T> data = mongoTemplate.find(query,tClass);
        PageItem<T> pageItem = new PageItem<>();
        pageItem.setCount(count.intValue());
        pageItem.setItems(data);
        return pageItem;
    }

    public <T> T findOne(Query query,Class<T> tClass){
        return mongoTemplate.findOne(query,tClass);
    }

}
