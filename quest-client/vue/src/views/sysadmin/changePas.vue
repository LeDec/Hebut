<template>
  <div>
    <!--    //显示个人信息的主页面-->
    <el-card class="infoOwner">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules">
        <el-form-item prop="username" label="账号" style="width: 90%">
          <el-input :disabled="true" v-model="form.userId"></el-input>
        </el-form-item>
        <el-form-item prop="p_old" label="旧密码" style="width: 90%">
          <el-input  v-model="form.p_old"></el-input>
        </el-form-item>
        <el-form-item prop="p_new" label="新密码" style="width: 90%">
          <el-input v-model="form.p_new"></el-input>
        </el-form-item>
      </el-form>
      <el-button @click="changePas" type="primary">修改密码</el-button>
    </el-card>
  </div>
</template>

<script>
import {change} from '../../api/index'
import global from "@/views/global";
export default {
  data(){
    return {
      form:{
        p_new:"",
        p_old:"",
        userId:global.UserId,
      },
      dialogVisible: false,
      rules:{
        p_old: [
          { required: true, message: "请输入旧密码", trigger: "blur" },
        ],
        p_new: [
          { required: true, message: "请输入密码", trigger: "blur" },
        ],
      }
    }
  },
  mounted(){

  },
  methods:{
    changePas(){
      change(this.form).then(({data})=>{
        if(data.code === 0){
          this.$message({
            type:'success',
            message:'修改成功'
          });
          this.$router.push({name:'login'})
        }
        else {
          this.$message({
            type:'error',
            message:'啊哦,哪里出问题了...'
          });
        }
      })
    },
  }
}
</script>

<style>
.infoOwner {
  width: 60%;
  margin: auto;
}

</style>