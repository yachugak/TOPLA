import Vue from 'vue'
import VueRouter from 'vue-router'
import loginInfo from "@/plugins/loginInfo";

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    name: "login page",
    component: () => import("../views/loginPage")
  },
  {
    path: '/todolist',
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
  },
    {
        path: '/test2',
        name: 'test mode2',
        component: () => import(/* webpackChunkName: "about" */ '../components/taskInfoForm')
    }
]

const router = new VueRouter({
  routes
})

router.beforeEach(function(to, from, next){
    if(to.name === "login page"){
        next();
        return;
    }

    if(loginInfo.isThereLoginInfo() === false){
        next("/");
        return;
    }

    window.axios.defaults.headers.common["Authorization"] = loginInfo.getLoginInfo();
    next();
})

export default router
