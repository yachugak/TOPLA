<template>
  <v-container>
    <v-row>
      <v-slider
          v-for="eachDay in daydata"
          :key="eachDay.day"
          v-model="eachDay.preset"
          :label="eachDay.dayInEnglish"
          vertical
          step="0.5"
          :max="eachDay.max"
          :min="min">
          <template v-slot:append>
            <v-text-field
                class="centered-input"
                v-model="eachDay.preset"
                style="width: 30px"
                :max="eachDay.max"
            ></v-text-field>
          </template>
      </v-slider>
    </v-row>
    <br>
    <v-card-actions>
      <v-spacer></v-spacer>
    <v-btn @click="check()">
      확인
    </v-btn>
    <v-btn @click="showPresetList = true">
      프리셋 교환
    </v-btn>
    </v-card-actions>

    <v-dialog
        v-model="showPresetList"
        persistent
        max-width="500"
    >
      <v-card v-if="showPresetList">
        <v-card-title>프리셋 리스트</v-card-title>
        <preset-list @input="change()"></preset-list>
        <v-card-actions>
          <v-spacer></v-spacer>

          <v-btn
              color="primary"
              @click="addPreset()"
          >
            추가
          </v-btn>
          <v-btn
              color="error"
              @click="showPresetList = false"
          >
            취소
          </v-btn>

        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>

</template>

<script>
import presetList from "@/components/presetList";

export default {
  data () {
    return {
      day:['일', '월', '화', '수', '목', '금', '토'],
      daydata:[],
      min:0,
      showPresetList:false,
    }
  },

  async created(){
    let res = await this.$axios.get("/preset")

    console.log(res.data)
      for (let i =0;i<7;i++){
        let day=new Object()

        day.day=i
        day.preset=res.data.schedulePreset[i]/60
        day.max=10
        day.dayInEnglish=this.day[i]
        this.daydata.push(day)
      }
  },

  components: {
    presetList,
  },

  methods:{
    async addPreset(){

      await this.$axios.post("/preset/create",{
          "schedulePreset":[0,0,0,0,0,0,0]
      })

      console.log( await this.$axios.get("/preset/list"))

    },

    change(){
      this.showPresetList = false
      window.location.reload()
    },

    check(){
      console.log(this.daydata)
    }
  }
}

</script>
<style>
  .centered-input input{
    text-align: center;
  }
</style>


