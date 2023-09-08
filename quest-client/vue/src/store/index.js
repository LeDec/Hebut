import Vue from "vue";
import Vuex from 'vuex';
import tab from "@/store/tab";
import tab2 from './tab2'
import tab3 from './tab3'
Vue.use(Vuex)

//创建Vuex的实例
export default new Vuex.Store({

    modules: {
        tab,
        tab2,
        tab3
    }
})