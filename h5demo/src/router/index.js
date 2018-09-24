import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Audio2Text from '@/components/Audio2Text'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/hello',
      name: 'HelloWorld',
      component: HelloWorld,
    },
    {
      path: '/',
      name: 'Audio2Text',
      title: 'Audio to Text',
      component: Audio2Text,
    },
  ]
})
