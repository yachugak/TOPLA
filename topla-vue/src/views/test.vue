<template>
  <div class="secondary">
    <v-btn @click="push()">푸싱~</v-btn>
    <div>
      {{myDeviceKey}}
    </div>
  </div>
</template>

<script>
export default {
  name: "test",

  data() {
    return {
      serverKey: "AAAASJTbBXo:APA91bGD4HAw7LNp4iCKcVarpJLqMDQ8zkhcYO0qzjMC3rUYS5og2cDl6Xu-wL5bMnCjF4y44NbGtkCsjOtZ-F5LrkXPMoowLy3rlt1G1cUlVVGynsUOSpX3Mh42AfazmyuR6T0iqbts",
      myDeviceKey: ""
    }
  },

  methods: {
    async push(){
      this.myDeviceKey = window.myDeviceKey;
      console.log(`server key = ${this.serverKey}`);
      console.log(`device key = ${this.myDeviceKey}`);
      let res = await this.$axios.post("https://fcm.googleapis.com/fcm/send", {
        to: this.myDeviceKey,
        data: {
          message: "HelloWorld!"
        }
      },
      {
        headers: {
          Authorization: `key=${this.serverKey}`
        }
      }
      );

      console.log(res.data);
    }
  }
}
</script>

<style scoped>

</style>