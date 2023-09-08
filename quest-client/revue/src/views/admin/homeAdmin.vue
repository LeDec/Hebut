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
                <p class="access">管理员</p>
              </div>
            </div>
            <div class="login-info">
              <p>上次登录的时间：<span>2023-05-13</span></p>
              <p>上次登录的地点：<span>天津市北辰区</span></p>
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
        person:"李东/付少康/张琳",
        teacher:"郝小可",
        time:"2023/05/12"
      }
    }
  },
  mounted() {
    //第一个表格
    // const myChart = echarts.init(this.$refs.echarts1);
    const myChart2 = echarts.init(this.$refs.echarts2);
    // const myChart3 = echarts.init(this.$refs.echarts3);

    // let option = {};
    // option = {
    //   backgroundColor: '#2c343c',
    //   title: {
    //     text: '货物类别运输数量',
    //     left: 'center',
    //     top: 20,
    //     textStyle: {
    //       color: '#ccc'
    //     }
    //   },
    //   tooltip: {
    //     trigger: 'item'
    //   },
    //   visualMap: {
    //     show: false,
    //     min: 80,
    //     max: 600,
    //     inRange: {
    //       colorLightness: [0, 1]
    //     }
    //   },
    //   series: [
    //     {
    //       name: '货物类别运输数量',
    //       type: 'pie',
    //       radius: '55%',
    //       center: ['50%', '50%'],
    //       data: [
    //         { value: 170, name: 'A' },
    //         { value: 310, name: 'B' },
    //         { value: 274, name: 'C' },
    //         { value: 200, name: 'D' },
    //         { value: 183, name: 'E' }
    //       ].sort(function (a, b) {
    //         return a.value - b.value;
    //       }),
    //       roseType: 'radius',
    //       label: {
    //         color: 'rgba(255, 255, 255, 0.3)'
    //       },
    //       labelLine: {
    //         lineStyle: {
    //           color: 'rgba(255, 255, 255, 0.3)'
    //         },
    //         smooth: 0.2,
    //         length: 10,
    //         length2: 20
    //       },
    //       itemStyle: {
    //         color: '#c23531',
    //         shadowBlur: 200,
    //         shadowColor: 'rgba(0, 0, 0, 0.5)'
    //       },
    //       animationType: 'scale',
    //       animationEasing: 'elasticOut',
    //       animationDelay: function () {
    //         return Math.random() * 200;
    //       }
    //     }
    //   ]
    // };
    let option2;
    option2 = {
      title: {
        text: '用户打卡任务类型统计'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['副本任务', '成就任务', '每日任务', '每周任务']
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
          name: '副本任务',
          type: 'line',
          stack: 'Total',
          data: [22, 18, 19, 23, 29, 30, 31]
        },
        {
          name: '成就任务',
          type: 'line',
          stack: 'Total',
          data: [15, 23, 20, 15, 19, 33, 10]
        },
        {
          name: '每周任务',
          type: 'line',
          stack: 'Total',
          data: [30, 21, 31, 14, 20, 30, 32]
        },
        {
          name: '每日任务',
          type: 'line',
          stack: 'Total',
          data: [20, 32, 51, 34, 29, 30, 40]
        }
      ]
    };

    // let option3 ;
    // option3 = {
    //   xAxis: {
    //     data: ['司机',  '货主']
    //   },
    //   yAxis: {},
    //   dataGroupId: '',
    //   animationDurationUpdate: 500,
    //   series: {
    //     type: 'bar',
    //     id: 'sales',
    //     data: [
    //       {
    //         value: 5,
    //         groupId: '司机'
    //       },
    //       {
    //         value: 4,
    //         groupId: '货主'
    //       }
    //     ],
    //     universalTransition: {
    //       enabled: true,
    //       divideShape: 'clone'
    //     }
    //   }
    // };
    // const drilldownData = [
    //   {
    //     dataGroupId: 'animals',
    //     data: [
    //       ['Cats', 4],
    //       ['Dogs', 2],
    //       ['Cows', 1],
    //       ['Sheep', 2],
    //       ['Pigs', 1]
    //     ]
    //   },
    //   {
    //     dataGroupId: 'fruits',
    //     data: [
    //       ['Apples', 4],
    //       ['Oranges', 2]
    //     ]
    //   },
    //   {
    //     dataGroupId: 'cars',
    //     data: [
    //       ['Toyota', 4],
    //       ['Opel', 2],
    //       ['Volkswagen', 2]
    //     ]
    //   }
    // ];
    // myChart.on('click', function (event) {
    //   if (event.data) {
    //     var subData = drilldownData.find(function (data) {
    //       return data.dataGroupId === event.data.groupId;
    //     });
    //     if (!subData) {
    //       return;
    //     }
    //     myChart.setOption({
    //       xAxis: {
    //         data: subData.data.map(function (item) {
    //           return item[0];
    //         })
    //       },
    //       series: {
    //         type: 'bar',
    //         id: 'sales',
    //         dataGroupId: subData.dataGroupId,
    //         data: subData.data.map(function (item) {
    //           return item[1];
    //         }),
    //         universalTransition: {
    //           enabled: true,
    //           divideShape: 'clone'
    //         }
    //       },
    //       graphic: [
    //         {
    //           type: 'text',
    //           left: 50,
    //           top: 20,
    //           style: {
    //             text: 'Back',
    //             fontSize: 18
    //           },
    //           onclick: function () {
    //             myChart.setOption(option);
    //           }
    //         }
    //       ]
    //     });
    //   }
    // });
    // option && myChart.setOption(option);
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