<!--예상 시간 선택기입니다.-->
<template>
  <div id="container">
    <v-slider
        :max="nowTimeMax"
        min="0"
        persistent-hint
        v-model="esTime"
        :hint="displayTime"
        step="30"
    ></v-slider>
    <v-expand-transition>
      <v-btn color="info" v-show="esTime>=nowTimeMax && esTime != timeMax" @click="increaseMaxTime()">더 긴 시간을 원하십니까?</v-btn>
    </v-expand-transition>
  </div>
</template>

<script>
export default {
  name: "durationSelector",

  data() {
    return {
      esTime: 0,
      nowTimeMax: 240,
      timeMax: 36000,
    }
  },

  computed: {
    displayTime() {
      let hour = Number.parseInt(this.esTime / 60); //몫 구하기
      let min = this.esTime % 60; //나머지 구하기

      return `${hour}시간 ${min}분`;
    }
  },

  methods: {
    increaseMaxTime(){
      this.nowTimeMax *= 2;
      if(this.nowTimeMax > this.timeMax){
        this.nowTimeMax = this.timeMax;
      }
    }
  },

  watch: {
    value(newValue){
      this.esTime = newValue
    },

    esTime(newValue){
      this.$emit("input", newValue);
    }
  },

  props: {
    value: {
      type: Number,
      default: 0
    }
  }
}
</script>

<style scoped>
#container {
  width: 100%;
}
</style>