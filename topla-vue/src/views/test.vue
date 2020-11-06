<template>
  <div class="secondary">
    <v-btn @click="push()">푸싱~</v-btn>
  </div>
</template>

<script>
export default {
  name: "test",

  data() {
    return {
    }
  },

  methods: {
    push(){
      let myWorker = new Worker('/service-worker.js');
      myWorker.postMessage("ssss");
    }
  }
}
</script>

<style scoped>

</style>