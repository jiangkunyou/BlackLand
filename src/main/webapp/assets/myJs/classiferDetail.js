/**
 * Created by jiangkunyou on 15/11/30.
 */
$(function(){

    $("#inputfile").change(function(){
        $.each(this.files, function(){
            var divNode = $("<div></div>");
            var iNode = $("<i class='am-icon-trash-o am-icon-fw'></i>").click(function(){
                if($(".am-offcanvas-content div").length == 1){
                    $("#uploadfilebtn").addClass("am-disabled");
                    $("#checkfile").addClass("am-disabled");
                    //$("#checkfile").text("no files");
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
        //$("#checkfile").css("font-size", "150%");
        //$("#checkfile").text("check files");
    });

    $("#selectFileBtn").hover(function(){
        $(this).css("color", "#ffffff");
        $(this).css("font-size", "180%");
        //$(this).text("1");
        $(this).text("select file");
        //$(this).text("");
        //$(this).find("div").show();
        //$(this).find("div").css("display", "");
    }, function(){
        //$(this).find("div").hide();
        //$(this).find("div").css("display", "none");
        //$(this).find("div").addClass("am-hide");
        $(this).text("1");
        $(this).css("font-size", "600%");
        this.blur();
    });

    $("#checkfile").hover(function(){
        $(this).css("color", "#ffffff");
        $(this).css("font-size", "180%");
        $(this).text("check file");
    }, function(){
        $(this).text("2");
        $(this).css("font-size", "600%");
        this.blur();
    });

    $("#uploadfilebtn").hover(function(){
        $(this).css("color", "#ffffff");
        $(this).css("font-size", "180%");
        $(this).val("upload file");
    }, function(){
        $(this).val("3");
        $(this).css("font-size", "600%");
        this.blur();
    });

    $("#runAlgorithmBtn").hover(function(){
        $(this).css("color", "#ffffff");
        $(this).css("font-size", "180%");
        $(this).val("start !");
    }, function(){
        $(this).val("4");
        $(this).css("font-size", "600%");
        this.blur();
    });

    $("#downloadFileBtn").hover(function(){
        $(this).css("color", "#ffffff");
        $(this).css("font-size", "180%");
        //$(this).find("input").css("margin-top", "50");
        $(this).find("a").text("download");
        //$("#downloadInput").val("download");
    }, function(){
        $(this).find("a").text("5");
        $(this).css("font-size", "600%");
        this.blur();
    });

    // ajaxFileUpload 上传完成后的动作
    function finishUpload(message){
        // 关闭"上传文件中"视图
        $("#my-modal-loading").modal('close');
        $('.am-offcanvas-content').html('');
        $("#alertcontent").text(message);
        // 打开"确认收到"提示框
        $("#my-alert").modal("open");
        setTimeout(function(){
            $("#my-alert").modal('close');
        }, 2000);
        $("#uploadfilebtn").addClass("am-disabled");
        $("#checkfile").addClass("am-disabled");
    }

    // ajax 完成后的动作
    function finishAjax(message){
        // 关闭"计算中"视图
        $("#my-modal-loading").modal('close');
        $('.am-offcanvas-content').html('');
        $("#alertcontent").text(message);
        // 打开"完成"提示框
        $("#my-alert").modal("open");
        setTimeout(function(){
            $("#my-alert").modal('close');
        }, 2000);
    }

    $("#uploadfilebtn").click(function(){
        this.blur();
        // 打开"上传文件中"视图
        $("#uploading").text("uploading ₍ↂ_ↂ₎ ...")
        $("#my-modal-loading").modal('open');
        var algorId = $("input:hidden").val();
        args = {"algorId": algorId};
        $.ajaxFileUpload({
            url: 'fileUpload.do',
            secureuri: false,
            dataType: 'json',
            data: args,
            fileElementId: 'inputfile',
            success: function(data){
                if(data == "success"){
                    finishUpload('Have received your files ^_^!');
                    //$("#checkfile").text("Haved files");
                    // 当输入文件上传成功才能开启运行算法按钮
                    $("#runAlgorithmBtn").removeClass("am-disabled");
                } else if(data == "failed"){
                    finishUpload('I did not receive your files >_<!');
                }
            },
            error: function(data){
            }
        });
    });

    // 跳转到运行算法界面
    $("#runAlgorithmBtn").click(function(){
        this.blur();
        // 打开"计算中..."视图
        $("#uploading").text("computing ₍ↂ_ↂ₎ ...")
        $("#my-modal-loading").modal('open');
        var url = "lucene.do";
        var classId = $("input:hidden").val();
        var data = {"classId": classId};
        $.ajax({
            type: "POST",
            url: url,
            data: data,
            success: function(data){
                if(data == "success"){
                    finishAjax("Algorithm completed successfully!");
                    $("#downloadFileBtn").removeClass("am-disabled");
                    $("#runAlgorithmBtn").addClass("am-disabled")
                    // 算法运行成功后,现实可视化tab
                    //$("li.am-hide").removeClass("am-hide");
                }
                else if(data == "failed"){
                    finishAjax("Algorithm failed!");
                }
            },
            error: function(data){
                finishAjax("Algorithm failed!");
            }
        });
    });

    // 下载结果文件
    $("#downloadFileBtn").click(function(){
        this.blur();
        //$(this).addClass("am-disabled")
        // 打开"计算中..."视图
        //$("#uploading").text("downloading ₍ↂ_ↂ₎ ...")
        //$("#my-modal-loading").modal('open');
        //var url = "fileDownload.do";
        //$.ajax({
        //    type: "POST",
        //    url: url,
        //    async: false,
        //    success: function(data){
        //        if(data == "success"){
        //            finishAjax("Download completed successfully!");
        //            //$("#downloadFile").removeClass("am-disabled");
        //            // 算法运行成功后,现实可视化tab
        //            //$("li.am-hide").removeClass("am-hide");
        //        }
        //        else if(data == "failed"){
        //            finishAjax("Download failed!");
        //        }
        //    },
        //    error: function(data){
        //        finishAjax("Download failed!");
        //    }
        //});
    });
});
