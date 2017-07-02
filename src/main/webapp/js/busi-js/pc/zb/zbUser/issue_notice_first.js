/**
 * Created by BenRay on 16/8/8.
 * 官方图文
 */
CommonUtils.regNamespace("notice", "first");
notice.first = (function(){
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/zb/notice',
        url2:CONTEXT_PATH+'/zb/notice/list',
        url3:CONTEXT_PATH+'/zb/department/list',
        url4:CONTEXT_PATH+'/zb/notice_config',
        url5:CONTEXT_PATH+'/zb/notice_right',
    };
    var _init = function(){
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
        $("#startTime2").datepicker(dateConfig);
        $("#endTime2").datepicker(dateConfig);
    };

    var _bind = function(){

        _timeTimepicker();
        $("#type").select2({
            theme: "jumi"
        });
        $("#type2").select2({
            theme: "jumi"
        });
        clickMenus();
        getNoticePage();
        searchClick();
        //已发布点击消息标题查看消息
        $('#issueNoticeList').on('click','li[id^="notice_"]',function() {
                var id = $(this).data('id');
                var url = ajaxUrl.url1+"/"+id;
                $.ajaxJsonGet(url,null, {
                    "done": function (res) {
                        var data = {
                            item: res.data,
                        };
                        jumi.template('zb/user/notice_dialog',data,function (tpl) {
                            var d = dialog({
                                title: '系统公告',
                                content: tpl,
                                width: 830,
                                onshow: function () {

                                }
                            }).showModal();
                        })
                    }
                })
            }
        )
        //草稿箱点击消息标题查看消息
        $('#issueNotice2List').on('click','li[id^="notice_"]',function() {
                var id = $(this).data('id');
                var url = ajaxUrl.url1+"/"+id;
                $.ajaxJsonGet(url,null, {
                    "done": function (res) {
                        var data = {
                            item: res.data,
                        };
                        jumi.template('zb/user/notice_dialog',data,function (tpl) {
                            var d = dialog({
                                title: '系统公告',
                                content: tpl,
                                width: 830,
                                onshow: function () {

                                }
                            }).showModal();
                        })
                    }
                })
            }
        )
    }
    //点击切换草稿箱和已发布
    var clickMenus = function () {
        $(".panel-hidden").hide().eq(0).show();
        var tabul = $(".m-tab ul li");
        tabul.click(function(){
            tabul.eq($(this).index()).addClass("z-sel").siblings().removeClass('z-sel');
            $(".panel-hidden").hide().eq($(this).index()).show();
            getDraftPage();
        });
    }
    //已发布查看消息列表
    var getNoticePage = function () {
        var url = ajaxUrl.url2;
        var params = {
            pageSize:10,
            status:2,
            bigType:2,
            lookType:1,
        };
        jumi.pagination('#issueNoticeToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items,
                };
                jumi.template('zb/user/issue_notice_list',data,function(tpl){
                    $('#issueNoticeList').html(tpl);
                })
            }
        })
    }
    //已发布点击查询查看消息列表
    var searchClick = function () {
        $('#search-btn').click( function(){
            var type = $('#type').find('option:selected').val();
            if($("#startTime").val()){
                var startTime = $("#startTime").val()+" 00:00:00";
            }
            if ($("#endTime").val()){
                var endTime =  $("#endTime").val()+" 23:59:59";
            }
            var params = {
                pageSize:10,
                status:2,
                type:type,
                startTime : startTime,
                endTime : endTime,
                bigType : 2,
                lookType:1,
            };
            var url = ajaxUrl.url2;
            jumi.pagination('#issueNoticeToolbar',url,params,function(res,curPage){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    };
                    jumi.template('zb/user/issue_notice_list',data,function(tpl){
                        $('#issueNoticeList').html(tpl);
                    })
                }
            })
        });
    }
    //草稿箱查看消息列表
    var getDraftPage = function () {
        var url = ajaxUrl.url2;
        var params = {
            pageSize:10,
            status:1,
            bigType:3,
            lookType:1,
        };
        jumi.pagination('#issueNotice2Toolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('zb/user/issue_notice_list_second',data,function(tpl){
                    $('#issueNotice2List').html(tpl);
                })
            }
        })
    }
    //草稿箱点击查询查看消息列表
    var searchDraft = function () {
            var type = $('#type2').find('option:selected').val();
            if($("#startTime2").val()){
                var startTime = $("#startTime2").val()+" 00:00:00";
            }
            if ($("#endTime2").val()){
                var endTime =  $("#endTime2").val()+" 23:59:59";
            }
            var params = {
                pageSize:10,
                status:1,
                type:type,
                startTime : startTime,
                endTime : endTime,
                bigType : 3,
                lookType:1,
            };
            var url = ajaxUrl.url2;
            jumi.pagination('#issueNotice2Toolbar',url,params,function(res,curPage){
                if(res.code===0){
                    var data = {
                        items:res.data.items
                    };
                    jumi.template('zb/user/issue_notice_list_second',data,function(tpl){
                        $('#issueNotice2List').html(tpl);
                    })
                }
        });
    }
    //公告撤回
    var _updateNotice = function (id) {
        var args = {};
        var url = ajaxUrl.url1+'/'+id;
        args.fn1 = function(){
            $.ajaxJsonPut(url,null,{
                "done":function (res) {
                    if(res.code===0) {
                        $('.one-level-memu.z-sel').trigger('click');
                    }
                }
            })
        };
        args.fn2 = function(){
        };
        jumi.dialogSure('是否撤回该公告?',args);
    }
    //草稿箱点击删除
    var _deleteNotice = function (id) {
            var args = {};
            var url = ajaxUrl.url1+'/'+id;
            args.fn1 = function(){
                $.ajaxJsonDel(url,null,{
                    "done":function (res) {
                        if(res.code===0) {
                            $('.m-tab ul li.z-sel').trigger('click');
                        }
                    }
                })
            };
            args.fn2 = function(){
            };
            jumi.dialogSure('是否确认删除?',args);
    }
    //点击发布公告
    var _issueNotice = function () {
        var url = ajaxUrl.url1+"/"+0;
        $.ajaxJsonGet(url,null, {
            "done": function (res) {
                var data = {
                    zbDepartmentList:res.data.zbDepartmentList,
                };
                jumi.template('zb/user/issue_notice', data, function (html) {
                    $("#shop_content").html(html);
                })
            }
        })
    }
    //点击编辑公告
    var _editNotice = function (id) {
        var url = ajaxUrl.url1+"/"+id;
        $.ajaxJsonGet(url,null, {
            "done": function (res) {
                var zbDepartmentList = res.data.zbDepartmentList;
                if (res.data.departments){
                    var arr = res.data.departments.split(',');
                    $.each(zbDepartmentList,function () {
                        var departmentId = this.departmentId.toString();
                        departmentId = '['+departmentId+']';
                        if ($.inArray(departmentId,arr)!=-1){
                            this.isChecked = 1;
                        }else {
                            this.isChecked = 0;
                        }
                    })
                    if ($.inArray("[0]",arr)!=-1){
                        var isAll = 1;
                    }else {
                        var isAll = 0;
                    }
                }
                var data = {
                    isAll:isAll,
                    item: res.data,
                    zbDepartmentList:zbDepartmentList,
                };
                jumi.template('zb/user/issue_notice', data, function (html) {
                    $("#shop_content").html(html);
                })
            }
        })
    }
    //点击配置权限
    var _configNotice = function (id) {
        var url = ajaxUrl.url4+"/"+id;
        $.ajaxJsonGet(url,null,{
            "done":function (res) {
                var zbDepartmentList = res.data.zbDepartmentList;
                if (res.data.departments){
                    var arr = res.data.departments.split(',');
                    $.each(zbDepartmentList,function () {
                        var departmentId = this.departmentId.toString();
                        if ($.inArray(departmentId,arr)!=-1){
                            this.isChecked = 1;
                        }else {
                            this.isChecked = 0;
                        }
                    })
                    if ($.inArray("0",arr)!=-1){
                        var isAll = 1;
                    }else {
                        var isAll = 0;
                    }
                }
                var data = {
                    id:id,
                    isAll:isAll,
                    departments:arr,
                    zbDepartmentList:zbDepartmentList,
                };
                var html = jumi.templateHtml("/tpl/zb/user/notice_config_dialog.html",data);
                dialog({
                    content: html,
                    id:'notice_config_dialog',
                    title: '选择权限',
                }).width(260).showModal();
            }
        });
    }
    //选择权限点击确定
    var _sureDepartment = function (id) {
        var departments = [];
        $('input[name="selectpower"]:checked').each(function(){
            departments.push($(this).val());
        })
        var url = ajaxUrl.url4;
        var noticeConfigUo = {};
        noticeConfigUo.id = id;
        noticeConfigUo.departments = departments.toString();
        var data = JSON.stringify(noticeConfigUo);
        $.ajaxJsonPut(url,data,{
            "done":function (res) {
                if (res.code===0){
                    var dm = new dialogMessage({
                        type:1,
                        fixed:true,
                        msg:'保存成功',
                        isAutoDisplay:true,
                        time:1500
                    });
                    dm.render();
                    dialog.get("notice_config_dialog").close();
                }
            }
        })
    }
    //选择查看权限按钮
    var _searchDepartment = function (id) {
        var url = ajaxUrl.url5+"/"+id;
        $.ajaxJsonGet(url,null, {
            "done": function (res) {
                var data = {
                    zbDepartmentList:res.data,
                };
                var html = jumi.templateHtml("/tpl/zb/user/notice_search_dialog.html",data);
                dialog({
                    content: html,
                    id:'notice_search_dialog',
                    title: '查看权限',
                }).width(260).showModal();
            }
        })

    }

    return {
        init :_init,
        searchDraft:searchDraft,
        deleteNotice:_deleteNotice,
        updateNotice:_updateNotice,
        issueNotice:_issueNotice,
        editNotice:_editNotice,
        configNotice:_configNotice,
        sureDepartment:_sureDepartment,
        searchDepartment:_searchDepartment,
    };
})();

