CommonUtils.regNamespace("official", "index");

official.index = (function () {
    var opt = {
        id: 0,
        addTypeSign: false,
        ImgTexBoxListSign: true
    };

    var _init = function (optsions) {
        opt.typeId = optsions.typeId;
        opt.mainContainer = optsions.mainContainer;
        opt.ImgTexBoxListSign = true;
        _bandEvent();
        opt.addTypeSign = false;
        var typedata = {
            typeId: opt.typeId
        }
        official.list.init(typedata);
    };
    var _bandEvent = function () {

        $("#officialTab ul li").click(function () {
            $(this).addClass("z-sel").siblings().removeClass("z-sel");
            var target = $(this).attr("data-target");
            $("#officialContent > div").removeClass("z-sel").addClass("z-hide");
            $("#" + target).removeClass("z-hide").addClass("z-sel");
            if (target == 'addTypeBox') {
                if (opt.addTypeSign) {

                }
            } else if (target == 'ImgTexBoxListBox') {
                if (opt.ImgTexBoxListSign) {
                    opt.ImgTexBoxListSign = false;
                    var imagedata = {
                        typeId:  opt.typeId
                    }
                    official.imglist.init(imagedata);
                }
            }
        });
        $("#op_add_image").click(function () {
            var data={
                id:0,
                typeId:opt.typeId,
                mainContainer:"image-text-details"
            }
            official.config.init(data);
            // jumi.template("h5poster/basic/dialog-type",function(tpl){
            //     var d = dialog({
            //         title: 'h5板式',
            //         content:tpl,
            //         id:'createType',
            //         width:500,
            //         onshow:function(){
            //             $('#choose_type').find('li').click(function () {
            //                 $(this).addClass('active').siblings().removeClass('active')
            //             })
            //             $('#dialog_sure').click(function () {
            //                 var index = $('#choose_type').find("li.active").data('index');
            //                 if(index===0){
            //                     official.config.init(data);
            //                     dialog.get('createType').close().remove();
            //                 }else{
            //
            //                 }
            //             })
            //             $('#dialog_cancel').click(function () {
            //                 dialog.get('createType').close().remove();
            //             })
            //         }
            //     });
            //     d.showModal();
            // })

        })
    }

    return {
        init: _init
    };
})();