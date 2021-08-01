import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import md5 from 'js-md5'
Vue.prototype.$md5 = md5
Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.use(ElementUI);
//axios.defaults.withCredentials = true
new Vue({
router,
render: h => h(App)
}).$mount('#app')
