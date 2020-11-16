<template>
  <v-alert
      border="left"
      dismissible
      type="error"
      v-if="isBoxShow"
  >
    마감일 내에 완료할 수 없는 작업이 있습니다.<br>
    최대 효율을 내기 위해 일부 작업의 시간이 축소되었습니다.<br>
    <v-btn text small @click="showDetail = true" v-show="showDetail===false">
      자세히...
    </v-btn>
    <v-expand-transition >
      <div v-show="showDetail">
        <br>
        TOPLA는 사용자가 설정한 스케줄 프리셋에 맞춰 일정을 배치합니다.
        스케줄에 맞춰 작업을 배치한 결과 마감일 안에 못 끝내는 작업이 발견되면 동일한 작업 시간 안에 최대의 보상을 얻을 수 있도록 별로 중요하지 않은 작업의 작업 시간을 줄여 다른 작업을 할 수 있는 시간을 확보하려고 시도합니다<br>
        <br>
        TOPLA는 어디까지나 사용자가 제공한 스케줄 프리셋에 맞춰 일정을 계획했을 뿐이므로 모든 작업을 완료하고 싶다면 추가 근무를 검토해 보십시오.
      </div>
    </v-expand-transition>
    <v-btn text small @click="showDetail = false" v-show="showDetail===true">
      닫기
    </v-btn>
  </v-alert>
</template>

<script>
export default {
  name: "ScheduleAlertBox",

  data() {
    return {
      showDetail: false,
      totalLoss: 0
    }
  },

  computed: {
    isBoxShow() {
      if(this.totalLoss > 0){
        return true;
      }

      return false;
    }
  },

  created(){
    this.getTotalLoss();
  },

  methods: {
    async getTotalLoss(){
      try {
        let res = await this.$axios.get(`/plan/loss`);
        this.totalLoss = res.data.totalLossPriority;
      }
      catch(e){
        console.error(e.response.data);
      }
    }
  }
}
</script>

<style scoped>

</style>