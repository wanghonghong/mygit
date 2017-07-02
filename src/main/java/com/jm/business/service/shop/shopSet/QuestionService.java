package com.jm.business.service.shop.shopSet;

import com.jm.business.service.shop.impl.QuestionServiceImpl;
import com.jm.mvc.vo.PageItem;
import com.jm.mvc.vo.shop.activity.question.*;
import com.jm.repository.jpa.shop.activity.question.QuestionActivityRepository;
import com.jm.repository.jpa.shop.activity.question.QuestionGroupRepository;
import com.jm.repository.jpa.shop.activity.question.ShopQuestionRepository;
import com.jm.repository.jpa.shop.activity.question.ShopSignRepository;
import com.jm.repository.po.shop.activity.question.QuestionActivity;
import com.jm.repository.po.shop.activity.question.QuestionGroup;
import com.jm.repository.po.shop.activity.question.ShopQuestion;
import com.jm.repository.po.shop.activity.question.ShopSign;
import com.jm.staticcode.converter.shop.QuestionActivityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/18 0018.
 */
@Service
public class QuestionService implements QuestionServiceImpl{

    @Autowired
    private ShopQuestionRepository shopQuestionRepository;

    @Autowired
    private ShopSignRepository shopSignRepository;

    @Autowired
    private QuestionActivityRepository questionActivityRepository;

    @Autowired
    private QuestionGroupRepository questionGroupRepository;

    @Override
    public void saveQuestions(ShopQuestionCo shopQuestionCo, Integer shopId) {
        ShopQuestion shopQuestion = QuestionActivityConverter.toShopQuestion(shopQuestionCo);
        //没有新建商家
        if(shopQuestionCo.getShopSignId()!=null){
            shopQuestion.setShopId(shopId);
            shopQuestionRepository.save(shopQuestion);
        } else{//有新建商家
            ShopSign shopSign = QuestionActivityConverter.toShopSign(shopQuestionCo.getShopSignCo());
            shopSign.setShopId(shopId);
            shopSign = shopSignRepository.save(shopSign);
            shopQuestion.setShopSignId(shopSign.getId());
            shopQuestion.setShopId(shopId);
            shopQuestionRepository.save(shopQuestion);
        }
    }

    @Override
    public void updateQuestion(ShopQuestionUo shopQuestionUo, Integer shopId) {
        ShopQuestion shopQuestion = shopQuestionRepository.findOne(shopQuestionUo.getId());
        shopQuestion = QuestionActivityConverter.toShopQuestion(shopQuestionUo,shopQuestion);
        if(shopQuestionUo.getShopSignId()!=null){
            shopQuestionRepository.save(shopQuestion);
        }else {
            ShopSign shopSign = QuestionActivityConverter.toShopSign(shopQuestionUo.getShopSignCo());
            shopSign.setShopId(shopId);
            shopSign = shopSignRepository.save(shopSign);
            shopQuestion.setShopSignId(shopSign.getId());
            shopQuestionRepository.save(shopQuestion);
        }
    }

    @Override
    public ShopQuestionVo getQuestion(Integer id,Integer shopId) {
        ShopQuestion shopQuestion = shopQuestionRepository.findOne(id);
        ShopQuestionVo shopQuestionVo = QuestionActivityConverter.toShopQuestionVo(shopQuestion);
        List<ShopSign> shopSigns = shopSignRepository.findByShopId(shopId);
        List<ShopSignVo> shopSignVos = new ArrayList<>();
        for (ShopSign shopSign:shopSigns){
            ShopSignVo shopSignVo = QuestionActivityConverter.toShopSignVo(shopSign);
            shopSignVos.add(shopSignVo);
        }
        shopQuestionVo.setShopSigns(shopSignVos);
        List<QuestionGroup> questionGroups = questionGroupRepository.findAll();
        List<QuestionGroupVo> questionGroupVos = new ArrayList<>();
        for (QuestionGroup questionGroup:questionGroups){
            QuestionGroupVo questionGroupVo = QuestionActivityConverter.toQuestionGroupVo(questionGroup);
            questionGroupVos.add(questionGroupVo);
        }
        shopQuestionVo.setQuestionGroups(questionGroupVos);
        return shopQuestionVo;
    }

    @Override
    public void updateStatus(String ids) {
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            Integer shopQuestionId = Integer.parseInt(id[i]);
            ShopQuestion shopQuestion = shopQuestionRepository.findOne(shopQuestionId);
            shopQuestion.setStatus(9);
            shopQuestionRepository.save(shopQuestion);
        }
    }

    @Override
    public void saveQuestionActivity(QuestionActivityCo questionActivityCo, Integer shopId) {
        QuestionActivity questionActivity = QuestionActivityConverter.toQuestionActivity(questionActivityCo);
        questionActivity.setShopId(shopId);
        questionActivityRepository.save(questionActivity);
    }

    @Override
    public void updateQuestionActivity(QuestionActivityUo questionActivityUo, Integer shopId) {
        QuestionActivity questionActivity = questionActivityRepository.findOne(questionActivityUo.getId());
        questionActivity = QuestionActivityConverter.toQuestionActivity(questionActivityUo,questionActivity);
        questionActivityRepository.save(questionActivity);
    }

    @Override
    public QuestionActivityVo getQuestionActivity(Integer id) {
        QuestionActivity questionActivity = questionActivityRepository.findOne(id);
        QuestionActivityVo questionActivityVo = QuestionActivityConverter.toQuestionActivityVo(questionActivity);
        return questionActivityVo;
    }

    @Override
    public void deleteQuestionActivity(String ids) {
        String[] id = ids.split(",");
        for (int i = 0; i < id.length; i++) {
            Integer questionActivityId = Integer.parseInt(id[i]);
            QuestionActivity questionActivity = questionActivityRepository.findOne(questionActivityId);
            questionActivity.setStatus(9);
            questionActivityRepository.save(questionActivity);
        }

    }

}
