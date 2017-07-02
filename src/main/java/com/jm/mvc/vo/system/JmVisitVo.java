package com.jm.mvc.vo.system;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.util.Date;

/**
 * <p>访问记录表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/5/6
 */
@Data
@ApiModel(description = "访问量Vo")
public class JmVisitVo {

    private Integer id;

    private String ip;

    private String requestURI;

    private Date visitTime;

    private Integer pid;

    private Integer userId;

    public JmVisitVo(){
        this.visitTime=new Date();
    }

}
