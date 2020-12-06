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
              ></vue-timepicker>

            </v-card-text>
            <v-btn
                class="ma-1"
                @click="selectTimeApply"
            >
              적용
            </v-btn>

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

        <v-btn
            @click="themeListShow=!themeListShow"
            block
            text>
          테마
          <v-spacer></v-spacer>
        </v-btn>
        <v-expand-transition>
          <div v-show="themeListShow">
            <v-divider></v-divider>
            <v-radio-group
                class="small-font"
                v-model="selectTheme"
                @change="selectThemeTest()"
            >
              <v-radio
                  class="ma-2"
                  v-for="(theme,index) in themeList"
                  :label="theme"
                  :key="index"
                  :value="index"
              ></v-radio>
            </v-radio-group>
            <v-btn id="buttonPos"
                   @click="selectThemeApply()"
            >적용
            </v-btn>
          </div>
        </v-expand-transition>

      </v-card-text>
    </v-card>
  </div>
</template>

<script>
import loginInfo from "@/plugins/loginInfo";
import VueTimepicker from 'vue2-timepicker/src/vue-timepicker.vue'

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


  created() {
    this.loginfo = loginInfo.getLoginInfo()
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

    selectThemeTest() {
      console.log(this.selectTheme)
    },

    selectThemeApply() {
      console.log("apply")
      console.log(this.selectTheme)
      this.themeListShow = false
    },

    selectTimeApply(){
      console.log(this.morningReportTime.HH+"시"+this.morningReportTime.mm+"분")
      console.log(this.eveningReportTime.HH+"시"+this.eveningReportTime.mm+"분")
      this.timeSet=false
    }
  }
}
</script>

<style scoped>
.small-font {
  font-size: 3px;
}

</style>