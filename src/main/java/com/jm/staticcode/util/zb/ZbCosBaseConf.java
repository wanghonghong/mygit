package com.jm.staticcode.util.zb;

import com.jm.staticcode.util.Toolkit;

/**
 * 腾讯云服务器配置的基础信息
 * @author: cj
 * @date: 2016-6-15
 */
public class ZbCosBaseConf {
	// 通过控制台获取AppId,SecretId,SecretKey
	public static final int APP_ID = 10046040;
    public static final String SECRET_ID = "AKIDOVfkAYOk6EcAHLr7iRg6KEh2mZ9motH7";
    public static final String SECRET_KEY = "pq1FCjhJGed3RTwUm4kSB1LRpyaOc0LK";
    // 默认超时时间，单位秒
    public static final int Time_Out = 60;

    // 通过控制台提前建好的bucket
    public static final String bucketName = Toolkit.getProperties("config/application.properties").getProperty("resource.bucket");

    // 默认获取的目录列表的数量
    public static final int DIR_NUM = 20;
    // 上传到cos文件夹的目录
    public static final String DIR_REMOTE_PATH = "/image/";   //先用来存放图片

    public static final String DIR_REMOTE_VIDEO_PATH = "/video/";   //用来存放视频

    public static final String DIR_REMOTE_AUDIO_PATH = "/audio/";   //用来存放语音

    public static final String COS_PATH = Toolkit.getProperties("config/application.properties").getProperty("resource.url");

    public static final String WEB_APP_REMOTE_PATH = Toolkit.getProperties("config/application.properties").getProperty("msa.webAppVersion");
   
}
