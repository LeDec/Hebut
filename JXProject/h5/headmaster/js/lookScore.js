var flag=0;
$(function () {
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/score/checkAllScore',
        data: JSON.stringify({
        "batchId":localStorage.getItem('batchId'),
          "checkRequest": {
            "commonRequest": {
                "token": localStorage.getItem('token'),
                "userId": localStorage.getItem('userId')
            },
            "pageRequest": {
                "currentPage": 1,
                "pageSize": 15
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
                                    page:1,// selected page
                                    step: 3   // pages before and after current
                                });
        var num=1
            for(i in data.data.records){
                if(data.data.records[i].usualScore!=-1){
                    var usualScore=data.data.records[i].usualScore
                }else{
                    var usualScore=""
                }
                if(data.data.records[i].defenseScore!=-1){
                    var defenseScore=data.data.records[i].defenseScore
                }else{
                    var defenseScore=""
                }
                if(data.data.records[i].activityScore!=-1){
                    var activityScore=data.data.records[i].activityScore
                }else{
                    var activityScore=""
                }
                if(data.data.records[i].score!=-1){
                    var score=data.data.records[i].score
                }else{
                    var score=""
                }
                tk = '<td>' + num + '</td>'+ '<td>' + data.data.records[i].projectName + '</td>'+'<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + defenseScore + '</td>'+'<td>' + usualScore + '</td>'+'<td>' + activityScore + '</td>'+'<td>' + score + '</td>'
                $("#yustb2").append('<tr>' + tk + '</tr>')
                num++
            }
        }
    })
})
function Func() {
        $("#yustb2 tr:not(:first)").remove();//清空
     $.ajax({
           url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/score/checkAllScore',
           data: JSON.stringify({
           "batchId":localStorage.getItem('batchId'),
             "checkRequest": {
               "commonRequest": {
                   "token": localStorage.getItem('token'),
                   "userId": localStorage.getItem('userId')
               },
               "pageRequest": {
                   "currentPage": Pagination.page,
                   "pageSize": 12
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
           var num=1
               for(i in data.data.records){
                   if(data.data.records[i].usualScore!=-1){
                       var usualScore=data.data.records[i].usualScore
                   }else{
                       var usualScore=""
                   }
                   if(data.data.records[i].defenseScore!=-1){
                       var defenseScore=data.data.records[i].defenseScore
                   }else{
                       var defenseScore=""
                   }
                   if(data.data.records[i].activityScore!=-1){
                       var activityScore=data.data.records[i].activityScore
                   }else{
                       var activityScore=""
                   }
                   if(data.data.records[i].score!=-1){
                       var score=data.data.records[i].score
                   }else{
                       var score=""
                   }
                   tk = '<td>' + num + '</td>'+ '<td>' + data.data.records[i].projectName + '</td>'+'<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + defenseScore + '</td>'+'<td>' + usualScore + '</td>'+'<td>' + activityScore + '</td>'+'<td>' + score + '</td>'
                   $("#yustb2").append('<tr>' + tk + '</tr>')
                   num++
               }
           }
       })
    }