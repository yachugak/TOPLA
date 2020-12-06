<template>
  <v-container class="vh-100">
    <v-row>
      <v-col cols="12">
        <v-alert color="info" dismissible border="left">
          <div class="infoHeader">스케줄 프리셋이란?</div>
          <div class="infoBody">요거예 당신이 매일매일 일할 시간을 지정할 수 있어예</div>
        </v-alert>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="6">
        <v-select
            v-if="modify"
            v-model="selected"
            :items="preset"
            label="적용 중 프리셋"
            @change="presetSelect(selected)"
        ></v-select>
        <v-text-field
          v-if="save"
          v-model="selected"
          label="적용 중 프리셋"
          >
        </v-text-field>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
    <v-row>
      <schedule-preset
          :max="max"
          v-model="dayData"
          :preset-change="modify"
      ></schedule-preset>
    </v-row>
    <br>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn
          v-if="modify"
          @click="modifyPreset()">
        수정
      </v-btn>
      <v-btn
          v-if="modify"
          @click="presetAdd()">
        프리셋 추가
      </v-btn>

      <v-btn
          v-if="save"
          @click="savePreset()">
        저장
      </v-btn>
      <v-btn
          v-if="save"
          @click="presetDelete()">
        프리셋 삭제
      </v-btn>
    </v-card-actions>


  </v-container>

</template>

<script>

import schedulePreset from "@/components/schedulePreset";

export default {
  data() {
    return {
      dayData: [],
      presetUid: null,
      presetList:[],
      showPresetList: false,
      max: 1440,
      preset:[],
      selected:null,
      selectedUid:0,
      modify: true,
      save: false,
    }
  },

  async created() {
    await this.presetSetting()
  },

  components: {
    schedulePreset
  },

  methods: {
    async presetSetting() {
      let res = await this.$axios.get("/preset")
      this.dayData = res.data.schedulePreset;
      this.max = Math.max.apply(null, this.dayData) + 240
      this.presetUid = res.data.presetUid
      this.selected=res.data.presetName

      let list=await this.$axios.get("/preset/list")
      this.presetList=list.data

      this.preset=[]
      for(let i in list.data) {
        this.preset.push(list.data[i].presetName)
      }
    },

    modifyPreset() {
      this.modify = false
      this.save=true
    },

    async savePreset() {
      this.modify = true
      this.save=false

      let uid=this.selectedUid
      this.preset[uid]=this.selected

      await this.$axios.put(`/preset/${this.presetUid}`,{
        "schedulePreset":this.dayData,
        "presetName":this.selected
      })
    },

    async presetSelect(selected){
      let index=0
      for (index=0;index<this.preset.length;index++){
        if(this.preset[index]===selected)
          break;
      }

      this.selectedUid=index
      this.dayData=this.presetList[index].schedulePreset
      this.presetUid=this.presetList[index].presetUid

      await this.$axios.put(`/preset/select?presetUid=${this.presetUid}`,{
        presetUid:this.presetUid
      })
    },

    async presetAdd(){
      let len=this.preset.length
      await this.$axios.post(`/preset`,{
        "schedulePreset":[0,0,0,0,0,0,0],
        "presetName":`추가된 프리셋 ${len+1}`
      })

      await this.presetSetting()
      this.selectedUid=len
      this.presetUid=this.presetList[len].presetUid
      this.selected=this.presetList[len].presetName
      this.dayData=this.presetList[len].schedulePreset
    },

    async presetDelete(){
      try{
        await this.$axios.delete(`/preset/${this.presetUid}`)
      }
      catch(e) {
        this.$dialog.error({
          title: "삭제 실패",
          text: "기본 프리셋은 삭제가 불가능합니다."
        });
      }

      await this.presetSetting()
      this.selected=this.preset[0]
      this.selectedUid=0
      this.presetUid=this.presetList[0].presetUid
      this.dayData=this.presetList[0].schedulePreset

      this.modify = true
      this.save=false
    }
  }
}

</script>
<style>
.centered-input input {
  text-align: center;
}

.vh-100 {
  height: 93vh;
}

.infoHeader {
  font-size: 20px;
}

.infoBody {
  font-size: 15px;
}
</style>


