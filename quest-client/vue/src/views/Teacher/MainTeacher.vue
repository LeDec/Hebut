<template>
  <div>
    <el-container>
      <!--    <el-aside  width="auto" >-->
      <el-aside  width="200px" >
        <common-aside></common-aside>
      </el-aside>
      <el-container>
        <el-header>
          <CommonHeader></CommonHeader>
        </el-header>
        <common-tag></common-tag>
        <el-main>
          <router-view></router-view>
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>
import CommonAside from "@/components/Teacher/CoAsTeacher";
import CommonHeader from "@/components/Teacher/CoHeTeacher";
import CommonTag from "@/components/Teacher/CoTagTeacher";
export default {
  data(){
    return {

    }
  },
  components:{
    CommonAside,
    CommonHeader,
    CommonTag
  },
  computed: {
    isCollapse(){
      return this.$store.state.tab.isCollapse
    }
  }
}
</script>

<style>
.el-header {
  padding: 0;

}
</style>