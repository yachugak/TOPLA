<template>
  <div id="backgroundDiv" class="back pa-4">
    <v-card class="pa-4">
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
        <v-text-field
            label="비밀번호 확인"
            outlined
            :type="isShowPassword ? 'text' : 'password'"
            :append-icon="isShowPassword ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append="isShowPassword = !isShowPassword"
            v-model="formInput.confirmPassword"
            v-if="isRegisterMode"
        >
        </v-text-field>
        <v-checkbox
            v-model="formInput.isContractAgree"
            v-if="isRegisterMode"
            :rules="rules.isContractAgree"
            label="개인 정보 처리 방침에 동의합니다."
        >
        </v-checkbox>
        <v-btn block v-if="!isRegisterMode" :loading="callCount>0" color="primary" @click="onLoginButtonClicked()">로그인</v-btn>
        <v-btn block v-else color="primary" :loading="callCount>0" @click="onRegisterButtonClicked()">회원 등록</v-btn>
        <div class="buttonGroup mt-2">
          <v-btn v-if="!isRegisterMode" :loading="callCount>0" color="primary" class="mr-2" @click="isRegisterMode=true">회원가입</v-btn>
          <v-btn v-if="!isRegisterMode" :loading="callCount>0" color="primary" class="mr-2" @click="isRegisterMode=true">비밀번호 찾기</v-btn>
          <v-btn v-else color="secondary" :loading="callCount>0" class="mr-2" @click="isRegisterMode = false">취소</v-btn>
        </div>
      </v-form>
    </v-card>

    <!--개인 정보 처리 방침 모달-->
    <v-dialog v-model="isShowContract">
      <v-card>
        <v-card-title>TOPLA 개인 정보 처리 방침</v-card-title>
        <div class="px-6">
          <div class="header1">팀 소개</div>
          <div class="contract-text">
            yachugak은 법인이 아닙니다.
          </div>
          <div class="contract-text">
            TOPLA 웹사이트 주소: https://topla.p-e.kr
          </div>
          <div class="contract-text">
            이용자께서는 슈나소프트의 서비스를 이용하시며 발생하는 모든 개인정보보호 관련 민원을 아래의 이메일로 연락하시기 바랍니다. 슈나소프트는 이용자의 문의사항에 대해 신속하게 충분한 답변을 드릴 것입니다.
          </div>
          <div class="contract-text">
            henry174@ajou.ac.kr
          </div>
          <div class="header1">수집하는 개인 데이터 종류 및 수집 이유</div>
          <div class="header2">
            이메일
          </div>
          <div class="contract-text">
            회원 개개인을 구분하기 위해 계정 ID로서 이메일을 수집하고 있습니다.
            수집된 이메일은 로그인을 위해 사용할 뿐 전송하는 메일은 없습니다.
          </div>
          <div class="header2">
            위치 정보
          </div>
          <div class="contract-text">
            사용자의 위치에 따른 정보 제공을 위하여 사용자가 TOPLA 접속에 사용한 단말로부터 위치 정보를 수집합니다.
            수집된 정보는 서버에 저장되지 않고 필요한 정보를 계산하는데 쓰이고 즉시 파기됩니다.
            위치 정보를 제공하고 싶지 않으신 사용자 분들께서는 브라우저 보안 설정에서 이를 차단할 수 있습니다.
            위치 정보를 제공하지 않을 시 사용자의 위치에 따른 정보 제공 서비스를 받지 못합니다.
          </div>
          <div class="header2">
            작업
          </div>
          <div class="contract-text">
            사용자는 자신의 작업을 등록할 수 있고 TOPLA는 데이터 백업을 위해 작업의 이름, 중요도, 행해지는 위치, 마감일을 수집하여 서버에 저장합니다.
          </div>
          <div class="header2">
            기기 고유 식별 번호
          </div>
          <div class="contract-text">
            TOPLA는 사용자에게 푸시 알림 서비스를 위하여 기기 고유 식별 번호를 수집하고 있습니다. 이 식별 번호는 랜덤 문자열로 각 기기간 구분은 할 수 있지만 특정 기기를 파악할 수는 없습니다.
            브라우저 설정에서 정보 제공을 차단할 수 있습니다. 이 경우 푸시 알람 서비스가 제한됩니다.
          </div>
          <div class="header1">
            데이터를 보존하는 기간
          </div>
          <div class="contract-text">
            사용자가 등록한 작업, 기기 고유 식별 번호, 일정 완료 기록은 사용자가 계정을 유지하는 한 영구히 보존됩니다.
            사용자가 작업을 삭제한 경우 작업과 일정 완료 기록은 그 즉시 삭제됩니다.
            사용자가 계정을 삭제한 경우 모든 정보는 그 즉시 삭제됩니다.
          </div>
          <div class="header1">
            연락 정보
          </div>
          <div class="contract-text">
            이용자께서는 TOPLA 서비스를 이용하시며 발생하는 모든 개인정보보호 관련 민원을 아래의 이메일로 연락하시기 바랍니다. yachugak은 이용자의 문의사항에 대해 신속하게 충분한 답변을 드릴 것입니다.
          </div>
          <div class="contract-text">
            henry174@ajou.ac.kr
          </div>
          <div class="header1">
            추가 정보
          </div>
          <div class="header2">
            데이터를 수집하는 제 3자의 종류
          </div>
          <div class="contract-text">
            TOPLA는 길찾기 서비를 위해 카카오맵으로 연계하고 있습니다. 카카오맵의 개인 정보 보호 정책을 확인하시기 바랍니다.
          </div>
          <div class="header2">
            개인 정보 관련 신고 및 분쟁 조정
          </div>
          <div class="contract-text">
            개인정보침해에 대한 신고 또는 상담이 필요하신 경우에는 한국정보보호진흥원(KISA) 개인정보침해신고센터로 문의하시기 바랍니다. 또한, 귀하가 개인정보침해를 통한 금전적, 정신적 피해를 입으신 경우에는 한국정보보호진흥원(KISA) 개인정보분쟁조정위원회에 피해구제를 신청하실 수 있습니다.
            <ul>
              <li>KISA 개인정보보호 (http://privacy.kisa.or.kr/ 국번 없이 118)</li>
              <li>개인정보 분쟁조정위원회 (http://www.kopico.go.kr/ 전화 03-405-5150)</li>
              <li>대검찰청 (http://www.spo.go.kr/ 전화 02-3480-2000)</li>
              <li>경찰청 사이버안전국 (http://www.ctrc.go.kr/ 전화 182)</li>
              <li>사이버경찰청 (http://www.police.go.kr/ 전화 182)</li>
            </ul>
          </div>
          <div class="header2">
            고지의 의무
          </div>
          <div class="contract-text">
            법령, 정책 또는 보안 기술 등의 변경에 따라 현 개인 정보 처리 방침의 내용 추가, 삭제 및 수정이 있을 시에는 개정되는 개인 정보 처리 방침을 시행하기 최소 7일 전에 TOPLA의 공지사항으로 고지할 것입니다.
          </div>
          <div class="header2">
            부칙
          </div>
          <div class="contract-text">
            <ul>
              <li>공고 일자: 2020년 11월 27일</li>
              <li>시행 일자: 2020년 11월 27일</li>
            </ul>
          </div>
        </div>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="isShowContract = false">
            확인
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
      isShowContract: false,

      formInput: {
        account: "",
        password: "",
        isContractAgree: false,
        confirmPassword: ""
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
        ],

        isContractAgree: [
          (v)=>{return v?true:"개인 정보 처리 방침에 동의하셔야 가입할 수 있습니다."}
        ],
      }
    }
  },

  computed: {
    title(){
      return this.isRegisterMode ? "회원 등록" : "로그인";
    }
  },

  watch: {
    "formInput.isContractAgree": function(newVal){
      if(newVal === true){
        this.isShowContract = true;
      }
    },

    isRegisterMode(){
      this.$refs.form.reset();
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
        this.$store.commit("setLoginInfo", accountCopy);
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

      if(this.formInput.password !== this.formInput.confirmPassword){
        this.$dialog.error({
          title: "회원 가입을 진행할 수 없습니다.",
          text: "비밀번호 확인이 틀립니다."
        })
        return;
      }

      this.callCount++;
      try {
        await this.$axios.post(`/user`, {
          email: this.formInput.account,
          password: this.formInput.password
        });

        this.$dialog.notify.success("계정이 생성되었습니다. 로그인 해 보세요!");
        this.isRegisterMode = false;
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

.header1 {
  font-size: 30px;
  margin-bottom: 5px;
}

.header2 {
  font-size: 25px;
  margin-bottom: 5px;
}
.contract-text {
  font-size: 15px;
  margin-bottom: 10px;
}

</style>