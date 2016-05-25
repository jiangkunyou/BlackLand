/**
 * Created by jiangkunyou on 15/12/3.
 */
$(function(){
    var url = window.location.href;
    url = url.split("?")[0];
    url = url.split("#")[0];
    url = url.substr(url.lastIndexOf("/") + 1);
    contextUrl = url.split(".")[0];
    if(contextUrl == "algorithm_detail" || contextUrl == "home" || contextUrl == "run_algorithm"){
        $(".amz-header-nav li[value='home']").addClass("am-active");
    }
    else{
        $(".amz-header-nav li[value='" + contextUrl + "']").addClass("am-active");
    }
})