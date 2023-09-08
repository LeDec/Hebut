

//个人信息加载
$(function () {

      $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/user/Information',
        data: JSON.stringify({
          token: localStorage.getItem('token'),
          userId: localStorage.getItem('userId'),
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
          if (data.code == 0) {
            document.getElementById("userName").innerText =  data.data.userName;
            document.getElementById("User_Name").innerText =  data.data.userName;
            document.getElementById("name").innerText =  data.data.userName;
            document.getElementById("User_Num").innerText =  data.data.userId;
            document.getElementById("email").innerText =  data.data.email;
            document.getElementById("User_Email").innerText =  data.data.email;
            document.getElementById("User-Email").value =  data.data.email;
            document.getElementById("User_Institute").innerText =  data.data.institute;
            console.log(data.data.type);
            if(data.data.type=="0")
            document.getElementById("usertype").innerText = "身份：系统管理员";
             else if(data.data.type=="1")
            document.getElementById("usertype").innerText = "身份：管理员";
            else if(data.data.type=="2")
            document.getElementById("usertype").innerText = "身份：班主任";
            else if(data.data.type=="3")
            document.getElementById("usertype").innerText = "身份：指导老师";
            else if(data.data.type=="4")
            document.getElementById("usertype").innerText = "身份：专家";
            else if(data.data.type=="5")
            document.getElementById("usertype").innerText = "身份：学生";
            else if(data.data.type=="6")
            document.getElementById("usertype").innerText = "身份：企业";
          } else {
            return false;
          }
        },
        error: function (e) {
        }
      });
    })

//显示未读消息数量
 function read_news() {
 $.ajax({
         url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/inform/checkWaitReadCount',
         data: JSON.stringify({
           token: localStorage.getItem('token'),
           userId: localStorage.getItem('userId')
         }),
         contentType: 'application/json',
         type: 'post',
         dataType: 'json',
         success: function (data) {
         if(data.code==0)
         {
         document.getElementById("new_num").innerText =  data.data;
         document.getElementById("new-num").innerText = "未读："+data.data;
         }
         else
         alert(data.msg);
         },
         error: function (e) {
         }
       });
 }


//消息加载
 function load_news() {
            $.ajax({
                url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/inform/checkInform',
                data: JSON.stringify({
                 "commonRequest": {
                                token: localStorage.getItem('token'),
                                userId: localStorage.getItem('userId'),
                                  },
                   "pageRequest": {
                                "currentPage": 1,
                                "pageSize": 800
                                   },
                }),
                contentType: 'application/json',
                type: 'post',
                dataType: 'json',
                success: function (data) {
                 $("#new_list").empty();
                    for (i in data.data.records)
                    {
                    var inf_id=data.data.records[i].informId;
                    if(data.data.records[i].isRead==0)
                    {
                    $("#new_list").append(
                                           '<li class="py-2 mb-1 border-bottom">'
                                           +'<a id='+inf_id+' href="javascript:void(0);" onclick="message(this)" data-bs-toggle="modal" data-bs-target="#my_inform" class="d-flex">'
                                           +'<div class="flex-fill ms-2">'
                                           +'<p class="d-flex justify-content-between mb-0 ">'
                                           +'<span class="font-weight-bold">'
                                           +data.data.records[i].sender+'</span>'
                                           +'<small>'+data.data.records[i].informTime+'</small>'
                                           +'</p>'+'<span>'+data.data.records[i].title+'<br>'+'<span class="badge bg-success">'+'未读'+'</span>'+'</span>'+'</div>'+'</a>'+'</li>'
                                           );

                    }
                    else
                    $("#new_list").append(
                                          '<li class="py-2 mb-1 border-bottom">'
                                          +'<a id='+inf_id+' href="javascript:void(0);" onclick="message(this)" data-bs-toggle="modal" data-bs-target="#my_inform" class="d-flex">'
                                          +'<div class="flex-fill ms-2">'
                                          +'<p class="d-flex justify-content-between mb-0 ">'
                                          +'<span class="font-weight-bold">'
                                          +data.data.records[i].sender+'</span>'
                                          +'<small>'+data.data.records[i].informTime+'</small>'
                                          +'</p>'+'<span>'+data.data.records[i].title+'<br>'+'</span>'+'</div>'+'</a>'+'</li>'
                                          );
                    }



                }
            })
        }

//显示消息
$(function (){
read_news();
load_news();
})

