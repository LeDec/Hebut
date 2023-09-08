<template>

  <div class="manage">
    <el-dialog title="项目进度上报" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
      <!--        用户信息-->

      <el-form>
        <el-form-item>
          <el-input
              type="textarea"
              v-model="pronote"
              style="width:400px"
              placeholder="请按照实际情况输入项目进度"
          ></el-input>
        </el-form-item>
        <el-form-item>
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
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
    <!--表单-->
    <div class="common-table">
      <el-table height="90%":data="tableData" style="width: 100%">
        <el-table-column prop="projectId" label="项目编号" ></el-table-column>
        <el-table-column prop="name" label="项目名称" ></el-table-column>
        <el-table-column prop="pro" label="项目进度" ></el-table-column>
        <el-table-column prop="type" label="项目类型" ></el-table-column>
        <el-table-column prop="teacher" label="申报教师"></el-table-column>
        <el-table-column prop="apply" label="申请时间" ></el-table-column>
        <el-table-column prop="is_audit" label="审核情况" ></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">更新项目进度</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
            layout="prev, pager, next"
            :total="total"
            @current-change="handlePage"
        >
        </el-pagination>
      </div>
    </div>
  </div>

</template>

<script>
import {update, deleteuser, SearchMyProject, search, addproject, checkMo, SearchMo} from '../../api/index'
import global from "@/views/global";
export default {
  data(){
    return {
      pronote:"",
      input:"",
      dialogVisible: false,
      form :{

        apply: "",
        is_audit: "",
        name: "",
        pro: "",
        projectId: "",
        teacher: "",
        type: ""

      },
      tableData:[

      ],
      modelType:0,  // 0表示新增的弹窗，1表示编辑
      total: 0,  //当前数据总条数

    }
  },
  methods: {
    submit(){

update({ pro:this.pronote, projectId:this.form.projectId,userId:global.UserId}).then((data)=>{
  console.log(this.pronote)
  if(data.data.success === true){
    this.$message({
      type:'success',
      message:'上报进度成功'
    });
  }else {
    this.$message({
      type:'error',
      message:'上报进度失败'
    });
  }
  this.flushList()
  this.dialogVisible = false
})


},
    flushList(){
      SearchMyProject({name:"",teacher:"",type:"",userId:global.UserId}).then(({data})=>{
        this.tableData = data.data
      })
    },

    handleClose() {
      this.dialogVisible = false
    },
    handleEdit(row) {
      this.dialogVisible = true
      this.form = JSON.parse(JSON.stringify(row))
    },

    // 获取列表的数据，使得增加数据或者删除数据能够体现
    getList() {

      SearchMyProject({name:"",teacher:"",type:"",userId:global.UserId}).then(({data}) => {
        console.log(data)

        this.tableData = data.data
        this.total = data.data ? data.data.length : 0
        // console.log(this.total)
      })

    },
    handlePage(val){
      //选择页码的回调函数
      console.log(val);
    },
    //查询功能
    onSubmitToSea(){
      if(this.input === ""||this.input===0){
        this.getList()
      }
      else{
        search({id:this.input}).then(({data}) =>{
          this.tableData = data.data
          console.log(data)

        })
      }
    }
  },

  mounted(){
    this.getList()
    //searchuser('user').then(({data}) =>{
    //   console.log(data)
    //   this.tableData = data.data
    // })
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
//el-table {
//  height: 90%;
//}
</style>