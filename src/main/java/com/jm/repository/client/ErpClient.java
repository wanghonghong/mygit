package com.jm.repository.client;

import com.jm.mvc.vo.PageItem;
import com.jm.repository.client.dto.DispatchDto;
import com.jm.repository.client.dto.NoticeContextDto;
import com.jm.repository.client.dto.NoticeDto;
import com.jm.staticcode.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * Created by ME on 2016/12/15.
 */
@Slf4j
@Repository
public class ErpClient extends BaseClient {

    public PageItem getDispatchList(DispatchDto dispatchDto) throws IOException {
        String url  = Constant.MSA_SERVICE+"/dispatch/list";
        PageItem dispatchs = restTemplate.postForObject(url,dispatchDto,PageItem.class);
        return dispatchs;
    }

    public PageItem getNoticeList(NoticeDto noticeDto) throws IOException {
        String url  = Constant.MSA_SERVICE+"/notices";
        PageItem notices = restTemplate.postForObject(url,noticeDto,PageItem.class);
        return notices;
    }

    public NoticeContextDto getNotice(NoticeDto noticeDto) throws IOException {
        String url  = Constant.MSA_SERVICE+"/join/notice";
        NoticeContextDto notice = restTemplate.postForObject(url,noticeDto,NoticeContextDto.class);
        return notice;
    }
}
