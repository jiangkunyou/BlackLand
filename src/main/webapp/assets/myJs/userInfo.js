/**
 * Created by jiangkunyou on 15/12/14.
 */
$(function(){

    // 给form隐藏域复制
    var userPass = $("input[name='tempPass']").val();
    $("input:hidden:eq(2)").val(userPass);
    var isSuper = $("input[name='tempSuper']").val();
    $("input:hidden:eq(3)").val(isSuper);
    var userId = $("input[name='tempUserId']").val();
    $("input:hidden:eq(4)").val(userId);


    // 从temp隐藏域获取当前用户信息 并赋值给相应textarea
    var userName = $("input[name='tempUserName']").val();
    $("textarea[id='userName']").val(userName);
    var userEmail = $("input[name='tempUserEmail']").val();
    $("textarea[id='userEmail']").val(userEmail);
    var userPhoneNumber = $("input[name='tempUserPhoneNumber']").val();
    $("textarea[id='userPhoneNumber']").val(userPhoneNumber);
    var userCompany = $("input[name='tempUserCompany']").val();
    $("textarea[id='userCompany']").val(userCompany);

    function openPop(){
        $('.theme-popover-mask').fadeIn(100);
        $('.theme-popover').slideDown(200);
        $("input[name='oldPassword']").val("");
        $("input[name='newPassword']").val("");
        $("input[name='newPassword_2']").val("");
    }

    function closePop(){
        $('.theme-popover-mask').fadeOut(100);
        $('.theme-popover').slideUp(200);
    }

    // ajax 更新密码完成后的动作
    function finishUpdate(message){
        $("#alertcontent").text(message);
        // 打开"确认收到"提示框
        $("#my-alert").modal("open");
        setTimeout(function(){
            $("#my-alert").modal('close');
        }, 1500);
    }

    function updatePassword(){
        // 打开"更新密码"视图
        $("#my-modal-loading").modal('open');
        var url = "updateUserInfo/updateUserPass.do";
        var innerUserPass = $("input:hidden:eq(2)").val();
        var args = {"userPass": innerUserPass};
        $.post(url, args, function(data){
            if(data == "success"){
                finishUpdate("update password success!");
                // 因为成功了,所以要把该字段更新成最新的密码
                $("input[name='tempPass']").val(innerUserPass);
            } else if(data == "failed") {
                finishUpdate("update password failed!");
                // 因为失败了,所以要把更新成新密码的隐藏域字段变回原值
                $("input:hidden:eq(2)").val(userPass);
            }
        });
    }

    // 验证更新的用户名是否已经存在
    function isUserNameUnique(newUserName){
        var url = "updateUserInfo/validateUserName.do";
        var args = {"newUserName": newUserName};
        var flag = true;
        //$.post(url, args, function(data){
        //    if(data == "failed") {
        //        finishUpdate("The name has been existed!");
        //        // 因为失败了,所以要把更新成新名字的隐藏域字段变回原值
        //        $("textarea[id='userName']").val(userName);
        //        //return false;
        //        flag = false;
        //    }
        //    else if(data == "success") {
        //        flag = true;
        //    }
        //});
        //return flag;

        $.ajax({
            type: "POST",
            url: url,
            data: args,
            async: false,  // 这里必须使用非异步,否则该函数不等服务器端运行结果出来就返回了一个true
            success: function(data){
                if(data == "failed") {
                    finishUpdate("The name has been existed!");
                    // 因为失败了,所以要把更新成新名字的隐藏域字段变回原值
                    $("textarea[id='userName']").val(userName);
                    flag = false;
                }
                else if(data == "success") {
                    flag = true;
                }
            },
            error: function(data){
                if(data == "failed") {
                    finishUpdate("The name has been existed!");
                    // 因为失败了,所以要把更新成新名字的隐藏域字段变回原值
                    $("textarea[id='userName']").val(userName);
                    flag = false;
                }
                else if(data == "success") {
                    flag = true;
                }
            }
        });
        return flag;
    }

    $("button:first").click(function(){
        openPop();
    });

    $("#cancelBtn").click(function(){
        closePop();
    });

    // 更新密码操作
    $("#sumbitNewPasswordBtn").click(function(){
        // 验证输入内容
        var userPass = $("input[name='tempPass']").val();
        var oldPassword = $("input[name='oldPassword']").val();
        if(oldPassword != userPass){
            alert("原始密码输入错误!");
            return false;
        }
        var newPassword = $("input[name='newPassword']").val();
        if(newPassword == ""){
            alert("请输入新密码!");
            return false;
        }
        var newPassword_2 = $("input[name='newPassword_2']").val();
        if(newPassword_2 == ""){
            alert("请再输入一遍新密码!");
            return false;
        }
        if(newPassword != newPassword_2){
            alert("两次新输入密码不一致!");
            return false;
        }

        // 把隐藏域设置成新密码  之后随总的提交按钮提交到后台
        // 因为新密码还没提交到后台  所以tempPass中的原始密码才是当前用户的密码
        $("input:hidden:eq(2)").val(newPassword);
        // 关闭模态视图
        closePop();
        // 异步更新密码
        updatePassword();
    });

    // 验证输入内容是否为空 如果为空将该项变成红色
    function validatorInput(){
        var userName = $("textarea[id='userName']").val();
        userName = $.trim(userName);
        if(userName == ""){
            $(".transparentTap:first").css("background-color", "rgba(255, 0, 0,.2)");
            //alert("请输入用户名!");
            setTimeout(function(){
                $(".transparentTap:first").css("background-color", "rgba(255, 255, 255,.8)");
            }, 1500);
            return false;
        }

        var userEmail = $("textarea[id='userEmail']").val();
        userEmail = $.trim(userEmail);
        if(userEmail == ""){
            $(".transparentTap:eq(1)").css("background-color", "rgba(255, 0, 0,.2)");
            //alert("请输入邮箱!");
            setTimeout(function(){
                $(".transparentTap:eq(1)").css("background-color", "rgba(255, 255, 255,.8)");
            }, 1500);
            return false;
        }

        var userPhoneNumber = $("textarea[id='userPhoneNumber']").val();
        userPhoneNumber = $.trim(userPhoneNumber);
        if(userPhoneNumber == ""){
            $(".transparentTap:eq(2)").css("background-color", "rgba(255, 0, 0,.2)");
            //alert("请输入电话!");
            setTimeout(function(){
                $(".transparentTap:eq(2)").css("background-color", "rgba(255, 255, 255,.8)");
            }, 1500);
            return false;
        }
        var userCompany = $("textarea[id='userCompany']").val();
        userCompany = $.trim(userCompany);
        if(userCompany == ""){
            $(".transparentTap:eq(3)").css("background-color", "rgba(255, 0, 0,.2)");
            //alert("请输入公司!");
            setTimeout(function(){
                $(".transparentTap:eq(3)").css("background-color", "rgba(255, 255, 255,.8)");
            }, 1500);
            return false;
        }

        $("textarea[id='userName']").val(userName);
        $("textarea[id='userEmail']").val(userEmail);
        $("textarea[id='userPhoneNumber']").val(userPhoneNumber);
        $("textarea[id='userCompany']").val(userCompany);
    }

    // 点击提交按钮后 判断用户信息是否有更改
    function isChanged(){
        // 先验证输入是否正确,也就是各项是否不为空
        if(validatorInput() == false){
            return false;
        }
        // 验证输入内容和原始内容是否相等,只要有一项不相等就认为更改了信息,需要提交到后台
        var tempUserName = $("textarea[id='userName']").val();
        if(tempUserName != userName){
            // 验证重名
            // alert(isUserNameUnique(tempUserName));
            return isUserNameUnique(tempUserName);
        }
        var tempUserEmail = $("textarea[id='userEmail']").val();
        if(tempUserEmail != userEmail){
            return true;
        }
        var tempUserPhoneNumber = $("textarea[id='userPhoneNumber']").val();
        if(tempUserPhoneNumber != userPhoneNumber){
            return true;
        }
        var tempUserCompany = $("textarea[id='userCompany']").val();
        if(tempUserCompany != userCompany){
            return true;
        }
        alert("没有更新内容!");
        return false;
    }

    // 提交按钮事件
    $(":submit:first").click(function(){
        return isChanged();
    });


})
