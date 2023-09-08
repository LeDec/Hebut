//body初始化事件
//body标签加οnlοad='方法名()'
//function 方法名(){
//   初始化操作
//   }
function init(){
    //获取显示器宽度
    var widths=window.screen.width;
    //body的宽与显示器一致(浏览器最大化的时候可以用)
    document.body.style.width=widths+'px';
}
//监听浏览器的宽度变化
window.onresize=function(){
    //获取浏览器当前的宽度
    var innerwidths =window.innerWidth;
    //获取当前显示器的宽度(一般情况下显示器的宽度是不会变得的)
    var widths=window.screen.width;
   
    //如果显示器宽度等于浏览器宽度那么这个情况大多数是最大化的时候
    if(innerwidths==widths){
        //所以直接把显示器的宽度赋给body
        document.body.style.width=widths+'px';
    }
    
    //如果浏览器的宽度不等于显示器的宽度时有两种情况
    // 第一种就是 缩小
    // 第二种就是 放大
    if(innerwidths!=widths){
        //先处理缩小的，因为浏览器在缩小的时候它的宽度会一直累加，这个时候浏览器宽度就大于显示器宽度了
        if (innerwidths>widths) {
            //这里给body赋值是模拟缩小页面的操作，幅度不能太大，要不然有点奇怪
            document.body.style.width=innerwidths*10%-widths*10%+'px';
            return;
        }

        //放大的操作就重新给body宽度赋显示器的宽度
        document.body.style.width=widths+'px';
    }
}