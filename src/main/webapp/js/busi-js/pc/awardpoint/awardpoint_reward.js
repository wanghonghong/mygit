/**
 * Created by ray on 16/11/17.
 */
CommonUtils.regNamespace("awardpoint", "reward");
awardpoint.reward = (function() {
    var ajaxUrl = {
        url1:CONTEXT_PATH+'/integral/award'
    };
    var _init = function(){
        _bind();
    }
    var _bind = function(){
        $('#awardpoint_search').on('click',function(){
            _queryRewardList();
        })
        $('#awardpoint_search').trigger('click');
        //tab切换页
        $('#awardpoint_business_list li').click(function () {
            var index = $(this).data('index');
            $(this).addClass('z-sel').siblings().removeClass('z-sel');
            _queryPage(index);//加载不同的选项卡模版
        });
    }

    var _loadReward = function(){
        jumi.template('awardpoint/awardpoint_reward',function(html){
            _queryRewardList();
        })
    }
    var _cashWithdrawal = function(){
        jumi.template('awardpoint/awardpoint_cash',function(html){
            _queryCashList();
        })
    }

    var _loadRecharge = function(){
        jumi.template('awardpoint/awardpoint_recharge',function(html){
            _rechargeList();
        })
    }

    var _rechargeList = function(){
        jumi.template('awardpoint/awardpoint_recharge_list',function(html){
            $('#recharge_list').html(html);
        })
    }
    var _loadRebate = function(){
        jumi.template('awardpoint/awardpoint_rebate',function(html){
            $('#awardpoint_content').html(html);
        })
        _rebateList();
    }

    var _rebateList = function () {
        jumi.template('awardpoint/awardpoint_rebate_list',function(html){
            $('#rebate_list').html(html);
        })
    }
    var _queryPage = function(index){
        switch(index)
        {
            case 1:
                _loadReward();//奖励清单
                break;
            case 2:
                _cashWithdrawal();//提现清单
                break;
            case 3:
                _loadRecharge();//充值清单
                break;
            case 4:
                _loadRebate();//返利清单
                break;
        }

    }
    //充值清单列表
    var _queryCashList = function(){
        var data = {};
        jumi.template('awardpoint/awardpoint_cash_list',data,function(tpl){
            $('#cash_list').html(tpl);
        })
    }
    //奖励清单列表
    var _queryRewardList = function(){
        var project = $('#awardpoint_project').val();
        var integralRecordQo = {};
        integralRecordQo.pageSize = 10;
        integralRecordQo.phoneNumber = $('input[name="phoneNumber"]').val();
        integralRecordQo.nickname = $('input[name="award_nic"]').val();
        integralRecordQo.userName = $('input[name="award_username"]').val();
        integralRecordQo.beginTime = $('#startTime').val();
        integralRecordQo.endTime = $('#overTime').val();
        if(project){
            integralRecordQo.integralType = project;
        }
        jumi.pagination('#pageAwardToolbar',ajaxUrl.url1,integralRecordQo,function(res,curPage){
            if(res.code===0){
                //判断是否第一页
                var data = {
                    items:res.data.items
                };
                jumi.template('awardpoint/awardpoint_reward_list',data,function(tpl){
                    $('#reward_list').html(tpl);
                })
            }
        })
    };
    //点击伸缩
    var _clickSlideToggle = function(){
        $(".btn-slide").click(function(){
            $("#m-search1").slideToggle("fast");
            $(this).toggleClass("btn-slide1"); return false;
        });
    };
    var _initSelect = function () {
        jumi.Select('#awardpoint_project');
        jumi.Select('#awardpoint_platform');
        jumi.Select('#awardpoint_customer');
    };
    var _timeTimepicker = function () {
        $("#startTime").datetimepicker({
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd'
        });
        $("#overTime").datetimepicker({
            timeFormat : 'hh:mm:ss',
            dateFormat : 'yy-mm-dd'
        });
    };
    //奖励清单
    return {
        init: _init
    };
})();