import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

let vuex =  new Vuex.Store({
  state: {
    loginInfo: null,
    email: null,
    isSeenGuideBook: true,
    isSuperUser: false,

    customPrimary: "#1976D2",
    customSecondary: "#424242",

    nowTheme: 0
  },
  mutations: {
    setLoginInfo(state, loginInfo){
      state.loginInfo = loginInfo;
    },

    setUserEmail(state, email){
      state.email = email;
    },

    setGuideBookState(state, booleanValue) {
      state.isSeenGuideBook = booleanValue;
    },

    setSuperUserFlag(state, flagValue){
      state.isSuperUser = flagValue;
    },

    setCustomTheme(state, primary, secondary){
      state.customPrimary = primary;
      state.customSecondary = secondary;
    },

    setNowTheme(state, themeNumber){
      state.nowTheme = themeNumber;
    }
  },
  actions: {
  },
  modules: {
  }
})

export default vuex;