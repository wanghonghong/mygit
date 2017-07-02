CommonUtils.regNamespace("customer", "modify");

var curPage = 0 ;
customer.modify = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/customer',
        url2:CONTEXT_PATH+'/wx/level',
        url3:CONTEXT_PATH+'/wx//groups',
        url4:CONTEXT_PATH+'/system/session'
    };
    var _init = function(){
       // _query(0);
        _sended_area_template();
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

    var _query = function(curPage){

        //查询等级列表
        var levels;
        $.ajaxHtmlGet(ajaxUrl.url2,null,{
            done:function(res) {
                levels=res.data;
            }
        });

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

        var reval="";
        $("#areaselectd div[class='u-sort active'] input[name='area']").each(function(){
            var val = $(this).attr("id");
            val = val.replace("chb","");
            reval = reval + val + ",";
        });
        var userId = $("#userId").val();
        var name = $("#name").val();
        var phoneNum = $("#phoneNum").val();
        var nikename = $("#nikename").val();
        var sex=$("#select3 option:selected").val();
        var starTime = $("#startTime").val();
        var endTime = $("#endTime").val();
        var isSubscribe = $("#isSubscribe option:selected").val();

        var url = ajaxUrl.url1+'/findAll/1';
        var params = {
            pageSize:10,
            nikename:nikename,
            name:name,
            phoneNum:phoneNum,
            sex:sex,
            starSubscribeTime:starTime,
            endSubscribeTime:endTime,
            area:reval,
            isSubscribe:isSubscribe,
            userId:userId,
            curPage:curPage
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items,
                    levels:levels,
                    groups:groups,
                    userSession:userSession
                };
                if(res.data.items.length==0){
                    $(".f-mt-m").hide();
                }else{
                    $(".f-mt-m").show();
                }

                $("#userCount").text(res.data.count);
                jumi.template('new_customer/customer_modify_list',data,function(tpl){
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
                                var url = CONTEXT_PATH+"/wx/group";
                                var group = {
                                    name:groupName,
                                    groupid:groupid
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




