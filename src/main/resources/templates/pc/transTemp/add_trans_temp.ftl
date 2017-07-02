
<div class="main-content-pc">
    <div class="mallBuilding manage">
        <div class="col-xs-12" style="padding-left: 0;">
            <div class="form-group col-xs-6 zhyh-mb zhyh-pl">
                <label>模板名称:</label><input type="text" id="transTemName" class="form-control distance" placeholder="请输模板名称" />

            </div>
        </div>
        <div class="jm-table" id="transTEM0" style="font-size: 12px;">
            <ul class="table-hander">
                <li style="width: 35%;text-align: left;  padding-left: 10px;">可配送至</li>
                <li>首件（个）</li>
                <li>运费（元）</li>
                <li>续件（个）</li>
                <li>续件（元）</li>
                <li>操作</li>
            </ul>
            <ul class="table-container" id="first-table-container">
                <li style="width: 35%;text-align: left;  padding-left: 10px;">
                    <div class="page-left" style="margin-top: 0;">
                        <div class="btn  btn-lightgray btn-sm dialog" onclick="addPS()">指定可配送区域和运费</div>
                    </div>

                    <div id="dialogAreainfo" class="dialoginfo dhk" >

                        <div class="jm-table jm-table1">
                            <ul class="table-hander">
                                <li>
                                    <i class="iconfont icon-classify1"></i>
                                    可选省、市、区
                                </li>
                            </ul>
                            <ul id="area_tree" class="ztree" style="border: 1px solid #617775;overflow-y: scroll;height: 500px;">

                            </ul>
                        </div>
                    </div>

                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <input style="float: none;width:50%;" type="text" class="form-control distance" placeholder="" />
                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <input style="float: none;width:50%;" type="text" class="form-control distance" placeholder="" />
                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <input style="float: none;width:50%;" type="text" class="form-control distance" placeholder="" />
                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <input style="float: none;width:50%;" type="text" class="form-control distance" placeholder="" />
                </li>
                <li style="display: none;" class="form-group form-group-zwmb">
                    <span  class='delete' >删除</span>
                </li>

            </ul>

        </div>
        <div style="margin: 61px 0 69px;">
            <div style="padding:0 30px;" class="btn btn-lightgray btn-radius-5 btn-lg col-xs-offset-4" id="back">返回</div>
            <div id="addTransTempBtn" style="margin-left:8px;" class="btn btn-darkorange btn-radius-5 btn-lg">保存</div>
        </div>

    </div>
</div>
<script type="text/javascript">

    $(document).ready(function(){
        onLoadZTree();
    });

    function addPS()  {
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        treeObj.checkAllNodes(false); // 清除上次操作
        showAreaTreeWindow();
    }
    function showAreaTreeWindow() {
        var elem = $('#dialogAreainfo');
        dialog({
            title:"选择可配送区域",
            content:elem,
            width :500,
            okValue:"点击确认",
            ok:function(){
                saveTransTemp();
            },
            cancelValue:"点击取消",
            cancel:function(){
            }
        }).show();
    }

    $(document).on("click",".delete",function(){
        $(this).parent().parent().remove();
    });

    $("#back").click(function () {
        url = CONTEXT_PATH + "/get_trans_templates_list/"+${shopid};
        // window.location.href=url;
        $.ajaxHtmlGet(url, null, {
            done: function (res) {
                $("#productList").empty();
                $("#productList").append(res.data);
            }
        });
    });

    function saveTransTemp(){
        var treeObj = $.fn.zTree.getZTreeObj("area_tree");
        var nodes = treeObj.getCheckedNodes(true);
        var areaIds ="";
        var areaNames ="";
        if(nodes.length===0){
            var d = dialog({
                content: '请勾选配送地区！'
            });
            d.show();
            setTimeout(function () {
                d.close().remove();
            }, 1000);
            return;
        }
        for(var i=0;i<nodes.length;i++){

            if(i==nodes.length-1){
                areaIds += nodes[i].id;
                areaNames += nodes[i].name;
            }else{
                areaIds += nodes[i].id+",";
                areaNames += nodes[i].name+",";
            }

        }

        var html=$("#first-table-container").clone();
        html.find(".page-left").remove();
        html.find("#dialogAreainfo").remove();
        html.find("li").css("display","static");
        html.removeAttr("id");
        html.find("li:first").append("<span style='display: block;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;' title="+areaNames+" areaIds="+areaIds+">"+areaNames+"</span>");

        $("#transTEM0").append(html);

    }

    //保存运费模板

    var arry = [];
    $("#addTransTempBtn").click(function () {
        var transTempName = $("#transTemName").val();
        if(transTempName.length===0){
            var d = dialog({
                content: '请填写模板名称!'
            });
            d.show();
            setTimeout(function () {
                d.close().remove();
            }, 1000);
            return;
        }
        var arrays = [];//总行数组
        $(".table-container:gt(0)").each(function(){
            var lines = [];//行数组
            var values = [];//input值数组
            $(this).find("li").each(function(index){
                if(index === 0){//遍历列，找出第一列
                    var ids = $(this).find("span").attr("areaids");
                    var names = $(this).find("span").html();
                    lines.push(ids.split(','));
                    lines.push(names.split(','));
                }else{
                    if(index<5){
                        values.push($(this).find("input").val());
                    }

                }
            });
            lines.push(values);
            arrays.push(lines);
        });
//			console.log(arrays);
        var shopId = ${shopid};



//判断首件和续件个数不能为0 start
        var sum = 0;
        $(".table-container:gt(0)").each(function(i){

            var text = $(this).find("li:eq(1) input").val();
            var text1 = $(this).find("li:eq(3) input").val();
            if(text==0||text1==0){
                sum += 1;
            }
        });
        if(sum>=1){
            var d = dialog({
                content: '首件和续件个数不能为0'
            });
            d.show();
            setTimeout(function () {
                d.close().remove();
            }, 1000);

        }
        else{
            trans.temp.addTransTemp(shopId,transTempName,arrays);
        }
//判断首件和续件个数不能为0 end

    })
</script>


</body>

</html>