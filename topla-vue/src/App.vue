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
      <v-btn icon @click="pushPage('/search')" v-if="isLogined && !isSuperUser">
        <v-icon>mdi-magnify</v-icon>
      </v-btn>
      <v-btn icon @click="onSwapButtonClicked()" v-if="isShowSwapButton">
        <v-icon>mdi-swap-horizontal</v-icon>
      </v-btn>
    </v-app-bar>

    <v-navigation-drawer
        v-model="isShowDrawer"
        fixed
        temporary
        left
    >
      <v-list nav dense>

        <v-list-item-group v-model="selectedNavItem">
          <v-list-item value="mypage" @click="onNavSelected('myPage')" v-if="!isSuperUser">
            <v-list-item-icon>
              <v-icon>mdi-account</v-icon>
            </v-list-item-icon>
            <v-list-item-title>마이 페이지</v-list-item-title>
          </v-list-item>
          <v-list-item value="all" @click="onNavSelected('all')" v-if="!isSuperUser">
            <v-list-item-icon>
              <v-icon>mdi-alpha-a-box</v-icon>
            </v-list-item-icon>
            <v-list-item-title>모든 작업</v-list-item-title>
          </v-list-item>
          <v-list-item value="todo" @click="onNavSelected('todo')" v-if="!isSuperUser">
            <v-list-item-icon>
              <v-icon>mdi-calendar-today</v-icon>
            </v-list-item-icon>
            <v-list-item-title>일간 작업 보기</v-list-item-title>
          </v-list-item>
          <v-list-item value="month" @click="onNavSelected('month')" v-if="!isSuperUser">
            <v-list-item-icon>
              <v-icon>mdi-calendar-month</v-icon>
            </v-list-item-icon>
            <v-list-item-title>월간 작업 보기</v-list-item-title>
          </v-list-item>
          <v-list-item value="schedulePreset" @click="onNavSelected('schedulePreset')" v-if="!isSuperUser">
            <v-list-item-icon>
              <v-icon>mdi-calendar-heart</v-icon>
            </v-list-item-icon>
            <v-list-item-title>스케줄 프리셋 설정</v-list-item-title>
          </v-list-item>
          <v-list-item value="stat" @click="onNavSelected('stat')">
            <v-list-item-icon>
              <v-icon>mdi-chart-areaspline</v-icon>
            </v-list-item-icon>
            <v-list-item-title>통계</v-list-item-title>
          </v-list-item>
          <v-list-item value="guide" @click="onNavSelected('guide')">
            <v-list-item-icon>
              <v-icon>mdi-book-information-variant</v-icon>
            </v-list-item-icon>
            <v-list-item-title>안내서</v-list-item-title>
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
import themeList from "@/plugins/theme";

export default {
  name: 'App',

  data() {
    return {
      isShowDrawer: false,
      selectedNavItem: null
    };
  },

  created(){
    this.setLoginInfoToVuex();
    window.dialog = this.$dialog;

    let selectTheme=window.localStorage.getItem("theme")*1
    let defTheme = this.$vuetify.theme.themes.light
    let theme= themeList[selectTheme]
    for(let color in theme){
      defTheme[color]=theme[color]
    }

    this.checkAuthToken();
    this.loadisSeenGuidBook();
  },

  watch: {
    isShowDrawer(newVal) {
      if (newVal === true) {
        this.matchPage();
      }
    }
  },

  computed: {
    isLoginPage() {
      return this.$route.name === "login page";
    },

    isLogined() {
      if (this.$store.state.loginInfo === null) {
        return false;
      }

      return true;
    },

    sidleBarMargin() {
      return {
        "mobile-margin": !this.$vuetify.breakpoint.mdAndUp
      }
    },

    isShowSwapButton() {
      if (this.isLogined === false) {
        return false;
      }

      let pageName = this.$route.name;
      return pageName === "todolist mode" || pageName === "calendar mode";
    },

    isSuperUser(){
      return this.$store.state.isSuperUser;
    }
  },

  methods: {
    async pushPage(location) {
      try {
        await this.$router.push(location)
      } catch {
        //아무것도 안 함.
        //같은 페이지로 이동시 예외가 던저지기 때문에 이렇게 함.
      }
    },

    onLogoutButtonClicked() {
      loginInfo.clearAll()
      this.$store.commit("setLoginInfo", null);
      this.$store.commit("setUserEmail", null);
      this.pushPage("/");
    },

    onNavSelected(mode) {
      switch (mode) {
        case "myPage":
          this.pushPage("/mypage");
          break;
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
        case "all":
          this.pushPage("/all");
          break;
        case "stat":
          this.pushPage("/stat");
          break;
        case "guide":
          this.pushPage("/guide");
          break;
      }

      this.isShowDrawer = false;
    },

    onSwapButtonClicked() {
      let pageName = this.$route.name;
      if (pageName === "todolist mode") {
        this.pushPage("/calendar");
        this.$dialog.message.info("월간 보기 화면으로 전환합니다.", {timeout: 800});
        return
      } else if (pageName === "calendar mode") {
        this.pushPage("/")
        this.$dialog.message.info("일간 보기 화면으로 전환합니다.", {timeout: 800});
        return
      }

      this.pushPage("/");
    },

    matchPage() {
      let pageName = this.$route.name;
      console.log(pageName);
      switch (pageName) {
        case "todolist mode":
          this.selectedNavItem = "todo";
          break;

        case "calendar mode":
          this.selectedNavItem = "month";
          break;

        case "all mode":
          this.selectedNavItem = "all";
          break;

        case "preset mode":
          this.selectedNavItem = "schedulePreset";
          break;

        case "mypage mode":
          this.selectedNavItem = "mypage";
          break;

        case "statistic mode":
          this.selectedNavItem = "stat";
          break;

        case "user guide mode":
          this.selectedNavItem = "guide";
          break;

        default:
          this.selectedNavItem = null;
          break;
      }
    },

    setLoginInfoToVuex(){
      if(loginInfo.isThereLoginInfo()){
        this.$store.commit("setLoginInfo", loginInfo.getLoginInfo());
      }

      if(loginInfo.isThereUserEmail()){
        this.$store.commit("setUserEmail", loginInfo.getUserEmail());
      }

      if(loginInfo.isThereSuperUserFlag()){
        this.$store.commit("setSuperUserFlag", loginInfo.getSuperUserFlag());
      }
    },

    async checkAuthToken(){
      if(this.$store.state.loginInfo === null){
        return;
      }

      let successFlag = false;

      try{
        let res = await this.$axios.post("/user/securecode/check", {
          secureCode: this.$store.state.loginInfo
        });
        if(res.data === "true" || res.data === true){
          successFlag = true;
        }else{
          successFlag = false;
        }
      }catch{
        successFlag = false;
      }

      if(successFlag === false){
        this.onLogoutButtonClicked();
      }
    },

    loadisSeenGuidBook(){
      let info = window.localStorage.getItem("isSeenGuideBook");

      if(info === null || info === undefined){
        this.$store.commit("setGuideBookState", false);
        return;
      }

      if(info === true || info === "true"){
        this.$store.commit("setGuideBookState", true);
        return;
      }

      if(info === false || info === "false"){
        this.$store.commit("setGuideBookState", false);
        return;
      }

      this.$store.commit("setGuideBookState", true);
    },

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
