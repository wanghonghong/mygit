package com.jm.staticcode.converter.shop;

import com.jm.mvc.vo.shop.activity.question.*;
import com.jm.repository.po.shop.activity.question.QuestionActivity;
import com.jm.repository.po.shop.activity.question.QuestionGroup;
import com.jm.repository.po.shop.activity.question.ShopQuestion;
import com.jm.repository.po.shop.activity.question.ShopSign;
import com.jm.staticcode.util.ImgUtil;
import org.springframework.beans.BeanUtils;

/**
 * <p>活动转换器</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
public class QuestionActivityConverter {


    public static ShopQuestion toShopQuestion(ShopQuestionCo shopQuestionCo) {
        ShopQuestion shopQuestion = new ShopQuestion();
        BeanUtils.copyProperties(shopQuestionCo,shopQuestion);
        if(shopQuestion.getImage1()!=null){
            shopQuestion.setImage1(ImgUtil.substringUrl(shopQuestion.getImage1()));
        }
        if(shopQuestion.getImage2()!=null){
            shopQuestion.setImage2(ImgUtil.substringUrl(shopQuestion.getImage2()));
        }
        if(shopQuestion.getImage3()!=null){
            shopQuestion.setImage3(ImgUtil.substringUrl(shopQuestion.getImage3()));
        }
        if(shopQuestion.getImage4()!=null){
            shopQuestion.setImage4(ImgUtil.substringUrl(shopQuestion.getImage4()));
        }
        if(shopQuestion.getImage5()!=null){
            shopQuestion.setImage5(ImgUtil.substringUrl(shopQuestion.getImage5()));
        }
        if(shopQuestion.getImage6()!=null){
            shopQuestion.setImage6(ImgUtil.substringUrl(shopQuestion.getImage6()));
        }
        return shopQuestion;
    }

    public static ShopSign toShopSign(ShopSignCo shopSignCo) {
        ShopSign shopSign = new ShopSign();
        BeanUtils.copyProperties(shopSignCo,shopSign);
        return shopSign;
    }

    public static ShopQuestion toShopQuestion(ShopQuestionUo shopQuestionUo, ShopQuestion shopQuestion) {
        BeanUtils.copyProperties(shopQuestionUo,shopQuestion);
        if(shopQuestion.getImage1()!=null){
            shopQuestion.setImage1(ImgUtil.substringUrl(shopQuestion.getImage1()));
        }
        if(shopQuestion.getImage2()!=null){
            shopQuestion.setImage2(ImgUtil.substringUrl(shopQuestion.getImage2()));
        }
        if(shopQuestion.getImage3()!=null){
            shopQuestion.setImage3(ImgUtil.substringUrl(shopQuestion.getImage3()));
        }
        if(shopQuestion.getImage4()!=null){
            shopQuestion.setImage4(ImgUtil.substringUrl(shopQuestion.getImage4()));
        }
        if(shopQuestion.getImage5()!=null){
            shopQuestion.setImage5(ImgUtil.substringUrl(shopQuestion.getImage5()));
        }
        if(shopQuestion.getImage6()!=null){
            shopQuestion.setImage6(ImgUtil.substringUrl(shopQuestion.getImage6()));
        }
        return shopQuestion;
    }

    public static ShopQuestionVo toShopQuestionVo(ShopQuestion shopQuestion) {
        ShopQuestionVo shopQuestionVo = new ShopQuestionVo();
        BeanUtils.copyProperties(shopQuestion,shopQuestionVo);
        if(shopQuestionVo.getImage1()!=null){
            shopQuestionVo.setImage1(ImgUtil.appendUrl(shopQuestion.getImage1()));
        }
        if(shopQuestionVo.getImage2()!=null){
            shopQuestionVo.setImage2(ImgUtil.appendUrl(shopQuestion.getImage2()));
        }
        if(shopQuestionVo.getImage3()!=null){
            shopQuestionVo.setImage3(ImgUtil.appendUrl(shopQuestion.getImage3()));
        }
        if(shopQuestionVo.getImage4()!=null){
            shopQuestionVo.setImage4(ImgUtil.appendUrl(shopQuestion.getImage4()));
        }
        if(shopQuestionVo.getImage5()!=null){
            shopQuestionVo.setImage5(ImgUtil.appendUrl(shopQuestion.getImage5()));
        }
        if(shopQuestionVo.getImage6()!=null){
            shopQuestionVo.setImage6(ImgUtil.appendUrl(shopQuestion.getImage6()));
        }
        return shopQuestionVo;
    }

    public static ShopSignVo toShopSignVo(ShopSign shopSign) {
        ShopSignVo shopSignVo = new ShopSignVo();
        BeanUtils.copyProperties(shopSign,shopSignVo);
        return shopSignVo;
    }

    public static QuestionGroupVo toQuestionGroupVo(QuestionGroup questionGroup) {
        QuestionGroupVo questionGroupVo = new QuestionGroupVo();
        BeanUtils.copyProperties(questionGroup,questionGroupVo);
        return questionGroupVo;
    }

    public static QuestionActivity toQuestionActivity(QuestionActivityCo questionActivityCo) {
        QuestionActivity questionActivity = new QuestionActivity();
        BeanUtils.copyProperties(questionActivityCo,questionActivity);
        if (questionActivity.getImageUrl()!=null){
            questionActivity.setImageUrl(ImgUtil.substringUrl(questionActivity.getImageUrl()));
        }
        return questionActivity;
    }

    public static QuestionActivity toQuestionActivity(QuestionActivityUo questionActivityUo, QuestionActivity questionActivity) {
        BeanUtils.copyProperties(questionActivityUo,questionActivity);
        if (questionActivity.getImageUrl()!=null){
            questionActivity.setImageUrl(ImgUtil.substringUrl(questionActivity.getImageUrl()));
        }
        return questionActivity;
    }

    public static QuestionActivityVo toQuestionActivityVo(QuestionActivity questionActivity) {
        QuestionActivityVo questionActivityVo = new QuestionActivityVo();
        BeanUtils.copyProperties(questionActivity,questionActivityVo);
        if(questionActivityVo.getImageUrl()!=null){
            questionActivityVo.setImageUrl(ImgUtil.appendUrl(questionActivityVo.getImageUrl()));
        }
        return questionActivityVo;
    }
}
