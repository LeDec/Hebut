//标记分页处是搜索还是主页
var flag=0;
//显示所有列表
$(function () {
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/user/checkClassmates',
        data: JSON.stringify({
            "batchId": localStorage.getItem('batchId'),
            "checkRequest": {
                "commonRequest": {
                    token: localStorage.getItem('token'),
                    userId: localStorage.getItem('userId'),
                },
                "pageRequest": {
                    "currentPage": 1,
                    "pageSize": 8
                },
            }
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            //总页数设置
            Pagination.Init(document.getElementById('pagination'), {
                size: data.data.pages, // pages size
                page: 1,  // selected page
                step: 3   // pages before and after current
            });
            for (i in data.data.records) {
                var tk;
                lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="' + data.data.records[i].enrollId + '">下载</button>',
                    tk = '<td>' + Number(i + 1) + '</td>' + '<td>' + data.data.records[i].projectSymbol + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].institute + '</td>' + '<td>' + data.data.records[i].major + '</td>' + '<td>' + lookcontent + '</td>'
                $("#yustb2").append('<tr>' + tk + '</tr>'),
                    console.log(Number(i + 1))
            }
        }
    })
})
//搜索列表
$(document).ready(function() {
    $('#search').bind('click', function () {
    flag=1;
        $.ajax({
            url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/user/searchClassmates',
            data: JSON.stringify({
                "batchId": localStorage.getItem('batchId'),
                "checkRequest": {
                    "commonRequest": {
                        token: localStorage.getItem('token'),
                        userId: localStorage.getItem('userId'),
                    },
                    "pageRequest": {
                        "currentPage": 1,
                        "pageSize": 8
                    },
                },
                "institute": $("#institute").val(),
                "major": $("#major").val(),
                "projectSymbol": $("#projectSymbol").val(),
                "projectName": $("#projectName").val(),
                "studentId": $("#userId").val(),
                "studentName": $("#name").val()
            }),
            contentType: 'application/json',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                //总页数设置
                Pagination.Init(document.getElementById('pagination'), {
                    size: data.data.pages, // pages size
                    page: 1,  // selected page
                    step: 3   // pages before and after current
                });
                $("#yustb2 tr:not(:first)").remove();//清空
                for (i in data.data.records) {
                    var tk;
                    lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">下载</button>',
                        tk = '<td>'  +Number(i+1)+ '</td>'+'<td>' + data.data.records[i].projectSymbol + '</td>'+'<td>' + data.data.records[i].projectName+ '</td>'+'<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].institute + '</td>' + '<td>' + data.data.records[i].major + '</td>'+'<td>'+lookcontent+'</td>'
                    $("#yustb2").append('<tr>' + tk + '</tr>'),
                        console.log(Number(i+1))
                }
            }
        })
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
  // 分页条功能:
    function Func() {

        $("#yustb2 tr:not(:first)").remove();//清空
        $.ajax({
            url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/user/checkClassmates',
            data: JSON.stringify({
                "batchId": localStorage.getItem('batchId'),
                "checkRequest": {
                    "commonRequest": {
                        token: localStorage.getItem('token'),
                        userId: localStorage.getItem('userId'),
                    },
                    "pageRequest": {
                        "currentPage": Pagination.page,
                        "pageSize": 8
                    },
                }
            }),
            contentType: 'application/json',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                //总页数设置
                Pagination.Init(document.getElementById('pagination'), {
                    size: data.data.pages, // pages size
                    page: Pagination.page,  // selected page
                    step: 3   // pages before and after current
                });
                for (i in data.data.records) {
                    var tk;
                    lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">下载</button>',
                        tk = '<td>'  +Number(i+1)+ '</td>'+'<td>' + data.data.records[i].projectSymbol + '</td>'+'<td>' + data.data.records[i].projectName+ '</td>'+'<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].institute + '</td>' + '<td>' + data.data.records[i].major + '</td>'+'<td>'+lookcontent+'</td>'
                    $("#yustb2").append('<tr>' + tk + '</tr>'),
                        console.log(Number(i+1))
                }
            }
        })


    }
    function Search(){
         $.ajax({
                    url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/user/searchClassmates',
                    data: JSON.stringify({
                        "batchId": localStorage.getItem('batchId'),
                        "checkRequest": {
                            "commonRequest": {
                                token: localStorage.getItem('token'),
                                userId: localStorage.getItem('userId'),
                            },
                            "pageRequest": {
                                "currentPage": Pagination.page,
                                "pageSize": 8
                            },
                        },
                        "institute": $("#institute").val(),
                        "major": $("#major").val(),
                        "projectSymbol": $("#projectSymbol").val(),
                        "projectName": $("#projectName").val(),
                        "studentId": $("#userId").val(),
                        "studentName": $("#name").val()
                    }),
                    contentType: 'application/json',
                    type: 'post',
                    dataType: 'json',
                    success: function (data) {
                        //总页数设置
                        Pagination.Init(document.getElementById('pagination'), {
                            size: data.data.pages, // pages size
                            page: Pagination.page,  // selected page
                            step: 3   // pages before and after current
                        });
                        $("#yustb2 tr:not(:first)").remove();//清空
                        for (i in data.data.records) {
                            var tk;
                            lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">下载</button>',
                                tk = '<td>'  +Number(i+1)+ '</td>'+'<td>' + data.data.records[i].projectSymbol + '</td>'+'<td>' + data.data.records[i].projectName+ '</td>'+'<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].institute + '</td>' + '<td>' + data.data.records[i].major + '</td>'+'<td>'+lookcontent+'</td>'
                            $("#yustb2").append('<tr>' + tk + '</tr>'),
                                console.log(Number(i+1))
                        }
                    }
                })
      }