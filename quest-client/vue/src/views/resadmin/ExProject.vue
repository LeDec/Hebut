<template>
  <div class="manage">


  <div class="manage-header">

    <!--from搜索区-->
    <el-form :inline="true">
      <el-form-item>
        <el-input v-model="input" placeholder="请输入项目编号"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmitToSea">查询</el-button>
      </el-form-item>
    </el-form>
  </div>
    <div class="common-table">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="projectId" label="项目编号" ></el-table-column>
        <el-table-column prop="name" label="项目名称" ></el-table-column>
        <el-table-column prop="pro" label="项目阶段" ></el-table-column>
        <el-table-column prop="type" label="项目类型" ></el-table-column>
        <el-table-column prop="teacher" label="申报教师"></el-table-column>
        <el-table-column prop="apply" label="申请时间" ></el-table-column>
        <el-table-column prop="is_audit" label="审核情况" ></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="agreeOrder(scope.row)">发布</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import {
  AuditProject,
  checkuser,
  disagreeIndemnifyAdmin,
  getIndemnifyAdmin,
  PublishProject,
  searchproject
} from "../../api/index";
import global from "@/views/global";
export default {
  data(){
    return{
      tableData:[
        {

        }
      ],
      sendForm:{
       name: "",
        teacher: "",
        type: ""
      }
    }
  },
  mounted() {
    searchproject(this.sendForm).then(({data})=>{
      this.tableData = data.data
    })
  },
  methods:{
    flushList(){
      searchproject(this.sendForm).then(({data})=>{
        this.tableData = data.data
      })
    },
    agreeOrder(row) {
      this.$confirm('您确定要发布该项目吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.tableData = JSON.parse(JSON.stringify(row))
        PublishProject(this.tableData).then(({data}) => {
          if (data.code === 0) {
            this.$message({
              type: 'success',
              message: '项目已发布!'
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
          message: '已取消'
        });
      });
    },
    //查询功能
    onSubmitToSea(){
      if(this.input === ''||this.input===0){
        this.getList()
      }
      else{
        checkuser({projectId:this.input}).then(({data}) =>{
          this.tableData = data.data
          console.log(data)

        })
      }
    }

  }
}
</script>

<style lang="less" scoped>
.manage {
  height: 90%;
  .manage-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .common-table {
    position: relative;
    height: calc(100% - 62px);
    .pager {
      position: absolute;
      bottom: 0;
      right: 20px;
    }
  }
}

</style>
