/*
//alert自定义设置
window.alert = function(msg, callback) {
    var div = document.createElement("div");
    div.innerHTML = "<style type=\"text/css\">"
            + ".nbaMask { position: fixed; z-index: 1000; top: 0; right: 0; left: 0; bottom: 0; background: rgba(0, 0, 0, 0.5); }                                                                                                                                                                       "
            + ".nbaMaskTransparent { position: fixed; z-index: 1000; top: 0; right: 0; left: 0; bottom: 0; }                                                                                                                                                                                            "
            + ".nbaDialog { position: fixed; z-index: 5000; width: 80%; max-width: 300px; top: 50%; left: 50%; -webkit-transform: translate(-50%, -50%); transform: translate(-50%, -50%); background-color: #fff; text-align: center; border-radius: 8px; overflow: hidden; opacity: 1; color: white; }"
            + ".nbaDialog .nbaDialogHd { padding: .2rem .27rem .08rem .27rem; }                                                                                                                                                                                                                         "
            + ".nbaDialog .nbaDialogHd .nbaDialogTitle { font-size: 17px; font-weight: 400; }                                                                                                                                                                                                           "
            + ".nbaDialog .nbaDialogBd { padding: 0 .27rem; font-size: 15px; line-height: 1.3; word-wrap: break-word; word-break: break-all; color: #000000; }                                                                                                                                          "
            + ".nbaDialog .nbaDialogFt { position: relative; line-height: 48px; font-size: 17px; display: -webkit-box; display: -webkit-flex; display: flex; }                                                                                                                                          "
            + ".nbaDialog .nbaDialogFt:after { content: \" \"; position: absolute; left: 0; top: 0; right: 0; height: 1px; border-top: 1px solid #e6e6e6; color: #e6e6e6; -webkit-transform-origin: 0 0; transform-origin: 0 0; -webkit-transform: scaleY(0.5); transform: scaleY(0.5); }               "
            + ".nbaDialog .nbaDialogBtn { display: block; -webkit-box-flex: 1; -webkit-flex: 1; flex: 1; color: #09BB07; text-decoration: none; -webkit-tap-highlight-color: transparent; position: relative; margin-bottom: 0; }                                                                       "
            + ".nbaDialog .nbaDialogBtn:after { content: \" \"; position: absolute; left: 0; top: 0; width: 1px; bottom: 0; border-left: 1px solid #e6e6e6; color: #e6e6e6; -webkit-transform-origin: 0 0; transform-origin: 0 0; -webkit-transform: scaleX(0.5); transform: scaleX(0.5); }             "
            + ".nbaDialog a { text-decoration: none; -webkit-tap-highlight-color: transparent; }"
            + "</style>"
            + "<div id=\"dialogs2\" style=\"display: none\">"
            + "<div class=\"nbaMask\"></div>"
            + "<div class=\"nbaDialog\">"
            + "    <div class=\"nbaDialogHd\">"
            + "        <strong class=\"nbaDialogTitle\"></strong>"
            + "    </div>"
            + "    <div class=\"nbaDialogBd\" id=\"dialog_msg2\">弹窗内容，告知当前状态、信息和解决方法，描述文字尽量控制在三行内</div>"
            + "    <div class=\"nbaDialogHd\">"
            + "        <strong class=\"nbaDialogTitle\"></strong>"
            + "    </div>"
            + "    <div class=\"nbaDialogFt\">"
            + "        <a href=\"javascript:;\" class=\"nbaDialogBtn nbaDialogBtnPrimary\" id=\"dialog_ok2\">确定</a>"
            + "    </div></div></div>";
    document.body.appendChild(div);

    var dialogs2 = document.getElementById("dialogs2");
    dialogs2.style.display = 'block';

    var dialog_msg2 = document.getElementById("dialog_msg2");
    dialog_msg2.innerHTML = msg;

    // var dialog_cancel = document.getElementById("dialog_cancel");
    // dialog_cancel.onclick = function() {
    // dialogs2.style.display = 'none';
    // };
    var dialog_ok2 = document.getElementById("dialog_ok2");
    dialog_ok2.onclick = function() {
        dialogs2.style.display = 'none';
        callback();
    };
};
*/
//当下批次
function nowJx(){
$.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/batch/nowBatch',
        data: JSON.stringify({
            "commonRequest": {
                "token": localStorage.getItem('token'),
                "userId": localStorage.getItem('userId')
            }
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
        if(data.code==0){
                    if(data.data.batchStage=="征集阶段")
                    window.location.href="J_NowJxProject_Stage1.html";
                    else if(data.data.batchStage=="报名阶段")
                    window.location.href="J_NowJxProject_Stage2.html";
                    else if(data.data.batchStage=="项目进行中期阶段")
                    window.location.href="J_NowJxProject_Stage3.html";
                    else if(data.data.batchStage=="项目进行答辩阶段")
                    window.location.href="J_NowJxProject_Stage4.html";
                    else if(data.data.batchStage=="匠心班未开启")
                    window.location.href="J_NowJxProject_Stage0.html";
                    else
                    window.location.href="J_NowJxProject_Stage5.html";
        }
          else
          {
            alert(data.msg);
            window.location.href="J_NowJxProject_Stage4_BeginNew.html";
          }

        }

    })
}

//分页条
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
        if(flag==1)
           Search();
        else
           Func();
    },

    // previous page
    Prev: function() {
        Pagination.page--;
        if (Pagination.page < 1) {
            Pagination.page = 1;
        }
        Pagination.Start();
       if(flag==1)
           Search();
       else
           Func();
    },

    // next page
    Next: function() {
        Pagination.page++;
        if (Pagination.page > Pagination.size) {
            Pagination.page--;
        }
        Pagination.Start();
        if(flag==1)
        Search();
        else
        Func();
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
/* * * * * * * * * * * * * * * * *
* Initialization
* * * * * * * * * * * * * * * * */

var init = function() {
    Pagination.Init(document.getElementById('pagination'), {
        size: 10, // pages size
        page: 1,  // selected page
        step: 3   // pages before and after current
    });
};
document.addEventListener('DOMContentLoaded', init, false);
//分页条加载完成