import Vue from 'vue'
import VueRouter from 'vue-router'
import loginInfo from "@/plugins/loginInfo";
import store from "@/store/index.js";

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
        path: '/search',
        name: 'search page',
        component: () => import(/* webpackChunkName: "about" */ '../views/taskSearch.vue')
    },

    {
        path: '/test',
        name: 'test mode',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import(/* webpackChunkName: "about" */ '../components/changePassword.vue')
    },
    {
        path: '/changepwd',
        name: 'changepwd mode',
        component: () => import(/* webpackChunkName: "about" */ '../components/changePassword.vue')
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
        path: '/mypage',
        name: 'mypage mode',
        component: () => import(/* webpackChunkName: "about" */ '../views/myPage.vue')
    },

    {
        path: '/test2',
        name: 'test mode2',
        component: () => import(/* webpackChunkName: "about" */ '../components/taskInfoForm')
    },

    {
        path: '/all',
        name: 'all mode',
        component: () => import(/* webpackChunkName: "about" */ '../views/allTask.vue')
    },

    {
        path: "/stat",
        name: "statistic mode",
        component: () => import("../views/statistic.vue")
    }
]

const router = new VueRouter({
    routes
})

router.beforeEach(function (to, from, next) {
    if (to.name === "login page") {
        next();
        return;
    }

    if (store.state.loginInfo === null) {
        next("/");
        return;
    }

    window.axios.defaults.headers.common["Authorization"] = loginInfo.getLoginInfo();
    next();
})

export default router
