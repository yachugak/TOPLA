import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

let vuex =  new Vuex.Store({
  state: {
    loginInfo: null,
    email: null,
    isSeenGuideBook: true
  },
  mutations: {
    setLoginInfo(state, loginInfo){
      state.loginInfo = loginInfo;
    },

    setUserEmail(state, email){
      state.email = email;
    },

    setGuideBookState(state, booleanValue){
      state.isSeenGuideBook = booleanValue;
    }
  },
  actions: {
  },
  modules: {
  }
})

export default vuex;