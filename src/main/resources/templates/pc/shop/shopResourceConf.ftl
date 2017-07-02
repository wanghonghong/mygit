<script src="${basePath}/js/our-js/map_material_conf.js" type="text/javascript" charset="utf-8"></script>
    <style>
        .material-management .goods-map-list{
            padding: 15px 0px;
        }
        .material-management .goods-map-list li{
            width:150px;
            float: left;
            position: relative;
            margin-bottom:15px;
            margin-left:2px;
        }
        .material-management .goods-map-list img{
            width:150px;
            height: 150px;
        }
        .material-management input{
            opacity: 0;
            -moz-opacity:0;
            filter:alpha(opacity=0);
            position: absolute;
            top:0;
            left:0;
            height:40px;
            width:150px;
        }
        .material-management .lstk-tittle{

            height: 22px;
            line-height: 22px;
            display:block;
            white-space:nowrap;
            overflow:hidden;
            text-overflow:ellipsis;
        }
        .material-management .map-name{
            font-size: 16px;
        }
        .material-management .lstk-tip{
            position:absolute;
            height: 25px;
            line-height: 25px;
            background:#676767;
            opacity: .5;
            bottom: 0px;
            width:100%;
        }
        .material-management span.listp{
            height: 25px;
            line-height: 25px;
            z-index:99999;
            display: block;
            opacity:1;
            padding-left:4px;
            color:#ffffff;
            position:absolute;
            bottom:0px;
        }
        .material-management .del{
            position: absolute;
            right:0px;
            top:0px;
            font-size: 16px;
            cursor: pointer;
        }

    </style>




    <div class="main-content-pc ">
        <div class="mallBuilding multimarketing">
            <div class="row clearfix">
                <div class="tab" id="mapList">
                    <ul>
                        <li class="active">历史记录</li>
                        <li>本地上传</li>
                    </ul>
                </div>

            </div>
        </div>

        <div id="goods-content"  class="material-management">
            <div class="goods-map-list clearfix" id="goods_0">

<#if shopResourceList??>
<ul>
	<#list shopResourceList as shopResource>

        <li class="floatleft margin-right-m">
            <div class="existing-classify-info lstk-img">
                <img src="${shopResource.resUrl}" alt="">
            </div>
            <input type="hidden" value="${shopResource.id}"/>
            <div class="lstk-tip"></div>
            <span class="listp">${shopResource.resName!}</span>
            <div class="del">
                <i class="iconfont icon-delete1"></i>
            </div>

        </li>
	</#list>
</ul>
</#if>

            </div>
            <div class="goods-map-list clearfix" id="goods_1" style="display:none;">
                <li class="addgoods-btn" id="sendBtn">
                    上传图片
                    <input type="file" id="file_one_first" name="myfile">
                    <span></span>
                </li>

            </div>

        </div>
    </div>

