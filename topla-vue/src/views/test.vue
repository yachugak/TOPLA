<template>
  <div class="secondary">
    <v-btn @click="push()">푸싱~</v-btn>
    <v-btn @click="noti()">권한 받기</v-btn>
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
    },

    noti(){
      Notification.requestPermission()
          .then((permission) => {
            console.log('permission ', permission)
            if (permission !== 'granted') {
              alert('리마인더 기능을 사용하기 위해서는 알림을 허용해야 합니다.')
            }
          })
    }
  }
}
</script>

<style scoped>

</style>