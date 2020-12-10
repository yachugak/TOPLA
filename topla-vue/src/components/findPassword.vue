<template>
  <div>
    <v-card-title>비밀번호 찾기</v-card-title>
    <v-stepper v-model="step">
      <v-stepper-header>
        <v-stepper-step :complete="step > 1" :step="1">
          임시 비밀번호 발급
        </v-stepper-step>
        <v-stepper-step :complete="step > 2" :step="2">
          임시 비밀번호 인증
        </v-stepper-step>
        <v-stepper-step :complete="step > 3" :step="3">
          새 비밀번호로 변경
        </v-stepper-step>
        <v-stepper-step :complete="step > 4" :step="4">
          완료
        </v-stepper-step>
      </v-stepper-header>

      <v-stepper-items>
        <v-stepper-content :step="1">
          <div>
            <v-alert type="info" >
              비밀번호를 까먹어 임시 비밀번호를 발급받고자 하는 계정(이메일)을 입력해 주세요.
              이 절차가 진행되면 해당 계정의 비밀번호가 임시 비밀번호로 변경됩니다.
            </v-alert>
            <v-form ref="form1">
              <v-text-field
                  label="계정"
                  outlined
                  v-model="formInput.account"
                  :rules="rules.account"
                  :disabled="callCount>0"
              >
              </v-text-field>
            </v-form>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="secondary" :loading="callCount>0" @click="backStep()">뒤로</v-btn>
              <v-btn color="primary" :loading="callCount>0" @click="requestTempPassword()">임시 비밀번호 발급</v-btn>
            </v-card-actions>
          </div>
        </v-stepper-content>

        <v-stepper-content :step="2">
          <v-alert type="info" >
            임시 비밀번호가 발급되었습니다.
            임시 비밀번호로도 TOPLA 서비스를 이용할 수 있지만 보안을 위해 비밀번호 변경 절차를 밟겠습니다.
            비밀번호 변경 없이 서비스를 이용하실 분들은 "로그인 페이지로 돌아가기" 버튼을 눌러 로그인을 진행하시기 바랍니다.
          </v-alert>
          <v-form ref="form2">
            <v-text-field
                label="계정"
                outlined
                v-model="formInput.account"
                :rules="rules.account"
                disabled
            >
            </v-text-field>
            <v-text-field
                label="임시 비밀번호"
                outlined
                :type="isShowPassword ? 'text' : 'password'"
                :append-icon="isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="isShowPassword = !isShowPassword"
                v-model="formInput.oldPassword"
                :rules="rules.password"
                :disabled="callCount > 0"
            >
            </v-text-field>
          </v-form>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="secondary" :loading="callCount>0" @click="backStep()">뒤로</v-btn>
            <v-btn color="primary" :loading="callCount>0" @click="onRequestLogin()">임시 비밀번호 인증</v-btn>
          </v-card-actions>
        </v-stepper-content>

        <v-stepper-content :step="3">
          <v-alert type="info">새 비밀번호로 변경합니다.</v-alert>
          <v-form ref="form3">
            <v-text-field
                label="계정"
                outlined
                v-model="formInput.account"
                :rules="rules.account"
                disabled
            >
            </v-text-field>
            <v-text-field
                label="기존 비밀번호"
                outlined
                :type="isShowPassword ? 'text' : 'password'"
                :append-icon="isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="isShowPassword = !isShowPassword"
                v-model="formInput.oldPassword"
                :rules="rules.password"
                disabled
            >
            </v-text-field>
            <v-text-field
                label="새 비밀번호"
                outlined
                :type="isShowPassword ? 'text' : 'password'"
                :append-icon="isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="isShowPassword = !isShowPassword"
                v-model="formInput.password"
                :rules="rules.password"
                :disabled="callCount>0"
            >
            </v-text-field>
            <v-text-field
                label="새 비밀번호 확인"
                outlined
                :type="isShowPassword ? 'text' : 'password'"
                :append-icon="isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
                @click:append="isShowPassword = !isShowPassword"
                v-model="formInput.confirmPassword"
                :rules="rules.password"
                :disabled="callCount > 0"
            >
            </v-text-field>
          </v-form>
          <v-card-actions>
            <v-spacer> </v-spacer>
            <v-btn color="secondary" :loading="callCount>0" @click="backWithAlert()">뒤로</v-btn>
            <v-btn color="primary" :loading="callCount>0" @click="requestPasswordChagne()">비밀번호 변경</v-btn>
          </v-card-actions>
        </v-stepper-content>

        <v-stepper-content :step="4">
          <div class="text-center py-3">
            <h1>비밀번호 변경이 완료되었습니다.</h1>
            <p>이제 바뀐 비밀번호로 접속할 수 있습니다.</p>
            <v-btn color="primary" @click="onBackLogin()">로그인 페이지로 돌아가기</v-btn>
          </div>
        </v-stepper-content>
      </v-stepper-items>
    </v-stepper>
  </div>
