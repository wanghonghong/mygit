/**
 * Created by wxz on 2016/9/23
 * 素材管理
 */

CommonUtils.regNamespace("mm", "picturemsg");
mm.picturemsg = (function () {
    var number = 1;  //消息图文增加的数量
    var tagnumber = 1;//消息图文增加的标记id
    var optstatus="add";//操作状态 新增 add、编辑 edit
    var editId ="";
    var dtstatus="";//数据状态
    var wxconten_item = {
        "title": "",
        "author": "",
        "thumbId": "",
        "thumbUrl": "",
        "digest": "",
        "content": "",
        "jmContent":"",
        "qrcodeType": "",
        "contentSourceUrl": "",
        "showCoverPic": "0",
        "sendType": "mpnews",
        "saveTime":""
    };//单条存储
    var newitemCos = {
        "title": "",
        "author": "",
        "thumbMediaId": "",
        "thumbUrl": "",
        "digest": "",
        "content": "",
        "jmContent":"",
        "qrcodeType": "",
        "contentSourceUrl": "",
        "showCoverPic": "0",
        "sendType": "mpnews",
        "saveTime":""

    };//单条存储新增
    var wxconten_all_json = {"wxContentCos": []};
    var wxconten_all_jsonedt = {"wxContentUos": []};

    var pictxtadd_dg = null;
    var pictxtadd_dg_edit = null;
    var objitems = {"contents": [], "thumbUrls": []};//图文路径存储
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/content/batchPicurl', //批量url，上传图文消息内的图片获取URL
        url2: CONTEXT_PATH + '/content/content_list',//获取图文消息列表
        url3: CONTEXT_PATH + '/content',      //删除 编辑
        url4: CONTEXT_PATH + '/content/detail',      //获取图文编辑详情  查询主图
        url5:CONTEXT_PATH+'/content/content_sub_detail', // 查询子类
        url6:CONTEXT_PATH+'/content/preview', //手机预览
        url7:CONTEXT_PATH+'/content/pub_qrcode'//二维码关注
    };
    var picmsg_editdt = null;
    var _init = function () {
        _picturemss_add("#picturemsgadd");
        _picturemss_smallimg_remove();
        _picturemss_del();
        _picturemss_list();//图文消息列表

    };

    var _picturemss_del = function () {
        $(".dialog2").click(function () {
            var dm = new dialogMessage({
                type: 2,
                title: '提示',
                fixed: true,
                msg: '功能还在开发中',
                isAutoDisplay: false

            });
            dm.render();
        });
    };


    var _picturemss_add = function (id) {
        $(id).click(function () {
            jumi.template('wechat/mm_picturemsg_add', function (tpl) {
                pictxtadd_dg = dialog({
                    title: '图文新建',
                    content: tpl,
                    zIndex: 100,
                    width: 1000,
                    id: 'dialog_edit',
                    onshow:function(){
                        var shopName = $("#shopName").val();
                        $("#bindi1_2").text(shopName);
                        $("#txtCon_2").val(shopName);//设置作者默认值
                        _initEditor('container');
                        _addImgEvent("#txtCon_3");//图片绑定
                        _picturemss_cancel();//取消
                        _picturemss_smallimg_add();
                        _picturemss_setselected();
                        _picturemss_datas_save();//保存
                        _mm_pictxt_mobileView();//手机预览
                        _bind_ritem();//数据绑定
                        localStorage.setItem("curnum", 1);//当前状态下的子项标识
                        optstatus="add";//新增
                        _mm_savetime();//日期控件初始化

                    },
                    onclose: function () {
                        dialog.get("dialog_edit").close().remove();
                    }
                });
                pictxtadd_dg.showModal();
            })


        });

    };


    //随机数
    var _picturemss_random_Id = function () {

        var random = (Math.random() * 10000000000) + "";
        random = random.substring(0, 10);
        var now = new Date();
        var strid = now.getYear() + "" + now.getMonth() + "" + now.getDate() + "" + now.getHours() + "" + now.getMinutes() + "" + now.getSeconds() + "" + random;
        return strid;
    }
    //新增子项
    var _picturemss_smallimg_add = function () {
        wxconten_item.saveTime =_getCurrentTime(); //日期
        wxconten_item.author = $("#shopName").val(); //作者默认为商店名称
        localStorage.setItem("pictxt_json1", JSON.stringify(wxconten_item));//大图json数据初始化
        $("#but_smallimg").click(function () {
            number = $(".pictxtitem-box").length;
            if (number < 8) {
                $("#fulltxt1").hide();//隐藏阅读全文
                var html = $("div[id='left_area'] div[id='smallimg0']").clone();
                var idstr = html.attr("id");
                tagnumber = tagnumber + 1;
                localStorage.setItem("curnum", tagnumber);//当前项下标
                idstr = "smallimg" + tagnumber;//新增项id
                var cls = "bindi" + tagnumber;//class 片段名
                html.attr("id", idstr);
                html.css("display", "");
                html.find(".icon-delete").attr("id", "del" + tagnumber);
                html.find("#bindi0_1").attr("id", cls + "_1");
                html.find("#bindi0_3").attr("id", cls + "_3");
                html.find(".pictxtdata0").attr("id", "pictxt_json" + tagnumber);
                html.addClass("b-sel");
                html.find(".readtext-hide").css("display", "");
                //  html.find(".readtext").text(html.find(".readtext").text()+number);//测试
                $("div[id='left_area'] .selborder").removeClass("b-sel");
                $("div[id='left_area'] .readtext-hide").css("display", "none");
                $("div[id='left_area'] div[id='smallimg0']").before(html);
                _picturemss_smallimg_down();
                _picturemss_smallimg_up();
                _bind_ritem_clear();//清空右边内容
                //作者
                wxconten_item.author = $("#shopName").val(); //作者默认为商店名称
                $("#txtCon_2").val(wxconten_item.author);//设置作者默认值

               // 作者
                //日期
                var dt = _getCurrentTime();
                $("#txtCon_6").val(dt);
                wxconten_item.saveTime =dt;
                //日期

                localStorage.setItem("pictxt_json" + tagnumber, JSON.stringify(wxconten_item));//小图json数据初始化
                if(number===7){
                    $("#but_smallimg").hide();
                }
            }
        });
    };

    var _picturemss_setselected = function () {

        $("div[id='left_area'] .selborder").click(function () {//大图选中状态
            $(this).addClass("b-sel");
            $("div[id='left_area_bottom'] .selborder").each(function () {
                $(this).removeClass("b-sel");
                $("div[id='left_area'] .readtext-hide").css("display", "none");//隐藏工具栏
            });
            var sltedId = $("div[id='left_area'] .selborder:first-child").attr("id");//选中项ID
            localStorage.setItem("curnum", sltedId.replace("smallimg", "")); //设置当前子项状态
            _bind_ritem();//绑定
            _bind_ritem_clear();//数据清除
            _bind_setleft_data();//设置大图数据

        });

        $(document).on("mouseover", "div[id='left_area_bottom'] .selborder", function () {//小图选中状态
            $("div[id='left_area'] .readtext-hide").css("display", "none");//隐藏工具栏
            $(this).children(".readtext-hide").css("display", "");//显示选中项的工具栏

        })
        $(document).on("click", "div[id='left_area_bottom'] .selborder", function () {//小图选中状态
               $("div[id='left_area'] .selborder").addClass("b-sel").removeClass("b-sel");
               $(this).addClass("b-sel").siblings().removeClass("b-sel");
            $("div[id='left_area'] .readtext-hide").css("display", "none");//隐藏工具栏
            $(this).children(".readtext-hide").css("display", "");//显示选中项的工具栏

            var sltedId = $(this).attr("id");//选中项ID
            localStorage.setItem("curnum", sltedId.replace("smallimg", "")); //设置当前子项状态
            _bind_ritem();//绑定
            _bind_ritem_clear();//数据清除
            _bind_setleft_data();//设置小图数据
        })
    };


    var _picturemss_smallimg_up = function () {
        $('.pictxt_up').unbind('click').bind('click',function() {
            var divobj = $(this).parent().parent(".pictxtitem-box");
            if (divobj.index() !== 0) {
                $(this).parent().hide();
                divobj.prev().before(divobj);
            }
        });
    };

    var _picturemss_smallimg_down = function () {
        $('.pictxt_down').unbind('click').bind('click',function() {
            var dLength = $(".pictxt_down").length - 2;//隐藏域、索引从0计起
            var divobj = $(this).parent().parent(".pictxtitem-box");
            if (divobj.index() !== dLength) {
                $(this).parent().hide();
                //divobj.next().find(".readtext-hide").show();
                divobj.next().after(divobj);

            }
        })
    };
    //图文消息子项删除
    var _picturemss_smallimg_remove = function () {
        $(document).on("click", ".icon-delete", function () {
            var obj = $(this).parent().parent();
            var rid = $(this).parent().parent().attr("id").replace("smallimg", "");
            var preId= $(this).parent().parent().prev().attr("id");
            var curnumber="1";
            if(isNull(preId)){
                curnumber = preId.replace("smallimg", "");
            }
            var d = dialog({
                title: '提示',
                content: '您确定要删除此条图文？',
                quickClose: false,// 点击空白处快速关闭
                width: 200,
                id: 'dialog_m',
                okValue: '确定',
                ok: function () {
                    number = number - 1;
                    obj.remove();
                    $("#but_smallimg").show();
                    if (number === 0) {
                        $("#fulltxt1").show();//隐藏阅读全文
                    }
                    _bind_save_removeRecodeId(rid,curnumber);
                },
                cancelValue: '取消',
                cancel: function () {
                    dialog.get("dialog_m").close().remove();
                }
            });
            var id = $(this).attr("id");
            d.show(document.getElementById(id));


        });
    };
    //保存被删除的子项的ID
    var _bind_save_removeRecodeId=function(id,curnumber){
        var curkey = "pictxt_json" + id;
        var itemjson = JSON.parse(localStorage.getItem(curkey));//获取当前记录

        if(isNull(itemjson.id)) {  //记录删除的ID 编辑
            var bpjson = JSON.parse(localStorage.getItem("pictxt_json1"));//取大图记录
            if(!isNull(bpjson.deleteIds)){
                bpjson.deleteIds="";
            }
            var tempid =  itemjson.id;
            bpjson.deleteIds =bpjson.deleteIds+tempid+",";
            localStorage.setItem("pictxt_json1", JSON.stringify(bpjson));//大图json数据初始化

            $("#smallimg"+curnumber).addClass("b-sel");//设置选中
            localStorage.setItem("curnum", curnumber); //设置当前子项状态
            _bind_ritem();//绑定
            _bind_ritem_clear();//数据清除
            _bind_setleft_data();//设置大图数据


        }
        localStorage.setItem(curkey,"");//清空删除的记录
    }
//记录剩余字符串的长度
    var _picturemss_fontnum = function (conid, numid, number) {
        var content = $(conid).val();
        // var len;//记录剩余字符串的长度
        if (content.length > number) {
            $(conid).val(content.substr(0, number));
            $(numid).text(number);
        } else {
            $(numid).text(content.length);
        }

    };
//输入文字信息提示
    var _picturemss_limitfontnum = function (conid, maxnumber, minnumber) {
        var content = $(conid).val();
        if (content.length >= maxnumber) {
            showTip("文字长度不超" + maxnumber + "!", conid);
        }
        if (content.length < minnumber) {
            showTip("<font color='red'>文字长度至少为" + minnumber + "!</font>", conid);
        }
    };

    /**
     * 提示信息 3秒 消失
     * @param {Object} str
     * @param {Object} id
     */
    function showTip(str, id) {
        id = id.replace("#", "");
        var d = dialog({
            align: 'right',
            content: str,
            quickClose: true,
            padding: 2

        });

        if (isNull(id)) {
            d.show(document.getElementById(id));
        } else {
            d.show();
        }
        setTimeout(function () {
            d.close().remove();
        }, 2000);
    };
    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    };
    function isNullNull(data) {
        return (data == " " || data == "" || data == undefined || data == null) ? false : true;
    };
    //点击取消
    var _picturemss_cancel = function () {
        $("#pmsg_cancel").click(function () {
            dialog.get("dialog_edit").close().remove();
        });
    };

    //图片选择
    var _addImgEvent = function (id) {
        $(id).click(function () {
            var d = new Dialog({
                context_path: CONTEXT_PATH, //请求路径,  必填
                resType: 1,//图片1，视频2，语音3   必填
                callback: function (url) {
                    $(id).attr('src', url);
                    var curtag = "pictxt_json" + localStorage.getItem("curnum");
                    var curstr = localStorage.getItem("curnum");
                    var leftimgId = "bindi" + curstr + "_3";
                    $("#" + leftimgId).attr("src", url);
                    var itemjson = JSON.parse(localStorage.getItem(curtag));
                    itemjson.thumbUrl = url;
                    localStorage.setItem(curtag, JSON.stringify(itemjson));

                }
            });
            d.render();
        })

    };
    //初始化编辑器
    var _initEditor = function (id) {
        UE.delEditor(id);
        var ue = UE.getEditor(id, {
            initialFrameWidth: 419,
            initialFrameHeight: 240,
            zIndex: 101,
            autoHeight: false
        });

        ue.addListener('contentchange', function () {//监听编辑器内容
            var val = ue.getContent();
           var txtVal = ue.getContentTxt();
            if (isNull(val)) {
                var curtag = "pictxt_json" + localStorage.getItem("curnum");
                var itemjson = JSON.parse(localStorage.getItem(curtag));
                if(isNull(itemjson)) {//暂存富文本内容
                    itemjson.jmContent = val;
                    itemjson.content = txtVal;//暂存纯文本内容
                }
               /* var zyao = $("#txtCon_4").val();
                 if(!isNull(zyao)){ //摘要绑定前54个字
                     zyao= txtVal.substring(0,53);
                     $("#txtCon_4").val(zyao);
                     itemjson.digest = zyao;
                 }*/
                localStorage.setItem(curtag, JSON.stringify(itemjson));
            }
        })
    };
    //双向绑定开始
    var _bind_ritem = function () {
        $(".bindright").bind('input propertychange', function () {  //监听input变化

            var curnum = localStorage.getItem("curnum");
            var rtId = $(this).attr("id").replace("txtCon_", "");
            var ltId = "bindi" + curnum + "_" + rtId;
            var val = $(this).val();
            $("#" + ltId).html(val); // console.log(val);console.log($("#" + ltId).html());
            _bind_jsondata(rtId, val);
        });
        $(".rbeweima").change(function () { //监听radio变化
            var curtag = "pictxt_json" + localStorage.getItem("curnum");
            var itemjson = JSON.parse(localStorage.getItem(curtag));
            itemjson.qrcodeType = $(this).val();
            localStorage.setItem(curtag, JSON.stringify(itemjson));

        });
    };
