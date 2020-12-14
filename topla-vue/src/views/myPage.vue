<template>
  <div class="full-height back">
    <v-card fluid class="back">
      <v-card-title>마이 페이지</v-card-title>
      <v-divider></v-divider>
      <v-card-text>
        <span class="text-h5">{{ loginfo }}</span>
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
                @change="selectThemeApply($event)"
            >
              <v-radio
                  class="ma-2"
                  v-for="(theme,index) in themeList"
                  :label="theme"
                  :key="index"
                  :value="index"
              ></v-radio>
            </v-radio-group>
            <v-expand-transition>
              <div v-show="selectTheme===2">
                <v-list two-line class="back pl-3">
                  <v-list-item>
                    <v-list-item-content>
                      <v-list-item-title>주색</v-list-item-title>
                      <v-list-item-subtitle>주로 표시되는 색상</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <v-btn small :color="$store.state.customPrimary" @click="onColorChangeButtonClicked('primary')"></v-btn>
                    </v-list-item-action>
                  </v-list-item>

                  <v-list-item>
                    <v-list-item-content>
                      <v-list-item-title>보조색</v-list-item-title>
                      <v-list-item-subtitle>주색에 곁들여 사용할 색상</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <v-btn small :color="$store.state.customSecondary" @click="onColorChangeButtonClicked('secondary')"></v-btn>
                    </v-list-item-action>
                  </v-list-item>
                </v-list>
                <v-dialog v-model="colorPicker.show" width="300">
                  <v-card>
                    <v-color-picker
                        dot-size="30"
                        v-model="colorPicker.color"
                        swatches-max-height="225"
                    ></v-color-picker>
                    <v-card-actions>
                      <v-spacer></v-spacer>
                      <v-btn color="primary" @click="colorPicker.show=false">결정</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </div>
            </v-expand-transition>
          </div>
        </v-expand-transition>
        <v-btn
            block
            text
            @click="pushAlarmOnOff()"
        >
          push알림
          <v-spacer></v-spacer>
          <v-switch
              v-model="pushCheck"
              @click="pushAlarmOnOff()"
          ></v-switch>
        </v-btn>

        <v-btn
            block
            text
            @click="changePwd()"
        >
          비밀번호 변경
          <v-spacer></v-spacer>
        </v-btn>

        <v-btn
            block
            text
            @click="confrimDelete()"
            color="error"
        >
          <strong>회원탈퇴</strong>
          <v-spacer></v-spacer>
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
      themeList: ["밝은 테마", "클래식 테마", "사용자 정의 테마"],
      selectTheme: null,
      morningReportTime:{
        HH:"09",
        mm:"00"
      },
      eveningReportTime:{
        HH:"21",
        mm:"00"
      },
      colorPicker: {
        show: false,
        target: "primary",
        color: this.$store.state.customPrimary
      }
    }
  },

  watch: {
    "colorPicker.color"(newVal){
      if(this.colorPicker.target === "primary"){
        this.$store.state.customPrimary = newVal;
        themeList[2].primary = newVal;
        window.localStorage.setItem("customPrimary", newVal);
      }else{
        this.$store.state.customSecondary = newVal;
        themeList[2].secondary = newVal;
        window.localStorage.setItem("customSecondary", newVal);
      }
      this.selectTheme = 2;
      this.selectThemeApply();
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
      this.selectTheme=window.localStorage.getItem("theme")*1
      if(this.selectTheme===null)
        this.selectTheme=0

      let res = await this.$axios.get("/user")
      this.loginfo=res.data.email

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

    async pushAlarmOnOff() {
      this.pushCheck = !this.pushCheck;
      console.log(this.pushCheck)
      try{
        await this.$axios.put('/user/push',{
          "pushAlarmStatus":`${this.pushCheck}`
        })
      }
      catch (e){
        errorDialog(this,"시간등록 실패",e)
      }
    },

    selectThemeApply() {
      this.$store.commit("setNowTheme", this.selectTheme);
      let defTheme = this.$vuetify.theme.themes.light
      let theme= themeList[this.selectTheme]

      for(let color in theme){
        defTheme[color]=theme[color]
      }

      window.localStorage.setItem("theme", this.selectTheme);
    },

    async selectTimeApply(){
      try{
        await this.$axios.put(`/user`,{
          "morningReportTime":`${this.morningReportTime.HH}:${this.morningReportTime.mm}+09:00`,
          "eveningReportTime":null
        })
      }
      catch(e){
        errorDialog(this,"시간등록 실패",e)
      }
    },

    statistic(){
      this.$router.push("/stat");
    },

    changePwd(){
      this.pushPage("/changepwd");
    },

    async confrimDelete(){
      let res,res2
      res = await this.$dialog.error({
        title:"회원탈퇴를 진행 하시겠습니까?",
        text: "이 작업은 되돌릴 수 없습니다.",
        actions: {
          true: {
            text: "탈퇴합니다.",
            color: "error"
          },
          false: {
            text: "아니오",
            color: "success"
          }
        }
      })
      if(res === undefined){
        res = false;
      }

      if(res===true){
        res2 = await this.$dialog.error({
          title:"진짜로 탈퇴하시겠습니까?",
          text: "이 작업을 실행시 사용자의 모든 정보가 사라지게 됩니다.",
          actions: {
            true: {
              text: "탈퇴합니다.",
              color: "error"
            },
            false: {
              text: "아니오",
              color: "success"
            }
          }
        })
      }

      if(res2 === undefined){
        res = false;
      }

      try{
        if(res2===true){
          await this.$axios.delete("/user")
          loginInfo.clearLoginInfo();
          this.$store.commit("setLoginInfo", null);
          this.pushPage("/");
        }
      }
      catch (e) {
        errorDialog(this,"탈퇴를 실패하였습니다.",e)
      }
    },

    onColorChangeButtonClicked(target){
      this.colorPicker.target = target;
      this.colorPicker.show = true;
    }
  }
}
</script>

<style scoped>
.small-font {
  font-size: 3px;
}

.full-height{
  height:94vh;
}

</style>