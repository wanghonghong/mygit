/**
 * Created by zx on 2017/5/10.
 */
CommonUtils.regNamespace("signup","conf");
signup.conf=(function () {
    var opt={
        enrolmentConfAjax:CONTEXT_PATH+"/enrolmentActivity/conf/",
        tplUrl:STATIC_URL+ "/tpl/activity/signup/",
        enrolmentConf:{}
    }

    var _init=function (data) {
        opt.signUpJson=signup.data.getdata();
         data.setInfo= data.setInfo||"1,2,3";
        correctData(data);
        openInfoConfWindow(data);
        drawSignUpFormBox(data);
        _bindEvent();
        _inifAttribute(data);
        opt.enrolmentConf=data;
    }

    var correctData=function (data) {
        var setInfoArr=data.setInfo.split(",");
        data.attrList=[];
        for (var i=0;i<setInfoArr.length;i++){
            for(var j=0;j<opt.signUpJson.length;j++){
                if(opt.signUpJson[j].id==setInfoArr[i]){
                    data.attrList.push(opt.signUpJson[j]);
                    break;
                }
            }
        }
    }
      var _bindEvent=function () {
          $("#enlist").delegate("li .icon-delete1","click",function(){
              _removeSignUpAttribute($(this).attr("item-id"));
              changeSetInfo();
          });
          $("#enblock").delegate("div span","click",function(){
              if($(this).hasClass("z-sel")){
                  _removeSignUpAttribute($(this).attr("item-id"));
              }else{
                  _addSignUpAttribute($(this).attr("item-id"));
              }
              changeSetInfo();
          });
          $("#infoconfwindow input[name='titleName']").change(function () {
                opt.enrolmentConf.titleName=$(this).val();
          })

        $("#confSaveBtn").click(function () {
            _dosave();
        })
      }
    var alertinfo = function (msg) {
        var dm = new dialogMessage({
            type:3,
            msg:msg,
            isAutoDisplay:true,
            time:1500
        });
        dm.render();
    }
      var _verifyfun=function () {
          if($.isEmptyObject(opt.enrolmentConf.titleName)){
              alertinfo("请输入板式名称");
              return true;
          }
          if($.isEmptyObject(opt.enrolmentConf.setInfo)){
              alertinfo("请至少选择一个板式")
              return true;
          }
          var setInfoArr=opt.enrolmentConf.setInfo.split(",");
          if(_.indexOf(setInfoArr,  "1")==-1){
              alertinfo("姓名为必选的报名项")
              return true;
          }
          if(_.indexOf(setInfoArr,  "2")==-1){
              alertinfo("电话号码为必选的报名项")
              return true;
          }
            return false;
      }

      var _dosave=function () {
          if(_verifyfun()){
              return ;
          }
          var jsonData = JSON.stringify(opt.enrolmentConf);
          if(opt.enrolmentConf.id){
              $.ajaxJsonPut(opt.enrolmentConfAjax, jsonData, {
                  "done": function (res) {
                      var dm = new dialogMessage({
                          type: 1,
                          fixed: true,
                          msg: res.data.msg,
                          isAutoDisplay: true,
                          time: 3000
                      });
                      dm.render();
                      signup.index.querySetInfoList();
                      opt.signdialog.close().remove();

                  },
                  "fail": function (res) {
                  }
              });
          }else{
              $.ajaxJson(opt.enrolmentConfAjax, jsonData, {
                  "done": function (res) {
                      var dm = new dialogMessage({
                          type: 1,
                          fixed: true,
                          msg: res.data.msg,
                          isAutoDisplay: true,
                          time: 3000
                      });
                      dm.render();
                      signup.index.querySetInfoList();
                      opt.signdialog.close().remove();
                  },
                  "fail": function (res) {
                  }
              });
          }
      }

      var changeSetInfo=function () {
          // opt.enrolmentConf.setInfo="1,2,3";
          var arr=[];
          $("#enlist li").each(function () {
              arr.push($(this).attr("item-id"));
          })
          opt.enrolmentConf.setInfo=arr.join(",")
          correctData(opt.enrolmentConf);
          drawSignUpFormBox(opt.enrolmentConf);
      }

    var _inifAttribute=function (data) {
        var tmparr=data.setInfo.split(",");
        for(var i=0;i<tmparr.length;i++){
            _addSignUpAttribute(tmparr[i]);
        }
    }
      var  _addSignUpAttribute=function (itemid) {
          var tmpdata={};
          for(var j=0;j<opt.signUpJson.length;j++){
              if(opt.signUpJson[j].id==itemid){
                  // data.attrList.push(opt.signUpJson[j]);
                  tmpdata=opt.signUpJson[j];
                  break;
              }
          }
          console.log(tmpdata);
          var tmphtml='模块'+($("#enlist li").length+1)+'：<span>'+tmpdata.name+'</span><i item-id="'+tmpdata.id+'"class="iconfont icon-delete1"></i>';
          $("<li>",{
              "item-id":tmpdata.id
          }).data(tmpdata).html(tmphtml).appendTo("#enlist");
          $("#enblock div  span[item-id='"+tmpdata.id+"']").addClass("z-sel");
      }
      var  _removeSignUpAttribute=function (itemid) {
          $("#enlist li[item-id='"+itemid+"']").remove();
          $("#enblock div  span[item-id='"+itemid+"']").removeClass("z-sel");
      }

    var drawSignUpFormBox=function (data) {
        $("#signUpFormBox").empty();
        var itemhtml = jumi.templateHtml("signUpForm.html",data,opt.tplUrl);
        $("#signUpFormBox").append(itemhtml);
    }

    var openInfoConfWindow=function (data) {
        //tpl/activity/signup/infoConfWindow.html
        var itemhtml = jumi.templateHtml("infoConfWindow.html",data,opt.tplUrl);
      opt.signdialog=  dialog({
            title: "信息创建",
            content: itemhtml,
        }).width(900).showModal();
    }
    



    return{
        init:_init,

    }
})();