//暂存消息图文对象
    var _bind_jsondata = function (index, val) {
        var curtag = "pictxt_json" + localStorage.getItem("curnum");
        var itemjson = JSON.parse(localStorage.getItem(curtag));
        switch (index) {
            case "1":
                itemjson.title = val;
                break;//图文标题
            case "2":
                itemjson.author = val;
                break;//作者名称
            case "4":
                itemjson.digest = val;
                break;//摘要
            case "5":
                itemjson.contentSourceUrl = val;
                break;//原文链接
        }

        localStorage.setItem(curtag, JSON.stringify(itemjson));

    }

    //清空右边内容
    var _bind_ritem_clear = function () {
        $(".bindright").each(function () {
            $(this).val("");
        });

        $("#txtCon_3").attr("src", THIRD_URL + "/css/pc/img/no_picture.png");//设置为无图片
        var ue = UE.getEditor('container');//清编辑器
        ue.setContent("");

        $("#abs_num").html(0);

        $("#radioBox1").prop("checked",true);
        $("#radioBox2").removeAttr("checked");
        $("#radioBox3").removeAttr("checked");

        var curTime = _getCurrentTime(); //设置日期
        $("#txtCon_6").val(curTime);
    };
    //设置右边值状态
    var _bind_setleft_data = function () {
        var curstr = localStorage.getItem("curnum");
        var curtag = "pictxt_json" + localStorage.getItem("curnum");
        var itemjson = JSON.parse(localStorage.getItem(curtag));

        for (var i = 1; i < 7; i++) {
            var ltId = "txtCon_";
            ltId += i;
            switch (i) {
                case 1:
                    $("#" + ltId).val(itemjson.title);
                    break;
                case 2:
                    $("#" + ltId).val(itemjson.author);
                    break;
                case 3:
                    if (isNull(itemjson.thumbUrl)) {
                        $("#" + ltId).attr("src", itemjson.thumbUrl);
                    }
                    break;
                case 4:
                    $("#" + ltId).val(itemjson.digest);
                    _picturemss_fontnum("#" + ltId,'#abs_num',60);//输入字数提示
                    break;
                case 5:
                    $("#" + ltId).val(itemjson.contentSourceUrl);
                    break;
                case 6:
                    $("#" + ltId).val(itemjson.saveTime);
                    if(curstr==="1"){
                        $("#savedate").html(_datamonthformat_edit(itemjson.saveTime));
                    }
                    break;
            }
        }
        localStorage.setItem("editorcontent","");
        localStorage.setItem("editorcontent",itemjson.jmContent);
        if (isNull(itemjson.jmContent)) {
            var ue = UE.getEditor('container');//设置编辑器
            ue.ready(function () {
                var val = localStorage.getItem("editorcontent");
                if(isNull(val)) {
                    ue.setContent(val);
                }
            });
        }

        var rd = "#radioBox" + itemjson.qrcodeType;
        // $(".u-rb :radio[name=bulid]:not(:checked)");//清除radio的选中状态
        $(rd).prop("checked", true);
    };

    var _picturemss_datas_save = function () {
        $("#pmsg_save").click(function () {
           // var str ="";
          // str = _bind_data_post();//数据格式化
            if(_bind_data_post()) {
                _bind_patchPicUrls().then(function () {//图片处理
                    if (optstatus === "add") {
                        _bind_pictxt_save();//图文消息保存
                    }
                    if (optstatus === "edit") {
                        _bind_pictxt_edit();//图文消息编辑
                    }
                })
            }
        });
    }
    //数据提取
    var _bind_data_post = function () {
    var nextStep= "";
        var vlStatus =false;
        var count=0;
        wxconten_all_json.wxContentCos = [];
        wxconten_all_jsonedt.wxContentUos = [];
        objitems.contents = [];
        objitems.thumbUrls = [];

        $("div[id^='smallimg']").each(function () {
            var num = $(this).attr("id").replace("smallimg", "");
            var index = $(this).index();
            if (num !== "0") {//0为小图模板ID
                var curkey = "pictxt_json" + num;
                var itemjson = JSON.parse(localStorage.getItem(curkey));

                if(num==="1"&&index===0){
                    count=1;
                }else
                    count=index+2;
                var tag = _validate_content(itemjson,count);//验证
                if(isNullNull(tag)) {//不通过提示
                    _mm_mobile_view_msg(tag);
                    return;
                }else{//通过记录
                    nextStep+= "1,";
                }
                //抓取正文前54个字
                var digVal = itemjson.content;
                if(!isNull(itemjson.digest)){
                    digVal= digVal.substring(0,54);
                    itemjson.digest = digVal;
                }
                itemjson.content="";//清空纯文本
                //
                //内容图片
                $("#tempcontent").html("");
                $("#tempcontent").html(itemjson.jmContent);
                $("#tempcontent").find("img").each(function () {
                    var itemobj = {};
                    var imgid = _picturemss_random_Id();
                        $(this).attr("id", imgid);
                        itemobj.picId = imgid;
                        itemobj.filePath = $(this).attr("src");
                        objitems.contents.push(itemobj);//内容图片批处理对像
                });

                itemjson.jmContent = $("#tempcontent").html();
                itemjson.content = $("#tempcontent").html();
                //缩略图批量
                var slobj = {};
                var randonnum = _picturemss_random_Id();
                slobj.thumbId = randonnum;
                slobj.thumbUrl = itemjson.thumbUrl;
                objitems.thumbUrls.push(slobj);//缩略图批处理对像
                itemjson.thumbId = randonnum;
                var newitem ={};
                newitem.title = "";
                newitem.author = "";
                newitem.thumbMediaId = "";
                newitem.thumbUrl = "";
                newitem.digest = "";
                newitem.content = "";
                newitem.jmContent = "";
                newitem.qrcodeType = "";
                newitem.contentSourceUrl = "";
                newitem.showCoverPic = "0";
                newitem.sendType = "mpnews";
                newitem.saveTime = "";
                newitem.title = itemjson.title;
                newitem.author = itemjson.author;
                newitem.thumbMediaId = itemjson.thumbId;
                newitem.thumbUrl = itemjson.thumbUrl;
                newitem.digest = itemjson.digest;
                newitem.content = itemjson.content;
                newitem.jmContent = itemjson.jmContent;
                newitem.qrcodeType = itemjson.qrcodeType;
                newitem.contentSourceUrl = itemjson.contentSourceUrl;
                newitem.showCoverPic = itemjson.showCoverPic;
                newitem.sendType = itemjson.sendType;
                newitem.saveTime = itemjson.saveTime;
                if(optstatus==="add"){
                    wxconten_all_json.wxContentCos.push(newitem);//保存图文消息集
                }
                if(optstatus==="edit"){
                    if(itemjson.id!=="") {//编辑存id
                        newitem.id = "";
                        newitem.id = itemjson.id;
                    }
                    if(isNull(itemjson.deleteIds)){//存储删除的id
                        var str =itemjson.deleteIds;
                     //   newitem.deleteIds= str.substring(0,str.length-1);
                        wxconten_all_jsonedt.deleteIds= str.substring(0,str.length-1);
                    }
                    if(num==="1") {//大图状态
                        newitem.status = dtstatus;
                    }else {  //大图不参与排序
                        newitem.sort = "";
                        newitem.sort = index;
                    }
                    wxconten_all_jsonedt.wxContentUos.push(newitem);//编辑图文消息集
                }
            }
        });

        if(isNull(nextStep)){
            nextStep=nextStep.substring(0,nextStep.length-1);
            var tempS= nextStep.split(",");
            if(tempS.length==count){
                vlStatus =true;
            }
        }

        return vlStatus;
    }
    //验证图文内容
    var _validate_content= function(itemjson,number){
        var tempObj={imgM:"",titleM:"",conM:"",urlM:""};
        var imgM="封面图";
        var titleM = "标题";
        var conM="正文";
        var urlM="原文链接地址无效"
        var tag="";
        if(!isNull(itemjson.thumbUrl)){
            tempObj.imgM=imgM;
        }
        if(!isNull(itemjson.title)){
            tempObj.titleM=titleM;
        }
        if(!isNull(itemjson.jmContent)){
            tempObj.conM=conM;
        }
        if(isNull(itemjson.contentSourceUrl)){
            if(!checkeURL(itemjson.contentSourceUrl)) {
                tempObj.urlM = urlM;
            }
        }
        if(isNull(tempObj.imgM)){
            tag += tempObj.imgM+" ";
        }
        if(isNull(tempObj.titleM)){
            tag += tempObj.titleM+" ";
        }
        if(isNull(tempObj.conM)){
            tag += tempObj.conM+" ";
        }

        if(isNullNull(tag)){
            tag ="图文"+number+"  请补充<font style='color:#f38925;'>"+tag+"</font>信息";
        }
        if(isNull(tempObj.urlM)&&isNullNull(tag)){  //原文链接验证信息
            tag+="</br>图文"+number+"  <font style='color:#f38925;'>"+tempObj.urlM+"</font>";
        }
        if(isNull(tempObj.urlM)&&!isNullNull(tag)){
            tag="图文"+number+"  <font style='color:#f38925;'>"+tempObj.urlM+"</font>";
        }
        return tag;
    }
    //

    /**
     * 判断是否是合法的url地址
     */
    function checkeURL(str){
        //在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号
        var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
        var objExp=new RegExp(Expression);
        if(str.indexOf("localhost")){
            str = str.replace("localhost","127.0.0.1");
        }
        if(objExp.test(str)==true){
            return true;
        }else{
            return false;
        }
    }
    //清空 wxconten_itemCos
    var _bind_clear_wxconten_itemCos=function () {
        newitemCos.title = "";
        newitemCos.author = "";
        newitemCos.thumbMediaId = "";
        newitemCos.thumbUrl = "";
        newitemCos.digest = "";
        newitemCos.content = "";
        newitemCos.jmContent = "";
        newitemCos.qrcodeType = "";
        newitemCos.contentSourceUrl = "";
        newitemCos.showCoverPic = "0";
        newitemCos.sendType = "mpnews";
    }

    //批处理图片
    var _bind_patchPicUrls = function () {
        var dfdPlay = $.Deferred(); // 新建一个deferred对象
        $.ajaxJson(ajaxUrl.url1, objitems, {
            "done": function (res) {
                if (res.code === 0) {
                    var wxcon =null;
                    if(optstatus==="add"){
                        wxcon = wxconten_all_json.wxContentCos;//保存图文消息集
                    }
                    if(optstatus==="edit"){
                        wxcon = wxconten_all_jsonedt.wxContentUos;//编辑图文消息集
                    }
                    var con_obj = res.data.contents;
                    var thumb_obj = res.data.thumbUrls;
                    for (var i = 0, num = wxcon.length; i < num; i++) {
                        $("#tempcontent").html("");
                        $("#tempcontent").html(wxcon[i].jmContent);
                        $("#tempcontent").find("img").each(function () {
                            var imgid = $(this).attr("id");
                            for (var k = 0, len = con_obj.length; k < len; k++) {
                                if (imgid === con_obj[k].picId) {
                                    $(this).attr("src", con_obj[k].filePath);
                                }
                            }
                        });
                        wxcon[i].content = $("#tempcontent").html();//微信端口内容
                        for (var j = 0, cnt = thumb_obj.length; j < cnt; j++) {//缩略图
                            if (wxcon[i].thumbMediaId === thumb_obj[j].thumbId) {
                                wxcon[i].thumbMediaId = thumb_obj[j].thumbMediaId;
                            }
                        }
                    }
                    if(optstatus==="add"){
                        wxconten_all_json.wxContentCos = wxcon;//保存微信端图文消息格式集
                    }
                    if(optstatus==="edit"){
                        wxconten_all_jsonedt.wxContentUos = wxcon;//编辑图文消息集
                    }


                } else {
                    alert("系统内部错误！");
                }
                dfdPlay.resolve(); // 动画完成
            },
            "fail": function (res) {
                dfdPlay.resolve(); // 动画完成
            }
        });
        return dfdPlay;
    }
    //编辑图文消息
    var _bind_pictxt_edit = function () {

        $.ajaxJson(ajaxUrl.url3, JSON.stringify(wxconten_all_jsonedt), {
            "done": function (res) {
                if (res.code === 0) {
                    var msg = '<div class="m-err-box" ><img class="floatleft" src="' + THIRD_URL + '/css/pc/img/icon_success.png" /><span class="jm-font-yahei-lg">恭喜您，操作成功 </span><span></span></div>';
                    var childrend = dialog({
                        title: '操作提醒',
                        content: msg,
                        id: 'dialog_pp',
                        width: 400,
                        cancelValue: '取消',
                        cancel: function () {
                            pictxtadd_dg.close().remove();
                        },
                        okValue: '&nbsp;确定&nbsp;',
                        ok: function () {
                            _picturemss_list_fixed();
                            pictxtadd_dg.close().remove();
                        }

                    });
                    childrend.showModal();
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

    //新增图文消息
    var _bind_pictxt_save = function () {

        $.ajaxJsonPut(ajaxUrl.url3, JSON.stringify(wxconten_all_json), {
            "done": function (res) {
                if (res.code === 0) {
                   /* console.log(res);*/
                    var msg = '<div class="m-err-box" ><img class="floatleft" src="' + THIRD_URL + '/css/pc/img/icon_success.png" /><span class="jm-font-yahei-lg">恭喜您，操作成功 </span><span></span></div>';
                    var childrend = dialog({
                        title: '操作提醒',
                        content: msg,
                        id: 'dialog_pp',
                        width: 400,
                        cancelValue: '取消',
                        cancel: function () {
                        },
                        okValue: '&nbsp;确定&nbsp;',
                        ok: function () {
                            _picturemss_list_fixed();
                            pictxtadd_dg.close().remove();
                        }

                    });
                    childrend.showModal();
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

    //预览开始
    var _bind_data_view = function () {
        $("div[id^='view_']").click(function () {
          //  optstatus="edit";
            picmsg_editdt = null;
            var id = $(this).attr("id").replace("view_", "");
            editId =id;
            _mobile_list_single(editId);

        });
    }

    var _mobile_list_close_single=function () {
        _mobile_list_single(editId);
    }

    var _mobile_list_single=function (id) {
        $.ajaxJsonGet(ajaxUrl.url4 + "/" + id, {
            "done": function (res) {
                if (res.code === 0) {

                   // picmsg_editdt = res.data;
                    var data = res.data;
                    if (isNull(data.saveTime)) {
                        var stime = _datamonthformat_edit(data.saveTime);//日期格式化
                        data.saveTime = stime;
                    }
                    jumi.template('wechat/mm_picturemsg_mobilelist_sg', data, function (tpl) {
                        $("#layer").show();
                        $("#layer").html("");
                        $("#layer").html(tpl);
                        document.body.scrollTop = 0;//滚动条复位
                        window.screenTop = 0;
                        $('body').css("overflow", "hidden");
                    });
                }
            }
        });
    };
    //预览结束
    //编辑开始
    var _bind_data_edit = function () {
        $("div[id^='edit_']").click(function () {
            optstatus="edit";
            picmsg_editdt = null;
            var id = $(this).attr("id").replace("edit_", "");
            editId =id;
            var tempid = "status_"+id;
            dtstatus= $("div[id^='"+tempid+"']").html();
            $.ajaxJsonGet(ajaxUrl.url4 + "/" + id, {
                "done": function (res) {
                    if (res.code === 0) {
                        picmsg_editdt = res.data;

                        jumi.template('wechat/mm_picturemsg_add', function (tpl) {
                            pictxtadd_dg = dialog({
                                title: '图文编辑',
                                content: tpl,
                                zIndex: 100,
                                width: 1000,
                                id: 'dialog_edit',
                                onshow: function () {
                                    //重置标记
                                    number = 1;  //消息图文增加的数量
                                    tagnumber = 1;//消息图文增加的标记id
                                    localStorage.setItem("curnum", tagnumber);//当前项下标
                                    _initEditor('container');
                                    _addImgEvent("#txtCon_3");//图片绑定
                                    _picturemss_cancel();//取消
                                    _picturemss_smallimg_add();
                                    _picturemss_setselected();
                                    _picturemss_datas_save();//保存
                                    _mm_pictxt_mobileView();//手机预览

                                    _bind_ritem();
                                    _mm_savetime();//日期控件初始化
                                    if(picmsg_editdt!==null) {
                                        _bind_data_con();
                                        _picturemss_smallimg_down();
                                        _picturemss_smallimg_up();
                                    }
                                },
                                onclose: function () {
                                    dialog.get("dialog_edit").close().remove();
                                }
                            });
                            pictxtadd_dg.showModal();
                        });
                    }
                }
            })
        });
    }

    var _bind_data_con = function () {
        var items = picmsg_editdt;
        wxconten_item.id=items.id;//父类id
        wxconten_item.title = items.title;
        wxconten_item.author = items.author;
        wxconten_item.thumbUrl = items.thumbUrl;
        wxconten_item.digest = items.digest;
        wxconten_item.content = items.content;
        wxconten_item.jmContent = items.jmContent;
        wxconten_item.qrcodeType = items.qrcodeType;
        wxconten_item.contentSourceUrl = items.contentSourceUrl;
        wxconten_item.showCoverPic = items.showCoverPic;
        wxconten_item.sendType = items.sendType;
        wxconten_item.saveTime = items.saveTime;
        localStorage.setItem("pictxt_json1", JSON.stringify(wxconten_item));//大图json数据初始化
        $("#bindi1_1").html("");
        $("#bindi1_1").html(wxconten_item.title);
        if(isNull(items.saveTime)) {
            var stime = _datamonthformat_edit(items.saveTime);//日期格式化
            $("#savedate").html(stime);
        }

        $("#bindi1_2").html(wxconten_item.author);
        $("#bindi1_3").attr("src",wxconten_item.thumbUrl);
        $("#bindi1_4").html(wxconten_item.digest);
        $("#smallimg1").addClass("b-sel");
        $("#txtCon_1").val(wxconten_item.title);
        $("#txtCon_2").val(wxconten_item.author);
        $("#txtCon_3").attr("src",wxconten_item.thumbUrl);
        $("#txtCon_4").val(wxconten_item.digest);
        $("#txtCon_6").val(wxconten_item.saveTime);
        _picturemss_fontnum('#txtCon_4','#abs_num',60);//输入字数提示
        localStorage.setItem("editorcontent","");
        localStorage.setItem("editorcontent",wxconten_item.jmContent);
        if (isNull(wxconten_item.jmContent)) {
            var ue = UE.getEditor('container');//清编辑器
            ue.ready(function () {
                ue.setContent(localStorage.getItem("editorcontent"));
            });
        }
        $("#radioBox"+wxconten_item.qrcodeType).prop("checked",true);
        $("#txtCon_5").val(wxconten_item.contentSourceUrl);
        _bind_clear_wxconten_item();//清空wxconten_item
        //小图数据绑定
        if(items.wxContentSubVos!==null){
            var datajsons =items.wxContentSubVos;
            for(var i=0,num=datajsons.length;i<num;i++){
                _bind_data_consmallpic(datajsons[i]);
            }

        }
    }

    var _bind_data_consmallpic=function (datajson) {

        number = $(".pictxtitem-box").length;
        if (number < 8) {
            $("#fulltxt1").hide();//隐藏阅读全文
            var html = $("div[id='left_area'] div[id='smallimg0']").clone();
            var idstr = html.attr("id");
            tagnumber = tagnumber + 1;
          //  localStorage.setItem("curnum", tagnumber);//当前项下标
            idstr = "smallimg" + tagnumber;//新增项id
            var cls = "bindi" + tagnumber;//class 片段名
            html.attr("id", idstr);
            html.css("display", "");
            html.find(".icon-delete").attr("id", "del" + tagnumber);
            html.find("#bindi0_1").html(datajson.title);
            html.find("#bindi0_1").attr("id", cls + "_1");
            html.find("#bindi0_3").attr("src",datajson.thumbUrl);
            html.find("#bindi0_3").attr("id", cls + "_3");
            html.find(".pictxtdata0").attr("id", "pictxt_json" + tagnumber);
            html.find(".readtext-hide").css("display", "none");

           // $("div[id='left_area_bottom'] .readtext-hide").css("display", "none");
            $("div[id='left_area_bottom'] div[id='smallimg0']").before(html);
            //  _bind_ritem_clear();//清空右边内容
            wxconten_item.id=datajson.id;//父类id
            wxconten_item.title = datajson.title;
            wxconten_item.author = datajson.author;
            wxconten_item.thumbUrl = datajson.thumbUrl;
            wxconten_item.digest = datajson.digest;
            wxconten_item.content = datajson.content;
            wxconten_item.jmContent = datajson.jmContent;
            wxconten_item.qrcodeType = datajson.qrcodeType;
            wxconten_item.contentSourceUrl = datajson.contentSourceUrl;
            wxconten_item.showCoverPic = datajson.showCoverPic;
            wxconten_item.sendType = datajson.sendType;
            wxconten_item.saveTime = datajson.saveTime;
            localStorage.setItem("pictxt_json" + tagnumber, JSON.stringify(wxconten_item));//小图json数据初始化
            _bind_clear_wxconten_item();//清空wxconten_item
            if(number===7){
                $("#but_smallimg").hide();
            }
        }

    }
   //清空wxconten_item
    var _bind_clear_wxconten_item = function(){
        wxconten_item.id="";
        wxconten_item.title = "";
        wxconten_item.author = "";
        wxconten_item.thumbId = "";
        wxconten_item.thumbUrl = "";
        wxconten_item.digest = "";
        wxconten_item.content = "";
        wxconten_item.jmContent = "";
        wxconten_item.qrcodeType = "";
        wxconten_item.contentSourceUrl = "";
        wxconten_item.showCoverPic = "0";
        wxconten_item.sendType = "mpnews";
        wxconten_item.saveTime = "";
    }


    //编辑结束


    //双向绑定结束


    //当前页定位 --- 图文信息列表
    var _picturemss_list_fixed = function () {
        var curPage = $("#pageToolbar_page").val();

        var contentRo = {
            pageSize: 10,
            curPage: curPage || 0,
            sendType:"mpnews"
        };
        $.ajaxJson(ajaxUrl.url2, contentRo, {
            "done": function (res) {
                if (res.code === 0) {
                    var data = {
                        items: res.data.items
                    }
                    for (var i = 0, len = data.items.length; i < len; i++) { //日期格式化
                        var obj = data.items[i];
                        if(isNull(obj.saveTime)) {
                            var dt = _timeformat(obj.saveTime);
                            obj.saveTime = dt;
                        }
                    }
                    jumi.template("wechat/mm_picturemsg_list", data, function (tpl) {
                        $('#mm_picmsg_list').empty();
                        $('#mm_picmsg_list').html(tpl);
                    })
                }
            }
        })
    };

    //图文信息列表
    var _picturemss_list = function () {
        var contentRo = {
            pageSize: 10,
            sendType:"mpnews"
        };
        jumi.pagination('#pageToolbar', ajaxUrl.url2, contentRo, function (res, curPage) {
            if (res.code === 0) {
                //判断是否第一页
                var data = {
                    items: res.data.items
                };

                for (var i = 0, len = data.items.length; i < len; i++) { //日期格式化
                    var obj = data.items[i];
                    if(isNull(obj.saveTime)) {
                        var dt = _timeformat(obj.saveTime);
                        obj.saveTime = dt;
                    }
                }
                if (curPage === 0) {
                    data.isFirstPage = 1;
                } else {
                    data.isFirstPage = 0;
                }
                jumi.template("wechat/mm_picturemsg_list", data, function (tpl) {
                    $('#mm_picmsg_list').empty();
                    $('#mm_picmsg_list').html(tpl);
                })


            }
        });
    };

    //日期截取
    var _timeformat = function (timedt) {
        var tm = new Date(Date.parse(timedt.replace(/-/g, "/")));
        var month = tm.getMonth() + 1;
        var dt = tm.getFullYear() + "-" + month + "-" + tm.getDate();
        return dt;
    }
    //日期 X月X日
    var _datamonthformat = function () {
        var oDate = new Date(); //实例一个时间对象；
        var month = oDate.getMonth() + 1;     //月
        var day = oDate.getDate();            //日
        var dt = month + "月" + day + "日";//
        $("#savedate").html(dt);
    }

    //日期 X月X日
    var _datamonthformat_edit = function (dt) {
        var oDate = new Date(Date.parse(dt.replace(/-/g, "/")));
        var month = oDate.getMonth() + 1;    //月
        var day = oDate.getDate();            //日
        var val = month + "月" + day + "日";
        return val;
    }


    ///////图文消息删除start
    var _mm_pictxt_del = function () {
        $("div[id^='del_']").click(function () {
            var itemid = $(this).attr("id").replace("del_", "");
            jumi.template('wechat/mm_picturemsg_del_dialog', function (tpl) {
                var wx_del = dialog({
                    title: '操作提醒',
                    content: tpl,
                    zIndex: 10000,
                    width:400,
                    id: 'dialog_wxdel',
                    onclose: function () {
                        dialog.get("dialog_wxdel").close().remove();
                    }
                });
                wx_del.showModal();
                _mm_pictxt_delitem(itemid);
                _mm_pictxt_delcancel();

            });
        });
    }

   var _mm_pictxt_delcancel = function(){
       $("#makesure_canl").click(function () {
           dialog.get("dialog_wxdel").close().remove();
       });
   }

    var _mm_pictxt_delitem = function (itemid) {
        $("#makesure_del").click(function () {
            dialog.get("dialog_wxdel").close().remove();
            $.ajaxJsonDel(ajaxUrl.url3 + "/" + itemid, {
                "done": function (res) {
                    if (res.code === 0) {
                        _picturemss_list_fixed();
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

    ///////删除end
    
    ////////手机预览开始
//关注帐号录入
    var _mm_mobile_view_dg=function () {

        $.ajaxJsonGet(ajaxUrl.url7 , {
            "done": function (res) {
                if (res.code === 0) {
                  // console.log(res);
                    jumi.template('wechat/mm_picturemsg_mb_dialog',res.data, function (tpl) {
                        wx_dg = dialog({
                            title: '发送预览',
                            content: tpl,
                            zIndex: 10000,
                            width: 659,
                            id: 'dialog_wx',
                            onclose: function () {
                                dialog.get("dialog_wx").close().remove();
                            }
                        });
                        wx_dg.showModal();
                    });
                }
            }
        });
    };


    var _mm_mobile_view = function () {
        //在JavaScript中，正则表达式只能使用"/"开头和结束，不能使用双引号
        var Expression=/[\u4e00-\u9fa5]/;//中文正式表达示
        var objExp=new RegExp(Expression);

        var wxhao = $("#wxhao").val();
        if(objExp.test(wxhao)) {
            $("#sCodeMsg").show();//关注提示
            return;
        }
        var wxContentVo = {};
        wxContentVo.contentId = editId;
        wxContentVo.towxname = wxhao;
        $.ajaxJson(ajaxUrl.url6, wxContentVo, {
            "done": function (res) {//console.log(res);
                if (res.code === 0) {
                    if(res.data.code===0) {
                        _mm_mobile_view_msg("发送成功，请留意您的手机");
                        dialog.get("dialog_wx").close().remove();
                    }else {
                        $("#sCodeMsg").show();
                    }
                }else{
                    $("#sCodeMsg").show();
                }
            }
        });
    };

    var _mm_mobile_view_msg =function(info){
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

    var _mm_mobile_cancel=function () {
        dialog.get("dialog_wx").close().remove();
    };

    //手机预览图文详情大图  wechat/mm_picturemsg_mobiledt
    var _mm_mobile_viewDetailbig=function (id,pathstr) {

        $.ajaxJsonGet(ajaxUrl.url4 + "/" + id, {
            "done": function (res) {
                if (res.code === 0) {
                    var data =res.data;
                    if(isNull(data.saveTime)) {
                        var stime = _datamonthformat_edit(data.saveTime);//日期格式化
                        data.saveTime =stime;
                    }
                    jumi.template(pathstr,data, function (tpl) {
                        document.body.scrollTop=0;
                        window.screenTop =0;
                        $('body').css("overflow","hidden");
                        $("#layer").show();
                        $("#layer").html("");
                        $("#layer").html(tpl);
                    })
                }
            }
        });
    };
    //手机预览图文详情小图 wechat/mm_picturemsg_mobiledt
    var _mm_mobile_viewDetailsmall=function (id,pathstr) {
        $.ajaxJsonGet(ajaxUrl.url5+ "/" + id, {
            "done": function (res) {
                if (res.code === 0) {
                    var data =res.data;
                    if(isNull(data.saveTime)) {
                        var stime = _datamonthformat_edit(data.saveTime);//日期格式化
                        data.saveTime =stime;
                    }
                    jumi.template(pathstr,data, function (tpl) {
                        document.body.scrollTop=0;
                        window.screenTop =0;
                        $('body').css("overflow","hidden");
                        $("#layer").show();
                        $("#layer").html("");
                        $("#layer").html(tpl);

                    })
                }
            }
        });
    };
    //返回
    var _mm_mobile_back=function () {
        _pictxt_mobile_list(editId);
    };
    
    var _mm_pictxt_mobileView=function () {
        $("#pmsg_view").click(function () {

            if(_bind_data_post()) {//数据格式化
                _bind_patchPicUrls().then(function () {//图片处理
                    if (optstatus === "add") {   //图文消息保存
                        _bind_pictxt_saveforMbView();
                    }
                    if (optstatus === "edit")//图文消息编辑
                    {
                        _bind_pictxt_editforMbView(editId);
                    }
                })
            }
        });
    };

    //编辑图文消息
    var _bind_pictxt_editforMbView = function (editId) {

        $.ajaxJson(ajaxUrl.url3, JSON.stringify(wxconten_all_jsonedt), {
            "done": function (res) {
                if (res.code === 0) {
                    if(res.data.msg==="ok") {
                        _pictxt_mobile_list(editId);

                    }
                } else {

                }
            },
            "fail": function (res) {
            }
        });
    };

    //新增图文消息
    var _bind_pictxt_saveforMbView = function () {

        $.ajaxJsonPut(ajaxUrl.url3, JSON.stringify(wxconten_all_json), {
            "done": function (res) {
                if (res.code === 0) {

                        optstatus="edit";
                        editId =res.data.data;
                        _pictxt_mobile_list(editId);
                } else {

                }
            },
            "fail": function (res) {
            }
        });
    };
//手机预览列表
    var _pictxt_mobile_list=function (id) {
        $.ajaxJsonGet(ajaxUrl.url4 + "/" + id, {
            "done": function (res) {
                if (res.code === 0) {
                  if(isNull(dialog.get("dialog_edit"))) {
                      dialog.get("dialog_edit").close().remove();
                      pictxtadd_dg=null;
                  }
                    picmsg_editdt = res.data;
                    var data =res.data;
                    if(isNull(data.saveTime)) {
                        var stime = _datamonthformat_edit(data.saveTime);//日期格式化
                        data.saveTime =stime;
                    }
                    jumi.template('wechat/mm_picturemsg_mobilelist',data, function (tpl) {
                        $("#layer").show();
                        $("#layer").html("");
                        $("#layer").html(tpl);
                        document.body.scrollTop=0;//滚动条复位
                        window.screenTop =0;
                        $('body').css("overflow","hidden");
                    });
                }
            }
        });
    };
    var _pictxt_mobile_close=function () {
        $("#layer").hide();
        $("#layer").html("");
        $('body').css("overflow","auto");

    };

    //重新加载对话框--手机预览返回
     var _pictxt_mobile_backDialog=function(){
         optstatus="edit";
         picmsg_editdt = null;
       //  var id = $(this).attr("id").replace("edit_", "");
      //   editId =id;
        // var tempid = "status_"+id;
       //  dtstatus= $("div[id^='"+tempid+"']").html();
         $.ajaxJsonGet(ajaxUrl.url4 + "/" + editId, {
             "done": function (res) {
                 if (res.code === 0) {
                     picmsg_editdt = res.data;

                     jumi.template('wechat/mm_picturemsg_add', function (tpl) {
                         pictxtadd_dg = dialog({
                             title: '图文编辑',
                             content: tpl,
                             zIndex: 100,
                             width: 1000,
                             id: 'dialog_edit',
                             onshow: function () {
                                 $("#layer").hide();//隐藏手机预览
                                 $("#layer").html("");
                                 $('body').css("overflow","auto");//恢复滚动条状态
                                 //重置标记
                                 number = 1;  //消息图文增加的数量
                                 tagnumber = 1;//消息图文增加的标记id
                                 localStorage.setItem("curnum", tagnumber);//当前项下标
                                 _initEditor('container');
                                 _addImgEvent("#txtCon_3");//图片绑定
                                 _picturemss_cancel();//取消
                                 _picturemss_smallimg_add();
                                 _picturemss_setselected();
                                 _picturemss_datas_save();//保存
                                 _mm_pictxt_mobileView();//手机预览
                                 _bind_ritem();
                                 _datamonthformat();
                                 if(picmsg_editdt!==null) {
                                     _bind_data_con();
                                 }
                                 _picturemss_list_fixed();
                             },
                             onclose: function () {
                                 dialog.get("dialog_edit").close().remove();
                             }
                         });
                         pictxtadd_dg.showModal();
                     });
                 }
             }
         })
     }
    ////////手机预览结束
    //日期
    var _mm_savetime=function () {
        var dtobj = $("#txtCon_6");
        $("#txtCon_6").datetimepicker({
            showSecond : false,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1,
            onSelect: function (selectedDateTime){  //绑定
                var tag = localStorage.getItem("curnum");
                var curtag = "pictxt_json" + tag;
                var itemjson = JSON.parse(localStorage.getItem(curtag));
                itemjson.saveTime = selectedDateTime; //缓存
                localStorage.setItem(curtag, JSON.stringify(itemjson));
                dtobj.datetimepicker('setDate', selectedDateTime);//日期控件
                var stime = _datamonthformat_edit(selectedDateTime);//日期格式化
                if(tag==="1"){
                    $("#savedate").html(stime);//右边文本框
                }

            }
        });
        var curTime = _getCurrentTime();
        dtobj.val(curTime);
        $("#savedate").html(_datamonthformat_edit(curTime));

    };
   //当前日期
    var _getCurrentTime = function () {
        var oDate = new Date();
        var month = oDate.getMonth() + 1;
        var dt = oDate.getFullYear() + "-" + month + "-" + oDate.getDate()+" 00:00:00";
        return dt;
    }

    return {
        init: _init,
        picturemss_fontnum: _picturemss_fontnum, //摘要输入字符限定
        picturemss_limitfontnum: _picturemss_limitfontnum,
        /*init_open: _init_open,*/
        bind_data_edit: _bind_data_edit,//图文消息编辑
        mm_pictxt_del: _mm_pictxt_del,//图文消息删除
        bind_data_view:_bind_data_view,//图文消息预览
        pictxt_mobile_close:_pictxt_mobile_close,
        mm_mobile_back:_mm_mobile_back,//返回
        mm_mobile_viewDetailbig:_mm_mobile_viewDetailbig,
        mm_mobile_viewDetailsmall:_mm_mobile_viewDetailsmall,
        mm_mobile_view:_mm_mobile_view,
        mm_mobile_view_dg:_mm_mobile_view_dg,//发送手机预览
        mm_mobile_cancel:_mm_mobile_cancel,
        pictxt_mobile_backDialog:_pictxt_mobile_backDialog,//重新加载对话框--手机预览返回
        mobile_list_close_single:_mobile_list_close_single //列表预览
    };
})();