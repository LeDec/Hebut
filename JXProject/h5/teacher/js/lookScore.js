$(function () {
    $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/score/checkAllScore',
        data: JSON.stringify({
            "batchId": localStorage.getItem('batchId'),
            "checkRequest": {
                "commonRequest": {
                    "token": localStorage.getItem('token'),
                    "userId": localStorage.getItem('userId')
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
            for(i in data.data.records){
                tk = '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' + '<td>' + data.data.records[i].usualScore + '</td>'+'<td>' + data.data.records[i].defenseScore + '</td>'+'<td>' + data.data.records[i].otherScore + '</td>'+'<td>' + data.data.records[i].score + '</td>'
                $("#yustb2").append('<tr>' + tk + '</tr>')
            }
        }
    })
})