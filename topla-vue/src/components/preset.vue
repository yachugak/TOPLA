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
    <br>
    <v-btn v-on:click="check">
      저장
    </v-btn>
    <v-btn @click="showPresetList = true">
      프리셋 교환
    </v-btn>

    <v-dialog
        v-model="showPresetList"
        persistent
        max-width="500"
    >
      <v-card v-if="showPresetList">
        <v-card-title>프리셋 리스트</v-card-title>
        <preset-list></preset-list>
        <v-card-actions>
          <v-spacer></v-spacer>
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
        day.max=7
        day.dayInEnglish=this.day[i]
        this.daydata.push(day)
      }
  },

  components: {
    presetList,
  },

  methods:{
    async check(){
      let res=[]

      for (let i=0;i<7;i++){
        res.push(60*this.daydata[i].preset)
      }
      await this.$axios.put("/preset",{res})
      console.log(res)
    }
  }
}

</script>

