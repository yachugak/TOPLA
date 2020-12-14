<template>
  <div id="backgroundDiv" class="back pa-4">
    <v-card class="pa-4 mx-auto" max-width="1000">
      <v-form v-if="currentMode==='login'" ref="form">
        <v-text-field
            label="계정"
            outlined
            v-model="formInput.account"
            :rules="rules.account"
        >
        </v-text-field>
        <v-text-field
            label="비밀번호"
            outlined
            :type="isShowPassword ? 'text' : 'password'"
            :append-icon="isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append="isShowPassword = !isShowPassword"
            v-model="formInput.password"
            :rules="rules.password"
        >
        </v-text-field>
        <v-btn text @click="currentMode='findPassword'">비밀번호가 기억나지 않으십니까?</v-btn>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="onRegisterButtonClicked()" :loading="callCount>0">회원가입</v-btn>
          <v-btn color="primary" @click="onLoginButtonClicked()" :loading="callCount>0">로그인</v-btn>
        </v-card-actions>
      </v-form>
      <user-register-form v-if="currentMode==='register'" @backLogin="currentMode='login'"></user-register-form>
      <find-password v-if="currentMode==='findPassword'" @back="currentMode='login'"></find-password>
    </v-card>
  </div>
</template>

<script>
import VuetifyJetValidator from "vuetify-jet-validator";
import loginInfo from "@/plugins/loginInfo";
import userRegisterForm from "@/components/userRegisterForm";
import findPassword from "@/components/findPassword";

export default {
  name: "loginPage",

  components: {
    userRegisterForm,
    findPassword
  },

  data() {
    const validator = new VuetifyJetValidator();
    return {
      isShowPassword: false,
      currentMode: "login",
      callCount: 0,

      formInput: {
        account: "",
        password: "",
      },

      rules: {
        account: [
            validator.rules.required("필수값입니다."),
            validator.rules.email("이메일 형식이여야 합니다.")
        ],

        password: [
            validator.rules.required("필수값입니다."),
            validator.rules.minLength(4, "최소 4자 이상 넣어야 합니다."),
            validator.rules.maxLength(30, "30자를 초과해서 넣을 수 없습니다.")
        ]
      }
    }
  },

  computed: {
  },

  watch: {
  },

  mounted() {
    this.checkLoginInfo();
  },

  methods: {
    async onLoginButtonClicked(){
      let validationResultFlag = this.$refs.form.validate();
      if(validationResultFlag === false){
        return;
      }

      this.callCount++;
      try {
        let copiedEmail = this.formInput.account;
        let copiedPassword = this.formInput.password;

        let authToken = await this.superUserLogin(copiedEmail, copiedPassword);

        console.log("authToken", authToken);
        //관리자 계정이 아니면 일반 회원 로그인 시도.
        if(authToken === null){
          console.log("일반");
          loginInfo.setSuperUserFlag(false);
          this.$store.commit("setSuperUserFlag", false);
          let res = await this.$axios.post("/user/login", {
            email: copiedEmail,
            password: copiedPassword
          });
          authToken = res.data;
        }
        else{
          console.log("관리자");
          loginInfo.setSuperUserFlag(true);
          this.$store.commit("setSuperUserFlag", true);
        }

        loginInfo.setLoginInfo(authToken);
        loginInfo.setUserEmail(copiedEmail);
        this.$store.commit("setLoginInfo", authToken);
        this.$store.commit("setUserEmail", copiedEmail);
        window.axios.defaults.headers.common["Authorization"] = authToken;

        await window.axios.put("/user/token", {
          "deviceToken": window.myDeviceKey
        })

        if(this.$store.state.isSuperUser){
          this.$router.push("/superuser");
        }
        else {
          this.$router.push("/todolist");
        }
      }
      catch(e){
        this.$dialog.error({
          title: "로그인 실패",
          text: e.response.data.message
        });
      }
      finally {
        this.callCount--;
      }
    },

    //관리자 로그인에 성공하면 인증토큰 반환, 실패하면 null 반환.
    async superUserLogin(account, password){
      this.callCount++;
      let authToken = null;
      try{
        let res = await this.$axios.post("/superuser/login",{
          email: account,
          password: password
        })

        authToken = res.data;
      }
      catch{
        loginInfo.setSuperUserFlag(false);
        this.$store.commit("setSuperUserFlag", false);
        authToken = null;
      }
      finally {
        this.callCount--;
      }

      return authToken;
    },

    async onRegisterButtonClicked(){
      this.currentMode = "register"
    },

    checkLoginInfo(){
      if(loginInfo.isThereLoginInfo()){
        this.$axios.defaults.headers.common["Authorization"] = loginInfo.getLoginInfo();
        this.$router.push("/todolist");
      }
    }
  }
}
</script>

<style scoped>
#backgroundDiv {
  height: 100%;
}

.buttonGroup {
  display: flex;
  flex-flow: row nowrap;
  justify-content: flex-end;
}


</style>