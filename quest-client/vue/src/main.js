import Vue from 'vue';
import App from './App.vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from './router';
import store from './store';
//import Cookie from "js-cookie";
import lottie from "vue-lottie";
Vue.component("lottie", lottie);


Vue.config.productionTip = false
Vue.use(ElementUI);
//添加全局前置导航守卫
// router.beforeEach((to,from,next)=>{
//   //判断token存不存在
//   const token = Cookie.get('token')
//   if(!token && to.name!=='login') {
//     next({name :'login'})
//   }else if(token && to.name=== 'login'){
//
//   }else{
//     next();
//   }
// })
new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
