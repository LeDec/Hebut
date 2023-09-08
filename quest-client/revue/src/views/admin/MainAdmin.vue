<template>
  <div>
    <el-container>
      <!--    <el-aside  width="auto" >-->
      <el-aside  width="200px" >
        <CoAs></CoAs>
      </el-aside>
      <el-container>
        <el-header>
          <CoHe></CoHe>
        </el-header>
        <CoTag></CoTag>
        <el-main>
          <router-view></router-view>
        </el-main>

      </el-container>
    </el-container>
  </div>
</template>

<script>
import CoAs from "@/components/CoAs";
import CoHe from "@/components/CoHe";
import CoTag from "@/components/CoTag";
export default {
  data(){
    return {

    }
  },
  components:{
    CoAs,
    CoHe,
    CoTag
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