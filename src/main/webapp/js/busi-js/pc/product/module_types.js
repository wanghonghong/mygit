/**
 * Created by zx on 2017/4/7.
 */
CommonUtils.regNamespace("module", "types");
module.types=(function () {
    var _getTypesJson=function () {
        var json= [{
            "type": "title",
            "name": "分享提示语",
            "title": "分享提示语",
            "content":"爱上一家好店：）",
            "pageTitle":"页面标题",
            "fixedTop":true,
            "typeDescribe": "微信、微博分享时显示此处文字"
        },{
            "type": "titleInfo",
            "title": "页面标题",
            "pageTitle":"页面标题",
            "fixedTop":true,
            "typeDescribe": "该组件不需要进行配置"
        }, {
            "type": "imageAd",
            "name": "轮播海报",
            "title": "图片推荐尺寸：750*352像素，最多上传6张"
        }, {
            "type": "goods",
            "name": "具体商品",
            "title": "具体商品",
            "sizeType":0
        },  {
            "type": "activityGoods",
            "name": "活动商品",
            "title": "活动商品",
            "sizeType":0
        }, {
            "type": "goodslist",
            "name": "分类商品",
            "title": "选择分类商品",
            "size":6,
            "sizeType":1,
            "goodsTypeId":"",
            "goodsTypeName":"请选择商品分类"
        }, {
            "type": "white",
            "name": "辅助空白条",
            "title": "辅助空白条",
            "height":"7"
        }, {
            "type": "line",
            "name": "辅助线",
            "title": "辅助线",
            "typeDescribe": "添加一条辅助线，无需进行其他配置"
        }, {
            "type": "search",
            "name": "搜索条",
            "title": "搜索条",
            "typeDescribe": "商城内容搜索，无需进行其他配置"
        }, {
            "type": "imageList",
            "name": "图片插入",
            "title": "上传图片:750:352像素 最多上传10张"
        }, {
            "type": "video",
            "name": "视频插入",
            "title": "视频插入",
            "typeDescribe": "支持腾讯、百度视频地址录入"
        }, {
            "type": "audio",
            "name": "音频插入",
            "title": "音频插入",
            "typeDescribe": "支持mp3、wav、amr格式，大小不超过30M，时长不超过30分钟"
        }, {
            "type": "richtext",
            "name": "自由编辑栏（公开）",
            "title": "自由编辑栏（公开）"
        }, {
            "type": "richtextlimit",
            "name": "自由编辑栏（限制）",
            "title": "自由编辑栏（限制）"
        },{
            "type":"magicSquare",
            "name":"魔方",
            "title":"魔方"
        },{
            "type": "weixintop",
            "fixedTop":true,
            "name": "仿微信行头",
            "title": "仿微信行头",
            "titleTxt":"我只想静静的做一个好标题！"
        },{
            "type": "titleBar",
            "name": "标题栏",
            "title": "标题栏"
        },{
            "type": "bottomPic",
            "name": "底部通用插图",
            "title": "底部通用插图",
            "limitNumber":1
        },{
            "type": "weixinbottom",
            "fixedBottom":true,
            "name": "更多好料+点赞数量",
            "title": "更多好料+点赞数量",
            "linkTxt":"http://",
            "readCount":13056,
            "likes":358,
            "limitNumber":1
        },{
            "type": "imageTextTitle",
            "name": "图文头部",
            "title": "图文头部",
            "fixedTop":true,
            "formatCode":1,
            "isAward":0,
            "awardType":0,
            "integralType":0,
            "awardSecond":30,
            "verifyArray":[
                {
                    "name":"imageTextTile",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"图文主题不能为空！"

                    }]
                },{
                    "name":"imageTextType",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"请选择一个图文分类！"

                    }]
                },{
                    "name":"imageUrl",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"图文主图不能为空！"
                    }]
                },{
                    "name":"shareText",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"分享语摘要不能为空！"
                    }]
                },{
                    "name":"awardType",
                    "rules":[ {
                        "ruleType":"custom",   //做自定义验证
                        "customName":"dependCardId"
                    }]
                }
            ]
        },{
            "type": "QRcode",
            "name": "二维码样式",
            "title": "二维码样式",
            "ewmCode":"1",
            "limitNumber":1
        },{
            "type": "rewardModule",
            "name": "打赏板式",
            "title": "打赏板式",
            "rewardCode":"1"
        },{
            "type": "vote",
            "name": "投票组件",
            "title": "投票组件",
            "limitNumber":1,
            "voteType":1,
            "styleType":1,
            "themeName":"me是一个投票活动啊",
            "voteItemList":[],
            "fixedMiddle":true,
            "verifyArray":[
                {
                    "name":"themeName",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"活动名称不能为空！"

                    }]
                },{
                    "name":"startTime",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"发布时间不能为空！"

                    },{
                        "ruleType":"compare",
                        "compareType":"<",
                        "compareObj":"endTime",
                        "ruleTip":"发布时间不能比结束时间大"

                    }]
                },{
                    "name":"endTime",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"结束时间不能为空！"
                    }]
                },{
                    "name":"voteItemList",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"投票活动列表不能为空！"
                    },{
                        "ruleType":"custom",   //做自定义验证
                        "customName":"voteItemList"
                    }]
                }
            ]
        },{
            "type": "signup",
            "name": "报名组件",
            "title": "报名组件",
            "limitNumber":1,
            "activityName":"快来参加活动吧",
            "fixedMiddle":true,
            "verifyArray":[
                {
                    "name":"img",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"活动主图不能为空！"
                    } ]
                },
                {
                    "name":"activityName",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"活动名称不能为空！"

                    }]
                },{
                    "name":"startDate",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"开始时间不能为空！"

                    },{
                        "ruleType":"compare",
                        "compareType":"<",
                        "compareObj":"endDate",
                        "ruleTip":"开始时间不能比结束时间大"

                    }]
                },{
                    "name":"endDate",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"结束时间不能为空！"
                    }]
                },{
                    "name":"confId",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"报名板式不能为空！"
                    } ]
                },{
                    "name":"sms",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"短信通知不能为空！"
                    } ]
                },{
                    "name":"noticeDate",
                    "rules":[{
                        "ruleType":"required",
                        "ruleTip":"通知时间不能为空！"
                    } ]
                }
            ]
        },{
            "type": "comment",
            "name": "评论",
            "title": "评论",
            "limitNumber":1,
            "typeDescribe": "添加一个评论模块，无需进行其他配置"
        }];
        return json;

    }

    return{
        getTypesJson:_getTypesJson

    }
})();
