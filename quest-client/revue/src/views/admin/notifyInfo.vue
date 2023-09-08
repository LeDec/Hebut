<template>
  <div>
    <el-dialog title="通知详情" :visible.sync="dialogVisible" >
      <el-input
          type="textarea"
          :autosize="{ minRows: 4, maxRows: 100}"
          placeholder="请输入内容"
          v-model="tableData2.article">
      </el-input>
    </el-dialog>

    <el-dialog title="发布通知" :visible.sync="dialogVisible2" >
      <el-form ref="NoteData" :model="NoteData" :inline="true" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input placeholder="请输入通知标题"  v-model="NoteData.title" ></el-input>
        </el-form-item>
      </el-form>
      <el-input
          type="textarea"
          :autosize="{ minRows: 4, maxRows: 100}"
          placeholder="请输入内容"
          v-model="NoteData.article">
      </el-input>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="submitAddNote">确 定</el-button>
      </span>
    </el-dialog>

    <el-button @click="showDialog" type="success">发布通知</el-button>

    <el-table :data="tableData" style="width: 100%"
              border
              stripe
              height="610">
      <el-table-column prop="id" label="通知编号" ></el-table-column>
      <el-table-column prop="title" label="标题" ></el-table-column>
      <el-table-column prop="createTime" label="发布时间"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="showNoteDetail(scope.row)">查看详情</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {getNote, addNote} from "@/api";

export default {
  data(){
    return{
      dialogVisible:false,
      dialogVisible2:false,
      tableData:[

      ],
      tableData2:[],
      NoteData:{
        adminId:1,
        article:"",
        title:""
      }
    }
  },
  mounted() {
    getNote({user_id:1}).then(({data})=>{
      if(data.code===0){
        this.tableData = data.data.noteTableList
      }
    })
  },
  methods: {
    showNoteDetail(row) {
      this.dialogVisible = true;
      this.tableData2 = JSON.parse(JSON.stringify(row))
    },
    showDialog() {
      this.dialogVisible2 = true;
    },
    flushList() {
      getNote({user_id:1}).then(({data})=>{
        if(data.code===0){
          this.tableData = data.data.noteTableList
        }
      })
    },
    submitAddNote() {
      addNote({adminId:this.NoteData.adminId,article:this.NoteData.article,
        title:this.NoteData.title}).then(({data})=>{
        if(data.code===0){
          this.dialogVisible2 = false;
          this.$message({
            type: 'success',
            message: '发布成功!'
          });
          this.flushList();
        }else {
          this.dialogVisible2 = false;
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