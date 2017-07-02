/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("server", "review");
server.review = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/join/list',
        url2:CONTEXT_PATH+'/zb/join',
        url3:CONTEXT_PATH+'/zb/check',
        url4:CONTEXT_PATH+'/zb/delete_join',

    };
    var _init = function(){
        _tab();
        _bind();
    };

    //时间空间渲染函数
    var _timeTimepicker = function () {
        var dateConfig = {
            showSecond : true,
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd',
            stepHour : 1,
            stepMinute : 1,
            stepSecond : 1,
        };
        $("#startTime").datepicker(dateConfig);
        $("#endTime").datepicker(dateConfig);
    };

    var _tab = function () {
        $(".m-jm-table").hide().eq(0).show();
        var tabul = $("#m-tab ul li");
        tabul.click(function(){
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".m-jm-table").hide().eq($(this).index()).show();
        });
    }

    var _bind = function(){
        $("#applyRole").select2({
            theme: "jumi"
        });
        $("#property").select2({
            theme: "jumi"
        });
        _timeTimepicker();
        var url = ajaxUrl.url1;
        var params = {
            pageSize:10,
            status:1,
            type:2,
        };
        jumi.pagination('#serviceToolbar1',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('zb/review/service_review_list',data,function(tpl){
                    $('#serviceReviewList1').html(tpl);
                })
            }
        })

        $('#m-tab').find('li').click(function(){
            var index = $(this).attr('data-index');
            var url = ajaxUrl.url1;
            if(index==='1'){
                var params = {
                    pageSize:10,
                    status :1,
                    type:2,
                };
                jumi.pagination('#serviceToolbar1',url,params,function(res,curPage){
                    if(res.code===0){
                        var data = {
                            items:res.data.items,
                            index:1
                        };
                        jumi.template('zb/review/service_review_list',data,function(tpl){
                            $('#serviceReviewList1').html(tpl);
                        })
                    }
                })
            }
            if(index==='2'){
                var params = {
                    pageSize:10,
                    status :2,
                    type:2,
                };
                jumi.pagination('#serviceToolbar2',url,params,function(res,curPage){
                    if(res.code===0){
                        var data = {
                            items:res.data.items,
                            index:2
                        };
                        jumi.template('zb/review/service_review_list',data,function(tpl){
                            $('#serviceReviewList2').html(tpl);
                        })
                    }
                })
            }
            if(index==='3'){
                var params = {
                    pageSize:10,
                    status :3,
                    type:2,
                };
                jumi.pagination('#serviceToolbar3',url,params,function(res,curPage){
                    if(res.code===0){
                        var data = {
                            items:res.data.items,
                            index:3
                        };
                        jumi.template('zb/review/service_review_list',data,function(tpl){
                            $('#serviceReviewList3').html(tpl);
                        })
                    }
                })
            }
        });

        $('#query').click( function(){
            var index = $('#m-tab').find('li.z-sel').attr('data-index');
            var userName = $("#userName").val();
            var phoneNumber = $("#phoneNumber").val();
            var companyName = $("#companyName").val();
            var property = $('#property').find('option:selected').val();
            var type = 2;
            var subType;
            if(property==1){
                subType=1;
            }else if(property==2){
                subType=2;
            }else if(property==''){
                subType="";
            }else {
                subType=3;
            }
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var applyRole = $("#applyRole").find('option:selected').val();
            var params = {
                pageSize:10,
                status:index,
                userName : userName,
                phoneNumber : phoneNumber,
                companyName : companyName,
                type : type,
                subType : subType,
                startTime : startTime,
                endTime : endTime,
                applyRole : applyRole
            };
            if(index==='1'){
                var url = ajaxUrl.url1;
                jumi.pagination('#serviceToolbar1',url,params,function(res,curPage){
                    if(res.code===0){
                        var data = {
                            items:res.data.items,
                            index:1
                        };
                        jumi.template('zb/review/service_review_list',data,function(tpl){
                            $('#serviceReviewList1').html(tpl);
                        })
                    }
                });
            }
            if(index==='2'){
                var url = ajaxUrl.url1;
                jumi.pagination('#serviceToolbar2',url,params,function(res,curPage){
                    if(res.code===0){
                        var data = {
                            items:res.data.items,
                            index:2
                        };
                        jumi.template('zb/review/service_review_list',data,function(tpl){
                            $('#serviceReviewList2').html(tpl);
                        })
                    }
                });
            }
            if(index==='3'){
                var url = ajaxUrl.url1;
                jumi.pagination('#serviceToolbar3',url,params,function(res,curPage){
                    if(res.code===0){
                        var data = {
                            items:res.data.items,
                            index:3
                        };
                        jumi.template('zb/review/service_review_list',data,function(tpl){
                            $('#serviceReviewList3').html(tpl);
                        })
                    }
                });
            }
        });

        $('[id^=serviceReviewList]').on('click','#delete_btn',function(){
            var args = {};
            var userId = $(this).attr('data-type-id');
            var joinVo = {};
            joinVo.userId = userId;
            joinVo.type = 2;
            var data = JSON.stringify(joinVo);
            var url = ajaxUrl.url4;
            args.fn1 = function(){
                $.ajaxJson(url,data,{
                    "done":function (res) {
                        if(res.code===0){
                            $("#m-tab li").eq(2).trigger('click');
                        }
                    }
                })
            };
            args.fn2 = function(){
            };
            jumi.dialogSure('是否确认删除?',args);
        });

        $('[id^=serviceReviewList]').on('click','div[id^="reviewbutton_"]',function(){
            var self = $(this);
            var userId = self.attr('data-type-id');
            var url = ajaxUrl.url2;
            var joinVo = {};
            joinVo.userId = userId;
            joinVo.type = 2;
            var data = JSON.stringify(joinVo);
            $.ajaxJson(url,data,{
                "done":function (res) {
                    var data = {
                        item:res.data,
                        checkList : res.data.checkList
                    };
                    jumi.template('zb/review/service_review_dialog',data,function (tpl) {
                        var d = dialog({
                            title: '审核操作',
                            content:tpl,
                            width:900,
                            height:560,
                            onshow:function () {
                                $('#close-btn').click(function(){
                                    d.close().remove();
                                });
                                $('#agree_btn').click(function () {
                                    var checkUo = {};
                                    checkUo.checkContext = $('#reviewAdvice').val();
                                    checkUo.status = 3;
                                    checkUo.type = 2;
                                    checkUo.userId = userId;
                                    var url = ajaxUrl.url3;
                                    var jsonData = JSON.stringify(checkUo);
                                    $.ajaxJson(url,jsonData,{
                                        "done":function (res) {
                                            if(res.code===0){
                                                var dm = new dialogMessage({
                                                    type:1,
                                                    fixed:true,
                                                    msg:"审核成功",
                                                    isAutoDisplay:true,
                                                    time:1500,
                                                });
                                                $("#m-tab li").eq(0).trigger('click');
                                                dm.render();
                                                d.close().remove();
                                            }
                                        }
                                    });
                                });

                                $('#refuse_btn').click(function () {
                                    var checkUo = {};
                                    checkUo.checkContext = $('#reviewAdvice').val();
                                    checkUo.status = 2;
                                    checkUo.type = 2;
                                    checkUo.userId = userId;
                                    var url = ajaxUrl.url3;
                                    var jsonData = JSON.stringify(checkUo);
                                    $.ajaxJson(url,jsonData,{
                                        "done":function (res) {
                                            if(res.code===0){
                                                var dm = new dialogMessage({
                                                    type:1,
                                                    fixed:true,
                                                    isAutoDisplay:true,
                                                    time:1500,
                                                });
                                                $("#m-tab li").eq(0).trigger('click');
                                                dm.render();
                                                d.close().remove();
                                            }
                                        }
                                    });
                                });
                            },
                            onremove: function () {
                                jumi.msg('对话框已销毁');
                            }
                        });
                        d.showModal();
                        //企业介绍全文/收起切换
                        $('.company-more').on('click',function(){
                            if($('.company-more').html() == "全文"){
                                $('.company-text').css('height','auto');
                                $('.company-more').html('收起');
                            }
                            else{
                                $('.company-text').css('height','34px');
                                $('.company-more').html('全文');
                            }
                        });
                        $(".panel-hidden").hide().eq(0).show();
                        var tabul1 = $("#m-tab1 ul li");
                        tabul1.click(function(){
                            tabul1.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
                            $(".panel-hidden").hide().eq($(this).index()).show();
                        });
                    })
                }
            });
        });


    }
    return {
        init :_init
    };
})();

