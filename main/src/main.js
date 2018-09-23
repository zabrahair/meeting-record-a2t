// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import {AudioRecorder, AudioPlayer} from 'vue-audio-recorder'

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App, AudioRecorder, AudioPlayer },
  template: '<App/>'
})
