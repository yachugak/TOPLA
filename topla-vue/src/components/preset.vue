<template>
  <v-container>
    <v-row>
      <v-slider
          v-for="eachDay in daydata"
          :key="eachDay.day"
          v-model="eachDay.preset"
          :label="eachDay.dayInEnglish"
          vertical
          thumb-label="always"
          :max="max"
          :min="min"
      ></v-slider>

    </v-row>
    <br>
    <v-btn v-on:click="check">
      저장
    </v-btn>
  </v-container>

</template>

<script>
export default {
  data () {
    return {
      day:["Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"],
      daydata:[],
      max:24,
      min:0,
    }
  },

  created(){
      for (let i =0;i<7;i++){
        let day=new Object()
        day.day=i
        day.preset=0
        day.dayInEnglish=this.day[i]
        this.daydata.push(day)
      }
      console.log(this.daydata)
  },
  methods:{
    check:function (){
      let hello=""
      for (let i =0;i<7;i++){
        hello=hello+this.daydata[i].dayInEnglish +" "+ this.daydata[i].preset+"\n"
      }
      console.log(this.daydata)
      alert(hello)
    }

  }
}

</script>

