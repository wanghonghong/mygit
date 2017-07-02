/**
 * Created by wxz on 16/10/31.
 */
CommonUtils.regNamespace("activity", "reply");

activity.reply = (function() {

//////自动回复
    //被添加关注回复切换
    var _act_beaddreply_change = function () {
        //被添加关注回复下标保存  0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接  7：商城连接
        //qq.face.read_file("#beadd_face","#beadd_qqC","#beadd_text");//加载表情包 按钮 , 容器 , 编辑框
        var tabul = $("div[id='reply_content_beadd'] .m-panel-zdhf ul li");
        var jIndex = localStorage.getItem("beadd_index");
        tabul.eq(jIndex).addClass("z-sel").siblings().removeClass('z-sel');
        $("div[id='reply_content_beadd'] .panel-list-auto").hide().eq(jIndex).show();
        tabul.click(function () {
            var index = $(this).index();
            localStorage.setItem("beadd_index", index);//被添加关注回复下标保存 0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接  7：商城连接
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $("div[id='reply_content_beadd'] .panel-list-auto").hide().eq(index).show();
            if (index === 0) {
                $('#panel_bedd').show();
                _setEditor('panel_beadd_text');
                $("#beadd_qqC").hide(); //表情 隐藏
                localStorage.setItem("#beadd_qqC",0); //设置表情显示标识
            }
            if(index===1){
                $('#panel_bedd').hide();
                $("div[id='reply_content_beadd'] .panel-list-auto").eq(index-1).show(); //表情与文本共用文本框
                $("div[id='reply_content_beadd'] .panel-list-auto").eq(index).hide();
            }
            if (index === 2) {
                $('#panel_bedd').hide();
                $("#beadd_qqC").hide(); //表情 隐藏
                localStorage.setItem("#beadd_qqC",0); //设置表情显示标识
                var status = $("#beadd_imgstatus").html();
                if (status === "0") {
                    _reply_img('#beadd_img', '#newmass_imgdiv', '#beadd_imgstatus');
                    _reply_beadd_imgdel();
                }
                _reply_beadd_imgdel();
            }
        })
    };



    ///////图片start
    var _reply_img = function (id, showid, imgstatus) {
        var d = new Dialog({
            context_path: CONTEXT_PATH, //请求路径,  必填
            resType: 1,//图片1，视频2，语音3   必填
            callback: function (url) {
                $(id).attr('src', url);
                $(showid).show();
                $(imgstatus).html("");
                $(imgstatus).html("1");
            }
        });
        d.render();
    }
    // 删除被添加回复图片
    var _reply_beadd_imgdel = function () {
        $("#beadd_imgdel").click(function () {
            $("#beadd_imgstatus").html("");
            $("#beadd_imgstatus").html("0");
            $("#newmass_imgdiv").hide();
            $("#beadd_img").attr("src", CONTEXT_PATH + '/css/pc/img/no_picture.png');
        });
    }
    ///////图片end

    //被添加关注回复保存开始
    var _act_replybeadd_click = function (callback) {
        _act_reply_bdadd_save(callback);
    }

    var _act_reply_bdadd_save = function (callback) {
        var url = CONTEXT_PATH + '/content/reply';//被添加关注回复
        var beadd_index = localStorage.getItem("beadd_index");//被添加关注回复下标保存  0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接
        if(beadd_index==null){
            beadd_index = "0";
            localStorage.setItem('beadd_index',beadd_index);
        }
        var beadd_indexfortxt = localStorage.setItem("beadd_indexfortxt",beadd_index);
        var wxReplyCo = {};
        var tempid = '';
        wxReplyCo.replyType = 1; //1：被添加关注回复    2：固定回复
        switch (beadd_index) {
            case "0":
                wxReplyCo.replyContentType = 1; //文本   // replyContentType 1：文本 2：表情 3： 商城连接   4：图片  5：音乐 6：语音 7：视频 8：外网链接
                String.prototype.replaceAll = function(s1,s2){
                    return this.replace(new RegExp(s1,"gm"),s2);
                }
                var ue = UE.getEditor('panel_beadd_text');
                String.prototype.replaceAll = function (FindText, RepText) {
                    regExp = new RegExp(FindText,'g');
                    return this.replace(regExp, RepText);
                }
                var str = ue.getContent();
                wxReplyCo.localContent =  str;
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
                wxReplyCo.content = content;
                localStorage.setItem('beadd_index',0);
                break;
            case "1":
                break;
            case "2":
                wxReplyCo.replyContentType = 4; //4：图片
                wxReplyCo.picUrl = $("#beadd_img").attr("src");
                localStorage.setItem('beadd_index',2);
                break;
            case "5":
                wxReplyCo.replyContentType = 8; //8：外网链接
                wxReplyCo.content = $("#beadd_outsidelink").val();
                break;
        }
        var data = JSON.stringify(wxReplyCo);
        $.ajaxJsonPut(url, data, {
            "done": function (res) {
                if (res.code === 0) {
                    tempid = res.data.data;
                    callback(tempid);
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

    };
    //被添加关注回复--标题
    var _act_beaddreply_text=function () {
        var index = localStorage.getItem("beadd_indexfortxt");////被添加关注回复下标保存  0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接
        var txt ="";
        switch (index) {
            case "0":
                txt ="文本"; //文本
                break;
            case "1":
                break;
            case "2":
                txt ="图片";
                break;
            case "3":
                txt ="音频";
                break;
            case "4":
                txt ="视频";
                break;
            case "5":
                txt ="外网连接";
                break;
        }
        return txt;
    };
    //编辑器
    var _setEditor = function(id,data){
        var id = id;
        var data = data||'';
        UE.delEditor(id);
        ue = UE.getEditor(id, {
            toolbars: [
                ['redo','link']
            ],
            initialFrameHeight:150,
            zIndex:1200
        });
        var ue = UE.getEditor(id);
        ue.ready(function() {
            if(data){
                ue.setContent(data);
            }
            setTimeout(function(){
                $('.edui-for-image').remove();
                $('.edui-for-redo').remove();
            },300)
        })
    }
    //被添加关注回复回填开始
    var _act_set_reply_beadd = function () {
        var url = CONTEXT_PATH + '/content/reply_list';//自动回复
        var data = null;
        $.ajaxJsonGet(url, {
            "done": function (res) {
                if (res.code === 0) {
                    data = res.data;
                    localStorage.setItem("rpl_datalst", JSON.stringify(data));//被添加关注回复  固定回复 列表
                    _act_reply_setdata_beadd(data);

                }
            },
            "fail": function (res) {
            }
        });

    }

    var _act_reply_setdata_beadd = function (data) {
        for (var i = 0; i < data.length; i++) {
            if (data[i].replyType === 1) {//被添加关注回复

                switch (data[i].replyContentType) {  // wxReplyCo.replyContentType=1; //文本   // replyContentType 1：文本 2：表情 3： 图文   4：图片  5：音乐 6：语音 7：视频 8：外网链接
                    case 1:
                        var lc = data[i].localContent;
                        $('#panel_bedd').show();
                        _setEditor('panel_beadd_text',lc);
                        _act_reply_tab_beadd(0);
                        localStorage.setItem("beadd_indexfortxt",0);
                        break;
                    case 2:
                        break;
                    case 4:
                        $('#panel_bedd').hide();
                        $("#beadd_img").attr("src", data[i].picUrl);
                        $("#newmass_imgdiv").show();
                        $("#beadd_imgstatus").html("");
                        $("#beadd_imgstatus").html("1");
                        _act_reply_tab_beadd(2);
                        _reply_beadd_imgdel();
                        localStorage.setItem("beadd_indexfortxt",2);
                        break;
                }
            }
        }
    }

    var _act_reply_tab_beadd = function (index) {
        var tabul = $("div[id='reply_content_beadd'] .m-panel-zdhf ul li");
        tabul.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
        var tabcon = $("div[id='reply_content_beadd'] .panel-list-auto");
        tabcon.hide().eq(index).show();
    }
    //////被添加关注回复

    return {
        act_beaddreply_change:_act_beaddreply_change,
        act_replybeadd_click:_act_replybeadd_click,
        act_beaddreply_text:_act_beaddreply_text,
        act_set_reply_beadd:_act_set_reply_beadd
    };
})();