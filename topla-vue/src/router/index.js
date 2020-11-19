import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'todolist mode',
    component: () => import(/* webpackChunkName: "about" */ '../views/todo.vue')
  },
  {
    path: '/test',
    name: 'test mode',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/test.vue')
  },
  {
    path: '/preset',
    name: 'preset mode',
    component: () => import(/* webpackChunkName: "about" */ '../views/preset.vue')
  },
  {
    path: '/calendar',
    name: 'calendar mode',
    component: () => import(/* webpackChunkName: "about" */ '../views/calendar.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
