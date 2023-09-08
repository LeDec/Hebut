<template>

  <div class="manage">
    <el-dialog
        title="论文信息"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose"
    >
      <!--        用户信息-->
      <el-form ref="form"  :inline="true" :model="form" label-width="90px">
        <el-form-item label="论文标题" prop="name">
          <el-input placeholder="请输入论文标题" v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="DOI号" prop="doi">
          <el-input placeholder="请输入DOI号" v-model="form.doi"></el-input>
        </el-form-item>
        <el-form-item label="出版社" prop="publication">
          <el-input placeholder="请输入出版社" v-model="form.publication"></el-input>
        </el-form-item>
        <el-form-item label="第一作者" prop="firstAuthor">
          <el-input placeholder="请输入第一作者" v-model="form.firstAuthor"></el-input>
        </el-form-item>
        <el-form-item label="其他作者" prop="otherAuthor">
          <el-input placeholder="请输入其他作者" v-model="form.otherAuthor"></el-input>
        </el-form-item>




      </el-form>

      <span slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取 消</el-button>
            <el-button type="primary" @click="submit">确 定</el-button>
        </span>
    </el-dialog>

    <div class="manage-header">
      <el-button @click="handleAdd" type="primary">
        +上传论文信息
      </el-button>

      <!--from搜索区-->

    </div>

    <div class="common-table">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="doi" label="DOI号" ></el-table-column>
        <el-table-column prop="name" label="论文标题" ></el-table-column>
        <el-table-column prop="type" label="专著类型" ></el-table-column>
        <el-table-column prop="pubilcsh" label="出版社" ></el-table-column>
        <el-table-column prop="firstAuthor" label="第一作者"></el-table-column>
        <el-table-column prop="otherAuthor" label="其他作者" ></el-table-column>
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
import {deletepa, Selectpa, insertpa, updatepa} from '../../api/index'
import global from "@/views/global";
export default {
  data(){
    return {
      input:0,
      dialogVisible: false,
      form :{

        applyId: global.UserId,
        createTime: "",
        doi: "",
        firstAuthor: "",
        id: "",
        informationId: "",
        name: "",
        note: "",
        otherAuthor: "",
        publication: "",
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
            insertpa(this.form).then(() => {
              console.log(this.form)
              //更新完数据重新获取列表接口
              this.getList()
            })
          } else {
            updatepa(this.form).then(() => {
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
      this.$confirm('此操作将永久删除该论文, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deletepa({type:"award",id:row.id}).then(()=>{
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

      Selectpa({userid:global.UserId}).then(({data}) => {
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