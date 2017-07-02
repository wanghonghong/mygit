



    <style>
        .actionRedelivery em{
            font-style: normal;
            padding: 0px 10px;

        }

        .actionRedelivery .w200{
            width:200px;
        }
        .actionRedelivery p{
            padding: 5px;
        }
        .actionRedelivery table{
            padding: 0px 5px;
        }


        .actionRedelivery .table-actionRedelivery-hander{

        }
        .actionRedelivery .table-actionRedelivery-hander ul li:first-child,.actionRedelivery .table-actionRedelivery-content ol li:first-child{
            width:180px;
            padding: 0px 5px;
        }
        .actionRedelivery .table-actionRedelivery-hander{
            background: #f6f5f4;
            border-radius: 10px;
            border:1px solid #e0dfe0;
            margin-top: 10px;
        }
        .actionRedelivery .form-group{
            margin-bottom: 0px;
        }
        .actionRedelivery .table-actionRedelivery-hander ul li{
            vertical-align: middle;
            width: 115px;
            display: table-cell;
            text-align: center;
            padding:2px 0px;
            border-bottom: 1px solid #ebebeb;
        }
        .actionRedelivery .table-actionRedelivery-content ol li{

            vertical-align: middle;
            width: 115px;
            display: table-cell;
            text-align: center;
            padding:10px 0px;
            border-bottom: 1px solid #ebebeb;
        }
        .actionRedelivery .actionRedelivery-set-details{
            display: table-cell;
            width:100%;

            vertical-align: top;
        }
        .actionRedelivery .table-actionRedelivery-content .ar_number li{
            height: 50px;
            line-height: 50px;
            display: table-cell;
            text-align: center;
            vertical-align: middle;
            width: 115px;
            padding: 10px 0px;
            border-bottom: 1px solid #ebebeb;
        }
        .actionRedelivery .logistics-set-details-left{
            width:25%;
        }
        .actionRedelivery .logistics-order-set{
            margin: 15px 0px;
        }
        .actionRedelivery .table-actionRedelivery-content{
            background:#ffffff;
        }
        .actionRedelivery .tip{
            margin-top: 10px;
            height:50px;
            line-height:50px;
        }
        .actionRedelivery .form-group label{
            padding:0px;
        }
        .actionRedelivery .fansbg{
            width:210px;
            height: 45px;
            color:#ffffff;
            font-size:18px;
            text-align: center;
            line-height: 45px;
            background: url(${THIRD_URL}/img/pc/fansbg.png) no-repeat;
        }
        .actionRedelivery .icon-w{
            font-size: 12px;
            padding-right: 5px;
        }
        .actionRedelivery .icon-lamp{
            color: red;
        }
        .actionRedelivery .btn-options{
            height:35px;
            line-height:35px;
            position: absolute;
            right:20px;
            top:10px;
            padding: 0 45px;
        }
        .actionRedelivery .error{
            padding-left: 10px;
            height: 50px;
            line-height: 50px;
            display: none;
        }
        .actionRedelivery #tipError{
            font-size: 14px;
        }
        .actionRedelivery .w80{
            width: 80%;
        }
    </style>






            <div class="row clearfix">
                <div class="tab">
                    <ul>
                        <li class="active">关注派送</li>
                        <li>购买派送</li>
                        <li>收货派送</li>
                    </ul>
                </div>
                <div class="col-xs-4">
                    <div class="phone-view   phone-billd">
                        <div class="phone-shell-top">
                        </div>
                        <div class="phone-window">


                        </div>
                        <div class="phone-shell-bottom">
                        </div>

                    </div>
                </div>
                <div class="col-xs-8 existing-classify-choose actionRedelivery">

                    <div class="app-config-region">

                        <div class="panel  config-panel">
                            <div class="error">
                                <div class="hint hint-error">
                                    <i class="iconfont icon-errorhint"></i>
                                    <span id="tipError"></span>
                                </div>
                            </div>
                            <div class="panel-heading">
                                <input type="hidden" name="p_appid" id="p_appid">
                                <input type="hidden" name="activity_id" id="activity_id">
                                <input type="hidden" name="edit">
                                <input type="hidden" name="activity_shopId" id="activity_shopId">
                                <input type="hidden" name="activity_status" id="activity_status">

                                <div class="form-group">
                                <p>活动名称:</p>
                                <input type="text" placeholder="请输入活动名称"class="form-control w80 require" id="actionName">

                            </div>
                                <div class="form-group">
                                    <p>祝&nbsp;福&nbsp;语&nbsp;:</p>
                                    <input type="text" placeholder="请输入祝福语"class="form-control w80 require" id="blessingName">
                                </div>
                                <div class="form-group">

                                    <p>设定现金红包派送金额</p>
                                    <input type="text" placeholder="请输入红包金额,限定到个位数"class="form-control w200 require" id="red_amount">
                                    <span><p class="iconfont">元</p></span>
                                    <div class="btn btn-darkorange btn-options">编辑</div>
                                </div>




                            </div>
                            <div class="event_actionRedelivery">
                                <div class="jm-table">
                                    <div class="table-actionRedelivery-hander">
                                        <ul>
                                            <li><div class="table-cell vertical-middle">获奖百分比(总分100%)</div></li>
                                            <li>
                                                <div class="form-group require">
                                                <input type="text" class="form-control require" value="40" data-id="percent_1">
                                                    <input type="hidden" id="activity_id_1">
                                                    <input type="hidden" id="appid_1">
                                                    <input type="hidden" id="id_1">
                                                    <input type="hidden" id="status_1">
                                                    <input type="hidden" id="shopId_1">
                                                </div>
                                            </li>
                                            <li><div class="form-group require">
                                                <input type="text" class="form-control require" value="20" data-id="percent_2">
                                                <input type="hidden" id="activity_id_2">
                                                <input type="hidden" id="appid_2">
                                                <input type="hidden" id="id_2">
                                                <input type="hidden" id="status_2">
                                                <input type="hidden" id="shopId_2">
                                            </div></li>
                                            <li><div class="form-group">
                                                <input type="text" class="form-control require" value="20" data-id="percent_3">
                                                <input type="hidden" id="activity_id_3">
                                                <input type="hidden" id="appid_3">
                                                <input type="hidden" id="id_3">
                                                <input type="hidden" id="status_3">
                                                <input type="hidden" id="shopId_3">
                                            </div></li>
                                            <li><div class="form-group">
                                                <input type="text" class="form-control require" value="10" data-id="percent_4">
                                                <input type="hidden" id="activity_id_4">
                                                <input type="hidden" id="appid_4">
                                                <input type="hidden" id="id_4">
                                                <input type="hidden" id="status_4">
                                                <input type="hidden" id="shopId_4">
                                            </div></li>
                                            <li><div class="form-group">
                                                <input type="text" class="form-control require" value="10" data-id="percent_5">
                                                <input type="hidden" id="activity_id_5">
                                                <input type="hidden" id="appid_5">
                                                <input type="hidden" id="id_5">
                                                <input type="hidden" id="status_5">
                                                <input type="hidden" id="shopId_5">
                                            </div></li>
                                        </ul>
                                    </div>
                                    <div class="table-actionRedelivery-content">
                                        <ol class="ar_number">
                                            <li>消耗金额(元)</li>
                                            <li data-id="consume_1">-</li>
                                            <li data-id="consume_2">-</li>
                                            <li data-id="consume_3">-</li>
                                            <li data-id="consume_4">-</li>
                                            <li data-id="consume_5">-</li>
                                        </ol>
                                        <ol>
                                            <li>单人红包金额(元)</li>
                                            <li><div class="form-group">
                                                <input type="text" class="form-control require" data-id="single_1"></div></li>
                                            <li><div class="form-group">
                                                <input type="text" class="form-control require" data-id="single_2"></div></li>
                                            <li><div class="form-group">
                                                <input type="text" class="form-control require" data-id="single_3"></div></li>
                                            <li><div class="form-group">
                                                <input type="text" class="form-control require" data-id="single_4"></div></li>
                                            <li><div class="form-group">
                                                <input type="text" class="form-control require" data-id="single_5"></div></li>
                                        </ol>
                                        <ol class="ar_number">
                                            <li>中奖人数(个)</li>
                                            <li data-id="winners_1">-</li>
                                            <li data-id="winners_2">-</li>
                                            <li data-id="winners_3">-</li>
                                            <li data-id="winners_4">-</li>
                                            <li data-id="winners_5">-</li>
                                        </ol>
                                        <ol>
                                            <li>不中奖比例设定</li>
                                            <li><div class="form-group">
                                                <select name="" id="" class="form-control" data-id="percentage_1">
                                                    <option value="">请选择</option>
                                                    <option value="0">全中奖</option>
                                                    <option value="1">1:1</option>
                                                    <option value="2">1:2</option>
                                                    <option value="3">1:3</option>
                                                    <option value="4">1:4</option>
                                                    <option value="5">1:5</option>
                                                    <option value="6">1:6</option>
                                                    <option value="7">1:7</option>
                                                    <option value="8">1:8</option>
                                                    <option value="9">1:9</option>
                                                    <option value="10">1:10</option>
                                                </select>
                                            </div></li>
                                            <li><div class="form-group">
                                                <select name="" id="" class="form-control" data-id="percentage_2">
                                                    <option value="">请选择</option>
                                                    <option value="0">全中奖</option>
                                                    <option value="1">1:1</option>
                                                    <option value="2">1:2</option>
                                                    <option value="3">1:3</option>
                                                    <option value="4">1:4</option>
                                                    <option value="5">1:5</option>
                                                    <option value="6">1:6</option>
                                                    <option value="7">1:7</option>
                                                    <option value="8">1:8</option>
                                                    <option value="9">1:9</option>
                                                    <option value="10">1:10</option>
                                                </select>
                                            </div></li>
                                            <li><div class="form-group">
                                                <select name="" id="" class="form-control" data-id="percentage_3">
                                                    <option value="">请选择</option>
                                                    <option value="0">全中奖</option>
                                                    <option value="1">1:1</option>
                                                    <option value="2">1:2</option>
                                                    <option value="3">1:3</option>
                                                    <option value="4">1:4</option>
                                                    <option value="5">1:5</option>
                                                    <option value="6">1:6</option>
                                                    <option value="7">1:7</option>
                                                    <option value="8">1:8</option>
                                                    <option value="9">1:9</option>
                                                    <option value="10">1:10</option>
                                                </select>
                                            </div></li>
                                            <li><div class="form-group">
                                                <select name="" id="" class="form-control" data-id="percentage_4">
                                                    <option value="">请选择</option>
                                                    <option value="0">全中奖</option>
                                                    <option value="1">1:1</option>
                                                    <option value="2">1:2</option>
                                                    <option value="3">1:3</option>
                                                    <option value="4">1:4</option>
                                                    <option value="5">1:5</option>
                                                    <option value="6">1:6</option>
                                                    <option value="7">1:7</option>
                                                    <option value="8">1:8</option>
                                                    <option value="9">1:9</option>
                                                    <option value="10">1:10</option>
                                                </select>
                                            </div></li>
                                            <li><div class="form-group">
                                                <select name="" id="" class="form-control" data-id="percentage_5">
                                                    <option value="">请选择</option>
                                                    <option value="0">全中奖</option>
                                                    <option value="1">1:1</option>
                                                    <option value="2">1:2</option>
                                                    <option value="3">1:3</option>
                                                    <option value="4">1:4</option>
                                                    <option value="5">1:5</option>
                                                    <option value="6">1:6</option>
                                                    <option value="7">1:7</option>
                                                    <option value="8">1:8</option>
                                                    <option value="9">1:9</option>
                                                    <option value="10">1:10</option>
                                                </select>
                                            </div></li>
                                        </ol>
                                        <ol class="ar_number">
                                            <li>不中奖人数</li>
                                            <li data-id="no_winning_1">-</li>
                                            <li data-id="no_winning_2">-</li>
                                            <li data-id="no_winning_3">-</li>
                                            <li data-id="no_winning_4">-</li>
                                            <li data-id="no_winning_5">-</li>
                                        </ol>
                                    </div>
                                    <div class="tip">
                                        <div class="form-group">
                                            <label><i style="" class="iconfont icon-lamp"></i><span class="icon-w">吸粉人数为中奖人数+不中奖人数,本次活动投放金额为<code id="amount"></code>预计可获得新增粉丝人数</span></label>
                                            <div class="fansbg">
                                                <strong id="amount_num"></strong>
                                            </div>
                                            人
                                        </div>
                                    </div>
                                    <div class="logistics-order-set">
                                        <div class="logistics-set-details">
                                            <div class="logistics-set-details-left">
                                                <div>活动时间设定</div>
                                            </div>
                                            <div class="logistics-set-details-right">
                                                <div class="form-group">
                                                    <label>开始时间</label>
                                                    <div class="unify-postage-set">

                                                        <div class="form-group last-form-group">
                                                            <input type="text" class="form-control" name="start" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
                                                        </div>
                                                    </div>

                                                </div>
                                                <div class="form-group">
                                                    <label>结束时间</label>
                                                    <div class="unify-postage-set">

                                                        <div class="form-group last-form-group">
                                                            <input type="text" class="form-control" name="over" onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})">
                                                        </div>
                                                    </div>

                                                </div>

                                            </div>
                                        </div>


                                    </div>
                                    <div class="logistics-order-set">
                                        <div class="logistics-set-details">
                                            <div class="logistics-set-details-left">
                                                <div>未中奖推送语</div>
                                            </div>
                                            <div class="logistics-set-details-right">
                                                <div class="form-group">
                                                    <label class="margin-right-m"><span style="color:red">*</span>推送语</label>
                                                    <textarea id="txt" class="form-control"  style="width: 70%;height:160px;"></textarea>
                                                </div>



                                            </div>
                                        </div>

                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div style="margin: 61px 0 69px;" id="button">
                        <div class="btn btn-darkorange btn-lg" id="saveButton">保存</div>
                    </div>
                </div>
            </div>


    <script src="${basePath}/js/busi-js/pc/shop/redPaper.js" type="text/javascript" ></script>










