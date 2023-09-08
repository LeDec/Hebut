
<template>
  <div class="manage">
    <el-dialog title="论文审核" :visible.sync="showChkStateDialog">
      <el-form>
        <el-form-item>
          <el-radio-group v-model="form.isCheck">
            <el-radio label="已通过" value="已通过">通过</el-radio>
            <el-radio label="未通过" value="未通过">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-input
              type="textarea"
              v-model="form.note"
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


    <div class="common-table">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="aid" label="申请号" ></el-table-column>
        <el-table-column prop="title" label="专利标题" ></el-table-column>
        <el-table-column prop="type" label="专利类型" ></el-table-column>
        <el-table-column prop="pid" label="公开号" ></el-table-column>
        <el-table-column prop="firstInventor" label="第一发明人"></el-table-column>
        <el-table-column prop="otherInventor" label="其他发明人" ></el-table-column>
        <el-table-column prop="applicant" label="申请人" ></el-table-column>

        <el-table-column prop="applyId" label="申请人编号" ></el-table-column>
        <el-table-column prop="isCheck" label="审核情况" ></el-table-column>
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
import {checkPa, SearchPa} from "../../api/index";
import global from "@/views/global";
export default {
  data(){
    return{
      input:0,
      // 项目审核
      form: {
        infrmation: "",
        isCheck: "",
        note: "",
        type: "papent"
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
    SearchPa().then(({data})=>{
      this.tableData = data.data
    })
  },
  methods:{

    // 点击审核的确定
    hSubmitChkState() {
      console.log(this.form)

      checkPa({ infrmation:this.form.infrmation, isCheck:this.form.isCheck,note:this.form.note,type: this.form.type }).then((data)=>{
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
      SearchPa().then(({data})=>{
        this.tableData = data.data
      })
    },
    agreeOrder(row) {
      console.log(row)
      this.form.infrmation=row.id
      console.log(this.form)
      this.showChkStateDialog = true
    },


  }
}
</script>

<style lang="less" scoped>
.manage {
  height: 90%;

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

