$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");

    // 获取标题和内容
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();

    // 发送异步请求 post方式
    $.post{
        CONTEXT_PATH + "/discuss/add",
        {"title":title, "content":content},
        function(data){ // 回调函数
            data = $.parseJSON(data);

            $("#hintBody").text(data.msg); // 在提示框中显示返回的消息

            $("#hintModal").modal("show"); // 显示提示框

            setTimeout(function(){ // 两秒后,自动隐藏提示框
                $("#hintModal").modal("hide");
                if(data.code == 0){ // 如果成功, code==0, 自动刷新
                    window.location.reload();
                }
            }, 2000);
        }
    }


};