//获取当前批次
     $(function () {
            $.ajax({
                url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/batch/nowBatch',
                data: JSON.stringify({
                 "commonRequest": {
                                token: localStorage.getItem('token'),
                                userId: localStorage.getItem('userId'),
                                  }
                }),
                contentType: 'application/json',
                type: 'post',
                dataType: 'json',
                success: function (data) {
               localStorage.setItem('batchId',data.data.batchId);
                }
            })
        })


$.ajax({
    url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/activity/checkActivity',
    data: JSON.stringify({
        "batchId": localStorage.getItem('batchId'),
        checkRequest:{
         "commonRequest": {
                    token: localStorage.getItem('token'),
                    userId: localStorage.getItem('userId'),
                },
                "pageRequest": {
                    "currentPage": 1,
                    "pageSize": 99
                }
        }

    }),
    contentType: 'application/json',
    type: 'post',
    dataType: 'json',
    success: function (data) {
            localStorage.setItem("totalActivity",data.data.total)
        }
})

var userId=[];
var vals=[];
$.ajax({
    url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/score/checkAllScore',
    data: JSON.stringify({
         "batchId": localStorage.getItem('batchId'),
        "checkRequest": {
            "commonRequest": {
                token: localStorage.getItem('token'),
                userId: localStorage.getItem('userId'),
            },
            "pageRequest": {
                "currentPage": 1,
                "pageSize": 10
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
                        page:  1,// selected page
                        step: 3   // pages before and after current
                    });
        var num=1;
        if(data.code!=0){
            alert(data.msg)
        }else{

            for (i in data.data.records) {
                userId.push(data.data.records[i].userId)
                var tk;
                if(data.data.records[i].activityScore==-1){
                data.data.records[i].activityScore="";
                input = '<input type="text" class="score" style="width:80px" >';
                }
                else
                input = '<input type="text" class="score" style="width:80px"  value="'+data.data.records[i].activityScore/0.1+'" >';
                projectId='<button  class="number"  value="'+data.data.records[i].projectId+'" style="width:0.00001px"></button>',
                tk ='<td>'+num+'</td>'+'<td class="projectId">' + data.data.records[i].projectSymbol + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>'+'<td>'+data.data.records[i].times+'/'+localStorage.getItem('totalActivity')+'</td>'+'<td>'+input+'</td>'+'<td>'+projectId+'</td>'
                $("#yustb2").append('<tr>' + tk + '</tr>')
                num++
            }
        }
    }

})
function Func() {
        $("#yustb2 tr:not(:first)").remove();//清空
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/score/checkAllScore',
        data: JSON.stringify({
             "batchId": localStorage.getItem('batchId'),
            "checkRequest": {
                "commonRequest": {
                    token: localStorage.getItem('token'),
                    userId: localStorage.getItem('userId'),
                },
                "pageRequest": {
                    "currentPage":Pagination.page,
                    "pageSize": 9
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
                            page: Pagination.page,// selected page
                            step: 3   // pages before and after current
                        });
            var num=1;
            if(data.code!=0){
                alert(data.msg)
            }else{

                for (i in data.data.records) {
                    userId.push(data.data.records[i].userId)
                    var tk;
                    if(data.data.records[i].activityScore==-1){
                    data.data.records[i].activityScore="";
                    input = '<input type="text" class="score" style="width:80px" >';
                    }
                    else
                    input = '<input type="text" class="score" style="width:80px"  value="'+data.data.records[i].activityScore/0.1+'" >';
                    projectId='<button  class="number"  value="'+data.data.records[i].projectId+'" style="width:0.00001px"></button>',
                    tk ='<td>'+num+'</td>'+'<td class="projectId">' + data.data.records[i].projectSymbol + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>'+'<td>'+data.data.records[i].times+'/'+localStorage.getItem('totalActivity')+'</td>'+'<td>'+input+'</td>'+'<td>'+projectId+'</td>'
                    $("#yustb2").append('<tr>' + tk + '</tr>')
                    num++
                }
            }
        }

    })
    }
//给定其他成绩
function give(btn){
    var score=$(btn).parents("tr").find(".score").val();
    var tr = btn.parentNode.parentNode;
    var cell1 = tr.cells[0];
    var cell2 = tr.cells[2];
    var projectId = cell1.innerHTML;
    var userId = cell2.innerHTML;
    console.log(score)
    console.log(projectId)
    console.log(userId)
    if(score!=""&&parseFloat(score).toString() != "NaN"){
        $.ajax({
            url:'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/score/giveScore',
            data: JSON.stringify({
                "commonRequest": {
                    token: localStorage.getItem('token'),
                    userId: localStorage.getItem('userId'),
                },
                "projectId": projectId,
                "score":score,
                "userId":userId
            }),
            contentType: 'application/json',
            type: 'post',
            dataType: 'json',
            success:function(data){
                location.reload();
            },
            error:function(data){
            }
        })

        alert("给定成功")
        location.reload();
    }
}