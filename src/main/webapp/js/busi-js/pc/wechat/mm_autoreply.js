/**
 * Created by wxz on 2016/9/27
 * 自动回复
 */
CommonUtils.regNamespace("mm", "autoreply");
mm.autoreply = (function () {
    var shoplinkid = 0;
    var tabpictxt = null;
    var dg_outsidelink = null;//外网链接对话框
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/content/reply',//自动回复
        url2: CONTEXT_PATH + '/content/reply_list',//自动回复
        url3: CONTEXT_PATH + '/content/wxaccount',//固定回复，尾链接 1：是 0：否
        url4: CONTEXT_PATH + '/content/account'//固定回复，尾链接 1：是 0：否

    };
    //初始化
    var _init = function () {
        _reply_tabchange();//自动回复切换
        _reply_beadd_cityLink();
        _reply_beadd_imgdel();// 删除被添加回复图片
        //  _reply_fixed_imgdel();// 删除自动回复图片

    };

    //自动回复切换
    var _reply_tabchange = function () {
        var tabul = $(".m-tab ul li");
        $("div[id='reply_content_beadd'] .panel-list").hide().eq(0).show();
        qq.face.read_file("#beadd_face","#beadd_qqC","#beadd_text");//加载表情包 按钮 , 容器 , 编辑框
        localStorage.setItem("replytype", 1);//自动回复下标保存  0:被添加关注回复 1:固定回复  2:关键字回复 3:尾链接
        //_reply_beadd_reread();//回填
        tabul.click(function () {
            var index = $(this).index();
            var num = index+1;
            localStorage.setItem("replytype", num);//自动回复下标保存
            tabul.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq(index).show();
            if (index === 0) {
                jumi.template('wechat/reply_fixed', function (tpl) {
                    $('#reply_content_fixed').html(tpl);
                })
                _reply_fixed_change();
                _reply_fixed_cityLink();
                qq.face.read_file("#fixed_face","#fixed_qqC","#fixed_text");//加载表情包 按钮 , 容器
                _reply_fixed_reread();//固定回复回填
                _reply_fixed_OpenStatus();//开启\关闭状态回填
            }
            if (index === 1) { //关键字回复
                jumi.template('wechat/reply_keyword', function (tpl) {
                    $('#reply_content_keyword').html(tpl);
                    autor.autoreply.kdel_list_btn();
                })
                _reply_keyword_change();
            }
            if (index === 2) {
                jumi.template('wechat/reply_tlink', function (tpl) {
                    $('#reply_content_tlink').html(tpl);
                })
                _reply_tlink_save();//尾链接保存
                _reply_tlink_setdata();//尾链接回填
                _reply_keyword_OpenStatus();////尾链接开启、关闭回填
            }

        }).eq(0).click();
    };

    //ajax获取后端菜单数据
    var _reply_fixed_OpenStatus = function () {

        $.ajaxJsonGet(ajaxUrl.url4, {
            done: function (res) {
                if (res.code == 0) {
                    var val = res.data.isFixedReply;//1开启 0关闭
                    var obj = $("#switch1");
                    if (val === 0) {
                        obj.parent().removeClass("data-on").addClass("data-off");
                        obj.val("0");
                        obj.siblings("label").text("关闭");
                    } else {
                        obj.parent().removeClass("data-off").addClass("data-on");
                        obj.val("1");
                        obj.siblings("label").text("开启");

                    }

                } else {
                    //  alert(res.msg);
                }
            }
        })
    }

    //被添加关注回复-商城链接
    var _reply_beadd_cityLink = function () {
        $("#reply_content_beadd .m-mall-link").on('mouseover', function () {
            $("#reply_content_beadd div[id='citylink1']").show();
            //   _reply_shopmlink_show("#reply_content_beadd","citylink1");
            //被添加关注回复-商城链接
        });
        $("#reply_content_beadd .m-mall-link").on('mouseout', function () {
            $("#reply_content_beadd div[id='citylink1']").hide();
        });

    };
    //固定回复-商城链接
    var _reply_fixed_cityLink = function () {
        $("#reply_content_fixed .m-mall-link").click(function () {
            $("#reply_content_fixed div[id='citylink2']").fadeToggle();
        });
    };
    //关键字回复-商城链接
    var _reply_keyword_cityLink = function () {
        $("#reply_content_keyword .m-mall-link").click(function () {
            $("#reply_content_keyword div[id='citylink3']").fadeToggle();
        });
    };
    //被添加关注回复切换
    var _reply_beadd_change = function () {
        localStorage.setItem("beadd_index", 0);//被添加关注回复下标保存  0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接  7：商城连接
        var tabul = $("div[id='reply_content_beadd'] .m-panel-zdhf ul li");
        tabul.eq(0).addClass("z-sel").siblings().removeClass('z-sel');
        tabul.click(function () {
            var index = $(this).index();
            localStorage.setItem("beadd_index", index);//被添加关注回复下标保存 0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接  7：商城连接
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $("div[id='reply_content_beadd'] .panel-list").hide().eq(index).show();
            if (index === 0) {
                $("#beadd_qqC").hide(); //表情 隐藏
                localStorage.setItem("#beadd_qqC",0); //设置表情显示标识
            }
            if(index===1){
                localStorage.setItem("beadd_index", 0);
                $("div[id='reply_content_beadd'] .panel-list").eq(index-1).show(); //表情与文本共用文本框
                $("div[id='reply_content_beadd'] .panel-list").eq(index).hide();
            }
            if (index === 2) {
                $("#beadd_qqC").hide(); //表情 隐藏
                localStorage.setItem("#beadd_qqC",0); //设置表情显示标识
                var status = $("#beadd_imgstatus").html();
                if (status === "0") {
                    _reply_img('#beadd_img', '#newmass_imgdiv', '#beadd_imgstatus');
                }
            }
            if(index===3){ //音频
                _mm_friendmsg(3);
                // _mm_voice();
            }
            if(index===4){ //视频
                _mm_friendmsg(4);
                // _mm_video();
            }
            if (index === 5) {
                $("#beadd_qqC").hide(); //表情 隐藏
                localStorage.setItem("#beadd_qqC",0); //设置表情显示标识
                if ($("#beadd_outsidelink").val() === "") {
                    _reply_outsidelink_dialog();//外网链接
                    _reply_outsidelink_makesure();//外网链接确定
                }
            }
            if (index === 7) {
                /*   $("#beadd_qqC").hide(); //表情 隐藏
                 localStorage.setItem("#beadd_qqC",0); //设置表情显示标识
                 _reply_shopurl("#beadd_resultType","#beadd_resultUrl","#beadd_linkid");*/
            }

        });
    };


    //友情提示-未开发功能
    var _mm_friendmsg=function (index) {
        switch(index){
            case 3:
            case 4:
                var dm = new dialogMessage({
                    type:2,
                    title:'提示',
                    fixed:true,
                    msg:'功能还在开发中',
                    isAutoDisplay:false

                });
                dm.render();
                break;

        }
    };

    var _reply_shopurl =function (resultType,resultUrl,linkid) {
        var shopId = $("#shopId").val();
        menuDialog.Menu.initPage({
            shop_id:shopId,
        },function(menu){
            menu.getUrl(function(link){
                var url;
                switch(link.link_type){
                    case '1':
                        url = DOMAIN+link.link_url+"?shopId="+link.shop_id;
                        break;
                    case '2':
                        url = DOMAIN+link.link_url+'/'+link.link_id+"?shopId="+link.shop_id;
                        break;
                }
                var beadd_type = $(resultType);
                var beadd_url = $(resultUrl);
                var beadd_id = $(linkid);
                beadd_type.val("");
                beadd_url.attr("href","");
                beadd_url.text("");
                beadd_id.text("");

                beadd_type.css("display","static");
                beadd_type.val(link.link_name);
                beadd_url.attr("href",url);
                if(isNull(link['data-name'])) {
                    beadd_url.text(link['data-name']);
                }else{
                    beadd_url.text(url);
                }
                beadd_id.text(link['link_id']);

            });
        });
    };


    ///////图片start
    var _reply_img = function (id, showid, imgstatus) {
        var d = new Dialog({
            context_path: CONTEXT_PATH, //请求路径,  必填
            resType: 1,//图片1，视频2，语音3   必填
            callback: function (url) {
                var url = jumi.picParse(url,0);
                $(id).attr('src', url);
                $(showid).show();
                $(imgstatus).html("");
                $(imgstatus).html("1");
            }
        });
        d.render();
    }
    ///////图片end
    //////音频
    var _mm_voice = function(id){
        var d = new Dialog({
            context_path:CONTEXT_PATH, //请求路径,  必填
            resType:3,//图片1，视频2，语音3   必填
            callback:function(url,node){
                $(id).attr('src',url);
            }
        });
        d.render();
    }
    //////音频
    //////视频
    var _mm_video = function(id){
        var d = new Dialog({
            context_path:CONTEXT_PATH, //请求路径,  必填
            resType:2,//图片1，视频2，语音3   必填
            callback:function(url,node){
                $(id).attr('src',url);
            }
        });
        d.render();
    }
    //////视频
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
            },300)
        })
    }
    //固定回复切换
    var _reply_fixed_change = function () {
        localStorage.setItem("fixed_index", 0);//固定回复下标保存
        var tabul = $("div[id='reply_content_fixed'] .m-panel-zdhf ul li");
        tabul.eq(0).addClass("z-sel").siblings().removeClass('z-sel');
        var tabcon = $("div[id='reply_content_fixed'] .panel-list");
        tabcon.hide().eq(0).show();
        tabul.click(function () {
            var index = $(this).index();
            localStorage.setItem("fixed_index", index);//固定回复下标保存   0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接   7：商城连接
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            tabcon.hide().eq(index).show();
            if (index === 0) {
                $('#panel_bedd').show();
                _setEditor('panel_beadd_text');
                $("#beadd_qqC").hide(); //表情 隐藏
                localStorage.setItem("#fixed_qqC",0); //设置表情显示标识
            }
            if(index===1){
                localStorage.setItem("fixed_index", 0);
                $("div[id='reply_content_fixed'] .panel-list").eq(index-1).show(); //表情与文本共用文本框
                $("div[id='reply_content_fixed'] .panel-list").eq(index).hide();
            }
            if (index === 2) {
                $("#fixed_qqC").hide(); //表情 隐藏
                localStorage.setItem("#fixed_qqC",0); //设置表情显示标识
                var status = $("#fixed_imgstatus").html();
                if (status === "0") {
                    _reply_img('#befixed_img', '#fixed_imgdiv', '#fixed_imgstatus');
                }
            }
        }).eq(0).click();
    };

    /**
     * 判断是否 为空
     * @param {Object} data
     */
    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    }

    //关键字回复切换
    var _reply_keyword_change = function () {
        $("div[id='reply_content_keyword'] .panel-list").hide().eq(0).show();
        var tabul = $("div[id='reply_content_keyword'] .m-panel-zdhf ul li");
        tabul.eq(0).addClass("z-sel").siblings().removeClass('z-sel');
        tabul.click(function () {
            var index = $(this).index();
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $("div[id='reply_content_keyword'] .panel-list").hide().eq(index).show();
            if (index === 1) {
                // alert(index);
            }
            if (index === 2) {
                //  alert(index);
            }
            if (index === 3) {
                // alert(index);
            }
        });
    };


