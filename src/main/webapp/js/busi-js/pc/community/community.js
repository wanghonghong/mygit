/**
 * Created by whh on 2016/12/15.
 */
CommonUtils.regNamespace("community", "index");
community.index = (function() {
    var ajaxUrl = {
        url1: CONTEXT_PATH + '/community/post',
        url2: CONTEXT_PATH + '/community/posts/all',
        url3: CONTEXT_PATH + '/community/reply',

    };
    var _init = function () {
        _bind();
    };

    var _bind = function(){
        _allPost();
    }

    var _allPost = function () {
        var target = $(event.target );
        target.addClass('active').parent().siblings().find('a').removeClass('active');
        var url = ajaxUrl.url2;
        var params = {
            pageSize:10,
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('community/index_list',data,function(tpl){
                    $('#postList').html(tpl);
                })
            }
        })
    }
    var _hotPost = function () {
        var target = $(event.target );
        target.addClass('active').parent().siblings().find('a').removeClass('active');
        var url = ajaxUrl.url2;
        var params = {
            pageSize:10,
            feature:2
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('community/index_list',data,function(tpl){
                    $('#postList').html(tpl);
                })
            }
        })
    }
    var _jingPost = function () {
        var target = $(event.target );
        target.addClass('active').parent().siblings().find('a').removeClass('active');
        var url = ajaxUrl.url2;
        var params = {
            pageSize:10,
            feature:3,
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('community/index_list',data,function(tpl){
                    $('#postList').html(tpl);
                })
            }
        })
    }

    var _searchPost = function () {
        var title = $('#searchPost').val();
        var url = ajaxUrl.url2;
        var params = {
            pageSize:10,
            title:title,
        };
        jumi.pagination('#pageToolbar',url,params,function(res,curPage){
            if(res.code===0){
                var data = {
                    items:res.data.items
                };
                jumi.template('community/index_list',data,function(tpl){
                    $('#postList').html(tpl);
                })
            }
        })
    }
    var _data = function(){
        jumi.template('community/data',function (tpl) {
            $('#shopIndexContent').empty();
            $('#shopIndexContent').html(tpl);
        })
    }

    var _post = function(){
        var url = ajaxUrl.url1;
        $.ajaxJsonGet(url, null, {
            done: function (res) {
                if (res.code === 0) {
                    var data = {
                        posts:res.data
                    };
                    jumi.template('community/post',data,function (tpl) {
                        $('#shopIndexContent').empty();
                        $('#shopIndexContent').html(tpl);
                    })
                }
            }
        })
    }

    var _showPost = function (id) {
        var readZanCo = {};
        readZanCo.postId = Number(id);
        readZanCo.type = 1;
        var data = JSON.stringify(readZanCo);
        var url = ajaxUrl.url1+'/one';
        $.ajaxJson(url, data, {
            done: function (res) {
                if (res.code === 0) {
                    var data = {
                        item:res.data
                    };
                    jumi.template('community/context',data,function (tpl) {
                        $('#shopIndexContent').empty();
                        $('#shopIndexContent').html(tpl);
                    })
                }
            }
        })
    }

    return {
        init :_init,
        data :_data,
        post:_post,
        hotPost:_hotPost,
        jingPost:_jingPost,
        allPost:_allPost,
        searchPost:_searchPost,
        showPost:_showPost,

    };
})();