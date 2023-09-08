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
import CommonAside from "@/components/resadmin/CoAsresadmin";
import CommonHeader from "@/components/resadmin/CoHeresadmin";
import CommonTag from "@/components/resadmin/CoTagadmin";
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
      return this.$store.state.tab2.isCollapse
    }
  }
}
</script>

<style>
.el-header {
  padding: 0;

}
</style>