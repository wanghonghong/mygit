package com.jm.staticcode.converter.online;


import com.jm.mvc.vo.online.ChatterCo;
import com.jm.repository.po.online.Chatter;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OnlineUserConverter {

    public static Chatter co2Chatter(ChatterCo chatterCo){
        Date date = new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String now = sdf.format(date);
        Chatter chatter = new Chatter();
        BeanUtils.copyProperties(chatterCo,chatter);
        chatter.setChatDate(now);
        return chatter;
    }

    public static List<Chatter> co2Chatters(List<ChatterCo> chatterCos){
        if(chatterCos.size()<1){
            return null;
        }
        List<Chatter> chatters = new ArrayList<>();
        for(ChatterCo chatterCo :chatterCos){
            chatters.add(co2Chatter(chatterCo));
        }
        return chatters;
    }

}