//查看消息细则
function message(informId){
 localStorage.setItem('informId', informId.id);
 $.ajax({
                 url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/inform/scanInform',
                 data: JSON.stringify({
                 "commonRequest": {
                                  token: localStorage.getItem('token'),
                                  userId: localStorage.getItem('userId'),
                                  },
                     informId: localStorage.getItem('informId'),
                 }),
                 contentType: 'application/json',
                 type: 'post',
                 dataType: 'json',
                 success: function (data) {
                  $ ("#title").empty () ;
                  $ ("#content").empty () ;
                     if (data.code == 0) {
                         data.data.information = data.data.information.replace(/brhh/g, "<br>");
                         data.data.information = data.data.information.replace(/nbspp/g, "&nbsp&nbsp&nbsp&nbsp");
                         $("#title").append(data.data.title);
                         $("#content").append(data.data.information);
                         if(data.data.attachment[0]){
                          var myParent = document.getElementById("new");
      			         var myImage = document.createElement("img");
 			             myImage.src = "/usr/local/nginx/html/resource/img/attachment/"+data.data.attachment[0];
 			             myParent.appendChild(myImage);
 			             myImage.style.width = "260px";
 			             myImage.style.height = "260px";
 			             var  tip = document.createElement("span");
 			             tip.innerText="(图片右击可保存后查看)";
 			             myParent.appendChild(tip);
                         }
                     } else {

                         return false;
                     }
                     read_news();
                 },
                 error: function (e) {
                 }
             });

}


//已读所有消息
function ViewAll() {
$.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/inform/oneClickRead',
        data: JSON.stringify({
          token: localStorage.getItem('token'),
          userId: localStorage.getItem('userId')
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
        if(data.code==0)
        {
        alert("全部消息已读");
        window.location.reload()
        }
        else
        alert(data.msg);
        },
        error: function (e) {
        }
      });

}


//修改信息
function reinf() {

      $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/user/changeInformation',
        data: JSON.stringify({
          commonRequest:{
          token: localStorage.getItem('token'),
          userId: localStorage.getItem('userId'),
          },
          newEMail:$("#User-Email").val(),
          newInstitute:$("#User-Institute").find("option:selected").val()
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
        alert("修改成功！");
        window.location.href = "home.html";
        },
        error: function (e) {
        }
      });
    }


//修改密码
 function revise() {
        var pwdRegex = new RegExp('(?=.*[0-9])(?=.*[a-zA-Z]).{8,30}');
        if (!pwdRegex.test($("#newpw").val())) {
        alert("您的密码复杂度太低（密码中必须包含字母、数字,且长度必须为8到30字符）！");
        }
        else{
         $.ajax({
        url: 'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/user/changePassword',
        data: JSON.stringify({
          commonRequest:{
          token: localStorage.getItem('token'),
          userId: localStorage.getItem('userId'),
          },
          newPassword:$("#newpw").val(),
        }),
        contentType: 'application/json',
        type: 'post',
        dataType: 'json',
        success: function (data) {
        if(data.code==0)
        {
        alert("修改成功！");
        window.location.href = "../login v2.0/Home.html";
        }
        else
        alert(data.msg);
        },
        error: function (e) {
        alert('系统异常');
        }
      });
        }

    }

//根据选择学院自动加载专业
 function getIntype(){
 $("#User-Major").empty();
    $.ajax({
        url:'http://'+localStorage.getItem('IP')+':'+localStorage.getItem('port')+'/project/institute-major-relation/showMajor',
         data: JSON.stringify({
                         "instituteId": $("#User-Institute").find("option:selected").val()
}),
  contentType: 'application/json',
                type: 'post',
                dataType: 'json',
        success:function(data){
             for (i in data.data){
                var option=document.createElement("option");
                $("#User-Major").append(('<option value='+data.data[i].majorId+'>'+data.data[i].majorName+'</option>'));
            }
        },
        error:function(data){
            console.log(data);
        }
    });
}


if (typeof jQuery === "undefined") {
    throw new Error("jQuery plugins need to be before this file");
}

$(function() {
    "use strict";
    // main sidebar toggle js
    $('.menu-toggle').on('click', function () {
        $('.sidebar').toggleClass('open');
        $('.open').removeClass('sidebar-mini');
    });

    // layout a sidebar mini version
    $('.sidebar-mini-btn').on('click', function () {
        $('.sidebar').toggleClass('sidebar-mini');
        $('.sidebar-mini').removeClass('open');
    });

    // Dropdown scroll hide using table responsive
    
    $('.table-responsive').on('show.bs.dropdown', function () {
        $('.table-responsive').css( "overflow", "inherit" );
    });

    $('.table-responsive').on('hide.bs.dropdown', function () {
            $('.table-responsive').css( "overflow", "auto" );
    })


    // end
});





 