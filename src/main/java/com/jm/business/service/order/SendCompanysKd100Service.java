package com.jm.business.service.order;

import com.jm.repository.jpa.order.SendCompanysKd100Repository;
import com.jm.repository.po.order.SendCompanysKd100;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * <p>物流信息</p>
 *
 * @author liangrs
 * @version latest
 * @date 2016/10/14
 */
@Log4j
@Service
public class SendCompanysKd100Service {

    @Autowired
    private SendCompanysKd100Repository sendCompanysKd100Repository;
   /* @Autowired
    private K100Service k100Service;*/

    public List<SendCompanysKd100> getSendCompanys(){
        List<SendCompanysKd100> sckd = sendCompanysKd100Repository.findAll();
        return sckd;
    }

    public String send_list(String code,String num) {
        String content = null;
        try
        {
            StringBuffer sb=new StringBuffer();
            sb.append("https://www.kuaidi100.com/applyurl?key=");
            sb.append("ddea0044faf8fdaf"); //46c152495f0fd853
            sb.append("&com=").append(code);
            sb.append("&nu=").append(num);
            URL url= new URL(sb.toString());
            URLConnection con = url.openConnection();
            con.setAllowUserInteraction(false);
            InputStream urlStream = url.openStream();
            byte b[] = new byte[10000];
            int numRead = urlStream.read(b);
            content = new String(b, 0, numRead);
            while (numRead != -1)
            {
                numRead = urlStream.read(b);
                if (numRead != -1)
                {
                    // String newContent = new String(b, 0, numRead);
                    String newContent = new String(b, 0, numRead, "UTF-8");
                    content += newContent;
                }
            }
            urlStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //log.error("快递查询错误");
        }
        if(content.contains("http")){
            content = content.replace("http","https");
        }
        log.info("_______content________"+content);
        return content;
    }
}
