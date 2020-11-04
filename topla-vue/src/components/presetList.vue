<template>
  <v-container>
    <v-row
        v-for ="(eachweek,index) in daydata"
        :key="index">
      <v-slider
          v-for="eachDay in eachweek"
          :key="eachDay.day"
          v-model="eachDay.preset"
          :label="eachDay.dayInEnglish"
          vertical
          step="0.5"
          :max="eachDay.max"
          :min="min">
        <template v-slot:append>
          <v-text-field
              v-model="eachDay.preset"
              class="mt-0 pt-0"
              type="number"
              style="width: 45px"
              step="0.5"
              :max="eachDay.max"
          ></v-text-field>
        </template>
      </v-slider>
    </v-row>
  </v-container>
</template>

<script>
export default {
  name: "presetList",

  data () {
    return {
      day:['일', '월', '화', '수', '목', '금', '토'],
      daydata:[],
      min:0,
    }
  },
  async created(){
    let res = await this.$axios.get("/preset/list")
    // console.log(res.data)
    this.daydata=new Array(res.data.length)
    for (let j=0;j<this.daydata.length;j++) {
      this.daydata[j]=new Array(7)
      for (let i = 0; i < 7; i++) {
        let day = new Object()

        day.day = i
        day.preset = res.data[j].schedulePreset[i] / 60
        day.max = 7
        day.dayInEnglish = this.day[i]
        this.daydata[j][i]=day
      }
    }
  },

}
</script>

<style scoped>

</style>