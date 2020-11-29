import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

let vuex =  new Vuex.Store({
  state: {
    loginInfo: null
  },
  mutations: {
    setLoginInfo(state, loginInfo){
      state.loginInfo = loginInfo;
    }
  },
  actions: {
  },
  modules: {
  }
})

export default vuex;