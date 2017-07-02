/**
 * Created by wxz on 2016/11/9
 * 自动回复--关键字回复
 */
CommonUtils.regNamespace("autor", "autoreply");
autor.autoreply = (function () {
    var keyw_tag = 0;//关键字规则添加状态
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/keyreply',//群发对象
        url2: CONTEXT_PATH + '/keyreply/list' //列表
    };
    //初始化
    var _init = function () {
        _kwrule_list(); //规则列表
        //_kwrule_placechange();//列表记录换位
        keyw_tag = 0;
    }



    var _kwrule_placechange = function () {
        $( "div[id='reply_content_keyword'] div[id='item_keyword'] " ).sortable({
            placeholder: "keyword_biglist",
            scrollSpeed: 0,//滚动条滚动速度
            scroll: false, //滚动条滚动效果
            tolerance: 'pointer',//设置当拖动元素越过其它元素多少时便对元素进行重新排序 pointer：鼠标指针重叠元素 intersect：至少重叠50%
            opacity: -1,     //显示的透明度
            placeholder: 'ui-state-highlight' //设置当排序动作发生时，空白占位符的CSS样式
        });
        $( "div[id='reply_content_keyword'] div[id='item_keyword'] " ).disableSelection();
    }

//关键字-规则新增
    var _kwrule_add_show = function () {

        if (keyw_tag === 0) {
            jumi.template('wechat/reply_keyword_add', function (tpl) {
                $('#add_keyword').empty();
                $('#add_keyword').html(tpl);
                $('#panel_bedd').show();
                _setEditor('panel_beadd_text_a');
            })
            $('#add_keyword').show();
            keyw_tag = 1;
            $(this).find('i[id^="belowtriangle_"]').removeClass("icon-uptriangle").addClass("icon-belowtriangle");
            $("div[id='reply_content_keyword'] div[id='item_keyword'] .keyword_bigtitle").siblings(".keyword_bigitem").show();
            $("div[id='reply_content_keyword'] div[id='item_keyword'] .keyword_bigtitle").siblings(".keyword_bigdetail").hide();
            $("div[id='reply_content_keyword'] div[id='item_keyword'] .keyword_bigtitle").siblings(".keyword_bigdetail").html("");
          //  qq.face.read_file("#keyface_face","#keyface_qqC","#keyface_text");//加载表情包 按钮 , 容器 , 编辑框
            _reply_keyword_change();//添加 选项卡切换
            _kwrule_key_show();//关键字添加框 显示状态
            _kwrule_key_add();//关键字添加
            _kwrule_key_cancel();//关键字添加框 取消
            _kwrule_detail_add();//关键字回复保存
            _kwrule_keyd_imgdel();//图片删除
            _kwrule_detail_del();//取消"新增"操作
        }
        else {
            $('#add_keyword').hide();
            $('#add_keyword').html("");
            keyw_tag = 0;

        }

    }

    //编辑器
    var _setEditor = function(id,data){
        var id = id;
        var data = data||'';
        UE.delEditor(id);
        ue = UE.getEditor(id, {
            toolbars: [
                ['redo','link']
            ],
            initialFrameHeight:150
        });
        var ue = UE.getEditor(id);
        ue.ready(function() {
            if(data){
                ue.setContent(data);
            }
            setTimeout(function(){
                $('.edui-for-image').remove();
                $('.edui-for-redo').remove();
            },1000)
        })
    }
    //关键字回复切换
    var _reply_keyword_change = function () {
        localStorage.setItem("keyword_index", 0);//固定回复下标保存
        var tabul = $("div[id='reply_content_keyword'] .m-panel-zdhf ul li");
        tabul.eq(0).addClass("z-sel").siblings().removeClass('z-sel');
        var tabcon = $("div[id='reply_content_keyword'] .panel-list");
        tabcon.hide().eq(0).show();
        tabul.click(function () {
            var index = $(this).index();
            localStorage.setItem("keyword_index", index);//固定回复下标保存  0:文本 1：表情 2：图片 3：音乐 4：语音 5：视频 6：外网连接  7：商城连接
            tabul.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
            tabcon.hide().eq(index).show();
            if (index === 0) {
                $('#panel_bedd').show();
                _setEditor('panel_beadd_text_a');
                localStorage.setItem("#keyd_qqC",0); //设置表情显示标识
            }
            if (index === 1) {
                $("#keyd_qqC").hide(); //表情 隐藏
                localStorage.setItem("#keyd_qqC",0); //设置表情显示标识
                var status = $("#keyd_imgstatus").html();
                if (status === "0") {
                    mm.autoreply.reply_img('#keyd_img', '#keyd_imgdiv', '#keyd_imgstatus');
                }
            }

            _reply_friendmsg(index);
        });
    };

    //友情提示
    var _reply_friendmsg = function (index) {
        switch (index) {
            case 3:
            case 4:
            case 5:
                var dm = new dialogMessage({
                    type: 2,
                    title: '提示',
                    fixed: true,
                    msg: '功能还在开发中',
                    isAutoDisplay: false

                });
                dm.render();
                break;

        }
    };
    //关键字-规则列表 当前页定位
    _kwrule_list_cur = function () {

        var curPage = $("#pageToolbar_page").val();
        var wxKeyReplyQo = {
            pageSize: 10,
            curPage: curPage || 0
        };
        $.ajaxJson(ajaxUrl.url2, wxKeyReplyQo, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    }
                    for (var i = 0; i < data.items.length; i++) {
                        var tempval = data.items[i].keyName;
                        data.items[i].keyNameArr = tempval.split(",");
                        data.items[i].contentTypeMsg = setContentTypeMsg(data.items[i].replyContentType);
                    }
                    jumi.template('wechat/reply_keyword_list', data, function (tpl) {
                        $("#item_keyword").empty();
                        $("#item_keyword").html(tpl);
                        //////
                        $('#add_keyword').hide();
                        $('#add_keyword').html("");
                        keyw_tag = 0;  //新增项显示状态
                        //////
                    });
                    _kwrule_biglist_status();
                }
            }
        })
    };
    //关键字-规则列表
    _kwrule_list = function () {
        var wxKeyReplyQo = {
            pageSize: 10
        };
        jumi.pagination('#pageToolbar', ajaxUrl.url2, wxKeyReplyQo, function (res, curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items
                };
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }
                for (var i = 0; i < data.items.length; i++) {
                    var tempval = data.items[i].keyName;
                    data.items[i].keyNameArr = tempval.split(",");
                    data.items[i].contentTypeMsg = setContentTypeMsg(data.items[i].replyContentType);
                }

                jumi.template('wechat/reply_keyword_list', data, function (tpl) {
                    $("#item_keyword").empty();
                    $("#item_keyword").html(tpl);
                    //////
                    $('#add_keyword').hide();
                    $('#add_keyword').html("");
                    keyw_tag = 0;  //新增项显示状态
                    //////
                });
                _kwrule_biglist_status();

            }
        });
    };

    function setContentTypeMsg(type) {
        var msg = "";
        switch (type) //1：文本 2：表情 3： 商城连接   4：图片 5：音乐 6：语音 7：视频 8：外网链接
        {
            case 1:
                msg = "一条文本";
                break;
            case 3:
                msg = "一条商城连接";
                break;
            case 4:
                msg = "一条图片";
                break;
            case 5:
                msg = "一条音乐";
                break;
            case 6:
                msg = "一条语音";
                break;
            case 7:
                msg = "一条视频";
                break;
            case 8:
                msg = "一条外网链接";
                break;
        }
        return msg;
    }

    //关键字添加-显示、隐藏
    _kwrule_key_show = function () {
        $("div[id='reply_content_keyword'] .kewwd_dg_btn").click(function () {
            $("div[id='reply_content_keyword'] .kewwd_dg").fadeToggle("fast");

        });
    }

    //关键字添加
    _kwrule_key_add = function () {
        $("div[id='reply_content_keyword'] .keywd_save").click(function () {
            var msg = "";
            var html ="";
            var num = 0;
            var key = $("div[id='reply_content_keyword'] .keywd_con").find("input");

            $("div[id='reply_content_keyword'] .keywd_newitem").each(function () {
                num = num+1;
            });
            if(key.val().length>20||key.val().length===0){
                msg = "关键字必须为 <font style='color:#f38925;'>1-20个字符</font>";
                _autoreply_add_msg(msg);
                return;
            }
            if(num>=30){
                msg = "关键字最多添加 <font style='color:#f38925;'>30条</font>";
                _autoreply_add_msg(msg);
                return;
            }

            //添加开始
            html = $("div[id='reply_content_keyword'] .keywd_item").clone();
            html.css("display", "static");
            html.removeClass("keywd_item");
            html.addClass("keywd_newitem");
            html.prepend(key.val());
            $("div[id='reply_content_keyword'] .keywd_list").append(html);
            key.val("");
            $("div[id='reply_content_keyword'] .kewwd_dg").hide();
            _kwrule_key_close();//关键字删除
        });
    }
    //删除关键字
    _kwrule_key_close = function () {
        $("div[id='reply_content_keyword'] .keywd_close").click(function () {
            $(this).parent().remove();
        });
    }
    _kwrule_key_cancel = function () {
        $("div[id='reply_content_keyword'] .keywd_cancel").click(function () {
            $("div[id='reply_content_keyword'] .keywd_con").find("input").val("");
            $(this).parent().hide();
        });
    }

    //编辑
    _kwrule_biglist_status = function () {
        _kdel_list_btn();
        $("div[id='reply_content_keyword'] div[id='item_keyword'] .keyword_bigtitle").unbind('click').bind('click', function (event) {
            event.stopPropagation();
            var cls = $(this).find("i").attr("class");
            cls = cls.replace("iconfont ", "");
            var obj = $(this).siblings(".keyword_bigdetail");
            //隐藏新增规则 及同级元素 start
            $('#add_keyword').hide();
            $('#add_keyword').html("");
            keyw_tag = 0;
            $(this).find('i[id^="belowtriangle_"]').removeClass("icon-uptriangle").addClass("icon-belowtriangle");
            $("div[id='reply_content_keyword'] div[id='item_keyword'] .keyword_bigtitle").siblings(".keyword_bigitem").show();
            $("div[id='reply_content_keyword'] div[id='item_keyword'] .keyword_bigtitle").siblings(".keyword_bigdetail").hide();
            $("div[id='reply_content_keyword'] div[id='item_keyword'] .keyword_bigtitle").siblings(".keyword_bigdetail").html("");//清空编辑内容
            //隐藏新增规则 及同级元素 end
            if (cls === "icon-belowtriangle") {
                var id = $(this).attr("id").replace("edititem_", "");
                $(this).siblings(".keyword_bigitem").hide();
                obj.show();//加载detail
                _kwrule_get_detail(id, obj);//读取详情并展示
                $(this).find('i[id^="belowtriangle_"]').removeClass("icon-belowtriangle");
                $(this).find('i[id^="belowtriangle_"]').addClass("icon-uptriangle");
            }
            if (cls === "icon-uptriangle") {

                $(this).siblings(".keyword_bigitem").show();
                obj.hide();//隐藏detail
                obj.html("");

                $(this).find('i[id^="belowtriangle_"]').removeClass("icon-uptriangle");
                $(this).find('i[id^="belowtriangle_"]').addClass("icon-belowtriangle");
            }
        });
    }
    //读取详情
    _kwrule_get_detail = function (editId, obj) {
        $.ajaxJsonGet(ajaxUrl.url1 + "/" + editId, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    var tempval = data.keyName;
                    data.keyNameArr = tempval.split(",");
                    jumi.template('wechat/reply_keyword_edit', data, function (tpl) {
                        obj.html("");
                        obj.html(tpl);
                    });


                    _kwrule_key_show();//关键字添加框 显示状态
                    _kwrule_key_add();//关键字添加
                    _kwrule_key_cancel();//关键字添加框 取消
                    _kwrule_detail_add();//关键字回复保存
                  //  qq.face.read_file("#keyface_face","#keyface_qqC","#keyface_text");//加载表情包 按钮 , 容器 , 编辑框
                    _kwrule_keyd_imgdel();//图片删除
                    _kwrule_key_close();//关键字删除
                    _reply_keyword_change();//添加 选项卡切换
                    _kwrule_setdata(data);//回填选项卡内容
                    _kwrule_detail_editsave();//编辑保存
                    _kwrule_detail_dgdel();//删除记录
                    _kdel_list_btn();//删除记录按钮
                }
            }
        });
    }
    _kwrule_setdata = function (data) {

                switch (data.replyContentType) {   //文本  1：文本 2：表情 3： 图文   4：图片 5：音乐 6：语音 7：视频 8：外网链接")
                    case 1:
                        var lc;
                        //注:旧数据localContent为null,兼容旧数据方法
                        if(!data.localContent){
                            lc = data.content;//旧数据
                        }else{
                            lc = data.localContent;//新的数据
                        }
                        $('#panel_bedd').show();
                        _setEditor('panel_beadd_text_a',lc);
                        _reply_tab_keywd(0);
                        break;
                    case 2:
                        break;
                    case 4:
                        $("#keyd_img").attr("src",data.picUrl);
                        $("#keyd_imgdiv").show();
                        $("#keyd_imgstatus").html("");
                        $("#keyd_imgstatus").html("1");
                        _reply_tab_keywd(1);//选中tab
                        break;
                }

    };
    var _reply_tab_keywd = function (index) {
        localStorage.setItem("keyword_index",index);
        var tabul = $("div[id='reply_content_keyword'] .m-panel-zdhf ul li");
        tabul.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
        var tabcon = $("div[id='reply_content_keyword'] .panel-list");
        tabcon.hide().eq(index).show();
    }

    //关键字回复保存
    _kwrule_detail_add = function () {
        $("div[id='reply_content_keyword'] .detail_save").click(function () {
            var wxKeyReplyCo = {};
            var keyword_index = localStorage.getItem("keyword_index");//固定回复下标保存  0:文本 1：表情 2：图片 3：音乐 4：语音 5：视频 6：外网连接  7：商城连接
            var ruleName = $("div[id='reply_content_keyword'] .rulename").find("input").val();
            var keyName = "";
            $("div[id='reply_content_keyword'] .keywd_newitem").each(function () {
                keyName = keyName + $(this).text() + ",";
            });
            if (isNull(keyName)) {
                keyName = keyName.substring(0, keyName.length - 1);
            }
           var msg = validateNull(ruleName,keyName,keyword_index);
            if(isNull(msg)){
                if(msg!=="1"){
                    _autoreply_add_msg(msg);
                }else{
                    _autoreply_add_msg("请填写 <font style='color:#f38925;'>回复</font>");
                }
                return;
            }
            wxKeyReplyCo.ruleName = ruleName;
            wxKeyReplyCo.keyName = keyName;
            switch (keyword_index) {
                case "0":
                    wxKeyReplyCo.replyContentType = 1; //文本   // replyContentType 1：文本 2：表情 3： 商城连接   4：图片  5：音乐 6：语音 7：视频 8：外网链接
                    String.prototype.replaceAll = function(s1,s2){
                        return this.replace(new RegExp(s1,"gm"),s2);
                    }
                    var ue = UE.getEditor('panel_beadd_text_a');
                    String.prototype.replaceAll = function (FindText, RepText) {
                        regExp = new RegExp(FindText,'g');
                        return this.replace(regExp, RepText);
                    }
                    var str = ue.getContent();
                    wxKeyReplyCo.localContent =  str;
                    var reg1 = /(textvalue=")(.*?)(")/;
                    var reg2 = /(target=")(.*?)(")/;
                    var result = str.replaceAll(reg1,'');
                    var result = result.replaceAll(reg2,'');
                    var result = result.replaceAll('</p>','~~');
                    result = result.replaceAll('&nbsp;','　');
                    result = result.replaceAll('<a','@@');
                    result = result.replaceAll('<br/>','%%');
                    result = result.replaceAll('</a>','&&');
                    var content = result.replace(/<\/?.+?>/g,"");
                    content = content.replaceAll('@@','<a');
                    content = content.replaceAll('&&','</a>');
                    content = content.replaceAll('~~','\n');
                    content = content.replaceAll('%%','\n');
                    wxKeyReplyCo.content = content;
                    break;
                case "1":
                    wxKeyReplyCo.replyContentType = 4; //4：图片
                    wxKeyReplyCo.picUrl = $("#keyd_img").attr("src");
                    break;
                case "2":

                    break;
                case "6":
                    wxKeyReplyCo.replyContentType = 8; //8：外网链接
                    wxKeyReplyCo.content = $("#keyd_outsidelink").val();
                    break;
             /*   case "7":
                    wxKeyReplyCo = _keyword_savelink(wxKeyReplyCo,"#keyword_resultType","#keyword_linkid","#keyword_shoplink");
                    wxKeyReplyCo.replyContentType = 3;//商城连接*/
            }
            var data = JSON.stringify(wxKeyReplyCo);
            $.ajaxJsonPut(ajaxUrl.url1, data, {
                "done": function (res) {
                    if (res.code === 0) {
                        _kwrule_list_refresh(); //规则列表
                        var dm = new dialogMessage({
                            type: 1,
                            title: '操作提醒',
                            fixed: true,
                            msg: "恭喜您，操作成功",
                            isAutoDisplay: false

                        });
                        dm.render();
                    } else {
                        var dm = new dialogMessage({
                            type: 2,
                            title: '操作提醒',
                            fixed: true,
                            msg: '对不起，操作失败',
                            isAutoDisplay: false

                        });
                        dm.render();
                    }
                },
                "fail": function (res) {
                }
            });

        });
    };
    //商城连接保存
    var _keyword_savelink = function (wxKeyReplyo,resultType,linkid,shoplink) {
        var rtype = $(resultType).val();
        var urlid = $(linkid).text();
        var html = $(shoplink).html();
        wxKeyReplyo.localContent =html;  //回填显示
        if(rtype==="项目图文") {
            wxKeyReplyo.imgTextType = 2;    //1、微信图文   2、项目图文     3、乐享图文  4、微博图文
            wxKeyReplyo.imgTextId = urlid;
        }else if(rtype==="微信图文") {
            wxKeyReplyo.imgTextType = 1;
            wxKeyReplyo.imgTextId = urlid;
        }else{
            var newhtml =  $(shoplink).clone();
            newhtml.find("input").remove();
            newhtml.find("span").remove();
            newhtml = newhtml.html();
            wxKeyReplyo.content = newhtml.trim();    //文本
        }

        return wxKeyReplyo;
    }
    var _autoreply_add_msg =function(info){
        var dm = new dialogMessage({
            type: 3,
            title: '操作提醒',
            fixed: true,
            msg: info,
            isAutoDisplay: false
        });
        dm.render();
        setTimeout(function () {
            if(isNull(dialog.get("dialog_1"))) {
                dialog.get("dialog_1").close().remove();
            }
        }, 3000);
    };
    //验证
    function  validateNull(ruleName,keyName,keyword_index){
        var msg = "";
        var val ="";
        if(!isNull(ruleName)){
            msg="请输入 <font style='color:#f38925;'>规则名</font>";
            return msg;
        }
        if(!isNull(keyName)){
            msg="请输入 <font style='color:#f38925;'>关键字</font>";
            return msg;
        }
        switch (keyword_index) {// keyword_index 1：文本 2：表情 3： 图文   4：图片  5：音乐 6：语音 7：视频 8：外网链接
            case "0":
                var ue = UE.getEditor('panel_beadd_text_a');
                var val = ue.getContent();
                if(!isNull(val)){
                    msg="1";
                }
                break;
            case "1":
                break;
            case "2":
                val =  $("#keyd_img").attr("src");
                if(!isNull(val)){
                    msg="1";
                }
                break;
            case "6":
                val = $("#keyd_outsidelink").val();
                if(!isNull(val)){
                    msg="1";
                }
                break;
        }
        return msg;
    }


    /**
     * 判断是否 为空
     * @param {Object} data
     */
    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    }

    // 删除关键字回复图片
    var _kwrule_keyd_imgdel = function () {
        $("#keyd_imgdel").click(function () {
            $("#keyd_imgstatus").html("");
            $("#keyd_imgstatus").html("0");
            $("#keyd_imgdiv").hide();
            $("#keyd_img").attr("src", THIRD_URL + '/css/pc/img/no_picture.png');
        });
    }
    //编辑保存
    _kwrule_detail_editsave = function () {
        $("div[id='reply_content_keyword'] .detail_editsave").click(function () {
            var id = $(this).attr("id");
            var wxKeyReplyUo = {};
            var keyword_index = localStorage.getItem("keyword_index");//固定回复下标保存  0:文本 1：表情 2：图片 3：音乐 4：语音 5：视频 6：外网连接  7：商城连接
            var ruleName = $("div[id='reply_content_keyword'] .rulename").find("input").val();
            var keyName = "";
            $("div[id='reply_content_keyword'] .keywd_newitem").each(function () {
                keyName = keyName + $(this).text() + ",";
            });
            if (isNull(keyName)) {
                keyName = keyName.substring(0, keyName.length - 1);
            }
            var msg = validateNull(ruleName,keyName,keyword_index);
            if(isNull(msg)){
                if(msg!=="1"){
                    _autoreply_add_msg(msg);
                }else{
                    _autoreply_add_msg("请填写 <font style='color:#f38925;'>回复</font>");
                }
                return;
            }
            wxKeyReplyUo.id=id;
            wxKeyReplyUo.ruleName = ruleName;
            wxKeyReplyUo.keyName = keyName;
            switch (keyword_index) {
                case "0":
                    wxKeyReplyUo.replyContentType = 1; //文本   // replyContentType 1：文本 2：表情 3： 商城连接   4：图片  5：音乐 6：语音 7：视频 8：外网链接
                    String.prototype.replaceAll = function(s1,s2){
                        return this.replace(new RegExp(s1,"gm"),s2);
                    }
                    var ue = UE.getEditor('panel_beadd_text_a');
                    String.prototype.replaceAll = function (FindText, RepText) {
                        regExp = new RegExp(FindText,'g');
                        return this.replace(regExp, RepText);
                    }
                    var str = ue.getContent();
                    wxKeyReplyUo.localContent =  str;
                    var reg1 = /(textvalue=")(.*?)(")/;
                    var reg2 = /(target=")(.*?)(")/;
                    var result = str.replaceAll(reg1,'');
                    var result = result.replaceAll(reg2,'');
                    var result = result.replaceAll('</p>','~~');
                    result = result.replaceAll('&nbsp;','　');
                    result = result.replaceAll('<a','@@');
                    result = result.replaceAll('<br/>','%%');
                    result = result.replaceAll('</a>','&&');
                    var content = result.replace(/<\/?.+?>/g,"");
                    content = content.replaceAll('@@','<a');
                    content = content.replaceAll('&&','</a>');
                    content = content.replaceAll('~~','\n');
                    content = content.replaceAll('%%','\n');
                    wxKeyReplyUo.content = content;
                    break;
                case "1":
                    wxKeyReplyUo.replyContentType = 4; //4：图片
                    wxKeyReplyUo.picUrl = $("#keyd_img").attr("src");
                    break;
                case "2":
                    break;
                case "6":
                    wxKeyReplyUo.replyContentType = 8; //8：外网链接
                    wxKeyReplyUo.content = $("#keyd_outsidelink").val();
                    break;
                case "7":
                  /*  wxKeyReplyUo = _keyword_savelink(wxKeyReplyUo,"#keyword_resultType","#keyword_linkid","#keyword_shoplink");
                    wxKeyReplyUo.replyContentType = 3;//商城连接*/
            }
            var data =JSON.stringify(wxKeyReplyUo);
            $.ajaxJson(ajaxUrl.url1, data, {
                "done": function (res) {
                    if (res.code === 0) {
                        _kwrule_list_refresh(); //规则列表
                        var dm = new dialogMessage({
                            type: 1,
                            title: '操作提醒',
                            fixed: true,
                            msg: "恭喜您，操作成功",
                            isAutoDisplay: false

                        });
                        dm.render();
                    } else {
                        var dm = new dialogMessage({
                            type: 2,
                            title: '操作提醒',
                            fixed: true,
                            msg: '对不起，操作失败',
                            isAutoDisplay: false

                        });
                        dm.render();
                    }
                },
                "fail": function (res) {
                }
            });

        });
    };
    //定位选项卡及当前页
    _kwrule_list_refresh = function(){
      var index = 1;//自动回复下标保存
        var tabul = $(".m-tab ul li");
        tabul.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
        $(".panel-hidden").hide().eq(index).show();
        _kwrule_list_cur(); //规则列表

    };
    _kdel_list_btn = function(){
        $('div[id^="belowDelete_"]').click(function(event){
            event.stopPropagation();
            var div = $(this).parent();
            var id = div.attr('id').replace("edititem_", "");
            jumi.template('common/delmsg/del_message', function (tpl) {
                var wx_del = dialog({
                    title: '操作提醒',
                    content: tpl,
                    zIndex: 10000,
                    width: 400,
                    id: 'dialog_wxdel',
                    onclose: function () {
                        dialog.get("dialog_wxdel").close().remove();
                    }
                });
                wx_del.showModal();
                _kwrule_detail_editdel(id);
                _kwrule_detail_delcancel();
            });
        })
    }
    _kwrule_detail_dgdel =function () {
        $("div[id='reply_content_keyword'] .detail_editdel").click(function () {
            var id = $(this).attr("id").replace("del_", "");
            jumi.template('common/delmsg/del_message', function (tpl) {
                var wx_del = dialog({
                    title: '操作提醒',
                    content: tpl,
                    zIndex: 10000,
                    width: 400,
                    id: 'dialog_wxdel',
                    onclose: function () {
                        dialog.get("dialog_wxdel").close().remove();
                    }
                });
                wx_del.showModal();
                _kwrule_detail_editdel(id);
                _kwrule_detail_delcancel();
            });
        });
    }
    //取消操作
    var _kwrule_detail_delcancel = function(){
        $("#makesure_canl").click(function () {
            dialog.get("dialog_wxdel").close().remove();
        });
    }
    //删除详情
    _kwrule_detail_editdel = function (id) {
        $("#makesure_del").click(function () {
            $.ajaxJsonDel(ajaxUrl.url1+"/"+id, {
                "done": function (res) {
                    if (res.code === 0) {
                        dialog.get("dialog_wxdel").close().remove();
                        _kwrule_list_refresh(); //规则列表
                        var dm = new dialogMessage({
                            type: 1,
                            title: '操作提醒',
                            fixed: true,
                            msg: "恭喜您，操作成功",
                            isAutoDisplay: false

                        });
                        dm.render();
                    } else {
                        var dm = new dialogMessage({
                            type: 2,
                            title: '操作提醒',
                            fixed: true,
                            msg: '对不起，操作失败',
                            isAutoDisplay: false

                        });
                        dm.render();
                    }
                },
                "fail": function (res) {
                }
            });
        });
    }
    //新增规则取消或删除
    _kwrule_detail_del = function () {
        $("div[id='reply_content_keyword'] .detail_del").click(function () {
            $('#add_keyword').hide();
            $('#add_keyword').html("");
            keyw_tag = 0; //置为隐藏
        });
    }

    return {
        init: _init,
        kdel_list_btn:_kdel_list_btn,
        kwrule_add_show: _kwrule_add_show,//关键字--添加规则--显示隐藏
        kwrule_biglist_status: _kwrule_biglist_status//列表收缩
    };
})();