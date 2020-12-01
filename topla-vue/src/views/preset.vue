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
      <v-spacer></v-spacer>
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
              color="error"
              @click="showPresetList = false"
          >
            취소
          </v-btn>

          <v-btn
              color="primary"
              @click="addPreset()"
          >
            추가
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
    presetList,
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

      for(let i in list.data) {
        this.preset.push(list.data[i].presetName)
      }
    },

    async addPreset() {

      await this.$axios.post("/preset", {
        "schedulePreset": [0, 0, 0, 0, 0, 0, 0]
      })

      await this.$refs["dialog"].getPresetList();
    },

    change() {
      this.showPresetList = false
      window.location.reload()
    },

    modifyPreset() {
      this.modify = false
      this.save=true
    },

    async savePreset() {
      this.modify = true
      this.save=false

      let num=this.selectedUid
      this.preset[num]=this.selected

      await this.$axios.put(`/preset/${this.presetUid}`,{
        "schedulePreset":this.dayData,
        "presetName":this.selected
      })
    },

    async presetSelect(selected){
      let i=0
      for (i=0;i<this.preset.length;i++){
        if(this.preset[i]===selected)
          break;
      }
      this.selectedUid=i

      this.dayData=this.presetList[i].schedulePreset
      this.presetUid=this.presetList[i].presetUid

      await this.$axios.put(`/preset/select?presetUid=${this.presetUid}`,{
        presetUid:this.presetUid
      })
    },

    async presetAdd(){
      let len=this.presetList.length
      await this.$axios.post(`/preset`,{
        "schedulePreset":[0,0,0,0,0,0,0],
        "presetName":`추가된 프리셋 ${len}`
      })
      await this.presetSetting()
      this.selectedUid=len
      this.selected=this.presetList[len].presetName
      this.dayData=this.presetList[len].schedulePreset

    },

    async presetDelete(){
      this.preset.splice(this.selectedUid,1)

      await this.$axios.delete(`/preset/${this.presetUid}`)
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


