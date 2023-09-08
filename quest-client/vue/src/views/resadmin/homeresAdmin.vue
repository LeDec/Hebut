
<template>
  <el-carousel indicator-position="outside"  style="width:100%" height="540px" interval="3000">
    <el-carousel-item v-for="item in imgArray" :key="item">
      <img :src="item" class="rightImg">
    </el-carousel-item>
  </el-carousel>
</template>
<script>
export default {
  data() {
    return {
      imgArray: [
        require('../../assets/1.jpg'),
        require('../../assets/2.jpg'),
        require('../../assets/3.jpg'),
      ]
    }
  }
}
</script>
<style>
.rightImg {
  width: 100%;
  height: 540px;
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>