package com.jm.mvc.vo.shop.activity;

import com.jm.repository.po.shop.activity.Activity;
import com.jm.repository.po.shop.activity.ActivityCard;
import com.jm.repository.po.shop.activity.ActivityCondition;

import com.jm.repository.po.shop.activity.ActivitySub;
import lombok.Data;
import java.util.List;

/**
 * <p></p>
 *
 * @author wukf
 * @version latest
 * @date 2016/10/27
 */
@Data
public class ActivityVo {

    private Activity activity;

    private List<ActivityCard> activityCardList;

    private List<ActivitySub> activitySubList;

    private ActivityCondition activityCondition;

}
