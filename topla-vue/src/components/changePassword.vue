<template>
  <v-form ref="form">
    <v-container>
      <h1>비밀번호 변경</h1>
        <v-alert type="info">

          안전한 비밀번호로 내정보를 보호하세요<br>

          다른 아이디/사이트에서 사용한 적 없는 비밀번호가 안전합니다.
        </v-alert>
      <v-text-field
          v-model="formInput.oldPassword"
          outlined
          :rules="rules.oldPassword"
          label="현재 비밀번호"
          :disabled="callCount>0"
      >
      </v-text-field>
      <v-text-field hide-details class="mb-2"
          v-model="formInput.password"
          label="새 비밀번호"
          outlined
          :rules="rules.password"
          :disabled="callCount>0"
      >
      </v-text-field>
      <v-text-field
          v-model="formInput.confirmPassword"
          label="새 비밀번호 확인"
          outlined
          :rules="rules.password"
          :disabled="callCount>0"
      >
      </v-text-field>
      <div class="float-right">
        <v-btn
            class="mr-2"
            @click="changePwd"
            :loading="callCount>0"
        >확인
        </v-btn>
        <v-btn
            :loading="callCount>0"
            @click="gomyPage"
        >취소</v-btn>
      </div>
    </v-container>
  </v-form>
</template>

<script>
import VuetifyJetValidator from "vuetify-jet-validator";
import errorDialog from "@/plugins/errorDialog";
import loginInfo from "@/plugins/loginInfo";

export default {
  name: "changePassword",
  data(){
    const validator = new VuetifyJetValidator();

    return {
      callCount: 0,

      formInput: {
        oldPassword: "",
        password: "",
        confirmPassword: "",
      },

      rules: {
        oldPassword:[
          validator.rules.required("필수값입니다."),
        ],
        password: [
          validator.rules.required("필수값입니다."),
          validator.rules.minLength(4, "최소 4자 이상 넣어야 합니다."),
          validator.rules.maxLength(30, "30자를 초과해서 넣을 수 없습니다."),
        ],
      },
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

    async changePwd() {
      let validationResultFlag = this.$refs.form.validate();
      if(validationResultFlag === false){
        return;
      }

      if(this.formInput.password !== this.formInput.confirmPassword){
        this.$dialog.error({
          title: "잘못된 비밀번호",
          text: "새비밀번호와 새비밀번호 확인이 일치하지 않습니다."
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
                Authorization: loginInfo.getLoginInfo()
              }
            });
        this.pushPage("/")
        this.$dialog.message.success("비밀번호 변경을 성공하였습니다.", {timeout: 800});
      }catch (e) {
        errorDialog(this, "비밀번호 변경 실패", e);
      }finally {
        this.callCount--;
      }
    },

    gomyPage(){
      this.pushPage("/mypage")
    }
  }
}


</script>

<style scoped>

</style>