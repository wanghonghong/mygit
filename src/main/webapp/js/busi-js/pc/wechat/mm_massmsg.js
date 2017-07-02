/**
 * Created by wxz on 2016/9/25
 * 群发消息
 */
CommonUtils.regNamespace("mm", "massmsg");
mm.massmsg = (function () {
    var dlg_tab;
    var searchTitle;
    var d_showcontent;
    var objitems = {"contents": [], "thumbUrl": ""};//乐享 项目图文路径存储
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/wx/groups',//群发对象
        url2: CONTEXT_PATH + '/image_text',//乐享、项目图文
        url3: CONTEXT_PATH + '/content/openids',//筛选
        url4: CONTEXT_PATH + '/content/send',//新建群发其他
        url5: CONTEXT_PATH + '/content/batchPicurl', //批量url，上传图文消息内的图片获取URL
        url6: CONTEXT_PATH + '/content', //保存图文消息 删除
        url7: CONTEXT_PATH + '/content/content_list', //获取图文消息列表
        url8: CONTEXT_PATH + '/content/detail',      //素材管理详情获取
        url9: CONTEXT_PATH + '/content/sendmpnews',  //群发图文消息
        url10: CONTEXT_PATH + '/content/content_as_list', //已发送列表
        url11: CONTEXT_PATH + '/content/delete_as',
    };
    //初始化
    var _init = function () {
        var selectObj = null;
        localStorage.setItem("selectObj", selectObj);//清除筛选对象
        _mm_tabchange();
        _sended_area_template();
        _mm_timesend();
        mm_newmmsg_change();
        _mm_initSelect('#userGroup');
        _mm_usergroup();
        _mm_initSelect('#sex');
        jumi.Select("#zbRole");
        _mm_bind_roleData();// 角色选择
        _mm_filter();
        _mm_massmsg_datasave();//保存新建群发消息
    };

    //筛选
    var _mm_filter = function () {
        $("#massmsg_select").click(function () {
            var reval = "";

            $("#areaselectd div[class='u-sort active'] input[name='area']").each(function () {  //地区

                var val = $(this).attr("id");//取地区
                val = val.replace("chb", "");
                reval = reval + val + ",";
            });
            if (reval.length > 0) {
                reval = reval.substr(0, reval.length - 1);
            }
            _mm_getProvinceChild(reval); //获取所属省份的地市值
        });
    };
//读取地区库
    var _mm_getProvinceChild = function (reval) {
        var newVal = "";
        if (isNull(reval))
            newVal = reval;
        localStorage.setItem("areaString", newVal);//存1
        jumi.file("areaAll-new.json", call_backPrchild);
    }
    /////  地区更多城市回调
    function call_backPrchild(res) {
        var areastr = localStorage.getItem("areaString");//取2
        localStorage.setItem("areaString", "");
        //地区补全市级编码 开始
        if (isNull(areastr)) {
            var areaArr = areastr.split(',');
            for (var i = 0; i < areaArr.length; i++) {
                var pstr = areaArr[i];
                if (pstr.substring(2, 6) === "0000") {
                    var newstr = "";//重组的地市字符串
                    var childobj = res["data"][pstr];
                    for (var item in childobj) {
                        newstr = newstr + item + ",";
                    }
                    newstr = newstr.substr(0, newstr.length - 1);
                    areastr = areastr.replace(pstr, newstr);
                }
            }
        }
        //  存3 地区补全市级编码 结束
        _mm_filterAjax(areastr);  //ajax请求筛选
    };
    //ajax筛选
    var _mm_filterAjax = function (reval) {
        var itemsarr = [];
        if (isNull(reval))
            itemsarr = reval.split(',');
        var wxContentVo = {};
        wxContentVo.areaIds = itemsarr;
        wxContentVo.groupid = $("#userGroup").val();  //组对象
        wxContentVo.sex = $("#sex").val();  //性别
        wxContentVo.role = $("#zbRole").val();//角色选择

        var data = JSON.stringify(wxContentVo);
        $("#getmassoption").val(data);   ///筛选条件
        $.ajaxJson(ajaxUrl.url3, data, {
            "done": function (res) {
                if (res.code === 0) {
                    localStorage.setItem("selectObj", JSON.stringify(res.data));//暂存筛选对象
                    var type = res.data.type;
                    var str = "";
                    if (type === 1) {
                        str = "本次发送目标粉丝 <font style='color:#f38925;'>全体</font> 对象";
                    }
                    if (type === 2) {
                        var gname = $("#userGroup").find("option:selected").text();//组对象
                        str = "本次发送目标粉丝 <font style='color:#f38925;'>" + gname + "</font> 对象";
                    }
                    if (type === 3) {
                        str = "本次发送目标粉丝 <font style='color:#f38925;'>" + res.data.openids.length + "</font> 人";
                    }
                    _mm_showMessage(str);
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
    }

    //新建群发保存
    var _mm_massmsg_datasave = function () {
        $("#newmms_save_btn").click(function () {
            var sendType = "";
            var selectObj = null;
            selectObj = JSON.parse(localStorage.getItem("selectObj"));//暂存筛选对象
            if (!isNull(selectObj)) {
                _mm_showMessage("请先筛选群体");
                return;
            }
            var openids = selectObj.openids;
            if (selectObj.type === 3 && openids.length < 2) {
                _mm_showMessage("请至少筛选2个用户");
                return;
            }

            var optiondt = JSON.parse($("#getmassoption").val());
            var wxContentCo = {}; //乐享、项目图文 文本、图片
            var wxContentVo = {};//微信图文对象
            $("input[id^='masssendType']").each(function () {  //发送类别
                var isstatus = $(this).prop('checked');
                if (isstatus) {  //1是 0否
                    wxContentVo.isTiming = $(this).val();
                }
            });
            //定时发送
            var timeVal = $("#timemass").val();
            if (!isNull(timeVal) && wxContentVo.isTiming === "1") {
                _mm_showMessage("请选择定时群发时间");
                return;
            }
            wxContentVo.timingSendTime = timeVal;
            if (!mm.wepushmsg.mm_checkEndTime(timeVal)&& wxContentVo.isTiming === "1") {
                _mm_showMessage("定时群发时间 需大于当前时间");
                return;
            }
            wxContentVo.type = selectObj.type;
            wxContentVo.openids = openids;
            wxContentVo.areaIds = optiondt.areaIds;
            wxContentVo.groupid = optiondt.groupid;
            wxContentVo.sex = optiondt.sex;
            wxContentVo.role = optiondt.role;
            var new_index = localStorage.getItem("new_index");//选项卡标识
            var xmlx_type = localStorage.getItem("xmlx_type");//图文消息选项卡标识
            switch (new_index) {
                case"0":
                    if (xmlx_type === "3") {
                        var contentid = $("#item_hidid").html();
                        if (!isNull(contentid)) {
                            _mm_showMessage("<font style='color:#f38925;'>请选择图文消息</font>");
                            return;
                        }
                        wxContentVo.contentId = contentid;
                    }
                    sendType = "mpnews";
                    break; //图文消息
                case"1":
                    sendType = "text";
                    var ue = UE.getEditor('panel_beadd_text_b');
                    var con = ue.getContent();
                    if (con.length < 1 || con.length > 600) {
                        _mm_showMessage("<font style='color:#f38925;'>文本须为1-600个字</font>");
                        return;
                    }
                    String.prototype.replaceAll = function (FindText, RepText) {
                        regExp = new RegExp(FindText,'g');
                        return this.replace(regExp, RepText);
                    }
                    var str = ue.getContent();
                    wxContentCo.localContent =  str;
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
                    wxContentCo.content = content;
                    break; //文本
                case"2":
                    sendType = "image";
                    var strpath = $("#newmass_img").attr("src");
                    if (!isNull(strpath)) {
                        _mm_showMessage("<font style='color:#f38925;'>请选择图片</font>");
                        return;
                    }else{
                        strpath=jumi.picParse(strpath,720);
                    }
                    wxContentCo.thumbUrl = strpath;
                    break; //图片
                case"3":
                    sendType = "voice";
                    break; //语音
                case"4":
                    sendType = "video";
                    break; //视频
            }

            wxContentCo.sendType = sendType;
            wxContentVo.wxContentCo = wxContentCo;
            var data = JSON.stringify(wxContentVo);
            if (new_index !== "0") {//其它保存

                var args = {};
                args.fn1 = function () {
                    _mm_other_save(data);
                };
                args.fn2 = function () {
                };
                jumi.dialogSure("消息开始群发后无法撤销，是否确认群发？", args);

            } else {//项目、乐享图文批处理后保存
                if (xmlx_type !== "3") {
                    var args = {};
                    args.fn1 = function () {
                        _mm_xmlx_deffered();//异步处理

                    };
                    args.fn2 = function () {
                    };
                    jumi.dialogSure("消息开始群发后无法撤销，是否确认群发？", args);
                } else {
                    //微信图文
                    var args = {};
                    args.fn1 = function () {
                        _mm_wechar_ajaxpost(data);
                    };
                    args.fn2 = function () {
                    };
                    jumi.dialogSure("消息开始群发后无法撤销，是否确认群发？", args);


                }
            }
        });
    }
    var _mm_massmsg_clear = function () {
        var new_index = localStorage.getItem("new_index");//选项卡标识
        var xmlx_type = localStorage.getItem("xmlx_type");//图文消息选项卡标识
        $("#timemass").val("");
        $("#masssendType11").prop("checked", true);
        $("#masssendType12").removeAttr("checked");
        switch (new_index) {
            case"0":
                if (xmlx_type === "3") {
                    $("#item_hidid").html("");
                }
                $('#pictxt_show_con').html("");//清空图文消息
                break; //图文消息
            case"1":
                $("#content").val("");//清空文本
                break; //文本
            case"2":
                $("#newmass_img").attr("src", STATIC_URL + "/css/pc/img/no_picture.png");
                $("#newmass_imgdiv").hide();
                break; //图片
            case"3":
                sendType = "voice";
                break; //语音
            case"4":
                sendType = "video";
                break; //视频
        }
    }

    //验证消息提示
    var _mm_showMessage = function (info) {

        var dm = new dialogMessage({
            type: 3,
            title: '操作提醒',
            fixed: true,
            msg: info,
            isAutoDisplay: false
        });
        dm.render();
        setTimeout(function () {
            if (isNull(dialog.get("dialog_1"))) {
                dialog.get("dialog_1").close().remove();
            }
        }, 2000);

    }
//微信图文消息群发
    var _mm_wechar_ajaxpost = function (data) {
        $.ajaxJson(ajaxUrl.url9, data, {
            "done": function (res) { console.log(res);
                if (res.code === 0) {
                    _mm_massmsg_clear(); // 清空文本和图片内容
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

    var _mm_other_save = function (data) {
        $.ajaxJson(ajaxUrl.url4, data, {
            "done": function (res) {
                if (res.code === 0) {
                    _mm_massmsg_clear(); // 清空文本和图片内容
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

    //项目、乐享图文图片地址后并保存（异步提交处理）
    var _mm_xmlx_deffered = function () {
        var dtd = $.Deferred(); // 新建一个deferred对象
        var wait = function (dtd) {
            var tasks = function () {
                _mm_xmlx_batchPicurl();
                dtd.resolve(); // 改变deferred对象的执行状态
            };
            setTimeout(tasks, 5000);
            return dtd;
        };
        $.when(wait(dtd))
            .done(function () {
                alert("哈哈，成功了！");
            })
            .fail(function () {
                alert("出错啦！");
            });
    }


//批处理内容中的图片地址，转换为微信格式
    var _mm_xmlx_batchPicurl = function () {

        var data = JSON.stringify(objitems);
        $.ajaxJson(ajaxUrl.url5, objitems, {
            "done": function (res) {
                if (res.code === 0) {

                    localStorage.setItem("xmlx_batchpicurl", res.data);//项目、乐享图文返回的批处理图片信息
                    var dm = new dialogMessage({
                        type: 1,
                        title: '操作提醒',
                        fixed: true,
                        msg: "恭喜您，操作成功1111",
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
//保存新的乐享、图文信息（图片地址批处理后回填并保存）
    var _mm_xmlx_batched_save = function (data) {
        var con_arr = data.content;
        var item = parse.json(localStorage.getItem("imageTextRo_json"));//暂存乐享、图文数据
        $("#mm_tempcontent").find("img").each(function () {//截取内容中的图片地址开始
            var itemobj = {};
            var imgId = $(this).attr("id");
            for (var i = 0, num = con_arr.length; i < num; i++) {//回填处理后的URL
                if (con_arr[i].picId === imgId) {
                    $(this).attr("src", con_arr[i].filePath);
                }
            }
        });
        item.imageTextDetail = $("#mm_tempcontent").html();//处理的数据保存
        item.thumbMediaId = con_arr.thumbMediaId;
        var data = JSON.stringify(item);
        $.ajaxJson(ajaxUrl.url6, data, {
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

    //初始化选择框
    var _mm_initSelect = function (id) {
        $.fn.select2.defaults.set('language', 'zh-CN');
        $(id).select2({
            theme: "jumi",
            language: "en"
        });
    };
    //群发对象
    var _mm_usergroup = function () {

        $.ajaxJsonGet(ajaxUrl.url1, {
            "done": function (res) {
                if (res.code === 0) {
                    var items = res.data;
                    var lng = items.length;
                    var tpl = '<option value="-1" selected="selected">--全部用户--</option>';
                    for (var i = 0, num = items.length; i < num; i++) {
                        if (items[i].name !== "黑名单") {
                            tpl += '<option value="' + items[i].groupid + '">' + items[i].name + '</option>';
                        }
                    }
                    $('#userGroup').html(tpl);
                }
            },
            "fail": function (res) {
            }
        });
    };

    var _mm_newmassimg = function () {
        // $("#newmass_imgdg").click(function () {
        _mm_img("#newmass_img");
        // });
    }

    ///////图片start
    var _mm_img = function (id) {
        var d = new Dialog({
            context_path: CONTEXT_PATH, //请求路径,  必填
            resType: 1,//图片1，视频2，语音3   必填
            callback: function (url) {
                $("#newmass_imgdiv").show();
                $(id).attr('src', url);
            }
        });
        d.render();
    }

    //群发消息页面切换
    var _mm_tabchange = function () {
        var tabul = $(".m-tab ul li");
        tabul.click(function () {
            var index = $(this).index();
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq(index).show();
            if (index === 1) {
                jumi.template('wechat/massmsg_sended', function (tpl) {
                    $('#massmsg_content_sended').html(tpl);
                })
            }
        });

    };

    //群发消息之图文消息 页面切换
    var _mm_imgtxt_tabchange = function () {
        localStorage.setItem("xmlx_type", 3);//微信 图文消息选项卡标识
        _pictxtmsg_list("");   //微信图文
        var tabul = $("div[id='massmsg_imgtxt_tab'] .m-tab ul li");
        tabul.click(function () {
            var index = $(this).index();
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
        });

    };

    var _mm_getwechatimgtxt = function () {

    }

    var _pictxt_gethref = function () {

    }

    //获取项目、乐享图文详细
    var _mm_xmlx_gethref = function () {
        var id = null;
        $('#pictxt_show_con').html("");
        $("#mm_type_list").find("input[type='radio']").each(function () {
            if ($(this).prop('checked') === true) {
                id = $(this).val();

            }
        });
        var type = localStorage.getItem("xmlx_type");
        // var url = ajaxUrl.url2+"/{"+id+"}";

    }
    //微信图文详细列表
    var _mm_wechat_click = function () {
        var type = localStorage.getItem("xmlx_type");
        var id = "";
        $("#mm_type_list").find("input[type='radio']").each(function () {
            if ($(this).prop('checked') === true) {
                id = $(this).val();
            }
        });
        if (type === "3") {
            // $('#pictxt_show_con').html("");
            if (isNull(id)) {
                $.ajaxJsonGet(ajaxUrl.url8 + "/" + id, {
                    "done": function (res) {
                        if (res.code === 0) {
                            dlg_tab.close().remove();
                            var data = res.data;
                            var dt = data.saveTime;
                            if (isNull(dt)) {
                                dt = _datamonthformat_edit(dt);
                                data.saveTime = dt;
                            }
                            if (data.wxContentSubVos.length > 0) {
                                for (var i = 0; i < data.wxContentSubVos.length; i++) {
                                    dt = data.wxContentSubVos[i].saveTime;
                                    if (isNull(dt)) {
                                        dt = _datamonthformat_edit(dt);
                                        data.wxContentSubVos[i].saveTime = dt;
                                    }
                                }
                            }
                            jumi.template('/wechat/massmsg_pictxtmsg_item', data, function (tpl) {
                                $('#pictxt_show_con').html("");
                                $('#pictxt_show_con').html(tpl);
                            });
                        }
                    }
                });
            } else {
                _mm_showMessage("请选择需发送的图文！");
            }
        }
    }
    //获取乐享、项目图文详细信息
    var _mm_xm_click = function () {
        var id = null;
        $('#pictxt_show_con').html("");
        $("#mm_type_list").find("input[type='radio']").each(function () {
            if ($(this).prop('checked') === true) {
                id = $(this).val();
            }
        });
        var url = ajaxUrl.url2 + '/' + id;
        $.ajaxJsonGet(url, {
            "done": function (res) {
                if (res.code === 0) {
                    var item = res.data.imageTextRo_;
                    localStorage.setItem("imageTextRo_json", item);//暂存乐享、图文数据
                    var content = item.imageTextDetail;
                    $("#mm_tempcontent").html(content);
                    $("#mm_tempcontent").find("img").each(function () {//截取内容中的图片地址开始
                        var itemobj = {};
                        var id = _picturemss_random_Id();//随te机数
                        var url = $(this).attr("src");
                        $(this).attr("id", id);
                        itemobj.picId = id;
                        itemobj.filePath = url;
                        objitems.contents.push(itemobj);

                    });
                    //  objitems.contents.push(imgarr);
                    objitems.thumbUrl = item.imageUrl;
                    item.timestr = _gettimeformat();
                    jumi.template('wechat/massmsg_picmsg_item', item, function (tpl) {
                        $('#pictxt_show_con').html("");
                        $('#pictxt_show_con').html(tpl);
                    })


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
        dlg_tab.close().remove();
    }

    var _gettimeformat = function () {
        var str = "";
        var now = new Date();
        var month = now.getMonth() + 1;     //月
        var day = now.getDate();            //日
        str = month + "月" + day + "日";
        return str;
    }
    //生成随机数id
    var _picturemss_random_Id = function () {

        var random = (Math.random() * 10000000000) + "";
        random = random.substring(0, 10);
        var now = new Date();
        var strid = now.getYear() + "" + now.getMonth() + "" + now.getDate() + "" + now.getHours() + "" + now.getMinutes() + "" + now.getSeconds() + "" + random;
        return strid;
    }
    //消息图片选项卡内容列表
    var _imgtxt_xmle_list = function (typeid, searchTitle) {

        var url = ajaxUrl.url2 + '/findAll/' + typeid;
        var imageTextQo = {
            imageTextTile: searchTitle,
            pageSize: 5
        };
        jumi.pagination('#pageToolbar', url, imageTextQo, function (res, curPage) {
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

                jumi.template('wechat/massmsg_picmsg_list', data, function (tpl) {
                    searchTitle = "";
                    $('#mm_type_list').empty();
                    $('#mm_type_list').html(tpl);
                })


            }
        })
    };
//项目图文、乐享图文标题搜索
    var _mm_getimgtxt_searchTitle = function (id) {
        searchTitle = $(id).val();
        var type = localStorage.getItem("xmlx_type");
        if (type === "1" || type === "2") {
            _imgtxt_xmle_list(type, searchTitle);
        }
        if (type === "3") {
            _pictxtmsg_list(searchTitle);
        }
    };
    //当前页定位
    var _sended_list_fixed = function () {
        var type = localStorage.getItem("sendedType");//已发送选项卡类型
        var pageurl = localStorage.getItem("sendedUrl");//已发送选项卡地址
        var curPage = $("#pageToolbar_page").val();
        var contentRo = {
            pageSize: 10,
            sendType: type,
            status: 1,   //1已发送
            curPage: curPage || 0
        };

        $.ajaxJson(ajaxUrl.url10, contentRo, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    }
                    for (var i = 0, len = data.items.length; i < len; i++) { //日期格式化
                        var obj = data.items[i];
                        if (isNull(obj.saveTime)) {
                            var dt = _sended_timeformat(obj.saveTime);
                            obj.saveTime = dt;
                        }
                    }
                    jumi.template(pageurl, data, function (tpl) {
                        $('#person1').empty();
                        $('#person1').html(tpl);
                    })
                }
            }
        })
    };
    //当前页定位
//已发送列表渲染
    var _sended_list = function (pageurl, type) {
        localStorage.setItem("sendedType", type);//已发送选项卡类型
        localStorage.setItem("sendedUrl", pageurl);//已发送选项卡地址
        var url = ajaxUrl.url10;
        var contentRo = {
            pageSize: 10,
            sendType: type,
            status: 1   //1已发送
        };
        jumi.pagination('#pageToolbar', url, contentRo, function (res, curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items
                };

                for (var i = 0, count = data.items.length; i < count; i++) {
                    var status = data.items[i].status;
                    switch (status) {
                        case 0:
                            data.items[i].statusName = "等待发送";
                            break;
                        case 1:
                            data.items[i].statusName = "发送成功";
                            break;
                        case 2:
                            data.items[i].statusName = "发送失败";
                            break;
                    }
                }
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }
                jumi.template(pageurl, data, function (tpl) {

                    $('#person1').empty();
                    $('#person1').html(tpl);
                });
                if(type==="mpnews"){
                    mm.wepushmsg.init_sended_view();
                }
                if(type==="text"){
                    _mm_sended_conditionview_txt();
                }
                if(type==="image"){
                    _mm_sended_conditionview_pic();
                }
            }
        })
    }


//已发送列表渲染
    //新建群发消息之 微信图文列表
    var _pictxtmsg_list = function (title) {

        var url = ajaxUrl.url7;
        var contentRo = {//  status:0,   //未发送
            pageSize: 10,
            sendType: "mpnews",
            title: title
        };
        jumi.pagination('#pageToolbar', url, contentRo, function (res, curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items
                };
                /*for(var i=0,len=data.items.length;i<len;i++){ //日期格式化
                 var obj = data.items[i];
                 if(isNull(obj.sendTime)) {
                 var dt = _sended_timeformat(obj.sendTime);
                 obj.sendTime = dt;
                 }
                 }*/
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }

                jumi.template("/wechat/massmsg_pictxtmsg_list", data, function (tpl) {
                    $('#mm_type_list').empty();
                    $('#mm_type_list').html(tpl);
                })


            }
        })
    }
//日期截取
    var _sended_timeformat = function (timedt) {
        var tm = new Date(Date.parse(timedt.replace(/-/g, "/")));
        var moth = tm.getMonth() + 1;
        var dt = tm.getFullYear() + "-" + moth + "-" + tm.getDate();
        return dt;
    }

    //日期 X月X日
    var _datamonthformat_edit = function (dt) {
        var oDate = new Date(Date.parse(dt.replace(/-/g, "/")));
        var month = oDate.getMonth() + 1;    //月
        var day = oDate.getDate();            //日
        var val = month + "月" + day + "日";
        return val;
    }

    //已发送页面切换
    var _sended_tabchange = function () {
        _sended_list('wechat/massmsg_r_pictxt', "mpnews");//图文消息模板
        $(".u-rb").click(function () {
            var tag = $(this).children("input[type='radio']").attr("id");
            tag = tag.replace("radioBox", "");
            if (tag === "1") {
                _sended_list('wechat/massmsg_r_pictxt', "mpnews");//图文消息模板
            }
            if (tag === "2") {
                _sended_list('wechat/massmsg_r_txt', "text");//文本
            }
            if (tag === "3") {
                _sended_list('wechat/massmsg_r_pic', "image");//图片

            }
            if (tag === "4") {
                jumi.template('wechat/massmsg_r_voic', function (tpl) {
                    $("#person1").empty();
                    $("#person1").html(tpl);
                })
                var dm = new dialogMessage({
                    type: 2,
                    title: '提示',
                    fixed: true,
                    msg: '功能还在开发中',
                    isAutoDisplay: false

                });
                dm.render();

            }
            if (tag === "5") {
                jumi.template('wechat/massmsg_r_video', function (tpl) {
                    $("#person1").empty();
                    $("#person1").html(tpl);
                })
                var dm = new dialogMessage({
                    type: 2,
                    title: '提示',
                    fixed: true,
                    msg: '功能还在开发中',
                    isAutoDisplay: false

                });
                dm.render();

            }

        });
    };
    var _mm_sended_txtview = function (index) {
        $("div[id^='txtontainer_']").hide();
        var doc = $('#txtview_'+index);
        var con = doc.data('content');
        d_showcontent = new dialogMessage({
            type: 3,
            title: '文本',
            fixed: true,
            msg: con,
            isAutoDisplay: true

        });
        d_showcontent.render();
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
            },300)
        })
    }
    //新建群发消息切换
    var mm_newmmsg_change = function () {
        localStorage.setItem("new_index", 0);
        localStorage.setItem("xmlx_type", 3);//图文消息选项卡标识
        var tabul = $("div[id='massmsg_content_new'] .m-panel-zdhf ul li");
        tabul.eq(0).addClass("z-sel").siblings().removeClass("z-sel");
        var tabcon = $("div[id='massmsg_content_new'] .panel-list");
        tabcon.hide().eq(0).show();
        tabul.click(function () {
            var index = $(this).index();
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass("z-sel");
            tabcon.hide().eq(index).show();
            localStorage.setItem("new_index", index);//localStorage.getItem("json")
            if (index === 0) {//图文消息
                $('#pictxt_show_con').show();
                _mm_picmsg_render();

            }
            if(index===1){
                $('#pictxt_show_con').hide();
                $('#panel_bedd').show();
                _setEditor('panel_beadd_text_b');
            }
            if (index === 2) {//图片
                var imgstatus = $("#imgstatus").text();
                _mm_img("#newmass_img");
            }
            if (index === 3) {
            }
            _mm_friendmsg(index);
        });
    }

    var _mm_picmsg_render = function () {

        jumi.template('wechat/massmsg_picmsg_tab', function (tpl) {
            dlg_tab = dialog({
                title: "图文消息",
                content: tpl,
                zIndex: 100,
                width: 1000,
                id: 'dialog_11',
                onshow: function () {
                    _mm_imgtxt_tabchange();
                },
                onclose: function () {
                    dialog.get('dialog_11').close().remove();
                }
            });
            dlg_tab.showModal();
        });

    };

    //友情提示-未开发功能
    var _mm_friendmsg = function (index) {
        switch (index) {
            case 3:
            case 4:
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
    //新建群发消息 定时
    var _mm_timesend = function () {

        $("#timemass").datetimepicker({
            closeText: '确定',
            showSecond: true,
            timeFormat: 'hh:mm:ss',
            dateFormat: 'yy-mm-dd',
            stepHour: 1,
            stepMinute: 1,
            stepSecond: 1
        });
    }
    ////地区模板start
    var _sended_area_template = function () {
        jumi.template('customer/customer_area', function (tpl) {
            $('#customer_content').html(tpl);
            $("#area_title").text("地区选择");
            $("#area_title").css("min-width", "60px");
            $("#area_title").css("margin-right", "5px");
            $("#area_title").css("margin-left", "-15px");

        })
    }
    ////地区模板end
    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    };

    ///////删除 已发送的文本、图片、微信图文start
    var _mm_picandtext_del = function () {
        $("div[id^='del_']").click(function () {
            var itemid = $(this).attr("id").replace("del_", "");
            jumi.template('wechat/mm_picturemsg_del_dialog', function (tpl) {
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
                _mm_massmsg_delitem(itemid);
                _mm_massmsg_delcancel();

            });
        });
    };

    var _mm_massmsg_delcancel = function () {
        $("#makesure_canl").click(function () {
            dialog.get("dialog_wxdel").close().remove();
        });
    };
    var _mm_massmsg_delitem = function (itemid) {
        $("#makesure_del").click(function () {
            dialog.get("dialog_wxdel").close().remove();
            $.ajaxJsonDel(ajaxUrl.url11 + "/" + itemid, {
                "done": function (res) {
                    if (res.code === 0) {
                        _sended_list_fixed();
                        var dm = new dialogMessage({
                            type: 1,
                            title: '操作提醒',
                            fixed: true,
                            msg: '恭喜您，操作成功',
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

    ///////删除 已发送的文本、图片、微信图文end

    var _mm_bind_roleData = function () {
        jumi.file("role.json", call_backface);
    }

    function call_backface(res) {
        var rolearr = res["data"];
        var tpl = '<option value="-1" selected="selected">--全部角色--</option>';
        for (var i = 0; i < rolearr.length; i++) {
            tpl += '<option value="' + rolearr[i].id + '">' + rolearr[i].name + '</option>';
            $('#zbRole').html(tpl);
        }
    };
    //筛选栏收缩
    var _mm_massmsg_slide = function () {
        $("#mass_pagebtn").click(function () {
            $("#massmsg_updown_page").slideToggle("fast");
            $(this).toggleClass("btn-slide1");
        });
    };

    //文本--查看条件
    var _mm_sended_conditionview_txt = function () {
        $("div[id^='txtlickview_']").click(function () {

            var id = $(this).attr("id").replace("txtlickview_","");
            var dataid = "#txtview_"+id;
            var data = JSON.parse($(dataid).val());
            data.id= id;
            data.sexName = mm.wepushmsg.getsex(data.sex);
            localStorage.setItem("txt_viewobj",JSON.stringify(data));
            _mm_set_roleData_txt();
        });
    }

    var _mm_set_roleData_txt = function () {
        jumi.file("role.json", call_backrole_txt);
    }

    var call_backrole_txt = function (res) {
        var rolearr = res["data"];
        var data = JSON.parse(localStorage.getItem("txt_viewobj"));
        var roleId =data.role;
        if(roleId===-1){
            data.roleName = "全部角色";
        }else {
            for (var i = 0; i < rolearr.length; i++) {
                if(roleId===rolearr[i].id){
                    data.roleName = rolearr[i].name;
                }
            }
        }
        if(!isNull(data.groupName)){
            data.groupName ="全部用户";
        }

        jumi.template('wechat/push_msg_view',data,function (tpl) {
            $("div[id^='txtontainer_']").hide();
            var container = "#txtontainer_"+data.id;
            $(container).show();
            $(container).html(tpl);
            _init_sended_viewclose_txt();
        });
    }
    //图片--查看条件
    var _mm_sended_conditionview_pic = function () {
        $("div[id^='piclickview_']").click(function () {
            var id = $(this).attr("id").replace("piclickview_","");
            var dataid = "#picview_"+id;
            var data = JSON.parse($(dataid).val());
            data.id= id;
            data.sexName = mm.wepushmsg.getsex(data.sex);
            localStorage.setItem("pic_viewobj",JSON.stringify(data));
            _mm_set_roleData();
        });
    };
    var _mm_set_roleData = function () {
        jumi.file("role.json", call_backrole);
    }

    function call_backrole(res){
        var rolearr = res["data"];
        var data = JSON.parse(localStorage.getItem("pic_viewobj"));
        var roleId =data.role;
        if(roleId===-1){
            data.roleName = "全部角色";
        }else {
            for (var i = 0; i < rolearr.length; i++) {
                if(roleId===rolearr[i].id){
                    data.roleName = rolearr[i].name;
                }
            }
        }
        if(!isNull(data.groupName)){
            data.groupName ="全部用户";
        }
        jumi.template('wechat/push_msg_view',data,function (tpl) {
            $("div[id^='picontainer_']").hide();
            var container = "#picontainer_"+data.id;
            $(container).show();
            $(container).html(tpl);
            _init_sended_viewclose_pic();
        });
    };

    //关闭图片"点击查看"对话框
    var _init_sended_viewclose_pic = function(){
        $("i[id^='colse_']").click(function () {
            $("div[id^='picontainer_']").hide();
        });
    };

    //关闭文本"点击查看"对话框
    var _init_sended_viewclose_txt = function(){
        $("i[id^='colse_']").click(function () {
            $("div[id^='txtontainer_']").hide();
        });
    };

    return {
        init: _init,
        sended_tabchange: _sended_tabchange,//已发送
        mm_wechat_click: _mm_wechat_click,//微信图文内容发送
        mm_sended_txtview: _mm_sended_txtview,//查看
        mm_getimgtxt_searchTitle: _mm_getimgtxt_searchTitle,//根据标题查询
        pictxtmsg_list: _pictxtmsg_list,//根据标题查询
        mm_picandtext_del: _mm_picandtext_del,//已发送文本、图文删除
        mm_massmsg_slide: _mm_massmsg_slide

    };
})();


