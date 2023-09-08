//全选控制所有子框
function checkAll(ckAll){
    var cks=document.getElementsByName("ck");
    for(var i=0;i<cks.length;i++){
        cks[i].checked=ckAll.checked;
    }
}
//通过子框控制全选
window.function()
{
    //给每一个子框绑定一个点击事件，每次触发都判断是否全选
    var cks = document.getElementsByName("ck");
    for(var i=0; i<cks.length; i++){
        cks[i].onclick=function(){
            //循环每一个按钮的值，判断是否选中
            var flag = true;
            var ckArr = document.getElementsByName("ck");
            for (var j=0; j<ckArr.length; j++) {
                //有未选中即终止循环，修改标记
                if(!ckArr[j].checked){
                    flag = false;
                    break;
                }
            }
            //通过标记控制全选
            if(flag){
                document.getElementById("ckAll").checked=true;
            }else{
                document.getElementById("ckAll").checked=false;
            }
        }
    }
}
