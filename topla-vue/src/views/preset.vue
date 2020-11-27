<template>
  <v-container class="secondary vh-100">
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
          v-if="save"
          @click="savePreset()">
        저장
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
  data() {
    return {
      dayData: [],
      presetUid: null,
      showPresetList: false,
      max: 1440,
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
    },

    async addPreset() {

      await this.$axios.post("/preset/create", {
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

      await this.$axios.put(`/preset/${this.presetUid}`,{
        "schedulePreset":this.dayData
      })
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
</style>


