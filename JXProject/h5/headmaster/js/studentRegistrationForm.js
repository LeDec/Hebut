//标记分页处是搜索还是主页
var flag=0;
//显示报名列表
$(function () {
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/checkAuditEnroll',
        data: JSON.stringify({
            batchId:localStorage.getItem('batchId'),
            "checkRequest": {
                "commonRequest": {
                    "token": localStorage.getItem('token'),
                    "userId": localStorage.getItem('userId')
                },
                "pageRequest": {
                    "currentPage":1,
                    "pageSize": 9
                },
            },
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
        var num=0;
            //总页数设置
            Pagination.Init(document.getElementById('pagination'), {
                size: data.data.pages, // pages size
                page: 1,  // selected page
                step: 3   // pages before and after current
            });
             for (i in data.data.records) {
                                var tk;
                                num++;
                                lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">下载</button>', isPassStr='<button onclick="isPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">√</button>'
                                noPassStr='<button onclick="noPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">×</button>'
                                if(data.data.records[i].isPass==1){
                                    tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ "已通过" +'</td>'
                                }else if(data.data.records[i].enrollStage=="已申请"){
                                    tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ isPassStr + noPassStr +'</td>'
                                }else if(data.data.records[i].enrollStage=="初试未通过")
                                tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ "未通过" +'</td>'
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
           $.dynamicSubmit('http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/file/download',{fileName:data.data.fileName,filePath:data.data.filePath});
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
    var vals = [];
    vals.push($(btn).val());
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/auditApplication',
        data: JSON.stringify({
            "applicantId": vals,
            "commonRequest": {
                "token": localStorage.getItem('token'),
                "userId": localStorage.getItem('userId')
            },
            "isPass":1
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
function  noPass(btn){
    var vals = [];
    vals.push($(btn).val());
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/auditApplication',
        data: JSON.stringify({
            "applicantId": vals,
            "commonRequest": {
                "token": localStorage.getItem('token'),
                "userId": localStorage.getItem('userId')
            },
            "isPass":0
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

    $('#search').bind('click',function(){
    flag=1;
        $.ajax({
            url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/searchAuditEnroll',
            data: JSON.stringify({
                "applicantName": $("#name").val(),
                "applicantUserId":$("#userId").val() ,
                "batchId":localStorage.getItem('batchId'),
                "checkRequest": {
                    "commonRequest": {
                        "token": localStorage.getItem('token'),
                        "userId": localStorage.getItem('userId')
                    },
                    "pageRequest": {
                        "currentPage": 1,
                        "pageSize": 9
                    }
                },
                "projectId":"" ,
                "projectSymbol":$("#projectSymbol").val() ,
                "projectName": $("#projectName").val()
            }),
            contentType: 'application/json',
            type: 'post',
            dataType: 'json',
            success: function (data) {
            var num=0;
                //总页数设置
                Pagination.Init(document.getElementById('pagination'), {
                    size: data.data.pages, // pages size
                    page: 1,  // selected page
                    step: 3   // pages before and after current
                });
                $("#yustb2 tr:not(:first)").remove();//清空
 for (i in data.data.records) {
                                        var tk;
                                        num++;
                                        lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">查看</button>', isPassStr='<button onclick="isPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">√</button>'
                                        noPassStr='<button onclick="noPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">×</button>'
                                        if(data.data.records[i].isPass==1){
                                            tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ "已通过" +'</td>'
                                        }else if(data.data.records[i].enrollStage=="已申请"){
                                            tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ isPassStr + noPassStr +'</td>'
                                        }else if(data.data.records[i].enrollStage=="初试未通过")
                                        tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ "未通过" +'</td>'
                                        $("#yustb2").append('<tr>' + tk + '</tr>')
                                    }
            }
        })
    });
});


    function Func() {
        $("#yustb2 tr:not(:first)").remove();//清空
        $.ajax({
               url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/checkAuditEnroll',
               data: JSON.stringify({
                   batchId:localStorage.getItem('batchId'),
                   "checkRequest": {
                       "commonRequest": {
                           "token": localStorage.getItem('token'),
                           "userId": localStorage.getItem('userId')
                       },
                       "pageRequest": {
                           "currentPage":Pagination.page,
                           "pageSize": 9
                       },
                   },
               }),
               contentType: 'application/json',
               type: 'post',
               dataType: 'json',
               success: function (data) {
               var num=0;
                   //总页数设置
                   Pagination.Init(document.getElementById('pagination'), {
                       size: data.data.pages, // pages size
                       page:Pagination.page,  // selected page
                       step: 3   // pages before and after current
                   });
                    for (i in data.data.records) {
                                       var tk;
                                       num++;
                                       lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">查看</button>', isPassStr='<button onclick="isPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">√</button>'
                                       noPassStr='<button onclick="noPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">×</button>'
                                       if(data.data.records[i].isPass==1){
                                           tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ "已通过" +'</td>'
                                       }else if(data.data.records[i].enrollStage=="已申请"){
                                           tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ isPassStr + noPassStr +'</td>'
                                       }else if(data.data.records[i].enrollStage=="初试未通过")
                                       tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ "未通过" +'</td>'
                                       $("#yustb2").append('<tr>' + tk + '</tr>')
                                   }

               }
           })
    }
  function Search(){
    $.ajax({
            url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/searchAuditEnroll',
            data: JSON.stringify({
                "applicantName": $("#name").val(),
                "applicantUserId":$("#userId").val() ,
                "batchId":localStorage.getItem('batchId'),
                "checkRequest": {
                    "commonRequest": {
                        "token": localStorage.getItem('token'),
                        "userId": localStorage.getItem('userId')
                    },
                    "pageRequest": {
                        "currentPage": Pagination.page,
                        "pageSize": 9
                    }
                },
                "projectId":"" ,
                "projectSymbol":$("#projectSymbol").val() ,
                "projectName": $("#projectName").val()
            }),
            contentType: 'application/json',
            type: 'post',
            dataType: 'json',
            success: function (data) {
            var num=0;
                //总页数设置
                Pagination.Init(document.getElementById('pagination'), {
                    size: data.data.pages, // pages size
                    page: Pagination.page,  // selected page
                    step: 3   // pages before and after current
                });
                $("#yustb2 tr:not(:first)").remove();//清空
                for (i in data.data.records) {
                                                       var tk;
                                                       num++;
                                                       lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">查看</button>', isPassStr='<button onclick="isPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">√</button>'
                                                       noPassStr='<button onclick="noPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">×</button>'
                                                       if(data.data.records[i].isPass==1){
                                                           tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ "已通过" +'</td>'
                                                       }else if(data.data.records[i].enrollStage=="已申请"){
                                                           tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ isPassStr + noPassStr +'</td>'
                                                       }else if(data.data.records[i].enrollStage=="初试未通过")
                                                       tk =  '<td>' + num + '</td>' +'<td>' + data.data.records[i].projectSymbol + '</td>' +'<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ "未通过" +'</td>'
                                                       $("#yustb2").append('<tr>' + tk + '</tr>')
                                                   }
            }
        })
  }