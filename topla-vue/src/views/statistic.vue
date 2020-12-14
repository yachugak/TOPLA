<template>
  <div class="back" style="height: 95vh">
    <v-container>
      <v-row>
        <v-col cols="12">
          <v-card>
            <v-card-title>통계</v-card-title>
            <v-card-subtitle>다양한 통계를 볼 수 있습니다.</v-card-subtitle>
            <v-card-text>현재 2개의 통계를 제공하고 있습니다.</v-card-text>
            <v-card-text v-if="!isDataLoading">통계 기준일: {{statisticDateString}}부터 {{yesterdayString}}까지</v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <v-row v-if="!isDataLoading">
        <v-col cols="12" md="6">
          <v-card>
            <v-card-title>최근 완료한 작업량</v-card-title>
            <v-card-subtitle>지난 7일동안 작업한 시간의 총합입니다.</v-card-subtitle>
            <v-card-text>
              <div class="text-center">
                <span class="text-h4">{{workTimeString}}</span>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" md="6">
          <v-card>
            <v-card-title>마감일 며칠 전에 일을 끝냈을까요?</v-card-title>
            <v-card-subtitle>지난 7일동안 마감일보다 빨리 끝낸 작업시간의 평균입니다.</v-card-subtitle>
            <v-card-text>
              <div class="text-center">
                <span class="text-h4">{{averageLeftDueDateString}}</span>
              </div>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
      <v-row v-if="isDataLoading">
        <v-col cols="12">
          <v-card class="pa-2">
            <div class="text-center text-h4">
              데이터를 로딩하고 있습니다.
              <br>
              <v-progress-linear indeterminate></v-progress-linear>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import errorDialog from "@/plugins/errorDialog";
import moment from "moment";

export default {
  name: "statistic",
  data() {
    return {
      statistic: null,
      callCount: 0,
      dayFormat: "YYYY[년] M[월] D[일]"
    }
  },

  computed: {
    statisticDate() {
      if (this.statistic === null) {
        return null;
      }

      return moment(this.statistic.targetdate);
    },

    statisticDateString(){
      if(this.statisticDate === null){
        return "정보 없음";
      }

      return this.statisticDate.format(this.dayFormat);
    },

    yesterdayString(){
      let now = moment();
      let yesterday = now.subtract(1, "days");

      return yesterday.format(this.dayFormat);
    },

    workTimeString(){
      if(this.statistic === null){
        return "데이터 없음";
      }

      if(this.statistic.total7daysWorkTime === null || this.statistic.total7daysWorkTime === undefined){
        return "데이터 없음";
      }

      return (this.statistic.total7daysWorkTime / 60).toFixed(2) + "시간";
    },

    averageLeftDueDateString(){
      if(this.statistic === null){
        return "데이터 없음";
      }

      if(this.statistic.averageWorkDoneInDeadline === null || this.statistic.averageWorkDoneInDeadline === undefined){
        return "데이터 없음";
      }

      if(this.statistic.averageWorkDoneInDeadline === "NaN"){
        return "데이터 없음";
      }

      return `평균 ${this.statistic.averageWorkDoneInDeadline.toFixed(2)}일 미리 끝냄`
    },

    isDataLoading(){
      if(this.statistic === null){
        return true;
      }

      return false;
    }
  },

  created() {
    this.getStatistic();
  },

  methods: {
    async getStatistic(){
      this.callCount++;
      try{
        let res = await this.$axios.get("/report/statisticsReport");
        this.statistic = res.data;
      }catch (e) {
        errorDialog(this, "통계 정보 획득 실패", e);
      }finally {
        this.callCount--;
      }
    }
  }
}
</script>

<style scoped>

</style>