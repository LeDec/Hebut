<template>
  <div>
    <!--增加副本可选任务-->
    <el-dialog
        title="增加副本可选任务"
        :visible.sync="dialogVisible1"
        width="30%"
    >
      <el-form ref="choiceForm" :model="addForm" :inline="true" label-width="100px" :rules="rules1">
        <el-form-item label="金币数" prop="coin">
          <el-input placeholder="请输入金币数"  v-model="addForm.coin" ></el-input>
        </el-form-item>
        <el-form-item label="任务类型" prop="questEnum">
          <el-select v-model="addForm.questEnum" placeholder="请选择任务类型">
            <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="任务标题" prop="title">
          <el-input placeholder="请输入任务标题"  v-model="addForm.title" ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible1 = false">取 消</el-button>
          <el-button type="primary" @click="submitAddTask">确 定</el-button>
      </span>
    </el-dialog>


    <!--集成副本-->
    <el-dialog
        title="集成副本"
        :visible.sync="dialogVisible2"
        width="40%"
    >
      <el-form ref="intForm" :model="intForm" :inline="true" label-width="120px" :rules="rules2">
        <el-form-item label="金币数" prop="coin">
          <el-input placeholder="请输入金币数"  v-model="intForm.coin" ></el-input>
        </el-form-item>
        <el-form-item label="任务编号列表" prop="questId">
            <el-input placeholder="请输入任务编号列表"  v-model="intForm.questId" ></el-input>
        </el-form-item>
        <el-form-item label="任务标题" prop="title">
          <el-input placeholder="请输入任务标题"  v-model="intForm.title" ></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible2 = false">取 消</el-button>
          <el-button type="primary" @click="submitIntTask">确 定</el-button>
      </span>
    </el-dialog>

    <div class="top_header">
      <el-form ref="sendForm" :model="sendForm" :inline="true">
        <el-form-item>
          <el-button type="primary" @click="showAddTask">增加可选</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="showIntTask">副本集成</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tableData" style="width: 100%"
              border
              stripe
              height="560">
      <el-table-column prop="quest_id" label="任务编号" ></el-table-column>
      <el-table-column prop="title" label="任务标题" ></el-table-column>
      <el-table-column prop="questEnum" label="任务类型"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import {addUserDungTask, getUserDungTask, intUserDungTask} from "@/api";

export default {
  data(){
    return {
      dialogVisible1: false,
      dialogVisible2:false,
      tableData: [],
      sendForm:{
        user_id: "",
      },
      addForm: {
        adminId:"",
        coin:"",
        questEnum:"",
        title:""
      },
      rules1:{
        coin: [
          {required:true,message:'请输入金币数'}
        ],
        questEnum: [
          {required:true,message:'请选择任务类型'}
        ],
        title: [
          {required:true,message:'请输入标题'}
        ]
      },
      options: [{
        value: 'daily',
        label: '每日任务'
      }, {
        value: 'weekly',
        label: '每周任务'
      }, {
        value: 'achievement',
        label: '成就任务'
      }],
      intForm: {
        coin:"",
        questId:"",
        title:""
      },
      rules2:{
        coin: [
          {required:true,message:'请输入金币数'}
        ],
        questId: [
          {required:true,message:'请输入任务编号，如（1,2,3）'}
        ],
        title: [
          {required:true,message:'请输入标题'}
        ]
      },
      questIdList:[]
    }
  },
  mounted() {
    getUserDungTask({user_id:1}).then(({data})=>{
      if(data.code===0){
        this.tableData = data.data.questList
      }
    })
  },
  methods: {
    flushList() {
      getUserDungTask({user_id:1}).then(({data})=>{
        if(data.code===0){
          this.tableData = data.data.questList
        }
      })
    },
    showAddTask() {
      this.dialogVisible1 = true;
    },
    submitAddTask(){
      addUserDungTask({adminId:1,coin:this.addForm.coin,questEnum:this.addForm.questEnum,
        title:this.addForm.title}).then(({data})=>{
        if(data.code===0){
          this.dialogVisible1 = false;
          this.$message({
            type: 'success',
            message: '增加成功!'
          });
          this.flushList();
        }else {
          this.dialogVisible1 = false;
          this.$message({
            type: 'warning',
            message: '操作错误!'
          });
        }
      })
    },
    showIntTask() {
      this.dialogVisible2 = true;
    },
    submitIntTask(){
      this.questIdList = this.intForm.questId.split(",");
      console.log("this.questIdList:  " + this.questIdList[0] + "----" + this.questIdList[1]);
      intUserDungTask({adminId:"1",coin:this.intForm.coin,questIdList:this.questIdList,
        title:this.intForm.title}).then(({data})=>{
        if(data.code===0){
          this.dialogVisible2 = false;
          this.$message({
            type: 'success',
            message: '集成成功!'
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
