<template>
  <div class="header-container">
    <div class="left-content">
      <el-button style="margin-right: 20px" @click="handleMenu" icon="el-icon-menu" size="mini"></el-button>
      <!--    //面包屑-->
      <!--    <span class="text">首页</span>-->
      <el-breadcrumb separator="/">
        <el-breadcrumb-item v-for="item in tags" :key="item.path" :to="{ path: item.path }"> {{item.label}} </el-breadcrumb-item>

      </el-breadcrumb>
    </div>
    <div class="right-content">
      <el-dropdown>
           <span class="el-dropdown-link">
             <img class="userImg" src="../../assets/js.jpg" alt="">
          </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>个人信息</el-dropdown-item>
          <el-dropdown-item @click.native="exitSys">退出</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

  </div>
</template>

<script>
//方便使用store数据的辅助函数
import {mapState} from 'vuex'
export default {
  data(){
    return {}
  },
  methods:{
    handleMenu(){
      this.$store.commit('collapseMenu')
    },
    exitSys(){
      this.$router.push({name:'login'})
    }
  },
  computed:{
    ...mapState({
      tags:state=>state.tab3.tabListTeacher
    })
  },
  mounted() {
    // console.log(this.tags,'tags');
  }
}
</script>

<style lang="less" scoped>
.header-container {
  background-color: #333;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  .text {
    color:#fff;
    font-size: 14px;
    margin-left: 10px;
  }
  .right-content {
    .userImg {
      width: 55px;
      height: 60px;
      border-radius: 50%;
    }
  }
  .left-content {
    display: flex;
    align-items: center;
    /deep/.el-breadcrumb__item {
      .el-breadcrumb__inner {
        font-weight: normal;
        &.is-link {
          color: #666;
        }
      }
      &:last-child {
        .el-breadcrumb__inner {
          color: #fff;
        }
      }
    }
  }
}

</style>