//商城链接之我的、其他
    var _reply_dialog_menu = function (sltId, name) {
        shoplinkid = sltId;
        jumi.template('wechat/reply_shopmlink_menu', function (tpl) {
            var dg = dialog({
                title: name,
                content: tpl,
                zIndex: 100,
                width: 820,
                id: 'dialog_mehis',
                onshow: function () {
                    replygetMenulink("1");
                },
                onclose: function () {
                    dialog.get('dialog_mehis').close().remove();
                }
            });
            dg.showModal();
        })
    }
//商城链接之图文
    var _reply_dialog_tabpictxt = function (sltId, name) {
        shoplinkid = sltId;
        jumi.template('wechat/reply_shopmlink_tab', function (tpl) {
            var tabpictxt = dialog({
                title: name,
                content: tpl,
                zIndex: 100,
                width: 1000,
                id: 'dialog_pictxt',
                onshow: function () {
                    replygetMenulink("0");
                },
                onclose: function () {
                    dialog.get('dialog_pictxt').close().remove();
                }
            });
            tabpictxt.showModal();
        })
    }

//ajax获取后端菜单数据
    function replygetMenulink(tag) {
        var url = CONTEXT_PATH + '/menu/setMenuLink';
        $.ajaxJsonGet(url, '', {
            done: function (res) {
                if (res.code == 0) {
                    if (tag === "0") {//图文
                        _tabDataRender_pictxt(res.data);
                    }
                    if (tag === "1") {//我的、其他
                        _menuDataRender(res.data);
                    }
                } else {
                    //  alert(res.msg);
                }
            }
        })
    }

    var _tabDataRender_pictxt = function (json_data) {
        $('#shopml-details-pictxttab').html("");
        var shopId = "";// $("#shopId").val();
        var array_1 = _.where(json_data, {'parent_id': parseInt(shoplinkid)});
        var len1 = array_1.length;
        var tpl1 = '';
        for (var j = 0; j < len1; j++) {
            if (j === 0) {
                tpl1 += "<li class='z-sel' onClick=\"mm.autoreply.replygetUrlRequest('" + array_1[j].type + "','" + array_1[j].link_name + "','" + array_1[j].link_key + "','" + shopId + "')\">" +
                    array_1[j].link_name +
                    "</li>";
            } else {
                tpl1 += "<li onClick=\"mm.autoreply.replygetUrlRequest('" + array_1[j].type + "','" + array_1[j].link_name + "','" + array_1[j].link_key + "','" + shopId + "')\">" +
                    array_1[j].link_name +
                    "</li>";
            }
        }
        $('#shopml-details-pictxttab').append(tpl1);
        $('#shopml-details-pictxttab').find('li').on('click', function () {
            $('#shopml-details-pictxttab li').removeClass('z-sel');
            $(this).addClass('z-sel');
        });

    }