</template>

<script>
import VuetifyJetValidator from "vuetify-jet-validator";
import errorDialog from "@/plugins/errorDialog";

export default {
  name: "findPassword",

  data() {
    const validator = new VuetifyJetValidator();

    return {
      step: 1,
      callCount: 0,
      isShowPassword: false,

      formInput: {
        account: "",
        oldPassword: "",
        password: "",
        confirmPassword: "",
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

  methods: {
    async backWithAlert(){
      let result = await this.$dialog.warning({
        title: "비밀번호 변경 작업 중단",
        text: "비밀번호를 변경하지 않으려고 하고 계십니다. 임시 비밀번호는 보안상 안전하지 않습니다. 정말로 비밀번호를 변경하지 않으시겠습니까?",
        width: 500,
        actions: {
          true: {
            text: "예",
            color: "error"
          },

          false: {
            text: "아니오",
            color: "success"
          }
        }
      });

        if(result !== true){
          return;
        }
        this.backStep();
    },

    onBackLogin(){
      this.$emit("back");
    },

    backStep(){
      this.step--;
      if(this.step < 1){
        this.step = 1;
        this.onBackLogin();
      }
    },

    nextStep(){
      this.step++;
      if(this.step > 4){
        this.step = 4
      }
    },

    async requestTempPassword(){
      if(this.$refs.form1.validate() === false){
        return;
      }

      this.callCount++;
      try{
        await this.$axios.put("/user/lostpassword", {
          email: this.formInput.account
        });
        this.nextStep();
        this.$dialog.notify.success(`임시 비밀번호가 발급되었습니다. ${this.formInput.account}으로 임시 비밀번호가 발송되었으니 확인 부탁드립니다.`);
      }catch (e) {
        errorDialog(this, "임시 비밀번호 발급 실패", e);
      }finally {
        this.callCount--;
      }
    },

    async onRequestLogin(){
      if(this.$refs.form2.validate() === false){
        return;
      }

      this.callCount++;
      try {
        await this.$axios.post("/user/login", {
          email: this.formInput.account,
          password: this.formInput.oldPassword
        });
        this.nextStep();
      }catch (e) {
        errorDialog(this, "임시 비밀번호로 로그인 실패", e);
      }finally {
        this.callCount--;
      }
    },

    async requestPasswordChagne(){
      if(this.$refs.form3.validate() === false){
        return;
      }

      if(this.formInput.password !== this.formInput.confirmPassword){
        this.$dialog.error({
          title: "잘못된 비밀번호",
          text: "비밀번호화 비밀번호 확인이 일치하지 않습니다."
        });
        return
      }

      this.callCount++;
      try{
        await this.$axios.put("/user/password", {
          oldPassword: this.formInput.oldPassword,
          newPassword: this.formInput.password
        },
        {
          headers: {
            Authorization: this.formInput.account
          }
        });

        this.nextStep();
      }catch (e) {
        errorDialog(this, "비밀번호 변경 실패", e);
      }finally {
        this.callCount--;
      }
    }
  }
}
</script>

<style scoped>

</style>