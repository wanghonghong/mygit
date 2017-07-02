CommonUtils.regNamespace("customer", "newall");

var curPage = 0 ;
customer.newall = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/customer',
        url2:CONTEXT_PATH+'/wx/level',
        url3:CONTEXT_PATH+'/wx//groups',
        url4:CONTEXT_PATH+'/system/session'
    };
    var _init = function(){
        //_query(0);
        _sended_area_template();
        _query(0);
    };

    var isNull = function (data) {
        return (data == "" || data == undefined || data == null) ? false : true;
    };

    //地区模板start
    var _sended_area_template=function(){
        jumi.template('customer/customer_area',function(tpl){
            $("#area_title").html("地区");
            $('#customer_content').html(tpl);
            $("#area_title").text("地区");
            $("#area_title").css("min-width","60px");
            $("#area_title").css("margin-right","5px");
        })
    }
    ////地区模板end

    var _query = function (curPage) {
        var reval="";
        $("#areaselectd div[class='u-sort active'] input[name='area']").each(function(){
            var val = $(this).attr("id");
            val = val.replace("chb","");
            reval = reval + val + ",";
        });
        if (reval.length > 0) {
            reval = reval.substr(0, reval.length - 1);
        }
        var newVal = "";
        if (isNull(reval))
            newVal = reval;
        localStorage.setItem("areaString", newVal);//存1
        jumi.file("areaAll-new.json", function (res) {
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
            _queryList(curPage,areastr);
        });
    };


    var _queryList = function(curPage,areastr){
        //查询等级列表
        var levels;
        $.ajaxHtmlGet(ajaxUrl.url2,null,{
            done:function(res) {
                levels=res.data;
            }
        });

        var platform=$("#platform option:selected").val();//平台
        if(platform == 1){
            ajaxUrl.url3 = CONTEXT_PATH+'/wbUserGroup/groups';
        }
        //查询分组列表
        var groups;
        $.ajaxHtmlGet(ajaxUrl.url3,null,{
            done:function(res) {
                groups=res.data;
            }
        });

        //获取用户session
        var userSession;
        $.ajaxHtmlGet(ajaxUrl.url4,null,{
            done:function(res) {
                userSession=res.data;
            }
        });

        var name = $("#name").val();
        var phoneNum = $("#phoneNum").val();
        var nikename = $("#nikename").val();
        var sex=$("#select3 option:selected").val();
        var starTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var isSubscribe = $("#isSubscribe option:selected").val();

        var params = {
            pageSize:10,
            nikename:nikename,
            name:name,
            phoneNum:phoneNum,
            sex:sex,
            starSubscribeTime:starTime,
            endSubscribeTime:endTime,
            area:areastr,
            isSubscribe:isSubscribe,
            curPage:curPage
        };

        var url = "";
        var tpurl = "";
        if(platform == 0){
            url = ajaxUrl.url1+'/findAll/1';
            tpurl = "new_customer/customer_wx_all_list";
            $("#platformFont").text("微信");
        }else{
            url = ajaxUrl.url1+'/findAllwb/1';
            tpurl = "new_customer/wb/customer_wb_all_list";
            $("#platformFont").text("微博");
        }

        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items,
                    levels:levels,
                    groups:groups,
                    userSession:userSession,
                    platform:platform
                };

                $("#userCount").text(res.data.count);
                jumi.template(tpurl,data,function(tpl){
                    $('#table-body').empty();
                    $('#table-body').html(tpl);

                    $.each($('.icon-modified'),function(i,modifyBtn){
                        $(modifyBtn).click(function(){
                            var input = $(this).parent().prev();
                            $(input).removeAttr("disabled");
                            $(input)[0].focus();
                            $(input).blur(function(){
                                $(this).attr("disabled","disabled");
                                var groupName = $(this).val();
                                var groupid = $(this).prev().find('input[type="radio"]').val();
                                var url ="";
                                var group ={};
                                group.name = groupName;
                                group.groupid = groupid;
                                if(platform==0){
                                    url = CONTEXT_PATH+"/wx/group";
                                }else{
                                    url = CONTEXT_PATH+"/wbUserGroup/group";
                                }
                                $.ajaxJsonPut(url,group,function(res){
                                    console.log(res);
                                })
                            });
                        });
                    });


                })
            }
        });

    };





    return {
        init :_init,
        query:_query
    };
})();




