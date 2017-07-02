package com.jm.business.service.online;

import com.jm.business.service.system.ResourceService;
import com.jm.repository.client.ImageClient;
import com.jm.repository.jpa.JdbcUtil;
import com.jm.repository.jpa.online.TempImgRepository;
import com.jm.repository.po.online.TempImg;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Log4j
@Service
public class TempImgService {

    @Autowired
    private TempImgRepository tempImgRepository;
    @Autowired
    private JdbcUtil jdbcUtil;
    @Autowired
    private ResourceService resourceService;

    @Transactional
    public TempImg saveTempImg(TempImg tempImg){

        return tempImgRepository.save(tempImg);
    }

    @Transactional
    public void deleteTempImg(TempImg tempImg){
         tempImgRepository.delete(tempImg);
    }

    @Transactional
    public void deleteTempImg(Iterable<Integer> tempIds){
        List<TempImg> tempImgList = tempImgRepository.findAll(tempIds);
        deleteTempImg(tempImgList);
    }
    @Transactional void deleteTempImg(List<TempImg> tempImgs){
        if(tempImgs!=null&&tempImgs.size()>0){
            tempImgRepository.delete(tempImgs);
        }
    }

    @Transactional
    public void cleanChatImg() throws IllegalAccessException, IOException, InstantiationException {
        String sql = "SELECT * from temp_img WHERE create_time <= unix_timestamp(subdate(NOW(),interval 7 day))";
        List<TempImg> tempImgs = jdbcUtil.queryList(sql,TempImg.class);
        if(tempImgs!=null&&tempImgs.size()>0){
            for(TempImg tempImg :tempImgs){
                ImageClient.delPic(tempImg.getFileId());
            }
        }
        deleteTempImg(tempImgs);

    }

}
