import Vue from 'vue'
import VueRouter from 'vue-router'
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
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
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
        path: '/superuser',
        name: 'superuser mode',
        component: () => import(/* webpackChunkName: "about" */ '../views/superUser.vue')
    },

    {
        path: '/forbidden',
        name: 'forbidden page',
        component: () => import(/* webpackChunkName: "about" */ '../views/forbidden.vue')
    },
]

const router = new VueRouter({
    routes
})

router.beforeEach(function (to, from, next) {
    if(store.state.loginInfo === null && to.name === "login page"){
        //로그인 정보 없는 사람이 로그인 페이지 진입은 허용
        next();
        return;
    }
    else if(store.state.loginInfo !== null && to.name === "login page"){
        //로그인 정보 있는데 로그인페이지로 가는 건 블락
        next("/todolist");
    }
    else if(store.state.loginInfo === null && to.name !== "login page"){
        //로그인 정보 없는데 로그인페이지 말고 다른 페이지는 못 감
        next("/");
        return;
    }

    window.axios.defaults.headers.common["Authorization"] = store.state.loginInfo;

    //여기서부터는 로그인된 사람이 로그인 페이말고 다른 곳을 갈 때의 처리

    if(store.state.isSuperUser && to.name !== "superuser mode"){
        //슈퍼유저가 슈퍼유저 페이지 말고 다른 곳 가는 것은 허용하지 않음.
        next("/superuser");
        return;
    }
    else if(store.state.isSuperUser === false && to.name === "superuser mode"){
        //슈퍼 유저가 아닌 사람이 슈퍼 유저 페이지로 가는 건 허용되지 않음
        next("/forbidden");
        return;
    }

    //그 외에는 자유롭게 허용
    next();
})

export default router
