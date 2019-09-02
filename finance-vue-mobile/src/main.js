// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/index.js'
import { initPlugins, initI18N } from './init-plugins'
import httpRequest from '@/utils/httpRequest'
import { isAuth } from '@/utils'
import VueCookie from 'vue-cookie'
import axios from 'axios'
import 'element-ui/lib/theme-chalk/index.css'
Vue.prototype.$axios = axios

//init the third-party plugins
//初始化第三方的组件
initPlugins();
//get i18n object
const i18n = initI18N('zh');

Vue.use(VueCookie)
Vue.use(ElementUI)
// 挂载全局
Vue.prototype.$http = httpRequest // ajax请求方法
Vue.prototype.isAuth = isAuth // 权限方法

window.SITE_CONFIG = {};

// api接口请求地址
window.SITE_CONFIG['baseUrl'] = 'http://192.168.1.125:8090/api';

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  i18n,
  store,
  template: '<App/>',
  components: { App }
})

/*Vue.config.productionTip = false;*/
/*document.addEventListener('deviceready', function() {
  new Vue({
    el: '#app',
    router,
    store,
    template: '<App/>',
    components: {App}
  })
  window.navigator.splashscreen.hide()
}, false);*/
