/**
 * Created by wxa on 2016/11/15
 * 消息精携
 */
CommonUtils.regNamespace("mm", "wepushmsg");
mm.wepushmsg = (function () {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/wx/groups',//群发对象
        url2: CONTEXT_PATH + '/wx/level',//等级选择
        url3: CONTEXT_PATH + '/content/openids_template',  //筛选
        url4: CONTEXT_PATH +'/content/templatemsg', //发送
        url5: CONTEXT_PATH +'/content/template_list'
    };
    var _init = function () {
        _mm_tabchange();
    };

    //群发消息页面切换
    var _mm_tabchange=function(){
        var tabul = $(".m-tab ul li");
        tabul.eq(0).removeClass('z-sel').addClass("z-sel");
        $(".panel-hidden").hide().eq(0).show();
        _init_pushmsg();
        tabul.click(function(){
            var index = $(this).index();
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq(index).show();
            if(index===0){
                _init_pushmsg();
            }
            if(index===1){
                _mm_pushmsg_list();
            }
        });
    };

    //已发送列表渲染
    var _mm_pushmsg_list=function (title) {

        var url = ajaxUrl.url5;
        var wxTemplateMsgQo = {//  status:0,   //未发送
            pageSize:10
        };
        jumi.pagination('#pageToolbar',url,wxTemplateMsgQo,function(res,curPage){
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

                for(var i=0,count=data.items.length;i<count;i++){
                    var status = data.items[i].status;
                    switch (status){
                        case 0:
                            data.items[i].statusName="等待发送";
                            break;
                        case 1:
                            data.items[i].statusName="发送成功";
                            break;
                        case 2:
                            data.items[i].statusName="发送失败";
                            break;
                    }
                }
                jumi.template('wechat/push_msg_send',data,function (tpl) {
                    $('#table-body').html("");
                    $('#table-body').html(tpl);
                });
                _init_sended_view();
            }
        })
    }

    var _init_pushmsg = function () {
        jumi.Select("#userGroup");
        _mm_usergroup();
        jumi.Select("#sex");
        _sended_area_template();
        jumi.Select("#zbRole");
        _mm_bind_roleData();// 角色选择
        _reply_linkset();//点击设置
        _mm_timesend();//定时
        var pushObj = null;
        localStorage.setItem("pushObj", pushObj);//清除筛选对象
        _mm_subStartDate(); //关注开始时间
        _mm_subEndtDate(); //关注结束时间
        _validate(); //验证绑定
        _addImgEvent();//绑定主图事件
    };
    


    var _addImgEvent = function(id){
        $('#abstract').click(function(){
            var d = new Dialog({
                context_path:CONTEXT_PATH, //请求路径,  必填
                resType:1 ,//图片1，视频2，语音3 必填
                callback:function(url){
                    $('#abstract').attr('src',url);
                    $('#abstract').data('url',url);
                }
            });
            d.render();
        })
    };


    //显示"点击查看"对话框
    var _init_sended_view = function () {
        $("div[id^='clickview_']").click(function () {
            var id = $(this).attr("id").replace("clickview_","");
            var dataid = "#pushview_"+id;
            var data = JSON.parse($(dataid).val());
            data.id= id;
            data.sexName = _getsex(data.sex);
            localStorage.setItem("push_viewobj",JSON.stringify(data));
            _mm_set_roleData();
        });
    };

    var _mm_set_roleData = function () {
        jumi.file("role.json", call_backrole);
    }

    function call_backrole(res){
        var rolearr = res["data"];
        var data = JSON.parse(localStorage.getItem("push_viewobj"));
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
            $("div[id^='pushcontainer_']").hide();
            var container = "#pushcontainer_"+data.id;
            $(container).show();
            $(container).html(tpl);

        });
    }

    var _getsex =function (key) {
        var val ="";
        switch (key){
            case -1:
                val="全部";break;
            case 0:
                val="无";break;
            case 1:
                val="男";break;
            case 2:
                val="女";break;
        }
        return val;
    }


    //关闭"点击查看"对话框
    var _init_sended_viewclose = function(){
        $("i[id^='colse_']").click(function () {
            $("div[id^='pushcontainer_']").hide();
        });
    }

    //筛选栏收缩
    var _mm_slide = function () {
        $("#push_pagebtn").click(function () {
            $("#push-m-search").slideToggle("fast");
            $(this).toggleClass("btn-slide1");
        });
    }

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


    ////地区选择start
    var _sended_area_template = function () {
        jumi.template('customer/customer_area', function (tpl) {
            $("#area_title").html("地区选择");
            $('#customer_content').html(tpl);
            $("#area_title").text("地区选择");
            $("#area_title").css("min-width", "90px");
            $("#area_title").css("margin-right", "5px");

        })
    }



    //点击设置
    var _reply_linkset = function () {
        $("#showset").click(function () {
            _reply_shopurl();
        });
    }

    //点击设置对话框  链接
    var _reply_shopurl = function () {
        var shopId = $("#shopId").val();
        menuDialog.Menu.initPage({
            shop_id: shopId,
        }, function (menu) {
            menu.getUrl(function (link) { //console.log(link);
                var url;
                switch (link.link_type) {
                    case '1':
                        url = DOMAIN + link.link_url + "?shopId=" + link.shop_id;
                        break;
                    case '2':
                        url = DOMAIN + link.link_url + '/' + link.link_id + "?shopId=" + link.shop_id;
                        break;
                }
                var urlLink = $("#urlLink");
                var urlName = $("#urlName");
                urlLink.val("");
                urlLink.val(url);
                urlName.val("");
                var title = link['data-name'];//选项卡详情名称
                if(!isNull(title)){
                    title =link.link_name;//类别名
                }
                urlName.val(title);
                //$("#first").val(title);
            });
        });
    };

    //筛选
    var _mm_filter = function () {
        $("#push_select").click(function () {
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
        localStorage.setItem("areaString","");
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
        var  tempselected ={};
        if (isNull(reval))
            itemsarr = reval.split(',');
        //发送时保存
        tempselected.areaIds=itemsarr;

        var wxContentVo = {};
        wxContentVo.areaIds = itemsarr;
        wxContentVo.groupid = $("#userGroup").val();  //组对象
        wxContentVo.sex = $("#sex").val();  //性别
        wxContentVo.role = $("#zbRole").val();//角色选择
        var subStartDate =$("#subStartDate").val();//关注时间
        var subEndtDate = $("#subEndtDate").val();


        if(subStartDate&&subStartDate!==""){
            wxContentVo.subStartDate = subStartDate+" 00:00:00" ;
        }else{
            wxContentVo.subStartDate = '';
        }
        if(subEndtDate&&subEndtDate!==""){
            wxContentVo.subEndtDate = subEndtDate+" 00:00:00" ;
        }else{
            wxContentVo.subEndtDate = '';
        }

        if(!_mm_checkSubDate(wxContentVo.subStartDate,wxContentVo.subEndtDate)){
            _mm_showMessage("关注开始时间 需大于结束时间");
            return;
        }

        var data = JSON.stringify(wxContentVo);
        $("#getoption").val(data);   ///筛选条件
        $.ajaxJson(ajaxUrl.url3, data, {
            "done": function (res) {
                if (res.code === 0) {
                    localStorage.setItem("pushObj", JSON.stringify(res.data));//暂存筛选对象
                    var str = "本次发送目标粉丝 <font style='color:#f38925;'>" + res.data.openids.length + "</font> 人";
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

    //验证消息提示
    var _mm_showMessage = function (info) {
        var dm = new dialogMessage({
            type: 3,
            title: '操作提醒',
            fixed: true,
            msg: info,
            isAutoDisplay: true,
            time: 2000
        });
        dm.render();
    }

    ////地区模板end
    function isNull(data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    };

    var _mm_push = function () {
        $("#pushmsg").click(function () {

          var form = $("#push_msg_form");
            if(form.valid()) {
                _mm_push_postcon();
             }
        });
    }

    
    
    var _mm_push_postcon= function () {
        var pushObj =null;
        pushObj = JSON.parse(localStorage.getItem("pushObj"));//暂存筛选对象

        if(!isNull(pushObj)){
            _mm_showMessage("请先筛选群体");
            return;
        }

        var openids = pushObj.openids;
        var optiondt = JSON.parse($("#getoption").val());
        var wxTemplateMsgCo = {};
        $("input[id^='sendType']").each(function () {  //发送类别
            var isstatus= $(this).prop('checked');
            if(isstatus){  //1是 0否
                wxTemplateMsgCo.isTiming =$(this).val();
            }
        });
       //定时发送
        var timeVal = $("#timemassmsg").val();
        if(!isNull(timeVal)&&wxTemplateMsgCo.isTiming==="1"){
            _mm_showMessage("请选择定时群发时间");
            return;
        }
        wxTemplateMsgCo.timingSendTime=timeVal;
        if(!_mm_checkEndTime(timeVal)&&wxTemplateMsgCo.isTiming==="1"){
            _mm_showMessage("定时群发时间 需大于当前时间");
            return;
        }

        var first = $("#first").val();
        var picUrl = $("#abstract").data('url');
        var urlLink = $("#urlLink").val();
        var remark = $("#remark").val();
        var urlName = $("#urlName").val();
        if(!picUrl){
            _mm_showMessage("主图未选择!");
            return;
        }
        wxTemplateMsgCo.first=first;
        wxTemplateMsgCo.picUrl = picUrl;
        wxTemplateMsgCo.url=urlLink;
        wxTemplateMsgCo.urlName =urlName;
        wxTemplateMsgCo.remark=remark;
        wxTemplateMsgCo.openids = openids;
        wxTemplateMsgCo.areaIds =optiondt.areaIds;
        wxTemplateMsgCo.groupid =optiondt.groupid;
        wxTemplateMsgCo.sex =optiondt.sex;
        wxTemplateMsgCo.role =optiondt.role;
        wxTemplateMsgCo.subStartDate= optiondt.subStartDate;//关注时间
        wxTemplateMsgCo.subEndtDate= optiondt.subEndtDate;
        
        var data = JSON.stringify(wxTemplateMsgCo);
        var args = {};
        args.fn1 = function(){
            _mm_data_post(data);
        };
        args.fn2 = function(){
        };
        jumi.dialogSure("消息开始推送后无法撤销，是否确认推送？", args);
    }


    var _mm_data_post = function (data) {
        $.ajaxJson(ajaxUrl.url4,data,{
            "done":function (res) {
                if(res.code===0) {
                    if(res.data.code===0){
                        _mm_clear();
                      ///  _init();//刷新

                        var dm = new dialogMessage({
                            type: 1,
                            title: '操作提醒',
                            fixed: true,
                            msg: "恭喜您，操作成功",
                            isAutoDisplay: false
                        });
                        dm.render();
                    }else{
                        _mm_showerror(res.data.msg);
                    }

                }else{
                    //alert("系统内部错误！");
                    _mm_showerror(res.msg);
                }
            },
            "fail":function (res) {
            }
        });
    }

    var _mm_showerror = function (message) {
        var dm = new dialogMessage({
            type:2,
            title:'操作提醒',
            fixed:true,
            msg:message,
            isAutoDisplay:false
        });
        dm.render();
    }

    var _mm_clear = function () {
        var pushObj = null;
        localStorage.setItem("pushObj", pushObj);//清除筛选对象
        $("#urlLink").val("");
        $("#urlName").val("");
        $("#getoption").val("");   ///筛选条件
        $("#timemassmsg").val("");//时间
        $("#sendType11").prop("checked",true);
        $("#sendType12").removeAttr("checked");

    };
    var _validate = function() {
        $("#push_msg_form").validate({
            rules: {
                first: {
                    required: true,
                    maxlength:20
                },
                urlLink: {
                    required: true
                },
                remark: {
                    required: true,
                    maxlength:50
                }
            },
            messages: {
                first: {
                    required: "请输入标题",
                    maxlength:"请输入一个 长度最多是20的字符"
                },
                urlLink: {
                    required: "请输入链接"
                },
                remark: {
                    required: "请输入备注",
                    maxlength:"请输入一个 长度最多是50的字符"
                }
            }
        });
    };

    var _mm_bind_roleData = function () {
        jumi.file("role.json", call_backface);
    }

    function call_backface(res){
        var rolearr = res["data"];
        var tpl = '<option value="-1" selected="selected">--全部角色--</option>';
        for(var i=0;i<rolearr.length;i++)
        {
            tpl += '<option value="' + rolearr[i].id + '">' + rolearr[i].name + '</option>';
            $('#zbRole').html(tpl);
        }
    }
//新建群发消息 定时
    var _mm_timesend=function () {

        $("#timemassmsg").datetimepicker({
            closeText: '确定',
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
    }

    var _mm_subStartDate=function () {
        $("#subStartDate").datepicker({
            closeText: '确定',
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
    }
    var _mm_subEndtDate=function () {
        $("#subEndtDate").datepicker({
            closeText: '确定',
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1
        });
    }
    //时间比较
    function _mm_checkEndTime(endTime){
        var start= new Date();
        var end=new Date(endTime.replace("-", "/").replace("-", "/"));
        if(end<=start){
            return false;
        }
        return true;
    }
    //关注时间比较
    function _mm_checkSubDate(startTime,endTime){
        var start= new Date(startTime.replace("-", "/").replace("-", "/"));
        var end=new Date(endTime.replace("-", "/").replace("-", "/"));
        if(end<=start){
            return false;
        }
        return true;
    }
    return {
        init: _init,
        mm_push:_mm_push,
        mm_filter:_mm_filter,//筛选
        init_sended_viewclose:_init_sended_viewclose,//关闭"点击查看"对话框
        mm_checkEndTime:_mm_checkEndTime,
        mm_slide: _mm_slide,   //呼吸页
        init_sended_view:_init_sended_view,
        getsex:_getsex
    };
})();
