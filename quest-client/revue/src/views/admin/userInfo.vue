<template>
  <div>
    <el-dialog title="用户任务" :visible.sync="dialogVisible" >
      <el-table :data="taskData" style="width: 100%"
                border
                stripe>
        <el-table-column prop="quest_id" label="任务编号" ></el-table-column>
        <el-table-column prop="title" label="任务标题" ></el-table-column>
        <el-table-column prop="questEnum" label="任务类型"></el-table-column>
        <el-table-column prop="is_complete" label="是否完成"></el-table-column>
        <el-table-column prop="self_treasure" label="自定义奖励"></el-table-column>
        <el-table-column prop="combo" label="连击"></el-table-column>
        <el-table-column prop="coin" label="硬币数" ></el-table-column>
      </el-table>
    </el-dialog>

    <el-table :data="tableData" style="width: 100%"
    border
    stripe
    height="610">
      <el-table-column prop="userId" label="编号" ></el-table-column>
      <el-table-column prop="nickname" label="昵称" ></el-table-column>
      <el-table-column prop="phone" label="手机"></el-table-column>
      <el-table-column prop="coin" label="硬币数" ></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="showUserTaskDialog(scope.row)">查看任务</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {getUserInfo,getUserTask} from "@/api";

export default {
  data(){
    return{
      dialogVisible:false,
      tableData:[

      ],
      tableData2:[

      ],
      taskData:[

      ]
    }
  },
  mounted() {
    getUserInfo({adminId:1}).then(({data})=>{
      if(data.code===0){
        this.tableData = data.data.checkUserResponseList
      }
    })
  },
  methods: {
    showUserTaskDialog(row) {
      this.dialogVisible = true;
      this.tableData2 = JSON.parse(JSON.stringify(row))
      console.log(this.tableData2);
      getUserTask({adminId: "1", userId: this.tableData2.userId}).then(({data}) => {
        if (data.code === 0) {
          this.taskData = data.data.questResponseList
          this.$message({
            type: 'success',
            message: '查看成功!'
          });
        } else {
          this.$message({
            type: 'warning',
            message: '操作错误!'
          });
        }
      })
    }
  }
}
</script>

<style scoped>

</style>