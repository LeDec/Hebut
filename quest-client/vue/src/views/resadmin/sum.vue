<template>
  <div>
    <!--块类统计-->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="box-card">
          <h3>已通过项目总数</h3>
          <div>
            <i class="el-icon-bank-card" style="color: purple"></i>
            <span>6</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <h3>横向项目数</h3>
          <div>
            <i class="el-icon-reading" style="color: green"></i>
            <span>2</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <h3>纵向项目数</h3>
          <div>
            <i class="el-icon-s-flag" style="color: red"></i>
            <span>4</span>
          </div>
        </el-card>
      </el-col>

    </el-row>
    <!--acharts统计图-->

    <el-row :gutter="20">

      <el-col :span="16">
        <el-card class="box-card">
          <echarts2 />
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card class="box-card">
          <echarts1 />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import echarts2 from '@/ECharts/echarts2'
import echarts1 from '@/ECharts/echarts1'
export default {
  data(){
    return {

      tableData:[

      ],
      total: 0,  //当前数据总条数

    }
  },

  name: 'home',
  components: {
    echarts2,
    echarts1,
  },
}
</script>

<style scoped>
.el-card{
  margin-bottom: 20px;
  text-align: center;
}

</style>
