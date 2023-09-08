export default {
    state: {
        isCollapse:false,   //控制菜单展开还是收起
        tabListAdmin:[
            {
                path:'/admin/homeAdmin',
                name:'homeAdmin',
                label:'首页',
                icon:'el-icon-s-home',
                url:'Home/homeAdmin'
            },
        ]//管理员面包屑数据
    },
    mutations:{
        collapseMenu(state){
            state.isCollapse = !state.isCollapse
        },
        //更新面包屑数据
        selectMenu(state,val){
            // console.log(val);
            if(val !== 'homeAdmin'){
                const index = state.tabListAdmin.findIndex(item  => item.name === val.name)
                //如果不存在
                if(index === -1 ){
                    state.tabListAdmin.push(val)
                }
            }
        },
        //删除指定的tag数据
        closeTag(state,item){
            // console.log(val);
            //删除图标
            const index = state.tabListAdmin.findIndex(val=>val.name===item.name)
            state.tabListAdmin.splice(index,1)

        }
    }
}