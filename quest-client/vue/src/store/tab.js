export default {
    state: {
        isCollapse:false,   //控制菜单展开还是收起
        tabListresAdmin:[
            {
                path:'/resadmin/homeresAdmin',
                name:'homeresAdmin',
                label:'首页',
                icon:'el-icon-s-home',
                url:'Home/HomeresAdmin'
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
            if(val !== 'homeresAdmin'){
                const index = state.tabListresAdmin.findIndex(item  => item.name === val.name)
                //如果不存在
                if(index === -1 ){
                    state.tabListresAdmin.push(val)
                }
            }
        },
        //删除指定的tag数据
        closeTag(state,item){
            // console.log(val);
            //删除图标
            const index = state.tabListresAdmin.findIndex(val=>val.name===item.name)
            state.tabListresAdmin.splice(index,1)

        }
    }
}