<template>

  <div class="manage">
    <el-dialog
        title="用户信息"
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose"
    >
      <!--        用户信息-->
      <el-form ref="form" :rules="rules" :inline="true" :model="form" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input placeholder="请输入姓名" v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="账号" prop="userId">
          <el-input placeholder="请输入账号" v-model="form.userId"></el-input>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input placeholder="请输入年龄" v-model="form.age"></el-input>
        </el-form-item>
        <el-form-item label="email" prop="email">
          <el-input placeholder="请输入email" v-model="form.email"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-select v-model="form.gender" placeholder="请选择性别">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>          </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="教师" value="教师"></el-option>
            <el-option label="科研管理员" value="科研管理员"></el-option>
          </el-select>          </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-select v-model="form.department" placeholder="请选择部门">
            <el-option label="1：人工智能与数据科学学院" value=1></el-option>
            <el-option label="2：理学院" value=2></el-option>
          </el-select>          </el-form-item>

      </el-form>

      <span slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
        </span>
    </el-dialog>

    <div class="manage-header">
      <el-button @click="handleAdd" type="primary">
        +新增
      </el-button>

      <!--from搜索区-->
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="input" placeholder="请输入用户账号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmitToSea">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--表单-->
    <div class="common-table">
      <el-table
          height="90%"
          :data="tableData"
          style="width: 100%">
        <el-table-column
            prop="userId"
            label="账号">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
        >
        </el-table-column>
        <el-table-column
            prop="age"
            label="年龄"
        >
        </el-table-column>
        <el-table-column
            prop="gender"
            label="性别"
        >
        </el-table-column>
        <el-table-column
            prop="department"
            label="部门"
        >
        </el-table-column>
        <el-table-column
            prop="role"
            label="职称">
        </el-table-column>
        <el-table-column
            prop="email"
            label="email">
        </el-table-column>
        <!--          <el-table-column-->
        <!--              prop="password"-->
        <!--              label="密码">-->
        <!--          </el-table-column>-->
        <el-table-column label="操作">
          <template v-slot="scope">
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
import {checkuser,adduser,updateuser,deleteuser,searchuser} from '../../api/index'
export default {
  data(){
    return {
      input:0,
      dialogVisible: false,
      form :{
        age: 0,
        department: 0,
        email: "",
        gender: "",
        name: "",
        role: "",
        userId: 0
      },
      sendData: {
        department:"",
        name:"",
        role:""
      },
      tableData: [

      ],
      modelType:0,  // 0表示新增的弹窗，1表示编辑
      total: 0,  //当前数据总条数
      rules:{
        name: [{required:true,message:'请输入姓名'}],
        email: [{required:true,message:'请输入email'}],
        age: [{required:true,message:'请输入年龄'}],
        role: [{required:true,message:'请选择角色'}],
        gender:[{required:true,message:'请选择性别'}],
        department:[{required:true,message:'请选择部门'}],
        userId: [{required:true,message:'请输入账号'}],
      }
    }
  },
  methods: {
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // 处理
          if (this.modelType === 0) {
            adduser(this.form).then(() => {
              console.log(this.form)
              //更新完数据重新获取列表接口
              this.getList()
            })
          } else {
            updateuser(this.form).then(() => {
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
      this.dialogVisible = false
    },
    handleEdit(row) {
      this.modelType = 1

      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteuser({userId:row.userId}).then(()=>{
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

        searchuser(this.sendData).then(({data}) => {
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
        checkuser({userId:this.input}).then(({data}) =>{
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
