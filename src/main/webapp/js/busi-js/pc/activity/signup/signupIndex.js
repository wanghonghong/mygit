/**
 * Created by zx on 2017/5/8.
 */
CommonUtils.regNamespace("signup", "index");
signup.index = (function () {
    var tabindex = 0;
    var opt = {
        enrolmentConfAjax: CONTEXT_PATH + "/enrolmentActivity/conf/",
        enrolmentUserAjax: CONTEXT_PATH + "/enrolmentActivity/user/",
        enrolmentActivityAjax: CONTEXT_PATH + "/enrolmentActivity",
        choiseWindowTpl: "/tpl/activity/signup/choiceWindow.html",
         usersWindowTpl: "/tpl/activity/signup/usersView.html",
    }
    var pageparam = [{
        url: CONTEXT_PATH + "/enrolmentActivity/conf/list",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar1",
        tableBodyObj: "tableBody1",
        searchFrom: "searchform1",
        fristSearch: false,
        template: "/tpl/activity/signup/setInfoItem.html"
    }, {
        url: CONTEXT_PATH + "/enrolmentActivity/list",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar2",
        tableBodyObj: "tableBody2",
        searchFrom: "searchform2",
        fristSearch: true,
        template: "/tpl/activity/signup/activityItem.html"
    }, {
        url: CONTEXT_PATH + "/enrolmentActivity/conf/list",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar3",
        tableBodyObj: "tableBody3",
        searchFrom: "searchform3",
        template: "/tpl/activity/signup/choiceItem.html"
    }, {
        url: CONTEXT_PATH + "/enrolmentActivity//user/list/",//对象列表
        pageSize: 10,
        curPage: 0,
        pageToolbarObj: "pageToolbar4",
        tableBodyObj: "tableBody4",
        searchFrom: "searchform4",
        template: "/tpl/activity/signup/userItem.html"
    }];
    var _init = function () {
        tabindex = 0;
        pageparam[1].fristSearch = true;
        _bindEvent();
        _querySetInfoList();
    }
    var _enrolmentConfAdd = function () {
        var data = {
            setInfo: "1,2,3"
        }
        signup.conf.init(data);
    }
    var _enrolmentConfEdit = function (el) {
        var data = $(el).data();
        // var url=opt.enrolmentConfAjax+data.id;
        signup.conf.init(data);
    }

    var _enrolmentConfDel = function (el) {
        var data = $(el).data();
        var url = opt.enrolmentConfAjax + data.id;
        var args = {};
        args.fn1 = function () {
            $.ajaxJsonDel(url, null, {
                "done": function (res) {
                    if (res.code == 0) {
                        if (res.data.code == 0) {
                            var dm = new dialogMessage({
                                type: 2,
                                fixed: true,
                                msg: '删除该配置成功',
                                isAutoDisplay: true,
                                time: 3000
                            });
                            dm.render();
                            vote.index.queryThemeList();
                        } else {
                            var dm = new dialogMessage({
                                type: 2,
                                fixed: true,
                                msg: res.data.msg,
                                isAutoDisplay: true,
                                time: 3000
                            });
                            dm.render();
                        }
                        _querySetInfoList();
                    }
                }
            })
        };
        args.fn2 = function () {
        };
        jumi.dialogSure('确定删除该配置吗?', args);
    }
    var _querySetInfoList = function () {
        _search(pageparam[0], function () {
            var signUpJson = signup.data.getdata();
            $("#tableBody1 .m-panel .panel-body").each(function (i) {
                var setinfo = $(this).data().setinfo;
                var setinfoArr = [];
                if (setinfo) {
                    setinfoArr = setinfo.split(",");
                }
                for (var i = 0; i < setinfoArr.length; i++) {
                    for (var j = 0; j < signUpJson.length; j++) {
                        if (signUpJson[j].id == setinfoArr[i]) {
                            // data.attrList.push(opt.signUpJson[j]);
                            $(this).append("<div class='u-sort'>" + signUpJson[j].name + "</div>");
                            break;
                        }
                    }
                }
            })
        });
    }
    var _queryActivityList = function (sign) {
        if (sign) {
            pageparam[1].curPage = 0;
        }
        _search(pageparam[1], function () {
        });

    }

    var _search = function (pageparam, fun) {
        var data = {
            pageSize: pageparam.pageSize,
            curPage: pageparam.curPage
        }
        if (pageparam.searchFrom) {
            $("input[type='text'],input[type='hidden'],select", "#" + pageparam.searchFrom).each(function () {
                if ($(this).attr("name")) {
                    if ($(this).val()) {
                        data[$(this).attr("name")] = $(this).val();
                    }
                }
            })
        }
        // var data = JSON.stringify(data);
        jumi.pagination("#" + pageparam.pageToolbarObj, pageparam.url, data, function (res, curPage) {
            pageparam.curPage = curPage;
            $("#" + pageparam.tableBodyObj).empty();
            var itemhtml = jumi.templateHtml(pageparam.template, res.data);
            $(itemhtml).appendTo("#" + pageparam.tableBodyObj);
            fun();
        })
    }
    var _bindEvent = function () {
        $("#signup .m-tab li").click(function () {
            var _this = $(this);
            _this.addClass("z-sel").siblings().removeClass("z-sel");
            var target = _this.attr("data-target");
            var index = _this.index();
            tabindex = index;
            if (pageparam[index].fristSearch) {
                pageparam[index].fristSearch = false;
                _search(pageparam[index], function () {
                });
            }
            $("#" + target).removeClass("z-hide").siblings(".panel-hidden").addClass("z-hide");
        })
        $("#searchform2 [name$='Date']").datetimepicker({timeFormat: 'hh:mm:ss'});

        $("#searchform2 select").select2({
            theme: "jumi"
        });

    }

    var _choiceWindow = function (callback) {
        pageparam[2].curPage = 0;
        initChoiceWindow(callback);
        _search(pageparam[2], function () {
            var signUpJson = signup.data.getdata();
            $("#tableBody3 .m-panel .panel-body").each(function (i) {
                var setinfo = $(this).data().setinfo;
                var setinfoArr = [];
                if (setinfo) {
                    setinfoArr = setinfo.split(",");
                }
                for (var i = 0; i < setinfoArr.length; i++) {
                    for (var j = 0; j < signUpJson.length; j++) {
                        if (signUpJson[j].id == setinfoArr[i]) {
                            // data.attrList.push(opt.signUpJson[j]);
                            $(this).append("<div class='u-sort'>" + signUpJson[j].name + "</div>");
                            break;
                        }
                    }
                }
            })
        });
    }
    var initChoiceWindow = function (callback) {

        var html = jumi.templateHtml(opt.choiseWindowTpl, null);
        dialog({
            title: "报名板式选择",
            content: html,
            okValue: '确定',
            ok: function () {
                var data=$("#tableBody3 .m-panel .u-rb :radio:checked").data();
                callback(data);
                // callback( );
            },
            cancelValue: '取消',
            cancel: function () {
            }
        }).width(900).showModal();
    }
    var _usersView=function (el) {
        var data=$(el).data();
        _initUsersWindow(data);
        _queryUsers();
    }
    var _queryUsers=function () {
            pageparam[3].curPage = 0;
        _search(pageparam[3], function () {
        });
    }

    var _initUsersWindow=function (data) {
        var html = jumi.templateHtml(opt.usersWindowTpl, data);
        dialog({
            title: "报名明显",
            content: html,

        }).width(900).showModal();

    }




    return {
        init: _init,
        enrolmentConfEdit: _enrolmentConfEdit,
        enrolmentConfAdd: _enrolmentConfAdd,
        enrolmentConfDel: _enrolmentConfDel,
        querySetInfoList: _querySetInfoList,
        queryActivityList: _queryActivityList,
        choiceWindow: _choiceWindow,
        usersView:_usersView,
        queryUsers:_queryUsers
    }


})();
