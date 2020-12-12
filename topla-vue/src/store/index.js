import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

let vuex =  new Vuex.Store({
  state: {
    loginInfo: null,
    email: null
  },
  mutations: {
    setLoginInfo(state, loginInfo){
      state.loginInfo = loginInfo;
    },

    setUserEmail(state, email){
      state.email = email;
    }
  },
  actions: {
  },
  modules: {
  }
})

export default vuex;