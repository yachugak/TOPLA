<template>
  <div class="flexBox">
    <v-slider
        v-for="i in 7"
        :key="i-1"
        v-model="value[i-1]"
        vertical
        :disabled=presetChange
        step="30"
        :max="max"
        :min="0"
        track-color="primary lighten-1"
        track-fill-color="primary darken-2"
        thumb-color="primary darken-2"
    >
      <template v-slot:append>
        <v-text-field
            class="centered-input"
            v-model="displayTime[i-1]"
            style="width:40px"
            step="30"
            :max="max"
            :min="0"
        ></v-text-field>
      </template>
      <template v-slot:prepend>
        {{labelList[i-1]}}
      </template>
    </v-slider>

  </div>
</template>

<script>
export default {
  name: "schedulePreset",

  data() {
    return {
      labelList: ["일", "월", "화", "수", "목", "금", "토"],
    }
  },

  props: {
    value: {
      type: Array,
      default: () => [0, 0, 0, 0, 0, 0, 0]
    },

    max: {
      type: Number,
      default: 1440
    },

    presetChange: {
      type: Boolean,
      default: true
    }

  },

  computed: {
    displayTime() {
      let time = []
      let timeHint = ''
      for (let i = 0; i < 7; i++) {
        let hour = this.value[i] / 60
        timeHint = `${hour}h`
        time.push(timeHint)
      }
      return time
    }
  }

}

</script>

<style scoped>
.flexBox {
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-around;
  align-items: stretch;
  width: 100%;
}
</style>