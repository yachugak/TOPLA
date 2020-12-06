<template>
  <div>
    <v-card>
      <v-card-title>마이 페이지</v-card-title>
      <v-col cols="6">
        <span>이메일 : {{loginfo}}</span>
      </v-col>
      <v-col cols="6">
        <v-btn
            :text="true"
            @click="onLogoutButtonClicked()"
            color="error"
        >로그아웃</v-btn>
      </v-col>
    </v-card>

    <v-card>
      <v-card-title
      >일반</v-card-title>
      <v-col cols="6">
        <v-btn
            @click="presetSet()"
            :text="true"
        >스케줄 프리셋 설정</v-btn>
      </v-col>

      <v-col cols="6">
        <v-btn
            :text="true"
        >리포트시간 설정</v-btn>
      </v-col>

      <v-col cols="6">
        <v-btn
            :text="true"
        >푸쉬알림</v-btn>
      </v-col>

      <v-col cols="6">
        <v-dialog
            v-model="dialog"
            width="500"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                text
                v-bind="attrs"
                v-on="on"
            >
              테마 선택
            </v-btn>
          </template>

          <v-card>
            <v-card-title class="headline grey lighten-2">
              테마
            </v-card-title>

            <v-card-text>
              테마를 선택하세요<br>
              1<br>
              2<br>
              3
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                  color="primary"
                  text
                  @click="dialog = false"
              >
                취소
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-col>
    </v-card>

  </div>
</template>

<script>
import loginInfo from "@/plugins/loginInfo";

export default {
  name: "myPage",
  data(){
    return{
      loginfo:null,
      themeDialog:false,
      dialog: false,
    }
  },


  created() {
    this.loginfo=loginInfo.getLoginInfo()
  },

  methods:{
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

    presetSet(){
      this.pushPage("/preset");
    },

    selectTheme(){
      console.log()
    }
}


}
</script>

<style scoped>

</style>