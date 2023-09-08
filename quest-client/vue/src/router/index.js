import Vue from 'vue'
import VueRouter from 'vue-router'
//登录
import Login from "@/views/Login";
Vue.use(VueRouter)
//系统管理员
import homesysAdmin from "@/views/sysadmin/homesysAdmin";
import changePas from "@/views/sysadmin/changePas";
import MainsysAdmin from "@/views/sysadmin/MainsysAdmin";
import MaAdmin from '../views/sysadmin/MaAdmin'
//教师
import homeTeacher from "@/views/Teacher/homeTeacher";
import MainTeacher from "@/views/Teacher/MainTeacher";
import changePasTeacher from "../views/Teacher/changePasTeacher";
//科研管理员
import MainresAdmin from "@/views/resadmin/MainresAdmin";
import homeresAdmin from "../views/resadmin/homeresAdmin";
import changePasres from "../views/resadmin/changePasres";
import ExProject from "@/views/resadmin/ExProject";
import update_pwd from "@/views/update_pwd";
import Audit from "@/views/resadmin/Audit";
import Maaward from "@/views/resadmin/Maaward";
import Mamonograph from "@/views/resadmin/Mamonograph";
import Mapaper from "@/views/resadmin/Mapaper";
import Mapatent from "@/views/resadmin/Mapatent";
import sum from "@/views/resadmin/sum";
import Myproject from "@/views/Teacher/Myproject";
import Updatepro from "@/views/Teacher/Updatepro";
import MaawardT from "../views/Teacher/MaawardT";
import MamonographT from "@/views/Teacher/MamonographT";
import MapaperT from "@/views/Teacher/MapaperT";
import MapatentT from "../views/Teacher/MapatentT";
import sumpaper from "@/views/resadmin/sumpaper";
import sumpatent from "@/views/resadmin/sumpatent";
import summonograph from "@/views/resadmin/summonograph";
import sumaward from "@/views/resadmin/sumaward";
//创建路由组件
const routes = [
    {
        path:'/login',
        component: Login,
        name:'login'
    },
    {
        path:'/update_pwd',
        component: update_pwd,
        name:'update_ped'
    },
    {//系统管理员
        path:'/sysadmin',
        component: MainsysAdmin,
        redirect:'/sysadmin/homesysadmin',
        children:[
            //子路由
            {path:'homesysAdmin',name:'homesysAdmin',component:homesysAdmin},
            {path:'MaAdmin',name:'MaAdmin',component:MaAdmin},
            {path:'changePas',name:'changePas',component:changePas},
        ]
    },
    {//教师
        path:'/Teacher',
        component: MainTeacher,
        redirect:'/Teacher/homeTeacher',
        children:[
            //子路由
            {path:'homeTeacher',name:'homeTeacher',component:homeTeacher},
            {path:'changePasTeacher',name:'changePasTeacher',component:changePasTeacher},
            {path:'Myproject',name:'Myproject',component:Myproject},
            {path:'Updatepro',name:'Updatepro',component:Updatepro},
            {path:'MaawardT',name:'MaawardT',component:MaawardT},
            {path:'MapaperT',name:'MapaperT',component:MapaperT},
            {path:'MamonographT',name:'MamonographT',component:MamonographT},
            {path:'MapatentT',name:'MapatentT',component:MapatentT},

        ]
    },
    {//科研管理员
        path:'/resadmin',
        component: MainresAdmin,
        redirect:'/resadmin/homeresAdmin',
        children:[
            //子路由
            {path:'homeresAdmin',name:'homeresAdmin',component:homeresAdmin},
            {path:'changePasres',name:'changePasres',component:changePasres},
            {path:'ExProject',name:'ExProject',component:ExProject},
            {path:'Audit',name:'Audit',component:Audit},
            {path:'Maaward',name:'Maaward',component:Maaward},
            {path:'Mamonograph',name:'Mamonograph',component:Mamonograph},
            {path:'Mapaper',name:'Mapaper',component:Mapaper},
            {path:'Mapatent',name:'Mapatent',component:Mapatent},
            {path:'sum',name:'sum',component:sum},
            {path:'sumpaper',name:'sumpaper',component:sumpaper},
            {path:'sumpatent',name:'sumpatent',component:sumpatent},
            {path:'summonograph',name:'summonograph',component:summonograph},
            {path:'sumaward',name:'sumaward',component:sumaward},

        ]
    }

    ]
const router = new VueRouter({
    routes // (缩写) 相当于 routes: routes
})

export default router
