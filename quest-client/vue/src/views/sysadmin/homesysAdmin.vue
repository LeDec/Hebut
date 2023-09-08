<template>
  <div>
    <el-row>
      <el-col :span="8">
        <div class="grid-content bg-purple">
          <el-card class="box-card" style="margin-right: 10px;margin-bottom: 20px">
            <div class="user">
              <img src="../../assets/pyy.jpg">
              <div class="user-info">
                <p class="name">Admin</p>
                <p class="access">系统管理员</p>
              </div>
            </div>
            <div class="login-info">
              <p>上次登录的时间：<span>2022-12-26</span></p>
              <p>上次登录的地点：<span>河北省沧州市</span></p>
            </div>
          </el-card>
          <!--          <el-card style="height:280px;margin-right: 10px;">-->
          <!--            &lt;!&ndash;          左下图&ndash;&gt;-->
          <!--            <div ref="echarts1" style="height: 250px;"></div>-->
          <!--          </el-card>-->
          <el-card style="height:230px;margin-right: 10px;">
            <el-form ref="formData" :model="formData" label-width="80px">
              <el-form-item prop="person" label="开发人员" style="width: 90%">
                <el-input :disabled="true" v-model="formData.person"></el-input>
              </el-form-item>
              <el-form-item prop="teacher" label="指导老师" style="width: 90%">
                <el-input :disabled="true" v-model="formData.teacher"></el-input>
              </el-form-item>
              <el-form-item prop="time" label="开发时间" style="width: 90%">
                <el-input :disabled="true" v-model="formData.time"></el-input>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-col>

      <el-col :span="16">
        <div class="grid-content bg-purple-light">
          <el-card class="circle" style="height: 307px;margin-bottom: 20px">
            <!--          <circle></circle>-->
            <!--          右上图-->
            <div ref="echarts2" style="height: 290px; width: 800px"></div>
          </el-card>
          <!--        右下-->
          <!--          <div class="right-bottom">-->
          <!--            <el-card style="height:280px;">-->
          <!--              <div ref="echarts3" style="height: 250px;width: 800px;"></div>-->
          <!--            </el-card>-->
          <!--          </div>-->

        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>

import * as echarts from 'echarts'
export default {
  data(){
    return {
      formData:{
        person:"李东/张琳/郭岩松/郭宇轩",
        teacher:"彭玉清",
        time:"2022/12/08"
      }
    }
  },
  mounted() {
    //第一个表格
    // const myChart = echarts.init(this.$refs.echarts1);
    const myChart2 = echarts.init(this.$refs.echarts2);

    let option2;
    option2 = {
      title: {
        text: '当下已上传科研信息'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['专著', '专利', '论文', '获奖']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      toolbox: {
        feature: {
          saveAsImage: {}
        }
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '专著',
          type: 'line',
          stack: 'Total',
          data: [220, 182, 191, 234, 290, 330, 310]
        },
        {
          name: '专利',
          type: 'line',
          stack: 'Total',
          data: [150, 232, 201, 154, 190, 330, 410]
        },
        {
          name: '论文',
          type: 'line',
          stack: 'Total',
          data: [320, 332, 301, 334, 390, 330, 320]
        },
        {
          name: '获奖',
          type: 'line',
          stack: 'Total',
          data: [820, 932, 901, 934, 1290, 1330, 1320]
        }
      ]
    };

    option2 && myChart2.setOption(option2);
    // option3 && myChart3.setOption(option3);

  }
}
</script>

<style lang="less" scoped>
.user {
  padding-bottom: 20px;
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  display: flex;
  align-items: center;
  img {
    width: 170px;
    height: 170px;
    border-radius: 50%;
    margin-right: 40px;
  }
  .user-info {
    .name {
      font-size: 32px;
      margin-bottom: 10px;
    }
    .access {
      color: #999999;
    }
  }
}
.login-info{
  p {
    line-height: 28px;
    font-size: 14px;
    color: #999999;
    span {
      color: #666666;
      margin-left: 60px;
    }
  }
}
.right-bottom {
  display: flex;
  justify-content: space-between;
}
</style>