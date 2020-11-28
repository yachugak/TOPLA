<template>
  <v-container fluid>
    <v-row
        v-for="(preset,index) in presetList"
        :key="index"
    >
      <v-col cols="12">
        <schedule-preset
            v-model="preset.schedulePreset"
            :key="preset.presetUid"
        >
        </schedule-preset>
      </v-col>
      <v-col cols="12">
        <div class="flexBox">
          <v-btn color="error"
                @click="deletePreset(preset.presetUid)"
                 class="mr-2"
          >삭제</v-btn>
          <v-btn color="primary"
                 @click="changePresetId(preset.presetUid)"
          >적용</v-btn>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import schedulePreset from "@/components/schedulePreset";
export default {
  name: "presetList",

  components: {
    schedulePreset
  },

  data () {
    return {
      presetList:[],
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
      this.presetList = res.data;
      console.log(res.data)
    },

    async changePresetId(presetUid){
      await this.$axios.put(`/preset/select?presetUid=${presetUid}`,{
        presetUid:presetUid
      })
      this.$emit('input')
    },

    async deletePreset(presetUid){
      await this.$axios.delete(`/preset/${presetUid}`)
      await this.getPresetList();
    }
  },


}
</script>

<style scoped>
  .centered-input input{
    text-align: center;
  }

  .flexBox {
    display: flex;
    flex-flow: row nowrap;
    justify-content: flex-end;
  }
</style>