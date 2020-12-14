<template>
  <div class="back pa-3" style="min-height: 96vh;">
    <v-card class="mb-2">
      <v-card-title>관리자 전용 페이지입니다.</v-card-title>
      <v-card-text>
        <v-progress-linear v-if="!isAllInfoGetted" indeterminate></v-progress-linear>
      </v-card-text>
    </v-card>
    <v-card v-if="overallInfo!==null" class="mb-2">
      <v-card-title>서비스 현황</v-card-title>
      <v-card-subtitle>서비스의 현황 대시보드</v-card-subtitle>
      <v-container fluid>
        <v-row>
          <v-col cols="12" md="6">
            <v-card>
              <v-card-title>유저 통계</v-card-title>
              <v-card-subtitle>TOPLA에 회원가입한 유저의 통계</v-card-subtitle>
              <v-card-text>
                <v-list>
                  <v-list-item two-line>
                    <v-list-item-content>
                      <v-list-item-title>전체 유저</v-list-item-title>
                      <v-list-item-subtitle>TOPLA에 회원 등록된 전체 유저의 수</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <span class="text-h6">{{overallInfo.allUserNum}}명</span>
                    </v-list-item-action>
                  </v-list-item>
                  <v-divider></v-divider>
                  <v-list-item two-line>
                    <v-list-item-content>
                      <v-list-item-title>활성 유저</v-list-item-title>
                      <v-list-item-subtitle>최근 7일간 작업을 등록한 유저의 수</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <span class="text-h6">{{overallInfo.activeUserNum}}명</span>
                    </v-list-item-action>
                  </v-list-item>
                </v-list>
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" md="6">
            <v-card>
              <v-card-title>작업 통계</v-card-title>
              <v-card-subtitle>사용자가 TOPLA에 등록하는 작업의 통계</v-card-subtitle>
              <v-card-text>
                <v-list>
                  <v-list-item two-line>
                    <v-list-item-content>
                      <v-list-item-title>전체 작업</v-list-item-title>
                      <v-list-item-subtitle>TOPLA에 등록된 전체 작업(할 일)의 수</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <span class="text-h6">{{overallInfo.taskNum}}개</span>
                    </v-list-item-action>
                  </v-list-item>
                  <v-divider></v-divider>
                  <v-list-item two-line>
                    <v-list-item-content>
                      <v-list-item-title>완료된 작업의 수</v-list-item-title>
                      <v-list-item-subtitle>완료 체크된 모든 작업의 수</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <span class="text-h6">{{overallInfo.doneTaskNum}}개</span>
                    </v-list-item-action>
                  </v-list-item>
                  <v-divider></v-divider>
                  <v-list-item two-line>
                    <v-list-item-content>
                      <v-list-item-title>미완료된 작업의 수</v-list-item-title>
                      <v-list-item-subtitle>완료 체크가 되지 않은 모든 작업의 수</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <span class="text-h6">{{overallInfo.undoneTaskNum}}개</span>
                    </v-list-item-action>
                  </v-list-item>
                  <v-divider></v-divider>
                  <v-list-item two-line>
                    <v-list-item-content>
                      <v-list-item-title>지난 7일간 등록된 작업의 수</v-list-item-title>
                      <v-list-item-subtitle>모든 사용자가 지난 7일동안 등록한 작업의 수</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <span class="text-h6">{{overallInfo.newTaskNumIn7Days}}개</span>
                    </v-list-item-action>
                  </v-list-item>
                  <v-divider></v-divider>
                  <v-list-item two-line>
                    <v-list-item-content>
                      <v-list-item-title>지난 7일간 완료된 작업의 수</v-list-item-title>
                      <v-list-item-subtitle>모든 사용자가 지난 7일동안 완료한 작업의 수</v-list-item-subtitle>
                    </v-list-item-content>
                    <v-list-item-action>
                      <span class="text-h6">{{overallInfo.doneTaskNumIn7Days}}개</span>
                    </v-list-item-action>
                  </v-list-item>
                </v-list>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-container>
    </v-card>
    <v-card v-if="userList!==null">
      <v-card-title>
        유저 관리
        <v-spacer></v-spacer>
        <v-text-field v-model="searchKeyword" append-icon="mdi-magnify" label="검색"></v-text-field>
      </v-card-title>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="primary" @click="showAllMailDialog()">
          일괄 메일 전송
        </v-btn>
      </v-card-actions>
      <v-card-text>
        <v-data-table
            :headers="userInfoTableHeaer"
            :items="userList"
            :search="searchKeyword"
        >
          <template v-slot:item.deviceToken="{item}">
            <v-btn v-if="isThereStringData(item.deviceToken)" small  @click="showDeviceToken(item.email, item.deviceToken)">보기</v-btn>
            <span v-else><v-btn disabled small>없음</v-btn></span>
          </template>
          <template v-slot:item.pushAlarmStatus="{item}">
            <v-icon color="green" v-if="item.pushAlarmStatus">mdi-bell</v-icon>
            <v-icon color="red" v-else>mdi-bell-off-outline</v-icon>
          </template>
          <template v-slot:item.activeUser="{item}">
            <v-icon color="green" v-if="item.activeUser">mdi-emoticon-excited</v-icon>
            <v-icon color="red" v-else>mdi-emoticon-dead-outline</v-icon>
          </template>
          <template v-slot:item.action="{item}">
            <v-btn small icon @click="showMailDialog(item.email)"><v-icon>mdi-email-edit</v-icon></v-btn>
            <v-btn small icon @click="onPasswordResetButtonClicked(item.email)"><v-icon>mdi-lock-reset</v-icon></v-btn>
            <v-btn small icon @click="onUserBanButtonClicked(item.email)"><v-icon>mdi-account-cancel</v-icon></v-btn>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>

    <!-- dialog 모음집 -->
    <v-dialog v-model="mailDialog.show" max-width="700" :persistent="callCount>0" >
      <v-card>
        <v-card-title>메일 작성</v-card-title>
        <v-card-subtitle>사용자에게 메일을 보냅니다.</v-card-subtitle>
        <v-card-text>
          <v-form ref="mailForm">
            <v-text-field label="보내는 사람" value="topla@syunasoft.com" disabled outlined></v-text-field>
            <v-text-field label="받는 사람" :value="mailDialog.to" disabled outlined></v-text-field>
            <v-text-field label="메일 제목" :rules="requireRule" :disabled="callCount>0" v-model="mailDialog.title" outlined></v-text-field>
            <v-textarea label="본문" :disabled="callCount>0" :rules="requireRule" v-model="mailDialog.body" outlined></v-textarea>
          </v-form>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="secondary" :loading="callCount>0" @click="mailDialog.show=false">닫기</v-btn>
            <v-btn color="primary" :loading="callCount>0" @click="onMailSendButtonClicked()">전송</v-btn>
          </v-card-actions>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog v-model="passwordChangeDialog.show" max-width="700" persistent>
      <v-card>
        <v-card-title>비밀번호 초기화 중</v-card-title>
        <v-card-text class="text-center">
          <v-progress-linear indeterminate></v-progress-linear>
          {{passwordChangeDialog.message}}
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog :persistent="callCount>0" max-width="700" v-model="userBanDialog.show">
      <v-card color="error">
        <v-card-title>
          사용자 삭제
          <v-spacer></v-spacer>
          <v-btn icon @click="userBanDialog.show=false"><v-icon>mdi-close</v-icon></v-btn>
        </v-card-title>
        <v-card-subtitle>사용자를 삭제합니다.</v-card-subtitle>
        <v-card-text class="text-center text-h6">
          현재 {{userBanDialog.target}} 계정을 삭제하려고 하고 있습니다.
        </v-card-text>
        <v-card-text>
          이 선택은 되돌릴 수 없습니다. 사용자가 삭제되면 사용자와 관련된 모든 정보가 그 즉시 서버에서 삭제되고 복원할 수 없습니다.<br>
          정당한 사유가 없음에도 불구하고 사용자의 동의 없이 계정을 삭제하는 행위는 약관 위반일 수 있습니다. 심사숙고 하십시오.<br>
          삭제된 계정으로 메일을 통해 삭제 통지가 날아갑니다.
        </v-card-text>
        <v-card-text>
          실수로 사용자의 계정을 삭제하는 것을 방지하기 위해 아래의 커멘드를 버튼으로 입력하십시오.
        </v-card-text>
        <v-card-text class="text-center text-h5">
          ↑ ↑ ↓ ↓ ← → ← → B A
        </v-card-text>
        <v-card-text class="text-center" v-if="callCount===0">
          <v-btn class="mr-1" @click="onGamePadButtonClicked('left')">←</v-btn>
          <v-btn class="mr-1" @click="onGamePadButtonClicked('up')">↑</v-btn>
          <v-btn class="mr-1" @click="onGamePadButtonClicked('down')">↓</v-btn>
          <v-btn class="mr-3" @click="onGamePadButtonClicked('right')">→</v-btn>
          <v-btn class="mr-1" @click="onGamePadButtonClicked('B')">B</v-btn>
          <v-btn class="mr-1" @click="onGamePadButtonClicked('A')">A</v-btn>
        </v-card-text>
      </v-card>
    </v-dialog>
    <v-dialog v-model="allMailDialog.show" max-width="700" :persistent="allMailDialog.isOnGoing" >
      <v-card>
        <v-card-title>공지 메일 작성</v-card-title>
        <v-card-subtitle>모든 사용자에게 메일을 보냅니다. 공지 메일은 신중히 작성합시다!</v-card-subtitle>
        <v-card-text v-if="!allMailDialog.isOnGoing">
          <v-form ref="allMailForm">
            <v-text-field label="보내는 사람" value="topla@syunasoft.com" disabled outlined></v-text-field>
            <v-text-field label="받는 사람" :value="`${userList.length}명의 사용자`" disabled outlined></v-text-field>
            <v-text-field label="메일 제목" :rules="requireRule" :disabled="callCount>0" v-model="allMailDialog.title" outlined></v-text-field>
            <v-textarea label="본문" :disabled="callCount>0" :rules="requireRule" v-model="allMailDialog.body" outlined></v-textarea>
          </v-form>
          <v-card-actions v-if="!allMailDialog.isOnGoing">
            <v-spacer></v-spacer>
            <v-btn color="secondary" :loading="callCount>0" @click="allMailDialog.show=false">닫기</v-btn>
            <v-btn color="primary" :loading="callCount>0" @click="onSendAllMailButtonClicked()">전송</v-btn>
          </v-card-actions>
        </v-card-text>
        <v-card-text v-else>
          <div class="text-center text-h5" v-if="!allMailDialog.finish">메일을 전송하고 있습니다.</div>
          <div class="text-center text-h5" v-else>메일을 전송이 끝났습니다.</div>
          <div class="text-center mb-2" v-if="!allMailDialog.finish">창을 종료하지 마십시오.</div>
          <div class="text-center mb-2" v-else>이제 창을 끄셔도 됩니다.</div>
          <v-progress-linear height="20" :value="(((allMailDialog.success+allMailDialog.fail)/allMailDialog.total)*100)">
            {{ (((allMailDialog.success+allMailDialog.fail)/allMailDialog.total)*100).toFixed(0)}}%
          </v-progress-linear>
          <div>전체 {{allMailDialog.total}}건 중 {{allMailDialog.success}}건 성공, {{allMailDialog.fail}}건 실패</div>
          <div class="text-h6 mt-2" v-if="allMailDialog.fail>0">전송 실패 리스트</div>
          <div style="height: 100px; overflow-y: auto;">
            <ul>
              <li v-for="email in allMailDialog.failList" :key="email">email</li>
            </ul>
          </div>
        </v-card-text>
        <v-card-actions v-if="allMailDialog.isOnGoing && allMailDialog.finish">
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="allMailDialog.show=false">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import errorDialog from "@/plugins/errorDialog";
import Validator from "vuetify-jet-validator";

