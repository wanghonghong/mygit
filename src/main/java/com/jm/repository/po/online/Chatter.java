package com.jm.repository.po.online;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="chatter")
public class Chatter {

    @Id
    private String id;

    private String chatDate;

    private String customer;

    private String service;

    private String openid;


}
