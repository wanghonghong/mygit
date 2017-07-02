package com.jm.repository.po.system;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>角色表</p>
 *
 * @author wukf
 * @version latest
 * @date 2016/4/15
 */
@Data
@Entity
public class LogJmException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //返回状态对应code
    private String status;

    //接口名称
    private String	logName;

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    private String 	logMsg;	//错误信息

    @Lob
    @Column(columnDefinition="TEXT", length = 65535)
    private String 	logDetail;	//错误详细信息

    private String inParam;	//入参

    private String outParam;  //出参

    private Date createTime;	//调用时间

    private String requestUrl;//请求地址

    private Integer shopId;

    private String appid;

}
