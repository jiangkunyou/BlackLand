/**
 * Created by jiangkunyou on 15/11/30.
 */
$(function(){
    var isFirst = true;
    // 算法类别选择
    $("select").change(function(){
        var url = $(this).val();
        if(isFirst != true) {
            //判断是否是第一次登陆,如果是第一次登陆不跳转页面
            window.location.href = url;
        }
        isFirst = false;
    });

    // 绑定所有的button动作,每个button就是一个算法,跳转url设成了button的id属性
    $("button").each(function(){
        $(this).click(function(){
            this.blur();
            var url = this.id;
            window.location.href = url;
        });
    });
});


