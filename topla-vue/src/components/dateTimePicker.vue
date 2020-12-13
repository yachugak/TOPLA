<template>
  <div>
    <v-menu
        ref="menu"
        v-model="isShowPicker"
        :close-on-content-click="false"
        transition="scale-transition"
        offset-y
        min-width="290px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-text-field
            v-model="displayText"
            label="알림 시각"
            readonly
            v-bind="attrs"
            v-on="on"
        ></v-text-field>
      </template>
      <v-card style="height: 500px">
        <v-tabs v-model="tabedItem" grow>
          <v-tab>날짜</v-tab>
          <v-tab>시간</v-tab>
        </v-tabs>
        <v-tabs-items v-model="tabedItem">
          <v-tab-item>
            <v-date-picker v-model="selectedDate"
                           :day-format="dateFormat"
            ></v-date-picker>
          </v-tab-item>
          <v-tab-item>
            <v-time-picker v-model="selectedTime"></v-time-picker>
          </v-tab-item>
        </v-tabs-items>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="error" text @click="abortTime()">취소</v-btn>
          <v-btn color="primary" text @click="confirmTime()">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>
  </div>
</template>

<script>
import moment from "moment";
export default {
  name: "dateTimePicker",

  data() {
    return {
      isShowPicker: false,
      tabedItem: undefined,
      displayText: "설정하지 않음",

      selectedDate: moment().format("YYYY-MM-DD"),
      selectedTime: moment().format("HH:mm"),
      timeZone: moment().format("Z")
    }
  },

  computed: {
    fullDateTime(){
      return `${this.selectedDate}T${this.selectedTime}:00${this.timeZone}`
    }
  },

  props:{
    value:{
      type: String,
      default: null
    }
  },

  created() {
    this.syncModel(this.value);
  },

  watch: {
    value(newVal){
      this.syncModel(newVal)
    }
  },

  methods: {
    confirmTime(){
      this.setDisplayText(this.fullDateTime);
      this.isShowPicker = false;
      this.$emit("input", this.fullDateTime);
    },

    setDisplayText(fullTimeString){
      this.displayText = moment(fullTimeString).format("M[월] D[일] a h[시] mm[분]");
    },

    abortTime(){
      this.displayText = "설정하지 않음"
      this.isShowPicker = false;
      this.$emit("input", null);
    },

    syncModel(newVal){
      if(newVal === this.fullDateTime){
        return;
      }

      if(newVal === null || newVal === undefined){
        this.abortTime();
        return;
      }

      try{
        let momentObject = moment(newVal);
        this.selectedDate = momentObject.format("YYYY-MM-DD");
        this.selectedTime = momentObject.format("HH:mm");
        this.timeZone = momentObject.format("Z");
        this.setDisplayText(newVal);
      }
      catch{
        this.abortTime();
      }
    },

    dateFormat(dateString){
      let ymd = dateString.split("-");
      return ymd[2];
    }
  }
}
</script>

<style scoped>
</style>