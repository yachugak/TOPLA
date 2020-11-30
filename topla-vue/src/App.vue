<template>
  <v-app>
    <v-app-bar
      app
      color="primary darken-2"
      dark
    >
      <v-app-bar-nav-icon @click="isShowDrawer = !isShowDrawer" v-if="isLogined"></v-app-bar-nav-icon>
      <v-toolbar-title>TOPLA</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn icon @click="pushPage('/search')" v-if="isLogined"><v-icon>mdi-magnify</v-icon></v-btn>
      <v-btn icon @click="pushPage('/')" v-if="isLogined"><v-icon>mdi-desk</v-icon></v-btn>
      <v-btn icon @click="pushPage('/calendar')" v-if="isLogined"><v-icon>mdi-calendar-month</v-icon></v-btn>
    </v-app-bar>

    <v-navigation-drawer
        v-model="isShowDrawer"
        fixed
        temporary
        left
    >
      <v-list nav dense>
        <v-list-item-group>
          <v-list-item value="todo" @click="onNavSelected('todo')">
            <v-list-item-icon><v-icon>mdi-desk</v-icon></v-list-item-icon>
            <v-list-item-title>작업 확인</v-list-item-title>
          </v-list-item>
          <v-list-item value="month" @click="onNavSelected('month')">
            <v-list-item-icon><v-icon>mdi-calendar-month</v-icon></v-list-item-icon>
            <v-list-item-title>월간 보기</v-list-item-title>
          </v-list-item>
          <v-list-item value="schedulePreset" @click="onNavSelected('schedulePreset')">
            <v-list-item-icon><v-icon>mdi-calendar-heart</v-icon></v-list-item-icon>
            <v-list-item-title>스케줄 프리셋 설정</v-list-item-title>
          </v-list-item>
          <v-list-item value="logout" @click="onNavSelected('logout')">
            <v-list-item-icon><v-icon>mdi-logout</v-icon></v-list-item-icon>
            <v-list-item-title>로그아웃</v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list>
    </v-navigation-drawer>

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
      isShowDrawer: false,
    };
  },

  computed: {
    isLoginPage(){
      return this.$route.name === "login page";
    },

    isLogined(){
      if(this.$store.state.loginInfo === null){
        return false;
      }

      return true;
    },

    sidleBarMargin(){
      return {
        "mobile-margin": !this.$vuetify.breakpoint.mdAndUp
      }
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
      this.$store.commit("setLoginInfo", null);
      this.pushPage("/");
    },

    onNavSelected(mode){
      switch (mode){
        case "todo":
          this.pushPage("/");
          break;
        case "month":
          this.pushPage("/calendar");
          break;
        case "schedulePreset":
          this.pushPage("/preset");
          break;
        case "logout":
          this.onLogoutButtonClicked();
          break;
      }

      this.isShowDrawer = false;
    }
  }
};
</script>

<style scoped>
.desktop-margin {
  margin-top: 64px;
}

.mobile-margin {
  margin-top: 56px;
}
</style>
