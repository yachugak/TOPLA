import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import './registerServiceWorker'
import vuetify from './plugins/vuetify';
import firebase from "firebase/app";
import "firebase/messaging";

Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')

// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyAvIewptVslnP63znkaPxQN6zKxAiadd7U",
  authDomain: "topla-5f186.firebaseapp.com",
  databaseURL: "https://topla-5f186.firebaseio.com",
  projectId: "topla-5f186",
  storageBucket: "topla-5f186.appspot.com",
  messagingSenderId: "311735027066",
  appId: "1:311735027066:web:2fb86c71d2238471a782c3",
  measurementId: "G-8MPLZFWPF4"
};

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

messaging.usePublicVapidKey("BE9PdOllh2n_xql6Mj7naem6s4D9VwWuYabNrP2AAvW3kbKyypnef47BkPbnDvrF2UWxJdAZf4zBLGuoX0sygWA");

Notification.requestPermission()
    .then((permission) => {
      console.log('permission ', permission)
      if (permission !== 'granted') {
        alert('리마인더 기능을 사용하기 위해서는 알림을 허용해야 합니다.')
      }
    })

// TODO: Send token to server for send notification
messaging.getToken()
    .then(function(key){
        console.log(`device key = ${key}`);
        window.myDeviceKey = key;
    })

// // Handle received push notification at foreground
messaging.onMessage(payload => {
    let notificationOptions = {
        body: payload.data.body,
    };
    new Notification(payload.data.title, notificationOptions);
    alert(payload.data.body);
})