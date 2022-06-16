import App from './App'

import {myRequest} from './util/api.js'
Vue.prototype.$myRequest = myRequest

// 引入
import {JSEncrypt} from 'common/jsencrypt/bin/jsencrypt.js'
Vue.prototype.$jsencrypt = JSEncrypt

// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif

// main.js
import uView from '@/uni_modules/uview-ui'
Vue.use(uView)
