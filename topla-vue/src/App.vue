<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
      dark
    >
      <div class="d-flex align-center">
        TOPLA
      </div>
      <v-spacer></v-spacer>
      <v-btn icon @click="pushPage('/')"><v-icon>mdi-desk</v-icon></v-btn>
      <v-btn icon @click="pushPage('/preset')"><v-icon>mdi-calendar-heart</v-icon></v-btn>
      <v-btn icon @click="onLogoutButtonClicked" v-if="!isLoginPage"><v-icon>mdi-logout</v-icon></v-btn>
    </v-app-bar>

    <v-main>
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
import loginInfo from "@/plugins/loginInfo";

export default {
  name: 'App',

  data() {
    return {
    };
  },

  computed: {
    isLoginPage(){
      return this.$route.name === "login page";
    }
  },

  methods: {
    async pushPage(location){
      try{
        await this.$router.push(location)
      }
      catch{
        //아무것도 안 함.
        //같은 페이지로 이동시 예외가 던저지기 때문에 이렇게 함.
      }
    },

    onLogoutButtonClicked(){
      loginInfo.clearLoginInfo();
      this.pushPage("/");
    }
  }
};
</script>
