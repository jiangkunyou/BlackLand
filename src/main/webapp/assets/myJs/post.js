/**
 * Created by jiangkunyou on 15/10/26.
 */
$(function(){
    $(":text[name='username']").change(function(){
        var value = $(this).val();
        value = $.trim(value);
        if(value != ""){
            var url = "${pageContext.request.contextPath}/didTheNameExist";
            var args = {"username": value, "time": new Date()};
            $.post(url, args, function(data){
                $("#message").html(data);
            });
        }
    });
});