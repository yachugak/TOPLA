<template>
  <v-container class="secondary vh-100">
    <v-row>
      <schedule-preset
          :max="1440"
          v-model="dayData"
      ></schedule-preset>
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
        <preset-list @input="change()" ref="dialog"></preset-list>
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
import schedulePreset from "@/components/schedulePreset";

export default {
  data () {
    return {
      dayData:[],
      showPresetList:false,
    }
  },

  async created(){
    let res = await this.$axios.get("/preset")

    this.dayData = res.data.schedulePreset;
  },

  components: {
    presetList,
    schedulePreset
  },

  methods:{
    async addPreset(){

      await this.$axios.post("/preset/create",{
          "schedulePreset":[0,0,0,0,0,0,0]
      })

      await this.$refs["dialog"].getPresetList();
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

  .vh-100 {
    height: 93vh;
  }
</style>


