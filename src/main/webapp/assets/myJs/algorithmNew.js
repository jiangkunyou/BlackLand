/**
 * Created by jiangkunyou on 15/12/2.
 */

$(function(){
    function closePop(){
        $('.theme-popover-mask').fadeOut(100);
        $('.theme-popover').slideUp(200);
    }

    function validatorInput(){
        var algorName = $("textarea[id='algorName']").val();
        algorName = $.trim(algorName);
        if(algorName == ""){
            $(".transparentTap:first").css("background-color", "rgba(255, 0, 0,.2)");
            alert("请输入算法名!");
            $(".transparentTap:first").css("background-color", "rgba(255, 255, 255,.8)");
            return false;
        }

        var classId = $("input:eq(2)").val();
        classId = $.trim(classId);
        if(classId == 0){
            $(".transparentTap:eq(1)").css("background-color", "rgba(255, 0, 0,.2)");
            alert("请选择算法类别!");
            $(".transparentTap:eq(1)").css("background-color", "rgba(255, 255, 255,.8)");
            return false;
        }

        var algorCommand = $("textarea[id='algorCommand']").val();
        algorCommand = $.trim(algorCommand);
        if(algorCommand == ""){
            $(".transparentTap:eq(2)").css("background-color", "rgba(255, 0, 0,.2)");
            alert("请输入算法命令!");
            $(".transparentTap:eq(2)").css("background-color", "rgba(255, 255, 255,.8)");
            return false;
        }

        var algorDescrib = $("textarea[id='algorDescrib']").val();
        algorDescrib = $.trim(algorDescrib);
        if(algorDescrib == ""){
            $(".transparentTapBig:first").css("background-color", "rgba(255, 0, 0,.2)");
            alert("请输入算法简介!");
            $(".transparentTapBig:first").css("background-color", "rgba(255, 255, 255,.8)");
            return false;
        }
        var algorCareful = $("textarea[id='algorCareful']").val();
        algorCareful = $.trim(algorCareful);
        if(algorCareful == ""){
            $(".transparentTapBig:eq(1)").css("background-color", "rgba(255, 0, 0,.2)");
            alert("请输入注意事项!");
            $(".transparentTapBig:eq(1)").css("background-color", "rgba(255, 255, 255,.8)");
            return false;
        }

        $("textarea[id='algorName']").val(algorName);
        $("textarea[id='algorDescrib']").val(algorDescrib);
        $("textarea[id='algorCareful']").val(algorCareful);
        $("textarea[id='algorCommand']").val(algorCommand);
    }

    $(".am-list li").each(function(){
        $(this).click(function(){
            var className;
            if($(this).val() == "0"){
                $('.theme-popover-mask').fadeIn(100);
                $('.theme-popover').slideDown(200);
            }
            else{
                // 给form中对应的这几个input字段赋值
                className = $.trim($(this).find("a:first").text());
                $("form input:eq(2)").val(this.value);
                $("form input:eq(3)").val(className);
            }
            $("#classifer-actions").modal("close");
            $("button:first div:first").text(className);
        });
    });


    $("#cancelclassbtn").click(function(){
        closePop();
    });

    $("#sumbitclassbtn").click(function(){
        // 验证输入内容
        var className = $("input[name='customClassName']").val();
        className = $.trim(className);
        var classDescrib = $("textarea[name='customClassDescrib']").val();
        classDescrib = $.trim(classDescrib);
        if(className == ""){
            alert("请输入类别名!");
            //$("#my-alert").modal("open");
            return false;
        }
        if(classDescrib == ""){
            alert("请输入类别简介");
            return false;
        }

        //$("textarea[id='classDescrib']").val(classDescrib);
        $("button:first div:first").text(className);
        // 这里先设置自定义的类别option value为-1,方便后面提交时候做判断
        var newOption = $("<option value='-1'></option>").text(className);
        // 删除自定义选项
        $("select option:last").remove();
        // 把新的类别选项加进select中 并选中
        $("select").append(newOption);
        // 设置value为-1的option被选中
        $("select").val(-1);
        // 关闭浮层
        $("form input:eq(2)").val(-1);
        $("form input:eq(3)").val(className);
        $("form input:eq(4)").val(classDescrib);

        closePop();
    });

    $("#inputfile").change(function(){
        $.each(this.files, function(){
            var divNode = $("<div></div>");
            var iNode = $("<i class='am-icon-trash-o am-icon-fw'></i>").click(function(){
                if($(".am-offcanvas-content div").length == 1){
                    $("#uploadfilebtn").addClass("am-disabled");
                    $("#checkfile").addClass("am-disabled");
                    $("#checkfile").text("no jar");
                }
                $(this).parent().remove();
            });
            divNode.append(iNode);
            divNode.append(' ' + this.name);
            divNode.append($("<br/>"));
            $(".am-offcanvas-content").append(divNode);
        });
        $("#uploadfilebtn").removeClass("am-disabled");
        $("#checkfile").removeClass("am-disabled");
        $("#checkfile").text("check jar");
    });


    $("#uploadfilebtn").click(function(){
        // 先验证上面信息填写是否完整
        return validatorInput();
    });
})

