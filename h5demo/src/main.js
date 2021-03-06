// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import {AudioRecorder, AudioPlayer} from 'vue-audio-recorder'
import VueAudioRecorder from 'vue-audio-recorder'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.config.productionTip = false

// use components block
// Vue.component('audio-player', AudioPlayer)
// Vue.component('audio-recorder', AudioRecorder)
Vue.use(VueAxios, axios)
Vue.use(VueAudioRecorder);
Vue.use(ElementUI);



/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
