/**
 * Created by jiangkunyou on 15/12/18.
 */
$(function () {

    // 统计日志当前已读偏移量
    var count = 0;
    var algorId = $("#algorId").val();

    // 画文件关键字字符云要用的变量
    var radius = 100;
    var dtr = Math.PI/180;
    var d=200;
    var mcList = [];
    var active = false;
    var lasta = 1;
    var lastb = 1;
    var distr = true;
    var tspeed = 1;
    var size = 500;
    var mouseX = 0;
    var mouseY = 0;
    var howElliptical = 1;
    var aA = null;
    var oDiv = null;

    // 配置echarts路径
    require.config({
        paths: {
            echarts: 'http://echarts.baidu.com/build/dist'
        }
    });

    var curTheme;

    // 为字符云中的每个字符随机选择颜色
    function createRandomItemStyle() {
        return {
            normal: {
                color: 'rgb(' + [
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160)
                ].join(',') + ')'
            }
        };
    }
    // 为文件关键字云中的每个字符随机选择颜色
    function createRandomItemStyle_2() {
        return 'rgb(' + [
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160),
                    Math.round(Math.random() * 160)
                ].join(',') + ')';
    }

    function fileWordCloudinit(){
        radius = 100;
        dtr = Math.PI / 180;
        d = 200;
        mcList = [];
        active = false;
        lasta = 1;
        lastb = 1;
        distr = true;
        tspeed = 1;
        size = 500;
        mouseX = 0;
        mouseY = 0;
        howElliptical = 1;
        aA = null;
        oDiv = null;
    }

    // 画文件字符云
    function drawFileWordCloud(){
        fileWordCloudinit()
        var i = 0;
        var oTag = null;
        oDiv = document.getElementById('fileWordCloud');
        aA = oDiv.getElementsByTagName('a');
        for(i = 0; i < aA.length; i++) {
            oTag = {};
            oTag.offsetWidth = aA[i].offsetWidth;
            oTag.offsetHeight = aA[i].offsetHeight;
            mcList.push(oTag);
        }
        sineCosine(0, 0, 0);
        positionAll();
        oDiv.onmouseover = function () {
            active = true;
        };
        oDiv.onmouseout = function () {
            active = false;
        };
        oDiv.onmousemove = function (ev) {
            var oEvent = window.event || ev;
            mouseX = oEvent.clientX-(oDiv.offsetLeft + oDiv.offsetWidth / 2);
            mouseY = oEvent.clientY-(oDiv.offsetTop + oDiv.offsetHeight / 2);
            mouseX /= 5;
            mouseY /= 5;
        };

        setInterval(update, 30);
    }

    // 画字符云
    function drewWordCloud(){
        // 使用
        require(
            [
                'echarts',
                'echarts/chart/wordCloud' // 使用柱状图就加载bar模块，按需加载
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart;
                // 获取主题并初始化
                require(['assets/theme/dark'], function(tarTheme){
                    curTheme = tarTheme;
                    myChart = ec.init(document.getElementById('charCloud'), tarTheme);
                    option = {
                        tooltip: {
                            show: true
                        },
                        series: [{
                            name: '',
                            type: 'wordCloud',
                            size: ['147%', '110%'],
                            textRotation : [0, 45, 90, -45],
                            textPadding: 0,
                            autoSize: {
                                enable: true,
                                minSize: 8
                            },
                            data: myData
                        }]
                    };
                    // 为echarts对象加载数据
                    myChart.setOption(option);
                });
            }
        );
    }

    // 画南丁格尔饼图
    function drawPie(){
        // 使用
        require(
            [
                'echarts',
                'echarts/chart/pie',
                'echarts/chart/funnel'
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart;
                // 获取主题并初始化
                require(['assets/theme/dark'], function(tarTheme){
                    curTheme = tarTheme;
                    myChart = ec.init(document.getElementById('pie'));
                    option = {
                        title : {
                            text: '',
                            subtext: '',
                            x:'center'
                        },
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        //legend: {
                            //x : 'center',
                            //y : 'bottom',
                            //data:['rose1','rose2','rose3','rose4','rose5','rose6','rose7','rose8']
                        //},
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                dataView : {show: true, readOnly: false},
                                magicType : {
                                    show: true,
                                    type: ['pie', 'funnel']
                                },
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        series : [
                            {
                                name:'面积模式',
                                type:'pie',
                                radius : [15, 110],
                                center : ['51%', 190],
                                roseType : 'area',
                                x: '25%',               // for funnel
                                max: 1500,                // for funnel
                                sort : 'ascending',     // for funnel
                                //width: '50%',
                                //funnelAlign: 'left',
                                data: myData2
                            }
                        ]
                    };
                    // 为echarts对象加载数据
                    myChart.setOption(option);
                });
            }
        );
    }

    // 画普通饼图
    function drawNormalPie(){
        // 使用
        require(
            [
                'echarts',
                'echarts/chart/pie',
                'echarts/chart/funnel'
            ],
            function (ec) {
                // 基于准备好的dom，初始化echarts图表
                var myChart;
                // 获取主题并初始化
                require(['assets/theme/dark'], function(tarTheme){
                    curTheme = tarTheme;
                    myChart = ec.init(document.getElementById('normalPie'));
                    option = {
                        tooltip : {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"
                        },
                        toolbox: {
                            show : true,
                            feature : {
                                mark : {show: true},
                                dataView : {show: true, readOnly: false},
                                magicType : {
                                    show: true,
                                    type: ['pie', 'funnel'],
                                    option: {
                                        funnel: {
                                            x: '25%',
                                            width: '50%',
                                            funnelAlign: 'left',
                                            max: 1548
                                        }
                                    }
                                },
                                restore : {show: true},
                                saveAsImage : {show: true}
                            }
                        },
                        calculable : true,
                        series : [
                            {
                                name:'访问来源',
                                type:'pie',
                                radius : '55%',
                                center: ['50%', '60%'],
                                data: myData3
                            }
                        ]
                    };

                    // 为echarts对象加载数据
                    myChart.setOption(option);
                });
            }
        );
    }

    var myData = [];
    // 发送请求给后端servlet
    function LDARequireWordCloud(args){
        // 下面的这个ajax是请求servlet运行算法
        var url = "visualization/drawWordCloud.do";
        $.ajax({
            type: "POST",
            url: url,
            data: args,
            dataType:"json",
            async: false,
            success: function(data){
                // 每次重新请求需要清掉上次的结果
                myData = [];
                for(var i = 0; i < data.length; i++){
                    //alert(data[i].content + ": " + data[i].percent);
                    myData.push({name: data[i].content, value: parseInt(data[i].percent * 10000000000), itemStyle: createRandomItemStyle()});
                }
                drewWordCloud();
            },
            error: function(data){
            }
        });
    }

    var myData2 = [];
    // 发送请求给后端servlet
    function LDARequireDrawPie(args){
        // 下面的这个ajax是请求servlet运行算法
        var url = "visualization/drawPie.do";
        $.ajax({
            type: "POST",
            url: url,
            data: args,
            dataType:"json",
            async: false,
            success: function(data){
                // 每次重新请求需要清掉上次的结果
                myData2 = [];
                for(var i = 0; i < data.length; i++){
                    //alert(data[i].content + ": " + data[i].percent);
                    myData2.push({value: parseInt(data[i].percent * 10000000000), name: data[i].content});
                }
                drawPie();
            },
            error: function(data){
            }
        });
    }

    var myData3 = [];
    // 发送请求给后端servlet
    function LDARequireDrawNormalPie(args){
        // 下面的这个ajax是请求servlet运行算法
        var url = "visualization/drawNormalPie.do";
        $.ajax({
            type: "POST",
            url: url,
            data: args,
            dataType:"json",
            async: false,
            success: function(data){
                // 每次重新请求需要清掉上次的结果
                myData3 = [];
                for(var i = 0; i < data.length; i++){
                    myData3.push({value: parseInt(data[i].percent), name: data[i].content});
                }
                drawNormalPie();
            },
            error: function(data){
            }
        });
    }

    // 发送请求给后端servlet
    function LDARequireDrawFileWordCloud(args){
        // 下面的这个ajax是请求servlet运行算法
        var url = "visualization/drawFileWordCloud.do";
        $.ajax({
            type: "POST",
            url: url,
            data: args,
            dataType:"json",
            async: false,
            success: function(data){
                var divNode = $("#fileWordCloud");
                $('#fileWordCloud a').remove();
                // 若没有该文件
                if(data[0].content == "noFile"){
                    finishAjax("No file >_< !")
                    return;
                }
                for(var i = 0; i < data.length; i++){
                    var aNode = $("<a></a>").text(data[i].content).css("color", createRandomItemStyle_2());
                    divNode.append(aNode);
                }
                drawFileWordCloud();
            },
            error: function(data){
            }
        });
    }

    // 点击可视化tab的动作
    $("#visualization").click(function(){
        // 目前是测试 所以直接设成14
        algorId = "14";
        if(algorId == "14"){
            // 拿到当前topicTile,形式如 Topic 1
            var topic = $("#topicTitle").text();
            // 获取TopicTitle后面的数字
            var topicId = topic.split(" ")[1];
            // 这是第一次向后台请求数据,所以加入一个isFirst参数,防止后台的map文件载入多次
            var args = {"algorId": "14", "topicId": topicId, "isFirst": "true"};
            LDARequireWordCloud(args);

            var fileName = $("#fileName").val();
            fileName = $.trim(fileName);
            if(fileName == ""){
                alert("请输入文件名!");
                return false;
            }
            args = {"algorId": "14", "fileName": fileName, "isFirst": "true"};
            LDARequireDrawPie(args);

            args = {"algorId": "14"};
            LDARequireDrawNormalPie(args);

            args = {"algorId": "14", "fileName": fileName};
            setTimeout(LDARequireDrawFileWordCloud(args), 2000);
        }
    });

    // LDA生成的饼图点击search动作
    //$("#search").click(function(){
    //    var fileName = $("#fileName").val();
    //    fileName = $.trim(fileName);
    //    if(fileName == ""){
    //        alert("请输入文件名!");
    //        return false;
    //    }
    //    args = {"algorId": "14", "fileName": fileName};
    //    LDARequireDrawPie(args);
    //});
    // LDA生成的饼图点击search动作
    $("#fileName").keydown(function(event){
        var fileName = this.value;
        if(validator(fileName) == false){
            return false;
        }
        args = {"algorId": "14", "fileName": fileName};
        doKeydown(event, LDARequireDrawPie, args);
    });

    // LDA生成的文件关键字字符云中点击search动作
    $("#fileName_2").keydown(function(event){
        var fileName = this.value;
        if(validator(fileName) == false){
            return false;
        }
        args = {"algorId": "14", "fileName": fileName};
        doKeydown(event, LDARequireDrawFileWordCloud, args);
    });

    // 验证value是否为空
    function validator(value){
        value = $.trim(value);
        if(value == ""){
            alert("请输入文件名!");
            return false;
        }
    }

    function doKeydown(event, f, args){
        event = event || window.event;
        var param = event.target || event.srcElement;
        if (event.keyCode == 13){
            f(args);
        }
    }


    // 点击上一个主题
    $("#previous").click(function(){
        // 拿到当前topicTile,形式如 Topic 1
        var topic = $("#topicTitle").text();
        // 获取TopicTitle后面的数字
        var topicNumber = parseInt(topic.split(" ")[1]);
        // 因为是返回上一个主题,所以减1
        topicNumber = topicNumber - 1;
        $("#topicTitle").text("Topic " + topicNumber);
        // 如果减完1发现为1了,那就说明已经是第一个主题了
        if(topicNumber == 1){
            $(this).addClass("am-disabled");
        }
        // 拿到当前topicTile,形式如 Topic 1
        topic = $("#topicTitle").text();
        // 获取TopicTitle后面的数字
        var topicId = topic.split(" ")[1];
        var args = {"algorId": "14", "topicId": topicId};
        LDARequireWordCloud(args);
        $("#next").removeClass("am-disabled");
    });

    // 点击下一个主题
    $("#next").click(function(){
        // 拿到当前topicTile,形式如 Topic 1
        var topic = $("#topicTitle").text();
        // 获取TopicTitle后面的数字
        var topicNumber = parseInt(topic.split(" ")[1]);
        // 因为是返回上一个主题,所以加1
        topicNumber = topicNumber + 1;
        $("#topicTitle").text("Topic " + topicNumber);
        // 如果加完1发现为最大主题数了,那就说明已经是最后一个主题了
        if(topicNumber == 20){
            $(this).addClass("am-disabled");
        }
        // 拿到当前topicTile,形式如 Topic 1
        topic = $("#topicTitle").text();
        // 获取TopicTitle后面的数字
        var topicId = topic.split(" ")[1];
        var args = {"algorId": "14", "topicId": topicId};
        LDARequireWordCloud(args);
        $("#previous").removeClass("am-disabled");
    });


    function update()
    {
        var a;
        var b;
        if(active) {
            a = (-Math.min(Math.max(-mouseY, -size), size) / radius) * tspeed;
            b = (Math.min(Math.max(-mouseX, -size), size) / radius) * tspeed;
        }
        else {
            a = lasta * 0.98;
            b = lastb * 0.98;
        }
        lasta = a;
        lastb = b;

        if(Math.abs(a)<=0.01 && Math.abs(b)<=0.01)
        {
            return;
        }

        var c = 0;
        sineCosine(a, b, c);
        for(var j = 0; j < mcList.length; j++)
        {
            var rx1 = mcList[j].cx;
            var ry1 = mcList[j].cy * ca + mcList[j].cz * (-sa);
            var rz1 = mcList[j].cy * sa + mcList[j].cz * ca;

            var rx2 = rx1 * cb + rz1 * sb;
            var ry2 = ry1;
            var rz2 = rx1 * (-sb) + rz1 * cb;

            var rx3 = rx2 * cc + ry2 * (-sc);
            var ry3 = rx2 * sc + ry2 * cc;
            var rz3 = rz2;

            mcList[j].cx = rx3;
            mcList[j].cy = ry3;
            mcList[j].cz = rz3;

            per = d / (d + rz3);

            mcList[j].x = (howElliptical * rx3 * per) - (howElliptical * 2);
            mcList[j].y = ry3 * per;
            mcList[j].scale = per;
            mcList[j].alpha = per;

            mcList[j].alpha = (mcList[j].alpha - 0.6) * (10 / 6);
        }

        doPosition();
        depthSort();
    }

    function depthSort()
    {
        var i=0;
        var aTmp=[];

        for(i=0;i<aA.length;i++)
        {
            aTmp.push(aA[i]);
        }

        aTmp.sort
        (
            function (vItem1, vItem2)
            {
                if(vItem1.cz>vItem2.cz)
                {
                    return -1;
                }
                else if(vItem1.cz<vItem2.cz)
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        );

        for(i=0;i<aTmp.length;i++)
        {
            aTmp[i].style.zIndex=i;
        }
    }

    function positionAll()
    {
        var phi=0;
        var theta=0;
        var max=mcList.length;
        var i=0;

        var aTmp=[];
        var oFragment=document.createDocumentFragment();

        //Ëæ»úÅÅÐò
        for(i=0;i<aA.length;i++)
        {
            aTmp.push(aA[i]);
        }

        aTmp.sort
        (
            function ()
            {
                return Math.random()<0.5?1:-1;
            }
        );

        for(i=0;i<aTmp.length;i++)
        {
            oFragment.appendChild(aTmp[i]);
        }

        oDiv.appendChild(oFragment);

        for( var i=1; i<max+1; i++){
            if(distr)
            {
                phi = Math.acos(-1+(2*i-1)/max);
                theta = Math.sqrt(max*Math.PI)*phi;
            }
            else
            {
                phi = Math.random()*(Math.PI);
                theta = Math.random()*(2*Math.PI);
            }
            //×ø±ê±ä»»
            mcList[i-1].cx = radius * Math.cos(theta)*Math.sin(phi);
            mcList[i-1].cy = radius * Math.sin(theta)*Math.sin(phi);
            mcList[i-1].cz = radius * Math.cos(phi);

            aA[i-1].style.left=mcList[i-1].cx+oDiv.offsetWidth/2-mcList[i-1].offsetWidth/2+'px';
            aA[i-1].style.top=mcList[i-1].cy+oDiv.offsetHeight/2-mcList[i-1].offsetHeight/2+'px';
        }
    }

    function doPosition()
    {
        var l=oDiv.offsetWidth/2;
        var t=oDiv.offsetHeight/2;
        for(var i=0;i<mcList.length;i++)
        {
            aA[i].style.left=mcList[i].cx+l-mcList[i].offsetWidth/2+'px';
            aA[i].style.top=mcList[i].cy+t-mcList[i].offsetHeight/2+'px';

            // 调整字符云中的字体大小
            aA[i].style.fontSize=Math.ceil(16 * mcList[i].scale / 2)+8+'px';

            aA[i].style.filter="alpha(opacity="+100*mcList[i].alpha+")";
            aA[i].style.opacity=mcList[i].alpha;
        }
    }

    function sineCosine( a, b, c)
    {
        sa = Math.sin(a * dtr);
        ca = Math.cos(a * dtr);
        sb = Math.sin(b * dtr);
        cb = Math.cos(b * dtr);
        sc = Math.sin(c * dtr);
        cc = Math.cos(c * dtr);
    }

    // ajax 完成后的动作
    function finishAjax(message){
        $("#alertcontent").text(message);
        // 打开"确认收到"提示框
        $("#my-alert").modal("open");
        setTimeout(function(){
            $("#my-alert").modal('close');
        }, 2000);
    }
//
//    // 下面的这个ajax是请求servlet运行算法
    var url = "hadoop/algorithmOnHadoop.do";
    var data = {"algorId": algorId};
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        success: function(data){
            if(data == "success"){
                finishAjax("Algorithm completed successfully!");
                $("#exportFile").removeClass("am-disabled");
                // 算法运行成功后,现实可视化tab
                $("li.am-hide").removeClass("am-hide");
            }
            else if(data == "failed"){
                finishAjax("Algorithm failed!");
            }
        },
        error: function(data){
            finishAjax("Algorithm failed!");
        }
    });

    var flag = true;
    function readLog(){
        // 下面这个ajax用于读取算法运行时的日志文件,一段一段读回来
        url = "hadoop/readLog.do";
        $.ajax({
            type: "POST",
            url: url,
            dataType: "json",
            success: function(data){
                if(data.len == "-2") {
                    flag = false;
                    finishAjax("Algorithm completed successfully!");
                    $("#exportFile").removeClass("am-disabled");
                } else if(data.len == "-3"){
                    flag = false;
                    finishAjax("Read log error!");
                } else {
                    var myLog = $("#myLog");
                    var currentVal = myLog.val();
                    myLog.val(currentVal + data.myContent);
                    if(myLog.length)
                        myLog.scrollTop(myLog[0].scrollHeight - myLog.height());
                }
            },
            error: function(data){
                flag = false;
                finishAjax("Read log error!");
            }
        });
    }

//  每隔1s发一次ajax请求
    function doReadLog(){
        var interalId = setInterval(function(){
            if(flag){
                readLog();
            }
            else{
                window.clearInterval(interalId)
            }
        }, 1000);
    }

    // 等算法跑起来1秒后,开始读日志
    //setTimeout(function(){
        doReadLog();
    //}, 500);
});
