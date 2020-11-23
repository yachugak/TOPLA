<template>
  <div id="backgroundDiv" class="secondary">
    <v-card>
      <v-card-title>{{title}}</v-card-title>
      <v-form ref="form">
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
        <div class="buttonGroup">
          <v-btn v-if="!isRegisterMode" :loading="callCount>0" color="primary" class="mr-2" @click="isRegisterMode=true">회원가입</v-btn>
          <v-btn v-else color="secondary" :loading="callCount>0" class="mr-2" @click="isRegisterMode = false">취소</v-btn>
          <v-btn v-if="!isRegisterMode" :loading="callCount>0" color="primary" @click="onLoginButtonClicked()">로그인</v-btn>
          <v-btn v-else color="primary" :loading="callCount>0" @click="onRegisterButtonClicked()">회원 등록</v-btn>
        </div>
      </v-form>
    </v-card>
  </div>
</template>

<script>
import VuetifyJetValidator from "vuetify-jet-validator";
import loginInfo from "@/plugins/loginInfo";

export default {
  name: "loginPage",
  data() {
    const validator = new VuetifyJetValidator();
    return {
      isShowPassword: false,
      isRegisterMode: false,
      callCount: 0,

      formInput: {
        account: "",
        password: ""
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
    title(){
      return this.isRegisterMode ? "회원 등록" : "로그인";
    }
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
        let accountCopy = this.formInput.account;
        await this.$axios.post("/user/login", {
          email: this.formInput.account,
          password: this.formInput.password
        });

        loginInfo.setLoginInfo(accountCopy);
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
      if(this.$refs.form.validate() === false){
        return;
      }

      this.callCount++;
      try {
        await this.$axios.post(`/user`, {
          email: this.formInput.account,
          password: this.formInput.password
        });

        this.$dialog.notify.success("계정이 생성되었습니다. 로그인 해 보세요!");
        this.$refs.form.reset();
      }
      catch (e){
        this.$dialog.error({
          title: "입구컷 당했습니다.",
          text: e.response.data.message
        });
      }
      finally {
        this.callCount--;
      }
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