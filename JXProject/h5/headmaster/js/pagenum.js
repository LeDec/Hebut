var Pagination = {

    code: '',

    // --------------------
    // Utility
    // --------------------

    // converting initialize data
    Extend: function(data) {
        data = data || {};
        Pagination.size = data.size || 300;
        Pagination.page = data.page || 1;
        Pagination.step = data.step || 3;
    },

    // add pages by number (from [s] to [f])
    Add: function(s, f) {
        for (var i = s; i < f; i++) {
            Pagination.code += '<a>' + i + '</a>';
        }
    },

    // add last page with separator
    Last: function() {
        Pagination.code += '<i>...</i><a>' + Pagination.size + '</a>';
    },

    // add first page with separator
    First: function() {
        Pagination.code += '<a>1</a><i>...</i>';
    },

// add start page with separator
    Start: function() {
        Pagination.code += '<a>1</a><i>...</i>';
    },


    // --------------------
    // Handlers
    // --------------------

    // change page
    Click: function() {
        Pagination.page = +this.innerHTML;
        Pagination.Start();
        $("#yustb2 tr:not(:first)").remove();//清空
        $(function () {
            $.ajax({
                url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/checkAuditEnroll',
                data: JSON.stringify({
                    "checkRequest": {
                        "commonRequest": {
                            "token": localStorage.getItem('token'),
                            "userId": localStorage.getItem('userId')
                        },
                        "pageRequest": {
                            "currentPage": 1,
                            "pageSize": 5
                        },
                    },
                }),
                contentType: 'application/json',
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    for (i in data.data.records) {
                        var tk;
                        lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">查看</button>',
                            isPassStr='<button onclick="isPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">√</button>'
                        noPassStr='<button onclick="noPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">×</button>'
                        tk = '<td>' + data.data.records[i].projectId + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ isPassStr + noPassStr +'</td>'
                        $("#yustb2").append('<tr>' + tk + '</tr>')

                    }

                }
            })
        })
    },

    // previous page
    Prev: function() {
        Pagination.page--;
        if (Pagination.page < 1) {
            Pagination.page = 1;
        }
        Pagination.Start();
        $("#yustb2 tr:not(:first)").remove();//清空
        $.ajax({
            url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/checkAuditEnroll',
            data: JSON.stringify({
                "checkRequest": {
                    "commonRequest": {
                        "token": localStorage.getItem('token'),
                        "userId": localStorage.getItem('userId')
                    },
                    "pageRequest": {
                        "currentPage": 2,
                        "pageSize": 5
                    },
                },
            }),
            contentType: 'application/json',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                for (i in data.data.records) {
                    var tk;
                    lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">查看</button>',
                        isPassStr='<button onclick="isPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">√</button>'
                    noPassStr='<button onclick="noPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">×</button>'
                    tk = '<td>' + data.data.records[i].projectId + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ isPassStr + noPassStr +'</td>'
                    $("#yustb2").append('<tr>' + tk + '</tr>')

                }

            }
        })


    },

    // next page
    Next: function() {
        Pagination.page++;
        if (Pagination.page > Pagination.size) {
            Pagination.size++;
        }
        Pagination.Start();
        $("#yustb2 tr:not(:first)").remove();//清空
        $.ajax({
            url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/application/checkAuditEnroll',
            data: JSON.stringify({
                "checkRequest": {
                    "commonRequest": {
                        "token": localStorage.getItem('token'),
                        "userId": localStorage.getItem('userId')
                    },
                    "pageRequest": {
                        "currentPage": 3,
                        "pageSize": 5
                    },
                },
            }),
            contentType: 'application/json',
            type: 'post',
            dataType: 'json',
            success: function (data) {
                for (i in data.data.records) {
                    var tk;
                    lookcontent = '<button onclick="lookContent(this)" class="loookContent" value="'+data.data.records[i].enrollId+'">查看</button>',
                        isPassStr='<button onclick="isPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">√</button>'
                    noPassStr='<button onclick="noPass(this)" class="isPass" value="'+data.data.records[i].enrollId+'">×</button>'
                    tk = '<td>' + data.data.records[i].projectId + '</td>' + '<td>' + data.data.records[i].projectName + '</td>' + '<td>' + data.data.records[i].userName + '</td>' + '<td>' + data.data.records[i].userId + '</td>' +'<td>' + lookcontent + '</td>' + '</td>' + '<td>'+ data.data.records[i].institute +'</td>'+'<td>'+ isPassStr + noPassStr +'</td>'
                    $("#yustb2").append('<tr>' + tk + '</tr>')

                }

            }
        })


    },



    // --------------------
    // Script
    // --------------------

    // binding pages
    Bind: function() {
        var a = Pagination.e.getElementsByTagName('a');
        for (var i = 0; i < a.length; i++) {
            if (+a[i].innerHTML === Pagination.page) a[i].className = 'current';
            a[i].addEventListener('click', Pagination.Click, false);
        }
    },

    // write pagination
    Finish: function() {
        Pagination.e.innerHTML = Pagination.code;
        Pagination.code = '';
        Pagination.Bind();
    },

    // find pagination type
    Start: function() {
        if (Pagination.size < Pagination.step * 2 + 6) {
            Pagination.Add(1, Pagination.size + 1);
        }
        else if (Pagination.page < Pagination.step * 2 + 1) {
            Pagination.Add(1, Pagination.step * 2 + 4);
            Pagination.Last();
        }
        else if (Pagination.page > Pagination.size - Pagination.step * 2) {
            Pagination.First();
            Pagination.Add(Pagination.size - Pagination.step * 2 - 2, Pagination.size + 1);
        }
        else {
            Pagination.First();
            Pagination.Add(Pagination.page - Pagination.step, Pagination.page + Pagination.step + 1);
            Pagination.Last();
        }
        Pagination.Finish();
    },



    // --------------------
    // Initialization
    // --------------------

    // binding buttons
    Buttons: function(e) {
        var nav = e.getElementsByTagName('a');
        nav[0].addEventListener('click', Pagination.Prev, false);
        nav[1].addEventListener('click', Pagination.Next, false);
    },

    // create skeleton
    Create: function(e) {

        var html = [
            '<a>&#9668;</a>', // previous button
            '<span></span>',  // pagination container
            '<a>&#9658;</a>'  // next button
        ];

        e.innerHTML = html.join('');
        Pagination.e = e.getElementsByTagName('span')[0];
        Pagination.Buttons(e);
    },

    // init
    Init: function(e, data) {
        Pagination.Extend(data);
        Pagination.Create(e);
        Pagination.Start();
    }
};
var init = function() {
    Pagination.Init(document.getElementById('pagination'), {
        size: 30, // pages size
        page: 1,  // selected page
        step: 3   // pages before and after current
    });
};

document.addEventListener('DOMContentLoaded', init, false);
