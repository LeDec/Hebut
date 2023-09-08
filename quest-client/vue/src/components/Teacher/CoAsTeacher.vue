<template>
  <el-menu default-active="1-4-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
           :collapse="isCollapse"
           background-color="#545c64"
           text-color="#fff"
           active-text-color="#ffd04b">
    <h3>{{isCollapse ? '教师' : '教师后台系统' }}</h3>
    <el-menu-item  @click="clickMenu(item)" v-for="item in noChildren" :key="item.name" :index="item.name">
      <i :class="item.icon"></i>
      <span slot="title">{{ item.label }}</span>
    </el-menu-item>


    <el-submenu v-for="item in hasChildren" :key="item.label" :index="item.label">
      <template slot="title">
        <i :class="item.icon"></i>
        <span slot="title">{{item.label}}</span>
      </template>
      <el-menu-item-group v-for="subItem in item.children" :key="subItem.name" :index="subItem.name">
        <el-menu-item @click="clickMenu(subItem)" :index="subItem.name">{{ subItem.label }}</el-menu-item>
      </el-menu-item-group>
    </el-submenu>
  </el-menu>
</template>

<script>

export default {
  data() {
    return {
      // isCollapse: false,
      menuData:[
        {
          path:'/Teacher/homeTeacher',
          name:'homeAdmin',
          label:'首页',
          icon:'el-icon-s-home',
          url:'Home/HomeTeacher'
        },
        {
          path:'/Teacher/changePasTeacher',
          name:'changePasTeacher.vue',
          label:'修改密码',
          icon:'el-icon-edit-outline',
          url:'Home/changePas'
        },
        {
          label:'科研信息管理',
          icon:'el-icon-document',
          children:[

            {
              path:'/Teacher/MapatentT',
              name:'MapatentT',
              label:'专利管理',
              icon:'el-icon-magic-stick',
              url:'InfoMa/MapatentT'
            },
            {
              path:'/Teacher/MamonographT',
              name:'MamonographT',
              label:'专著管理',
              icon:'el-icon-collection',
              url:'InfoMa/MamonographT'
            },
            {
              path:'/Teacher/MapaperT',
              name:'MapaperT',
              label:'论文管理',
              icon:'el-icon-notebook-2',
              url:'InfoMa/MapaperT'
            },
            {
              path:'/Teacher/MaawardT',
              name:'MaawardT',
              label:'获奖管理',
              icon:'el-icon-trophy',
              url:'InfoMa/MaawardT'
            }
          ]
        },
        {
        label:'科研项目管理',
        icon:'el-icon-edit',
        children:[

      {
        path:'/Teacher/Myproject',
        name:'Myproject',
        label:'我的项目',
        icon:'el-icon-magic-stick',
        url:'/Project/Myproject'
      },
      {
        path:'/Teacher/Updatepro',
        name:'Updatepro',
        label:'进度上报',
        icon:'el-icon-collection',
        url:'/Project/Updatepro'
      },],},
      ]
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      // console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      // console.log(key, keyPath);
    },
    clickMenu(item){
      if(this.$route.path != item.path && !(this.$route.path ==='/Teacher/homeTeacher' && item.path === '/Teacher'))
        this.$router.push(item.path)
      this.$store.commit('selectMenu',item)
    }
  },
  computed:{
    noChildren(){
      // console.log(localhost)
      return this.menuData.filter(item => !item.children)
    },
    hasChildren(){
      return this.menuData.filter(item => item.children)
    },
    isCollapse(){
      return this.$store.state.tab3.isCollapse
    }
  }
}
</script>

<style lang="less" scoped>
//el-scrollbar {
//  width: 100%;
//}
.el-menu-vertical-demo:not(.el-menu--collapse) {
  //width: 200px; //不去底部除有滚动条
  min-height: 400px;
}
.el-menu {
  height: 100vh;
  border-right: none;
  h3 {
    color:#fff;
    font-size: 16px;
    font-weight: 400;
    text-align: center;
    line-height: 48px;
  }
}
</style>