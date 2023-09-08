<template>
  <div class="tags">
    <el-tag
        v-for="(item,indexx) in tags"
        :key="item.path"
        :closable="item.name !== 'homeAdmin'"
        :effect="$route.name === item.name ? 'dark' : 'plain' "
        @click="changeMenu(item)"
        @close="handleClose(item,indexx)"
        size="small"
    >
      {{ item.label }}
    </el-tag>
  </div>
</template>

<script>
import {mapState,mapMutations} from 'vuex'
export default {
  name: "CommonTag",
  data(){
    return {

    }
  },
  computed:{
    ...mapState({
      tags:state=>state.tab.tabListAdmin
    })
  },
  methods:{
    ...mapMutations(['closeTag']),
    //点击tag进行跳转
    changeMenu(item){
      this.$router.push({name: item.name})
    },
    //点击tag删除
    handleClose(item,index){
      this.closeTag(item)
      const length = this.tags.length
      //删除之后的跳转逻辑
      if(item.name !== this.$route.name){
        return
      }
      if(index === length){
        this.$router.push({
          name: this.tags[index-1].name
        })
      } else{
        this.$router.push({
          name:this.tags[index].name
        })
      }
    },
  }
}
</script>

<style lang="less" scoped>
.tags {
  //padding: 15px;
  padding-right: 15px;
  padding-left: 15px;
  padding-top: 5px;
  padding-bottom: 5px;
  border-bottom: 1px solid rgb(222,224,228);
  .el-tag {
    margin-right: 8px;
    cursor: pointer;
  }
}
</style>