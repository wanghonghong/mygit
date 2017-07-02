package com.jm.business.service.shop.impl;

import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.activity.question.*;
import com.jm.repository.po.shop.activity.question.QuestionActivity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24 0024.
 */
public interface QuestionServiceImpl {

    /**
     * 新增问题
     * @param shopQuestionCo
     * @param shopId
     */
    void saveQuestions(ShopQuestionCo shopQuestionCo, Integer shopId);

    /**
     * 修改问题
     * @param shopQuestionUo
     * @param shopId
     */
    void updateQuestion(ShopQuestionUo shopQuestionUo, Integer shopId);

    /**
     * 获取单个问题
     * @param id
     * @return
     */
    ShopQuestionVo getQuestion(Integer id,Integer shopId);

    /**
     * 删除问题,支持批量，伪删除
     * @param ids
     */
    void updateStatus(String ids);

    /**
     * 新增问卷
     * @param questionActivityCo
     * @param shopId
     */
    void saveQuestionActivity(QuestionActivityCo questionActivityCo, Integer shopId);

    /**
     * 修改问卷
     * @param questionActivityUo
     * @param shopId
     */
    void updateQuestionActivity(QuestionActivityUo questionActivityUo, Integer shopId);

    /**
     * 获取问卷
     * @param id
     * @return
     */
    QuestionActivityVo getQuestionActivity(Integer id);

    /**
     * 删除问卷
     * @param ids
     */
    void deleteQuestionActivity(String ids);
}
