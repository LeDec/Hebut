
//垂直导航栏
/*当整个html文档加载完成之后再执行此函数*/
$(function () {
    /*隐藏所有子标题*/
    $(".nav-menu").each(function () {
        $(this).children(".nav-content").hide();
    });
    /*给所有的菜单项主标题绑定事件*/
    $(".nav-title").each(function () {
        $(this).click(function () {
            //当我点击主标题时，其子标题区展示出来
            //获取当前点击主标题下的子标题内容区

            var conEle = $(this).parents(".nav-menu").children(".nav-content");
            //判断conEle对象的display属性值是否为none
            if (conEle.css("display") != "none") {
                //隐藏
                conEle.hide(500);//延时隐藏500毫秒
            }
            else {
                //当要展开某一个子标题列表时需要其他所有子标题列表关闭
                var navEle = conEle.parents(".nav-menu");
                //找到除自己之外的其他所有兄弟节点
                var menuEle = navEle.siblings();
                menuEle.each(function () {
                    $(this).children(".nav-content").hide(500);
                });

                //展示
                conEle.show(500);//延时展示500毫秒
            }
        });
    });
});
$(document).ready(function () {
    $("#firstpaneDiv .menu_nva:eq(0)").show();
    $("#firstpaneDiv h3.menu_head").click(function () {
        $(this).addClass("current").next("div.menu_nva").slideToggle(200).siblings("div.menu_nva").slideUp("slow");
        $(this).siblings().removeClass("current");
    });
    $("#secondpane .menu_nva:eq(0)").show();
    $("#secondpane h3.menu_head").mouseover(function () {
        $(this).addClass("current").next("div.menu_nva").slideDown(400).siblings("div.menu_nva").slideUp("slow");
        $(this).siblings().removeClass("current");
    });
});