//后台菜单数据渲染
    var _menuDataRender = function (json_data) {
        $('#menu-details-content').html("");
        var shopId = "";// $("#shopId").val();
        var array_1 = _.where(json_data, {'parent_id': parseInt(shoplinkid)});
        var len1 = array_1.length;
        var tpl1 = '';
        for (var j = 0; j < len1; j++) {
            if (array_1[j].link_type == '1' && array_1[j].link_name != '二维码海报') {
                tpl1 += "<div class='u-sort1' onClick=\"mm.autoreply.replysetMLink('" + array_1[j].type + "','" + array_1[j].link_name + "','" + array_1[j].link_key + "','" + array_1[j].link_url + "','" + shopId + "')\" >" +
                    array_1[j].link_name +
                    "<input type='checkbox'  />" +
                    "<label class='iconfont icon-avoid' ></label>" +
                    "</div>";
            }
            if (array_1[j].link_name == '二维码海报' && array_1[j].link_type == '1') {
                tpl1 += "<div class='u-more1 active' onClick=\"mm.autoreply.replysetMLink('" + array_1[j].type + "','" + array_1[j].link_name + "','" + array_1[j].link_key + "','" + array_1[j].link_url + "','" + shopId + "')\" >" +
                    array_1[j].link_name +
                    "<span></span>" +
                    "</div>";
            }
        }
        $('#menu-details-content').append(tpl1);
        $('#menu-details-content').find('.u-sort1').on('mouseover', function () {
            $('.u-sort1').removeClass('active');
            $(this).addClass('active');
        });
        $('#menu-details-content').find('.u-sort1').on('mouseout', function () {
            $('.u-sort1').removeClass('active');

        })
    }
    /***
     * 根据不同的选择项显示不同的列表
     */
    var _replygetUrlRequest = function (type, name, key, shopId) {
        //  tabpictxt.height(800);

        if (name == "项目图文") {
            var newurl = "/imageText/";
            //   _itemtext_page_bar(type,name,newurl,key,shopId);
        }
        else if (name == "乐享图文") {
            var newurl = "/shop/lenjoyPhoto";
            //  _enjoytext_page_bar(type,name,newurl,key,shopId);
        }

    };
    var _replysetMLink = function (type, name, key, url, shopId) {
    };
