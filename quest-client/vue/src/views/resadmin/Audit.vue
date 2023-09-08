
<template>
  <div class="manage">
    <el-dialog title="项目审核" :visible.sync="showChkStateDialog">
      <el-form>
        <el-form-item>
          <el-radio-group v-model="form.chkState">
            <el-radio label="1" value="1">通过</el-radio>
            <el-radio label="0" value="0">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-input
              type="textarea"
              v-model="form.chkRemarks"
              style="width:400px"
              placeholder="请输入审核意见"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button @click="showChkStateDialog = false">取 消</el-button>
          <el-button type="primary" @click="hSubmitChkState">确定</el-button>
        </el-form-item>
    </el-form>
    </el-dialog>

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
            <el-button size="mini" type="primary" @click="agreeOrder(scope.row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>



<script>
import {AuditProject, checkProject, SearchAuditProject} from "../../api/index";
import global from "@/views/global";
export default {
  data(){
    return{
      input:0,
      // 项目审核
      form: {
        chkState: "",
        chkRemarks: "",
        id: ""
      },
      showChkStateDialog:false,


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

    SearchAuditProject(this.sendForm).then(({data})=>{
      this.tableData = data.data
    })
  },
  methods:{
    handleClose() {
      this.showChkStateDialog = false
    },
    // 点击审核的确定
    hSubmitChkState() {
      console.log(this.form)

      AuditProject({ audit:this.form.chkState, id: this.form.id }).then((data)=>{
        console.log(data)
        if(data.data.success === true){
          this.$message({
            type:'success',
            message:'审核成功'
          });
        }else {
          this.$message({
            type:'error',
            message:'审核失败'
          });
        }
        this.flushList()
        this.showChkStateDialog = false
      })


    },

    flushList(){
      SearchAuditProject(this.sendForm).then(({data})=>{
        this.tableData = data.data
      })
    },
    agreeOrder(row) {
      console.log(row)
      this.form.id=row.projectId
      console.log(this.form)
      this.showChkStateDialog = true
    },
    //查询功能
    onSubmitToSea(){
      if(this.input === ''||this.input===0){
        this.getList()
      }
      else{
        checkProject({id:this.input}).then(({data}) =>{
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
