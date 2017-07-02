//佣金设置//支付设置
CommonUtils.regNamespace("shopSetting");
$(function () {
    //$("#test1").addclassify();


    setRadio(true);	//不可用
    var switch1 = new switchControl("#switch1", {
        onSwitchChange: function (v) {
            if (v) {
                $("#switchpanel").show();
            } else {
                $("#switchpanel").hide();
            }
        }
    });
    switch1.render();
//     switch1.dispose();
});


function checkRadio(rid) {

    if (rid == 'radioBox4') {
        setRadio(true);
    } else if (rid == 'radioBox3' || rid == 'radioBox5') {
        setRadio(false);
    } else if (rid == 'radioBox1' || rid == 'radioBox2') {
        $("#radioBox4").attr("checked", true);
        //setRadio(false);
    } else {
        alert('异常');
    }

}


//设置单选框是否可用
//flag=true（可用），flag=false（不可用）
function setRadio(flag) {
    if (flag) {
        $("#radioBox1").attr("disabled", false);
        $("#radioBox2").attr("disabled", false);
        //document.getElementById("radioBox1").disabled=false;	//可用
        //document.getElementById("radioBox2").disabled=false;	//可用
    } else {
        $("#radioBox1").attr("disabled", true);
        $("#radioBox2").attr("disabled", true);
        $("#radioBox1").attr("checked", false);
        $("#radioBox2").attr("checked", false);
//  		document.getElementById("radioBox1").disabled=true;	//不可用
//  		document.getElementById("radioBox2").disabled=true;	//不可用
//  		document.getElementById("radioBox1").checked=false;	//取消选中
//  		document.getElementById("radioBox2").checked=false;	//取消选中

    }
}

shopSetting.shopSetting = function () {
    var shopSetId = $("#shopSetId").val();
    var url = "";
    var isOpen = "";

    if ($('#switch1').is(':checked')) {
        // do something
        isOpen = 0;
    } else {
        isOpen = 1;
    }
    var total = Number($("#brokerageOne").val()) + Number($("#brokerageTwo").val()) + Number($("#brokerageThree").val());
    if (total > 100) {
        alert("佣金比例不能超过100%");
        return;
    }
    if (null == shopSetId || "" == shopSetId) {//新增
        var shopSettingForCreateVo = {
            shopSetId: $("#shopSetId").val(),
            shopId: $("#shopId").val(),
            isOpen: isOpen,
            brokerageOne: $("#brokerageOne").val(),
            brokerageTwo: $("#brokerageTwo").val(),
            brokerageThree: $("#brokerageThree").val(),
            payType: $("#payType").val(),
            timeSetting: $("#timeSetting").val(),
            delayDate: $("#delayDate").val(),
            nextMonth: $("#nextMonth").val()
        };
        url = CONTEXT_PATH + "/shopsetting/add_manager";
        var jsonStr = JSON.stringify(shopSettingForCreateVo);
        shopSetting.ajaxJson(url, jsonStr);
    } else {//修改
        var shopSettingForUpdateVo = {
            shopSetId: $("#shopSetId").val(),
            shopId: $("#shopId").val(),
            isOpen: isOpen,
            brokerageOne: $("#brokerageOne").val(),
            brokerageTwo: $("#brokerageTwo").val(),
            brokerageThree: $("#brokerageThree").val(),
            payType: $("#payType").val(),
            timeSetting: $("#timeSetting").val(),
            delayDate: $("#delayDate").val(),
            nextMonth: $("#nextMonth").val()
        };
        url = CONTEXT_PATH + "/shopsetting/update_manager";
        var jsonStr = JSON.stringify(shopSettingForUpdateVo);
        shopSetting.ajaxJson(url, jsonStr);
    }
};


shopSetting.paySetting = function () {
    var shopSetId = $("#shopSetId").val();
    var url = "";
    var payType = $('input:radio[name="payType"]:checked').val();
    var timeSetting = $('input:radio[name="timeSetting"]:checked').val();
    var delayDate = $("#delayDate").val();
    //var delayDate =$("#delayDate").find("option:selected").text();
    var nextMonth = $("#nextMonth").val();
    if (null == payType || "" == payType) {
        alert("请选择一种发放类型");
        return;
    } else if (payType == "0" || payType == "2") {
        timeSetting = "";
        delayDate = "";
        nextMonth = "";
    } else {
        if (payType == "1") {
            if ("" == timeSetting || null == timeSetting) {
                alert("请选择延时发放类型");
                return;
            } else {
                if ("0" == timeSetting) {
                    if (delayDate == "-1") {
                        alert("请选择延迟日次");
                        return;
                    } else {
                        nextMonth = "";
                    }
                } else {
                    if (null == nextMonth || "" == nextMonth) {
                        alert("请选择次月时间");
                        return;
                    }
                    delayDate = "";
                }
            }
        }
    }

    if (null == shopSetId || "" == shopSetId) {//新增
        var shopSettingForCreateVo = {
            shopSetId: $("#shopSetId").val(),
            shopId: $("#shopId").val(),
            isOpen: $("#isOpen").val(),
            brokerageOne: $("#brokerageOne").val(),
            brokerageTwo: $("#brokerageTwo").val(),
            brokerageThree: $("#brokerageThree").val(),
            payType: payType,
            timeSetting: timeSetting,
            delayDate: delayDate,
            nextMonth: nextMonth
        };
        console.log(nextMonth);
        url = CONTEXT_PATH + "/shopsetting/add_pay_manager";
        var jsonStr = JSON.stringify(shopSettingForCreateVo);
        shopSetting.ajaxJson(url, jsonStr);
    } else {//修改
        var shopSettingForUpdateVo = {
            shopSetId: $("#shopSetId").val(),
            shopId: $("#shopId").val(),
            isOpen: $("#isOpen").val(),
            brokerageOne: $("#brokerageOne").val(),
            brokerageTwo: $("#brokerageTwo").val(),
            brokerageThree: $("#brokerageThree").val(),
            payType: payType,
            timeSetting: timeSetting,
            delayDate: delayDate,
            nextMonth: nextMonth
        };
        url = CONTEXT_PATH + "/shopsetting/update_pay_manager";
        var jsonStr = JSON.stringify(shopSettingForUpdateVo);
        shopSetting.ajaxJson(url, jsonStr);
    }
};

shopSetting.ajaxJson = function (url, jsonStr) {
    $.ajaxHtmlPut(url, jsonStr, {
        done: function (res) {
            if (res.code == 0) {
                var dm = new dialogMessage({
                    type: 1,
                    fixed: true,
                    msg: '操作成功',
                    isAutoDisplay: true,
                    time: 3000
                });
                dm.render();
                $("#shop_content").empty();
                $("#shop_content").append(res.data);
            }
        }
    });
};
