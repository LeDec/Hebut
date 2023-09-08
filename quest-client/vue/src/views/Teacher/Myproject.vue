<template>

  <div class="manage">
    <el-dialog
        title="项目信息"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose"
    >
      <!--        用户信息-->
      <el-form ref="form" :rules="rules" :inline="true" :model="form" label-width="80px">
        <el-form-item label="项目名称" prop="name">
          <el-input placeholder="请输入项目名称" v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="教师" prop="teacher">
          <el-input placeholder="请输入教师姓名" v-model="form.teacher"></el-input>
        </el-form-item>
        <el-form-item label="项目进度" prop="pro">
          <el-input placeholder="请输入项目进度" v-model="form.pro"></el-input>
        </el-form-item>
        <el-form-item label="类别" prop="type">
          <el-select v-model="form.type" placeholder="请选择项目类别">
            <el-option label="纵向科研项目" value="纵向科研项目"></el-option>
            <el-option label="横向科研项目" value="横向科研项目"></el-option>
          </el-select>          </el-form-item>
        <el-form-item label="申请年" prop="year">
          <el-input placeholder="请输入项目进度" v-model="form.year"></el-input>
        </el-form-item>
        <el-form-item label="申请月" prop="month">
          <el-input placeholder="请输入项目进度" v-model="form.month"></el-input>
        </el-form-item>
        <el-form-item label="申请日" prop="day">
          <el-input placeholder="请输入项目进度" v-model="form.day"></el-input>
        </el-form-item>


      </el-form>

      <span slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
        </span>
    </el-dialog>

    <div class="manage-header">
      <el-button @click="handleAdd" type="primary">
        +申报
      </el-button>

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
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
import { SearchMyProject, search, addproject, deletepro, updatepro} from '../../api/index'
import global from "@/views/global";
export default {
  data(){
    return {
      input:"",
      dialogVisible: false,
      form :{
        day: 0,
        month: 0,
        year: 0,
        name: "",
        pro: "",
        teacher: "",
        type: "",
        userId: global.UserId
      },
      tableData:[

      ],
      modelType:0,  // 0表示新增的弹窗，1表示编辑
      total: 0,  //当前数据总条数
    }
  },
  methods: {
    submit() {
      this.$refs.form.validate((valid) => {
        // console.log(valid,'valid');
        if (valid) {
          // 处理
          if (this.modelType === 0) {
            addproject(this.form).then(() => {
              console.log(this.form)
              //更新完数据重新获取列表接口
              this.getList()
            })
          } else {
            updatepro({day:this.form.day,month:this.form.month,name:this.form.name,pro:this.form.pro,projectId: this.tableData.projectId,teacher: this.form.teacher,type:this.form.type,year:this.form.year}).then(() => {
              console.log(this.form)
              //更新完数据重新获取列表接口
              this.getList()
            })
          }
          this.handleClose()
        }
      })
    },
    handleClose() {
      this.$refs.form.resetFields()
      this.dialogVisible = false
    },
    handleEdit(row) {
      this.modelType = 1

      this.tableData.projectId=row.projectId
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletepro({projectId:row.projectId}).then(()=>{
          this.$message({
            // type: 'success',
            // message: '删除成功!'
            type:'info',
            message:'删除成功'
          });
          this.getList()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleAdd() {
      this.modelType = 0
      this.dialogVisible = true
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
      if(this.input === ''||this.input===0){
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
