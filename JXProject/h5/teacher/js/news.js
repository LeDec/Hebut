$(function () {$.ajax({
    url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/inform/checkInform',
    data: JSON.stringify({
        "commonRequest": {
            token: localStorage.getItem('token'),
            userId: localStorage.getItem('userId'),
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
        if(data.data.records[0].isRead==0){
            var element = document.getElementById('XiaoXi');
            element.src = "图片/消息2.png";
        }
    }
})

})
