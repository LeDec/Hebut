
var userId=[];
var vals=[];
$.ajax({
    url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/score/checkRemainScore',
    data: JSON.stringify({
        "batchId": localStorage.getItem('batchId'),
        "checkRequest": {
            "commonRequest": {
                token: localStorage.getItem('token'),
                userId: localStorage.getItem('userId'),
            },
            "pageRequest": {
                "currentPage": 1,
                "pageSize": 5
            },
        }
    }),
    contentType: 'application/json',
    type: 'post',
    dataType: 'json',
    success: function (data) {
        var num=1
        if(data.code!=0){
            alert(data.msg)
        }else{

            for (i in data.data.records) {
                userId.push(data.data.records[i].userId)
                var tk;
                input = '<input type="text" class="score" style="width:80px">',
                    tk ='<td>'+num+'</td>'+'<td class="projectId">' + data.data.records[i].projectSymbol + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].userName + '</td>'+'<td>'+input+'</td>'
                $("#yustb2").append('<tr>' + tk + '</tr>')
                num++
            }
        }
    }

})
//给定平时成绩
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