export default {
  name: "superUser",

  data(){
    let validator = new Validator();
    return{
      callCount: 0,
      overallInfo: null,
      userList: null,

      userInfoTableHeaer: [
        {
          text: "계정",
          value: "email"
        },
        {
          text: "기기 토큰",
          value: "deviceToken"
        },
        {
          text:"푸시 허용",
          value: "pushAlarmStatus"
        },
        {
          text:"최근 등록 작업",
          value: "numberOfNewTasksIn7Days"
        },
        {
          text:"활성 상태",
          value: "activeUser"
        },
        {
          text: "액션",
          value: "action"
        }
      ],

      requireRule: [validator.rules.required("필수값입니다.")],

      mailDialog: {
        to: "",
        title: "",
        body: "",
        show: false
      },

      passwordChangeDialog: {
        show: false,
        message: ""
      },

      userBanDialog: {
        show: false,
        target: "",
      },

      allMailDialog: {
        title: "",
        body: "",
        show: false,
        total: 10,
        success: 0,
        fail: 0,
        failList: [],
        isOnGoing: false,
        finish: false
      },

      deleteCommandList: ["up", "up", "down", "down", "left", "right", "left", "right", "B", "A"],
      deleteCommandState: 0, //해당 인덱스를 눌러야 할 차례
      searchKeyword: ""
    }
  },

  created() {
    this.getOverallInfo();
    this.getUserList();
  },

  computed: {
    isAllInfoGetted(){
      if(this.overallInfo === null){
        return false;
      }
      if(this.userList === null){
        return false;
      }
      return true;
    }
  },

  methods: {
    async getOverallInfo(){
      this.callCount++;
      try{
        let res = await this.$axios.get("/superuser/overallinfo");
        this.overallInfo = res.data;
      }
      catch (e) {
        errorDialog(this, "정보 획득 실패", e);
      }
      finally {
        this.callCount--;
      }
    },

    async getUserList(){
      this.callCount++;
      try{
        let res = await this.$axios.get("/superuser/userlist");
        this.userList = res.data;
      }
      catch (e) {
        errorDialog(this, "정보 획득 실패", e);
      }
      finally {
        this.callCount--;
      }
    },

    morningReportTimeFormatting(str){
      let splited = str.split("+");
      if(splited.length !== 2){
        splited = str.split("-");
        if(splited.length !== 2){
          return "알 수 없는 시간 포맷"
        }
      }

      return splited[0];
    },

    showDeviceToken(account, deviceToken){
      this.$dialog.info({
        title:`${account}의 기기 토큰`,
        text: deviceToken,
        actions:{
          true: {
            text:"확인"
          }
        }
      });
    },

    isThereStringData(str){
      return typeof str === "string";
    },

    showMailDialog(to){
      this.mailDialog.to = to;
      this.mailDialog.title = "";
      this.mailDialog.body = "";
      this.mailDialog.show = true;
    },

    async onMailSendButtonClicked(){
      if(this.$refs.mailForm.validate()===false){
        return;
      }

      let res = await this.$dialog.info({
        title: "메일을 전송하시겠습니까?",
        text: "이 작업은 취소할 수 없습니다.",
        actions: {
          false: {
            text: "아니오",
          },
          true: {
            text: "예",
          }
        }
      });

      if(res !== true){
        return;
      }

      this.callCount++;
      try{
        await this.$axios.post("/superuser/sendmail", {
          to: this.mailDialog.to,
          title: this.mailDialog.title,
          content: this.mailDialog.body
        });
        this.mailDialog.show = false;
        this.$dialog.notify.success("메일 전송 요청이 성공하였습니다.");
      }
      catch (e) {
        errorDialog(this, "메일 전송 요청 실패", e);
      }
      finally {
        this.callCount--;
      }
    },

    async onPasswordResetButtonClicked(targetEmail){
      let res = await this.$dialog.info({
        title: "사용자의 비밀번호 초기화",
        text: `${targetEmail}의 비밀번호를 초기화 하시겠습니까?`,
        actions: {
          false: {
            text: "아니오",
          },
          true: {
            text: "예",
          }
        }
      });

      if(res !== true){
        return;
      }

      res = await this.$dialog.warning({
        title: "사용자의 동의를 받으셨습니까?",
        text: `고객의 동의 없이 비밀번호를 변경하는 행위는 위험합니다.`,
        actions: {
          false: {
            text: "아니오",
          },
          true: {
            text: "예",
          }
        }
      });

      if(res !== true){
        return;
      }

      res = await this.$dialog.error({
        title: "자동으로 메일이 전송됩니다.",
        text: `비밀번호를 초기화하면 사용자에게 해당 내용이 메일로 통지됩니다. 그래도 하시겠습니까?`,
        actions: {
          false: {
            text: "아니오",
          },
          true: {
            text: "예",
          }
        }
      });

      if(res !== true){
        return;
      }

      this.callCount++;
      this.passwordChangeDialog.show = true;
      this.passwordChangeDialog.message = "비밀번호를 초기화 하는 중..."
      let passwordChangeSuccessFlag = false;
      try{
        await this.$axios.put("/superuser/resetpassword", {
          email: targetEmail
        });
        passwordChangeSuccessFlag = true;
        this.passwordChangeDialog.message = "메일 전송 중..."
        await this.$axios.post("/superuser/sendmail",{
          to: targetEmail,
          title: "[TOPLA] 귀하의 계정의 비밀번호가 초기화되었습니다.",
          content: `귀하의 TOPLA 계정 ${targetEmail}의 비밀번호가 초기화되었습니다. 자세한 문의는 topla@syunasoft.com으로 문의해 주시기 바랍니다.`
        });
        this.$dialog.notify.success("비밀번호가 초기화되었고 메일 전송 요청이 성공했습니다.");
      }
      catch (e) {
        errorDialog(this, "요청 실패", e);
        if(passwordChangeSuccessFlag){
          this.$dialog.notify.error("비밀번호는 초기화 했지만 메일 전송 요청에 실패했습니다.");
        }
      }
      finally {
        this.callCount--;
        this.passwordChangeDialog.show = false;
      }
    },

    //성공하면 true반환
    checkCommand(key){
      if(this.deleteCommandList[this.deleteCommandState] === key){
        this.deleteCommandState++;
        if(this.deleteCommandState >= this.deleteCommandList.length){
          this.deleteCommandState = 0;
          return true;
        }
      }
      else{
        this.deleteCommandState = 0;
      }

      return false;
    },

    onGamePadButtonClicked(key){
      let result = this.checkCommand(key)
      if(result !== true){
        return;
      }

      this.$dialog.notify.success("커맨드 확인됨");
      this.requestUserBan();
    },

    async requestUserBan(){
      this.callCount++;
      let successFlag = false;
      try {
        await this.$axios.delete(`/superuser/${this.userBanDialog.target}`);
        successFlag = true;
        this.$dialog.notify.success("사용자를 삭제하였습니다.");
      }
      catch (e) {
        errorDialog(this, "사용자 삭제 실패", e);
      }
      finally {
        this.callCount--;
      }

      if(successFlag !== true){
        return;
      }

      this.callCount++;
      try{
        await this.$axios.post("/superuser/sendmail", {
          to: this.userBanDialog.target,
          title: "[TOPLA] 계정 삭제 안내",
          content: `귀하의 TOPLA 계정 ${this.userBanDialog.target}이 TOPLA측에 의해 삭제되었습니다. 자세한 문의는 topla@syunasoft.com으로 해 주시기 바랍니다.`
        });
        this.$dialog.notify.success("계정이 삭제된 사용자에게 안내 메일을 보냈습니다.");
      }
      catch (e) {
        this.$dialog.notify.error("계정이 삭제된 사용자에게 안내 메일을 보내지 못했습니다.");
      }
      finally {
        this.callCount--;
      }

      this.callCount++;
      try{
        await this.getOverallInfo();
        await this.getUserList();
        this.$dialog.notify.success("정보를 다시 불러왔습니다.");
      }
      catch (e) {
        errorDialog(this, "통계 정보 업데이트 실패", e);
      }
      finally {
        this.callCount--;
        this.userBanDialog.show = false;
      }
    },

    onUserBanButtonClicked(targetEmail){
      this.userBanDialog.show = true;
      this.userBanDialog.target = targetEmail;
    },

    async onSendAllMailButtonClicked(){
      if(this.allMailDialog.isOnGoing === true){
        return;
      }

      if(this.$refs.allMailForm.validate() === false){
        return;
      }

      let res = await this.$dialog.warning({
        title: "공지 메일 전송?",
        text: "공지 메일을 전송하시겠습니까? 이 작업은 되돌릴 수 없습니다.",
        actions: {
          false: {
            text: "아니오"
          },
          true: {
            text: "예"
          }
        }
      });

      if(res !== true){
        return;
      }

      let emailList = this.userList.map(function(user){
        return user.email;
      });


      this.sendingAllMail(emailList, this.allMailDialog.title, this.allMailDialog.body);

    },

    showAllMailDialog(){
      this.allMailDialog.total = 0;
      this.allMailDialog.success = 0;
      this.allMailDialog.fail = 0;
      this.allMailDialog.failList = [];
      this.allMailDialog.isOnGoing = false;
      this.allMailDialog.finish = false;
      this.allMailDialog.show = true;
      this.allMailDialog.title = "";
      this.allMailDialog.body = "";
    },

    async sendingAllMail(emailList, title, body){
      this.allMailDialog.total = emailList.length;
      this.allMailDialog.success = 0;
      this.allMailDialog.fail = 0;
      this.allMailDialog.failList = [];
      this.allMailDialog.isOnGoing = true;
      this.allMailDialog.finish = false;
      for(let email of emailList){
        try{
          await this.$axios.post("/superuser/sendmail", {
            to: email,
            title: title,
            content: body
          });
          this.allMailDialog.success++;
        }
        catch{
          this.allMailDialog.fail--;
          this.allMailDialog.failList.push(email);
        }
      }
      this.allMailDialog.finish = true;
      this.$dialog.notify.info(`[메일 전송 결과] 총 ${this.allMailDialog.total}건 중 ${this.allMailDialog.success}건 성공, ${this.allMailDialog.fail}건 실패`);
    }
  }
}
</script>

<style scoped>
</style>