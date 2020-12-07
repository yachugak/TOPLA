<template>
  <div>
    <v-card fluid>
      <v-card-title>마이 페이지</v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        이메일 : {{ loginfo }}
        <v-btn
            text

            @click="onLogoutButtonClicked()"
            color="error">
          <strong>로그아웃</strong>
        </v-btn>

      </v-card-text>

      <v-divider></v-divider>

      <v-card-title>
        설정
      </v-card-title>

      <v-card-text>
        <v-btn
            block
            @click="presetSet()"
            text>
          스케줄 프리셋 설정
          <v-spacer></v-spacer>
        </v-btn>

        <v-btn
            @click="timeSet=!timeSet"
            block
            text>
          리포트시간 설정
          <v-spacer></v-spacer>
          <v-icon v-if="!timeSet">mdi-chevron-down</v-icon>
          <v-icon v-if="timeSet">mdi-chevron-up</v-icon>
        </v-btn>

        <v-expand-transition>
          <div v-if="timeSet">
            <v-card-text>
              <span>
                아침 알림 시간 :
              </span>
              <vue-timepicker
                  v-model="morningReportTime"
                  :minute-interval="10"
                  close-on-complete
                  @change="selectTimeApply"
              ></vue-timepicker>
            </v-card-text>

            <v-card-text>
              <span>
                저녁 알림 시간 :
              </span>
              <vue-timepicker
                  v-model="eveningReportTime"
                  :minute-interval="10"
                  close-on-complete
                  @change="selectTimeApply"
              ></vue-timepicker>
            </v-card-text>
          </div>
        </v-expand-transition>

        <v-btn
            @click="themeListShow=!themeListShow"
            block
            text>
          테마
          <v-spacer></v-spacer>
          <v-icon v-if="themeListShow">mdi-chevron-up</v-icon>
          <v-icon v-if="!themeListShow">mdi-chevron-down</v-icon>
        </v-btn>
        <v-expand-transition>
          <div v-show="themeListShow">
            <v-divider></v-divider>
            <v-radio-group
                class="small-font"
                v-model="selectTheme"
                @change="selectThemeApply()"
            >
              <v-radio
                  class="ma-2"
                  v-for="(theme,index) in themeList"
                  :label="theme"
                  :key="index"
                  :value="index"
              ></v-radio>
            </v-radio-group>
          </div>
        </v-expand-transition>
        <v-btn
            block
            text
            @click="pushAlarmOnOff()"
        >
          push알림
          <v-spacer></v-spacer>
          <div v-if="pushCheck">
            <strong>o n</strong>
            <v-icon>mdi-toggle-switch</v-icon>
          </div>

          <div v-if="!pushCheck">
            <strong>off</strong>
            <v-icon>mdi-toggle-switch-off</v-icon>
          </div>

        </v-btn>

      </v-card-text>
      <v-divider></v-divider>
      <v-card-title>
        통계
      </v-card-title>
      <v-card-text>
        <v-btn
            block
            text
            @click="statistic()"
        >
          통계보기
          <v-spacer></v-spacer>
        </v-btn>
      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import loginInfo from "@/plugins/loginInfo";
import VueTimepicker from 'vue2-timepicker/src/vue-timepicker.vue'
import errorDialog from "@/plugins/errorDialog";
import themeList from '@/plugins/theme';

export default {
  name: "myPage",
  components:{
    VueTimepicker
  },

  data() {
    return {
      loginfo: null,
      themeDialog: false,
      dialog: false,
      pushCheck: true,
      themeListShow: false,
      timeSet:false,
      themeList: ["밝은 테마", "어두운 테마", "클래식 테마"],
      selectTheme: 0,
      morningReportTime:{
        HH:"09",
        mm:"00"
      },
      eveningReportTime:{
        HH:"21",
        mm:"00"
      }
    }
  },


  async created() {
    await this.preSetting()
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

    async preSetting(){
      this.loginfo = loginInfo.getLoginInfo()
      let res = await this.$axios.get("/user")

      this.eveningReportTime.HH=res.data.eveningReportTime.substring(0,2)
      this.eveningReportTime.mm=res.data.eveningReportTime.substring(3,5)

      this.morningReportTime.HH=res.data.morningReportTime.substring(0,2)
      this.morningReportTime.mm=res.data.morningReportTime.substring(3,5)

    },

    onLogoutButtonClicked() {
      loginInfo.clearLoginInfo();
      this.$store.commit("setLoginInfo", null);
      this.pushPage("/");
    },

    presetSet() {
      this.pushPage("/preset");
    },

    pushAlarmOnOff() {
      this.pushCheck = !this.pushCheck;
      console.log(this.pushCheck)
    },

    selectThemeApply() {
      console.log(`${this.selectTheme}`+"theme apply")
      let defTheme = this.$vuetify.theme.themes.light
      let theme= themeList[this.selectTheme]

      for(let color in theme){
        defTheme[color]=theme[color]
      }
    },

    async selectTimeApply(){
      try{
        await this.$axios.put(`/user`,{
          "morningReportTime":`${this.morningReportTime.HH}:${this.morningReportTime.mm}+09:00`,
          "eveningReportTime":`${this.eveningReportTime.HH}:${this.eveningReportTime.mm}+09:00`
        })
      }
      catch(e){
        errorDialog(this,"시간등록 실패",e)
      }
    },

    statistic(){
      console.log("통계창 추가예정")
    }
  }
}
</script>

<style scoped>
.small-font {
  font-size: 3px;
}

</style>