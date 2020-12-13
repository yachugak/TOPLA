<template>
  <div id="backgroundDiv" class="back pa-4">
    <v-card class="pa-4">
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
        let res = await this.$axios.post("/user/login", {
          email: this.formInput.account,
          password: this.formInput.password
        });

        loginInfo.setLoginInfo(res.data);
        loginInfo.setUserEmail(this.formInput.account);
        this.$store.commit("setLoginInfo", res.data);
        this.$store.commit("setUserEmail", copiedEmail);
        window.axios.defaults.headers.common["Authorization"] = loginInfo.getLoginInfo();

        await window.axios.put("/user/token", {
          "deviceToken": window.myDeviceKey
        })

        await this.$router.push("/todolist");
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