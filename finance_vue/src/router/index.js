import Vue from 'vue'
import Router from 'vue-router'
// import HelloWorld from '@/components/HelloWorld'
// import Test from '@/components/Test'
import ArticleList from '@/components/ArticleList'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'table',
      component: (resolve) => require(['../view/table.vue'], resolve)
    }, {
      path: '/table',
      name: 'table',
      component: (resolve) => require(['../view/table.vue'], resolve)
    }, {
      path: '/article',
      name: 'article',
      component: (resolve) => require(['../view/article.vue'], resolve)
    }, {
      path: '/ArticleList',
      name: 'ArticleList',
      component: ArticleList
    }
  ]
})
