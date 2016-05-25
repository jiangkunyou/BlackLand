/**
 * Created by jiangkunyou on 15/12/6.
 */
$(function () {

    // 解析评论中的表情
    var resultface= $(".resultface").innerText;
    $('.result').html(resultface).parseEmotion();
    $("textarea:first").val("");

    // 点击表情按钮,显示表情table
    $('#face').click(function(event){
        if(! $('#sinaEmotion').is(':visible')){
            $(this).sinaEmotion();
            event.stopPropagation();
        }
    });

    // 点击发送留言
    $(".ds-post-button").click(function(){
        var cont = $("textarea:first").val();
        cont = $.trim(cont);
        if(cont == ""){
            alert("亲，说好的建议呢^_^!")
            return false;
        }
    });
});

