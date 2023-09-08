import Vue from 'vue'
import VueRouter from 'vue-router'

//管理员
import MainAdmin from "@/views/admin/MainAdmin";
import homeAdmin from "@/views/admin/homeAdmin";
import userInfo from "@/views/admin/userInfo";
// import userTask from "@/views/admin/userTask";
import dungeonInfo from "@/views/admin/dungeonInfo";
import dungeonIntegration from "@/views/admin/dungeonIntegration";
import notifyInfo from "@/views/admin/notifyInfo";
//登录
import Login from "@/views/myLogin";

Vue.use(VueRouter)
const routes =[
    {
        path:'/login',
        component: Login,
        name:'login'
    },
    {  //管理员
        path:'/admin',
        component: MainAdmin,
        redirect:'/admin/homeAdmin',
        children:[
            //子路由
            //管理员
            {path:'homeAdmin',name:'homeAdmin',component:homeAdmin},
            {path:'userInfo',name:'userInfo',component:userInfo},
            // {path:'userTask',name:'userTask',component:userTask},
            {path:'dungeonInfo',name:'dungeonInfo',component:dungeonInfo},
            {path:'dungeonIntegration',name:'dungeonIntegration',component:dungeonIntegration},
            {path:'notifyInfo',name:'notifyInfo',component:notifyInfo}
        ]
    }
]
const router = new VueRouter({
    routes // (缩写) 相当于 routes: routes
})

export default router