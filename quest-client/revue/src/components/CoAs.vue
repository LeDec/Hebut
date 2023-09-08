<template>
  <el-menu default-active="1-4-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
           :collapse="isCollapse"
           background-color="rgb(46,64,84)"
           text-color="#fff"
           active-text-color="#ffd04b">
    <!--    background-color="#545c64"-->
    <h3>{{isCollapse ? '管理员' : '管理员后台系统' }}</h3>
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
.el-menu-item:hover{
  outline: 0 !important;
  //color: #409EFF !important;
  background-color: rgb(144,147,153) !important;
}
</style>

<script>

export default {
  data() {
    return {
      // isCollapse: false,
      menuData:[
        {
          path:'/admin/homeAdmin',
          name:'homeAdmin',
          label:'首页',
          icon:'el-icon-s-home',
          url:'Home/homeAdmin'
        },
        {
          path:'/admin/notifyInfo',
          name:'notifyInfo',
          label:'通知管理',
          icon:'el-icon-edit',
          url:'notifyInfo/notifyInfo'
        },
        {
          path:'/admin/userInfo',
          name:'userInfo',
          label:'用户信息',
          icon:'el-icon-s-custom',
          url:'userInfo/userInfo'
        },
        // {
        //   label:'用户管理',
        //   icon:'el-icon-s-custom',
        //   children:[
        //     {
        //       path:'/admin/userInfo',
        //       name:'userInfo',
        //       label:'用户信息',
        //       icon:'el-icon-cherry',
        //       url:'userInfo/userInfo'
        //     },
        //     {
        //       path:'/admin/userTask',
        //       name:'userTask',
        //       label:'用户任务',
        //       icon:'el-icon-watermelon',
        //       url:'userTask/userTask'
        //     }
        //   ]
        // },
        {
          label:'副本管理',
          icon:'el-icon-cpu',
          children:[
            {
              path:'/admin/dungeonInfo',
              name:'dungeonInfo',
              label:'副本信息',
              icon:'el-icon-cherry',
              url:'dungeonInfo/dungeonInfo'
            },
            {
              path:'/admin/dungeonIntegration',
              name:'dungeonIntegration',
              label:'副本集成',
              icon:'el-icon-watermelon',
              url:'dungeonIntegration/dungeonIntegration'
            }
          ]
        }
      ]
    };
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    clickMenu(item){
      if(this.$route.path != item.path && !(this.$route.path ==='/admin/homeAdmin' && item.path === '/admin'))
        this.$router.push(item.path)
      this.$store.commit('selectMenu',item)
    }
  },
  computed:{
    noChildren(){
      return this.menuData.filter(item => !item.children)
    },
    hasChildren(){
      return this.menuData.filter(item => item.children)
    },
    isCollapse(){
      return this.$store.state.tab.isCollapse
    }
  }
}

</script>