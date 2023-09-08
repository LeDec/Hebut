<template>
  <div style="width: auto;height: 225px" id="echarts2">
  </div>

</template>

<script>
import echarts from 'echarts'
import {checkuser, SummaryProject} from "@/api";
export default {
  name: 'echarts2',
  data () {
    return {
      departmentId: 1,
      wide:1,
      deep:1,
      // option配置
      option: {
        title: {
          text: '人工智能与数据科学学院',
          subtext: '已通过项目汇总',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: 'Sum Project',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1, name: '横向项目数' },
              { value: 0, name: '纵向项目数' },

            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
    }
  },
  mounted () {
    this.getData();
  },

  methods: {
    getData(){
      SummaryProject({departmentId:this.departmentId}).then(({data}) =>{
        console.log(this.option)
        this.option.series[0].data[0].value=data.data.wide,
            this.option.series[0].data[1].value=data.data.deep,

        console.log(this.option.series[0].data[0].value),
        console.log(this.option.series[0].data[1].value)
        this.echartsInit();
      })
    },
    echartsInit () {

          console.log(this.option)
      // 在生命周期中挂载


      echarts.init(document.getElementById('echarts2')).setOption(this.option);
      //echarts.init(document.getElementById('echarts2')).resize();

    }
  }
}
</script>

<style scoped>

</style>