//显示报名列表
$(function () {
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/checkAuditEnroll',
        data: JSON.stringify({
            "commonRequest": {
                "token": localStorage.getItem('token'),
                "userId": localStorage.getItem('userId')
            },
            "pageRequest": {
                "currentPage": 1,
                "pageSize": 5
            },
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            for (i in data.data.records) {
                var tk;
                    lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">查看</button>',
                    input='<input type="checkbox" name="ck" value="'+data.data.records[i].enrollId+'">',
                    tk = '<td>'+input+'</td>'+'<td>' + data.data.records[i].projectId + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>'  + '<td>'+ data.data.records[i].institute +'</td>'
                $("#yustb2").append('<tr>' + tk + '</tr>')

            }
        }
    })
})
//查看报名表
function lookContent(btn){
    console.log($(btn).parents("tr").find(".loookContent").val())
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/downloadApplication',
        data: JSON.stringify({
        "applicationId": $(btn).parents("tr").find(".loookContent").val(),
        "commonRequest": {
            "token": localStorage.getItem('token'),
            "userId": localStorage.getItem('userId')
    }
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            localStorage.setItem('name', data.data.fileName);
            window.location.href = "../picture.html";
            // $.dynamicSubmit('http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/file/download',{fileName:data.data.fileName,filePath:data.data.filePath});
        }
    })
}
$.dynamicSubmit = function (url, datas) {

    var form = $('#dynamicForm');

    if (form.length <= 0) {
        form = $("<form>");
        form.attr('id', 'dynamicForm');
        form.attr('style', 'display:none');
        form.attr('target', '');
        form.attr('method', 'post');

        $('body').append(form);
    }

    form = $('#dynamicForm');
    form.attr('action', url);
    form.empty();

    if (datas && typeof (datas) == 'object') {
        for (var item in datas) {
            var $_input = $('<input>');
            $_input.attr('type', 'hidden');
            $_input.attr('name', item);
            $_input.val(datas[item]);

            $_input.appendTo(form);
        }
    }

    form.submit();
}

//通过/不通过方法
function  isPass(btn){
    var isPass=$(btn).val()
    //获取多个复选框勾选对应的enrollId
    var vals = [];
    $('input:checkbox:checked').each(function (index, item) {
        vals.push($(this).val());
    });
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/auditApplication',
        data: JSON.stringify({
            "applicantId": vals,
            "commonRequest": {
                "token": localStorage.getItem('token'),
                "userId": localStorage.getItem('userId')
            },
            "isPass":isPass
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            if(data.code==0){
                alert("操作成功"),
                //刷新
                location.reload([true])
            }else{
                alert(data.msg)
            }
        }
    })

}
//搜索
$(document).ready(function(){
    $('#sousuo').bind('click',function(){
        $.ajax({
            url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/searchAuditEnroll',
            data: JSON.stringify({
                "applicantName": $("#name").val(),
                "applicantUserId":$("#userId").val() ,
                "checkRequest": {
                    "commonRequest": {
                        "token": localStorage.getItem('token'),
                        "userId": localStorage.getItem('userId')
                    },
                    "pageRequest": {
                        "currentPage": 1,
                        "pageSize": 5
                    }
                },
                "projectId":$("#projectId").val() ,
                "projectName": $("#projectName").val()
            }),
            contentType: 'application/json',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                $("#yustb2 tr:not(:first)").remove();//清空
                for (i in data.data.records) {
                    var tk;
                    lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">查看</button>',
                        input='<input type="checkbox" name="ck" value="'+data.data.records[i].enrollId+'">',
                        tk = '<td>'+input+'</td>'+'<td>' + data.data.records[i].projectId + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'
                    $("#yustb2").append('<tr>' + tk + '</tr>')

                }
            }
        })
    });
});

