<template>
  <!--这一个文件弃用-->
  <!--这一个文件弃用-->
  <div>
    <el-dialog
        title="选定用户"
        :visible.sync="dialogVisible"
        width="30%"
    >
      <el-form ref="choiceForm" :model="choiceForm" :inline="true" label-width="100px" :rules="rules">
        <el-form-item label="管理员编号" prop="adminId">
          <el-input  placeholder="请输入管理员编号" v-model="choiceForm.adminId" ></el-input>
        </el-form-item>
        <el-form-item label="用户编号" prop="userId">
          <el-input placeholder="请输入用户编号"  v-model="choiceForm.userId" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitID">确 定</el-button>
      </span>
    </el-dialog>
    <el-button @click="showDialog" type="primary">查看用户任务</el-button>
    <el-table :data="tableData" style="width: 100%"
              border
              stripe
              height="610">
      <el-table-column prop="quest_id" label="任务编号" ></el-table-column>
      <el-table-column prop="title" label="任务标题" ></el-table-column>
      <el-table-column prop="questEnum" label="任务类型"></el-table-column>
      <el-table-column prop="is_complete" label="是否完成"></el-table-column>
      <el-table-column prop="self_treasure" label="自定义奖励"></el-table-column>
      <el-table-column prop="combo" label="连击"></el-table-column>
      <el-table-column prop="coin" label="硬币数" ></el-table-column>
<!--      <el-table-column label="操作">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button size="mini" type="danger" @click="deleteUserClick(scope.row)">拒绝</el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>
  </div>
</template>

<script>
import {getUserTask} from "@/api";

export default {
  data(){
    return{
      dialogVisible:false,
      tableData:[

      ],
      choiceForm:{
        adminId:"",
        userId:""
      },
      rules:{
        adminId: [
          {required:true,message:'请输入管理员编号'}
        ],
        userId: [
          {required:true,message:'请输入用户编号'}
        ],
      }
    }
  },
  mounted() {
  },
  methods: {
    showDialog() {
      this.dialogVisible=true
    },
    submitID() {
      getUserTask({adminId:this.choiceForm.adminId,userId:this.choiceForm.userId}).then(({data})=>{
        console.log(data);
        if(data.code === 0){
          this.$message({
            type:'success',
            message:'查看成功'
          });
          this.tableData = data.data.questResponseList
          this.dialogVisible=false
        }
        else{
          this.$message({
            type:'error',
            message:'啊哦,哪里出问题了...'
          });
        }
      })
    }
  }
}
</script>

<style scoped>

</style>