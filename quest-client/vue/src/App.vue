<template>
  <div id="app">

    <router-view></router-view>
  </div>
</template>

<script>

export default {
  name: 'App',
  mounted() {
    this.$router.push({name:'login'})
  }

}
</script>
<style lang="less">
html,body,h3,p {
  margin:0;
  padding: 0;
}

</style>

