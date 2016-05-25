/**
 * Created by jiangkunyou on 15/10/27.
 */
$(function(){
    
    $("#loginbtn").click(function(){
        var userName = $(":text[id='userName']").val();
        userName = $.trim(userName);
        if(userName == "") {
            alert("请输入用户名！");
            return false;
        }
        var password = $(":password").val();
        password = $.trim(password);
        if(password == ""){
            alert("请输入密码！");
            return false;
        }
    });
    //$(":text[id='userName']").blur(function(){
    //    var userName = $(this).val();
    //    userName = $.trim(userName);
    //    if(userName == "") {
    //        alert("请输入用户名！");
    //        return false;
    //    }
    //});
    //$(":password").blur(function(){
    //    var password = $(this).val();
    //    if(password == ""){
    //        alert("请输入密码！");
    //        return false;
    //    }
    //});

//                if(password == ''){
//                    alert("请输入密码！");
//                    return;
//                }
//        var url = ;
//                这里加
//        var args = {"userName": userName, "password": password, "time": new Date()};
//        $.post("LoginSystemServlet",
//            {
//                userName:userName,
//                password:password
//            },
//            function (data) //回传函数
//            {
//                if (data == "true") {
//                    window.location.href = "index.jsp";
//                } else {
//                    alert("用户名或密码错误，请重新输入!");
//                }
//            });;
});
