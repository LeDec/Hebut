<template>
  <el-menu default-active="1-4-1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
           :collapse="isCollapse"
           background-color="#545c64"
           text-color="#fff"
           active-text-color="#ffd04b">
    <h3>{{isCollapse ? '科研管理员' : '科研管理员后台系统' }}</h3>
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
          path:'/resadmin/homeresAdmin',
          name:'homeAdmin',
          label:'首页',
          icon:'el-icon-s-home',
          url:'Home/HomeresAdmin'
        },
        {
          path:'/resadmin/changePasres',
          name:'changePasTeacher.vue',
          label:'修改密码',
          icon:'el-icon-edit-outline',
          url:'Home/changePas'
        },
        {
          label:'科研信息审核',
          icon:'el-icon-document',
          children:[

            {
              path:'/resadmin/Mapatent',
              name:'Mapatent',
              label:'专利审核',
              icon:'el-icon-magic-stick',
              url:'InfoMa/Mapatent'
            },
            {
              path:'/resadmin/Mamonograph',
              name:'Mamonograph',
              label:'专著审核',
              icon:'el-icon-collection',
              url:'InfoMa/Mamonograph'
            },
            {
              path:'/resadmin/Mapaper',
              name:'Mapaper',
              label:'论文审核',
              icon:'el-icon-notebook-2',
              url:'InfoMa/Mapaper'
            },
            {
              path:'/resadmin/Maaward',
              name:'Maaward',
              label:'获奖审核',
              icon:'el-icon-trophy',
              url:'InfoMa/Maaward'
            }
          ]
        },
        {
          label:'科研信息汇总',
          icon:'el-icon-collection',
          children:[

            {
              path:'/resadmin/sumpatent',
              name:'sumpatent',
              label:'专利汇总',
              icon:'el-icon-magic-stick',
              url:'InfoSum/sumpatent'
            },
            {
              path:'/resadmin/summonograph',
              name:'summonograph',
              label:'专著汇总',
              icon:'el-icon-collection',
              url:'InfoSum/summonograph'
            },
            {
              path:'/resadmin/sumpaper',
              name:'sumpaper',
              label:'论文汇总',
              icon:'el-icon-notebook-2',
              url:'InfoSum/sumpaper'
            },
            {
              path:'/resadmin/sumaward',
              name:'sumaward',
              label:'获奖汇总',
              icon:'el-icon-trophy',
              url:'InfoSum/sumaward'
            }
          ]
        },
        {
          label:'科研项目管理',
          icon:'el-icon-notebook-2',
          children:[

            {
              path:'/resadmin/ExProject',
              name:'ExProject',
              label:'科研项目发布',
              icon:'el-icon-document',
              url:'project/Exproject'
            },
            {
              path:'/resadmin/Audit',
              name:'Audit',
              label:'科研项目审核',
              icon:'el-icon-collection',
              url:'project/Audit'
            },
            {
              path:'/resadmin/sum',
              name:'teacherMaAdmin',
              label:'科研项目汇总',
              icon:'el-icon-notebook-2',
              url:'project/sum'
            },

          ]
        },


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
      if(this.$route.path != item.path && !(this.$route.path ==='/resadmin/homeresAdmin' && item.path === '/resadmin'))
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
      return this.$store.state.tab.isCollapse
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