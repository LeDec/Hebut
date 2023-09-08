<template>
  <div>

    <el-table :data="tableData" style="width: 100%"
              border
              stripe
              height="560">
      <el-table-column prop="dungeon_id" label="副本编号" ></el-table-column>
      <el-table-column prop="title" label="副本标题" ></el-table-column>
      <el-table-column prop="userCount" label="参与人数"></el-table-column>
      <el-table-column prop="coin" label="硬币数" ></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="deleteDunClick(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {deleteUserDung, getUserDung} from "@/api";

export default {
  data(){
    return {
      dialogVisible: false,
      tableData: [],
      sendForm:{
        user_id: "",
      }
    }
  },
  mounted() {
    getUserDung({user_id:1}).then(({data})=>{
      if(data.code===0){
        this.tableData = data.data.checkDungeonRequestList
      }else {
        this.$message({
          type: 'warning',
          message: '操作错误!'
        });
      }
    })
  },
  methods: {
    deleteDunClick(row) {
      this.$confirm('您确定要删除该副本吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableData = JSON.parse(JSON.stringify(row))
        console.log(this.tableData);
        deleteUserDung({adminId:1,dungeonId:this.tableData.dungeon_id}).then(({data}) => {
          if (data.code === 0) {
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
            this.flushList()
          } else {
            this.$message({
              type: 'warning',
              message: '操作错误!'
            });
            this.flushList()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消!'
        });
      });
    },
    flushList() {
      getUserDung({user_id:this.sendForm.user_id}).then(({data})=>{
        if(data.code===0){
          this.tableData = data.data.checkDungeonRequestList
        }
      })
    }
  }
}
</script>

<style scoped>

</style>