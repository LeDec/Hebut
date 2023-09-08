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