package com.jm.repository.po.online;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="chat_log")
public class ChatLog {

    @Id
    private String id;

    private String to_user;

    private String from_user;

    private String source;

    private String type;

    private String msg;

    private String file_name;

    private String file_url;

    private String create_date;

}
