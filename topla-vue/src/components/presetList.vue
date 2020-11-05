<template>
  <v-container>
    <v-row
        v-for="(eachweek,index) in daydata"
        :key="index"
    >
      <v-row>
        <v-slider
            v-for="(eachDay,slIndex) in eachweek"
            :key="slIndex"
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

        <div>
          <v-btn
              color="primary"
              @click="modifyPreset(index)"
          >
            저장
          </v-btn>

          <v-btn
              color="primary"
              @click="changePresetId(index)"
          >
            적용
          </v-btn>
          <v-btn
              color="primary"
              @click="deletePreset(index)"
          >
            삭제
          </v-btn>
        </div>
      </v-row>
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
      preset:null,
      min:0,
      changePreset:-1,

    }
  },

  created() {
    this.getPresetList()
  },

  methods:{

    async getPresetList(){
      let res = await this.$axios.get("/preset/list")
      this.preset=res.data
      console.log(res.data)
      this.daydata=new Array(res.data.length)
      for (let j=0;j<this.daydata.length;j++) {
        this.daydata[j]=new Array(7)
        for (let i = 0; i < 7; i++) {
          let day = new Object()

          day.day = i
          day.preset = res.data[j].schedulePreset[i] / 60
          day.max = 10
          day.dayInEnglish = this.day[i]
          this.daydata[j][i]=day
        }
      }
    },

    async changePresetId(index){
      console.log(index)
      await this.$axios.put(`/preset/select?presetUid=${index}`,{
        presetUid:index
      })

      this.$emit('input')
    },

    async modifyPreset(index){

      let preset=[]

      for( let i =0;i<7;i++){
        preset.push(60*this.daydata[index][i].preset)
      }
      await this.$axios.put(`/preset/${index}`,{
        "schedulePreset":preset
      })
    },
    async deletePreset(index){
      await this.$axios.delete(`/preset/${index}`,{
      })
      window.location.reload()
    }
  },


}
</script>

<style scoped>
  .centered-input input{
    text-align: center;
  }
</style>