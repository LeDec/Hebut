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
      <el-button @click="sumAward" type="primary">
        汇总文档
      </el-button>
      <el-button @click="handleDownload" type="primary">
        下载文档
      </el-button>
      <!--from搜索区-->

    </div>

    <div class="common-table">
      <el-table :data="tableData" style="width: 100%">
        <el-table-column prop="name" label="获奖名称" ></el-table-column>
        <el-table-column prop="awardTime" label="获奖时间" ></el-table-column>
        <el-table-column prop="level" label="获奖等级" ></el-table-column>
        <el-table-column prop="firstPerson" label="第一获奖人"></el-table-column>
        <el-table-column prop="otherPerson" label="其他获奖人" ></el-table-column>
        <el-table-column prop="applyId" label="申请人编号" ></el-table-column>
        <el-table-column prop="isCheck" label="审核情况" ></el-table-column>

      </el-table>
    </div>

  </div>

</template>

<script>
import {Checkaw, Checkaward, Checknoaw, lightExport} from '../../api/index'
import global from "@/views/global";
import axios from "axios";
export default {
  data(){
    return {

      tableData:[

      ],
      total: 0,  //当前数据总条数

    }
  },
  methods: {
    sumAward()
    {
      lightExport()
      alert("汇总完成");
    },
    handleDownload () {
          axios('award.xlsx', {
            responseType: 'blob', //重要代码
          }).then(res => {
            const url = window.URL.createObjectURL(res.data);
            const link = document.createElement('a');
            link.href = url;
            let fileName = "获奖信息文档.xlsx" //保存到本地的文件名称
            link.setAttribute("href",url);
            link.setAttribute('download', fileName);
            link.click();
            URL.revokeObjectURL(url);
          })
    },
    handleAdd() {

      Checkaw().then(({data}) => {

        this.tableData = data.data
        // console.log(this.total)
      })
    },
    handle(){
      Checknoaw().then(({data}) => {

        this.tableData = data.data
        // console.log(this.total)
      })
    },
    hanleall(){
      this.getList()
    },
    // 获取列表的数据，使得增加数据或者删除数据能够体现
    getList() {
      Checkaward().then(({data}) => {
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
