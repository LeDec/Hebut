<template>

  <div class="manage">


    <div class="manage-header">
      <el-button @click="handleAdd" type="primary">
        审核已通过
      </el-button>
      <el-button @click="handle" type="primary">
        审核未通过
      </el-button>
      <el-button @click="hanleall" type="primary">
        所有信息
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
        <el-table-column prop="applyId" label="申请人编号" ></el-table-column>
        <el-table-column prop="isCheck" label="审核情况" ></el-table-column>

      </el-table>
    </div>

  </div>

</template>

<script>
import {Checkpaper, Checkpap, Checknopap} from '../../api/index'
import global from "@/views/global";
export default {
  data(){
    return {

      tableData:[

      ],
      total: 0,  //当前数据总条数

    }
  },
  methods: {


    handleAdd() {

      Checkpap().then(({data}) => {

        this.tableData = data.data
        // console.log(this.total)
      })
    },
    handle(){
      Checknopap().then(({data}) => {

        this.tableData = data.data
        // console.log(this.total)
      })
    },
    hanleall(){
      this.getList()
    },
    // 获取列表的数据，使得增加数据或者删除数据能够体现
    getList() {

      Checkpaper().then(({data}) => {
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