////项目图文分页start
    /*var _itemtext_page_bar=function(type,name,newurl,key,shopId){
     var curl=CONTEXT_PATH + '/image_text/findAll/1';//ajax请求的链接
     var tmpdata={};
     tmpdata.pageSize=5;
     tmpdata.curPage=0;
     $('#text_pageToolbar').show();
     $("#page").children().remove();
     jumi.pagination('#text_pageToolbar',curl,tmpdata,function(res,curPage){

     if(res.code===0){
     //判断是否第一页
     var data = {
     items:res.data.items
     };
     if(curPage===0){
     data.isFirstPage = 1;
     }else{
     data.isFirstPage = 0;
     }
     // console.log(data);
     for(var i=0;i<data.items.length;i++){
     data.items[i].ultype=type;
     data.items[i].ulurlname=name;
     data.items[i].ulnewurl=newurl;
     data.items[i].ulkey=key;
     data.items[i].ulshopId=shopId;
     }
     jumi.template('menu/itemtext_list',data,function(tpl){
     $('#products').empty();
     $('#products').html(tpl);
     })
     }
     })
     }*/
////项目图文分页end

    //被添加关注回复保存开始
    var _reply_bdadd_save = function () {
        var replytype = localStorage.getItem("replytype");//自动回复下标 0:被添加关注回复 1:固定回复  2:关键字回复 3:尾链接
        var beadd_index = localStorage.getItem("beadd_index");//被添加关注回复下标保存  0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接
        var wxReplyCo = {};
        wxReplyCo.replyType = 1; //1：被添加关注回复    2：固定回复
        if (replytype === "0") {
            switch (beadd_index) {
                case "0":
                    wxReplyCo.replyContentType = 1; //文本   // replyContentType 1：文本 2：表情 3： 商城连接   4：图片  5：音乐 6：语音 7：视频 8：外网链接
                    wxReplyCo.content = $("#beadd_text").val();
                    break;
                case "1":
                    break;
                case "2":
                    wxReplyCo.replyContentType = 4; //4：图片
                    wxReplyCo.picUrl = $("#beadd_img").attr("src");
                    break;
                case "5":
                    wxReplyCo.replyContentType = 8; //8：外网链接
                    wxReplyCo.content = $("#beadd_outsidelink").val();
                    break;
                /* case "7":
                 wxReplyCo = _beadd_savelink(wxReplyCo,"#beadd_resultType","#beadd_linkid","#beadd_shoplink");
                 wxReplyCo.replyContentType = 3;//商城连接*/

            }
            var data = JSON.stringify(wxReplyCo);
            $.ajaxJsonPut(ajaxUrl.url1, data, {
                "done": function (res) {
                    if (res.code === 0) {
                        var dm = new dialogMessage({
                            type: 1,
                            title: '操作提醒',
                            fixed: true,
                            msg: "恭喜您，操作成功",
                            isAutoDisplay: false

                        });
                        dm.render();
                    } else {
                        //alert("系统内部错误！");
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

        }
    };
//商城连接保存
    var _beadd_savelink = function (wxReplyCo,resultType,linkid,shoplink) {
        var rtype = $(resultType).val();
        var urlid = $(linkid).text();
        var html = $(shoplink).html();
        wxReplyCo.localContent =html;  //回填显示

        if(rtype==="项目图文") {
            wxReplyCo.imgTextType = 2;    //1、微信图文   2、项目图文     3、乐享图文  4、微博图文
            wxReplyCo.imgTextId = urlid;
        }else if(rtype==="微信图文") {
            wxReplyCo.imgTextType = 1;
            wxReplyCo.imgTextId = urlid;
        }else{
            var newhtml =  $(shoplink).clone();
            newhtml.find("input").remove();
            newhtml.find("span").remove();
            newhtml = newhtml.html();
            wxReplyCo.content = newhtml.trim();    //文本
        }

        return wxReplyCo;
    }
    //被添加关注回复保存结束

    //固定回复保存开始
    var _reply_fixed_save = function () {
        var replytype = localStorage.getItem("replytype");//自动回复下标 0:被添加关注回复 1:固定回复  2:关键字回复 3:尾链接
        var fixed_index = localStorage.getItem("fixed_index");//固定回复下标保存   0:文本 1：表情 2：图片 3：音频  4：视频 5：外网连接   7：商城连接
        var wxReplyCo = {};
        wxReplyCo.replyType = 2; //1：被添加关注回复    2：固定回复
        if (replytype === "1") {
            switch (fixed_index) {
                case "0":
                    // wxReplyCo.replyContentType = 1; //文本   // replyContentType 1：文本 2：表情 3： 图文   4：图片  5：音乐 6：语音 7：视频 8：外网链接
                    // wxReplyCo.content = $("#fixed_text").val();
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
                    wxReplyCo.picUrl = $("#befixed_img").attr("src");
                    break;
                case "5":
                    wxReplyCo.replyContentType = 8; //8：外网链接
                    wxReplyCo.content = $("#fixed_outsidelink").val();
                    break;
                /*case "7":
                 wxReplyCo = _beadd_savelink(wxReplyCo,"#fixed_resultType","#fixed_linkid","#fixed_shoplink");
                 wxReplyCo.replyContentType = 3;//商城连接*/
            }
            var data = JSON.stringify(wxReplyCo);
            $.ajaxJsonPut(ajaxUrl.url1, data, {
                "done": function (res) {
                    if (res.code === 0) {

                        var dm = new dialogMessage({
                            type: 1,
                            title: '操作提醒',
                            fixed: true,
                            msg: "恭喜您，操作成功",
                            isAutoDisplay: false

                        });
                        dm.render();
                    } else {
                        //alert("系统内部错误！");
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
        }
    }
    //固定回复保存结束
    //固定回复回填开始
    var _reply_fixed_reread = function () {

        var replytype = localStorage.getItem("replytype");//自动回复下标 0:被添加关注回复 1:固定回复  2:关键字回复 3:尾链接
        // var fixed_index = localStorage.getItem("fixed_index");//固定回复下标保存  0:文本 1：表情 2：图片 3：音乐 4：语音 5：视频 6：外网连接  7：商城连接

        if (replytype === "1") {
            var data = null;

            $.ajaxJsonGet(ajaxUrl.url2, {
                "done": function (res) {
                    if (res.code === 0) {
                        data = res.data;
                        localStorage.setItem("rpl_datalst", JSON.stringify(data));//被添加关注回复  固定回复 列表
                        _reply_setdata_fixed(data);
                    }
                },
                "fail": function (res) {
                }
            });
            // }
        }

    }
    //固定回复填充
    var _reply_setdata_fixed = function (data) {
        for (var i = 0; i < data.length; i++) {
            if (data[i].replyType === 2) {//固定回复

                switch (data[i].replyContentType) {  // wxReplyCo.replyContentType=1; //文本   // replyContentType 1：文本 2：表情 3： 图文   4：图片  5：音乐 6：语音 7：视频 8：外网链接
                    case 1:
                        var lc = data[i].localContent;
                        $('#panel_bedd').show();
                        var ue = UE.getEditor('panel_beadd_text');
                        ue.ready(function() {
                            if(data){
                                ue.setContent(lc);
                            }
                        })
                        _reply_tab_fixed(0);
                        break;
                    case 2:
                        break;
                    /*  case 3:
                     $('#fixed_shoplink').html("");
                     $('#fixed_shoplink').html(data[i].localContent);
                     _reply_tab_fixed(7);
                     break;*/
                    case 4:
                        $("#befixed_img").attr("src", data[i].picUrl);
                        $("#fixed_imgdiv").show();
                        $("#fixed_imgstatus").html("");
                        $("#fixed_imgstatus").html("1");
                        _reply_tab_fixed(2);
                        break;
                    case 8:
                        $("#fixed_outsidelink").val(data[i].content);
                        _reply_tab_fixed(5);
                        break;
                }
            }
        }
    }

    var _reply_tab_fixed = function (index) {
        var tabul = $("div[id='reply_content_fixed'] .m-panel-zdhf ul li");
        tabul.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
        var tabcon = $("div[id='reply_content_fixed'] .panel-list");
        tabcon.hide().eq(index).show();
    }
    //固定回复回填结束
    //被添加关注回复回填开始
    var _reply_beadd_reread = function () {

        var replytype = localStorage.getItem("replytype");//自动回复下标 0:被添加关注回复 1:固定回复  2:关键字回复 3:尾链接
        // var fixed_index = localStorage.getItem("fixed_index");//固定回复下标保存  0:文本 1：表情 2：图片 3：音乐 4：语音 5：视频 6：外网连接  7：商城连接
        if (replytype === "0") {
            var data = null;
            $.ajaxJsonGet(ajaxUrl.url2, {
                "done": function (res) {
                    if (res.code === 0) {
                        data = res.data;
                        localStorage.setItem("rpl_datalst", JSON.stringify(data));//被添加关注回复  固定回复 列表
                        _reply_setdata_beadd(data);

                    }
                },
                "fail": function (res) {
                }
            });
            //  }
        }
    }

    var _reply_setdata_beadd = function (data) {
        for (var i = 0; i < data.length; i++) {
            if (data[i].replyType === 1) {//被添加关注回复

                switch (data[i].replyContentType) {  // wxReplyCo.replyContentType=1; //文本   // replyContentType 1：文本 2：表情 3： 图文   4：图片  5：音乐 6：语音 7：视频 8：外网链接
                    case 1:
                        $("#beadd_text").val(data[i].content);
                        _reply_tab_beadd(0);
                        break;
                    case 2:
                        break;
                    case 4:
                        $("#beadd_img").attr("src", data[i].picUrl);
                        $("#newmass_imgdiv").show();
                        $("#beadd_imgstatus").html("");
                        $("#beadd_imgstatus").html("1");
                        _reply_tab_beadd(2);
                        break;
                    case 8:
                        $("#beadd_outsidelink").val(data[i].content);
                        _reply_tab_beadd(5);
                        break;
                }
            }
        }
    }

    var _reply_tab_beadd = function (index) {
        var tabul = $("div[id='reply_content_beadd'] .m-panel-zdhf ul li");
        tabul.eq(index).addClass("z-sel").siblings().removeClass('z-sel');
        var tabcon = $("div[id='reply_content_beadd'] .panel-list");
        tabcon.hide().eq(index).show();
    }
    //被添加关注回复回填结束
    //开启固定回复
    var _reply_auto = function (id) {//是否开启自动回复(1：是 0：否) 1开启
        var val = $(id).val();
        if (val === "0") {
            $(id).parent().removeClass("data-off").addClass("data-on");
            $(id).val("1");
            $(id).siblings("label").text("开启");
        } else {

            $(id).parent().removeClass("data-on").addClass("data-off");
            $(id).val("0");
            $(id).siblings("label").text("关闭");
        }

        var wxPubAccountUo = {};
        wxPubAccountUo.isFixedReply = $(id).val();
        $.ajaxJson(ajaxUrl.url3, wxPubAccountUo, {
            "done": function (res) {
                if (res.code === 0) {

                } else {
                }
            },
            "fail": function (res) {
            }
        });
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
// 删除自动回复图片
    var _reply_fixed_imgdel = function () {
        $("#fixed_imgdel").click(function () {
            $("#fixed_imgstatus").html("");
            $("#fixed_imgstatus").html("0");
            $("#fixed_imgdiv").hide();
            $("#befixed_img").attr("src", CONTEXT_PATH + '/css/pc/img/no_picture.png');
        });
    }
    //外网链接对话框
    var _reply_outsidelink_dialog = function () {
        jumi.template('wechat/reply_outside', function (tpl) {
            dg_outsidelink = dialog({
                title: "外网连接",
                content: tpl,
                zIndex: 100,
                width: 661,
                id: 'dialog_out',
                onclose: function () {
                    dialog.get('dialog_out').close().remove();
                }
            });
            dg_outsidelink.showModal();
        })
    }
    //外网链接取值
    var _reply_outsidelink_makesure = function () {
        $("#outside_msure").click(function () {
            var replytype = localStorage.getItem("replytype");//自动回复下标 0:被添加关注回复 1:固定回复  2:关键字回复 3:尾链接
            var outs = $("#reply_outside").val();
            if (replytype === "0") {
                $("#beadd_outsidelink").val(outs);
            }
            if (replytype === "1") {
                $("#fixed_outsidelink").val(outs);
            }
            if(replytype === "2"){
                $("#keyd_outsidelink").val(outs);
            }
            dg_outsidelink.close().remove();
        });
    }

    //尾链接保存
    var _reply_tlink_save = function () {
        $("#save_tlink_btn").click(function () {
            var wxReplyCo = {};
            wxReplyCo.replyType = 3; //1：被添加关注回复    2：固定回复  3：尾链接
            wxReplyCo.content = $("#tlink_content").val();
            wxReplyCo.replyContentType = 1;
            $.ajaxJsonPut(ajaxUrl.url1, wxReplyCo, {
                "done": function (res) {
                    if (res.code === 0) {

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
//尾链接回填
    _reply_tlink_setdata = function () {
        $.ajaxJsonGet(ajaxUrl.url2, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = res.data;
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].replyType === 3) {//尾链接
                            $("#tlink_content").val(data[i].content);
                        }
                    }
                }
            },
            "fail": function (res) {
            }
        });
    };
//尾链接开启、关闭
    var _reply_tlink_auto = function (id) {//是否开启自动回复(1：是 0：否) 1开启
        var val = $(id).val();
        if (val === "0") {
            $(id).parent().removeClass("data-off").addClass("data-on");
            $(id).val("1");
            $(id).siblings("label").text("开启");
        } else {

            $(id).parent().removeClass("data-on").addClass("data-off");
            $(id).val("0");
            $(id).siblings("label").text("关闭");
        }

        var wxPubAccountUo = {};
        wxPubAccountUo.isLastLink = $(id).val();
        $.ajaxJson(ajaxUrl.url3, wxPubAccountUo, {
            "done": function (res) {
                if (res.code === 0) {

                } else {
                }
            },
            "fail": function (res) {
            }
        });
    };

    //尾链接开启，关闭回填
    var _reply_keyword_OpenStatus = function () {

        $.ajaxJsonGet(ajaxUrl.url4, {
            done: function (res) {
                if (res.code == 0) {
                    var val = res.data.isLastLink;//1开启 0关闭
                    var obj = $("#switch2");
                    if (val === 0) {
                        obj.parent().removeClass("data-on").addClass("data-off");
                        obj.val("0");
                        obj.siblings("label").text("关闭");
                    } else {
                        obj.parent().removeClass("data-off").addClass("data-on");
                        obj.val("1");
                        obj.siblings("label").text("开启");

                    }

                } else {
                    //  alert(res.msg);
                }
            }
        })
    }

    //关键字-规则新增
    var headerfade = 1; // 表头是否已经隐藏
    var _kwrule_addfor_show = function (idbtn) {
        $('#add_keyword').fadeToggle("fast");
        /*  if (headerfade == 1) {
         $('#add_keyword').fadeIn("fast", function () {
         headerfade = 0;
         });
         }
         if (headerfade == 0) {
         $('#add_keyword').fadeOut("fast", function () {
         headerfade = 1;
         }); // 二页隐藏菜单
         }*/

    }


    return {
        init: _init,
        reply_dialog_menu: _reply_dialog_menu,
        reply_dialog_tabpictxt: _reply_dialog_tabpictxt,
        replygetUrlRequest: _replygetUrlRequest,
        replysetMLink: _replysetMLink,
        reply_bdadd_save: _reply_bdadd_save,
        reply_fixed_save: _reply_fixed_save,
        reply_auto: _reply_auto,
        reply_fixed_imgdel: _reply_fixed_imgdel,
        reply_beadd_imgdel: _reply_beadd_imgdel,
        reply_tlink_auto: _reply_tlink_auto, //尾链接
        kwrule_addfor_show:_kwrule_addfor_show,//关键字--添加规则--显示隐藏
        reply_img:_reply_img,
        reply_outsidelink_dialog:_reply_outsidelink_dialog,//外网连接
        reply_outsidelink_makesure:_reply_outsidelink_makesure, //外网连接确定
        reply_shopurl:_reply_shopurl, //商城连接对话框
        reply_beadd_change:_reply_beadd_change //被添加关注回复切换
    };
})();