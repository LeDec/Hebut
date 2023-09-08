<template>

  <div class="manage">
    <el-dialog
        title="专利信息"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose"
    >
      <!--        用户信息-->
      <el-form ref="form"  :inline="true" :model="form" label-width="90px">

        <el-form-item label="专利名称" prop="title">
          <el-input placeholder="请输入专利名称" v-model="form.title"></el-input>
        </el-form-item>
        <el-form-item label="申请人" prop="applicant">
        <el-input placeholder="请输入申请人" v-model="form.applicant"></el-input>
        </el-form-item>
        <el-form-item label="申请号" prop="aid">
          <el-input placeholder="请输入申请号" v-model="form.aid"></el-input>
        </el-form-item>
        <el-form-item label="公开号" prop="pid">
          <el-input placeholder="请输入公开号" v-model="form.pid"></el-input>
        </el-form-item>
        <el-form-item label="国别" prop="country">
          <el-input placeholder="请输入国家" v-model="form.country"></el-input>
        </el-form-item>
        <el-form-item label="获得专利时间" prop="obtainDate">
          <el-input placeholder="请输入获得专利时间" v-model="form.obtainDate"></el-input>
        </el-form-item>
        <el-form-item label="专利类型" prop="type">
          <el-input placeholder="请输入专利类型" v-model="form.type"></el-input>
        </el-form-item>
        <el-form-item label="第一发明人" prop="firstInventor">
          <el-input placeholder="请输入第一发明人" v-model="form.firstInventor"></el-input>
        </el-form-item>
        <el-form-item label="其他发明人" prop="otherInventor">
          <el-input placeholder="请输入其他发明人" v-model="form.otherInventor"></el-input>
        </el-form-item>




      </el-form>

      <span slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
        </span>
    </el-dialog>

    <div class="manage-header">
      <el-button @click="handleAdd" type="primary">
        +上传专利信息
      </el-button>

      <!--from搜索区-->

    </div>

    <div class="common-table">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="aid" label="申请号" ></el-table-column>
        <el-table-column prop="title" label="专利标题" ></el-table-column>
        <el-table-column prop="type" label="专利类型" ></el-table-column>
        <el-table-column prop="pid" label="公开号" ></el-table-column>
        <el-table-column prop="firstInventor" label="第一发明人"></el-table-column>
        <el-table-column prop="otherInventor" label="其他发明人" ></el-table-column>
        <el-table-column prop="applicant" label="申请人" ></el-table-column>
        <el-table-column prop="isCheck" label="审核情况" ></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>

</template>

<script>
import { Selectp, deletep, updatep, insertp} from '../../api/index'
import global from "@/views/global";
export default {
  data(){
    return {
      input:0,
      dialogVisible: false,
      form :{

        aid: "",
        applicant: "",
        applyId: global.UserId,
        country: "",
        createTime: "",
        firstInventor: "",
        id: "",
        informationId: "",
        note: "",
        obtainDate: "",
        otherInventor: "",
        pid: "",
        title: "",
        type: "",
        updateTime: ""

      },
      tableData:[

      ],
      sendData: {
        department:"",
        name:"",
        role:""
      },
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
            insertp(this.form).then(() => {
              console.log(this.form)
              //更新完数据重新获取列表接口
              this.getList()
            })
          } else {
            updatep(this.form).then(() => {
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
      this.dialogVisible = true
      this.form = JSON.parse(JSON.stringify(row))
    },
    handleDelete(row) {
      this.$confirm('此操作将永久删除该专利, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletep({type:"award",id:row.id}).then(()=>{
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

      Selectp({userid:global.UserId}).then(({data}) => {
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